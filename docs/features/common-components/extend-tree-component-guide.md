# ExtendTree 扩展树组件使用指南

## 概述

`ExtendTree` 是一个功能强大的扩展树组件，基于 Element Plus 的 `el-tree` 组件构建，提供了丰富的功能和灵活的配置选项。该组件支持拖拽、搜索、操作按钮、自定义节点内容等功能。

## 核心特性

- **拖拽功能**: 支持树节点的拖拽排序和移动
- **搜索功能**: 支持树节点的搜索和过滤
- **操作按钮**: 支持节点的增删改查操作
- **自定义内容**: 支持自定义节点内容渲染
- **状态保存**: 支持树状态的自动保存和恢复
- **事件驱动**: 提供丰富的事件回调

## 组件结构

```
ExtendTree
├── 搜索框 (可选)
├── 树容器
│   ├── 树节点
│   │   ├── 节点内容 (自定义)
│   │   ├── 操作按钮 (可选)
│   │   └── 子节点
│   └── 空状态
└── 加载状态
```

## Props 配置

### 基础配置

```typescript
interface ExtendTreeProps {
  // 数据相关
  data: TreeNode[]                    // 树数据
  config: TreeConfig                  // 树配置
  operations?: TreeOperations         // 操作配置
  dragBusiness?: DragBusinessConfig   // 拖拽业务配置
  
  // 显示控制
  showSearch?: boolean                // 是否显示搜索框
  showActions?: boolean               // 是否显示操作按钮
  searchPlaceholder?: string          // 搜索框占位符
  autoSaveState?: boolean             // 是否自动保存状态
  defaultExpandAll?: boolean          // 是否默认展开所有节点
  
  // 交互控制
  draggable?: boolean                 // 是否可拖拽
  selectable?: boolean                // 是否可选择
  checkable?: boolean                 // 是否可勾选
  checkStrictly?: boolean             // 是否严格模式
}
```

### 树配置 (TreeConfig)

```typescript
interface TreeConfig {
  // 数据适配器
  dataAdapter: {
    idField: string                   // ID字段名
    nameField: string                 // 名称字段名
    childrenField: string             // 子节点字段名
    parentIdField: string             // 父节点ID字段名
  }
  
  // 节点配置
  nodeConfig: {
    showIcon: boolean                 // 是否显示图标
    showCount: boolean                // 是否显示数量
    showActions: boolean              // 是否显示操作按钮
    actions: string[]                 // 操作按钮列表
    nameField: string                 // 名称字段
    iconField: string                 // 图标字段
    defaultIcon: string               // 默认图标
    iconMapping: Record<string, string> // 图标映射
  }
  
  // 交互配置
  interaction: {
    draggable: boolean                // 是否可拖拽
    selectable: boolean               // 是否可选择
    checkable: boolean                // 是否可勾选
    checkStrictly: boolean            // 是否严格模式
  }
}
```

### 操作配置 (TreeOperations)

```typescript
interface TreeOperations {
  createApi?: (data: any) => Promise<any>    // 创建API
  updateApi?: (data: any) => Promise<any>    // 更新API
  deleteApi?: (data: any) => Promise<any>    // 删除API
}
```

### 拖拽业务配置 (DragBusinessConfig)

```typescript
interface DragBusinessConfig {
  updateApi: (data: any) => Promise<any>     // 拖拽更新API
  rules?: {                                   // 拖拽规则
    maxDepth: number                          // 最大层级
    allowCycle: boolean                       // 是否允许循环
  }
  messages?: {                                // 消息配置
    dragSuccess: string                       // 拖拽成功消息
    dragError: string                         // 拖拽失败消息
    maxDepthExceeded: string                  // 超出最大层级消息
    cycleNotAllowed: string                   // 不允许循环消息
  }
}
```

## 事件说明

### 基础事件

- `@select`: 节点选择时触发
- `@node-click`: 节点点击时触发
- `@node-expand`: 节点展开时触发
- `@node-collapse`: 节点收起时触发
- `@check`: 节点勾选时触发

