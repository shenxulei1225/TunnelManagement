# Figma精确架构复刻设计

> **完全复刻Figma工作流：Project → File → Page → Frame → Element**

---

## 📋 Figma架构层级结构

### 完整数据层次
```
Team/Organization (团队)
├── Project (项目)
│   ├── File (文件) 
│   │   ├── Page (页面)
│   │   │   ├── Frame (画框)
│   │   │   │   ├── Element (元素)
│   │   │   │   │   └── Children (子元素)
│   │   │   │   └── ...
│   │   │   └── ...
│   │   └── ...
│   └── ...
└── Shared Components (共享组件库)
```

### 界面层级对应
```
1. Home页面 (Team Dashboard)
   ├── Project管理
   └── File管理 (每个Project下的文件列表)

2. 设计工作区 (File Editor)
   ├── 左侧面板
   │   ├── Pages列表 (切换页面)
   │   ├── Layers树 (当前Page的Frame和Element层级)
   │   └── Assets资源库
   ├── 中间画布区域
   │   └── 当前Page的Frame布局
   └── 右侧属性面板
       ├── Design属性
       ├── Prototype原型
       └── Inspect检查
```

---

## 🗂️ 数据模型设计

### 核心数据结构

```typescript
// 团队/组织
interface Team {
  id: string
  name: string
  members: TeamMember[]
  projects: Project[]
}

// 项目 (对应Figma的Project)
interface Project {
  id: string
  name: string
  description?: string
  teamId: string
  files: DesignFile[]          // 项目下的文件列表
  sharedComponents: Component[]
  createdAt: Date
  updatedAt: Date
}

// 设计文件 (对应Figma的File)
interface DesignFile {
  id: string
  name: string
  projectId: string
  pages: Page[]                // 文件包含的页面
  sharedStyles: SharedStyle[]
  thumbnail?: string
  lastOpenedPage?: string
  createdAt: Date
  updatedAt: Date
}

// 页面 (对应Figma的Page)
interface Page {
  id: string
  name: string
  fileId: string
  frames: Frame[]              // 页面内的Frame列表
  guidesVisible: boolean
  backgroundColor: string
  flowStartingPoints: FlowStartingPoint[]
}

// 画框 (对应Figma的Frame)
interface Frame {
  id: string
  name: string
  pageId: string
  type: 'frame' | 'component' | 'instance'
  
  // Frame在Page中的位置
  position: {
    x: number
    y: number
  }
  
  // Frame尺寸
  size: {
    width: number
    height: number
  }
  
  // Frame内的元素
  children: FrameElement[]
  
  // Frame样式和约束
  fills: Fill[]
  strokes: Stroke[]
  cornerRadius?: number
  constraints: Constraints
  layoutMode?: 'NONE' | 'HORIZONTAL' | 'VERTICAL'
  
  // 组件相关
  componentId?: string         // 如果是组件实例
  componentProperties?: Record<string, any>
}

// Frame内元素 (对应Figma的Element)
interface FrameElement {
  id: string
  name: string
  frameId: string
  type: ElementType           // 'rectangle' | 'ellipse' | 'text' | 'image' | 'group' | etc.
  
  // 相对于Frame的位置 (Figma的相对坐标系)
  relativePosition: {
    x: number
    y: number
  }
  
  // 元素尺寸
  size: {
    width: number
    height: number
  }
  
  // 元素样式
  fills: Fill[]
  strokes: Stroke[]
  effects: Effect[]
  cornerRadius?: number
  
  // 文本属性 (if type === 'text')
  characters?: string
  textStyle?: TextStyle
  
  // 约束和布局
  constraints: Constraints
  layoutAlign?: LayoutAlign
  layoutGrow?: number
  
  // 嵌套子元素
  children?: FrameElement[]
  
  // 组件实例属性
  componentId?: string
  componentProperties?: Record<string, any>
}

// 辅助类型定义
type ElementType = 
  | 'rectangle' 
  | 'ellipse' 
  | 'text' 
  | 'image' 
  | 'vector'
  | 'group'
  | 'frame'
  | 'component'
  | 'instance'

interface Constraints {
  horizontal: 'LEFT' | 'RIGHT' | 'CENTER' | 'LEFT_RIGHT' | 'SCALE'
  vertical: 'TOP' | 'BOTTOM' | 'CENTER' | 'TOP_BOTTOM' | 'SCALE'
}
```

---

## 🏠 Home页面设计 (Team Dashboard)

### 界面布局

