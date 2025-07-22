-- 创建通用树节点表
CREATE TABLE IF NOT EXISTS `system_tree_node` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '节点ID',
    `tree_type` varchar(50) NOT NULL COMMENT '树类型',
    `name` varchar(255) NOT NULL COMMENT '节点名称',
    `code` varchar(100) DEFAULT NULL COMMENT '节点编码',
    `parent_id` bigint DEFAULT 0 COMMENT '父节点ID',
    `tree_path` varchar(1000) DEFAULT NULL COMMENT '树路径',
    `level` int DEFAULT 1 COMMENT '层级深度',
    `sort` int DEFAULT 0 COMMENT '排序号',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态(0-禁用 1-启用)',
    `readonly` bit(1) DEFAULT b'0' COMMENT '是否只读',
    `description` varchar(500) DEFAULT NULL COMMENT '节点描述',
    `icon` varchar(100) DEFAULT NULL COMMENT '图标',
    `color` varchar(50) DEFAULT NULL COMMENT '颜色',
    `extra_attrs` json DEFAULT NULL COMMENT '扩展属性',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_tree_type` (`tree_type`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_code` (`code`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_tree_type_parent` (`tree_type`, `parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通用树节点表';

-- 插入测试数据配置
INSERT IGNORE INTO system_tree_config (
    id, tree_type, tree_name, description, allowed_data_types, 
    max_level, node_name_label, node_code_label, sort_type, status, config_json,
    creator, create_time, updater, update_time, deleted, tenant_id
) VALUES (
    1001, 'test_tree', '测试树结构', '用于测试和开发的树形结构管理', 
    '["test_node", "test_data"]', 10, '节点名称', '节点编码', 1, 1, 
    '{"supportBusinessType": true, "defaultSort": "asc", "enableDrag": true}',
    '1', NOW(), '1', NOW(), 0, 1
); 