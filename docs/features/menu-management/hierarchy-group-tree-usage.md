# 分级组树形数据与 Element Plus Tree 组件集成

## 概述

`HierarchyGroupRespVO` 已经提供了完整的树形结构数据，可以直接用于 Element Plus Tree 组件，无需额外的数据转换。

## 数据结构

### HierarchyGroupRespVO 字段说明

```java
public class HierarchyGroupRespVO extends HierarchyGroupBaseVO {
    private Long id;                    // 节点唯一标识
    private Integer level;              // 层级信息
    private String path;                // 路径信息
    private LocalDateTime createTime;   // 创建时间
    private List<HierarchyGroupRespVO> children; // 子节点列表
    private Long count;                 // 关联对象数量
    
    // Element Plus Tree 兼容字段
    public String getLabel() { return getName(); }        // 显示文本
    public Boolean getIsLeaf() { return children == null || children.isEmpty(); } // 是否叶子节点
    public Boolean getDisabled() { return getStatus() != null && getStatus() == 0; } // 是否禁用
}
```

## 前端使用示例

### 1. 基本使用

```vue
<template>
  <el-tree
    :data="treeData"
    :props="treeProps"
    node-key="id"
    @node-click="handleNodeClick"
  />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getHierarchyGroupTreeApi } from '@/api/system/hierarchy'

const treeData = ref([])
const treeProps = {
  children: 'children',
  label: 'name'  // 使用 name 字段作为显示文本
}

const handleNodeClick = (data) => {
  console.log('选中节点:', data)
}

onMounted(async () => {
  try {
    const data = await getHierarchyGroupTreeApi()
    treeData.value = data
  } catch (error) {
    console.error('获取分级组树失败:', error)
  }
})
</script>
```

### 2. 带搜索功能

```vue
<template>
  <div>
    <el-input
      v-model="searchKeyword"
      placeholder="搜索分级组..."
      clearable
    />
    <el-tree
      ref="treeRef"
      :data="treeData"
      :props="treeProps"
      :filter-node-method="filterNode"
      node-key="id"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const searchKeyword = ref('')
const treeRef = ref()

const filterNode = (value, data) => {
  if (!value) return true
  return data.name.includes(value)
}

watch(searchKeyword, (val) => {
  treeRef.value?.filter(val)
})
</script>
```

### 3. 带自定义节点内容

```vue
<template>
  <el-tree
    :data="treeData"
    :props="treeProps"
    node-key="id"
  >
    <template #default="{ node, data }">
      <div class="custom-tree-node">
        <span>{{ data.name }}</span>
        <el-tag v-if="data.count > 0" size="small" type="info">
          {{ data.count }} 个对象
        </el-tag>
        <el-tag v-if="data.status === 0" size="small" type="danger">
          已禁用
        </el-tag>
      </div>
    </template>
  </el-tree>
</template>

<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
```

### 4. 带选择功能

```vue
<template>
  <el-tree
    :data="treeData"
    :props="treeProps"
    :show-checkbox="true"
    node-key="id"
    @check="handleCheck"
  />
</template>

<script setup>
const handleCheck = (data, checkedInfo) => {
  console.log('选中节点:', data)
  console.log('选中状态:', checkedInfo)
}
</script>
```

## API 接口

### 获取分级组树形数据

```javascript
// 接口地址: GET /admin-api/system/hierarchy-group/tree
// 返回数据: HierarchyGroupRespVO[]
const getHierarchyGroupTreeApi = () => {
  return request.get('/admin-api/system/hierarchy-group/tree')
}
```

## 数据字段映射

| Element Plus Tree 属性 | HierarchyGroupRespVO 字段 | 说明 |
|----------------------|-------------------------|------|
| `children` | `children` | 子节点列表 |
| `label` | `name` | 节点显示文本 |
| `id` | `id` | 节点唯一标识 |
| `isLeaf` | `getIsLeaf()` | 是否为叶子节点 |
| `disabled` | `getDisabled()` | 是否禁用 |

## 优势

1. **无需数据转换**: 后端直接返回树形结构，前端可直接使用
2. **类型安全**: 使用 TypeScript 接口确保类型安全
3. **功能完整**: 包含层级、路径、关联对象数量等丰富信息
4. **状态管理**: 支持禁用状态，便于权限控制
5. **扩展性好**: 可以轻松添加自定义字段和功能

## 注意事项

1. **性能考虑**: 对于大型树结构，建议使用虚拟滚动
2. **内存管理**: 及时清理不需要的节点引用
3. **状态同步**: 确保选中状态与业务逻辑同步
4. **错误处理**: 添加适当的错误处理和加载状态 