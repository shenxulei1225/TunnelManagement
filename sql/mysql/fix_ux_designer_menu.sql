-- 快速修复UX设计器菜单配置
-- 更新组件路径以匹配新的文件结构

-- 更新UX设计器相关菜单的组件路径
UPDATE sys_menu 
SET component = 'ux-designer/index', 
    component_name = 'UxDesigner',
    update_time = NOW(),
    update_by = 'admin'
WHERE path = 'ux-designer' 
   OR component LIKE '%PageDesigner%'
   OR component LIKE '%ux-designer/PageDesigner%';

-- 如果路由地址是 /ux-designer，确保组件路径正确
UPDATE sys_menu 
SET component = 'ux-designer/index'
WHERE path = '/ux-designer';

-- 验证更新结果
SELECT 
    menu_id,
    menu_name,
    path,
    component,
    component_name,
    status
FROM sys_menu 
WHERE menu_name LIKE '%UX%' 
   OR component LIKE '%ux-designer%'
   OR path LIKE '%ux-designer%'
ORDER BY menu_id; 