-- 添加数据结构分析工具菜单（修复版）

-- 先检查系统管理菜单是否存在
SELECT id, name, parent_id, path, visible, status, deleted 
FROM system_menu 
WHERE name LIKE '%系统%' OR name LIKE '%System%' 
ORDER BY id;

-- 获取系统管理的菜单ID（多种方式查找）
SET @system_menu_id = (
    SELECT id FROM system_menu 
    WHERE (name = '系统管理' OR name = 'System' OR path = '/system') 
    AND deleted = 0 
    AND status = 1 
    LIMIT 1
);

-- 如果没找到系统管理菜单，使用ID=1（通常是系统管理）
SET @parent_id = COALESCE(@system_menu_id, 1);

-- 显示将要使用的父级菜单
SELECT @parent_id as parent_menu_id, 
       (SELECT name FROM system_menu WHERE id = @parent_id) as parent_menu_name;

-- 检查是否已经存在同名菜单
SELECT COUNT(*) as existing_count 
FROM system_menu 
WHERE name = '数据结构分析器' AND deleted = 0;

-- 删除可能存在的重复菜单
DELETE FROM system_menu 
WHERE name = '数据结构分析器' AND deleted = 0;

-- 添加数据结构分析工具菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name,
    status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted, tenant_id
) VALUES (
    '数据结构分析器', 
    'system:category:query', 
    2,                              -- type: 2=菜单
    11,                             -- sort: 排序
    @parent_id,                     -- parent_id: 父菜单ID
    'data-structure-analyzer',      -- path: 路由路径
    'data-board',                   -- icon: 图标（使用更常见的图标）
    'system/category/DataStructureAnalyzer',  -- component: 组件路径
    'DataStructureAnalyzer',        -- component_name: 组件名称
    1,                              -- status: 1=正常
    1,                              -- visible: 1=显示
    1,                              -- keep_alive: 1=缓存
    0,                              -- always_show: 0=自动
    '1',                            -- creator: 创建者
    NOW(),                          -- create_time: 创建时间
    '1',                            -- updater: 更新者  
    NOW(),                          -- update_time: 更新时间
    0,                              -- deleted: 0=未删除
    1                               -- tenant_id: 租户ID
);

-- 获取刚创建的菜单ID
SET @analyzer_menu_id = LAST_INSERT_ID();

-- 显示创建的菜单信息
SELECT @analyzer_menu_id as new_menu_id,
       'Menu created successfully' as status;

-- 为超级管理员角色分配权限（role_id = 1）
INSERT IGNORE INTO system_role_menu (
    role_id, menu_id, creator, create_time, updater, update_time, deleted, tenant_id
) VALUES (
    1,                     -- role_id: 超级管理员
    @analyzer_menu_id,     -- menu_id: 刚创建的菜单ID
    '1',                   -- creator
    NOW(),                 -- create_time
    '1',                   -- updater
    NOW(),                 -- update_time
    0,                     -- deleted: 0=未删除
    1                      -- tenant_id: 租户ID
);

-- 也为其他可能的管理员角色分配权限
INSERT IGNORE INTO system_role_menu (
    role_id, menu_id, creator, create_time, updater, update_time, deleted, tenant_id
)
SELECT DISTINCT 
    r.id as role_id,
    @analyzer_menu_id as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted,
    1 as tenant_id
FROM system_role r
WHERE r.code IN ('super_admin', 'admin', 'system_admin')
AND r.deleted = 0
AND r.status = 1
AND NOT EXISTS (
    SELECT 1 FROM system_role_menu rm 
    WHERE rm.role_id = r.id 
    AND rm.menu_id = @analyzer_menu_id 
    AND rm.deleted = 0
);

-- 验证菜单创建结果
SELECT 
    m.id, 
    m.name, 
    m.permission, 
    m.path, 
    m.component, 
    m.status,
    m.visible,
    m.parent_id,
    pm.name as parent_name,
    m.deleted
FROM system_menu m
LEFT JOIN system_menu pm ON m.parent_id = pm.id
WHERE m.name = '数据结构分析器' 
ORDER BY m.id DESC
LIMIT 1;

-- 验证权限分配结果
SELECT 
    rm.role_id,
    r.name as role_name,
    rm.menu_id,
    m.name as menu_name
FROM system_role_menu rm
JOIN system_role r ON rm.role_id = r.id
JOIN system_menu m ON rm.menu_id = m.id
WHERE m.name = '数据结构分析器'
AND rm.deleted = 0;

-- 显示系统管理下的所有子菜单（用于对比）
SELECT 
    id, name, path, sort, visible, status, deleted
FROM system_menu 
WHERE parent_id = @parent_id 
AND deleted = 0
ORDER BY sort, id; 