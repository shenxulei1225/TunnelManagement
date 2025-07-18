-- 动态场景模板表
CREATE TABLE dynamic_scene_template (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    code VARCHAR(64) NOT NULL COMMENT '模板编码',
    name VARCHAR(128) NOT NULL COMMENT '模板名称',
    business_type VARCHAR(64) NOT NULL COMMENT '业务类型',
    description TEXT COMMENT '场景描述',
    recommended_fields JSON COMMENT '推荐字段配置',
    default_layout JSON COMMENT '默认布局配置',
    validation_rules JSON COMMENT '验证规则配置',
    icon VARCHAR(64) COMMENT '图标',
    sort INT DEFAULT 100 COMMENT '排序',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0:禁用，1:启用）',
    creator VARCHAR(64) COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    tenant_id BIGINT NOT NULL COMMENT '租户编号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code),
    KEY idx_business_type (business_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态场景模板表';

-- 动态视图配置表
CREATE TABLE dynamic_view_config (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    model_code VARCHAR(64) NOT NULL COMMENT '业务模型编码',
    view_type VARCHAR(32) NOT NULL COMMENT '视图类型（list:列表，detail:详情，form:表单）',
    view_code VARCHAR(64) NOT NULL COMMENT '视图编码',
    view_name VARCHAR(128) NOT NULL COMMENT '视图名称',
    layout_config JSON COMMENT '布局配置',
    field_config JSON COMMENT '字段配置',
    action_config JSON COMMENT '操作配置',
    permission_config JSON COMMENT '权限配置',
    sort INT DEFAULT 100 COMMENT '排序',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0:禁用，1:启用）',
    creator VARCHAR(64) COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    tenant_id BIGINT NOT NULL COMMENT '租户编号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_model_view (model_code, view_code),
    KEY idx_model_code (model_code),
    KEY idx_view_type (view_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态视图配置表';

-- 动态用户行为表
CREATE TABLE dynamic_user_behavior (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '行为ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    business_type VARCHAR(64) NOT NULL COMMENT '业务类型',
    scene_type VARCHAR(64) NOT NULL COMMENT '场景类型',
    model_code VARCHAR(64) COMMENT '模型编码',
    field_usage JSON COMMENT '字段使用情况',
    operation_type VARCHAR(32) NOT NULL COMMENT '操作类型（create:创建，update:更新，view:查看）',
    operation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    operation_detail JSON COMMENT '操作详情',
    creator VARCHAR(64) COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    tenant_id BIGINT NOT NULL COMMENT '租户编号',
    PRIMARY KEY (id),
    KEY idx_user_business (user_id, business_type),
    KEY idx_business_type (business_type),
    KEY idx_operation_time (operation_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动态用户行为表';

-- 插入初始场景模板数据
INSERT INTO dynamic_scene_template (code, name, business_type, description, recommended_fields, default_layout, validation_rules, icon, sort, status, tenant_id) VALUES
('inspection_daily', '日常巡检', 'inspection', '适用于日常巡检业务场景', 
'[{"fieldCode":"inspection_date","fieldName":"巡检日期","fieldType":"date","required":true,"defaultValue":"","recommendationReason":"巡检业务必备字段","weight":0.9},{"fieldCode":"inspection_person","fieldName":"巡检人员","fieldType":"string","required":true,"defaultValue":"","recommendationReason":"记录巡检责任人","weight":0.8},{"fieldCode":"inspection_result","fieldName":"巡检结果","fieldType":"enum","required":true,"defaultValue":"","recommendationReason":"记录巡检状态","weight":0.7},{"fieldCode":"inspection_notes","fieldName":"巡检备注","fieldType":"textarea","required":false,"defaultValue":"","recommendationReason":"记录巡检发现的问题","weight":0.6}]',
'{"type":"form","columns":2,"fields":[{"code":"inspection_date","span":12},{"code":"inspection_person","span":12},{"code":"inspection_result","span":12},{"code":"inspection_notes","span":24}]}',
'{"rules":{"inspection_date":{"required":true,"message":"巡检日期不能为空"},"inspection_person":{"required":true,"message":"巡检人员不能为空"},"inspection_result":{"required":true,"message":"巡检结果不能为空"}}}',
'el-icon-search', 100, 1, 1),

('inspection_key', '重点巡检', 'inspection', '适用于重点设备巡检业务场景',
'[{"fieldCode":"inspection_date","fieldName":"巡检日期","fieldType":"date","required":true,"defaultValue":"","recommendationReason":"巡检业务必备字段","weight":0.9},{"fieldCode":"inspection_person","fieldName":"巡检人员","fieldType":"string","required":true,"defaultValue":"","recommendationReason":"记录巡检责任人","weight":0.9},{"fieldCode":"inspection_result","fieldName":"巡检结果","fieldType":"enum","required":true,"defaultValue":"","recommendationReason":"记录巡检状态","weight":0.8},{"fieldCode":"inspection_notes","fieldName":"巡检备注","fieldType":"textarea","required":false,"defaultValue":"","recommendationReason":"记录巡检发现的问题","weight":0.8},{"fieldCode":"inspection_priority","fieldName":"巡检优先级","fieldType":"enum","required":true,"defaultValue":"","recommendationReason":"重点巡检需要设置优先级","weight":0.7}]',
'{"type":"form","columns":2,"fields":[{"code":"inspection_date","span":12},{"code":"inspection_person","span":12},{"code":"inspection_result","span":12},{"code":"inspection_priority","span":12},{"code":"inspection_notes","span":24}]}',
'{"rules":{"inspection_date":{"required":true,"message":"巡检日期不能为空"},"inspection_person":{"required":true,"message":"巡检人员不能为空"},"inspection_result":{"required":true,"message":"巡检结果不能为空"},"inspection_priority":{"required":true,"message":"巡检优先级不能为空"}}}',
'el-icon-star', 200, 1, 1),

('device_maintenance', '设备维护', 'device', '适用于设备维护业务场景',
'[{"fieldCode":"device_name","fieldName":"设备名称","fieldType":"string","required":true,"defaultValue":"","recommendationReason":"设备维护必备字段","weight":0.9},{"fieldCode":"maintenance_type","fieldName":"维护类型","fieldType":"enum","required":true,"defaultValue":"","recommendationReason":"记录维护类型","weight":0.8},{"fieldCode":"maintenance_date","fieldName":"维护日期","fieldType":"date","required":true,"defaultValue":"","recommendationReason":"记录维护时间","weight":0.8},{"fieldCode":"maintenance_person","fieldName":"维护人员","fieldType":"string","required":true,"defaultValue":"","recommendationReason":"记录维护责任人","weight":0.7},{"fieldCode":"maintenance_result","fieldName":"维护结果","fieldType":"enum","required":true,"defaultValue":"","recommendationReason":"记录维护状态","weight":0.7},{"fieldCode":"maintenance_cost","fieldName":"维护费用","fieldType":"number","required":false,"defaultValue":"","recommendationReason":"记录维护成本","weight":0.6}]',
'{"type":"form","columns":2,"fields":[{"code":"device_name","span":12},{"code":"maintenance_type","span":12},{"code":"maintenance_date","span":12},{"code":"maintenance_person","span":12},{"code":"maintenance_result","span":12},{"code":"maintenance_cost","span":12}]}',
'{"rules":{"device_name":{"required":true,"message":"设备名称不能为空"},"maintenance_type":{"required":true,"message":"维护类型不能为空"},"maintenance_date":{"required":true,"message":"维护日期不能为空"},"maintenance_person":{"required":true,"message":"维护人员不能为空"},"maintenance_result":{"required":true,"message":"维护结果不能为空"}}}',
'el-icon-tools', 300, 1, 1); 