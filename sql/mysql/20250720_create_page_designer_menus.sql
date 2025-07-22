-- 页面设计器菜单数据插入脚本

-- 1. 页面设计器主菜单
INSERT INTO `system_menu` (
  `id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, 
  `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
  `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
  6001, '页面设计器', 'system:page-designer:query', 2, 7, 1, 'page-designer', 'ep:edit-pen',
  'system/page-designer/index', 'PageDesigner', 0, 1, 1, 0,
  'admin', NOW(), 'admin', NOW(), 0
);

-- 2. 页面设计器权限按钮
INSERT INTO `system_menu` (
  `id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, 
  `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
  `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
-- 页面设计查询权限
(6002, '页面设计查询', 'system:page-designer:query', 3, 1, 6001, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
-- 页面设计新增权限
(6003, '页面设计新增', 'system:page-designer:create', 3, 2, 6001, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
-- 页面设计修改权限
(6004, '页面设计修改', 'system:page-designer:update', 3, 3, 6001, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
-- 页面设计删除权限
(6005, '页面设计删除', 'system:page-designer:delete', 3, 4, 6001, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
-- 页面构建权限
(6006, '页面构建', 'system:page-designer:build', 3, 5, 6001, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
-- 页面部署权限
(6007, '页面部署', 'system:page-designer:deploy', 3, 6, 6001, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 3. 页面模板主菜单
INSERT INTO `system_menu` (
  `id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, 
  `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
  `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
  6008, '页面模板', 'system:page-template:query', 2, 8, 1, 'page-template', 'ep:files',
  'system/page-template/index', 'PageTemplate', 0, 1, 1, 0,
  'admin', NOW(), 'admin', NOW(), 0
);

-- 4. 页面模板权限按钮
INSERT INTO `system_menu` (
  `id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, 
  `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
  `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
-- 页面模板查询权限
(6009, '页面模板查询', 'system:page-template:query', 3, 1, 6008, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
-- 页面模板新增权限
(6010, '页面模板新增', 'system:page-template:create', 3, 2, 6008, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
-- 页面模板修改权限
(6011, '页面模板修改', 'system:page-template:update', 3, 3, 6008, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
-- 页面模板删除权限
(6012, '页面模板删除', 'system:page-template:delete', 3, 4, 6008, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 菜单说明：
-- 1. 页面设计器主菜单 (ID: 6001)
--    - 路径: /system/page-designer
--    - 组件: system/page-designer/index
--    - 图标: ep:edit-pen
--    - 排序: 7 (在系统管理下)
--
-- 2. 页面设计器权限按钮 (ID: 6002-6007)
--    - 查询、新增、修改、删除、构建、部署权限
--    - 父菜单: 6001 (页面设计器)
--
-- 3. 页面模板主菜单 (ID: 6008)
--    - 路径: /system/page-template
--    - 组件: system/page-template/index
--    - 图标: ep:files
--    - 排序: 8 (在系统管理下)
--
-- 4. 页面模板权限按钮 (ID: 6009-6012)
--    - 查询、新增、修改、删除权限
--    - 父菜单: 6008 (页面模板) 