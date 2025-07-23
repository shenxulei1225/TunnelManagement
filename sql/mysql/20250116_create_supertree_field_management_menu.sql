-- ====================================================================
-- SuperTree 字段管理页面 - 完整菜单权限配置
-- 文件: 20250116_create_supertree_field_management_menu.sql
-- 创建日期: 2025-01-16
-- 说明: 为基于SuperTree组件的字段管理功能创建菜单和权限配置
-- ====================================================================

-- 0. 先获取系统管理菜单ID
SET @system_menu_id = (SELECT id FROM system_menu WHERE path = '/system' AND type = 1 LIMIT 1);

-- 1. 创建主菜单：SuperTree字段管理
INSERT INTO system_menu (
    name, 
    path, 
    component, 
    icon, 
    sort, 
    visible, 
    status, 
    type, 
    parent_id, 
    keep_alive,
    create_time,
    creator
) VALUES (
    'SuperTree字段管理',
    '/system/field-super-tree',
    '/system/field/FieldDef/FieldManagementWithSuperTree',
    'ep:folder-opened',
    15,
    true,
    1,
    2,  -- 菜单类型
    @system_menu_id,
    true,
    NOW(),
    '系统'
);

-- 获取刚创建的菜单ID
SET @main_menu_id = LAST_INSERT_ID();

-- 2. 创建功能权限按钮
-- 2.1 字段管理基础权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('字段管理查询', 'system:field-super:query', 3, @main_menu_id, 1, 1, NOW(), '系统'),
('字段管理新增', 'system:field-super:create', 3, @main_menu_id, 2, 1, NOW(), '系统'),
('字段管理修改', 'system:field-super:update', 3, @main_menu_id, 3, 1, NOW(), '系统'),
('字段管理删除', 'system:field-super:delete', 3, @main_menu_id, 4, 1, NOW(), '系统'),
('字段管理导入', 'system:field-super:import', 3, @main_menu_id, 5, 1, NOW(), '系统'),
('字段管理导出', 'system:field-super:export', 3, @main_menu_id, 6, 1, NOW(), '系统');

-- 2.2 字段分组管理权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('字段分组查询', 'system:field-super:category:query', 3, @main_menu_id, 10, 1, NOW(), '系统'),
('字段分组新增', 'system:field-super:category:create', 3, @main_menu_id, 11, 1, NOW(), '系统'),
('字段分组修改', 'system:field-super:category:update', 3, @main_menu_id, 12, 1, NOW(), '系统'),
('字段分组删除', 'system:field-super:category:delete', 3, @main_menu_id, 13, 1, NOW(), '系统'),
('字段分组排序', 'system:field-super:category:sort', 3, @main_menu_id, 14, 1, NOW(), '系统');

-- 2.3 SuperTree 特有功能权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('树形拖拽操作', 'system:field-super:drag', 3, @main_menu_id, 20, 1, NOW(), '系统'),
('树形搜索功能', 'system:field-super:search', 3, @main_menu_id, 21, 1, NOW(), '系统'),
('右键菜单操作', 'system:field-super:context-menu', 3, @main_menu_id, 22, 1, NOW(), '系统'),
('树形配置管理', 'system:field-super:config', 3, @main_menu_id, 23, 1, NOW(), '系统'),
('主题切换功能', 'system:field-super:theme', 3, @main_menu_id, 24, 1, NOW(), '系统');

-- 2.4 字段关联管理权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('字段分组关联', 'system:field-super:relation:link', 3, @main_menu_id, 30, 1, NOW(), '系统'),
('字段分组解除', 'system:field-super:relation:unlink', 3, @main_menu_id, 31, 1, NOW(), '系统'),
('批量关联操作', 'system:field-super:relation:batch', 3, @main_menu_id, 32, 1, NOW(), '系统'),
('关联关系查看', 'system:field-super:relation:view', 3, @main_menu_id, 33, 1, NOW(), '系统');

