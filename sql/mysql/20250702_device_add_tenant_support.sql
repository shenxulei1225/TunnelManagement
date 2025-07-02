-- 为设备档案表添加多租户支持
-- 执行时间：2025-07-02

-- 1. 为 system_device 表添加 tenant_id 字段
ALTER TABLE `system_device` ADD COLUMN `tenant_id` bigint NOT NULL DEFAULT 1 COMMENT '租户编号' AFTER `id`;

-- 2. 添加 tenant_id 索引以提高查询性能
ALTER TABLE `system_device` ADD INDEX `idx_tenant_id` (`tenant_id`) COMMENT '租户ID索引';

-- 3. 修改设备编号唯一索引，改为租户级别唯一
ALTER TABLE `system_device` DROP INDEX `uk_device_code`;
ALTER TABLE `system_device` ADD UNIQUE KEY `uk_tenant_device_code` (`tenant_id`, `device_code`) COMMENT '租户内设备编号唯一索引';

-- 4. 更新现有数据的租户ID为默认租户(1)
UPDATE `system_device` SET `tenant_id` = 1 WHERE `tenant_id` = 0 OR `tenant_id` IS NULL;

-- 5. 确保 tenant_id 字段不能为 NULL（在设置默认值后）
ALTER TABLE `system_device` MODIFY COLUMN `tenant_id` bigint NOT NULL COMMENT '租户编号';

-- 注意事项：
-- 1. 执行前请备份数据库
-- 2. 如果有大量数据，建议分批执行更新操作
-- 3. 执行后请重启应用服务以刷新缓存
-- 4. 多租户功能开启后，不同租户的设备数据将完全隔离 