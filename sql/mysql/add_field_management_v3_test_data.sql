-- 为FieldManagementV3测试页面增加40条树结构数据
-- 用于测试滚动功能和完整工作流验证

-- 清空现有的测试数据（可选）
-- DELETE FROM system_tree_data_rel WHERE tree_type = 'field_category' AND data_id >= 1000;

-- 插入40条字段分类树数据（用于V3测试页面）
INSERT IGNORE INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
-- 主分类 1-10
('field_category', 0, 'field_category', 1001, '基础设施管理', '字段分类', 1, false, 1, '{"parentId": 0, "level": 1, "code": "infrastructure", "description": "基础设施管理分类"}'),
('field_category', 0, 'field_category', 1002, '网络通信系统', '字段分类', 2, false, 1, '{"parentId": 0, "level": 1, "code": "network", "description": "网络通信系统分类"}'),
('field_category', 0, 'field_category', 1003, '安全防护体系', '字段分类', 3, false, 1, '{"parentId": 0, "level": 1, "code": "security", "description": "安全防护体系分类"}'),
('field_category', 0, 'field_category', 1004, '监控检测设备', '字段分类', 4, false, 1, '{"parentId": 0, "level": 1, "code": "monitoring", "description": "监控检测设备分类"}'),
('field_category', 0, 'field_category', 1005, '环境控制系统', '字段分类', 5, false, 1, '{"parentId": 0, "level": 1, "code": "environment", "description": "环境控制系统分类"}'),
('field_category', 0, 'field_category', 1006, '电力供应系统', '字段分类', 6, false, 1, '{"parentId": 0, "level": 1, "code": "power", "description": "电力供应系统分类"}'),
('field_category', 0, 'field_category', 1007, '通风排烟系统', '字段分类', 7, false, 1, '{"parentId": 0, "level": 1, "code": "ventilation", "description": "通风排烟系统分类"}'),
('field_category', 0, 'field_category', 1008, '给排水系统', '字段分类', 8, false, 1, '{"parentId": 0, "level": 1, "code": "water", "description": "给排水系统分类"}'),
('field_category', 0, 'field_category', 1009, '消防灭火系统', '字段分类', 9, false, 1, '{"parentId": 0, "level": 1, "code": "fire", "description": "消防灭火系统分类"}'),
('field_category', 0, 'field_category', 1010, '应急处理系统', '字段分类', 10, false, 1, '{"parentId": 0, "level": 1, "code": "emergency", "description": "应急处理系统分类"}'),

-- 基础设施管理子分类
('field_category', 1001, 'field_category', 10011, '隧道结构', '字段分类', 1, false, 1, '{"parentId": 1001, "level": 2, "code": "tunnel_structure", "description": "隧道结构子分类"}'),
('field_category', 1001, 'field_category', 10012, '桥梁结构', '字段分类', 2, false, 1, '{"parentId": 1001, "level": 2, "code": "bridge_structure", "description": "桥梁结构子分类"}'),
('field_category', 1001, 'field_category', 10013, '道路设施', '字段分类', 3, false, 1, '{"parentId": 1001, "level": 2, "code": "road_facility", "description": "道路设施子分类"}'),

-- 网络通信系统子分类
('field_category', 1002, 'field_category', 10021, '通信设备', '字段分类', 1, false, 1, '{"parentId": 1002, "level": 2, "code": "communication_device", "description": "通信设备子分类"}'),
('field_category', 1002, 'field_category', 10022, '数据传输', '字段分类', 2, false, 1, '{"parentId": 1002, "level": 2, "code": "data_transmission", "description": "数据传输子分类"}'),
('field_category', 1002, 'field_category', 10023, '信号系统', '字段分类', 3, false, 1, '{"parentId": 1002, "level": 2, "code": "signal_system", "description": "信号系统子分类"}'),

-- 安全防护体系子分类
('field_category', 1003, 'field_category', 10031, '门禁系统', '字段分类', 1, false, 1, '{"parentId": 1003, "level": 2, "code": "access_control", "description": "门禁系统子分类"}'),
('field_category', 1003, 'field_category', 10032, '视频监控', '字段分类', 2, false, 1, '{"parentId": 1003, "level": 2, "code": "video_surveillance", "description": "视频监控子分类"}'),
('field_category', 1003, 'field_category', 10033, '报警系统', '字段分类', 3, false, 1, '{"parentId": 1003, "level": 2, "code": "alarm_system", "description": "报警系统子分类"}'),

