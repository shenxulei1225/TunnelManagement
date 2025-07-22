-- 字段管理统一页面菜单权限配置
-- 包含字段定义、分类管理、关联关系管理的完整权限

-- 1. 创建主菜单：字段管理
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, 
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES (
    '字段管理', '', 2, 2010, 1, 'field-management', 'ep:collection',
    'system/field/unified/index', 'FieldManagement', 0, 1, 1, 1,
    'admin', NOW(), 'admin', NOW(), 0
);

-- 获取字段管理菜单ID
SET @field_menu_id = (SELECT id FROM `system_menu` WHERE `name` = '字段管理' AND `component_name` = 'FieldManagement' ORDER BY id DESC LIMIT 1);

-- 2. 字段定义权限按钮 (4个)
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询字段', 'system:field:query', 3, 1, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增字段', 'system:field:create', 3, 2, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('修改字段', 'system:field:update', 3, 3, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除字段', 'system:field:delete', 3, 4, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 3. 分类管理权限按钮 (4个)
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询分类', 'system:category:query', 3, 5, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增分类', 'system:category:create', 3, 6, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('修改分类', 'system:category:update', 3, 7, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除分类', 'system:category:delete', 3, 8, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 4. 字段分类关联权限按钮 (4个)
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('查询关联', 'system:field-category:query', 3, 9, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('新增关联', 'system:field-category:create', 3, 10, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('修改关联', 'system:field-category:update', 3, 11, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
('删除关联', 'system:field-category:delete', 3, 12, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 5. 拖拽操作权限按钮 (可选，推荐复用现有权限)
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('拖拽操作', 'system:field-category:drag', 3, 13, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 6. 导出权限 (可选)
INSERT INTO `system_menu` (
    `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`,
    `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
) VALUES 
('导出字段', 'system:field:export', 3, 14, @field_menu_id, '', '', '', '', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 验证插入结果
SELECT 
    id,
    name,
    permission,
    type,
    sort,
    parent_id
FROM system_menu 
WHERE parent_id = @field_menu_id OR id = @field_menu_id
ORDER BY sort; 