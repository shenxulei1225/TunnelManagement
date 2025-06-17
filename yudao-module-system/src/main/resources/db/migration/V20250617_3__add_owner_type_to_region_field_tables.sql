-- 为字段定义与值表增加归属类型 owner_type，并调整唯一索引，默认值 REGI0N

-- 字段定义表
ALTER TABLE system_region_field_def
  ADD COLUMN owner_type VARCHAR(32) NOT NULL DEFAULT 'REGION' COMMENT '字段归属类型' AFTER id,
  DROP INDEX idx_field_key,
  ADD UNIQUE KEY uk_owner_type_key (owner_type, field_key);

-- 字段值表
ALTER TABLE system_region_field_value
  ADD COLUMN owner_type VARCHAR(32) NOT NULL DEFAULT 'REGION' COMMENT '字段归属类型' AFTER id,
  DROP INDEX uk_owner_region_field,
  ADD UNIQUE KEY uk_owner_region_field (owner_type, region_id, field_key);
