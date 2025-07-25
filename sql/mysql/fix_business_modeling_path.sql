-- 修复 business-modeling 菜单路径的SQL脚本
-- 将 business-modeling 从系统管理子菜单改为顶级菜单

-- 1. 查找当前的 business-modeling 菜单
SELECT id, name, parent_id, path, component 
FROM system_menu 
WHERE (path LIKE '%business-modeling%' OR component LIKE '%BusinessModeling%' OR name LIKE '%业务建模%')
AND deleted = 0;

-- 2. 更新菜单配置，使其成为顶级菜单
UPDATE system_menu 
SET 
  parent_id = 0,  -- 设为顶级菜单
  path = '/business-modeling',  -- 修正路径
  sort = 900  -- 设置排序
WHERE (path LIKE '%business-modeling%' OR component LIKE '%BusinessModeling%' OR name LIKE '%业务建模%')
AND deleted = 0;

-- 3. 验证修改结果
SELECT id, name, parent_id, path, icon, component, component_name, status, visible
FROM system_menu 
WHERE (path LIKE '%business-modeling%' OR component LIKE '%BusinessModeling%' OR name LIKE '%业务建模%')
AND deleted = 0
ORDER BY parent_id, sort; 