```
┌─────────────────────────────────────────────────────────────┐
│ 🎨 Universal X Designer  [Kevin ▼]     [🔍 Search]  [+Project]│
├─────────────────────────────────────────────────────────────┤
│ 📂 xulei.shen's team                                        │
│                                                             │
│ 🗂️ 最近项目                                    [View all →]    │
│ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐             │
│ │📋 Project A │ │📋 Project B │ │📋 Project C │             │
│ │ 3 files     │ │ 5 files     │ │ 2 files     │             │
│ │ 2 days ago  │ │ 1 week ago  │ │ 3 days ago  │             │
│ └─────────────┘ └─────────────┘ └─────────────┘             │
│                                                             │
│ 📄 最近文件                                    [View all →]    │
│ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐             │
│ │🎨 Design    │ │🎨 Landing   │ │🎨 Mobile    │             │
│ │   System    │ │   Page      │ │   App       │             │
│ │ Project A   │ │ Project B   │ │ Project C   │             │
│ │ 1 hour ago  │ │ 2 hours ago │ │ 5 hours ago │             │
│ └─────────────┘ └─────────────┘ └─────────────┘             │
│                                                             │
│ 🎨 设计模板                                   [Browse all →]   │
│ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐             │
│ │📱 Mobile    │ │🖥️ Desktop   │ │📊 Dashboard │             │
│ │   App UI    │ │  Website    │ │  Template   │             │
│ └─────────────┘ └─────────────┘ └─────────────┘             │
└─────────────────────────────────────────────────────────────┘
```

### Home页面功能模块

```typescript
// Home页面组件
interface HomePageFeatures {
  // 项目管理
  projectManagement: {
    listRecentProjects(): Project[]
    createProject(name: string, template?: Template): Project
    openProject(projectId: string): void
    duplicateProject(projectId: string): void
    deleteProject(projectId: string): void
  }
  
  // 文件管理
  fileManagement: {
    listRecentFiles(): DesignFile[]
    createFile(projectId: string, name: string): DesignFile
    openFile(fileId: string): void
    moveFile(fileId: string, targetProjectId: string): void
    duplicateFile(fileId: string): void
    deleteFile(fileId: string): void
  }
  
  // 模板管理
  templateManagement: {
    listTemplates(): Template[]
    useTemplate(templateId: string): DesignFile
    createTemplate(fileId: string): Template
  }
  
  // 团队协作
  teamCollaboration: {
    inviteMembers(emails: string[]): void
    managePermissions(userId: string, permissions: Permission[]): void
    listTeamActivity(): Activity[]
  }
}
```

---

## 🎨 设计工作区 (File Editor)

### 完整界面布局

```
┌─────────────────────────────────────────────────────────────┐
│ ← Back  📄 Design System - Project A    [👁️ View] [Share ▼] │
├─────┬───────────────────────────────────────────────┬───────┤
│📄   │                                              │Design │
│Pages│                画布区域                        │      │
│     │ ┌─────────────────────────────────────────────┐│      │
│Page1│ │                                             ││      │
│●    │ │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ││Frame │
│Page2│ │  │ Frame 1  │  │ Frame 2  │  │ Frame 3  │  ││Name: │
│     │ │  │ Header   │  │ Content  │  │ Footer   │  ││Header│
│🌳   │ │  │ 1200×80  │  │ 1200×600 │  │ 1200×120 │  ││      │
│Layer│ │  └──────────┘  └──────────┘  └──────────┘  ││Width:│
│     │ │                                             ││1200px│
│🔍   │ │                                             ││      │
│Frame│ │                                             ││Height│
│├Logo│ │                                             ││80px  │
│├Nav │ │                                             ││      │
│└Menu│ │                                             ││Fill: │
│     │ └─────────────────────────────────────────────┘│#FFF  │
│🎨   │                                              │      │
│Asset│                                              │      │
│     │                                              │Proto │
│📦   │                                              │type  │
│Comp │                                              │      │
└─────┴───────────────────────────────────────────────┴───────┘
```

### 左侧面板结构

```typescript
// 左侧面板组件配置
interface LeftPanelStructure {
  // 页面管理
  pagesPanel: {
    currentFilePages: Page[]
    activePage: string
    createPage(name: string): Page
    deletePage(pageId: string): void
    renamePage(pageId: string, newName: string): void
    switchPage(pageId: string): void
  }
  
  // 图层树 (当前页面的Frame和Element层级)
  layersPanel: {
    layerTree: LayerNode[]          // 当前Page的完整层级树
    selectedLayers: string[]
    expandedNodes: string[]
    selectLayer(layerId: string, multi?: boolean): void
    expandNode(nodeId: string): void
    reorderLayers(fromIndex: number, toIndex: number): void
    toggleVisibility(layerId: string): void
    lockLayer(layerId: string): void
  }
  
  // 资源库
  assetsPanel: {
    images: ImageAsset[]
    icons: IconAsset[]
    colors: ColorAsset[]
    typography: TypographyAsset[]
    searchAssets(query: string): Asset[]
    addAsset(file: File): Asset
    useAsset(assetId: string): void
  }
  
  // 组件库
  componentsPanel: {
    localComponents: Component[]     // 当前文件的组件
    teamComponents: Component[]      // 团队共享组件
    publishedComponents: Component[] // 已发布的组件
    createComponent(frameId: string): Component
    createInstance(componentId: string): ComponentInstance
  }
}
```