-- 监控检测设备子分类
('field_category', 1004, 'field_category', 10041, '传感器设备', '字段分类', 1, false, 1, '{"parentId": 1004, "level": 2, "code": "sensor_device", "description": "传感器设备子分类"}'),
('field_category', 1004, 'field_category', 10042, '检测仪器', '字段分类', 2, false, 1, '{"parentId": 1004, "level": 2, "code": "detection_instrument", "description": "检测仪器子分类"}'),
('field_category', 1004, 'field_category', 10043, '分析设备', '字段分类', 3, false, 1, '{"parentId": 1004, "level": 2, "code": "analysis_device", "description": "分析设备子分类"}'),

-- 环境控制系统子分类
('field_category', 1005, 'field_category', 10051, '温湿度控制', '字段分类', 1, false, 1, '{"parentId": 1005, "level": 2, "code": "temp_humidity", "description": "温湿度控制子分类"}'),
('field_category', 1005, 'field_category', 10052, '空气质量', '字段分类', 2, false, 1, '{"parentId": 1005, "level": 2, "code": "air_quality", "description": "空气质量子分类"}'),
('field_category', 1005, 'field_category', 10053, '照明系统', '字段分类', 3, false, 1, '{"parentId": 1005, "level": 2, "code": "lighting_system", "description": "照明系统子分类"}'),

-- 电力供应系统子分类
('field_category', 1006, 'field_category', 10061, '主配电系统', '字段分类', 1, false, 1, '{"parentId": 1006, "level": 2, "code": "main_power", "description": "主配电系统子分类"}'),
('field_category', 1006, 'field_category', 10062, '应急电源', '字段分类', 2, false, 1, '{"parentId": 1006, "level": 2, "code": "emergency_power", "description": "应急电源子分类"}'),
('field_category', 1006, 'field_category', 10063, 'UPS系统', '字段分类', 3, false, 1, '{"parentId": 1006, "level": 2, "code": "ups_system", "description": "UPS系统子分类"}'),

-- 通风排烟系统子分类
('field_category', 1007, 'field_category', 10071, '通风设备', '字段分类', 1, false, 1, '{"parentId": 1007, "level": 2, "code": "ventilation_device", "description": "通风设备子分类"}'),
('field_category', 1007, 'field_category', 10072, '排烟设备', '字段分类', 2, false, 1, '{"parentId": 1007, "level": 2, "code": "smoke_exhaust", "description": "排烟设备子分类"}'),
('field_category', 1007, 'field_category', 10073, '风管系统', '字段分类', 3, false, 1, '{"parentId": 1007, "level": 2, "code": "duct_system", "description": "风管系统子分类"}'),

-- 给排水系统子分类
('field_category', 1008, 'field_category', 10081, '给水设备', '字段分类', 1, false, 1, '{"parentId": 1008, "level": 2, "code": "water_supply", "description": "给水设备子分类"}'),
('field_category', 1008, 'field_category', 10082, '排水设备', '字段分类', 2, false, 1, '{"parentId": 1008, "level": 2, "code": "drainage_device", "description": "排水设备子分类"}'),
('field_category', 1008, 'field_category', 10083, '管道系统', '字段分类', 3, false, 1, '{"parentId": 1008, "level": 2, "code": "pipeline_system", "description": "管道系统子分类"}'),

-- 消防灭火系统子分类
('field_category', 1009, 'field_category', 10091, '消防栓系统', '字段分类', 1, false, 1, '{"parentId": 1009, "level": 2, "code": "fire_hydrant", "description": "消防栓系统子分类"}'),
('field_category', 1009, 'field_category', 10092, '喷淋系统', '字段分类', 2, false, 1, '{"parentId": 1009, "level": 2, "code": "sprinkler_system", "description": "喷淋系统子分类"}'),
('field_category', 1009, 'field_category', 10093, '灭火器', '字段分类', 3, false, 1, '{"parentId": 1009, "level": 2, "code": "fire_extinguisher", "description": "灭火器子分类"}'),

-- 应急处理系统子分类
('field_category', 1010, 'field_category', 10101, '应急照明', '字段分类', 1, false, 1, '{"parentId": 1010, "level": 2, "code": "emergency_lighting", "description": "应急照明子分类"}'),
('field_category', 1010, 'field_category', 10102, '疏散指示', '字段分类', 2, false, 1, '{"parentId": 1010, "level": 2, "code": "evacuation_guide", "description": "疏散指示子分类"}'),
('field_category', 1010, 'field_category', 10103, '应急广播', '字段分类', 3, false, 1, '{"parentId": 1010, "level": 2, "code": "emergency_broadcast", "description": "应急广播子分类"}');

