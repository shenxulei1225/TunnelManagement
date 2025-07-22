# 通用树组件使用示例

## 🎯 **基本使用**

### **1. 部门树示例**
```vue
<template>
  <div class="dept-page">
    <div class="left-panel">
      <!-- 部门树 -->
      <UniversalTree
        business-type="dept"
        :plugins="['search', 'crud', 'drag']"
        :config="deptConfig"
        @node-select="handleDeptSelect"
        @node-created="handleDeptCreated"
        @node-updated="handleDeptUpdated"
        @node-deleted="handleDeptDeleted"
      />
    </div>
    
    <div class="right-panel">
      <!-- 部门详情 -->
      <DeptDetail v-if="selectedDept" :dept="selectedDept" />
      
      <!-- 部门员工列表 -->
      <DeptUserList v-if="selectedDept" :dept-id="selectedDept.id" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { UniversalTree } from '@/components/UniversalTree'

const selectedDept = ref(null)

// 部门树配置
const deptConfig = {
  display: {
    showSearch: true,
    showCounter: true,    // 显示员工数量
    showIcons: true
  },
  interaction: {
    draggable: true,      // 支持拖拽排序
    sortable: true
  }
}

// 事件处理
const handleDeptSelect = (dept) => {
  selectedDept.value = dept
  console.log('选中部门:', dept)
  
  // 可以触发其他动作，如加载员工列表
  loadDeptUsers(dept.id)
}

const handleDeptCreated = (dept) => {
  ElMessage.success(`部门 "${dept.name}" 创建成功`)
  // 可以刷新相关数据
}

const handleDeptUpdated = (dept) => {
  ElMessage.success(`部门 "${dept.name}" 更新成功`)
}

const handleDeptDeleted = (deptId) => {
  ElMessage.success('部门删除成功')
  // 清除选中状态
  if (selectedDept.value?.id === deptId) {
    selectedDept.value = null
  }
}

const loadDeptUsers = async (deptId) => {
  // 加载部门员工数据
}
</script>
```

### **2. 设备树示例**
```vue
<template>
  <div class="device-page">
    <div class="left-panel">
      <!-- 设备树 -->
      <UniversalTree
        business-type="device"
        :plugins="['search', 'crud', 'virtual-scroll', 'status-indicator']"
        :config="deviceConfig"
        theme="card"
        @node-select="handleDeviceSelect"
      />
    </div>
    
    <div class="right-panel">
      <!-- 设备详情 -->
      <DeviceDetail v-if="selectedDevice" :device="selectedDevice" />
      
      <!-- 设备监控 -->
      <DeviceMonitor v-if="selectedDevice" :device-id="selectedDevice.id" />
      
      <!-- 设备报警 -->
      <DeviceAlerts v-if="selectedDevice" :device-id="selectedDevice.id" />
    </div>
  </div>
</template>

<script setup lang="ts">
const selectedDevice = ref(null)

// 设备树配置
const deviceConfig = {
  display: {
    showSearch: true,
    showCounter: true,
    showIcons: true,
    virtualScroll: true    // 设备数量多，启用虚拟滚动
  },
  interaction: {
    selectable: true,
    multiSelect: false
  },
  // 自定义字段显示
  customFields: {
    status: {
      type: 'badge',
      mapping: {
        online: { text: '在线', color: 'success' },
        offline: { text: '离线', color: 'danger' },
        maintenance: { text: '维护', color: 'warning' },
        error: { text: '故障', color: 'danger' }
      }
    },
    location: {
      type: 'text',
      prefix: '📍'
    }
  }
}

const handleDeviceSelect = (device) => {
  selectedDevice.value = device
  
  // 触发设备相关数据加载
  loadDeviceRealTimeData(device.id)
  loadDeviceAlerts(device.id)
}

const loadDeviceRealTimeData = async (deviceId) => {
  // 加载设备实时数据
}

const loadDeviceAlerts = async (deviceId) => {
  // 加载设备报警信息
}
</script>
```

