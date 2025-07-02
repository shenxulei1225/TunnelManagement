# UE Widget控件库实现记录

## 📋 项目概述

### 目标
基于Unreal Engine UMG控件系统，使用Vue 3 + TypeScript构建完整的Web端控件库，提供与UE编辑器一致的用户体验。

### 技术栈
- **前端框架**: Vue 3 (Composition API)
- **类型系统**: TypeScript
- **样式方案**: CSS Modules + Scoped CSS
- **构建工具**: Vite
- **设计参考**: Unreal Engine UMG系统

## 🏗️ 架构设计

### 目录结构
```
tunnel-management-ui/src/components/Widget/
├── index.ts                    # 控件库统一导出入口
├── Types/                      # 类型定义模块
│   ├── UEWidgetTypes.ts       # 控件基础类型定义
│   ├── UELayoutTypes.ts       # 布局系统类型定义
│   └── UEStyleTypes.ts        # 样式系统类型定义
├── Utils/                      # 工具函数模块
│   ├── UEWidgetUtils.ts       # 控件通用工具函数
│   ├── UELayoutUtils.ts       # 布局计算工具函数
│   └── UEStyleUtils.ts        # 样式处理工具函数
├── Basic/                      # 基础控件模块
│   ├── UEButton.vue           # 按钮控件
│   ├── UEText.vue             # 文本控件 (待实现)
│   ├── UEImage.vue            # 图像控件 (待实现)
│   └── ...                    # 其他基础控件
├── Layout/                     # 布局控件模块
│   ├── UECanvasPanel.vue      # 画布面板
│   ├── UEVerticalBox.vue      # 垂直盒子 (待实现)
│   ├── UEHorizontalBox.vue    # 水平盒子 (待实现)
│   └── ...                    # 其他布局控件
├── Container/                  # 容器控件模块 (待实现)
├── Input/                      # 输入控件模块 (待实现)
└── Advanced/                   # 高级控件模块 (待实现)
```

### 设计原则
1. **类型安全优先**: 所有API都有完整的TypeScript类型定义
2. **模块化设计**: 按功能划分模块，便于维护和扩展
3. **UE原生体验**: 完全遵循UE的交互模式和视觉风格
4. **性能优化**: 使用Vue 3响应式系统，避免不必要的重渲染
5. **可扩展性**: 提供丰富的工具函数，便于自定义扩展

## 📊 核心类型系统

### 基础数据类型 (UEWidgetTypes.ts)

#### 数学类型
```typescript
// 二维向量 - UE中的基础数学类型
interface FVector2D {
  x: number
  y: number
}

// 线性颜色 - RGBA颜色表示 (0-1范围)
interface FLinearColor {
  r: number  // 红色分量
  g: number  // 绿色分量  
  b: number  // 蓝色分量
  a: number  // 透明度分量
}

// 边距定义 - 控件内外边距
interface FMargin {
  left: number
  top: number
  right: number
  bottom: number
}

// 几何体信息 - 控件的位置和大小信息
interface FGeometry {
  absolutePosition: FVector2D  // 绝对位置
  localSize: FVector2D         // 本地大小
  scale: number                // 缩放比例
}
```

#### 枚举类型
```typescript
// 水平对齐方式
enum EHorizontalAlignment {
  Fill = 'Fill',           // 填充
  Left = 'Left',           // 左对齐
  Center = 'Center',       // 居中对齐
  Right = 'Right'          // 右对齐
}

// 垂直对齐方式
enum EVerticalAlignment {
  Fill = 'Fill',           // 填充
  Top = 'Top',             // 顶部对齐
  Center = 'Center',       // 居中对齐
  Bottom = 'Bottom'        // 底部对齐
}

// 可见性状态 - 控制控件的显示和交互
enum ESlateVisibility {
  Visible = 'Visible',                    // 可见且可交互
  Collapsed = 'Collapsed',                // 折叠（不占用空间）
  Hidden = 'Hidden',                      // 隐藏（占用空间但不可见）
  HitTestInvisible = 'HitTestInvisible',  // 可见但不可交互
  SelfHitTestInvisible = 'SelfHitTestInvisible' // 自身不可交互但子控件可交互
}

// 按钮点击方法
enum EButtonClickMethod {
  DownAndUp = 'DownAndUp',       // 按下并释放
  MouseDown = 'MouseDown',       // 鼠标按下
  MouseUp = 'MouseUp',           // 鼠标释放
  PreciseClick = 'PreciseClick'  // 精确点击
}
```

