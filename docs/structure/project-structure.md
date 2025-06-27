# 平台兼容性功能 - 项目结构说明

## 目录结构概览

```
tunnel-management-ui/src/views/custom-page/
├── components/                     # Vue组件
│   ├── ComponentRenderer.vue       # 组件渲染器 (已实现)
│   ├── DesignerViewport.vue        # 设计视口 (已实现)
│   ├── PropertiesPanel.vue         # 属性面板 (已实现)
│   ├── ResourcePanel.vue           # 资源面板 (已实现)
│   ├── ImportDialog.vue            # 导入对话框 (待实现)
│   ├── ExportDialog.vue            # 导出对话框 (待实现)
│   └── PlatformPreview.vue         # 平台预览 (待实现)
├── types/                          # TypeScript类型定义
│   ├── component.ts                # 基础组件类型 (已扩展metadata)
│   └── compatibility.ts           # 平台兼容性类型 (已实现)
├── utils/                          # 工具函数
│   ├── converters/                 # 平台转换器
│   │   ├── figmaConverter.ts       # Figma转换器 (已实现)
│   │   ├── umgConverter.ts         # UMG转换器 (已实现)
│   │   ├── mockingbotConverter.ts  # 墨刀转换器 (待实现)
│   │   ├── lanhuConverter.ts       # 蓝湖转换器 (待实现)
│   │   └── index.ts               # 转换器统一入口 (待实现)
│   ├── componentFactory.ts        # 组件工厂 (已实现)
│   ├── importExportService.ts      # 导入导出服务 (待实现)
│   └── resourceManager.ts         # 资源管理器 (待实现)
├── apis/                           # API接口
│   ├── figmaApi.ts                 # Figma API (已集成在converter中)
│   ├── mockingbotApi.ts            # 墨刀API (待实现)
│   └── platformApis.ts             # 其他平台API (待实现)
└── PageDesigner.vue               # 主设计器组件 (已实现)
```

## 文件详细说明

### 1. 核心组件 (`components/`)

#### 已实现组件
- **ComponentRenderer.vue**: 负责渲染单个组件实例，支持文本、按钮、图片等基础控件
- **DesignerViewport.vue**: 设计视口，包含画布、标尺、工具栏等
- **PropertiesPanel.vue**: 右侧属性配置面板
- **ResourcePanel.vue**: 左侧资源面板，包含模版库和控件库

#### 待实现组件
- **ImportDialog.vue**: 导入对话框
  ```vue
  <template>
    <el-dialog title="导入设计" v-model="visible">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Figma" name="figma">
          <FigmaImportForm @import="handleFigmaImport" />
        </el-tab-pane>
        <el-tab-pane label="墨刀" name="mockingbot">
          <MockingbotImportForm @import="handleMockingbotImport" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </template>
  ```

- **ExportDialog.vue**: 导出对话框
  ```vue
  <template>
    <el-dialog title="导出设计" v-model="visible">
      <el-form :model="exportConfig">
        <el-form-item label="导出格式">
          <el-select v-model="exportConfig.platform">
            <el-option label="UMG Widget" value="umg" />
            <el-option label="Vue组件" value="vue" />
            <el-option label="React组件" value="react" />
          </el-select>
        </el-form-item>
      </el-form>
    </el-dialog>
  </template>
  ```

### 2. 类型定义 (`types/`)

#### component.ts (已扩展)
```typescript
interface ComponentInstance {
  // 基础字段...
  metadata?: {                    // 新增兼容性元数据
    figmaId?: string
    figmaType?: string
    umgClassName?: string
    originalPlatform?: string
  }
}
```

#### compatibility.ts (已实现)
```typescript
// 定义各平台数据结构
interface FigmaNode { }         // Figma节点结构
interface UMGWidget { }         // UMG控件结构
interface PlatformConverter<T>  // 转换器接口
```

### 3. 转换器 (`utils/converters/`)

#### figmaConverter.ts (已实现)
- **FigmaConverter类**: 实现Figma数据双向转换
- **FigmaAPI类**: 封装Figma REST API调用
- **核心方法**:
  - `import()`: Figma数据 → 设计器组件
  - `export()`: 设计器组件 → Figma数据
  - `validate()`: 数据格式验证

#### umgConverter.ts (已实现)
- **UMGConverter类**: 实现UMG数据双向转换
- **UMGCodeGenerator类**: 生成UE Blueprint代码
- **核心方法**:
  - `convertUMGWidget()`: UMG控件 → 设计器组件
  - `convertInstanceToUMGWidget()`: 设计器组件 → UMG控件
  - `generateBlueprintCode()`: 生成C++/Blueprint代码

