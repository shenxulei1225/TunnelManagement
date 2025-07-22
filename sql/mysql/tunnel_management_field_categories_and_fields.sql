-- =============================================
-- 隧道管理系统字段分类和字段定义初始化脚本
-- 基于图中显示的分类结构创建行业内常用字段
-- =============================================

-- 1. 首先创建主要分类（如果不存在）
-- 插入设备分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'equipment', '设备', '/1', 1, 10,
    0, NOW(), 'system', 1
);

-- 获取设备分类ID
SET @equipment_id = (SELECT id FROM system_field_category WHERE code = 'equipment' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @equipment_id) WHERE id = @equipment_id;

-- 插入技术部门分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'technical_department', '技术部门', '/2', 1, 20,
    0, NOW(), 'system', 1
);

SET @tech_dept_id = (SELECT id FROM system_field_category WHERE code = 'technical_department' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @tech_dept_id) WHERE id = @tech_dept_id;

-- 插入部门分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'department', '部门', '/3', 1, 30,
    0, NOW(), 'system', 1
);

SET @dept_id = (SELECT id FROM system_field_category WHERE code = 'department' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @dept_id) WHERE id = @dept_id;

-- 插入建筑分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'architecture', '建筑', '/4', 1, 40,
    0, NOW(), 'system', 1
);

SET @arch_id = (SELECT id FROM system_field_category WHERE code = 'architecture' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @arch_id) WHERE id = @arch_id;

-- 插入电气分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'electrical', '电气', '/5', 1, 50,
    0, NOW(), 'system', 1
);

SET @electrical_id = (SELECT id FROM system_field_category WHERE code = 'electrical' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @electrical_id) WHERE id = @electrical_id;

-- 插入位置分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'location', '位置', '/6', 1, 60,
    0, NOW(), 'system', 1
);

SET @location_id = (SELECT id FROM system_field_category WHERE code = 'location' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @location_id) WHERE id = @location_id;

-- 插入管理分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'management', '管理', '/7', 1, 70,
    0, NOW(), 'system', 1
);

SET @management_id = (SELECT id FROM system_field_category WHERE code = 'management' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @management_id) WHERE id = @management_id;

-- 插入展示分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'display', '展示', '/8', 1, 80,
    0, NOW(), 'system', 1
);

SET @display_id = (SELECT id FROM system_field_category WHERE code = 'display' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @display_id) WHERE id = @display_id;

-- 插入度量分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'measurement', '度量', '/9', 1, 90,
    0, NOW(), 'system', 1
);

SET @measurement_id = (SELECT id FROM system_field_category WHERE code = 'measurement' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @measurement_id) WHERE id = @measurement_id;

-- 插入结构分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'structure', '结构', '/10', 1, 100,
    0, NOW(), 'system', 1
);

SET @structure_id = (SELECT id FROM system_field_category WHERE code = 'structure' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @structure_id) WHERE id = @structure_id;

-- 插入道路分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'road', '道路', '/11', 1, 110,
    0, NOW(), 'system', 1
);

SET @road_id = (SELECT id FROM system_field_category WHERE code = 'road' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @road_id) WHERE id = @road_id;

-- 插入环境分类
INSERT IGNORE INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'environment', '环境', '/12', 1, 120,
    0, NOW(), 'system', 1
);

SET @environment_id = (SELECT id FROM system_field_category WHERE code = 'environment' AND parent_id = 0);
UPDATE system_field_category SET tree_path = CONCAT('/', @environment_id) WHERE id = @environment_id;

