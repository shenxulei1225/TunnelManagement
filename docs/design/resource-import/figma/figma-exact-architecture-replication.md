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



## 🎨 设计工作区 (File Editor)

### 完整界面布局

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

## 🔧 核心功能实现

### Frame坐标系统

### 拖拽系统

## 🚀 代码生成 (Frame级别)

### Frame to Code生成器

- 生成模板部分
- 递归生成子元素模板
- 生成CSS样式
- 生成Frame样式
- 递归生成元素样式
- 递归处理子元素

这种精确复刻Figma的架构设计能够提供：

1. **🎯 完全一致的用户体验** - 设计师可以无缝从Figma迁移
2. **📂 清晰的文件组织** - Project → File → Page → Frame → Element 层级管理
3. **🔧 专业的编辑功能** - 左侧资源+图层树，右侧属性面板
4. **🚀 高质量代码输出** - Frame级别的组件化生成
5. **👥 团队协作支持** - 完整的项目和文件管理系统

这样的设计真正实现了**Universal X Designer**的愿景：让UX设计师在熟悉的环境中工作，并获得高质量的代码输出！ 