#### 控件基础接口
```typescript
// 控件基础属性 - 所有控件的通用属性
interface UEWidgetBase {
  name?: string                    // 控件名称
  isEnabled?: boolean             // 是否启用
  visibility?: ESlateVisibility   // 可见性
  renderOpacity?: number          // 渲染透明度
  toolTipText?: string           // 工具提示文本
  cursor?: string                // 光标类型
}

// 面板控件槽位基础属性
interface UEPanelSlot {
  horizontalAlignment?: EHorizontalAlignment  // 水平对齐
  verticalAlignment?: EVerticalAlignment      // 垂直对齐
  padding?: FMargin                          // 边距
}
```

### 布局系统类型 (UELayoutTypes.ts)

#### 锚点系统
```typescript
// 锚点定义 - UE布局系统的核心概念
interface FAnchors {
  minimum: FVector2D  // 最小锚点 (0-1范围内的相对位置)
  maximum: FVector2D  // 最大锚点 (0-1范围内的相对位置)
}

// 槽位大小规则
enum ESlateSizeRule {
  Automatic = 'Automatic',  // 自动大小 - 根据内容调整
  Fill = 'Fill'            // 填充可用空间
}

// 槽位大小信息
interface FSlateChildSize {
  sizeRule: ESlateSizeRule  // 大小规则
  value: number            // 大小值 (当规则为Fill时使用)
}
```

#### 画布面板布局
```typescript
// 画布面板槽位数据
interface UECanvasPanelSlotData {
  anchors: FAnchors        // 锚点设置
  offsets: FMargin         // 偏移量
  alignment: FVector2D     // 对齐方式
  autoSize: boolean        // 是否自动计算大小
  zOrder: number          // Z轴顺序
}

// 画布面板布局信息
interface UECanvasLayoutInfo {
  position: FVector2D      // 控件在画布中的绝对位置
  size: FVector2D          // 控件大小
  anchors: FAnchors        // 控件的锚点信息
  alignment: FVector2D     // 对齐偏移
}
```

### 样式系统类型 (UEStyleTypes.ts)

#### 画刷系统
```typescript
// 画刷绘制类型
enum ESlateBrushDrawType {
  NoDrawType = 'NoDrawType',    // 不绘制
  Box = 'Box',                  // 盒子绘制
  Border = 'Border',            // 边框绘制
  Image = 'Image',              // 图像绘制
  RoundedBox = 'RoundedBox'     // 圆角盒子
}

// Slate画刷 - UE样式系统的核心
interface FSlateBrush {
  resourceType: ESlateBrushResourceType  // 资源类型
  resourceName?: string                  // 资源名称或路径
  imageSize: FVector2D                   // 图像大小
  margin: FMargin                        // 边距
  tintColor: FLinearColor               // 着色颜色
  drawAs: ESlateBrushDrawType           // 绘制类型
  tiling: ESlateBrushTileType           // 平铺类型
  mirroring: ESlateBrushMirrorType      // 镜像类型
  imageType: 'Linear' | 'Vector'        // 图像类型
  hasUObject: boolean                   // 是否有UObject资源
}
```

#### 字体和文本样式
```typescript
// 字体信息
interface FSlateFontInfo {
  fontFamilyName: string           // 字体族名称
  size: number                     // 字体大小
  typefaceFontName?: string        // 字体样式名称
  fontWeight: number               // 字体粗细
  fontStyle: 'Normal' | 'Italic' | 'Oblique'  // 字体倾斜
  fontStretch: number              // 字体拉伸
  fontMaterial?: string            // 字体材质
  outlineSettings?: {              // 轮廓设置
    outlineSize: number
    separateFillAlpha: boolean
    outlineColor: FLinearColor
  }
}

// 文本块样式
interface FTextBlockStyle {
  font: FSlateFontInfo                    // 字体信息
  colorAndOpacity: FLinearColor          // 文本颜色
  shadowOffset: FVector2D                // 阴影偏移
  shadowColorAndOpacity: FLinearColor    // 阴影颜色
  selectedBackgroundColor: FLinearColor  // 选中背景色
  highlightColor: FLinearColor           // 高亮颜色
  highlightShape: FSlateBrush            // 高亮形状
  strikeBrush: FSlateBrush              // 删除线画刷
  underlineBrush: FSlateBrush           // 下划线画刷
}
```

