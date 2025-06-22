-- 自定义字段分类表，新增 readonly 标记字段（0=可编辑，1=系统只读）
ALTER TABLE `system_field_category`
  ADD COLUMN `readonly` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '系统只读' AFTER `sort`;
