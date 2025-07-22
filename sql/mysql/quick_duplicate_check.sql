-- 快速检查重复数据脚本
-- 用于快速定位 DuplicateKeyException 的来源

-- 1. 检查具体的错误记录
-- 错误：Duplicate entry '50-8-1' for key 'system_field_hierarchy_rel.uk_field_hierarchy'
SELECT 
    '错误相关的重复记录' as type,
    id,
    field_id,
    hierarchy_group_id,
    tenant_id,
    sort,
    create_time,
    deleted,
    creator
FROM system_field_hierarchy_rel 
WHERE field_id = 50 
    AND hierarchy_group_id = 8 
    AND tenant_id = 1
ORDER BY create_time;

-- 2. 快速查看所有重复数据概览
SELECT 
    field_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as duplicate_count,
    MIN(create_time) as first_created,
    MAX(create_time) as last_created,
    GROUP_CONCAT(id ORDER BY create_time) as all_ids
FROM system_field_hierarchy_rel 
WHERE deleted = 0
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1
ORDER BY duplicate_count DESC
LIMIT 10;

-- 3. 检查约束状态
SHOW INDEX FROM system_field_hierarchy_rel WHERE Key_name = 'uk_field_hierarchy';

-- 4. 统计重复数据影响
SELECT 
    COUNT(*) as total_duplicate_combinations,
    SUM(dup_count - 1) as excess_records
FROM (
    SELECT 
        field_id, hierarchy_group_id, tenant_id,
        COUNT(*) as dup_count
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
) duplicates;

-- 5. 查看字段信息
SELECT 
    f.id,
    f.field_label,
    f.field_key,
    COUNT(rel.id) as total_relations
FROM system_field f
LEFT JOIN system_field_hierarchy_rel rel ON f.id = rel.field_id AND rel.deleted = 0
WHERE f.id IN (
    SELECT DISTINCT field_id 
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
)
GROUP BY f.id, f.field_label, f.field_key
ORDER BY total_relations DESC; 