-- 创建动态业务相关表结构
-- 1. 业务模型配置表
CREATE TABLE IF NOT EXISTS `dynamic_business_model` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模型ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `directory_id` bigint DEFAULT NULL COMMENT '目录ID',
    `code` varchar(100) NOT NULL COMMENT '业务类型编码',
    `name` varchar(100) NOT NULL COMMENT '业务类型名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `table_name` varchar(100) DEFAULT NULL COMMENT '数据表名',
    `model_type` tinyint NOT NULL DEFAULT '1' COMMENT '模型类型（0:系统，1:自定义）',
    `sort` int DEFAULT '0' COMMENT '排序号',
    `structure_type` tinyint NOT NULL DEFAULT '1' COMMENT '数据结构类型（1:树形，2:列表）',
    `storage_strategy` tinyint NOT NULL DEFAULT '1' COMMENT '存储策略（1:单表，2:分表）',
    `config` text COMMENT '配置JSON',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:禁用，1:启用）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_code` (`tenant_id`, `code`),
    KEY `idx_directory_id` (`directory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务模型配置表';

-- 2. 字段定义表
CREATE TABLE IF NOT EXISTS `dynamic_field_definition` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字段ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `directory_id` bigint DEFAULT NULL COMMENT '目录ID',
    `model_code` varchar(100) NOT NULL COMMENT '业务模型编码',
    `code` varchar(100) NOT NULL COMMENT '字段编码',
    `name` varchar(100) NOT NULL COMMENT '字段名称',
    `type` varchar(50) NOT NULL COMMENT '字段类型',
    `required` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否必填',
    `default_value` varchar(255) DEFAULT NULL COMMENT '默认值',
    `validation_rules` text COMMENT '验证规则（JSON格式）',
    `display_config` text COMMENT '显示配置（JSON格式）',
    `group_name` varchar(100) DEFAULT NULL COMMENT '字段分组',
    `sort` int DEFAULT '0' COMMENT '排序号',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:禁用，1:启用）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_model_code` (`tenant_id`, `model_code`, `code`),
    KEY `idx_directory_id` (`directory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字段定义表';

-- 3. 业务数据记录表
CREATE TABLE IF NOT EXISTS `dynamic_business_record` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `model_code` varchar(100) NOT NULL COMMENT '业务模型编码',
    `parent_id` bigint DEFAULT NULL COMMENT '父记录ID',
    `tree_path` varchar(255) NOT NULL DEFAULT '/' COMMENT '树路径',
    `level` int NOT NULL DEFAULT '1' COMMENT '层级',
    `sort` int DEFAULT '0' COMMENT '排序号',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:禁用，1:启用）',
    `data` text NOT NULL COMMENT '数据JSON',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_model` (`tenant_id`, `model_code`),
    KEY `idx_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务数据记录表';

-- 4. 视图配置表
CREATE TABLE IF NOT EXISTS `dynamic_view_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `model_code` varchar(100) NOT NULL COMMENT '业务模型编码',
    `type` varchar(50) NOT NULL COMMENT '视图类型（form:表单，list:列表，tree:树形）',
    `code` varchar(100) NOT NULL COMMENT '视图编码',
    `name` varchar(100) NOT NULL COMMENT '视图名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `layout` text COMMENT '布局配置（JSON格式）',
    `components` text COMMENT '组件配置（JSON格式）',
    `actions` text COMMENT '操作配置（JSON格式）',
    `permissions` text COMMENT '权限配置（JSON格式）',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:禁用，1:启用）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_model_type_code` (`tenant_id`, `model_code`, `type`, `code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视图配置表';

-- 5. 通用目录表
CREATE TABLE IF NOT EXISTS `system_directory` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '目录ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `business_type` varchar(50) NOT NULL COMMENT '业务类型',
    `parent_id` bigint DEFAULT 0 COMMENT '父目录ID（0表示根目录）',
    `name` varchar(100) NOT NULL COMMENT '目录名称',
    `code` varchar(100) NOT NULL COMMENT '目录编码',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `icon` varchar(100) DEFAULT NULL COMMENT '图标',
    `sort` int DEFAULT 0 COMMENT '排序号',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0:禁用，1:启用）',
    `ext_data` text COMMENT '扩展属性（JSON格式）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_business_code` (`tenant_id`, `business_type`, `code`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_business_type` (`business_type`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通用目录表';

-- 插入默认目录数据（动态业务模型）
INSERT IGNORE INTO `system_directory` (`id`, `tenant_id`, `business_type`, `parent_id`, `name`, `code`, `description`, `icon`, `sort`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(1, 1, 'dynamic_model', 0, '系统模型', 'system_models', '系统预置的业务模型', 'el-icon-setting', 1, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(2, 1, 'dynamic_model', 0, '设备管理', 'device_management', '设备相关的业务模型', 'el-icon-cpu', 2, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(3, 1, 'dynamic_model', 0, '人员管理', 'personnel_management', '人员相关的业务模型', 'el-icon-user', 3, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(4, 1, 'dynamic_model', 0, '项目管理', 'project_management', '项目相关的业务模型', 'el-icon-folder', 4, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(5, 1, 'dynamic_model', 0, '财务管理', 'finance_management', '财务相关的业务模型', 'el-icon-money', 5, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(6, 1, 'dynamic_model', 0, '其他', 'others', '其他业务模型', 'el-icon-more', 999, 1, 'admin', NOW(), 'admin', NOW(), b'0'); 