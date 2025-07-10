-- 添加树形结构相关字段
ALTER TABLE system_region
    ADD COLUMN tree_path varchar(255) NOT NULL DEFAULT '/' COMMENT '树路径',
    ADD COLUMN level int NOT NULL DEFAULT 1 COMMENT '层级深度',
    ADD INDEX idx_tree_path (tree_path) COMMENT '树路径索引';

-- 更新现有数据的树路径和层级
UPDATE system_region r1
SET r1.tree_path = CONCAT('/', r1.id, '/'),
    r1.level = 1
WHERE r1.parent_id = 0 OR r1.parent_id IS NULL;

UPDATE system_region r2
SET r2.tree_path = CONCAT(
        (SELECT r3.tree_path FROM (SELECT * FROM system_region) r3 WHERE r3.id = r2.parent_id),
        r2.id,
        '/'
    ),
    r2.level = (
        LENGTH(CONCAT(
            (SELECT r3.tree_path FROM (SELECT * FROM system_region) r3 WHERE r3.id = r2.parent_id),
            r2.id,
            '/'
        )) - LENGTH(REPLACE(CONCAT(
            (SELECT r3.tree_path FROM (SELECT * FROM system_region) r3 WHERE r3.id = r2.parent_id),
            r2.id,
            '/'
        ), '/', ''))
    )
WHERE r2.parent_id > 0; 