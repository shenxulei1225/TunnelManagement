-- 检查数据库中实际存在的表结构
USE tunnel_management;

-- 1. 检查 field 相关表
SHOW TABLES LIKE '%field%';

-- 2. 检查 category 相关表  
SHOW TABLES LIKE '%category%';

-- 3. 检查 directory 相关表
SHOW TABLES LIKE '%directory%';

-- 4. 检查 system_field 表结构（如果存在）
SELECT 'system_field table structure:' AS info;
DESCRIBE system_field;

-- 5. 检查 system_category 表结构（如果存在）
SELECT 'system_category table structure:' AS info;
DESCRIBE system_category;

-- 6. 检查 system_directory 表结构（如果存在）
SELECT 'system_directory table structure:' AS info;
DESCRIBE system_directory;

-- 7. 检查 system_field_def 表结构（如果存在）
SELECT 'system_field_def table structure:' AS info;
DESCRIBE system_field_def;

-- 8. 检查 system_field_category 表结构（如果存在）
SELECT 'system_field_category table structure:' AS info;
DESCRIBE system_field_category;

-- 9. 检查 system_field_value 表结构（如果存在）
SELECT 'system_field_value table structure:' AS info;
DESCRIBE system_field_value; 