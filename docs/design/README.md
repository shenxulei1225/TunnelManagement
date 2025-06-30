# 设计文档目录

> 本目录包含项目的设计相关文档，包括架构设计、UI设计、交互设计等。

## 📂 文档结构

### 🎨 用户界面设计
- [标签颜色系统设计](./tag-color-system-design.md) - 统一的标签颜色管理系统
- [Figma样式系统集成](./figma-style-system-integration.md) - 与Figma设计系统的集成方案
- [UX设计师架构](./figma-style-ux-designer-architecture.md) - UX设计工具的架构设计

### 🏗️ 系统架构设计
- [Figma精确架构复制](./figma-exact-architecture-replication.md) - Figma组件架构的复制方案
- [资源导入总体计划](./resource-import-master-plan.md) - 设计资源导入的整体规划

### 📋 快速指南
- [UX设计师快速设置指南](./ux-designer-quick-setup-guide.md) - 快速开始使用UX设计工具
- [UX设计师UI转换器设计](./ux-designer-ui-converter-design.md) - UI转换器的设计方案

### 📁 资源导入设计
- [资源导入](./resource-import/) - 设计资源导入相关的详细设计文档

---

## 🎯 菜单配置参数
每生成一个新的设计工具页面时，需要配置以下菜单参数：

```json
{
  "name": "设计工具名称",
  "type": 2,
  "sort": 1000,
  "parentId": 1,
  "path": "design-tool-path",
  "icon": "ep:paint-brush",
  "component": "design/tool-name/index",
  "componentName": "DesignToolName",
  "permission": "design:tool:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

---

## 📝 文档维护

### 更新规范
- 新增设计功能时同步更新相关文档
- 设计变更时及时更新文档版本
- 定期检查文档的准确性和完整性

### 贡献指南
- 遵循项目文档标准
- 使用清晰的标题和结构
- 提供充分的示例和说明
- 包含必要的图表和截图

---

*本目录持续更新，如有疑问请联系设计团队。* 