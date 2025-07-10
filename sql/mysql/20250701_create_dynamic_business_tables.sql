-- 业务模型配置表
CREATE TABLE `dynamic_business_model` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模型ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `code` varchar(100) NOT NULL COMMENT '业务类型编码',
    `name` varchar(100) NOT NULL COMMENT '业务类型名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
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
    UNIQUE KEY `uk_tenant_code` (`tenant_id`, `code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务模型配置表';

-- 字段定义表
CREATE TABLE `dynamic_field_definition` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字段ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
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
    UNIQUE KEY `uk_tenant_model_code` (`tenant_id`, `model_code`, `code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字段定义表';

-- 业务数据记录表
CREATE TABLE `dynamic_business_record` (
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

-- 视图配置表
CREATE TABLE `dynamic_view_config` (
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