-- 修复动态菜单路径问题
-- 创建时间: 2025-01-15
-- 说明: 修复错误的动态路由路径配置

-- 1. 删除可能存在的错误菜单记录
DELETE FROM system_menu 
WHERE path = 'dynamic-classification/:configId' 
   OR path = '/system/dynamic-classification/:configId'
   OR (path LIKE '%dynamic-classification%' AND component = 'examples/DragDropClassificationExample');

-- 2. 确保正确的动态菜单存在
INSERT IGNORE INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, 
    status, visible, keep_alive, always_show, dynamic_config
) VALUES (
    '动态分类管理',
    'system:dynamic-classification:query', 
    2, 
    100, 
    1, 
    '/examples/drag-drop-classification/:configId',  -- 正确的绝对路径
    'ep:magic-stick',
    'examples/drag-drop-classification/index',
    'DragDropClassificationDynamic',
    0,
    true,
    true,
    false,
    JSON_OBJECT(
        'parameterName', 'configId',
        'parameterType', 'string',
        'titleTemplate', '动态分类 - ${configId}',
        'enableMultiInstance', true
    )
);

-- 3. 验证修复结果
SELECT 
    id,
    name, 
    path,
    component,
    JSON_EXTRACT(dynamic_config, '$.parameterName') as parameter_name,
    JSON_EXTRACT(dynamic_config, '$.titleTemplate') as title_template
FROM system_menu 
WHERE path LIKE '%drag-drop-classification%' 
   OR dynamic_config IS NOT NULL;

-- 4. 检查是否还有其他问题路径
SELECT 
    id,
    name,
    path,
    component,
    parent_id
FROM system_menu 
WHERE (component IS NULL OR component = '') 
   AND type = 2  -- 菜单类型
   AND path LIKE '%:%';  -- 包含参数的路径

COMMIT; 