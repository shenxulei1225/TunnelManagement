-- 测试树数据查询
-- 检查system_tree_data_rel表中是否有数据

-- 1. 检查表是否存在
SHOW TABLES LIKE 'system_tree_data_rel';

-- 2. 检查表结构
DESCRIBE system_tree_data_rel;

-- 3. 查询field_category类型的数据
SELECT 
    id,
    tree_type,
    tree_node_id,
    data_type,
    data_id,
    data_name,
    data_type_label,
    display_order,
    is_required,
    status,
    metadata,
    create_time,
    update_time
FROM system_tree_data_rel 
WHERE tree_type = 'field_category'
ORDER BY display_order ASC, create_time ASC;

-- 4. 统计各类型的数据量
SELECT 
    tree_type,
    data_type,
    COUNT(*) as count
FROM system_tree_data_rel 
GROUP BY tree_type, data_type
ORDER BY tree_type, data_type;

-- 5. 检查是否有field_category的根节点数据
SELECT 
    id,
    tree_type,
    tree_node_id,
    data_type,
    data_id,
    data_name,
    data_type_label,
    display_order,
    is_required,
    status,
    metadata
FROM system_tree_data_rel 
WHERE tree_type = 'field_category' AND tree_node_id = 0
ORDER BY display_order ASC; 