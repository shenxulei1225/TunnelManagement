-- =============================================
-- 重建 system_field_hierarchy_rel 表的索引和关联
-- =============================================

-- 1. 删除现有的所有索引（除了主键）
-- 检查并删除索引
SET @sql = IF((SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'system_field_hierarchy_rel' AND index_name = 'idx_field_id') > 0,
    'ALTER TABLE system_field_hierarchy_rel DROP INDEX idx_field_id',
    'SELECT "索引 idx_field_id 不存在，跳过删除" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'system_field_hierarchy_rel' AND index_name = 'idx_hierarchy_group_id') > 0,
    'ALTER TABLE system_field_hierarchy_rel DROP INDEX idx_hierarchy_group_id',
    'SELECT "索引 idx_hierarchy_group_id 不存在，跳过删除" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'system_field_hierarchy_rel' AND index_name = 'idx_tenant_id') > 0,
    'ALTER TABLE system_field_hierarchy_rel DROP INDEX idx_tenant_id',
    'SELECT "索引 idx_tenant_id 不存在，跳过删除" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'system_field_hierarchy_rel' AND index_name = 'uk_field_hierarchy') > 0,
    'ALTER TABLE system_field_hierarchy_rel DROP INDEX uk_field_hierarchy',
    'SELECT "索引 uk_field_hierarchy 不存在，跳过删除" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'system_field_hierarchy_rel' AND index_name = 'uk_field_def_hierarchy') > 0,
    'ALTER TABLE system_field_hierarchy_rel DROP INDEX uk_field_def_hierarchy',
    'SELECT "索引 uk_field_def_hierarchy 不存在，跳过删除" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. 重新创建基本索引（检查后添加）
-- 添加 idx_field_id 索引
SET @index_exists = (
    SELECT COUNT(*)
    FROM information_schema.statistics 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND index_name = 'idx_field_id'
);

SET @sql = CASE 
    WHEN @index_exists = 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD INDEX idx_field_id (field_id)'
    ELSE 
        'SELECT "索引 idx_field_id 已存在，跳过添加" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 idx_hierarchy_group_id 索引
SET @index_exists = (
    SELECT COUNT(*)
    FROM information_schema.statistics 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND index_name = 'idx_hierarchy_group_id'
);

SET @sql = CASE 
    WHEN @index_exists = 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD INDEX idx_hierarchy_group_id (hierarchy_group_id)'
    ELSE 
        'SELECT "索引 idx_hierarchy_group_id 已存在，跳过添加" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 idx_tenant_id 索引
SET @index_exists = (
    SELECT COUNT(*)
    FROM information_schema.statistics 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND index_name = 'idx_tenant_id'
);

SET @sql = CASE 
    WHEN @index_exists = 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD INDEX idx_tenant_id (tenant_id)'
    ELSE 
        'SELECT "索引 idx_tenant_id 已存在，跳过添加" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. 添加复合索引（检查后添加）
-- 添加 idx_field_tenant 复合索引
SET @index_exists = (
    SELECT COUNT(*)
    FROM information_schema.statistics 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND index_name = 'idx_field_tenant'
);

SET @sql = CASE 
    WHEN @index_exists = 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD INDEX idx_field_tenant (field_id, tenant_id)'
    ELSE 
        'SELECT "索引 idx_field_tenant 已存在，跳过添加" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 idx_hierarchy_tenant 复合索引
SET @index_exists = (
    SELECT COUNT(*)
    FROM information_schema.statistics 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND index_name = 'idx_hierarchy_tenant'
);

SET @sql = CASE 
    WHEN @index_exists = 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD INDEX idx_hierarchy_tenant (hierarchy_group_id, tenant_id)'
    ELSE 
        'SELECT "索引 idx_hierarchy_tenant 已存在，跳过添加" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. 添加唯一约束（检查后添加）
SET @constraint_exists = (
    SELECT COUNT(*)
    FROM information_schema.table_constraints 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND constraint_name = 'uk_field_hierarchy'
);

SET @sql = CASE 
    WHEN @constraint_exists = 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD CONSTRAINT uk_field_hierarchy UNIQUE (field_id, hierarchy_group_id, tenant_id)'
    ELSE 
        'SELECT "唯一约束 uk_field_hierarchy 已存在，跳过添加" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 5. 添加外键约束（如果需要）
-- 检查 system_field_def 表是否存在
SET @field_table_exists = (
    SELECT COUNT(*)
    FROM information_schema.tables 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_def'
);

-- 检查 system_hierarchy_group 表是否存在
SET @hierarchy_table_exists = (
    SELECT COUNT(*)
    FROM information_schema.tables 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_hierarchy_group'
);

-- 检查并删除旧的外键约束
SET @fk_field_exists = (
    SELECT COUNT(*)
    FROM information_schema.table_constraints 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND constraint_name = 'fk_field_hierarchy_field'
);

SET @sql = CASE 
    WHEN @fk_field_exists > 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel DROP FOREIGN KEY fk_field_hierarchy_field'
    ELSE 
        'SELECT "外键约束 fk_field_hierarchy_field 不存在，跳过删除" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @fk_group_exists = (
    SELECT COUNT(*)
    FROM information_schema.table_constraints 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND constraint_name = 'fk_field_hierarchy_group'
);

