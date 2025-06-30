-- =============================================
-- 文件管理分类初始化 SQL 脚本
-- 基于现有的 system_field_category 表
-- =============================================

-- 1. 插入文件管理根分类
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES (
    0, 'file_management', '文件管理', '/1', 1, 100,
    0, NOW(), 'system', 1
);

-- 获取根分类ID
SET @root_id = LAST_INSERT_ID();
UPDATE system_field_category SET tree_path = CONCAT('/', @root_id) WHERE id = @root_id;

-- 2. 插入二级分类
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES 
-- 工作台文档
(@root_id, 'workspace_docs', '工作台文档', '', 2, 10, 0, NOW(), 'system', 1),
-- 维护手册  
(@root_id, 'maintenance_manual', '维护手册', '', 2, 20, 0, NOW(), 'system', 1),
-- 用户资料
(@root_id, 'user_profile', '用户资料', '', 2, 30, 0, NOW(), 'system', 1),
-- 项目附件
(@root_id, 'project_attachments', '项目附件', '', 2, 40, 0, NOW(), 'system', 1),
-- 系统文件
(@root_id, 'system_files', '系统文件', '', 2, 50, 0, NOW(), 'system', 1);

-- 更新二级分类的tree_path
UPDATE system_field_category 
SET tree_path = CONCAT('/', @root_id, '/', id) 
WHERE parent_id = @root_id;

-- 3. 插入三级分类 - 工作台文档子分类
SET @workspace_id = (SELECT id FROM system_field_category WHERE code = 'workspace_docs' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES 
(@workspace_id, 'design_docs', '设计文档', '', 3, 10, 0, NOW(), 'system', 1),
(@workspace_id, 'project_docs', '项目文档', '', 3, 20, 0, NOW(), 'system', 1),
(@workspace_id, 'temp_files', '临时文件', '', 3, 30, 0, NOW(), 'system', 1);

-- 更新工作台文档子分类的tree_path
UPDATE system_field_category 
SET tree_path = CONCAT('/', @root_id, '/', @workspace_id, '/', id) 
WHERE parent_id = @workspace_id;

-- 4. 插入三级分类 - 维护手册子分类
SET @manual_id = (SELECT id FROM system_field_category WHERE code = 'maintenance_manual' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES 
(@manual_id, 'operation_manual', '操作手册', '', 3, 10, 0, NOW(), 'system', 1),
(@manual_id, 'troubleshooting', '故障排除', '', 3, 20, 0, NOW(), 'system', 1),
(@manual_id, 'system_config', '系统配置', '', 3, 30, 0, NOW(), 'system', 1);

-- 更新维护手册子分类的tree_path
UPDATE system_field_category 
SET tree_path = CONCAT('/', @root_id, '/', @manual_id, '/', id) 
WHERE parent_id = @manual_id;

-- 5. 插入三级分类 - 用户资料子分类
SET @profile_id = (SELECT id FROM system_field_category WHERE code = 'user_profile' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES 
(@profile_id, 'avatar_images', '头像图片', '', 3, 10, 0, NOW(), 'system', 1),
(@profile_id, 'personal_docs', '个人文档', '', 3, 20, 0, NOW(), 'system', 1),
(@profile_id, 'user_settings', '用户设置', '', 3, 30, 0, NOW(), 'system', 1);

-- 更新用户资料子分类的tree_path
UPDATE system_field_category 
SET tree_path = CONCAT('/', @root_id, '/', @profile_id, '/', id) 
WHERE parent_id = @profile_id;

-- 6. 插入三级分类 - 项目附件子分类
SET @attachment_id = (SELECT id FROM system_field_category WHERE code = 'project_attachments' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES 
(@attachment_id, 'requirements', '需求文档', '', 3, 10, 0, NOW(), 'system', 1),
(@attachment_id, 'design_files', '设计稿', '', 3, 20, 0, NOW(), 'system', 1),
(@attachment_id, 'test_files', '测试文件', '', 3, 30, 0, NOW(), 'system', 1);

-- 更新项目附件子分类的tree_path
UPDATE system_field_category 
SET tree_path = CONCAT('/', @root_id, '/', @attachment_id, '/', id) 
WHERE parent_id = @attachment_id;

-- 7. 插入三级分类 - 系统文件子分类  
SET @system_id = (SELECT id FROM system_field_category WHERE code = 'system_files' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    readonly, create_time, creator, tenant_id
) VALUES 
(@system_id, 'config_files', '配置文件', '', 3, 10, 0, NOW(), 'system', 1),
(@system_id, 'log_files', '日志文件', '', 3, 20, 0, NOW(), 'system', 1),
(@system_id, 'backup_files', '备份文件', '', 3, 30, 0, NOW(), 'system', 1);

-- 更新系统文件子分类的tree_path
UPDATE system_field_category 
SET tree_path = CONCAT('/', @root_id, '/', @system_id, '/', id) 
WHERE parent_id = @system_id;

-- 8. 创建文件信息表
CREATE TABLE IF NOT EXISTS sys_file_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文件ID',
    category_id BIGINT NOT NULL COMMENT '分类ID，关联system_field_category.id',
    business_id VARCHAR(64) DEFAULT NULL COMMENT '业务关联ID（如用户ID、项目ID等）',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    stored_name VARCHAR(255) NOT NULL COMMENT '存储文件名',
    file_path VARCHAR(512) NOT NULL COMMENT '文件存储路径',
    file_size BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小（字节）',
    mime_type VARCHAR(100) DEFAULT NULL COMMENT 'MIME类型',
    file_extension VARCHAR(20) DEFAULT NULL COMMENT '文件扩展名',
    md5_hash VARCHAR(32) DEFAULT NULL COMMENT 'MD5校验值',
    upload_status TINYINT NOT NULL DEFAULT 1 COMMENT '上传状态：1-成功，2-失败，3-处理中',
    access_level TINYINT NOT NULL DEFAULT 1 COMMENT '访问级别：1-公开，2-内部，3-私有',
    tags JSON DEFAULT NULL COMMENT '文件标签（JSON数组）',
    metadata JSON DEFAULT NULL COMMENT '文件元数据（JSON对象）',
    description TEXT DEFAULT NULL COMMENT '文件描述',
    download_count INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    last_access_time DATETIME DEFAULT NULL COMMENT '最后访问时间',
    expiry_time DATETIME DEFAULT NULL COMMENT '过期时间（NULL表示永不过期）',
    
    -- 基础字段
    creator VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT NULL COMMENT '更新者', 
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户ID',
    
    -- 索引
    INDEX idx_category_business (category_id, business_id),
    INDEX idx_tenant_creator (tenant_id, creator),
    INDEX idx_upload_time (create_time),
    INDEX idx_file_hash (md5_hash),
    INDEX idx_expiry (expiry_time),
    
    -- 外键约束
    FOREIGN KEY (category_id) REFERENCES system_field_category(id) ON UPDATE CASCADE
) COMMENT '文件信息表';

-- 9. 查询验证分类创建结果
SELECT 
    id,
    parent_id,
    code,
    name,
    tree_path,
    level,
    sort
FROM system_field_category 
WHERE tree_path LIKE CONCAT('%/', @root_id, '%') 
   OR id = @root_id
ORDER BY level, sort;

-- 输出提示信息
SELECT CONCAT('文件管理分类初始化完成！根分类ID: ', @root_id) AS message; 