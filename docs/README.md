# Universal X Designer - UX设计器文档中心

> **Experience Everything, Export Everywhere**  
> 让用户体验设计师成为真正的产品创造者

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