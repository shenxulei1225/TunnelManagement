# 业务语义化通用框架使用指南

## 概述

为了解决 `hierarchy_group`（多级组）和 `category`（分类）等不同业务概念的区别，我们创建了业务语义化的通用框架 `useTreeBusiness`。

## 核心设计理念

### 1. **业务语义分离**
- 相同的技术实现
- 不同的业务语义
- 不同的用户界面
- 不同的操作逻辑

### 2. **配置化业务规则**
- 预定义业务语义配置
- 可覆盖的默认行为
- 灵活的业务规则配置

## 使用示例

### 1. **多级组（Hierarchy Group）使用**

```typescript
// 使用业务语义化框架
import { useTreeBusiness } from '@/composables/useTreeBusiness'
import { 
  getHierarchyGroupTreeApi,
  createHierarchyGroupApi,
  updateHierarchyGroupApi,
  deleteHierarchyGroupApi
} from '@/api/system/hierarchy-group'

export function useHierarchyBusiness() {
  const treeBusiness = useTreeBusiness({
    // 业务类型
    businessType: 'hierarchy',
    
    // API 配置
    api: {
      fetchApi: getHierarchyGroupTreeApi,
      createApi: createHierarchyGroupApi,
      updateApi: updateHierarchyGroupApi,
      deleteApi: deleteHierarchyGroupApi
    },
    
    // 数据转换配置
    transformers: {
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
      })
    },
    
    // 业务规则配置
    businessRules: {
      allowSelectVirtualRoot: false,
      enableDrag: true,
      enableSearch: true,
      enableExpand: true,
      persistState: true
    },
    
    // 回调函数
    callbacks: {
      onDataUpdate: (data: any[]) => {
        console.log('多级组数据更新:', data)
      },
      
      onSelectionChange: (selectedId: number | null, selectedData: any) => {
        console.log('多级组选择变化:', selectedId, selectedData)
      }
    }
  })

  return {
    // 业务信息
    businessInfo: treeBusiness.businessInfo,
    businessRules: treeBusiness.businessRules,
    
    // API 方法
    fetchHierarchyGroups: treeBusiness.fetchTreeData,
    createHierarchyGroup: treeBusiness.createNode,
    updateHierarchyGroup: treeBusiness.updateNode,
    deleteHierarchyGroup: treeBusiness.deleteNode,
    dragUpdateHierarchyGroup: treeBusiness.dragUpdateNode,
    
    // 数据状态
    selectedHierarchyId: treeBusiness.selectedId,
    hierarchyGroups: treeBusiness.treeData,
    expandedKeys: treeBusiness.expandedKeys,
    currentHierarchyName: treeBusiness.currentSelectedName,
    
    // 状态管理
    saveExpandedState: treeBusiness.saveExpandedState,
    restoreExpandedState: treeBusiness.restoreExpandedState,
    selectNode: treeBusiness.selectNode,
    
    // 工具方法
    findHierarchyById: treeBusiness.findById,
    updateHierarchyGroups: treeBusiness.updateTreeData
  }
}
```

### 2. **分类（Category）使用**

```typescript
// 分类管理使用业务语义化框架
import { useTreeBusiness } from '@/composables/useTreeBusiness'
import { 
  getCategoryTreeApi,
  createCategoryApi,
  updateCategoryApi,
  deleteCategoryApi
} from '@/api/system/category'

export function useCategoryBusiness() {
  const treeBusiness = useTreeBusiness({
    // 业务类型
    businessType: 'category',
    
    // API 配置
    api: {
      fetchApi: getCategoryTreeApi,
      createApi: createCategoryApi,
      updateApi: updateCategoryApi,
      deleteApi: deleteCategoryApi
    },
    
    // 自定义语义配置（覆盖默认配置）
    semantics: {
      displayName: '内容分类',
      virtualRootName: '全部分类',
      defaultSelectedName: '所有分类',
      icon: 'Collection',
      color: '#67C23A'
    },
    
    // 数据转换配置
    transformers: {
      createTransformer: (formData: any) => ({
        id: 0,
        name: formData.name,
        code: formData.code,
        parentId: formData.parentId || 0,
        level: 0,
        path: '',
        sort: formData.sort || 0,
        color: '#67C23A',
        icon: formData.icon || 'Collection',
        description: formData.description || '',
        status: 0,
        createTime: new Date()
      })
    },
    
    // 业务规则配置
    businessRules: {
      allowSelectVirtualRoot: true, // 分类允许选择虚拟根节点
      enableDrag: true,
      enableSearch: true,
      enableExpand: true,
      persistState: true
    }
  })

  return {
    // 业务信息
    businessInfo: treeBusiness.businessInfo,
    businessRules: treeBusiness.businessRules,
    
    // API 方法
    fetchCategories: treeBusiness.fetchTreeData,
    createCategory: treeBusiness.createNode,
    updateCategory: treeBusiness.updateNode,
    deleteCategory: treeBusiness.deleteNode,
    dragUpdateCategory: treeBusiness.dragUpdateNode,
    
    // 数据状态
    selectedCategoryId: treeBusiness.selectedId,
    categories: treeBusiness.treeData,
    expandedKeys: treeBusiness.expandedKeys,
    currentCategoryName: treeBusiness.currentSelectedName,
    
    // 状态管理
    saveExpandedState: treeBusiness.saveExpandedState,
    restoreExpandedState: treeBusiness.restoreExpandedState,
    selectNode: treeBusiness.selectNode,
    
    // 工具方法
    findCategoryById: treeBusiness.findById,
    updateCategories: treeBusiness.updateTreeData
  }
}
```

