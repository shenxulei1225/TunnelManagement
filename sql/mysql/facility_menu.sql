-- 设施管理菜单配置SQL脚本
-- 适用于MySQL数据库

-- 1. 创建主菜单：设施管理
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, 
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '设施管理', '', 1, 5000, 0, 'facility', 'ep:office-building',
    '', '', 0, 1, 1, 1,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 获取主菜单ID
SET @facility_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '设施管理' AND `parent_id` = 0 ORDER BY id DESC LIMIT 1);

-- 2. 创建设施健康状态总览子菜单
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '健康状态总览', 'facility:health:query', 2, 5010, @facility_menu_id, 'health', 'ep:data-board',
    'facility/health/index', 'FacilityHealth', 0, 1, 1, 1,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 获取健康状态总览菜单ID
SET @health_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '健康状态总览' AND `parent_id` = @facility_menu_id ORDER BY id DESC LIMIT 1);

-- 3. 创建健康状态总览权限按钮
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查看统计', 'facility:health:stats', 3, 1, @health_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), 'admin', NOW(), 0),
('查看趋势', 'facility:health:trend', 3, 2, @health_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), 'admin', NOW(), 0),
('查看预警', 'facility:health:alert', 3, 3, @health_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), 'admin', NOW(), 0),
('导出报表', 'facility:health:export', 3, 4, @health_menu_id, '', '', '', '', 0, 1, 1, 1, 'admin', NOW(), 'admin', NOW(), 0); 