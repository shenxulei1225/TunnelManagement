# Figma风格UX设计器架构设计

> **Experience Everything, Export Everywhere**  
> 基于Figma设计模式的专业UX设计工具架构

---

## 🎯 设计理念

### 核心设计原则
采用**Figma工作流模式**，提供熟悉的设计体验：
- **项目管理** - 统一的项目和模板管理主页
- **页面系统** - 一个项目包含多个设计页面
- **Frame独立性** - 每个Frame作为独立的设计单元
- **相对坐标系** - Frame内元素使用相对坐标系
- **组件化输出** - 以Frame为单位生成代码

### 数据模型层次
```
Project (项目)
├── Meta (项目元信息)
│   ├── name: string
│   ├── description: string
│   ├── createdAt: Date
│   └── updatedAt: Date
├── Pages[] (页面集合)
│   ├── Page (单个页面)
│   │   ├── id: string
│   │   ├── name: string
│   │   ├── frames: Frame[]
│   │   └── viewport: ViewportConfig
│   └── ...
└── SharedAssets (共享资源)
    ├── components: ComponentDefinition[]
    ├── styles: StyleDefinition[]
    └── assets: AssetDefinition[]
```

---

## 🏗️ 界面架构设计

### 1. 主页界面 (Dashboard)

```
┌─────────────────────────────────────────────────────────────┐
│                    Universal X Designer                      │
├─────────────────────────────────────────────────────────────┤
│  🏠 主页    📋 项目    📦 模板    ⚙️ 设置                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  📋 最近项目                                    + 新建项目     │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐             │
│  │ Project │ │ Project │ │ Project │ │ Project │             │
│  │   Alpha │ │  Beta   │ │ Gamma   │ │ Delta   │             │
│  │ 2 Pages │ │ 5 Pages │ │ 1 Page  │ │ 3 Pages │             │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘             │
│                                                             │
│  🎨 设计模板                                   浏览所有模板 →   │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐             │
│  │ Mobile  │ │ Desktop │ │ Landing │ │ Dashboard│             │
│  │   App   │ │ Website │ │  Page   │ │ Template │             │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘             │
│                                                             │
│  📊 使用统计                                                 │
│  • 本月设计: 15 个Frame                                      │
│  • 代码生成: 28 次                                           │
│  • 模板使用: 6 次                                            │
└─────────────────────────────────────────────────────────────┘
```

### 2. 项目编辑界面 (Project Editor)

```
┌─────────────────────────────────────────────────────────────┐
│ ← 返回主页    Project Alpha                     预览 | 发布   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  📄 页面管理                                                 │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐                        │
│  │ Page 1  │ │ Page 2  │ │ + 新页面 │                        │
│  │ ●       │ │         │ │         │                        │
│  │ 3 Frames│ │ 2 Frames│ │         │                        │
│  └─────────┘ └─────────┘ └─────────┘                        │
│                                                             │
│  🖼️ Frame 预览 (Page 1)                                     │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │                                                         │ │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐                 │ │
│  │  │ Frame 1 │  │ Frame 2 │  │ Frame 3 │                 │ │
│  │  │ Header  │  │ Content │  │ Footer  │                 │ │
│  │  │ 1920×80 │  │1920×600 │  │1920×120 │                 │ │
│  │  └─────────┘  └─────────┘  └─────────┘                 │ │
│  │                                                         │ │
│  │                    + 添加Frame                          │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 3. Frame编辑界面 (Frame Editor)

```
┌─────────────────────────────────────────────────────────────┐
│ ← Page 1   Frame: Header Component            导出Frame     │
├─────┬───────────────────────────────────────────────┬───────┤
│🧩组件│                 Frame画布                     │⚙️属性 │
│     │ ┌─────────────────────────────────────────────┐│       │
│基础  │ │              Header Frame                   ││Frame  │
│┌───┐│ │                1920 × 80                    ││Name:  │
││Btn││ │  ┌──────┐  ┌─────────────┐  ┌─────┐        ││Header │
│└───┘│ │  │ Logo │  │    Title    │  │Menu │        ││       │
│     │ │  │(0,20)│  │   (200,25)  │  │(1600││Width: │
│文本  │ │  │80×40 │  │   600×30    │  │,20) ││1920px │
│┌───┐│ │  └──────┘  └─────────────┘  │200× ││       │
││TXT││ │                             │40   ││Height:│
│└───┘│ │                             └─────┘││80px   │
│     │ └─────────────────────────────────────────────┘│       │
│布局  │                                              │Element│
│┌───┐│                                              │Logo:  │
││DIV││  坐标: Frame内相对坐标                          │X: 0   │
│└───┘│  Logo: (0, 20)   相对于Frame左上角             │Y: 20  │
│     │  Title: (200, 25) 相对于Frame左上角            │W: 80  │
│图层  │  Menu: (1600, 20) 相对于Frame左上角           │H: 40  │
│┌Logo┐│                                              │       │
│├Title│                                              │       │
│└Menu┘│                                              │       │
└─────┴───────────────────────────────────────────────┴───────┘
```

---

## 📊 数据模型设计

### Frame独立坐标系实现

```typescript
// Frame数据结构
interface Frame {
  id: string
  name: string
  type: 'component' | 'page' | 'section'
  
