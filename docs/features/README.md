# 功能模块文档

本目录包含各个功能模块的完整文档，每个模块都有独立的设计、开发、测试文档。

## 功能模块列表

### 1. 业务模型管理模块 ✅

**状态**: 已完成  
**路径**: `docs/features/business-model-management/`

**功能特性**:
- ✅ 业务模型CRUD操作
- ✅ 拖拽排序功能
- ✅ 状态管理（启用/禁用）
- ✅ 只读权限控制
- ✅ 模型类型区分（系统/自定义）
- ✅ 响应式UI设计

**文档结构**:
```
business-model-management/
├── README.md                    # 模块概述
├── cache-analysis.md           # 缓存处理分析
├── design/                      # 设计文档
│   ├── architecture.md          # 架构设计
│   ├── database-schema.md       # 数据库设计
│   └── api-design.md           # API设计
├── development/                 # 开发文档
│   ├── backend-implementation.md # 后端实现
│   ├── frontend-implementation.md # 前端实现
│   └── implementation-summary.md # 实现总结
├── testing/                    # 测试文档
│   ├── test-cases.md          # 测试用例
│   └── api-testing.http       # API测试文件
└── assets/                     # 资源文件
    ├── screenshots/            # 截图
    └── diagrams/              # 图表
```

**快速链接**:
- [模块概述](./business-model-management/README.md)
- [缓存处理分析](./business-model-management/cache-analysis.md)
- [设计文档](./business-model-management/design/)
- [开发文档](./business-model-management/development/)
- [测试文档](./business-model-management/testing/)

### 2. 菜单管理
- **路径**: `menu-management/`
- **描述**: 系统菜单的管理功能，包括菜单创建、权限配置等
- **状态**: 🔄 开发中
- **文档**: [查看文档](./menu-management/)

### 3. 动态业务
- **路径**: `dynamic-business/`
- **描述**: 动态业务系统的核心功能
- **状态**: 🔄 开发中
- **文档**: [查看文档](./dynamic-business/)

### 4. 文件管理
- **路径**: `file-organization/`
- **描述**: 文件组织和管理的相关功能
- **状态**: 🔄 开发中
- **文档**: [查看文档](./file-organization/)

### 5. Element Plus 集成
- **路径**: `element-plus-integration/`
- **描述**: Element Plus UI 库的集成和使用
- **状态**: 🔄 开发中
- **文档**: [查看文档](./element-plus-integration/)

### 6. 通用组件
- **路径**: `common-components/`
- **描述**: 系统通用组件的开发和使用
- **状态**: 🔄 开发中
- **文档**: [查看文档](./common-components/)

## 文档结构

每个模块的文档都遵循统一的结构：

```
{module-name}/
├── README.md                    # 模块概述
├── design/                      # 设计文档
│   ├── architecture.md          # 架构设计
│   ├── database-schema.md       # 数据库设计
│   └── api-design.md           # API设计
├── development/                 # 开发文档
│   ├── backend-implementation.md # 后端实现
│   ├── frontend-implementation.md # 前端实现
│   └── deployment-guide.md     # 部署指南
├── testing/                    # 测试文档
│   ├── test-cases.md          # 测试用例
│   ├── api-testing.md         # API测试
│   └── ui-testing.md          # UI测试
└── assets/                     # 资源文件
    ├── screenshots/            # 截图
    └── diagrams/              # 图表
```

## 文档规范

### 1. 命名规范
- 文件名使用 kebab-case
- 目录名使用 kebab-case
- 中文文件名使用拼音或英文

### 2. 内容规范
- 使用 Markdown 格式
- 包含完整的代码示例
- 提供详细的配置说明
- 包含测试用例和验证方法

### 3. 更新规范
- 功能变更时同步更新文档
- 添加版本更新日志
- 保持文档的时效性

## 快速导航

### 按功能分类
- **核心功能**: 业务模型管理、菜单管理
- **UI组件**: Element Plus 集成、通用组件
- **业务功能**: 动态业务、文件管理

### 按状态分类
- **已完成**: 业务模型管理
- **开发中**: 菜单管理、动态业务、文件管理、Element Plus 集成、通用组件

## 贡献指南

### 添加新模块
1. 在 `features/` 目录下创建新模块目录
2. 按照标准结构创建文档
3. 更新本 README.md 文件
4. 提交代码和文档

### 更新现有模块
1. 修改对应的文档文件
2. 更新版本信息
3. 添加变更日志
4. 提交更新

### 文档审查
- 确保文档内容准确完整
- 验证代码示例可执行
- 检查链接和引用正确
- 保持格式统一

## 联系方式

如有文档相关问题，请联系：
- 开发团队: dev-team@company.com
- 文档维护: docs@company.com 