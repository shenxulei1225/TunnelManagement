-- 创建业务分组表（支持多级树形结构）
CREATE TABLE IF NOT EXISTS `business_module` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父节点 ID，0 表示根',
  `code` varchar(64) DEFAULT NULL COMMENT '节点编码',
  `name` varchar(255) NOT NULL COMMENT '展示名称',
  `tree_path` varchar(255) NOT NULL COMMENT '树路径，如 "1/15/37"',
  `level` int NOT NULL DEFAULT '1' COMMENT '层级深度',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `readonly` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否系统只读，1=只读（禁止删除/改名）',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_tree_path` (`tree_path`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务分组/系统表（多级树形）';

-- 插入一些测试数据
INSERT INTO `business_module` (`id`, `parent_id`, `code`, `name`, `tree_path`, `level`, `sort`, `readonly`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(1, 0, 'INSPECTION', '巡检系统', '0', 1, 1, 0, 'admin', NOW(), 'admin', NOW(), 0),
(2, 1, 'DAILY_INSPECTION', '日常巡检', '0/1', 2, 1, 0, 'admin', NOW(), 'admin', NOW(), 0),
(3, 1, 'KEY_INSPECTION', '重点巡检', '0/1', 2, 2, 0, 'admin', NOW(), 'admin', NOW(), 0),
(4, 2, 'ROUTINE_CHECK', '例行检查', '0/1/2', 3, 1, 0, 'admin', NOW(), 'admin', NOW(), 0),
(5, 2, 'SPECIAL_CHECK', '专项检查', '0/1/2', 3, 2, 0, 'admin', NOW(), 'admin', NOW(), 0),
(6, 0, 'MAINTENANCE', '维护系统', '0', 1, 2, 0, 'admin', NOW(), 'admin', NOW(), 0),
(7, 6, 'PREVENTIVE_MAINTENANCE', '预防性维护', '0/6', 2, 1, 0, 'admin', NOW(), 'admin', NOW(), 0),
(8, 6, 'CORRECTIVE_MAINTENANCE', '纠正性维护', '0/6', 2, 2, 0, 'admin', NOW(), 'admin', NOW(), 0); 