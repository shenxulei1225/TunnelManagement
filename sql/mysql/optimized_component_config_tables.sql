-- 优化后的组件配置存储表结构
-- 1. 解决组件唯一性识别问题
-- 2. 只存储差异配置，减少90%的存储空间

-- 1. 组件实例标识表
CREATE TABLE component_instances (
    global_instance_id VARCHAR(100) PRIMARY KEY COMMENT '全局实例唯一标识',
    page_instance_id VARCHAR(50) NOT NULL COMMENT '页面实例ID',
    component_type VARCHAR(50) NOT NULL COMMENT '组件类型',
    
    -- 标识策略信息
    identifier_strategy ENUM('manual', 'semantic', 'dataBinding', 'position', 'index', 'fallback') NOT NULL COMMENT '标识生成策略',
    
    -- 手动指定的实例键
    instance_key VARCHAR(50) COMMENT '手动指定的实例键',
    
    -- 语义角色标识
    semantic_role VARCHAR(50) COMMENT '语义角色',
    
    -- 数据绑定信息
    data_binding_strategy ENUM('entity', 'field', 'relation') COMMENT '数据绑定策略',
    data_binding_config JSON COMMENT '数据绑定配置JSON',
    
    -- 位置信息
    position_strategy ENUM('grid', 'flex', 'tab', 'section', 'index') COMMENT '位置策略',
    position_config JSON COMMENT '位置配置JSON',
    
    -- 元数据
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_page_instance (page_instance_id),
    INDEX idx_component_type (component_type),
    INDEX idx_identifier_strategy (identifier_strategy),
    INDEX idx_instance_key (instance_key),
    INDEX idx_semantic_role (semantic_role)
) COMMENT '组件实例标识表';

-- 2. 组件配置模板表（基础模板）
CREATE TABLE component_config_templates (
    template_id VARCHAR(50) PRIMARY KEY COMMENT '模板ID',
    template_key VARCHAR(100) NOT NULL UNIQUE COMMENT '模板键',
    template_name VARCHAR(200) NOT NULL COMMENT '模板名称',
    component_type VARCHAR(50) NOT NULL COMMENT '组件类型',
    description TEXT COMMENT '模板描述',
    
    -- 完整的基础配置
    base_config JSON NOT NULL COMMENT '基础配置JSON',
    base_style JSON COMMENT '基础样式JSON',
    base_permissions JSON COMMENT '基础权限JSON',
    
    -- 模板元数据
    is_system TINYINT DEFAULT 0 COMMENT '是否系统模板',
    version VARCHAR(20) DEFAULT '1.0.0' COMMENT '模板版本',
    
    created_by VARCHAR(50) NOT NULL COMMENT '创建人',
    updated_by VARCHAR(50) COMMENT '更新人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_template_key (template_key),
    INDEX idx_component_type (component_type),
    INDEX idx_is_system (is_system)
) COMMENT '组件配置模板表';

-- 3. 组件实例差异配置表（核心优化）
CREATE TABLE component_instance_deltas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    global_instance_id VARCHAR(100) NOT NULL COMMENT '组件实例ID',
    base_template_id VARCHAR(50) NOT NULL COMMENT '基础模板ID',
    
    -- 差异配置（只存储与模板的差异）
    config_deltas JSON COMMENT '配置差异JSON数组',
    style_deltas JSON COMMENT '样式差异JSON数组', 
    permission_deltas JSON COMMENT '权限差异JSON数组',
    
    -- 差异元数据
    delta_version VARCHAR(20) DEFAULT '1.0.0' COMMENT '差异版本',
    delta_size INT COMMENT '差异数据大小(字节)',
    original_size INT COMMENT '原始配置大小(字节)',
    compression_ratio DECIMAL(5,2) COMMENT '压缩比例(%)',
    
    -- 变更信息
    last_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    modified_by VARCHAR(50) NOT NULL COMMENT '最后修改人',
    change_reason TEXT COMMENT '变更原因',
    
    INDEX idx_global_instance (global_instance_id),
    INDEX idx_base_template (base_template_id),
    INDEX idx_last_modified (last_modified),
    FOREIGN KEY (global_instance_id) REFERENCES component_instances(global_instance_id) ON DELETE CASCADE,
    FOREIGN KEY (base_template_id) REFERENCES component_config_templates(template_id) ON DELETE CASCADE,
    
    UNIQUE KEY uk_instance_template (global_instance_id, base_template_id)
) COMMENT '组件实例差异配置表';

