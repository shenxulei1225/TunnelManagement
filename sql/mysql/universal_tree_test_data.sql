-- 通用树测试数据 - 40个字段分类
-- 支持多种树类型的测试数据，不绑定特定业务

-- ==================== 清除原有数据 ====================
DELETE FROM system_tree_data_rel WHERE tree_type = 'field_category';

-- ==================== 插入40个字段分类 ====================
INSERT INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata, tenant_id) VALUES

-- 基础信息分类 (1-5)
('field_category', 0, 'field_category', 1, '基础信息', '字段分类', 1, 0, 1, '{"parentId": 0, "level": 1, "code": "basic_info", "description": "基础信息字段分类", "icon": "info-circle"}', 1),
('field_category', 0, 'field_category', 2, '位置信息', '字段分类', 2, 0, 1, '{"parentId": 0, "level": 1, "code": "location_info", "description": "位置信息字段分类", "icon": "environment"}', 1),
('field_category', 0, 'field_category', 3, '设备属性', '字段分类', 3, 0, 1, '{"parentId": 0, "level": 1, "code": "device_props", "description": "设备属性字段分类", "icon": "setting"}', 1),
('field_category', 0, 'field_category', 4, '性能指标', '字段分类', 4, 0, 1, '{"parentId": 0, "level": 1, "code": "performance_metrics", "description": "性能指标字段分类", "icon": "dashboard"}', 1),
('field_category', 0, 'field_category', 5, '安全信息', '字段分类', 5, 0, 1, '{"parentId": 0, "level": 1, "code": "security_info", "description": "安全信息字段分类", "icon": "safety"}', 1),

