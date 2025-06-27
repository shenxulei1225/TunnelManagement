# UE设计器视口实现文档

## 概述

本文档记录了基于Unreal Engine (UE) UMG编辑器实现的设计器视口组件 `DesignerViewport.vue` 的完整架构和实现细节。

## 核心架构

### UE源码参考
- **文件位置**: `/Users/kevin/Documents/Github/UnrealEngine/Engine/Source/Editor/UMGEditor`
- **核心文件**:
  - `SRuler.cpp` - 标尺组件实现
  - `SDesignSurface.cpp` - 设计表面实现  
  - `SDesignerView.cpp` - 设计视图主容器

### 组件层次结构

```
ue-designer-container
├── ue-toolbar (工具栏)
│   ├── toolbar-section (文件操作)
│   ├── toolbar-section (设备模式)
│   └── toolbar-section (缩放控制)
└── ue-designer-view (主设计视图)
    ├── ue-ruler-horizontal (水平标尺)
    ├── ue-ruler-vertical (垂直标尺)
    ├── ue-ruler-corner (标尺角落)
    └── ue-design-surface (设计表面)
        ├── ue-grid (网格背景)
        ├── ue-debug-point (调试点)
        └── ue-canvas-widget (画布控件)
            ├── ue-canvas-content (画布内容)
            └── ue-resize-handles (调整手柄)
```

## 核心坐标系统

### UE坐标转换公式

基于UE源码中的核心转换算法：

```typescript
// 图形空间到屏幕空间转换
// UE公式: ScreenSpace = (GraphSpace - ViewOffset) * ZoomAmount
const graphSpaceToScreenSpace = (graphCoord: FVector2D): FVector2D => {
  const viewOff = getViewOffset()
  const zoom = getZoomAmount()
  return {
    x: (graphCoord.x - viewOff.x) * zoom,
    y: (graphCoord.y - viewOff.y) * zoom
  }
}

// 屏幕空间到图形空间转换
// UE公式: GraphSpace = ScreenSpace / ZoomAmount + ViewOffset
const screenSpaceToGraphSpace = (screenCoord: FVector2D): FVector2D => {
  const viewOff = getViewOffset()
  const zoom = getZoomAmount()
  return {
    x: screenCoord.x / zoom + viewOff.x,
    y: screenCoord.y / zoom + viewOff.y
  }
}
```

### 坐标系统概念

- **坐标系**: 抽象的数学坐标系统，有X轴和Y轴，永远不动
- **设计表面**: 坐标系中的一个工作区域，与坐标系固定不动  
- **视口**: 一个"窗口"，可以在坐标系上移动查看不同区域
- **画布**: 坐标系中(0,0)位置的一个控件
- **其他控件**: 都有各自在坐标系中的固定坐标

### 交互行为本质

当拖动视口时，实际是改变坐标系相对于视口的显示位置。视觉效果看起来像设计表面在移动，但实际是视口的"观察位置"在坐标系中移动。

## UE标尺系统 (SRuler)

### 标尺配置

基于缩放级别的动态刻度配置：

```typescript
const RULER_CONFIGS = [
  { minZoom: 0.1, maxZoom: 0.25, majorTick: 500, minorTick: 100 },
  { minZoom: 0.25, maxZoom: 0.5, majorTick: 200, minorTick: 50 },
  { minZoom: 0.5, maxZoom: 0.8, majorTick: 100, minorTick: 25 },
  { minZoom: 0.8, maxZoom: 1.5, majorTick: 50, minorTick: 10 },
  { minZoom: 1.5, maxZoom: 2.5, majorTick: 25, minorTick: 5 },
  { minZoom: 2.5, maxZoom: 10.0, majorTick: 10, minorTick: 2 }
]
```

### 标尺刻度计算

1. **获取可见范围**: 将屏幕边界转换到图形空间
2. **计算起始刻度**: 根据主刻度间隔对齐
3. **生成刻度**: 在可见范围内生成所有主刻度
4. **屏幕定位**: 将图形空间刻度转换回屏幕位置

```typescript
// 水平标尺刻度计算示例
const leftScreen = { x: 0, y: 0 }
const rightScreen = { x: rulerWidth, y: 0 }
const leftGraph = screenSpaceToGraphSpace(leftScreen)
const rightGraph = screenSpaceToGraphSpace(rightScreen)

const startTick = Math.floor(leftGraph.x / config.majorTick) * config.majorTick
const endTick = Math.ceil(rightGraph.x / config.majorTick) * config.majorTick
```

