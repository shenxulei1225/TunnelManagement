-- ===========================================
-- 增强版UniversalTree测试页面菜单创建脚本
-- ===========================================

-- 1. 创建"功能测试"父级菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '功能测试', '', 1, 999, 0, '/test', 'TestTube', '', '', 0, true, false, true, 'admin', NOW(), 'admin', NOW(), false
);

-- 获取刚插入的父级菜单ID（假设为最新插入的ID）
SET @test_parent_id = LAST_INSERT_ID();

-- 2. 创建"增强版树组件测试"子菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '增强版树组件测试', 'test:enhanced-tree:page', 2, 1, @test_parent_id, 'enhanced-tree', 'TreeTable', '/test/enhanced-tree', 'EnhancedTreeTest', 0, true, true, false, 'admin', NOW(), 'admin', NOW(), false
);

-- 获取增强版树测试菜单ID
SET @enhanced_tree_menu_id = LAST_INSERT_ID();

-- 3. 创建测试相关的权限菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES 
-- 配置管理权限
('测试配置管理', 'test:enhanced-tree:config', 3, 1, @enhanced_tree_menu_id, '', '', '', '', 0, false, false, false, 'admin', NOW(), 'admin', NOW(), false),
-- 状态管理权限  
('状态管理', 'test:enhanced-tree:state', 3, 2, @enhanced_tree_menu_id, '', '', '', '', 0, false, false, false, 'admin', NOW(), 'admin', NOW(), false),
-- 调试功能权限
('调试功能', 'test:enhanced-tree:debug', 3, 3, @enhanced_tree_menu_id, '', '', '', '', 0, false, false, false, 'admin', NOW(), 'admin', NOW(), false),
-- 导出功能权限
('导出功能', 'test:enhanced-tree:export', 3, 4, @enhanced_tree_menu_id, '', '', '', '', 0, false, false, false, 'admin', NOW(), 'admin', NOW(), false);

-- 4. 为超级管理员角色分配权限
-- 获取超级管理员角色ID（通常为1）
SET @admin_role_id = (SELECT id FROM system_role WHERE code = 'super_admin' LIMIT 1);

-- 如果找不到super_admin，使用ID为1的角色
SET @admin_role_id = COALESCE(@admin_role_id, 1);

-- 分配菜单权限给超级管理员
INSERT INTO system_role_menu (role_id, menu_id, creator, create_time, updater, update_time, deleted, tenant_id) 
SELECT @admin_role_id, id, 'admin', NOW(), 'admin', NOW(), false, 1
FROM system_menu 
WHERE (id = @test_parent_id OR parent_id = @test_parent_id OR parent_id = @enhanced_tree_menu_id)
AND deleted = false;

-- 5. 输出创建结果
SELECT 
    '功能测试菜单创建完成' as message,
    @test_parent_id as parent_menu_id,
    @enhanced_tree_menu_id as enhanced_tree_menu_id,
    @admin_role_id as admin_role_id;

-- 6. 验证创建结果
SELECT 
    m.id,
    m.name,
    m.permission,
    m.type,
    m.sort,
    m.parent_id,
    m.path,
    m.icon,
    m.component,
    m.status,
    m.visible
FROM system_menu m
WHERE (m.id = @test_parent_id OR m.parent_id = @test_parent_id OR m.parent_id = @enhanced_tree_menu_id)
AND m.deleted = false
ORDER BY m.parent_id, m.sort;

-- 7. 检查权限分配
SELECT 
    r.name as role_name,
    m.name as menu_name,
    m.permission,
    rm.create_time
FROM system_role_menu rm
JOIN system_role r ON rm.role_id = r.id
JOIN system_menu m ON rm.menu_id = m.id
WHERE rm.role_id = @admin_role_id 
AND (m.id = @test_parent_id OR m.parent_id = @test_parent_id OR m.parent_id = @enhanced_tree_menu_id)
AND rm.deleted = false
ORDER BY m.sort; 