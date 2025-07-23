-- SuperForm表单组件测试页面菜单
-- 用于在菜单管理中添加SuperForm组件测试页面

-- 插入SuperForm测试页面菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    'SuperForm表单组件测试',           -- 菜单名称
    'test:super-form:query',          -- 权限标识
    2,                                -- 菜单类型：2=菜单
    20,                               -- 排序号
    (SELECT id FROM system_menu WHERE path = '/test' AND deleted = 0 LIMIT 1), -- 父菜单ID（测试菜单）
    'super-form',                     -- 路由地址
    'document',                       -- 菜单图标
    'SuperFormTest',                  -- 组件名称（对应路由name）
    'SuperFormTest',                  -- 组件缓存名称
    0,                                -- 状态：0=正常
    1,                                -- 是否显示：1=显示
    1,                                -- 是否缓存：1=缓存
    0,                                -- 是否总是显示：0=否
    'admin',                          -- 创建者
    NOW(),                            -- 创建时间
    'admin',                          -- 更新者
    NOW(),                            -- 更新时间
    0                                 -- 是否删除：0=未删除
);

-- 验证插入结果
SELECT 
    id,
    name,
    permission,
    path,
    component,
    parent_id,
    sort,
    status
FROM system_menu 
WHERE name = 'SuperForm表单组件测试' 
  AND deleted = 0;

-- 显示完整的测试菜单树结构
SELECT 
    m1.id,
    m1.name AS menu_name,
    m1.path,
    m1.component,
    m1.sort,
    m2.name AS parent_name
FROM system_menu m1
LEFT JOIN system_menu m2 ON m1.parent_id = m2.id
WHERE (m1.path = '/test' OR m1.parent_id = (SELECT id FROM system_menu WHERE path = '/test' AND deleted = 0))
  AND m1.deleted = 0
ORDER BY m1.parent_id, m1.sort; 