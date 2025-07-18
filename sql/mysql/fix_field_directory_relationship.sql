-- 修复字段与目录关系设计
-- 解决删除目录导致的字段丢失问题

-- 1. 备份当前数据
CREATE TABLE system_field_def_backup AS SELECT * FROM system_field_def;
CREATE TABLE system_field_def_category_rel_backup AS SELECT * FROM system_field_def_category_rel;

-- 2. 清理重复的关联数据
DELETE t1 FROM system_field_def_category_rel t1
INNER JOIN system_field_def_category_rel t2 
WHERE 
    t1.field_def_id = t2.field_def_id 
    AND t1.category_id = t2.category_id 
    AND t1.tenant_id = t2.tenant_id
    AND t1.id < t2.id
    AND t1.deleted = 0 
    AND t2.deleted = 0;

-- 3. 将 system_field_def.category_id 数据迁移到关联表
INSERT INTO system_field_def_category_rel (field_def_id, category_id, tenant_id, creator, create_time, updater, update_time, deleted, required, sort)
SELECT 
    id as field_def_id,
    category_id,
    tenant_id,
    creator,
    create_time,
    updater,
    update_time,
    deleted,
    0 as required,
    sort
FROM system_field_def 
WHERE category_id > 0 
  AND deleted = 0
  AND id NOT IN (
    SELECT field_def_id 
    FROM system_field_def_category_rel 
    WHERE deleted = 0
  );

-- 4. 删除 system_field_def 表中的 category_id 字段
ALTER TABLE system_field_def DROP COLUMN category_id;
ALTER TABLE system_field_def DROP INDEX uk_category_field;
ALTER TABLE system_field_def DROP INDEX idx_category;

-- 5. 为关联表添加唯一约束，防止重复关联
ALTER TABLE system_field_def_category_rel 
ADD UNIQUE INDEX uk_field_category_tenant (field_def_id, category_id, tenant_id, deleted);

-- 6. 修改外键约束，添加删除保护
-- 删除原有的 CASCADE 约束
ALTER TABLE system_field_def_category_rel 
DROP FOREIGN KEY fk_rel_category;

-- 重新添加约束，但不使用 CASCADE
ALTER TABLE system_field_def_category_rel 
ADD CONSTRAINT fk_rel_category 
FOREIGN KEY (category_id) REFERENCES system_field_category (id);

-- 7. 创建删除目录时的检查函数
DELIMITER $$

CREATE FUNCTION check_category_deletable(category_id BIGINT) 
RETURNS BOOLEAN
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE field_count INT DEFAULT 0;
    
    -- 检查是否有字段关联到此目录
    SELECT COUNT(*) INTO field_count
    FROM system_field_def_category_rel 
    WHERE category_id = category_id 
      AND deleted = 0;
    
    -- 检查是否有子目录
    SELECT COUNT(*) + field_count INTO field_count
    FROM system_field_category 
    WHERE parent_id = category_id 
      AND deleted = 0;
    
    RETURN field_count = 0;
END$$

DELIMITER ;

-- 8. 创建获取字段关联目录的函数
DELIMITER $$

CREATE FUNCTION get_field_categories(field_id BIGINT) 
RETURNS TEXT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE result TEXT DEFAULT '';
    
    SELECT GROUP_CONCAT(c.name SEPARATOR ', ') INTO result
    FROM system_field_def_category_rel r
    JOIN system_field_category c ON r.category_id = c.id
    WHERE r.field_def_id = field_id 
      AND r.deleted = 0 
      AND c.deleted = 0;
    
    RETURN IFNULL(result, '未分类');
END$$

DELIMITER ;

-- 9. 创建视图，方便查询字段的目录信息
CREATE VIEW v_field_with_categories AS
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
    GROUP_CONCAT(c.name ORDER BY c.sort SEPARATOR ', ') as category_names,
    GROUP_CONCAT(c.id ORDER BY c.sort SEPARATOR ',') as category_ids,
    COUNT(r.category_id) as category_count
FROM system_field_def f
LEFT JOIN system_field_def_category_rel r ON f.id = r.field_def_id AND r.deleted = 0
LEFT JOIN system_field_category c ON r.category_id = c.id AND c.deleted = 0
WHERE f.deleted = 0
GROUP BY f.id;

-- 10. 创建删除目录前的检查存储过程
DELIMITER $$

