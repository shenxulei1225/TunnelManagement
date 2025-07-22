-- 更新正确的组件路径
-- 创建时间: 2025-01-15  
-- 说明: 基于智能匹配结果，更新数据库中的组件路径为正确值

-- 1. 更新分类管理的组件路径
UPDATE system_menu 
SET component = 'system/field/FieldCategory/CategoryManagePage'
WHERE name = '分类管理' 
  AND component = 'system/field-category/CategoryManagePage';

-- 2. 更新所有 field-category 路径为正确的 field/FieldCategory
UPDATE system_menu 
SET component = REPLACE(component, 'field-category', 'field/FieldCategory')
WHERE component LIKE '%field-category%';

-- 3. 验证更新结果
SELECT 
    id,
    name,
    path,
    component,
    '已更新' as status
FROM system_menu 
WHERE component LIKE '%field/FieldCategory%'
ORDER BY name;

COMMIT; 