## 🛠️ 工具函数库

### 控件工具函数 (UEWidgetUtils.ts)

#### 数学运算函数
```typescript
// 向量操作
function createVector2D(x: number = 0, y: number = 0): FVector2D
function addVector2D(a: FVector2D, b: FVector2D): FVector2D
function subtractVector2D(a: FVector2D, b: FVector2D): FVector2D
function multiplyVector2D(v: FVector2D, scalar: number): FVector2D
function getVector2DLength(v: FVector2D): number
function normalizeVector2D(v: FVector2D): FVector2D
function lerpVector2D(a: FVector2D, b: FVector2D, t: number): FVector2D

// 几何计算
function isPointInRect(point: FVector2D, rectPos: FVector2D, rectSize: FVector2D): boolean
function doRectsIntersect(pos1: FVector2D, size1: FVector2D, pos2: FVector2D, size2: FVector2D): boolean
function getRectCenter(pos: FVector2D, size: FVector2D): FVector2D
function clampPointToRect(point: FVector2D, rectPos: FVector2D, rectSize: FVector2D): FVector2D
```

#### 颜色处理函数
```typescript
// 颜色创建和转换
function createLinearColor(r: number, g: number, b: number, a: number): FLinearColor
function createLinearColorFromRGB(r: number, g: number, b: number, a?: number): FLinearColor
function createLinearColorFromHex(hex: string, alpha?: number): FLinearColor
function linearColorToCss(color: FLinearColor): string
function linearColorToHex(color: FLinearColor): string
function lerpLinearColor(a: FLinearColor, b: FLinearColor, t: number): FLinearColor
```

#### 事件处理函数
```typescript
// 事件创建
function createMouseEvent(position: FVector2D, button: string, modifierKeys: object, widget?: any): UEMouseEvent
function createKeyEvent(key: string, modifierKeys: object, charCode?: number, widget?: any): UEKeyEvent
function createMouseEventFromDOM(domEvent: MouseEvent, widget?: any): UEMouseEvent
function createKeyEventFromDOM(domEvent: KeyboardEvent, widget?: any): UEKeyEvent
```

### 布局工具函数 (UELayoutUtils.ts)

#### 锚点操作函数
```typescript
// 锚点创建
function createAnchors(minX: number, minY: number, maxX: number, maxY: number): FAnchors
function createFillAnchors(): FAnchors                    // 占满父容器
function createCenterAnchors(): FAnchors                  // 居中锚点
function createTopLeftAnchors(): FAnchors                 // 左上角锚点
function createBottomRightAnchors(): FAnchors             // 右下角锚点
function createHorizontalStretchAnchors(y: number): FAnchors  // 水平拉伸
function createVerticalStretchAnchors(x: number): FAnchors    // 垂直拉伸

// 锚点分析
function isAnchorStretched(anchors: FAnchors): {horizontal: boolean, vertical: boolean}
function getAnchorCenter(anchors: FAnchors): FVector2D
```

#### 布局计算函数
```typescript
// 画布布局计算
function calculateCanvasLayout(
  slotData: UECanvasPanelSlotData, 
  parentSize: FVector2D, 
  childDesiredSize: FVector2D
): UECanvasLayoutInfo

// 盒子布局计算
function calculateVerticalBoxLayout(
  slots: UEVerticalBoxSlotData[], 
  childDesiredSizes: FVector2D[], 
  availableSize: FVector2D
): FVector2D[]

function calculateHorizontalBoxLayout(
  slots: UEHorizontalBoxSlotData[], 
  childDesiredSizes: FVector2D[], 
  availableSize: FVector2D
): FVector2D[]
```

### 样式工具函数 (UEStyleUtils.ts)

