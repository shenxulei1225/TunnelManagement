-- 混合配置架构数据库表结构

-- 1. 页面配置表（轻量级）
CREATE TABLE page_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    page_path VARCHAR(200) NOT NULL UNIQUE COMMENT '页面路径',
    page_name VARCHAR(100) NOT NULL COMMENT '页面名称',
    description TEXT COMMENT '页面描述',
    
    -- 页面布局配置
    layout_type VARCHAR(20) DEFAULT 'grid' COMMENT '布局类型：grid, flex, absolute',
    layout_columns INT DEFAULT 12 COMMENT '网格列数',
    layout_gap INT DEFAULT 16 COMMENT '间距',
    layout_padding INT DEFAULT 24 COMMENT '内边距',
    
    -- 页面主题配置
    theme_config JSON COMMENT '主题配置JSON',
    
    -- 权限配置
    view_permissions JSON COMMENT '查看权限',
    edit_permissions JSON COMMENT '编辑权限',
    
    version VARCHAR(20) DEFAULT '1.0.0' COMMENT '版本号',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_page_path (page_path),
    INDEX idx_status (status),
    INDEX idx_created_time (created_time)
) COMMENT '页面配置表';

-- 2. 组件配置表（详细配置，支持复用）
CREATE TABLE component_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(150) NOT NULL UNIQUE COMMENT '配置唯一键',
    component_type VARCHAR(50) NOT NULL COMMENT '组件类型',
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    description TEXT COMMENT '配置描述',
    config_data JSON NOT NULL COMMENT '配置数据JSON',
    
    -- 分类和标签
    category VARCHAR(50) COMMENT '配置分类：业务表单、数据展示、导航菜单等',
    tags JSON COMMENT '标签数组，便于搜索和分类',
    
    -- 复用性配置
    is_template TINYINT DEFAULT 0 COMMENT '是否为模板：1-是，0-否',
    is_shared TINYINT DEFAULT 0 COMMENT '是否可跨页面共享：1-是，0-否',
    is_system TINYINT DEFAULT 0 COMMENT '是否为系统配置：1-是，0-否',
    
    -- 使用统计
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    last_used_time DATETIME COMMENT '最后使用时间',
    
    version VARCHAR(20) DEFAULT '1.0.0' COMMENT '版本号',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_config_key (config_key),
    INDEX idx_component_type (component_type),
    INDEX idx_category (category),
    INDEX idx_is_shared (is_shared),
    INDEX idx_is_template (is_template),
    INDEX idx_usage_count (usage_count),
    INDEX idx_status (status)
) COMMENT '组件配置表';

-- 3. 页面组件关联表
CREATE TABLE page_component_refs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    page_config_id BIGINT NOT NULL COMMENT '页面配置ID',
    component_id VARCHAR(50) NOT NULL COMMENT '页面内组件标识',
    component_type VARCHAR(50) NOT NULL COMMENT '组件类型',
    config_key VARCHAR(150) NOT NULL COMMENT '组件配置键',
    
    -- 位置信息
    position_row INT COMMENT '网格行位置',
    position_col INT COMMENT '网格列位置',
    position_x INT COMMENT '绝对定位X坐标',
    position_y INT COMMENT '绝对定位Y坐标',
    position_width INT COMMENT '组件宽度',
    position_height INT COMMENT '组件高度',
    
    -- 显示配置
    visible TINYINT DEFAULT 1 COMMENT '是否可见：1-是，0-否',
    display_order INT DEFAULT 0 COMMENT '显示顺序',
    
    -- 页面级配置覆盖
    override_config JSON COMMENT '页面级配置覆盖JSON',
    
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_page_config (page_config_id),
    INDEX idx_component_id (component_id),
    INDEX idx_config_key (config_key),
    INDEX idx_display_order (display_order),
    FOREIGN KEY (page_config_id) REFERENCES page_configs(id) ON DELETE CASCADE,
    FOREIGN KEY (config_key) REFERENCES component_configs(config_key) ON DELETE CASCADE,
    
    UNIQUE KEY uk_page_component (page_config_id, component_id)
) COMMENT '页面组件关联表';

