-- 业务实例权限配置表结构

-- 1. 业务实例表
CREATE TABLE business_instances (
    id VARCHAR(50) PRIMARY KEY COMMENT '实例ID',
    instance_key VARCHAR(100) NOT NULL UNIQUE COMMENT '实例唯一标识',
    instance_name VARCHAR(200) NOT NULL COMMENT '实例名称',
    template_id VARCHAR(50) NOT NULL COMMENT '权限模板ID',
    business_type VARCHAR(50) NOT NULL COMMENT '业务类型',
    business_scope VARCHAR(100) NOT NULL COMMENT '业务范围',
    description TEXT COMMENT '实例描述',
    
    -- 实例状态
    status ENUM('active', 'inactive', 'draft') DEFAULT 'active' COMMENT '实例状态',
    
    -- 实例元数据
    metadata JSON COMMENT '实例元数据JSON',
    
    created_by VARCHAR(50) NOT NULL COMMENT '创建人',
    updated_by VARCHAR(50) COMMENT '更新人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_instance_key (instance_key),
    INDEX idx_business_type (business_type),
    INDEX idx_business_scope (business_scope),
    INDEX idx_status (status),
    INDEX idx_template_id (template_id)
) COMMENT '业务实例表';

-- 2. 业务实例权限配置表
CREATE TABLE business_instance_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    instance_id VARCHAR(50) NOT NULL COMMENT '实例ID',
    template_permission_id VARCHAR(50) NOT NULL COMMENT '模板权限ID',
    
    -- 权限覆盖配置
    permission_overrides JSON COMMENT '权限覆盖规则JSON',
    
    -- 动态权限规则
    dynamic_rules JSON COMMENT '动态权限规则JSON',
    
    version VARCHAR(20) DEFAULT '1.0.0' COMMENT '配置版本',
    is_active TINYINT DEFAULT 1 COMMENT '是否启用',
    
    created_by VARCHAR(50) NOT NULL COMMENT '创建人',
    updated_by VARCHAR(50) COMMENT '更新人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_instance_id (instance_id),
    INDEX idx_template_permission_id (template_permission_id),
    INDEX idx_is_active (is_active),
    FOREIGN KEY (instance_id) REFERENCES business_instances(id) ON DELETE CASCADE
) COMMENT '业务实例权限配置表';

-- 3. 权限模板表
CREATE TABLE permission_templates (
    id VARCHAR(50) PRIMARY KEY COMMENT '模板ID',
    template_key VARCHAR(100) NOT NULL UNIQUE COMMENT '模板标识',
    template_name VARCHAR(200) NOT NULL COMMENT '模板名称',
    component_type VARCHAR(50) NOT NULL COMMENT '组件类型',
    description TEXT COMMENT '模板描述',
    
    -- 基础权限配置
    base_permission_config JSON NOT NULL COMMENT '基础权限配置JSON',
    
    -- 可覆盖的权限点
    overridable_permissions JSON COMMENT '可覆盖权限点JSON',
    
    -- 模板变量
    template_variables JSON COMMENT '模板变量JSON',
    
    is_system TINYINT DEFAULT 0 COMMENT '是否系统模板',
    version VARCHAR(20) DEFAULT '1.0.0' COMMENT '模板版本',
    
    created_by VARCHAR(50) NOT NULL COMMENT '创建人',
    updated_by VARCHAR(50) COMMENT '更新人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_template_key (template_key),
    INDEX idx_component_type (component_type),
    INDEX idx_is_system (is_system)
) COMMENT '权限模板表';

-- 4. 业务实例类型定义表
CREATE TABLE business_instance_types (
    type_key VARCHAR(50) PRIMARY KEY COMMENT '类型标识',
    type_name VARCHAR(100) NOT NULL COMMENT '类型名称',
    description TEXT COMMENT '类型描述',
    
    -- 支持的模板
    supported_templates JSON COMMENT '支持的模板ID数组',
    
    -- 默认权限覆盖规则
    default_overrides JSON COMMENT '默认权限覆盖规则JSON',
    
    -- 业务特定权限配置
    business_specific_permissions JSON COMMENT '业务特定权限配置JSON',
    
    is_active TINYINT DEFAULT 1 COMMENT '是否启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_type_name (type_name),
    INDEX idx_is_active (is_active)
) COMMENT '业务实例类型表';

