-- 为业务模型表添加目录ID字段
ALTER TABLE `business_model` 
ADD COLUMN `directory_id` bigint DEFAULT NULL COMMENT '目录ID' AFTER `tenant_id`,
ADD INDEX `idx_directory_id` (`directory_id`) COMMENT '目录ID索引';

-- 为字段定义表添加目录ID字段（如果需要）
ALTER TABLE `field_definition` 
ADD COLUMN `directory_id` bigint DEFAULT NULL COMMENT '目录ID' AFTER `tenant_id`,
ADD INDEX `idx_directory_id` (`directory_id`) COMMENT '目录ID索引'; 