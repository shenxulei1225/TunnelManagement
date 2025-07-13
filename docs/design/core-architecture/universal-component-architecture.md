# Universal X Designer - 通用组件架构设计

## 概述

Universal X Designer 作为一个通用的UI设计器，需要兼容多种设计工具（Figma、Element UI、UMG、Vue组件等）。本文档描述了通用组件架构的设计方案。

## 架构设计

### 核心理念：三层架构

```
┌─────────────────────────────────────────────────────────────────┐
│                     展示层 (Presentation Layer)                 │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │
│  │ Vue渲染器   │ │ React渲染器 │ │ UMG渲染器   │ │ 其他渲染器   │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                   抽象层 (Abstraction Layer)                    │
│                        通用组件模型                             │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │
│  │ Button      │ │ Text        │ │ Container   │ │ Input       │ │
│  │ Component   │ │ Component   │ │ Component   │ │ Component   │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    适配层 (Adapter Layer)                       │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ │
│  │ Figma       │ │ Element     │ │ UMG         │ │ 其他工具     │ │
│  │ Adapter     │ │ Adapter     │ │ Adapter     │ │ Adapter     │ │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## 通用组件模型

### 1. 基础组件接口

```typescript
interface UniversalComponent {
  id: string
  type: ComponentType
  name: string
  properties: UniversalProperties
  layout: UniversalLayout
  styles: UniversalStyles
  children?: UniversalComponent[]
  metadata?: ComponentMetadata
}

enum ComponentType {
  BUTTON = 'button',
  TEXT = 'text',
  INPUT = 'input',
  CONTAINER = 'container',
  IMAGE = 'image',
  LIST = 'list',
  CUSTOM = 'custom'
}
```

### 2. 通用属性系统

```typescript
interface UniversalProperties {
  // 基础属性
  text?: string
  placeholder?: string
  value?: any
  disabled?: boolean
  visible?: boolean
  
  // 交互属性
  onClick?: EventHandler
  onInput?: EventHandler
  onFocus?: EventHandler
  
  // 验证属性
  required?: boolean
  validation?: ValidationRule[]
  
  // 扩展属性
  custom?: Record<string, any>
}
```

### 3. 通用布局系统

```typescript
interface UniversalLayout {
  // 位置
  x: number
  y: number
  
  // 尺寸
  width: number | 'auto' | string
  height: number | 'auto' | string
  
  // 约束系统
  constraints: LayoutConstraints
  
  // 响应式
  responsive?: ResponsiveLayout[]
}

interface LayoutConstraints {
  // 锚点系统（兼容UMG）
  anchor?: {
    horizontal: 'left' | 'center' | 'right' | 'stretch'
    vertical: 'top' | 'center' | 'bottom' | 'stretch'
  }
  
  // Flexbox系统（兼容Web）
  flex?: {
    direction: 'row' | 'column'
    justify: 'start' | 'center' | 'end' | 'space-between'
    align: 'start' | 'center' | 'end' | 'stretch'
  }
  
  // 绝对定位（兼容Figma）
  absolute?: {
    left?: number | string
    top?: number | string
    right?: number | string
    bottom?: number | string
  }
}
```

### 4. 通用样式系统

```typescript
interface UniversalStyles {
  // 颜色
  color?: Color
  backgroundColor?: Color
  borderColor?: Color
  
  // 字体
  font?: FontStyle
  
  // 边框
  border?: BorderStyle
  
  // 阴影
  shadow?: ShadowStyle
  
  // 变换
  transform?: TransformStyle
  
  // 动画
  animation?: AnimationStyle
  
  // 平台特定样式
  platform?: {
    web?: CSSProperties
    umg?: UMGProperties
    native?: NativeProperties
  }
}
```

## 适配器设计

### 1. Figma适配器

```typescript
class FigmaAdapter implements ComponentAdapter {
  fromFigma(figmaNode: FigmaNode): UniversalComponent {
    return {
      id: figmaNode.id,
      type: this.mapFigmaType(figmaNode.type),
      name: figmaNode.name,
      properties: this.mapFigmaProperties(figmaNode),
      layout: this.mapFigmaLayout(figmaNode),
      styles: this.mapFigmaStyles(figmaNode),
      children: figmaNode.children?.map(child => this.fromFigma(child))
    }
  }
  
  toFigma(component: UniversalComponent): FigmaNode {
    // 反向转换逻辑
  }
  
  private mapFigmaType(figmaType: string): ComponentType {
    const typeMap = {
      'RECTANGLE': ComponentType.CONTAINER,
      'TEXT': ComponentType.TEXT,
      'COMPONENT': ComponentType.BUTTON,
      'FRAME': ComponentType.CONTAINER
    }
    return typeMap[figmaType] || ComponentType.CUSTOM
  }
}
```

### 2. Element UI适配器

```typescript
class ElementAdapter implements ComponentAdapter {
  fromElement(elementConfig: ElementConfig): UniversalComponent {
    return {
      id: elementConfig.id || generateId(),
      type: this.mapElementType(elementConfig.tag),
      name: elementConfig.name || elementConfig.tag,
      properties: this.mapElementProperties(elementConfig.props),
      layout: this.mapElementLayout(elementConfig.style),
      styles: this.mapElementStyles(elementConfig.style),
      children: elementConfig.children?.map(child => this.fromElement(child))
    }
  }
  
