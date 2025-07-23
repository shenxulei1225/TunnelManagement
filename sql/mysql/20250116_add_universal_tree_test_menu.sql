-- 添加 UniversalTreeView 测试页面菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, 
    visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    'UniversalTreeView测试', 'system:category:universal-tree-test', 2, 999, 1, 'universal-tree-test', 'ep:cpu', 
    'system/category/UniversalTreeViewTest', 'UniversalTreeViewTest', 0, 
    1, 1, 0, 'admin', NOW(), 'admin', NOW(), 0
);

-- 验证插入结果
SELECT 
    id, name, path, component, status,
    '菜单已创建，可通过 /universal-tree-test 访问' as note
FROM system_menu 
WHERE name = 'UniversalTreeView测试'; 