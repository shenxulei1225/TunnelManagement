-- 增强版字段分类数据
-- 包含40条常用的字段分类，涵盖各种业务场景
-- 用于隧道管理系统和其他业务系统的字段分类

-- ==================== 清除原有数据 ====================
-- 清除现有的field_category数据（包括分类和字段）
DELETE FROM system_tree_data_rel WHERE tree_type = 'field_category';

-- 清除相关的树配置数据（可选）
-- DELETE FROM system_tree_config WHERE tree_type = 'field_category';

-- 清除相关的数据类型元数据（可选）
-- DELETE FROM system_data_type_meta WHERE data_type IN ('field_category', 'field');

-- 验证清除结果
SELECT '清除前数据统计' as info;
SELECT 
    tree_type,
    data_type,
    COUNT(*) as count
FROM system_tree_data_rel 
WHERE tree_type = 'field_category'
GROUP BY tree_type, data_type
ORDER BY tree_type, data_type;

-- ==================== 插入新数据 ====================
-- 插入40条丰富的字段分类数据
INSERT INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES

-- ==================== 基础信息分类 (1-5) ====================
('field_category', 0, 'field_category', 1, '基础信息', '字段分类', 1, 0, 1, '{"parentId": 0, "level": 1, "code": "basic_info", "description": "基础信息字段分类", "icon": "info-circle"}'),
('field_category', 0, 'field_category', 2, '位置信息', '字段分类', 2, 0, 1, '{"parentId": 0, "level": 1, "code": "location_info", "description": "位置信息字段分类", "icon": "environment"}'),
('field_category', 0, 'field_category', 3, '设备属性', '字段分类', 3, 0, 1, '{"parentId": 0, "level": 1, "code": "device_props", "description": "设备属性字段分类", "icon": "setting"}'),
('field_category', 0, 'field_category', 4, '性能指标', '字段分类', 4, 0, 1, '{"parentId": 0, "level": 1, "code": "performance_metrics", "description": "性能指标字段分类", "icon": "dashboard"}'),
('field_category', 0, 'field_category', 5, '安全信息', '字段分类', 5, 0, 1, '{"parentId": 0, "level": 1, "code": "security_info", "description": "安全信息字段分类", "icon": "safety"}'),

