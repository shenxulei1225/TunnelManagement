# 📚 项目文档中心

> **Tunnel Management System - 完整文档体系**  
> 基于文档持续更新机制的项目知识管理中心

---

## 🚀 首要开发标准

### 📋 核心原则
**文档持续更新机制**是本项目开发的**首要标准**，所有开发工作必须严格遵循以下规范：

| 文档类型 | 更新频率 | 责任人 | 质量标准 |
|----------|----------|--------|----------|
| **进度跟踪** | 每日更新 | 各模块开发者 | 状态与代码同步 |
| **问题记录** | 实时更新 | 发现问题者 | 详细描述和解决方案 |
| **版本日志** | 版本发布时 | 项目负责人 | 完整记录所有变更 |
| **架构设计** | 重大变更时 | 架构师 | 保持架构图和代码一致 |

### 🎯 必读文档
- [🚀 核心开发指南 - 首要开发标准](development/core-development-guide.md) **⭐ 必读**
- [📋 规范性文档体系总结](standards/documentation-standards-summary.md) **⭐ 规范梳理**
- [📖 文档使用说明 - 快速上手指南](development/documentation-usage-guide.md) **⭐ 新手必读**

---

## 📊 当前项目状态概览

### 🎯 资源导入功能项目
- **总体进度**: 15% 完成
- **当前阶段**: 🔧 核心模块开发（60%）
- **下一里程碑**: 2025-01-15 完成导入器完善
- **详细跟踪**: [资源导入功能 - 完整设计方案](design/resource-import-master-plan.md)

### 🔧 核心模块状态
| 模块 | 状态 | 完成度 | 负责人 |
|------|------|--------|--------|
| 导入管理器 | ✅ 已实现 | 90% | 架构师 |
| Element导入器 | ✅ 已实现 | 85% | 前端开发 |
| 墨刀导入器 | 🔄 进行中 | 60% | 前端开发 |
| 用户界面 | 🔄 进行中 | 70% | UI开发 |

### 🐛 当前问题跟踪
- **ISS-001**: MockPlusImporter.ts文件创建失败 (🟡 中等)
- **ISS-002**: ResourceImport.vue界面未完全集成 (🟡 中等)

---

## 📁 文档体系结构

### 🎯 设计与规划
```
design/
├── 📋 resource-import-master-plan.md       # 资源导入功能完整设计方案 ⭐
├── 🎨 figma-style-system-integration.md    # Figma样式系统集成
├── 🏗️ figma-style-ux-designer-architecture.md  # UX设计器架构
└── 📐 universal-component-system.md        # 通用组件系统设计
```

### 📋 规范标准
```
standards/
└── 📋 documentation-standards-summary.md  # 规范性文档体系总结 ⭐
```

### 🛠️ 开发指南
```
development/
├── 🚀 core-development-guide.md            # 核心开发指南 ⭐ 必读
├── 📖 documentation-usage-guide.md         # 文档使用说明 ⭐ 新手必读
├── 🔧 common-issues-guide.md               # 常见问题指导
├── 📋 complete-workflow-guide.md           # 完整工作流程指南
├── 🏗️ multi-entry-import-architecture.md  # 多入口导入架构
├── 🎯 platform-compatibility-design.md    # 平台兼容性设计
├── 📱 umg-file-formats-guide.md            # UMG文件格式指南
└── 🔗 universal-x-designer-integration.md # Universal X Designer整合
```

### 📊 项目总结
```
summary/
├── 📈 platform-compatibility-summary.md   # 平台兼容性总结
├── 🎨 ui-converter-concept-summary.md     # UI转换器概念总结
└── 🧩 universal-component-system-implementation.md  # 通用组件系统实现
```

### 🏗️ 项目结构
```
structure/
└── 📁 project-structure.md                # 项目结构说明
```

---

## 🎯 功能模块文档

### 🔄 资源导入系统
**状态**: 🔧 开发中 (15%)

#### 核心文档
- [📋 完整设计方案](design/resource-import-master-plan.md) - 项目主控文档
- [🚀 核心开发标准](development/core-development-guide.md) - 开发规范
- [🏗️ 多入口导入架构](development/multi-entry-import-architecture.md) - 技术架构

#### 支持格式
| 格式 | 状态 | 完成度 | 特性支持 |
|------|------|--------|----------|
| **Figma** | ✅ 支持 | 95% | 完整样式、组件树、资产提取 |
| **Element UI** | ✅ 支持 | 85% | Vue模板、组件配置 |
| **UMG** | ✅ 支持 | 90% | UE组件、蓝图支持 |
| **墨刀** | 🔄 开发中 | 60% | 基础样式、页面结构 |

