# 🚀 核心开发指南 - 首要开发标准

> **📋 Core Development Guide**  
> 项目开发的首要标准和核心流程规范  
> 最后更新：2024-12-28

## 🎯 必读文档

- [🚀 核心开发指南 - 首要开发标准](development/core-development-guide.md) **⭐ 必读**
- [📋 项目结构文档](../structure/project-structure.md) **⭐ 必读**
- [📖 文档使用说明 - 快速上手指南](documentation-usage-guide.md) **⭐ 新手必读**
- [📱 Element Plus 组件使用规范](element-plus-components-reference.md) **🚨 强制执行标准 - 每次开发前必须阅读**

## 📋 规范标准

standards/
└── 📋 documentation-standards-summary.md  # 规范性文档体系总结 ⭐

## 🛠️ 开发指南

├── 🚀 core-development-guide.md            # 核心开发指南 ⭐ 必读
├── 📖 documentation-usage-guide.md         # 文档使用说明 ⭐ 新手必读
├── 🔧 common-issues-guide.md               # 常见问题指导
├── 📋 complete-workflow-guide.md           # 完整工作流程指南
├── 🏗️ multi-entry-import-architecture.md  # 多入口导入架构

## 🎯 首要原则：文档持续更新机制

### 📋 动态跟踪系统规范

**文档持续更新**是本项目开发的**首要标准**，所有开发工作必须遵循文档与代码同步更新的机制：

#### 1. 状态更新标准 🔄

**每完成一个模块，必须立即更新对应状态和完成度**

- 实时同步: 开发状态与文档记录保持一致
- 完成度跟踪: 精确到百分比的进度管理
- 责任明确: 每个模块都有明确的负责人

```markdown
| 模块名称 | 文件路径 | 状态 | 完成度 | 备注 |
|----------|----------|------|--------|------|
| **导入管理器** | `ResourceImportManager.ts` | ✅ 已实现 | 90% | 核心逻辑完成，待测试 |
| **Element导入器** | `importers/ElementImporter.ts` | ✅ 已实现 | 85% | 新增，待完善 |
```

**状态标识规范：**
- ✅ **已完成** - 功能实现完毕，已通过测试
- 🔄 **进行中** - 正在开发，功能部分完成
- 📋 **待开始** - 已计划，尚未开始开发
- ⚠️ **有问题** - 开发中遇到阻碍，需要解决
- 🚫 **已取消** - 计划取消或延期

#### 2. 进度可视化更新 📊

**每周更新总体进度可视化**

```markdown
### 总体进度：[当前完成百分比]% 
```
████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ [XX]%
```

| 阶段 | 状态 | 完成度 | 预计完成时间 |
|------|------|--------|-------------|
| 📐 架构设计 | ✅ 已完成 | 100% | 2024-12-28 |
| 🔧 核心模块 | 🔄 进行中 | 60% | 2025-01-15 |
```

#### 3. 问题跟踪记录 🐛

**所有问题必须记录并持续跟踪直至解决**

- 实时记录: 遇到问题立即记录
- 分级管理: 按严重程度分类处理
- 解决跟踪: 从发现到解决的全过程记录

```markdown
| 问题ID | 描述 | 严重程度 | 状态 | 解决方案 |
|--------|------|----------|------|----------|
| ISS-001 | MockPlusImporter.ts文件创建失败 | 中等 | 🔄 处理中 | 手动创建文件并集成 |
| ISS-002 | ResourceImport.vue界面未完全集成 | 中等 | 🔄 处理中 | 完善路由和组件注册 |
```

**严重程度分类：**
- 🔴 **严重** - 阻碍开发进展，必须立即解决
- 🟡 **中等** - 影响功能完整性，需要及时处理
- 🟢 **轻微** - 不影响核心功能，可以延后处理

#### 4. 版本日志维护 📝

**每个版本发布必须详细记录更新内容**

- 详细记录: 每个版本的完整变更日志
- 分类管理: 新增功能、技术改进、问题修复、文档更新
- 可追溯性: 完整的版本演进历史

```markdown
### v1.0.0 - 2024-12-28
**🎉 初始版本发布**

**新增功能：**
- ✅ 完成资源导入架构设计
- ✅ 实现ResourceImportManager核心模块

**技术改进：**
- ✅ 扩展UniversalConverter支持多种导入器
- ✅ 优化格式检测算法

**文档完善：**
- ✅ 创建完整的设计方案文档
- ✅ 定义所有菜单配置参数
```

