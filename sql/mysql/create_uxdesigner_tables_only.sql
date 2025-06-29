-- ----------------------------
-- UX Designer 模块数据库表结构（仅建表）
-- ----------------------------

-- ----------------------------
-- 工作台文件表
-- ----------------------------
DROP TABLE IF EXISTS `uxd_workspace_file`;
CREATE TABLE `uxd_workspace_file` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `name` varchar(255) NOT NULL COMMENT '文件名称',
  `type` varchar(50) NOT NULL COMMENT '文件类型',
  `thumbnail` varchar(500) DEFAULT NULL COMMENT '缩略图URL',
  `content` longtext COMMENT '文件内容',
  `category` varchar(50) NOT NULL COMMENT '文件分类',
  `starred` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否收藏',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `project_id` bigint DEFAULT NULL COMMENT '所属项目ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `team_id` bigint DEFAULT NULL COMMENT '团队ID',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0=回收站 1=正常）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_team_id` (`team_id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工作台文件表';

-- ----------------------------
-- 工作台项目表
-- ----------------------------
DROP TABLE IF EXISTS `uxd_workspace_project`;
CREATE TABLE `uxd_workspace_project` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `name` varchar(255) NOT NULL COMMENT '项目名称',
  `description` varchar(500) DEFAULT NULL COMMENT '项目描述',
  `cover` varchar(500) DEFAULT NULL COMMENT '项目封面',
  `type` varchar(50) NOT NULL COMMENT '项目类型',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '项目状态（0=归档 1=正常）',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `team_id` bigint DEFAULT NULL COMMENT '团队ID',
  `starred` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否收藏',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_team_id` (`team_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工作台项目表';

-- ----------------------------
-- 插入测试数据
-- ----------------------------

-- 插入示例项目
INSERT INTO `uxd_workspace_project` (`name`, `description`, `cover`, `type`, `status`, `user_id`, `team_id`, `starred`, `remark`, `creator`) VALUES 
('UX Designer 主项目', '核心设计项目，体验一切，导出无界', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=400', 'web', 1, 1, 1, 1, '核心项目', 'admin'),
('电商平台设计', '现代化电商平台的完整设计系统', 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=400', 'web', 1, 1, 1, 0, '电商项目设计', 'admin'),
('移动端设计规范', '移动端设计系统和组件库', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=400', 'mobile', 1, 1, 1, 1, '移动端项目', 'admin'),
('企业管理后台', '企业级管理系统界面设计', 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=400', 'web', 1, 1, 1, 0, '企业级后台', 'admin');

-- 插入示例文件
INSERT INTO `uxd_workspace_file` (`name`, `type`, `thumbnail`, `content`, `category`, `starred`, `file_size`, `project_id`, `user_id`, `team_id`, `status`, `remark`, `creator`) VALUES 
('品牌首页设计', 'design', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=300', '{"type":"design"}', 'drafts', 1, 2048, 1, 1, 1, 1, '品牌主页设计', 'admin'),
('用户旅程地图', 'figjam', 'https://images.unsplash.com/photo-1559028006-448665bd7c7f?w=300', '{"type":"figjam"}', 'drafts', 1, 1536, 1, 1, 1, 1, '用户体验旅程', 'admin'),
('商品详情页', 'design', 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=300', '{"type":"design"}', 'drafts', 0, 2560, 2, 1, 1, 1, '商品详情页', 'admin'),
('移动端组件库', 'template', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=300', '{"type":"template"}', 'templates', 1, 3500, 3, 1, 1, 1, '移动端组件库', 'admin');

-- 表创建完成，可以通过菜单管理界面配置UX设计器菜单 