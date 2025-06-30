-- UI转换器模块菜单配置
-- 用于快速创建UI转换器相关的菜单和权限
-- 执行前请确保已登录正确的租户

-- 1. 创建主菜单 - UI转换器
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, 
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    'UI转换器', '', 1, 6000, 0, 'ui-converter', 'ep:magic-stick',
    '', '', 0, 1, 0, 1,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 获取主菜单ID (假设为6000，实际需要根据插入结果调整)
SET @main_menu_id = (SELECT id FROM `system_menu` WHERE `name` = 'UI转换器' AND `parent_id` = 0 ORDER BY id DESC LIMIT 1);

-- 2. 创建子菜单
-- 2.1 资源导入
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '资源导入', 'ui-converter:import:query', 2, 6010, @main_menu_id, 'import', 'ep:upload',
    'ui-converter/import/index', 'UIConverterImport', 0, 1, 1, 0,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 2.2 组件树管理
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '组件树管理', 'ui-converter:component-tree:query', 2, 6020, @main_menu_id, 'component-tree', 'ep:tree',
    'ui-converter/component-tree/index', 'UIConverterComponentTree', 0, 1, 1, 0,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 2.3 可视化设计器
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '可视化设计器', 'ui-converter:design-canvas:query', 2, 6030, @main_menu_id, 'design-canvas', 'ep:edit',
    'ui-converter/design-canvas/index', 'UIConverterDesignCanvas', 0, 1, 1, 0,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 2.4 属性编辑器
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '属性编辑器', 'ui-converter:property-panel:query', 2, 6040, @main_menu_id, 'property-panel', 'ep:setting',
    'ui-converter/property-panel/index', 'UIConverterPropertyPanel', 0, 1, 1, 0,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 2.5 代码导出
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '代码导出', 'ui-converter:export:query', 2, 6050, @main_menu_id, 'export', 'ep:download',
    'ui-converter/export/index', 'UIConverterExport', 0, 1, 1, 0,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 2.6 预览中心
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '预览中心', 'ui-converter:preview:query', 2, 6060, @main_menu_id, 'preview', 'ep:view',
    'ui-converter/preview/index', 'UIConverterPreview', 0, 1, 1, 0,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 3. 为每个子菜单创建基础权限按钮
-- 3.1 资源导入权限
SET @import_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '资源导入' AND `parent_id` = @main_menu_id ORDER BY id DESC LIMIT 1);

INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询', 'ui-converter:import:query', 3, 1, @import_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增', 'ui-converter:import:create', 3, 2, @import_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('编辑', 'ui-converter:import:update', 3, 3, @import_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除', 'ui-converter:import:delete', 3, 4, @import_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('导出', 'ui-converter:import:export', 3, 5, @import_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 3.2 组件树管理权限
SET @component_tree_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '组件树管理' AND `parent_id` = @main_menu_id ORDER BY id DESC LIMIT 1);

INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询', 'ui-converter:component-tree:query', 3, 1, @component_tree_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增', 'ui-converter:component-tree:create', 3, 2, @component_tree_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('编辑', 'ui-converter:component-tree:update', 3, 3, @component_tree_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除', 'ui-converter:component-tree:delete', 3, 4, @component_tree_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('导出', 'ui-converter:component-tree:export', 3, 5, @component_tree_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 3.3 可视化设计器权限
SET @design_canvas_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '可视化设计器' AND `parent_id` = @main_menu_id ORDER BY id DESC LIMIT 1);

INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询', 'ui-converter:design-canvas:query', 3, 1, @design_canvas_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增', 'ui-converter:design-canvas:create', 3, 2, @design_canvas_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('编辑', 'ui-converter:design-canvas:update', 3, 3, @design_canvas_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除', 'ui-converter:design-canvas:delete', 3, 4, @design_canvas_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('导出', 'ui-converter:design-canvas:export', 3, 5, @design_canvas_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 3.4 属性编辑器权限
SET @property_panel_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '属性编辑器' AND `parent_id` = @main_menu_id ORDER BY id DESC LIMIT 1);

INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询', 'ui-converter:property-panel:query', 3, 1, @property_panel_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增', 'ui-converter:property-panel:create', 3, 2, @property_panel_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('编辑', 'ui-converter:property-panel:update', 3, 3, @property_panel_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除', 'ui-converter:property-panel:delete', 3, 4, @property_panel_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('导出', 'ui-converter:property-panel:export', 3, 5, @property_panel_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 3.5 代码导出权限
SET @export_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '代码导出' AND `parent_id` = @main_menu_id ORDER BY id DESC LIMIT 1);

INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询', 'ui-converter:export:query', 3, 1, @export_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增', 'ui-converter:export:create', 3, 2, @export_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('编辑', 'ui-converter:export:update', 3, 3, @export_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除', 'ui-converter:export:delete', 3, 4, @export_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('导出', 'ui-converter:export:export', 3, 5, @export_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 3.6 预览中心权限
SET @preview_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '预览中心' AND `parent_id` = @main_menu_id ORDER BY id DESC LIMIT 1);

INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询', 'ui-converter:preview:query', 3, 1, @preview_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增', 'ui-converter:preview:create', 3, 2, @preview_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('编辑', 'ui-converter:preview:update', 3, 3, @preview_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除', 'ui-converter:preview:delete', 3, 4, @preview_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('导出', 'ui-converter:preview:export', 3, 5, @preview_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 提交事务
COMMIT;

-- 显示创建结果
SELECT 
    m.id,
    m.name,
    m.type,
    CASE 
        WHEN m.type = 1 THEN '目录'
        WHEN m.type = 2 THEN '菜单'
        WHEN m.type = 3 THEN '按钮'
        ELSE '未知'
    END as type_name,
    m.sort,
    m.path,
    m.permission,
    m.component
FROM system_menu m 
WHERE m.name LIKE '%UI转换器%' 
   OR m.name IN ('资源导入', '组件树管理', '可视化设计器', '属性编辑器', '代码导出', '预览中心')
   OR m.parent_id IN (
       SELECT id FROM system_menu WHERE name = 'UI转换器' OR name IN ('资源导入', '组件树管理', '可视化设计器', '属性编辑器', '代码导出', '预览中心')
   )
ORDER BY m.sort, m.id; 