-- 修复 system_field_def_category_rel 表重复数据并添加唯一约束
-- 解决 TooManyResultsException 问题

-- 1. 备份当前数据（可选）
-- CREATE TABLE system_field_def_category_rel_backup AS SELECT * FROM system_field_def_category_rel;

-- 2. 查找重复数据
SELECT 
    field_def_id, 
    category_id, 
    tenant_id, 
    COUNT(*) as count 
FROM system_field_def_category_rel 
WHERE deleted = 0 
GROUP BY field_def_id, category_id, tenant_id 
HAVING COUNT(*) > 1;

-- 3. 删除重复数据，保留最新的记录
DELETE t1 FROM system_field_def_category_rel t1
INNER JOIN system_field_def_category_rel t2 
WHERE 
    t1.field_def_id = t2.field_def_id 
    AND t1.category_id = t2.category_id 
    AND t1.tenant_id = t2.tenant_id
    AND t1.id < t2.id  -- 保留ID最大（最新）的记录
    AND t1.deleted = 0 
    AND t2.deleted = 0;

-- 4. 添加唯一约束，防止future重复插入
ALTER TABLE system_field_def_category_rel 
ADD UNIQUE INDEX uk_field_category_tenant (field_def_id, category_id, tenant_id, deleted);

-- 5. 验证修复结果
SELECT 
    field_def_id, 
    category_id, 
    tenant_id, 
    COUNT(*) as count 
FROM system_field_def_category_rel 
WHERE deleted = 0 
GROUP BY field_def_id, category_id, tenant_id 
HAVING COUNT(*) > 1; 