### 图层树数据结构

```typescript
// 图层树节点 (对应Figma的Layers面板)
interface LayerNode {
  id: string
  name: string
  type: 'page' | 'frame' | 'group' | 'text' | 'rectangle' | 'ellipse' | 'image' | 'vector'
  
  // 层级关系
  parentId?: string
  children?: LayerNode[]
  depth: number
  
  // 显示状态
  visible: boolean
  locked: boolean
  selected: boolean
  expanded: boolean
  
  // 缩略图 (for frames)
  thumbnail?: string
  
  // 特殊标记
  isComponent: boolean
  isInstance: boolean
  hasWarnings: boolean
}

// 图层树操作
class LayerTreeManager {
  
  /**
   * 构建当前页面的图层树
   */
  buildLayerTree(page: Page): LayerNode[] {
    const rootNodes: LayerNode[] = []
    
    // 遍历页面中的所有Frame
    page.frames.forEach(frame => {
      const frameNode: LayerNode = {
        id: frame.id,
        name: frame.name,
        type: 'frame',
        depth: 0,
        visible: true,
        locked: false,
        selected: false,
        expanded: true,
        isComponent: frame.type === 'component',
        isInstance: frame.type === 'instance',
        hasWarnings: false,
        children: this.buildElementNodes(frame.children, 1)
      }
      rootNodes.push(frameNode)
    })
    
    return rootNodes
  }
  
  /**
   * 递归构建元素节点
   */
  private buildElementNodes(elements: FrameElement[], depth: number): LayerNode[] {
    return elements.map(element => ({
      id: element.id,
      name: element.name,
      type: element.type as any,
      depth,
      visible: true,
      locked: false,
      selected: false,
      expanded: false,
      isComponent: !!element.componentId,
      isInstance: !!element.componentId,
      hasWarnings: false,
      children: element.children ? this.buildElementNodes(element.children, depth + 1) : []
    }))
  }
}
```

---

## 🎯 右侧属性面板

### 属性面板布局

```typescript
// 右侧属性面板结构
interface RightPanelStructure {
  // 设计属性
  designPanel: {
    // 基础属性
    basicProperties: {
      position: { x: number, y: number }
      size: { width: number, height: number }
      rotation: number
      cornerRadius: number[]
      constraints: Constraints
    }
    
    // 填充样式
    fillProperties: {
      fills: Fill[]
      addFill(type: 'solid' | 'gradient' | 'image'): void
      removeFill(index: number): void
      reorderFills(fromIndex: number, toIndex: number): void
    }
    
    // 描边样式
    strokeProperties: {
      strokes: Stroke[]
      strokeAlign: 'inside' | 'center' | 'outside'
      strokeCap: StrokeCap
      strokeJoin: StrokeJoin
    }
    
    // 效果
    effectProperties: {
      effects: Effect[]
      addEffect(type: 'drop_shadow' | 'inner_shadow' | 'blur'): void
      removeEffect(index: number): void
    }
    
    // 文本属性 (当选中文本时)
    textProperties?: {
      fontFamily: string
      fontWeight: number
      fontSize: number
      lineHeight: number
      letterSpacing: number
      paragraphSpacing: number
      textAlign: TextAlign
      verticalAlign: VerticalAlign
    }
  }
  
  // 原型属性
  prototypePanel: {
    interactions: Interaction[]
    addInteraction(trigger: InteractionTrigger): void
    flowStartingPoint: boolean
    overflowBehavior: OverflowBehavior
  }
  
  // 检查面板
  inspectPanel: {
    cssCode: string
    iosCode: string
    androidCode: string
    exportSettings: ExportSetting[]
  }
}
```

---

## 🔧 核心功能实现

### Frame坐标系统

