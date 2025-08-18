-- =====================================================================================
-- 组件默认值系统数据库优化方案
-- 实现Element/UMG风格的默认值机制，大幅减少配置数据存储量
-- =====================================================================================

-- 1. 全局默认配置表
-- =====================================================================================
CREATE TABLE IF NOT EXISTS `component_global_defaults` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `theme` varchar(50) NOT NULL DEFAULT 'light' COMMENT '主题 (light/dark/auto)',
  `size` varchar(20) NOT NULL DEFAULT 'medium' COMMENT '尺寸 (small/medium/large)',
  `border_radius` int NOT NULL DEFAULT 4 COMMENT '边框圆角',
  `spacing` int NOT NULL DEFAULT 8 COMMENT '间距',
  `primary_color` varchar(20) NOT NULL DEFAULT '#409EFF' COMMENT '主色调',
  `font_family` varchar(200) NOT NULL DEFAULT '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto' COMMENT '字体',
  `version` varchar(20) NOT NULL DEFAULT '1.0.0' COMMENT '版本号',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否激活',
  `created_by` bigint NULL COMMENT '创建者ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint NULL COMMENT '更新者ID', 
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  INDEX `idx_version_active` (`version`, `is_active`),
  INDEX `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='全局默认配置表';

-- 2. 组件类型默认配置表
-- =====================================================================================
CREATE TABLE IF NOT EXISTS `component_type_defaults` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `component_type` varchar(100) NOT NULL COMMENT '组件类型 (SuperTree/SuperList/SuperForm等)',
  `category` varchar(50) NULL COMMENT '组件分类 (basic/form/data/feedback/navigation/layout)',
  `version` varchar(20) NOT NULL DEFAULT '1.0.0' COMMENT '版本号',
  `default_config` longtext NOT NULL COMMENT '默认配置JSON',
  `inheritance_chain` json NULL COMMENT '继承链',
  `description` text NULL COMMENT '描述',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否激活',
  `created_by` bigint NULL COMMENT '创建者ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint NULL COMMENT '更新者ID',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_version` (`component_type`, `version`, `deleted`),
  INDEX `idx_component_type` (`component_type`),
  INDEX `idx_category` (`category`),
  INDEX `idx_version_active` (`version`, `is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组件类型默认配置表';

-- 3. 业务模板默认配置表
-- =====================================================================================
CREATE TABLE IF NOT EXISTS `component_template_defaults` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` varchar(100) NOT NULL COMMENT '模板ID',
  `template_name` varchar(200) NOT NULL COMMENT '模板名称',
  `template_category` varchar(50) NULL COMMENT '模板分类',
  `global_overrides` json NULL COMMENT '全局配置覆盖',
  `component_overrides` json NOT NULL COMMENT '组件配置覆盖',
  `description` text NULL COMMENT '描述',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否激活',
  `created_by` bigint NULL COMMENT '创建者ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint NULL COMMENT '更新者ID',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_id` (`template_id`, `deleted`),
  INDEX `idx_template_category` (`template_category`),
  INDEX `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务模板默认配置表';

-- 4. 优化后的组件实例表
-- =====================================================================================
CREATE TABLE IF NOT EXISTS `component_instances_optimized` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_id` varchar(100) NOT NULL COMMENT '实例ID',
  `component_type` varchar(100) NOT NULL COMMENT '组件类型',
  `config_version` varchar(20) NOT NULL DEFAULT '1.0.0' COMMENT '配置版本',
  `template_id` varchar(100) NULL COMMENT '模板ID',
  `custom_config` longtext NULL COMMENT '自定义配置JSON (只存储差异)',
  `inheritance_chain` json NULL COMMENT '继承链',
  `page_id` bigint NULL COMMENT '页面ID',
  `parent_id` bigint NULL COMMENT '父组件ID',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否激活',
  `created_by` bigint NULL COMMENT '创建者ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint NULL COMMENT '更新者ID',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_instance_id` (`instance_id`, `deleted`),
  INDEX `idx_component_type` (`component_type`),
  INDEX `idx_template_id` (`template_id`),
  INDEX `idx_page_id` (`page_id`),
  INDEX `idx_parent_id` (`parent_id`),
  INDEX `idx_config_version` (`config_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优化后的组件实例表(只存储差异配置)';

-- 5. 配置变更历史表 (用于版本管理和回滚)
-- =====================================================================================
CREATE TABLE IF NOT EXISTS `component_config_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_id` varchar(100) NOT NULL COMMENT '实例ID',
  `change_type` varchar(20) NOT NULL COMMENT '变更类型 (create/update/delete)',
  `before_config` longtext NULL COMMENT '变更前配置',
  `after_config` longtext NULL COMMENT '变更后配置',
  `config_diff` longtext NULL COMMENT '配置差异',
  `change_reason` varchar(500) NULL COMMENT '变更原因',
  `created_by` bigint NULL COMMENT '操作者ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_instance_id` (`instance_id`),
  INDEX `idx_change_type` (`change_type`),
  INDEX `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组件配置变更历史表';

-- 6. 存储优化统计表
-- =====================================================================================
CREATE TABLE IF NOT EXISTS `storage_optimization_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `total_instances` int NOT NULL DEFAULT 0 COMMENT '总实例数',
  `instances_with_custom_config` int NOT NULL DEFAULT 0 COMMENT '有自定义配置的实例数',
  `avg_config_size_before` int NOT NULL DEFAULT 0 COMMENT '优化前平均配置大小(字节)',
  `avg_custom_config_size` int NOT NULL DEFAULT 0 COMMENT '平均自定义配置大小(字节)', 
  `total_storage_before` bigint NOT NULL DEFAULT 0 COMMENT '优化前总存储大小(字节)',
  `total_storage_after` bigint NOT NULL DEFAULT 0 COMMENT '优化后总存储大小(字节)',
  `storage_saved_bytes` bigint NOT NULL DEFAULT 0 COMMENT '节省的存储大小(字节)',
  `storage_saved_percentage` decimal(5,2) NOT NULL DEFAULT 0.00 COMMENT '节省百分比',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_stat_date` (`stat_date`),
  INDEX `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='存储优化统计表';

-- =====================================================================================
-- 初始化默认数据
-- =====================================================================================

-- 1. 插入全局默认配置
INSERT INTO `component_global_defaults` (
  `theme`, `size`, `border_radius`, `spacing`, `primary_color`, `font_family`, `version`, `is_active`
) VALUES (
  'light', 'medium', 4, 8, '#409EFF', 
  '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
  '1.0.0', 1
) ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;

-- 2. 插入SuperTree组件默认配置
INSERT INTO `component_type_defaults` (
  `component_type`, `category`, `version`, `default_config`, `description`, `is_active`
) VALUES (
  'SuperTree', 'data', '1.0.0',
  '{
    "theme": "modern",
    "size": "medium", 
    "showBorder": true,
    "borderRadius": 6,
    "height": 400,
    "showCheckbox": false,
    "checkStrictly": false,
    "showIcon": true,
    "iconSize": 16,
    "nodeKey": "id",
    "allowDrag": false,
    "allowDrop": false,
    "draggable": false,
    "clickToExpand": true,
    "showToolbar": true,
    "toolbarPosition": "top",
    "showSearch": true,
    "showRefresh": true,
    "showExpandAll": true,
    "showCollapseAll": true,
    "showFilter": false,
    "lazy": false,
    "load": null,
    "defaultExpandAll": false,
    "defaultExpandLevel": 1,
    "autoExpandParent": true,
    "defaultCheckedKeys": [],
    "defaultExpandedKeys": [],
    "filterNodeMethod": null,
    "searchPlaceholder": "搜索节点",
    "highlightCurrent": true,
    "expandOnClickNode": true,
    "checkOnClickNode": false,
    "virtualScroll": false,
    "itemHeight": 32,
    "bufferSize": 10,
    "renderAfterExpand": true,
    "indent": 18,
    "emptyText": "暂无数据",
    "showCheckboxLeafOnly": false
  }',
  'SuperTree组件默认配置', 1
) ON DUPLICATE KEY UPDATE 
  `default_config` = VALUES(`default_config`),
  `updated_time` = CURRENT_TIMESTAMP;

-- 3. 插入SuperList组件默认配置
INSERT INTO `component_type_defaults` (
  `component_type`, `category`, `version`, `default_config`, `description`, `is_active`
) VALUES (
  'SuperList', 'data', '1.0.0',
  '{
    "theme": "modern",
    "size": "medium",
    "stripe": true,
    "border": true,
    "fit": true,
    "showHeader": true,
    "highlightCurrentRow": false,
    "selection": false,
    "sortable": true,
    "filterable": true,
    "resizable": true,
    "showSummary": false,
    "pagination": true,
    "pageSize": 20,
    "pageSizes": [10, 20, 50, 100],
    "paginationPosition": "bottom",
    "showTotal": true,
    "showSizeChanger": true,
    "showQuickJumper": false,
    "showToolbar": true,
    "toolbarPosition": "top",
    "showAdd": true,
    "showEdit": true,
    "showDelete": true,
    "showExport": true,
    "showImport": false,
    "showRefresh": true,
    "showColumnSettings": true,
    "columns": [],
    "defaultSort": {},
    "spanMethod": null,
    "lazy": false,
    "virtualScroll": false,
    "rowHeight": 48,
    "maxHeight": null,
    "rowKey": "id",
    "expandRowKeys": [],
    "defaultExpandAll": false,
    "selectOnIndeterminate": true,
    "emptyText": "暂无数据",
    "tableLayout": "fixed",
    "showOverflowTooltip": true
  }',
  'SuperList组件默认配置', 1
) ON DUPLICATE KEY UPDATE 
  `default_config` = VALUES(`default_config`),
  `updated_time` = CURRENT_TIMESTAMP;

