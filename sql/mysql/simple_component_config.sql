-- 简化版组件配置表（只存储差异配置）

CREATE TABLE IF NOT EXISTS `component_instances` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_id` varchar(100) NOT NULL COMMENT '实例ID',
  `component_type` varchar(100) NOT NULL COMMENT '组件类型',
  `custom_config` json NULL COMMENT '自定义配置JSON (只存储与默认值不同的部分)',
  `page_id` bigint NULL COMMENT '页面ID',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_instance_id` (`instance_id`),
  INDEX `idx_component_type` (`component_type`),
  INDEX `idx_page_id` (`page_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组件实例表(只存储差异配置)';

-- 示例数据
INSERT INTO `component_instances` (
  `instance_id`, `component_type`, `custom_config`, `page_id`, `sort_order`
) VALUES 
('tree_001', 'SuperTree', '{"theme": "dark", "showCheckbox": true}', 1, 1),
('list_001', 'SuperList', '{"pageSize": 50, "stripe": false}', 1, 2),
('tree_002', 'SuperTree', NULL, 2, 1)  -- 使用默认配置，custom_config为NULL
ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;