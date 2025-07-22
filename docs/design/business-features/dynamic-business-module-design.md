# 动态业务模块设计文档

> **📋 Dynamic Business Module Design Document**  
> 动态业务模块的完整概念定义、流程设计和实现方案  
> 最后更新：2025-01-20

---

## 🎯 核心概念定义

### 业务模块 (Business Module)
```
业务模块 = 业务领域 + 页面集合
├── 设施管理模块
│   ├── 设施数据管理页面
│   ├── 设施仪表盘页面
│   └── 设施实时监控页面
├── 巡检模块
│   ├── 巡检任务管理页面
│   ├── 巡检路线规划页面
│   └── 巡检报告页面
└── 其他业务模块...
```

### 业务模型 (Business Model)
```
业务模型 = 页面模板 + 数据配置
├── 设施数据管理模型
│   ├── 数据表结构
│   ├── 字段定义
│   └── 业务规则
├── 设施仪表盘模型
│   ├── 数据源配置
│   ├── 图表配置
│   └── 展示规则
└── 设施实时监控模型
    ├── 监控指标
    ├── 告警规则
    └── 数据流配置
```

### 页面模板 (Page Template)
```
页面模板 = 预定义的页面结构 + 可配置参数
├── 数据管理模板
│   ├── 列表展示
│   ├── 搜索筛选
│   └── 批量操作
├── 仪表盘模板
│   ├── 图表展示
│   ├── 指标统计
│   └── 实时更新
└── 监控模板
    ├── 状态监控
    ├── 告警管理
    └── 日志查看
```

---

## 🔄 完整业务流程

### 1. 创建业务模块 (Business Module)
```
创建业务模块
├── 设施管理模块
│   ├── 模块编码: facility_management
│   ├── 模块名称: 设施管理
│   └── 模块描述: 设施信息管理、监控、维护
├── 巡检模块
│   ├── 模块编码: inspection_management
│   ├── 模块名称: 巡检管理
│   └── 模块描述: 巡检任务、路线、报告管理
└── 其他业务模块...
```

### 2. 选择页面模板 (Template Selection)
```
为业务模块选择页面模板
├── 设施管理模块
│   ├── 选择"数据管理"模板 → 生成"设施数据管理页面"
│   ├── 选择"仪表盘"模板 → 生成"设施仪表盘页面"
│   └── 选择"实时监控"模板 → 生成"设施实时监控页面"
├── 巡检模块
│   ├── 选择"任务管理"模板 → 生成"巡检任务管理页面"
│   ├── 选择"路线规划"模板 → 生成"巡检路线规划页面"
│   └── 选择"报告管理"模板 → 生成"巡检报告页面"
└── 其他页面...
```

### 3. 页面配置 (Page Configuration)
```
配置页面参数
├── 数据源配置
│   ├── 关联数据表
│   ├── 字段映射
│   └── 查询条件
├── 界面配置
│   ├── 布局设置
│   ├── 组件配置
│   └── 样式定制
└── 功能配置
    ├── 权限设置
    ├── 操作按钮
    └── 业务规则
```

### 4. 生成页面 (Page Generation)
```
生成页面代码
├── 前端页面生成
│   ├── Vue组件代码
│   ├── 路由配置
│   └── 样式文件
├── 后端接口生成
│   ├── Controller接口
│   ├── Service服务
│   └── 数据访问层
└── 数据库表生成
    ├── 数据表结构
    ├── 索引配置
    └── 约束规则
```

### 5. 重复2-4过程
```
为业务模块生成完整页面集合
├── 设施管理模块
│   ├── 设施数据管理页面 ✓
│   ├── 设施仪表盘页面 ✓
│   └── 设施实时监控页面 ✓
├── 巡检模块
│   ├── 巡检任务管理页面 ✓
│   ├── 巡检路线规划页面 ✓
│   └── 巡检报告页面 ✓
└── 其他页面...
```

### 6. 一键部署 (One-Click Deployment)
```
部署业务模块
├── 菜单配置
│   ├── 自动生成菜单结构
│   ├── 配置菜单权限
│   └── 设置菜单路由
├── 系统集成
│   ├── 用户权限集成
│   ├── 日志系统集成
│   └── 监控系统集成
└── 最终验证
    ├── 功能测试
    ├── 性能测试
    └── 用户体验测试
```

