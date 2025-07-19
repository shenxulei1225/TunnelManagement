-- 优化表索引脚本
-- 为 system_directory、system_category、system_field 表添加常用查询索引

USE tunnel_management;

-- 1. system_directory 表索引优化
-- 添加状态索引（用于按状态查询）
ALTER TABLE system_directory 
ADD INDEX idx_status (status);

-- 添加创建时间索引（用于时间范围查询）
ALTER TABLE system_directory 
ADD INDEX idx_create_time (create_time);

-- 2. system_category 表索引优化
-- 添加状态索引（用于按状态查询）
ALTER TABLE system_category 
ADD INDEX idx_status (status);

-- 添加排序索引（用于排序查询）
ALTER TABLE system_category 
ADD INDEX idx_sort (sort);

-- 添加创建时间索引（用于时间范围查询）
ALTER TABLE system_category 
ADD INDEX idx_create_time (create_time);

-- 3. system_field 表索引优化
-- 添加字段类型索引（用于按类型查询）
ALTER TABLE system_field 
ADD INDEX idx_field_type (field_type);

-- 添加是否自定义索引（用于按自定义字段查询）
ALTER TABLE system_field 
ADD INDEX idx_is_custom (is_custom);

-- 验证索引创建结果
SELECT '=== system_directory 索引 ===' AS info;
SHOW INDEX FROM system_directory;

SELECT '=== system_category 索引 ===' AS info;
SHOW INDEX FROM system_category;

SELECT '=== system_field 索引 ===' AS info;
SHOW INDEX FROM system_field;

SELECT '=== 索引优化完成 ===' AS info; 