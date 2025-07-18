-- 语义目录删除保护机制
-- 解决删除语义目录时字段丢失的问题

-- 1. 备份当前数据
CREATE TABLE system_directory_backup AS SELECT * FROM system_directory;
CREATE TABLE system_field_def_backup AS SELECT * FROM system_field_def;

-- 2. 检查当前字段与语义目录的关联情况
SELECT 
    '字段语义目录关联统计' as info,
    COUNT(*) as total_fields,
    COUNT(CASE WHEN semantic_directory_id IS NOT NULL AND semantic_directory_id > 0 THEN 1 END) as categorized_fields,
    COUNT(CASE WHEN semantic_directory_id IS NULL OR semantic_directory_id = 0 THEN 1 END) as uncategorized_fields
FROM system_field_def 
WHERE deleted = 0;

-- 3. 查看每个语义目录关联的字段数量
SELECT 
    d.id as directory_id,
    d.name as directory_name,
    d.code as directory_code,
    COUNT(f.id) as field_count
FROM system_directory d
LEFT JOIN system_field_def f ON d.id = f.semantic_directory_id AND f.deleted = 0
WHERE d.business_type = 'FIELD_SEMANTIC' 
  AND d.deleted = 0
GROUP BY d.id, d.name, d.code
ORDER BY field_count DESC;

-- 4. 创建语义目录删除前的检查函数
DELIMITER $$

CREATE FUNCTION check_semantic_directory_deletable(directory_id BIGINT) 
RETURNS BOOLEAN
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE field_count INT DEFAULT 0;
    
    -- 检查是否有字段关联到此语义目录
    SELECT COUNT(*) INTO field_count
    FROM system_field_def 
    WHERE semantic_directory_id = directory_id 
      AND deleted = 0;
    
    -- 检查是否有子目录
    SELECT COUNT(*) + field_count INTO field_count
    FROM system_directory 
    WHERE parent_id = directory_id 
      AND business_type = 'FIELD_SEMANTIC'
      AND deleted = 0;
    
    RETURN field_count = 0;
END$$

DELIMITER ;

-- 5. 创建获取字段语义目录的函数
DELIMITER $$

CREATE FUNCTION get_field_semantic_directory(field_id BIGINT) 
RETURNS TEXT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE result TEXT DEFAULT '';
    
    SELECT d.name INTO result
    FROM system_field_def f
    LEFT JOIN system_directory d ON f.semantic_directory_id = d.id AND d.deleted = 0
    WHERE f.id = field_id AND f.deleted = 0;
    
    RETURN IFNULL(result, '未分类');
END$$

DELIMITER ;

-- 6. 创建视图，方便查询字段的语义目录信息
CREATE VIEW v_field_with_semantic_directory AS
SELECT 
    f.id,
    f.field_key,
    f.field_label,
    f.value_type,
    f.unit,
    f.sort,
    f.creator,
    f.create_time,
    f.tenant_id,
    f.semantic_directory_id,
    d.name as semantic_directory_name,
    d.code as semantic_directory_code,
    d.description as semantic_directory_description
FROM system_field_def f
LEFT JOIN system_directory d ON f.semantic_directory_id = d.id AND d.deleted = 0
WHERE f.deleted = 0;

-- 7. 创建删除语义目录前的检查存储过程
DELIMITER $$

