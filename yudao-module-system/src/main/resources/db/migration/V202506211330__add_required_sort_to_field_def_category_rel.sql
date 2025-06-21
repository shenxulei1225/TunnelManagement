-- 在字段定义-分类关联表中补充必填标记及排序号
ALTER TABLE system_field_def_category_rel
    ADD COLUMN required TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否必填',
    ADD COLUMN sort     INT         NOT NULL DEFAULT 0 COMMENT '排序';