-- 2. 为设备分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
-- 基础信息字段
(@equipment_id, 'equipment_name', '设备名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@equipment_id, 'equipment_code', '设备编号', 'string', NULL, NULL, 20, NOW(), 'system', 1),
(@equipment_id, 'equipment_type', '设备类型', 'enum', NULL, '[{"value":"ventilation","label":"通风设备"},{"value":"lighting","label":"照明设备"},{"value":"monitoring","label":"监控设备"},{"value":"fire_safety","label":"消防设备"},{"value":"drainage","label":"排水设备"},{"value":"power_supply","label":"供电设备"}]', 30, NOW(), 'system', 1),
(@equipment_id, 'manufacturer', '生产厂家', 'string', NULL, NULL, 40, NOW(), 'system', 1),
(@equipment_id, 'model_number', '型号规格', 'string', NULL, NULL, 50, NOW(), 'system', 1),
(@equipment_id, 'installation_date', '安装日期', 'date', NULL, NULL, 60, NOW(), 'system', 1),
(@equipment_id, 'warranty_period', '保修期', 'number', '月', NULL, 70, NOW(), 'system', 1),
(@equipment_id, 'equipment_status', '设备状态', 'enum', NULL, '[{"value":"normal","label":"正常"},{"value":"maintenance","label":"维护中"},{"value":"fault","label":"故障"},{"value":"stopped","label":"停用"}]', 80, NOW(), 'system', 1),
(@equipment_id, 'power_consumption', '功耗', 'number', 'kW', NULL, 90, NOW(), 'system', 1),
(@equipment_id, 'maintenance_cycle', '维护周期', 'number', '天', NULL, 100, NOW(), 'system', 1);

-- 3. 为技术部门分类创建字段  
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@tech_dept_id, 'dept_name', '部门名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@tech_dept_id, 'dept_code', '部门编码', 'string', NULL, NULL, 20, NOW(), 'system', 1),
(@tech_dept_id, 'technical_field', '技术领域', 'enum', NULL, '[{"value":"civil_engineering","label":"土木工程"},{"value":"electrical_engineering","label":"电气工程"},{"value":"mechanical_engineering","label":"机械工程"},{"value":"automation","label":"自动化"},{"value":"safety_engineering","label":"安全工程"},{"value":"environmental_engineering","label":"环境工程"}]', 30, NOW(), 'system', 1),
(@tech_dept_id, 'responsibility_scope', '职责范围', 'string', NULL, NULL, 40, NOW(), 'system', 1),
(@tech_dept_id, 'team_leader', '团队负责人', 'string', NULL, NULL, 50, NOW(), 'system', 1),
(@tech_dept_id, 'team_size', '团队规模', 'number', '人', NULL, 60, NOW(), 'system', 1),
(@tech_dept_id, 'certification_level', '资质等级', 'enum', NULL, '[{"value":"level_1","label":"一级资质"},{"value":"level_2","label":"二级资质"},{"value":"level_3","label":"三级资质"},{"value":"special","label":"专项资质"}]', 70, NOW(), 'system', 1);

-- 4. 为部门分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@dept_id, 'department_name', '部门名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@dept_id, 'department_code', '部门代码', 'string', NULL, NULL, 20, NOW(), 'system', 1),
(@dept_id, 'department_type', '部门类型', 'enum', NULL, '[{"value":"administration","label":"行政部门"},{"value":"operation","label":"运营部门"},{"value":"maintenance","label":"维护部门"},{"value":"safety","label":"安全部门"},{"value":"emergency","label":"应急部门"}]', 30, NOW(), 'system', 1),
(@dept_id, 'department_level', '部门级别', 'enum', NULL, '[{"value":"headquarters","label":"总部"},{"value":"branch","label":"分部"},{"value":"section","label":"科室"},{"value":"team","label":"班组"}]', 40, NOW(), 'system', 1),
(@dept_id, 'manager_name', '部门经理', 'string', NULL, NULL, 50, NOW(), 'system', 1),
(@dept_id, 'contact_phone', '联系电话', 'string', NULL, NULL, 60, NOW(), 'system', 1),
(@dept_id, 'office_location', '办公地点', 'string', NULL, NULL, 70, NOW(), 'system', 1),
(@dept_id, 'staff_count', '人员数量', 'number', '人', NULL, 80, NOW(), 'system', 1);

-- 5. 为建筑分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@arch_id, 'building_name', '建筑名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@arch_id, 'building_type', '建筑类型', 'enum', NULL, '[{"value":"tunnel_entrance","label":"隧道洞口"},{"value":"ventilation_building","label":"通风建筑"},{"value":"control_room","label":"控制室"},{"value":"emergency_exit","label":"紧急出口"},{"value":"maintenance_facility","label":"维护设施"}]', 20, NOW(), 'system', 1),
(@arch_id, 'construction_year', '建设年份', 'number', '年', NULL, 30, NOW(), 'system', 1),
(@arch_id, 'building_area', '建筑面积', 'number', '平方米', NULL, 40, NOW(), 'system', 1),
(@arch_id, 'building_height', '建筑高度', 'number', '米', NULL, 50, NOW(), 'system', 1),
(@arch_id, 'structure_type', '结构类型', 'enum', NULL, '[{"value":"reinforced_concrete","label":"钢筋混凝土"},{"value":"steel_structure","label":"钢结构"},{"value":"mixed_structure","label":"混合结构"},{"value":"masonry","label":"砌体结构"}]', 60, NOW(), 'system', 1),
(@arch_id, 'fire_rating', '耐火等级', 'enum', NULL, '[{"value":"level_1","label":"一级"},{"value":"level_2","label":"二级"},{"value":"level_3","label":"三级"},{"value":"level_4","label":"四级"}]', 70, NOW(), 'system', 1),
(@arch_id, 'seismic_level', '抗震等级', 'enum', NULL, '[{"value":"level_6","label":"6度"},{"value":"level_7","label":"7度"},{"value":"level_8","label":"8度"},{"value":"level_9","label":"9度"}]', 80, NOW(), 'system', 1);

-- 6. 为电气分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@electrical_id, 'electrical_system_name', '电气系统名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@electrical_id, 'voltage_level', '电压等级', 'enum', NULL, '[{"value":"220v","label":"220V"},{"value":"380v","label":"380V"},{"value":"10kv","label":"10kV"},{"value":"35kv","label":"35kV"},{"value":"110kv","label":"110kV"}]', 20, NOW(), 'system', 1),
(@electrical_id, 'rated_current', '额定电流', 'number', 'A', NULL, 30, NOW(), 'system', 1),
(@electrical_id, 'rated_power', '额定功率', 'number', 'kW', NULL, 40, NOW(), 'system', 1),
(@electrical_id, 'protection_level', '防护等级', 'enum', NULL, '[{"value":"ip54","label":"IP54"},{"value":"ip65","label":"IP65"},{"value":"ip67","label":"IP67"},{"value":"ip68","label":"IP68"}]', 50, NOW(), 'system', 1),
(@electrical_id, 'cable_type', '电缆类型', 'enum', NULL, '[{"value":"power_cable","label":"电力电缆"},{"value":"control_cable","label":"控制电缆"},{"value":"signal_cable","label":"信号电缆"},{"value":"fiber_optic","label":"光纤电缆"}]', 60, NOW(), 'system', 1),
(@electrical_id, 'grounding_type', '接地方式', 'enum', NULL, '[{"value":"tn_s","label":"TN-S"},{"value":"tn_c","label":"TN-C"},{"value":"tn_c_s","label":"TN-C-S"},{"value":"tt","label":"TT"},{"value":"it","label":"IT"}]', 70, NOW(), 'system', 1),
(@electrical_id, 'emergency_power', '应急电源', 'enum', NULL, '[{"value":"ups","label":"UPS"},{"value":"generator","label":"发电机"},{"value":"battery","label":"蓄电池"},{"value":"none","label":"无"}]', 80, NOW(), 'system', 1);

-- 7. 为位置分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@location_id, 'location_name', '位置名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@location_id, 'coordinate_x', 'X坐标', 'number', '米', NULL, 20, NOW(), 'system', 1),
(@location_id, 'coordinate_y', 'Y坐标', 'number', '米', NULL, 30, NOW(), 'system', 1),
(@location_id, 'elevation', '高程', 'number', '米', NULL, 40, NOW(), 'system', 1),
(@location_id, 'mileage', '里程桩号', 'string', NULL, NULL, 50, NOW(), 'system', 1),
(@location_id, 'tunnel_section', '隧道区段', 'enum', NULL, '[{"value":"entrance","label":"洞口段"},{"value":"transition","label":"过渡段"},{"value":"main","label":"主体段"},{"value":"exit","label":"出口段"},{"value":"emergency","label":"应急段"}]', 60, NOW(), 'system', 1),
(@location_id, 'side_position', '左右侧位置', 'enum', NULL, '[{"value":"left","label":"左侧"},{"value":"right","label":"右侧"},{"value":"center","label":"中央"},{"value":"both","label":"双侧"}]', 70, NOW(), 'system', 1),
(@location_id, 'access_difficulty', '可达性', 'enum', NULL, '[{"value":"easy","label":"容易"},{"value":"normal","label":"一般"},{"value":"difficult","label":"困难"},{"value":"very_difficult","label":"很困难"}]', 80, NOW(), 'system', 1);

-- 8. 为管理分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@management_id, 'management_object', '管理对象', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@management_id, 'responsible_person', '责任人', 'string', NULL, NULL, 20, NOW(), 'system', 1),
(@management_id, 'management_level', '管理级别', 'enum', NULL, '[{"value":"strategic","label":"战略级"},{"value":"tactical","label":"战术级"},{"value":"operational","label":"操作级"},{"value":"emergency","label":"应急级"}]', 30, NOW(), 'system', 1),
(@management_id, 'inspection_frequency', '检查频率', 'enum', NULL, '[{"value":"daily","label":"每日"},{"value":"weekly","label":"每周"},{"value":"monthly","label":"每月"},{"value":"quarterly","label":"每季度"},{"value":"annually","label":"每年"}]', 40, NOW(), 'system', 1),
(@management_id, 'risk_level', '风险等级', 'enum', NULL, '[{"value":"very_low","label":"极低"},{"value":"low","label":"低"},{"value":"medium","label":"中"},{"value":"high","label":"高"},{"value":"very_high","label":"极高"}]', 50, NOW(), 'system', 1),
(@management_id, 'emergency_plan', '应急预案', 'string', NULL, NULL, 60, NOW(), 'system', 1),
(@management_id, 'last_inspection_date', '最后检查日期', 'date', NULL, NULL, 70, NOW(), 'system', 1),
(@management_id, 'next_inspection_date', '下次检查日期', 'date', NULL, NULL, 80, NOW(), 'system', 1);