---

## 📋 标准开发流程

### Phase 1: 项目启动 🎯

#### 1.1 设计最优先
每个模块开发前需要梳理功能模块的需求整理出整体的功能分析、设计思路、实现步骤 等完善的模块化开发指导意见，记录到doc 的design目录下，需根据模块建立单独目录， 文件以模块名称-plan.md 命名， 。

#### 1.2 文档创建优先
- [ ] **创建项目主文档** - 设计方案、进度跟踪
- [ ] **建立菜单配置记录** - 所有页面菜单参数
- [ ] **初始化问题跟踪表** - 准备问题记录体系
- [ ] **设定里程碑时间表** - 明确各阶段完成时间

#### 1.3 技术架构设计
- [ ] **完成架构图设计** - Mermaid流程图
- [ ] **定义核心模块接口** - TypeScript类型定义
- [ ] **规划文件结构** - 目录和命名规范
- [ ] **确定技术栈选择** - 框架和工具选型

### Phase 2: 开发实施 🔧

#### 2.1 每日开发规范
1. **开始开发前**：
   - 检查当前模块在文档中的状态
   - 确认前置依赖是否完成
   - 更新状态为"🔄 进行中"

2. **开发过程中**：
   - 遇到问题立即记录到问题跟踪表
   - 重要决策和变更及时更新文档
   - 完成子功能及时更新完成度

3. **完成开发后**：
   - 更新状态为"✅ 已完成"
   - 记录实际完成时间
   - 更新总体进度百分比

#### 2.2 菜单管理集成规范

**每个新增页面必须提供完整菜单配置参数：**

```javascript
// 标准菜单配置模板
{
  name: "[页面名称]",
  type: 2,                    // 菜单类型
  sort: [排序号],
  parentId: [父菜单ID],
  path: "[路由路径]",
  icon: "[图标名称]",
  component: "[组件路径]",
  componentName: "[组件名]",
  permission: "[权限标识]",
  status: 0,                 // 正常状态
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```
菜单配置按照下方示例填写：
示例：
- 组件名称：分类管理优化测试
- 组件路径：system/field/FieldCategory/ComponentTest-Fixed
- 路由路径：field/FieldCategory/ComponentTest-Fixed
- 权限标识：system:field-category:list
- 图标：ep:cpu
- 排序：2
- 状态：启用
**菜单创建状态跟踪表：**

```markdown
| 菜单名称 | 路径 | 创建状态 | 菜单ID | 创建时间 |
|----------|------|----------|--------|----------|
| [页面名称] | `/[路径]` | 📋 待创建 | - | - |
```

### Phase 3: 测试验证 🧪

#### 3.1 测试文档规范
- [ ] **单元测试覆盖率** - 目标90%+
- [ ] **集成测试场景** - 关键业务流程
- [ ] **性能基准测试** - 响应时间和资源使用
- [ ] **兼容性测试记录** - 浏览器和设备兼容

#### 3.2 测试状态跟踪

```markdown
| 测试类型 | 测试文件 | 覆盖率目标 | 当前状态 |
|----------|----------|------------|----------|
| 单元测试 | `[Module].test.ts` | 95% | 📋 待创建 |
| 集成测试 | `[Feature].e2e.ts` | 90% | 📋 待创建 |
```

### Phase 4: 发布部署 🚀

#### 4.1 发布前检查清单
- [ ] **文档完整性检查** - 所有模块状态为"已完成"
- [ ] **问题解决验证** - 所有问题已关闭或延期
- [ ] **版本日志编写** - 完整记录所有变更
- [ ] **菜单配置验证** - 所有菜单已成功创建

#### 4.2 版本发布规范

```markdown
### v[版本号] - [发布日期]
**🎉 [VERSION_NAME]**

**新增功能：**
- ✅ [具体功能描述]

**技术改进：**
- ✅ [技术优化说明]

**问题修复：**
- ✅ [修复的问题描述]

**文档更新：**
- ✅ [文档变更内容]
```

---

## 🛠️ 工具和辅助函数

### 文档更新自动化

#### 1. 进度更新函数