### 标尺样式

- **水平标尺**: 位于顶部，显示X轴坐标
- **垂直标尺**: 位于左侧，显示Y轴坐标  
- **标尺角落**: 左上角20x20像素区域
- **刻度数字**: 动态显示/隐藏，避免重叠

## UE设计表面 (SDesignSurface)

### 网格系统

- **网格大小**: 基础10像素，随缩放调整
- **网格对齐**: 基于坐标系原点位置计算偏移
- **透明度**: 根据缩放级别动态调整 (0.2-0.8)

```typescript
// 网格样式计算
const gridPixels = GRID_SIZE * zoom
const gridOrigin = graphSpaceToScreenSpace({ x: 0, y: 0 })
const offsetX = gridOrigin.x % gridPixels
const offsetY = gridOrigin.y % gridPixels
```

### 视口平移

UE的反向偏移逻辑：拖拽时移动视图偏移的相反方向

```typescript
// 平移计算
const deltaX = event.clientX - dragStartPos.value.x
const deltaY = event.clientY - dragStartPos.value.y
const zoom = getZoomAmount()

viewOffset.value = {
  x: dragStartOffset.value.x - deltaX / zoom,
  y: dragStartOffset.value.y - deltaY / zoom
}
```

### 鼠标滚轮缩放

向鼠标位置缩放，保持鼠标下的点位置不变：

```typescript
// 缩放前后的图形空间坐标
const graphPointBefore = screenSpaceToGraphSpace({ x: mouseX, y: mouseY })
zoomLevel.value = newZoom
const graphPointAfter = screenSpaceToGraphSpace({ x: mouseX, y: mouseY })

// 调整视图偏移补偿位置变化
viewOffset.value = {
  x: viewOffset.value.x + (graphPointAfter.x - graphPointBefore.x),
  y: viewOffset.value.y + (graphPointAfter.y - graphPointBefore.y)
}
```

## UE画布控件 (Canvas Widget)

### 画布定位

画布在坐标系中的位置通过props传入，使用坐标转换公式计算屏幕位置：

```typescript
const canvasGraphPos = { x: props.canvasPanel.x, y: props.canvasPanel.y }
const canvasScreenPos = graphSpaceToScreenSpace(canvasGraphPos)
const zoom = getZoomAmount()

// 画布样式
{
  position: 'absolute',
  left: `${canvasScreenPos.x}px`,
  top: `${canvasScreenPos.y}px`,
  width: `${props.canvasPanel.width * zoom}px`,
  height: `${props.canvasPanel.height * zoom}px`
}
```

### 调整手柄系统

8个方向的调整手柄，支持画布大小调整：

```typescript
const resizeHandles = [
  { direction: 'nw' }, { direction: 'n' }, { direction: 'ne' },
  { direction: 'w' }, { direction: 'e' },
  { direction: 'sw' }, { direction: 's' }, { direction: 'se' }
]
```

### 调整逻辑

1. **记录初始状态**: 鼠标位置、画布位置和尺寸
2. **计算增量**: 屏幕空间增量转换为图形空间增量
3. **应用调整**: 根据调整方向更新画布属性
4. **约束检查**: 最小尺寸100像素

```typescript
// 屏幕增量转图形空间增量
const graphDeltaX = deltaX / zoom
const graphDeltaY = deltaY / zoom

// 东向调整示例
if (direction.includes('e')) {
  newWidth = Math.max(100, resizeStartData.value.canvasWidth + graphDeltaX)
}
```

## 工具栏功能

### 文件操作
- **新建页面**: 创建新的设计页面
- **打开页面**: 打开已有页面
- **保存页面**: 保存当前设计

### 视图操作
- **重置视图**: 恢复默认视图状态 (viewOffset: {x: -150, y: -150}, zoom: 0.5)
- **适应画布**: 自动调整缩放和位置以适应画布大小

### 设备模式
- **PC模式**: 1920px宽度
- **平板模式**: 768px宽度  
- **手机模式**: 375px宽度

### 缩放控制
- **滑块**: 0.1-3.0倍缩放，步长0.1
- **显示**: 百分比显示，整数格式
- **滚轮**: ±0.1倍增量

