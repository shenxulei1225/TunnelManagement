# UMG控件属性与Vue/CSS样式映射关系

## 概述

本文档定义了Unreal Engine UMG控件属性与Vue/CSS样式之间的映射关系，确保在转换过程中能够准确地将UMG设计转换为Web界面。

## 1. 基础布局属性映射

### 1.1 位置和尺寸 (Transform & Size)

| UMG属性 | 类型 | Vue/CSS属性 | 转换规则 | 备注 |
|---------|------|-------------|----------|------|
| `Slot.Anchors.Minimum` | Vector2D | `position`, `left`, `top` | 锚点 * 父容器尺寸 + 偏移 | 锚点系统核心 |
| `Slot.Anchors.Maximum` | Vector2D | `position`, `right`, `bottom` | 锚点 * 父容器尺寸 + 偏移 | 锚点系统核心 |
| `Slot.Offsets.Left` | Float | `left` | 直接像素值 | Canvas Panel专用 |
| `Slot.Offsets.Top` | Float | `top` | 直接像素值 | Canvas Panel专用 |
| `Slot.Offsets.Right` | Float | `width` 或 `right` | 计算宽度或右边距 | 取决于锚点模式 |
| `Slot.Offsets.Bottom` | Float | `height` 或 `bottom` | 计算高度或下边距 | 取决于锚点模式 |
| `RenderTransform.Translation` | Vector2D | `transform: translate()` | 额外的位移变换 | 叠加在布局之上 |
| `RenderTransform.Scale` | Vector2D | `transform: scale()` | 缩放变换 | 影响视觉尺寸 |
| `RenderTransform.Angle` | Float | `transform: rotate()` | 旋转变换 | 角度转弧度 |

### 1.2 对齐方式 (Alignment)

| UMG属性 | 枚举值 | Vue/CSS属性 | CSS值 | 备注 |
|---------|--------|-------------|-------|------|
| `Slot.HorizontalAlignment` | `HAlign_Left` | `justify-content` | `flex-start` | Flex容器中 |
| `Slot.HorizontalAlignment` | `HAlign_Center` | `justify-content` | `center` | Flex容器中 |
| `Slot.HorizontalAlignment` | `HAlign_Right` | `justify-content` | `flex-end` | Flex容器中 |
| `Slot.HorizontalAlignment` | `HAlign_Fill` | `width` | `100%` | 填充模式 |
| `Slot.VerticalAlignment` | `VAlign_Top` | `align-items` | `flex-start` | Flex容器中 |
| `Slot.VerticalAlignment` | `VAlign_Center` | `align-items` | `center` | Flex容器中 |
| `Slot.VerticalAlignment` | `VAlign_Bottom` | `align-items` | `flex-end` | Flex容器中 |
| `Slot.VerticalAlignment` | `VAlign_Fill` | `height` | `100%` | 填充模式 |

## 2. 控件特定属性映射

### 2.1 Button (按钮)

| UMG属性 | 类型 | Vue/CSS属性 | 转换规则 | 示例 |
|---------|------|-------------|----------|------|
| `Style.Normal.Tint` | LinearColor | `background-color` | RGBA转换 | `rgba(255,255,255,1)` |
| `Style.Hovered.Tint` | LinearColor | `:hover background-color` | RGBA转换 | 悬停状态 |
| `Style.Pressed.Tint` | LinearColor | `:active background-color` | RGBA转换 | 按下状态 |
| `Style.Disabled.Tint` | LinearColor | `:disabled background-color` | RGBA转换 | 禁用状态 |
| `Style.Normal.DrawAs` | ESlateBrushDrawType | `background-image` | 图片或渐变 | 背景类型 |
| `Style.Normal.Margin` | FMargin | `padding` | 边距转换 | 内边距 |
| `ColorAndOpacity` | LinearColor | `opacity` | Alpha值 | 整体透明度 |
| `IsEnabled` | Boolean | `disabled` | 布尔转换 | 启用状态 |
| `Visibility` | ESlateVisibility | `display`/`visibility` | 可见性转换 | 显示状态 |

### 2.2 TextBlock (文本)