-- 插入对应的字段定义数据（为每个分类添加3-5个字段）
INSERT IGNORE INTO system_tree_data_rel (tree_type, tree_node_id, data_type, data_id, data_name, data_type_label, display_order, is_required, status, metadata) VALUES
-- 基础设施管理字段
('field_category', 1001, 'field', 2001, '结构类型', '字段定义', 1, true, 1, '{"parentId": 1001, "level": 2, "fieldKey": "structure_type", "fieldType": "enum", "description": "结构类型字段"}'),
('field_category', 1001, 'field', 2002, '建设年代', '字段定义', 2, false, 1, '{"parentId": 1001, "level": 2, "fieldKey": "construction_year", "fieldType": "number", "description": "建设年代字段"}'),
('field_category', 1001, 'field', 2003, '设计标准', '字段定义', 3, false, 1, '{"parentId": 1001, "level": 2, "fieldKey": "design_standard", "fieldType": "string", "description": "设计标准字段"}'),

-- 网络通信系统字段
('field_category', 1002, 'field', 2004, '网络类型', '字段定义', 1, true, 1, '{"parentId": 1002, "level": 2, "fieldKey": "network_type", "fieldType": "enum", "description": "网络类型字段"}'),
('field_category', 1002, 'field', 2005, '带宽容量', '字段定义', 2, false, 1, '{"parentId": 1002, "level": 2, "fieldKey": "bandwidth_capacity", "fieldType": "number", "description": "带宽容量字段"}'),
('field_category', 1002, 'field', 2006, '通信协议', '字段定义', 3, false, 1, '{"parentId": 1002, "level": 2, "fieldKey": "communication_protocol", "fieldType": "string", "description": "通信协议字段"}'),

-- 安全防护体系字段
('field_category', 1003, 'field', 2007, '安全等级', '字段定义', 1, true, 1, '{"parentId": 1003, "level": 2, "fieldKey": "security_level", "fieldType": "enum", "description": "安全等级字段"}'),
('field_category', 1003, 'field', 2008, '防护类型', '字段定义', 2, false, 1, '{"parentId": 1003, "level": 2, "fieldKey": "protection_type", "fieldType": "string", "description": "防护类型字段"}'),
('field_category', 1003, 'field', 2009, '监控范围', '字段定义', 3, false, 1, '{"parentId": 1003, "level": 2, "fieldKey": "monitoring_scope", "fieldType": "string", "description": "监控范围字段"}'),

-- 监控检测设备字段
('field_category', 1004, 'field', 2010, '检测精度', '字段定义', 1, false, 1, '{"parentId": 1004, "level": 2, "fieldKey": "detection_accuracy", "fieldType": "number", "description": "检测精度字段"}'),
('field_category', 1004, 'field', 2011, '检测范围', '字段定义', 2, false, 1, '{"parentId": 1004, "level": 2, "fieldKey": "detection_range", "fieldType": "string", "description": "检测范围字段"}'),
('field_category', 1004, 'field', 2012, '校准周期', '字段定义', 3, false, 1, '{"parentId": 1004, "level": 2, "fieldKey": "calibration_cycle", "fieldType": "number", "description": "校准周期字段"}'),

-- 环境控制系统字段
('field_category', 1005, 'field', 2013, '温度范围', '字段定义', 1, false, 1, '{"parentId": 1005, "level": 2, "fieldKey": "temperature_range", "fieldType": "string", "description": "温度范围字段"}'),
('field_category', 1005, 'field', 2014, '湿度范围', '字段定义', 2, false, 1, '{"parentId": 1005, "level": 2, "fieldKey": "humidity_range", "fieldType": "string", "description": "湿度范围字段"}'),
('field_category', 1005, 'field', 2015, '空气质量标准', '字段定义', 3, false, 1, '{"parentId": 1005, "level": 2, "fieldKey": "air_quality_standard", "fieldType": "string", "description": "空气质量标准字段"}'),

