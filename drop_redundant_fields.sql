-- 删除多余字段脚本
USE tunnel_management;

-- 1. 删除 system_field 表中的旧字段
-- 删除旧字段，保留新字段
ALTER TABLE system_field 
DROP COLUMN field_key,
DROP COLUMN field_label, 
DROP COLUMN value_type,
DROP COLUMN config,
DROP COLUMN remark,


-- 2. 删除 system_category 表中的多余字段
-- 删除 DO 对象中没有对应的字段
ALTER TABLE system_category 
DROP COLUMN status,
DROP COLUMN icon;

-- 3. 重命名 system_category 表中的字段以匹配 DO 对象
ALTER TABLE system_category 
CHANGE parent_id parentId BIGINT NOT NULL DEFAULT '0' COMMENT '父节点 ID，0 表示根',
CHANGE tree_path treePath VARCHAR(500) DEFAULT NULL COMMENT '树路径，如 "1/15/37"';

-- 4. 验证删除结果
SELECT 'system_field structure after cleanup:' AS info;
DESCRIBE system_field;

SELECT 'system_category structure after cleanup:' AS info;
DESCRIBE system_category;

-- 5. 检查数据完整性
SELECT 'system_field data count:' AS info;
SELECT COUNT(*) as total_records FROM system_field;

SELECT 'system_category data count:' AS info;
SELECT COUNT(*) as total_records FROM system_category; 