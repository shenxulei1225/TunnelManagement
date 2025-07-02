# 页面设计器多平台导入导出功能实现指南

## 概述

本文档详细说明了页面设计器的多平台导入导出功能实现方案，支持Figma、墨刀、蓝湖等设计软件的原型和页面导入，同时兼容UE的UMG结构，实现各种格式的导入导出，达到快速生成页面的目的。

## 系统架构

### 1. 核心组件架构

```
Page Designer
├── Types System (类型系统)
│   ├── ComponentInstance (组件实例)
│   ├── Platform Compatibility (平台兼容性)
│   └── Import/Export Config (导入导出配置)
├── Converter System (转换器系统)
│   ├── Platform Converter Interface (平台转换器接口)
│   ├── Figma Converter (Figma转换器)
│   ├── UMG Converter (UMG转换器)
│   ├── Mockingbot Converter (墨刀转换器)
│   └── Lanhu Converter (蓝湖转换器)
├── Import/Export Service (导入导出服务)
│   ├── Platform Detection (平台检测)
│   ├── File Processing (文件处理)
│   ├── Data Validation (数据验证)
│   └── Code Generation (代码生成)
└── UI Components (界面组件)
    ├── Import Dialog (导入对话框)
    ├── Export Dialog (导出对话框)
    └── Designer Toolbar (设计器工具栏)
```

### 2. 数据流程

```
External Design File → Platform Detection → Converter → Component Instances → Designer Canvas
Designer Canvas → Component Instances → Converter → Target Format → Export File
```

## 核心技术实现

### 1. 统一组件模型

设计器使用统一的组件实例模型 `ComponentInstance`，包含：

```typescript
interface ComponentInstance {
  id: string                    // 唯一标识
  type: string                  // 组件类型
  name: string                  // 组件名称
  parentId?: string            // 父组件ID
  children: string[]           // 子组件ID列表
  
  // 变换信息
  transform: {
    x: number                  // X坐标
    y: number                  // Y坐标
    width: number              // 宽度
    height: number             // 高度
    rotation: number           // 旋转角度
    scaleX: number             // X轴缩放
    scaleY: number             // Y轴缩放
  }
  
  // UMG风格锚点系统
  anchors: {
    min: { x: number; y: number }   // 最小锚点
    max: { x: number; y: number }   // 最大锚点
  }
  
  alignment: { x: number; y: number }  // 对齐方式
  margin: {                            // 边距
    left: number
    top: number
    right: number
    bottom: number
  }
  
  style: Record<string, any>     // 样式属性
  props: Record<string, any>     // 组件属性
  state: ComponentState          // 组件状态
  hierarchy: HierarchyInfo       // 层级信息
}
```

### 2. 平台转换器接口

所有平台转换器都实现统一的接口：

```typescript
interface PlatformConverter<T> {
  import(data: T, config?: ImportConfig): Promise<ComponentInstance[]>
  export(instances: ComponentInstance[], config?: ExportConfig): Promise<T>
  validate(data: T): boolean
  getName(): string
  getSupportedFormats(): string[]
}
```

### 3. Figma转换器

支持Figma设计文件的导入导出：

**特性：**
- 支持 `.fig` 和 `.json` 格式
- Figma API 集成
- 递归处理设计层级
- 样式和属性转换
- 字体和颜色映射

**关键转换逻辑：**
```typescript
// Figma节点到组件实例的转换
private mapFigmaNodeToComponent(node: FigmaNode): ComponentInstance {
  return {
    id: node.id,
    type: this.mapFigmaTypeToComponentType(node.type),
    transform: {
      x: node.x,
      y: node.y,
      width: node.width,
      height: node.height,
      // ... 其他变换属性
    },
    style: this.convertFigmaStyle(node),
    // ... 其他属性
  }
}
```

### 4. UMG转换器

支持UE UMG界面系统的导入导出：

**特性：**
- 支持 `.uasset` 和 `.json` 格式
- UMG槽位系统转换
- 锚点和对齐系统
- Blueprint代码生成
- UE控件类型映射

**关键转换逻辑：**
```typescript
// UMG锚点到绝对坐标的转换
private calculateAbsoluteTransform(slot: UMGSlot): Transform {
  const anchorMinX = slot.anchors.minimum.x * canvasWidth
  const anchorMinY = slot.anchors.minimum.y * canvasHeight
  // ... 计算实际位置和尺寸
  return transform
}

// 生成UMG Blueprint代码
static generateBlueprintCode(widget: UMGWidget): string {
  return `
    // Create ${widget.name}
    ${widget.name} = CreateWidget<${widget.className}>(this);
    // Set slot properties...
  `
}
```