  // Frame在页面中的位置 (页面级坐标)
  pagePosition: {
    x: number      // Frame在页面画布中的X位置
    y: number      // Frame在页面画布中的Y位置
  }
  
  // Frame自身尺寸
  dimensions: {
    width: number  // Frame宽度
    height: number // Frame高度
  }
  
  // Frame内的元素 (使用Frame内相对坐标)
  elements: FrameElement[]
  
  // Frame级别的样式和配置
  styles: FrameStyles
  metadata: FrameMetadata
}

// Frame内元素结构
interface FrameElement {
  id: string
  name: string
  type: ElementType
  
  // 相对于Frame左上角的坐标
  position: {
    x: number      // 相对于Frame的X偏移
    y: number      // 相对于Frame的Y偏移
  }
  
  dimensions: {
    width: number
    height: number
  }
  
  // 元素样式和属性
  styles: ElementStyles
  properties: ElementProperties
  
  // 嵌套子元素
  children?: FrameElement[]
}

// 页面结构
interface Page {
  id: string
  name: string
  frames: Frame[]           // 页面包含的Frame列表
  viewport: ViewportConfig  // 页面视口配置
  settings: PageSettings    // 页面级设置
}

// 项目结构
interface Project {
  id: string
  name: string
  description: string
  pages: Page[]                    // 项目包含的页面
  sharedAssets: SharedAssets       // 共享资源
  metadata: ProjectMetadata        // 项目元信息
}
```

### 坐标转换系统

```typescript
// Frame坐标转换工具
class FrameCoordinateSystem {
  
  /**
   * Frame内相对坐标转换为页面绝对坐标
   */
  frameToPageCoordinate(
    framePosition: Position, 
    elementPosition: Position
  ): Position {
    return {
      x: framePosition.x + elementPosition.x,
      y: framePosition.y + elementPosition.y
    }
  }
  
  /**
   * 页面绝对坐标转换为Frame内相对坐标
   */
  pageToFrameCoordinate(
    pagePosition: Position,
    framePosition: Position
  ): Position {
    return {
      x: pagePosition.x - framePosition.x,
      y: pagePosition.y - framePosition.y
    }
  }
  
  /**
   * 检查点是否在Frame范围内
   */
  isPointInFrame(
    point: Position,
    frame: Frame
  ): boolean {
    const { x, y } = frame.pagePosition
    const { width, height } = frame.dimensions
    
    return point.x >= x && 
           point.x <= x + width &&
           point.y >= y && 
           point.y <= y + height
  }
}
```

---

## 🎨 界面组件设计

### 1. 主页组件 (Dashboard.vue)

```typescript
// 主页组件功能
interface DashboardFeatures {
  // 项目管理
  projectManagement: {
    listRecentProjects(): Project[]
    createNewProject(template?: Template): void
    openProject(projectId: string): void
    deleteProject(projectId: string): void
  }
  
  // 模板管理
  templateManagement: {
    listTemplates(): Template[]
    useTemplate(templateId: string): void
    createCustomTemplate(): void
  }
  
  // 统计信息
  analytics: {
    getUsageStats(): UsageStats
    getProjectStats(): ProjectStats
  }
}
```

### 2. 项目编辑器 (ProjectEditor.vue)

```typescript
// 项目编辑器功能
interface ProjectEditorFeatures {
  // 页面管理
  pageManagement: {
    listPages(): Page[]
    createPage(name: string): Page
    deletePage(pageId: string): void
    renamePage(pageId: string, newName: string): void
  }
  
  // Frame预览
  framePreview: {
    listFrames(pageId: string): Frame[]
    selectFrame(frameId: string): void
    editFrame(frameId: string): void
  }
  
  // 项目设置
  projectSettings: {
    updateProjectInfo(info: ProjectInfo): void
    manageSharedAssets(): void
    exportProject(): void
  }
}
```

### 3. Frame编辑器 (FrameEditor.vue)

```typescript
// Frame编辑器功能
interface FrameEditorFeatures {
  // Frame画布
  frameCanvas: {
    renderFrame(frame: Frame): void
    addElement(elementType: ElementType): void
    selectElement(elementId: string): void
    updateElement(elementId: string, updates: Partial<FrameElement>): void
  }
  