```typescript
/**
 * 更新模块完成状态
 */
export function updateModuleStatus(
  moduleName: string,
  status: 'completed' | 'in-progress' | 'pending' | 'issue' | 'cancelled',
  completion: number,
  notes?: string
) {
  // 自动更新文档中的模块状态表格
  console.log(`📋 更新模块状态: ${moduleName}`)
  console.log(`状态: ${getStatusIcon(status)} ${getStatusText(status)}`)
  console.log(`完成度: ${completion}%`)
  if (notes) {
    console.log(`备注: ${notes}`)
  }
}

function getStatusIcon(status: string): string {
  const icons = {
    'completed': '✅',
    'in-progress': '🔄',
    'pending': '📋',
    'issue': '⚠️',
    'cancelled': '🚫'
  }
  return icons[status] || '📋'
}
```

#### 2. 菜单配置生成器

```typescript
/**
 * 自动生成菜单配置并记录到文档
 */
export function generateAndRecordMenuConfig(options: {
  pageName: string
  routePath: string
  componentPath: string
  parentMenuId?: number
  icon?: string
  sort?: number
}) {
  const config = {
    name: options.pageName,
    type: 2,
    sort: options.sort || Date.now(),
    parentId: options.parentMenuId || 0,
    path: options.routePath,
    icon: options.icon || 'ep:document',
    component: options.componentPath,
    componentName: toPascalCase(options.componentPath.replace(/\//g, '')),
    permission: `${extractModulePrefix(options.routePath)}:${options.routePath}:query`,
    status: 0,
    visible: true,
    keepAlive: true,
    alwaysShow: false
  }

  // 输出配置参数供菜单管理使用
  console.log(`
🎯 菜单管理配置参数 - ${options.pageName}：
${JSON.stringify(config, null, 2)}

📝 请复制以上配置到菜单管理系统中创建菜单
📋 并更新文档中的菜单创建状态跟踪表
  `)

  return config
}
```

#### 3. 问题跟踪记录器

```typescript
/**
 * 记录开发问题到跟踪表
 */
export function recordIssue(
  description: string,
  severity: 'critical' | 'medium' | 'minor',
  solution?: string
) {
  const issueId = `ISS-${Date.now().toString().slice(-3)}`
  
  console.log(`
🐛 新问题记录：
问题ID: ${issueId}
描述: ${description}
严重程度: ${getSeverityIcon(severity)} ${severity.toUpperCase()}
状态: 🔄 处理中
解决方案: ${solution || '待确定'}

📋 请将此问题添加到文档的问题跟踪表中
  `)

  return issueId
}

function getSeverityIcon(severity: string): string {
  const icons = {
    'critical': '🔴',
    'medium': '🟡',
    'minor': '🟢'
  }
  return icons[severity] || '🟡'
}
```

#### 4. 版本日志生成器

```typescript
/**
 * 生成版本更新日志模板
 */
export function generateVersionLog(
  version: string,
  features: string[],
  improvements: string[],
  fixes: string[],
  docs: string[]
) {
  const versionName = getVersionName(version)
  const date = new Date().toISOString().split('T')[0]

  console.log(`
### v${version} - ${date}
**🎉 ${versionName}**

**新增功能：**
${features.map(f => `- ✅ ${f}`).join('\n')}

**技术改进：**
${improvements.map(i => `- ✅ ${i}`).join('\n')}

**问题修复：**
${fixes.map(f => `- ✅ ${f}`).join('\n')}

**文档更新：**
${docs.map(d => `- ✅ ${d}`).join('\n')}

📋 请将此版本日志添加到项目文档中
  `)
}
```

---

## 📊 质量控制标准

### 文档质量指标

- **更新及时性** - 状态变更后24小时内必须更新文档
- **信息完整性** - 所有模块都必须有完整的状态和完成度记录
- **问题追踪率** - 所有问题都必须有对应的跟踪记录
- **版本记录覆盖率** - 每个版本都必须有详细的更新日志

### 开发质量指标

- **代码覆盖率** - 单元测试覆盖率 >90%
- **TypeScript严格性** - 严格模式 100%
- **ESLint合规率** - 零警告 100%
- **菜单集成率** - 所有页面都有对应菜单配置

### 用户体验指标

- **响应时间** - 关键操作 <3秒
- **错误恢复率** - 用户友好错误处理 >95%
- **界面一致性** - 设计规范遵循率 100%
- **跨平台兼容性** - 目标浏览器支持率 100%

---

## 🔄 持续改进机制

### 每周回顾

