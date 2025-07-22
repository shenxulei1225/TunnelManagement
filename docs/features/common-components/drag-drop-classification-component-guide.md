# 通用拖拽归类组件使用指南

## 概述

`DragDropClassification` 是一个通用的拖拽归类组件，可以用于各种业务场景的分类管理，如字段管理、设备管理、任务管理等。该组件完全通过配置驱动，不包含任何业务相关的字段或展示信息。

## 核心特性

- **通用性**: 通过配置适配不同业务场景
- **拖拽功能**: 支持树节点的拖拽排序和归类
- **双视图**: 支持卡片视图和表格视图
- **搜索功能**: 支持分类和项目的搜索
- **操作管理**: 支持增删改查等基本操作
- **事件驱动**: 通过事件回调处理业务逻辑

## 组件结构

```
DragDropClassification
├── 页面头部 (标题、操作按钮)
├── 主要内容
│   ├── 左侧面板 (分类树)
│   └── 右侧面板 (项目列表)
└── 对话框 (添加/编辑分类、项目)
```

## 配置接口

### DragDropClassificationConfig

```typescript
interface DragDropClassificationConfig {
  // 页面配置
  pageTitle: string
  refreshText: string
  addCategoryText: string
  addItemText: string
  
  // 分类配置
  categoryTitle: string
  searchCategoryPlaceholder: string
  
  // 项目配置
  itemUnit: string
  searchItemPlaceholder: string
  cardViewText: string
  tableViewText: string
  groupText: string
  deleteText: string
  operationText: string
  emptyText: string
  
  // 项目字段配置
  itemLabelField: string
  itemLabelText: string
  itemKeyField: string
  itemKeyText: string
  itemTypeField: string
  itemTypeText: string
  
  // 业务配置
  business: {
    treeType: string
    dataSource: string
    permissions: string[]
    nodeType: string
  }
  
  // 树配置
  treeConfig: any
  
  // 操作配置
  operations: {
    createApi: Function
    updateApi: Function
    deleteApi: Function
    moveApi: Function
  }
  
  // 消息配置
  messages: {
    createSuccess: string
    createError: string
    updateSuccess: string
    updateError: string
    deleteSuccess: string
    deleteError: string
    deleteConfirm: string
    dragSuccess: string
    dragError: string
    maxDepthExceeded: string
    cycleNotAllowed: string
  }
  
  // 拖拽规则
  dragRules: {
    maxDepth: number
    allowCycle: boolean
  }
  
  // 自定义函数
  getItemLabel: (item: any) => string
  getItemType: (item: any) => string
  getItemExtra: (item: any) => string
  getHeaderTitle: (selectedNode: any) => string
}
```

## 使用示例

### 1. 字段管理场景