```typescript
// 精确复刻Figma的坐标系统
class FigmaCoordinateSystem {
  
  /**
   * Frame内元素的相对坐标转换为画布绝对坐标
   */
  frameRelativeToAbsolute(
    framePosition: { x: number, y: number },
    elementRelativePosition: { x: number, y: number }
  ): { x: number, y: number } {
    return {
      x: framePosition.x + elementRelativePosition.x,
      y: framePosition.y + elementRelativePosition.y
    }
  }
  
  /**
   * 画布绝对坐标转换为Frame内相对坐标
   */
  absoluteToFrameRelative(
    canvasPosition: { x: number, y: number },
    framePosition: { x: number, y: number }
  ): { x: number, y: number } {
    return {
      x: canvasPosition.x - framePosition.x,
      y: canvasPosition.y - framePosition.y
    }
  }
  
  /**
   * 检查点击是否在Frame内
   */
  isPointInFrame(
    clickPosition: { x: number, y: number },
    frame: Frame
  ): boolean {
    return clickPosition.x >= frame.position.x &&
           clickPosition.x <= frame.position.x + frame.size.width &&
           clickPosition.y >= frame.position.y &&
           clickPosition.y <= frame.position.y + frame.size.height
  }
  
  /**
   * 查找点击位置的目标元素 (支持嵌套)
   */
  findTargetElement(
    clickPosition: { x: number, y: number },
    frame: Frame
  ): FrameElement | null {
    if (!this.isPointInFrame(clickPosition, frame)) {
      return null
    }
    
    const relativePos = this.absoluteToFrameRelative(clickPosition, frame.position)
    return this.findElementRecursive(relativePos, frame.children)
  }
  
  private findElementRecursive(
    relativePos: { x: number, y: number },
    elements: FrameElement[]
  ): FrameElement | null {
    // 从后往前遍历 (层级高的优先)
    for (let i = elements.length - 1; i >= 0; i--) {
      const element = elements[i]
      
      if (this.isPointInElement(relativePos, element)) {
        // 如果有子元素，递归查找
        if (element.children && element.children.length > 0) {
          const childRelativePos = {
            x: relativePos.x - element.relativePosition.x,
            y: relativePos.y - element.relativePosition.y
          }
          const childResult = this.findElementRecursive(childRelativePos, element.children)
          return childResult || element
        }
        return element
      }
    }
    return null
  }
  
  private isPointInElement(
    relativePos: { x: number, y: number },
    element: FrameElement
  ): boolean {
    return relativePos.x >= element.relativePosition.x &&
           relativePos.x <= element.relativePosition.x + element.size.width &&
           relativePos.y >= element.relativePosition.y &&
           relativePos.y <= element.relativePosition.y + element.size.height
  }
}
```

### 拖拽系统

```typescript
// 拖拽管理器 (复刻Figma的拖拽体验)
class DragManager {
  private dragState: DragState | null = null
  
  /**
   * 开始拖拽
   */
  startDrag(
    target: FrameElement | Frame,
    startPosition: { x: number, y: number },
    dragType: 'move' | 'resize' | 'create'
  ): void {
    this.dragState = {
      target,
      startPosition,
      currentPosition: startPosition,
      dragType,
      startTime: Date.now()
    }
    
    document.addEventListener('mousemove', this.onDragMove)
    document.addEventListener('mouseup', this.onDragEnd)
  }
  
  /**
   * 拖拽过程中
   */
  private onDragMove = (event: MouseEvent): void => {
    if (!this.dragState) return
    
    const newPosition = { x: event.clientX, y: event.clientY }
    const deltaX = newPosition.x - this.dragState.startPosition.x
    const deltaY = newPosition.y - this.dragState.startPosition.y
    
    this.dragState.currentPosition = newPosition
    
    // 根据拖拽类型执行不同操作
    switch (this.dragState.dragType) {
      case 'move':
        this.handleMoveElement(deltaX, deltaY)
        break
      case 'resize':
        this.handleResizeElement(deltaX, deltaY)
        break
      case 'create':
        this.handleCreateElement(deltaX, deltaY)
        break
    }
  }
  
  /**
   * 结束拖拽
   */
  private onDragEnd = (): void => {
    if (this.dragState) {
      // 触发拖拽完成事件
      this.onDragComplete(this.dragState)
      this.dragState = null
    }
    
    document.removeEventListener('mousemove', this.onDragMove)
    document.removeEventListener('mouseup', this.onDragEnd)
  }
  
  private handleMoveElement(deltaX: number, deltaY: number): void {
    // 移动元素逻辑
    if (this.dragState?.target) {
      // 更新元素位置，考虑网格对齐
      const snappedDelta = this.snapToGrid(deltaX, deltaY)
      // 更新UI显示
      this.updateElementPosition(this.dragState.target, snappedDelta)
    }
  }
  
  private snapToGrid(deltaX: number, deltaY: number, gridSize: number = 8): { x: number, y: number } {
    return {
      x: Math.round(deltaX / gridSize) * gridSize,
      y: Math.round(deltaY / gridSize) * gridSize
    }
  }
}

interface DragState {
  target: FrameElement | Frame
  startPosition: { x: number, y: number }
  currentPosition: { x: number, y: number }
  dragType: 'move' | 'resize' | 'create'
  startTime: number
}
```

