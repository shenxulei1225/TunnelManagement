# 统一树形组件设计方案

## 概述

基于项目中现有的多个树形组件分析，设计一个真正统一的`UniversalTreeView`组件，通过参数配置实现所有功能需求，替代项目中的所有树形组件实现。

## 功能需求分析

### 1. 拖拽功能支持
- **节点拖拽排序**：支持同级节点排序
- **跨级拖拽**：支持节点在不同层级间移动
- **拖拽限制**：可配置哪些节点可拖拽、哪些位置可放置
- **拖拽回调**：提供拖拽开始、进行中、结束的回调函数

### 2. 动态字段显示
- **字段标签展示**：在节点旁显示动态字段标签
- **字段类型区分**：不同类型字段使用不同样式
- **字段值预览**：支持显示字段值的简要信息
- **字段编辑**：支持直接编辑字段值

### 3. 展开/折叠状态管理
- **状态持久化**：展开状态在操作后保持
- **智能展开**：搜索时自动展开匹配路径
- **批量操作**：支持全部展开/折叠
- **记忆功能**：记住用户的展开偏好

### 4. 其他核心功能
- **搜索高亮**：关键词高亮显示
- **右键菜单**：可配置的上下文菜单
- **多选支持**：支持checkbox多选
- **虚拟滚动**：大数据量优化
- **自定义渲染**：支持自定义节点内容

## 组件设计

### 核心属性配置

```typescript
interface UniversalTreeProps {
  // === 数据相关 ===
  data: TreeNode[]
  nodeKey?: string
  props?: TreeProps
  
  // === 拖拽功能 ===
  draggable?: boolean
  dragConfig?: {
    allowDrag?: (node: TreeNode) => boolean
    allowDrop?: (draggingNode: TreeNode, dropNode: TreeNode, type: 'prev' | 'inner' | 'next') => boolean
    ghostClass?: string
    animation?: number
    dragHandle?: string // CSS选择器
  }
  
  // === 动态字段显示 ===
  showDynamicFields?: boolean
  fieldConfig?: {
    fields: DynamicField[]
    fieldRenderer?: (field: DynamicField, value: any) => string
    fieldEditor?: (field: DynamicField, value: any) => VNode
    showFieldCount?: boolean
    maxFieldDisplay?: number
  }
  
  // === 展开/折叠控制 ===
  expandConfig?: {
    defaultExpandAll?: boolean
    expandOnClickNode?: boolean
    autoExpandParent?: boolean
    persistExpandState?: boolean
    expandedKeys?: string[]
    expandMemoryKey?: string // localStorage key
  }
  
  // === 搜索功能 ===
  searchConfig?: {
    searchable?: boolean
    searchPlaceholder?: string
    searchFields?: string[] // 搜索哪些字段
    highlightMatch?: boolean
    caseSensitive?: boolean
    autoExpandOnSearch?: boolean
  }
  
  // === 显示控制 ===
  displayConfig?: {
    showIcon?: boolean
    showCheckbox?: boolean
    showActions?: boolean
    showToolbar?: boolean
    showStats?: boolean
    height?: string
    virtualScroll?: boolean
    itemHeight?: number
  }
  
  // === 交互控制 ===
  interactionConfig?: {
    editable?: boolean
    deletable?: boolean
    addable?: boolean
    copyable?: boolean
    showContextMenu?: boolean
    highlightCurrent?: boolean
    checkStrictly?: boolean
  }
  
  // === 自定义渲染 ===
  renderConfig?: {
    nodeRenderer?: (node: TreeNode, data: any) => VNode
    iconRenderer?: (node: TreeNode) => string | VNode
    labelRenderer?: (node: TreeNode) => string | VNode
    tagRenderer?: (node: TreeNode) => { type: string; text: string; class?: string }
    actionsRenderer?: (node: TreeNode) => VNode[]
  }
  
  // === 样式配置 ===
  styleConfig?: {
    nodeClass?: string | ((node: TreeNode) => string)
    selectedClass?: string
    hoverClass?: string
    disabledClass?: string
    customStyles?: Record<string, any>
  }
  
  // === 文本配置 ===
  textConfig?: {
    emptyText?: string
    noResultsText?: string
    expandAllText?: string
    collapseAllText?: string
    addText?: string
    editText?: string
    deleteText?: string
    copyText?: string
    cutText?: string
    pasteText?: string
  }
}
```