---

## 🎨 用户界面设计

### 1. 业务模块创建页面
```
┌─────────────────────────────────────┐
│ 创建业务模块                        │
├─────────────────────────────────────┤
│ 模块基础信息                        │
│ ├── 模块编码: [facility_management] │
│ ├── 模块名称: [设施管理]            │
│ └── 模块描述: [设施信息管理、监控、维护] │
│                                     │
│ 模块分类                            │
│ ├── 业务领域: [设备管理 ▼]          │
│ ├── 模块类型: [核心业务模块]        │
│ └── 优先级: [高 ▼]                 │
│                                     │
│ [下一步：选择页面模板]               │
└─────────────────────────────────────┘
```

### 2. 页面模板选择页面
```
┌─────────────────────────────────────┐
│ 为"设施管理"模块选择页面模板        │
├─────────────────────────────────────┤
│ 推荐模板                            │
│ ┌─────────┐ ┌─────────┐ ┌─────────┐ │
│ │数据管理 │ │仪表盘   │ │实时监控 │ │
│ │★★★★★  │ │★★★★☆  │ │★★★★☆  │ │
│ │适合设施 │ │适合设施 │ │适合设施 │ │
│ │数据管理 │ │数据展示 │ │状态监控 │ │
│ └─────────┘ └─────────┘ └─────────┘ │
│                                     │
│ 模板分类                            │
│ ├── 数据管理模板                    │
│ │   ├── 数据列表模板                │
│ │   ├── 数据表单模板                │
│ │   └── 数据详情模板                │
│ ├── 监控分析模板                    │
│ │   ├── 仪表盘模板                  │
│ │   ├── 实时监控模板                │
│ │   └── 趋势分析模板                │
│ └── 业务管理模板                    │
│     ├── 任务管理模板                │
│     ├── 流程管理模板                │
│     └── 报告管理模板                │
└─────────────────────────────────────┘
```

### 3. 页面配置页面
```
┌─────────────────────────────────────┐
│ 配置"设施数据管理"页面              │
├─────────────────────────────────────┤
│ 页面基础信息                        │
│ ├── 页面标题: [设施数据管理]        │
│ ├── 页面描述: [设施信息管理列表]    │
│ └── 页面图标: [选择图标]            │
│                                     │
│ 数据源配置                          │
│ ├── 数据表: [facility_info]         │
│ ├── 主键字段: [facility_id]         │
│ └── 查询条件: [配置条件]            │
│                                     │
│ 字段配置                            │
│ ├── 设施编码 (string, 必填)         │
│ ├── 设施名称 (string, 必填)         │
│ ├── 设施类型 (select, 必填)         │
│ ├── 设施状态 (status, 必填)         │
│ ├── 设施位置 (string, 可选)         │
│ └── 维护日期 (date, 可选)           │
│                                     │
│ 界面配置                            │
│ ├── 布局方式: [表格布局]            │
│ ├── 搜索功能: [启用]                │
│ ├── 分页功能: [启用]                │
│ └── 批量操作: [启用]                │
└─────────────────────────────────────┘
```

---

## 🏗️ 技术实现架构

### 1. 后端模块结构
```
cheers-module-dynamic-business/
├── 业务模块管理 (Business Module)
│   ├── DynamicBusinessModuleDO
│   ├── DynamicBusinessModuleService
│   └── DynamicBusinessModuleController
├── 页面模板管理 (Page Template)
│   ├── PageTemplateDO
│   ├── PageTemplateService
│   └── PageTemplateController
├── 页面配置管理 (Page Configuration)
│   ├── PageConfigDO
│   ├── PageConfigService
│   └── PageConfigController
└── 代码生成管理 (Code Generation)
    ├── CodeGeneratorService
    ├── TemplateEngine
    └── DeploymentService
```

### 2. 前端页面结构
```
tunnel-management-ui/src/views/dynamic-business/
├── module/
│   ├── index.vue (业务模块列表)
│   └── form.vue (业务模块创建/编辑)
├── template/
│   ├── index.vue (模板选择页面)
│   ├── detail.vue (模板详情页面)
│   └── preview.vue (模板预览页面)
├── page/
│   ├── config.vue (页面配置页面)
│   ├── preview.vue (页面预览页面)
│   └── generate.vue (页面生成页面)
└── deploy/
    ├── index.vue (业务模块部署页面)
    └── result.vue (部署结果页面)
```

