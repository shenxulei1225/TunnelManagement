# UX工作台整合方案

## 现状分析

### 功能重复的页面

1. **`custom-page/PageDesigner.vue`** - UMG风格页面设计器
   - ✅ 专业三栏布局（层级树+组件库、设计画布、属性面板）
   - ✅ 深色主题，类似VS Code
   - ✅ 拖拽式组件添加
   - ✅ 精确定位和大小调整
   - ✅ 网格背景和标尺系统
   - ✅ 撤销/重做功能
   - ✅ 丰富的组件库（表单、展示、布局、高级交互）

2. **`converter/ConverterView.vue`** - 转换器工作台
   - ✅ 三栏布局（组件树、设计画布、属性面板）
   - ✅ 设计导入/导出功能
   - ✅ 多设备预览模式
   - ✅ 内容导入功能
   - ✅ 文件管理功能

3. **`converter/index.vue`** - 转换器流程页面
   - ✅ 步骤化导入流程
   - ✅ 设计树预览
   - ✅ 转换配置
   - ✅ 代码生成

## 整合方案

### 🎯 统一为一个UX工作台

**保留**: `custom-page/PageDesigner.vue` 作为主工作台
**原因**: 
- 更专业的设计器界面
- 已有完整的UMG风格实现
- 功能更完善（网格、标尺、撤销重做等）

**整合内容**:
1. 将`ConverterView.vue`的导入/导出功能整合到PageDesigner
2. 将`index.vue`的步骤化流程作为可选的引导模式
3. 将转换器的多设备预览功能添加到PageDesigner

### 📁 新的文件结构

```
src/views/ux-designer/
├── index.vue                    # 主入口（整合后的UX工作台）
├── components/
│   ├── Workspace/
│   │   ├── DesignerViewport.vue    # 设计画布（来自PageDesigner）
│   │   ├── ResourcePanel.vue       # 资源面板（整合组件库+资产库）
│   │   ├── PropertiesPanel.vue     # 属性面板
│   │   ├── LayerPanel.vue          # 图层面板
│   │   └── Toolbar.vue             # 工具栏
│   ├── Import/
│   │   ├── ImportWizard.vue        # 导入向导（来自index.vue）
│   │   ├── FigmaImporter.vue       # Figma导入器
│   │   └── ContentImporter.vue     # 内容导入器
│   ├── Export/
│   │   ├── ExportDialog.vue        # 导出对话框
│   │   └── CodeGenerator.vue       # 代码生成器
│   └── Preview/
│       ├── PreviewDialog.vue       # 预览对话框
│       └── DeviceSimulator.vue     # 设备模拟器
├── types/
│   └── workspace.ts               # 工作台类型定义
└── utils/
    ├── designManager.ts           # 设计管理器
    ├── componentFactory.ts        # 组件工厂
    └── fileManager.ts             # 文件管理器
```

### 🚀 实施步骤

#### 第一阶段：核心整合
1. **重命名和移动**
   ```bash
   # 将PageDesigner重命名为主工作台
   mv src/views/custom-page src/views/ux-designer
   mv src/views/ux-designer/PageDesigner.vue src/views/ux-designer/index.vue
   ```

2. **整合导入功能**
   - 将`converter/ImportDialog.vue`整合到`ux-designer/components/Import/`
   - 将`converter/index.vue`的步骤化流程提取为ImportWizard组件

3. **整合导出功能**
   - 将`converter/ExportDialog.vue`移动到`ux-designer/components/Export/`
   - 整合转换器核心引擎到工作台

#### 第二阶段：功能增强
1. **左侧面板改进**
   ```
   左侧面板标签页：
   📁 文件管理    - 项目文件管理
   🧩 组件库      - 基础组件
   🖼️ 资产库      - 图片、图标等资产
   🌳 图层树      - 组件层级结构
   ```

2. **工具栏增强**
   ```
   工具栏功能：
   导入: 📥 Figma导入 | 📄 JSON导入 | 📦 内容导入
   操作: ↶ 撤销 | ↷ 重做 | 📋 复制 | 📌 对齐
   视图: 🖥️ 桌面 | 📱 平板 | 📱 手机 | 🔍 缩放
   导出: 👁️ 预览 | 🚀 生成代码 | 💾 保存项目
   ```

3. **画布功能增强**
   - 保留UMG的网格和标尺
   - 添加多设备预览模式
   - 支持拖拽式导入（从Figma、文件等）

#### 第三阶段：体验优化
1. **布局参考主流工具**
   ```
   参考Figma布局：
   ┌─────────────────────────────────────────────────────────┐
   │  🎨 Universal X Designer              🖥️📱📱  👁️🚀💾  │  <- 顶部工具栏
   ├─────────────┬─────────────────────────┬─────────────────┤
   │📁📦🧩🌳    │                         │ ⚙️ 属性面板      │
   │             │                         │                 │
   │ 文件/组件/  │     🎨 设计画布         │ 组件属性        │
   │ 资产/图层   │                         │ 样式设置        │
   │             │                         │ 事件配置        │
   │             │                         │                 │
   └─────────────┴─────────────────────────┴─────────────────┘
   ```

2. **用户体验优化**
   - 统一的快捷键系统
   - 智能的组件推荐
   - 模板库和示例项目
   - 实时协作功能预留

### 📊 迁移对比

| 功能 | PageDesigner | ConverterView | 整合后 |
|------|-------------|---------------|--------|
| 界面布局 | ✅ 专业三栏 | ✅ 简单三栏 | ✅ 增强三栏 |
| 组件库 | ✅ 丰富 | ⚠️ 基础 | ✅ 整合增强 |
| 拖拽操作 | ✅ 完善 | ⚠️ 基础 | ✅ 保持完善 |
| 导入功能 | ❌ 无 | ✅ 多种 | ✅ 全面支持 |
| 导出功能 | ❌ 无 | ✅ 代码生成 | ✅ 增强导出 |
| 设备预览 | ❌ 无 | ✅ 三种模式 | ✅ 整合支持 |
| 网格标尺 | ✅ 专业 | ❌ 无 | ✅ 保持专业 |
| 撤销重做 | ✅ 50步 | ⚠️ 基础 | ✅ 保持50步 |
| 文件管理 | ❌ 无 | ✅ 本地存储 | ✅ 增强管理 |

### 🎯 最终目标

创建一个统一的**Universal X Designer**工作台：
- **专业性**: 保持UMG风格的专业设计体验
- **完整性**: 整合所有导入、设计、导出功能
- **一致性**: 统一的界面风格和交互逻辑
- **可扩展性**: 为未来功能扩展预留空间

### 📝 待删除的文件

完成整合后，可以删除：
- `src/views/converter/ConverterView.vue`
- 或将其重构为简化版的快速转换页面

保留：
- `src/views/converter/index.vue` - 作为引导式转换流程
- `src/views/converter/demo.vue` - 作为功能演示

这样既避免了功能重复，又为不同用户群体提供了合适的入口。 