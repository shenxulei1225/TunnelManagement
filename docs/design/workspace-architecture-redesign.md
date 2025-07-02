# Universal X Designer 统一坐标系架构设计

## 📋 背景与问题

传统的多设计器模式存在以下问题：
1. **重复开发**：为不同设计工具创建多套设计器
2. **数据孤岛**：各设计器间数据难以共享和转换
3. **学习成本**：用户需要学习多套操作方式

## 🎯 解决方案：Figma式统一坐标系

### 核心理念
采用Figma的设计管理方式，实现真正的通用设计器：

#### 统一架构模型
- **Project 项目**：包含多个Page页面
- **Page 页面**：使用无限画布，统一坐标系管理
- **Frame 设计块**：等同于UMG Widget，具有独立坐标系
- **Component 组件**：Frame内的具体UI元素

#### 双坐标系设计
- **Page坐标系**：无限画布 (-∞, -∞) to (+∞, +∞)
- **Frame坐标系**：独立坐标系 (0,0) to (width, height)

## 🏗️ 架构设计

### 1. 统一项目结构

```
Universal X Designer
├── 工作台首页 (Workspace Home)
│   ├── 项目管理 (Project Manager)
│   └── 模板库 (Template Library)
│
└── 统一设计器 (Universal Designer)
    ├── 项目导航 (Project Navigator)
    │   ├── 页面列表 (Pages List)
    │   └── 组件库 (Component Library)
    │
    ├── 设计画布 (Design Canvas)
    │   ├── 无限画布 (Infinite Canvas)
    │   ├── Page坐标系 (Page Coordinate System)
    │   └── Frame管理 (Frame Management)
    │
    ├── Frame编辑器 (Frame Editor)
    │   ├── Frame坐标系 (Frame Coordinate System)
    │   ├── 组件编辑 (Component Editing)
    │   └── 标尺系统 (Ruler System - Shift+R)
    │
    └── 属性面板 (Properties Panel)
        ├── Frame属性 (Frame Properties)
        ├── 组件属性 (Component Properties)
        └── 导出设置 (Export Settings)
```

### 2. 核心数据结构

#### 项目结构 (Project Structure)
```typescript
interface Project {
  id: string
  name: string
  description?: string
  pages: Page[]
  assets: Asset[]
  componentLibrary: ComponentLibrary
  exportSettings: ExportSettings
  createdAt: Date
  updatedAt: Date
}

interface Page {
  id: string
  name: string
  frames: Frame[]
  // Page坐标系：无限画布
  canvasSize: {
    width: number     // 当前可视区域宽度
    height: number    // 当前可视区域高度
  }
  viewOffset: {
    x: number         // 视图偏移X
    y: number         // 视图偏移Y
  }
  zoom: number        // 缩放级别
}

interface Frame {
  id: string
  name: string
  type: 'web' | 'mobile' | 'desktop' | 'game' | 'component'
  
  // 在Page坐标系中的位置
  pagePosition: {
    x: number         // Page坐标系中的X位置
    y: number         // Page坐标系中的Y位置
  }
  
  // Frame自身的坐标系
  frameSize: {
    width: number     // Frame宽度
    height: number    // Frame高度
  }
  
  // Frame内的组件
  components: Component[]
  
  // 导出配置
  exportConfig: {
    format: 'umg' | 'vue' | 'react' | 'html' | 'json'
    targetResolution?: { width: number; height: number }
    responsive?: boolean
  }
}

interface Component {
  id: string
  type: ComponentType
  name: string
  
  // 在Frame坐标系中的位置
  position: {
    x: number         // Frame坐标系中的X位置
    y: number         // Frame坐标系中的Y位置
  }
  
  size: {
    width: number
    height: number
  }
  
  properties: UniversalProperties
  styles: UniversalStyles
  children?: Component[]
}
```

### 3. 坐标系转换机制

#### 双坐标系工作原理
```typescript
// Page坐标系：无限画布坐标系
interface PageCoordinate {
  x: number    // 可以是负数，支持无限画布
  y: number    // 可以是负数，支持无限画布
}

// Frame坐标系：独立的设计坐标系
interface FrameCoordinate {
  x: number    // 相对于Frame左上角的坐标 (0, 0)
  y: number    // 相对于Frame左上角的坐标 (0, 0)
}

// 坐标转换工具类
class CoordinateTransform {
  // Page坐标 → Frame坐标
  pageToFrame(pageCoord: PageCoordinate, frame: Frame): FrameCoordinate {
    return {
      x: pageCoord.x - frame.pagePosition.x,
      y: pageCoord.y - frame.pagePosition.y
    }
  }
  
  // Frame坐标 → Page坐标
  frameToPage(frameCoord: FrameCoordinate, frame: Frame): PageCoordinate {
    return {
      x: frameCoord.x + frame.pagePosition.x,
      y: frameCoord.y + frame.pagePosition.y
    }
  }
  
  // Frame坐标 → 导出坐标（根据导出格式）
  frameToExport(frameCoord: FrameCoordinate, exportConfig: ExportConfig): any {
    switch (exportConfig.format) {
      case 'umg':
        return this.frameToUMG(frameCoord, exportConfig)
      case 'vue':
        return this.frameToVue(frameCoord, exportConfig)
      case 'react':
        return this.frameToReact(frameCoord, exportConfig)
    }
  }
}
```

