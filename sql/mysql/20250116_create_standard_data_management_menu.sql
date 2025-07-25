-- 创建StandardDataManagement模板菜单
-- 执行时间: 2025-01-16

-- 1. 创建模板中心菜单（如果不存在）
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2000, '模板中心', '', 1, 0, 0, '/template', 'ep:files', 'Layout', 'Template', 0, true, true, true, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 2. 创建StandardDataManagement模板组菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2001, 'StandardDataManagement模板', '', 1, 1, 2000, 'standard-data-management', 'ep:connection', 'Layout', 'StandardDataManagementGroup', 0, true, true, true, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 3. 创建模板主页面菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2002, '模板主页面', '', 2, 1, 2001, 'index', 'ep:connection', 'views/template/StandardDataManagement/index', 'StandardDataManagementTemplate', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 4. 创建使用示例菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2003, '使用示例', '', 2, 2, 2001, 'example', 'ep:data-analysis', 'views/template/StandardDataManagement/example', 'StandardDataManagementExample', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 5. 创建功能测试菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2004, '功能测试', '', 2, 3, 2001, 'test', 'ep:monitor', 'views/template/StandardDataManagement/test', 'StandardDataManagementTest', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 6. 创建业务实例菜单（设备管理示例）
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2005, '设备管理系统', '', 1, 2, 2000, 'device-management', 'ep:cpu', 'Layout', 'DeviceManagementGroup', 0, true, true, true, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 7. 创建设备分类管理菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2006, '设备分类管理', '', 2, 1, 2005, 'category', 'ep:folder', 'views/template/StandardDataManagement/index', 'DeviceCategoryManagement', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 8. 创建设备列表管理菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2007, '设备列表管理', '', 2, 2, 2005, 'device', 'ep:list', 'views/template/StandardDataManagement/index', 'DeviceListManagement', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 9. 创建产品管理示例菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2008, '产品管理系统', '', 1, 3, 2000, 'product-management', 'ep:goods', 'Layout', 'ProductManagementGroup', 0, true, true, true, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 10. 创建产品分类管理菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2009, '产品分类管理', '', 2, 1, 2008, 'category', 'ep:folder', 'views/template/StandardDataManagement/index', 'ProductCategoryManagement', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 11. 创建产品列表管理菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2010, '产品列表管理', '', 2, 2, 2008, 'product', 'ep:list', 'views/template/StandardDataManagement/index', 'ProductListManagement', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 12. 创建知识管理示例菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2011, '知识管理系统', '', 1, 4, 2000, 'knowledge-management', 'ep:document', 'Layout', 'KnowledgeManagementGroup', 0, true, true, true, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 13. 创建知识分类管理菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2012, '知识分类管理', '', 2, 1, 2011, 'category', 'ep:folder', 'views/template/StandardDataManagement/index', 'KnowledgeCategoryManagement', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 14. 创建知识条目管理菜单
INSERT INTO system_menu_config (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    2013, '知识条目管理', '', 2, 2, 2011, 'knowledge', 'ep:list', 'views/template/StandardDataManagement/index', 'KnowledgeItemManagement', 0, true, true, false, '1', NOW(), '1', NOW(), false
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    icon = VALUES(icon),
    component = VALUES(component),
    component_name = VALUES(component_name),
    update_time = NOW();

-- 查询创建的菜单
SELECT 
    id,
    name,
    parent_id,
    path,
    icon,
    component,
    component_name,
    sort,
    status,
    visible
FROM system_menu_config 
WHERE id >= 2000 AND id <= 2013
ORDER BY id; 