---

## 🚀 代码生成 (Frame级别)

### Frame to Code生成器

```typescript
// Frame级别的代码生成 (复刻Figma的Dev Mode)
class FrameCodeGenerator {
  
  /**
   * 生成Frame的Vue组件代码
   */
  generateVueComponent(frame: Frame): GeneratedCode {
    const componentName = this.sanitizeComponentName(frame.name)
    
    const template = this.generateTemplate(frame)
    const script = this.generateScript(frame)
    const styles = this.generateStyles(frame)
    
    return {
      fileName: `${componentName}.vue`,
      code: `<template>
${template}
</template>

<script setup lang="ts">
${script}
</script>

<style scoped>
${styles}
</style>`,
      dependencies: this.extractDependencies(frame)
    }
  }
  
  /**
   * 生成模板部分
   */
  private generateTemplate(frame: Frame): string {
    const frameClass = this.generateFrameClass(frame)
    const children = this.generateChildrenTemplate(frame.children, 1)
    
    return `  <div class="${frameClass}">
${children}
  </div>`
  }
  
  /**
   * 递归生成子元素模板
   */
  private generateChildrenTemplate(elements: FrameElement[], depth: number): string {
    const indent = '  '.repeat(depth + 1)
    
    return elements.map(element => {
      const elementTag = this.getElementTag(element)
      const elementClass = this.generateElementClass(element)
      const elementProps = this.generateElementProps(element)
      
      if (element.children && element.children.length > 0) {
        const childrenTemplate = this.generateChildrenTemplate(element.children, depth + 1)
        return `${indent}<${elementTag} class="${elementClass}"${elementProps}>
${childrenTemplate}
${indent}</${elementTag}>`
      } else {
        const content = this.getElementContent(element)
        return `${indent}<${elementTag} class="${elementClass}"${elementProps}>${content}</${elementTag}>`
      }
    }).join('\n')
  }
  
  /**
   * 生成CSS样式
   */
  private generateStyles(frame: Frame): string {
    const frameStyles = this.generateFrameStyles(frame)
    const elementStyles = this.generateElementStyles(frame.children)
    
    return `${frameStyles}

${elementStyles}`
  }
  
  /**
   * 生成Frame样式
   */
  private generateFrameStyles(frame: Frame): string {
    const className = this.sanitizeClassName(frame.name)
    
    return `.${className} {
  position: relative;
  width: ${frame.size.width}px;
  height: ${frame.size.height}px;
  ${this.generateFillCSS(frame.fills)}
  ${this.generateStrokeCSS(frame.strokes)}
  ${this.generateCornerRadiusCSS(frame.cornerRadius)}
  ${this.generateEffectCSS(frame.effects)}
}`
  }
  
  /**
   * 递归生成元素样式
   */
  private generateElementStyles(elements: FrameElement[]): string {
    return elements.map(element => {
      const className = this.sanitizeClassName(element.name)
      
      let styles = `.${className} {
  position: absolute;
  left: ${element.relativePosition.x}px;
  top: ${element.relativePosition.y}px;
  width: ${element.size.width}px;
  height: ${element.size.height}px;
  ${this.generateFillCSS(element.fills)}
  ${this.generateStrokeCSS(element.strokes)}
  ${this.generateCornerRadiusCSS(element.cornerRadius)}
  ${this.generateEffectCSS(element.effects)}
  ${element.type === 'text' ? this.generateTextCSS(element.textStyle) : ''}
}`
      
      // 递归处理子元素
      if (element.children && element.children.length > 0) {
        styles += '\n\n' + this.generateElementStyles(element.children)
      }
      
      return styles
    }).join('\n\n')
  }
}
```

这种精确复刻Figma的架构设计能够提供：

1. **🎯 完全一致的用户体验** - 设计师可以无缝从Figma迁移
2. **📂 清晰的文件组织** - Project → File → Page → Frame → Element 层级管理
3. **🔧 专业的编辑功能** - 左侧资源+图层树，右侧属性面板
4. **🚀 高质量代码输出** - Frame级别的组件化生成
5. **👥 团队协作支持** - 完整的项目和文件管理系统

这样的设计真正实现了**Universal X Designer**的愿景：让UX设计师在熟悉的环境中工作，并获得高质量的代码输出！ 