-- 电力供应系统字段
('field_category', 1006, 'field', 2016, '额定功率', '字段定义', 1, true, 1, '{"parentId": 1006, "level": 2, "fieldKey": "rated_power", "fieldType": "number", "description": "额定功率字段"}'),
('field_category', 1006, 'field', 2017, '电压等级', '字段定义', 2, true, 1, '{"parentId": 1006, "level": 2, "fieldKey": "voltage_level", "fieldType": "enum", "description": "电压等级字段"}'),
('field_category', 1006, 'field', 2018, '供电方式', '字段定义', 3, false, 1, '{"parentId": 1006, "level": 2, "fieldKey": "power_supply_mode", "fieldType": "enum", "description": "供电方式字段"}'),

-- 通风排烟系统字段
('field_category', 1007, 'field', 2019, '风量参数', '字段定义', 1, false, 1, '{"parentId": 1007, "level": 2, "fieldKey": "air_flow_parameter", "fieldType": "number", "description": "风量参数字段"}'),
('field_category', 1007, 'field', 2020, '风压参数', '字段定义', 2, false, 1, '{"parentId": 1007, "level": 2, "fieldKey": "air_pressure_parameter", "fieldType": "number", "description": "风压参数字段"}'),
('field_category', 1007, 'field', 2021, '噪音控制', '字段定义', 3, false, 1, '{"parentId": 1007, "level": 2, "fieldKey": "noise_control", "fieldType": "string", "description": "噪音控制字段"}'),

-- 给排水系统字段
('field_category', 1008, 'field', 2022, '水压参数', '字段定义', 1, false, 1, '{"parentId": 1008, "level": 2, "fieldKey": "water_pressure_parameter", "fieldType": "number", "description": "水压参数字段"}'),
('field_category', 1008, 'field', 2023, '流量参数', '字段定义', 2, false, 1, '{"parentId": 1008, "level": 2, "fieldKey": "flow_parameter", "fieldType": "number", "description": "流量参数字段"}'),
('field_category', 1008, 'field', 2024, '水质要求', '字段定义', 3, false, 1, '{"parentId": 1008, "level": 2, "fieldKey": "water_quality_requirement", "fieldType": "string", "description": "水质要求字段"}'),

-- 消防灭火系统字段
('field_category', 1009, 'field', 2025, '灭火剂类型', '字段定义', 1, true, 1, '{"parentId": 1009, "level": 2, "fieldKey": "extinguishing_agent_type", "fieldType": "enum", "description": "灭火剂类型字段"}'),
('field_category', 1009, 'field', 2026, '覆盖范围', '字段定义', 2, false, 1, '{"parentId": 1009, "level": 2, "fieldKey": "coverage_range", "fieldType": "string", "description": "覆盖范围字段"}'),
('field_category', 1009, 'field', 2027, '响应时间', '字段定义', 3, false, 1, '{"parentId": 1009, "level": 2, "fieldKey": "response_time", "fieldType": "number", "description": "响应时间字段"}'),

-- 应急处理系统字段
('field_category', 1010, 'field', 2028, '应急等级', '字段定义', 1, true, 1, '{"parentId": 1010, "level": 2, "fieldKey": "emergency_level", "fieldType": "enum", "description": "应急等级字段"}'),
('field_category', 1010, 'field', 2029, '响应时间', '字段定义', 2, false, 1, '{"parentId": 1010, "level": 2, "fieldKey": "emergency_response_time", "fieldType": "number", "description": "响应时间字段"}'),
('field_category', 1010, 'field', 2030, '疏散路线', '字段定义', 3, false, 1, '{"parentId": 1010, "level": 2, "fieldKey": "evacuation_route", "fieldType": "string", "description": "疏散路线字段"}');

-- 验证插入的数据
SELECT 
    'Total records inserted' as info,
    COUNT(*) as count
FROM system_tree_data_rel 
WHERE tree_type = 'field_category' AND data_id >= 1000;

-- 显示插入的分类数据
SELECT 
    tree_node_id,
    data_id,
    data_name,
    data_type_label,
    display_order,
    status
FROM system_tree_data_rel 
WHERE tree_type = 'field_category' 
    AND data_type = 'field_category'
    AND data_id >= 1000
ORDER BY data_id;

-- 显示插入的字段数据
SELECT 
    tree_node_id,
    data_id,
    data_name,
    data_type_label,
    display_order,
    status
FROM system_tree_data_rel 
WHERE tree_type = 'field_category' 
    AND data_type = 'field'
    AND data_id >= 2000
ORDER BY data_id; 