1. **进度评估**
   - 检查各模块完成度
   - 更新总体进度百分比
   - 识别进度风险和瓶颈

2. **问题分析**
   - 分析问题产生原因
   - 总结解决方案效果
   - 优化开发流程

3. **文档维护**
   - 更新过期信息
   - 补充缺失内容
   - 优化文档结构

### 月度总结

1. **里程碑检查**
   - 评估阶段性目标达成情况
   - 调整后续开发计划
   - 更新项目时间表

2. **质量分析**
   - 统计质量指标达成情况
   - 识别质量改进机会
   - 制定改进行动计划

3. **团队反馈**
   - 收集开发体验反馈
   - 优化开发工具和流程
   - 分享最佳实践

---

## 📚 相关文档链接

### 🚀 首要文档
- [📖 文档使用说明 - 如何使用文档持续更新机制](documentation-usage-guide.md) **⭐ 新手必读**
- [📚 项目文档中心](../README.md) - 文档体系入口

### 📚 基础开发指导
- [📚 基础开发指导文档目录](basic/README.md) **⭐ 基础资料**
  - [📱 Element Plus 组件属性参考文档](basic/element-plus-components-reference.md) **⭐ 前端必读**
  - [📋 菜单配置参数模板](basic/menu-template.md) **⭐ 配置必读**  
  - [🎨 标签颜色系统开发文档](tag-color-system-development.md) **⭐ UI规范必读**
  - [🛠️ 开发指导原则](basic/development-guidelines.md) **⭐ 规范必读**

### 核心设计文档
- [资源导入功能 - 完整设计方案](../design/resource-import-master-plan.md)
- [标签颜色系统设计文档](../design/tag-color-system-design.md) **⭐ UI设计规范**
- [项目开发常见问题指导](common-issues-guide.md)
- [完整工作流程指南](complete-workflow-guide.md)

### 技术实现文档
- [多入口导入架构](multi-entry-import-architecture.md)
- [平台兼容性设计](platform-compatibility-design.md)
- [UMG文件格式指南](umg-file-formats-guide.md)

### 菜单和权限配置
- [UI转换器菜单设置](ui-converter-menu-setup.md)
- [权限配置参数说明](../sql/mysql/converter_menu.sql)

### 开发工具

#### 开发建议工具
项目内置了智能的开发建议工具，帮助开发者发现页面中的潜在问题：

```vue
<template>
  <div>
    <!-- 页面内容 -->
    
    <!-- 添加开发建议组件 -->
    <DevSuggestions 
      :page-name="'your-page-name'"
      :auto-check="true"
    />
  </div>
</template>

<script setup lang="ts">
import { DevSuggestions } from '@/components/DevTools'
</script>
```

详细使用方法请参考：[开发建议工具文档](./dev-suggestions-tool.md)

#### 全局开发工具
在开发环境中，右上角会显示开发工具按钮，提供：
- 环境信息查看
- 配置管理
- 页面分析
- 快捷操作

---

## 🎯 总结

**文档持续更新机制**是确保项目高质量交付的**首要保障**。所有团队成员必须严格遵循本指南中的文档更新标准，确保：

1. **实时状态同步** - 开发状态与文档记录保持一致
2. **完整问题跟踪** - 所有问题都有记录和解决方案
3. **详细版本记录** - 每个版本都有完整的更新日志
4. **标准化流程** - 统一的开发和文档维护规范

通过严格执行这些标准，我们能够确保项目的可维护性、可追溯性和高质量交付。

---

## 🛠️ 开发工具化案例：标签颜色系统

### 优秀实践案例

标签颜色系统是我们将简单问题系统化解决的典型案例，体现了开发工具化的核心思想：

#### 问题驱动的设计思路
```typescript
// ❌ 原始问题：Element Plus类型验证错误
Invalid prop: validation failed for prop "type". 
Expected one of ["primary", "success", "info", "warning", "danger"], got value "default".

// ✅ 深层分析：概念设计问题
// Element Plus的type是消息级别语义，我们需要的是业务类型分类
Button → 'interactive'  // 交互组件
Text → 'content'       // 内容组件
```

#### 系统性解决方案
1. **概念重新设计** - 建立基于业务语义的8大组件分类
2. **技术架构升级** - 创建独立的颜色管理系统
3. **开发体验优化** - 提供简洁的API和调试工具
4. **文档体系完善** - 设计文档 + 开发文档 + 集成指南