-- 9. 为展示分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@display_id, 'display_name', '展示名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@display_id, 'display_type', '展示类型', 'enum', NULL, '[{"value":"dashboard","label":"仪表盘"},{"value":"chart","label":"图表"},{"value":"map","label":"地图"},{"value":"3d_model","label":"三维模型"},{"value":"video","label":"视频监控"}]', 20, NOW(), 'system', 1),
(@display_id, 'display_position', '显示位置', 'enum', NULL, '[{"value":"main_screen","label":"主屏幕"},{"value":"sub_screen","label":"副屏幕"},{"value":"mobile","label":"移动端"},{"value":"led_panel","label":"LED面板"}]', 30, NOW(), 'system', 1),
(@display_id, 'refresh_interval', '刷新间隔', 'number', '秒', NULL, 40, NOW(), 'system', 1),
(@display_id, 'display_priority', '显示优先级', 'enum', NULL, '[{"value":"critical","label":"关键"},{"value":"important","label":"重要"},{"value":"normal","label":"一般"},{"value":"low","label":"低"}]', 50, NOW(), 'system', 1),
(@display_id, 'color_scheme', '配色方案', 'enum', NULL, '[{"value":"default","label":"默认"},{"value":"dark","label":"深色"},{"value":"light","label":"浅色"},{"value":"high_contrast","label":"高对比度"}]', 60, NOW(), 'system', 1),
(@display_id, 'access_permission', '访问权限', 'enum', NULL, '[{"value":"public","label":"公开"},{"value":"internal","label":"内部"},{"value":"restricted","label":"受限"},{"value":"confidential","label":"机密"}]', 70, NOW(), 'system', 1);

