-- 测试：将所有节点设为根节点，看看是否能正常显示161条数据
-- 这样可以排除树结构问题，专注于数据显示

-- 备份当前状态（可选）
-- CREATE TABLE system_category_backup AS SELECT * FROM system_category;

-- 将所有节点设为根节点
UPDATE system_category 
SET 
    parent_id = 0,
    tree_path = CONCAT('/', id, '/')
WHERE 1=1;

-- 验证结果
SELECT 
    '所有节点已设为根节点，验证结果:' as status;

-- 查看前20条记录
SELECT 
    id,
    name,
    parent_id,
    tree_path,
    '根节点' as node_type
FROM system_category 
ORDER BY id
LIMIT 20;

-- 显示总数统计
SELECT 
    COUNT(*) as total_categories,
    COUNT(CASE WHEN parent_id = 0 THEN 1 END) as root_nodes,
    COUNT(CASE WHEN parent_id != 0 THEN 1 END) as child_nodes
FROM system_category;

-- 显示所有数据的简要信息
SELECT 
    CONCAT('共有 ', COUNT(*), ' 条分类数据，全部设为根节点') as summary
FROM system_category; 