### 动态字段配置

```typescript
interface DynamicField {
  id: string
  key: string
  label: string
  type: 'string' | 'number' | 'date' | 'enum' | 'boolean' | 'image' | 'computed'
  required?: boolean
  unit?: string
  enumOptions?: { value: any; label: string }[]
  computeExpr?: string
  displayOrder?: number
  editable?: boolean
  showInTree?: boolean
  treeDisplayType?: 'tag' | 'text' | 'icon' | 'badge'
  validation?: {
    min?: number
    max?: number
    pattern?: string
    custom?: (value: any) => boolean | string
  }
}
```

### 事件系统

```typescript
interface UniversalTreeEvents {
  // 节点交互
  'node-click': (node: TreeNode, event: MouseEvent) => void
  'node-dblclick': (node: TreeNode, event: MouseEvent) => void
  'node-contextmenu': (node: TreeNode, event: MouseEvent) => void
  
  // 展开/折叠
  'node-expand': (node: TreeNode) => void
  'node-collapse': (node: TreeNode) => void
  'expand-change': (expandedKeys: string[]) => void
  
  // 选择
  'selection-change': (selectedNodes: TreeNode[]) => void
  'check-change': (checkedNodes: TreeNode[], checkedKeys: string[]) => void
  
  // 拖拽
  'drag-start': (node: TreeNode, event: DragEvent) => void
  'drag-over': (draggingNode: TreeNode, dropNode: TreeNode, type: string) => void
  'drag-end': (draggingNode: TreeNode, dropNode: TreeNode, type: string) => void
  
  // 编辑操作
  'node-add': (parent: TreeNode, newNode: TreeNode) => void
  'node-edit': (node: TreeNode, field: string, oldValue: any, newValue: any) => void
  'node-delete': (node: TreeNode) => void
  'node-copy': (node: TreeNode) => void
  'node-cut': (node: TreeNode) => void
  'node-paste': (targetNode: TreeNode, clipboardNode: TreeNode) => void
  
  // 搜索
  'search': (keyword: string, results: TreeNode[]) => void
  'search-clear': () => void
  
  // 动态字段
  'field-change': (node: TreeNode, field: DynamicField, value: any) => void
  'field-validate': (node: TreeNode, field: DynamicField, value: any) => boolean | string
}
```

## 使用示例

### 1. 基础树形组件（替代CategoryTree）

```vue
<template>
  <UniversalTreeView
    :data="categoryData"
    :expand-config="{ defaultExpandAll: false, persistExpandState: true }"
    :search-config="{ searchable: true, autoExpandOnSearch: true }"
    :display-config="{ showToolbar: true, showStats: true }"
    :interaction-config="{ editable: true, deletable: true, addable: true }"
    @node-click="handleNodeClick"
    @node-add="handleAddCategory"
    @node-edit="handleEditCategory"
    @node-delete="handleDeleteCategory"
  />
</template>
```

### 2. 支持拖拽的设计树（替代DesignTreeView）

```vue
<template>
  <UniversalTreeView
    :data="designData"
    :draggable="true"
    :drag-config="{
      allowDrag: (node) => node.type !== 'root',
      allowDrop: (dragging, drop, type) => drop.type === 'container' && type === 'inner',
      animation: 200,
      ghostClass: 'drag-ghost'
    }"
    :display-config="{ showIcon: true, showActions: false }"
    :render-config="{
      iconRenderer: (node) => getComponentIcon(node.type),
      tagRenderer: (node) => ({ type: getComponentTagType(node.type), text: node.type })
    }"
    @drag-end="handleNodeDrop"
  />
</template>
```

### 3. 动态字段显示（替代分类管理）

