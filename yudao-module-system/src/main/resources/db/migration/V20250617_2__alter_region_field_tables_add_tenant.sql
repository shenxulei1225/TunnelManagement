-- 为自定义字段表补充租户字段，避免多租户插件查询报错
ALTER TABLE system_region_field_def ADD COLUMN tenant_id BIGINT DEFAULT 0 COMMENT '租户编号' AFTER deleted;
ALTER TABLE system_region_field_value ADD COLUMN tenant_id BIGINT DEFAULT 0 COMMENT '租户编号' AFTER deleted;
