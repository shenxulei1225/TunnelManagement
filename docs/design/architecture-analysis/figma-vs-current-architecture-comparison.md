# Figma+UMG混合架构 vs 当前架构对比分析

> **架构升级指南：从单一画布到Figma+UMG混合设计系统**

---

## 📊 架构对比总览

### 当前架构
```
UX Designer
├── 项目管理 (单一项目)
├── 组件画布 (全局坐标系)
├── 组件实例 (绝对定位)
└── 整体导出 (页面级)
```

### Figma+UMG混合架构
```
UX Designer
├── 项目管理 (多项目Dashboard)
├── 文件管理 (Project → Files)
├── 资源管理 (Project → Assets)
├── 页面系统 (File → Pages)
├── 混合Layer (Figma + UMG)
└── 组件化导出 (Frame级 + UMG级)
```

---

## 🔄 核心变化点

### 1. 数据模型变化

#### 当前模型
```typescript
// 当前数据结构
interface CurrentProject {
  name: string
  canvasPanel: {
    width: number
    height: number
    x: number
    y: number
  }
  componentInstances: ComponentInstance[]  // 全局组件列表
}

interface ComponentInstance {
  id: string
  name: string
  type: string
  x: number      // 全局画布坐标
  y: number      // 全局画布坐标
  width: number
  height: number
}
```

#### Figma+UMG混合模型
```typescript
// 新的混合数据结构
interface Project {
  id: string
  name: string
  description: string
  
  // 文件管理
  files: DesignFile[]           // 支持多个设计文件
  
  // 资源管理
  assets: ProjectAssets         // 项目级资源库
  
  // 模板管理
  templates: ProjectTemplate[]
  
  // 协作管理
  collaborators: Collaborator[]
}

// 项目资源库
interface ProjectAssets {
  images: ImageAsset[]          // 图片资源
  fonts: FontAsset[]            // 字体资源  
  icons: IconAsset[]            // 图标资源
  colors: ColorAsset[]          // 色彩样式
  umgAssets: UMGAsset[]         // UMG资源
  sharedComponents: SharedComponent[]
}

// 混合Layer节点
interface HybridLayerNode {
  id: string
  name: string
  type: 'figma' | 'umg' | 'hybrid'  // 支持混合类型
  
  // Figma风格属性
  figmaProperties?: FigmaElementProps
  
  // UMG风格属性
  umgProperties?: {
    widgetClass: string
    slot: UMGSlot
    anchors: UMGAnchors
    alignment: UMGAlignment
    umgSpecificProps: Record<string, any>
  }
  
  children?: HybridLayerNode[]
}
```

### 2. 界面架构变化

#### 当前界面结构
```
单一工作台
├── 左侧：组件库+图层
├── 中间：单一画布
└── 右侧：属性面板
```

#### Figma+UMG混合界面结构
```
多层级界面
├── Dashboard：项目+文件+资源管理
├── FileEditor：页面+Frame+混合Layer
└── AssetManager：资源分类管理
```

### 3. Layer系统变化

#### 当前Layer系统
- **单一类型**：只支持通用组件
- **平面结构**：简单的组件列表
- **全局坐标**：所有组件使用画布坐标

#### 混合Layer系统
- **多类型支持**：Figma元素 + UMG控件
- **层级结构**：完整的树状层级
- **相对坐标**：Frame内相对坐标系
- **智能转换**：UMG控件自动映射为Figma兼容元素

---

## 🎯 实施优先级和计划

### 阶段1：数据模型升级 (高优先级)

**目标**：建立混合数据结构，支持资源管理

```typescript
// 1.1 扩展项目数据模型
interface EnhancedProject {
  // 保持现有字段 (向后兼容)
  name: string
  canvasPanel: CanvasPanel
  componentInstances: ComponentInstance[]
  
  // 新增字段
  id: string
  files?: DesignFile[]          // 文件管理
  assets?: ProjectAssets        // 资源管理
  version: '1.0' | '2.0'
}

// 1.2 资源管理数据结构
interface ProjectAssets {
  images: ImageAsset[]
  fonts: FontAsset[]
  icons: IconAsset[]
  colors: ColorAsset[]
  umgAssets: UMGAsset[]         // UMG资源支持
  categories: AssetCategory[]
  usage: AssetUsage[]
}

// 1.3 UMG资源定义
interface UMGAsset {
  id: string
  name: string
  type: 'blueprint' | 'widget' | 'animation' | 'texture'
  filePath: string
  metadata: UMGMetadata
  dependencies: string[]
}

// 1.4 数据迁移工具
class HybridDataMigrationManager {
  migrateV1ToV2(v1Project: CurrentProject): EnhancedProject {
    // 将现有项目转换为混合模式
    return {
      ...v1Project,
      id: generateId(),
      files: [this.createDefaultFile(v1Project)],
      assets: this.createDefaultAssets(),      // 空资源库
      version: '2.0'
    }
  }
  
  // UMG导入转换
  importUMGAsset(umgFile: File): Promise<UMGAsset> {
    // 解析UMG文件，提取元数据
    return this.parseUMGFile(umgFile)
  }
}
```

