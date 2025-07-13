-- 为dynamic_business_model表添加model_type字段
-- 执行时间：2025-07-03

-- 检查字段是否存在，如果不存在则添加
ALTER TABLE `dynamic_business_model` 
ADD COLUMN `model_type` tinyint NOT NULL DEFAULT '1' COMMENT '模型类型（0:系统，1:自定义）' 
AFTER `table_name`;

-- 为现有记录设置默认类型为自定义
UPDATE `dynamic_business_model` 
SET `model_type` = 1 
WHERE `model_type` IS NULL; 