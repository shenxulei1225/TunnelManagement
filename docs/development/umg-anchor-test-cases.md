# UMG锚点计算测试用例

## 概述

本文档包含了UMG锚点计算的全面测试用例，用于验证我们的锚点转换逻辑是否正确处理所有可能的锚点组合。

## 测试环境设置

```typescript
// 测试用的父容器尺寸
const PARENT_SIZE = { width: 1920, height: 1080 }

// 测试用的标准偏移
const STANDARD_OFFSETS = {
  Left: 10,
  Top: 20,
  Right: 110,
  Bottom: 70
}
```

## 基础锚点模式测试

### 1. 固定位置锚点 (Fixed)

#### 测试用例 1.1: 左上角固定
```typescript
const anchors = {
  Minimum: { X: 0, Y: 0 },
  Maximum: { X: 0, Y: 0 }
}
const offsets = { Left: 10, Top: 20, Right: 110, Bottom: 70 }

// 期望结果
const expected = {
  x: 10,        // 0 * 1920 + 10
  y: 20,        // 0 * 1080 + 20
  width: 100,   // 110 - 10
  height: 50    // 70 - 20
}

// 期望CSS
const expectedCSS = {
  position: 'absolute',
  left: '10px',
  top: '20px',
  width: '100px',
  height: '50px'
}
```

#### 测试用例 1.2: 中心固定
```typescript
const anchors = {
  Minimum: { X: 0.5, Y: 0.5 },
  Maximum: { X: 0.5, Y: 0.5 }
}
const offsets = { Left: -50, Top: -25, Right: 50, Bottom: 25 }

// 期望结果
const expected = {
  x: 910,       // 0.5 * 1920 + (-50) = 960 - 50
  y: 515,       // 0.5 * 1080 + (-25) = 540 - 25
  width: 100,   // 50 - (-50)
  height: 50    // 25 - (-25)
}

// 期望CSS
const expectedCSS = {
  position: 'absolute',
  left: 'calc(50% + -50px)',
  top: 'calc(50% + -25px)',
  width: '100px',
  height: '50px'
}
```

#### 测试用例 1.3: 右下角固定
```typescript
const anchors = {
  Minimum: { X: 1, Y: 1 },
  Maximum: { X: 1, Y: 1 }
}
const offsets = { Left: -110, Top: -70, Right: -10, Bottom: -20 }

// 期望结果
const expected = {
  x: 1810,      // 1 * 1920 + (-110)
  y: 1010,      // 1 * 1080 + (-70)
  width: 100,   // -10 - (-110)
  height: 50    // -20 - (-70)
}

// 期望CSS
const expectedCSS = {
  position: 'absolute',
  left: 'calc(100% + -110px)',
  top: 'calc(100% + -70px)',
  width: '100px',
  height: '50px'
}
```

### 2. 水平拉伸锚点 (Stretch-X)

#### 测试用例 2.1: 顶部横向填充
```typescript
const anchors = {
  Minimum: { X: 0, Y: 0 },
  Maximum: { X: 1, Y: 0 }
}
const offsets = { Left: 10, Top: 20, Right: -10, Bottom: 70 }

// 期望结果
const expected = {
  x: 10,        // 0 * 1920 + 10
  y: 20,        // 0 * 1080 + 20
  width: 1900,  // (1-0) * 1920 + (-10 - 10) = 1920 - 20
  height: 50    // 70 - 20
}

// 期望CSS
const expectedCSS = {
  position: 'absolute',
  left: 'calc(0% + 10px)',
  right: 'calc(0% - -10px)',  // 简化为 right: '10px'
  top: 'calc(0% + 20px)',
  height: '50px'
}
```

