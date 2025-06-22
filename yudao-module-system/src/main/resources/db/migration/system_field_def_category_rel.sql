-- 字段定义-分类关联表，包含租户列

CREATE TABLE `system_field_def_category_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_def_id` bigint NOT NULL COMMENT '字段定义 ID',
  `category_id` bigint NOT NULL COMMENT '分类 ID',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  `required` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否必填',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_def` (`field_def_id`),
  KEY `idx_cat` (`category_id`),
  CONSTRAINT `fk_rel_category` FOREIGN KEY (`category_id`) REFERENCES `system_field_category` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_rel_field_def` FOREIGN KEY (`field_def_id`) REFERENCES `system_field_def` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=86 
DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字段定义-分类 关联表';