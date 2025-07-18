-- 检查语义分类数据是否存在
SELECT COUNT(*) as total_count FROM system_directory WHERE business_type = 'FIELD_SEMANTIC';

-- 查看具体的语义分类数据
SELECT id, name, code, description, sort, status FROM system_directory 
WHERE business_type = 'FIELD_SEMANTIC' 
ORDER BY sort;

-- 检查是否有相关的字段数据
SELECT COUNT(*) as field_count FROM system_field_def WHERE semantic_directory_id IS NOT NULL;

-- 查看字段与语义分类的关联情况
SELECT 
    d.name as directory_name,
    d.code as directory_code,
    COUNT(f.id) as field_count
FROM system_directory d 
LEFT JOIN system_field_def f ON d.id = f.semantic_directory_id 
WHERE d.business_type = 'FIELD_SEMANTIC'
GROUP BY d.id, d.name, d.code
ORDER BY d.sort; 