#### 测试用例 2.2: 中间水平拉伸
```typescript
const anchors = {
  Minimum: { X: 0.2, Y: 0.3 },
  Maximum: { X: 0.8, Y: 0.3 }
}
const offsets = { Left: 5, Top: 10, Right: -5, Bottom: 40 }

// 期望结果
const expected = {
  x: 389,       // 0.2 * 1920 + 5 = 384 + 5
  y: 334,       // 0.3 * 1080 + 10 = 324 + 10
  width: 1142,  // (0.8-0.2) * 1920 + (-5 - 5) = 1152 - 10
  height: 30    // 40 - 10
}

// 期望CSS
const expectedCSS = {
  position: 'absolute',
  left: 'calc(20% + 5px)',
  right: 'calc(20% - -5px)',  // 简化为 right: 'calc(20% + 5px)'
  top: 'calc(30% + 10px)',
  height: '30px'
}
```

### 3. 垂直拉伸锚点 (Stretch-Y)

#### 测试用例 3.1: 左侧纵向填充
```typescript
const anchors = {
  Minimum: { X: 0, Y: 0 },
  Maximum: { X: 0, Y: 1 }
}
const offsets = { Left: 10, Top: 20, Right: 110, Bottom: -20 }

// 期望结果
const expected = {
  x: 10,        // 0 * 1920 + 10
  y: 20,        // 0 * 1080 + 20
  width: 100,   // 110 - 10
  height: 1040  // (1-0) * 1080 + (-20 - 20) = 1080 - 40
}

// 期望CSS
const expectedCSS = {
  position: 'absolute',
  left: 'calc(0% + 10px)',
  top: 'calc(0% + 20px)',
  bottom: 'calc(0% - -20px)',  // 简化为 bottom: '20px'
  width: '100px'
}
```

### 4. 双向拉伸锚点 (Stretch-Both)

#### 测试用例 4.1: 全屏填充
```typescript
const anchors = {
  Minimum: { X: 0, Y: 0 },
  Maximum: { X: 1, Y: 1 }
}
const offsets = { Left: 10, Top: 20, Right: -10, Bottom: -20 }

// 期望结果
const expected = {
  x: 10,        // 0 * 1920 + 10
  y: 20,        // 0 * 1080 + 20
  width: 1900,  // (1-0) * 1920 + (-10 - 10) = 1920 - 20
  height: 1040  // (1-0) * 1080 + (-20 - 20) = 1080 - 40
}

// 期望CSS
const expectedCSS = {
  position: 'absolute',
  left: 'calc(0% + 10px)',
  right: 'calc(0% - -10px)',   // 简化为 right: '10px'
  top: 'calc(0% + 20px)',
  bottom: 'calc(0% - -20px)'   // 简化为 bottom: '20px'
}
```

#### 测试用例 4.2: 相对居中的拉伸区域
```typescript
const anchors = {
  Minimum: { X: 0.2, Y: 0.3 },
  Maximum: { X: 0.8, Y: 0.7 }
}
const offsets = { Left: 0, Top: 0, Right: 0, Bottom: 0 }

// 期望结果
const expected = {
  x: 384,       // 0.2 * 1920
  y: 324,       // 0.3 * 1080
  width: 1152,  // (0.8-0.2) * 1920 = 0.6 * 1920
  height: 432   // (0.7-0.3) * 1080 = 0.4 * 1080
}

// 期望CSS (优化后)
const expectedCSS = {
  position: 'absolute',
  left: '20%',
  right: '20%',
  top: '30%',
  bottom: '30%'
}
```

## 边界情况测试

### 5. 异常锚点值处理

#### 测试用例 5.1: 负值锚点
```typescript
const anchors = {
  Minimum: { X: -0.1, Y: -0.1 },
  Maximum: { X: 0.1, Y: 0.1 }
}

// 期望自动修正为
const correctedAnchors = {
  Minimum: { X: 0, Y: 0 },
  Maximum: { X: 0.1, Y: 0.1 }
}
```

#### 测试用例 5.2: 超范围锚点
```typescript
const anchors = {
  Minimum: { X: 0.9, Y: 0.9 },
  Maximum: { X: 1.1, Y: 1.1 }
}

// 期望自动修正为
const correctedAnchors = {
  Minimum: { X: 0.9, Y: 0.9 },
  Maximum: { X: 1, Y: 1 }
}
```