-- 10. 为度量分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@measurement_id, 'measurement_name', '度量名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@measurement_id, 'measurement_type', '度量类型', 'enum', NULL, '[{"value":"length","label":"长度"},{"value":"area","label":"面积"},{"value":"volume","label":"体积"},{"value":"weight","label":"重量"},{"value":"temperature","label":"温度"},{"value":"pressure","label":"压力"},{"value":"flow_rate","label":"流量"},{"value":"velocity","label":"速度"}]', 20, NOW(), 'system', 1),
(@measurement_id, 'unit_of_measure', '计量单位', 'enum', NULL, '[{"value":"meter","label":"米"},{"value":"square_meter","label":"平方米"},{"value":"cubic_meter","label":"立方米"},{"value":"kilogram","label":"千克"},{"value":"celsius","label":"摄氏度"},{"value":"pascal","label":"帕斯卡"},{"value":"liter_per_second","label":"升/秒"}]', 30, NOW(), 'system', 1),
(@measurement_id, 'measurement_precision', '测量精度', 'number', NULL, NULL, 40, NOW(), 'system', 1),
(@measurement_id, 'measurement_range_min', '测量范围最小值', 'number', NULL, NULL, 50, NOW(), 'system', 1),
(@measurement_id, 'measurement_range_max', '测量范围最大值', 'number', NULL, NULL, 60, NOW(), 'system', 1),
(@measurement_id, 'calibration_date', '校准日期', 'date', NULL, NULL, 70, NOW(), 'system', 1),
(@measurement_id, 'calibration_period', '校准周期', 'number', '月', NULL, 80, NOW(), 'system', 1);