| UMG属性 | 类型 | Vue/CSS属性 | 转换规则 | 示例 |
|---------|------|-------------|----------|------|
| `Text` | FText | `textContent` | 直接文本内容 | 显示文本 |
| `ColorAndOpacity` | FSlateColor | `color` | 颜色转换 | 文本颜色 |
| `Font.FontObject` | UFont | `font-family` | 字体名称 | 字体族 |
| `Font.Size` | Integer | `font-size` | 像素值 | `16px` |
| `Font.TypefaceFontName` | FName | `font-weight` | 字重映射 | `bold`, `normal` |
| `Justification` | ETextJustify | `text-align` | 对齐转换 | `left`, `center`, `right` |
| `AutoWrapText` | Boolean | `white-space` | 换行控制 | `nowrap`, `normal` |
| `LineHeightPercentage` | Float | `line-height` | 行高比例 | `1.2` |
| `Margin` | FMargin | `margin` | 外边距 | `10px 5px` |
| `ShadowOffset` | Vector2D | `text-shadow` | 阴影偏移 | `2px 2px` |
| `ShadowColorAndOpacity` | LinearColor | `text-shadow` | 阴影颜色 | 配合偏移 |

### 2.3 Image (图像)

| UMG属性 | 类型 | Vue/CSS属性 | 转换规则 | 示例 |
|---------|------|-------------|----------|------|
| `Brush.ResourceObject` | UTexture2D | `background-image` | 图片URL | `url()` |
| `Brush.ImageSize` | Vector2D | `background-size` | 尺寸设置 | `100px 50px` |
| `Brush.DrawAs` | ESlateBrushDrawType | `background-repeat` | 重复模式 | `no-repeat` |
| `Brush.Tiling` | ESlateBrushTileType | `background-repeat` | 平铺方式 | `repeat-x` |
| `ColorAndOpacity` | LinearColor | `opacity`/`filter` | 颜色叠加 | 透明度或滤镜 |
| `Brush.Margin` | FMargin | `border-image-slice` | 九宫格切片 | 图片边框 |

### 2.4 EditableText (可编辑文本)

| UMG属性 | 类型 | Vue/CSS属性 | 转换规则 | 示例 |
|---------|------|-------------|----------|------|
| `Text` | FText | `value` | 输入框值 | 默认文本 |
| `HintText` | FText | `placeholder` | 占位符 | 提示文本 |
| `IsReadOnly` | Boolean | `readonly` | 只读属性 | 布尔值 |
| `IsPassword` | Boolean | `type` | 输入类型 | `password` |
| `Font` | FSlateFontInfo | `font-*` | 字体属性组 | 同TextBlock |
| `SelectAllTextWhenFocused` | Boolean | JavaScript | 聚焦行为 | 自定义脚本 |

### 2.5 ProgressBar (进度条)

| UMG属性 | 类型 | Vue/CSS属性 | 转换规则 | 示例 |
|---------|------|-------------|----------|------|
| `Percent` | Float | `width` (内部) | 百分比宽度 | `50%` |
| `FillColorAndOpacity` | LinearColor | `background-color` | 填充颜色 | 进度条颜色 |
| `BackgroundImage` | FSlateBrush | `background-image` | 背景图片 | 进度条背景 |
| `FillImage` | FSlateBrush | `background-image` | 填充图片 | 进度条前景 |
| `MarqueeImage` | FSlateBrush | `animation` | 动画效果 | 滚动动画 |

## 3. 容器控件映射

### 3.1 CanvasPanel (画布面板)

| UMG特性 | Vue/CSS实现 | 转换规则 |
|---------|-------------|----------|
| 绝对定位 | `position: absolute` | 子元素绝对定位 |
| 锚点系统 | `left`, `top`, `right`, `bottom` | 基于父容器计算 |
| Z-Order | `z-index` | 层级控制 |

### 3.2 VerticalBox (垂直盒子)

| UMG特性 | Vue/CSS实现 | 转换规则 |
|---------|-------------|----------|
| 垂直排列 | `display: flex; flex-direction: column` | Flexbox布局 |
| 子项间距 | `gap` | 统一间距 |
| 填充模式 | `flex: 1` | 子项拉伸 |

### 3.3 HorizontalBox (水平盒子)

