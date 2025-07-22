-- 清理字段层级关联表中的重复数据
-- 错误信息：Duplicate entry '50-8-1' for key 'system_field_hierarchy_rel.uk_field_hierarchy'
-- 
-- ⚠️ 执行前请务必备份数据！
-- 

-- =============================================================================
-- 第一步：备份原始数据
-- =============================================================================

-- 创建备份表
CREATE TABLE system_field_hierarchy_rel_backup_$(date +%Y%m%d_%H%M%S) AS 
SELECT * FROM system_field_hierarchy_rel;

-- =============================================================================
-- 第二步：分析重复数据情况
-- =============================================================================

-- 查看重复数据摘要
SELECT 
    '清理前重复组合数' as stage,
    COUNT(*) as duplicate_combinations
FROM (
    SELECT field_id, hierarchy_group_id, tenant_id
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
) duplicates;

-- =============================================================================
-- 第三步：清理策略选择
-- =============================================================================

-- 策略A：保留最新的记录（推荐）
-- 删除旧的重复记录，保留 create_time 最新的

-- 策略B：保留最早的记录
-- 删除新的重复记录，保留 create_time 最早的

-- 策略C：保留 ID 最小的记录
-- 删除其他重复记录，保留 id 最小的

-- =============================================================================
-- 第四步：执行清理（策略A - 保留最新记录）
-- =============================================================================

-- 4.1 标记要删除的重复记录
-- 找出每组重复记录中除了最新记录之外的所有记录
UPDATE system_field_hierarchy_rel 
SET deleted = 1, 
    update_time = NOW(),
    updater = 'SYSTEM_CLEANUP'
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
    WHERE rn > 1  -- 保留最新的（rn=1），删除其他的
);

-- 4.2 验证清理结果
SELECT 
    '清理后重复组合数' as stage,
    COUNT(*) as duplicate_combinations
FROM (
    SELECT field_id, hierarchy_group_id, tenant_id
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
) duplicates;

-- =============================================================================
-- 第五步：特殊处理（如果策略A不适用）
-- =============================================================================

-- 如果需要使用策略B（保留最早记录），请注释掉第四步，执行以下代码：
/*
UPDATE system_field_hierarchy_rel 
SET deleted = 1, 
    update_time = NOW(),
    updater = 'SYSTEM_CLEANUP'
WHERE id IN (
    SELECT old_id FROM (
        SELECT 
            id as old_id,
            ROW_NUMBER() OVER (
                PARTITION BY field_id, hierarchy_group_id, tenant_id 
                ORDER BY create_time ASC, id ASC
            ) as rn
        FROM system_field_hierarchy_rel 
        WHERE deleted = 0
    ) ranked 
    WHERE rn > 1  -- 保留最早的（rn=1），删除其他的
);
*/

-- 如果需要使用策略C（保留最小ID），请注释掉第四步，执行以下代码：
/*
UPDATE system_field_hierarchy_rel 
SET deleted = 1, 
    update_time = NOW(),
    updater = 'SYSTEM_CLEANUP'
WHERE id IN (
    SELECT old_id FROM (
        SELECT 
            id as old_id,
            ROW_NUMBER() OVER (
                PARTITION BY field_id, hierarchy_group_id, tenant_id 
                ORDER BY id ASC
            ) as rn
        FROM system_field_hierarchy_rel 
        WHERE deleted = 0
    ) ranked 
    WHERE rn > 1  -- 保留最小ID（rn=1），删除其他的
);
*/

-- =============================================================================
-- 第六步：验证和统计
-- =============================================================================

-- 6.1 验证没有重复数据了
SELECT 
    field_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as count
FROM system_field_hierarchy_rel 
WHERE deleted = 0
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;

-- 6.2 统计清理结果
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
WHERE deleted = 1

UNION ALL

SELECT 
    'Total Records' as record_type,
    COUNT(*) as count
FROM system_field_hierarchy_rel;

-- 6.3 查看被清理的记录详情
SELECT 
    rel.id,
    rel.field_id,
    f.field_label,
    rel.hierarchy_group_id,
    hg.name as group_name,
    rel.tenant_id,
    rel.create_time,
    rel.update_time,
    rel.updater