-- 11. 为结构分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@structure_id, 'structure_name', '结构名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@structure_id, 'structure_type', '结构类型', 'enum', NULL, '[{"value":"primary_support","label":"初期支护"},{"value":"secondary_lining","label":"二次衬砌"},{"value":"waterproof_layer","label":"防水层"},{"value":"drainage_system","label":"排水系统"},{"value":"joint_system","label":"接缝系统"}]', 20, NOW(), 'system', 1),
(@structure_id, 'material_type', '材料类型', 'enum', NULL, '[{"value":"concrete","label":"混凝土"},{"value":"steel","label":"钢材"},{"value":"composite","label":"复合材料"},{"value":"polymer","label":"聚合物"},{"value":"ceramic","label":"陶瓷"}]', 30, NOW(), 'system', 1),
(@structure_id, 'design_strength', '设计强度', 'number', 'MPa', NULL, 40, NOW(), 'system', 1),
(@structure_id, 'thickness', '厚度', 'number', '毫米', NULL, 50, NOW(), 'system', 1),
(@structure_id, 'construction_date', '施工日期', 'date', NULL, NULL, 60, NOW(), 'system', 1),
(@structure_id, 'structural_health', '结构健康状态', 'enum', NULL, '[{"value":"excellent","label":"优良"},{"value":"good","label":"良好"},{"value":"acceptable","label":"可接受"},{"value":"poor","label":"较差"},{"value":"critical","label":"危险"}]', 70, NOW(), 'system', 1),
(@structure_id, 'load_capacity', '承载能力', 'number', 'kN', NULL, 80, NOW(), 'system', 1);

