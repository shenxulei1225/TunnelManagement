## 📋 核心组件使用规范
### ✅ 正确实现示例

```typescript
// ✅ 正确：使用官方推荐方式
const expandedKeys = ref<number[]>([])

// ✅ 使用官方方法
treeRef.value.setCurrentKey(nodeId)

// ✅ 使用官方事件和属性
@node-expand="handleNodeExpand"
:default-expanded-keys="expandedKeys"
:default-active="activeMenuId"
```

## 📋 核心组件使用规范

### Tree组件展开状态管理

```typescript
// ✅ 正确：使用官方推荐方式
const expandedKeys = ref<number[]>([])

// ✅ 官方方法
treeRef.value.setCurrentKey(nodeId)

// ✅ 官方事件
@node-expand="handleNodeExpand"
@node-collapse="handleNodeCollapse" 
:default-expanded-keys="expandedKeys"

// ❌ 严格禁止：自定义实现官方已有功能
// 不允许自行实现展开状态管理
```

### Menu组件高亮控制

```typescript
// ✅ 正确：使用官方属性
:default-active="activeMenuId"
@select="handleSelect"

// ❌ 严格禁止：手动CSS类控制
// 不允许自行添加active类名
```

## 🏗️ 官方标准实现示例

### Tree组件完整示例

```vue
<template>
  <el-tree
    ref="treeRef"
    :data="treeData"
    :default-expanded-keys="expandedKeys"
    node-key="id"
    @node-expand="handleNodeExpand"
    @node-collapse="handleNodeCollapse"
    @node-click="handleNodeClick"
  />
</template>

<script setup lang="ts">
// ✅ 使用官方推荐的响应式状态管理
const expandedKeys = ref<number[]>([])
const treeRef = ref()

// ✅ 使用官方事件处理器
const handleNodeExpand = (data: any) => {
  if (!expandedKeys.value.includes(data.id)) {
    expandedKeys.value.push(data.id)
  }
}

const handleNodeCollapse = (data: any) => {
  const index = expandedKeys.value.indexOf(data.id)
  if (index > -1) {
    expandedKeys.value.splice(index, 1)
  }
}

// ✅ 使用官方方法设置当前节点
const focusNode = (nodeId: number) => {
  treeRef.value?.setCurrentKey(nodeId)
}
</script>
```

### Form表单验证示例

```vue
<template>
  <el-form ref="formRef" :model="form" :rules="rules">
    <!-- ✅ 正确：必须添加 prop 属性进行验证 -->
    <el-form-item label="用户名" prop="username">
      <el-input v-model="form.username" />
    </el-form-item>
    
    <!-- ❌ 错误：缺少 prop 属性 -->
    <!-- <el-form-item label="用户名">
      <el-input v-model="form.username" />
    </el-form-item> -->
  </el-form>
</template>

<script setup lang="ts">
const formRef = ref<FormInstance>()
const form = reactive({
  username: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ]
}
</script>
```


### ✅ 正确实现示例

```typescript
// ✅ 正确：使用官方推荐方式
const expandedKeys = ref<number[]>([])

// ✅ 使用官方方法
treeRef.value.setCurrentKey(nodeId)

// ✅ 使用官方事件和属性
@node-expand="handleNodeExpand"
:default-expanded-keys="expandedKeys"
:default-active="activeMenuId"
```

## 📋 核心组件使用规范

### Tree组件展开状态管理

```typescript
// ✅ 正确：使用官方推荐方式
const expandedKeys = ref<number[]>([])

// ✅ 官方方法
treeRef.value.setCurrentKey(nodeId)

// ✅ 官方事件
@node-expand="handleNodeExpand"
@node-collapse="handleNodeCollapse" 
:default-expanded-keys="expandedKeys"

// ❌ 严格禁止：自定义实现官方已有功能
// 不允许自行实现展开状态管理
```

### Menu组件高亮控制

```typescript
// ✅ 正确：使用官方属性
:default-active="activeMenuId"
@select="handleSelect"

// ❌ 严格禁止：手动CSS类控制
// 不允许自行添加active类名
```

## 🏗️ 官方标准实现示例

### Tree组件完整示例

```vue
<template>
  <el-tree
    ref="treeRef"
    :data="treeData"
    :default-expanded-keys="expandedKeys"
    node-key="id"
    @node-expand="handleNodeExpand"
    @node-collapse="handleNodeCollapse"
    @node-click="handleNodeClick"
  />
</template>

<script setup lang="ts">
// ✅ 使用官方推荐的响应式状态管理
const expandedKeys = ref<number[]>([])
const treeRef = ref()

// ✅ 使用官方事件处理器
const handleNodeExpand = (data: any) => {
  if (!expandedKeys.value.includes(data.id)) {
    expandedKeys.value.push(data.id)
  }
}

const handleNodeCollapse = (data: any) => {
  const index = expandedKeys.value.indexOf(data.id)
  if (index > -1) {
    expandedKeys.value.splice(index, 1)
  }
}

// ✅ 使用官方方法设置当前节点
const focusNode = (nodeId: number) => {
  treeRef.value?.setCurrentKey(nodeId)
}
</script>
```

### Form表单验证示例

```vue
<template>
  <el-form ref="formRef" :model="form" :rules="rules">
    <!-- ✅ 正确：必须添加 prop 属性进行验证 -->
    <el-form-item label="用户名" prop="username">
      <el-input v-model="form.username" />
    </el-form-item>
    
    <!-- ❌ 错误：缺少 prop 属性 -->
    <!-- <el-form-item label="用户名">
      <el-input v-model="form.username" />
    </el-form-item> -->
  </el-form>
</template>

<script setup lang="ts">
const formRef = ref<FormInstance>()
const form = reactive({
  username: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ]
}
</script>
```

