-- 将智能推荐菜单从动态业务移动到业务建模的SQL脚本
-- 适用于MySQL数据库

-- 1. 查找业务建模菜单的ID（假设已存在）
SET @business_modeling_id = (
    SELECT id FROM system_menu 
    WHERE (path LIKE '%business-modeling%' OR name LIKE '%业务建模%') 
    AND parent_id = 0 
    AND deleted = 0 
    LIMIT 1
);

-- 2. 查找动态业务菜单的ID
SET @dynamic_business_id = (
    SELECT id FROM system_menu 
    WHERE (path LIKE '%dynamic%' OR name LIKE '%动态业务%') 
    AND parent_id = 0 
    AND deleted = 0 
    LIMIT 1
);

-- 3. 查找智能推荐菜单
SELECT id, name, parent_id, path, component 
FROM system_menu 
WHERE (name LIKE '%智能推荐%' OR name LIKE '%推荐%' OR component LIKE '%recommendation%')
AND deleted = 0;

-- 4. 更新智能推荐主菜单的父菜单ID，将其移动到业务建模下
UPDATE system_menu 
SET 
  parent_id = @business_modeling_id,
  path = 'recommendation',
  sort = 3,
  updater = 'admin',
  update_time = NOW()
WHERE (name LIKE '%智能推荐%' OR component LIKE '%recommendation/index%')
AND parent_id = @dynamic_business_id
AND deleted = 0;

-- 5. 更新智能推荐配置菜单的父菜单ID
UPDATE system_menu 
SET 
  parent_id = @business_modeling_id,
  path = 'recommendation/configure',
  sort = 4,
  name = '推荐配置',
  updater = 'admin',
  update_time = NOW()
WHERE (name LIKE '%模板配置%' OR component LIKE '%recommendation/configure%')
AND parent_id = @dynamic_business_id
AND deleted = 0;

-- 6. 验证移动结果
SELECT 
    m.id,
    m.name,
    m.parent_id,
    p.name as parent_name,
    m.path,
    m.icon,
    m.component,
    m.sort,
    m.visible
FROM system_menu m
LEFT JOIN system_menu p ON m.parent_id = p.id
WHERE (m.component LIKE '%recommendation%' OR m.name LIKE '%推荐%')
AND m.deleted = 0
ORDER BY m.parent_id, m.sort;

-- 7. 查看业务建模下的所有子菜单
SELECT 
    id,
    name,
    path,
    icon,
    component,
    sort,
    visible
FROM system_menu 
WHERE parent_id = @business_modeling_id
AND deleted = 0
ORDER BY sort; 