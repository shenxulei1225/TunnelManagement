-- 初始化通用树数据（修复版本）
-- 用于测试动态树数据管理系统
-- 使用 INSERT IGNORE 避免重复插入错误

-- 插入树配置数据
INSERT IGNORE INTO system_tree_config (tree_type, tree_name, description, allowed_data_types, max_level, node_name_label, node_code_label, sort_type, status, config_json) VALUES
('field_category', '字段分类树', '用于管理字段分类的树形结构', '["field_category", "field"]', 3, '分类名称', '分类编码', 1, 1, '{"allowDrag": true, "allowDrop": true}'),
('hierarchy_group', '分级组树', '用于管理分级组的树形结构', '["hierarchy_group", "group"]', 5, '组名称', '组编码', 1, 1, '{"allowDrag": true, "allowDrop": true}'),
('device_tree', '设备树', '用于管理设备的树形结构', '["device_category", "device"]', 4, '设备名称', '设备编码', 1, 1, '{"allowDrag": true, "allowDrop": true}');

-- 插入数据类型元数据
INSERT IGNORE INTO system_data_type_meta (data_type, data_name, description, icon, color, table_name, id_field, name_field, status_field, config_json, status) VALUES
('field_category', '字段分类', '字段分类类型', 'folder', '#1890ff', 'system_field_category', 'id', 'name', 'status', '{"icon": "folder", "color": "#1890ff"}', 1),
('field', '字段定义', '字段定义类型', 'field', '#52c41a', 'system_field_def', 'id', 'field_label', 'status', '{"icon": "field", "color": "#52c41a"}', 1),
('hierarchy_group', '分级组', '分级组类型', 'team', '#722ed1', 'system_hierarchy_group', 'id', 'name', 'status', '{"icon": "team", "color": "#722ed1"}', 1),
('device_category', '设备分类', '设备分类类型', 'cluster', '#fa8c16', 'system_device_category', 'id', 'name', 'status', '{"icon": "cluster", "color": "#fa8c16"}', 1),
('device', '设备', '设备类型', 'desktop', '#eb2f96', 'system_device', 'id', 'name', 'status', '{"icon": "desktop", "color": "#eb2f96"}', 1);

-- 清空现有的树数据（可选，用于重新初始化）
-- DELETE FROM system_tree_data_rel WHERE tree_type IN ('field_category', 'hierarchy_group', 'device_tree');

-- 插入字段分类树数据
INSERT IGNORE INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
-- 根级分类
('field_category', 0, 'field_category', 1, '基础信息', '字段分类', 1, false, 1, '{"parentId": 0, "level": 1, "code": "basic_info", "description": "基础信息字段分类"}'),
('field_category', 0, 'field_category', 2, '位置信息', '字段分类', 2, false, 1, '{"parentId": 0, "level": 1, "code": "location_info", "description": "位置信息字段分类"}'),
('field_category', 0, 'field_category', 3, '设备属性', '字段分类', 3, false, 1, '{"parentId": 0, "level": 1, "code": "device_props", "description": "设备属性字段分类"}'),

-- 基础信息分类下的字段
('field_category', 1, 'field', 11, '设备名称', '字段定义', 1, true, 1, '{"parentId": 1, "level": 2, "fieldKey": "device_name", "fieldType": "string", "description": "设备名称字段"}'),
('field_category', 1, 'field', 12, '设备编号', '字段定义', 2, true, 1, '{"parentId": 1, "level": 2, "fieldKey": "device_code", "fieldType": "string", "description": "设备编号字段"}'),
('field_category', 1, 'field', 13, '设备类型', '字段定义', 3, false, 1, '{"parentId": 1, "level": 2, "fieldKey": "device_type", "fieldType": "enum", "description": "设备类型字段"}'),