### 阶段2：资源管理系统 (高优先级)

**目标**：实现完整的项目资源管理

```typescript
// 2.1 资源管理器组件
// tunnel-management-ui/src/views/ux-designer/AssetManager.vue
<template>
  <div class="asset-manager">
    <div class="asset-header">
      <h2>📦 {{ project.name }} - 资源管理</h2>
      <div class="asset-actions">
        <el-button @click="uploadAssets">上传资源</el-button>
        <el-button @click="importUMG">导入UMG</el-button>
      </div>
    </div>
    
    <!-- 资源分类 -->
    <div class="asset-categories">
      <el-tabs v-model="activeCategory">
        <el-tab-pane label="📷 图片" name="images">
          <ImageAssetGrid :assets="assets.images" />
        </el-tab-pane>
        <el-tab-pane label="🎯 UMG" name="umg">
          <UMGAssetGrid :assets="assets.umgAssets" />
        </el-tab-pane>
        <el-tab-pane label="🧩 组件" name="components">
          <ComponentAssetGrid :assets="assets.sharedComponents" />
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <!-- UMG导入预览 -->
    <UMGImportDialog 
      v-model="showUMGImport"
      @import="onUMGImport"
    />
  </div>
</template>

// 2.2 UMG导入对话框
// tunnel-management-ui/src/views/ux-designer/components/UMGImportDialog.vue
<template>
  <el-dialog title="🎯 UMG资源导入" v-model="visible">
    <div class="umg-import-content">
      <!-- 文件选择 -->
      <el-upload
        drag
        accept=".uasset,.umap"
        :before-upload="previewUMG"
      >
        <el-icon><Upload /></el-icon>
        <div>拖拽UMG文件到此处，或点击选择</div>
      </el-upload>
      
      <!-- UMG预览 -->
      <div v-if="umgPreview" class="umg-preview">
        <h3>{{ umgPreview.name }}</h3>
        <div class="umg-structure">
          <UMGStructureTree :structure="umgPreview.structure" />
        </div>
      </div>
    </div>
    
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button 
        type="primary" 
        @click="confirmImport"
        :disabled="!umgPreview"
      >
        导入到项目
      </el-button>
    </template>
  </el-dialog>
</template>
```

### 阶段3：混合Layer系统 (中优先级)

**目标**：实现Figma+UMG混合Layer树

