-- 清理废弃菜单项
-- 创建时间: 2025-01-15
-- 说明: 删除已经不存在的页面和组件对应的菜单记录

-- 1. 删除字段语义目录管理相关菜单
DELETE FROM system_menu 
WHERE name = '字段语义目录管理' 
   OR component = '/field/FieldDef/FieldDefSemanticDirectory'
   OR component = 'field/FieldDef/FieldDefSemanticDirectory';

-- 2. 删除其他可能的废弃菜单（组件文件不存在）
DELETE FROM system_menu 
WHERE component IN (
    '/field/FieldDef/FieldDefSemanticDirectory',
    'examples/DragDropClassificationExample',
    'system/field-category/CategoryManagePage'
) AND type = 2;  -- 只删除菜单类型

-- 3. 查找并列出其他可能的问题菜单
SELECT 
    id,
    name,
    path,
    component,
    parent_id,
    '可能需要检查' as status
FROM system_menu 
WHERE type = 2  -- 菜单类型
  AND component IS NOT NULL 
  AND component != ''
  AND (
    component LIKE '/field/%'          -- 绝对路径格式错误
    OR component LIKE '%Example%'      -- 示例文件
    OR component LIKE '%Test%'         -- 测试文件
    OR component LIKE '%Demo%'         -- 演示文件
    OR component NOT REGEXP '^[a-zA-Z][a-zA-Z0-9/_-]*$'  -- 不规范的路径
  )
ORDER BY name;

-- 4. 检查孤儿菜单（父菜单不存在）
SELECT 
    child.id,
    child.name,
    child.parent_id,
    '孤儿菜单' as issue
FROM system_menu child
LEFT JOIN system_menu parent ON child.parent_id = parent.id
WHERE child.parent_id > 0 
  AND parent.id IS NULL
ORDER BY child.name;

-- 5. 检查重复菜单名称
SELECT 
    parent_id,
    name,
    COUNT(*) as count,
    GROUP_CONCAT(id) as menu_ids
FROM system_menu 
WHERE type IN (1, 2)  -- 目录和菜单
GROUP BY parent_id, name
HAVING COUNT(*) > 1
ORDER BY name;

-- 6. 验证清理结果
SELECT 
    COUNT(*) as total_menus,
    SUM(CASE WHEN type = 1 THEN 1 ELSE 0 END) as directories,
    SUM(CASE WHEN type = 2 THEN 1 ELSE 0 END) as pages,
    SUM(CASE WHEN type = 3 THEN 1 ELSE 0 END) as buttons
FROM system_menu;

COMMIT; 