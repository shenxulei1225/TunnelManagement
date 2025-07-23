-- 添加分类管理中心菜单
-- 整合了树形展示、API测试、数据分析等功能的综合管理页面

INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '分类管理中心', 'system:category:query', 2, 30, 1, 'category-manager', 'ep:management', 'system/category/CategoryManager', 'CategoryManager', 0, true, true, false, '1', NOW(), '1', NOW(), false
);

-- 添加分类管理中心权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES 
-- 基本权限
('分类查询', 'system:category:query', 3, 1, (SELECT id FROM system_menu WHERE name = '分类管理中心' AND parent_id = 1 ORDER BY id DESC LIMIT 1), '', '', '', '', 0, false, false, false, '1', NOW(), '1', NOW(), false),
('分类创建', 'system:category:create', 3, 2, (SELECT id FROM system_menu WHERE name = '分类管理中心' AND parent_id = 1 ORDER BY id DESC LIMIT 1), '', '', '', '', 0, false, false, false, '1', NOW(), '1', NOW(), false),
('分类更新', 'system:category:update', 3, 3, (SELECT id FROM system_menu WHERE name = '分类管理中心' AND parent_id = 1 ORDER BY id DESC LIMIT 1), '', '', '', '', 0, false, false, false, '1', NOW(), '1', NOW(), false),
('分类删除', 'system:category:delete', 3, 4, (SELECT id FROM system_menu WHERE name = '分类管理中心' AND parent_id = 1 ORDER BY id DESC LIMIT 1), '', '', '', '', 0, false, false, false, '1', NOW(), '1', NOW(), false);

-- 输出菜单ID以便后续引用
SELECT id as category_manager_menu_id FROM system_menu WHERE name = '分类管理中心' AND parent_id = 1 ORDER BY id DESC LIMIT 1; 