| UMG特性 | Vue/CSS实现 | 转换规则 |
|---------|-------------|----------|
| 水平排列 | `display: flex; flex-direction: row` | Flexbox布局 |
| 子项间距 | `gap` | 统一间距 |
| 填充模式 | `flex: 1` | 子项拉伸 |

### 3.4 GridPanel (网格面板)

| UMG特性 | Vue/CSS实现 | 转换规则 |
|---------|-------------|----------|
| 网格布局 | `display: grid` | CSS Grid |
| 行列定义 | `grid-template-rows/columns` | 网格模板 |
| 单元格跨越 | `grid-column/row-span` | 跨列跨行 |

## 4. 颜色系统映射

### 4.1 UE LinearColor 到 CSS颜色

```typescript
// UE LinearColor (0.0-1.0 范围)
interface UELinearColor {
  R: number  // 0.0-1.0
  G: number  // 0.0-1.0  
  B: number  // 0.0-1.0
  A: number  // 0.0-1.0
}

// 转换为CSS颜色
function convertUEColorToCSS(color: UELinearColor): string {
  const r = Math.round(color.R * 255)
  const g = Math.round(color.G * 255)  
  const b = Math.round(color.B * 255)
  const a = color.A
  
  if (a < 1.0) {
    return `rgba(${r}, ${g}, ${b}, ${a})`
  } else {
    return `rgb(${r}, ${g}, ${b})`
  }
}
```

### 4.2 UE FSlateColor 到 CSS颜色

```typescript
interface UESlateColor {
  ColorUseRule: 'UseColor_Specified' | 'UseColor_Foreground' | 'UseColor_Subdued'
  SpecifiedColor: UELinearColor
}

function convertSlateColorToCSS(slateColor: UESlateColor): string {
  switch (slateColor.ColorUseRule) {
    case 'UseColor_Specified':
      return convertUEColorToCSS(slateColor.SpecifiedColor)
    case 'UseColor_Foreground':
      return 'inherit' // 继承前景色
    case 'UseColor_Subdued':
      return 'rgba(255, 255, 255, 0.6)' // 半透明白色
    default:
      return 'inherit'
  }
}
```

## 5. 字体系统映射

### 5.1 UE字体到Web字体

```typescript
interface UEFontInfo {
  FontObject: string        // 字体资源路径
  TypefaceFontName: string  // 字体名称
  Size: number             // 字体大小
  LetterSpacing: number    // 字符间距
}

function convertUEFontToCSS(font: UEFontInfo): CSSStyleDeclaration {
  return {
    fontFamily: mapUEFontToWebFont(font.FontObject, font.TypefaceFontName),
    fontSize: `${font.Size}px`,
    letterSpacing: `${font.LetterSpacing}px`
  }
}

// UE字体到Web字体映射表
const FONT_MAPPING = {
  'Roboto-Regular': 'Roboto, sans-serif',
  'Roboto-Bold': 'Roboto, sans-serif',
  'NotoSans-Regular': 'Noto Sans, sans-serif',
  'SourceCodePro': 'Source Code Pro, monospace'
}
```

## 6. 事件系统映射

### 6.1 UMG事件到Vue事件

| UMG事件 | Vue事件 | 转换说明 |
|---------|---------|----------|
| `OnClicked` | `@click` | 点击事件 |
| `OnPressed` | `@mousedown` | 鼠标按下 |
| `OnReleased` | `@mouseup` | 鼠标释放 |
| `OnHovered` | `@mouseenter` | 鼠标悬停 |
| `OnUnhovered` | `@mouseleave` | 鼠标离开 |
| `OnTextChanged` | `@input` | 文本改变 |
| `OnTextCommitted` | `@change` | 文本提交 |

## 7. 动画系统映射

### 7.1 UMG动画到CSS动画

| UMG动画类型 | CSS实现 | 转换规则 |
|-------------|---------|----------|
| `UWidgetAnimation` | `@keyframes` | 关键帧动画 |
| `FloatTrack` | `transition` | 属性过渡 |
| `ColorTrack` | `transition` | 颜色过渡 |
| `TransformTrack` | `transform` + `transition` | 变换动画 |

## 8. 响应式设计映射

### 8.1 UMG锚点到CSS响应式

