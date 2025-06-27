# 多入口导入架构设计

## 概述

为了解决当前架构兼容性设计中依赖文件名和数据特征推测平台类型的不足，我们设计了一个全新的多入口导入架构。该架构通过明确的入口选择来区分不同平台的数据，提供更可靠和准确的导入体验。

## 核心设计理念

### 1. 明确入口，消除歧义
- 用户必须明确选择数据来源平台
- 每个平台都有专用的导入入口
- 提供自动检测作为备选方案

### 2. 专业化验证
- 每个入口都有专门的数据验证器
- 针对平台特性进行定制化预处理
- 提供详细的错误诊断信息

### 3. 上下文感知
- 记录用户的选择和行为
- 区分用户明确指定和自动检测
- 提供完整的导入追踪信息

## 架构组件

### 1. 多入口导入管理器 (MultiEntryImportManager)

```typescript
export class MultiEntryImportManager {
  // 获取所有可用入口
  getAllEntries(): PlatformEntry[]
  
  // 通过指定入口导入数据
  importWithEntry(entryId: string, data: any, context: ImportContext): Promise<ImportResult>
  
  // 批量导入支持
  batchImport(imports: ImportRequest[]): Promise<ImportResult[]>
  
  // 跨平台转换
  crossPlatformConvert(sourceEntryId: string, targetEntryId: string, data: any): Promise<ImportResult>
}
```

### 2. 平台入口定义 (PlatformEntry)

每个平台入口包含：
- **基础信息**: 名称、描述、图标
- **格式支持**: 支持的文件扩展名
- **转换器**: 平台专用的数据转换器
- **验证器**: 数据格式验证函数
- **预处理器**: 数据预处理管道

```typescript
export interface PlatformEntry {
  id: string
  name: string
  description: string
  icon: string
  supportedFormats: string[]
  converter: PlatformConverter<any>
  validator: (data: any) => boolean
  preprocessor?: (data: any, file?: File) => Promise<any>
}
```

### 3. 导入上下文 (ImportContext)

记录导入的完整上下文信息：

```typescript
export interface ImportContext {
  entryId: string           // 明确的入口ID
  originalFileName?: string
  fileSize?: number
  uploadTime: number
  userSpecified: boolean    // 是否用户明确指定
}
```

## 支持的入口类型

### 1. Figma 入口
- **支持格式**: `.json`, `.fig`
- **验证特征**: 
  - Figma API 响应结构
  - 节点类型标识符
  - 绝对边界框信息
- **预处理**: 处理 API 响应格式，检查 .fig 文件

### 2. UMG 入口
- **支持格式**: `.json`, `.uasset`
- **验证特征**:
  - Widget Tree 结构
  - UE 控件类名前缀
  - Slot 锚点系统
- **预处理**: 处理二进制 .uasset 文件，提供降级支持

### 3. 墨刀入口
- **支持格式**: `.json`, `.mb`
- **验证特征**:
  - 项目页面结构
  - 组件框架信息
  - 交互定义
- **预处理**: 标准化组件数据

### 4. 自动检测入口
- **支持格式**: `.json`
- **验证特征**: 使用智能检测器分析数据
- **预处理**: 根据检测结果路由到具体入口

## 使用流程

### 1. 用户选择入口
```typescript
// 用户在界面上明确选择平台入口
const entryId = 'figma' // 或 'umg', 'modao', 'auto'
```

### 2. 上传数据
```typescript
// 支持多种上传方式
const uploadMethods = ['file', 'url', 'clipboard']
```

### 3. 数据验证
```typescript
// 使用入口专用验证器
const entry = importManager.getEntry(entryId)
const isValid = entry.validator(data)
```

### 4. 执行导入
```typescript
const context: ImportContext = {
  entryId: 'figma',
  originalFileName: 'design.json',
  fileSize: 1024000,
  uploadTime: Date.now(),
  userSpecified: true
}

const result = await importManager.importWithEntry(entryId, data, context, config)
```

## 优势对比

### 之前的方案（基于推测）
```typescript
// ❌ 依赖文件名推测
const platform = detectFromFileName(file.name)

// ❌ 依赖数据特征推测  
const platform = detectFromData(data)

// ❌ 可能误判，用户无法控制
```

### 新的方案（明确入口）
```typescript
// ✅ 用户明确选择
const entryId = userSelectedEntry

// ✅ 专业化验证
const entry = importManager.getEntry(entryId)
const isValid = entry.validator(data)

// ✅ 上下文记录
const context = { entryId, userSpecified: true, ... }
```

## 错误处理

### 1. 入口级别错误
- 入口不存在
- 入口暂不支持
- 配置错误

### 2. 验证级别错误
- 数据格式不匹配
- 必需字段缺失
- 版本不兼容

### 3. 转换级别错误
- 组件类型不支持
- 样式转换失败
- 资源缺失

## 扩展性

### 添加新平台入口

```typescript
// 1. 实现转换器
class NewPlatformConverter implements PlatformConverter<NewFormat> {
  // ... 实现接口
}

// 2. 注册入口
importManager.registerEntry({
  id: 'new-platform',
  name: '新平台',
  description: '新平台设计工具',
  icon: 'new-platform',
  supportedFormats: ['.newformat'],
  converter: new NewPlatformConverter(),
  validator: (data) => validateNewFormat(data),
  preprocessor: async (data) => preprocessNewFormat(data)
})
```

### 自定义验证器

```typescript
// 针对特定需求自定义验证逻辑
const customValidator = (data: any) => {
  // 自定义验证规则
  return data.customField && data.version === '2.0'
}
```

## 性能优化

### 1. 懒加载转换器
- 只在需要时加载特定平台转换器
- 减少初始化开销

### 2. 并行验证
- 文件上传时并行进行格式验证
- 提前发现问题

### 3. 缓存机制
- 缓存验证结果
- 重用转换配置

## 测试策略

### 1. 单元测试
- 每个入口的验证器测试
- 转换器功能测试
- 错误处理测试

### 2. 集成测试  
- 端到端导入流程测试
- 跨平台转换测试
- 批量导入测试

### 3. 用户体验测试
- 入口选择易用性
- 错误信息清晰度
- 导入成功率

## 未来计划

### 1. 智能推荐
- 基于历史选择推荐入口
- 根据文件特征提供建议

### 2. 批量操作
- 支持多文件不同入口导入
- 批量格式转换

### 3. 云端集成
- 直接从设计工具云端导入
- 实时同步设计变更

## 总结

多入口导入架构通过明确的入口选择彻底解决了依赖推测的问题，提供了：

1. **更高的准确性** - 用户明确指定数据来源
2. **更好的用户体验** - 清晰的操作流程和反馈
3. **更强的扩展性** - 容易添加新平台支持
4. **更完整的追踪** - 记录完整的导入上下文

这个架构为页面设计器提供了可靠、专业、易用的多平台数据导入能力。 