#### 测试用例 5.3: 倒序锚点
```typescript
const anchors = {
  Minimum: { X: 0.7, Y: 0.8 },
  Maximum: { X: 0.3, Y: 0.2 }
}

// 期望自动修正为 (交换值)
const correctedAnchors = {
  Minimum: { X: 0.3, Y: 0.2 },
  Maximum: { X: 0.7, Y: 0.8 }
}
```

### 6. 零尺寸和负尺寸处理

#### 测试用例 6.1: 零宽度偏移
```typescript
const anchors = {
  Minimum: { X: 0.5, Y: 0.5 },
  Maximum: { X: 0.5, Y: 0.5 }
}
const offsets = { Left: 50, Top: 25, Right: 50, Bottom: 75 }

// 期望结果 (宽度为0，应该修正为最小值1)
const expected = {
  x: 1010,     // 0.5 * 1920 + 50
  y: 565,      // 0.5 * 1080 + 25
  width: 1,    // max(50 - 50, 1) = 1
  height: 50   // 75 - 25
}
```

#### 测试用例 6.2: 负尺寸偏移
```typescript
const anchors = {
  Minimum: { X: 0.5, Y: 0.5 },
  Maximum: { X: 0.5, Y: 0.5 }
}
const offsets = { Left: 100, Top: 100, Right: 50, Bottom: 75 }

// 期望结果 (负尺寸应该修正为最小值1)
const expected = {
  x: 1060,     // 0.5 * 1920 + 100
  y: 640,      // 0.5 * 1080 + 100
  width: 1,    // max(50 - 100, 1) = 1
  height: 1    // max(75 - 100, 1) = 1
}
```

## 动态锚点调整测试

### 7. 控件特定规则

#### 测试用例 7.1: ScrollBox自动填充
```typescript
const widget = {
  Class: 'UScrollBox',
  Properties: {}
}
const originalAnchors = {
  Minimum: { X: 0.2, Y: 0.2 },
  Maximum: { X: 0.2, Y: 0.2 }
}

// 期望调整为全屏填充
const adjustedAnchors = {
  Minimum: { X: 0, Y: 0 },
  Maximum: { X: 1, Y: 1 }
}
```

#### 测试用例 7.2: 自动尺寸控件
```typescript
const widget = {
  Class: 'UTextBlock',
  Properties: { bAutoSize: true }
}
const originalAnchors = {
  Minimum: { X: 0.2, Y: 0.3 },
  Maximum: { X: 0.8, Y: 0.7 }
}

// 期望调整为固定锚点
const adjustedAnchors = {
  Minimum: { X: 0.2, Y: 0.3 },
  Maximum: { X: 0.2, Y: 0.3 }
}
```

#### 测试用例 7.3: 按钮固定尺寸优化
```typescript
const widget = {
  Class: 'UButton',
  Properties: { Text: 'Click Me' }
}
const originalAnchors = {
  Minimum: { X: 0.2, Y: 0.3 },
  Maximum: { X: 0.8, Y: 0.7 }
}

// 期望调整为固定锚点
const adjustedAnchors = {
  Minimum: { X: 0.2, Y: 0.3 },
  Maximum: { X: 0.2, Y: 0.3 }
}
```

## 响应式断点测试

### 8. 移动端适配

#### 测试用例 8.1: 移动端固定元素拉伸
```typescript
const anchors = {
  Minimum: { X: 0.4, Y: 0.3 },
  Maximum: { X: 0.4, Y: 0.3 }  // 固定尺寸
}

// 移动端调整 (viewport <= 768px)
const mobileAdjusted = {
  Minimum: { X: 0.05, Y: 0.3 },
  Maximum: { X: 0.95, Y: 0.3 }  // 转为水平拉伸
}
```

### 9. 平板端适配

#### 测试用例 9.1: 平板端边距调整
```typescript
const anchors = {
  Minimum: { X: 0, Y: 0.2 },
  Maximum: { X: 1, Y: 0.8 }
}

// 平板端调整 (769px <= viewport <= 1024px)
const tabletAdjusted = {
  Minimum: { X: 0.1, Y: 0.2 },  // 最小10%边距
  Maximum: { X: 0.9, Y: 0.8 }   // 最大90%边距
}
```

