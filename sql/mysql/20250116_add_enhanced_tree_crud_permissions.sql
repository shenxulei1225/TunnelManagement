-- ===========================================
-- 增强版树组件测试CRUD权限补充脚本
-- ===========================================

-- 获取增强版树测试菜单ID
SET @enhanced_tree_menu_id = (
    SELECT id FROM system_menu 
    WHERE permission = 'test:enhanced-tree:page' 
    AND deleted = false 
    LIMIT 1
);

-- 检查菜单是否存在（如果不存在会显示警告信息）
SELECT 
    CASE 
        WHEN @enhanced_tree_menu_id IS NULL THEN '错误：找不到增强版树测试菜单，请先执行 20250116_add_enhanced_tree_test_menu.sql'
        ELSE CONCAT('找到增强版树测试菜单，ID：', @enhanced_tree_menu_id)
    END as check_result;

-- 只有在菜单存在的情况下才插入权限（使用子查询确保安全）
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
)
SELECT 
    perm_data.name,
    perm_data.permission,
    perm_data.type,
    perm_data.sort,
    @enhanced_tree_menu_id,
    perm_data.path,
    perm_data.icon,
    perm_data.component,
    perm_data.component_name,
    perm_data.status,
    perm_data.visible,
    perm_data.keep_alive,
    perm_data.always_show,
    perm_data.creator,
    NOW(),
    perm_data.updater,
    NOW(),
    perm_data.deleted
FROM (
    SELECT '查询权限' as name, 'test:enhanced-tree:query' as permission, 3 as type, 10 as sort, '' as path, '' as icon, '' as component, '' as component_name, 0 as status, false as visible, false as keep_alive, false as always_show, 'admin' as creator, 'admin' as updater, false as deleted
    UNION ALL
    SELECT '创建权限', 'test:enhanced-tree:create', 3, 11, '', '', '', '', 0, false, false, false, 'admin', 'admin', false
    UNION ALL
    SELECT '更新权限', 'test:enhanced-tree:update', 3, 12, '', '', '', '', 0, false, false, false, 'admin', 'admin', false
    UNION ALL
    SELECT '删除权限', 'test:enhanced-tree:delete', 3, 13, '', '', '', '', 0, false, false, false, 'admin', 'admin', false
) as perm_data
WHERE @enhanced_tree_menu_id IS NOT NULL;

-- 获取超级管理员角色ID
SET @admin_role_id = (SELECT id FROM system_role WHERE code = 'super_admin' LIMIT 1);

-- 如果找不到super_admin，使用ID为1的角色
SET @admin_role_id = COALESCE(@admin_role_id, 1);

-- 为超级管理员分配新增的CRUD权限
INSERT INTO system_role_menu (role_id, menu_id, creator, create_time, updater, update_time, deleted, tenant_id) 
SELECT @admin_role_id, id, 'admin', NOW(), 'admin', NOW(), false, 1
FROM system_menu 
WHERE permission IN ('test:enhanced-tree:query', 'test:enhanced-tree:create', 'test:enhanced-tree:update', 'test:enhanced-tree:delete')
AND deleted = false
AND @enhanced_tree_menu_id IS NOT NULL;

-- 输出结果
SELECT 
    'CRUD权限添加完成' as message,
    @enhanced_tree_menu_id as enhanced_tree_menu_id,
    @admin_role_id as admin_role_id;

-- 验证创建的权限
SELECT 
    m.id,
    m.name,
    m.permission,
    m.type,
    m.sort,
    m.parent_id
FROM system_menu m
WHERE m.permission LIKE 'test:enhanced-tree:%'
AND m.deleted = false
ORDER BY m.sort;

-- 检查权限分配
SELECT 
    r.name as role_name,
    m.name as menu_name,
    m.permission,
    rm.create_time
FROM system_role_menu rm
JOIN system_role r ON rm.role_id = r.id
JOIN system_menu m ON rm.menu_id = m.id
WHERE rm.role_id = @admin_role_id 
AND m.permission LIKE 'test:enhanced-tree:%'
AND rm.deleted = false
ORDER BY m.sort; 