-- 4. 插入SuperForm组件默认配置
INSERT INTO `component_type_defaults` (
  `component_type`, `category`, `version`, `default_config`, `description`, `is_active`
) VALUES (
  'SuperForm', 'form', '1.0.0',
  '{
    "theme": "modern",
    "size": "medium",
    "labelPosition": "right",
    "labelWidth": "120px",
    "labelSuffix": "",
    "inline": false,
    "columns": 1,
    "gutter": 20,
    "rules": {},
    "validateOnRuleChange": true,
    "showMessage": true,
    "inlineMessage": false,
    "statusIcon": false,
    "validateOnBlur": true,
    "validateOnChange": true,
    "fields": [],
    "disabled": false,
    "showSubmit": true,
    "showReset": true,
    "showCancel": false,
    "submitText": "提交",
    "resetText": "重置",
    "cancelText": "取消",
    "hideRequiredAsterisk": false,
    "scrollToError": false,
    "scrollIntoViewOptions": null
  }',
  'SuperForm组件默认配置', 1
) ON DUPLICATE KEY UPDATE 
  `default_config` = VALUES(`default_config`),
  `updated_time` = CURRENT_TIMESTAMP;

-- 5. 插入一些业务模板示例
INSERT INTO `component_template_defaults` (
  `template_id`, `template_name`, `template_category`, `global_overrides`, `component_overrides`, `description`, `sort_order`
) VALUES (
  'data-management', '数据管理模板', 'business',
  '{"theme": "light", "size": "medium"}',
  '{
    "SuperTree": {"showToolbar": true, "showCheckbox": true, "showSearch": true},
    "SuperList": {"pagination": true, "showToolbar": true, "stripe": true},
    "SuperForm": {"labelPosition": "right", "showSubmit": true, "showReset": true}
  }',
  '标准数据管理页面模板', 1
),
(
  'dashboard', '仪表板模板', 'business', 
  '{"theme": "dark", "size": "large"}',
  '{
    "SuperTree": {"showBorder": false, "theme": "minimal"},
    "SuperList": {"border": false, "stripe": false},
    "SuperForm": {"inline": true, "size": "small"}
  }',
  '仪表板页面模板', 2
),
(
  'mobile-friendly', '移动端友好模板', 'responsive',
  '{"size": "small", "spacing": 4}',
  '{
    "SuperTree": {"itemHeight": 40, "showToolbar": false},
    "SuperList": {"rowHeight": 40, "showColumnSettings": false},
    "SuperForm": {"labelPosition": "top", "columns": 1}
  }',
  '移动端优化模板', 3
) ON DUPLICATE KEY UPDATE 
  `component_overrides` = VALUES(`component_overrides`),
  `updated_time` = CURRENT_TIMESTAMP;