#### index.ts (待实现)
```typescript
// 转换器统一入口
export class ConverterFactory {
  static getConverter(platform: string): PlatformConverter<any> {
    switch (platform) {
      case 'figma': return new FigmaConverter()
      case 'umg': return new UMGConverter()
      case 'mockingbot': return new MockingbotConverter()
      default: throw new Error(`Unsupported platform: ${platform}`)
    }
  }
}
```

### 4. 服务类 (`utils/`)

#### importExportService.ts (待实现)
```typescript
export class ImportExportService {
  async importFromPlatform(platform: string, data: any, config: ImportConfig) {
    const converter = ConverterFactory.getConverter(platform)
    return await converter.import(data, config)
  }
  
  async exportToPlatform(instances: ComponentInstance[], platform: string, config: ExportConfig) {
    const converter = ConverterFactory.getConverter(platform)
    return await converter.export(instances, config)
  }
}
```

#### resourceManager.ts (待实现)
```typescript
export class ResourceManager {
  async uploadImage(file: File): Promise<string>
  async downloadImage(url: string): Promise<Blob>
  async convertImageFormat(blob: Blob, format: string): Promise<Blob>
  async manageFont(fontFamily: string): Promise<FontInfo>
}
```

### 5. API接口 (`apis/`)

#### figmaApi.ts (已集成)
- 当前实现在 `figmaConverter.ts` 中
- 包含文件获取、节点查询、图片导出等功能

#### 待实现API
- **mockingbotApi.ts**: 墨刀平台API封装
- **lanhuApi.ts**: 蓝湖平台API封装
- **platformApis.ts**: 通用API工具

## 开发优先级

### 第一优先级 (立即开始)
1. **ImportDialog.vue** - 用户导入界面
2. **ExportDialog.vue** - 用户导出界面
3. **converters/index.ts** - 转换器统一入口

### 第二优先级 (1-2周内)
1. **importExportService.ts** - 核心业务逻辑
2. **mockingbotConverter.ts** - 墨刀转换器
3. **resourceManager.ts** - 资源管理

### 第三优先级 (1个月内)
1. **PlatformPreview.vue** - 预览功能
2. **lanhuConverter.ts** - 蓝湖转换器
3. **性能优化和错误处理**

## 集成到主应用

### 1. 在PageDesigner.vue中添加导入导出按钮
```vue
<template>
  <div class="designer-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button @click="showImportDialog">导入设计</el-button>
      <el-button @click="showExportDialog">导出设计</el-button>
    </div>
    
    <!-- 现有设计器内容 -->
    <!-- ... -->
    
    <!-- 导入导出对话框 -->
    <ImportDialog v-model="importDialogVisible" @import="handleImport" />
    <ExportDialog v-model="exportDialogVisible" @export="handleExport" />
  </div>
</template>
```

### 2. 集成转换服务
```typescript
import { ImportExportService } from './utils/importExportService'

const importExportService = new ImportExportService()

async function handleImport(platform: string, data: any, config: ImportConfig) {
  try {
    const instances = await importExportService.importFromPlatform(platform, data, config)
    componentInstances.value = instances
  } catch (error) {
    ElMessage.error(`导入失败: ${error.message}`)
  }
}
```

## 测试策略

### 1. 单元测试目录结构
```
tests/
├── unit/
│   ├── converters/
│   │   ├── figmaConverter.test.ts
│   │   ├── umgConverter.test.ts
│   │   └── mockingbotConverter.test.ts
│   ├── utils/
│   │   ├── importExportService.test.ts
│   │   └── resourceManager.test.ts
│   └── components/
│       ├── ImportDialog.test.ts
│       └── ExportDialog.test.ts
└── fixtures/
    ├── figma-sample.json
    ├── umg-sample.json
    └── mockingbot-sample.json
```

### 2. 测试数据管理
- 在 `tests/fixtures/` 中存放测试用的示例数据
- 包含各平台的典型设计文件格式
- 覆盖正常情况和边界情况

## 部署和构建

### 1. 环境变量配置
```env
# Figma API配置
VITE_FIGMA_CLIENT_ID=your_figma_client_id
VITE_FIGMA_CLIENT_SECRET=your_figma_client_secret

# 其他平台API配置
VITE_MOCKINGBOT_API_KEY=your_mockingbot_key
```

### 2. 构建优化
- 转换器按需加载：`import()`动态导入
- 图片资源压缩和CDN部署
- API响应缓存策略

---

*此结构文档将作为开发团队的参考指南，确保代码组织的一致性和可维护性。* 