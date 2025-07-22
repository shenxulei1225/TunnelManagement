# 数据库重复数据判断标准详解

## 🎯 核心问题

根据错误信息：`Duplicate entry '50-8-1' for key 'system_field_hierarchy_rel.uk_field_hierarchy'`

这个错误告诉我们：
- **表名**：`system_field_hierarchy_rel` （字段层级关联表）
- **约束名**：`uk_field_hierarchy` （唯一约束）
- **重复值**：`50-8-1` （具体的重复数据组合）

## 📊 重复数据的判断标准

### 基础概念
重复数据的判断基于数据库的 **唯一约束 (UNIQUE CONSTRAINT)**：

```sql
-- 唯一约束的一般形式
CONSTRAINT uk_field_hierarchy UNIQUE (field_id, hierarchy_group_id, tenant_id)
```

### 重复判断规则

根据约束 `uk_field_hierarchy`，以下组合被认为是**重复**：

| 字段 | 说明 | 示例 |
|------|------|------|
| `field_id` | 字段ID | 50 |
| `hierarchy_group_id` | 分级组ID | 8 |
| `tenant_id` | 租户ID | 1 |

**重复条件**：当 `(field_id, hierarchy_group_id, tenant_id)` 这三个字段的组合完全相同时。

### 具体示例

#### ✅ 允许的情况（不重复）
```sql
-- 记录1: 字段50 关联到 分组8 在租户1下
(field_id=50, hierarchy_group_id=8, tenant_id=1)

-- 记录2: 字段50 关联到 分组9 在租户1下 （不同分组）
(field_id=50, hierarchy_group_id=9, tenant_id=1)

-- 记录3: 字段50 关联到 分组8 在租户2下 （不同租户）
(field_id=50, hierarchy_group_id=8, tenant_id=2)

-- 记录4: 字段51 关联到 分组8 在租户1下 （不同字段）
(field_id=51, hierarchy_group_id=8, tenant_id=1)
```

#### ❌ 不允许的情况（重复）
```sql
-- 记录1: 字段50 关联到 分组8 在租户1下
(field_id=50, hierarchy_group_id=8, tenant_id=1, id=1001, create_time='2024-01-01')

-- 记录2: 再次尝试让字段50 关联到 分组8 在租户1下
(field_id=50, hierarchy_group_id=8, tenant_id=1, id=1002, create_time='2024-01-02')
-- ❌ 这会触发 DuplicateKeyException
```

## 🔍 业务含义

### 为什么要有这个约束？

1. **业务逻辑一致性**：
   - 一个字段在同一个租户下只能关联到同一个分级组一次
   - 防止数据不一致和混乱

2. **数据完整性**：
   - 确保字段-分组关系的唯一性
   - 避免重复关联导致的查询异常

3. **系统稳定性**：
   - 防止因重复数据导致的业务逻辑错误
   - 保证界面显示的一致性

### 实际业务场景

```
字段：地基基础设计等级 (ID: 50)
分组：设备 (ID: 8)  
租户：默认租户 (ID: 1)

业务规则：
- ✅ "地基基础设计等级" 可以属于 "设备" 分组
- ✅ "地基基础设计等级" 也可以属于 "建筑" 分组
- ❌ "地基基础设计等级" 不能重复属于同一个 "设备" 分组
```

## 🔧 deleted 字段的影响

### 软删除机制

表中通常有 `deleted` 字段来实现软删除：
- `deleted = 0`：有效记录
- `deleted = 1`：已删除记录

### 约束检查范围

**关键问题**：`deleted` 字段是否包含在唯一约束中？

#### 情况1：约束不包含 deleted 字段
```sql
UNIQUE (field_id, hierarchy_group_id, tenant_id)
```
- **结果**：即使记录被标记为删除 (`deleted=1`)，仍然参与唯一性检查
- **影响**：删除的记录会阻止创建相同组合的新记录

#### 情况2：约束包含 deleted 字段
```sql
UNIQUE (field_id, hierarchy_group_id, tenant_id, deleted)
```
- **结果**：删除和未删除的记录被视为不同的组合
- **影响**：可以有多个相同组合但不同删除状态的记录

## 📋 诊断命令

### 查看约束定义
```sql
-- 执行此脚本查看具体的约束定义
mysql -u [用户名] -p [数据库名] < sql/mysql/simple_duplicate_analysis.sql
```

### 快速检查重复数据
```sql
-- 查看所有重复的组合
SELECT 
    field_id, hierarchy_group_id, tenant_id,
    COUNT(*) as 重复次数
FROM system_field_hierarchy_rel 
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;
```

### 查看具体错误记录
```sql
-- 查看错误中提到的具体记录 (50-8-1)
SELECT 
    id, field_id, hierarchy_group_id, tenant_id,
    create_time, deleted, creator
FROM system_field_hierarchy_rel 
WHERE field_id = 50 AND hierarchy_group_id = 8 AND tenant_id = 1
ORDER BY create_time;
```

## 🛠️ 解决策略

### 策略1：保留最新记录
```sql
-- 删除旧的重复记录，保留最新的
DELETE FROM system_field_hierarchy_rel 
WHERE (field_id, hierarchy_group_id, tenant_id, create_time) NOT IN (
    SELECT field_id, hierarchy_group_id, tenant_id, MAX(create_time)
    FROM system_field_hierarchy_rel 
    GROUP BY field_id, hierarchy_group_id, tenant_id
);
```

### 策略2：软删除处理
```sql
-- 将重复记录标记为删除
UPDATE system_field_hierarchy_rel 
SET deleted = 1, update_time = NOW()
WHERE id IN (选择要删除的ID列表);
```

### 策略3：业务逻辑调整
- 在应用层增加重复检查
- 修改唯一约束定义
- 调整软删除机制

## 📊 数据统计示例

假设查询结果显示：

| field_id | hierarchy_group_id | tenant_id | 重复次数 | 记录ID列表 |
|----------|--------------------|-----------|---------| ----------|
| 50 | 8 | 1 | 3 | 1001,1005,1008 |
| 23 | 5 | 1 | 2 | 2001,2003 |

**解读**：
- 字段50在分组8下有3条重复记录
- 字段23在分组5下有2条重复记录
- 需要每个组合只保留1条记录

## 💡 预防措施

1. **应用层检查**：
```java
// 创建关联前先检查是否已存在
if (existsRelation(fieldId, hierarchyGroupId, tenantId)) {
    return "字段已在此分组中";
}
```

2. **数据库约束优化**：
```sql
-- 如果需要考虑软删除，调整约束
ALTER TABLE system_field_hierarchy_rel 
DROP INDEX uk_field_hierarchy;

ALTER TABLE system_field_hierarchy_rel 
ADD CONSTRAINT uk_field_hierarchy 
UNIQUE (field_id, hierarchy_group_id, tenant_id, deleted);
```

3. **事务处理**：
```java
@Transactional
public void createRelation(Long fieldId, Long hierarchyGroupId) {
    // 原子性操作，避免并发问题
}
``` 