CREATE PROCEDURE check_category_before_delete(IN category_id BIGINT, OUT can_delete BOOLEAN, OUT message TEXT)
BEGIN
    DECLARE field_count INT DEFAULT 0;
    DECLARE child_count INT DEFAULT 0;
    DECLARE category_name VARCHAR(64);
    
    -- 获取目录名称
    SELECT name INTO category_name FROM system_field_category WHERE id = category_id;
    
    -- 检查是否有字段关联
    SELECT COUNT(*) INTO field_count
    FROM system_field_def_category_rel 
    WHERE category_id = category_id AND deleted = 0;
    
    -- 检查是否有子目录
    SELECT COUNT(*) INTO child_count
    FROM system_field_category 
    WHERE parent_id = category_id AND deleted = 0;
    
    IF field_count > 0 OR child_count > 0 THEN
        SET can_delete = FALSE;
        SET message = CONCAT('目录"', category_name, '"无法删除：');
        
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

-- 11. 创建安全删除目录的存储过程
DELIMITER $$

CREATE PROCEDURE safe_delete_category(IN category_id BIGINT, OUT success BOOLEAN, OUT message TEXT)
BEGIN
    DECLARE can_delete BOOLEAN DEFAULT TRUE;
    DECLARE category_name VARCHAR(64);
    
    -- 检查是否可以删除
    CALL check_category_before_delete(category_id, can_delete, message);
    
    IF can_delete THEN
        -- 获取目录名称
        SELECT name INTO category_name FROM system_field_category WHERE id = category_id;
        
        -- 软删除目录
        UPDATE system_field_category 
        SET deleted = 1, update_time = NOW() 
        WHERE id = category_id;
        
        SET success = TRUE;
        SET message = CONCAT('目录"', category_name, '"已成功删除');
    ELSE
        SET success = FALSE;
    END IF;
END$$

DELIMITER ;

-- 12. 创建字段重新分类的存储过程
DELIMITER $$

CREATE PROCEDURE reassign_field_categories(
    IN field_id BIGINT, 
    IN new_category_ids TEXT, 
    OUT success BOOLEAN, 
    OUT message TEXT
)
BEGIN
    DECLARE category_id BIGINT;
    DECLARE i INT DEFAULT 1;
    DECLARE category_count INT;
    
    -- 删除原有关联
    UPDATE system_field_def_category_rel 
    SET deleted = 1, update_time = NOW() 
    WHERE field_def_id = field_id AND deleted = 0;
    
    -- 解析新的分类ID列表
    SET category_count = (LENGTH(new_category_ids) - LENGTH(REPLACE(new_category_ids, ',', '')) + 1);
    
    -- 添加新的关联
    WHILE i <= category_count DO
        SET category_id = CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(new_category_ids, ',', i), ',', -1) AS UNSIGNED);
        
        INSERT INTO system_field_def_category_rel (
            field_def_id, category_id, tenant_id, creator, create_time, 
            updater, update_time, deleted, required, sort
        ) VALUES (
            field_id, category_id, 
            (SELECT tenant_id FROM system_field_def WHERE id = field_id),
            (SELECT creator FROM system_field_def WHERE id = field_id),
            NOW(), 
            (SELECT updater FROM system_field_def WHERE id = field_id),
            NOW(), 0, 0, 0
        );
        
        SET i = i + 1;
    END WHILE;
    
    SET success = TRUE;
    SET message = '字段分类更新成功';
END$$

DELIMITER ;

-- 13. 验证修复结果
SELECT '修复完成，验证结果：' as info;

-- 检查是否有字段没有分类
SELECT 
    COUNT(*) as uncategorized_fields,
    '未分类字段数量' as description
FROM system_field_def f
WHERE f.deleted = 0 
  AND NOT EXISTS (
    SELECT 1 FROM system_field_def_category_rel r 
    WHERE r.field_def_id = f.id AND r.deleted = 0
  );

-- 检查重复关联
SELECT 
    field_def_id, 
    category_id, 
    COUNT(*) as duplicate_count
FROM system_field_def_category_rel 
WHERE deleted = 0 
GROUP BY field_def_id, category_id 
HAVING COUNT(*) > 1;

-- 显示字段分类统计
SELECT 
    c.name as category_name,
    COUNT(r.field_def_id) as field_count
FROM system_field_category c
LEFT JOIN system_field_def_category_rel r ON c.id = r.category_id AND r.deleted = 0
WHERE c.deleted = 0
GROUP BY c.id, c.name
ORDER BY c.sort, c.name; 