#### 菜单配置
每个新增页面的菜单配置参数：
```javascript
{
  name: "资源导入中心",
  type: 1,
  path: "/resource",
  icon: "ep:upload",
  permission: "resource:view"
}
```

### 🎨 UX设计器系统  
**状态**: 🔧 持续完善

#### 核心功能
- **设计画布**: 可视化编辑界面
- **组件库**: 丰富的UI组件集合  
- **样式系统**: 统一的设计语言
- **多端导出**: Vue、React、UMG支持

#### 相关文档
- [Universal X Designer 产品概述](design/universal-x-designer-product-overview.md)
- [Universal X Designer 品牌指南](design/universal-x-designer-brand.md)
- [UX设计器视窗实现](development/ue-designer-viewport-implementation.md)

---

## 🔧 开发工作流程

### 🎯 标准开发流程
1. **项目启动**
   - [ ] 创建项目主文档
   - [ ] 建立菜单配置记录  
   - [ ] 初始化问题跟踪表
   - [ ] 设定里程碑时间表

2. **开发实施**
   - [ ] 每日更新模块状态
   - [ ] 实时记录遇到的问题
   - [ ] 及时更新完成度百分比
   - [ ] 提供菜单配置参数

3. **测试验证**
   - [ ] 单元测试覆盖率 >90%
   - [ ] 集成测试场景验证
   - [ ] 性能基准测试
   - [ ] 兼容性测试记录

4. **发布部署**
   - [ ] 文档完整性检查
   - [ ] 问题解决验证
   - [ ] 版本日志编写
   - [ ] 菜单配置验证

