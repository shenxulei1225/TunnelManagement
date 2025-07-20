-- 字段定义-分级组关联表
CREATE TABLE IF NOT EXISTS `system_field_def_hierarchy_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_def_id` bigint NOT NULL COMMENT '字段定义 ID',
  `hierarchy_group_id` bigint NOT NULL COMMENT '分级组 ID',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_field_def_id` (`field_def_id`),
  KEY `idx_hierarchy_group_id` (`hierarchy_group_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  UNIQUE KEY `uk_field_hierarchy` (`field_def_id`, `hierarchy_group_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字段定义-分级组 关联表'; 