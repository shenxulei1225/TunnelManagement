-- 分类管理DynamicForm演示页面菜单配置
-- 展示SuperTree + DynamicFormV2的集成效果

-- 插入菜单记录
INSERT INTO system_menu (
    id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    3002, '分类管理演示', '', 2, 2, 3000, 'category-demo', 'ep:management', 'system/category/CategoryManagerDemo', 'CategoryManagerDemo', 0, 1, 1, 1, '1', NOW(), '1', NOW(), 0
);

-- 验证插入结果
SELECT 
    id,
    name,
    permission,
    type,
    sort,
    parent_id,
    path,
    icon,
    component,
    component_name,
    status,
    visible
FROM system_menu 
WHERE id = 3002;

-- 显示完整的系统管理菜单结构（包含新增的演示页面）
SELECT 
    CASE 
        WHEN parent_id IS NULL THEN CONCAT('📁 ', name)
        WHEN parent_id = 1 THEN CONCAT('  📄 ', name)
        ELSE CONCAT('    📋 ', name)
    END AS menu_structure,
    id,
    name,
    path,
    component,
    icon,
    sort,
    status
FROM system_menu 
WHERE (id = 1 OR parent_id = 1 OR parent_id IN (SELECT id FROM system_menu WHERE parent_id = 1))
  AND deleted = 0
ORDER BY 
    CASE WHEN parent_id IS NULL THEN id ELSE parent_id END,
    sort, 
    id; 