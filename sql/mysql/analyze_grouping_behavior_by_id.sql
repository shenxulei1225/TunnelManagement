-- 分析不同分组ID归档行为差异的诊断脚本

-- 1. 查看所有分组的基本信息
SELECT '=== 分组基本信息 ===' as step;
SELECT 
    id as '分组ID',
    name as '分组名称',
    code as '分组代码',
    parent_id as '父分组ID',
    level as '层级',
    path as '路径',
    sort as '排序',
    color as '颜色',
    icon as '图标',
    status as '状态',
    create_time as '创建时间'
FROM system_hierarchy_group 
WHERE deleted = 0
ORDER BY id;

-- 2. 查看建筑分组的详细信息
SELECT '=== 建筑分组详情 ===' as step;
SELECT 
    id, name, code, parent_id, level, path, sort, status,
    '建筑分组' as '分组类型'
FROM system_hierarchy_group 
WHERE name LIKE '%建筑%' 
    OR code LIKE '%building%' 
    OR code LIKE '%construct%'
    AND deleted = 0;

-- 3. 查看电力分组的详细信息（分组ID=8）
SELECT '=== 电力分组详情 ===' as step;
SELECT 
    id, name, code, parent_id, level, path, sort, status,
    '电力分组' as '分组类型'
FROM system_hierarchy_group 
WHERE id = 8 
    OR name LIKE '%电力%' 
    OR code LIKE '%electric%' 
    OR code LIKE '%power%'
    AND deleted = 0;

-- 4. 查看这两个分组现有的字段关联情况
SELECT '=== 建筑分组现有字段关联 ===' as step;
SELECT 
    fhr.id as '关联ID',
    fhr.field_id as '字段ID',
    f.field_label as '字段名称',
    fhr.hierarchy_group_id as '分组ID',
    hg.name as '分组名称',
    fhr.sort as '排序',
    fhr.create_time as '关联时间',
    fhr.deleted as '删除状态'
FROM system_field_hierarchy_rel fhr
JOIN system_field f ON fhr.field_id = f.id
JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE hg.name LIKE '%建筑%' 
    AND fhr.deleted = 0
ORDER BY fhr.create_time DESC
LIMIT 10;

SELECT '=== 电力分组现有字段关联 ===' as step;
SELECT 
    fhr.id as '关联ID',
    fhr.field_id as '字段ID',
    f.field_label as '字段名称',
    fhr.hierarchy_group_id as '分组ID',
    hg.name as '分组名称',
    fhr.sort as '排序',
    fhr.create_time as '关联时间',
    fhr.deleted as '删除状态'
FROM system_field_hierarchy_rel fhr
JOIN system_field f ON fhr.field_id = f.id
JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE hg.id = 8 
    AND fhr.deleted = 0
ORDER BY fhr.create_time DESC
LIMIT 10;

-- 5. 检查是否有重复数据会导致不同的行为
SELECT '=== 检查可能的重复数据模式 ===' as step;

-- 检查建筑分组是否有重复关联的模式
SELECT 
    '建筑分组重复检查' as check_type,
    field_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(id ORDER BY create_time) as record_ids,
    GROUP_CONCAT(deleted ORDER BY create_time) as deleted_status
FROM system_field_hierarchy_rel fhr
JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE hg.name LIKE '%建筑%'
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;

-- 检查电力分组（ID=8）是否有重复关联的模式
SELECT 
    '电力分组重复检查' as check_type,
    field_id,
    hierarchy_group_id,
    tenant_id,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(id ORDER BY create_time) as record_ids,
    GROUP_CONCAT(deleted ORDER BY create_time) as deleted_status
FROM system_field_hierarchy_rel 
WHERE hierarchy_group_id = 8
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1;

-- 6. 检查分组的层级结构是否有影响
SELECT '=== 分组层级结构分析 ===' as step;
SELECT 
    hg.id,
    hg.name,
    hg.parent_id,
    parent.name as parent_name,
    hg.level,
    hg.path,
    CASE 
        WHEN hg.parent_id = 0 THEN '根级分组'
        WHEN hg.level = 1 THEN '一级分组'
        WHEN hg.level = 2 THEN '二级分组'
        ELSE CONCAT(hg.level, '级分组')
    END as level_desc,
    '可能影响归档行为' as note
FROM system_hierarchy_group hg
LEFT JOIN system_hierarchy_group parent ON hg.parent_id = parent.id
WHERE hg.deleted = 0 
    AND (hg.name LIKE '%建筑%' OR hg.name LIKE '%电力%' OR hg.id = 8)
ORDER BY hg.level, hg.sort;

-- 7. 检查是否有特殊的业务规则或约束
SELECT '=== 分组约束和规则检查 ===' as step;

-- 检查分组的状态是否不同
SELECT 
    id, name, status,
    CASE 
        WHEN status = 0 THEN '启用'
        WHEN status = 1 THEN '禁用'
        ELSE '未知状态'
    END as status_desc,
    '状态可能影响行为' as note
FROM system_hierarchy_group 
WHERE (name LIKE '%建筑%' OR name LIKE '%电力%' OR id = 8) 
    AND deleted = 0;

-- 8. 检查数据库约束和触发器
SELECT '=== 数据库约束检查 ===' as step;
SELECT 
    CONSTRAINT_NAME as '约束名称',
    CONSTRAINT_TYPE as '约束类型',
    TABLE_NAME as '表名',
    COLUMN_NAME as '字段名',
    REFERENCED_TABLE_NAME as '引用表',
    REFERENCED_COLUMN_NAME as '引用字段'
FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'system_field_hierarchy_rel'
ORDER BY CONSTRAINT_NAME, ORDINAL_POSITION;

-- 9. 分析最近的操作记录
SELECT '=== 最近的字段归组操作记录 ===' as step;
SELECT 
    fhr.id,
    f.field_label as '字段名称',
    hg.name as '分组名称',
    fhr.hierarchy_group_id as '分组ID',
    fhr.create_time as '操作时间',
    fhr.creator as '操作者',
    CASE 
        WHEN fhr.create_time > DATE_SUB(NOW(), INTERVAL 1 HOUR) THEN '1小时内'
        WHEN fhr.create_time > DATE_SUB(NOW(), INTERVAL 1 DAY) THEN '1天内'
        WHEN fhr.create_time > DATE_SUB(NOW(), INTERVAL 1 WEEK) THEN '1周内'
        ELSE '更早'
    END as '时间范围'
FROM system_field_hierarchy_rel fhr
JOIN system_field f ON fhr.field_id = f.id
JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE fhr.deleted = 0
ORDER BY fhr.create_time DESC
LIMIT 20;

-- 10. 总结分析
SELECT '=== 分析总结 ===' as step;
SELECT 
    '可能的原因分析' as analysis_type,
    '1. 检查建筑分组和电力分组的基本配置差异' as reason1,
    '2. 检查是否存在不同的重复数据处理逻辑' as reason2,
    '3. 检查分组层级结构对归档行为的影响' as reason3,
    '4. 检查前端代码中是否有针对特定分组ID的特殊处理' as reason4,
    '5. 检查数据库约束是否对不同分组有不同影响' as reason5; 