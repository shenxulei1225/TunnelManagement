-- 迁移test服务到tree服务的SQL脚本
-- 1. 在system_tree_config中注册test_tree类型
INSERT IGNORE INTO system_tree_config (
    id, tree_type, tree_name, description, allowed_data_types, 
    max_level, node_name_label, node_code_label, sort_type, status, config_json,
    creator, create_time, updater, update_time, deleted, tenant_id
) VALUES (
    1001, 'test_tree', '测试树结构', '用于测试和开发的树形结构管理', 
    '["test_node", "test_data"]', 10, '节点名称', '节点编码', 1, 1, 
    '{"supportBusinessType": true, "defaultSort": "asc", "enableDrag": true}',
    '1', NOW(), '1', NOW(), 0, 1
);

-- 2. 迁移现有test_tree_node数据到tree_data_relation
-- 注意：这里假设现有的test_tree_node作为树节点结构，需要先创建对应的树节点
-- 然后建立关联关系

-- 3. 为tree服务添加相应的菜单权限
INSERT IGNORE INTO system_menu (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name,
    status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES 
(2010, '树结构管理', 'system:tree:query', 2, 1, 1, 'tree', 'tree', 'system/tree/index', 'SystemTree', 1, 1, 1, 0, '1', NOW(), '1', NOW(), 0),
(2011, '树结构查询', 'system:tree:query', 3, 1, 2010, '', '', '', '', 1, 1, 1, 0, '1', NOW(), '1', NOW(), 0),
(2012, '树结构创建', 'system:tree:create', 3, 2, 2010, '', '', '', '', 1, 1, 1, 0, '1', NOW(), '1', NOW(), 0),
(2013, '树结构修改', 'system:tree:update', 3, 3, 2010, '', '', '', '', 1, 1, 1, 0, '1', NOW(), '1', NOW(), 0),
(2014, '树结构删除', 'system:tree:delete', 3, 4, 2010, '', '', '', '', 1, 1, 1, 0, '1', NOW(), '1', NOW(), 0);

-- 4. 为管理员角色分配tree权限
INSERT IGNORE INTO system_role_menu (id, role_id, menu_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 
    NULL as id,
    1 as role_id,  -- 超级管理员角色
    m.id as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted,
    1 as tenant_id
FROM system_menu m
WHERE m.permission LIKE 'system:tree:%'
AND NOT EXISTS (
    SELECT 1 FROM system_role_menu rm 
    WHERE rm.role_id = 1 AND rm.menu_id = m.id AND rm.deleted = 0
); 