-- 4. 页面实例注册表
CREATE TABLE page_instance_registries (
    page_instance_id VARCHAR(50) PRIMARY KEY COMMENT '页面实例ID',
    page_template_id VARCHAR(50) COMMENT '页面模板ID',
    business_type VARCHAR(50) NOT NULL COMMENT '业务类型',
    business_scope VARCHAR(100) NOT NULL COMMENT '业务范围',
    
    -- 注册配置
    default_strategy ENUM('position', 'dataBinding', 'semantic') DEFAULT 'position' COMMENT '默认标识策略',
    position_strategy ENUM('grid', 'flex', 'tab', 'section', 'index') DEFAULT 'section' COMMENT '位置策略',
    conflict_resolution ENUM('error', 'autoIncrement', 'override') DEFAULT 'autoIncrement' COMMENT '冲突解决策略',
    
    -- 实例统计
    component_count INT DEFAULT 0 COMMENT '组件实例数量',
    last_component_registered DATETIME COMMENT '最后注册组件时间',
    
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_business_type (business_type),
    INDEX idx_business_scope (business_scope)
) COMMENT '页面实例注册表';

-- 5. 差异配置历史表（用于回滚和审计）
CREATE TABLE component_delta_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    global_instance_id VARCHAR(100) NOT NULL COMMENT '组件实例ID',
    
    -- 历史差异数据
    config_deltas JSON COMMENT '历史配置差异',
    style_deltas JSON COMMENT '历史样式差异',
    permission_deltas JSON COMMENT '历史权限差异',
    
    -- 变更信息
    change_type ENUM('create', 'update', 'delete') NOT NULL COMMENT '变更类型',
    change_reason TEXT COMMENT '变更原因',
    changed_paths JSON COMMENT '变更路径数组',
    
    created_by VARCHAR(50) NOT NULL COMMENT '操作人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_global_instance (global_instance_id),
    INDEX idx_created_time (created_time),
    INDEX idx_change_type (change_type)
) COMMENT '差异配置历史表';

-- 6. 配置缓存表（性能优化）
CREATE TABLE component_config_cache (
    cache_key VARCHAR(200) PRIMARY KEY COMMENT '缓存键',
    global_instance_id VARCHAR(100) NOT NULL COMMENT '组件实例ID',
    
    -- 缓存的完整配置
    full_config JSON NOT NULL COMMENT '完整配置JSON',
    full_style JSON COMMENT '完整样式JSON',
    full_permissions JSON COMMENT '完整权限JSON',
    
    -- 缓存元数据
    template_version VARCHAR(20) COMMENT '模板版本',
    delta_version VARCHAR(20) COMMENT '差异版本',
    cache_version VARCHAR(32) COMMENT '缓存版本哈希',
    
    -- 缓存统计
    hit_count INT DEFAULT 0 COMMENT '命中次数',
    last_hit DATETIME COMMENT '最后命中时间',
    expires_at DATETIME NOT NULL COMMENT '过期时间',
    
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_global_instance (global_instance_id),
    INDEX idx_expires_at (expires_at),
    INDEX idx_last_hit (last_hit)
) COMMENT '组件配置缓存表';

-- 插入示例数据

