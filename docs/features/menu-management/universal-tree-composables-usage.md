# 通用树形组合式函数使用指南

## 概述

我们创建了三个通用的组合式函数来处理树形组件的各种功能：

1. **useTreeDrag** - 拖拽业务逻辑
2. **useTreeState** - 树形状态管理
3. **useTreeOperations** - 树形操作（增删改）

## 使用示例

### 1. 在 FieldManagement.vue 中使用

```typescript
// 导入通用组合式函数
import { useTreeDrag } from '@/composables/useTreeDrag'
import { useTreeState } from '@/composables/useTreeState'
import { useTreeOperations } from '@/composables/useTreeOperations'

// 在组件中使用
export default {
  setup() {
    // 树形状态管理
    const treeState = useTreeState({
      expandConfig: {
        defaultExpandAll: true,
        persistExpandState: true,
        expandMemoryKey: 'hierarchy-group-expanded'
      }
    })

    // 拖拽业务逻辑
    const treeDrag = useTreeDrag({
      updateApi: async (data) => {
        const { draggingNode, dropNode, dropType } = data
        return await hierarchyApi.dragUpdateHierarchyGroup(
          draggingNode, 
          dropNode, 
          dropType
        )
      },
      rules: {
        forbiddenDragIds: [0], // 禁止拖拽虚拟根节点
        maxDepth: 5,
        allowCycle: false
      },
      messages: {
        dragForbidden: '不能拖拽虚拟根节点',
        dragSuccess: '拖拽移动成功',
        dragError: '拖拽移动失败'
      },
      onAfterDrag: () => {
        // 拖拽成功后不立即刷新，保持前端状态
        console.log('拖拽成功，保持前端状态')
      }
    })

    // 树形操作
    const treeOperations = useTreeOperations({
      createApi: hierarchyApi.createHierarchyGroup,
      updateApi: hierarchyApi.updateHierarchyGroup,
      deleteApi: hierarchyApi.deleteHierarchyGroup,
      messages: {
        createSuccess: '创建分组成功',
        updateSuccess: '更新分组成功',
        deleteSuccess: '删除分组成功'
      },
      onAfterCreate: () => {
        // 创建成功后刷新数据
        fetchHierarchyGroups()
      },
      onAfterUpdate: () => {
        // 更新成功后刷新数据
        fetchHierarchyGroups()
      },
      onAfterDelete: () => {
        // 删除成功后刷新数据
        fetchHierarchyGroups()
      }
    })

    // 拖拽事件处理
    const handleNodeDrag = async (draggingNode: any, dropNode: any, dropType: string) => {
      const success = await treeDrag.handleElementPlusDrag({
        draggingNode,
        dropNode,
        dropType
      })
      
      if (success) {
        // 拖拽成功后的处理
        console.log('拖拽成功')
      }
    }

    // 节点操作处理
    const handleNodeAdd = async (formData: any) => {
      await treeOperations.handleCreate(formData, extendTreeRef, () => {
        treeState.saveExpandedState(extendTreeRef)
      })
    }

    const handleNodeUpdate = async (formData: any) => {
      await treeOperations.handleUpdate(formData, extendTreeRef, () => {
        treeState.saveExpandedState(extendTreeRef)
      })
    }

    const handleNodeDelete = async (data: any) => {
      await treeOperations.handleDelete(data, extendTreeRef, () => {
        treeState.saveExpandedState(extendTreeRef)
      })
    }

    return {
      // 状态
      ...treeState,
      
      // 拖拽
      handleNodeDrag,
      
      // 操作
      handleNodeAdd,
      handleNodeUpdate,
      handleNodeDelete
    }
  }
}
```

### 2. 在 ExtendTree 组件中使用

