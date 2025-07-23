-- 检查菜单状态和问题诊断

-- 1. 检查系统管理菜单
SELECT '=== 系统管理菜单检查 ===' as info;
SELECT id, name, parent_id, path, visible, status, deleted 
FROM system_menu 
WHERE name LIKE '%系统%' OR name LIKE '%System%' OR path = '/system'
ORDER BY id;

-- 2. 检查数据结构分析器菜单是否存在
SELECT '=== 数据结构分析器菜单检查 ===' as info;
SELECT id, name, parent_id, path, component, visible, status, deleted, sort
FROM system_menu 
WHERE name LIKE '%数据结构%' OR name LIKE '%analyzer%' OR component LIKE '%DataStructureAnalyzer%'
ORDER BY id DESC;

-- 3. 检查系统管理下的所有子菜单
SET @system_parent_id = (SELECT id FROM system_menu WHERE (name = '系统管理' OR path = '/system') AND deleted = 0 LIMIT 1);
SELECT '=== 系统管理子菜单列表 ===' as info;
SELECT @system_parent_id as system_menu_id;
SELECT id, name, path, sort, visible, status, deleted
FROM system_menu 
WHERE parent_id = @system_parent_id
ORDER BY sort, id;

-- 4. 检查当前用户的角色和权限
SELECT '=== 超级管理员权限检查 ===' as info;
SELECT COUNT(*) as total_permissions
FROM system_role_menu 
WHERE role_id = 1 AND deleted = 0;

-- 5. 检查数据结构分析器的权限分配
SELECT '=== 数据结构分析器权限检查 ===' as info;
SELECT 
    rm.role_id,
    r.name as role_name,
    m.name as menu_name,
    rm.deleted as permission_deleted
FROM system_role_menu rm
LEFT JOIN system_role r ON rm.role_id = r.id
LEFT JOIN system_menu m ON rm.menu_id = m.id
WHERE m.name LIKE '%数据结构%'
ORDER BY rm.role_id;

-- 6. 检查菜单表结构（确认字段）
SELECT '=== 菜单表字段检查 ===' as info;
DESCRIBE system_menu;

-- 7. 检查角色菜单表结构
SELECT '=== 角色菜单表字段检查 ===' as info;
DESCRIBE system_role_menu; 