-- 2.5 高级功能权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('字段验证管理', 'system:field-super:validation', 3, @main_menu_id, 40, 1, NOW(), '系统'),
('字段模板管理', 'system:field-super:template', 3, @main_menu_id, 41, 1, NOW(), '系统'),
('字段统计分析', 'system:field-super:analytics', 3, @main_menu_id, 42, 1, NOW(), '系统'),
('字段历史记录', 'system:field-super:history', 3, @main_menu_id, 43, 1, NOW(), '系统');

-- 3. 创建演示页面菜单
INSERT INTO system_menu (
    name, 
    path, 
    component, 
    icon, 
    sort, 
    visible, 
    status, 
    type, 
    parent_id, 
    keep_alive,
    create_time,
    creator
) VALUES (
    'SuperTree组件演示',
    '/system/super-tree-demo',
    '/system/field/FieldDef/SuperTreeDemo',
    'ep:magic-stick',
    16,
    true,
    1,
    2,  -- 菜单类型
    @system_menu_id,
    true,
    NOW(),
    '系统'
);

-- 获取演示页面菜单ID
SET @demo_menu_id = LAST_INSERT_ID();

-- 演示页面权限
INSERT INTO system_menu (name, permission, type, parent_id, sort, status, create_time, creator) VALUES
('组件演示查看', 'system:super-tree:demo:view', 3, @demo_menu_id, 1, 1, NOW(), '系统'),
('配置导出功能', 'system:super-tree:demo:export', 3, @demo_menu_id, 2, 1, NOW(), '系统'),
('配置导入功能', 'system:super-tree:demo:import', 3, @demo_menu_id, 3, 1, NOW(), '系统'),
('预设模板使用', 'system:super-tree:demo:preset', 3, @demo_menu_id, 4, 1, NOW(), '系统');

-- 4. 获取管理员角色ID
SET @admin_role_id = (SELECT id FROM system_role WHERE code = 'admin' LIMIT 1);

-- 5. 分配菜单权限给管理员角色
-- 5.1 分配主菜单给管理员
INSERT INTO system_role_menu (role_id, menu_id, creator, create_time) VALUES
(@admin_role_id, @main_menu_id, '系统', NOW());

-- 5.2 分配所有子权限给管理员
INSERT INTO system_role_menu (role_id, menu_id, creator, create_time)
SELECT 
    @admin_role_id as role_id,
    id as menu_id,
    '系统' as creator,
    NOW() as create_time
FROM system_menu 
WHERE parent_id = @main_menu_id;

-- 5.3 分配演示页面给管理员
INSERT INTO system_role_menu (role_id, menu_id, creator, create_time) VALUES
(@admin_role_id, @demo_menu_id, '系统', NOW());

-- 5.4 分配演示页面子权限给管理员
INSERT INTO system_role_menu (role_id, menu_id, creator, create_time)
SELECT 
    @admin_role_id as role_id,
    id as menu_id,
    '系统' as creator,
    NOW() as create_time
FROM system_menu 
WHERE parent_id = @demo_menu_id;

-- 6. 分配菜单权限给其他系统管理员角色（如果存在）
INSERT IGNORE INTO system_role_menu (role_id, menu_id, creator, create_time)
SELECT 
    sr.id as role_id,
    sm.id as menu_id,
    '系统' as creator,
    NOW() as create_time
FROM system_role sr
CROSS JOIN system_menu sm
WHERE sr.code IN ('system', 'super-admin') 
AND (sm.id = @main_menu_id OR sm.parent_id = @main_menu_id OR sm.id = @demo_menu_id OR sm.parent_id = @demo_menu_id);

