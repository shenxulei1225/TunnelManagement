-- 添加业务建模菜单到系统管理
-- 基于分类字段关联的动态表单配置管理

-- 查找系统管理菜单的ID
SET @system_menu_id = (SELECT id FROM system_menu WHERE name = '系统管理' AND parent_id = 0 LIMIT 1);

-- 插入业务建模菜单
INSERT INTO system_menu (
    name, 
    permission, 
    type, 
    sort, 
    parent_id, 
    path, 
    icon, 
    component, 
    component_name, 
    status, 
    visible, 
    keep_alive, 
    always_show, 
    creator, 
    create_time, 
    updater, 
    update_time, 
    deleted
) VALUES (
    '业务建模', 
    'system:business-modeling:query', 
    2, 
    99, 
    @system_menu_id, 
    'business-modeling', 
    'ep:cpu', 
    'business-module/BusinessModeling', 
    'BusinessModeling', 
    0, 
    1, 
    1, 
    0, 
    '1', 
    NOW(), 
    '1', 
    NOW(), 
    0
);

-- 获取新插入的菜单ID
SET @business_modeling_menu_id = LAST_INSERT_ID();

-- 插入字段配置指南菜单（作为业务建模的辅助页面）
INSERT INTO system_menu (
    name, 
    permission, 
    type, 
    sort, 
    parent_id, 
    path, 
    icon, 
    component, 
    component_name, 
    status, 
    visible, 
    keep_alive, 
    always_show, 
    creator, 
    create_time, 
    updater, 
    update_time, 
    deleted
) VALUES (
    '字段配置指南', 
    'system:field-config-guide:query', 
    2, 
    100, 
    @system_menu_id, 
    'field-config-guide', 
    'ep:guide', 
    'business-module/FieldConfigGuide', 
    'FieldConfigGuide', 
    0, 
    1, 
    1, 
    0, 
    '1', 
    NOW(), 
    '1', 
    NOW(), 
    0
);

-- 验证插入结果
SELECT 
    m.id,
    m.name,
    m.permission,
    m.type,
    m.sort,
    p.name as parent_name,
    m.path,
    m.icon,
    m.component,
    m.component_name,
    m.status,
    m.visible
FROM system_menu m 
LEFT JOIN system_menu p ON m.parent_id = p.id
WHERE m.id = @business_modeling_menu_id;

-- 显示系统管理下的所有菜单
SELECT 
    m.id,
    m.name,
    m.path,
    m.component,
    m.icon,
    m.sort,
    m.status
FROM system_menu m 
WHERE m.parent_id = @system_menu_id
  AND m.deleted = 0
ORDER BY m.sort, m.id;

-- 提示信息
SELECT 
    CONCAT('✅ 业务建模菜单已成功添加到系统管理下') as result,
    CONCAT('📋 菜单ID: ', @business_modeling_menu_id) as menu_info,
    CONCAT('🔗 访问路径: /system/business-modeling') as access_path,
    CONCAT('🎯 功能描述: 基于分类字段关联的动态表单配置管理') as description; 