```typescript
// 3.1 混合Layer管理器
// tunnel-management-ui/src/views/ux-designer/composables/useHybridLayer.ts
export function useHybridLayer(page: Ref<Page>) {
  
  // 构建混合Layer树
  const layerTree = computed(() => {
    const manager = new HybridLayerTreeManager()
    return manager.buildHybridLayerTree(page.value)
  })
  
  // Layer显示模式
  const displayMode = ref<LayerDisplayMode>('unified')
  
  // 过滤Layer节点
  const filteredLayers = computed(() => {
    return layerTree.value.filter(node => {
      switch (displayMode.value) {
        case 'figma_only':
          return node.type === 'figma'
        case 'umg_only':
          return node.type === 'umg'
        case 'unified':
        default:
          return true
      }
    })
  })
  
  // UMG导入为Frame
  const importUMGAsFrame = async (umgAsset: UMGAsset) => {
    const frame = await convertUMGToFrame(umgAsset)
    page.value.frames.push(frame)
    
    // 更新Layer树
    nextTick(() => {
      // Layer树会自动重新计算
    })
  }
  
  return {
    layerTree: filteredLayers,
    displayMode,
    importUMGAsFrame
  }
}

// 3.2 混合Layer组件
// tunnel-management-ui/src/views/ux-designer/components/HybridLayerPanel.vue
<template>
  <div class="hybrid-layer-panel">
    <div class="layer-header">
      <h3>🌳 Layers</h3>
      <div class="layer-mode-switcher">
        <el-button-group size="small">
          <el-button 
            :type="displayMode === 'unified' ? 'primary' : ''"
            @click="displayMode = 'unified'"
          >
            混合
          </el-button>
          <el-button 
            :type="displayMode === 'figma_only' ? 'primary' : ''"
            @click="displayMode = 'figma_only'"
          >
            🎨 Figma
          </el-button>
          <el-button 
            :type="displayMode === 'umg_only' ? 'primary' : ''"
            @click="displayMode = 'umg_only'"
          >
            🎯 UMG
          </el-button>
        </el-button-group>
      </div>
    </div>
    
    <!-- Layer搜索 -->
    <el-input
      v-model="searchQuery"
      placeholder="搜索图层..."
      prefix-icon="Search"
      size="small"
    />
    
    <!-- Layer树 -->
    <div class="layer-tree">
      <HybridLayerNode
        v-for="node in filteredLayers"
        :key="node.id"
        :node="node"
        :display-mode="displayMode"
        @select="selectLayer"
        @toggle-visibility="toggleLayerVisibility"
      />
    </div>
  </div>
</template>

// 3.3 混合Layer节点组件
// tunnel-management-ui/src/views/ux-designer/components/HybridLayerNode.vue
<template>
  <div 
    class="hybrid-layer-node"
    :class="{
      'selected': node.selected,
      'umg-import': node.isUMGImport,
      'figma-element': node.type === 'figma',
      'umg-element': node.type === 'umg'
    }"
    :style="{ marginLeft: `${node.depth * 16}px` }"
    @click="selectNode"
  >
    <!-- 展开按钮 -->
    <el-button
      v-if="node.children?.length"
      size="small"
      text
      @click.stop="toggleExpand"
    >
      <el-icon>
        <ArrowRight v-if="!node.expanded" />
        <ArrowDown v-else />
      </el-icon>
    </el-button>
    
    <!-- 节点图标 -->
    <span class="layer-icon">{{ getNodeIcon(node) }}</span>
    
    <!-- 节点名称 -->
    <span class="layer-name">{{ node.name }}</span>
    
    <!-- UMG标识 -->
    <el-tag 
      v-if="node.isUMGImport" 
      size="small" 
      type="warning"
    >
      UMG
    </el-tag>
    
    <!-- 节点操作 -->
    <div class="layer-actions">
      <el-button
        size="small"
        text
        @click.stop="toggleVisibility"
      >
        <el-icon>
          <View v-if="node.visible" />
          <Hide v-else />
        </el-icon>
      </el-button>
    </div>
    
    <!-- 子节点 -->
    <div v-if="node.expanded && node.children" class="layer-children">
      <HybridLayerNode
        v-for="child in node.children"
        :key="child.id"
        :node="child"
        :display-mode="displayMode"
        @select="$emit('select', $event)"
        @toggle-visibility="$emit('toggle-visibility', $event)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  node: HybridLayerNode
  displayMode: LayerDisplayMode
}

const props = defineProps<Props>()
const emit = defineEmits<{
  select: [nodeId: string]
  'toggle-visibility': [nodeId: string]
}>()

// 获取节点图标
const getNodeIcon = (node: HybridLayerNode): string => {
  if (node.type === 'umg') {
    const umgIcons: Record<string, string> = {
      'Button': '🔘',
      'Text': '📝',
      'Image': '🖼️',
      'Panel': '📦',
      'Canvas': '🎨',
      'HorizontalBox': '↔️',
      'VerticalBox': '↕️'
    }
    return umgIcons[node.nodeType] || '🎯'
  } else {
    const figmaIcons: Record<string, string> = {
      'frame': '📱',
      'rectangle': '🟦',
      'text': '📝',
      'image': '🖼️'
    }
    return figmaIcons[node.nodeType] || '⬜'
  }
}
</script>
```

### 阶段4：UMG代码生成 (中优先级)

**目标**：支持UMG到多平台的代码生成

```typescript
// 4.1 UMG代码生成器
class UMGCodeGenerator {
  
  /**
   * 生成UMG控件的Vue代码
   */
  generateUMGVueComponent(umgWidget: UMGWidget): GeneratedCode {
    const vueTemplate = this.generateUMGVueTemplate(umgWidget)
    const vueScript = this.generateUMGVueScript(umgWidget)
    const vueStyles = this.generateUMGVueStyles(umgWidget)
    
    return {
      fileName: `${umgWidget.name}.vue`,
      code: `<template>
${vueTemplate}
</template>

<script setup lang="ts">
${vueScript}
</script>

