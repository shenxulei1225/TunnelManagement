# 页面设计器租户支持

## 概述

页面设计器功能已完全支持多租户架构，所有相关的数据库表和代码都已配置为支持租户隔离。

## 数据库表结构

### 1. 页面设计表 (system_page_design)

```sql
CREATE TABLE `system_page_design` (
  `id` varchar(64) NOT NULL COMMENT '页面设计ID',
  `page_name` varchar(100) NOT NULL COMMENT '页面名称',
  `page_path` varchar(200) NOT NULL COMMENT '页面路径',
  `menu_title` varchar(100) DEFAULT NULL COMMENT '菜单标题',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `description` varchar(500) DEFAULT NULL COMMENT '页面描述',
  `page_type` varchar(50) DEFAULT NULL COMMENT '页面类型',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态',
  `build_status` varchar(20) DEFAULT NULL COMMENT '构建状态',
  `components` text DEFAULT NULL COMMENT '组件配置(JSON)',
  `config` text DEFAULT NULL COMMENT '页面配置(JSON)',
  `page_code` longtext DEFAULT NULL COMMENT '页面代码',
  `route_config` text DEFAULT NULL COMMENT '路由配置(JSON)',
  `menu_config` text DEFAULT NULL COMMENT '菜单配置(JSON)',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_page_path` (`page_path`),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面设计表';
```

### 2. 页面构建记录表 (system_page_build_record)

```sql
CREATE TABLE `system_page_build_record` (
  `id` varchar(64) NOT NULL COMMENT '构建记录ID',
  `page_design_id` varchar(64) NOT NULL COMMENT '页面设计ID',
  `build_id` varchar(64) NOT NULL COMMENT '构建ID',
  `build_status` varchar(20) NOT NULL DEFAULT 'building' COMMENT '构建状态',
  `progress` int DEFAULT 0 COMMENT '构建进度(0-100)',
  `message` varchar(500) DEFAULT NULL COMMENT '构建消息',
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '完成时间',
  `error_message` text DEFAULT NULL COMMENT '错误信息',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_build_id` (`build_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_page_design_id` (`page_design_id`),
  KEY `idx_build_status` (`build_status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面构建记录表';
```

### 3. 页面部署记录表 (system_page_deploy_record)

```sql
CREATE TABLE `system_page_deploy_record` (
  `id` varchar(64) NOT NULL COMMENT '部署记录ID',
  `page_design_id` varchar(64) NOT NULL COMMENT '页面设计ID',
  `deploy_status` varchar(20) NOT NULL DEFAULT 'deploying' COMMENT '部署状态',
  `deploy_path` varchar(200) DEFAULT NULL COMMENT '部署路径',
  `deploy_message` varchar(500) DEFAULT NULL COMMENT '部署消息',
  `deploy_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '部署时间',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_page_design_id` (`page_design_id`),
  KEY `idx_deploy_status` (`deploy_status`),
  KEY `idx_deploy_time` (`deploy_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面部署记录表';
```

### 4. 页面模板表 (system_page_template)

```sql
CREATE TABLE `system_page_template` (
  `id` varchar(64) NOT NULL COMMENT '模板ID',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_code` varchar(100) NOT NULL COMMENT '模板代码',
  `template_type` varchar(50) NOT NULL COMMENT '模板类型',
  `description` varchar(500) DEFAULT NULL COMMENT '模板描述',
  `template_config` text DEFAULT NULL COMMENT '模板配置(JSON)',
  `preview_image` varchar(200) DEFAULT NULL COMMENT '预览图片',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '状态',
  `sort` int DEFAULT 0 COMMENT '排序',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_code_tenant` (`template_code`, `tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_template_type` (`template_type`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='页面模板表';
```

## 代码架构

### 数据对象 (DO)

所有页面设计器相关的数据对象都继承自 `TenantBaseDO`，自动支持租户隔离：

- `PageDesignDO` - 页面设计数据对象
- `PageBuildRecordDO` - 页面构建记录数据对象
- `PageDeployRecordDO` - 页面部署记录数据对象
- `PageTemplateDO` - 页面模板数据对象

### 服务层 (Service)

服务层实现使用 `TenantBaseDO` 的自动租户隔离功能：

- `PageDesignerService` - 页面设计器服务
- `PageTemplateService` - 页面模板服务

### 控制器层 (Controller)

控制器层提供RESTful API接口：

- `PageDesignerController` - 页面设计器管理
- `PageTemplateController` - 页面模板管理

## 租户隔离特性

### 1. 自动租户ID设置

所有继承 `TenantBaseDO` 的实体在创建时会自动设置当前租户ID。

### 2. 自动租户过滤

所有查询操作会自动添加租户ID过滤条件，确保数据隔离。

### 3. 租户唯一性约束

页面模板表使用 `(template_code, tenant_id)` 联合唯一索引，确保同一租户内模板代码唯一。

### 4. 系统默认模板

租户ID为1的模板作为系统默认模板，所有租户都可以使用。

## API接口

### 页面设计器接口

- `POST /system/page-design/create` - 创建页面设计
- `PUT /system/page-design/update` - 更新页面设计
- `DELETE /system/page-design/delete` - 删除页面设计
- `GET /system/page-design/get` - 获取页面设计
- `GET /system/page-design/list` - 获取页面设计列表

### 页面模板接口

- `POST /system/page-template/create` - 创建页面模板
- `PUT /system/page-template/update` - 更新页面模板
- `DELETE /system/page-template/delete` - 删除页面模板
- `GET /system/page-template/get` - 获取页面模板
- `GET /system/page-template/list` - 获取页面模板列表
- `GET /system/page-template/list-by-type` - 根据类型获取模板列表

## 权限控制

所有接口都配置了相应的权限控制：

- `system:page-design:create` - 创建页面设计权限
- `system:page-design:update` - 更新页面设计权限
- `system:page-design:delete` - 删除页面设计权限
- `system:page-design:query` - 查询页面设计权限
- `system:page-template:create` - 创建页面模板权限
- `system:page-template:update` - 更新页面模板权限
- `system:page-template:delete` - 删除页面模板权限
- `system:page-template:query` - 查询页面模板权限

## 使用说明

1. **创建页面设计**：调用创建接口，系统会自动设置当前租户ID
2. **查询页面设计**：只能查询到当前租户的页面设计
3. **更新页面设计**：只能更新当前租户的页面设计
4. **删除页面设计**：只能删除当前租户的页面设计
5. **模板管理**：每个租户可以创建自己的模板，也可以使用系统默认模板

## 注意事项

1. 所有数据操作都会自动进行租户隔离
2. 系统默认模板（租户ID=1）对所有租户可见
3. 租户模板代码在同一租户内必须唯一
4. 删除操作会进行软删除，不会物理删除数据
5. 查询操作会自动按创建时间倒序排列 