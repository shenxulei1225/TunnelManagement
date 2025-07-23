-- 修复category表的treepath字段（避免NULL约束问题）
-- 分步骤根据parentId重新生成正确的treepath

-- 步骤1：将所有treepath设为临时标记（而不是NULL）
UPDATE system_category SET tree_path = 'TEMP';

-- 步骤2：设置根节点（parent_id = 0 或 NULL）
UPDATE system_category 
SET tree_path = CONCAT('/', id, '/') 
WHERE parent_id = 0 OR parent_id IS NULL;

-- 步骤3：设置第一层子节点
UPDATE system_category c1
JOIN system_category c2 ON c1.parent_id = c2.id
SET c1.tree_path = CONCAT(c2.tree_path, c1.id, '/')
WHERE c1.tree_path = 'TEMP' 
AND c2.tree_path != 'TEMP';

-- 步骤4：设置第二层子节点（重复执行直到所有节点都有treepath）
UPDATE system_category c1
JOIN system_category c2 ON c1.parent_id = c2.id
SET c1.tree_path = CONCAT(c2.tree_path, c1.id, '/')
WHERE c1.tree_path = 'TEMP' 
AND c2.tree_path != 'TEMP';

-- 步骤5：继续设置更深层的子节点（多执行几次确保覆盖所有层级）
UPDATE system_category c1
JOIN system_category c2 ON c1.parent_id = c2.id
SET c1.tree_path = CONCAT(c2.tree_path, c1.id, '/')
WHERE c1.tree_path = 'TEMP' 
AND c2.tree_path != 'TEMP';

UPDATE system_category c1
JOIN system_category c2 ON c1.parent_id = c2.id
SET c1.tree_path = CONCAT(c2.tree_path, c1.id, '/')
WHERE c1.tree_path = 'TEMP' 
AND c2.tree_path != 'TEMP';

UPDATE system_category c1
JOIN system_category c2 ON c1.parent_id = c2.id
SET c1.tree_path = CONCAT(c2.tree_path, c1.id, '/')
WHERE c1.tree_path = 'TEMP' 
AND c2.tree_path != 'TEMP';

UPDATE system_category c1
JOIN system_category c2 ON c1.parent_id = c2.id
SET c1.tree_path = CONCAT(c2.tree_path, c1.id, '/')
WHERE c1.tree_path = 'TEMP' 
AND c2.tree_path != 'TEMP';

-- 步骤6：处理孤儿节点（找不到父节点的），设为根节点
UPDATE system_category c1 
LEFT JOIN system_category c2 ON c1.parent_id = c2.id 
SET c1.tree_path = CONCAT('/', c1.id, '/'), c1.parent_id = 0
WHERE c1.tree_path = 'TEMP' 
AND c1.parent_id != 0 
AND c2.id IS NULL;

-- 验证结果
SELECT 
    '修复完成，验证结果:' as status;

SELECT 
    id,
    name,
    parent_id,
    tree_path,
    CASE 
        WHEN parent_id = 0 OR parent_id IS NULL THEN '根节点'
        ELSE '子节点'
    END as node_type
FROM system_category 
ORDER BY tree_path
LIMIT 20;

-- 检查是否还有未处理的记录
SELECT COUNT(*) as temp_treepath_count 
FROM system_category 
WHERE tree_path = 'TEMP';

-- 显示统计信息
SELECT 
    COUNT(*) as total_categories,
    COUNT(CASE WHEN parent_id = 0 OR parent_id IS NULL THEN 1 END) as root_nodes,
    COUNT(CASE WHEN parent_id != 0 AND parent_id IS NOT NULL THEN 1 END) as child_nodes,
    COUNT(CASE WHEN tree_path = 'TEMP' THEN 1 END) as unprocessed_nodes
FROM system_category; 