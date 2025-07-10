-- 创建树形结构配置表
CREATE TABLE `system_tree_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `code` varchar(100) NOT NULL COMMENT '业务类型编码',
    `name` varchar(100) NOT NULL COMMENT '业务类型名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `node_name_label` varchar(50) NOT NULL DEFAULT '名称' COMMENT '节点名称标签',
    `node_code_label` varchar(50) DEFAULT '编码' COMMENT '节点编码标签',
    `max_level` int DEFAULT NULL COMMENT '最大层级（NULL表示不限制）',
    `sort_type` tinyint NOT NULL DEFAULT '1' COMMENT '排序类型：1-手动排序，2-编码排序，3-名称排序',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-启用，1-禁用',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='树形结构配置表';

-- 创建树形结构节点表
CREATE TABLE `system_tree_node` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '节点ID',
    `tree_code` varchar(100) NOT NULL COMMENT '树形业务类型编码',
    `parent_id` bigint DEFAULT NULL COMMENT '父节点ID',
    `name` varchar(100) NOT NULL COMMENT '节点名称',
    `code` varchar(100) DEFAULT NULL COMMENT '节点编码',
    `tree_path` varchar(255) NOT NULL DEFAULT '/' COMMENT '树路径',
    `level` int NOT NULL DEFAULT 1 COMMENT '层级深度',
    `sort` int DEFAULT 0 COMMENT '排序号',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-启用，1-禁用',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_tree_code` (`tree_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_tree_path` (`tree_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='树形结构节点表';

-- 创建树形结构节点属性表
CREATE TABLE `system_tree_node_attribute` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '属性ID',
    `tree_code` varchar(100) NOT NULL COMMENT '树形业务类型编码',
    `node_id` bigint NOT NULL COMMENT '节点ID',
    `attr_key` varchar(100) NOT NULL COMMENT '属性键',
    `attr_value` text COMMENT '属性值',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_node_id` (`node_id`),
    KEY `idx_tree_code` (`tree_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='树形结构节点属性表'; 