# �� 项目文档中心

> **TunnelManagement** 项目完整文档体系  
> **版本**: v2.0.0 | **最后更新**: 2025-01-26

---

## 🚀 快速导航

### 👥 角色导向导航

#### 🆕 新手入门
- [📖 项目概览](./structure/project-structure.md) - 了解项目整体架构
- [🛠️ 开发指导原则](./development/basic/development-guidelines.md) - 核心开发规范
- [📱 Element Plus 组件参考](./development/basic/element-plus-components-reference.md) - 前端组件使用
- [📋 菜单配置模板](./development/basic/menu-template.md) - 页面菜单配置

#### 🎨 设计师专区
- [🎨 标签颜色系统设计](./design/tag-color-system-design.md) - UI颜色规范体系
- [🎯 UX设计师快速指南](./design/ux-designer-quick-setup-guide.md) - 设计工具快速上手
- [📋 Figma样式系统集成](./design/figma-style-system-integration.md) - 设计稿导入规范

#### 💻 开发工程师专区
- [💻 标签颜色系统开发](./development/tag-color-system-development.md) - 颜色管理技术实现
- [🏗️ 核心开发指南](./development/core-development-guide.md) - 项目核心功能开发
- [🔧 常见问题解决](./development/common-issues-guide.md) - 开发问题排查手册

#### 🔧 运维管理员专区
- [🗄️ 数据库管理](./database-management.md) - 数据库运维指南
- [🔄 跨平台数据库恢复](./cross-platform-database-restore.md) - 数据恢复方案
- [📊 Git备份策略](./git-backup-strategy.md) - 代码备份策略

---

## 📂 文档体系结构

### 🎨 设计文档 (`/design/`)
> **目标用户**: 设计师、产品经理、UI/UX专家

```
design/
├── 📋 README.md                           # 设计文档总览
├── 🎨 tag-color-system-design.md         # 标签颜色系统设计 ⭐
├── 🎯 ux-designer-quick-setup-guide.md   # UX设计师快速指南
├── 📋 figma-style-system-integration.md  # Figma集成方案
├── 🏗️ figma-exact-architecture-replication.md # 架构复制方案
├── 📋 resource-import-master-plan.md     # 资源导入总体规划
└── resource-import/                       # 资源导入详细设计
    ├── 📊 data-processing-architecture.md
    ├── 🎯 figma-import-design.md
    └── ⚡ performance-optimization.md
```

### 💻 开发文档 (`/development/`)
> **目标用户**: 前端开发、后端开发、全栈工程师

```
development/
├── 📋 README.md                           # 开发文档总览
├── 🎨 tag-color-system-development.md    # 标签颜色系统开发 ⭐
├── 🏗️ core-development-guide.md          # 核心开发指南
├── 📋 complete-workflow-guide.md         # 完整工作流程
├── 🔧 common-issues-guide.md             # 常见问题解决
├── 🏗️ multi-entry-import-architecture.md # 多入口导入架构
├── 🌐 platform-compatibility-design.md   # 平台兼容性设计
└── basic/                                 # 基础开发指导
    ├── 📋 README.md                       # 基础指导总览
    ├── 🛠️ development-guidelines.md       # 开发指导原则
    ├── 📱 element-plus-components-reference.md # Element Plus参考
    └── 📋 menu-template.md                # 菜单配置模板
```

### 📊 标准规范 (`/standards/`)
> **目标用户**: 团队协作、质量保证、项目管理

```
standards/
├── 📋 documentation-standards-summary.md  # 文档标准总结
└── 其他规范文档...
```

### 📁 项目结构 (`/structure/`)
> **目标用户**: 新成员、架构师、技术负责人

```
structure/
└── 📋 project-structure.md               # 项目结构说明
```

### 📊 总结文档 (`/summary/`)
> **目标用户**: 决策者、架构师、技术总监

```
summary/
├── 🌐 platform-compatibility-summary.md   # 平台兼容性总结
├── 🎯 ui-converter-concept-summary.md     # UI转换器概念总结
└── 🔧 universal-component-system-implementation.md # 通用组件系统
```

