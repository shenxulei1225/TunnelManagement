-- 快速清空字段层级关系表
-- 警告：此操作会删除所有字段与分组的关联关系！

-- 显示清空前的记录数
SELECT CONCAT('清空前记录数: ', COUNT(*)) as before_count FROM system_field_hierarchy_rel;

-- 清空表
TRUNCATE TABLE system_field_hierarchy_rel;

-- 验证清空结果
SELECT CONCAT('清空后记录数: ', COUNT(*)) as after_count FROM system_field_hierarchy_rel;

-- 显示结果
SELECT 
    CASE 
        WHEN COUNT(*) = 0 
        THEN '✅ 字段层级关系表已成功清空，可以重新配置字段分组关系' 
        ELSE '❌ 清空失败' 
    END as result
FROM system_field_hierarchy_rel; 