<style scoped>
${vueStyles}
</style>`,
      type: 'vue-component',
      umgSource: umgWidget
    }
  }
  
  /**
   * 生成UMG布局的CSS
   */
  private generateUMGLayoutCSS(umgWidget: UMGWidget): string {
    const css: string[] = []
    
    // 根据UMG Slot类型生成布局
    switch (umgWidget.slot.slotType) {
      case 'CanvasSlot':
        css.push('position: absolute;')
        css.push(`left: ${this.convertUMGPosition(umgWidget.anchors.minimum.x)}px;`)
        css.push(`top: ${this.convertUMGPosition(umgWidget.anchors.minimum.y)}px;`)
        break
        
      case 'HorizontalBoxSlot':
        css.push('display: flex;')
        css.push('flex-direction: row;')
        if (umgWidget.slot.sizeRule === 'Fill') {
          css.push('flex: 1;')
        }
        break
        
      case 'VerticalBoxSlot':
        css.push('display: flex;')
        css.push('flex-direction: column;')
        if (umgWidget.slot.sizeRule === 'Fill') {
          css.push('flex: 1;')
        }
        break
    }
    
    return css.join('\n  ')
  }
}
```

---

## ⚡ 迁移策略

### 渐进式迁移方案

#### 1. 向后兼容模式
```typescript
interface MigrationManager {
  // 检测项目版本
  detectProjectVersion(projectData: any): '1.0' | '2.0'
  
  // 兼容模式运行
  runInCompatibilityMode(v1Project: CurrentProject): void
  
  // 资源管理升级
  upgradeToAssetManagement(project: CurrentProject): EnhancedProject
  
  // UMG导入支持
  enableUMGImport(project: EnhancedProject): void
  
  // 混合Layer启用
  enableHybridLayer(project: EnhancedProject): void
}
```

#### 2. 功能标志控制
```typescript
const featureFlags = {
  enableAssetManagement: true,        // 资源管理
  enableUMGImport: true,             // UMG导入
  enableHybridLayer: true,           // 混合Layer
  enableMultipleFiles: false,        // 多文件支持
  enableFrameSystem: false,          // Frame系统
  enableDashboard: true              // Dashboard
}
```

#### 3. 用户体验升级路径
```
Current User Journey:
项目列表 → 单一工作台 → 编辑组件 → 导出页面

New Hybrid User Journey:
Dashboard → 选择项目 → 管理资源/文件 → 混合Layer编辑 → 多格式导出
                      ↓
                   UMG导入 → Frame转换 → 混合设计 → 跨平台代码
```

---

## 🎯 实施建议

### 技术优先级

1. **📊 数据模型扩展** (必须先做)
   - 资源管理数据结构
   - UMG数据结构定义
   - 混合Layer数据模型
   - 向后兼容保证

2. **🎨 资源管理系统** (高优先级)
   - 资源管理器界面
   - UMG导入功能
   - 资源分类和搜索
   - 团队资源共享

3. **🌳 混合Layer系统** (高优先级)
   - Figma+UMG混合树
   - Layer显示模式切换
   - UMG控件映射
   - 智能转换机制

4. **🔧 代码生成升级** (中优先级)
   - UMG到Vue/React转换
   - Frame级别生成
   - 混合组件支持
   - 多平台输出

5. **🚀 用户体验优化** (低优先级)
   - Dashboard完善
   - 动画过渡
   - 性能优化
   - 协作功能

### 开发里程碑

- **里程碑1** (3周)：数据模型扩展 + 资源管理基础
- **里程碑2** (3周)：UMG导入 + 混合Layer基础
- **里程碑3** (4周)：完整混合Layer系统 + UMG转换
- **里程碑4** (3周)：代码生成升级 + 用户体验优化

### 关键技术挑战

1. **🎯 UMG解析**
   - UE蓝图文件解析
   - 控件层级提取
   - 属性映射转换

2. **🔄 坐标系转换**
   - UMG Anchors → Figma相对坐标
   - 布局系统差异处理
   - 响应式适配

3. **🧩 混合渲染**
   - Figma+UMG统一渲染
   - 性能优化
   - 实时预览

4. **🚀 代码质量**
   - UMG语义保持
   - 平台特性适配
   - 可维护性保证

这种混合架构的优势：

- 🎯 **UMG无缝集成** - 支持现有UMG资源直接导入使用
- 📂 **完整资源管理** - 项目级别的统一资源库
- 🌳 **智能Layer系统** - 自动识别和转换不同类型元素
- 🔄 **向后兼容** - 现有项目可以平滑升级
- 🚀 **跨平台输出** - 同时支持Web和游戏平台代码生成
- 👥 **团队协作** - 资源共享和版本管理

这样的设计能够真正实现Figma设计体验与UMG游戏开发的完美融合！ 