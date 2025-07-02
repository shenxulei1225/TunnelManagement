# 开发文档目录

> 本目录包含项目开发相关的技术文档、规范指南和最佳实践。

## 📂 文档结构

### 🛠️ 基础开发指导
- [开发指导原则](./basic/development-guidelines.md) - 项目开发的核心规范和最佳实践
- [Element Plus 组件参考](./basic/element-plus-components-reference.md) - 前端组件使用指南
- [菜单配置模板](./basic/menu-template.md) - 统一的菜单配置规范

### 🎨 UI/UX 开发规范
- [标签颜色系统开发文档](./tag-color-system-development.md) - 统一标签颜色管理系统实现
- [操作日志控件开发文档](./operation-log-development.md) - 通用操作日志控件的技术实现
- [标签颜色系统设计文档](../design/tag-color-system-design.md) - 标签颜色系统的设计理念
- [操作日志控件设计文档](../design/operation-log-design.md) - 操作日志控件的设计规范

### 📋 核心开发指南
- [核心开发指南](./core-development-guide.md) - 项目核心功能开发指导
- [完整工作流程指南](./complete-workflow-guide.md) - 端到端开发流程
- [常见问题指导](./common-issues-guide.md) - 开发中的常见问题和解决方案

### 🏗️ 架构和设计
- [多入口导入架构](./multi-entry-import-architecture.md) - 多平台资源导入架构设计
- [平台兼容性设计](./platform-compatibility-design.md) - 跨平台兼容性解决方案
- [通用文件管理器API规范](./universal-file-manager-api-spec.md) - 文件管理统一接口

### 📱 功能模块开发
- [文件管理进度跟踪](./file-manager-progress.md) - 文件管理功能开发进度
- [文件管理分类集成](./file-management-category-integration.md) - 文件分类功能集成
- [UMG文件格式指南](./umg-file-formats-guide.md) - UMG文件处理指南

### 🔧 工具和配置
- [UI转换器菜单设置](./ui-converter-menu-setup.md) - 转换器菜单配置
- [ESLint和代码质量](./eslint-code-quality-guide.md) - 代码质量规范
- [TypeScript错误处理](./typescript-error-handling.md) - TS错误解决方案

---

## 🎯 快速导航

### 新手入门
1. 📖 [开发指导原则](./basic/development-guidelines.md) - 了解项目规范
2. 📱 [Element Plus 组件参考](./basic/element-plus-components-reference.md) - 学习组件使用
3. 📋 [菜单配置模板](./basic/menu-template.md) - 配置页面菜单
4. 🎨 [标签颜色系统开发文档](./tag-color-system-development.md) - UI组件规范

### 常见问题解决
- 🐛 Element Plus组件报错 → [标签颜色系统开发文档](./tag-color-system-development.md)
- 🔧 TypeScript类型错误 → [TypeScript错误处理](./typescript-error-handling.md)
- 📋 菜单配置问题 → [菜单配置模板](./basic/menu-template.md)
- 🗂️ 文件管理功能 → [通用文件管理器API规范](./universal-file-manager-api-spec.md)

### 架构设计参考
- 🏗️ 多平台导入 → [多入口导入架构](./multi-entry-import-architecture.md)
- 🌐 跨平台兼容 → [平台兼容性设计](./platform-compatibility-design.md)
- 🎨 UI设计规范 → [标签颜色系统设计文档](../design/tag-color-system-design.md)

---

## 🎯 菜单配置参数
每生成一个新的开发工具页面时，需要配置以下菜单参数：

```json
{
  "name": "开发工具名称",
  "type": 2,
  "sort": 2000,
  "parentId": 1,
  "path": "dev-tool-path",
  "icon": "ep:tools",
  "component": "development/tool-name/index",
  "componentName": "DevToolName",
  "permission": "dev:tool:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

---



*本目录持续更新，如有疑问请联系开发团队。* 