-- UX设计器菜单配置SQL脚本
-- 适用于MySQL数据库

-- 1. 添加主菜单：UX设计器 (Universal X Designer)
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2180, 'UX设计器', 0, 4, 'ux-designer', NULL, 1, 0, 'M', '0', '0', '', 'ep:brush', 'admin', NOW(), '', NULL, 'Universal X Designer'),
(2181, 'UX工作台', 2180, 1, 'workspace', 'ux-designer/index', 1, 0, 'C', '0', '0', 'ux:workspace:list', 'ep:edit', 'admin', NOW(), '', NULL, '用户体验设计工作台'),
(2182, '体验演示', 2180, 2, 'demo', 'converter/demo', 1, 0, 'C', '0', '0', 'ux:demo:list', 'ep:view', 'admin', NOW(), '', NULL, '用户体验设计演示'),
(2183, '设计模板', 2180, 3, 'templates', 'converter/index', 1, 0, 'C', '0', '0', 'ux:templates:list', 'ep:collection', 'admin', NOW(), '', NULL, 'UX设计模板中心');

-- 功能权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(2184, '体验导入', 2181, 1, '', '', 1, 0, 'F', '0', '0', 'ux:experience:import', '#', 'admin', NOW(), '', NULL, '导入用户体验设计'),
(2185, '多端导出', 2181, 2, '', '', 1, 0, 'F', '0', '0', 'ux:multiplatform:export', '#', 'admin', NOW(), '', NULL, '多平台代码导出'),
(2186, '体验素材', 2181, 3, '', '', 1, 0, 'F', '0', '0', 'ux:assets:manage', '#', 'admin', NOW(), '', NULL, '用户体验素材管理'),
(2187, '组件体系', 2181, 4, '', '', 1, 0, 'F', '0', '0', 'ux:components:system', '#', 'admin', NOW(), '', NULL, '用户体验组件体系');

-- 更新现有记录（如果已存在）
UPDATE sys_menu 
SET component = 'ux-designer/index', 
    component_name = 'UxDesigner',
    update_time = NOW()
WHERE path = 'ux-designer' OR component LIKE '%PageDesigner%';

-- 查询验证插入结果
SELECT 
    id,
    name,
    permission,
    type,
    sort,
    parent_id,
    path,
    icon,
    component,
    component_name,
    status
FROM system_menu 
WHERE permission LIKE 'ux:%' 
AND deleted = false
ORDER BY parent_id, sort; 