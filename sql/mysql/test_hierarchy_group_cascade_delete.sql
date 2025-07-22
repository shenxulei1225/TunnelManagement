-- 测试层级组删除时的级联删除功能
-- 验证删除分组后，字段与该分组的关联关系也被删除

-- 1. 查看删除前的状态
SELECT '=== 删除前的分组列表 ===' as info;
SELECT id, name, code FROM system_hierarchy_group WHERE usage_type = 'FIELD' ORDER BY id;

SELECT '=== 删除前的字段层级关系 ===' as info;
SELECT id, field_id, hierarchy_group_id 
FROM system_field_hierarchy_rel 
ORDER BY hierarchy_group_id, field_id;

-- 2. 统计每个分组关联的字段数量
SELECT '=== 每个分组的字段数量 ===' as info;
SELECT 
    hg.id as group_id,
    hg.name as group_name,
    COUNT(fhr.field_id) as field_count
FROM system_hierarchy_group hg
LEFT JOIN system_field_hierarchy_rel fhr ON hg.id = fhr.hierarchy_group_id
WHERE hg.usage_type = 'FIELD'
GROUP BY hg.id, hg.name
ORDER BY hg.id;

-- 3. 如果要测试删除功能，请取消注释以下部分：
-- 注意：这会实际删除数据，请谨慎操作！

/*
-- 选择一个要删除的测试分组（请根据实际情况修改ID）
SET @test_group_id = (
    SELECT id 
    FROM system_hierarchy_group 
    WHERE usage_type = 'FIELD' 
    AND name LIKE '%测试%' 
    LIMIT 1
);

-- 显示要删除的分组信息
SELECT CONCAT('准备删除分组ID: ', @test_group_id) as info;

-- 查看删除前该分组的字段关联
SELECT 
    CONCAT('删除前分组 ', @test_group_id, ' 关联的字段:') as info;
SELECT field_id 
FROM system_field_hierarchy_rel 
WHERE hierarchy_group_id = @test_group_id;

-- 执行删除（这会触发级联删除）
-- DELETE FROM system_hierarchy_group WHERE id = @test_group_id;

-- 验证删除后的状态
SELECT '=== 删除后验证 ===' as info;
SELECT 
    CASE 
        WHEN COUNT(*) = 0 
        THEN '✅ 字段关联关系已正确删除' 
        ELSE CONCAT('❌ 还有 ', COUNT(*), ' 条关联关系未删除') 
    END as result
FROM system_field_hierarchy_rel 
WHERE hierarchy_group_id = @test_group_id;
*/

-- 4. 手动测试指导
SELECT '=== 手动测试指导 ===' as info;
SELECT '请按以下步骤测试：
1. 选择一个有字段关联的分组
2. 记录该分组ID和关联的字段ID
3. 在前端或API中删除该分组
4. 检查 system_field_hierarchy_rel 表中是否还有该分组的记录
5. 如果没有记录，说明级联删除正常工作' as instructions; 