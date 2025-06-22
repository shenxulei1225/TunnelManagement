-- 动态字段值表
CREATE TABLE IF NOT EXISTS `system_field_value` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `biz_type` varchar(64) NOT NULL COMMENT '业务类型',
  `biz_id` bigint NOT NULL COMMENT '业务记录 ID',
  `field_id` bigint NOT NULL COMMENT '字段定义 ID',
  `value_json` text NOT NULL COMMENT '值 JSON',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY `idx_biz` (`biz_type`, `biz_id`),
  KEY `idx_field` (`field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态字段值表';
