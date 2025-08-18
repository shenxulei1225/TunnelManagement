-- 权限配置存储表结构

-- 1. 扩展组件配置表，增加权限配置字段
ALTER TABLE component_configs 
ADD COLUMN permission_config JSON COMMENT '权限配置JSON' AFTER config_data;

-- 2. 权限规则模板表
CREATE TABLE permission_rule_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_key VARCHAR(100) NOT NULL UNIQUE COMMENT '模板键',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    description TEXT COMMENT '模板描述',
    component_type VARCHAR(50) NOT NULL COMMENT '适用组件类型',
    rule_config JSON NOT NULL COMMENT '权限规则配置JSON',
    
    -- 模板分类
    category VARCHAR(50) COMMENT '权限类别：业务权限、系统权限、数据权限等',
    scenario VARCHAR(100) COMMENT '适用场景：用户管理、数据展示、文件操作等',
    
    is_system TINYINT DEFAULT 0 COMMENT '是否为系统模板：1-是，0-否',
    is_active TINYINT DEFAULT 1 COMMENT '是否启用：1-是，0-否',
    
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_template_key (template_key),
    INDEX idx_component_type (component_type),
    INDEX idx_category (category),
    INDEX idx_scenario (scenario),
    INDEX idx_is_system (is_system)
) COMMENT '权限规则模板表';

-- 3. 角色权限映射表（支持动态角色）
CREATE TABLE role_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(50) NOT NULL COMMENT '角色代码',
    role_name VARCHAR(100) NOT NULL COMMENT '角色名称',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限代码',
    permission_name VARCHAR(100) COMMENT '权限名称',
    
    -- 权限范围
    scope_type VARCHAR(20) DEFAULT 'global' COMMENT '权限范围：global-全局，dept-部门，user-个人',
    scope_value VARCHAR(100) COMMENT '范围值：部门ID、用户ID等',
    
    -- 权限条件
    conditions JSON COMMENT '权限生效条件JSON',
    
    is_active TINYINT DEFAULT 1 COMMENT '是否启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_role_code (role_code),
    INDEX idx_permission_code (permission_code),
    INDEX idx_scope (scope_type, scope_value),
    UNIQUE KEY uk_role_permission_scope (role_code, permission_code, scope_type, scope_value)
) COMMENT '角色权限映射表';

-- 4. 用户角色关联表
CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_code VARCHAR(50) NOT NULL COMMENT '角色代码',
    
    -- 权限范围限制
    scope_type VARCHAR(20) DEFAULT 'global' COMMENT '权限范围',
    scope_value VARCHAR(100) COMMENT '范围值',
    
    -- 生效时间
    effective_time DATETIME COMMENT '生效时间',
    expire_time DATETIME COMMENT '失效时间',
    
    is_active TINYINT DEFAULT 1 COMMENT '是否启用',
    created_by BIGINT COMMENT '分配人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_role_code (role_code),
    INDEX idx_scope (scope_type, scope_value),
    INDEX idx_effective_time (effective_time),
    INDEX idx_expire_time (expire_time),
    UNIQUE KEY uk_user_role_scope (user_id, role_code, scope_type, scope_value)
) COMMENT '用户角色关联表';

-- 5. 权限检查日志表
CREATE TABLE permission_check_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限代码',
    resource_type VARCHAR(50) COMMENT '资源类型：component, button, menu等',
    resource_id VARCHAR(100) COMMENT '资源标识',
    
    check_result TINYINT NOT NULL COMMENT '检查结果：1-通过，0-拒绝',
    check_reason VARCHAR(200) COMMENT '检查原因',
    
    -- 上下文信息
    page_path VARCHAR(200) COMMENT '页面路径',
    component_id VARCHAR(50) COMMENT '组件ID',
    action_type VARCHAR(50) COMMENT '操作类型',
    
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_permission_code (permission_code),
    INDEX idx_resource (resource_type, resource_id),
    INDEX idx_check_result (check_result),
    INDEX idx_created_time (created_time)
) COMMENT '权限检查日志表';