  // 组件库
  componentLibrary: {
    listBasicComponents(): ComponentDefinition[]
    listLayoutComponents(): ComponentDefinition[]
    listCustomComponents(): ComponentDefinition[]
    dragComponent(componentId: string): void
  }
  
  // 属性面板
  propertyPanel: {
    showFrameProperties(frame: Frame): void
    showElementProperties(element: FrameElement): void
    updateProperties(updates: PropertyUpdates): void
  }
  
  // 图层管理
  layerManagement: {
    showLayerTree(): LayerNode[]
    reorderLayers(fromIndex: number, toIndex: number): void
    toggleLayerVisibility(layerId: string): void
  }
}
```

---

## 🔧 技术实现方案

### Frame渲染引擎

```typescript
// Frame渲染器
class FrameRenderer {
  private frameCoordinates: FrameCoordinateSystem
  
  /**
   * 渲染Frame到画布
   */
  renderFrame(frame: Frame, parentElement: HTMLElement): void {
    const frameContainer = this.createFrameContainer(frame)
    
    // 设置Frame容器样式
    frameContainer.style.cssText = `
      position: absolute;
      left: ${frame.pagePosition.x}px;
      top: ${frame.pagePosition.y}px;
      width: ${frame.dimensions.width}px;
      height: ${frame.dimensions.height}px;
      border: 1px solid #e4e7ed;
      background: white;
      overflow: hidden;
    `
    
    // 渲染Frame内的元素
    frame.elements.forEach(element => {
      const elementNode = this.renderElement(element, frame)
      frameContainer.appendChild(elementNode)
    })
    
    parentElement.appendChild(frameContainer)
  }
  
  /**
   * 渲染Frame内的元素
   */
  private renderElement(element: FrameElement, frame: Frame): HTMLElement {
    const elementNode = document.createElement('div')
    
    // 使用Frame内相对坐标
    elementNode.style.cssText = `
      position: absolute;
      left: ${element.position.x}px;
      top: ${element.position.y}px;
      width: ${element.dimensions.width}px;
      height: ${element.dimensions.height}px;
      ${this.generateElementStyles(element.styles)}
    `
    
    // 渲染元素内容
    this.renderElementContent(element, elementNode)
    
    return elementNode
  }
}
```

### Frame操作系统

```typescript
// Frame操作管理器
class FrameOperationManager {
  
  /**
   * 在Frame内添加元素
   */
  addElementToFrame(
    frameId: string, 
    elementType: ElementType, 
    position: Position
  ): FrameElement {
    const frame = this.getFrame(frameId)
    const element: FrameElement = {
      id: generateId(),
      name: `${elementType}_${Date.now()}`,
      type: elementType,
      position: position,  // Frame内相对坐标
      dimensions: this.getDefaultDimensions(elementType),
      styles: this.getDefaultStyles(elementType),
      properties: this.getDefaultProperties(elementType)
    }
    
    frame.elements.push(element)
    this.saveFrame(frame)
    
    return element
  }
  
  /**
   * 移动Frame内的元素
   */
  moveElementInFrame(
    frameId: string,
    elementId: string,
    newPosition: Position
  ): void {
    const frame = this.getFrame(frameId)
    const element = frame.elements.find(el => el.id === elementId)
    
    if (element) {
      // 确保新位置在Frame范围内
      const constrainedPosition = this.constrainPositionToFrame(
        newPosition, 
        element.dimensions, 
        frame.dimensions
      )
      
      element.position = constrainedPosition
      this.saveFrame(frame)
    }
  }
  
  /**
   * 限制元素位置在Frame范围内
   */
  private constrainPositionToFrame(
    position: Position,
    elementSize: Dimensions,
    frameSize: Dimensions
  ): Position {
    return {
      x: Math.max(0, Math.min(position.x, frameSize.width - elementSize.width)),
      y: Math.max(0, Math.min(position.y, frameSize.height - elementSize.height))
    }
  }
}
```

---

## 🚀 代码生成策略

### Frame级别代码生成

```typescript
// Frame代码生成器
class FrameCodeGenerator {
  