### 3. **部门（Department）使用**

```typescript
// 部门管理使用业务语义化框架
import { useTreeBusiness } from '@/composables/useTreeBusiness'
import { 
  getDeptTreeApi,
  createDeptApi,
  updateDeptApi,
  deleteDeptApi
} from '@/api/system/dept'

export function useDeptBusiness() {
  const treeBusiness = useTreeBusiness({
    // 业务类型
    businessType: 'department',
    
    // API 配置
    api: {
      fetchApi: getDeptTreeApi,
      createApi: createDeptApi,
      updateApi: updateDeptApi,
      deleteApi: deleteDeptApi
    },
    
    // 自定义语义配置
    semantics: {
      displayName: '组织架构',
      virtualRootName: '全部部门',
      defaultSelectedName: '所有部门',
      icon: 'OfficeBuilding',
      color: '#E6A23C'
    },
    
    // 业务规则配置
    businessRules: {
      allowSelectVirtualRoot: false,
      enableDrag: true,
      enableSearch: true,
      enableExpand: true,
      persistState: true
    }
  })

  return {
    // 业务信息
    businessInfo: treeBusiness.businessInfo,
    businessRules: treeBusiness.businessRules,
    
    // API 方法
    fetchDepts: treeBusiness.fetchTreeData,
    createDept: treeBusiness.createNode,
    updateDept: treeBusiness.updateNode,
    deleteDept: treeBusiness.deleteNode,
    dragUpdateDept: treeBusiness.dragUpdateNode,
    
    // 数据状态
    selectedDeptId: treeBusiness.selectedId,
    depts: treeBusiness.treeData,
    expandedKeys: treeBusiness.expandedKeys,
    currentDeptName: treeBusiness.currentSelectedName,
    
    // 状态管理
    saveExpandedState: treeBusiness.saveExpandedState,
    restoreExpandedState: treeBusiness.restoreExpandedState,
    selectNode: treeBusiness.selectNode,
    
    // 工具方法
    findDeptById: treeBusiness.findById,
    updateDepts: treeBusiness.updateTreeData
  }
}
```

## 在组件中使用

### 1. **多级组组件**

```vue
<template>
  <div class="hierarchy-management">
    <div class="header">
      <h3>{{ businessInfo.displayName }}管理</h3>
      <el-button 
        type="primary" 
        :icon="businessInfo.icon"
        @click="handleCreate"
      >
        新建{{ businessInfo.displayName }}
      </el-button>
    </div>
    
    <el-tree
      ref="treeRef"
      :data="hierarchyGroups"
      :props="{ children: 'children', label: 'name' }"
      :expand-on-click-node="false"
      :default-expanded-keys="expandedKeys"
      :highlight-current="true"
      draggable
      @node-click="selectNode"
      @node-drop="handleDrag"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <el-icon :color="businessInfo.color">
            <component :is="data.icon || businessInfo.icon" />
          </el-icon>
          <span>{{ node.label }}</span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup lang="ts">
import { useHierarchyBusiness } from './composables/useHierarchyBusiness'

const {
  businessInfo,
  businessRules,
  fetchHierarchyGroups,
  createHierarchyGroup,
  updateHierarchyGroup,
  deleteHierarchyGroup,
  dragUpdateHierarchyGroup,
  selectedHierarchyId,
  hierarchyGroups,
  expandedKeys,
  currentHierarchyName,
  saveExpandedState,
  restoreExpandedState,
  selectNode
} = useHierarchyBusiness()

// 初始化
onMounted(async () => {
  await fetchHierarchyGroups()
  restoreExpandedState(treeRef)
})

// 保存展开状态
const handleNodeExpand = () => {
  saveExpandedState(treeRef)
}

// 拖拽处理
const handleDrag = async (draggingNode: any, dropNode: any, dropType: string) => {
  if (businessRules.enableDrag) {
    await dragUpdateHierarchyGroup(draggingNode.data, dropNode.data, dropType)
  }
}
</script>
```

