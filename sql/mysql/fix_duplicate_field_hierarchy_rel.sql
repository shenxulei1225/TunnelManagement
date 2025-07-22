-- 修复 system_field_def_hierarchy_rel 表的重复数据问题
-- 解决唯一约束 uk_field_hierarchy 冲突

-- 1. 备份原始数据
CREATE TABLE IF NOT EXISTS system_field_def_hierarchy_rel_backup AS 
SELECT * FROM system_field_def_hierarchy_rel;

-- 2. 查找重复数据
SELECT 
    field_def_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(id ORDER BY id) as all_ids,
    MIN(id) as keep_id,
    GROUP_CONCAT(id ORDER BY id DESC LIMIT 999 OFFSET 1) as delete_ids
FROM system_field_def_hierarchy_rel 
GROUP BY field_def_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;

-- 3. 删除重复数据（保留最早的记录）
DELETE r1 FROM system_field_def_hierarchy_rel r1
JOIN system_field_def_hierarchy_rel r2 
WHERE r1.field_def_id = r2.field_def_id 
  AND r1.hierarchy_group_id = r2.hierarchy_group_id
  AND r1.tenant_id = r2.tenant_id
  AND r1.id > r2.id;

-- 4. 验证清理结果
SELECT 
    '清理后重复数据检查' as check_type,
    field_def_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as count
FROM system_field_def_hierarchy_rel 
GROUP BY field_def_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;

-- 5. 检查唯一约束是否正常
SELECT 
    '数据统计' as info,
    COUNT(*) as total_records,
    COUNT(DISTINCT CONCAT(field_def_id, '-', hierarchy_group_id, '-', tenant_id)) as unique_combinations
FROM system_field_def_hierarchy_rel;

-- 6. 如果需要，可以重新创建唯一约束（仅在约束不存在时）
-- ALTER TABLE system_field_def_hierarchy_rel 
-- ADD CONSTRAINT uk_field_hierarchy UNIQUE (field_def_id, hierarchy_group_id, tenant_id);

-- 7. 显示修复后的数据概览
SELECT 
    field_def_id,
    hierarchy_group_id,
    tenant_id,
    id,
    create_time,
    update_time
FROM system_field_def_hierarchy_rel 
ORDER BY field_def_id, hierarchy_group_id, tenant_id; 