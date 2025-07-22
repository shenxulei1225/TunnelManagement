-- 动态业务模块相关表结构
-- 创建时间：2025-01-20
-- 描述：支持动态业务模块创建、页面模板选择、页面配置和代码生成

-- 业务模块表
CREATE TABLE `dynamic_business_module` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模块ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `code` varchar(100) NOT NULL COMMENT '模块编码',
    `name` varchar(100) NOT NULL COMMENT '模块名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `category` varchar(50) NOT NULL COMMENT '模块分类',
    `priority` tinyint NOT NULL DEFAULT '1' COMMENT '优先级（1:低，2:中，3:高）',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:草稿，1:已发布，2:已归档，3:已删除）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_code` (`tenant_id`, `code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务模块表';

-- 页面模板表
CREATE TABLE `dynamic_page_template` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    `code` varchar(100) NOT NULL COMMENT '模板编码',
    `name` varchar(100) NOT NULL COMMENT '模板名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `category` varchar(50) NOT NULL COMMENT '模板分类（data_management:数据管理，monitor_analysis:监控分析，business_management:业务管理）',
    `type` varchar(50) NOT NULL COMMENT '模板类型（list:列表，form:表单，dashboard:仪表盘，monitor:监控）',
    `config_schema` text COMMENT '配置模式（JSON格式）',
    `components` text COMMENT '组件配置（JSON格式）',
    `data_source` text COMMENT '数据源配置（JSON格式）',
    `permissions` text COMMENT '权限配置（JSON格式）',
    `preview_image` varchar(255) DEFAULT NULL COMMENT '预览图片路径',
    `popularity` int DEFAULT '0' COMMENT '使用次数',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:禁用，1:启用）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面模板表';