-- 插入示例数据

-- 权限规则模板
INSERT INTO permission_rule_templates (template_key, template_name, description, component_type, rule_config, category, scenario) VALUES
('supertree_admin_full', 'SuperTree管理员完整权限', 'SuperTree组件管理员级别的完整权限模板', 'SuperTree', 
 JSON_OBJECT(
   'component', JSON_OBJECT(
     'visible', JSON_OBJECT('roles', JSON_ARRAY('admin', 'manager')),
     'editable', JSON_OBJECT('roles', JSON_ARRAY('admin', 'manager')),
     'selectable', JSON_OBJECT('roles', JSON_ARRAY('admin', 'manager'))
   ),
   'actions', JSON_OBJECT(
     'enabled', JSON_OBJECT('roles', JSON_ARRAY('admin', 'manager')),
     'buttons', JSON_ARRAY(
       JSON_OBJECT(
         'buttonKey', 'export',
         'visible', JSON_OBJECT('roles', JSON_ARRAY('admin', 'manager'), 'permissions', JSON_ARRAY('system:export')),
         'enabled', JSON_OBJECT('roles', JSON_ARRAY('admin', 'manager'), 'permissions', JSON_ARRAY('system:export')),
         'clickable', JSON_OBJECT('roles', JSON_ARRAY('admin', 'manager'), 'permissions', JSON_ARRAY('system:export'))
       )
     )
   )
 ), '业务权限', '用户管理'),

('supertree_readonly', 'SuperTree只读权限', 'SuperTree组件只读权限模板', 'SuperTree',
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
         'buttonKey', 'export',
         'visible', JSON_OBJECT('roles', JSON_ARRAY('viewer'), 'permissions', JSON_ARRAY('system:export')),
         'enabled', JSON_OBJECT('roles', JSON_ARRAY('viewer'), 'permissions', JSON_ARRAY('system:export')),
         'clickable', JSON_OBJECT('roles', JSON_ARRAY('viewer'), 'permissions', JSON_ARRAY('system:export'))
       )
     )
   )
 ), '业务权限', '数据查看');

-- 角色权限映射
INSERT INTO role_permissions (role_code, role_name, permission_code, permission_name) VALUES
('admin', '系统管理员', 'system:user:view', '查看用户'),
('admin', '系统管理员', 'system:user:add', '添加用户'),
('admin', '系统管理员', 'system:user:edit', '编辑用户'),
('admin', '系统管理员', 'system:user:delete', '删除用户'),
('admin', '系统管理员', 'system:user:export', '导出用户'),
('admin', '系统管理员', 'system:user:import', '导入用户'),
('admin', '系统管理员', 'system:user:batch_delete', '批量删除用户'),

('user_manager', '用户管理员', 'system:user:view', '查看用户'),
('user_manager', '用户管理员', 'system:user:add', '添加用户'),
('user_manager', '用户管理员', 'system:user:edit', '编辑用户'),
('user_manager', '用户管理员', 'system:user:export', '导出用户'),
('user_manager', '用户管理员', 'system:user:import', '导入用户'),

('viewer', '查看者', 'system:user:view', '查看用户'),
('viewer', '查看者', 'system:export', '导出数据');

-- 用户角色关联示例
INSERT INTO user_roles (user_id, role_code) VALUES
(1, 'admin'),
(2, 'user_manager'),
(3, 'viewer'),
(4, 'guest');

-- 更新组件配置表，添加权限配置示例
UPDATE component_configs 
SET permission_config = JSON_OBJECT(
  'component', JSON_OBJECT(
    'visible', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager', 'viewer')),
    'editable', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager')),
    'selectable', JSON_OBJECT('roles', JSON_ARRAY('admin', 'user_manager', 'viewer'))
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
)
WHERE config_key = 'system_user_userTree';