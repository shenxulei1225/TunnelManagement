-- 添加 unit 字段到自定义字段定义表
ALTER TABLE system_field_def
  ADD COLUMN unit VARCHAR(16) COMMENT '单位（仅 number 类型使用）' AFTER value_type;
