# 隧道管理系统页面设计器 - 平台兼容性设计文档

## 文档信息
- **版本**: v1.0
- **创建日期**: 2024年12月
- **最后更新**: 2024年12月
- **状态**: 设计阶段

## 概述

本设计器旨在实现与多个主流设计平台的兼容性，支持快速导入优质设计资源并导出到目标平台使用。主要目标是建立一个统一的设计器平台，作为Figma、墨刀、蓝湖等设计工具与UE UMG系统之间的桥梁。

## 目标平台分析

### 1. Figma
**平台特性**:
- **数据格式**: REST API + JSON
- **设计概念**: Frame、Component、Vector、Text、Effects
- **坐标系统**: 左上角原点，像素单位，绝对定位
- **样式系统**: CSS-like properties，支持渐变、阴影、混合模式
- **优势**: 丰富的设计资源，强大的API支持
- **API能力**: 文件读取、节点导出、图片导出

**关键数据结构**:
```typescript
interface FigmaNode {
  id: string
  name: string
  type: 'FRAME' | 'COMPONENT' | 'TEXT' | 'VECTOR' | 'RECTANGLE' | 'GROUP'
  x: number, y: number, width: number, height: number
  fills?: Array<{ type: 'SOLID' | 'GRADIENT_LINEAR', color: RGBA }>
  constraints?: { horizontal: string, vertical: string }
  children?: FigmaNode[]
}
```

### 2. 墨刀/蓝湖
**平台特性**:
- **数据格式**: 私有API，主要面向切图和标注
- **设计概念**: 页面、组件、切片、标注
- **导出格式**: CSS、移动端代码、设计规范
- **优势**: 国内用户习惯，标注功能完善
- **限制**: API开放程度有限

### 3. UE UMG (Unreal Engine User Interface)
**平台特性**:
- **数据格式**: .uasset 二进制文件 + Blueprint
- **设计概念**: Widget、Slot、Panel、Anchor
- **坐标系统**: 相对锚点定位，支持响应式布局
- **样式系统**: Material-based，支持动画和交互
- **优势**: 游戏引擎集成，高性能渲染

**关键概念**:
```typescript
interface UMGWidget {
  className: 'UTextBlock' | 'UButton' | 'UImage' | 'UCanvasPanel'
  slot: {
    anchors: { minimum: {x,y}, maximum: {x,y} }
    offsets: { left, top, right, bottom }
    alignment: {x, y}
  }
}
```

## 核心兼容性挑战

### 1. 坐标系统差异
**问题描述**:
- Figma: 绝对像素定位 (x: 100, y: 200)
- UMG: 相对锚点定位 (anchor: 0.5, offset: 50)

**解决方案**:
```typescript
// 统一变换系统
interface UnifiedTransform {
  // 绝对定位 (兼容Figma)
  x: number, y: number, width: number, height: number
  
  // 相对定位 (兼容UMG)
  anchors: { min: {x,y}, max: {x,y} }
  alignment: {x, y}
  margin: { left, top, right, bottom }
}
```

### 2. 样式系统映射
**问题描述**:
- CSS颜色 → UMG Material
- Figma渐变 → UMG Brush
- 阴影效果转换

**解决方案**:
建立样式映射表，支持近似转换和fallback机制

### 3. 组件类型映射
**映射表**:
| 设计器组件 | Figma节点 | UMG控件 | 描述 |
|-----------|----------|---------|------|
| text | TEXT | UTextBlock | 文本显示 |
| button | COMPONENT | UButton | 按钮交互 |
| image | VECTOR/RECTANGLE | UImage | 图片显示 |
| container | FRAME | UCanvasPanel | 容器布局 |
| input | - | UEditableTextBox | 输入框 |

## 技术架构设计

### 1. 转换器架构
```typescript
interface PlatformConverter<T> {
  import(data: T): Promise<ComponentInstance[]>
  export(instances: ComponentInstance[]): Promise<T>
  validate(data: T): boolean
}

// 具体实现
class FigmaConverter implements PlatformConverter<FigmaNode>
class UMGConverter implements PlatformConverter<UMGWidget>
class MockingbotConverter implements PlatformConverter<MockingbotComponent>
```

### 2. 数据模型扩展
```typescript
interface ComponentInstance {
  // 现有字段...
  
  // 新增平台兼容性元数据
  metadata?: {
    figmaId?: string
    figmaType?: string
    figmaConstraints?: any
    umgClassName?: string
    umgSlot?: any
    originalPlatform?: 'figma' | 'mockingbot' | 'lanhu' | 'umg'
    [key: string]: any
  }
}
```

### 3. 导入导出配置
```typescript
interface ImportConfig {
  platform: 'figma' | 'mockingbot' | 'lanhu' | 'umg'
  preserveHierarchy: boolean    // 保持层级结构
  convertFonts: boolean         // 转换字体
  optimizeForTarget: boolean    // 针对目标平台优化
  targetPlatform?: 'web' | 'mobile' | 'umg'
}

interface ExportConfig {
  platform: 'figma' | 'umg' | 'css' | 'vue' | 'react'
  includeAssets: boolean        // 包含资源文件
  generateCode: boolean         // 生成代码
  optimizeSize: boolean         // 尺寸优化
  targetResolution?: { width: number; height: number }
}
```

## 实施路线图

