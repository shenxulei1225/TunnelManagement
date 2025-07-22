# 数据库约束条件详解

## 🔍 什么是约束条件？

### 简单理解
约束条件就像是数据库的"规则"，防止插入重复或不合法的数据。

```sql
-- 错误信息分解：
Duplicate entry '50-8-1' for key 'system_field_hierarchy_rel.uk_field_hierarchy'

📋 表名: system_field_hierarchy_rel
🔑 约束名: uk_field_hierarchy  
❌ 重复值: 50-8-1
```

## 📊 约束条件的具体内容

### uk_field_hierarchy 约束包含的字段
通常是这样的组合：
```sql
UNIQUE KEY uk_field_hierarchy (field_id, hierarchy_group_id, tenant_id)
```

| 字段位置 | 字段名 | 含义 | 错误中的值 |
|---------|--------|------|-----------|
| 第1个 | field_id | 字段ID | 50 |
| 第2个 | hierarchy_group_id | 分组ID | 8 |
| 第3个 | tenant_id | 租户ID | 1 |

### 约束的意思
**这三个字段的组合必须是唯一的**，不能重复！

## 🎯 具体的重复情况

### 错误场景重现
```sql
-- 假设数据库中已经存在这条记录：
INSERT INTO system_field_hierarchy_rel 
(field_id, hierarchy_group_id, tenant_id, ...) 
VALUES (50, 8, 1, ...);  -- 第一次插入，成功 ✅

-- 然后又尝试插入相同组合：
INSERT INTO system_field_hierarchy_rel 
(field_id, hierarchy_group_id, tenant_id, ...) 
VALUES (50, 8, 1, ...);  -- 第二次插入，失败 ❌
-- 报错：Duplicate entry '50-8-1' for key 'uk_field_hierarchy'
```

### 重复的原因
数据库发现：
1. **字段ID = 50** ✓
2. **分组ID = 8** ✓  
3. **租户ID = 1** ✓

这个组合 `(50, 8, 1)` 已经存在了！

## 🔍 找出具体的重复记录

### 查看重复的具体记录
```sql
-- 查看字段50在分组8、租户1下的所有记录
SELECT 
    id, field_id, hierarchy_group_id, tenant_id,
    create_time, deleted, creator
FROM system_field_hierarchy_rel 
WHERE field_id = 50 
    AND hierarchy_group_id = 8 
    AND tenant_id = 1
ORDER BY create_time;
```

### 可能的查询结果示例
```
id  | field_id | hierarchy_group_id | tenant_id | create_time         | deleted | creator
----|----------|--------------------|-----------|--------------------|---------|--------
123 | 50       | 8                  | 1         | 2024-01-01 10:00  | 0       | admin
456 | 50       | 8                  | 1         | 2024-01-02 15:30  | 0       | user1
```

**这就是重复！** 同样的组合 `(50, 8, 1)` 出现了两次。

## 🚨 软删除的影响

### 关键问题
**deleted 字段是否包含在约束中？**

#### 情况1：约束不包含 deleted 字段
```sql
UNIQUE (field_id, hierarchy_group_id, tenant_id)  -- 没有 deleted
```
**结果**：即使记录被标记为删除 (`deleted=1`)，仍然算重复！

```sql
-- 已有记录（被删除）
id=123, field_id=50, hierarchy_group_id=8, tenant_id=1, deleted=1

-- 新记录（尝试插入）
field_id=50, hierarchy_group_id=8, tenant_id=1, deleted=0
-- ❌ 仍然会报重复错误！
```

#### 情况2：约束包含 deleted 字段
```sql
UNIQUE (field_id, hierarchy_group_id, tenant_id, deleted)  -- 包含 deleted
```
**结果**：删除和未删除的记录不算重复。

```sql
-- 已有记录（被删除）
id=123, field_id=50, hierarchy_group_id=8, tenant_id=1, deleted=1

-- 新记录（尝试插入）
field_id=50, hierarchy_group_id=8, tenant_id=1, deleted=0
-- ✅ 不会报错，因为 deleted 值不同
```

## 🛠️ 实际操作步骤

### 第一步：执行诊断脚本
```bash
mysql -u [用户名] -p [数据库名] < sql/mysql/explain_duplicate_constraint.sql
```

### 第二步：查看结果
重点关注以下几个部分：
1. **约束定义** - 确认包含哪些字段
2. **重复记录详情** - 找出具体冲突的记录
3. **deleted字段影响** - 确认软删除是否影响约束

### 第三步：解决重复
根据查询结果选择解决方案：

#### 方案A：删除旧记录
```sql
-- 删除较早的重复记录
DELETE FROM system_field_hierarchy_rel 
WHERE id = 123;  -- 删除第一条记录
```

#### 方案B：软删除旧记录
```sql
-- 将旧记录标记为删除
UPDATE system_field_hierarchy_rel 
SET deleted = 1, update_time = NOW()
WHERE id = 123;
```

#### 方案C：修改新记录
```sql
-- 将新记录分配到不同的分组
UPDATE new_record SET hierarchy_group_id = 9;  -- 改为分组9
```

## 📋 总结

1. **约束条件**：`uk_field_hierarchy` 确保 `(field_id, hierarchy_group_id, tenant_id)` 组合唯一
2. **重复原因**：尝试插入已存在的组合 `(50, 8, 1)`
3. **查找重复**：执行 SQL 查询找出具体的冲突记录
4. **解决方法**：删除旧记录、软删除、或修改组合

关键是先执行诊断脚本，看清楚具体的重复情况！ 