### 5. 智能平台检测

系统能够自动检测文件格式和数据来源：

```typescript
class PlatformDetector {
  // 根据文件扩展名检测
  static detectFromFile(file: File): string | null
  
  // 根据URL检测
  static detectFromURL(url: string): string | null
  
  // 根据数据结构特征检测
  static detectFromData(data: any): string | null
}
```

## 支持的平台和格式

### 1. 当前支持 (已实现)

#### Figma
- **格式**: `.fig`, `.json`
- **导入方式**: 文件上传、URL链接、API授权
- **支持特性**: 图层、样式、文本、图片、组件
- **代码生成**: Vue、React

#### UE UMG
- **格式**: `.uasset`, `.json`
- **导入方式**: 文件上传
- **支持特性**: 控件、槽位、锚点、蓝图
- **代码生成**: Blueprint C++

### 2. 计划支持 (开发中)

#### 墨刀 (Mockingbot)
- **格式**: `.mb`, `.json`
- **导入方式**: 文件上传、URL链接
- **支持特性**: 原型、交互、页面流程

#### 蓝湖 (Lanhu)
- **格式**: `.lh`, `.json`
- **导入方式**: 文件上传、URL链接
- **支持特性**: 设计稿、标注、切图

#### Sketch
- **格式**: `.sketch`, `.json`
- **导入方式**: 文件上传
- **支持特性**: Symbol、样式、画板

#### Adobe XD
- **格式**: `.xd`, `.json`
- **导入方式**: 文件上传、云端同步
- **支持特性**: 组件、状态、原型

## 使用指南

### 1. 导入流程

1. **选择平台**: 选择源设计平台 (Figma/UMG/等)
2. **选择方式**: 文件上传/URL链接/API授权
3. **配置选项**: 设置导入参数 (层级保持/字体转换/等)
4. **预览结果**: 查看转换后的组件预览
5. **确认导入**: 将组件添加到设计画布

### 2. 导出流程

1. **选择格式**: 选择目标格式 (Figma JSON/UMG JSON/Vue/React)
2. **配置选项**: 设置导出参数 (包含资源/生成代码/压缩/等)
3. **预览输出**: 查看生成的数据或代码
4. **下载文件**: 获取导出的文件

### 3. 代码生成

系统支持多种代码框架的生成：

#### Vue 3 单文件组件
```vue
<template>
  <div class="generated-component">
    <!-- 自动生成的组件结构 -->
  </div>
</template>

<script setup>
// 自动生成的逻辑代码
</script>

<style scoped>
/* 自动生成的样式 */
</style>
```

#### React JSX 组件
```jsx
import React from 'react';

const GeneratedComponent = () => {
  return (
    <div className="generated-component">
      {/* 自动生成的组件结构 */}
    </div>
  );
};

export default GeneratedComponent;
```

#### UMG Blueprint 代码
```cpp
// Create Canvas Panel
RootCanvas = CreateWidget<UCanvasPanel>(this);

// Create components and set slot properties
Component_0 = CreateWidget<UTextBlock>(this);
if (auto* Slot = Cast<UCanvasPanelSlot>(RootCanvas->AddChild(Component_0)))
{
    Slot->SetAnchors(FAnchors(0.0f, 0.0f, 0.0f, 0.0f));
    Slot->SetOffsets(FMargin(0.0f, 0.0f, 100.0f, 50.0f));
}
```

## 配置选项

### 导入配置 (ImportConfig)

```typescript
interface ImportConfig {
  preserveHierarchy?: boolean      // 保持层级结构
  convertFonts?: boolean          // 转换字体
  optimizeForPlatform?: 'web' | 'mobile' | 'umg'  // 目标平台优化
  baseCanvasSize?: { width: number; height: number }  // 基础画布尺寸
  importMode?: 'replace' | 'merge'  // 导入模式：替换或合并
}
```

### 导出配置 (ExportConfig)

```typescript
interface ExportConfig {
  includeAssets?: boolean         // 包含资源文件
  generateCode?: boolean          // 生成代码
  codeFramework?: 'vue' | 'react' | 'umg'  // 代码框架
  compression?: boolean           // 压缩输出
  exportMode?: 'all' | 'selected'  // 导出范围
}
```

## 技术特色

### 1. UMG风格锚点系统

借鉴UE4/5的UMG界面系统，实现了强大的响应式布局：

- **锚点 (Anchors)**: 定义组件在父容器中的相对位置
- **对齐 (Alignment)**: 控制组件在锚点区域内的对齐方式
- **边距 (Margin)**: 设置组件与锚点边界的距离

