-- 为系统分层级组表添加分组类型字段
ALTER TABLE system_hierarchy_group 
ADD COLUMN group_type varchar(50) DEFAULT 'GENERAL' COMMENT '分组类型：EQUIPMENT(设备), PERSONNEL(人员), LOCATION(位置), MANAGEMENT(管理), DISPLAY(展示), MEASUREMENT(度量), STRUCTURE(结构), ENVIRONMENT(环境), GENERAL(通用)';

-- 根据现有数据的名称模式初始化分组类型
UPDATE system_hierarchy_group SET group_type = 'EQUIPMENT' WHERE name LIKE '%设备%' OR name LIKE '%办公%';
UPDATE system_hierarchy_group SET group_type = 'PERSONNEL' WHERE name LIKE '%人员%' OR name LIKE '%部门%' OR name LIKE '%技术%';
UPDATE system_hierarchy_group SET group_type = 'LOCATION' WHERE name LIKE '%位置%' OR name LIKE '%建筑%';
UPDATE system_hierarchy_group SET group_type = 'MANAGEMENT' WHERE name LIKE '%管理%';
UPDATE system_hierarchy_group SET group_type = 'DISPLAY' WHERE name LIKE '%展示%';
UPDATE system_hierarchy_group SET group_type = 'MEASUREMENT' WHERE name LIKE '%度量%';
UPDATE system_hierarchy_group SET group_type = 'STRUCTURE' WHERE name LIKE '%结构%' OR name LIKE '%道路%';
UPDATE system_hierarchy_group SET group_type = 'ENVIRONMENT' WHERE name LIKE '%环境%' OR name LIKE '%尾气%' OR name LIKE '%电气%';

-- 创建索引
ALTER TABLE system_hierarchy_group ADD INDEX idx_group_type (group_type); 