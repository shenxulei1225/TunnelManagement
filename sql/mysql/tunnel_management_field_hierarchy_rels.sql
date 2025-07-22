-- =============================================
-- 隧道管理系统字段关联到分级组的SQL脚本
-- 将创建的字段关联到相应的分级组中，而不是分类中
-- =============================================

-- 1. 首先创建分级组（如果不存在）
-- 插入设备分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '设备管理组', 'equipment_group', 0, 1, '/1', 10,
    '#409EFF', 'Tool', '设备相关字段分组', 0, NOW(), 'system'
);

-- 获取设备分级组ID
SET @equipment_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'equipment_group' AND parent_id = 0);

-- 插入技术部门分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '技术部门组', 'technical_department_group', 0, 1, '/2', 20,
    '#67C23A', 'Peoples', '技术部门相关字段分组', 0, NOW(), 'system'
);

SET @tech_dept_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'technical_department_group' AND parent_id = 0);

-- 插入部门分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '部门管理组', 'department_group', 0, 1, '/3', 30,
    '#E6A23C', 'OfficeBuilding', '部门相关字段分组', 0, NOW(), 'system'
);

SET @dept_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'department_group' AND parent_id = 0);

-- 插入建筑分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '建筑工程组', 'architecture_group', 0, 1, '/4', 40,
    '#F56C6C', 'House', '建筑相关字段分组', 0, NOW(), 'system'
);

SET @arch_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'architecture_group' AND parent_id = 0);

-- 插入电气分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '电气工程组', 'electrical_group', 0, 1, '/5', 50,
    '#909399', 'Lightning', '电气相关字段分组', 0, NOW(), 'system'
);

SET @electrical_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'electrical_group' AND parent_id = 0);

-- 插入位置分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '位置管理组', 'location_group', 0, 1, '/6', 60,
    '#409EFF', 'Location', '位置相关字段分组', 0, NOW(), 'system'
);

SET @location_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'location_group' AND parent_id = 0);

-- 插入管理分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '管理事务组', 'management_group', 0, 1, '/7', 70,
    '#67C23A', 'Management', '管理相关字段分组', 0, NOW(), 'system'
);

SET @management_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'management_group' AND parent_id = 0);

-- 插入展示分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '展示控制组', 'display_group', 0, 1, '/8', 80,
    '#E6A23C', 'Monitor', '展示相关字段分组', 0, NOW(), 'system'
);

SET @display_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'display_group' AND parent_id = 0);

-- 插入度量分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '度量分析组', 'measurement_group', 0, 1, '/9', 90,
    '#F56C6C', 'DataLine', '度量相关字段分组', 0, NOW(), 'system'
);

SET @measurement_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'measurement_group' AND parent_id = 0);

-- 插入结构分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '结构工程组', 'structure_group', 0, 1, '/10', 100,
    '#909399', 'Grid', '结构相关字段分组', 0, NOW(), 'system'
);

SET @structure_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'structure_group' AND parent_id = 0);

-- 插入道路分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '道路交通组', 'road_group', 0, 1, '/11', 110,
    '#409EFF', 'Guide', '道路相关字段分组', 0, NOW(), 'system'
);

SET @road_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'road_group' AND parent_id = 0);

-- 插入环境分级组
INSERT IGNORE INTO system_hierarchy_group (
    tenant_id, name, code, parent_id, level, path, sort, 
    color, icon, description, status, create_time, creator
) VALUES (
    1, '环境监测组', 'environment_group', 0, 1, '/12', 120,
    '#67C23A', 'Sunny', '环境相关字段分组', 0, NOW(), 'system'
);

SET @environment_group_id = (SELECT id FROM system_hierarchy_group WHERE code = 'environment_group' AND parent_id = 0);

-- 2. 将字段关联到相应的分级组中
-- 设备相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @equipment_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'equipment_name', 'equipment_code', 'equipment_type', 'manufacturer', 'model_number',
    'installation_date', 'warranty_period', 'equipment_status', 'power_consumption', 'maintenance_cycle'
);

-- 技术部门相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @tech_dept_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'dept_name', 'dept_code', 'technical_field', 'responsibility_scope', 
    'team_leader', 'team_size', 'certification_level'
);

-- 部门相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @dept_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'department_name', 'department_code', 'department_type', 'department_level',
    'manager_name', 'contact_phone', 'office_location', 'staff_count'
);

-- 建筑相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @arch_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'building_name', 'building_type', 'construction_year', 'building_area',
    'building_height', 'structure_type', 'fire_rating', 'seismic_level'
);

-- 电气相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @electrical_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'electrical_system_name', 'voltage_level', 'rated_current', 'rated_power',
    'protection_level', 'cable_type', 'grounding_type', 'emergency_power'
);

-- 位置相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @location_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'location_name', 'coordinate_x', 'coordinate_y', 'elevation',
    'mileage', 'tunnel_section', 'side_position', 'access_difficulty'
);

-- 管理相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @management_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'management_object', 'responsible_person', 'management_level', 'inspection_frequency',
    'risk_level', 'emergency_plan', 'last_inspection_date', 'next_inspection_date'
);

-- 展示相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @display_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'display_name', 'display_type', 'display_position', 'refresh_interval',
    'display_priority', 'color_scheme', 'access_permission'
);

-- 度量相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @measurement_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'measurement_name', 'measurement_type', 'unit_of_measure', 'measurement_precision',
    'measurement_range_min', 'measurement_range_max', 'calibration_date', 'calibration_period'
);