这种系统可以完美适配不同屏幕尺寸和分辨率。

### 2. 智能样式转换

系统能够智能地在不同平台间转换样式：

- **颜色转换**: RGB ↔ Hex ↔ HSL
- **字体映射**: 平台字体到Web安全字体
- **单位转换**: px ↔ % ↔ rem ↔ UE单位

### 3. 层级结构保持

在转换过程中完整保持原始设计的层级结构：

- 父子关系维护
- Z-index顺序保持
- 嵌套组件处理

### 4. 错误处理和恢复

完善的错误处理机制：

- 数据验证和格式检查
- 转换错误收集和报告
- 部分失败时的优雅降级

## API 接口

### 转换器工厂

```typescript
// 获取支持的平台列表
ConverterFactory.getSupportedPlatforms(): string[]

// 获取转换器实例
ConverterFactory.getConverter(platform: string): PlatformConverter

// 注册自定义转换器
ConverterFactory.register(platform: string, factory: () => PlatformConverter)
```

### 导入导出服务

```typescript
// 从文件导入
importExportService.importFromFile(file: File, platform?: string, config?: ImportConfig): Promise<ImportResult>

// 从URL导入
importExportService.importFromURL(url: string, platform?: string, config?: ImportConfig): Promise<ImportResult>

// 导出到平台
importExportService.exportToPlatform(instances: ComponentInstance[], platform: string, config?: ExportConfig): Promise<ExportResult>

// 平台间转换
importExportService.convertBetweenPlatforms(data: any, fromPlatform: string, toPlatform: string): Promise<ConversionResult>
```

## 扩展开发

### 添加新平台支持

1. **实现转换器接口**:
```typescript
class NewPlatformConverter implements PlatformConverter<NewPlatformData> {
  getName(): string { return 'New Platform' }
  getSupportedFormats(): string[] { return ['.newformat'] }
  validate(data: NewPlatformData): boolean { /* 验证逻辑 */ }
  async import(data: NewPlatformData): Promise<ComponentInstance[]> { /* 导入逻辑 */ }
  async export(instances: ComponentInstance[]): Promise<NewPlatformData> { /* 导出逻辑 */ }
}
```

2. **注册转换器**:
```typescript
ConverterFactory.register('newplatform', () => new NewPlatformConverter())
```

3. **更新类型定义**:
```typescript
// 在compatibility.ts中添加平台数据类型
interface NewPlatformData {
  // 平台特定的数据结构
}
```

### 自定义代码生成器

```typescript
class CustomCodeGenerator {
  static generate(instances: ComponentInstance[], framework: string): string {
    // 自定义代码生成逻辑
    return generatedCode
  }
}

// 注册到导出服务
exportService.registerCodeGenerator('custom', CustomCodeGenerator.generate)
```

## 性能优化

### 1. 大文件处理

- 流式处理大型设计文件
- 分块导入复杂层级结构
- 异步转换避免UI阻塞

### 2. 内存管理

- 组件实例池化
- 及时释放转换中间数据
- 图片资源懒加载

### 3. 缓存策略

- 转换结果缓存
- 平台检测结果缓存
- API响应缓存

## 测试策略

### 1. 单元测试

- 转换器逻辑测试
- 数据验证测试
- 工具函数测试

### 2. 集成测试

- 端到端导入导出流程
- 多平台转换测试
- UI组件交互测试

### 3. 性能测试

- 大文件处理性能
- 内存使用监控
- 转换速度基准

## 未来规划

### 1. 短期目标 (1-3个月)

- [ ] 完成墨刀和蓝湖转换器
- [ ] 增加Sketch支持
- [ ] 优化代码生成质量
- [ ] 添加批量处理功能

### 2. 中期目标 (3-6个月)

- [ ] Adobe XD 集成
- [ ] 协作设计工具支持
- [ ] 云端文件同步
- [ ] 实时预览功能

### 3. 长期目标 (6-12个月)

- [ ] AI辅助设计转换
- [ ] 自动化测试生成
- [ ] 设计系统集成
- [ ] 多人协作功能

## 总结

通过构建这套完整的多平台导入导出系统，页面设计器能够：

1. **无缝集成** 主流设计工具和游戏引擎
2. **快速转换** 不同平台间的设计资产
3. **智能生成** 高质量的代码输出
4. **灵活扩展** 支持新平台和格式
5. **用户友好** 提供直观的操作界面

这大大提高了设计到开发的效率，实现了真正的"设计即代码"的工作流程。 