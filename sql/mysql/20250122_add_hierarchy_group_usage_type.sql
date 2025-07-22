-- 为系统分层级组表添加用途类型字段
ALTER TABLE system_hierarchy_group 
ADD COLUMN usage_type varchar(50) DEFAULT 'FIELD' COMMENT '用途类型：FIELD(字段分组), BUSINESS(业务分组)';

-- 更新现有数据：将当前所有分组标记为字段分组
UPDATE system_hierarchy_group SET usage_type = 'FIELD';

-- 创建索引
ALTER TABLE system_hierarchy_group ADD INDEX idx_usage_type (usage_type);

-- 为了演示，我们可以创建一些业务分组示例
-- 插入设备业务分组
INSERT INTO system_hierarchy_group (tenant_id, name, code, parent_id, level, path, sort, group_type, usage_type, status, creator, create_time) VALUES
(1, '生产设备组', 'PRODUCTION_DEVICE_GROUP', 0, 1, '/', 10, 'EQUIPMENT', 'BUSINESS', 0, 'system', NOW()),
(1, '办公设备组', 'OFFICE_DEVICE_GROUP', 0, 1, '/', 20, 'EQUIPMENT', 'BUSINESS', 0, 'system', NOW());

-- 插入人员业务分组  
INSERT INTO system_hierarchy_group (tenant_id, name, code, parent_id, level, path, sort, group_type, usage_type, status, creator, create_time) VALUES
(1, '技术团队', 'TECH_TEAM_GROUP', 0, 1, '/', 30, 'PERSONNEL', 'BUSINESS', 0, 'system', NOW()),
(1, '管理团队', 'MANAGEMENT_TEAM_GROUP', 0, 1, '/', 40, 'PERSONNEL', 'BUSINESS', 0, 'system', NOW());

-- 验证结果
SELECT usage_type, group_type, COUNT(*) as count 
FROM system_hierarchy_group 
GROUP BY usage_type, group_type
ORDER BY usage_type, group_type; 