-- 为字段定义表添加语义目录ID字段，用于字段的语义分类管理
ALTER TABLE system_field_def
    ADD COLUMN semantic_directory_id BIGINT NULL DEFAULT NULL COMMENT '语义目录ID，用于字段的语义分类管理';

-- 为语义目录ID字段添加索引，优化查询性能
CREATE INDEX idx_system_field_def_semantic_directory_id 
    ON system_field_def(semantic_directory_id); 