#### 开发工具化特点
- **API简洁直观**: `getComponentTagClass(type)` 语义化，自解释
- **调试工具丰富**: 开发环境下的配置检查器和验证工具
- **扩展机制完善**: 支持动态注册新类型和颜色配置
- **TypeScript集成**: 编译时类型检查，避免运行时错误

#### 价值体现
1. **问题解决的系统性** - 不只修复错误，而是重新设计整个颜色管理体系
2. **开发体验的提升** - 降低认知负担，提高问题排查效率
3. **架构的前瞻性** - 支持无限扩展，可演化为完整设计系统基础
4. **工程化的完整性** - 构建优化、性能考虑、测试覆盖

### 设计哲学总结

这个解决方案体现了几个重要的设计思维：

1. **问题本质挖掘** - 从表面错误深入到概念设计问题
2. **系统性思考** - 不只解决当前问题，而是建立长远解决方案
3. **用户中心设计** - 从开发者体验出发，简化使用复杂度
4. **工程化思维** - 考虑性能、扩展性、维护性等工程因素
5. **文档驱动开发** - 完善的文档体系确保知识传承

### 相关文档
- [🎨 标签颜色系统设计文档](../design/tag-color-system-design.md) - 设计理念和规范
- [💻 标签颜色系统开发文档](./tag-color-system-development.md) - 技术实现和工具化

---

**📋 文档状态：** ✅ 已发布  
**🔄 维护责任：** 全体开发团队  
**📅 下次更新：** 随开发进展持续更新 

## 📁 文件组织和架构原则

### 🎨 组件文件组织原则

#### 🏗️ 页面级组件管理
**重要规则**: 为避免引用混乱，所有从页面抽取的组件都必须放在该页面同级的 `components` 目录下

**文件结构示例**:
```
src/views/
├── system/
│   ├── field/
│   │   ├── FieldCategory/
│   │   │   ├── components/           ← 页面级组件目录
│   │   │   │   ├── CategoryTreePanel.vue
│   │   │   │   ├── CategoryAttributePanel.vue  
│   │   │   │   └── CategoryTreePanelOptimized.vue
│   │   │   ├── index.vue             ← 主页面
│   │   │   ├── CategoryOptimized.vue ← 优化版页面
│   │   │   └── ComponentTest.vue     ← 测试页面
│   │   └── FieldDef/
│   │       ├── components/           ← 独立的页面级组件目录
│   │       └── index.vue
└── src/components/                   ← 全局通用组件
    ├── DictTag/
    ├── Dialog/
    └── ...
```

#### 📋 组件引用规范
- **页面引用页面级组件**: 使用相对路径
  ```vue
  // ✅ 正确：页面引用同级components
  import CategoryTreePanel from './components/CategoryTreePanel.vue'
  
  // ❌ 错误：不要从全局components引用页面级组件
  import CategoryTreePanel from '@/components/CategoryTreePanel.vue'
  ```

- **全局组件引用**: 使用绝对路径
  ```vue
  // ✅ 正确：引用全局通用组件
  import { DictTag } from '@/components'
  ```

#### 🎯 组织原则优势
- **依赖关系清晰**: 页面和其专用组件放在一起，依赖关系一目了然
- **避免引用混乱**: 不会出现全局组件和页面组件混淆的情况
- **便于维护**: 修改页面时，相关组件都在同一目录下
- **便于重构**: 移动或删除页面时，组件跟随移动，不会遗漏
- **团队协作**: 多人开发时减少文件冲突和引用错误

#### 🛠️ 实施要求
1. **新页面开发**: 创建页面时同时创建 `components` 目录
2. **组件抽取**: 从现有页面抽取组件时，必须放到页面级 `components` 目录
3. **代码审查**: 所有涉及组件引用的代码必须检查引用路径是否符合规范
4. **文档更新**: 每次组件重构后必须更新相关文档和菜单配置

---

## 📋 标准开发流程

### Phase 1: 项目启动
- **设计最优先**: 每个模块开发前需要梳理功能模块的需求整理出整体的功能分析、设计思路、实现步骤等完善的模块化开发指导意见，记录到docs的design目录下，文件以`模块名称-plan.md`命名
- **文档创建优先**: 创建项目主文档、菜单配置记录、问题跟踪表、里程碑时间表
- **技术架构设计**: 完成架构图设计、定义核心模块接口、规划文件结构、确定技术栈
- **组件目录规划**: 创建合理的页面级 `components` 目录结构

