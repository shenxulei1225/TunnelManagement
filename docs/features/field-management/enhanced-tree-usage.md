# 增强版UniversalTree使用指南

## 🚀 **快速开始**

### **基础使用**
```vue
<template>
  <UniversalTreeEnhanced
    business-type="dept"
    :plugins="['search', 'crud', 'drag', 'persistence']"
    @node-select="handleSelect"
  />
</template>

<script setup lang="ts">
import { UniversalTreeEnhanced } from '@/components/UniversalTree'

const handleSelect = (node) => {
  console.log('选中节点:', node)
}
</script>
```

## 🔥 **ExtendTree功能完全迁移**

### **1. 高级拖拽验证**
```vue
<template>
  <UniversalTreeEnhanced
    business-type="device"
    :plugins="['search', 'crud', 'drag']"
    :drag-rules="advancedDragConfig"
    @drag-complete="handleDragComplete"
  />
</template>

<script setup lang="ts">
// 🔥 ExtendTree的高级拖拽验证 - 完全移植
const advancedDragConfig = {
  enabled: true,
  rules: {
    forbiddenDragIds: [1, 2, 3],           // 禁止拖拽的节点
    forbiddenDropIds: [99, 100],           // 禁止作为目标的节点
    maxDepth: 5,                           // 最大层级深度
    allowCycle: false,                     // 禁止循环引用
    customValidator: (dragNode, dropNode, dropType) => {
      // 自定义验证逻辑
      if (dragNode.type === 'important' && dropType === 'inner') {
        return '重要节点不能作为子节点'
      }
      return true
    }
  },
  messages: {
    dragForbidden: '此设备不允许移动',
    dropForbidden: '不能移动到此位置',
    maxDepthExceeded: '超过设备层级深度限制',
    cycleNotAllowed: '不允许循环引用',
    customError: '移动验证失败'
  }
}

const handleDragComplete = (result) => {
  console.log('拖拽完成:', result)
}
</script>
```

### **2. 状态持久化**
```vue
<template>
  <UniversalTreeEnhanced
    business-type="dept"
    :plugins="['search', 'crud', 'persistence']"
    :persistence="persistenceConfig"
    show-state-panel
  />
</template>

<script setup lang="ts">
// 🔥 ExtendTree的状态持久化 - 完全移植
const persistenceConfig = {
  enabled: true,
  storageKey: 'dept-tree-state',          // 自定义存储键
  expandedKeys: true,                     // 保存展开状态
  selectedKey: true,                      // 保存选中状态
  searchKeyword: true,                    // 保存搜索关键词
  autoSave: true,                         // 自动保存
  debounceTime: 300                       // 防抖时间
}
</script>
```

### **3. 生命周期钩子**
```vue
<template>
  <UniversalTreeEnhanced
    business-type="category"
    :plugins="['search', 'crud', 'hooks']"
    :hooks="lifecycleHooks"
    :messages="customMessages"
  />
</template>

<script setup lang="ts">
// 🔥 ExtendTree的生命周期钩子 - 完全移植
const lifecycleHooks = {
  // 创建前验证
  onBeforeCreate: async (data) => {
    console.log('准备创建:', data)
    // 权限检查
    if (!hasPermission('category:create')) {
      ElMessage.error('无创建权限')
      return false
    }
    return true
  },
  
  // 创建后处理
  onAfterCreate: async (data, result) => {
    console.log('创建完成:', data, result)
    // 刷新相关缓存
    await refreshCategoryCache()
    // 记录操作日志
    logUserAction('create_category', result)
  },
  
  // 删除前确认
  onBeforeDelete: async (data) => {
    // 检查是否有子项
    if (data.children?.length > 0) {
      ElMessage.error('请先删除子分类')
      return false
    }
    return true
  },
  
  // 拖拽前验证
  onBeforeDrag: async (dragData, dropData, dropType) => {
    // 业务规则验证
    if (dragData.type === 'system' && dropData.type !== 'system') {
      return false
    }
    return true
  },
  
  // 错误处理
  onError: (error, operation, data) => {
    console.error(`操作失败: ${operation}`, error, data)
    // 发送错误报告
    reportError({
      operation,
      error: error.message,
      data: data?.name,
      timestamp: new Date().toISOString()
    })
  }
}

// 🔥 ExtendTree的消息配置 - 完全移植
const customMessages = {
  success: {
    create: '分类创建成功！',
    update: '分类更新成功！',
    delete: '分类删除成功！'
  },
  error: {
    create: '分类创建失败，请重试',
    update: '分类更新失败，请重试',
    delete: '分类删除失败，请重试',
    permission: '权限不足，请联系管理员'
  },
  confirm: {
    delete: '确认删除此分类？删除后不可恢复',
    batchDelete: '确认删除选中的分类？'
  },
  customMessage: (operation, data, error) => {
    if (operation === 'delete' && data.children?.length > 0) {
      return '删除失败：请先删除子分类'
    }
    return ''
  }
}
</script>
```

