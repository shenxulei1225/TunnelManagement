-- ----------------------------
-- 测试树节点表
-- ----------------------------
CREATE TABLE `system_test_tree_node` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) NOT NULL COMMENT '节点名称',
  `type` varchar(20) NOT NULL COMMENT '节点类型',
  `business_type` varchar(20) NOT NULL COMMENT '业务类型',
  `parent_id` bigint DEFAULT NULL COMMENT '父节点编号',
  `description` varchar(200) DEFAULT NULL COMMENT '节点描述',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `count` int DEFAULT '0' COMMENT '节点计数',
  `extra_attrs` json DEFAULT NULL COMMENT '扩展属性',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_business_type` (`business_type`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_sort` (`sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试树节点表';

-- ----------------------------
-- 初始化测试数据
-- ----------------------------
INSERT INTO `system_test_tree_node` VALUES 
(1, '总公司', 'dept', 'dept', NULL, '顶级部门', 1, 0, 5, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(2, '技术部', 'dept', 'dept', 1, '技术开发部门', 1, 0, 3, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(3, '产品部', 'dept', 'dept', 1, '产品管理部门', 2, 0, 2, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(4, '前端组', 'dept', 'dept', 2, '前端开发小组', 1, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(5, '后端组', 'dept', 'dept', 2, '后端开发小组', 2, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(6, '测试组', 'dept', 'dept', 2, '测试验证小组', 3, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(7, '产品经理', 'dept', 'dept', 3, '产品经理岗位', 1, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(8, '用户体验', 'dept', 'dept', 3, '用户体验设计', 2, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(9, '分公司', 'dept', 'dept', NULL, '分支机构', 2, 0, 2, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(10, '北京分公司', 'dept', 'dept', 9, '北京地区分公司', 1, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(11, '上海分公司', 'dept', 'dept', 9, '上海地区分公司', 2, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),

(12, '服务器A', 'device', 'device', NULL, 'Linux服务器', 1, 0, 3, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(13, '数据库', 'device', 'device', 12, 'MySQL数据库服务', 1, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(14, 'Web服务', 'device', 'device', 12, 'Nginx Web服务', 2, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(15, '应用服务', 'device', 'device', 12, 'Tomcat应用服务', 3, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(16, '服务器B', 'device', 'device', NULL, 'Windows服务器', 2, 0, 2, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(17, '文件服务', 'device', 'device', 16, '文件存储服务', 1, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(18, '备份服务', 'device', 'device', 16, '数据备份服务', 2, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),

(19, '基础分类', 'category', 'category', NULL, '基础数据分类', 1, 0, 2, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(20, '系统配置', 'category', 'category', 19, '系统配置参数', 1, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(21, '业务数据', 'category', 'category', 19, '业务相关数据', 2, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(22, '扩展分类', 'category', 'category', NULL, '扩展数据分类', 2, 0, 1, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1),
(23, '第三方集成', 'category', 'category', 22, '第三方系统集成', 1, 0, 0, NULL, 'system', '2025-01-16 23:00:00', 'system', '2025-01-16 23:00:00', b'0', 1); 