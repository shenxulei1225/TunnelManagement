-- SuperTree 字段分类管理菜单配置
-- 说明：为SuperTree替代El-Tree的字段分类管理页面创建菜单

-- 1. 新增SuperTree字段分类管理主菜单
INSERT INTO system_menu (
    name,
    permission,
    type,
    parent_id,
    path,
    component,
    component_name,
    icon,
    sort,
    status,
    visible,
    keep_alive,
    create_time,
    creator
) VALUES (
    'SuperTree字段分类管理',
    'system:field-category-super:query',
    2,
    1,
    'field-category-super',
    'system/field/FieldCategory/SuperTreeIndex',
    'SystemFieldCategorySuper',
    'ep:collection',
    15,
    1,
    true,
    true,
    NOW(),
    '系统'
);

-- 获取刚插入的主菜单ID
SET @super_tree_menu_id = LAST_INSERT_ID();

-- 2. 创建SuperTree字段分类管理权限按钮
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('SuperTree字段分类查询', 'system:field-category-super:query', 3, @super_tree_menu_id, 1, 1, NOW(), '系统'),
('SuperTree字段分类新增', 'system:field-category-super:create', 3, @super_tree_menu_id, 2, 1, NOW(), '系统'),
('SuperTree字段分类修改', 'system:field-category-super:update', 3, @super_tree_menu_id, 3, 1, NOW(), '系统'),
('SuperTree字段分类删除', 'system:field-category-super:delete', 3, @super_tree_menu_id, 4, 1, NOW(), '系统'),
('SuperTree字段分类导入', 'system:field-category-super:import', 3, @super_tree_menu_id, 5, 1, NOW(), '系统'),
('SuperTree字段分类导出', 'system:field-category-super:export', 3, @super_tree_menu_id, 6, 1, NOW(), '系统');

-- 3. SuperTree 增强功能权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('SuperTree拖拽排序', 'system:field-category-super:drag-sort', 3, @super_tree_menu_id, 10, 1, NOW(), '系统'),
('SuperTree右键菜单', 'system:field-category-super:context-menu', 3, @super_tree_menu_id, 11, 1, NOW(), '系统'),
('SuperTree主题切换', 'system:field-category-super:theme-switch', 3, @super_tree_menu_id, 12, 1, NOW(), '系统'),
('SuperTree配置管理', 'system:field-category-super:config', 3, @super_tree_menu_id, 13, 1, NOW(), '系统'),
('SuperTree高级搜索', 'system:field-category-super:advanced-search', 3, @super_tree_menu_id, 14, 1, NOW(), '系统');

-- 4. 新增SuperTree演示页面菜单
INSERT INTO system_menu (
    name,
    permission,
    type,
    parent_id,
    path,
    component,
    component_name,
    icon,
    sort,
    status,
    visible,
    keep_alive,
    create_time,
    creator
) VALUES (
    'SuperTree对比演示',
    'system:super-tree:demo',
    2,
    1,
    'super-tree-demo',
    'system/field/FieldCategory/SuperTreeDemo',
    'SuperTreeDemo',
    'ep:data-analysis',
    16,
    1,
    true,
    true,
    NOW(),
    '系统'
);

-- 获取演示页面菜单ID
SET @demo_menu_id = LAST_INSERT_ID();

-- 5. 演示页面权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('SuperTree演示查看', 'system:super-tree:demo:view', 3, @demo_menu_id, 1, 1, NOW(), '系统'),
('SuperTree功能测试', 'system:super-tree:demo:test', 3, @demo_menu_id, 2, 1, NOW(), '系统'),
('SuperTree配置导出', 'system:super-tree:demo:export-config', 3, @demo_menu_id, 3, 1, NOW(), '系统'),
('SuperTree事件监控', 'system:super-tree:demo:event-monitor', 3, @demo_menu_id, 4, 1, NOW(), '系统');

-- 6. 分配菜单权限给管理员角色（角色ID为1）
-- 主菜单权限
INSERT INTO system_role_menu (role_id, menu_id) 
SELECT 1, id FROM system_menu 
WHERE id = @super_tree_menu_id 
   OR parent_id = @super_tree_menu_id 
   OR id = @demo_menu_id 
   OR parent_id = @demo_menu_id;