-- 位置信息分类下的字段
('field_category', 2, 'field', 21, '所在区域', '字段定义', 1, false, 1, '{"parentId": 2, "level": 2, "fieldKey": "region", "fieldType": "string", "description": "所在区域字段"}'),
('field_category', 2, 'field', 22, '具体位置', '字段定义', 2, false, 1, '{"parentId": 2, "level": 2, "fieldKey": "location", "fieldType": "string", "description": "具体位置字段"}'),
('field_category', 2, 'field', 23, '坐标信息', '字段定义', 3, false, 1, '{"parentId": 2, "level": 2, "fieldKey": "coordinates", "fieldType": "object", "description": "坐标信息字段"}'),

-- 设备属性分类下的字段
('field_category', 3, 'field', 31, '设备状态', '字段定义', 1, false, 1, '{"parentId": 3, "level": 2, "fieldKey": "device_status", "fieldType": "enum", "description": "设备状态字段"}'),
('field_category', 3, 'field', 32, '设备型号', '字段定义', 2, false, 1, '{"parentId": 3, "level": 2, "fieldKey": "device_model", "fieldType": "string", "description": "设备型号字段"}'),
('field_category', 3, 'field', 33, '生产厂家', '字段定义', 3, false, 1, '{"parentId": 3, "level": 2, "fieldKey": "manufacturer", "fieldType": "string", "description": "生产厂家字段"}');

-- 插入分级组树数据
INSERT IGNORE INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
-- 根级分组
('hierarchy_group', 0, 'hierarchy_group', 101, '总公司', '分级组', 1, false, 1, '{"parentId": 0, "level": 1, "code": "headquarters", "description": "总公司"}'),
('hierarchy_group', 0, 'hierarchy_group', 102, '分公司', '分级组', 2, false, 1, '{"parentId": 0, "level": 1, "code": "branch", "description": "分公司"}'),

-- 总公司下的子分组
('hierarchy_group', 101, 'hierarchy_group', 1011, '技术部', '分级组', 1, false, 1, '{"parentId": 101, "level": 2, "code": "tech_dept", "description": "技术部"}'),
('hierarchy_group', 101, 'hierarchy_group', 1012, '运营部', '分级组', 2, false, 1, '{"parentId": 101, "level": 2, "code": "ops_dept", "description": "运营部"}'),

-- 分公司下的子分组
('hierarchy_group', 102, 'hierarchy_group', 1021, '区域A', '分级组', 1, false, 1, '{"parentId": 102, "level": 2, "code": "region_a", "description": "区域A"}'),
('hierarchy_group', 102, 'hierarchy_group', 1022, '区域B', '分级组', 2, false, 1, '{"parentId": 102, "level": 2, "code": "region_b", "description": "区域B"}');

-- 插入设备树数据
INSERT IGNORE INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
-- 根级设备分类
('device_tree', 0, 'device_category', 201, '网络设备', '设备分类', 1, false, 1, '{"parentId": 0, "level": 1, "code": "network_device", "description": "网络设备分类"}'),
('device_tree', 0, 'device_category', 202, '安全设备', '设备分类', 2, false, 1, '{"parentId": 0, "level": 1, "code": "security_device", "description": "安全设备分类"}'),

-- 网络设备分类下的设备
('device_tree', 201, 'device', 2011, '路由器', '设备', 1, false, 1, '{"parentId": 201, "level": 2, "code": "router", "description": "路由器设备"}'),
('device_tree', 201, 'device', 2012, '交换机', '设备', 2, false, 1, '{"parentId": 201, "level": 2, "code": "switch", "description": "交换机设备"}'),

-- 安全设备分类下的设备
('device_tree', 202, 'device', 2021, '防火墙', '设备', 1, false, 1, '{"parentId": 202, "level": 2, "code": "firewall", "description": "防火墙设备"}'),
('device_tree', 202, 'device', 2022, '入侵检测', '设备', 2, false, 1, '{"parentId": 202, "level": 2, "code": "ids", "description": "入侵检测设备"}'); 