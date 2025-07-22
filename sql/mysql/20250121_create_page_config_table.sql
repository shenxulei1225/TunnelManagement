-- 创建页面配置表
CREATE TABLE IF NOT EXISTS `system_page_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_data` json NOT NULL COMMENT '配置数据',
  `business_name` varchar(100) NOT NULL COMMENT '业务名称',
  `page_title` varchar(100) NOT NULL COMMENT '页面标题',
  `template_type` varchar(50) NOT NULL COMMENT '模板类型',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0-启用 1-禁用）',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key_tenant` (`config_key`, `tenant_id`),
  KEY `idx_business_name` (`business_name`),
  KEY `idx_template_type` (`template_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面配置表'; 