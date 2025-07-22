-- 页面设计表
CREATE TABLE `system_page_design` (
  `id` varchar(64) NOT NULL COMMENT '页面设计ID',
  `page_name` varchar(100) NOT NULL COMMENT '页面名称',
  `page_path` varchar(200) NOT NULL COMMENT '页面路径',
  `menu_title` varchar(100) DEFAULT NULL COMMENT '菜单标题',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `description` varchar(500) DEFAULT NULL COMMENT '页面描述',
  `page_type` varchar(50) DEFAULT NULL COMMENT '页面类型',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态',
  `build_status` varchar(20) DEFAULT NULL COMMENT '构建状态',
  `components` text DEFAULT NULL COMMENT '组件配置(JSON)',
  `config` text DEFAULT NULL COMMENT '页面配置(JSON)',
  `page_code` longtext DEFAULT NULL COMMENT '页面代码',
  `route_config` text DEFAULT NULL COMMENT '路由配置(JSON)',
  `menu_config` text DEFAULT NULL COMMENT '菜单配置(JSON)',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_page_path` (`page_path`),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面设计表';

-- 页面构建记录表
CREATE TABLE `system_page_build_record` (
  `id` varchar(64) NOT NULL COMMENT '构建记录ID',
  `page_design_id` varchar(64) NOT NULL COMMENT '页面设计ID',
  `build_id` varchar(64) NOT NULL COMMENT '构建ID',
  `build_status` varchar(20) NOT NULL DEFAULT 'building' COMMENT '构建状态',
  `progress` int DEFAULT 0 COMMENT '构建进度(0-100)',
  `message` varchar(500) DEFAULT NULL COMMENT '构建消息',
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '完成时间',
  `error_message` text DEFAULT NULL COMMENT '错误信息',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_build_id` (`build_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_page_design_id` (`page_design_id`),
  KEY `idx_build_status` (`build_status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面构建记录表';

-- 页面部署记录表
CREATE TABLE `system_page_deploy_record` (
  `id` varchar(64) NOT NULL COMMENT '部署记录ID',
  `page_design_id` varchar(64) NOT NULL COMMENT '页面设计ID',
  `deploy_status` varchar(20) NOT NULL DEFAULT 'deploying' COMMENT '部署状态',
  `deploy_path` varchar(200) DEFAULT NULL COMMENT '部署路径',
  `deploy_message` varchar(500) DEFAULT NULL COMMENT '部署消息',
  `deploy_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '部署时间',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_page_design_id` (`page_design_id`),
  KEY `idx_deploy_status` (`deploy_status`),
  KEY `idx_deploy_time` (`deploy_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面部署记录表';

-- 页面模板表
CREATE TABLE `system_page_template` (
  `id` varchar(64) NOT NULL COMMENT '模板ID',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_code` varchar(100) NOT NULL COMMENT '模板代码',
  `template_type` varchar(50) NOT NULL COMMENT '模板类型',
  `description` varchar(500) DEFAULT NULL COMMENT '模板描述',
  `template_config` text DEFAULT NULL COMMENT '模板配置(JSON)',
  `preview_image` varchar(200) DEFAULT NULL COMMENT '预览图片',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '状态',
  `sort` int DEFAULT 0 COMMENT '排序',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_code_tenant` (`template_code`, `tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_template_type` (`template_type`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面模板表';

-- 插入一些默认模板数据（租户1为系统默认模板）
INSERT INTO `system_page_template` (`id`, `template_name`, `template_code`, `template_type`, `description`, `template_config`, `status`, `sort`, `tenant_id`) VALUES
('template-001', '表单页面', 'form-page', 'form', '标准的表单页面模板，包含输入框、选择器等组件', '{"components": ["input", "select", "textarea", "button"]}', 'active', 1, 1),
('template-002', '列表页面', 'list-page', 'list', '数据列表页面模板，包含表格、分页、搜索等功能', '{"components": ["table", "pagination", "search", "button"]}', 'active', 2, 1),
('template-003', '详情页面', 'detail-page', 'detail', '数据详情页面模板，用于显示单条记录的详细信息', '{"components": ["description", "card", "button"]}', 'active', 3, 1),
('template-004', '树形页面', 'tree-page', 'tree', '树形结构页面模板，支持层级数据的展示和操作', '{"components": ["tree", "button", "modal"]}', 'active', 4, 1),
('template-005', '拖拽分类', 'drag-drop-classification', 'classification', '拖拽分类页面模板，支持拖拽操作的数据分类管理', '{"components": ["tree", "drag-drop", "button"]}', 'active', 5, 1);

-- 修改已插入数据的租户ID（如果之前插入的是租户0，现在改为租户1）
UPDATE `system_page_template` SET `tenant_id` = 1 WHERE `tenant_id` = 0; 