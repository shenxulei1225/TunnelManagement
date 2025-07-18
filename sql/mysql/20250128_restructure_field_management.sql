-- 字段管理重构：简化命名和表结构


-- 3. 创建字段与语义目录的关联表
CREATE TABLE system_field_semantic_directory_rel (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    field_id BIGINT NOT NULL COMMENT '字段ID',
    semantic_directory_id BIGINT NOT NULL COMMENT '语义目录ID',
    sort INT DEFAULT 0 COMMENT '排序',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_field_semantic_directory (field_id, semantic_directory_id, tenant_id, deleted),
    KEY idx_semantic_directory_id (semantic_directory_id),
    KEY idx_field_id (field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字段与语义目录关联表';

-- 4. 为system_category表添加business_type字段
ALTER TABLE system_category 
ADD COLUMN business_type VARCHAR(50) DEFAULT 'FIELD_CATEGORY' COMMENT '业务类型，用于区分不同业务的分类';

-- 4. 更新关联表字段名


-- 5. 迁移现有字段的语义目录关联（如果有的话）
-- 注意：这里假设system_field表中有semantic_directory_id字段，如果没有则跳过
-- INSERT INTO system_field_semantic_directory_rel (field_id, semantic_directory_id, tenant_id, creator, create_time, updater, update_time, deleted, sort)
-- SELECT 
--     id as field_id,
--     semantic_directory_id,
--     tenant_id,
--     creator,
--     create_time,
--     updater,
--     update_time,
--     deleted,
--     0 as sort
-- FROM system_field 
-- WHERE semantic_directory_id IS NOT NULL AND semantic_directory_id > 0 AND deleted = 0;

-- 6. 创建索引
CREATE INDEX idx_system_category_parent_id ON system_category(parent_id);
CREATE INDEX idx_system_category_tree_path ON system_category(tree_path);
CREATE INDEX idx_system_field_category_rel_field_id ON system_field_category_rel(field_id);
CREATE INDEX idx_system_field_category_rel_category_id ON system_field_category_rel(category_id);

-- 7. 添加外键约束（可选，根据实际需要决定）
-- ALTER TABLE system_field_category_rel 
-- ADD CONSTRAINT fk_field_category_rel_field_id 
-- FOREIGN KEY (field_id) REFERENCES system_field(id);

-- ALTER TABLE system_field_category_rel 
-- ADD CONSTRAINT fk_field_category_rel_category_id 
-- FOREIGN KEY (category_id) REFERENCES system_category(id);

-- ALTER TABLE system_field_semantic_directory_rel 
-- ADD CONSTRAINT fk_field_semantic_directory_rel_field_id 
-- FOREIGN KEY (field_id) REFERENCES system_field(id);

-- ALTER TABLE system_field_semantic_directory_rel 
-- ADD CONSTRAINT fk_field_semantic_directory_rel_directory_id 
-- FOREIGN KEY (semantic_directory_id) REFERENCES system_directory(id); 