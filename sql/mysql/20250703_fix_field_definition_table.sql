-- 修复字段定义表结构
-- 添加缺失的字段（逐个添加，避免重复字段错误）

-- 添加字段长度
ALTER TABLE `dynamic_field_definition` 
ADD COLUMN `length` int DEFAULT NULL COMMENT '字段长度' AFTER `type`;

-- 添加字段精度
ALTER TABLE `dynamic_field_definition` 
ADD COLUMN `field_precision` int DEFAULT NULL COMMENT '字段精度' AFTER `length`;

-- 添加显示类型
ALTER TABLE `dynamic_field_definition` 
ADD COLUMN `display_type` varchar(50) DEFAULT NULL COMMENT '显示类型' AFTER `validation_rules`;

-- 添加备注
ALTER TABLE `dynamic_field_definition` 
ADD COLUMN `remark` varchar(500) DEFAULT NULL COMMENT '备注' AFTER `status`; 