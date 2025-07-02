# 平台兼容性功能 - 技术实现备忘录

## 代码实现状态

### ✅ 已完成
1. **类型定义扩展**
   - `ComponentInstance` 添加 `metadata` 字段支持平台兼容性信息
   - 创建 `compatibility.ts` 定义各平台数据结构
   - 定义 `PlatformConverter` 接口规范

2. **Figma转换器实现**
   - 文件路径: `src/views/custom-page/utils/converters/figmaConverter.ts`
   - 实现 `FigmaConverter` 类和 `FigmaAPI` 类
   - 支持节点类型映射: TEXT → text, RECTANGLE → button/container
   - 颜色转换: Figma RGBA → Hex, Hex → Figma RGBA

3. **UMG转换器实现**
   - 文件路径: `src/views/custom-page/utils/converters/umgConverter.ts`
   - 实现 `UMGConverter` 类和 `UMGCodeGenerator` 类
   - 支持控件映射: UTextBlock → text, UButton → button
   - 锚点转换: UMG anchors/offsets ↔ 绝对坐标

### 🔄 进行中
1. **UI界面集成**
   - 需要在设计器中添加导入/导出按钮
   - 创建导入导出对话框组件

### ⏳ 待实现
1. **墨刀/蓝湖转换器**
2. **导入导出服务类**
3. **资源管理系统**

## 关键代码结构

### 1. 类型定义
```typescript
// ComponentInstance 扩展 (已实现)
interface ComponentInstance {
  // ... 现有字段
  metadata?: {
    figmaId?: string
    figmaType?: string
    figmaConstraints?: any
    umgClassName?: string
    umgSlot?: any
    originalPlatform?: 'figma' | 'mockingbot' | 'lanhu' | 'umg'
    [key: string]: any
  }
}

// 转换器接口 (已实现)
interface PlatformConverter<T> {
  import(data: T): Promise<ComponentInstance[]>
  export(instances: ComponentInstance[]): Promise<T>
  validate(data: T): boolean
}
```

### 2. 坐标转换算法
```typescript
// Figma绝对坐标 → 设计器坐标
function convertFigmaPosition(node: FigmaNode, parentOffset: {x,y}) {
  return {
    x: Math.round(node.x - parentOffset.x),
    y: Math.round(node.y - parentOffset.y)
  }
}

// UMG锚点 → 绝对坐标 (简化版)
function calculatePositionFromSlot(slot: UMGSlot) {
  return {
    x: slot.offsets.left,
    y: slot.offsets.top
  }
}
```

### 3. 样式转换
```typescript
// Figma颜色 → Hex
function convertFigmaColor(color: {r,g,b,a}) {
  const r = Math.round(color.r * 255)
  const g = Math.round(color.g * 255) 
  const b = Math.round(color.b * 255)
  return `#${r.toString(16).padStart(2,'0')}${g.toString(16).padStart(2,'0')}${b.toString(16).padStart(2,'0')}`
}

// Hex → UMG颜色
function hexToUMGColor(hex: string) {
  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex)
  return {
    r: parseInt(result[1], 16) / 255,
    g: parseInt(result[2], 16) / 255,
    b: parseInt(result[3], 16) / 255,
    a: 1
  }
}
```

## 技术要点

### 1. 错误处理
- 所有转换器都需要 try-catch 包装
- 使用 `error instanceof Error` 检查错误类型
- 提供详细的错误信息和fallback机制

### 2. 数据验证
- 实现 `validate()` 方法检查输入数据完整性
- 必需字段检查: id, name, type, 坐标信息
- 类型安全: 使用 TypeScript 严格模式

### 3. 性能优化
- 使用 `Math.round()` 确保像素对齐
- 递归处理子节点时避免深度过大
- 大文件分块处理（待实现）

### 4. 兼容性处理
- 保存原始平台信息在 `metadata` 中
- 支持双向转换时的信息保留
- 处理不同平台的特有属性

## 实现注意事项

### 1. Figma API使用
```typescript
// API认证
const figmaAPI = new FigmaAPI('your-figma-token')

// 获取文件
const fileData = await figmaAPI.getFile('file-key')

// 导出图片
const images = await figmaAPI.exportImages('file-key', ['node-id'], 'png')
```

### 2. UMG代码生成
```typescript
// 生成Blueprint代码
const code = UMGCodeGenerator.generateBlueprintCode(umgWidgets)

// 输出格式示例:
// UTextBlock MyText = CreateWidget<UTextBlock>();
// MyText->SetText(TEXT("Hello World"));
// MyText->SetAnchors(FAnchors(0, 0, 1, 1));
```

### 3. 组件类型推断
```typescript
// 基于名称推断组件类型
function inferComponentType(name: string): string {
  const lowerName = name.toLowerCase()
  if (lowerName.includes('button')) return 'button'
  if (lowerName.includes('text')) return 'text'
  if (lowerName.includes('input')) return 'input'
  return 'container' // 默认类型
}
```

## 调试技巧

### 1. 控制台日志
```typescript
// 在转换过程中添加详细日志
console.log('转换Figma节点:', {
  type: node.type,
  name: node.name,
  position: {x: node.x, y: node.y},
  size: {width: node.width, height: node.height}
})
```

### 2. 数据完整性检查
```typescript
// 验证转换结果
function validateConversion(original: any, converted: ComponentInstance) {
  console.assert(converted.id, '组件ID不能为空')
  console.assert(converted.type, '组件类型不能为空')
  console.assert(converted.transform.width > 0, '组件宽度必须大于0')
}
```

### 3. 中间数据保存
```typescript
// 保存转换过程中的中间数据用于调试
instance.metadata = {
  ...instance.metadata,
  debug: {
    originalData: node,
    conversionTime: Date.now(),
    conversionStep: 'figma-to-designer'
  }
}
```

## 待解决问题

### 1. 字体映射
- Figma字体 → Web字体 → UMG字体
- 字体文件管理和引用
- 字体版权问题

### 2. 图片资源处理
- Figma图片导出和存储
- 不同格式转换 (SVG → PNG → UE Texture)
- 资源路径管理

### 3. 复杂布局转换
- Auto Layout → Flex布局 → UMG Grid
- 约束系统映射
- 响应式设计转换

### 4. 动画和交互
- Figma原型 → 设计器交互 → UMG动画
- 状态管理和事件绑定
- 时间轴和关键帧转换

## 扩展计划

### 近期 (1个月内)
1. 完善错误处理和用户反馈
2. 添加转换进度显示
3. 实现基础的预览功能

### 中期 (3个月内)
1. 支持更多Figma节点类型
2. 实现墨刀/蓝湖转换器
3. 添加资源管理功能

### 远期 (6个月内)
1. 支持复杂布局和约束
2. 实现动画转换
3. 建立完整的测试覆盖

## 参考资源

### 1. API文档
- [Figma API Reference](https://www.figma.com/developers/api)
- [UE UMG Documentation](https://docs.unrealengine.com/5.3/en-US/umg-ui-designer-for-unreal-engine/)

### 2. 示例代码
- Figma插件开发示例
- UMG Widget Blueprint示例
- 跨平台UI转换案例

### 3. 社区资源
- Figma开发者社区
- UE开发者论坛
- 开源转换工具参考

---

*此文档记录开发过程中的关键技术细节，将随着实现进度持续更新。* 