  private mapElementType(tag: string): ComponentType {
    const typeMap = {
      'el-button': ComponentType.BUTTON,
      'el-input': ComponentType.INPUT,
      'el-text': ComponentType.TEXT,
      'el-container': ComponentType.CONTAINER,
      'div': ComponentType.CONTAINER,
      'span': ComponentType.TEXT
    }
    return typeMap[tag] || ComponentType.CUSTOM
  }
}
```

### 3. UMG适配器

```typescript
class UMGAdapter implements ComponentAdapter {
  fromUMG(umgWidget: UMGWidget): UniversalComponent {
    return {
      id: umgWidget.Name || generateId(),
      type: this.mapUMGType(umgWidget.Class),
      name: umgWidget.Name,
      properties: this.mapUMGProperties(umgWidget.Properties),
      layout: this.mapUMGLayout(umgWidget.Slot),
      styles: this.mapUMGStyles(umgWidget.Properties),
      children: umgWidget.Children?.map(child => this.fromUMG(child))
    }
  }
  
  toUMG(component: UniversalComponent): UMGWidget {
    return {
      Class: this.mapToUMGClass(component.type),
      Name: component.name,
      Properties: this.mapToUMGProperties(component.properties, component.styles),
      Slot: this.mapToUMGSlot(component.layout),
      Children: component.children?.map(child => this.toUMG(child))
    }
  }
}
```

## 转换流程

### 1. 导入流程

```typescript
class UniversalImporter {
  async import(source: ImportSource): Promise<UniversalComponent[]> {
    const adapter = this.getAdapter(source.type)
    const rawComponents = await this.loadRawData(source)
    
    return rawComponents.map(raw => adapter.fromNative(raw))
  }
  
  private getAdapter(type: string): ComponentAdapter {
    const adapters = {
      'figma': new FigmaAdapter(),
      'element': new ElementAdapter(),
      'umg': new UMGAdapter(),
      'vue': new VueAdapter()
    }
    return adapters[type]
  }
}
```

### 2. 导出流程

```typescript
class UniversalExporter {
  async export(
    components: UniversalComponent[], 
    target: ExportTarget
  ): Promise<ExportResult> {
    const adapter = this.getAdapter(target.type)
    const nativeComponents = components.map(comp => adapter.toNative(comp))
    
    return this.generateOutput(nativeComponents, target)
  }
}
```

## 属性映射规则

### 1. 按钮组件映射

| 通用属性 | Figma | Element | UMG | Vue |
|---------|-------|---------|-----|-----|
| text | text | label | Text | label |
| disabled | - | disabled | IsEnabled | disabled |
| onClick | - | @click | OnClicked | @click |
| color | fills[0] | type | ForegroundColor | style.color |

### 2. 文本组件映射

| 通用属性 | Figma | Element | UMG | Vue |
|---------|-------|---------|-----|-----|
| text | characters | text | Text | textContent |
| fontSize | fontSize | style.fontSize | Font.Size | style.fontSize |
| color | fills[0] | style.color | ColorAndOpacity | style.color |
| fontFamily | fontName | style.fontFamily | Font.TypefaceFontName | style.fontFamily |

## 扩展性设计

### 1. 插件系统

```typescript
interface ComponentPlugin {
  name: string
  version: string
  supportedTypes: ComponentType[]
  
  enhance(component: UniversalComponent): UniversalComponent
  validate(component: UniversalComponent): ValidationResult
}

class PluginManager {
  private plugins: ComponentPlugin[] = []
  
  register(plugin: ComponentPlugin): void {
    this.plugins.push(plugin)
  }
  
  applyPlugins(component: UniversalComponent): UniversalComponent {
    return this.plugins.reduce((comp, plugin) => {
      if (plugin.supportedTypes.includes(comp.type)) {
        return plugin.enhance(comp)
      }
      return comp
    }, component)
  }
}
```

### 2. 自定义组件支持

```typescript
interface CustomComponentDefinition {
  type: string
  baseType: ComponentType
  properties: PropertyDefinition[]
  defaultStyles: UniversalStyles
  renderer: ComponentRenderer
}

class CustomComponentRegistry {
  private definitions = new Map<string, CustomComponentDefinition>()
  
  register(definition: CustomComponentDefinition): void {
    this.definitions.set(definition.type, definition)
  }
  
  create(type: string, props: any): UniversalComponent {
    const definition = this.definitions.get(type)
    if (!definition) {
      throw new Error(`Unknown component type: ${type}`)
    }
    
    return {
      type: ComponentType.CUSTOM,
      customType: type,
      properties: this.mergeProperties(definition.properties, props),
      styles: definition.defaultStyles,
      // ...
    }
  }
}
```

## 实现优先级

### 阶段1：核心架构
1. 定义通用组件接口
2. 实现基础适配器（Figma、UMG）
3. 创建转换引擎

### 阶段2：扩展支持
1. 添加Element UI适配器
2. 实现Vue组件适配器
3. 完善样式映射系统

### 阶段3：高级特性
1. 插件系统
2. 自定义组件支持
3. 智能转换优化

## 总结

通过三层架构设计，我们可以：

1. **降低复杂度**：只需要N个适配器，而不是N×(N-1)个转换器
2. **提高可维护性**：每个适配器独立维护
3. **增强扩展性**：新增工具只需要实现一个适配器
4. **保证一致性**：所有工具都通过统一的抽象层交互
5. **支持渐进式**：可以逐步添加新的工具支持

这种架构既保持了各工具的特色，又实现了统一的开发体验。 