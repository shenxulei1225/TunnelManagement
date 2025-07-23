-- 添加树构建测试页面菜单
-- 用于测试修复后的树构建逻辑

INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '树构建测试', '', 2, 31, 1, 'tree-build-test', 'ep:cpu', 'system/category/TreeBuildTest', 'TreeBuildTest', 0, true, false, false, '1', NOW(), '1', NOW(), false
);

-- 获取插入的菜单ID (通过查询最后插入的记录)
-- SELECT LAST_INSERT_ID() as menu_id;

-- 手动设置菜单ID (如果需要)
-- UPDATE system_menu SET id = 1296 WHERE name = '树构建测试' AND parent_id = 1; 