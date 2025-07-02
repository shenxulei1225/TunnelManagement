-- 区域批量导入日志表（简化版）
CREATE TABLE IF NOT EXISTS `system_region_import_log` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `batch_id` VARCHAR(64) NOT NULL COMMENT '批次ID',
  `file_name` VARCHAR(255) NOT NULL COMMENT '导入文件名',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `total_count` INT NOT NULL DEFAULT 0 COMMENT '总记录数',
  `success_count` INT NOT NULL DEFAULT 0 COMMENT '成功记录数',
  `failed_count` INT NOT NULL DEFAULT 0 COMMENT '失败记录数',
  `error_message` TEXT COMMENT '错误信息摘要',
  `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `idx_batch_id` (`batch_id`),
  INDEX `idx_category_id` (`category_id`),
  INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域批量导入日志表'; 