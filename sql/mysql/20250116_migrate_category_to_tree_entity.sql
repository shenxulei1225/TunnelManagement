-- CategoryDO迁移到TreeEntity框架
-- 该脚本用于确保system_category表支持TreeEntity框架的所有功能

-- 检查并添加必要的字段（如果不存在）
-- treePath和level字段应该已经存在，这里确保数据一致性

-- 1. 确保root节点存在且符合TreeEntity规范
INSERT IGNORE INTO system_category (
    id, parent_id, code, name, tree_path, level, sort, status, readonly, description,
    creator, create_time, updater, update_time, deleted, tenant_id
) VALUES (
    1, 0, 'ROOT', '根分类', '/', 1, 0, 1, 1, '系统根分类，所有分类的父级',
    '1', NOW(), '1', NOW(), 0, 1
);

-- 2. 更新现有数据，确保treePath格式符合TreeEntity规范
-- TreeEntity使用"/"开始和结尾的路径格式，而原有可能使用其他格式
UPDATE system_category 
SET tree_path = CASE 
    WHEN parent_id = 0 OR parent_id IS NULL THEN '/'
    ELSE CONCAT('/', REPLACE(TRIM(LEADING '0' FROM tree_path), '/', '/'), '/')
END
WHERE deleted = 0;

-- 3. 重新计算level字段，确保与TreeEntity规范一致
-- TreeEntity中根节点level=1
UPDATE system_category c1
JOIN (
    SELECT id, 
           CASE 
               WHEN parent_id = 0 OR parent_id IS NULL THEN 1
               ELSE (LENGTH(tree_path) - LENGTH(REPLACE(tree_path, '/', ''))) 
           END as new_level
    FROM system_category 
    WHERE deleted = 0
) c2 ON c1.id = c2.id
SET c1.level = c2.new_level
WHERE c1.deleted = 0;

-- 4. 添加CategoryDO TreeEntity迁移完成标记
INSERT IGNORE INTO system_dict_data (
    id, sort, label, value, dict_type, status, color_type, css_class, remark,
    creator, create_time, updater, update_time, deleted
) VALUES (
    NULL, 1, 'CategoryDO已迁移TreeEntity', 'category_tree_entity_migrated', 'system_migration_status', 
    0, 'success', '', 'CategoryDO已成功迁移到TreeEntity框架',
    '1', NOW(), '1', NOW(), 0
);

-- 5. 验证迁移结果的查询（可用于手动验证）
-- SELECT id, parent_id, name, tree_path, level, sort FROM system_category WHERE deleted = 0 ORDER BY sort; 