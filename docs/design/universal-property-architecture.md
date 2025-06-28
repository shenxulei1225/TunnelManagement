# Universal X Designer - 统一属性架构设计

> **一套属性系统，兼容所有平台 - 导入转换，统一编辑，多平台导出**

---

## 🎯 核心设计理念

### 统一属性系统（Universal Property System）

Universal X Designer项目中多种设计工具兼容性的架构设计问题。项目是一个专为UX设计师打造的端到端设计开发工具，目标是"Experience Everything, Export Everywhere"。

**设计哲学**：不区分Figma、UMG、Element等导入来源，建立一套标准的设计器属性系统。所有导入内容都转换为统一属性，在设计工作区提供一致的编辑体验，导出时再转换为目标平台格式。

#### 关键设计决策

1. **Frame就是Frame**：不区分来源，Figma导入多个Frame，UMG导入单个Frame，都直接放在Layer根层级
2. **属性统一化**：Element、Figma、UMG的所有属性映射为设计器标准属性
3. **智能转换**：导入时自动转换，编辑时使用统一属性，导出时反向转换
4. **平台无关**：设计器本身不关心来源和目标，专注于设计体验

#### 架构核心优势

- **🎨 设计体验统一**：无论来源如何，都是相同的设计和编辑体验
- **🔄 转换灵活性**：支持任意导入格式到任意导出格式的转换
- **⚡ 开发效率**：设计一次，多平台导出
- **🌱 可扩展性**：新增平台只需添加导入/导出适配器
- **🔧 维护简单**：属性映射配置化，易于维护扩展
- **提高可维护性**：每个适配器独立维护
- **增强扩展性**：新增工具只需要实现一个适配器
- **保证一致性**：所有工具都通过统一的抽象层交互

#### 三层架构设计

- **展示层**：Vue渲染器、React渲染器、UMG渲染器等
- **抽象层**：通用组件模型（Button、Text、Container、Input等）
- **适配层**：各种工具的适配器（Figma Adapter、Element Adapter、UMG Adapter等）

### 转换流程示意

```
📥 多源导入                🎨 统一设计器               📤 多平台导出
┌─────────────┐          ┌─────────────┐          ┌─────────────┐
│ Figma       │─────────▶│  Universal  │─────────▶│ Vue/Element │
│ UMG         │  导入转换 │  Properties │  导出转换 │ React       │
│ Sketch      │          │   System    │          │ UMG         │
│ Element     │          │             │          │ Flutter     │
│ PSD/XD      │          │             │          │ 小程序      │
└─────────────┘          └─────────────┘          └─────────────┘
```

---

## 具体实现

### 1. 核心类型系统 (`tunnel-management-ui/src/core/universal/types.ts`)

定义了完整的通用组件类型系统：

- `ComponentType`枚举：BUTTON、TEXT、INPUT、CONTAINER、IMAGE、LIST、CUSTOM
- `UniversalComponent`接口：包含id、type、name、properties、layout、styles、children、metadata
- `UniversalProperties`：通用属性系统（文本、占位符、禁用状态、交互事件等）
- `UniversalLayout`：灵活布局系统（支持锚点、Flexbox、绝对定位）
- `UniversalStyles`：丰富样式系统（颜色、字体、边框、阴影、变换、动画）
- 各种辅助接口：Color、FontStyle、BorderStyle、ShadowStyle等

### 2. 通用转换引擎 (`tunnel-management-ui/src/core/universal/UniversalConverter.ts`)

实现了核心转换逻辑：

- `UniversalConverter`类：管理适配器注册、组件导入导出、格式转换
- 支持多种数据源：Figma、UMG、Element、Vue、JSON
- 完整的验证和优化机制
- 插件系统支持
- 错误处理和回滚机制

### 3. UMG适配器 (`tunnel-management-ui/src/core/universal/adapters/UMGAdapter.ts`)

实现UMG与通用组件的双向转换：

- 类型映射：`/Script/UMG.Button` → `ComponentType.BUTTON`
- 属性映射：`Text` → `properties.text`、`IsEnabled` → `properties.disabled`
- 布局转换：UMG锚点系统与通用约束系统的转换
- 样式转换：UMG颜色格式与通用颜色格式的转换

### 4. Element UI适配器 (`tunnel-management-ui/src/core/universal/adapters/ElementAdapter.ts`)

实现Element UI与通用组件的双向转换：

- 标签映射：`el-button` → `ComponentType.BUTTON`
- 属性映射：`label` → `properties.text`、`placeholder` → `properties.placeholder`
- 样式转换：CSS样式与通用样式的转换
- 布局转换：Flexbox与通用约束系统的转换

