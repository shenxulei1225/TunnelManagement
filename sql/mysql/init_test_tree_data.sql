-- 初始化测试树数据
-- 确保system_tree_data_rel表中有field_category的测试数据

-- 检查表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS `system_tree_data_rel` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系ID',
    `tree_type` varchar(100) NOT NULL COMMENT '树类型',
    `tree_node_id` bigint DEFAULT NULL COMMENT '树节点ID',
    `data_type` varchar(100) NOT NULL COMMENT '数据类型',
    `data_id` bigint NOT NULL COMMENT '数据ID',
    `data_name` varchar(100) NOT NULL COMMENT '数据名称',
    `data_type_label` varchar(100) DEFAULT NULL COMMENT '数据类型标签',
    `display_order` int DEFAULT 0 COMMENT '显示顺序',
    `is_required` tinyint(1) DEFAULT 0 COMMENT '是否必填',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态',
    `metadata` text COMMENT '元数据（JSON格式）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_tree_type` (`tree_type`),
    KEY `idx_tree_node_id` (`tree_node_id`),
    KEY `idx_data_type` (`data_type`),
    KEY `idx_data_id` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='树数据关系表';

-- 清空现有的field_category数据
DELETE FROM system_tree_data_rel WHERE tree_type = 'field_category';

-- 插入测试数据
INSERT INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
-- 根级分类
('field_category', 0, 'field_category', 1, '基础信息', '字段分类', 1, 0, 1, '{"parentId": 0, "level": 1, "code": "basic_info", "description": "基础信息字段分类"}'),
('field_category', 0, 'field_category', 2, '位置信息', '字段分类', 2, 0, 1, '{"parentId": 0, "level": 1, "code": "location_info", "description": "位置信息字段分类"}'),
('field_category', 0, 'field_category', 3, '设备属性', '字段分类', 3, 0, 1, '{"parentId": 0, "level": 1, "code": "device_props", "description": "设备属性字段分类"}'),

-- 基础信息分类下的字段
('field_category', 1, 'field', 11, '设备名称', '字段定义', 1, 1, 1, '{"parentId": 1, "level": 2, "fieldKey": "device_name", "fieldType": "string", "description": "设备名称字段"}'),
('field_category', 1, 'field', 12, '设备编号', '字段定义', 2, 1, 1, '{"parentId": 1, "level": 2, "fieldKey": "device_code", "fieldType": "string", "description": "设备编号字段"}'),
('field_category', 1, 'field', 13, '设备类型', '字段定义', 3, 0, 1, '{"parentId": 1, "level": 2, "fieldKey": "device_type", "fieldType": "enum", "description": "设备类型字段"}'),

-- 位置信息分类下的字段
('field_category', 2, 'field', 21, '所在区域', '字段定义', 1, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "region", "fieldType": "string", "description": "所在区域字段"}'),
('field_category', 2, 'field', 22, '具体位置', '字段定义', 2, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "location", "fieldType": "string", "description": "具体位置字段"}'),
('field_category', 2, 'field', 23, '坐标信息', '字段定义', 3, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "coordinates", "fieldType": "object", "description": "坐标信息字段"}'),

-- 设备属性分类下的字段
('field_category', 3, 'field', 31, '设备状态', '字段定义', 1, 0, 1, '{"parentId": 3, "level": 2, "fieldKey": "device_status", "fieldType": "enum", "description": "设备状态字段"}'),
('field_category', 3, 'field', 32, '设备型号', '字段定义', 2, 0, 1, '{"parentId": 3, "level": 2, "fieldKey": "device_model", "fieldType": "string", "description": "设备型号字段"}'),
('field_category', 3, 'field', 33, '生产厂家', '字段定义', 3, 0, 1, '{"parentId": 3, "level": 2, "fieldKey": "manufacturer", "fieldType": "string", "description": "生产厂家字段"}');

-- 验证数据插入成功
SELECT 
    tree_type,
    data_type,
    COUNT(*) as count
FROM system_tree_data_rel 
WHERE tree_type = 'field_category'
GROUP BY tree_type, data_type
ORDER BY tree_type, data_type; 