-- 重新创建 system_field 表的索引
USE tunnel_management;

-- 1. 创建基于 field_code 的唯一索引（替代之前的 field_key 索引）
ALTER TABLE system_field 
ADD UNIQUE INDEX uk_field_code_tenant_deleted (field_code, tenant_id, deleted);

-- 2. 验证索引创建结果
SHOW INDEX FROM system_field;

-- 3. 验证数据完整性
SELECT '=== 索引重建完成 ===' AS info;
SELECT COUNT(*) as total_records FROM system_field; 