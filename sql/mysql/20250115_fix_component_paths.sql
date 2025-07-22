-- 修复组件路径映射问题
-- 创建时间: 2025-01-15
-- 说明: 修复数据库中错误的组件路径配置，确保与实际文件路径匹配

-- 1. 修复分类管理组件路径（如果存在）
UPDATE system_menu 
SET component = 'system/field/FieldCategory/CategoryManagePage'
WHERE name = '分类管理' 
  AND component = 'system/field-category/CategoryManagePage';

-- 2. 检查其他可能的路径问题并修复
UPDATE system_menu 
SET component = REPLACE(component, '/field/', 'system/field/')
WHERE component LIKE '/field/%';

UPDATE system_menu 
SET component = REPLACE(component, 'field-category', 'field/FieldCategory')
WHERE component LIKE '%field-category%';

-- 3. 确保动态路由组件路径正确
UPDATE system_menu 
SET component = 'examples/drag-drop-classification/index'
WHERE component IN (
    'examples/DragDropClassificationExample',
    'examples/drag-drop-classification/DragDropClassificationExample'
) AND path LIKE '%drag-drop-classification%';

-- 4. 验证修复结果
SELECT 
    id,
    name,
    path,
    component,
    component_name
FROM system_menu 
WHERE type = 2  -- 菜单类型
  AND (
    component LIKE '%field%' 
    OR component LIKE '%drag-drop%'
    OR component LIKE '/field/%'
  )
ORDER BY name;

-- 5. 检查是否还有其他无效的组件路径
SELECT 
    id,
    name,
    path,
    component,
    'Missing component file' as issue
FROM system_menu 
WHERE type = 2  -- 菜单类型
  AND component IS NOT NULL 
  AND component != ''
  AND (
    component LIKE '/field/%'          -- 错误的绝对路径
    OR component LIKE '%Example%'      -- 示例文件名
    OR component NOT LIKE '%/%'       -- 缺少路径分隔符的组件
  );

COMMIT; 