-- 通用树结构-数据关联表
-- 支持各种类型的数据（字段、设备、区域、文件等）插入到树结构中
CREATE TABLE IF NOT EXISTS `system_tree_data_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tree_type` varchar(50) NOT NULL COMMENT '树类型：field_category/hierarchy_group/region_tree/device_tree/file_category等',
  `tree_node_id` bigint NOT NULL COMMENT '树节点ID',
  `data_type` varchar(50) NOT NULL COMMENT '数据类型：field_def/device/region/file/document等',
  `data_id` bigint NOT NULL COMMENT '数据ID',
  `data_name` varchar(255) NOT NULL COMMENT '数据名称（冗余字段，便于查询）',
  `data_type_label` varchar(50) DEFAULT NULL COMMENT '数据类型标签（如：字段、设备、区域等）',
  `display_order` int NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `is_required` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否必填（针对字段类型）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `metadata` json DEFAULT NULL COMMENT '扩展元数据（JSON格式）',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_tree_type_node` (`tree_type`, `tree_node_id`),
  KEY `idx_data_type_id` (`data_type`, `data_id`),
  KEY `idx_tenant` (`tenant_id`),
  KEY `idx_status` (`status`),
  UNIQUE KEY `uk_tree_data` (`tree_type`, `tree_node_id`, `data_type`, `data_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通用树结构-数据关联表';

-- 树结构配置表（用于定义不同类型的树结构）
CREATE TABLE IF NOT EXISTS `system_tree_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `tree_type` varchar(50) NOT NULL COMMENT '树类型编码',
  `tree_name` varchar(100) NOT NULL COMMENT '树类型名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `allowed_data_types` json NOT NULL COMMENT '允许的数据类型列表',
  `max_level` int DEFAULT NULL COMMENT '最大层级（NULL表示不限制）',
  `node_name_label` varchar(50) NOT NULL DEFAULT '名称' COMMENT '节点名称标签',
  `node_code_label` varchar(50) DEFAULT '编码' COMMENT '节点编码标签',
  `sort_type` tinyint NOT NULL DEFAULT '1' COMMENT '排序类型：1-按名称，2-按编码，3-按创建时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `config_json` json DEFAULT NULL COMMENT '扩展配置（JSON格式）',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tree_type` (`tree_type`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='树结构配置表';

-- 插入默认的树结构配置
INSERT INTO `system_tree_config` (
  `tree_type`, `tree_name`, `description`, `allowed_data_types`, 
  `max_level`, `node_name_label`, `node_code_label`, `sort_type`, `status`
) VALUES 
('field_category', '字段分类', '用于管理字段的分类结构', '["field_def"]', 5, '分类名称', '分类编码', 1, 1),
('hierarchy_group', '分级组', '用于管理业务实体的分级结构', '["field_def", "device", "region"]', 10, '组名称', '组编码', 1, 1),
('region_tree', '区域树', '用于管理地理区域的分级结构', '["device", "field_def"]', 8, '区域名称', '区域编码', 1, 1),
('device_tree', '设备树', '用于管理设备的分级结构', '["field_def", "document"]', 6, '设备名称', '设备编码', 1, 1),
('file_category', '文件分类', '用于管理文件的分类结构', '["file", "document"]', 4, '分类名称', '分类编码', 1, 1);

-- 数据类型的元数据表（用于定义各种数据类型的属性）
CREATE TABLE IF NOT EXISTS `system_data_type_meta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_type` varchar(50) NOT NULL COMMENT '数据类型编码',
  `data_name` varchar(100) NOT NULL COMMENT '数据类型名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `color` varchar(20) DEFAULT NULL COMMENT '颜色',
  `table_name` varchar(100) DEFAULT NULL COMMENT '对应的数据表名',
  `id_field` varchar(50) DEFAULT 'id' COMMENT 'ID字段名',
  `name_field` varchar(50) DEFAULT 'name' COMMENT '名称字段名',
  `status_field` varchar(50) DEFAULT NULL COMMENT '状态字段名',
  `config_json` json DEFAULT NULL COMMENT '扩展配置（JSON格式）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_data_type` (`data_type`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据类型元数据表';

-- 插入默认的数据类型元数据
INSERT INTO `system_data_type_meta` (
  `data_type`, `data_name`, `description`, `icon`, `color`, `table_name`, `id_field`, `name_field`
) VALUES 
('field_def', '字段定义', '动态字段定义', 'el-icon-document', '#409EFF', 'system_field_def', 'id', 'field_label'),
('device', '设备', '设备信息', 'el-icon-cpu', '#67C23A', 'system_device', 'id', 'name'),
('region', '区域', '地理区域', 'el-icon-location', '#E6A23C', 'system_region', 'id', 'name'),
('file', '文件', '文件信息', 'el-icon-folder', '#F56C6C', 'sys_file_info', 'id', 'original_name'),
('document', '文档', '文档信息', 'el-icon-document-copy', '#909399', 'sys_document', 'id', 'title'); 