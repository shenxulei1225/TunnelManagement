-- SuperTree 配置器菜单配置
-- 添加 SuperTree 配置器到系统管理菜单下

-- 1. 获取系统管理菜单ID（假设为1）
SELECT @system_menu_id := id FROM system_menu WHERE name = '系统管理' AND type = 1 LIMIT 1;

-- 2. 插入 SuperTree 配置器菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status,
    visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    'SuperTree配置器',
    'system:supertree-config:query',
    2,  -- 菜单类型
    999, -- 排序
    IFNULL(@system_menu_id, 1), -- 父菜单ID
    'super-tree-config',
    'ep:setting',
    'test/SuperTreeConfig',
    'SuperTreeConfig',
    0,  -- 状态：0=正常
    1,  -- 可见：1=显示
    1,  -- 缓存：1=缓存
    0,  -- 总是显示：0=否
    'system',
    NOW(),
    'system', 
    NOW(),
    0   -- 未删除
);

-- 3. 获取刚插入的菜单ID
SELECT @config_menu_id := LAST_INSERT_ID();

-- 4. 添加权限数据（如果有角色权限系统）
-- INSERT INTO system_role_menu (role_id, menu_id, creator, create_time, updater, update_time, deleted)
-- SELECT 1, @config_menu_id, 'system', NOW(), 'system', NOW(), 0
-- WHERE EXISTS (SELECT 1 FROM system_role WHERE id = 1);

-- 5. 验证插入结果
SELECT 
    id,
    name,
    path,
    component,
    icon,
    sort,
    parent_id,
    status
FROM system_menu 
WHERE name = 'SuperTree配置器' 
AND deleted = 0; 