-- =====================================================================================
-- 数据迁移存储过程
-- =====================================================================================

DELIMITER $$

-- 迁移现有组件实例到优化格式的存储过程
CREATE PROCEDURE `MigrateToOptimizedFormat`()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE v_id BIGINT;
  DECLARE v_instance_id VARCHAR(100);
  DECLARE v_component_type VARCHAR(100);
  DECLARE v_full_config LONGTEXT;
  DECLARE v_default_config LONGTEXT;
  DECLARE v_custom_config LONGTEXT;
  
  -- 声明游标，用于遍历旧格式的组件实例
  DECLARE instance_cursor CURSOR FOR
    SELECT id, instance_id, component_type, config 
    FROM component_instances 
    WHERE deleted = 0;
    
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  
  -- 开始事务
  START TRANSACTION;
  
  OPEN instance_cursor;
  
  migration_loop: LOOP
    FETCH instance_cursor INTO v_id, v_instance_id, v_component_type, v_full_config;
    
    IF done THEN
      LEAVE migration_loop;
    END IF;
    
    -- 获取组件类型的默认配置
    SELECT default_config INTO v_default_config
    FROM component_type_defaults 
    WHERE component_type = v_component_type AND is_active = 1
    LIMIT 1;
    
    -- 如果找到默认配置，计算差异
    IF v_default_config IS NOT NULL THEN
      -- 这里应该调用一个计算配置差异的函数
      -- 由于MySQL的JSON处理能力有限，实际项目中建议在应用层处理
      SET v_custom_config = v_full_config; -- 临时处理，实际应计算差异
      
      -- 插入到优化表
      INSERT INTO component_instances_optimized (
        instance_id, component_type, config_version, custom_config,
        page_id, parent_id, sort_order, created_by, created_time, updated_by, updated_time
      )
      SELECT 
        v_instance_id, v_component_type, '1.0.0', v_custom_config,
        page_id, parent_id, sort_order, created_by, created_time, updated_by, updated_time
      FROM component_instances 
      WHERE id = v_id;
      
    END IF;
    
  END LOOP;
  
  CLOSE instance_cursor;
  
  -- 提交事务
  COMMIT;
  
  -- 记录迁移日志
  INSERT INTO component_config_history (
    instance_id, change_type, change_reason, created_time
  ) VALUES (
    'MIGRATION', 'migrate', '批量迁移到优化格式', NOW()
  );
  
