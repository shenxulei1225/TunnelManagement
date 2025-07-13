-- 模型目录表
CREATE TABLE `dynamic_model_directory` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '目录ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `parent_id` bigint DEFAULT 0 COMMENT '父目录ID（0表示根目录）',
    `name` varchar(100) NOT NULL COMMENT '目录名称',
    `code` varchar(100) NOT NULL COMMENT '目录编码',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `icon` varchar(100) DEFAULT NULL COMMENT '图标',
    `sort` int DEFAULT 0 COMMENT '排序号',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0:禁用，1:启用）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_code` (`tenant_id`, `code`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模型目录表';

-- 为dynamic_business_model表添加directory_id字段
ALTER TABLE `dynamic_business_model` 
ADD COLUMN `directory_id` bigint DEFAULT 0 COMMENT '所属目录ID（0表示未分类）' AFTER `model_type`;

-- 插入默认目录数据
INSERT INTO `dynamic_model_directory` (`id`, `tenant_id`, `parent_id`, `name`, `code`, `description`, `icon`, `sort`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(1, 1, 0, '系统模型', 'system_models', '系统预置的业务模型', 'el-icon-setting', 1, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(2, 1, 0, '设备管理', 'device_management', '设备相关的业务模型', 'el-icon-cpu', 2, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(3, 1, 0, '人员管理', 'personnel_management', '人员相关的业务模型', 'el-icon-user', 3, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(4, 1, 0, '项目管理', 'project_management', '项目相关的业务模型', 'el-icon-folder', 4, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(5, 1, 0, '财务管理', 'finance_management', '财务相关的业务模型', 'el-icon-money', 5, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(6, 1, 0, '其他', 'others', '其他业务模型', 'el-icon-more', 999, 1, 'admin', NOW(), 'admin', NOW(), b'0');

-- 更新现有模型的目录ID
UPDATE `dynamic_business_model` SET `directory_id` = 1 WHERE `model_type` = 0;
UPDATE `dynamic_business_model` SET `directory_id` = 6 WHERE `model_type` = 1 AND `directory_id` = 0; 