-- 5. 权限继承日志表
CREATE TABLE permission_inheritance_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    instance_id VARCHAR(50) NOT NULL COMMENT '实例ID',
    user_id VARCHAR(50) NOT NULL COMMENT '用户ID',
    
    -- 权限计算上下文
    calculation_context JSON COMMENT '权限计算上下文JSON',
    
    -- 继承结果
    inheritance_result JSON COMMENT '权限继承结果JSON',
    applied_overrides JSON COMMENT '应用的覆盖规则JSON',
    inheritance_chain JSON COMMENT '继承链JSON',
    
    -- 计算性能
    calculation_time_ms INT COMMENT '计算耗时(毫秒)',
    
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_instance_id (instance_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_time (created_time)
) COMMENT '权限继承日志表';

-- 6. 实例权限缓存表
CREATE TABLE instance_permission_cache (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cache_key VARCHAR(200) NOT NULL UNIQUE COMMENT '缓存键',
    instance_id VARCHAR(50) NOT NULL COMMENT '实例ID',
    user_context_hash VARCHAR(64) NOT NULL COMMENT '用户上下文哈希',
    
    -- 缓存的权限配置
    cached_permission_config JSON NOT NULL COMMENT '缓存的权限配置JSON',
    
    -- 缓存元数据
    cache_version VARCHAR(20) COMMENT '缓存版本',
    expires_at DATETIME NOT NULL COMMENT '过期时间',
    
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_cache_key (cache_key),
    INDEX idx_instance_id (instance_id),
    INDEX idx_expires_at (expires_at),
    FOREIGN KEY (instance_id) REFERENCES business_instances(id) ON DELETE CASCADE
) COMMENT '实例权限缓存表';

-- 插入示例数据

-- 业务实例类型
INSERT INTO business_instance_types (type_key, type_name, description, supported_templates, default_overrides) VALUES
('dept_user_manage', '部门用户管理', '部门级别的用户管理业务实例', 
 JSON_ARRAY('tpl_user_manage_standard'), 
 JSON_ARRAY(
   JSON_OBJECT(
     'targetPath', 'nodes.edit.conditions',
     'overrideType', 'append',
     'overrideValue', JSON_OBJECT(
       'field', 'departmentId',
       'operator', 'eq',
       'value', 'currentUserDepartment',
       'dataSource', 'user'
     ),
     'reason', '只能编辑本部门用户'
   )
 )),

('region_manage', '区域管理', '区域级别的管理业务实例',
 JSON_ARRAY('tpl_region_manage_standard'),
 JSON_ARRAY(
   JSON_OBJECT(
     'targetPath', 'actions.buttons',
     'overrideType', 'append',
     'overrideValue', JSON_OBJECT(
       'buttonKey', 'regionExport',
       'visible', JSON_OBJECT('roles', JSON_ARRAY('region_manager')),
       'enabled', JSON_OBJECT('roles', JSON_ARRAY('region_manager')),
       'clickable', JSON_OBJECT('roles', JSON_ARRAY('region_manager'))
     ),
     'reason', '区域管理增加区域导出功能'
   )
 ));

-- 权限模板
INSERT INTO permission_templates (id, template_key, template_name, component_type, description, base_permission_config, overridable_permissions) VALUES
('tpl_user_manage_standard', 'user_management_standard', '标准用户管理权限模板', 'SuperTree', 
 '适用于标准用户管理场景的权限模板',
 JSON_OBJECT(
   'component', JSON_OBJECT(
     'visible', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager')),
     'editable', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager')),
     'selectable', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager'))
   ),
   'actions', JSON_OBJECT(
     'enabled', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager')),
     'buttons', JSON_ARRAY(
       JSON_OBJECT(
         'buttonKey', 'export',
         'visible', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager'), 'permissions', JSON_ARRAY('system:user:export')),
         'enabled', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager'), 'permissions', JSON_ARRAY('system:user:export')),
         'clickable', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager'), 'permissions', JSON_ARRAY('system:user:export'))
       )
     )
   )
 ),
 JSON_ARRAY(
   JSON_OBJECT('path', 'component.visible', 'name', '组件可见性', 'allowedOverrideTypes', JSON_ARRAY('replace', 'merge')),
   JSON_OBJECT('path', 'actions.buttons.export.visible', 'name', '导出按钮可见性', 'allowedOverrideTypes', JSON_ARRAY('replace', 'merge'))
 )),