FROM system_field_hierarchy_rel rel
LEFT JOIN system_field f ON rel.field_id = f.id
LEFT JOIN system_hierarchy_group hg ON rel.hierarchy_group_id = hg.id
WHERE rel.deleted = 1 
    AND rel.updater = 'SYSTEM_CLEANUP'
ORDER BY rel.field_id, rel.hierarchy_group_id, rel.create_time;

-- =============================================================================
-- 第七步：针对特定错误的处理
-- =============================================================================

-- 专门处理错误中提到的 field_id=50, hierarchy_group_id=8, tenant_id=1
SELECT 
    'Before cleanup - field 50 in group 8' as description,
    id,
    field_id,
    hierarchy_group_id,
    tenant_id,
    create_time,
    deleted
FROM system_field_hierarchy_rel 
WHERE field_id = 50 
    AND hierarchy_group_id = 8 
    AND tenant_id = 1
ORDER BY create_time;

-- 如果上面的通用清理没有解决这个特定问题，手动处理：
-- （通常不需要，因为通用清理应该已经处理了）
/*
-- 保留最新的记录，删除其他
UPDATE system_field_hierarchy_rel 
SET deleted = 1, 
    update_time = NOW(),
    updater = 'MANUAL_CLEANUP'
WHERE field_id = 50 
    AND hierarchy_group_id = 8 
    AND tenant_id = 1
    AND id != (
        SELECT max_id FROM (
            SELECT MAX(id) as max_id
            FROM system_field_hierarchy_rel 
            WHERE field_id = 50 
                AND hierarchy_group_id = 8 
                AND tenant_id = 1
                AND deleted = 0
        ) latest
    );
*/

-- =============================================================================
-- 第八步：预防未来重复数据
-- =============================================================================

-- 8.1 检查唯一约束是否正确设置
SHOW INDEX FROM system_field_hierarchy_rel WHERE Key_name = 'uk_field_hierarchy';

-- 8.2 如果约束不存在或定义不正确，重新创建
-- 注意：这可能会失败如果还有重复数据，请先执行上面的清理
/*
-- 删除旧约束（如果存在）
ALTER TABLE system_field_hierarchy_rel DROP INDEX uk_field_hierarchy;

-- 创建新的唯一约束
ALTER TABLE system_field_hierarchy_rel 
ADD CONSTRAINT uk_field_hierarchy 
UNIQUE (field_id, hierarchy_group_id, tenant_id);
*/

-- =============================================================================
-- 第九步：最终验证
-- =============================================================================

-- 9.1 确认没有重复数据
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '✅ 没有重复数据'
        ELSE CONCAT('❌ 仍有 ', COUNT(*), ' 组重复数据')
    END as final_status
FROM (
    SELECT field_id, hierarchy_group_id, tenant_id
    FROM system_field_hierarchy_rel 
    WHERE deleted = 0
    GROUP BY field_id, hierarchy_group_id, tenant_id
    HAVING COUNT(*) > 1
) remaining_duplicates;

-- 9.2 测试插入重复数据（应该失败）
-- 取消注释下面的语句来测试约束是否工作
/*
INSERT INTO system_field_hierarchy_rel 
(field_id, hierarchy_group_id, tenant_id, sort, create_time, update_time, creator, updater) 
VALUES 
(50, 8, 1, 0, NOW(), NOW(), 'TEST', 'TEST');
-- 这应该失败并显示约束违反错误
*/

-- =============================================================================
-- 使用说明
-- =============================================================================
/*
1. 在执行清理前，请确保数据库已备份
2. 建议在测试环境先执行一遍，确认结果符合预期
3. 默认策略是保留最新的记录，如需其他策略请修改第四步
4. 执行后请检查应用功能是否正常
5. 如果发现问题，可以从备份表恢复数据

恢复数据的方法：
TRUNCATE TABLE system_field_hierarchy_rel;
INSERT INTO system_field_hierarchy_rel 
SELECT * FROM system_field_hierarchy_rel_backup_YYYYMMDD_HHMMSS;
*/ 