### **4. 图标映射系统**
```vue
<template>
  <UniversalTreeEnhanced
    business-type="device"
    :plugins="['search', 'crud', 'icon-mapping']"
    :icon-mapping="iconMappingConfig"
  />
</template>

<script setup lang="ts">
// 🔥 ExtendTree的图标映射 - 完全移植
const iconMappingConfig = {
  enabled: true,
  iconField: 'deviceType',              // 图标字段
  iconMapping: {
    'server': 'Monitor',
    'router': 'Connection',
    'switch': 'Grid',
    'camera': 'Camera',
    'sensor': 'Radar'
  },
  defaultIcon: 'Box',                   // 默认图标
  dynamicIcon: (node) => {              // 动态图标生成
    if (node.status === 'online') {
      return node.deviceType + '-online'
    } else if (node.status === 'offline') {
      return node.deviceType + '-offline'
    }
    return node.deviceType
  },
  iconPrefix: 'el-icon-',               // 图标前缀
  useBusinessTypeIcon: true             // 使用业务类型默认图标
}
</script>
```

### **5. 外部拖拽支持**
```vue
<template>
  <div class="layout">
    <!-- 外部元素 -->
    <div class="external-items">
      <div 
        v-for="item in externalItems"
        :key="item.id"
        class="draggable-item"
        draggable="true"
        @dragstart="handleExternalDragStart($event, item)"
      >
        {{ item.name }}
      </div>
    </div>
    
    <!-- 树形组件 -->
    <UniversalTreeEnhanced
      business-type="category"
      :plugins="['search', 'crud', 'external-drag']"
      :external-drag="externalDragConfig"
      @external-drop="handleExternalDrop"
    />
  </div>
</template>

<script setup lang="ts">
// 🔥 ExtendTree的外部拖拽 - 完全移植
const externalDragConfig = {
  enabled: true,
  acceptTypes: ['text/plain', 'application/json'],
  dropZoneClass: 'drop-zone-active',
  onExternalDragOver: (event) => {
    // 检查拖拽数据类型
    return event.dataTransfer?.types.includes('text/plain')
  },
  onExternalDrop: async (event, targetNode) => {
    const data = event.dataTransfer?.getData('text/plain')
    if (data) {
      const item = JSON.parse(data)
      console.log('外部元素拖拽到树中:', item, targetNode)
      
      // 创建新节点
      await createNodeFromExternal(item, targetNode)
    }
  }
}

const handleExternalDragStart = (event, item) => {
  event.dataTransfer.setData('text/plain', JSON.stringify(item))
}

const handleExternalDrop = (event, targetNode) => {
  console.log('外部拖拽完成:', event, targetNode)
}
</script>
```

## 🎯 **高级用法示例**

### **1. 多实例状态隔离**
```vue
<template>
  <div class="multi-tree-layout">
    <!-- 部门树 -->
    <UniversalTreeEnhanced
      business-type="dept"
      :persistence="{ 
        enabled: true, 
        storageKey: 'dept-tree-instance-1' 
      }"
    />
    
    <!-- 设备树 -->
    <UniversalTreeEnhanced
      business-type="device"
      :persistence="{ 
        enabled: true, 
        storageKey: 'device-tree-instance-1' 
      }"
    />
  </div>
</template>
```

### **2. 完整业务场景**
```vue
<template>
  <div class="business-page">
    <UniversalTreeEnhanced
      business-type="dept"
      :plugins="['search', 'crud', 'drag', 'persistence', 'hooks']"
      :config="enhancedConfig"
      :drag-rules="dragRules"
      :persistence="persistence"
      :hooks="hooks"
      :messages="messages"
      debug
      show-state-panel
      @node-select="handleDeptSelect"
      @node-created="handleDeptCreated"
      @drag-complete="handleDragComplete"
      @state-change="handleStateChange"
    />
  </div>
</template>

<script setup lang="ts">
// 🚀 完整的企业级配置示例
const enhancedConfig = {
  // 业务驱动配置
  businessType: 'dept',
  
  // 显示配置
  display: {
    showSearch: true,
    showCounter: true,
    showIcons: true,
    showActions: true,
    virtualScroll: false
  },
  
  // 交互配置
  interaction: {
    selectable: true,
    multiSelect: false,
    draggable: true,
    editable: true,
    sortable: true,
    checkable: false
  },
  
  // 权限配置
  permissions: {
    create: hasPermission('dept:create'),
    edit: hasPermission('dept:edit'),
    delete: hasPermission('dept:delete'),
    move: hasPermission('dept:move')
  },
  
  // 性能配置
  performance: {
    virtualScroll: {
      enabled: false,
      itemHeight: 32,
      threshold: 1000
    },
    search: {
      debounceTime: 300,
      maxResults: 100,
      minLength: 2,
      cache: true
    }
  }
}

// 高级拖拽规则
const dragRules = {
  enabled: true,
  rules: {
    forbiddenDragIds: getSystemDeptIds(),
    maxDepth: 6,
    allowCycle: false,
    customValidator: (dragNode, dropNode, dropType) => {
      // 不能将上级部门拖拽到下级部门
      if (isParentDept(dragNode, dropNode)) {
        return '不能将上级部门移动到下级部门'
      }
      return true
    }
  }
}

// 状态持久化
const persistence = {
  enabled: true,
  storageKey: 'enterprise-dept-tree',
  expandedKeys: true,
  selectedKey: true,
  searchKeyword: true,
  autoSave: true
}

// 生命周期钩子
const hooks = {
  onBeforeCreate: validateDeptCreation,
  onAfterCreate: syncDeptToSystem,
  onBeforeDelete: checkDeptDependencies,
  onAfterDelete: cleanupDeptData,
  onError: reportDeptError
}

// 自定义消息
const messages = {
  success: {
    create: '部门创建成功，相关权限已自动配置',
    delete: '部门删除成功，相关数据已清理'
  },
  error: {
    create: '部门创建失败，请检查部门编码是否重复',
    delete: '部门删除失败，请先转移部门下的员工'
  }
}
</script>
```

