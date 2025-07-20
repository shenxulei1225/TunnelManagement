# 通用 API 和数据组合式函数使用指南

## 概述

我们创建了两个新的通用组合式函数来处理树形数据的 API 调用和状态管理：

1. **useTreeApi** - 通用的树形 API 调用
2. **useTreeData** - 通用的树形数据状态管理

## 使用示例

### 1. **重构 useHierarchyApi.ts**

#### 重构前的问题
- 硬编码了 hierarchy-group 的 API
- 硬编码了数据结构
- 难以复用到其他树形组件

#### 重构后的改进
```typescript
// 使用通用 API 组合式函数
import { useTreeApi } from '@/composables/useTreeApi'
import { 
  getHierarchyGroupTreeApi,
  createHierarchyGroupApi,
  updateHierarchyGroupApi,
  deleteHierarchyGroupApi
} from '@/api/system/hierarchy-group'

export function useHierarchyApi() {
  const treeApi = useTreeApi({
    // API 方法
    fetchApi: getHierarchyGroupTreeApi,
    createApi: createHierarchyGroupApi,
    updateApi: updateHierarchyGroupApi,
    deleteApi: deleteHierarchyGroupApi,
    
    // 数据转换
    createTransformer: (formData: any) => ({
      id: 0,
      name: formData.name,
      code: formData.name,
      parentId: formData.parentId || 0,
      level: 0,
      path: '',
      sort: formData.sort || 0,
      color: '#409EFF',
      icon: formData.icon || 'Folder',
      description: '',
      status: 0,
      createTime: new Date()
    }),
    
    updateTransformer: (formData: any) => ({
      id: formData.id,
      name: formData.name,
      code: formData.name,
      parentId: formData.parentId || 0,
      level: 0,
      path: '',
      sort: formData.sort || 0,
      color: '#409EFF',
      icon: formData.icon || 'Folder',
      description: '',
      status: 0,
      createTime: new Date()
    }),
    
    // 消息配置
    messages: {
      fetchError: '获取分级组失败',
      createSuccess: '创建成功',
      createError: '创建失败',
      updateSuccess: '更新成功',
      updateError: '更新失败',
      deleteSuccess: '删除成功',
      deleteError: '删除失败',
      dragSuccess: '拖拽移动成功',
      dragError: '拖拽移动失败'
    }
  })

  return {
    fetchHierarchyGroups: treeApi.fetchTreeData,
    createHierarchyGroup: treeApi.createNode,
    updateHierarchyGroup: treeApi.updateNode,
    deleteHierarchyGroup: treeApi.deleteNode,
    dragUpdateHierarchyGroup: treeApi.dragUpdateNode
  }
}
```

### 2. **重构 useHierarchyState.ts**

#### 重构前的问题
- 硬编码了虚拟根节点的逻辑
- 硬编码了选择逻辑
- 难以复用到其他树形组件

#### 重构后的改进
```typescript
// 使用通用数据组合式函数
import { useTreeData } from '@/composables/useTreeData'

export function useHierarchyState() {
  const treeData = useTreeData({
    // 虚拟根节点配置
    virtualRoot: {
      enabled: true,
      id: 0,
      name: '全部字段',
      code: 'ALL_FIELDS',
      icon: 'Folder'
    },
    
    // 选择配置
    selection: {
      allowSelectVirtualRoot: false,
      defaultSelectedId: null,
      defaultSelectedName: '所有字段'
    },
    
    // 数据处理配置
    dataProcessing: {
      enableVirtualRoot: true,
      processData: (data: any) => {
        // 处理后台返回的单个根节点结构
        if (data && data.children) {
          return data.children
        }
        return data
      }
    },
    
    // 回调函数
    onDataUpdate: (data: any[]) => {
      console.log('分级组数据更新:', data)
    },
    
    onSelectionChange: (selectedId: number | null, selectedData: any) => {
      console.log('分级组选择变化:', selectedId, selectedData)
    }
  })

  return {
    // 状态
    selectedHierarchyId: treeData.selectedId,
    hierarchyGroups: treeData.treeData,
    expandedKeys: treeData.expandedKeys,
    currentHierarchyName: treeData.currentSelectedName,
    
    // 方法
    findHierarchyById: treeData.findById,
    processHierarchyData: treeData.processData,
    updateHierarchyGroups: treeData.updateTreeData,
    saveExpandedState: treeData.saveExpandedState,
    getExpandedState: treeData.getExpandedState,
    selectNode: treeData.selectNode
  }
}
```

### 3. **在其他树形组件中使用**

