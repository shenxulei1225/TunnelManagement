-- 优化字段层级移动操作 - 解决拖拽移动时的重复键冲突问题
-- 专门处理字段在分组间移动时的数据一致性

-- 1. 创建临时视图查看当前状态
CREATE OR REPLACE VIEW v_field_hierarchy_status AS
SELECT 
    field_def_id,
    COUNT(*) as rel_count,
    GROUP_CONCAT(hierarchy_group_id ORDER BY hierarchy_group_id) as group_ids,
    GROUP_CONCAT(id ORDER BY hierarchy_group_id) as rel_ids,
    tenant_id
FROM system_field_def_hierarchy_rel 
GROUP BY field_def_id, tenant_id;

-- 2. 查找有多个分组关联的字段（可能存在移动冲突）
SELECT 
    '多分组字段检查' as check_type,
    field_def_id,
    rel_count,
    group_ids,
    tenant_id
FROM v_field_hierarchy_status 
WHERE rel_count > 1
ORDER BY field_def_id;

-- 3. 检查可能的重复数据
SELECT 
    '重复关联检查' as check_type,
    field_def_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(id ORDER BY id) as all_ids
FROM system_field_def_hierarchy_rel 
GROUP BY field_def_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;

-- 4. 备份当前数据
CREATE TABLE IF NOT EXISTS system_field_def_hierarchy_rel_move_backup AS 
SELECT 
    *,
    NOW() as backup_time
FROM system_field_def_hierarchy_rel;

-- 5. 修复重复数据：为每个字段只保留一个分组关联（保留ID最小的）
-- 首先，删除重复记录
DELETE r1 FROM system_field_def_hierarchy_rel r1
JOIN system_field_def_hierarchy_rel r2 
WHERE r1.field_def_id = r2.field_def_id 
  AND r1.hierarchy_group_id = r2.hierarchy_group_id
  AND r1.tenant_id = r2.tenant_id
  AND r1.id > r2.id;

-- 6. 对于有多个分组的字段，只保留最新的关联（ID最大的分组）
DELETE r1 FROM system_field_def_hierarchy_rel r1
JOIN (
    SELECT 
        field_def_id,
        tenant_id,
        MAX(id) as max_id
    FROM system_field_def_hierarchy_rel
    GROUP BY field_def_id, tenant_id
    HAVING COUNT(*) > 1
) r2 ON r1.field_def_id = r2.field_def_id 
    AND r1.tenant_id = r2.tenant_id
    AND r1.id < r2.max_id;

-- 7. 验证修复结果
SELECT 
    '修复后状态检查' as check_type,
    COUNT(*) as total_records,
    COUNT(DISTINCT field_def_id) as unique_fields,
    COUNT(DISTINCT CONCAT(field_def_id, '-', tenant_id)) as field_tenant_combinations
FROM system_field_def_hierarchy_rel;

-- 8. 查看修复后每个字段的分组状态
SELECT 
    '字段分组状态' as info,
    field_def_id,
    hierarchy_group_id,
    tenant_id,
    id,
    create_time,
    update_time
FROM system_field_def_hierarchy_rel 
ORDER BY field_def_id, tenant_id;

-- 9. 创建监控视图：用于实时监控字段移动状态
CREATE OR REPLACE VIEW v_field_move_monitor AS
SELECT 
    r.field_def_id,
    r.hierarchy_group_id,
    r.tenant_id,
    r.id as rel_id,
    r.create_time,
    r.update_time,
    f.field_key,
    f.field_name,
    CASE 
        WHEN EXISTS (
            SELECT 1 FROM system_field_def_hierarchy_rel r2 
            WHERE r2.field_def_id = r.field_def_id 
              AND r2.tenant_id = r.tenant_id 
              AND r2.id != r.id
        ) THEN 'MULTIPLE_GROUPS'
        ELSE 'SINGLE_GROUP'
    END as status
FROM system_field_def_hierarchy_rel r
LEFT JOIN system_field_def f ON r.field_def_id = f.id
ORDER BY r.field_def_id, r.tenant_id;

-- 10. 显示监控结果
SELECT * FROM v_field_move_monitor WHERE status = 'MULTIPLE_GROUPS';

-- 11. 清理临时视图
DROP VIEW IF EXISTS v_field_hierarchy_status;

COMMIT;

-- 操作完成提示
SELECT 
    '字段移动优化完成' as message,
    NOW() as completion_time,
    '请重新部署应用以使用优化后的移动逻辑' as next_step; 