### 5. 系统集成 (`tunnel-management-ui/src/core/universal/index.ts`)

提供统一的入口和初始化：

- `initializeUniversalSystem()`：自动注册所有适配器
- 快捷函数：`convertComponents`、`importComponents`、`exportComponents`
- 全局转换器实例：`universalConverter`

## 模块化重构方案

助手设计并实现了完整的模块化架构：

#### 1. 项目管理模块 (`useProjectManager.ts`)

- **功能**：项目创建、保存、加载、导入导出
- **接口**：`ProjectData`、`ComponentInstance`
- **方法**：`createNewProject`、`openProject`、`saveProject`、`exportProject`、`importProject`等

#### 2. 历史管理模块 (`useHistoryManager.ts`)

- **功能**：撤销/重做、历史状态管理
- **特性**：支持50条历史记录、深拷贝状态、分支历史管理
- **方法**：`saveState`、`undo`、`redo`、`jumpToState`、`batchOperation`等

#### 3. 组件管理模块 (`useComponentManager.ts`)

- **功能**：组件增删改查、选择、复制、排序
- **组件库**：预定义按钮、文本、输入框、图片、容器等基础组件
- **方法**：`addComponent`、`deleteComponent`、`selectComponent`、`duplicateComponent`等

#### 4. 导入导出模块 (`useImportExport.ts`)

- **功能**：多格式文件导入导出
- **支持格式**：JSON、Figma、UMG、Vue、Element UI
- **特性**：格式自动检测、批量导入、导入预览、数据验证
- **方法**：`importComponents`、`exportComponents`、`batchImport`、`previewImport`等

#### 5. 调试工具模块 (`useDebugTools.ts`)

- **功能**：开发调试辅助
- **特性**：调试模式、测试组件生成、性能监控、内存分析
- **方法**：`toggleDebugMode`、`addTestComponent`、`debugComponents`、`testUniversalConverter`等

## 统一坐标系架构

#### 核心理念

- **Project 项目**：包含多个Page页面
- **Page 页面**：使用无限画布，统一坐标系管理
- **Frame 设计块**：等同于UMG Widget，具有独立坐标系
- **Component 组件**：Frame内的具体UI元素

#### 双坐标系设计

- **Page坐标系**：无限画布 (-∞, -∞) to (+∞, +∞)
- **Frame坐标系**：独立坐标系 (0,0) to (width, height)

## 📋 统一数据结构

### 项目层级结构（简化）

```typescript
interface Project {
  id: string
  name: string
  files: DesignFile[]
  assets: ProjectAssets
}

interface DesignFile {
  id: string
  name: string
  pages: Page[]
}

interface Page {
  id: string
  name: string
  frames: Frame[]  // 根级Frame，不区分来源
}

interface Frame {
  id: string
  name: string
  type: 'frame'    // 统一类型
  
  // 统一的设计器属性
  properties: UniversalProperties
  
  // 子元素
  children: Element[]
  
  // 元数据（保留原始信息用于高质量导出）
  metadata: {
    originalSource: 'figma' | 'umg' | 'sketch' | 'element' | 'manual'
    originalData?: any       // 原始数据，确保导出质量
    importedAt?: Date
    version?: string
  }
}
```

### 统一属性系统核心

