-- 业务建模菜单配置SQL脚本
-- 适用于MySQL数据库

-- 1. 添加主菜单：业务建模
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '业务建模', 'business:modeling:view', 1, 900, 0, '/business-modeling', 'ep:setting', 'Layout', NULL, 0, 1, 1, 1, 'admin', NOW(), 'admin', NOW(), 0
);

-- 获取刚插入的主菜单ID
SET @parent_menu_id = LAST_INSERT_ID();

-- 2. 添加子菜单：业务建模工作台
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '业务建模工作台', 'business:modeling:workspace', 2, 1, @parent_menu_id, '', 'ep:setting', 'business-module/BusinessModeling', 'BusinessModelingHome', 0, 1, 1, 0, 'admin', NOW(), 'admin', NOW(), 0
);

-- 3. 添加子菜单：页面生成器
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '页面生成器', 'business:modeling:generator', 2, 2, @parent_menu_id, 'page-generator', 'ep:magic-stick', 'business-module/page-generator/index', 'PageGenerator', 0, 1, 1, 0, 'admin', NOW(), 'admin', NOW(), 0
);

-- 4. 添加子菜单：页面配置（隐藏）
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '页面配置', 'business:modeling:configure', 2, 3, @parent_menu_id, 'page-generator/configure', 'ep:setting', 'business-module/page-generator/configure', 'PageGeneratorConfigure', 0, 0, 1, 0, 'admin', NOW(), 'admin', NOW(), 0
);

-- 5. 为超级管理员角色分配权限（假设角色ID为1）
INSERT INTO system_role_menu (role_id, menu_id, creator, create_time, updater, update_time, deleted)
SELECT 1, id, 'admin', NOW(), 'admin', NOW(), 0
FROM system_menu 
WHERE permission IN ('business:modeling:view', 'business:modeling:workspace', 'business:modeling:generator', 'business:modeling:configure')
AND deleted = 0;

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
    status,
    visible
FROM system_menu 
WHERE permission LIKE 'business:modeling:%' 
AND deleted = 0
ORDER BY parent_id, sort; 