-- 7. 创建菜单目录分组（可选）
INSERT INTO system_menu (
    name,
    permission,
    type,
    parent_id,
    path,
    component,
    component_name,
    icon,
    sort,
    status,
    visible,
    keep_alive,
    create_time,
    creator
) VALUES (
    'SuperTree组件',
    '',
    1,
    0,
    '/super-tree',
    'Layout',
    'SuperTreeLayout',
    'ep:cpu',
    100,
    1,
    true,
    false,
    NOW(),
    '系统'
);

-- 获取组件目录ID
SET @component_dir_id = LAST_INSERT_ID();

-- 8. 将SuperTree相关菜单移动到组件目录下（可选）
UPDATE system_menu 
SET parent_id = @component_dir_id 
WHERE id IN (@super_tree_menu_id, @demo_menu_id);

-- 9. 为组件目录分配权限
INSERT INTO system_role_menu (role_id, menu_id) VALUES (1, @component_dir_id);

-- 10. 创建详细的字段关联权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('字段关联管理', 'system:field-category-super:relation:manage', 3, @super_tree_menu_id, 20, 1, NOW(), '系统'),
('字段批量关联', 'system:field-category-super:relation:batch-link', 3, @super_tree_menu_id, 21, 1, NOW(), '系统'),
('字段批量解除', 'system:field-category-super:relation:batch-unlink', 3, @super_tree_menu_id, 22, 1, NOW(), '系统'),
('字段排序调整', 'system:field-category-super:relation:sort', 3, @super_tree_menu_id, 23, 1, NOW(), '系统'),
('字段分组统计', 'system:field-category-super:relation:statistics', 3, @super_tree_menu_id, 24, 1, NOW(), '系统');

-- 11. SuperTree插件权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('SuperTree搜索插件', 'system:field-category-super:plugin:search', 3, @super_tree_menu_id, 30, 1, NOW(), '系统'),
('SuperTree拖拽插件', 'system:field-category-super:plugin:drag', 3, @super_tree_menu_id, 31, 1, NOW(), '系统'),
('SuperTree菜单插件', 'system:field-category-super:plugin:context-menu', 3, @super_tree_menu_id, 32, 1, NOW(), '系统'),
('SuperTree虚拟滚动', 'system:field-category-super:plugin:virtual-scroll', 3, @super_tree_menu_id, 33, 1, NOW(), '系统'),
('SuperTree持久化', 'system:field-category-super:plugin:persistence', 3, @super_tree_menu_id, 34, 1, NOW(), '系统');

-- 12. 分配新增的权限给管理员角色
INSERT INTO system_role_menu (role_id, menu_id) 
SELECT 1, id FROM system_menu 
WHERE parent_id = @super_tree_menu_id 
  AND id NOT IN (
    SELECT menu_id FROM system_role_menu WHERE role_id = 1
  );

-- 13. 验证菜单创建结果
SELECT 
    m.id,
    m.name,
    m.permission,
    m.type,
    CASE m.type 
        WHEN 1 THEN '目录'
        WHEN 2 THEN '菜单'
        WHEN 3 THEN '按钮'
        ELSE '未知'
    END as type_name,
    m.parent_id,
    p.name as parent_name,
    m.sort,
    CASE m.status 
        WHEN 1 THEN '启用'
        WHEN 0 THEN '禁用'
        ELSE '未知'
    END as status_name
FROM system_menu m
LEFT JOIN system_menu p ON m.parent_id = p.id
WHERE m.name LIKE '%SuperTree%' 
   OR m.name LIKE '%super-tree%'
   OR m.component LIKE '%SuperTree%'
ORDER BY m.parent_id, m.sort;

-- 输出创建的菜单ID信息
SELECT 
    'SuperTree字段分类管理主菜单ID' as menu_type,
    @super_tree_menu_id as menu_id
UNION ALL
SELECT 
    'SuperTree演示页面菜单ID' as menu_type,
    @demo_menu_id as menu_id
UNION ALL
SELECT 
    'SuperTree组件目录ID' as menu_type,
    @component_dir_id as menu_id; 