```typescript
interface UniversalProperties {
  // === 核心属性分组 ===
  layout: LayoutProperties      // 布局定位
  size: SizeProperties         // 尺寸约束
  appearance: AppearanceProperties  // 视觉外观
  typography: TypographyProperties  // 文字样式
  interaction: InteractionProperties // 交互状态
  behavior: BehaviorProperties    // 行为动画
  responsive: ResponsiveProperties // 响应式约束
}

// 布局属性 - 兼容所有平台定位方式
interface LayoutProperties {
  // 定位模式
  positioning: {
    type: 'absolute' | 'relative' | 'flex' | 'grid' | 'anchored'
    x: number
    y: number
    
    // UMG锚点系统（当type为'anchored'时）
    anchors?: {
      min: { x: number, y: number }  // 0.0-1.0
      max: { x: number, y: number }  // 0.0-1.0
    }
    
    // Figma约束系统
    constraints?: {
      horizontal: 'left' | 'right' | 'center' | 'left-right' | 'scale'
      vertical: 'top' | 'bottom' | 'center' | 'top-bottom' | 'scale'
    }
  }
  
  // 对齐方式
  alignment: {
    horizontal: 'left' | 'center' | 'right' | 'stretch'
    vertical: 'top' | 'center' | 'bottom' | 'stretch'
  }
  
  // 布局方向（容器专用）
  flexDirection?: 'horizontal' | 'vertical'
  justifyContent?: 'start' | 'center' | 'end' | 'space-between' | 'space-around'
  alignItems?: 'start' | 'center' | 'end' | 'stretch'
  gap?: number
  
  // 间距
  padding: EdgeInsets
  margin: EdgeInsets
  
  // 层级
  zIndex: number
}

// 尺寸属性 - 兼容固定、自适应、约束等所有模式
interface SizeProperties {
  width: {
    type: 'fixed' | 'auto' | 'fill' | 'percent' | 'min-content' | 'max-content'
    value: number
    min?: number
    max?: number
  }
  height: {
    type: 'fixed' | 'auto' | 'fill' | 'percent' | 'min-content' | 'max-content'
    value: number
    min?: number
    max?: number
  }
  aspectRatio?: number  // 宽高比锁定
}

// 外观属性 - 兼容所有平台的视觉样式
interface AppearanceProperties {
  // 背景
  background: {
    type: 'none' | 'solid' | 'gradient' | 'image' | 'pattern'
    color?: ColorValue
    gradient?: {
      type: 'linear' | 'radial' | 'conic'
      stops: ColorStop[]
      angle?: number
    }
    image?: {
      url: string
      mode: 'fill' | 'fit' | 'crop' | 'tile' | 'stretch'
      position?: { x: number, y: number }
    }
  }
  
  // 边框
  border: {
    width: EdgeInsets
    style: 'none' | 'solid' | 'dashed' | 'dotted'
    color: ColorValue
  }
  
  // 圆角
  borderRadius: CornerRadius
  
  // 阴影
  shadows: Array<{
    type: 'drop-shadow' | 'inner-shadow'
    color: ColorValue
    offset: { x: number, y: number }
    blur: number
    spread?: number
  }>
  
  // 效果
  opacity: number
  blur?: number
  
  // 遮罩和裁剪
  clipPath?: string
  mask?: ImageValue
}

// 文字属性 - 兼容所有平台文字系统
interface TypographyProperties {
  // 字体
  fontFamily: string
  fontSize: number
  fontWeight: number | 'normal' | 'bold'
  fontStyle: 'normal' | 'italic'
  
  // 颜色
  color: ColorValue
  
  // 对齐和间距
  textAlign: 'left' | 'center' | 'right' | 'justify'
  verticalAlign: 'top' | 'middle' | 'bottom' | 'baseline'
  lineHeight: number
  letterSpacing: number
  wordSpacing: number
  paragraphSpacing: number
  
  // 装饰
  textDecoration: 'none' | 'underline' | 'line-through' | 'overline'
  textTransform: 'none' | 'uppercase' | 'lowercase' | 'capitalize'
  
  // 溢出处理
  textOverflow: 'clip' | 'ellipsis' | 'fade'
  whiteSpace: 'normal' | 'nowrap' | 'pre' | 'pre-wrap'
  
  // 阴影
  textShadow?: {
    color: ColorValue
    offset: { x: number, y: number }
    blur: number
  }
}

// 交互属性 - 统一所有平台的交互状态
interface InteractionProperties {
  // 交互能力
  interactive: boolean
  focusable: boolean
  selectable: boolean
  draggable: boolean
  
  // 鼠标样式
  cursor: 'auto' | 'pointer' | 'text' | 'grab' | 'grabbing' | 'not-allowed' | 'help'
  
  // 状态样式（支持状态覆盖）
  states: {
    normal: StateOverrides
    hover?: StateOverrides
    active?: StateOverrides
    focused?: StateOverrides
    disabled?: StateOverrides
    selected?: StateOverrides
  }
  
  // 事件映射
  events: {
    onClick?: string
    onDoubleClick?: string
    onMouseEnter?: string
    onMouseLeave?: string
    onFocus?: string
    onBlur?: string
    onChange?: string
  }
}

// 行为属性 - 动画和过渡效果
interface BehaviorProperties {
  // 过渡动画
  transitions: Array<{
    property: string
    duration: number
    easing: string
    delay?: number
  }>
  
  // 关键帧动画
  animations: Array<{
    name: string
    duration: number
    easing: string
    iterations: number | 'infinite'
    direction: 'normal' | 'reverse' | 'alternate'
    keyframes: Array<{
      offset: number  // 0.0-1.0
      properties: Partial<UniversalProperties>
    }>
  }>
  
  // 原型交互（Figma风格）
  prototypes: Array<{
    trigger: 'click' | 'hover' | 'drag'
    action: 'navigate' | 'overlay' | 'scroll' | 'animate'
    target?: string
    transition?: string
  }>
}

// 响应式属性 - 多屏幕适配
interface ResponsiveProperties {
  // 断点覆盖
  breakpoints: {
    [breakpoint: string]: Partial<UniversalProperties>
  }
  
  // 缩放策略
  scaling: {
    mode: 'fixed' | 'scale' | 'responsive'
    baseWidth?: number
    baseHeight?: number
  }
  
  // 设备适配
  deviceOverrides: {
    mobile?: Partial<UniversalProperties>
    tablet?: Partial<UniversalProperties>
    desktop?: Partial<UniversalProperties>
  }
}

// 辅助类型定义
interface ColorValue {
  r: number  // 0-255
  g: number  // 0-255
  b: number  // 0-255
  a: number  // 0.0-1.0
}

interface EdgeInsets {
  top: number
  right: number
  bottom: number
  left: number
}

interface CornerRadius {
  topLeft: number
  topRight: number
  bottomRight: number
  bottomLeft: number
}

type StateOverrides = {
  [K in keyof UniversalProperties]?: Partial<UniversalProperties[K]>
}
```

