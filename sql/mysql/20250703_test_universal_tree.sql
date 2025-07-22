-- 测试通用树数据初始化
-- 用于验证数据是否正确插入

-- 1. 检查表是否存在
SELECT 
  TABLE_NAME,
  TABLE_COMMENT
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME IN ('system_tree_data_rel', 'system_tree_config', 'system_data_type_meta');

-- 2. 检查树配置数据
SELECT 
  tree_type,
  tree_name,
  description,
  status
FROM system_tree_config 
WHERE tree_type IN ('field_category', 'hierarchy_group', 'device_tree');

-- 3. 检查数据类型元数据
SELECT 
  data_type,
  data_name,
  description,
  status
FROM system_data_type_meta 
WHERE data_type IN ('field_category', 'field', 'hierarchy_group', 'device_category', 'device');

-- 4. 检查字段分类树数据
SELECT 
  tree_type,
  data_type,
  data_name,
  display_order,
  is_required,
  status
FROM system_tree_data_rel 
WHERE tree_type = 'field_category'
ORDER BY display_order;

-- 5. 检查分级组树数据
SELECT 
  tree_type,
  data_type,
  data_name,
  display_order,
  is_required,
  status
FROM system_tree_data_rel 
WHERE tree_type = 'hierarchy_group'
ORDER BY display_order;

-- 6. 检查设备树数据
SELECT 
  tree_type,
  data_type,
  data_name,
  display_order,
  is_required,
  status
FROM system_tree_data_rel 
WHERE tree_type = 'device_tree'
ORDER BY display_order;

-- 7. 统计各树类型的数据量
SELECT 
  tree_type,
  COUNT(*) as node_count,
  COUNT(CASE WHEN data_type = 'field_category' THEN 1 END) as category_count,
  COUNT(CASE WHEN data_type = 'field' THEN 1 END) as field_count
FROM system_tree_data_rel 
WHERE tree_type IN ('field_category', 'hierarchy_group', 'device_tree')
GROUP BY tree_type; 