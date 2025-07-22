-- 检查分类数据的SQL脚本

-- 1. 检查system_category表是否存在
SHOW TABLES LIKE 'system_category';

-- 2. 检查表结构
DESCRIBE system_category;

-- 3. 检查现有数据
SELECT 
    id, 
    parent_id, 
    name, 
    code, 
    tree_path, 
    level, 
    sort, 
    status, 
    readonly,
    description,
    deleted,
    tenant_id
FROM system_category 
WHERE deleted = 0 
ORDER BY sort, id;

-- 4. 检查是否有根节点
SELECT * FROM system_category WHERE parent_id = 0 AND deleted = 0;

-- 5. 如果没有数据，插入基础数据
INSERT IGNORE INTO system_category (
    id, parent_id, code, name, tree_path, level, sort, status, readonly, description,
    creator, create_time, updater, update_time, deleted, tenant_id
) VALUES (
    1, 0, 'ROOT', '根分类', '/', 1, 0, 1, 1, '系统根分类，所有分类的父级',
    '1', NOW(), '1', NOW(), 0, 1
);

-- 6. 插入一些示例数据
INSERT IGNORE INTO system_category (
    parent_id, code, name, tree_path, level, sort, status, readonly, description,
    creator, create_time, updater, update_time, deleted, tenant_id
) VALUES 
(1, 'ELECTRONICS', '电子产品', '/1/', 2, 1, 1, 0, '电子产品分类', '1', NOW(), '1', NOW(), 0, 1),
(1, 'CLOTHING', '服装服饰', '/1/', 2, 2, 1, 0, '服装服饰分类', '1', NOW(), '1', NOW(), 0, 1),
(1, 'BOOKS', '图书文具', '/1/', 2, 3, 1, 0, '图书文具分类', '1', NOW(), '1', NOW(), 0, 1);

-- 7. 插入二级分类示例
INSERT IGNORE INTO system_category (
    parent_id, code, name, tree_path, level, sort, status, readonly, description,
    creator, create_time, updater, update_time, deleted, tenant_id
) VALUES 
(2, 'PHONES', '手机通讯', '/1/2/', 3, 1, 1, 0, '手机通讯设备', '1', NOW(), '1', NOW(), 0, 1),
(2, 'COMPUTERS', '电脑办公', '/1/2/', 3, 2, 1, 0, '电脑办公设备', '1', NOW(), '1', NOW(), 0, 1);

-- 8. 验证数据插入结果
SELECT 
    id, 
    parent_id, 
    name, 
    code, 
    tree_path, 
    level, 
    sort, 
    status,
    readonly
FROM system_category 
WHERE deleted = 0 
ORDER BY tree_path, sort; 