---

## 🔄 属性转换系统

### 导入适配器架构

```typescript
interface ImportAdapter {
  name: string
  version: string
  supportedFormats: string[]
  
  // 检测是否支持该格式
  canHandle(data: any): boolean
  
  // 导入转换
  import(sourceData: any, options?: ImportOptions): Promise<ImportResult>
  
  // 属性映射
  mapToUniversal(sourceElement: any): UniversalProperties
}

interface ImportResult {
  project: Project
  warnings: ImportWarning[]
  errors: ImportError[]
  stats: ImportStats
}

// Figma导入适配器
class FigmaImportAdapter implements ImportAdapter {
  name = 'Figma Import Adapter'
  version = '1.0.0'
  supportedFormats = ['figma-api', 'figma-json']
  
  canHandle(data: any): boolean {
    return data.document && data.document.type === 'DOCUMENT'
  }
  
  async import(figmaData: FigmaFile): Promise<ImportResult> {
    const project = await this.convertFigmaToProject(figmaData)
    return {
      project,
      warnings: this.collectWarnings(),
      errors: [],
      stats: this.calculateStats(project)
    }
  }
  
  mapToUniversal(figmaNode: FigmaNode): UniversalProperties {
    return {
      layout: this.mapFigmaLayout(figmaNode),
      size: this.mapFigmaSize(figmaNode),
      appearance: this.mapFigmaAppearance(figmaNode),
      typography: this.mapFigmaTypography(figmaNode),
      interaction: this.mapFigmaInteraction(figmaNode),
      behavior: this.mapFigmaBehavior(figmaNode),
      responsive: this.mapFigmaResponsive(figmaNode)
    }
  }
  
  private mapFigmaLayout(node: FigmaNode): LayoutProperties {
    return {
      positioning: {
        type: 'absolute',
        x: node.absoluteBoundingBox?.x || 0,
        y: node.absoluteBoundingBox?.y || 0,
        constraints: {
          horizontal: node.constraints?.horizontal || 'left',
          vertical: node.constraints?.vertical || 'top'
        }
      },
      alignment: this.mapFigmaAlignment(node),
      padding: this.mapFigmaPadding(node),
      margin: { top: 0, right: 0, bottom: 0, left: 0 },
      zIndex: 0
    }
  }
  
  private mapFigmaSize(node: FigmaNode): SizeProperties {
    return {
      width: {
        type: 'fixed',
        value: node.absoluteBoundingBox?.width || 100
      },
      height: {
        type: 'fixed',
        value: node.absoluteBoundingBox?.height || 100
      }
    }
  }
  
  // ... 其他属性映射方法
}

// UMG导入适配器  
class UMGImportAdapter implements ImportAdapter {
  name = 'UMG Import Adapter'
  version = '1.0.0'
  supportedFormats = ['umg-blueprint', 'umg-json', 'uasset']
  
  canHandle(data: any): boolean {
    return data.Class && data.Class.includes('Widget')
  }
  
  async import(umgData: UMGWidgetData): Promise<ImportResult> {
    // UMG导入为单个Frame
    const frame = this.convertUMGWidgetToFrame(umgData)
    
    const project: Project = {
      id: generateId(),
      name: umgData.name || 'UMG Import',
      files: [{
        id: generateId(),
        name: 'Main',
        pages: [{
          id: generateId(),
          name: 'Page 1',
          frames: [frame]
        }]
      }],
      assets: this.extractUMGAssets(umgData)
    }
    
    return {
      project,
      warnings: this.collectUMGWarnings(),
      errors: [],
      stats: this.calculateStats(project)
    }
  }
  
  mapToUniversal(umgWidget: UMGWidget): UniversalProperties {
    return {
      layout: this.mapUMGLayout(umgWidget),
      size: this.mapUMGSize(umgWidget),
      appearance: this.mapUMGAppearance(umgWidget),
      typography: this.mapUMGTypography(umgWidget),
      interaction: this.mapUMGInteraction(umgWidget),
      behavior: this.mapUMGBehavior(umgWidget),
      responsive: this.mapUMGResponsive(umgWidget)
    }
  }
  
  private mapUMGLayout(widget: UMGWidget): LayoutProperties {
    // 参考已有的UMG-Vue映射文档
    return {
      positioning: {
        type: 'anchored',
        x: widget.slot?.offsets?.left || 0,
        y: widget.slot?.offsets?.top || 0,
        anchors: {
          min: widget.slot?.anchors?.minimum || { x: 0, y: 0 },
          max: widget.slot?.anchors?.maximum || { x: 0, y: 0 }
        }
      },
      alignment: {
        horizontal: this.mapUMGHorizontalAlignment(widget.slot?.horizontalAlignment),
        vertical: this.mapUMGVerticalAlignment(widget.slot?.verticalAlignment)
      },
      padding: this.mapUMGPadding(widget.style?.normal?.margin),
      margin: { top: 0, right: 0, bottom: 0, left: 0 },
      zIndex: widget.zOrder || 0
    }
  }
  
  private mapUMGSize(widget: UMGWidget): SizeProperties {
    const offsetWidth = (widget.slot?.offsets?.right || 0) - (widget.slot?.offsets?.left || 0)
    const offsetHeight = (widget.slot?.offsets?.bottom || 0) - (widget.slot?.offsets?.top || 0)
    
    return {
      width: {
        type: this.determineUMGSizeType(widget.slot?.horizontalAlignment),
        value: offsetWidth
      },
      height: {
        type: this.determineUMGSizeType(widget.slot?.verticalAlignment),
        value: offsetHeight
      }
    }
  }
  
  // ... 其他UMG属性映射方法，参考umg-vue-style-mapping.md
}
```

