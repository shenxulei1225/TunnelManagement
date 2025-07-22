-- 检查字段层级关联表中的重复数据
-- 错误信息：Duplicate entry '50-8-1' for key 'system_field_hierarchy_rel.uk_field_hierarchy'

-- 1. 检查表结构和约束
SHOW CREATE TABLE system_field_hierarchy_rel;

-- 2. 查看唯一约束 uk_field_hierarchy 的定义
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    ORDINAL_POSITION
FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'system_field_hierarchy_rel' 
    AND CONSTRAINT_NAME = 'uk_field_hierarchy'
ORDER BY ORDINAL_POSITION;

-- 3. 查找具体的重复数据
-- 根据错误信息，field_id=50, hierarchy_group_id=8, tenant_id=1 的组合重复了
SELECT 
    id,
    field_id,
    hierarchy_group_id,
    tenant_id,
    sort,
    create_time,
    update_time,
    creator,
    deleted
FROM system_field_hierarchy_rel 
WHERE field_id = 50 
    AND hierarchy_group_id = 8 
    AND tenant_id = 1
ORDER BY create_time;

-- 4. 查找所有重复的组合
SELECT 
    field_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(id ORDER BY create_time) as duplicate_ids,
    GROUP_CONCAT(create_time ORDER BY create_time) as create_times
FROM system_field_hierarchy_rel 
WHERE deleted = 0  -- 只查看未删除的记录
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1
ORDER BY duplicate_count DESC, field_id, hierarchy_group_id;

-- 5. 查看字段信息（了解是什么字段）
SELECT 
    f.id as field_id,
    f.field_label,
    f.field_key,
    COUNT(rel.id) as relation_count
FROM system_field f
LEFT JOIN system_field_hierarchy_rel rel ON f.id = rel.field_id AND rel.deleted = 0
WHERE f.id IN (
    SELECT field_id 
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
)
GROUP BY f.id, f.field_label, f.field_key
ORDER BY relation_count DESC;

-- 6. 查看分级组信息
SELECT 
    hg.id as hierarchy_group_id,
    hg.name as group_name,
    hg.code as group_code,
    COUNT(rel.id) as relation_count
FROM system_hierarchy_group hg
LEFT JOIN system_field_hierarchy_rel rel ON hg.id = rel.hierarchy_group_id AND rel.deleted = 0
WHERE hg.id IN (
    SELECT hierarchy_group_id 
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
)
GROUP BY hg.id, hg.name, hg.code
ORDER BY relation_count DESC;

-- 7. 详细查看重复记录的完整信息
SELECT 
    rel.id,
    rel.field_id,
    f.field_label,
    rel.hierarchy_group_id,
    hg.name as group_name,
    rel.tenant_id,
    rel.sort,
    rel.create_time,
    rel.update_time,
    rel.creator,
    rel.deleted
FROM system_field_hierarchy_rel rel
LEFT JOIN system_field f ON rel.field_id = f.id
LEFT JOIN system_hierarchy_group hg ON rel.hierarchy_group_id = hg.id
WHERE (rel.field_id, rel.hierarchy_group_id, rel.tenant_id) IN (
    SELECT field_id, hierarchy_group_id, tenant_id
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
)
ORDER BY rel.field_id, rel.hierarchy_group_id, rel.tenant_id, rel.create_time;

-- 8. 统计重复数据的影响范围
SELECT 
    '总重复组合数' as metric,
    COUNT(*) as value
FROM (
    SELECT field_id, hierarchy_group_id, tenant_id
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
) duplicates

UNION ALL

SELECT 
    '总重复记录数' as metric,
    COUNT(*) as value
FROM system_field_hierarchy_rel rel
WHERE (rel.field_id, rel.hierarchy_group_id, rel.tenant_id) IN (
    SELECT field_id, hierarchy_group_id, tenant_id
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
)
AND rel.deleted = 0

UNION ALL

SELECT 
    '涉及字段数' as metric,
    COUNT(DISTINCT field_id) as value
FROM system_field_hierarchy_rel rel
WHERE (rel.field_id, rel.hierarchy_group_id, rel.tenant_id) IN (
    SELECT field_id, hierarchy_group_id, tenant_id
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
)
AND rel.deleted = 0

UNION ALL

SELECT 
    '涉及分组数' as metric,
    COUNT(DISTINCT hierarchy_group_id) as value
FROM system_field_hierarchy_rel rel
WHERE (rel.field_id, rel.hierarchy_group_id, rel.tenant_id) IN (
    SELECT field_id, hierarchy_group_id, tenant_id
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
)
AND rel.deleted = 0; 