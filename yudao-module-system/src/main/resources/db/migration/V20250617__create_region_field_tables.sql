-- 区域自定义字段定义表
CREATE TABLE IF NOT EXISTS system_region_field_def (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    region_type INT DEFAULT 0 COMMENT '适用区域类型，0=全部',
    field_key   VARCHAR(64)  NOT NULL COMMENT '字段标识',
    field_label VARCHAR(64)  NOT NULL COMMENT '显示名称',
    value_type  VARCHAR(32)  NOT NULL COMMENT '数据类型',
    required    TINYINT(1) DEFAULT 0 COMMENT '是否必填',
    enum_json   TEXT NULL COMMENT '枚举 JSON',
    sort        INT DEFAULT 0 COMMENT '显示排序',
    creator     VARCHAR(64),
    create_time DATETIME,
    updater     VARCHAR(64),
    update_time DATETIME,
    deleted     TINYINT(1) DEFAULT 0,
    INDEX idx_field_key (field_key)
) COMMENT='区域自定义字段定义';

-- 区域自定义字段值表
CREATE TABLE IF NOT EXISTS system_region_field_value (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    region_id   BIGINT       NOT NULL COMMENT '区域 ID',
    field_key   VARCHAR(64)  NOT NULL COMMENT '字段标识',
    value_text  TEXT COMMENT '字段值',
    creator     VARCHAR(64),
    create_time DATETIME,
    updater     VARCHAR(64),
    update_time DATETIME,
    deleted     TINYINT(1) DEFAULT 0,
    UNIQUE KEY uk_region_field (region_id, field_key)
) COMMENT='区域自定义字段值';
