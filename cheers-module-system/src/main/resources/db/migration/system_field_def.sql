-- 创建自定义字段定义表（若不存在）
CREATE TABLE IF NOT EXISTS system_field_def (
  id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  field_key    VARCHAR(64)  NOT NULL COMMENT '字段唯一标识',
  field_label  VARCHAR(64)  NOT NULL COMMENT '字段名称',
  value_type   VARCHAR(16)  NOT NULL COMMENT '数据类型 string/number/date/enum',
  unit         VARCHAR(16)           DEFAULT NULL COMMENT '单位（仅 number 类型使用）',
  enum_json    TEXT                  COMMENT '枚举值 JSON，value-label 数组，用于 enum 类型',
  sort         INT          NOT NULL DEFAULT 0 COMMENT '显示排序',
  creator      VARCHAR(64)           DEFAULT NULL,
  create_time  DATETIME              DEFAULT CURRENT_TIMESTAMP,
  updater      VARCHAR(64)           DEFAULT NULL,
  update_time  DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted      TINYINT      NOT NULL DEFAULT 0,
  tenant_id    BIGINT       NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (id),
  UNIQUE KEY uk_field_key (field_key)
) COMMENT='系统-自定义字段定义';