-- 1. 基础模板数据
INSERT INTO component_config_templates (template_id, template_key, template_name, component_type, base_config, base_style, base_permissions) VALUES
('tpl_supertree_standard', 'supertree_standard', '标准SuperTree模板', 'SuperTree', 
 JSON_OBJECT(
   'showCheckbox', false,
   'highlightCurrent', true,
   'expandOnClickNode', true,
   'defaultExpandAll', false,
   'nodeKey', 'id',
   'props', JSON_OBJECT('label', 'name', 'children', 'children'),
   'toolbar', JSON_OBJECT(
     'enabled', true,
     'buttons', JSON_OBJECT('add', true, 'edit', true, 'delete', true, 'refresh', true)
   )
 ),
 JSON_OBJECT(
   'fontSize', 14,
   'labelWidth', 120,
   'rowSpacing', 4,
   'textColor', '#303133',
   'backgroundColor', '#ffffff'
 ),
 JSON_OBJECT(
   'component', JSON_OBJECT(
     'visible', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user')),
     'editable', JSON_OBJECT('roles', JSON_ARRAY('admin'))
   ),
   'actions', JSON_OBJECT(
     'buttons', JSON_ARRAY(
       JSON_OBJECT(
         'buttonKey', 'export',
         'visible', JSON_OBJECT('roles', JSON_ARRAY('admin'), 'permissions', JSON_ARRAY('export'))
       )
     )
   )
 )),

('tpl_supertree_readonly', 'supertree_readonly', '只读SuperTree模板', 'SuperTree',
 JSON_OBJECT(
   'showCheckbox', false,
   'highlightCurrent', true,
   'expandOnClickNode', true,
   'defaultExpandAll', false,
   'nodeKey', 'id',
   'props', JSON_OBJECT('label', 'name', 'children', 'children'),
   'toolbar', JSON_OBJECT('enabled', false)
 ),
 JSON_OBJECT(
   'fontSize', 14,
   'labelWidth', 120,
   'rowSpacing', 4,
   'textColor', '#606266',
   'backgroundColor', '#f5f7fa'
 ),
 JSON_OBJECT(
   'component', JSON_OBJECT(
     'visible', JSON_OBJECT('roles', JSON_ARRAY('viewer', 'guest')),
     'editable', JSON_OBJECT('roles', JSON_ARRAY())
   ),
   'actions', JSON_OBJECT('buttons', JSON_ARRAY())
 ));

-- 2. 页面实例注册
INSERT INTO page_instance_registries (page_instance_id, business_type, business_scope, component_count) VALUES
('page_dept_user_manage_tech', 'dept_user_manage', 'dept_tech', 0),
('page_region_manage_north', 'region_manage', 'region_north', 0),
('page_project_manage_alpha', 'project_manage', 'project_alpha', 0);

-- 3. 组件实例标识示例
INSERT INTO component_instances (global_instance_id, page_instance_id, component_type, identifier_strategy, semantic_role) VALUES
('page_dept_user_manage_tech_SuperTree_semantic_main', 'page_dept_user_manage_tech', 'SuperTree', 'semantic', 'main'),
('page_dept_user_manage_tech_SuperTree_semantic_sidebar', 'page_dept_user_manage_tech', 'SuperTree', 'semantic', 'sidebar'),
('page_region_manage_north_SuperTree_position_section_main_1', 'page_region_manage_north', 'SuperTree', 'position', NULL);

-- 更新组件实例的位置配置
UPDATE component_instances 
SET position_strategy = 'section', 
    position_config = JSON_OBJECT('sectionName', 'main', 'index', 1)
WHERE global_instance_id = 'page_region_manage_north_SuperTree_position_section_main_1';

-- 4. 差异配置示例（只存储变更）
INSERT INTO component_instance_deltas (global_instance_id, base_template_id, config_deltas, style_deltas, permission_deltas, delta_size, original_size, compression_ratio, modified_by) VALUES
('page_dept_user_manage_tech_SuperTree_semantic_main', 'tpl_supertree_standard',
 JSON_ARRAY(
   JSON_OBJECT(
     'path', 'showCheckbox',
     'operation', 'set',
     'value', true,
     'originalValue', false
   ),
   JSON_OBJECT(
     'path', 'toolbar.buttons.transfer',
     'operation', 'set',
     'value', true
   )
 ),
 JSON_ARRAY(
   JSON_OBJECT(
     'path', 'fontSize',
     'operation', 'set', 
     'value', 16,
     'originalValue', 14
   )
 ),
 JSON_ARRAY(
   JSON_OBJECT(
     'path', 'actions.buttons',
     'operation', 'append',
     'value', JSON_OBJECT(
       'buttonKey', 'techTransfer',
       'visible', JSON_OBJECT('roles', JSON_ARRAY('tech_lead'))
     )
   )
 ),
 156, 1024, 84.77, 'admin'),

