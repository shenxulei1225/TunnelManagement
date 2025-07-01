-- 修复组件名称重复问题
-- 解决 FieldDef 组件名称重复导致的 TooManyResultsException

-- 1. 查看当前重复的组件名称
SELECT 
    id,
    name,
    component_name,
    component,
    permission,
    create_time
FROM system_menu 
WHERE component_name = 'FieldDef'
ORDER BY create_time;

-- 2. 修改重复的组件名称，保持业务语义清晰
-- 保留 ID=5017 的 "字段属性管理" 使用 "FieldDef"
-- 修改 ID=5148 的 "测试字段属性" 使用 "FieldDefTest"

UPDATE system_menu 
SET 
    component_name = 'FieldDefTest',
    update_time = NOW(),
    updater = 'admin'
WHERE id = 5148;

-- 3. 验证修复结果
SELECT 
    id,
    name,
    component_name,
    component,
    permission
FROM system_menu 
WHERE id IN (5017, 5148);

-- 4. 检查是否还有其他组件名称重复
SELECT 
    component_name,
    COUNT(*) as count,
    GROUP_CONCAT(id ORDER BY id) as menu_ids,
    GROUP_CONCAT(name ORDER BY id SEPARATOR ' | ') as menu_names
FROM system_menu 
WHERE component_name IS NOT NULL 
    AND component_name != '' 
    AND deleted = 0
GROUP BY component_name
HAVING COUNT(*) > 1;

-- 5. 创建调试信息，便于后续排查
SELECT 
    'Component Name Validation' as debug_info,
    id,
    name,
    component_name,
    'SELECT * FROM system_menu WHERE component_name = ''' || component_name || '''' as debug_query
FROM system_menu 
WHERE id IN (5017, 5148); 