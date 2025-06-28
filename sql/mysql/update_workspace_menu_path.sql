-- 更新工作台菜单组件路径
-- 由于文件从 views/workspace/index.vue 移动到 views/ux-designer/workspace/index.vue
-- 需要更新菜单配置中的组件路径

-- 更新工作台菜单的组件路径
UPDATE sys_menu 
SET component = 'ux-designer/workspace/index',
    component_name = 'UxDesignerWorkspace',
    update_time = NOW(),
    update_by = 'admin'
WHERE path = 'workspace' 
   AND (
       component = 'workspace/index' 
       OR component LIKE '%workspace%'
   );

-- 确保UX Designer主菜单的组件路径正确
UPDATE sys_menu 
SET component = 'ux-designer/index',
    component_name = 'UxDesigner',
    update_time = NOW(),
    update_by = 'admin'
WHERE path = 'ux-designer' 
   AND (
       component IS NULL 
       OR component = 'Layout'
       OR component LIKE '%ux-designer%'
   );

-- 验证更新结果
SELECT 
    menu_id,
    menu_name,
    path,
    component,
    component_name,
    parent_id,
    status
FROM sys_menu 
WHERE menu_name LIKE '%工作台%' 
   OR menu_name LIKE '%UX Designer%'
   OR component LIKE '%workspace%'
   OR component LIKE '%ux-designer%'
ORDER BY parent_id, sort; 