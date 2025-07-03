-- 创建UX设计器工作台项目表
DROP TABLE IF EXISTS `uxd_workspace_project`;
CREATE TABLE `uxd_workspace_project` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `tenant_id` bigint NOT NULL DEFAULT 1 COMMENT '租户编号',
  `name` varchar(100) NOT NULL COMMENT '项目名称',
  `description` varchar(500) DEFAULT NULL COMMENT '项目描述',
  `cover` varchar(500) DEFAULT NULL COMMENT '项目封面URL',
  `type` varchar(50) NOT NULL DEFAULT 'web' COMMENT '项目类型：web、mobile、desktop等',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '项目状态：0=回收站，1=正常',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `team_id` bigint DEFAULT NULL COMMENT '团队ID',
  `starred` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否收藏',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户ID索引',
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引',
  KEY `idx_team_id` (`team_id`) COMMENT '团队ID索引',
  KEY `idx_type` (`type`) COMMENT '项目类型索引',
  KEY `idx_status` (`status`) COMMENT '项目状态索引',
  KEY `idx_starred` (`starred`) COMMENT '收藏状态索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='UX设计器工作台项目表';

-- 插入示例数据
INSERT INTO `uxd_workspace_project` (`id`, `tenant_id`, `name`, `description`, `cover`, `type`, `status`, `user_id`, `team_id`, `starred`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(1, 1, 'Universal X Designer 主项目', '核心设计项目，包含主要的用户界面设计', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=400', 'web', 1, 1, NULL, b'1', '这是主要的设计项目', 'admin', '2025-01-23 10:00:00', 'admin', '2025-01-23 10:00:00', b'0'),
(2, 1, '移动端设计系统', '移动应用的设计系统和组件库', NULL, 'mobile', 1, 1, NULL, b'0', '移动端UI设计', 'admin', '2025-01-20 10:00:00', 'admin', '2025-01-20 10:00:00', b'0'),
(3, 1, '桌面应用界面', '桌面应用的用户界面设计', NULL, 'desktop', 1, 1, NULL, b'1', '桌面端应用设计', 'admin', '2025-01-18 10:00:00', 'admin', '2025-01-18 10:00:00', b'0'); 