```vue
<template>
  <DragDropClassification
    :config="fieldConfig"
    :tree-data="treeData"
    :item-list-data="fieldListData"
    @tree-data-change="handleTreeDataChange"
    @item-list-data-change="handleItemListDataChange"
    @item-click="handleItemClick"
    @item-group="handleItemGroup"
    @item-delete="handleItemDelete"
    @category-create="handleCategoryCreate"
    @category-update="handleCategoryUpdate"
    @category-delete="handleCategoryDelete"
    @item-create="handleItemCreate"
    @item-update="handleItemUpdate"
    @item-delete="handleItemDelete"
    @drag-success="handleDragSuccess"
    @drag-error="handleDragError"
  />
</template>

<script setup lang="ts">
import DragDropClassification from '@/components/Common/DragDropClassification.vue'
import type { DragDropClassificationConfig } from '@/components/Common/DragDropClassification.vue'

// 字段管理配置
const fieldConfig: DragDropClassificationConfig = {
  pageTitle: '字段管理',
  refreshText: '刷新',
  addCategoryText: '添加分类',
  addItemText: '添加字段',
  categoryTitle: '字段分类',
  searchCategoryPlaceholder: '搜索分类...',
  itemUnit: '个字段',
  searchItemPlaceholder: '搜索字段...',
  cardViewText: '卡片视图',
  tableViewText: '表格视图',
  groupText: '分组',
  deleteText: '删除',
  operationText: '操作',
  emptyText: '暂无字段数据',
  itemLabelField: 'fieldName',
  itemLabelText: '字段名称',
  itemKeyField: 'fieldKey',
  itemKeyText: '字段标识',
  itemTypeField: 'fieldType',
  itemTypeText: '字段类型',
  business: {
    treeType: 'field_category',
    dataSource: 'universal_tree',
    permissions: ['field:read', 'field:write', 'field:delete'],
    nodeType: 'category'
  },
  treeConfig: {
    dataAdapter: {
      idField: 'id',
      nameField: 'name',
      childrenField: 'children',
      parentIdField: 'parentId'
    },
    nodeConfig: {
      showIcon: true,
      showCount: true,
      showActions: true,
      actions: ['add', 'edit', 'delete'],
      nameField: 'name',
      iconField: 'icon',
      defaultIcon: 'Folder',
      iconMapping: {
        category: 'Folder',
        field: 'Document'
      }
    },
    interaction: {
      draggable: true,
      selectable: true,
      checkable: false,
      checkStrictly: false
    }
  },
  operations: {
    createApi: async (treeType: string, data: any) => {
      return await universalTreeApi.addTreeNode(treeType, data)
    },
    updateApi: async (treeType: string, id: number, data: any) => {
      return await universalTreeApi.updateTreeNode(treeType, id, data)
    },
    deleteApi: async (treeType: string, id: number) => {
      return await universalTreeApi.deleteTreeNode(treeType, id)
    },
    moveApi: async (treeType: string, nodeId: number, targetParentId: number, position: number) => {
      return await universalTreeApi.moveTreeNode(treeType, nodeId, targetParentId, position)
    }
  },
  messages: {
    createSuccess: '创建成功',
    createError: '创建失败',
    updateSuccess: '更新成功',
    updateError: '更新失败',
    deleteSuccess: '删除成功',
    deleteError: '删除失败',
    deleteConfirm: '确定要删除这个分类吗？',
    dragSuccess: '拖拽成功',
    dragError: '拖拽失败',
    maxDepthExceeded: '超出最大层级限制',
    cycleNotAllowed: '不允许循环引用'
  },
  dragRules: {
    maxDepth: 3,
    allowCycle: false
  },
  getItemLabel: (item: any) => item.fieldName || '未知字段',
  getItemType: (item: any) => item.fieldType || '未知类型',
  getItemExtra: (item: any) => item.unit || '',
  getHeaderTitle: (selectedNode: any) => {
    if (selectedNode) {
      return `${selectedNode.name} - 字段列表`
    }
    return '所有字段'
  }
}

// 事件处理方法
const handleTreeDataChange = (data: TreeNode[]) => {
  // 处理树数据变化
}

const handleItemListDataChange = (data: any[]) => {
  // 处理项目列表数据变化
}

const handleItemClick = (item: any) => {
  // 处理项目点击
}

const handleItemGroup = (item: any) => {
  // 处理项目分组
}

const handleItemDelete = async (item: any) => {
  // 处理项目删除
}

const handleCategoryCreate = async (data: any) => {
  // 处理分类创建
}

const handleCategoryUpdate = async (data: any) => {
  // 处理分类更新
}

const handleCategoryDelete = async (data: any) => {
  // 处理分类删除
}

const handleItemCreate = async (data: any) => {
  // 处理项目创建
}

const handleItemUpdate = async (data: any) => {
  // 处理项目更新
}

const handleDragSuccess = (draggedData: any, dropData: any, dropType: string) => {
  // 处理拖拽成功
}

const handleDragError = (draggedData: any, dropData: any, dropType: string) => {
  // 处理拖拽失败
}
</script>
```

### 2. 设备管理场景

