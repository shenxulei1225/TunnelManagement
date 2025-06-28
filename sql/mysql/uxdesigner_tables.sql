-- ----------------------------
-- UX Designer 模块数据库表结构
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
('Universal X Designer 主项目', '核心设计项目，包含主要的用户界面设计', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=400', 'web', 1, 1, 1, 1, '核心项目', 'admin'),
('移动端设计规范', '移动端设计系统和组件库', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=400', 'mobile', 1, 1, 1, 0, '移动端项目', 'admin');

-- 插入示例文件
INSERT INTO `uxd_workspace_file` (`name`, `type`, `thumbnail`, `content`, `category`, `starred`, `file_size`, `project_id`, `user_id`, `team_id`, `status`, `remark`, `creator`) VALUES 
('首页设计稿', 'design', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=300', '{"version":"1.0","type":"design","elements":[]}', 'drafts', 1, 2048, 1, 1, 1, 1, '主页设计文件', 'admin'),
('用户流程图', 'figjam', 'https://images.unsplash.com/photo-1559028006-448665bd7c7f?w=300', '{"version":"1.0","type":"figjam","elements":[]}', 'drafts', 0, 1536, 1, 1, 1, 1, '用户体验流程', 'admin'),
('组件库模板', 'template', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=300', '{"version":"1.0","type":"template","elements":[]}', 'templates', 1, 3072, 2, 1, 1, 1, '可重用组件', 'admin'),
('登录页面设计', 'design', 'https://images.unsplash.com/photo-1517077304055-6e89abbf09b0?w=300', '{"version":"1.0","type":"design","elements":[]}', 'drafts', 0, 1792, 1, 1, 1, 1, '用户登录界面', 'admin');

-- ----------------------------
-- 菜单管理配置SQL
-- ----------------------------

-- 插入UX Designer主菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) VALUES 
('UX Designer', '', 1, 50, 0, '/uxdesigner', 'ep:brush', NULL, NULL, 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0);

-- 获取刚插入的UX Designer菜单ID
SET @uxdesigner_menu_id = LAST_INSERT_ID();

-- 插入工作台子菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) VALUES 
('工作台', 'uxdesigner:workspace:query', 2, 1, @uxdesigner_menu_id, 'workspace', 'ep:monitor', 'uxdesigner/workspace/index', 'UXDesignerWorkspace', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0);

-- 获取工作台菜单ID
SET @workspace_menu_id = LAST_INSERT_ID();

-- 插入工作台文件管理功能按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) VALUES 
('文件查询', 'uxdesigner:workspace-file:query', 3, 1, @workspace_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0),
('文件创建', 'uxdesigner:workspace-file:create', 3, 2, @workspace_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0),
('文件更新', 'uxdesigner:workspace-file:update', 3, 3, @workspace_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0),
('文件删除', 'uxdesigner:workspace-file:delete', 3, 4, @workspace_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0),
('文件导出', 'uxdesigner:workspace-file:export', 3, 5, @workspace_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0);

-- 插入其他子菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) VALUES 
('设计器', 'uxdesigner:designer:query', 2, 2, @uxdesigner_menu_id, 'designer', 'ep:edit', 'uxdesigner/designer/index', 'UXDesignerCanvas', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0),
('模板中心', 'uxdesigner:template:query', 2, 3, @uxdesigner_menu_id, 'template', 'ep:collection-tag', 'uxdesigner/template/index', 'UXDesignerTemplate', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0),
('组件库', 'uxdesigner:component:query', 2, 4, @uxdesigner_menu_id, 'component', 'ep:grid', 'uxdesigner/component/index', 'UXDesignerComponent', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0),
('资源管理', 'uxdesigner:asset:query', 2, 5, @uxdesigner_menu_id, 'asset', 'ep:folder', 'uxdesigner/asset/index', 'UXDesignerAsset', 0, 1, 1, 1, 'admin', NOW(), '', NOW(), 0); 