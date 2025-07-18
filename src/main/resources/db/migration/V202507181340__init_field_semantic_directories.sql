-- 初始化字段语义分类目录数据
-- 使用 cheers-directory 服务的 system_directory 表

-- 1. 测量类字段目录
INSERT INTO system_directory (business_type, parent_id, name, code, description, icon, sort, status, creator, create_time, updater, update_time, deleted, tenant_id) 
VALUES ('FIELD_SEMANTIC', 0, '测量类', 'measurement', '长度、面积、体积、重量等测量相关字段', 'icon-ruler', 1, 1, 'system', NOW(), 'system', NOW(), 0, 0);

-- 2. 电气类字段目录  
INSERT INTO system_directory (business_type, parent_id, name, code, description, icon, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('FIELD_SEMANTIC', 0, '电气类', 'electrical', '电压、电流、功率、电阻等电气相关字段', 'icon-zap', 2, 1, 'system', NOW(), 'system', NOW(), 0, 0);

-- 3. 地理类字段目录
INSERT INTO system_directory (business_type, parent_id, name, code, description, icon, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('FIELD_SEMANTIC', 0, '地理类', 'geographic', '坐标、海拔、方位、区域等地理相关字段', 'icon-map-pin', 3, 1, 'system', NOW(), 'system', NOW(), 0, 0);

-- 4. 合同类字段目录
INSERT INTO system_directory (business_type, parent_id, name, code, description, icon, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('FIELD_SEMANTIC', 0, '合同类', 'contract', '合同编号、甲乙方、金额、期限等合同相关字段', 'icon-file-text', 4, 1, 'system', NOW(), 'system', NOW(), 0, 0);

-- 5. 维护类字段目录
INSERT INTO system_directory (business_type, parent_id, name, code, description, icon, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('FIELD_SEMANTIC', 0, '维护类', 'maintenance', '维护周期、最后维护时间、维护状态等维护相关字段', 'icon-tool', 5, 1, 'system', NOW(), 'system', NOW(), 0, 0);

-- 6. 检查类字段目录
INSERT INTO system_directory (business_type, parent_id, name, code, description, icon, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('FIELD_SEMANTIC', 0, '检查类', 'inspection', '检查结果、检查时间、检查人员等检查相关字段', 'icon-search', 6, 1, 'system', NOW(), 'system', NOW(), 0, 0);

-- 7. 状态类字段目录
INSERT INTO system_directory (business_type, parent_id, name, code, description, icon, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('FIELD_SEMANTIC', 0, '状态类', 'status', '运行状态、健康状态、工作状态等状态相关字段', 'icon-activity', 7, 1, 'system', NOW(), 'system', NOW(), 0, 0);

-- 8. 时间类字段目录
INSERT INTO system_directory (business_type, parent_id, name, code, description, icon, sort, status, creator, create_time, updater, update_time, deleted, tenant_id)
VALUES ('FIELD_SEMANTIC', 0, '时间类', 'temporal', '创建时间、更新时间、开始时间、结束时间等时间相关字段', 'icon-clock', 8, 1, 'system', NOW(), 'system', NOW(), 0, 0); 