-- 12. 为道路分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@road_id, 'road_name', '道路名称', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@road_id, 'road_type', '道路类型', 'enum', NULL, '[{"value":"tunnel_interior","label":"隧道内道路"},{"value":"approach_road","label":"接线道路"},{"value":"service_road","label":"服务道路"},{"value":"emergency_lane","label":"应急车道"},{"value":"maintenance_road","label":"养护通道"}]', 20, NOW(), 'system', 1),
(@road_id, 'design_speed', '设计速度', 'number', 'km/h', NULL, 30, NOW(), 'system', 1),
(@road_id, 'lane_count', '车道数量', 'number', '条', NULL, 40, NOW(), 'system', 1),
(@road_id, 'lane_width', '车道宽度', 'number', '米', NULL, 50, NOW(), 'system', 1),
(@road_id, 'pavement_type', '路面类型', 'enum', NULL, '[{"value":"asphalt_concrete","label":"沥青混凝土"},{"value":"cement_concrete","label":"水泥混凝土"},{"value":"composite_pavement","label":"复合式路面"},{"value":"steel_deck","label":"钢桥面"}]', 60, NOW(), 'system', 1),
(@road_id, 'gradient', '纵坡坡度', 'number', '%', NULL, 70, NOW(), 'system', 1),
(@road_id, 'traffic_capacity', '通行能力', 'number', '辆/小时', NULL, 80, NOW(), 'system', 1),
(@road_id, 'surface_condition', '路面状况', 'enum', NULL, '[{"value":"excellent","label":"优"},{"value":"good","label":"良"},{"value":"fair","label":"中"},{"value":"poor","label":"次"},{"value":"very_poor","label":"差"}]', 90, NOW(), 'system', 1);

-- 13. 为环境分类创建字段
INSERT INTO system_field_def (
    category_id, field_key, field_label, value_type, unit, enum_json, sort, 
    create_time, creator, tenant_id
) VALUES
(@environment_id, 'environment_factor', '环境因子', 'string', NULL, NULL, 10, NOW(), 'system', 1),
(@environment_id, 'monitoring_type', '监测类型', 'enum', NULL, '[{"value":"air_quality","label":"空气质量"},{"value":"noise_level","label":"噪声水平"},{"value":"vibration","label":"振动"},{"value":"temperature_humidity","label":"温湿度"},{"value":"illumination","label":"照度"},{"value":"wind_speed","label":"风速"}]', 20, NOW(), 'system', 1),
(@environment_id, 'measurement_value', '测量值', 'number', NULL, NULL, 30, NOW(), 'system', 1),
(@environment_id, 'standard_limit', '标准限值', 'number', NULL, NULL, 40, NOW(), 'system', 1),
(@environment_id, 'monitoring_frequency', '监测频率', 'enum', NULL, '[{"value":"continuous","label":"连续监测"},{"value":"hourly","label":"每小时"},{"value":"daily","label":"每日"},{"value":"weekly","label":"每周"},{"value":"monthly","label":"每月"}]', 50, NOW(), 'system', 1),
(@environment_id, 'alert_threshold', '报警阈值', 'number', NULL, NULL, 60, NOW(), 'system', 1),
(@environment_id, 'environmental_status', '环境状态', 'enum', NULL, '[{"value":"normal","label":"正常"},{"value":"warning","label":"预警"},{"value":"alarm","label":"报警"},{"value":"critical","label":"严重"}]', 70, NOW(), 'system', 1),
(@environment_id, 'seasonal_variation', '季节变化', 'enum', NULL, '[{"value":"spring","label":"春季"},{"value":"summer","label":"夏季"},{"value":"autumn","label":"秋季"},{"value":"winter","label":"冬季"}]', 80, NOW(), 'system', 1);

-- 查询验证结果
SELECT 
    c.name AS category_name,
    COUNT(f.id) AS field_count