### 2. **分类组件**

```vue
<template>
  <div class="category-management">
    <div class="header">
      <h3>{{ businessInfo.displayName }}管理</h3>
      <el-button 
        type="success" 
        :icon="businessInfo.icon"
        @click="handleCreate"
      >
        新建{{ businessInfo.displayName }}
      </el-button>
    </div>
    
    <el-tree
      ref="treeRef"
      :data="categories"
      :props="{ children: 'children', label: 'name' }"
      :expand-on-click-node="false"
      :default-expanded-keys="expandedKeys"
      :highlight-current="true"
      draggable
      @node-click="selectNode"
      @node-drop="handleDrag"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <el-icon :color="businessInfo.color">
            <component :is="data.icon || businessInfo.icon" />
          </el-icon>
          <span>{{ node.label }}</span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup lang="ts">
import { useCategoryBusiness } from './composables/useCategoryBusiness'

const {
  businessInfo,
  businessRules,
  fetchCategories,
  createCategory,
  updateCategory,
  deleteCategory,
  dragUpdateCategory,
  selectedCategoryId,
  categories,
  expandedKeys,
  currentCategoryName,
  saveExpandedState,
  restoreExpandedState,
  selectNode
} = useCategoryBusiness()

// 初始化
onMounted(async () => {
  await fetchCategories()
  restoreExpandedState(treeRef)
})

// 保存展开状态
const handleNodeExpand = () => {
  saveExpandedState(treeRef)
}

// 拖拽处理
const handleDrag = async (draggingNode: any, dropNode: any, dropType: string) => {
  if (businessRules.enableDrag) {
    await dragUpdateCategory(draggingNode.data, dropNode.data, dropType)
  }
}
</script>
```

## 业务语义配置详解

### 1. **预定义业务语义**

```typescript
export const BUSINESS_SEMANTICS = {
  hierarchy: {
    displayName: '多级组',
    virtualRootName: '全部字段',
    defaultSelectedName: '所有字段',
    icon: 'Folder',
    color: '#409EFF',
    messages: {
      fetchError: '获取多级组失败',
      createSuccess: '创建多级组成功',
      // ... 其他消息
    }
  },
  category: {
    displayName: '分类',
    virtualRootName: '全部分类',
    defaultSelectedName: '所有分类',
    icon: 'Collection',
    color: '#67C23A',
    messages: {
      fetchError: '获取分类失败',
      createSuccess: '创建分类成功',
      // ... 其他消息
    }
  }
  // ... 其他业务类型
}
```

### 2. **业务规则配置**

```typescript
interface BusinessRules {
  // 是否允许选择虚拟根节点
  allowSelectVirtualRoot?: boolean
  // 是否启用拖拽
  enableDrag?: boolean
  // 是否启用搜索
  enableSearch?: boolean
  // 是否启用展开/折叠
  enableExpand?: boolean
  // 是否持久化状态
  persistState?: boolean
}
```

## 优势总结

### 1. **业务语义清晰**
- 多级组：强调层级结构，用于字段管理
- 分类：强调分类属性，用于内容管理
- 部门：强调组织架构，用于人员管理

### 2. **技术实现统一**
- 相同的树形结构
- 相同的拖拽逻辑
- 相同的状态管理
- 相同的API调用

### 3. **用户体验一致**
- 统一的交互模式
- 统一的视觉风格
- 统一的操作反馈

### 4. **开发效率提升**
- 90% 的代码复用
- 配置化开发
- 快速集成新业务

### 5. **维护成本降低**
- 统一的代码规范
- 统一的测试策略
- 统一的文档标准

这样的设计让不同业务概念在技术实现上保持统一，同时在用户体验上体现各自的业务特色！ 