### 操作事件

- `@action`: 操作按钮点击时触发
- `@add`: 添加节点时触发
- `@update`: 更新节点时触发
- `@delete`: 删除节点时触发

### 拖拽事件

- `@drag-start`: 拖拽开始时触发
- `@drag-end`: 拖拽结束时触发
- `@drag-success`: 拖拽成功时触发
- `@drag-error`: 拖拽失败时触发

## 使用示例

### 1. 基础用法

```vue
<template>
  <ExtendTree
    :data="treeData"
    :config="treeConfig"
    :show-search="true"
    :show-actions="true"
    search-placeholder="搜索节点..."
    @select="handleNodeSelect"
    @action="handleNodeAction"
  />
</template>

<script setup lang="ts">
import ExtendTree from '@/components/extendTree/index.vue'

const treeData = ref([
  {
    id: 1,
    name: '根节点',
    children: [
      {
        id: 2,
        name: '子节点1',
        children: []
      },
      {
        id: 3,
        name: '子节点2',
        children: []
      }
    ]
  }
])

const treeConfig = {
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
      folder: 'Folder',
      file: 'Document'
    }
  },
  interaction: {
    draggable: false,
    selectable: true,
    checkable: false,
    checkStrictly: false
  }
}

const handleNodeSelect = (data: any) => {
  console.log('选中节点:', data)
}

const handleNodeAction = (action: string, data: any) => {
  console.log('操作:', action, data)
}
</script>
```

### 2. 带拖拽功能

```vue
<template>
  <ExtendTree
    :data="treeData"
    :config="treeConfig"
    :drag-business="dragBusinessConfig"
    :show-search="true"
    :show-actions="true"
    @drag-success="handleDragSuccess"
    @drag-error="handleDragError"
  />
</template>

<script setup lang="ts">
const dragBusinessConfig = {
  updateApi: async (data: any) => {
    const { draggingNode, dropNode, dropType } = data
    
    let targetParentId = 0
    let position = 0
    
    if (dropType === 'inner') {
      targetParentId = dropNode.id
    } else if (dropType === 'before') {
      targetParentId = dropNode.parentId || 0
      position = dropNode.sort || 0
    } else if (dropType === 'after') {
      targetParentId = dropNode.parentId || 0
      position = (dropNode.sort || 0) + 1
    }
    
    // 调用后端API
    return await api.moveNode(draggingNode.id, targetParentId, position)
  },
  rules: {
    maxDepth: 3,
    allowCycle: false
  },
  messages: {
    dragSuccess: '拖拽成功',
    dragError: '拖拽失败',
    maxDepthExceeded: '超出最大层级限制',
    cycleNotAllowed: '不允许循环引用'
  }
}

const handleDragSuccess = (draggedData: any, dropData: any, dropType: string) => {
  console.log('拖拽成功:', { draggedData, dropData, dropType })
  ElMessage.success('拖拽成功')
}

const handleDragError = (draggedData: any, dropData: any, dropType: string) => {
  console.log('拖拽失败:', { draggedData, dropData, dropType })
  ElMessage.error('拖拽失败')
}
</script>
```

### 3. 带操作功能

```vue
<template>
  <ExtendTree
    :data="treeData"
    :config="treeConfig"
    :operations="treeOperations"
    :show-search="true"
    :show-actions="true"
    @action="handleNodeAction"
    @add="handleAddNode"
    @update="handleUpdateNode"
    @delete="handleDeleteNode"
  />
</template>

<script setup lang="ts">
const treeOperations = {
  createApi: async (data: any) => {
    return await api.createNode(data)
  },
  updateApi: async (data: any) => {
    return await api.updateNode(data.id, data)
  },
  deleteApi: async (data: any) => {
    return await api.deleteNode(data.id)
  }
}

const handleNodeAction = (action: string, data: any) => {
  switch (action) {
    case 'add':
      handleAddNode(data.id)
      break
    case 'edit':
      handleUpdateNode(data)
      break
    case 'delete':
      handleDeleteNode(data)
      break
  }
}

const handleAddNode = (parentId?: number) => {
  // 打开添加对话框
  console.log('添加节点:', parentId)
}

const handleUpdateNode = (data: any) => {
  // 打开编辑对话框
  console.log('更新节点:', data)
}

const handleDeleteNode = async (data: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除 "${data.name}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await treeOperations.deleteApi(data)
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}
</script>
```