-- 页面配置表
CREATE TABLE `dynamic_page_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `module_code` varchar(100) NOT NULL COMMENT '业务模块编码',
    `template_code` varchar(100) NOT NULL COMMENT '页面模板编码',
    `page_code` varchar(100) NOT NULL COMMENT '页面编码',
    `page_name` varchar(100) NOT NULL COMMENT '页面名称',
    `page_description` varchar(500) DEFAULT NULL COMMENT '页面描述',
    `page_icon` varchar(100) DEFAULT NULL COMMENT '页面图标',
    `data_source_config` text COMMENT '数据源配置（JSON格式）',
    `field_config` text COMMENT '字段配置（JSON格式）',
    `layout_config` text COMMENT '布局配置（JSON格式）',
    `function_config` text COMMENT '功能配置（JSON格式）',
    `permission_config` text COMMENT '权限配置（JSON格式）',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:草稿，1:已生成，2:已部署）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_module_page` (`tenant_id`, `module_code`, `page_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面配置表';

-- 代码生成记录表
CREATE TABLE `dynamic_code_generation` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `module_code` varchar(100) NOT NULL COMMENT '业务模块编码',
    `page_code` varchar(100) NOT NULL COMMENT '页面编码',
    `generation_type` varchar(50) NOT NULL COMMENT '生成类型（frontend:前端，backend:后端，database:数据库）',
    `file_path` varchar(500) NOT NULL COMMENT '文件路径',
    `file_content` longtext COMMENT '文件内容',
    `file_size` bigint DEFAULT '0' COMMENT '文件大小（字节）',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:生成中，1:已生成，2:已部署，3:生成失败）',
    `error_message` text COMMENT '错误信息',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_module_page` (`tenant_id`, `module_code`, `page_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='代码生成记录表';

-- 部署记录表
CREATE TABLE `dynamic_deployment_record` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `module_code` varchar(100) NOT NULL COMMENT '业务模块编码',
    `deployment_type` varchar(50) NOT NULL COMMENT '部署类型（menu:菜单，permission:权限，route:路由）',
    `target_path` varchar(500) NOT NULL COMMENT '目标路径',
    `deployment_data` text COMMENT '部署数据（JSON格式）',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:部署中，1:已部署，2:部署失败）',
    `error_message` text COMMENT '错误信息',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_module` (`tenant_id`, `module_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部署记录表';

-- 插入基础页面模板数据
INSERT INTO `dynamic_page_template` (`code`, `name`, `description`, `category`, `type`, `config_schema`, `components`, `data_source`, `permissions`, `status`, `creator`) VALUES
-- 数据管理模板
('data_list_template', '数据列表模板', '标准的数据列表页面，支持搜索、筛选、分页、批量操作', 'data_management', 'list', 
'{"dataSource": {"type": "table", "config": {}}, "layout": {"type": "table", "config": {}}, "components": {"search": true, "filter": true, "pagination": true, "batch": true}}',
'[{"type": "search", "config": {}}, {"type": "table", "config": {}}, {"type": "pagination", "config": {}}]',
'{"type": "database", "config": {"table": "", "primaryKey": "id"}}',
'{"read": true, "write": true, "delete": true}', 1, 'admin'),

('data_form_template', '数据表单模板', '数据录入和编辑表单页面', 'data_management', 'form',
'{"dataSource": {"type": "table", "config": {}}, "layout": {"type": "form", "config": {}}, "components": {"validation": true, "submit": true}}',
'[{"type": "form", "config": {}}, {"type": "button", "config": {}}]',
'{"type": "database", "config": {"table": "", "primaryKey": "id"}}',
'{"read": true, "write": true}', 1, 'admin'),

('data_detail_template', '数据详情模板', '详细数据展示页面', 'data_management', 'detail',
'{"dataSource": {"type": "table", "config": {}}, "layout": {"type": "detail", "config": {}}, "components": {"display": true, "action": true}}',
'[{"type": "detail", "config": {}}, {"type": "action", "config": {}}]',
'{"type": "database", "config": {"table": "", "primaryKey": "id"}}',
'{"read": true}', 1, 'admin'),

-- 监控分析模板
('dashboard_template', '仪表盘模板', '数据可视化仪表盘页面', 'monitor_analysis', 'dashboard',
'{"dataSource": {"type": "api", "config": {}}, "layout": {"type": "grid", "config": {}}, "components": {"chart": true, "metric": true}}',
'[{"type": "chart", "config": {}}, {"type": "metric", "config": {}}]',
'{"type": "api", "config": {"endpoint": "", "method": "GET"}}',
'{"read": true}', 1, 'admin'),

('monitor_template', '实时监控模板', '实时数据监控页面', 'monitor_analysis', 'monitor',
'{"dataSource": {"type": "websocket", "config": {}}, "layout": {"type": "monitor", "config": {}}, "components": {"realtime": true, "alert": true}}',
'[{"type": "realtime", "config": {}}, {"type": "alert", "config": {}}]',
'{"type": "websocket", "config": {"endpoint": "", "interval": 5000}}',
'{"read": true}', 1, 'admin'),

('trend_analysis_template', '趋势分析模板', '数据趋势分析页面', 'monitor_analysis', 'analysis',
'{"dataSource": {"type": "api", "config": {}}, "layout": {"type": "analysis", "config": {}}, "components": {"trend": true, "comparison": true}}',
'[{"type": "trend", "config": {}}, {"type": "comparison", "config": {}}]',
'{"type": "api", "config": {"endpoint": "", "method": "GET"}}',
'{"read": true}', 1, 'admin'),

-- 业务管理模板
('task_management_template', '任务管理模板', '任务分配和管理页面', 'business_management', 'task',
'{"dataSource": {"type": "table", "config": {}}, "layout": {"type": "kanban", "config": {}}, "components": {"kanban": true, "timeline": true}}',
'[{"type": "kanban", "config": {}}, {"type": "timeline", "config": {}}]',
'{"type": "database", "config": {"table": "", "primaryKey": "id"}}',
'{"read": true, "write": true}', 1, 'admin'),

('workflow_template', '流程管理模板', '业务流程管理页面', 'business_management', 'workflow',
'{"dataSource": {"type": "table", "config": {}}, "layout": {"type": "flow", "config": {}}, "components": {"flow": true, "approval": true}}',
'[{"type": "flow", "config": {}}, {"type": "approval", "config": {}}]',
'{"type": "database", "config": {"table": "", "primaryKey": "id"}}',
'{"read": true, "write": true}', 1, 'admin'),

('report_template', '报告管理模板', '报告生成和管理页面', 'business_management', 'report',
'{"dataSource": {"type": "api", "config": {}}, "layout": {"type": "report", "config": {}}, "components": {"report": true, "export": true}}',
'[{"type": "report", "config": {}}, {"type": "export", "config": {}}]',
'{"type": "api", "config": {"endpoint": "", "method": "GET"}}',
'{"read": true, "export": true}', 1, 'admin');

-- 插入示例业务模块数据
INSERT INTO `dynamic_business_module` (`tenant_id`, `code`, `name`, `description`, `category`, `priority`, `status`, `creator`) VALUES
(1, 'facility_management', '设施管理', '设施信息管理、监控、维护', 'device_management', 3, 1, 'admin'),
(1, 'inspection_management', '巡检管理', '巡检任务、路线、报告管理', 'operation_management', 3, 1, 'admin'),
(1, 'maintenance_management', '维护管理', '设备维护计划、执行、记录管理', 'device_management', 2, 1, 'admin'),
(1, 'alarm_management', '告警管理', '系统告警监控、处理、统计管理', 'monitor_management', 2, 1, 'admin'); 