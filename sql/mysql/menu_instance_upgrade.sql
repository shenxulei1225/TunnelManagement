-- 菜单管理系统实例化升级脚本
-- 支持模板实例化的菜单系统

-- 1. 扩展现有菜单表，添加实例化支持字段
ALTER TABLE system_menu ADD COLUMN `is_template_instance` TINYINT(1) DEFAULT 0 COMMENT '是否为模板实例: 0=普通菜单 1=模板实例';
ALTER TABLE system_menu ADD COLUMN `template_id` VARCHAR(100) DEFAULT NULL COMMENT '模板ID，用于标识基于哪个模板生成';
ALTER TABLE system_menu ADD COLUMN `instance_config_id` VARCHAR(100) DEFAULT NULL COMMENT '实例配置ID，关联到具体的配置参数';
ALTER TABLE system_menu ADD COLUMN `dynamic_params` JSON DEFAULT NULL COMMENT '动态路由参数配置，存储参数名、标题模板等';

-- 2. 创建模板实例配置表
CREATE TABLE `system_template_instance` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_id` VARCHAR(100) NOT NULL COMMENT '实例唯一标识',
  `template_id` VARCHAR(100) NOT NULL COMMENT '模板ID',
  `instance_name` VARCHAR(255) NOT NULL COMMENT '实例名称',
  `business_type` VARCHAR(100) NOT NULL COMMENT '业务类型',
  `config_data` JSON NOT NULL COMMENT '实例配置数据',
  `route_config` JSON DEFAULT NULL COMMENT '路由配置信息',
  `menu_id` BIGINT(20) DEFAULT NULL COMMENT '关联的菜单ID',
  `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_instance_id` (`instance_id`),
  INDEX `idx_template_id` (`template_id`),
  INDEX `idx_business_type` (`business_type`),
  INDEX `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板实例配置表';

-- 3. 创建模板定义表
CREATE TABLE `system_template_definition` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` VARCHAR(100) NOT NULL COMMENT '模板唯一标识',
  `template_name` VARCHAR(255) NOT NULL COMMENT '模板名称',
  `template_type` VARCHAR(100) NOT NULL COMMENT '模板类型: drag-drop-classification, data-table, form-builder等',
  `component_path` VARCHAR(500) NOT NULL COMMENT '组件路径',
  `route_pattern` VARCHAR(500) NOT NULL COMMENT '路由模式: 静态路径或动态路径模板',
  `supports_multi_instance` TINYINT(1) DEFAULT 1 COMMENT '是否支持多实例: 0=否 1=是',
  `default_config_schema` JSON DEFAULT NULL COMMENT '默认配置结构定义',
  `config_form_schema` JSON DEFAULT NULL COMMENT '配置表单结构定义',
  `preview_component` VARCHAR(500) DEFAULT NULL COMMENT '预览组件路径',
  `description` TEXT COMMENT '模板描述',
  `tags` JSON DEFAULT NULL COMMENT '模板标签',
  `status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '状态: 0=正常 1=禁用',
  `sort` INT(11) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_id` (`template_id`),
  INDEX `idx_template_type` (`template_type`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板定义表';

-- 4. 插入预定义模板数据
INSERT INTO `system_template_definition` (`template_id`, `template_name`, `template_type`, `component_path`, `route_pattern`, `supports_multi_instance`, `default_config_schema`, `description`, `tags`, `status`, `sort`) VALUES
('draggable-category', '拖拽分类管理', 'drag-drop-classification', 'examples/DragDropClassificationExample', '/examples/drag-drop-classification/:configId', 1, 
'{"pageTitle": "分类管理", "categoryTitle": "分类", "itemUnit": "个项目", "enableDragSort": true, "enableSearch": true}',
'支持拖拽排序的分类管理模板，适用于设备分类、产品分类、内容分类等场景', 
'["分类管理", "拖拽排序", "树形结构", "多实例"]', 0, 1),

('data-table', '数据表格管理', 'data-table', 'examples/DataTableExample', '/examples/data-table/:configId', 1,
'{"pageTitle": "数据管理", "enableSearch": true, "enablePagination": true, "pageSize": 20}',
'通用数据表格管理模板，支持CRUD操作、搜索、分页等功能',
'["数据管理", "表格", "CRUD", "搜索分页"]', 0, 2),

('form-builder', '表单构建器', 'form-builder', 'examples/FormBuilderExample', '/examples/form-builder/:configId', 1,
'{"pageTitle": "表单管理", "enableValidation": true, "submitText": "提交", "resetText": "重置"}',
'动态表单构建器模板，支持自定义字段、验证规则等',
'["表单构建", "动态字段", "数据验证", "自定义"]', 0, 3);

-- 5. 创建实例使用统计表（可选，用于监控和优化）
CREATE TABLE `system_template_instance_stats` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_id` VARCHAR(100) NOT NULL COMMENT '实例ID',
  `access_count` BIGINT(20) DEFAULT 0 COMMENT '访问次数',
  `last_access_time` DATETIME DEFAULT NULL COMMENT '最后访问时间',
  `data_count` BIGINT(20) DEFAULT 0 COMMENT '数据记录数',
  `status` TINYINT(4) DEFAULT 1 COMMENT '实例状态: 1=活跃 2=休眠 3=待删除',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_instance_id` (`instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板实例使用统计表';

-- 添加索引优化查询性能
CREATE INDEX idx_menu_template ON system_menu(is_template_instance, template_id);
CREATE INDEX idx_menu_instance_config ON system_menu(instance_config_id); 