('tpl_readonly_viewer', 'readonly_viewer', '只读查看权限模板', 'SuperTree',
 '适用于只读查看场景的权限模板', 
 JSON_OBJECT(
   'component', JSON_OBJECT(
     'visible', JSON_OBJECT('roles', JSON_ARRAY('viewer', 'guest')),
     'editable', JSON_OBJECT('roles', JSON_ARRAY()),
     'selectable', JSON_OBJECT('roles', JSON_ARRAY('viewer'))
   ),
   'actions', JSON_OBJECT(
     'enabled', JSON_OBJECT('roles', JSON_ARRAY('viewer')),
     'buttons', JSON_ARRAY(
       JSON_OBJECT(
         'buttonKey', 'refresh',
         'visible', JSON_OBJECT('roles', JSON_ARRAY('viewer', 'guest')),
         'enabled', JSON_OBJECT('roles', JSON_ARRAY('viewer', 'guest')),
         'clickable', JSON_OBJECT('roles', JSON_ARRAY('viewer', 'guest'))
       )
     )
   )
 ),
 JSON_ARRAY());

-- 示例业务实例
INSERT INTO business_instances (id, instance_key, instance_name, template_id, business_type, business_scope, description, metadata) VALUES
('inst_tech_dept_user', 'dept_user_manage_tech', '技术部用户管理', 'tpl_user_manage_standard', 'dept_user_manage', 'dept_tech', 
 '技术部门的用户管理实例', JSON_OBJECT('departmentId', 'dept_tech', 'departmentName', '技术部')),

('inst_sales_dept_user', 'dept_user_manage_sales', '销售部用户管理', 'tpl_user_manage_standard', 'dept_user_manage', 'dept_sales',
 '销售部门的用户管理实例', JSON_OBJECT('departmentId', 'dept_sales', 'departmentName', '销售部')),

('inst_north_region', 'region_manage_north', '北区管理', 'tpl_user_manage_standard', 'region_manage', 'region_north',
 '北区的管理实例', JSON_OBJECT('regionId', 'region_north', 'regionName', '北区'));

-- 示例业务实例权限配置
INSERT INTO business_instance_permissions (instance_id, template_permission_id, permission_overrides, dynamic_rules) VALUES
('inst_tech_dept_user', 'tpl_user_manage_standard', 
 JSON_ARRAY(
   JSON_OBJECT(
     'targetPath', 'actions.buttons',
     'overrideType', 'append',
     'overrideValue', JSON_OBJECT(
       'buttonKey', 'techTransfer',
       'visible', JSON_OBJECT('roles', JSON_ARRAY('tech_lead'), 'permissions', JSON_ARRAY('tech:user:transfer')),
       'enabled', JSON_OBJECT('roles', JSON_ARRAY('tech_lead'), 'permissions', JSON_ARRAY('tech:user:transfer')),
       'clickable', JSON_OBJECT('roles', JSON_ARRAY('tech_lead'), 'permissions', JSON_ARRAY('tech:user:transfer'))
     ),
     'reason', '技术部增加技术人员调动功能'
   )
 ),
 JSON_OBJECT(
   'dataBasedRules', JSON_OBJECT(
     'userLevel', JSON_OBJECT(
       'conditions', JSON_ARRAY(
         JSON_OBJECT(
           'operator', 'eq',
           'value', 'senior',
           'permissionRule', JSON_OBJECT('roles', JSON_ARRAY('senior_dev'))
         )
       )
     )
   )
 )),

('inst_north_region', 'tpl_user_manage_standard',
 JSON_ARRAY(
   JSON_OBJECT(
     'targetPath', 'actions.buttons.export.visible.roles',
     'overrideType', 'append',
     'overrideValue', JSON_ARRAY('region_manager'),
     'reason', '区域管理员可以导出'
   )
 ),
 JSON_OBJECT(
   'timeBasedRules', JSON_OBJECT(
     'workHours', JSON_OBJECT(
       'enabled', true,
       'startTime', '09:00',
       'endTime', '18:00',
       'permissionRule', JSON_OBJECT('roles', JSON_ARRAY('region_staff'))
     )
   )
 ));

-- 创建清理过期缓存的存储过程
DELIMITER //
CREATE PROCEDURE CleanExpiredPermissionCache()
BEGIN
    DELETE FROM instance_permission_cache WHERE expires_at < NOW();
END //
DELIMITER ;

-- 创建定时事件清理过期缓存（每小时执行一次）
-- CREATE EVENT IF NOT EXISTS clean_permission_cache
-- ON SCHEDULE EVERY 1 HOUR
-- DO CALL CleanExpiredPermissionCache();