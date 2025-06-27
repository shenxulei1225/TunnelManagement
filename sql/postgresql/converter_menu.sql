-- UI转换器菜单配置SQL脚本
-- 适用于PostgreSQL数据库

-- 1. 添加主菜单：UI转换器
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    'UI转换器', 'converter:view', 1, 100, 0, '/converter', 'magic-stick', 'Layout', NULL, 0, true, true, true, 'admin', NOW(), 'admin', NOW(), false
) RETURNING id;

-- 2. 添加子菜单：转换演示（需要手动替换parent_id）
-- 请将下面的 {PARENT_MENU_ID} 替换为上面查询返回的实际ID
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '转换演示', 'converter:demo:view', 2, 1, {PARENT_MENU_ID}, '/converter/demo', 'cpu', 'converter/demo', 'ConverterDemo', 0, true, true, false, 'admin', NOW(), 'admin', NOW(), false
);

-- 3. 添加完整转换器页面
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '完整转换器', 'converter:full:view', 2, 2, {PARENT_MENU_ID}, '/converter/index', 'magic-stick', 'converter/ConverterView', 'ConverterView', 0, true, true, false, 'admin', NOW(), 'admin', NOW(), false
);

-- 4. 为超级管理员角色分配权限（假设角色ID为1）
INSERT INTO system_role_menu (role_id, menu_id, creator, create_time, updater, update_time, deleted)
SELECT 1, id, 'admin', NOW(), 'admin', NOW(), false
FROM system_menu 
WHERE permission IN ('converter:view', 'converter:demo:view', 'converter:full:view')
AND deleted = false;

-- 查询验证插入结果
SELECT 
    id,
    name,
    permission,
    type,
    sort,
    parent_id,
    path,
    icon,
    component,
    component_name,
    status
FROM system_menu 
WHERE permission LIKE 'converter:%' 
AND deleted = false
ORDER BY parent_id, sort; 