FROM system_field_category c
LEFT JOIN system_field_def f ON c.id = f.category_id
WHERE c.code IN ('equipment', 'technical_department', 'department', 'architecture', 'electrical', 'location', 'management', 'display', 'measurement', 'structure', 'road', 'environment')
GROUP BY c.id, c.name
ORDER BY c.sort;

-- 输出创建完成信息
SELECT '隧道管理系统字段分类和字段定义初始化完成！' AS message;

-- =============================================
-- 菜单管理创建参数提示
-- =============================================
/*
每个分类页面的菜单管理创建参数：

1. 设备管理页面
   - 菜单名称：设备管理
   - 菜单编码：equipment_management
   - 菜单类型：菜单
   - 路由地址：/system/equipment
   - 组件路径：system/equipment/index
   - 权限标识：system:equipment:list
   - 图标：tool
   - 排序：10

2. 技术部门管理页面
   - 菜单名称：技术部门管理
   - 菜单编码：technical_department_management
   - 菜单类型：菜单
   - 路由地址：/system/technical-department
   - 组件路径：system/technical-department/index
   - 权限标识：system:technical-department:list
   - 图标：peoples
   - 排序：20

3. 部门管理页面
   - 菜单名称：部门管理
   - 菜单编码：department_management
   - 菜单类型：菜单
   - 路由地址：/system/department
   - 组件路径：system/department/index
   - 权限标识：system:department:list
   - 图标：tree
   - 排序：30

4. 建筑管理页面
   - 菜单名称：建筑管理
   - 菜单编码：architecture_management
   - 菜单类型：菜单
   - 路由地址：/system/architecture
   - 组件路径：system/architecture/index
   - 权限标识：system:architecture:list
   - 图标：office-building
   - 排序：40

5. 电气管理页面
   - 菜单名称：电气管理
   - 菜单编码：electrical_management
   - 菜单类型：菜单
   - 路由地址：/system/electrical
   - 组件路径：system/electrical/index
   - 权限标识：system:electrical:list
   - 图标：lightning
   - 排序：50

6. 位置管理页面
   - 菜单名称：位置管理
   - 菜单编码：location_management
   - 菜单类型：菜单
   - 路由地址：/system/location
   - 组件路径：system/location/index
   - 权限标识：system:location:list
   - 图标：location
   - 排序：60

7. 管理事务页面
   - 菜单名称：管理事务
   - 菜单编码：management_affairs
   - 菜单类型：菜单
   - 路由地址：/system/management
   - 组件路径：system/management/index
   - 权限标识：system:management:list
   - 图标：management
   - 排序：70

8. 展示管理页面
   - 菜单名称：展示管理
   - 菜单编码：display_management
   - 菜单类型：菜单
   - 路由地址：/system/display
   - 组件路径：system/display/index
   - 权限标识：system:display:list
   - 图标：monitor
   - 排序：80

9. 度量管理页面
   - 菜单名称：度量管理
   - 菜单编码：measurement_management
   - 菜单类型：菜单
   - 路由地址：/system/measurement
   - 组件路径：system/measurement/index
   - 权限标识：system:measurement:list
   - 图标：data-line
   - 排序：90

10. 结构管理页面
    - 菜单名称：结构管理
    - 菜单编码：structure_management
    - 菜单类型：菜单
    - 路由地址：/system/structure
    - 组件路径：system/structure/index
    - 权限标识：system:structure:list
    - 图标：building
    - 排序：100

11. 道路管理页面
    - 菜单名称：道路管理
    - 菜单编码：road_management
    - 菜单类型：菜单
    - 路由地址：/system/road
    - 组件路径：system/road/index
    - 权限标识：system:road:list
    - 图标：guide
    - 排序：110

12. 环境管理页面
    - 菜单名称：环境管理
    - 菜单编码：environment_management
    - 菜单类型：菜单
    - 路由地址：/system/environment
    - 组件路径：system/environment/index
    - 权限标识：system:environment:list
    - 图标：sunny
    - 排序：120
*/ 