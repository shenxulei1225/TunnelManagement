# 字段显示逻辑重新设计

## 需求分析

### 左侧树形结构
1. **分组点击行为**：点击分组后，右侧字段列表只显示该分组关联的字段
2. **递归显示**：显示的字段包含该分组及其所有子分组的关联字段
3. **虚拟根节点**：点击"全部字段"时，显示所有已关联的字段

### 右侧字段列表
1. **两种显示状态**：
   - 显示全部字段：显示所有字段（已关联 + 未关联）
   - 显示已关联字段：只显示已关联到分组的字段
2. **状态切换**：
   - 点击分组时，自动切换到"显示已关联字段"状态
   - 点击"显示全部"按钮时，切换到"显示全部字段"状态
3. **状态保持**：用户手动切换状态后，该状态会保持，直到用户再次切换

## 实现方案

### 1. 状态管理
```typescript
// 显示模式枚举
enum DisplayMode {
  ALL_FIELDS = 'all',           // 显示全部字段
  ASSIGNED_FIELDS = 'assigned'  // 显示已关联字段
}

// 状态变量
const displayMode = ref<DisplayMode>(DisplayMode.ALL_FIELDS)
const selectedHierarchyId = ref<number | null>(null)
```

### 2. 字段过滤逻辑
```typescript
const displayFields = computed(() => {
  let fields = fieldList.value
  
  // 根据显示模式过滤
  if (displayMode.value === DisplayMode.ALL_FIELDS) {
    // 显示全部字段，不做过滤
    fields = fieldList.value
  } else {
    // 显示已关联字段
    if (selectedHierarchyId.value !== null) {
      if (selectedHierarchyId.value === 0) {
        // 选中"全部字段"时，显示所有已关联的字段
        fields = fieldList.value.filter(field => field.hierarchyGroupId)
      } else {
        // 选中特定分组时，显示该分组及其子分组的字段
        fields = getFieldsForHierarchyGroup(selectedHierarchyId.value)
      }
    } else {
      // 未选中任何分组时，显示所有已关联的字段
      fields = fieldList.value.filter(field => field.hierarchyGroupId)
    }
  }
  
  // 根据搜索文本过滤
  if (fieldSearchText.value) {
    const searchLower = fieldSearchText.value.toLowerCase()
    fields = fields.filter(field =>
      (field.fieldLabel || '').toLowerCase().includes(searchLower) ||
      (field.fieldKey || '').toLowerCase().includes(searchLower)
    )
  }
  
  return fields
})
```

### 3. 分组选择处理
```typescript
const handleNodeSelect = (data: HierarchyGroupTreeVO) => {
  console.log('分组选择:', data)
  
  // 更新选中的分组ID
  if (data?.id === 0) {
    selectedHierarchyId.value = null
  } else {
    selectedHierarchyId.value = data?.id || null
  }
  
  // 点击分组时，自动切换到"显示已关联字段"模式
  displayMode.value = DisplayMode.ASSIGNED_FIELDS
  
  console.log('状态更新:', {
    selectedHierarchyId: selectedHierarchyId.value,
    displayMode: displayMode.value
  })
}
```

### 4. 显示模式切换
```typescript
const toggleDisplayMode = () => {
  if (displayMode.value === DisplayMode.ALL_FIELDS) {
    displayMode.value = DisplayMode.ASSIGNED_FIELDS
  } else {
    displayMode.value = DisplayMode.ALL_FIELDS
  }
  
  console.log('显示模式切换为:', displayMode.value)
}
```

### 5. 递归获取分组字段
```typescript
const getFieldsForHierarchyGroup = (hierarchyId: number): FieldVO[] => {
  // 递归获取指定分组及其所有子分组的ID
  const getChildGroupIds = (nodeId: number, treeData: any[]): number[] => {
    const ids = [nodeId]
    
    const findNode = (nodes: any[], targetId: number): any => {
      for (const node of nodes) {
        if (node.id === targetId) {
          return node
        }
        if (node.children && node.children.length > 0) {
          const found = findNode(node.children, targetId)
          if (found) return found
        }
      }
      return null
    }
    
    const targetNode = findNode(treeData, nodeId)
    if (targetNode && targetNode.children && targetNode.children.length > 0) {
      for (const child of targetNode.children) {
        ids.push(...getChildGroupIds(child.id, treeData))
      }
    }
    
    return ids
  }
  
  // 获取所有相关分组的ID
  const groupIds = getChildGroupIds(hierarchyId, hierarchyTreeData.value)
  
  // 返回这些分组下的所有字段
  return fieldList.value.filter(field => 
    groupIds.includes(field.hierarchyGroupId || 0)
  )
}
```

## UI 组件更新

### 1. 头部显示
```vue
<template #header>
  <div class="flex items-center justify-between">
    <div class="header-info">
      <span class="font-semibold" style="white-space: nowrap;">
        {{ getHeaderTitle() }}
      </span>
      <el-tag v-if="displayFields.length > 0" size="small" class="ml-2">
        {{ displayFields.length }} 个字段
      </el-tag>
    </div>
    
    <div class="header-actions">
      <el-button 
        size="small" 
        :type="displayMode === DisplayMode.ALL_FIELDS ? 'primary' : 'default'"
        @click="toggleDisplayMode"
        title="切换显示模式"
      >
        {{ displayMode === DisplayMode.ALL_FIELDS ? '仅显示已关联' : '显示全部字段' }}
      </el-button>
    </div>
  </div>
</template>
```

### 2. 头部标题计算
```typescript
const getHeaderTitle = () => {
  if (displayMode.value === DisplayMode.ALL_FIELDS) {
    return '所有字段'
  } else {
    if (selectedHierarchyId.value === null) {
      return '已关联字段'
    } else if (selectedHierarchyId.value === 0) {
      return '已关联字段'
    } else {
      return currentHierarchyName.value || '已关联字段'
    }
  }
}
```

## 交互流程

### 1. 初始状态
- 显示模式：显示全部字段
- 选中分组：无
- 字段列表：显示所有字段

### 2. 点击分组
- 更新选中分组ID
- 自动切换到"显示已关联字段"模式
- 字段列表：显示该分组及其子分组的关联字段

### 3. 点击"显示全部字段"按钮
- 切换到"显示全部字段"模式
- 字段列表：显示所有字段（已关联 + 未关联）
- 保持当前选中的分组状态

### 4. 点击"仅显示已关联"按钮
- 切换到"显示已关联字段"模式
- 字段列表：根据当前选中的分组显示关联字段

## 优势

1. **逻辑清晰**：两种显示模式，行为明确
2. **状态保持**：用户手动切换的状态会被保持
3. **自动切换**：点击分组时自动切换到合适的显示模式
4. **递归显示**：正确显示分组及其子分组的字段
5. **易于理解**：用户界面和交互逻辑简单直观 