#### 画刷创建函数
```typescript
// 各种画刷创建
function createEmptyBrush(): FSlateBrush
function createColorBrush(color: FLinearColor): FSlateBrush
function createImageBrush(resourceName: string, imageSize?: FVector2D, tintColor?: FLinearColor): FSlateBrush
function createBorderBrush(resourceName: string, margin: FMargin, tintColor?: FLinearColor): FSlateBrush
function createRoundedBoxBrush(color: FLinearColor, cornerRadius?: number): FSlateBrush

// 样式转换
function brushToCss(brush: FSlateBrush): Partial<CSSStyleDeclaration>
```

#### 主题系统函数
```typescript
// 主题创建
function createDefaultTheme(): UETheme
function createDarkTheme(): UETheme
function createDefaultThemeColors(): UEThemeColors
function createDarkThemeColors(): UEThemeColors

// 主题应用
function generateCssVariables(theme: UETheme): string
function applyThemeToDOM(theme: UETheme): void
```

## 🎨 已实现控件

### UEButton 控件

#### 功能特性
- **多种变体**: default、primary、success、danger
- **三种尺寸**: small、medium、large
- **完整状态管理**: normal、hovered、pressed、disabled
- **UE风格交互**: 支持DownAndUp、MouseDown、MouseUp、PreciseClick等点击方法
- **视觉效果**: 悬停效果、按压反馈、波纹动画
- **可见性控制**: 支持UE的所有可见性状态
- **完全可定制**: 支持自定义样式和CSS类

#### 使用示例
```vue
<template>
  <!-- 基础按钮 -->
  <UEButton text="默认按钮" />
  
  <!-- 主要按钮 -->
  <UEButton 
    text="主要按钮" 
    variant="primary" 
    size="large"
    @click="handleClick"
  />
  
  <!-- 自定义按钮 -->
  <UEButton 
    variant="success"
    :click-method="EButtonClickMethod.MouseDown"
    :render-opacity="0.8"
    custom-class="my-button"
  >
    <Icon name="check" />
    确认
  </UEButton>
</template>
```

#### 技术实现
- **响应式状态管理**: 使用Vue 3 Composition API管理按钮状态
- **计算属性优化**: 动态计算样式和类名，避免不必要的重渲染
- **事件系统**: 完整的鼠标和键盘事件处理
- **样式系统**: 基于UE画刷系统的动态样式生成

### UECanvasPanel 控件

#### 功能特性
- **绝对定位布局**: 基于UE锚点系统的精确控件定位
- **Z轴层级管理**: 支持控件的前后层级排序
- **网格系统**: 可视化网格显示和控件吸附功能
- **动态响应**: 自动响应容器大小变化，重新计算布局
- **交互支持**: 预留拖拽、调整大小等交互接口
- **性能优化**: 使用ResizeObserver监听大小变化

#### 使用示例
```vue
<template>
  <UECanvasPanel 
    :show-grid="true"
    :grid-size="20"
    :enable-snap="true"
    :children="canvasChildren"
    @child-click="handleChildClick"
    @child-move="handleChildMove"
  />
</template>

<script setup>
import { UEButton, UECanvasPanel } from '@/components/Widget'
import { createCanvasPanelSlot, createTopLeftAnchors } from '@/components/Widget/Utils/UELayoutUtils'

const canvasChildren = ref([
  {
    key: 'button1',
    component: UEButton,
    props: {
      text: '按钮1',
      variant: 'primary'
    },
    slot: createCanvasPanelSlot(
      createVector2D(100, 100),  // 位置
      createVector2D(120, 40),   // 大小
      createTopLeftAnchors(),    // 锚点
      createVector2D(0, 0),      // 对齐
      false,                     // 自动大小
      1                          // Z顺序
    ),
    desiredSize: createVector2D(120, 40)
  }
])
</script>
```

#### 技术实现
- **锚点布局算法**: 完整实现UE的锚点定位计算
- **动态子控件渲染**: 使用Vue的动态组件系统
- **性能优化**: 计算属性缓存和ResizeObserver优化
- **事件委托**: 高效的子控件事件管理

## 📈 开发进度

### ✅ 已完成
1. **核心架构设计** - 完整的模块化架构
2. **类型系统** - 全面的TypeScript类型定义
3. **工具函数库** - 丰富的数学、颜色、布局、样式工具
4. **UEButton控件** - 功能完整的按钮组件
5. **UECanvasPanel控件** - 高级画布布局面板
6. **主题系统** - 支持亮色和暗色主题
7. **文档系统** - 完整的API文档和使用指南