### **3. 分类树示例**
```vue
<template>
  <div class="category-page">
    <div class="left-panel">
      <!-- 分类树 -->
      <UniversalTree
        business-type="category"
        :plugins="['search', 'crud', 'sort']"
        :config="categoryConfig"
        @node-select="handleCategorySelect"
        @node-updated="handleSortUpdate"
      />
    </div>
    
    <div class="right-panel">
      <!-- 分类商品列表 -->
      <ProductList 
        v-if="selectedCategory" 
        :category-id="selectedCategory.id" 
      />
    </div>
  </div>
</template>

<script setup lang="ts">
const selectedCategory = ref(null)

// 分类树配置
const categoryConfig = {
  display: {
    showSearch: true,
    showCounter: true,    // 显示商品数量
    showActions: true
  },
  interaction: {
    draggable: true,      // 支持拖拽重新排序
    sortable: true,       // 支持同级排序
    editable: true
  }
}

const handleCategorySelect = (category) => {
  selectedCategory.value = category
  
  // 加载分类下的商品
  loadCategoryProducts(category.id)
}

const handleSortUpdate = (category) => {
  ElMessage.success('分类排序已更新')
}
</script>
```

## 🔧 **高级配置**

### **1. 自定义API端点**
```javascript
const customConfig = {
  api: {
    baseUrl: '/api/custom',
    endpoints: {
      list: '/tree-data',
      create: '/create-node',
      update: '/update-node/:id',
      delete: '/delete-node/:id',
      move: '/move-node',
      search: '/search-nodes'
    }
  }
}
```

### **2. 自定义数据转换器**
```javascript
const customTransformer = {
  transformList(rawData) {
    return rawData.map(item => ({
      id: item.nodeId,
      name: item.nodeName,
      children: item.subNodes || [],
      icon: item.nodeIcon,
      // 其他自定义字段
      customData: item.extraData
    }))
  },
  
  transformNode(rawNode) {
    return {
      id: rawNode.nodeId,
      name: rawNode.nodeName,
      // 转换逻辑
    }
  }
}

const configWithTransformer = {
  api: {
    transformer: customTransformer
  }
}
```

### **3. 权限控制**
```javascript
const permissionConfig = {
  permissions: {
    create: hasPermission('node:create'),
    edit: hasPermission('node:edit'),
    delete: hasPermission('node:delete'),
    move: hasPermission('node:move')
  }
}
```

## 🎨 **主题定制**

### **1. 使用预设主题**
```vue
<template>
  <!-- 默认主题 -->
  <UniversalTree theme="default" />
  
  <!-- 紧凑主题 -->
  <UniversalTree theme="compact" />
  
  <!-- 卡片主题 -->
  <UniversalTree theme="card" />
</template>
```

### **2. 自定义样式**
```css
/* 自定义业务类型样式 */
.business-device .tree-node {
  --tree-node-icon-color: #409eff;
}

.business-dept .tree-node {
  --tree-node-icon-color: #67c23a;
}

.business-category .tree-node {
  --tree-node-icon-color: #e6a23c;
}

/* 自定义主题 */
.theme-dark .universal-tree {
  background: #1e1e1e;
  color: #ffffff;
}
```

## 📱 **响应式布局**

### **移动端适配**
```vue
<template>
  <div class="responsive-layout">
    <!-- 桌面端：左右布局 -->
    <div class="desktop-layout" v-if="!isMobile">
      <div class="left-panel">
        <UniversalTree 
          business-type="dept"
          @node-select="handleSelect"
        />
      </div>
      <div class="right-panel">
        <DetailPanel :selected="selected" />
      </div>
    </div>
    
    <!-- 移动端：上下布局 -->
    <div class="mobile-layout" v-else>
      <div class="tree-panel" :class="{ collapsed: showDetail }">
        <UniversalTree 
          business-type="dept"
          theme="compact"
          @node-select="handleMobileSelect"
        />
      </div>
      <div class="detail-panel" v-if="showDetail">
        <DetailPanel :selected="selected" />
        <el-button @click="showDetail = false">返回</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useBreakpoints } from '@vueuse/core'

const breakpoints = useBreakpoints({
  tablet: 768,
  desktop: 1024
})

const isMobile = computed(() => breakpoints.smaller('tablet'))
const selected = ref(null)
const showDetail = ref(false)

const handleSelect = (node) => {
  selected.value = node
}

const handleMobileSelect = (node) => {
  selected.value = node
  showDetail.value = true
}
</script>
```