CREATE PROCEDURE check_semantic_directory_before_delete(
    IN directory_id BIGINT, 
    OUT can_delete BOOLEAN, 
    OUT message TEXT
)
BEGIN
    DECLARE field_count INT DEFAULT 0;
    DECLARE child_count INT DEFAULT 0;
    DECLARE directory_name VARCHAR(100);
    
    -- 获取目录名称
    SELECT name INTO directory_name FROM system_directory WHERE id = directory_id;
    
    -- 检查是否有字段关联
    SELECT COUNT(*) INTO field_count
    FROM system_field_def 
    WHERE semantic_directory_id = directory_id AND deleted = 0;
    
    -- 检查是否有子目录
    SELECT COUNT(*) INTO child_count
    FROM system_directory 
    WHERE parent_id = directory_id 
      AND business_type = 'FIELD_SEMANTIC'
      AND deleted = 0;
    
    IF field_count > 0 OR child_count > 0 THEN
        SET can_delete = FALSE;
        SET message = CONCAT('语义目录"', directory_name, '"无法删除：');
        
        IF field_count > 0 THEN
            SET message = CONCAT(message, '关联了 ', field_count, ' 个字段');
        END IF;
        
        IF child_count > 0 THEN
            IF field_count > 0 THEN
                SET message = CONCAT(message, '，');
            END IF;
            SET message = CONCAT(message, '包含 ', child_count, ' 个子目录');
        END IF;
    ELSE
        SET can_delete = TRUE;
        SET message = '可以安全删除';
    END IF;
END$$

DELIMITER ;

-- 8. 创建安全删除语义目录的存储过程
DELIMITER $$

CREATE PROCEDURE safe_delete_semantic_directory(
    IN directory_id BIGINT, 
    OUT success BOOLEAN, 
    OUT message TEXT
)
BEGIN
    DECLARE can_delete BOOLEAN DEFAULT TRUE;
    DECLARE directory_name VARCHAR(100);
    
    -- 检查是否可以删除
    CALL check_semantic_directory_before_delete(directory_id, can_delete, message);
    
    IF can_delete THEN
        -- 获取目录名称
        SELECT name INTO directory_name FROM system_directory WHERE id = directory_id;
        
        -- 软删除目录
        UPDATE system_directory 
        SET deleted = 1, update_time = NOW() 
        WHERE id = directory_id;
        
        SET success = TRUE;
        SET message = CONCAT('语义目录"', directory_name, '"已成功删除');
    ELSE
        SET success = FALSE;
    END IF;
END$$

DELIMITER ;

-- 9. 创建字段重新分类的存储过程
DELIMITER $$

CREATE PROCEDURE reassign_field_semantic_directory(
    IN field_id BIGINT, 
    IN new_directory_id BIGINT, 
    OUT success BOOLEAN, 
    OUT message TEXT
)
BEGIN
    DECLARE field_exists BOOLEAN DEFAULT FALSE;
    DECLARE directory_exists BOOLEAN DEFAULT FALSE;
    
    -- 检查字段是否存在
    SELECT COUNT(*) > 0 INTO field_exists
    FROM system_field_def 
    WHERE id = field_id AND deleted = 0;
    
    -- 检查新目录是否存在
    SELECT COUNT(*) > 0 INTO directory_exists
    FROM system_directory 
    WHERE id = new_directory_id 
      AND business_type = 'FIELD_SEMANTIC'
      AND deleted = 0;
    
    IF NOT field_exists THEN
        SET success = FALSE;
        SET message = '字段不存在';
    ELSEIF NOT directory_exists AND new_directory_id IS NOT NULL AND new_directory_id > 0 THEN
        SET success = FALSE;
        SET message = '目标语义目录不存在';
    ELSE
        -- 更新字段的语义目录
        UPDATE system_field_def 
        SET semantic_directory_id = new_directory_id, 
            update_time = NOW() 
        WHERE id = field_id;
        
        SET success = TRUE;
        SET message = '字段语义目录更新成功';
    END IF;
END$$

DELIMITER ;

-- 10. 创建批量字段重新分类的存储过程
DELIMITER $$

