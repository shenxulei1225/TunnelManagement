-- 通用目录表
CREATE TABLE `system_directory` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '目录ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `business_type` varchar(50) NOT NULL COMMENT '业务类型',
    `parent_id` bigint DEFAULT 0 COMMENT '父目录ID（0表示根目录）',
    `name` varchar(100) NOT NULL COMMENT '目录名称',
    `code` varchar(100) NOT NULL COMMENT '目录编码',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `icon` varchar(100) DEFAULT NULL COMMENT '图标',
    `sort` int DEFAULT 0 COMMENT '排序号',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0:禁用，1:启用）',
    `ext_data` text COMMENT '扩展属性（JSON格式）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_business_code` (`tenant_id`, `business_type`, `code`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_business_type` (`business_type`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通用目录表';

-- 插入默认目录数据（动态业务模型）
INSERT INTO `system_directory` (`id`, `tenant_id`, `business_type`, `parent_id`, `name`, `code`, `description`, `icon`, `sort`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(1, 1, 'dynamic_model', 0, '系统模型', 'system_models', '系统预置的业务模型', 'el-icon-setting', 1, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(2, 1, 'dynamic_model', 0, '设备管理', 'device_management', '设备相关的业务模型', 'el-icon-cpu', 2, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(3, 1, 'dynamic_model', 0, '人员管理', 'personnel_management', '人员相关的业务模型', 'el-icon-user', 3, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(4, 1, 'dynamic_model', 0, '项目管理', 'project_management', '项目相关的业务模型', 'el-icon-folder', 4, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(5, 1, 'dynamic_model', 0, '财务管理', 'finance_management', '财务相关的业务模型', 'el-icon-money', 5, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(6, 1, 'dynamic_model', 0, '其他', 'others', '其他业务模型', 'el-icon-more', 999, 1, 'admin', NOW(), 'admin', NOW(), b'0');

-- 插入文件管理默认目录
INSERT INTO `system_directory` (`id`, `tenant_id`, `business_type`, `parent_id`, `name`, `code`, `description`, `icon`, `sort`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(7, 1, 'file_management', 0, '文档', 'documents', '文档文件', 'el-icon-document', 1, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(8, 1, 'file_management', 0, '图片', 'images', '图片文件', 'el-icon-picture', 2, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(9, 1, 'file_management', 0, '视频', 'videos', '视频文件', 'el-icon-video-camera', 3, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(10, 1, 'file_management', 0, '音频', 'audios', '音频文件', 'el-icon-headset', 4, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(11, 1, 'file_management', 0, '其他', 'others', '其他文件', 'el-icon-files', 999, 1, 'admin', NOW(), 'admin', NOW(), b'0');

-- 插入资产管理默认目录
INSERT INTO `system_directory` (`id`, `tenant_id`, `business_type`, `parent_id`, `name`, `code`, `description`, `icon`, `sort`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(12, 1, 'asset_management', 0, '固定资产', 'fixed_assets', '固定资产', 'el-icon-office-building', 1, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(13, 1, 'asset_management', 0, '流动资产', 'current_assets', '流动资产', 'el-icon-money', 2, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(14, 1, 'asset_management', 0, '无形资产', 'intangible_assets', '无形资产', 'el-icon-trophy', 3, 1, 'admin', NOW(), 'admin', NOW(), b'0'),
(15, 1, 'asset_management', 0, '其他', 'others', '其他资产', 'el-icon-box', 999, 1, 'admin', NOW(), 'admin', NOW(), b'0'); 