```vue
<template>
  <UniversalTreeView
    :data="categoryData"
    :show-dynamic-fields="true"
    :field-config="{
      fields: dynamicFields,
      showFieldCount: true,
      maxFieldDisplay: 3
    }"
    :expand-config="{
      persistExpandState: true,
      expandMemoryKey: 'category-tree-expand'
    }"
    @field-change="handleFieldChange"
    @node-click="handleCategorySelect"
  />
</template>

<script setup>
const dynamicFields = ref([
  {
    id: 'name',
    key: 'name',
    label: '名称',
    type: 'string',
    required: true,
    showInTree: true,
    treeDisplayType: 'text'
  },
  {
    id: 'count',
    key: 'itemCount',
    label: '项目数',
    type: 'number',
    showInTree: true,
    treeDisplayType: 'badge'
  }
])
</script>
```

### 4. 固定展开状态（替代UETreeView）

```vue
<template>
  <UniversalTreeView
    :data="ueData"
    :expand-config="{
      defaultExpandAll: true,
      expandOnClickNode: false,
      persistExpandState: true,
      expandedKeys: fixedExpandedKeys
    }"
    :display-config="{ 
      showCheckbox: true,
      showToolbar: false,
      virtualScroll: true,
      itemHeight: 32
    }"
    :style-config="{
      nodeClass: 'ue-tree-node',
      selectedClass: 'ue-selected',
      customStyles: ueTreeStyles
    }"
    @expand-change="handleExpandChange"
  />
</template>
```

## 实现优势

### 1. 统一性
- **一致的API**：所有树形组件使用相同的接口
- **统一的样式**：保证视觉一致性
- **统一的行为**：相同的交互逻辑

### 2. 灵活性
- **按需配置**：只启用需要的功能
- **高度可定制**：支持自定义渲染和样式
- **可扩展**：易于添加新功能

### 3. 性能优化
- **虚拟滚动**：处理大量数据
- **智能更新**：只更新变化的节点
- **内存管理**：自动清理不需要的数据

### 4. 开发效率
- **减少重复代码**：90%的树形相关代码可复用
- **快速开发**：新页面只需配置参数
- **易于维护**：集中管理所有树形组件逻辑

## 迁移计划

### 第一阶段：核心功能实现
1. 实现基础的UniversalTreeView组件
2. 支持基本的展开/折叠、搜索、选择功能
3. 实现拖拽功能支持

### 第二阶段：动态字段支持
1. 实现动态字段显示功能
2. 支持字段编辑和验证
3. 实现字段在树中的不同展示方式

### 第三阶段：高级功能
1. 实现虚拟滚动优化
2. 添加右键菜单和批量操作
3. 实现状态持久化功能

### 第四阶段：组件迁移
1. 逐步替换现有的树形组件
2. 提供兼容性适配器
3. 更新相关文档和示例

## 菜单配置参数

使用统一组件后，新页面的菜单配置将更加简单：

```javascript
// 通用树形管理页面菜单配置
const createTreePageMenuConfig = (options) => ({
  name: options.name,
  type: 1,
  sort: options.sort,
  parentId: options.parentId,
  path: options.path,
  icon: options.icon || 'ep:files',
  component: 'common/UniversalTreePage', // 统一的页面组件
  componentName: options.componentName,
  permission: options.permission,
  status: 0,
  visible: true,
  keepAlive: true,
  alwaysShow: false,
  // 页面配置参数
  meta: {
    treeConfig: options.treeConfig, // 树形组件配置
    apiConfig: options.apiConfig,   // API配置
    fieldConfig: options.fieldConfig // 字段配置
  }
})
```

## 总结

通过这个统一的`UniversalTreeView`组件，我们可以：

1. **解决现有问题**：统一所有树形组件的实现
2. **提升开发效率**：新页面只需配置参数
3. **保证一致性**：统一的交互和视觉体验
4. **简化维护**：集中管理所有树形相关逻辑
5. **支持扩展**：易于添加新功能和定制需求

这个方案完全符合您提出的需求：通过参数控制展示内容和操作效果，一个组件实现所有树形功能需求。 