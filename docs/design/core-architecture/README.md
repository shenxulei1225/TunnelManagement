# 核心架构设计

> 本目录包含项目的核心架构设计文档，涵盖系统的基础架构、组件设计、工作区架构等。

## 📂 文档列表

### 🏗️ 统一属性架构
- [统一属性架构设计](./universal-property-architecture.md) - Universal X Designer统一属性系统架构
  - 统一属性系统（Universal Property System）设计理念
  - 三层架构：展示层、抽象层、适配层
  - 多平台导入导出转换流程

### 🔧 通用组件架构
- [通用组件架构设计](./universal-component-architecture.md) - 三层架构设计
  - 展示层：Vue渲染器、React渲染器、UMG渲染器等
  - 抽象层：通用组件模型（Button、Text、Container、Input等）
  - 适配层：各种工具的适配器（Figma Adapter、Element Adapter、UMG Adapter等）

### 🎨 工作区架构
- [工作区架构重新设计](./workspace-architecture-redesign.md) - Figma式统一坐标系架构设计
  - 统一项目结构：Project → Files → Pages → Frames
  - 双坐标系设计：Page坐标系（无限画布）+ Frame坐标系（独立坐标系）
  - 统一设计器属性系统

### 🔄 混合架构
- [Figma+UMG混合架构](./figma-umg-hybrid-architecture.md) - 统一设计器属性系统架构
  - 不区分Figma、UMG、Element等导入来源
  - 建立标准的设计器属性系统
  - 智能转换：导入时自动转换，编辑时使用统一属性，导出时反向转换

### 🌐 平台兼容性
- [平台兼容性设计](./platform-compatibility-design.md) - 多平台兼容性架构设计
  - 支持Figma、墨刀、蓝湖、UE UMG等主流平台
  - 统一的设计器平台，作为各设计工具与UMG系统之间的桥梁
  - "设计一次，多平台使用"的愿景

---

## 🎯 设计原则

1. **统一性** - 所有平台通过统一的数据模型和属性系统进行交互
2. **扩展性** - 新增平台只需添加导入/导出适配器
3. **灵活性** - 支持任意导入格式到任意导出格式的转换
4. **一致性** - 无论来源如何，都提供相同的设计和编辑体验

---

*本目录持续更新，如有疑问请联系架构设计团队。* 