-- ==================== 隧道管理分类 (6-10) ====================
('field_category', 0, 'field_category', 6, '隧道基础', '字段分类', 6, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_basic", "description": "隧道基础信息字段分类", "icon": "tunnel"}'),
('field_category', 0, 'field_category', 7, '隧道结构', '字段分类', 7, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_structure", "description": "隧道结构信息字段分类", "icon": "build"}'),
('field_category', 0, 'field_category', 8, '隧道环境', '字段分类', 8, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_environment", "description": "隧道环境信息字段分类", "icon": "cloud"}'),
('field_category', 0, 'field_category', 9, '隧道维护', '字段分类', 9, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_maintenance", "description": "隧道维护信息字段分类", "icon": "tool"}'),
('field_category', 0, 'field_category', 10, '隧道监控', '字段分类', 10, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_monitoring", "description": "隧道监控信息字段分类", "icon": "monitor"}'),

-- ==================== 设备管理分类 (11-15) ====================
('field_category', 0, 'field_category', 11, '网络设备', '字段分类', 11, 0, 1, '{"parentId": 0, "level": 1, "code": "network_device", "description": "网络设备字段分类", "icon": "wifi"}'),
('field_category', 0, 'field_category', 12, '安全设备', '字段分类', 12, 0, 1, '{"parentId": 0, "level": 1, "code": "security_device", "description": "安全设备字段分类", "icon": "shield"}'),
('field_category', 0, 'field_category', 13, '监控设备', '字段分类', 13, 0, 1, '{"parentId": 0, "level": 1, "code": "monitoring_device", "description": "监控设备字段分类", "icon": "camera"}'),
('field_category', 0, 'field_category', 14, '通信设备', '字段分类', 14, 0, 1, '{"parentId": 0, "level": 1, "code": "communication_device", "description": "通信设备字段分类", "icon": "phone"}'),
('field_category', 0, 'field_category', 15, '照明设备', '字段分类', 15, 0, 1, '{"parentId": 0, "level": 1, "code": "lighting_device", "description": "照明设备字段分类", "icon": "bulb"}'),

-- ==================== 人员管理分类 (16-20) ====================
('field_category', 0, 'field_category', 16, '人员基础', '字段分类', 16, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_basic", "description": "人员基础信息字段分类", "icon": "user"}'),
('field_category', 0, 'field_category', 17, '人员权限', '字段分类', 17, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_permission", "description": "人员权限信息字段分类", "icon": "key"}'),
('field_category', 0, 'field_category', 18, '人员考勤', '字段分类', 18, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_attendance", "description": "人员考勤信息字段分类", "icon": "clock-circle"}'),
('field_category', 0, 'field_category', 19, '人员培训', '字段分类', 19, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_training", "description": "人员培训信息字段分类", "icon": "read"}'),
('field_category', 0, 'field_category', 20, '人员绩效', '字段分类', 20, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_performance", "description": "人员绩效信息字段分类", "icon": "trophy"}'),

-- ==================== 业务管理分类 (21-25) ====================
('field_category', 0, 'field_category', 21, '业务基础', '字段分类', 21, 0, 1, '{"parentId": 0, "level": 1, "code": "business_basic", "description": "业务基础信息字段分类", "icon": "shop"}'),
('field_category', 0, 'field_category', 22, '业务流程', '字段分类', 22, 0, 1, '{"parentId": 0, "level": 1, "code": "business_process", "description": "业务流程信息字段分类", "icon": "deployment-unit"}'),
('field_category', 0, 'field_category', 23, '业务统计', '字段分类', 23, 0, 1, '{"parentId": 0, "level": 1, "code": "business_statistics", "description": "业务统计信息字段分类", "icon": "bar-chart"}'),
('field_category', 0, 'field_category', 24, '业务报表', '字段分类', 24, 0, 1, '{"parentId": 0, "level": 1, "code": "business_report", "description": "业务报表信息字段分类", "icon": "file-text"}'),
('field_category', 0, 'field_category', 25, '业务配置', '字段分类', 25, 0, 1, '{"parentId": 0, "level": 1, "code": "business_config", "description": "业务配置信息字段分类", "icon": "control"}'),

-- ==================== 系统管理分类 (26-30) ====================
('field_category', 0, 'field_category', 26, '系统基础', '字段分类', 26, 0, 1, '{"parentId": 0, "level": 1, "code": "system_basic", "description": "系统基础信息字段分类", "icon": "computer"}'),
('field_category', 0, 'field_category', 27, '系统配置', '字段分类', 27, 0, 1, '{"parentId": 0, "level": 1, "code": "system_config", "description": "系统配置信息字段分类", "icon": "setting"}'),
('field_category', 0, 'field_category', 28, '系统日志', '字段分类', 28, 0, 1, '{"parentId": 0, "level": 1, "code": "system_log", "description": "系统日志信息字段分类", "icon": "file"}'),
('field_category', 0, 'field_category', 29, '系统监控', '字段分类', 29, 0, 1, '{"parentId": 0, "level": 1, "code": "system_monitoring", "description": "系统监控信息字段分类", "icon": "eye"}'),
('field_category', 0, 'field_category', 30, '系统备份', '字段分类', 30, 0, 1, '{"parentId": 0, "level": 1, "code": "system_backup", "description": "系统备份信息字段分类", "icon": "save"}'),

-- ==================== 财务分类 (31-35) ====================
('field_category', 0, 'field_category', 31, '财务基础', '字段分类', 31, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_basic", "description": "财务基础信息字段分类", "icon": "dollar"}'),
('field_category', 0, 'field_category', 32, '财务收入', '字段分类', 32, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_income", "description": "财务收入信息字段分类", "icon": "rise"}'),
('field_category', 0, 'field_category', 33, '财务支出', '字段分类', 33, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_expense", "description": "财务支出信息字段分类", "icon": "fall"}'),
('field_category', 0, 'field_category', 34, '财务预算', '字段分类', 34, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_budget", "description": "财务预算信息字段分类", "icon": "calculator"}'),
('field_category', 0, 'field_category', 35, '财务审计', '字段分类', 35, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_audit", "description": "财务审计信息字段分类", "icon": "audit"}'),

-- ==================== 其他分类 (36-40) ====================
('field_category', 0, 'field_category', 36, '文档管理', '字段分类', 36, 0, 1, '{"parentId": 0, "level": 1, "code": "document_management", "description": "文档管理信息字段分类", "icon": "folder"}'),
('field_category', 0, 'field_category', 37, '通知消息', '字段分类', 37, 0, 1, '{"parentId": 0, "level": 1, "code": "notification_message", "description": "通知消息信息字段分类", "icon": "message"}'),
('field_category', 0, 'field_category', 38, '工作流', '字段分类', 38, 0, 1, '{"parentId": 0, "level": 1, "code": "workflow", "description": "工作流信息字段分类", "icon": "branches"}'),
('field_category', 0, 'field_category', 39, '数据字典', '字段分类', 39, 0, 1, '{"parentId": 0, "level": 1, "code": "data_dictionary", "description": "数据字典信息字段分类", "icon": "book"}'),
('field_category', 0, 'field_category', 40, '扩展字段', '字段分类', 40, 0, 1, '{"parentId": 0, "level": 1, "code": "extended_fields", "description": "扩展字段信息字段分类", "icon": "plus"}');

-- 为每个分类添加示例字段
-- 基础信息分类下的字段
INSERT INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
('field_category', 1, 'field', 101, '名称', '字段定义', 1, 1, 1, '{"parentId": 1, "level": 2, "fieldKey": "name", "fieldType": "string", "description": "名称字段", "validation": "required"}'),
('field_category', 1, 'field', 102, '编码', '字段定义', 2, 1, 1, '{"parentId": 1, "level": 2, "fieldKey": "code", "fieldType": "string", "description": "编码字段", "validation": "required"}'),
('field_category', 1, 'field', 103, '描述', '字段定义', 3, 0, 1, '{"parentId": 1, "level": 2, "fieldKey": "description", "fieldType": "text", "description": "描述字段"}'),
('field_category', 1, 'field', 104, '状态', '字段定义', 4, 1, 1, '{"parentId": 1, "level": 2, "fieldKey": "status", "fieldType": "enum", "description": "状态字段", "options": ["启用", "禁用"]}'),
('field_category', 1, 'field', 105, '创建时间', '字段定义', 5, 0, 1, '{"parentId": 1, "level": 2, "fieldKey": "create_time", "fieldType": "datetime", "description": "创建时间字段"}'),

-- 位置信息分类下的字段
INSERT INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
('field_category', 2, 'field', 201, '省份', '字段定义', 1, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "province", "fieldType": "string", "description": "省份字段"}'),
('field_category', 2, 'field', 202, '城市', '字段定义', 2, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "city", "fieldType": "string", "description": "城市字段"}'),
('field_category', 2, 'field', 203, '区县', '字段定义', 3, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "district", "fieldType": "string", "description": "区县字段"}'),
('field_category', 2, 'field', 204, '详细地址', '字段定义', 4, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "address", "fieldType": "text", "description": "详细地址字段"}'),
('field_category', 2, 'field', 205, '经度', '字段定义', 5, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "longitude", "fieldType": "number", "description": "经度字段"}'),
('field_category', 2, 'field', 206, '纬度', '字段定义', 6, 0, 1, '{"parentId": 2, "level": 2, "fieldKey": "latitude", "fieldType": "number", "description": "纬度字段"}'),

-- 设备属性分类下的字段
INSERT INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
('field_category', 3, 'field', 301, '设备类型', '字段定义', 1, 1, 1, '{"parentId": 3, "level": 2, "fieldKey": "device_type", "fieldType": "enum", "description": "设备类型字段", "options": ["网络设备", "安全设备", "监控设备"]}'),
('field_category', 3, 'field', 302, '设备型号', '字段定义', 2, 0, 1, '{"parentId": 3, "level": 2, "fieldKey": "device_model", "fieldType": "string", "description": "设备型号字段"}'),
('field_category', 3, 'field', 303, '生产厂家', '字段定义', 3, 0, 1, '{"parentId": 3, "level": 2, "fieldKey": "manufacturer", "fieldType": "string", "description": "生产厂家字段"}'),
('field_category', 3, 'field', 304, '序列号', '字段定义', 4, 0, 1, '{"parentId": 3, "level": 2, "fieldKey": "serial_number", "fieldType": "string", "description": "序列号字段"}'),
('field_category', 3, 'field', 305, '购买日期', '字段定义', 5, 0, 1, '{"parentId": 3, "level": 2, "fieldKey": "purchase_date", "fieldType": "date", "description": "购买日期字段"}'),
('field_category', 3, 'field', 306, '保修期', '字段定义', 6, 0, 1, '{"parentId": 3, "level": 2, "fieldKey": "warranty_period", "fieldType": "number", "description": "保修期字段（月）"}'),

-- 性能指标分类下的字段
INSERT INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
('field_category', 4, 'field', 401, 'CPU使用率', '字段定义', 1, 0, 1, '{"parentId": 4, "level": 2, "fieldKey": "cpu_usage", "fieldType": "number", "description": "CPU使用率字段", "unit": "%"}'),
('field_category', 4, 'field', 402, '内存使用率', '字段定义', 2, 0, 1, '{"parentId": 4, "level": 2, "fieldKey": "memory_usage", "fieldType": "number", "description": "内存使用率字段", "unit": "%"}'),
('field_category', 4, 'field', 403, '磁盘使用率', '字段定义', 3, 0, 1, '{"parentId": 4, "level": 2, "fieldKey": "disk_usage", "fieldType": "number", "description": "磁盘使用率字段", "unit": "%"}'),
('field_category', 4, 'field', 404, '网络流量', '字段定义', 4, 0, 1, '{"parentId": 4, "level": 2, "fieldKey": "network_traffic", "fieldType": "number", "description": "网络流量字段", "unit": "MB/s"}'),
('field_category', 4, 'field', 405, '响应时间', '字段定义', 5, 0, 1, '{"parentId": 4, "level": 2, "fieldKey": "response_time", "fieldType": "number", "description": "响应时间字段", "unit": "ms"}'),

-- 安全信息分类下的字段
INSERT INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
('field_category', 5, 'field', 501, '安全等级', '字段定义', 1, 1, 1, '{"parentId": 5, "level": 2, "fieldKey": "security_level", "fieldType": "enum", "description": "安全等级字段", "options": ["低", "中", "高", "极高"]}'),
('field_category', 5, 'field', 502, '访问权限', '字段定义', 2, 0, 1, '{"parentId": 5, "level": 2, "fieldKey": "access_permission", "fieldType": "enum", "description": "访问权限字段", "options": ["公开", "内部", "机密", "绝密"]}'),
('field_category', 5, 'field', 503, '加密方式', '字段定义', 3, 0, 1, '{"parentId": 5, "level": 2, "fieldKey": "encryption_method", "fieldType": "string", "description": "加密方式字段"}'),
('field_category', 5, 'field', 504, '认证方式', '字段定义', 4, 0, 1, '{"parentId": 5, "level": 2, "fieldKey": "auth_method", "fieldType": "string", "description": "认证方式字段"}'),
('field_category', 5, 'field', 505, '安全策略', '字段定义', 5, 0, 1, '{"parentId": 5, "level": 2, "fieldKey": "security_policy", "fieldType": "text", "description": "安全策略字段"}');

-- 验证数据插入成功
SELECT 
    tree_type,
    data_type,
    COUNT(*) as count
FROM system_tree_data_rel 
WHERE tree_type = 'field_category'
GROUP BY tree_type, data_type
ORDER BY tree_type, data_type; 