-- 隧道管理分类 (6-10)
('field_category', 0, 'field_category', 6, '隧道基础', '字段分类', 6, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_basic", "description": "隧道基础信息字段分类", "icon": "tunnel"}', 1),
('field_category', 0, 'field_category', 7, '隧道结构', '字段分类', 7, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_structure", "description": "隧道结构信息字段分类", "icon": "build"}', 1),
('field_category', 0, 'field_category', 8, '隧道环境', '字段分类', 8, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_environment", "description": "隧道环境信息字段分类", "icon": "cloud"}', 1),
('field_category', 0, 'field_category', 9, '隧道维护', '字段分类', 9, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_maintenance", "description": "隧道维护信息字段分类", "icon": "tool"}', 1),
('field_category', 0, 'field_category', 10, '隧道监控', '字段分类', 10, 0, 1, '{"parentId": 0, "level": 1, "code": "tunnel_monitoring", "description": "隧道监控信息字段分类", "icon": "monitor"}', 1),

-- 设备管理分类 (11-15)
('field_category', 0, 'field_category', 11, '网络设备', '字段分类', 11, 0, 1, '{"parentId": 0, "level": 1, "code": "network_device", "description": "网络设备字段分类", "icon": "wifi"}', 1),
('field_category', 0, 'field_category', 12, '安全设备', '字段分类', 12, 0, 1, '{"parentId": 0, "level": 1, "code": "security_device", "description": "安全设备字段分类", "icon": "shield"}', 1),
('field_category', 0, 'field_category', 13, '监控设备', '字段分类', 13, 0, 1, '{"parentId": 0, "level": 1, "code": "monitoring_device", "description": "监控设备字段分类", "icon": "camera"}', 1),
('field_category', 0, 'field_category', 14, '通信设备', '字段分类', 14, 0, 1, '{"parentId": 0, "level": 1, "code": "communication_device", "description": "通信设备字段分类", "icon": "phone"}', 1),
('field_category', 0, 'field_category', 15, '照明设备', '字段分类', 15, 0, 1, '{"parentId": 0, "level": 1, "code": "lighting_device", "description": "照明设备字段分类", "icon": "bulb"}', 1),

-- 人员管理分类 (16-20)
('field_category', 0, 'field_category', 16, '人员基础', '字段分类', 16, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_basic", "description": "人员基础信息字段分类", "icon": "user"}', 1),
('field_category', 0, 'field_category', 17, '人员权限', '字段分类', 17, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_permission", "description": "人员权限信息字段分类", "icon": "key"}', 1),
('field_category', 0, 'field_category', 18, '人员考勤', '字段分类', 18, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_attendance", "description": "人员考勤信息字段分类", "icon": "clock-circle"}', 1),
('field_category', 0, 'field_category', 19, '人员培训', '字段分类', 19, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_training", "description": "人员培训信息字段分类", "icon": "read"}', 1),
('field_category', 0, 'field_category', 20, '人员绩效', '字段分类', 20, 0, 1, '{"parentId": 0, "level": 1, "code": "personnel_performance", "description": "人员绩效信息字段分类", "icon": "trophy"}', 1),

-- 业务管理分类 (21-25)
('field_category', 0, 'field_category', 21, '业务基础', '字段分类', 21, 0, 1, '{"parentId": 0, "level": 1, "code": "business_basic", "description": "业务基础信息字段分类", "icon": "shop"}', 1),
('field_category', 0, 'field_category', 22, '业务流程', '字段分类', 22, 0, 1, '{"parentId": 0, "level": 1, "code": "business_process", "description": "业务流程信息字段分类", "icon": "deployment-unit"}', 1),
('field_category', 0, 'field_category', 23, '业务统计', '字段分类', 23, 0, 1, '{"parentId": 0, "level": 1, "code": "business_statistics", "description": "业务统计信息字段分类", "icon": "bar-chart"}', 1),
('field_category', 0, 'field_category', 24, '业务报表', '字段分类', 24, 0, 1, '{"parentId": 0, "level": 1, "code": "business_report", "description": "业务报表信息字段分类", "icon": "file-text"}', 1),
('field_category', 0, 'field_category', 25, '业务配置', '字段分类', 25, 0, 1, '{"parentId": 0, "level": 1, "code": "business_config", "description": "业务配置信息字段分类", "icon": "control"}', 1),

-- 系统管理分类 (26-30)
('field_category', 0, 'field_category', 26, '系统基础', '字段分类', 26, 0, 1, '{"parentId": 0, "level": 1, "code": "system_basic", "description": "系统基础信息字段分类", "icon": "computer"}', 1),
('field_category', 0, 'field_category', 27, '系统配置', '字段分类', 27, 0, 1, '{"parentId": 0, "level": 1, "code": "system_config", "description": "系统配置信息字段分类", "icon": "setting"}', 1),
('field_category', 0, 'field_category', 28, '系统日志', '字段分类', 28, 0, 1, '{"parentId": 0, "level": 1, "code": "system_log", "description": "系统日志信息字段分类", "icon": "file"}', 1),
('field_category', 0, 'field_category', 29, '系统监控', '字段分类', 29, 0, 1, '{"parentId": 0, "level": 1, "code": "system_monitoring", "description": "系统监控信息字段分类", "icon": "eye"}', 1),
('field_category', 0, 'field_category', 30, '系统备份', '字段分类', 30, 0, 1, '{"parentId": 0, "level": 1, "code": "system_backup", "description": "系统备份信息字段分类", "icon": "save"}', 1),

-- 财务分类 (31-35)
('field_category', 0, 'field_category', 31, '财务基础', '字段分类', 31, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_basic", "description": "财务基础信息字段分类", "icon": "dollar"}', 1),
('field_category', 0, 'field_category', 32, '财务收入', '字段分类', 32, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_income", "description": "财务收入信息字段分类", "icon": "rise"}', 1),
('field_category', 0, 'field_category', 33, '财务支出', '字段分类', 33, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_expense", "description": "财务支出信息字段分类", "icon": "fall"}', 1),
('field_category', 0, 'field_category', 34, '财务预算', '字段分类', 34, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_budget", "description": "财务预算信息字段分类", "icon": "calculator"}', 1),
('field_category', 0, 'field_category', 35, '财务审计', '字段分类', 35, 0, 1, '{"parentId": 0, "level": 1, "code": "finance_audit", "description": "财务审计信息字段分类", "icon": "audit"}', 1),

-- 其他分类 (36-40)
('field_category', 0, 'field_category', 36, '文档管理', '字段分类', 36, 0, 1, '{"parentId": 0, "level": 1, "code": "document_management", "description": "文档管理信息字段分类", "icon": "folder"}', 1),
('field_category', 0, 'field_category', 37, '通知消息', '字段分类', 37, 0, 1, '{"parentId": 0, "level": 1, "code": "notification_message", "description": "通知消息信息字段分类", "icon": "message"}', 1),
('field_category', 0, 'field_category', 38, '工作流', '字段分类', 38, 0, 1, '{"parentId": 0, "level": 1, "code": "workflow", "description": "工作流信息字段分类", "icon": "branches"}', 1),
('field_category', 0, 'field_category', 39, '数据字典', '字段分类', 39, 0, 1, '{"parentId": 0, "level": 1, "code": "data_dictionary", "description": "数据字典信息字段分类", "icon": "book"}', 1),
('field_category', 0, 'field_category', 40, '扩展字段', '字段分类', 40, 0, 1, '{"parentId": 0, "level": 1, "code": "extended_fields", "description": "扩展字段信息字段分类", "icon": "plus"}', 1);

-- 验证数据插入成功
SELECT 
    tree_type,
    data_type,
    COUNT(*) as count
FROM system_tree_data_rel 
WHERE tree_type = 'field_category'
GROUP BY tree_type, data_type
ORDER BY tree_type, data_type; 