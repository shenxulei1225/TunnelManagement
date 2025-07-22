-- 调试建筑分组显示问题

-- 1. 查看所有分组信息
SELECT '=== 所有分组信息 ===' as step;
SELECT 
    id as '分组ID',
    name as '分组名称',
    code as '分组代码',
    parent_id as '父分组ID',
    level as '层级',
    path as '路径',
    sort as '排序',
    status as '状态',
    deleted as '删除标记',
    create_time as '创建时间'
FROM system_hierarchy_group 
WHERE deleted = 0
ORDER BY id;

-- 2. 查找建筑分组
SELECT '=== 建筑分组查找 ===' as step;
SELECT 
    id, name, code, parent_id, level, path, sort, status, deleted
FROM system_hierarchy_group 
WHERE deleted = 0 
    AND (name LIKE '%建筑%' 
         OR code LIKE '%build%' 
         OR code LIKE '%construct%'
         OR code LIKE '%jianzhu%')
ORDER BY id;

-- 3. 查看所有字段的分组关联情况
SELECT '=== 字段分组关联情况 ===' as step;
SELECT 
    fhr.id as '关联ID',
    fhr.field_id as '字段ID',
    f.field_label as '字段名称',
    fhr.hierarchy_group_id as '分组ID',
    hg.name as '分组名称',
    fhr.sort as '排序',
    fhr.tenant_id as '租户ID',
    fhr.deleted as '关联删除标记',
    fhr.create_time as '关联创建时间'
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_field f ON fhr.field_id = f.id
LEFT JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE fhr.deleted = 0
ORDER BY fhr.hierarchy_group_id, fhr.sort, fhr.create_time;

-- 4. 统计每个分组的字段数量
SELECT '=== 分组字段数量统计 ===' as step;
SELECT 
    hg.id as '分组ID',
    hg.name as '分组名称',
    COUNT(fhr.id) as '字段数量',
    GROUP_CONCAT(f.field_label ORDER BY fhr.sort) as '字段列表'
FROM system_hierarchy_group hg
LEFT JOIN system_field_hierarchy_rel fhr ON hg.id = fhr.hierarchy_group_id AND fhr.deleted = 0
LEFT JOIN system_field f ON fhr.field_id = f.id AND f.deleted = 0
WHERE hg.deleted = 0
GROUP BY hg.id, hg.name
ORDER BY hg.id; 