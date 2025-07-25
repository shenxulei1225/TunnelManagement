-- 创建分类业务类型关联表
CREATE TABLE IF NOT EXISTS `system_category_biz_type_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint NOT NULL COMMENT '分类编号',
  `business_type` varchar(64) NOT NULL COMMENT '业务类型编码（关联system_business_type.type_code）',
  `business_id` bigint DEFAULT NULL COMMENT '业务对象ID（该业务类型下的具体记录ID）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `required` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否必填',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_business_id` (`business_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  CONSTRAINT `fk_category_biz_type_rel_category` FOREIGN KEY (`category_id`) REFERENCES `system_category` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_category_biz_type_rel_business_type` FOREIGN KEY (`business_type`) REFERENCES `system_business_type` (`type_code`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类业务类型关联表';

-- 删除旧表（如果存在）
DROP TABLE IF EXISTS `system_category_biz_type_rel`;

-- 重新创建表
CREATE TABLE `system_category_biz_type_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint NOT NULL COMMENT '分类编号',
  `business_type` varchar(64) NOT NULL COMMENT '业务类型编码（关联system_business_type.type_code）',
  `business_id` bigint DEFAULT NULL COMMENT '业务对象ID（该业务类型下的具体记录ID）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `required` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否必填',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_business_id` (`business_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类业务类型关联表';

-- 插入正确的示例数据
-- 字段管理分类关联数据
INSERT INTO `system_category_biz_type_rel` (`category_id`, `business_type`, `business_id`, `sort`, `required`, `tenant_id`) VALUES
-- 基础分类
(1, 'FIELD_MANAGEMENT', NULL, 1, 1, 1),
(2, 'FIELD_MANAGEMENT', NULL, 2, 1, 1),
(3, 'FIELD_MANAGEMENT', NULL, 3, 1, 1),
(4, 'FIELD_MANAGEMENT', NULL, 4, 1, 1),
(5, 'FIELD_MANAGEMENT', NULL, 5, 1, 1),
(6, 'FIELD_MANAGEMENT', NULL, 6, 1, 1),
(7, 'FIELD_MANAGEMENT', NULL, 7, 1, 1),
(8, 'FIELD_MANAGEMENT', NULL, 8, 1, 1),
(9, 'FIELD_MANAGEMENT', NULL, 9, 1, 1),
(10, 'FIELD_MANAGEMENT', NULL, 10, 1, 1),

-- 设备管理分类关联数据
(11, 'DEVICE_MANAGEMENT', NULL, 1, 1, 1),
(12, 'DEVICE_MANAGEMENT', NULL, 2, 1, 1),
(13, 'DEVICE_MANAGEMENT', NULL, 3, 1, 1),
(14, 'DEVICE_MANAGEMENT', NULL, 4, 1, 1),
(15, 'DEVICE_MANAGEMENT', NULL, 5, 1, 1),

-- 任务管理分类关联数据
(16, 'TASK_MANAGEMENT', NULL, 1, 1, 1),
(17, 'TASK_MANAGEMENT', NULL, 2, 1, 1),
(18, 'TASK_MANAGEMENT', NULL, 3, 1, 1),
(19, 'TASK_MANAGEMENT', NULL, 4, 1, 1),
(20, 'TASK_MANAGEMENT', NULL, 5, 1, 1),

-- 区域管理分类关联数据
(21, 'REGION_MANAGEMENT', NULL, 1, 1, 1),
(22, 'REGION_MANAGEMENT', NULL, 2, 1, 1),
(23, 'REGION_MANAGEMENT', NULL, 3, 1, 1),
(24, 'REGION_MANAGEMENT', NULL, 4, 1, 1),
(25, 'REGION_MANAGEMENT', NULL, 5, 1, 1); 