## 自定义节点内容

### 1. 使用插槽

```vue
<template>
  <ExtendTree :data="treeData" :config="treeConfig">
    <template #default="{ node, data }">
      <div class="custom-node">
        <el-icon v-if="data.icon">
          <component :is="data.icon" />
        </el-icon>
        <span class="node-label">{{ data.name }}</span>
        <span v-if="data.count" class="node-count">({{ data.count }})</span>
      </div>
    </template>
  </ExtendTree>
</template>

<style scoped>
.custom-node {
  display: flex;
  align-items: center;
  gap: 8px;
}

.node-label {
  font-weight: 500;
}

.node-count {
  color: #909399;
  font-size: 12px;
}
</style>
```

### 2. 使用配置

```typescript
const treeConfig = {
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
      folder: 'Folder',
      file: 'Document',
      category: 'Collection',
      field: 'Edit'
    }
  },
  interaction: {
    draggable: true,
    selectable: true,
    checkable: false,
    checkStrictly: false
  }
}
```

## 最佳实践

### 1. 数据格式化

```typescript
// 数据格式化函数
const formatTreeData = (data: any[]) => {
  return data.map(item => ({
    id: item.id,
    name: item.name,
    icon: item.type === 'folder' ? 'Folder' : 'Document',
    count: item.children?.length || 0,
    children: item.children ? formatTreeData(item.children) : []
  }))
}

// 使用格式化后的数据
const treeData = computed(() => formatTreeData(rawData.value))
```

### 2. 操作处理

```typescript
// 统一的操作处理
const createOperationHandler = (type: string) => {
  return async (data: any) => {
    try {
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
  create: api.createNode,
  update: api.updateNode,
  delete: api.deleteNode
}
```

### 3. 拖拽验证

```typescript
// 拖拽验证函数
const validateDrag = (draggedData: any, dropData: any, dropType: string) => {
  // 检查是否拖拽到自己
  if (draggedData.id === dropData.id) {
    return false
  }
  
  // 检查是否拖拽到自己的子节点
  const isChild = (parent: any, child: any): boolean => {
    if (!parent.children) return false
    return parent.children.some((item: any) => 
      item.id === child.id || isChild(item, child)
    )
  }
  
  if (isChild(draggedData, dropData)) {
    return false
  }
  
  return true
}
```

## 注意事项

1. **数据格式**: 确保数据格式符合组件期望的结构
2. **图标映射**: 确保图标映射中的图标组件已正确导入
3. **权限控制**: 根据用户权限动态调整可用的操作
4. **性能优化**: 对于大量数据，考虑使用虚拟滚动
5. **错误处理**: 妥善处理API调用失败的情况

## 扩展功能

### 1. 虚拟滚动

对于大量数据的树，可以考虑使用虚拟滚动：

```typescript
// 虚拟滚动配置
const virtualScrollConfig = {
  itemSize: 32,           // 每项高度
  bufferSize: 10,         // 缓冲区大小
  maxHeight: 400          // 最大高度
}
```

### 2. 懒加载

对于动态加载的树，可以实现懒加载：

```typescript
// 懒加载配置
const lazyLoadConfig = {
  loadData: async (node: any) => {
    const children = await api.getChildren(node.id)
    return children
  }
}
```

### 3. 多选功能

启用多选功能：

```typescript
const treeConfig = {
  // ... 其他配置
  interaction: {
    draggable: true,
    selectable: true,
    checkable: true,        // 启用多选
    checkStrictly: false    // 父子节点关联
  }
}
```

通过以上配置和使用方式，`ExtendTree` 组件可以满足各种复杂的树形结构需求，提供丰富的交互功能。 