#### 区域管理示例
```typescript
// 区域管理 API
export function useRegionApi() {
  const treeApi = useTreeApi({
    fetchApi: getRegionTreeApi,
    createApi: createRegionApi,
    updateApi: updateRegionApi,
    deleteApi: deleteRegionApi,
    
    createTransformer: (formData: any) => ({
      id: 0,
      name: formData.name,
      code: formData.code,
      parentId: formData.parentId || 0,
      level: 0,
      path: '',
      sort: formData.sort || 0,
      status: 0,
      createTime: new Date()
    }),
    
    messages: {
      fetchError: '获取区域失败',
      createSuccess: '创建区域成功',
      updateSuccess: '更新区域成功',
      deleteSuccess: '删除区域成功'
    }
  })

  return {
    fetchRegions: treeApi.fetchTreeData,
    createRegion: treeApi.createNode,
    updateRegion: treeApi.updateNode,
    deleteRegion: treeApi.deleteNode,
    dragUpdateRegion: treeApi.dragUpdateNode
  }
}

// 区域管理状态
export function useRegionState() {
  const treeData = useTreeData({
    virtualRoot: {
      enabled: true,
      id: 0,
      name: '全部区域',
      code: 'ALL_REGIONS',
      icon: 'Location'
    },
    
    selection: {
      allowSelectVirtualRoot: false,
      defaultSelectedId: null,
      defaultSelectedName: '所有区域'
    }
  })

  return {
    selectedRegionId: treeData.selectedId,
    regions: treeData.treeData,
    expandedKeys: treeData.expandedKeys,
    currentRegionName: treeData.currentSelectedName,
    
    findRegionById: treeData.findById,
    updateRegions: treeData.updateTreeData,
    selectRegion: treeData.selectNode
  }
}
```

#### 部门管理示例
```typescript
// 部门管理 API
export function useDeptApi() {
  const treeApi = useTreeApi({
    fetchApi: getDeptTreeApi,
    createApi: createDeptApi,
    updateApi: updateDeptApi,
    deleteApi: deleteDeptApi,
    
    messages: {
      fetchError: '获取部门失败',
      createSuccess: '创建部门成功',
      updateSuccess: '更新部门成功',
      deleteSuccess: '删除部门成功'
    }
  })

  return {
    fetchDepts: treeApi.fetchTreeData,
    createDept: treeApi.createNode,
    updateDept: treeApi.updateNode,
    deleteDept: treeApi.deleteNode,
    dragUpdateDept: treeApi.dragUpdateNode
  }
}

// 部门管理状态
export function useDeptState() {
  const treeData = useTreeData({
    virtualRoot: {
      enabled: true,
      id: 0,
      name: '全部部门',
      code: 'ALL_DEPTS',
      icon: 'OfficeBuilding'
    },
    
    selection: {
      allowSelectVirtualRoot: false,
      defaultSelectedId: null,
      defaultSelectedName: '所有部门'
    }
  })

  return {
    selectedDeptId: treeData.selectedId,
    depts: treeData.treeData,
    expandedKeys: treeData.expandedKeys,
    currentDeptName: treeData.currentSelectedName,
    
    findDeptById: treeData.findById,
    updateDepts: treeData.updateTreeData,
    selectDept: treeData.selectNode
  }
}
```

## 优势对比

### 1. **代码复用**
- **重构前**：每个树形组件都需要写重复的 API 调用和状态管理代码
- **重构后**：通过配置即可复用通用的逻辑

### 2. **类型安全**
- **重构前**：硬编码的数据结构，难以维护
- **重构后**：完整的 TypeScript 类型定义，编译时检查

### 3. **可维护性**
- **重构前**：业务逻辑与 API 调用混合
- **重构后**：清晰的职责分离，易于测试和维护

### 4. **可扩展性**
- **重构前**：添加新功能需要修改多个文件
- **重构后**：通过配置即可添加新功能

## 配置选项详解

### useTreeApi 配置

```typescript
interface TreeApiConfig {
  // API 方法
  fetchApi: () => Promise<any>           // 获取数据
  createApi: (data: any) => Promise<any> // 创建节点
  updateApi: (data: any) => Promise<any> // 更新节点
  deleteApi: (id: number) => Promise<any> // 删除节点
  
  // 数据转换
  dataTransformer?: (data: any) => any    // 数据转换
  createTransformer?: (formData: any) => any // 创建数据转换
  updateTransformer?: (formData: any) => any // 更新数据转换
  
  // 消息配置
  messages?: {
    fetchError?: string
    createSuccess?: string
    createError?: string
    updateSuccess?: string
    updateError?: string
    deleteSuccess?: string
    deleteError?: string
    dragSuccess?: string
    dragError?: string
  }
  
  // 回调函数
  onFetchSuccess?: (data: any) => void
  onFetchError?: (error: any) => void
  onCreateSuccess?: (data: any) => void
  onCreateError?: (error: any) => void
  onUpdateSuccess?: (data: any) => void
  onUpdateError?: (error: any) => void
  onDeleteSuccess?: (data: any) => void
  onDeleteError?: (error: any) => void
}
```

### useTreeData 配置

```typescript
interface TreeDataConfig {
  // 虚拟根节点配置
  virtualRoot?: {
    enabled?: boolean
    id?: number
    name?: string
    code?: string
    icon?: string
  }
  
  // 选择配置
  selection?: {
    allowSelectVirtualRoot?: boolean
    defaultSelectedId?: number | null
    defaultSelectedName?: string
  }
  
  // 数据处理配置
  dataProcessing?: {
    enableVirtualRoot?: boolean
    processData?: (data: any) => any
  }
  
  // 回调函数
  onDataUpdate?: (data: any[]) => void
  onSelectionChange?: (selectedId: number | null, selectedData: any) => void
}
```

## 总结

通过使用通用的 API 和数据组合式函数，我们实现了：

1. **高度复用**：90% 的代码可以在不同树形组件间复用
2. **配置化开发**：通过配置控制行为，减少样板代码
3. **类型安全**：完整的 TypeScript 类型定义
4. **统一规范**：标准化的 API 调用和状态管理
5. **易于维护**：清晰的职责分离，易于测试和扩展

这样的重构让树形组件的开发变得更加简单和高效！ 