END$$

-- 计算存储优化统计的存储过程
CREATE PROCEDURE `CalculateStorageOptimization`(IN stat_date DATE)
BEGIN
  DECLARE v_total_instances INT DEFAULT 0;
  DECLARE v_instances_with_custom INT DEFAULT 0;
  DECLARE v_avg_size_before INT DEFAULT 0;
  DECLARE v_avg_custom_size INT DEFAULT 0;
  DECLARE v_total_before BIGINT DEFAULT 0;
  DECLARE v_total_after BIGINT DEFAULT 0;
  DECLARE v_saved_bytes BIGINT DEFAULT 0;
  DECLARE v_saved_percentage DECIMAL(5,2) DEFAULT 0.00;
  
  -- 计算总实例数
  SELECT COUNT(*) INTO v_total_instances
  FROM component_instances_optimized 
  WHERE deleted = 0;
  
  -- 计算有自定义配置的实例数
  SELECT COUNT(*) INTO v_instances_with_custom
  FROM component_instances_optimized 
  WHERE custom_config IS NOT NULL AND deleted = 0;
  
  -- 模拟计算平均配置大小（实际项目中需要更精确的计算）
  SELECT 
    COALESCE(AVG(CHAR_LENGTH(CONCAT(COALESCE(default_config, '{}'), COALESCE(custom_config, '{}')))), 0),
    COALESCE(AVG(CHAR_LENGTH(COALESCE(custom_config, '{}'))), 0)
  INTO v_avg_size_before, v_avg_custom_size
  FROM component_instances_optimized cio
  LEFT JOIN component_type_defaults ctd ON cio.component_type = ctd.component_type
  WHERE cio.deleted = 0 AND ctd.is_active = 1;
  
  -- 计算总存储大小
  SET v_total_before = v_total_instances * v_avg_size_before;
  SET v_total_after = v_instances_with_custom * v_avg_custom_size + 
                      (SELECT SUM(CHAR_LENGTH(default_config)) FROM component_type_defaults WHERE is_active = 1);
  
  -- 计算节省的存储
  SET v_saved_bytes = v_total_before - v_total_after;
  
  IF v_total_before > 0 THEN
    SET v_saved_percentage = (v_saved_bytes / v_total_before) * 100;
  END IF;
  
  -- 插入或更新统计数据
  INSERT INTO storage_optimization_stats (
    stat_date, total_instances, instances_with_custom_config,
    avg_config_size_before, avg_custom_config_size,
    total_storage_before, total_storage_after,
    storage_saved_bytes, storage_saved_percentage
  ) VALUES (
    stat_date, v_total_instances, v_instances_with_custom,
    v_avg_size_before, v_avg_custom_size,
    v_total_before, v_total_after,
    v_saved_bytes, v_saved_percentage
  ) ON DUPLICATE KEY UPDATE
    total_instances = VALUES(total_instances),
    instances_with_custom_config = VALUES(instances_with_custom_config),
    avg_config_size_before = VALUES(avg_config_size_before),
    avg_custom_config_size = VALUES(avg_custom_config_size),
    total_storage_before = VALUES(total_storage_before),
    total_storage_after = VALUES(total_storage_after),
    storage_saved_bytes = VALUES(storage_saved_bytes),
    storage_saved_percentage = VALUES(storage_saved_percentage);
    