```typescript
// 设备管理配置
const deviceConfig: DragDropClassificationConfig = {
  pageTitle: '设备管理',
  refreshText: '刷新',
  addCategoryText: '添加区域',
  addItemText: '添加设备',
  categoryTitle: '设备区域',
  searchCategoryPlaceholder: '搜索区域...',
  itemUnit: '台设备',
  searchItemPlaceholder: '搜索设备...',
  cardViewText: '卡片视图',
  tableViewText: '表格视图',
  groupText: '分组',
  deleteText: '删除',
  operationText: '操作',
  emptyText: '暂无设备数据',
  itemLabelField: 'deviceName',
  itemLabelText: '设备名称',
  itemKeyField: 'deviceCode',
  itemKeyText: '设备编号',
  itemTypeField: 'deviceType',
  itemTypeText: '设备类型',
  business: {
    treeType: 'device_area',
    dataSource: 'universal_tree',
    permissions: ['device:read', 'device:write', 'device:delete'],
    nodeType: 'area'
  },
  // ... 其他配置
  getItemLabel: (item: any) => item.deviceName || '未知设备',
  getItemType: (item: any) => item.deviceType || '未知类型',
  getItemExtra: (item: any) => item.status || '',
  getHeaderTitle: (selectedNode: any) => {
    if (selectedNode) {
      return `${selectedNode.name} - 设备列表`
    }
    return '所有设备'
  }
}
```

### 3. 任务管理场景

```typescript
// 任务管理配置
const taskConfig: DragDropClassificationConfig = {
  pageTitle: '任务管理',
  refreshText: '刷新',
  addCategoryText: '添加类型',
  addItemText: '添加任务',
  categoryTitle: '任务类型',
  searchCategoryPlaceholder: '搜索类型...',
  itemUnit: '个任务',
  searchItemPlaceholder: '搜索任务...',
  cardViewText: '卡片视图',
  tableViewText: '表格视图',
  groupText: '分组',
  deleteText: '删除',
  operationText: '操作',
  emptyText: '暂无任务数据',
  itemLabelField: 'taskName',
  itemLabelText: '任务名称',
  itemKeyField: 'taskCode',
  itemKeyText: '任务编号',
  itemTypeField: 'taskType',
  itemTypeText: '任务类型',
  business: {
    treeType: 'task_category',
    dataSource: 'universal_tree',
    permissions: ['task:read', 'task:write', 'task:delete'],
    nodeType: 'category'
  },
  // ... 其他配置
  getItemLabel: (item: any) => item.taskName || '未知任务',
  getItemType: (item: any) => item.taskType || '未知类型',
  getItemExtra: (item: any) => item.priority || '',
  getHeaderTitle: (selectedNode: any) => {
    if (selectedNode) {
      return `${selectedNode.name} - 任务列表`
    }
    return '所有任务'
  }
}
```

## 事件说明

### 树相关事件

- `@tree-data-change`: 树数据变化时触发
- `@category-create`: 创建分类时触发
- `@category-update`: 更新分类时触发
- `@category-delete`: 删除分类时触发

### 项目相关事件

- `@item-list-data-change`: 项目列表数据变化时触发
- `@item-click`: 点击项目时触发
- `@item-group`: 项目分组时触发
- `@item-delete`: 删除项目时触发
- `@item-create`: 创建项目时触发
- `@item-update`: 更新项目时触发

### 拖拽相关事件

- `@drag-success`: 拖拽成功时触发
- `@drag-error`: 拖拽失败时触发

## 配置要点

### 1. 业务配置

```typescript
business: {
  treeType: string,        // 树类型标识
  dataSource: string,      // 数据源
  permissions: string[],   // 权限列表
  nodeType: string        // 节点类型
}
```

### 2. 树配置

