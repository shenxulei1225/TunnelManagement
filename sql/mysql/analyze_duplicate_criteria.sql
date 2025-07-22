-- 分析重复数据的判断标准
-- 查看 system_field_hierarchy_rel 表的结构和约束定义

-- 1. 查看表结构
DESCRIBE system_field_hierarchy_rel;

-- 2. 查看表的完整创建语句（包含所有约束）
SHOW CREATE TABLE system_field_hierarchy_rel;

-- 3. 查看所有索引和约束
SHOW INDEX FROM system_field_hierarchy_rel;

-- 4. 查看具体的唯一约束 uk_field_hierarchy 包含哪些字段
SELECT 
    CONSTRAINT_NAME as '约束名称',
    TABLE_NAME as '表名',
    COLUMN_NAME as '字段名',
    ORDINAL_POSITION as '字段顺序'
FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'system_field_hierarchy_rel' 
    AND CONSTRAINT_NAME = 'uk_field_hierarchy'
ORDER BY ORDINAL_POSITION;

-- 5. 查看所有唯一约束的定义
SELECT 
    tc.CONSTRAINT_NAME as '约束名称',
    tc.CONSTRAINT_TYPE as '约束类型',
    GROUP_CONCAT(kcu.COLUMN_NAME ORDER BY kcu.ORDINAL_POSITION) as '包含字段'
FROM information_schema.TABLE_CONSTRAINTS tc
JOIN information_schema.KEY_COLUMN_USAGE kcu 
    ON tc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME 
    AND tc.TABLE_SCHEMA = kcu.TABLE_SCHEMA
WHERE tc.TABLE_SCHEMA = DATABASE()
    AND tc.TABLE_NAME = 'system_field_hierarchy_rel'
    AND tc.CONSTRAINT_TYPE IN ('UNIQUE', 'PRIMARY KEY')
GROUP BY tc.CONSTRAINT_NAME, tc.CONSTRAINT_TYPE;

-- 6. 根据约束定义分析什么情况下算重复
-- 假设约束是 (field_id, hierarchy_group_id, tenant_id)
SELECT 
    '重复判断标准' as description,
    'field_id + hierarchy_group_id + tenant_id 组合相同时算重复' as criteria;

-- 7. 查看实际数据示例 - 显示所有记录的关键字段组合
SELECT 
    id,
    field_id,
    hierarchy_group_id,
    tenant_id,
    CONCAT(field_id, '-', hierarchy_group_id, '-', tenant_id) as '组合标识',
    sort,
    create_time,
    deleted,
    creator
FROM system_field_hierarchy_rel 
ORDER BY field_id, hierarchy_group_id, tenant_id, create_time
LIMIT 20;

-- 8. 查看错误中提到的具体记录
-- 错误: Duplicate entry '50-8-1' for key 'uk_field_hierarchy'
-- 这意味着 field_id=50, hierarchy_group_id=8, tenant_id=1 的组合重复了
SELECT 
    '错误示例分析' as type,
    id,
    field_id,
    hierarchy_group_id,
    tenant_id,
    CONCAT(field_id, '-', hierarchy_group_id, '-', tenant_id) as '重复标识',
    sort,
    create_time,
    deleted,
    creator
FROM system_field_hierarchy_rel 
WHERE field_id = 50 
    AND hierarchy_group_id = 8 
    AND tenant_id = 1
ORDER BY create_time;

-- 9. 统计各种组合的出现次数
SELECT 
    field_id,
    hierarchy_group_id,
    tenant_id,
    CONCAT(field_id, '-', hierarchy_group_id, '-', tenant_id) as '组合标识',
    COUNT(*) as '出现次数',
    COUNT(CASE WHEN deleted = 0 THEN 1 END) as '有效记录数',
    COUNT(CASE WHEN deleted = 1 THEN 1 END) as '已删除记录数',
    MIN(create_time) as '最早创建时间',
    MAX(create_time) as '最晚创建时间',
    GROUP_CONCAT(id ORDER BY create_time) as '所有记录ID'
FROM system_field_hierarchy_rel 
GROUP BY field_id, hierarchy_group_id, tenant_id
ORDER BY COUNT(*) DESC, field_id, hierarchy_group_id;

-- 10. 查看 deleted 字段的作用
SELECT 
    deleted as '删除状态',
    COUNT(*) as '记录数量',
    CASE 
        WHEN deleted = 0 THEN '有效记录（参与唯一约束检查）'
        WHEN deleted = 1 THEN '已删除记录（可能不参与唯一约束检查）'
        ELSE '其他状态'
    END as '状态说明'
FROM system_field_hierarchy_rel 
GROUP BY deleted;

-- 11. 分析是否 deleted 字段影响唯一约束
-- 检查唯一约束是否包含 deleted 字段
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN 'deleted 字段包含在唯一约束中'
        ELSE 'deleted 字段不在唯一约束中'
    END as '约束分析'
FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'system_field_hierarchy_rel' 
    AND CONSTRAINT_NAME = 'uk_field_hierarchy'
    AND COLUMN_NAME = 'deleted';

-- 12. 总结重复数据的判断逻辑
SELECT 
    '重复数据判断总结' as summary,
    '相同的 field_id, hierarchy_group_id, tenant_id 组合' as what_is_duplicate,
    '一个字段只能在同一个租户下关联到同一个分级组一次' as business_rule,
    'deleted=1 的记录可能不参与唯一性检查（取决于约束定义）' as deleted_impact; 