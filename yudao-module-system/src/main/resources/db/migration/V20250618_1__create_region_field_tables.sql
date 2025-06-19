-- 创建区域自定义字段定义表（若不存在）
CREATE TABLE IF NOT EXISTS system_field_def (
  id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  owner_type  VARCHAR(32)  NOT NULL COMMENT '归属类型 REGION/DEVICE/ALARM',
  region_type INT          NOT NULL DEFAULT 0 COMMENT '区域类型 0=通用',
  field_key   VARCHAR(64)  NOT NULL COMMENT '字段英文 Key',
  field_label VARCHAR(64)  NOT NULL COMMENT '字段名称',
  value_type  VARCHAR(16)  NOT NULL COMMENT '类型 string/number/date/enum',
  required    TINYINT      NOT NULL DEFAULT 0 COMMENT '是否必填 0=否 1=是',
  enum_json   TEXT                  COMMENT '枚举 JSON',
  sort        INT          NOT NULL DEFAULT 0 COMMENT '排序',
  creator     VARCHAR(64)           DEFAULT NULL,
  create_time DATETIME              DEFAULT CURRENT_TIMESTAMP,
  updater     VARCHAR(64)           DEFAULT NULL,
  update_time DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted     TINYINT      NOT NULL DEFAULT 0,
  tenant_id   BIGINT       NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (id),
  KEY idx_owner_region (owner_type, region_type)
) COMMENT='系统-自定义字段定义';

-- 创建区域自定义字段值表（若不存在）
CREATE TABLE IF NOT EXISTS system_field_value (
  id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  field_id    BIGINT       NOT NULL COMMENT '字段定义 ID',
  owner_type  VARCHAR(32)  NOT NULL COMMENT '归属类型 REGION/DEVICE/ALARM',
  owner_id    BIGINT       NOT NULL COMMENT '归属对象 ID',
  value       VARCHAR(255) NOT NULL COMMENT '字段值，统一字符串存储',
  creator     VARCHAR(64)           DEFAULT NULL,
  create_time DATETIME              DEFAULT CURRENT_TIMESTAMP,
  updater     VARCHAR(64)           DEFAULT NULL,
  update_time DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted     TINYINT      NOT NULL DEFAULT 0,
  tenant_id   BIGINT       NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (id),
  UNIQUE KEY uk_owner_field (field_id, owner_type, owner_id),
  KEY idx_owner (owner_type, owner_id)
) COMMENT='系统-自定义字段值';