### 导出适配器架构

```typescript
interface ExportAdapter {
  name: string
  targetPlatform: string
  version: string
  
  // 导出转换
  export(project: Project, options?: ExportOptions): Promise<ExportResult>
  
  // 属性反向映射
  mapFromUniversal(universalProps: UniversalProperties): any
  
  // 代码生成
  generateCode(frame: Frame): string
}

interface ExportResult {
  files: GeneratedFile[]
  assets: AssetFile[]
  metadata: ExportMetadata
}

// Vue + Element UI 导出适配器
class VueElementExportAdapter implements ExportAdapter {
  name = 'Vue + Element UI Exporter'
  targetPlatform = 'web'
  version = '1.0.0'
  
  async export(project: Project): Promise<ExportResult> {
    const components = this.generateComponents(project)
    const assets = this.generateAssets(project.assets)
    
    return {
      files: [
        ...components,
        this.generatePackageJson(),
        this.generateMainApp(),
        this.generateRouter(),
        this.generateStyles()
      ],
      assets,
      metadata: {
        platform: 'web',
        framework: 'vue3',
        uiLibrary: 'element-plus',
        generatedAt: new Date()
      }
    }
  }
  
  mapFromUniversal(props: UniversalProperties): CSSProperties {
    return {
      // 布局转CSS
      position: this.convertPositioning(props.layout.positioning),
      left: `${props.layout.positioning.x}px`,
      top: `${props.layout.positioning.y}px`,
      width: this.convertSize(props.size.width),
      height: this.convertSize(props.size.height),
      
      // 外观转CSS
      backgroundColor: this.convertColor(props.appearance.background.color),
      border: this.convertBorder(props.appearance.border),
      borderRadius: this.convertBorderRadius(props.appearance.borderRadius),
      boxShadow: this.convertShadows(props.appearance.shadows),
      opacity: props.appearance.opacity,
      
      // 文字转CSS
      fontFamily: props.typography.fontFamily,
      fontSize: `${props.typography.fontSize}px`,
      fontWeight: props.typography.fontWeight,
      color: this.convertColor(props.typography.color),
      textAlign: props.typography.textAlign,
      lineHeight: props.typography.lineHeight,
      
      // 动画转CSS
      transition: this.convertTransitions(props.behavior.transitions)
    }
  }
  
  generateCode(frame: Frame): string {
    const template = this.generateTemplate(frame)
    const script = this.generateScript(frame)
    const style = this.generateStyle(frame)
    
    return `
<template>
${template}
</template>

<script setup lang="ts">
${script}
</script>

