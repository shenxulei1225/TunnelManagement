-- 创建系统分组关系表
CREATE TABLE IF NOT EXISTS `system_hierarchy_group_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `hierarchy_group_id` bigint NOT NULL COMMENT '分级组ID',
  `target_type` varchar(100) NOT NULL COMMENT '目标类型',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `tenant_id` bigint NOT NULL DEFAULT '1' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_hierarchy_group_id` (`hierarchy_group_id`),
  KEY `idx_target` (`target_type`, `target_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  UNIQUE KEY `uk_hierarchy_target` (`hierarchy_group_id`, `target_type`, `target_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统分级组关系表（多租户）';

-- 添加外键约束（如果需要）
-- ALTER TABLE system_hierarchy_group_relation 
-- ADD CONSTRAINT fk_hierarchy_group_relation_group 
-- FOREIGN KEY (hierarchy_group_id) REFERENCES system_hierarchy_group(id) ON DELETE CASCADE; 