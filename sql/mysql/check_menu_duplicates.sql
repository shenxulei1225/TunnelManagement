-- 菜单重复检查和清理脚本
-- 解决 TooManyResultsException: Expected one result but found: 2

-- 1. 查看重复的菜单（相同父级和名称）
SELECT 
    parent_id,
    name,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(id ORDER BY id) as menu_ids,
    GROUP_CONCAT(CONCAT('ID:', id, ' Path:', path, ' Component:', component) SEPARATOR ' | ') as details
FROM system_menu 
WHERE deleted = 0
GROUP BY parent_id, name
HAVING COUNT(*) > 1
ORDER BY name;

-- 2. 查看字段属性管理相关的菜单
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
    create_time
FROM system_menu 
WHERE name LIKE '%字段%' 
   OR name LIKE '%field%'
   OR component LIKE '%field%'
   OR permission LIKE '%field%'
ORDER BY name, id;

-- 3. 查看组件名重复的菜单
SELECT 
    component_name,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(id ORDER BY id) as menu_ids,
    GROUP_CONCAT(CONCAT('ID:', id, ' Name:', name) SEPARATOR ' | ') as details
FROM system_menu 
WHERE deleted = 0 AND component_name IS NOT NULL AND component_name != ''
GROUP BY component_name
HAVING COUNT(*) > 1
ORDER BY component_name;

-- 4. 清理重复的菜单（保留最早创建的，删除后创建的）
-- 注意：执行前请先备份数据库

-- 4.1 删除重复菜单的子菜单（权限按钮）
DELETE FROM system_menu 
WHERE parent_id IN (
    SELECT temp.menu_id FROM (
        SELECT 
            m2.id as menu_id
        FROM system_menu m1
        JOIN system_menu m2 ON m1.parent_id = m2.parent_id AND m1.name = m2.name
        WHERE m1.deleted = 0 AND m2.deleted = 0
          AND m1.id < m2.id  -- 保留ID较小的（更早创建的）
    ) AS temp
);

-- 4.2 删除重复的菜单主体（保留最早的）
DELETE FROM system_menu 
WHERE id IN (
    SELECT temp.menu_id FROM (
        SELECT 
            m2.id as menu_id
        FROM system_menu m1
        JOIN system_menu m2 ON m1.parent_id = m2.parent_id AND m1.name = m2.name
        WHERE m1.deleted = 0 AND m2.deleted = 0
          AND m1.id < m2.id  -- 保留ID较小的（更早创建的）
    ) AS temp
);

-- 5. 验证清理结果
SELECT 
    parent_id,
    name,
    COUNT(*) as count
FROM system_menu 
WHERE deleted = 0
GROUP BY parent_id, name
HAVING COUNT(*) > 1;

-- 6. 创建唯一索引防止未来重复（可选）
-- ALTER TABLE system_menu ADD UNIQUE INDEX uk_parent_name (parent_id, name, deleted);

-- 7. 对于字段属性管理，确保配置正确
UPDATE system_menu 
SET 
    component = 'system/field/FieldDef/index',
    component_name = 'FieldDef',
    permission = 'system:field-def:list',
    icon = 'ep:baseball',
    update_time = NOW(),
    updater = 'admin'
WHERE name = '字段属性管理' 
  AND deleted = 0;

-- 8. 显示最终的菜单配置参数
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
    visible,
    keep_alive,
    always_show
FROM system_menu 
WHERE name = '字段属性管理' 
  AND deleted = 0; 