<style scoped>
${style}
</style>
    `
  }
  
  private generateTemplate(frame: Frame): string {
    // 生成Vue模板，使用Element UI组件
    const componentType = this.determineElementUIComponent(frame.properties)
    const attributes = this.generateAttributes(frame.properties)
    const children = frame.children.map(child => this.generateChildTemplate(child)).join('\n')
    
    return `
  <${componentType} ${attributes}>
    ${children}
  </${componentType}>
    `
  }
  
  private generateStyle(frame: Frame): string {
    const cssProps = this.mapFromUniversal(frame.properties)
    return Object.entries(cssProps)
      .map(([key, value]) => `  ${this.kebabCase(key)}: ${value};`)
      .join('\n')
  }
}

// UMG导出适配器
class UMGExportAdapter implements ExportAdapter {
  name = 'UMG Blueprint Exporter'
  targetPlatform = 'unreal'
  version = '1.0.0'
  
  async export(project: Project): Promise<ExportResult> {
    const blueprints = this.generateUMGBlueprints(project)
    const assets = this.generateUMGAssets(project.assets)
    
    return {
      files: blueprints,
      assets,
      metadata: {
        platform: 'unreal',
        engineVersion: '5.3',
        generatedAt: new Date()
      }
    }
  }
  
  mapFromUniversal(props: UniversalProperties): UMGWidgetStyle {
    return {
      // 布局转UMG
      slot: this.convertToUMGSlot(props.layout, props.size),
      
      // 外观转UMG样式
      style: {
        normal: {
          tint: this.convertToUEColor(props.appearance.background.color),
          margin: this.convertToUEMargin(props.layout.padding)
        },
        hovered: this.convertStateToUMG(props.interaction.states.hover),
        pressed: this.convertStateToUMG(props.interaction.states.active)
      },
      
      // 文字转UMG字体
      font: {
        fontObject: this.mapToUEFont(props.typography.fontFamily),
        size: props.typography.fontSize,
        typefaceFontName: this.mapToUEFontWeight(props.typography.fontWeight)
      },
      
      // 可见性和Z序
      visibility: props.interaction.interactive ? 'Visible' : 'Hidden',
      zOrder: props.layout.zIndex
    }
  }
  
  private convertToUMGSlot(layout: LayoutProperties, size: SizeProperties): UMGSlot {
    // 根据positioning类型决定转换策略
    if (layout.positioning.type === 'anchored' && layout.positioning.anchors) {
      return {
        anchors: layout.positioning.anchors,
        offsets: {
          left: layout.positioning.x,
          top: layout.positioning.y,
          right: layout.positioning.x + size.width.value,
          bottom: layout.positioning.y + size.height.value
        },
        alignment: {
          horizontal: this.mapToUMGAlignment(layout.alignment.horizontal),
          vertical: this.mapToUMGAlignment(layout.alignment.vertical)
        }
      }
    } else {
      // 将绝对定位转换为UMG锚点
      const parentWidth = 1920  // 假设父容器宽度
      const parentHeight = 1080 // 假设父容器高度
      
      return {
        anchors: {
          min: { 
            x: layout.positioning.x / parentWidth, 
            y: layout.positioning.y / parentHeight 
          },
          max: { 
            x: layout.positioning.x / parentWidth, 
            y: layout.positioning.y / parentHeight 
          }
        },
        offsets: {
          left: 0,
          top: 0,
          right: size.width.value,
          bottom: size.height.value
        }
      }
    }
  }
}
```

---

## 🎨 设计工作区体验

### 统一的Layer面板

在新的架构中，Layer面板不再区分Frame的来源：

```
┌─────────────────────────────────┐
│ 🌳 Layers                       │
├─────────────────────────────────┤
│ 🔍 [Search layers...]          │
├─────────────────────────────────┤
│ ┌─ 📱 Mobile Frame              │  ← Figma导入的Frame
│ │  ├─ 🔘 Login Button           │
│ │  ├─ 📝 Title Text             │
│ │  └─ 🖼️ Logo Image             │
│ └─                              │
│ ┌─ 🎮 Game UI Frame             │  ← UMG导入的Frame
│ │  ├─ 🎯 Health Bar             │
│ │  ├─ ⚔️ Action Button          │
│ │  └─ 💰 Score Display          │
│ └─                              │
│ ┌─ 🖥️ Dashboard Frame           │  ← 手动创建的Frame
│ │  ├─ 📊 Chart Widget           │
│ │  └─ 📋 Data Table             │
│ └─                              │
├─────────────────────────────────┤
│ 💡 All frames use unified props │
└─────────────────────────────────┘
```

### 统一的属性面板

属性面板智能显示相关属性，根据元素类型和平台兼容性调整：

