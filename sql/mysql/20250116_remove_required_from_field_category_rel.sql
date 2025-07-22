-- 移除字段分类关联表中的required字段
-- 日期：2025-01-16
-- 目的：让字段分类关联表与字段分组关联表保持一致，只保留sort字段

-- 1. 备份表结构和数据（可选，生产环境建议执行）
-- CREATE TABLE system_field_category_rel_backup AS SELECT * FROM system_field_category_rel;

-- 2. 移除required字段
ALTER TABLE system_field_category_rel DROP COLUMN required;

-- 3. 验证表结构
-- DESCRIBE system_field_category_rel;

-- 预期结果：表中应该只有以下字段
-- id, field_id, category_id, sort, tenant_id, creator, create_time, updater, update_time, deleted 