## 调试功能

### 坐标验证点

两个调试点用于验证坐标系统正确性：

- **红色原点**: 图形空间 (0,0) 位置
- **蓝色参考点**: 图形空间 (100,100) 位置

```typescript
// 调试点样式计算
const getDebugPointStyle = (x: number, y: number) => {
  const screenPos = graphSpaceToScreenSpace({ x, y })
  return {
    position: 'absolute',
    left: `${screenPos.x - 5}px`,
    top: `${screenPos.y - 5}px`,
    width: '10px',
    height: '10px',
    background: x === 0 && y === 0 ? '#ff0000' : '#0066ff'
  }
}
```

## 事件处理

### 鼠标事件层次

1. **视图级别**: 背景拖拽平移
2. **表面级别**: 取消画布选择
3. **画布级别**: 选择画布，阻止事件冒泡
4. **手柄级别**: 调整大小，阻止事件冒泡

### 事件传播控制

使用 `event.stopPropagation()` 控制事件传播，确保正确的交互层次。

## 样式系统

### UE主题色彩

- **背景色**: #1e1e1e (主容器), #2d2d30 (工具栏/设计表面)
- **边框色**: #3e3e42 (分割线), #555 (标尺边框)
- **文字色**: #eee (标尺文字), #cccccc (缩放显示)
- **强调色**: #007ACC (选择状态), #66b1ff (悬停状态)

### 响应式设计

- **最小尺寸**: 工具栏48px高度
- **弹性布局**: flex布局适应不同屏幕
- **缩放适配**: 所有元素支持高DPI显示

## 性能优化

### 计算缓存

使用Vue的computed属性缓存复杂计算：
- 标尺刻度计算
- 画布样式计算
- 网格样式计算

### 事件节流

鼠标移动事件通过浏览器原生requestAnimationFrame节流。

### 渲染优化

- 标尺刻度visibility控制，避免过多DOM元素
- 网格使用CSS背景图，不创建DOM元素
- 调整手柄仅在选择时显示

## 扩展接口

### Props接口

```typescript
interface Props {
  mode: 'pc' | 'tablet' | 'mobile'  // 设备模式
  canvasPanel: {                    // 画布配置
    width: number                   // 画布宽度
    height: number                  // 画布高度
    x: number                       // 画布X坐标
    y: number                       // 画布Y坐标
  }
}
```

### Events接口

```typescript
const emit = defineEmits<{
  'update:mode': [mode: 'pc' | 'tablet' | 'mobile']
  'update:canvas-panel': [panel: { width: number; height: number; x: number; y: number }]
  'drop': [event: DragEvent]
  'new-page': []
  'open-page': []
  'save-page': []
}>()
```

### 插槽接口

- **components**: 画布内容插槽，用于渲染实际组件

## 使用示例

```vue
<template>
  <DesignerViewport
    :mode="currentMode"
    :canvas-panel="canvasConfig"
    @update:mode="handleModeChange"
    @update:canvas-panel="handleCanvasChange"
    @drop="handleDrop"
    @new-page="handleNewPage"
    @open-page="handleOpenPage"
    @save-page="handleSavePage"
  >
    <template #components>
      <!-- 在这里渲染实际的UI组件 -->
    </template>
  </DesignerViewport>
</template>
```

## 技术栈

- **Vue 3**: Composition API + TypeScript
- **Element Plus**: UI组件库
- **CSS**: 原生CSS，UE风格主题
- **TypeScript**: 类型安全

## 兼容性

- **浏览器**: Chrome 88+, Firefox 85+, Safari 14+
- **设备**: 桌面端优先，支持触摸设备
- **分辨率**: 支持高DPI显示

## 维护说明

### 代码组织

- 按功能模块组织代码块
- 中文注释说明关键逻辑
- TypeScript类型定义完整

### 调试建议

1. 使用调试点验证坐标转换
2. 检查标尺刻度对齐
3. 测试各种缩放级别下的行为
4. 验证事件传播层次

### 常见问题

1. **坐标偏移**: 检查viewOffset初始值
2. **标尺错位**: 验证坐标转换公式
3. **性能问题**: 检查computed依赖
4. **事件冲突**: 验证stopPropagation调用

---

*文档版本: 1.0*  
*最后更新: 2024年12月*  
*作者: AI Assistant* 