-- 创建业务类型定义表
CREATE TABLE IF NOT EXISTS `system_business_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_code` varchar(64) NOT NULL COMMENT '业务类型编码',
  `type_name` varchar(128) NOT NULL COMMENT '业务类型名称',
  `description` varchar(500) DEFAULT NULL COMMENT '业务类型描述',
  `icon` varchar(128) DEFAULT NULL COMMENT '图标',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_code` (`type_code`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务类型定义表';

-- 删除旧表（如果存在）
DROP TABLE IF EXISTS `system_business_type`;

-- 重新创建表
CREATE TABLE `system_business_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_code` varchar(64) NOT NULL COMMENT '业务类型编码',
  `type_name` varchar(128) NOT NULL COMMENT '业务类型名称',
  `description` varchar(500) DEFAULT NULL COMMENT '业务类型描述',
  `icon` varchar(128) DEFAULT NULL COMMENT '图标',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_code` (`type_code`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务类型定义表';

-- 插入基础业务类型数据
INSERT INTO `system_business_type` (`type_code`, `type_name`, `description`, `icon`, `sort`, `status`, `tenant_id`) VALUES
('FIELD_MANAGEMENT', '字段管理', '字段分类和管理', 'el-icon-document', 1, 1, 1),
('DEVICE_MANAGEMENT', '设备管理', '设备分类和管理', 'el-icon-cpu', 2, 1, 1),
('TASK_MANAGEMENT', '任务管理', '任务分类和管理', 'el-icon-s-order', 3, 1, 1),
('REGION_MANAGEMENT', '区域管理', '区域分类和管理', 'el-icon-location', 4, 1, 1),
('PERSONNEL_MANAGEMENT', '人员管理', '人员分类和管理', 'el-icon-user', 5, 1, 1),
('PROJECT_MANAGEMENT', '项目管理', '项目分类和管理', 'el-icon-folder', 6, 1, 1),
('FINANCE_MANAGEMENT', '财务管理', '财务分类和管理', 'el-icon-money', 7, 1, 1),
('ASSET_MANAGEMENT', '资产管理', '资产分类和管理', 'el-icon-office-building', 8, 1, 1),
('FILE_MANAGEMENT', '文件管理', '文件分类和管理', 'el-icon-document', 9, 1, 1); 