## 🔌 **插件扩展**

### **1. 状态指示器插件**
```javascript
// 在组件中使用
const plugins = ['search', 'crud', 'status-indicator']

const config = {
  customFields: {
    status: {
      type: 'badge',
      mapping: {
        active: { text: '活跃', color: 'success' },
        inactive: { text: '非活跃', color: 'info' },
        blocked: { text: '已禁用', color: 'danger' }
      }
    }
  }
}
```

### **2. 统计信息插件**
```javascript
const plugins = ['search', 'crud', 'statistics']

// 显示节点统计信息
const config = {
  display: {
    showCounter: true,
    showStatistics: true
  },
  statistics: {
    fields: ['childCount', 'totalItems', 'lastUpdate']
  }
}
```

## 📊 **性能优化**

### **1. 大数据量处理**
```vue
<template>
  <UniversalTree
    business-type="device"
    :plugins="['search', 'virtual-scroll']"
    :config="bigDataConfig"
  />
</template>

<script setup lang="ts">
const bigDataConfig = {
  display: {
    virtualScroll: true,      // 启用虚拟滚动
    lazyLoad: true           // 启用懒加载
  },
  performance: {
    itemHeight: 32,          // 固定项高度
    bufferSize: 20,          // 缓冲区大小
    threshold: 1000          // 虚拟滚动阈值
  }
}
</script>
```

### **2. 搜索优化**
```javascript
const searchConfig = {
  search: {
    debounceTime: 300,       // 防抖时间
    minLength: 2,            // 最小搜索长度
    fuzzyMatch: true,        // 启用模糊匹配
    highlightResults: true,  // 高亮搜索结果
    maxResults: 100          // 最大结果数量
  }
}
```

## 🎯 **最佳实践**

### **1. 数据管理**
```javascript
// 使用 composable 管理树形数据
const useTreeData = (businessType) => {
  const { treeData, loading, error } = useUniversalTree(businessType)
  
  return {
    treeData,
    loading,
    error,
    refresh: () => fetchTreeData(businessType)
  }
}
```

### **2. 事件处理**
```javascript
// 统一的事件处理模式
const treeEventHandlers = {
  onNodeSelect: (node) => {
    // 记录用户行为
    analytics.track('tree_node_select', { nodeId: node.id, nodeType: node.type })
    
    // 更新选中状态
    updateSelectedNode(node)
    
    // 触发相关数据加载
    loadRelatedData(node)
  },
  
  onNodeCreate: async (nodeData) => {
    try {
      await createNode(nodeData)
      ElMessage.success('创建成功')
    } catch (error) {
      ElMessage.error('创建失败：' + error.message)
    }
  }
}
```

### **3. 错误处理**
```javascript
// 全局错误处理
const treeErrorHandler = {
  onApiError: (error) => {
    console.error('Tree API Error:', error)
    
    if (error.code === 'NETWORK_ERROR') {
      ElMessage.error('网络连接失败，请检查网络设置')
    } else if (error.code === 'PERMISSION_DENIED') {
      ElMessage.error('权限不足，请联系管理员')
    } else {
      ElMessage.error('操作失败：' + error.message)
    }
  }
}
```

这样设计的通用树组件可以满足您提到的所有需求：

✅ **内置搜索功能**：支持实时搜索、高亮显示、搜索建议
✅ **完整的CRUD操作**：创建、编辑、删除、拖拽排序
✅ **多业务场景适配**：部门、设备、分类等不同业务类型
✅ **丰富的交互功能**：点击选择、展开折叠、多选等
✅ **高性能支持**：虚拟滚动、懒加载、智能缓存
✅ **灵活的配置系统**：主题、权限、API、数据转换等 