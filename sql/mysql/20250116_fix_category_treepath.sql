-- 修复category表的treepath字段
-- 根据parentId重新生成正确的treepath

-- 首先清空所有treepath
UPDATE system_category SET tree_path = NULL;

-- 创建递归存储过程来生成treepath
DELIMITER //

DROP PROCEDURE IF EXISTS UpdateTreePath//

CREATE PROCEDURE UpdateTreePath()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE current_id BIGINT;
    DECLARE current_parent_id BIGINT;
    DECLARE current_path VARCHAR(1000);
    DECLARE parent_path VARCHAR(1000);
    
    -- 游标声明
    DECLARE cur CURSOR FOR 
        SELECT id, parent_id FROM system_category ORDER BY parent_id, id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- 第一步：设置根节点的treepath
    UPDATE system_category SET tree_path = CONCAT('/', id, '/') WHERE parent_id = 0 OR parent_id IS NULL;
    
    -- 第二步：循环处理非根节点，最多循环10次以防无限循环
    SET @iteration = 0;
    WHILE @iteration < 10 DO
        SET @iteration = @iteration + 1;
        SET @updated_count = 0;
        
        -- 更新那些父节点已有treepath但自己还没有的节点
        UPDATE system_category c1 
        JOIN system_category c2 ON c1.parent_id = c2.id 
        SET c1.tree_path = CONCAT(c2.tree_path, c1.id, '/')
        WHERE c1.tree_path IS NULL 
        AND c2.tree_path IS NOT NULL
        AND c1.parent_id != 0;
        
        SET @updated_count = ROW_COUNT();
        
        -- 如果没有更新任何记录，说明完成了
        IF @updated_count = 0 THEN
            LEAVE;
        END IF;
    END WHILE;
    
    -- 处理孤儿节点（找不到父节点的节点），将它们设为根节点
    UPDATE system_category c1 
    LEFT JOIN system_category c2 ON c1.parent_id = c2.id 
    SET c1.tree_path = CONCAT('/', c1.id, '/'), c1.parent_id = 0
    WHERE c1.tree_path IS NULL 
    AND c1.parent_id != 0 
    AND c2.id IS NULL;
    
END//

DELIMITER ;

-- 执行存储过程
CALL UpdateTreePath();

-- 删除存储过程
DROP PROCEDURE UpdateTreePath;

-- 验证结果
SELECT 
    id,
    name,
    parent_id,
    tree_path,
    CASE 
        WHEN parent_id = 0 OR parent_id IS NULL THEN '根节点'
        ELSE '子节点'
    END as node_type
FROM system_category 
ORDER BY tree_path;

-- 检查是否还有treepath为空的记录
SELECT COUNT(*) as empty_treepath_count 
FROM system_category 
WHERE tree_path IS NULL OR tree_path = '';

-- 显示统计信息
SELECT 
    COUNT(*) as total_categories,
    COUNT(CASE WHEN parent_id = 0 OR parent_id IS NULL THEN 1 END) as root_nodes,
    COUNT(CASE WHEN parent_id != 0 AND parent_id IS NOT NULL THEN 1 END) as child_nodes,
    COUNT(CASE WHEN tree_path IS NULL OR tree_path = '' THEN 1 END) as missing_treepath
FROM system_category; 