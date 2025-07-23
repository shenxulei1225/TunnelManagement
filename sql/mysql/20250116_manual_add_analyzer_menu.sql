-- 手工添加数据结构分析器菜单（最简单直接的方式）

-- 直接插入菜单，假设系统管理的ID是1
INSERT INTO system_menu (
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
    visible, 
    keep_alive, 
    always_show, 
    creator, 
    create_time, 
    updater, 
    update_time, 
    deleted
) VALUES (
    '数据结构分析器',           -- name
    'system:category:query',    -- permission
    2,                          -- type (2=菜单)
    999,                        -- sort (使用999确保显示在最后)
    1,                          -- parent_id (假设系统管理ID是1)
    'data-structure-analyzer',  -- path
    'document',                 -- icon (使用简单的图标)
    'system/category/DataStructureAnalyzer',  -- component
    'DataStructureAnalyzer',    -- component_name
    1,                          -- status (1=启用)
    1,                          -- visible (1=显示)
    1,                          -- keep_alive
    0,                          -- always_show
    '1',                        -- creator
    NOW(),                      -- create_time
    '1',                        -- updater
    NOW(),                      -- update_time
    0                           -- deleted (0=未删除)
);

-- 获取刚插入的菜单ID
SET @new_menu_id = LAST_INSERT_ID();

-- 为超级管理员分配权限
INSERT INTO system_role_menu (
    role_id, 
    menu_id, 
    creator, 
    create_time, 
    updater, 
    update_time, 
    deleted
) VALUES (
    1,           -- role_id (超级管理员)
    @new_menu_id, -- menu_id
    '1',         -- creator
    NOW(),       -- create_time
    '1',         -- updater
    NOW(),       -- update_time
    0            -- deleted
);

-- 验证插入结果
SELECT 
    id, name, path, parent_id, sort, visible, status
FROM system_menu 
WHERE id = @new_menu_id;

-- 验证权限分配
SELECT 
    role_id, menu_id
FROM system_role_menu 
WHERE menu_id = @new_menu_id AND deleted = 0; 