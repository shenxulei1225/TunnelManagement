-- 新增字段分类树表
CREATE TABLE IF NOT EXISTS system_field_category (
  id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  parent_id     BIGINT       NOT NULL DEFAULT 0 COMMENT '父 ID',
  code          VARCHAR(32)  NOT NULL COMMENT '节点编码，同一父下唯一',
  name          VARCHAR(64)  NOT NULL COMMENT '名称',
  readonly      TINYINT      NOT NULL DEFAULT 0 COMMENT '系统只读',
  tree_path     VARCHAR(256) NOT NULL COMMENT '完整路径 1/2/3',
  level         TINYINT      NOT NULL DEFAULT 1 COMMENT '层级深度',
  sort          INT          NOT NULL DEFAULT 0 COMMENT '排序',
  creator       VARCHAR(64)           DEFAULT NULL,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  updater       VARCHAR(64)           DEFAULT NULL,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted       TINYINT      NOT NULL DEFAULT 0,
  tenant_id     BIGINT       NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (id),
) COMMENT='系统-自定义字段分类';

-- system_field_def 表新增 category_id，删除 region_type
ALTER TABLE system_field_def
    ADD COLUMN category_id BIGINT NOT NULL DEFAULT 0 COMMENT '分类 ID' AFTER owner_type,
    ADD UNIQUE KEY uk_category_field (category_id, field_key),
    ADD KEY idx_category (category_id);



