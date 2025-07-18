-- 创建系统目录表
CREATE TABLE IF NOT EXISTS `system_directory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '目录ID',
  `business_type` varchar(50) NOT NULL COMMENT '业务类型',
  `parent_id` bigint DEFAULT 0 COMMENT '父目录ID，0表示根目录',
  `name` varchar(100) NOT NULL COMMENT '目录名称',
  `code` varchar(100) NOT NULL COMMENT '目录编码',
  `description` varchar(500) DEFAULT NULL COMMENT '目录描述',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT 1 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `ext_data` json DEFAULT NULL COMMENT '扩展数据',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_code` (`code`),
  KEY `idx_tenant_id` (`tenant_id`),
  UNIQUE KEY `uk_business_code_tenant` (`business_type`, `code`, `tenant_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统目录表';

-- 插入测试数据
INSERT INTO `system_directory` (`business_type`, `parent_id`, `name`, `code`, `description`, `icon`, `sort`, `status`, `creator`, `updater`, `tenant_id`) VALUES
('FIELD_SEMANTIC', 0, '测量类', 'measurement', '测量相关的字段', 'Ruler', 1, 1, 'system', 'system', 1),
('FIELD_SEMANTIC', 0, '电气类', 'electrical', '电气相关的字段', 'Lightning', 2, 1, 'system', 'system', 1),
('FIELD_SEMANTIC', 0, '地理类', 'geographic', '地理位置相关的字段', 'Location', 3, 1, 'system', 'system', 1),
('FIELD_SEMANTIC', 1, '长度测量', 'length_measurement', '长度、距离等测量', 'Ruler', 11, 1, 'system', 'system', 1),
('FIELD_SEMANTIC', 1, '温度测量', 'temperature_measurement', '温度相关测量', 'Thermometer', 12, 1, 'system', 'system', 1),
('dynamic_model', 0, '业务模型', 'business_model', '动态业务模型分类', 'Grid', 1, 1, 'system', 'system', 1),
('file_management', 0, '文档管理', 'document_management', '文件和文档分类', 'Folder', 1, 1, 'system', 'system', 1); 