END$$

DELIMITER ;

-- =====================================================================================
-- 创建视图用于便捷查询
-- =====================================================================================

-- 1. 组件实例完整配置视图
CREATE OR REPLACE VIEW `v_component_instances_full_config` AS
SELECT 
  cio.id,
  cio.instance_id,
  cio.component_type,
  cio.config_version,
  cio.template_id,
  ctd.default_config,
  cio.custom_config,
  -- 这里应该是合并后的完整配置，但MySQL的JSON处理有限
  -- 实际项目中建议在应用层处理合并逻辑
  COALESCE(cio.custom_config, ctd.default_config) as merged_config,
  cio.page_id,
  cio.parent_id,
  cio.sort_order,
  cio.is_active,
  cio.created_time,
  cio.updated_time
FROM component_instances_optimized cio
LEFT JOIN component_type_defaults ctd 
  ON cio.component_type = ctd.component_type 
  AND cio.config_version = ctd.version
  AND ctd.is_active = 1
WHERE cio.deleted = 0;

-- 2. 存储优化趋势视图
CREATE OR REPLACE VIEW `v_storage_optimization_trend` AS
SELECT 
  stat_date,
  total_instances,
  storage_saved_percentage,
  storage_saved_bytes,
  total_storage_before,
  total_storage_after,
  LAG(storage_saved_percentage) OVER (ORDER BY stat_date) as prev_saved_percentage,
  (storage_saved_percentage - LAG(storage_saved_percentage) OVER (ORDER BY stat_date)) as percentage_change
FROM storage_optimization_stats
ORDER BY stat_date DESC;

-- =====================================================================================
-- 索引优化建议
-- =====================================================================================

-- 1. 复合索引优化查询性能
CREATE INDEX `idx_instance_type_template` ON `component_instances_optimized` (`component_type`, `template_id`);
CREATE INDEX `idx_page_type_order` ON `component_instances_optimized` (`page_id`, `component_type`, `sort_order`);

-- 2. JSON字段索引（MySQL 5.7+支持）
-- ALTER TABLE `component_type_defaults` ADD INDEX `idx_config_theme` ((JSON_EXTRACT(`default_config`, '$.theme')));
-- ALTER TABLE `component_instances_optimized` ADD INDEX `idx_custom_theme` ((JSON_EXTRACT(`custom_config`, '$.theme')));

-- =====================================================================================
-- 示例查询
-- =====================================================================================

-- 查询某页面所有组件的完整配置
-- SELECT * FROM v_component_instances_full_config WHERE page_id = 1;

-- 查询存储优化效果
-- SELECT * FROM v_storage_optimization_trend ORDER BY stat_date DESC LIMIT 30;

-- 查询最常被修改的配置项（需要在应用层分析JSON）
-- SELECT component_type, JSON_KEYS(custom_config) as modified_keys 
-- FROM component_instances_optimized 
-- WHERE custom_config IS NOT NULL;

-- =====================================================================================
-- 维护和清理
-- =====================================================================================

-- 定期执行存储优化统计（建议每日执行）
-- CALL CalculateStorageOptimization(CURDATE());

-- 清理过期的配置历史（保留6个月）
-- DELETE FROM component_config_history WHERE created_time < DATE_SUB(NOW(), INTERVAL 6 MONTH);