```typescript
// 锚点模式检测
function getAnchorMode(anchors: UEAnchors): 'fixed' | 'stretch-x' | 'stretch-y' | 'stretch-both' {
  const { Minimum, Maximum } = anchors
  
  if (Minimum.X === Maximum.X && Minimum.Y === Maximum.Y) {
    return 'fixed' // 固定尺寸
  } else if (Minimum.X !== Maximum.X && Minimum.Y === Maximum.Y) {
    return 'stretch-x' // 水平拉伸
  } else if (Minimum.X === Maximum.X && Minimum.Y !== Maximum.Y) {
    return 'stretch-y' // 垂直拉伸
  } else {
    return 'stretch-both' // 双向拉伸
  }
}

// 根据锚点模式生成CSS
function generateResponsiveCSS(mode: string, anchors: UEAnchors, offsets: UEOffsets): CSSProperties {
  switch (mode) {
    case 'fixed':
      return {
        position: 'absolute',
        left: `calc(${anchors.Minimum.X * 100}% + ${offsets.Left}px)`,
        top: `calc(${anchors.Minimum.Y * 100}% + ${offsets.Top}px)`,
        width: `${offsets.Right - offsets.Left}px`,
        height: `${offsets.Bottom - offsets.Top}px`
      }
    case 'stretch-x':
      return {
        position: 'absolute',
        left: `calc(${anchors.Minimum.X * 100}% + ${offsets.Left}px)`,
        right: `calc(${(1 - anchors.Maximum.X) * 100}% - ${offsets.Right}px)`,
        top: `calc(${anchors.Minimum.Y * 100}% + ${offsets.Top}px)`,
        height: `${offsets.Bottom - offsets.Top}px`
      }
    // ... 其他模式
  }
}
```

## 9. 实际应用示例

### 9.1 完整的Button转换示例

```typescript
// UMG Button数据
const umgButton = {
  Class: 'Button',
  Properties: {
    Style: {
      Normal: { Tint: { R: 0.2, G: 0.4, B: 0.8, A: 1.0 } },
      Hovered: { Tint: { R: 0.3, G: 0.5, B: 0.9, A: 1.0 } },
      Pressed: { Tint: { R: 0.1, G: 0.3, B: 0.7, A: 1.0 } }
    },
    ColorAndOpacity: { R: 1, G: 1, B: 1, A: 1 },
    IsEnabled: true
  },
  Slot: {
    Anchors: { Minimum: { X: 0.1, Y: 0.1 }, Maximum: { X: 0.1, Y: 0.1 } },
    Offsets: { Left: 0, Top: 0, Right: 200, Bottom: 50 }
  }
}

// 转换为Vue组件
const vueButton = {
  template: `
    <button 
      :style="buttonStyle"
      :disabled="!isEnabled"
      @click="handleClick"
      @mouseenter="onHover"
      @mouseleave="onUnhover"
      @mousedown="onPress"
      @mouseup="onRelease"
    >
      {{ text }}
    </button>
  `,
  computed: {
    buttonStyle() {
      return {
        position: 'absolute',
        left: 'calc(10% + 0px)',
        top: 'calc(10% + 0px)', 
        width: '200px',
        height: '50px',
        backgroundColor: this.currentColor,
        border: 'none',
        borderRadius: '4px',
        cursor: this.isEnabled ? 'pointer' : 'not-allowed',
        transition: 'background-color 0.2s ease'
      }
    },
    currentColor() {
      if (!this.isEnabled) return 'rgba(128, 128, 128, 1)'
      if (this.isPressed) return 'rgba(25, 76, 178, 1)'
      if (this.isHovered) return 'rgba(76, 127, 229, 1)'
      return 'rgba(51, 102, 204, 1)'
    }
  }
}
```

## 10. 转换器实现建议

1. **创建映射配置文件**: 将所有映射规则配置化，便于维护和扩展
2. **实现渐进式转换**: 先实现基础属性，再逐步添加高级特性
3. **建立测试用例**: 为每种控件类型创建测试用例，确保转换准确性
4. **性能优化**: 缓存转换结果，避免重复计算
5. **错误处理**: 对不支持的属性提供降级方案

这个映射关系将作为转换器的核心依据，确保UMG设计能够准确地转换为Web界面。 