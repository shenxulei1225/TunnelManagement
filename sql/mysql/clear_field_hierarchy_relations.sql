-- 清空字段层级关系表数据
-- 目的：清除可能存在的错误数据和孤儿记录，确保数据一致性

-- 1. 显示清空前的数据统计
SELECT '=== 清空前数据统计 ===' as info;

SELECT 
    '字段层级关系表记录数' as table_name,
    COUNT(*) as record_count
FROM system_field_hierarchy_rel
UNION ALL
SELECT 
    '字段表记录数' as table_name,
    COUNT(*) as record_count
FROM system_field
UNION ALL
SELECT 
    '层级组表记录数' as table_name,
    COUNT(*) as record_count
FROM system_hierarchy_group
WHERE usage_type = 'FIELD';

-- 2. 显示孤儿记录统计
SELECT '=== 孤儿记录统计 ===' as info;

-- 检查引用不存在字段的关系记录
SELECT 
    '引用不存在字段的关系记录' as orphan_type,
    COUNT(*) as orphan_count
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_field f ON fhr.field_id = f.id
WHERE f.id IS NULL

UNION ALL

-- 检查引用不存在分组的关系记录
SELECT 
    '引用不存在分组的关系记录' as orphan_type,
    COUNT(*) as orphan_count
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE hg.id IS NULL;

-- 3. 显示详细的孤儿记录
SELECT '=== 详细孤儿记录 ===' as info;

-- 显示引用不存在字段的记录
SELECT 'orphan_field_relations' as type, fhr.*
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_field f ON fhr.field_id = f.id
WHERE f.id IS NULL
LIMIT 10;

-- 显示引用不存在分组的记录
SELECT 'orphan_group_relations' as type, fhr.*
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE hg.id IS NULL
LIMIT 10;

-- 4. 执行清空操作
-- 警告：以下操作会删除所有字段层级关系数据！
-- 请确认后取消注释执行

/*
-- 清空字段层级关系表
TRUNCATE TABLE system_field_hierarchy_rel;

-- 验证清空结果
SELECT 
    CASE 
        WHEN COUNT(*) = 0 
        THEN '✅ 字段层级关系表已成功清空' 
        ELSE CONCAT('❌ 清空失败，还有 ', COUNT(*), ' 条记录') 
    END as result
FROM system_field_hierarchy_rel;
*/

-- 5. 如果只想删除孤儿记录，取消注释以下部分：

/*
-- 删除引用不存在字段的关系记录
DELETE fhr 
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_field f ON fhr.field_id = f.id
WHERE f.id IS NULL;

-- 删除引用不存在分组的关系记录
DELETE fhr 
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE hg.id IS NULL;

-- 验证孤儿记录清理结果
SELECT 
    CASE 
        WHEN COUNT(*) = 0 
        THEN '✅ 孤儿记录已清理完成' 
        ELSE CONCAT('❌ 还有 ', COUNT(*), ' 条孤儿记录') 
    END as result
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_field f ON fhr.field_id = f.id
LEFT JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE f.id IS NULL OR hg.id IS NULL;
*/

-- 6. 操作指导
SELECT '=== 操作指导 ===' as info;
SELECT '
请根据需要选择清理方式：

选项1：完全清空字段层级关系表
- 取消注释第4部分的 TRUNCATE 语句
- 适用于重新开始配置字段分组关系

选项2：只删除孤儿记录
- 取消注释第5部分的 DELETE 语句
- 适用于保留有效关系，只清理错误数据

选项3：手动处理
- 根据上面显示的孤儿记录信息手动处理
- 适用于需要精确控制的情况

执行前请务必备份数据！
' as instructions; 