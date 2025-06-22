-- 添加计算字段表达式列
ALTER TABLE system_field_def
    ADD COLUMN calc_expr varchar(255) NULL COMMENT '计算公式表达式';
