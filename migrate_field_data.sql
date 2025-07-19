-- 数据迁移脚本：将原有字段数据复制到新字段
USE tunnel_management;

-- 1. 迁移 system_field 表数据
-- 将 field_key 的数据复制到 field_code
UPDATE system_field SET field_code = field_key WHERE field_code IS NULL OR field_code = '';

-- 将 field_label 的数据复制到 field_name  
UPDATE system_field SET field_name = field_label WHERE field_name IS NULL OR field_name = '';

-- 将 value_type 的数据复制到 field_type
UPDATE system_field SET field_type = value_type WHERE field_type IS NULL OR field_type = '';

-- 将 config 的数据复制到 display
UPDATE system_field SET display = config WHERE display IS NULL OR display = '';

-- 将 remark 的数据复制到 description
UPDATE system_field SET description = remark WHERE description IS NULL OR description = '';

-- 将 status 的数据复制到 is_custom
UPDATE system_field SET is_custom = status WHERE is_custom IS NULL;

-- 将 enum_json 的数据复制到 enumJson（如果字段名不同）
UPDATE system_field SET enumJson = enum_json WHERE enumJson IS NULL OR enumJson = '';

-- 将 calc_expr 的数据复制到 calcExpr
UPDATE system_field SET calcExpr = calc_expr WHERE calcExpr IS NULL OR calcExpr = '';

-- 2. 迁移 system_category 表数据
-- 将 parent_id 的数据复制到 parentId（如果字段名不同）
UPDATE system_category SET parentId = parent_id WHERE parentId IS NULL;

-- 将 tree_path 的数据复制到 treePath
UPDATE system_category SET treePath = tree_path WHERE treePath IS NULL OR treePath = '';

-- 验证迁移结果
SELECT 'system_field migration verification:' AS info;
SELECT COUNT(*) as total_records, 
       COUNT(field_code) as field_code_count,
       COUNT(field_name) as field_name_count,
       COUNT(field_type) as field_type_count,
       COUNT(display) as display_count,
       COUNT(description) as description_count,
       COUNT(is_custom) as is_custom_count
FROM system_field;

SELECT 'system_category migration verification:' AS info;
SELECT COUNT(*) as total_records,
       COUNT(parentId) as parentId_count,
       COUNT(treePath) as treePath_count
FROM system_category; 