-- 结构相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @structure_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'structure_name', 'structure_type', 'material_type', 'design_strength',
    'thickness', 'construction_date', 'structural_health', 'load_capacity'
);

-- 道路相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @road_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'road_name', 'road_type', 'design_speed', 'lane_count', 'lane_width',
    'pavement_type', 'gradient', 'traffic_capacity', 'surface_condition'
);

-- 环境相关字段关联
INSERT IGNORE INTO system_field_hierarchy_rel (
    field_id, hierarchy_group_id, sort, tenant_id, create_time, creator
)
SELECT 
    id, @environment_group_id, sort, tenant_id, NOW(), 'system'
FROM system_field_def
WHERE field_key IN (
    'environment_factor', 'monitoring_type', 'measurement_value', 'standard_limit',
    'monitoring_frequency', 'alert_threshold', 'environmental_status', 'seasonal_variation'
);

-- 查询验证结果
SELECT 
    hg.name AS group_name,
    COUNT(hr.id) AS field_count
FROM system_hierarchy_group hg
LEFT JOIN system_field_hierarchy_rel hr ON hg.id = hr.hierarchy_group_id
WHERE hg.code IN (
    'equipment_group', 'technical_department_group', 'department_group', 'architecture_group',
    'electrical_group', 'location_group', 'management_group', 'display_group',
    'measurement_group', 'structure_group', 'road_group', 'environment_group'
)
GROUP BY hg.id, hg.name
ORDER BY hg.sort;

-- 输出创建完成信息
SELECT '隧道管理系统字段已成功关联到分级组！' AS message;

-- =============================================
-- 菜单管理创建参数提示（基于分级组）
-- =============================================
/*
每个分级组对应页面的菜单管理创建参数：

1. 设备管理页面
   - 菜单名称：设备管理
   - 菜单编码：equipment_management
   - 菜单类型：菜单
   - 路由地址：/system/equipment
   - 组件路径：system/equipment/index
   - 权限标识：system:equipment:list
   - 图标：tool
   - 排序：10
   - 备注：设备相关字段分组管理

2. 技术部门管理页面
   - 菜单名称：技术部门管理
   - 菜单编码：technical_department_management
   - 菜单类型：菜单
   - 路由地址：/system/technical-department
   - 组件路径：system/technical-department/index
   - 权限标识：system:technical-department:list
   - 图标：peoples
   - 排序：20
   - 备注：技术部门相关字段分组管理

3. 部门管理页面
   - 菜单名称：部门管理
   - 菜单编码：department_management
   - 菜单类型：菜单
   - 路由地址：/system/department
   - 组件路径：system/department/index
   - 权限标识：system:department:list
   - 图标：office-building
   - 排序：30
   - 备注：部门相关字段分组管理

4. 建筑管理页面
   - 菜单名称：建筑管理
   - 菜单编码：architecture_management
   - 菜单类型：菜单
   - 路由地址：/system/architecture
   - 组件路径：system/architecture/index
   - 权限标识：system:architecture:list
   - 图标：house
   - 排序：40
   - 备注：建筑相关字段分组管理

5. 电气管理页面
   - 菜单名称：电气管理
   - 菜单编码：electrical_management
   - 菜单类型：菜单
   - 路由地址：/system/electrical
   - 组件路径：system/electrical/index
   - 权限标识：system:electrical:list
   - 图标：lightning
   - 排序：50
   - 备注：电气相关字段分组管理

6. 位置管理页面
   - 菜单名称：位置管理
   - 菜单编码：location_management
   - 菜单类型：菜单
   - 路由地址：/system/location
   - 组件路径：system/location/index
   - 权限标识：system:location:list
   - 图标：location
   - 排序：60
   - 备注：位置相关字段分组管理

7. 管理事务页面
   - 菜单名称：管理事务
   - 菜单编码：management_affairs
   - 菜单类型：菜单
   - 路由地址：/system/management
   - 组件路径：system/management/index
   - 权限标识：system:management:list
   - 图标：management
   - 排序：70
   - 备注：管理相关字段分组管理

8. 展示管理页面
   - 菜单名称：展示管理
   - 菜单编码：display_management
   - 菜单类型：菜单
   - 路由地址：/system/display
   - 组件路径：system/display/index
   - 权限标识：system:display:list
   - 图标：monitor
   - 排序：80
   - 备注：展示相关字段分组管理

9. 度量管理页面
   - 菜单名称：度量管理
   - 菜单编码：measurement_management
   - 菜单类型：菜单
   - 路由地址：/system/measurement
   - 组件路径：system/measurement/index
   - 权限标识：system:measurement:list
   - 图标：data-line
   - 排序：90
   - 备注：度量相关字段分组管理

10. 结构管理页面
    - 菜单名称：结构管理
    - 菜单编码：structure_management
    - 菜单类型：菜单
    - 路由地址：/system/structure
    - 组件路径：system/structure/index
    - 权限标识：system:structure:list
    - 图标：grid
    - 排序：100
    - 备注：结构相关字段分组管理

11. 道路管理页面
    - 菜单名称：道路管理
    - 菜单编码：road_management
    - 菜单类型：菜单
    - 路由地址：/system/road
    - 组件路径：system/road/index
    - 权限标识：system:road:list
    - 图标：guide
    - 排序：110
    - 备注：道路相关字段分组管理

12. 环境管理页面
    - 菜单名称：环境管理
    - 菜单编码：environment_management
    - 菜单类型：菜单
    - 路由地址：/system/environment
    - 组件路径：system/environment/index
    - 权限标识：system:environment:list
    - 图标：sunny
    - 排序：120
    - 备注：环境相关字段分组管理
*/ 