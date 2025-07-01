-- 详细的菜单重复问题检查脚本
-- 分析 TooManyResultsException 的具体原因

-- 1. 检查菜单ID=5017的详细信息
SELECT 
    id,
    name,
    parent_id,
    path,
    component,
    component_name,
    permission,
    type,
    status,
    visible,
    deleted,
    create_time,
    update_time
FROM system_menu 
WHERE id = 5017;

-- 2. 检查是否有软删除的重复记录
SELECT 
    id,
    name,
    parent_id,
    path,
    component,
    component_name,
    permission,
    deleted,
    create_time
FROM system_menu 
WHERE name = '字段属性管理'
ORDER BY create_time;

-- 3. 检查组件名称是否重复（这可能是真正的问题）
SELECT 
    component_name,
    COUNT(*) as count,
    GROUP_CONCAT(id ORDER BY id) as menu_ids,
    GROUP_CONCAT(CONCAT('ID:', id, ',Name:', name, ',Deleted:', deleted) SEPARATOR ' | ') as details
FROM system_menu 
WHERE component_name = 'FieldDef'
GROUP BY component_name;

-- 4. 检查权限标识是否重复
SELECT 
    permission,
    COUNT(*) as count,
    GROUP_CONCAT(id ORDER BY id) as menu_ids,
    GROUP_CONCAT(CONCAT('ID:', id, ',Name:', name, ',Deleted:', deleted) SEPARATOR ' | ') as details
FROM system_menu 
WHERE permission = 'system:field-def:list'
GROUP BY permission;

-- 5. 检查路径是否重复
SELECT 
    path,
    COUNT(*) as count,
    GROUP_CONCAT(id ORDER BY id) as menu_ids,
    GROUP_CONCAT(CONCAT('ID:', id, ',Name:', name, ',Deleted:', deleted) SEPARATOR ' | ') as details
FROM system_menu 
WHERE path = 'field-def'
GROUP BY path;

-- 6. 全面检查所有可能重复的字段组合
SELECT 
    '名称重复' as check_type,
    parent_id,
    name,
    COUNT(*) as count,
    GROUP_CONCAT(id ORDER BY id) as ids
FROM system_menu 
WHERE deleted = 0
GROUP BY parent_id, name
HAVING COUNT(*) > 1

UNION ALL

SELECT 
    '组件名重复' as check_type,
    NULL as parent_id,
    component_name as name,
    COUNT(*) as count,
    GROUP_CONCAT(id ORDER BY id) as ids
FROM system_menu 
WHERE component_name IS NOT NULL AND component_name != '' AND deleted = 0
GROUP BY component_name
HAVING COUNT(*) > 1

UNION ALL

SELECT 
    '权限重复' as check_type,
    NULL as parent_id,
    permission as name,
    COUNT(*) as count,
    GROUP_CONCAT(id ORDER BY id) as ids
FROM system_menu 
WHERE permission IS NOT NULL AND permission != '' AND deleted = 0
GROUP BY permission
HAVING COUNT(*) > 1

UNION ALL

SELECT 
    '路径重复' as check_type,
    NULL as parent_id,
    path as name,
    COUNT(*) as count,
    GROUP_CONCAT(id ORDER BY id) as ids
FROM system_menu 
WHERE path IS NOT NULL AND path != '' AND deleted = 0
GROUP BY path
HAVING COUNT(*) > 1

ORDER BY check_type, count DESC;

-- 7. 检查最近的菜单操作日志（如果有的话）
SELECT 
    id,
    name,
    component_name,
    permission,
    deleted,
    create_time,
    update_time,
    creator,
    updater
FROM system_menu 
WHERE name LIKE '%字段%' 
   OR component_name LIKE '%Field%'
   OR permission LIKE '%field%'
ORDER BY update_time DESC, create_time DESC
LIMIT 20; 