-- 7. 创建菜单配置记录表（用于存储页面配置信息）
CREATE TABLE IF NOT EXISTS `system_menu_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    `config_key` varchar(100) NOT NULL COMMENT '配置键',
    `config_value` text COMMENT '配置值',
    `config_type` varchar(50) DEFAULT 'string' COMMENT '配置类型',
    `description` varchar(500) COMMENT '配置说明',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_menu_config` (`menu_id`, `config_key`, `deleted`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB COMMENT='菜单配置信息表';

-- 8. 插入SuperTree字段管理页面的配置信息
INSERT INTO system_menu_config (menu_id, config_key, config_value, config_type, description, creator) VALUES
(@main_menu_id, 'component_type', 'SuperTree', 'string', 'SuperTree通用树组件', '系统'),
(@main_menu_id, 'theme_default', 'default', 'string', '默认主题配置', '系统'),
(@main_menu_id, 'plugins_enabled', '["search", "drag", "contextMenu"]', 'json', '启用的插件列表', '系统'),
(@main_menu_id, 'api_base_url', '/api/system/field-category', 'string', 'API基础路径', '系统'),
(@main_menu_id, 'persistence_key', 'supertree_field_management', 'string', '状态持久化键名', '系统'),
(@main_menu_id, 'max_tree_depth', '5', 'number', '最大树深度限制', '系统'),
(@main_menu_id, 'enable_virtual_scroll', 'false', 'boolean', '是否启用虚拟滚动', '系统'),
(@main_menu_id, 'default_expand_level', '2', 'number', '默认展开层级', '系统');

-- 插入演示页面的配置信息
INSERT INTO system_menu_config (menu_id, config_key, config_value, config_type, description, creator) VALUES
(@demo_menu_id, 'component_type', 'SuperTreeDemo', 'string', 'SuperTree演示组件', '系统'),
(@demo_menu_id, 'demo_themes', '["default", "dark", "business"]', 'json', '演示主题列表', '系统'),
(@demo_menu_id, 'demo_presets', '["fileManager", "organization", "fieldManagement"]', 'json', '演示预设列表', '系统'),
(@demo_menu_id, 'enable_config_export', 'true', 'boolean', '允许配置导出', '系统'),
(@demo_menu_id, 'enable_live_preview', 'true', 'boolean', '启用实时预览', '系统');

-- 9. 验证创建结果
SELECT 
    m.id,
    m.name,
    m.path,
    m.component,
    m.permission,
    m.type,
    CASE m.type 
        WHEN 1 THEN '目录'
        WHEN 2 THEN '菜单' 
        WHEN 3 THEN '按钮'
        ELSE '未知'
    END as type_name,
    m.sort,
    m.status,
    CASE m.status 
        WHEN 1 THEN '启用'
        WHEN 0 THEN '禁用'
        ELSE '未知'
    END as status_name
FROM system_menu m 
WHERE m.id = @main_menu_id 
   OR m.parent_id = @main_menu_id 
   OR m.id = @demo_menu_id 
   OR m.parent_id = @demo_menu_id
ORDER BY m.id, m.sort;

-- 10. 统计信息
SELECT 
    'SuperTree字段管理' as menu_name,
    COUNT(*) as total_permissions,
    SUM(CASE WHEN type = 2 THEN 1 ELSE 0 END) as menu_count,
    SUM(CASE WHEN type = 3 THEN 1 ELSE 0 END) as button_count
FROM system_menu 
WHERE id = @main_menu_id OR parent_id = @main_menu_id

UNION ALL

SELECT 
    'SuperTree组件演示' as menu_name,
    COUNT(*) as total_permissions,
    SUM(CASE WHEN type = 2 THEN 1 ELSE 0 END) as menu_count,
    SUM(CASE WHEN type = 3 THEN 1 ELSE 0 END) as button_count
FROM system_menu 
WHERE id = @demo_menu_id OR parent_id = @demo_menu_id;

-- ====================================================================
-- 执行完成提示
-- ====================================================================
SELECT 
    '✅ SuperTree字段管理菜单创建完成！' as message,
    CONCAT('主菜单ID: ', @main_menu_id) as main_menu_info,
    CONCAT('演示页面ID: ', @demo_menu_id) as demo_menu_info,
    '🎯 包含完整的权限配置和功能权限' as features,
    '📊 已分配给管理员角色' as role_assignment; 