-- 将UX Designer从目录改为菜单类型
-- 作为主入口，直接指向设计器页面

-- 1. 更新UX Designer主菜单：从目录改为菜单
UPDATE sys_menu 
SET type = 2,  -- 2表示菜单类型（1=目录，2=菜单，3=按钮）
    component = 'ux-designer/index',
    component_name = 'UxDesigner', 
    update_time = NOW(),
    update_by = 'admin'
WHERE path = 'ux-designer' 
   AND (name LIKE '%UX Designer%' OR name LIKE '%UX设计器%');

-- 2. 删除或禁用原有的子菜单（如工作台、设计器等子菜单）
-- 因为现在这些功能都集成在主页面中
UPDATE sys_menu 
SET status = 1,  -- 1表示禁用
    update_time = NOW(),
    update_by = 'admin'
WHERE parent_id IN (
    SELECT menu_id FROM (
        SELECT menu_id FROM sys_menu 
        WHERE path = 'ux-designer' 
           AND (name LIKE '%UX Designer%' OR name LIKE '%UX设计器%')
    ) AS parent_menu
);

-- 3. 或者完全删除子菜单（如果不需要保留）
-- DELETE FROM sys_menu 
-- WHERE parent_id IN (
--     SELECT menu_id FROM (
--         SELECT menu_id FROM sys_menu 
--         WHERE path = 'ux-designer' 
--            AND (name LIKE '%UX Designer%' OR name LIKE '%UX设计器%')
--     ) AS parent_menu
-- );

-- 4. 确保UX Designer菜单的基本配置正确
UPDATE sys_menu 
SET visible = 1,      -- 显示菜单
    keep_alive = 1,   -- 缓存页面
    always_show = 0,  -- 不强制显示（因为现在是单页面）
    update_time = NOW(),
    update_by = 'admin'
WHERE path = 'ux-designer' 
   AND (name LIKE '%UX Designer%' OR name LIKE '%UX设计器%');

-- 5. 验证更新结果
SELECT 
    menu_id,
    name as menu_name,
    type,
    path,
    component,
    component_name,
    parent_id,
    visible,
    status,
    sort
FROM sys_menu 
WHERE path = 'ux-designer' 
   OR parent_id IN (
       SELECT menu_id FROM (
           SELECT menu_id FROM sys_menu WHERE path = 'ux-designer'
       ) AS parent_menu
   )
ORDER BY parent_id, sort;

-- 查看最终的菜单结构
SELECT 
    CASE 
        WHEN parent_id = 0 THEN CONCAT('├─ ', name)
        ELSE CONCAT('│  ├─ ', name) 
    END as menu_structure,
    CASE type 
        WHEN 1 THEN '目录'
        WHEN 2 THEN '菜单' 
        WHEN 3 THEN '按钮'
    END as menu_type,
    path,
    component,
    CASE status 
        WHEN 0 THEN '正常'
        WHEN 1 THEN '禁用'
    END as status
FROM sys_menu 
WHERE menu_id IN (
    SELECT menu_id FROM sys_menu WHERE path = 'ux-designer'
    UNION ALL
    SELECT menu_id FROM sys_menu WHERE parent_id IN (
        SELECT menu_id FROM sys_menu WHERE path = 'ux-designer'
    )
)
ORDER BY parent_id, sort; 