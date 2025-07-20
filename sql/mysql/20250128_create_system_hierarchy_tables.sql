-- 系统分级组表（重命名自system_group，用于设备管理、人员分组等需要层级结构的场景）
CREATE TABLE `system_hierarchy_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分级组ID',
  `tenant_id` bigint NOT NULL DEFAULT 1 COMMENT '租户编号',
  `name` varchar(100) NOT NULL COMMENT '分级组名称',
  `code` varchar(100) DEFAULT NULL COMMENT '分级组编码',
  `parent_id` bigint DEFAULT 0 COMMENT '父分级组ID',
  `level` int NOT NULL DEFAULT 1 COMMENT '层级',
  `path` varchar(255) NOT NULL DEFAULT '/' COMMENT '分级组路径',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序号',
  `color` varchar(20) DEFAULT '#409EFF' COMMENT '颜色',
  `icon` varchar(50) DEFAULT 'Folder' COMMENT '图标',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0正常 1停用）',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统分级组表';

-- 分级组关系表（重命名自system_group_relation，用于关联分级组与具体业务对象）
CREATE TABLE `system_hierarchy_group_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `tenant_id` bigint NOT NULL DEFAULT 1 COMMENT '租户编号',
  `hierarchy_group_id` bigint NOT NULL COMMENT '分级组ID',
  `target_type` varchar(50) NOT NULL COMMENT '目标类型（如：DEVICE、USER等）',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hierarchy_group_target` (`hierarchy_group_id`, `target_type`, `target_id`),
  KEY `idx_target_type_id` (`target_type`, `target_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分级组关系表';

-- 分布式组表（用于角色组、用户组等可以相互包含的场景）
CREATE TABLE `system_distributed_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分布式组ID',
  `tenant_id` bigint NOT NULL DEFAULT 1 COMMENT '租户编号',
  `name` varchar(100) NOT NULL COMMENT '分布式组名称',
  `code` varchar(100) DEFAULT NULL COMMENT '分布式组编码',
  `group_type` varchar(50) NOT NULL COMMENT '组类型（ROLE、USER等）',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `color` varchar(20) DEFAULT '#409EFF' COMMENT '颜色',
  `icon` varchar(50) DEFAULT 'Collection' COMMENT '图标',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0正常 1停用）',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_group_type` (`group_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分布式组表';

-- 分布式组关系表（支持多对多关系）
CREATE TABLE `system_distributed_group_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `tenant_id` bigint NOT NULL DEFAULT 1 COMMENT '租户编号',
  `group_id` bigint NOT NULL COMMENT '分布式组ID',
  `target_type` varchar(50) NOT NULL COMMENT '目标类型（如：ROLE、USER等）',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_target` (`group_id`, `target_type`, `target_id`),
  KEY `idx_target_type_id` (`target_type`, `target_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分布式组关系表';

-- 插入一些示例数据
INSERT INTO `system_hierarchy_group` (`id`, `tenant_id`, `name`, `code`, `parent_id`, `level`, `path`, `sort`, `color`, `icon`, `description`, `status`) VALUES
(1, 1, '设备分级', 'DEVICE_HIERARCHY', 0, 1, '/1', 1, '#409EFF', 'Folder', '设备分级根节点', 0),
(2, 1, '生产设备', 'PRODUCTION_DEVICE', 1, 2, '/1/2', 1, '#67C23A', 'Monitor', '生产设备分组', 0),
(3, 1, '办公设备', 'OFFICE_DEVICE', 1, 2, '/1/3', 2, '#E6A23C', 'Monitor', '办公设备分组', 0),
(4, 1, '人员分级', 'USER_HIERARCHY', 0, 1, '/4', 2, '#F56C6C', 'User', '人员分级根节点', 0),
(5, 1, '技术部门', 'TECH_DEPT', 4, 2, '/4/5', 1, '#67C23A', 'User', '技术部门', 0),
(6, 1, '管理部门', 'MANAGE_DEPT', 4, 2, '/4/6', 2, '#E6A23C', 'User', '管理部门', 0);

INSERT INTO `system_distributed_group` (`id`, `tenant_id`, `name`, `code`, `group_type`, `description`, `color`, `icon`, `status`) VALUES
(1, 1, '管理员角色组', 'ADMIN_ROLE_GROUP', 'ROLE', '管理员角色集合', '#F56C6C', 'Collection', 0),
(2, 1, '普通用户角色组', 'USER_ROLE_GROUP', 'ROLE', '普通用户角色集合', '#67C23A', 'Collection', 0),
(3, 1, '技术用户组', 'TECH_USER_GROUP', 'USER', '技术用户集合', '#409EFF', 'User', 0),
(4, 1, '管理用户组', 'MANAGE_USER_GROUP', 'USER', '管理用户集合', '#E6A23C', 'User', 0); 