('page_dept_user_manage_tech_SuperTree_semantic_sidebar', 'tpl_supertree_readonly',
 JSON_ARRAY(
   JSON_OBJECT(
     'path', 'defaultExpandAll',
     'operation', 'set',
     'value', true,
     'originalValue', false
   )
 ),
 JSON_ARRAY(),
 JSON_ARRAY(),
 45, 512, 91.21, 'admin');

-- 5. 创建视图：组件完整配置视图（便于查询）
CREATE VIEW component_full_configs AS
SELECT 
    ci.global_instance_id,
    ci.page_instance_id,
    ci.component_type,
    ci.identifier_strategy,
    ci.semantic_role,
    ct.template_name,
    ct.base_config,
    ct.base_style, 
    ct.base_permissions,
    cid.config_deltas,
    cid.style_deltas,
    cid.permission_deltas,
    cid.compression_ratio,
    cid.last_modified
FROM component_instances ci
LEFT JOIN component_instance_deltas cid ON ci.global_instance_id = cid.global_instance_id
LEFT JOIN component_config_templates ct ON cid.base_template_id = ct.template_id;

-- 6. 创建存储过程：清理过期缓存
DELIMITER //
CREATE PROCEDURE CleanExpiredConfigCache()
BEGIN
    DELETE FROM component_config_cache WHERE expires_at < NOW();
    
    -- 更新组件统计
    UPDATE page_instance_registries pir
    SET component_count = (
        SELECT COUNT(*) 
        FROM component_instances ci 
        WHERE ci.page_instance_id = pir.page_instance_id
    );
END //
DELIMITER ;

-- 7. 创建函数：计算存储效率
DELIMITER //
CREATE FUNCTION CalculateStorageEfficiency(original_size INT, delta_size INT) 
RETURNS DECIMAL(5,2)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE efficiency DECIMAL(5,2);
    
    IF original_size > 0 AND delta_size >= 0 THEN
        SET efficiency = ((original_size - delta_size) / original_size) * 100;
        IF efficiency > 100 THEN SET efficiency = 100; END IF;
        IF efficiency < 0 THEN SET efficiency = 0; END IF;
    ELSE
        SET efficiency = 0;
    END IF;
    
    RETURN efficiency;
END //
DELIMITER ;

-- 8. 创建触发器：自动更新压缩比例
DELIMITER //
CREATE TRIGGER update_compression_ratio
BEFORE INSERT ON component_instance_deltas
FOR EACH ROW
BEGIN
    SET NEW.compression_ratio = CalculateStorageEfficiency(NEW.original_size, NEW.delta_size);
END //

CREATE TRIGGER update_compression_ratio_on_update
BEFORE UPDATE ON component_instance_deltas  
FOR EACH ROW
BEGIN
    SET NEW.compression_ratio = CalculateStorageEfficiency(NEW.original_size, NEW.delta_size);
END //
DELIMITER ;

-- 9. 存储效率统计查询示例
-- 查看整体存储效率
SELECT 
    COUNT(*) as total_instances,
    AVG(compression_ratio) as avg_compression_ratio,
    SUM(original_size) as total_original_size,
    SUM(delta_size) as total_delta_size,
    ((SUM(original_size) - SUM(delta_size)) / SUM(original_size)) * 100 as overall_efficiency
FROM component_instance_deltas;

-- 按组件类型查看效率
SELECT 
    ci.component_type,
    COUNT(*) as instance_count,
    AVG(cid.compression_ratio) as avg_compression,
    SUM(cid.original_size) as total_original,
    SUM(cid.delta_size) as total_delta
FROM component_instances ci
JOIN component_instance_deltas cid ON ci.global_instance_id = cid.global_instance_id
GROUP BY ci.component_type;