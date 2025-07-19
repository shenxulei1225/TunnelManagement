-- 检查当前表结构，确认字段名称
USE tunnel_management;

-- 检查 system_field 表的完整结构
DESCRIBE system_field;

-- 检查 system_category 表的完整结构  
DESCRIBE system_category;

-- 检查 system_directory 表的完整结构
DESCRIBE system_directory;

-- 查看 system_field 表的前几条数据
SELECT 'system_field sample data:' AS info;
SELECT * FROM system_field LIMIT 3;

-- 查看 system_category 表的前几条数据
SELECT 'system_category sample data:' AS info;
SELECT * FROM system_category LIMIT 3; 