### 🛠️ 开发工具
- **进度跟踪**: [updateModuleStatus()函数](development/core-development-guide.md#进度更新函数)
- **菜单生成**: [generateAndRecordMenuConfig()函数](development/core-development-guide.md#菜单配置生成器)
- **问题记录**: [recordIssue()函数](development/core-development-guide.md#问题跟踪记录器)
- **版本日志**: [generateVersionLog()函数](development/core-development-guide.md#版本日志生成器)

---

## 📖 文档导航

### 🎯 产品概览
- **[产品概览](./summary/universal-x-designer-product-overview.md)** - 完整的产品介绍和发展历程
- **[品牌设计](./design/universal-x-designer-brand.md)** - 品牌定位、用户画像和文案体系
- **[UI转换器概念总结](./summary/ui-converter-concept-summary.md)** - 核心技术概念和实现思路

### 🏗️ 技术设计
- **[平台兼容性设计](./design/platform-compatibility-design.md)** - 多端输出技术方案和架构设计
- **[多入口导入架构](./design/multi-entry-import-architecture.md)** - 设计文件导入系统架构
- **[UE设计器视口实现](./design/ue-designer-viewport-implementation.md)** - 可视化编辑器技术实现
- **[平台兼容性总结](./summary/platform-compatibility-summary.md)** - 技术兼容性分析和解决方案

### 🛠️ 开发指南
- **[完整工作流指南](./development/complete-workflow-guide.md)** - 开发环境配置和工作流程
- **[菜单配置指南](./development/ui-converter-menu-setup.md)** - 系统菜单权限配置步骤
- **[项目结构重构](./development/project-structure-refactor.md)** - 代码结构优化和重构方案

### 📊 项目信息
- **[项目结构说明](./structure/project-structure.md)** - 整体项目架构和模块说明

---

## 🌟 产品简介

**Universal X Designer (UXD)** 是一个专为UX设计师打造的端到端设计开发工具，通过智能的设计解析和代码生成技术，实现从用户体验设计到多平台产品的无缝转换。

### 核心价值
- 🎯 **用户体验优先** - 以UX思维驱动设计决策
- 🔄 **全平台输出** - 一次设计，多端部署
- ⚡ **端到端效率** - 从概念到代码的完整工作流
- 🎨 **设计师友好** - 专为UX设计师量身定制

### 主要功能
- **🎨 多格式导入** - 支持Figma、UMG、JSON等设计文件导入
- **🖼️ 可视化编辑** - 专业级的设计画布和组件编辑器
- **🚀 智能转换** - AI驱动的设计到代码转换引擎
- **📱 多端输出** - 生成Vue、React、Flutter、UMG等多平台代码
- **📦 资源管理** - 组件库、模板库和素材资源管理

---

## 🎯 目标用户

### 主要用户：UX/UI设计师
- 关注用户体验和交互设计
- 希望设计能直接转化为产品
- 需要跨平台设计能力
- 追求设计到开发的效率提升

### 次要用户：产品经理
- 负责产品整体体验规划
- 需要快速验证设计想法
- 关注开发效率和成本控制

### 受益用户：前端开发者
- 接收高质量的组件代码
- 专注于功能实现而非重复UI编码
- 需要规范化和可维护的代码结构

---

## 🏗️ 技术架构

### 核心技术栈
```
前端框架: Vue 3 + TypeScript + Element Plus
构建工具: Vite + ESBuild  
状态管理: Pinia
代码生成: 模板引擎 + AST操作
文件处理: JSZip + FileSaver
```

### 系统架构
```
Universal X Designer
├── 导入系统 (Figma, UMG, Content, Template)
├── 转换引擎 (Universal Converter + Plugins)
├── 编辑器 (Canvas, Tree, Properties, Preview)
└── 输出系统 (Vue, React, Flutter, UMG)
```

### 插件化设计
- **导入器插件** - 可扩展的设计文件解析系统
- **转换器插件** - 多目标平台代码生成器
- **组件插件** - 自定义组件类型扩展
- **主题插件** - 视觉主题和样式系统

---

## 🚀 快速开始

### 环境要求
- Node.js 16+ 
- npm 或 yarn
- 现代浏览器支持

### 安装步骤
```bash
# 1. 克隆项目
git clone https://github.com/your-org/tunnel-management.git
cd tunnel-management/tunnel-management-ui

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev

# 4. 配置菜单权限 (参考菜单配置指南)
# 执行 sql/mysql/converter_menu.sql 或 sql/postgresql/converter_menu.sql
```

### 访问地址
- **开发环境**: http://localhost:3000
- **UX工作台**: /ux-designer/workspace
- **体验演示**: /ux-designer/demo
- **设计模板**: /ux-designer/templates

---

## 📋 功能模块

### 1. UX工作台 (`ConverterView.vue`)
完整的设计工作环境，包含四步骤工作流：
1. **导入设计** - 多格式设计文件导入
2. **预览解析** - 设计树可视化展示
3. **配置选项** - 转换参数和选项配置
4. **生成代码** - 多平台代码生成和下载

### 2. 体验演示 (`demo.vue`)
快速体验产品功能，包含：
- 预设设计模板展示
- 实时转换效果预览
- 代码生成和复制功能
- 转换历史记录管理

### 3. 设计模板 (`templates.vue`)
模板资源中心，提供：
- 多种预制UI模板
- 模板预览和定制
- 模板导入和使用
- 模板分享和管理

---

## 📚 核心组件说明

### 转换引擎
- **[UniversalConverter.ts](../tunnel-management-ui/src/components/Converter/UniversalConverter.ts)** - 核心转换引擎
- **[types.ts](../tunnel-management-ui/src/components/Converter/types.ts)** - 类型定义系统
- **[utils.ts](../tunnel-management-ui/src/components/Converter/utils.ts)** - 工具函数库

### 导入器系统
- **[FigmaImporter.ts](../tunnel-management-ui/src/components/Converter/importers/FigmaImporter.ts)** - Figma设计文件导入
- **[UMGImporter.ts](../tunnel-management-ui/src/components/Converter/importers/UMGImporter.ts)** - UE UMG文件导入
- **[TemplateGenerator.ts](../tunnel-management-ui/src/components/Converter/importers/TemplateGenerator.ts)** - 模板生成器

### 转换器系统
- **[VueConverter.ts](../tunnel-management-ui/src/components/Converter/converters/VueConverter.ts)** - Vue 3代码生成
- **[UMGConverter.ts](../tunnel-management-ui/src/components/Converter/converters/UMGConverter.ts)** - UMG代码生成

### UI组件
- **[ImportDialog.vue](../tunnel-management-ui/src/components/Converter/ImportDialog.vue)** - 设计文件导入对话框
- **[ContentImportDialog.vue](../tunnel-management-ui/src/components/Converter/ContentImportDialog.vue)** - 内容素材导入对话框
- **[ExportDialog.vue](../tunnel-management-ui/src/components/Converter/ExportDialog.vue)** - 代码导出配置对话框
- **[ComponentTreeView.vue](../tunnel-management-ui/src/components/Converter/ComponentTreeView.vue)** - 组件层级树视图
- **[PropertyPanel.vue](../tunnel-management-ui/src/components/Converter/PropertyPanel.vue)** - 属性编辑面板
- **[PreviewDialog.vue](../tunnel-management-ui/src/components/Converter/PreviewDialog.vue)** - 设计预览对话框

---

## 🎨 设计理念

### UX设计器的"X"含义
- **eXperience** - 用户体验设计器
- **eXtended** - 扩展设计能力边界
- **Cross-platform** - 跨平台无缝输出
- **eXport** - 导出多种格式代码
- **eXpert** - 专家级设计工具
- **eXcellence** - 追求设计卓越

### 品牌文案体系
- **主标语**: Experience Everything, Export Everywhere
- **核心理念**: 让用户体验设计师成为真正的产品创造者
- **价值主张**: 从用户洞察到交互原型，从视觉设计到代码实现

---

## 📊 数据库配置

### 菜单权限配置
项目使用基于菜单的动态路由系统，需要在数据库中配置菜单权限：

#### MySQL配置
```sql
-- 执行 sql/mysql/converter_menu.sql
-- 包含主菜单、子菜单和权限配置
```

#### PostgreSQL配置
```sql
-- 执行 sql/postgresql/converter_menu.sql 
-- 包含主菜单、子菜单和权限配置
```

### 权限体系
- `ux:workspace:view` - UX工作台访问权限
- `ux:demo:view` - 体验演示访问权限
- `ux:templates:view` - 设计模板访问权限
- `ux:experience:import` - 体验导入权限
- `ux:multiplatform:export` - 多端导出权限

---

## 🔧 开发指南

### 代码结构
```
tunnel-management-ui/src/
├── components/Converter/          # 转换器核心组件
│   ├── importers/                # 导入器插件
│   ├── converters/               # 转换器插件
│   ├── UniversalConverter.ts     # 核心转换引擎
│   ├── types.ts                  # 类型定义
│   └── utils.ts                  # 工具函数
├── views/converter/              # 页面视图
│   ├── ConverterView.vue         # UX工作台
│   ├── demo.vue                  # 体验演示
│   └── templates.vue             # 设计模板
└── api/tunnel/                   # API接口
```

### 开发工作流
1. **环境配置** - 参考完整工作流指南
2. **功能开发** - 基于插件化架构扩展
3. **测试验证** - 单元测试和集成测试
4. **部署发布** - 生产环境部署流程

### 扩展开发
- **新增导入器** - 实现`ImporterPlugin`接口
- **新增转换器** - 实现`ConverterPlugin`接口
- **新增组件类型** - 扩展`ComponentType`枚举
- **新增模板** - 扩展`TemplateGenerator`模板系统

---

## 📈 发展规划

### 短期目标 (3个月)
- [ ] 多端输出完善 (React、Flutter)
- [ ] 组件库扩展和标准化
- [ ] 性能优化和用户体验提升
- [ ] 用户反馈和迭代机制建立

### 中期目标 (6个月)
- [ ] AI功能集成和智能优化
- [ ] 云端服务和项目同步
- [ ] 团队协作和版本管理
- [ ] 插件生态和开发者工具

### 长期愿景 (1年+)
- [ ] 建立设计到代码的行业标准
- [ ] 构建完整的设计师开发者生态
- [ ] 全球化产品和国际市场拓展
- [ ] 下一代设计工具技术引领

---

## 🤝 参与贡献

### 贡献方式
- **问题反馈** - 通过GitHub Issues报告问题
- **功能建议** - 提出新功能和改进建议
- **代码贡献** - 提交Pull Request参与开发
- **文档完善** - 改进和补充项目文档

### 开发规范
- 遵循TypeScript和Vue 3最佳实践
- 保持代码风格一致性
- 添加适当的测试覆盖
- 更新相关文档说明

---

## 📞 联系我们

### 社区支持
- **GitHub**: https://github.com/your-org/tunnel-management
- **文档中心**: 本仓库docs目录
- **问题反馈**: GitHub Issues
- **功能建议**: GitHub Discussions

### 商务合作
- **产品咨询**: 联系产品团队
- **技术支持**: 联系技术团队
- **商务合作**: 联系商务团队

---

**Universal X Designer - 让用户体验设计师成为真正的产品创造者**

*Experience Everything, Export Everywhere* 