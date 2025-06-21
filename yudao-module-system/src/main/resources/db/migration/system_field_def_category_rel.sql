-- 字段定义-分类关联表，包含租户列
CREATE TABLE IF NOT EXISTS system_field_def_category_rel (
  id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  field_def_id BIGINT       NOT NULL COMMENT '字段定义 ID',
  category_id  BIGINT       NOT NULL COMMENT '分类 ID',
  required     TINYINT      NOT NULL DEFAULT 0 COMMENT '是否必填 0=否 1=是',
  sort     INT         NOT NULL DEFAULT 0 COMMENT '排序',
tenant_id    BIGINT       NOT NULL DEFAULT 0 COMMENT '租户编号',
  creator      VARCHAR(64)           DEFAULT NULL,
  create_time  DATETIME              DEFAULT CURRENT_TIMESTAMP,
  updater      VARCHAR(64)           DEFAULT NULL,
  update_time  DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted      TINYINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_def (field_def_id),
  KEY idx_cat (category_id)
) COMMENT='系统-字段定义与分类关联表';