```
┌─────────────────────────────────┐
│ 🎛️ Properties                   │
├─────────────────────────────────┤
│ 📐 Layout                       │
│   Position: [Absolute] ▼        │
│   X: [150] Y: [100]            │
│   Constraints: 🔗 [Left] [Top]  │  ← Figma约束
│   Anchors: ▣▣ ▣▣               │  ← UMG锚点（当需要时显示）
├─────────────────────────────────┤
│ 📏 Size                         │
│   Width: [Fixed] [200px]        │
│   Height: [Fixed] [50px]        │
│   Min/Max: [Auto] [Auto]        │
│   Aspect Ratio: 🔒 [4:1]       │
├─────────────────────────────────┤
│ 🎨 Appearance                   │
│   Background: [Solid] [#3498db] │
│   Border: [2px] [Solid] [#2c3e50]│
│   Radius: [4px] 🔗              │
│   Shadow: [Drop] + Add          │
│   Opacity: ▬▬▬●▬ 80%           │
├─────────────────────────────────┤
│ 📝 Typography                   │
│   Font: [Roboto] [16px] [Bold]  │
│   Color: [#ffffff] ⚫            │
│   Align: ◀ ▬ ▶ ↕               │
│   Spacing: Line[1.4] Letter[0]  │
├─────────────────────────────────┤
│ 🖱️ Interaction                  │
│   Interactive: ☑ Enabled       │
│   Cursor: [Pointer] ▼           │
│   States: [Normal] [Hover] [Press]│
│   Events: onClick, onHover...    │
├─────────────────────────────────┤
│ 🎬 Behavior                     │
│   Transitions: [All] [0.3s]     │
│   Animations: + Add Animation   │
│   Prototypes: + Add Interaction │
├─────────────────────────────────┤
│ 📱 Responsive                   │
│   Breakpoints: [SM][MD][LG][XL] │
│   Device: 📱 📟 🖥️             │
│   Scaling: [Responsive] ▼       │
└─────────────────────────────────┘
```

### 平台兼容性提示

当编辑属性时，显示在不同平台的表现：

```
┌─────────────────────────────────┐
│ 💡 Platform Compatibility       │
├─────────────────────────────────┤
│ 🌐 Web (Vue + Element):        │
│   ✓ Fully supported            │
│   CSS: width: 200px; height..  │
│                                 │
│ 🎮 UMG (Unreal Engine):        │
│   ✓ Supported with conversion  │
│   Anchors: (0.2, 0.1, 0.2, 0.1)│
│   Offsets: (0, 0, 200, 50)     │
│                                 │
│ ⚡ React:                      │
│   ✓ Fully supported            │
│   style={{width: 200, height.. │
│                                 │
│ 🎯 Flutter:                    │
│   ⚠️ Blur effect not supported │
│   Container(width: 200.0, ...)  │
└─────────────────────────────────┘
```

### 多平台导出界面

```
┌─────────────────────────────────┐
│ 📤 Export Project               │
├─────────────────────────────────┤
│ 🎯 Target Platform:             │
│   ☑ Vue + Element UI            │
│   ☑ React + Ant Design          │
│   ☑ UMG Blueprint               │
│   ☐ Flutter Widgets             │
│   ☐ 微信小程序                    │
├─────────────────────────────────┤
│ ⚙️ Export Options:              │
│   Code Quality: [Production] ▼  │
│   TypeScript: ☑ Enabled        │
│   Responsive: ☑ Include        │
│   Animations: ☑ Include        │
│   Comments: ☑ Include mapping   │
├─────────────────────────────────┤
│ 📁 Output Structure:            │
│   📦 Components/                │
│   📦 Assets/                    │
│   📦 Styles/                    │
│   📄 README.md                  │
│   📄 package.json               │
├─────────────────────────────────┤
│   [Preview] [Export All] [⚙️]   │
└─────────────────────────────────┘
```

---

## 🔧 实现策略

### 1. 渐进式实现路线

**第一阶段：核心属性支持**

- 实现基础的layout、size、appearance属性
- 建立Figma和UMG的基础导入适配器
- 支持Vue+Element和UMG的基础导出

**第二阶段：交互和动画**

- 添加interaction和behavior属性支持
- 实现状态管理和事件映射
- 支持CSS动画和UMG动画轨道

**第三阶段：响应式和高级特性**

- 实现responsive属性和多断点支持
- 添加更多导入/导出平台
- 优化属性映射算法

### 2. 属性映射配置化