### 🚧 进行中
1. **控件库扩展** - 继续添加更多基础控件
2. **交互系统** - 拖拽、调整大小等高级交互
3. **动画系统** - 更丰富的过渡和动画效果

### 📋 待实现
1. **基础控件**
   - UEText - 文本显示控件
   - UEImage - 图像显示控件
   - UETextBox - 文本输入控件
   - UECheckBox - 复选框控件
   - UESlider - 滑块控件
   - UEProgressBar - 进度条控件

2. **布局控件**
   - UEVerticalBox - 垂直盒子布局
   - UEHorizontalBox - 水平盒子布局
   - UEGridPanel - 网格面板布局
   - UEScrollBox - 滚动盒子
   - UESizeBox - 尺寸盒子
   - UEBorder - 边框容器

3. **容器控件**
   - UEUserWidget - 用户控件基类
   - UEWidgetSwitcher - 控件切换器
   - UETabWidget - 标签页控件
   - UEScrollViewer - 滚动查看器

4. **输入控件**
   - UEEditableText - 可编辑文本
   - UEMultiLineEditableText - 多行文本编辑
   - UESpinBox - 数值输入框
   - UEComboBox - 下拉组合框

5. **高级控件**
   - UEListView - 列表视图
   - UETreeView - 树形视图
   - UEMenuBar - 菜单栏
   - UEToolbar - 工具栏

## 🎯 技术亮点

### 1. 完全的UE兼容性
- **API设计**: 完全遵循UE UMG的API设计模式
- **行为一致**: 控件行为与UE编辑器完全一致
- **视觉风格**: 忠实还原UE的视觉设计语言

### 2. 现代前端技术栈
- **Vue 3 Composition API**: 充分利用Vue 3的响应式系统
- **TypeScript**: 100%类型覆盖，提供完整的智能提示
- **模块化设计**: 清晰的模块划分，便于维护和扩展

### 3. 高性能优化
- **计算属性缓存**: 避免不必要的重复计算
- **事件优化**: 高效的事件处理和委托机制
- **内存管理**: 合理的组件生命周期管理

### 4. 开发体验优化
- **丰富的工具函数**: 提供大量便捷的工具函数
- **完整的类型提示**: TypeScript提供完整的智能提示
- **模块化导入**: 支持按需导入，减少包体积

## 🔧 使用指南

### 安装和导入
```typescript
// 完整导入
import * as UEWidget from '@/components/Widget'

// 按需导入
import { UEButton, UECanvasPanel } from '@/components/Widget'
import { createVector2D, createLinearColor } from '@/components/Widget/Utils/UEWidgetUtils'
import { createTopLeftAnchors, calculateCanvasLayout } from '@/components/Widget/Utils/UELayoutUtils'
```

### 基础使用
```vue
<template>
  <div class="widget-demo">
    <!-- 简单按钮 -->
    <UEButton text="点击我" @click="handleClick" />
    
    <!-- 画布布局 -->
    <UECanvasPanel 
      :children="widgets"
      :show-grid="true"
      style="width: 800px; height: 600px;"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { UEButton, UECanvasPanel } from '@/components/Widget'
import { createVector2D } from '@/components/Widget/Utils/UEWidgetUtils'
import { createCanvasPanelSlot, createCenterAnchors } from '@/components/Widget/Utils/UELayoutUtils'

const widgets = ref([
  {
    key: 'center-button',
    component: UEButton,
    props: {
      text: '居中按钮',
      variant: 'primary',
      size: 'large'
    },
    slot: createCanvasPanelSlot(
      createVector2D(0, 0),
      createVector2D(150, 50),
      createCenterAnchors()
    )
  }
])

const handleClick = () => {
  console.log('按钮被点击了!')
}
</script>
```

