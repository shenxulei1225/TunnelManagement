-- 清理通用树数据
-- 用于重新初始化前清理现有数据

-- 清理树数据关联表
DELETE FROM system_tree_data_rel WHERE tree_type IN ('field_category', 'hierarchy_group', 'device_tree');

-- 清理树配置表
DELETE FROM system_tree_config WHERE tree_type IN ('field_category', 'hierarchy_group', 'device_tree');

-- 清理数据类型元数据表
DELETE FROM system_data_type_meta WHERE data_type IN ('field_category', 'field', 'hierarchy_group', 'device_category', 'device');

-- 重置自增ID（可选）
-- ALTER TABLE system_tree_data_rel AUTO_INCREMENT = 1;
-- ALTER TABLE system_tree_config AUTO_INCREMENT = 1;
-- ALTER TABLE system_data_type_meta AUTO_INCREMENT = 1; 