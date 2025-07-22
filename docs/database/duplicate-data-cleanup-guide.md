# 数据库重复数据清理指南

## 🔍 问题描述

遇到 `DuplicateKeyException` 错误：
```
Duplicate entry '50-8-1' for key 'system_field_hierarchy_rel.uk_field_hierarchy'
```

这表示在 `system_field_hierarchy_rel` 表中存在重复的字段-分组关联记录。

## 📋 诊断步骤

### 第一步：快速检查
执行快速诊断脚本：
```bash
mysql -u [username] -p [database_name] < sql/mysql/quick_duplicate_check.sql
```

### 第二步：详细分析
执行完整的重复数据检查：
```bash
mysql -u [username] -p [database_name] < sql/mysql/check_duplicate_field_hierarchy_relations.sql
```

## 🛠️ 清理方案

### ⚠️ 重要提醒
**在执行任何清理操作前，请务必备份数据库！**

```bash
# 备份整个数据库
mysqldump -u [username] -p [database_name] > backup_$(date +%Y%m%d_%H%M%S).sql

# 或者只备份相关表
mysqldump -u [username] -p [database_name] system_field_hierarchy_rel > field_hierarchy_backup_$(date +%Y%m%d_%H%M%S).sql
```

### 方案一：自动清理（推荐）
执行自动清理脚本，默认保留最新的记录：
```bash
mysql -u [username] -p [database_name] < sql/mysql/clean_duplicate_field_hierarchy_relations.sql
```

### 方案二：手动清理
如果需要手动控制清理过程：

1. **查看重复数据**：
```sql
SELECT 
    field_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(id ORDER BY create_time) as all_ids
FROM system_field_hierarchy_rel 
WHERE deleted = 0
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;
```

2. **选择保留策略**：
   - **保留最新记录**（推荐）：`ORDER BY create_time DESC, id DESC`
   - **保留最早记录**：`ORDER BY create_time ASC, id ASC`
   - **保留最小ID**：`ORDER BY id ASC`

3. **执行清理**：
```sql
-- 标记重复记录为删除（保留最新）
UPDATE system_field_hierarchy_rel 
SET deleted = 1, 
    update_time = NOW(),
    updater = 'MANUAL_CLEANUP'
WHERE id IN (
    SELECT old_id FROM (
        SELECT 
            id as old_id,
            ROW_NUMBER() OVER (
                PARTITION BY field_id, hierarchy_group_id, tenant_id 
                ORDER BY create_time DESC, id DESC
            ) as rn
        FROM system_field_hierarchy_rel 
        WHERE deleted = 0
    ) ranked 
    WHERE rn > 1
);
```

## ✅ 验证结果

### 1. 检查是否还有重复数据
```sql
SELECT 
    field_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as count
FROM system_field_hierarchy_rel 
WHERE deleted = 0
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;
```

**结果应该为空**，表示没有重复数据。

### 2. 验证约束工作正常
```sql
-- 这个插入应该失败
INSERT INTO system_field_hierarchy_rel 
(field_id, hierarchy_group_id, tenant_id, sort, create_time, update_time, creator, updater) 
VALUES 
(50, 8, 1, 0, NOW(), NOW(), 'TEST', 'TEST');
```

### 3. 检查清理统计
```sql
SELECT 
    'Active Records' as record_type,
    COUNT(*) as count
FROM system_field_hierarchy_rel 
WHERE deleted = 0

UNION ALL

SELECT 
    'Deleted Records' as record_type,
    COUNT(*) as count
FROM system_field_hierarchy_rel 
WHERE deleted = 1;
```

## 🔧 修复约束（如果需要）

如果唯一约束缺失或定义不正确：

```sql
-- 检查现有约束
SHOW INDEX FROM system_field_hierarchy_rel WHERE Key_name = 'uk_field_hierarchy';

-- 如果约束不存在，创建它
ALTER TABLE system_field_hierarchy_rel 
ADD CONSTRAINT uk_field_hierarchy 
UNIQUE (field_id, hierarchy_group_id, tenant_id);
```

## 🚨 问题排查

### 情况1：清理后仍有重复数据
**可能原因**：
- SQL查询语法问题
- 事务未提交
- 并发操作干扰

**解决方案**：
1. 检查SQL执行结果
2. 手动提交事务：`COMMIT;`
3. 重新执行清理脚本

### 情况2：约束创建失败
**错误信息**：
```
Duplicate entry 'xxx' for key 'uk_field_hierarchy'
```

**解决方案**：
1. 确保先清理重复数据
2. 再创建约束

### 情况3：应用功能异常
**可能原因**：
- 删除了错误的记录
- 关联关系丢失

**解决方案**：
1. 检查被删除的记录：
```sql
SELECT * FROM system_field_hierarchy_rel 
WHERE deleted = 1 
    AND updater LIKE '%CLEANUP%'
ORDER BY update_time DESC;
```

2. 如果需要恢复，从备份恢复数据

## 🔄 恢复数据

如果清理结果不符合预期，可以从备份恢复：

```bash
# 恢复整个数据库
mysql -u [username] -p [database_name] < backup_YYYYMMDD_HHMMSS.sql

# 或者只恢复相关表
mysql -u [username] -p -e "TRUNCATE TABLE system_field_hierarchy_rel;" [database_name]
mysql -u [username] -p [database_name] < field_hierarchy_backup_YYYYMMDD_HHMMSS.sql
```

## 📝 预防措施

### 1. 应用层改进
- 在创建关联前检查是否已存在
- 使用事务确保原子性
- 添加重复检查逻辑

### 2. 数据库约束
- 确保唯一约束正确设置
- 定期检查数据一致性

### 3. 监控告警
- 设置重复数据检测任务
- 监控约束违反错误

## 📞 技术支持

如果遇到问题，请提供：
1. 错误信息完整日志
2. 重复数据检查结果
3. 清理操作执行记录
4. 当前数据库状态

## 🎯 常用命令汇总

```bash
# 快速诊断
mysql -u root -p database_name < sql/mysql/quick_duplicate_check.sql

# 完整检查
mysql -u root -p database_name < sql/mysql/check_duplicate_field_hierarchy_relations.sql

# 自动清理
mysql -u root -p database_name < sql/mysql/clean_duplicate_field_hierarchy_relations.sql

# 备份数据
mysqldump -u root -p database_name system_field_hierarchy_rel > backup.sql
``` 