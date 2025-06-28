-- 清理重复的UX Designer菜单
-- 解决 TooManyResultsException: Expected one result but found: 7

-- 1. 查看当前重复的UX Designer菜单
SELECT 
    id,
    name,
    path,
    component,
    parent_id,
    type,
    status,
    create_time
FROM sys_menu 
WHERE name LIKE '%UX Designer%' 
   OR name LIKE '%UX设计器%'
   OR path LIKE '%ux-designer%'
ORDER BY id;

-- 2. 查看这些菜单的子菜单
SELECT 
    p.id as parent_id,
    p.name as parent_name,
    c.id as child_id,
    c.name as child_name,
    c.path as child_path
FROM sys_menu p
LEFT JOIN sys_menu c ON p.id = c.parent_id
WHERE p.name LIKE '%UX Designer%' 
   OR p.name LIKE '%UX设计器%'
   OR p.path LIKE '%ux-designer%'
ORDER BY p.id, c.id;

-- 3. 保留最早创建的UX Designer菜单（ID最小的）
-- 先获取要保留的菜单ID
SET @keep_menu_id = (
    SELECT MIN(id) 
    FROM sys_menu 
    WHERE (name LIKE '%UX Designer%' OR name LIKE '%UX设计器%') 
       AND parent_id = 0
);

-- 4. 删除或禁用其他重复的菜单及其子菜单
-- 4.1 先删除重复菜单的子菜单
DELETE FROM sys_menu 
WHERE parent_id IN (
    SELECT temp.id FROM (
        SELECT id 
        FROM sys_menu 
        WHERE (name LIKE '%UX Designer%' OR name LIKE '%UX设计器%') 
           AND parent_id = 0 
           AND id != @keep_menu_id
    ) AS temp
);

-- 4.2 再删除重复的主菜单
DELETE FROM sys_menu 
WHERE (name LIKE '%UX Designer%' OR name LIKE '%UX设计器%') 
   AND parent_id = 0 
   AND id != @keep_menu_id;

-- 5. 更新保留的菜单配置
UPDATE sys_menu 
SET name = 'UX Designer',
    type = 2,  -- 改为菜单类型
    path = 'ux-designer',
    component = 'ux-designer/index',
    component_name = 'UxDesigner',
    permission = 'uxdesigner:view',
    icon = 'ep:edit',
    status = 0,  -- 启用
    visible = 1,
    keep_alive = 1,
    always_show = 0,  -- 单页面不需要强制显示
    update_time = NOW(),
    updater = 'admin'
WHERE id = @keep_menu_id;

-- 6. 验证清理结果
SELECT 
    id,
    name,
    path,
    component,
    type,
    status,
    parent_id
FROM sys_menu 
WHERE name LIKE '%UX Designer%' 
   OR name LIKE '%UX设计器%'
   OR path LIKE '%ux-designer%'
ORDER BY id;

-- 7. 确认菜单名称唯一性
SELECT 
    name,
    parent_id,
    COUNT(*) as count
FROM sys_menu 
WHERE name LIKE '%UX Designer%'
GROUP BY name, parent_id
HAVING COUNT(*) > 1; 