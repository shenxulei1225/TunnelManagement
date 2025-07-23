-- 为分类管理中心页面添加菜单配置
-- 页面路径: tunnel-management-ui/src/views/system/category/CategoryManager.vue

-- 先获取系统管理菜单ID
SET @system_menu_id = (SELECT id FROM system_menu WHERE name = '系统管理' AND deleted = 0 LIMIT 1);

-- 添加分类管理中心菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, 
    path, icon, component, component_name, status, 
    visible, keep_alive, creator, create_time, updater, 
    update_time, deleted
) VALUES (
    '分类管理中心', 
    'system:category:manager', 
    2, 
    100, 
    @system_menu_id,
    'category-manager', 
    'ep:files', 
    'system/category/CategoryManager', 
    'CategoryManager', 
    0, 
    1, 
    1, 
    '1', 
    NOW(), 
    '1', 
    NOW(), 
    0
);

-- 添加分类管理中心的操作权限
SET @menu_id = LAST_INSERT_ID();

-- 查看权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, 
    path, icon, component, component_name, status, 
    visible, keep_alive, creator, create_time, updater, 
    update_time, deleted
) VALUES (
    '分类管理查看', 
    'system:category:manager:query', 
    3, 
    1, 
    @menu_id,
    '', 
    '', 
    '', 
    '', 
    0, 
    1, 
    1, 
    '1', 
    NOW(), 
    '1', 
    NOW(), 
    0
);

-- 创建权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, 
    path, icon, component, component_name, status, 
    visible, keep_alive, creator, create_time, updater, 
    update_time, deleted
) VALUES (
    '分类管理创建', 
    'system:category:manager:create', 
    3, 
    2, 
    @menu_id,
    '', 
    '', 
    '', 
    '', 
    0, 
    1, 
    1, 
    '1', 
    NOW(), 
    '1', 
    NOW(), 
    0
);

-- 更新权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, 
    path, icon, component, component_name, status, 
    visible, keep_alive, creator, create_time, updater, 
    update_time, deleted
) VALUES (
    '分类管理更新', 
    'system:category:manager:update', 
    3, 
    3, 
    @menu_id,
    '', 
    '', 
    '', 
    '', 
    0, 
    1, 
    1, 
    '1', 
    NOW(), 
    '1', 
    NOW(), 
    0
);

-- 删除权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, 
    path, icon, component, component_name, status, 
    visible, keep_alive, creator, create_time, updater, 
    update_time, deleted
) VALUES (
    '分类管理删除', 
    'system:category:manager:delete', 
    3, 
    4, 
    @menu_id,
    '', 
    '', 
    '', 
    '', 
    0, 
    1, 
    1, 
    '1', 
    NOW(), 
    '1', 
    NOW(), 
    0
);

-- 导出权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, 
    path, icon, component, component_name, status, 
    visible, keep_alive, creator, create_time, updater, 
    update_time, deleted
) VALUES (
    '分类管理导出', 
    'system:category:manager:export', 
    3, 
    5, 
    @menu_id,
    '', 
    '', 
    '', 
    '', 
    0, 
    1, 
    1, 
    '1', 
    NOW(), 
    '1', 
    NOW(), 
    0
); 