  /**
   * 生成单个Frame的Vue组件代码
   */
  generateFrameComponent(frame: Frame): GeneratedCode {
    const componentName = this.sanitizeComponentName(frame.name)
    
    return {
      fileName: `${componentName}.vue`,
      code: `<template>
  <div class="${componentName.toLowerCase()}-frame" 
       :style="frameStyles">
    ${this.generateElementsTemplate(frame.elements)}
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

// Frame样式计算
const frameStyles = computed(() => ({
  width: '${frame.dimensions.width}px',
  height: '${frame.dimensions.height}px',
  position: 'relative',
  ${this.generateFrameCSS(frame.styles)}
}))

${this.generateElementsScript(frame.elements)}
</script>

<style scoped>
${this.generateFrameStyles(frame)}
</style>`
    }
  }
  
  /**
   * 生成页面级别代码（包含多个Frame）
   */
  generatePageComponent(page: Page): GeneratedCode {
    const imports = page.frames.map(frame => 
      `import ${this.sanitizeComponentName(frame.name)} from './frames/${this.sanitizeComponentName(frame.name)}.vue'`
    ).join('\n')
    
    const components = page.frames.map(frame => {
      const componentName = this.sanitizeComponentName(frame.name)
      return `    <${componentName} 
        :style="{
          position: 'absolute',
          left: '${frame.pagePosition.x}px',
          top: '${frame.pagePosition.y}px'
        }" 
      />`
    }).join('\n')
    
    return {
      fileName: `${page.name}.vue`,
      code: `<template>
  <div class="page-container" :style="pageStyles">
${components}
  </div>
</template>

<script setup lang="ts">
${imports}

const pageStyles = {
  position: 'relative',
  width: '100%',
  height: '100vh',
  overflow: 'hidden'
}
</script>`
    }
  }
}
```

### 批量导出功能

```typescript
// 项目导出管理器
class ProjectExportManager {
  
  /**
   * 导出整个项目
   */
  async exportProject(
    project: Project, 
    format: 'vue' | 'react' | 'flutter'
  ): Promise<ExportResult> {
    const results: GeneratedCode[] = []
    
    // 1. 为每个Frame生成组件代码
    for (const page of project.pages) {
      for (const frame of page.frames) {
        const frameCode = this.codeGenerator.generateFrameComponent(frame)
        results.push(frameCode)
      }
      
      // 2. 为每个Page生成页面代码
      const pageCode = this.codeGenerator.generatePageComponent(page)
      results.push(pageCode)
    }
    
    // 3. 生成项目配置文件
    const configCode = this.generateProjectConfig(project)
    results.push(configCode)
    
    // 4. 打包下载
    return this.packageForDownload(results, format)
  }
  
  /**
   * 导出单个Frame
   */
  async exportFrame(
    frame: Frame, 
    format: 'vue' | 'react' | 'flutter'
  ): Promise<ExportResult> {
    const frameCode = this.codeGenerator.generateFrameComponent(frame)
    return this.packageForDownload([frameCode], format)
  }
}
```

---

## 📱 响应式和自适应

### Frame响应式系统

```typescript
// Frame响应式配置
interface ResponsiveFrameConfig {
  breakpoints: {
    mobile: number    // 375px
    tablet: number    // 768px
    desktop: number   // 1200px
  }
  
  frameVariants: {
    [frameId: string]: {
      mobile?: FrameDimensions
      tablet?: FrameDimensions  
      desktop?: FrameDimensions
    }
  }
  
  elementVariants: {
    [elementId: string]: {
      mobile?: ElementLayout
      tablet?: ElementLayout
      desktop?: ElementLayout
    }
  }
}

// 响应式Frame渲染
class ResponsiveFrameRenderer {
  
  renderResponsiveFrame(
    frame: Frame, 
    viewport: ViewportSize,
    config: ResponsiveFrameConfig
  ): void {
    const breakpoint = this.getBreakpoint(viewport.width, config.breakpoints)
    const frameVariant = config.frameVariants[frame.id]?.[breakpoint] || frame.dimensions
    
    // 渲染Frame with responsive dimensions
    this.renderFrame({
      ...frame,
      dimensions: frameVariant
    })
    
    // 渲染elements with responsive layouts  
    frame.elements.forEach(element => {
      const elementVariant = config.elementVariants[element.id]?.[breakpoint]
      if (elementVariant) {
        this.updateElementLayout(element.id, elementVariant)
      }
    })
  }
}
```

这种Figma风格的架构设计具有以下优势：

1. **🎯 熟悉的工作流** - 设计师无需学习新的操作模式
2. **🧩 组件化思维** - Frame天然支持组件化开发
3. **📱 响应式支持** - Frame独立坐标系便于响应式适配
4. **🔄 代码复用** - Frame可以在不同页面和项目间复用
5. **⚡ 性能优化** - Frame级别的局部渲染和更新
6. **🎨 设计系统** - 支持构建完整的设计系统和组件库

这样的架构能够真正实现**"Experience Everything, Export Everywhere"**的产品愿景！ 