不要随意单独形成文档，创建文档时需要检查是否有该功能所属的上级文档，避免文档过于分散

### Phase 2: 开发实施
- **组件开发**: 严格按照文件组织原则开发页面和组件
- **引用规范**: 确保所有组件引用符合规范要求
- **实时更新**: 开发过程中实时更新文档和状态

### Phase 3: 测试验证
- **功能测试**: 验证页面和组件功能正确性
- **引用检查**: 检查所有组件引用路径是否正确
- **文档验证**: 确保文档和实际代码一致

### Phase 4: 发布部署
- **菜单配置**: 提供标准格式的菜单配置参数
- **文档发布**: 更新所有相关文档
- **版本记录**: 完整记录版本变更内容

---

## 🎛️ 菜单管理集成规范

### 📋 菜单配置参数标准格式
根据系统菜单管理界面格式，每个新增页面文档必须按以下格式提供菜单配置参数：

**格式要求**：
```
菜单配置参数：
- 组件名称：[页面显示名称]
- 组件路径：[Vue组件文件路径，如：/field/FieldCategory/ComponentName]
- 路由路径：[路由配置路径，不含前导斜杠，如：field/FieldCategory/ComponentName]
- 权限标识：[权限控制标识，如：system:field-category:list]
- 图标：[Element Plus图标名，如：ep:document]
- 排序：[数字排序，如：1]
- 状态：[启用/禁用]
```

**示例**：
```
菜单配置参数：
- 组件名称：分类管理优化版
- 组件路径：/field/FieldCategory/CategoryOptimized
- 路由路径：field/FieldCategory/CategoryOptimized
- 权限标识：system:field-category:list
- 图标：ep:tree-table
- 排序：1
- 状态：启用
```

**注意事项**：
- 组件路径必须以 `/` 开头，对应 `src/views` 目录下的实际组件路径
- 路由路径不能以 `/` 开头，用于系统内部路由配置
- 权限标识需要遵循现有权限体系命名规范
- 图标名称必须是 Element Plus 支持的图标

---

## 🛠️ 工具和辅助函数

### 📊 进度管理工具
- 进度更新函数: `updateModuleStatus()`
- 完成度计算器: `calculateProgress()`
- 状态同步器: `syncDocumentStatus()`

### 🎛️ 菜单管理工具
- 菜单配置生成器: `generateAndRecordMenuConfig()`
- 路由验证器: `validateRouterPath()`
- 组件路径检查器: `checkComponentPath()`

### 📝 文档管理工具
- 问题跟踪记录器: `recordIssue()`
- 版本日志生成器: `generateVersionLog()`
- 文档一致性检查器: `checkDocumentConsistency()`

### 🏗️ 组件管理工具
- 组件引用检查器: `checkComponentReferences()`
- 文件组织验证器: `validateFileStructure()`
- 依赖关系分析器: `analyzeDependencies()`

---

## ✅ 开发检查清单

### 每日开始工作前
- [ ] 阅读核心开发指南了解首要标准
- [ ] 查看项目主控文档确认当前模块状态
- [ ] 检查是否有分配给自己的问题需要处理
- [ ] 更新模块状态为"🔄 进行中"

### 开发过程中
- [ ] 遇到问题立即记录到问题跟踪表
- [ ] 完成子功能时更新完成度百分比
- [ ] 重大决策和变更及时更新相关文档
- [ ] 组件开发时严格遵循文件组织原则
- [ ] 组件引用时使用正确的路径规范

### 完成开发后
- [ ] 更新模块状态为"✅ 已完成"
- [ ] 提供标准格式的菜单配置参数（如适用）
- [ ] 更新总体进度百分比
- [ ] 关闭或更新相关问题记录
- [ ] 验证组件引用路径正确性
- [ ] 确认页面级 `components` 目录结构规范

### 代码审查检查项
- [ ] 文件组织是否符合页面级组件管理原则
- [ ] 组件引用路径是否正确（相对路径 vs 绝对路径）
- [ ] 菜单配置参数是否按标准格式提供
- [ ] 文档是否与实际代码保持一致
- [ ] 版本更新日志是否完整记录

---

**注意**: 本指南是所有开发工作的首要标准，任何违反本指南的代码都不应该合并到主分支。所有团队成员都有责任确保代码质量和规范一致性。 