SET @sql = CASE 
    WHEN @fk_group_exists > 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel DROP FOREIGN KEY fk_field_hierarchy_group'
    ELSE 
        'SELECT "外键约束 fk_field_hierarchy_group 不存在，跳过删除" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 如果字段表存在，添加外键约束
SET @sql = CASE 
    WHEN @field_table_exists > 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD CONSTRAINT fk_field_hierarchy_field FOREIGN KEY (field_id) REFERENCES system_field_def(id) ON DELETE CASCADE'
    ELSE 
        'SELECT "system_field_def 表不存在，跳过字段外键约束" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 如果分级组表存在，添加外键约束
SET @sql = CASE 
    WHEN @hierarchy_table_exists > 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD CONSTRAINT fk_field_hierarchy_group FOREIGN KEY (hierarchy_group_id) REFERENCES system_hierarchy_group(id) ON DELETE CASCADE'
    ELSE 
        'SELECT "system_hierarchy_group 表不存在，跳过分组外键约束" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 6. 验证表结构
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    COLUMN_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM information_schema.columns 
WHERE table_schema = DATABASE() 
AND table_name = 'system_field_hierarchy_rel'
ORDER BY ordinal_position;

-- 7. 验证索引
SELECT 
    INDEX_NAME,
    COLUMN_NAME,
    NON_UNIQUE,
    INDEX_TYPE
FROM information_schema.statistics 
WHERE table_schema = DATABASE() 
AND table_name = 'system_field_hierarchy_rel'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

-- 8. 验证外键约束
SELECT 
    kcu.CONSTRAINT_NAME,
    kcu.COLUMN_NAME,
    kcu.REFERENCED_TABLE_NAME,
    kcu.REFERENCED_COLUMN_NAME,
    COALESCE(rc.DELETE_RULE, 'N/A') AS DELETE_RULE,
    COALESCE(rc.UPDATE_RULE, 'N/A') AS UPDATE_RULE
FROM information_schema.key_column_usage kcu
LEFT JOIN information_schema.referential_constraints rc 
    ON kcu.CONSTRAINT_NAME = rc.CONSTRAINT_NAME 
    AND kcu.TABLE_SCHEMA = rc.CONSTRAINT_SCHEMA
WHERE kcu.table_schema = DATABASE() 
AND kcu.table_name = 'system_field_hierarchy_rel'
AND kcu.referenced_table_name IS NOT NULL;

-- 9. 统计数据
SELECT 
    COUNT(*) as total_records,
    COUNT(DISTINCT field_id) as unique_fields,
    COUNT(DISTINCT hierarchy_group_id) as unique_groups
FROM system_field_hierarchy_rel;

-- 输出完成信息
SELECT CONCAT(
    '索引和关联重建完成！',
    '表名: system_field_hierarchy_rel, ',
    '索引数量: ', (
        SELECT COUNT(DISTINCT INDEX_NAME) 
        FROM information_schema.statistics 
        WHERE table_schema = DATABASE() 
        AND table_name = 'system_field_hierarchy_rel'
    )
) AS message; 