```typescript
treeConfig: {
  dataAdapter: {
    idField: string,           // ID字段名
    nameField: string,         // 名称字段名
    childrenField: string,     // 子节点字段名
    parentIdField: string      // 父节点ID字段名
  },
  nodeConfig: {
    showIcon: boolean,         // 是否显示图标
    showCount: boolean,        // 是否显示数量
    showActions: boolean,      // 是否显示操作按钮
    actions: string[],         // 操作按钮列表
    nameField: string,         // 名称字段
    iconField: string,         // 图标字段
    defaultIcon: string,       // 默认图标
    iconMapping: object        // 图标映射
  },
  interaction: {
    draggable: boolean,        // 是否可拖拽
    selectable: boolean,       // 是否可选择
    checkable: boolean,        // 是否可勾选
    checkStrictly: boolean     // 是否严格模式
  }
}
```

### 3. 操作配置

```typescript
operations: {
  createApi: Function,     // 创建API
  updateApi: Function,     // 更新API
  deleteApi: Function,     // 删除API
  moveApi: Function        // 移动API
}
```

### 4. 自定义函数

```typescript
// 获取项目标签
getItemLabel: (item: any) => string

// 获取项目类型
getItemType: (item: any) => string

// 获取项目额外信息
getItemExtra: (item: any) => string

// 获取头部标题
getHeaderTitle: (selectedNode: any) => string
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
  deleteText: '删除',
  operationText: '操作',
  // ... 其他通用配置
}

// 字段管理配置
const fieldConfig = {
  ...baseConfig,
  pageTitle: '字段管理',
  addCategoryText: '添加分类',
  addItemText: '添加字段',
  // ... 字段特有配置
}

// 设备管理配置
const deviceConfig = {
  ...baseConfig,
  pageTitle: '设备管理',
  addCategoryText: '添加区域',
  addItemText: '添加设备',
  // ... 设备特有配置
}
```

### 2. 事件处理

```typescript
// 统一的事件处理
const createEventHandler = (type: string) => {
  return async (data: any) => {
    try {
      // 根据类型调用不同的API
      const result = await apiMap[type](data)
      ElMessage.success('操作成功')
      return result
    } catch (error) {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
      throw error
    }
  }
}

const apiMap = {
  field: {
    create: fieldApi.createField,
    update: fieldApi.updateField,
    delete: fieldApi.deleteField
  },
  device: {
    create: deviceApi.createDevice,
    update: deviceApi.updateDevice,
    delete: deviceApi.deleteDevice
  }
}
```

### 3. 数据转换

```typescript
// 数据转换函数
const transformData = (data: any, type: string) => {
  const transformers = {
    field: (item: any) => ({
      ...item,
      fieldLabel: item.fieldName,
      fieldKey: item.fieldCode,
      valueType: item.fieldType
    }),
    device: (item: any) => ({
      ...item,
      deviceLabel: item.deviceName,
      deviceKey: item.deviceCode,
      deviceType: item.type
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

## 扩展功能

### 1. 自定义对话框

可以通过事件回调实现自定义的添加/编辑对话框：

```typescript
const handleItemCreate = async (data: any) => {
  // 打开自定义对话框
  const dialogVisible = ref(true)
  const formData = reactive({})
  
  // 处理表单提交
  const handleSubmit = async () => {
    const result = await api.createItem(formData)
    dialogVisible.value = false
    return result
  }
}
```

### 2. 批量操作

可以添加批量操作功能：

```typescript
const handleBatchDelete = async (items: any[]) => {
  try {
    await api.batchDelete(items.map(item => item.id))
    ElMessage.success('批量删除成功')
  } catch (error) {
    ElMessage.error('批量删除失败')
  }
}
```

### 3. 导入导出

可以添加数据导入导出功能：

```typescript
const handleExport = async () => {
  const data = await api.exportData()
  downloadFile(data, 'export.xlsx')
}

const handleImport = async (file: File) => {
  const data = await parseExcel(file)
  await api.importData(data)
  ElMessage.success('导入成功')
}
```

通过以上配置和使用方式，`DragDropClassification` 组件可以灵活地适配各种业务场景，实现统一的拖拽归类功能。 