### 高级使用
```vue
<template>
  <UECanvasPanel 
    ref="canvasRef"
    :children="dynamicWidgets"
    :show-grid="showGrid"
    :enable-snap="enableSnap"
    @child-move="handleWidgetMove"
    @child-resize="handleWidgetResize"
  />
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { UEButton, UECanvasPanel } from '@/components/Widget'
import type { FVector2D } from '@/components/Widget/Types/UEWidgetTypes'

const canvasRef = ref<InstanceType<typeof UECanvasPanel>>()
const showGrid = ref(true)
const enableSnap = ref(true)

const dynamicWidgets = ref([
  // 动态控件配置...
])

// 添加新控件
const addWidget = (type: string, position: FVector2D) => {
  const newWidget = {
    key: `widget_${Date.now()}`,
    component: UEButton,
    props: { text: `新按钮` },
    slot: createCanvasPanelSlot(position, createVector2D(100, 32))
  }
  
  canvasRef.value?.addChild(newWidget)
}

// 处理控件移动
const handleWidgetMove = (child: any, newPosition: FVector2D) => {
  console.log(`控件 ${child.key} 移动到:`, newPosition)
}
</script>
```

## 🎨 主题定制

### 使用预设主题
```typescript
import { createDefaultTheme, createDarkTheme, applyThemeToDOM } from '@/components/Widget/Utils/UEStyleUtils'

// 应用默认主题
const defaultTheme = createDefaultTheme()
applyThemeToDOM(defaultTheme)

// 应用暗色主题
const darkTheme = createDarkTheme()
applyThemeToDOM(darkTheme)
```

### 自定义主题
```typescript
import { createDefaultTheme } from '@/components/Widget/Utils/UEStyleUtils'
import { createLinearColorFromHex } from '@/components/Widget/Utils/UEWidgetUtils'

const customTheme = {
  ...createDefaultTheme(),
  name: 'Custom',
  colors: {
    ...createDefaultTheme().colors,
    primary: createLinearColorFromHex('#ff6b6b'),
    secondary: createLinearColorFromHex('#4ecdc4'),
    success: createLinearColorFromHex('#45b7d1')
  }
}

applyThemeToDOM(customTheme)
```

## 📚 最佳实践

### 1. 类型安全
```typescript
// 推荐：使用完整的类型定义
interface MyWidgetProps extends UEWidgetBase {
  customProp: string
}

// 推荐：使用工具函数创建数据
const position = createVector2D(100, 200)
const color = createLinearColorFromHex('#1976d2')
const anchors = createCenterAnchors()
```

### 2. 性能优化
```typescript
// 推荐：使用计算属性缓存复杂计算
const layoutInfo = computed(() => 
  calculateCanvasLayout(props.slot, canvasSize.value, desiredSize.value)
)

// 推荐：避免在模板中进行复杂计算
const widgetStyle = computed(() => ({
  left: `${layoutInfo.value.position.x}px`,
  top: `${layoutInfo.value.position.y}px`
}))
```

### 3. 组件设计
```typescript
// 推荐：继承基础控件属性
interface CustomWidgetProps extends UEWidgetBase {
  // 自定义属性
}

// 推荐：使用统一的事件命名
const emit = defineEmits<{
  click: [event: MouseEvent]
  change: [value: any]
  'update:modelValue': [value: any]
}>()
```

## 🚀 后续规划

### 短期目标 (1-2周)
1. 完成基础控件集合 (UEText, UEImage, UETextBox等)
2. 实现基础布局控件 (UEVerticalBox, UEHorizontalBox)
3. 添加拖拽和调整大小交互功能
4. 完善文档和示例

### 中期目标 (1个月)
1. 完成所有核心控件的实现
2. 添加高级交互功能 (多选、复制粘贴等)
3. 实现控件序列化和反序列化
4. 添加撤销/重做功能

### 长期目标 (3个月)
1. 完整的UE控件编辑器
2. 可视化界面设计器
3. 代码生成功能
4. 插件系统

## 📖 参考资料

### UE官方文档
- [UMG UI Designer User Guide](https://docs.unrealengine.com/4.27/en-US/InteractiveExperiences/UMG/)
- [Slate UI Framework](https://docs.unrealengine.com/4.27/en-US/ProgrammingAndScripting/Slate/)

### 技术文档
- [Vue 3 Composition API](https://vuejs.org/guide/extras/composition-api-faq.html)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)

### 设计参考
- [Material Design](https://material.io/design)
- [Ant Design](https://ant.design/)
- [Element Plus](https://element-plus.org/)

---

**文档版本**: v1.0  
**最后更新**: 2024年12月  
**维护者**: 开发团队 