-- 添加 category_id 字段到 system_region 表，用于区域分类
ALTER TABLE system_region
  ADD COLUMN category_id BIGINT DEFAULT 0 COMMENT '分类ID' AFTER parent_id;
