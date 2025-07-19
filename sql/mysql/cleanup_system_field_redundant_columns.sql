-- 清理 system_field 表中的多余字段
-- 删除与DO类不匹配的字段

USE tunnel_management;

-- 1. 备份当前数据（可选）
-- CREATE TABLE system_field_backup AS SELECT * FROM system_field;

-- 2. 先删除依赖于要删除字段的索引
ALTER TABLE system_field 
DROP INDEX uk_field_key_tenant_deleted;

-- 3. 删除多余的字段
ALTER TABLE system_field 
DROP COLUMN field_key,
DROP COLUMN field_label, 
DROP COLUMN value_type,
DROP COLUMN config;

-- 4. 验证结果
SELECT '=== 清理完成后的表结构 ===' AS info;
DESCRIBE system_field;

-- 5. 验证数据完整性
SELECT '=== 数据完整性检查 ===' AS info;
SELECT COUNT(*) as total_records, 
       COUNT(field_code) as field_code_count,
       COUNT(field_name) as field_name_count,
       COUNT(field_type) as field_type_count,
       COUNT(display) as display_count,
       COUNT(description) as description_count,
       COUNT(is_custom) as is_custom_count
FROM system_field;

SELECT '=== 清理完成 ===' AS info; 