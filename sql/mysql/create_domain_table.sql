-- ----------------------------
-- Table structure for system_domain
-- ----------------------------
DROP TABLE IF EXISTS `system_domain`;
CREATE TABLE `system_domain` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '领域名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '领域编码',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父领域ID',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '领域描述',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '领域类型',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `sort` int DEFAULT '0' COMMENT '排序',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `readonly` tinyint NOT NULL DEFAULT '0' COMMENT '系统只读标识（0普通 1只读）',
  `tree_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '完整路径（如：1/2/3）',
  `level` tinyint NOT NULL DEFAULT '1' COMMENT '层级深度（根节点为1）',
  `field_count` int NOT NULL DEFAULT '0' COMMENT '直接字段数量',
  `child_count` int NOT NULL DEFAULT '0' COMMENT '直接子领域数量',
  `total_field_count` int NOT NULL DEFAULT '0' COMMENT '总字段数量（包含子领域）',
  `total_child_count` int NOT NULL DEFAULT '0' COMMENT '总子领域数量（包含所有后代）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`,`deleted`,`tenant_id`) USING BTREE COMMENT '领域编码唯一索引',
  UNIQUE KEY `uk_parent_name` (`parent_id`,`name`,`deleted`,`tenant_id`) USING BTREE COMMENT '同父级下名称唯一索引',
  KEY `idx_parent` (`parent_id`) USING BTREE COMMENT '父领域索引',
  KEY `idx_name` (`name`) USING BTREE COMMENT '领域名称索引',
  KEY `idx_type` (`type`) USING BTREE COMMENT '领域类型索引',
  KEY `idx_status` (`status`) USING BTREE COMMENT '状态索引',
  KEY `idx_readonly` (`readonly`) USING BTREE COMMENT '只读状态索引',
  KEY `idx_tree_path` (`tree_path`) USING BTREE COMMENT '树路径索引',
  KEY `idx_level` (`level`) USING BTREE COMMENT '层级索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='领域模型表';

-- ----------------------------
-- Table structure for system_domain_field_rel
-- ----------------------------
DROP TABLE IF EXISTS `system_domain_field_rel`;
CREATE TABLE `system_domain_field_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `domain_id` bigint NOT NULL COMMENT '领域ID',
  `field_id` bigint NOT NULL COMMENT '字段ID',
  `required` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否必填',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_domain_field` (`domain_id`,`field_id`,`deleted`,`tenant_id`) USING BTREE COMMENT '领域字段唯一索引',
  KEY `idx_domain` (`domain_id`) USING BTREE COMMENT '领域索引',
  KEY `idx_field` (`field_id`) USING BTREE COMMENT '字段索引',
  CONSTRAINT `fk_domain_rel_domain` FOREIGN KEY (`domain_id`) REFERENCES `system_domain` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_domain_rel_field` FOREIGN KEY (`field_id`) REFERENCES `system_field_def` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='领域模型字段关联表';

-- ----------------------------
-- Records of system_domain
-- ----------------------------
INSERT INTO `system_domain` (`name`, `code`, `parent_id`, `description`, `type`, `status`, `sort`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES 
('建筑', 'Architecture', 0, '建筑', 'business', 1, 1, '建筑模型', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', b'0', 1),
('设备', 'Equipment', 0, '设备', 'Equipment', 1, 2, '设备模型', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', b'0', 1);