### 3. 数据库表结构
```sql
-- 业务模块表
CREATE TABLE `dynamic_business_module` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模块ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `code` varchar(100) NOT NULL COMMENT '模块编码',
    `name` varchar(100) NOT NULL COMMENT '模块名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `category` varchar(50) NOT NULL COMMENT '模块分类',
    `priority` tinyint NOT NULL DEFAULT '1' COMMENT '优先级',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_code` (`tenant_id`, `code`)
) COMMENT='业务模块表';

-- 页面模板表
CREATE TABLE `dynamic_page_template` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    `code` varchar(100) NOT NULL COMMENT '模板编码',
    `name` varchar(100) NOT NULL COMMENT '模板名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `category` varchar(50) NOT NULL COMMENT '模板分类',
    `type` varchar(50) NOT NULL COMMENT '模板类型',
    `config_schema` text COMMENT '配置模式（JSON格式）',
    `components` text COMMENT '组件配置（JSON格式）',
    `data_source` text COMMENT '数据源配置（JSON格式）',
    `permissions` text COMMENT '权限配置（JSON格式）',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) COMMENT='页面模板表';

-- 页面配置表
CREATE TABLE `dynamic_page_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `module_code` varchar(100) NOT NULL COMMENT '业务模块编码',
    `template_code` varchar(100) NOT NULL COMMENT '页面模板编码',
    `page_code` varchar(100) NOT NULL COMMENT '页面编码',
    `page_name` varchar(100) NOT NULL COMMENT '页面名称',
    `page_description` varchar(500) DEFAULT NULL COMMENT '页面描述',
    `data_source_config` text COMMENT '数据源配置（JSON格式）',
    `field_config` text COMMENT '字段配置（JSON格式）',
    `layout_config` text COMMENT '布局配置（JSON格式）',
    `function_config` text COMMENT '功能配置（JSON格式）',
    `permission_config` text COMMENT '权限配置（JSON格式）',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_module_page` (`tenant_id`, `module_code`, `page_code`)
) COMMENT='页面配置表';
```

---

## 🎯 业务建模概念对应

### 1. 业务建模的定义
```
业务建模 = 将业务需求转换为系统模型
├── 业务模块建模 (Business Module Modeling)
│   ├── 识别业务领域
│   ├── 定义模块边界
│   └── 确定模块关系
├── 页面建模 (Page Modeling)
│   ├── 选择页面模板
│   ├── 配置页面参数
│   └── 生成页面模型
└── 数据建模 (Data Modeling)
    ├── 定义数据结构
    ├── 配置字段属性
    └── 设置业务规则
```

### 2. 模板选择与业务建模的关系
```
模板选择 = 业务建模的核心步骤
├── 模板选择是业务建模的起点
│   ├── 选择合适的业务模型模板
│   ├── 配置业务模型参数
│   └── 生成具体的业务模型
├── 业务建模通过模板实现
│   ├── 预定义的业务模型模板
│   ├── 可配置的模型参数
│   └── 自动化的模型生成
└── 建模结果体现为页面
    ├── 页面是业务模型的实例
    ├── 页面配置是模型参数
    └── 页面功能是模型行为
```

---

## 📈 发展路线图

### 第一阶段：基础模板库（当前）
- **目标**：建立基础的模板分类体系
- **重点**：按业务场景和功能类型分类
- **推荐策略**：手动选择 + 基础搜索

### 第二阶段：丰富模板库（中期）
- **目标**：增加更多行业模板
- **重点**：电商、ERP、CRM等行业模板
- **推荐策略**：热门推荐 + 相似推荐

### 第三阶段：智能推荐（长期）
- **目标**：基于AI的智能推荐
- **重点**：业务特征匹配 + 用户行为学习
- **推荐策略**：个性化智能推荐

---

## 💡 核心优势

1. **零代码开发**：用户只需选择模板和配置数据，无需编写代码
2. **业务建模驱动**：通过模板选择实现业务建模，降低技术门槛
3. **模块化设计**：业务模块独立，便于维护和扩展
4. **快速部署**：自动生成完整的页面和功能，立即可用
5. **灵活定制**：支持模板的个性化配置和扩展

---

*本文档持续更新，如有疑问请联系动态业务模块开发团队。* 