### 第一阶段：基础兼容性 (已完成)
- ✅ 扩展ComponentInstance类型支持metadata字段
- ✅ 实现Figma转换器基础功能
- ✅ 实现UMG转换器基础功能
- ✅ 创建兼容性类型定义

### 第二阶段：UI集成 (进行中)
- 🔄 添加导入/导出UI界面
- 🔄 集成Figma API认证
- 🔄 实现文件选择和预览
- 🔄 添加转换进度提示

### 第三阶段：深度集成
- ⏳ 实现墨刀/蓝湖转换器
- ⏳ 完善样式映射算法
- ⏳ 添加字体和资源管理
- ⏳ 支持响应式布局转换

### 第四阶段：高级功能
- ⏳ 实现动画和交互转换
- ⏳ 支持组件库同步
- ⏳ 添加版本控制和协作
- ⏳ 性能优化和批量处理

## 关键技术难点及解决方案

### 1. 坐标系统统一
**难点**: 绝对定位 vs 相对定位
**解决方案**: 
- 建立中间坐标系统
- 支持双向转换算法
- 保留原始定位信息

### 2. 样式系统差异
**难点**: CSS vs UMG Material vs Figma Effects
**解决方案**:
- 建立样式映射表
- 支持近似转换
- 提供fallback机制

### 3. 字体和资源管理
**难点**: 跨平台字体兼容性，图片资源格式
**解决方案**:
- 资源库统一管理
- 自动格式转换
- 字体替换机制

### 4. 响应式布局
**难点**: 不同平台的布局概念差异
**解决方案**:
- 基于锚点系统的统一布局模型
- 智能布局推断
- 多尺寸适配预览

## API接口设计

### 1. Figma集成
```typescript
class FigmaAPI {
  constructor(token: string)
  async getFile(fileKey: string): Promise<{document: FigmaNode}>
  async getFileNodes(fileKey: string, nodeIds: string[]): Promise<any>
  async exportImages(fileKey: string, nodeIds: string[]): Promise<any>
}
```

### 2. 导入导出服务
```typescript
class ImportExportService {
  async importFromFigma(fileKey: string, config: ImportConfig): Promise<ComponentInstance[]>
  async exportToUMG(instances: ComponentInstance[], config: ExportConfig): Promise<UMGWidget>
  async exportToCode(instances: ComponentInstance[], format: 'vue' | 'react'): Promise<string>
}
```

## 用户使用流程

### 导入流程
1. 用户选择导入平台（Figma/墨刀/蓝湖）
2. 输入文件链接或上传文件
3. 配置导入选项（保持层级、字体转换等）
4. 预览导入结果
5. 确认导入到设计器

### 导出流程
1. 完成设计器中的设计
2. 选择导出目标（UMG/代码/其他格式）
3. 配置导出选项（包含资源、代码生成等）
4. 预览导出结果
5. 下载导出文件

## 文件组织结构

```
src/views/custom-page/
├── types/
│   ├── component.ts              # 基础组件类型（已扩展metadata）
│   └── compatibility.ts          # 平台兼容性类型定义
├── utils/
│   ├── converters/
│   │   ├── figmaConverter.ts     # Figma转换器
│   │   ├── umgConverter.ts       # UMG转换器
│   │   ├── mockingbotConverter.ts # 墨刀转换器
│   │   └── index.ts              # 转换器统一入口
│   ├── componentFactory.ts       # 组件工厂（已有）
│   └── importExportService.ts    # 导入导出服务
├── components/
│   ├── ImportDialog.vue          # 导入对话框
│   ├── ExportDialog.vue          # 导出对话框
│   └── PlatformPreview.vue       # 平台预览组件
└── apis/
    ├── figmaApi.ts               # Figma API集成
    └── platformApis.ts           # 其他平台API
```

## 测试策略

### 1. 单元测试
- 转换器功能测试
- 数据格式验证测试
- API接口测试

### 2. 集成测试
- 端到端导入导出测试
- 跨平台兼容性测试
- 性能压力测试

### 3. 用户测试
- 真实设计文件导入测试
- 用户操作流程测试
- 导出结果质量验证

## 风险评估与应对

### 1. API依赖风险
**风险**: 第三方平台API变更
**应对**: 版本兼容性处理，备用方案

### 2. 转换精度风险
**风险**: 跨平台转换精度损失
**应对**: 提供预览功能，手动调整选项

### 3. 性能风险
**风险**: 大文件处理性能问题
**应对**: 分块处理，进度显示，异步加载

## 后续扩展计划

### 短期目标 (3个月)
- 完成Figma导入基础功能
- 实现UMG导出基础功能
- 添加基础UI界面

### 中期目标 (6个月)
- 支持墨刀、蓝湖平台
- 完善样式转换算法
- 添加资源管理功能

### 长期目标 (12个月)
- 支持动画和交互转换
- 实现协作和版本控制
- 建立完整的生态系统

## 结论

通过建立统一的平台兼容性架构，本设计器将能够：

1. **提升设计效率**: 快速导入现有优质设计资源
2. **降低开发成本**: 自动生成目标平台代码
3. **保证设计还原**: 高精度的跨平台转换
4. **支持团队协作**: 统一的设计资产管理

这个兼容性设计为实现"设计一次，多平台使用"的目标奠定了坚实基础，将大大提升从设计到实现的整体效率。

---

*本文档将随着功能开发进度持续更新，请关注最新版本。* 