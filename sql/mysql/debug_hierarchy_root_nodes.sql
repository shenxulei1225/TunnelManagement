-- 调试层级组根节点问题
-- 检查是否有多个parentId=0的根节点导致显示问题

-- 1. 查看所有字段类型的层级组
SELECT '=== 所有字段层级组 ===' as info;
SELECT 
    id,
    name,
    code,
    parent_id,
    sort,
    status,
    usage_type,
    create_time
FROM system_hierarchy_group 
WHERE usage_type = 'FIELD' 
    AND deleted = 0
ORDER BY parent_id, sort, id;

-- 2. 统计根节点数量（parent_id = 0）
SELECT '=== 根节点统计 ===' as info;
SELECT 
    COUNT(*) as root_count,
    GROUP_CONCAT(CONCAT(id, ':', name) ORDER BY sort, id) as root_nodes
FROM system_hierarchy_group 
WHERE usage_type = 'FIELD' 
    AND deleted = 0 
    AND (parent_id = 0 OR parent_id IS NULL);

-- 3. 查看每个根节点的子节点数量
SELECT '=== 每个根节点的子节点统计 ===' as info;
SELECT 
    p.id as parent_id,
    p.name as parent_name,
    p.sort as parent_sort,
    COUNT(c.id) as children_count,
    GROUP_CONCAT(CONCAT(c.id, ':', c.name) ORDER BY c.sort, c.id) as children
FROM system_hierarchy_group p
LEFT JOIN system_hierarchy_group c ON p.id = c.parent_id AND c.deleted = 0
WHERE p.usage_type = 'FIELD' 
    AND p.deleted = 0 
    AND (p.parent_id = 0 OR p.parent_id IS NULL)
GROUP BY p.id, p.name, p.sort
ORDER BY p.sort, p.id;

-- 4. 树形结构展示
SELECT '=== 完整树形结构 ===' as info;
SELECT 
    CASE 
        WHEN h.parent_id = 0 OR h.parent_id IS NULL THEN CONCAT('├── ', h.name, ' (ID:', h.id, ')')
        ELSE CONCAT('│   ├── ', h.name, ' (ID:', h.id, ', Parent:', h.parent_id, ')')
    END as tree_structure,
    h.sort,
    h.status
FROM system_hierarchy_group h
WHERE h.usage_type = 'FIELD' 
    AND h.deleted = 0
ORDER BY 
    CASE WHEN h.parent_id = 0 OR h.parent_id IS NULL THEN 0 ELSE h.parent_id END,
    h.sort, 
    h.id;

-- 5. 检查是否有数据不一致问题
SELECT '=== 数据一致性检查 ===' as info;
SELECT 
    id,
    name,
    parent_id,
    sort,
    status,
    deleted,
    CASE 
        WHEN parent_id != 0 AND parent_id IS NOT NULL 
             AND NOT EXISTS (
                 SELECT 1 FROM system_hierarchy_group p 
                 WHERE p.id = system_hierarchy_group.parent_id 
                     AND p.deleted = 0
             ) 
        THEN '⚠️  父节点不存在'
        WHEN status != 0 THEN '⚠️  节点已禁用'
        WHEN deleted = 1 THEN '⚠️  节点已删除'
        ELSE '✅ 正常'
    END as status_check
FROM system_hierarchy_group 
WHERE usage_type = 'FIELD'
ORDER BY parent_id, sort, id; 