#### Frame类型与用途
```typescript
enum FrameType {
  DESKTOP = 'desktop',      // 桌面端设计 (1920x1080)
  MOBILE = 'mobile',        // 移动端设计 (375x812)
  TABLET = 'tablet',        // 平板设计 (768x1024)
  GAME_UI = 'game',         // 游戏UI (自定义分辨率)
  COMPONENT = 'component'   // 组件设计 (自适应大小)
}

// Frame预设模板
const FRAME_PRESETS = {
  [FrameType.DESKTOP]: { width: 1920, height: 1080 },
  [FrameType.MOBILE]: { width: 375, height: 812 },
  [FrameType.TABLET]: { width: 768, height: 1024 },
  [FrameType.GAME_UI]: { width: 1920, height: 1080 },
  [FrameType.COMPONENT]: { width: 300, height: 200 }
}
```

### 4. 统一设计器界面

#### 界面布局 (Figma风格)
```
┌─────────────────────────────────────────────────────────────────┐
│  Universal X Designer                                            │
├─────────────────────────────────────────────────────────────────┤
│ 首页 │ 用户中心 │ 商品管理 │ 订单管理 │ +                          │  ← Page标签
├─────────────────────────────────────────────────────────────────┤
│ 📁 Pages    │                                  │ 🎛️ Properties    │
│ └ 首页      │                                  │ ┌─────────────┐  │
│ └ 用户中心   │         无限画布区域                │ │ Frame Info  │  │
│ └ 商品管理   │                                  │ │ Name: 桌面版 │  │
│ └ 订单管理   │  ┌─────────────┐                │ │ Size: 1920x │  │
│            │  │   桌面版     │  ┌──────────┐   │ │      1080   │  │
│ 🧩 Components│  │  1920x1080  │  │  移动版   │   │ └─────────────┘  │
│ └ Button    │  │             │  │ 375x812  │   │                 │
│ └ Input     │  │    [UI组件]  │  │          │   │ 🎨 Styles       │
│ └ Card      │  │             │  │ [UI组件] │   │ ┌─────────────┐  │
│ └ Modal     │  └─────────────┘  └──────────┘   │ │ Background  │  │
│            │                                  │ │ Border      │  │
│ 📦 Assets   │         ┌─────────────┐         │ │ Shadow      │  │
│ └ Images    │         │   组件库     │         │ └─────────────┘  │
│ └ Icons     │         │  300x200    │         │                 │
│ └ Fonts     │         │             │         │ 📤 Export       │
│            │         │  [组件展示]  │         │ ┌─────────────┐  │
│            │         └─────────────┘         │ │ Format: UMG │  │
│            │                                  │ │ Target: UE5 │  │
│            │                                  │ └─────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## 🔧 技术实现

### 1. 模式切换器

```typescript
class WorkspaceModeManager {
  currentMode: 'web' | 'game' = 'web'
  
  switchMode(mode: 'web' | 'game', project: ProjectConfig) {
    if (mode === 'web') {
      this.initWebDesignMode(project)
    } else {
      this.initGameUIMode(project)
    }
  }
  
  private initWebDesignMode(project: ProjectConfig) {
    // 初始化Figma风格界面
    // 启用多页面管理
    // 设置响应式坐标系
  }
  
  private initGameUIMode(project: ProjectConfig) {
    // 初始化UMG风格界面
    // 启用单页面设计
    // 设置绝对坐标系
  }
}
```

### 2. 通用组件适配

```typescript
interface UniversalComponent {
  id: string
  type: ComponentType
  // 通用属性
  properties: UniversalProperties
  
  // 模式特定布局
  layout: {
    web?: WebLayoutSystem      // Web模式布局
    game?: GameUILayoutSystem  // 游戏模式布局
  }
  
  // 样式系统
  styles: UniversalStyles
}
```

### 3. 坐标系转换

```typescript
class CoordinateConverter {
  // Web模式 → 游戏模式
  webToGame(webLayout: WebLayoutSystem, viewport: ViewportConfig): GameUILayoutSystem {
    // 将相对坐标转换为绝对坐标
    // 处理响应式布局到固定布局的转换
  }
  
  // 游戏模式 → Web模式
  gameToWeb(gameLayout: GameUILayoutSystem, viewport: ViewportConfig): WebLayoutSystem {
    // 将绝对坐标转换为相对坐标
    // 生成响应式布局规则
  }
}
```

## 📊 对比分析

| 特性 | Web设计模式 | 游戏UI模式 |
|------|------------|-----------|
| **页面管理** | 多页面统一 | 单页面独立 |
| **坐标系** | 相对/响应式 | 绝对/固定 |
| **精度** | 布局级 | 像素级 |
| **复用性** | 高(组件库) | 中(模板) |
| **学习曲线** | 低(熟悉) | 中(专业) |
| **适用场景** | Web/移动应用 | 游戏UI |
| **导出格式** | HTML/CSS/Vue | UMG/Unity |

## 🎯 推荐策略

### 1. 默认模式选择
- **新用户**：默认Web设计模式(降低学习成本)
- **游戏开发者**：推荐游戏UI模式(专业需求)
- **混合项目**：支持模式切换

### 2. 渐进式引导
1. **入门**：从Web模式开始，熟悉基础概念
2. **进阶**：根据需求切换到游戏UI模式
3. **专家**：自由切换，混合使用

### 3. 兼容性保证
- 项目文件格式统一
- 组件在两种模式间可转换
- 导入导出支持双模式

## 🚀 实施计划

