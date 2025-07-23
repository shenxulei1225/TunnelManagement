-- 添加Tree Entity版分类管理页面菜单

-- 获取系统管理的菜单ID
SET @system_menu_id = (SELECT id FROM system_menu WHERE name = '系统管理' AND deleted = 0 LIMIT 1);

-- 如果没找到系统管理菜单，默认使用1
SET @parent_id = COALESCE(@system_menu_id, 1);

-- 添加Tree Entity版分类管理页面菜单
INSERT IGNORE INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name,
    status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '分类管理(Tree Entity)', 'system:category:query', 2, 8, @parent_id, 'category-tree-entity', 
    'tree', 'system/category/TreeEntityIndex', 'CategoryTreeEntity',
    1, 1, 1, 0, '1', NOW(), '1', NOW(), 0
);

-- 获取刚创建的菜单ID
SET @tree_entity_menu_id = LAST_INSERT_ID();

-- 为超级管理员角色分配权限
INSERT IGNORE INTO system_role_menu (role_id, menu_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 
    1 as role_id,
    @tree_entity_menu_id as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted,
    1 as tenant_id
WHERE @tree_entity_menu_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = 1 AND menu_id = @tree_entity_menu_id AND deleted = 0
);

-- 验证菜单创建结果
SELECT 
    id, name, permission, path, component, status
FROM system_menu 
WHERE name = '分类管理(Tree Entity)' 
AND deleted = 0; 