```typescript
// ExtendTree/index.vue
import { useTreeDrag } from '@/composables/useTreeDrag'
import { useTreeState } from '@/composables/useTreeState'
import { useTreeOperations } from '@/composables/useTreeOperations'

export default {
  props: {
    dragBusiness: Object,
    operations: Object
  },
  
  setup(props, { emit }) {
    // 树形状态管理
    const treeState = useTreeState({
      expandConfig: {
        defaultExpandAll: props.defaultExpandAll,
        persistExpandState: props.autoSaveState,
        expandMemoryKey: 'extend-tree-expanded'
      }
    })

    // 拖拽业务逻辑
    const treeDrag = useTreeDrag(props.dragBusiness || {})

    // 树形操作
    const treeOperations = useTreeOperations(props.operations || {})

    // 拖拽事件处理
    const handleDrag = async (draggingData: any, dropData: any, dropType: string) => {
      const success = await treeDrag.handleDrag(
        draggingData, 
        dropData, 
        dropType,
        treeContentRef,
        () => treeState.saveExpandedState(treeContentRef)
      )
      
      if (success) {
        emit('drag-success', draggingData, dropData, dropType)
      } else {
        emit('drag-error', draggingData, dropData, dropType)
      }
    }

    // 节点操作处理
    const handleAdd = () => {
      // 打开新增对话框
      dialogVisible.value = true
    }

    const handleEdit = (data: any) => {
      // 打开编辑对话框
      formData.value = { ...data }
      dialogVisible.value = true
    }

    const handleDelete = async (data: any) => {
      await treeOperations.handleDelete(data, treeContentRef, () => {
        treeState.saveExpandedState(treeContentRef)
      })
    }

    const handleSubmit = async (formData: any) => {
      if (formData.id) {
        await treeOperations.handleUpdate(formData, treeContentRef, () => {
          treeState.saveExpandedState(treeContentRef)
        })
      } else {
        await treeOperations.handleCreate(formData, treeContentRef, () => {
          treeState.saveExpandedState(treeContentRef)
        })
      }
      
      dialogVisible.value = false
    }

    return {
      // 状态
      ...treeState,
      
      // 拖拽
      handleDrag,
      
      // 操作
      handleAdd,
      handleEdit,
      handleDelete,
      handleSubmit
    }
  }
}
```

### 3. 在其他树形组件中使用

```typescript
// 区域管理页面
export default {
  setup() {
    const treeState = useTreeState({
      expandConfig: {
        defaultExpandAll: true,
        persistExpandState: true,
        expandMemoryKey: 'region-tree-expanded'
      }
    })

    const treeDrag = useTreeDrag({
      updateApi: async (data) => {
        return await regionApi.updateRegion(data)
      },
      rules: {
        maxDepth: 3 // 区域最多3级
      }
    })

    const treeOperations = useTreeOperations({
      createApi: regionApi.createRegion,
      updateApi: regionApi.updateRegion,
      deleteApi: regionApi.deleteRegion
    })

    return {
      ...treeState,
      ...treeDrag,
      ...treeOperations
    }
  }
}
```

## 优势

### 1. **代码复用**
- 拖拽逻辑可以在多个树形组件中复用
- 状态管理逻辑统一
- 操作逻辑标准化

### 2. **配置化**
- 通过配置来控制行为
- 支持自定义验证规则
- 支持自定义消息

### 3. **可扩展**
- 易于添加新的功能
- 支持回调函数
- 支持错误处理

### 4. **类型安全**
- 完整的 TypeScript 类型定义
- 编译时错误检查
- 更好的 IDE 支持

## 迁移指南

### 从现有代码迁移

1. **替换拖拽逻辑**
```typescript
// 之前
const handleNodeDrag = async (draggingNode, dropNode, dropType) => {
  // 复杂的拖拽逻辑
}

// 之后
const treeDrag = useTreeDrag({
  updateApi: yourUpdateApi,
  rules: yourRules,
  messages: yourMessages
})

const handleNodeDrag = treeDrag.handleElementPlusDrag
```

2. **替换状态管理**
```typescript
// 之前
const expandedKeys = ref([])
const saveExpandedState = () => { /* ... */ }

// 之后
const treeState = useTreeState({
  expandConfig: { defaultExpandAll: true }
})
const { expandedKeys, saveExpandedState } = treeState
```

3. **替换操作逻辑**
```typescript
// 之前
const handleDelete = async (data) => {
  try {
    await deleteApi(data.id)
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 之后
const treeOperations = useTreeOperations({
  deleteApi: deleteApi
})
const { handleDelete } = treeOperations
```

## 总结

通过使用这些通用组合式函数，我们可以：

1. **减少重复代码**：拖拽、状态管理、操作逻辑都可以复用
2. **提高开发效率**：配置化开发，减少样板代码
3. **提高代码质量**：统一的错误处理和消息提示
4. **提高可维护性**：逻辑集中，易于修改和扩展

这样就能实现真正的通用化，让树形组件的开发变得更加简单和高效。 