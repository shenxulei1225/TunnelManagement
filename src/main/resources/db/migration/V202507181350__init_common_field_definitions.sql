-- 初始化常用字段定义数据
-- 参考智能推荐模版，创建隧道管理系统中的常用字段
-- 使用 INSERT IGNORE 避免重复插入冲突

-- =================================
-- 1. 测量类字段 (measurement)
-- =================================

-- 长度
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'length', '长度', 'NUMBER', '米', 1, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'measurement';

-- 宽度
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'width', '宽度', 'NUMBER', '米', 2, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'measurement';

-- 高度
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'height', '高度', 'NUMBER', '米', 3, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'measurement';

-- 直径
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'diameter', '直径', 'NUMBER', '毫米', 4, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'measurement';

-- 深度
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'depth', '深度', 'NUMBER', '米', 5, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'measurement';

-- 面积
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'area', '面积', 'NUMBER', '平方米', 6, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'measurement';

-- =================================
-- 2. 电气类字段 (electrical)
-- =================================

-- 电压
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'voltage', '电压', 'NUMBER', '伏特', 1, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'electrical';

-- 电流
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'current', '电流', 'NUMBER', '安培', 2, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'electrical';

-- 功率
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'power', '功率', 'NUMBER', '瓦特', 3, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'electrical';

-- =================================
-- 3. 地理类字段 (geographic)
-- =================================

-- 经度
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'longitude', '经度', 'NUMBER', '度', 1, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'geographic';

-- 纬度
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'latitude', '纬度', 'NUMBER', '度', 2, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'geographic';

-- 海拔
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'altitude', '海拔', 'NUMBER', '米', 3, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'geographic';

-- =================================
-- 4. 状态类字段 (status)
-- =================================

-- 运行状态
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, enum_json, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'operational_status', '运行状态', 'ENUM', '[{"value":"running","label":"运行中"},{"value":"stopped","label":"已停止"},{"value":"maintenance","label":"维护中"},{"value":"error","label":"故障"}]', 1, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'status';

-- 健康状态
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, enum_json, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'health_status', '健康状态', 'ENUM', '[{"value":"good","label":"良好"},{"value":"warning","label":"警告"},{"value":"critical","label":"严重"},{"value":"unknown","label":"未知"}]', 2, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'status';

-- =================================
-- 5. 时间类字段 (temporal)
-- =================================

-- 创建时间
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'created_at', '创建时间', 'DATE', 1, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'temporal';

-- 更新时间
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'updated_at', '更新时间', 'DATE', 2, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'temporal';

-- =================================
-- 6. 维护类字段 (maintenance)
-- =================================

-- 维护周期
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'maintenance_cycle', '维护周期', 'NUMBER', '天', 1, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'maintenance';

-- 最后维护时间
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'last_maintenance_time', '最后维护时间', 'DATE', 2, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'maintenance';

-- =================================
-- 7. 检查类字段 (inspection)
-- =================================

-- 检查结果
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, enum_json, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'inspection_result', '检查结果', 'ENUM', '[{"value":"pass","label":"合格"},{"value":"fail","label":"不合格"},{"value":"pending","label":"待检查"}]', 1, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'inspection';

-- 检查时间
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'inspection_time', '检查时间', 'DATE', 2, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'inspection';

-- 检查人员
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'inspector', '检查人员', 'STRING', 3, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'inspection';

-- =================================
-- 8. 合同类字段 (contract)
-- =================================

-- 合同编号
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'contract_number', '合同编号', 'STRING', 1, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'contract';

-- 合同方
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'contract_party', '合同方', 'STRING', 2, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'contract';

-- 合同金额
INSERT IGNORE INTO system_field_def (field_key, field_label, value_type, unit, sort, semantic_directory_id, creator, create_time, updater, update_time, deleted, tenant_id)
SELECT 'contract_amount', '合同金额', 'NUMBER', '元', 3, d.id, 'system', NOW(), 'system', NOW(), 0, 0
FROM system_directory d WHERE d.business_type = 'FIELD_SEMANTIC' AND d.code = 'contract'; 