---

## 🎯 文档特色功能

### ⭐ 重点推荐文档

#### 🔥 最新发布
- **[标签颜色系统设计](./design/tag-color-system-design.md)** - 完整的UI颜色管理解决方案
- **[标签颜色系统开发](./development/tag-color-system-development.md)** - 技术实现和开发工具化

#### 🛠️ 开发必读
- **[开发指导原则](./development/basic/development-guidelines.md)** - 项目开发核心规范
- **[Element Plus 组件参考](./development/basic/element-plus-components-reference.md)** - 前端组件详细使用指南
- **[核心开发指南](./development/core-development-guide.md)** - 核心功能开发详解

#### 🎨 设计必读
- **[UX设计师快速指南](./design/ux-designer-quick-setup-guide.md)** - 设计工具快速上手
- **[Figma样式系统集成](./design/figma-style-system-integration.md)** - 设计稿集成规范

### 🔍 智能索引

#### 按问题类型索引
- **🐛 错误修复**: Element Plus组件报错 → [标签颜色系统开发](./development/tag-color-system-development.md)
- **🔧 功能开发**: 新页面开发 → [菜单配置模板](./development/basic/menu-template.md)
- **🎨 UI设计**: 颜色规范 → [标签颜色系统设计](./design/tag-color-system-design.md)
- **🗂️ 文件管理**: 文件上传下载 → [通用文件管理器API规范](./development/universal-file-manager-api-spec.md)

#### 按技术栈索引
- **Vue 3 + TypeScript**: [开发指导原则](./development/basic/development-guidelines.md)
- **Element Plus**: [组件参考文档](./development/basic/element-plus-components-reference.md)
- **Figma 集成**: [Figma导入设计](./design/resource-import/figma-import-design.md)
- **多租户架构**: [开发指导原则 - 多租户部分](./development/basic/development-guidelines.md#多租户开发规范)

---

## 🎯 菜单配置标准

每个新页面/功能都需要配置标准菜单参数：

```json
{
  "name": "功能名称",
  "type": 2,
  "sort": 1000,
  "parentId": 1,
  "path": "feature-path",
  "icon": "ep:icon-name",
  "component": "module/feature/index",
  "componentName": "FeatureName",
  "permission": "module:feature:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

详细配置说明参见：[菜单配置模板](./development/basic/menu-template.md)

---

## 📈 文档版本管理

### 版本说明
- **v2.0.0** (2025-01-26): 标签颜色系统完整版本，优化文档结构
- **v1.5.0** (2025-01-20): 添加多租户开发规范
- **v1.0.0** (2025-01-15): 基础文档体系建立

### 更新频率
- **实时更新**: 开发进展文档随功能开发同步更新
- **每周汇总**: 问题解决方案和最佳实践总结
- **月度回顾**: 架构设计和规范标准评估更新

---

## 🤝 文档贡献指南

### 写作规范
- 使用清晰的 Markdown 格式
- 遵循项目文档标准
- 提供完整的代码示例
- 包含必要的菜单配置参数

### 更新流程
1. 功能开发完成后同步更新文档
2. 问题解决后记录到相应文档
3. 定期检查文档时效性和准确性
4. 重要更新需要通知相关团队成员

### 质量标准
- 技术内容准确性 100%
- 代码示例可运行性 100%
- 菜单配置完整性 100%
- 跨文档引用有效性 100%

---

## 📞 支持与反馈

### 获取帮助
- **开发问题**: 查阅 [常见问题指南](./development/common-issues-guide.md)
- **设计问题**: 参考 [设计文档](./design/)
- **配置问题**: 查看 [菜单配置模板](./development/basic/menu-template.md)

### 反馈渠道
- 文档问题：提交 Issue 或 PR
- 功能建议：产品需求讨论
- 技术交流：团队技术分享会

---

**📋 文档状态**: ✅ 持续更新  
**🔄 维护团队**: 全体开发团队  
**📅 下次大版本更新**: 2025-02-26

*本文档中心致力于为团队提供最优质的开发体验和协作效率。* 