-- 4. 配置使用记录表（用于统计和分析）
CREATE TABLE config_usage_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(150) NOT NULL COMMENT '配置键',
    page_path VARCHAR(200) NOT NULL COMMENT '使用页面',
    component_id VARCHAR(50) NOT NULL COMMENT '组件标识',
    user_id BIGINT COMMENT '使用用户ID',
    usage_type VARCHAR(20) NOT NULL COMMENT '使用类型：view, edit, copy',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_config_key (config_key),
    INDEX idx_page_path (page_path),
    INDEX idx_user_id (user_id),
    INDEX idx_created_time (created_time)
) COMMENT '配置使用记录表';

-- 5. 配置分组表
CREATE TABLE config_groups (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_key VARCHAR(100) NOT NULL UNIQUE COMMENT '分组键',
    group_name VARCHAR(100) NOT NULL COMMENT '分组名称',
    description TEXT COMMENT '分组描述',
    component_type VARCHAR(50) NOT NULL COMMENT '组件类型',
    is_system TINYINT DEFAULT 0 COMMENT '是否为系统分组：1-是，0-否',
    
    created_by BIGINT COMMENT '创建人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_group_key (group_key),
    INDEX idx_component_type (component_type),
    INDEX idx_is_system (is_system)
) COMMENT '配置分组表';

-- 6. 配置分组关联表
CREATE TABLE config_group_relations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_id BIGINT NOT NULL COMMENT '分组ID',
    config_key VARCHAR(150) NOT NULL COMMENT '配置键',
    sort_order INT DEFAULT 0 COMMENT '排序',
    
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_group_id (group_id),
    INDEX idx_config_key (config_key),
    INDEX idx_sort_order (sort_order),
    FOREIGN KEY (group_id) REFERENCES config_groups(id) ON DELETE CASCADE,
    FOREIGN KEY (config_key) REFERENCES component_configs(config_key) ON DELETE CASCADE,
    
    UNIQUE KEY uk_group_config (group_id, config_key)
) COMMENT '配置分组关联表';

-- 7. 配置历史表
CREATE TABLE component_config_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(150) NOT NULL COMMENT '配置键',
    config_data JSON NOT NULL COMMENT '历史配置数据',
    version VARCHAR(20) COMMENT '版本号',
    change_reason VARCHAR(200) COMMENT '变更原因',
    change_summary TEXT COMMENT '变更摘要',
    
    created_by BIGINT COMMENT '操作人ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_config_key (config_key),
    INDEX idx_created_time (created_time),
    FOREIGN KEY (config_key) REFERENCES component_configs(config_key) ON DELETE CASCADE
) COMMENT '组件配置历史表';

-- 插入一些示例数据

-- 示例页面配置
INSERT INTO page_configs (page_path, page_name, description, layout_type, layout_columns) VALUES
('/system/user', '用户管理', '系统用户管理页面', 'grid', 12),
('/system/role', '角色管理', '系统角色管理页面', 'grid', 12),
('/system/menu', '菜单管理', '系统菜单管理页面', 'grid', 12);

-- 示例组件配置
INSERT INTO component_configs (config_key, component_type, config_name, description, config_data, category, tags, is_shared) VALUES
('system_user_userTree', 'SuperTree', '用户树组件配置', '用户管理页面的树形组件配置', '{"fieldMapping":{"label":"name","children":"children","id":"id"},"toolbar":{"enabled":true}}', '数据展示', '["用户", "树形", "管理"]', 1),
('system_role_roleList', 'SuperList', '角色列表配置', '角色管理页面的列表组件配置', '{"pagination":{"enabled":true,"pageSize":20},"selection":{"enabled":true}}', '数据展示', '["角色", "列表", "管理"]', 1);

-- 示例页面组件关联
INSERT INTO page_component_refs (page_config_id, component_id, component_type, config_key, position_row, position_col, position_width, position_height) VALUES
(1, 'userTree', 'SuperTree', 'system_user_userTree', 1, 1, 6, 12),
(2, 'roleList', 'SuperList', 'system_role_roleList', 1, 1, 12, 10);