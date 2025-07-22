-- 菜单管理系统动态路由支持 - 数据库升级脚本
-- 创建时间: 2025-01-15
-- 说明: 为现有菜单表添加动态路由配置支持

-- 添加动态路由配置字段
ALTER TABLE system_menu ADD COLUMN `dynamic_config` JSON DEFAULT NULL COMMENT '动态路由配置: {parameterName, parameterType, titleTemplate, enableMultiInstance}';

-- 创建索引提升查询性能 (可选)
CREATE INDEX idx_menu_dynamic ON system_menu(path) COMMENT '动态路由路径查询优化';

-- 示例数据插入 (演示如何创建动态路由菜单)
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, component_name, 
    status, visible, keep_alive, always_show, dynamic_config
) VALUES (
    '动态分类管理',
    'system:dynamic-classification:query', 
    2, 
    100, 
    1, 
    '/examples/drag-drop-classification/:configId',  -- 动态路径（绝对路径）
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

-- 验证插入结果
SELECT 
    name, 
    path, 
    JSON_EXTRACT(dynamic_config, '$.parameterName') as parameter_name,
    JSON_EXTRACT(dynamic_config, '$.titleTemplate') as title_template,
    JSON_EXTRACT(dynamic_config, '$.enableMultiInstance') as multi_instance
FROM system_menu 
WHERE dynamic_config IS NOT NULL; 