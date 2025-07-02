-- 创建设备档案表
DROP TABLE IF EXISTS `system_device`;
CREATE TABLE `system_device` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `tenant_id` bigint NOT NULL DEFAULT 1 COMMENT '租户编号',
  `name` varchar(100) NOT NULL COMMENT '设备名称',
  `device_code` varchar(50) NOT NULL COMMENT '设备编号',
  `parent_id` bigint DEFAULT 0 COMMENT '父设备ID（支持多级设备管理）',
  `device_type_id` bigint DEFAULT NULL COMMENT '设备类型ID',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID（关联字段分类）',
  `region_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `sort` int DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '设备状态（0正常 1停用 2维修中 3报废）',
  `responsible_user_id` bigint DEFAULT NULL COMMENT '设备负责人',
  `manufacturer` varchar(100) DEFAULT NULL COMMENT '制造商',
  `model` varchar(100) DEFAULT NULL COMMENT '设备型号',
  `serial_number` varchar(100) DEFAULT NULL COMMENT '序列号',
  `purchase_date` datetime DEFAULT NULL COMMENT '购买日期',
  `install_date` datetime DEFAULT NULL COMMENT '安装日期',
  `warranty_expire_date` datetime DEFAULT NULL COMMENT '保修到期日期',
  `description` varchar(500) DEFAULT NULL COMMENT '设备描述',
  `extra_attrs` json DEFAULT NULL COMMENT '动态扩展属性(JSON) - 存储自定义字段数据',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_device_code` (`tenant_id`, `device_code`) COMMENT '租户内设备编号唯一索引',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户ID索引',
  KEY `idx_parent_id` (`parent_id`) COMMENT '父设备ID索引',
  KEY `idx_device_type_id` (`device_type_id`) COMMENT '设备类型ID索引',
  KEY `idx_category_id` (`category_id`) COMMENT '分类ID索引',
  KEY `idx_region_id` (`region_id`) COMMENT '区域ID索引',
  KEY `idx_status` (`status`) COMMENT '设备状态索引',
  KEY `idx_responsible_user_id` (`responsible_user_id`) COMMENT '负责人索引',
  KEY `idx_manufacturer` (`manufacturer`) COMMENT '制造商索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备档案表';

-- 插入示例数据（默认租户ID为1）
INSERT INTO `system_device` (`id`, `tenant_id`, `name`, `device_code`, `parent_id`, `device_type_id`, `category_id`, `region_id`, `sort`, `status`, `responsible_user_id`, `manufacturer`, `model`, `serial_number`, `purchase_date`, `install_date`, `warranty_expire_date`, `description`, `extra_attrs`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(1, 1, '主变压器', 'DEV-001', 0, 1, 1, 1, 1, 0, 1, '西门子', 'SGB10-1000', 'SN123456789', '2023-01-01 00:00:00', '2023-01-15 00:00:00', '2025-01-01 00:00:00', '主变压器设备，用于电力变压', '{"voltage": "10kV", "power": "1000kVA", "frequency": "50Hz"}', '设备运行正常', 'admin', '2023-01-15 10:00:00', 'admin', '2023-01-15 10:00:00', b'0'),
(2, 1, '配电柜A', 'DEV-002', 1, 2, 1, 1, 2, 0, 1, 'ABB', 'MNS-LV', 'SN987654321', '2023-02-01 00:00:00', '2023-02-15 00:00:00', '2025-02-01 00:00:00', '低压配电柜，主变压器下级设备', '{"input_voltage": "380V", "output_voltage": "220V", "capacity": "500A"}', '配电柜运行稳定', 'admin', '2023-02-15 10:00:00', 'admin', '2023-02-15 10:00:00', b'0'),
(3, 1, '开关柜1', 'DEV-003', 2, 3, 1, 1, 3, 0, 2, '施耐德', 'SM6-24kV', 'SN456789123', '2023-03-01 00:00:00', '2023-03-15 00:00:00', '2025-03-01 00:00:00', '中压开关柜', '{"rated_voltage": "24kV", "rated_current": "630A", "breaking_capacity": "20kA"}', '开关柜状态良好', 'admin', '2023-03-15 10:00:00', 'admin', '2023-03-15 10:00:00', b'0');

-- 创建设备状态字典类型
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`) VALUES 
(200, '设备状态', 'device_status', 0, '设备档案状态字典', 'admin', NOW(), 'admin', NOW());

-- 创建设备状态字典数据
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`) VALUES 
(2001, 1, '正常', '0', 'device_status', 0, 'success', '', '设备正常运行', 'admin', NOW(), 'admin', NOW()),
(2002, 2, '停用', '1', 'device_status', 0, 'info', '', '设备已停用', 'admin', NOW(), 'admin', NOW()),
(2003, 3, '维修中', '2', 'device_status', 0, 'warning', '', '设备正在维修', 'admin', NOW(), 'admin', NOW()),
(2004, 4, '报废', '3', 'device_status', 0, 'danger', '', '设备已报废', 'admin', NOW(), 'admin', NOW()); 