```typescript
// 平台映射配置文件
const PlatformMappingConfig = {
  figma: {
    import: {
      'absoluteBoundingBox.x': 'layout.positioning.x',
      'absoluteBoundingBox.y': 'layout.positioning.y',
      'absoluteBoundingBox.width': 'size.width.value',
      'absoluteBoundingBox.height': 'size.height.value',
      'fills[0].color': 'appearance.background.color',
      'strokes[0]': 'appearance.border',
      'constraints': 'layout.positioning.constraints'
    },
    export: {
      'layout.positioning.x': 'left',
      'layout.positioning.y': 'top',
      'size.width.value': 'width',
      'size.height.value': 'height',
      'appearance.background.color': 'backgroundColor',
      'appearance.border': 'border'
    }
  },
  
  umg: {
    import: {
      'slot.offsets.left': 'layout.positioning.x',
      'slot.offsets.top': 'layout.positioning.y',
      'slot.anchors': 'layout.positioning.anchors',
      'style.normal.tint': 'appearance.background.color',
      'font.size': 'typography.fontSize'
    },
    export: {
      'layout.positioning.anchors': 'slot.anchors',
      'layout.positioning.x': 'slot.offsets.left',
      'layout.positioning.y': 'slot.offsets.top',
      'appearance.background.color': 'style.normal.tint',
      'typography.fontSize': 'font.size'
    }
  }
}
```

### 3. 属性验证和降级

```typescript
class PropertyValidator {
  validateForPlatform(props: UniversalProperties, platform: string): ValidationResult {
    const platformLimits = this.getPlatformLimitations(platform)
    const issues: ValidationIssue[] = []
    const warnings: ValidationWarning[] = []
    
    // 检查不支持的属性
    if (platform === 'umg' && props.appearance.blur) {
      warnings.push({
        type: 'unsupported-property',
        property: 'appearance.blur',
        message: 'UMG does not support blur effects',
        suggestion: 'Consider using drop shadow instead'
      })
    }
    
    // 检查属性值范围
    if (props.appearance.opacity < 0 || props.appearance.opacity > 1) {
      issues.push({
        type: 'invalid-value',
        property: 'appearance.opacity',
        value: props.appearance.opacity,
        expected: '0.0 to 1.0'
      })
    }
    
    return { valid: issues.length === 0, issues, warnings }
  }
  
  applyPlatformFallbacks(props: UniversalProperties, platform: string): UniversalProperties {
    const fallbackProps = { ...props }
    
    // 应用平台特定的降级策略
    if (platform === 'umg') {
      // UMG不支持某些CSS特性，提供降级
      if (fallbackProps.appearance.blur) {
        delete fallbackProps.appearance.blur
        // 可以转换为阴影效果
        if (!fallbackProps.appearance.shadows) {
          fallbackProps.appearance.shadows = [{
            type: 'drop-shadow',
            color: { r: 0, g: 0, b: 0, a: 0.3 },
            offset: { x: 2, y: 2 },
            blur: 4
          }]
        }
      }
    }
    
    return fallbackProps
  }
}
```

### 4. 性能优化策略

```typescript
class PropertyCache {
  private cache = new Map<string, UniversalProperties>()
  private conversionCache = new Map<string, any>()
  
  // 缓存属性转换结果
  getCachedConversion(sourceData: any, targetPlatform: string): any | null {
    const key = this.generateCacheKey(sourceData, targetPlatform)
    return this.conversionCache.get(key) || null
  }
  
  setCachedConversion(sourceData: any, targetPlatform: string, result: any): void {
    const key = this.generateCacheKey(sourceData, targetPlatform)
    this.conversionCache.set(key, result)
  }
  
  // 增量更新缓存
  invalidateRelatedCache(propertyPath: string): void {
    // 当某个属性更新时，只清理相关的缓存项
    for (const [key, value] of this.cache) {
      if (key.includes(propertyPath)) {
        this.cache.delete(key)
      }
    }
  }
}
```

---

## 🎯 架构价值总结

这个统一属性架构设计实现了：

### 🎨 用户体验层面

1. **设计体验统一**：无论Frame来源如何，都提供完全一致的设计体验
2. **学习成本降低**：用户只需学习一套属性系统，适用于所有平台
3. **工作流简化**：导入→编辑→导出，流程清晰简单
4. **实时反馈**：编辑时即时看到在不同平台的表现

### 💻 技术架构层面

1. **转换灵活性**：支持任意导入格式到任意导出格式的转换
2. **可扩展性强**：新增平台只需添加对应的适配器
3. **维护成本低**：属性映射配置化，规则清晰
4. **质量保证**：保留原始数据，确保高质量代码生成

### 🚀 业务价值层面

1. **开发效率**：真正的"设计一次，多平台导出"
2. **技能复用**：Web开发者可以直接参与UMG开发
3. **协作增强**：设计师和不同平台开发者使用统一语言
4. **生态建设**：形成跨平台的设计资源生态

**核心优势**：Universal X Designer不再是"混合架构"，而是真正的"统一架构" - 一套属性系统，兼容所有平台，让设计师和开发者专注于创造价值，而不是平台差异！
