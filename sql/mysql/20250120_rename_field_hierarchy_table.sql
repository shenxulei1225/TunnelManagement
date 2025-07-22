-- =============================================
-- 重命名字段层级关联表和字段的SQL脚本
-- 将 system_field_def_hierarchy_rel 重命名为 system_field_hierarchy_rel
-- 将 field_def_id 重命名为 field_id
-- =============================================

-- 1. 检查旧表是否存在
SET @table_exists = (
    SELECT COUNT(*)
    FROM information_schema.tables 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_def_hierarchy_rel'
);

-- 2. 如果旧表存在，则进行重命名操作
SET @sql = CASE 
    WHEN @table_exists > 0 THEN 
        'RENAME TABLE system_field_def_hierarchy_rel TO system_field_hierarchy_rel'
    ELSE 
        'SELECT "旧表不存在，跳过重命名" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. 检查新表是否存在且包含旧字段名
SET @field_exists = (
    SELECT COUNT(*)
    FROM information_schema.columns 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND column_name = 'field_def_id'
);

-- 4. 如果存在旧字段名，则重命名字段
SET @sql = CASE 
    WHEN @field_exists > 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel CHANGE COLUMN field_def_id field_id bigint NOT NULL COMMENT ''字段 ID'''
    ELSE 
        'SELECT "字段已经是正确名称或表不存在，跳过字段重命名" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 5. 确保新表存在（如果都不存在则创建）
CREATE TABLE IF NOT EXISTS `system_field_hierarchy_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_id` bigint NOT NULL COMMENT '字段 ID',
  `hierarchy_group_id` bigint NOT NULL COMMENT '分组 ID',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_field_id` (`field_id`),
  KEY `idx_hierarchy_group_id` (`hierarchy_group_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  UNIQUE KEY `uk_field_hierarchy` (`field_id`, `hierarchy_group_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字段-分组 关联表';

-- 6. 检查并删除可能存在的旧约束
SET @constraint_exists = (
    SELECT COUNT(*)
    FROM information_schema.table_constraints 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND constraint_name = 'uk_field_def_hierarchy'
);

SET @sql = CASE 
    WHEN @constraint_exists > 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel DROP INDEX uk_field_def_hierarchy'
    ELSE 
        'SELECT "旧约束不存在，跳过删除" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 7. 确保正确的索引存在（检查后添加）
-- 检查并添加 idx_field_id 索引
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

-- 检查并添加 idx_hierarchy_group_id 索引
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

-- 检查并添加 idx_tenant_id 索引
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

-- 8. 添加唯一约束（如果不存在）
SET @unique_constraint_exists = (
    SELECT COUNT(*)
    FROM information_schema.table_constraints 
    WHERE table_schema = DATABASE() 
    AND table_name = 'system_field_hierarchy_rel'
    AND constraint_name = 'uk_field_hierarchy'
);

SET @sql = CASE 
    WHEN @unique_constraint_exists = 0 THEN 
        'ALTER TABLE system_field_hierarchy_rel ADD CONSTRAINT uk_field_hierarchy UNIQUE (field_id, hierarchy_group_id, tenant_id)'
    ELSE 
        'SELECT "唯一约束已存在，跳过添加" as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 9. 验证表结构
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

-- 10. 验证索引
SELECT 
    INDEX_NAME,
    COLUMN_NAME,
    NON_UNIQUE
FROM information_schema.statistics 
WHERE table_schema = DATABASE() 
AND table_name = 'system_field_hierarchy_rel'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

-- 输出完成信息
SELECT CONCAT(
    '表重命名和字段修改完成！',
    '当前表名: system_field_hierarchy_rel, ',
    '字段名: field_id, hierarchy_group_id'
) AS message; 