### **3. 性能优化场景**
```vue
<template>
  <UniversalTreeEnhanced
    business-type="device"
    :plugins="['search', 'crud', 'virtual-scroll']"
    :config="performanceConfig"
  />
</template>

<script setup lang="ts">
// 大数据量性能优化配置
const performanceConfig = {
  display: {
    virtualScroll: true
  },
  performance: {
    virtualScroll: {
      enabled: true,
      itemHeight: 32,
      bufferSize: 20,
      threshold: 500              // 超过500个节点启用虚拟滚动
    },
    lazyLoad: {
      enabled: true,
      loadChildren: async (node) => {
        // 懒加载子节点
        return await api.getDeviceChildren(node.id)
      },
      loadThreshold: 100          // 超过100个子节点启用懒加载
    },
    search: {
      debounceTime: 500,          // 搜索防抖
      maxResults: 200,            // 最大搜索结果
      cache: true                 // 启用搜索缓存
    },
    rendering: {
      enableMemo: true,           // 启用记忆化
      updateStrategy: 'batched',  // 批量更新策略
      batchSize: 50               // 批量大小
    }
  }
}
</script>
```

## 🔄 **从ExtendTree迁移**

### **迁移对比表**

| ExtendTree | UniversalTree Enhanced | 迁移难度 |
|------------|----------------------|---------|
| 基础功能 | ✅ 完全兼容 | 🟢 简单 |
| 拖拽验证 | ✅ 完全移植 | 🟢 简单 |
| 状态持久化 | ✅ 完全移植 | 🟢 简单 |
| 生命周期钩子 | ✅ 完全移植 | 🟢 简单 |
| 图标映射 | ✅ 完全移植 | 🟢 简单 |
| 外部拖拽 | ✅ 完全移植 | 🟡 中等 |
| 自定义配置 | ✅ 增强版本 | 🟢 简单 |

### **一键迁移脚本**
```typescript
// 从ExtendTree配置转换为EnhancedTree配置
function migrateFromExtendTree(extendTreeConfig: any): EnhancedTreeConfig {
  return {
    businessType: extendTreeConfig.businessType || 'category',
    
    // 基础配置迁移
    display: {
      showSearch: extendTreeConfig.showSearch ?? true,
      showActions: extendTreeConfig.showActions ?? true,
      showIcons: extendTreeConfig.nodeConfig?.showIcon ?? true,
      showCounter: extendTreeConfig.nodeConfig?.showCount ?? true
    },
    
    // 拖拽配置迁移
    dragRules: {
      enabled: extendTreeConfig.interaction?.draggable ?? false,
      rules: extendTreeConfig.dragBusiness || {},
      messages: extendTreeConfig.dragMessages || {}
    },
    
    // 状态持久化迁移
    persistence: {
      enabled: extendTreeConfig.autoSaveState ?? false,
      storageKey: extendTreeConfig.expandMemoryKey || 'universal-tree-state',
      expandedKeys: true,
      selectedKey: true,
      autoSave: true
    },
    
    // 生命周期钩子迁移
    hooks: extendTreeConfig.operations || {},
    
    // 图标映射迁移
    iconMapping: {
      enabled: Boolean(extendTreeConfig.nodeConfig?.iconMapping),
      iconMapping: extendTreeConfig.nodeConfig?.iconMapping || {},
      defaultIcon: extendTreeConfig.nodeConfig?.defaultIcon || 'Folder'
    }
  }
}
```

## 🎉 **升级优势总结**

### **✅ 保留ExtendTree所有优秀特性**
- 🔥 高级拖拽验证系统
- 🔥 状态持久化功能
- 🔥 生命周期钩子系统
- 🔥 图标映射系统
- 🔥 外部拖拽支持

### **🚀 新增强大功能**
- ⚡ 虚拟滚动性能优化
- 🎯 业务类型驱动配置
- 🔍 智能搜索和高亮
- 🎨 多主题支持
- 📊 状态管理面板
- 🔧 插件化架构

### **📈 技术升级**
- 🛡️ 完整TypeScript支持
- ⚡ Vue 3 Composition API
- 🎭 响应式设计
- 🧪 可测试架构
- 📚 完整文档支持

**结果：您现在拥有一个功能更全面、性能更优秀、维护更简单的树形组件！** 🎉 