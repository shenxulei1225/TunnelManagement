# 通用组件文档索引

本目录包含了项目中各种通用组件的使用指南和设计文档。

## 组件列表

### 1. 拖拽归类组件

- **DragDropClassification** - 通用拖拽归类组件
  - [使用指南](./drag-drop-classification-component-guide.md)
  - 功能：支持各种业务场景的分类管理，如字段管理、设备管理、任务管理等
  - 特点：完全通过配置驱动，不包含任何业务相关的字段或展示信息

### 2. 扩展树组件

- **ExtendTree** - 扩展树组件
  - [使用指南](./extend-tree-component-guide.md)
  - 功能：基于 Element Plus 的 `el-tree` 组件构建，提供丰富的功能和灵活的配置选项
  - 特点：支持拖拽、搜索、操作按钮、自定义节点内容等功能

### 3. 通用树组件

- **UniversalTreeView** - 通用树视图组件
  - [设计文档](./universal-tree-component-design.md)
  - 功能：提供统一的树形结构展示和管理功能
  - 特点：支持多种数据源，提供标准化的树操作接口

## 组件关系

```
DragDropClassification (拖拽归类组件)
├── 使用 ExtendTree 作为分类树
└── 提供完整的拖拽归类功能

ExtendTree (扩展树组件)
├── 基于 Element Plus el-tree
├── 支持拖拽、搜索、操作按钮
└── 可自定义节点内容

UniversalTreeView (通用树组件)
├── 提供标准化的树操作接口
├── 支持多种数据源
└── 作为其他树组件的基础
```

## 使用场景

### 1. 字段管理
- 使用 `DragDropClassification` 组件
- 配置字段分类树和字段列表
- 支持字段的拖拽归类

### 2. 设备管理
- 使用 `DragDropClassification` 组件
- 配置设备区域树和设备列表
- 支持设备的区域归类

### 3. 任务管理
- 使用 `DragDropClassification` 组件
- 配置任务类型树和任务列表
- 支持任务的类型归类

### 4. 通用树展示
- 使用 `ExtendTree` 组件
- 配置树数据和操作
- 支持自定义节点内容

## 快速开始

### 拖拽归类组件

```vue
<template>
  <DragDropClassification
    :config="config"
    :tree-data="treeData"
    :item-list-data="itemListData"
    @tree-data-change="handleTreeDataChange"
    @item-list-data-change="handleItemListDataChange"
  />
</template>

<script setup lang="ts">
import DragDropClassification from '@/components/Common/DragDropClassification.vue'

const config = {
  pageTitle: '字段管理',
  addCategoryText: '添加分类',
  addItemText: '添加字段',
  // ... 其他配置
}
</script>
```

### 扩展树组件

```vue
<template>
  <ExtendTree
    :data="treeData"
    :config="treeConfig"
    :show-search="true"
    :show-actions="true"
    @select="handleNodeSelect"
  />
</template>

<script setup lang="ts">
import ExtendTree from '@/components/extendTree/index.vue'

const treeConfig = {
  dataAdapter: {
    idField: 'id',
    nameField: 'name',
    childrenField: 'children'
  },
  nodeConfig: {
    showIcon: true,
    showActions: true,
    actions: ['add', 'edit', 'delete']
  }
}
</script>
```

## 最佳实践

### 1. 配置复用
对于相似的业务场景，可以创建配置模板：

```typescript
// 基础配置模板
const baseConfig = {
  refreshText: '刷新',
  cardViewText: '卡片视图',
  tableViewText: '表格视图',
  groupText: '分组',
  deleteText: '删除'
}

// 字段管理配置
const fieldConfig = {
  ...baseConfig,
  pageTitle: '字段管理',
  addCategoryText: '添加分类',
  addItemText: '添加字段'
}
```

### 2. 事件处理
统一的事件处理方式：

```typescript
const createEventHandler = (type: string) => {
  return async (data: any) => {
    try {
      const result = await apiMap[type](data)
      ElMessage.success('操作成功')
      return result
    } catch (error) {
      ElMessage.error('操作失败')
      throw error
    }
  }
}
```

### 3. 数据转换
标准化的数据转换：

```typescript
const transformData = (data: any, type: string) => {
  const transformers = {
    field: (item: any) => ({
      ...item,
      fieldLabel: item.fieldName,
      fieldKey: item.fieldCode
    }),
    device: (item: any) => ({
      ...item,
      deviceLabel: item.deviceName,
      deviceKey: item.deviceCode
    })
  }
  
  return transformers[type] ? transformers[type](data) : data
}
```

## 注意事项

1. **配置完整性**: 确保所有必需的配置项都已提供
2. **API一致性**: 确保API接口的返回格式与组件期望的格式一致
3. **错误处理**: 在事件处理函数中妥善处理错误情况
4. **性能优化**: 对于大量数据，考虑使用分页或虚拟滚动
5. **权限控制**: 根据用户权限动态调整可用的操作

## 扩展开发

### 1. 自定义组件
可以基于现有组件进行扩展：

```typescript
// 自定义拖拽归类组件
const CustomDragDropClassification = {
  extends: DragDropClassification,
  props: {
    // 添加自定义属性
  },
  methods: {
    // 添加自定义方法
  }
}
```

### 2. 插件化开发
将通用功能封装为插件：

```typescript
// 拖拽归类插件
const dragDropClassificationPlugin = {
  install(app: App) {
    app.component('DragDropClassification', DragDropClassification)
  }
}
```

### 3. 组合式开发
使用组合式API开发新功能：

```typescript
// 拖拽归类组合函数
export function useDragDropClassification(config: any) {
  // 实现逻辑
  return {
    // 返回方法和数据
  }
}
```

通过以上文档和示例，可以快速了解和使用项目中的各种通用组件。 