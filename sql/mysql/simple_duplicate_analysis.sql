-- 简单分析重复数据的判断标准

-- 第一步：查看表结构
SELECT '=== 表结构分析 ===' as title;
DESCRIBE system_field_hierarchy_rel;

-- 第二步：查看唯一约束定义
SELECT '=== 唯一约束定义 ===' as title;
SELECT 
    CONSTRAINT_NAME as '约束名',
    COLUMN_NAME as '约束字段',
    ORDINAL_POSITION as '字段顺序'
FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'system_field_hierarchy_rel' 
    AND CONSTRAINT_NAME = 'uk_field_hierarchy'
ORDER BY ORDINAL_POSITION;

-- 第三步：查看具体的重复数据示例
SELECT '=== 重复数据示例 ===' as title;
SELECT 
    field_id as '字段ID',
    hierarchy_group_id as '分组ID', 
    tenant_id as '租户ID',
    CONCAT(field_id, '-', hierarchy_group_id, '-', tenant_id) as '唯一标识组合',
    COUNT(*) as '重复次数',
    GROUP_CONCAT(id ORDER BY create_time) as '记录ID列表',
    GROUP_CONCAT(deleted ORDER BY create_time) as '删除状态列表',
    MIN(create_time) as '最早时间',
    MAX(create_time) as '最晚时间'
FROM system_field_hierarchy_rel 
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1
ORDER BY COUNT(*) DESC
LIMIT 10;

-- 第四步：查看错误相关的具体记录
SELECT '=== 错误记录详情 (50-8-1) ===' as title;
SELECT 
    id,
    field_id,
    hierarchy_group_id,
    tenant_id,
    sort,
    create_time,
    update_time,
    creator,
    deleted,
    CASE 
        WHEN deleted = 0 THEN '有效记录'
        WHEN deleted = 1 THEN '已删除记录'
        ELSE '未知状态'
    END as '记录状态'
FROM system_field_hierarchy_rel 
WHERE field_id = 50 
    AND hierarchy_group_id = 8 
    AND tenant_id = 1
ORDER BY create_time;

-- 第五步：理解重复判断逻辑
SELECT '=== 重复判断规则说明 ===' as title;
SELECT 
    '字段ID (field_id)' as '维度1',
    '分组ID (hierarchy_group_id)' as '维度2', 
    '租户ID (tenant_id)' as '维度3',
    '这三个字段组合相同时即为重复' as '重复判断标准',
    '同一字段在同一租户下只能关联到同一分组一次' as '业务含义';

-- 第六步：检查deleted字段是否影响唯一性
SELECT '=== deleted字段对约束的影响 ===' as title;
SELECT 
    deleted,
    COUNT(*) as '记录数',
    CASE 
        WHEN deleted = 0 THEN '有效记录 - 参与唯一约束检查'
        WHEN deleted = 1 THEN '逻辑删除记录 - 可能仍参与约束检查'
    END as '说明'
FROM system_field_hierarchy_rel 
GROUP BY deleted; 