CREATE PROCEDURE batch_reassign_fields_semantic_directory(
    IN field_ids TEXT, 
    IN new_directory_id BIGINT, 
    OUT success BOOLEAN, 
    OUT message TEXT
)
BEGIN
    DECLARE field_id BIGINT;
    DECLARE i INT DEFAULT 1;
    DECLARE field_count INT;
    DECLARE processed_count INT DEFAULT 0;
    DECLARE error_count INT DEFAULT 0;
    
    -- 解析字段ID列表
    SET field_count = (LENGTH(field_ids) - LENGTH(REPLACE(field_ids, ',', '')) + 1);
    
    -- 批量更新字段
    WHILE i <= field_count DO
        SET field_id = CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(field_ids, ',', i), ',', -1) AS UNSIGNED);
        
        -- 检查字段是否存在
        IF EXISTS(SELECT 1 FROM system_field_def WHERE id = field_id AND deleted = 0) THEN
            UPDATE system_field_def 
            SET semantic_directory_id = new_directory_id, 
                update_time = NOW() 
            WHERE id = field_id;
            SET processed_count = processed_count + 1;
        ELSE
            SET error_count = error_count + 1;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    SET success = TRUE;
    SET message = CONCAT('批量更新完成：成功 ', processed_count, ' 个，失败 ', error_count, ' 个');
END$$

DELIMITER ;

-- 11. 创建语义目录迁移的存储过程
DELIMITER $$

CREATE PROCEDURE migrate_semantic_directory_fields(
    IN source_directory_id BIGINT,
    IN target_directory_id BIGINT,
    OUT success BOOLEAN,
    OUT message TEXT
)
BEGIN
    DECLARE field_count INT DEFAULT 0;
    DECLARE source_name VARCHAR(100);
    DECLARE target_name VARCHAR(100);
    
    -- 获取源目录和目标目录名称
    SELECT name INTO source_name FROM system_directory WHERE id = source_directory_id;
    SELECT name INTO target_name FROM system_directory WHERE id = target_directory_id;
    
    -- 统计需要迁移的字段数量
    SELECT COUNT(*) INTO field_count
    FROM system_field_def 
    WHERE semantic_directory_id = source_directory_id AND deleted = 0;
    
    IF field_count = 0 THEN
        SET success = FALSE;
        SET message = CONCAT('源目录"', source_name, '"没有关联的字段');
    ELSE
        -- 迁移字段到目标目录
        UPDATE system_field_def 
        SET semantic_directory_id = target_directory_id, 
            update_time = NOW() 
        WHERE semantic_directory_id = source_directory_id AND deleted = 0;
        
        SET success = TRUE;
        SET message = CONCAT('成功将 ', field_count, ' 个字段从"', source_name, '"迁移到"', target_name, '"');
    END IF;
END$$

DELIMITER ;

-- 12. 验证修复结果
SELECT '修复完成，验证结果：' as info;

-- 检查是否有字段没有语义目录
SELECT 
    COUNT(*) as uncategorized_fields,
    '未分类字段数量' as description
FROM system_field_def f
WHERE f.deleted = 0 
  AND (f.semantic_directory_id IS NULL OR f.semantic_directory_id = 0);

-- 显示语义目录统计
SELECT 
    d.name as directory_name,
    d.code as directory_code,
    COUNT(f.id) as field_count
FROM system_directory d
LEFT JOIN system_field_def f ON d.id = f.semantic_directory_id AND f.deleted = 0
WHERE d.business_type = 'FIELD_SEMANTIC' 
  AND d.deleted = 0
GROUP BY d.id, d.name, d.code
ORDER BY field_count DESC;

-- 13. 使用示例
-- 检查目录是否可以删除
-- CALL check_semantic_directory_before_delete(1, @can_delete, @message);
-- SELECT @can_delete, @message;

-- 安全删除目录
-- CALL safe_delete_semantic_directory(1, @success, @message);
-- SELECT @success, @message;

-- 重新分类字段
-- CALL reassign_field_semantic_directory(1, 2, @success, @message);
-- SELECT @success, @message;

-- 批量重新分类
-- CALL batch_reassign_fields_semantic_directory('1,2,3', 2, @success, @message);
-- SELECT @success, @message;

-- 迁移目录下的所有字段
-- CALL migrate_semantic_directory_fields(1, 2, @success, @message);
-- SELECT @success, @message; 