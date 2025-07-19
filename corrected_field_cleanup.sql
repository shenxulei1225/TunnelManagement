-- 根据实际字段名修正的清理脚本
USE tunnel_management;

-- 第一步：数据迁移（将旧字段数据复制到新字段）
-- 1.1 迁移 system_field 表数据
UPDATE system_field SET field_code = field_key WHERE field_code IS NULL OR field_code = '';
UPDATE system_field SET field_name = field_label WHERE field_name IS NULL OR field_name = '';
UPDATE system_field SET field_type = value_type WHERE field_type IS NULL OR field_type = '';
UPDATE system_field SET display = config WHERE display IS NULL OR display = '';
UPDATE system_field SET description = remark WHERE description IS NULL OR description = '';
UPDATE system_field SET is_custom = status WHERE is_custom IS NULL;
UPDATE system_field SET enumJson = enum_json WHERE enumJson IS NULL OR enumJson = '';
UPDATE system_field SET calcExpr = calc_expr WHERE calcExpr IS NULL OR calcExpr = '';

-- 1.2 迁移 system_category 表数据
UPDATE system_category SET parentId = parent_id WHERE parentId IS NULL;
UPDATE system_category SET treePath = tree_path WHERE treePath IS NULL OR treePath = '';

-- 第二步：删除旧字段（根据实际字段名）
-- 2.1 删除 system_field 表中的旧字段
ALTER TABLE system_field 
DROP COLUMN field_key,
DROP COLUMN field_label, 
DROP COLUMN value_type,
DROP COLUMN config,
DROP COLUMN remark,
DROP COLUMN status,
DROP COLUMN enum_json,
DROP COLUMN calc_expr;

-- 2.2 删除 system_category 表中的多余字段
ALTER TABLE system_category 
DROP COLUMN status,
DROP COLUMN icon;

-- 2.3 重命名 system_category 表中的字段
ALTER TABLE system_category 
CHANGE parent_id parentId BIGINT NOT NULL DEFAULT '0' COMMENT '父节点 ID，0 表示根',
CHANGE tree_path treePath VARCHAR(500) DEFAULT NULL COMMENT '树路径，如 "1/15/37"';

-- 第三步：验证结果
SELECT '=== 清理完成后的表结构 ===' AS info;

SELECT 'system_field 表结构:' AS info;
DESCRIBE system_field;

SELECT 'system_category 表结构:' AS info;
DESCRIBE system_category;

SELECT 'system_directory 表结构:' AS info;
DESCRIBE system_directory;

-- 第四步：验证数据完整性
SELECT '=== 数据完整性检查 ===' AS info;

SELECT 'system_field 数据统计:' AS info;
SELECT COUNT(*) as total_records, 
       COUNT(field_code) as field_code_count,
       COUNT(field_name) as field_name_count,
       COUNT(field_type) as field_type_count,
       COUNT(display) as display_count,
       COUNT(description) as description_count,
       COUNT(is_custom) as is_custom_count
FROM system_field;

SELECT 'system_category 数据统计:' AS info;
SELECT COUNT(*) as total_records,
       COUNT(parentId) as parentId_count,
       COUNT(treePath) as treePath_count
FROM system_category;

SELECT '=== 清理完成 ===' AS info; 