## CSS优化测试

### 10. CSS生成优化

#### 测试用例 10.1: 整数百分比优化
```typescript
const anchors = {
  Minimum: { X: 0.2, Y: 0.3 },
  Maximum: { X: 0.2, Y: 0.3 }
}
const offsets = { Left: 0, Top: 0, Right: 100, Bottom: 50 }

// 期望优化的CSS (20%和30%是整数百分比，偏移为0)
const optimizedCSS = {
  position: 'absolute',
  left: '20%',    // 而不是 calc(20% + 0px)
  top: '30%',     // 而不是 calc(30% + 0px)
  width: '100px',
  height: '50px'
}
```

#### 测试用例 10.2: 零偏移简化
```typescript
const anchors = {
  Minimum: { X: 0.25, Y: 0.35 },
  Maximum: { X: 0.75, Y: 0.35 }
}
const offsets = { Left: 0, Top: 0, Right: 0, Bottom: 50 }

// 期望简化的CSS
const simplifiedCSS = {
  position: 'absolute',
  left: '25%',      // 简化
  right: '25%',     // 简化
  top: '35%',       // 简化
  height: '50px'
}
```

## 性能测试

### 11. 缓存机制测试

#### 测试用例 11.1: 缓存命中
```typescript
const anchors = { Minimum: { X: 0.5, Y: 0.5 }, Maximum: { X: 0.5, Y: 0.5 } }
const offsets = { Left: 0, Top: 0, Right: 100, Bottom: 50 }
const parentSize = { width: 1920, height: 1080 }

// 第一次计算
const result1 = UMGStyleConverter.calculateAnchorPosition(anchors, offsets, parentSize)

// 第二次计算应该命中缓存
const result2 = UMGStyleConverter.calculateAnchorPosition(anchors, offsets, parentSize)

// 期望: result1 === result2 (相同引用，来自缓存)
```

#### 测试用例 11.2: 批量计算优化
```typescript
const widgets = [
  { Name: 'Widget1', Slot: { Properties: { Anchors: anchors1, Offsets: offsets1 } } },
  { Name: 'Widget2', Slot: { Properties: { Anchors: anchors2, Offsets: offsets2 } } },
  // ... 更多widget
]

// 批量计算应该比单独计算更快
const batchResults = UMGStyleConverter.batchCalculateAnchors(widgets)
```

## 测试执行脚本

```typescript
// 测试执行器
class UMGAnchorTestRunner {
  static runAllTests(): void {
    console.log('开始UMG锚点计算测试...')
    
    // 运行基础测试
    this.runBasicAnchorTests()
    
    // 运行边界情况测试
    this.runBoundaryTests()
    
    // 运行动态调整测试
    this.runDynamicAdjustmentTests()
    
    // 运行响应式测试
    this.runResponsiveTests()
    
    // 运行性能测试
    this.runPerformanceTests()
    
    console.log('所有测试完成!')
  }
  
  private static runBasicAnchorTests(): void {
    // 实现基础测试...
  }
  
  private static runBoundaryTests(): void {
    // 实现边界测试...
  }
  
  // ... 其他测试方法
}

// 运行测试
UMGAnchorTestRunner.runAllTests()
```

## 总结

通过这些全面的测试用例，我们可以确保UMG锚点计算系统：

1. **正确处理所有锚点模式**: 固定、水平拉伸、垂直拉伸、双向拉伸
2. **妥善处理边界情况**: 异常值、零尺寸、负尺寸
3. **智能应用动态规则**: 根据控件类型自动调整
4. **支持响应式设计**: 不同屏幕尺寸的适配
5. **优化CSS生成**: 简化输出，提高性能
6. **提供缓存机制**: 避免重复计算，提升效率

这些测试用例为我们的锚点转换系统提供了可靠的验证基础。