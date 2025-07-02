# Element Plus 使用规范 ⚠️ **强制执行标准**

> **🚨 强制性开发标准：这是项目开发的强制执行要求，不可违反**
> 
> **核心原则：优先使用官方方法，禁止重复造轮子**
> 
> **⚠️ 强制要求：每次开发前必须阅读本文档**

## 🔴 强制性开发原则 **（违反将拒绝代码合并）**

### 🚨 绝对禁止行为

- **如果Element Plus官方有对应功能，禁止自定义实现**
- **禁止重复造轮子，必须使用官方方法增加组件通用性**
- **禁止自行添加CSS类名进行样式控制（应使用官方属性）**
- **禁止绕过官方API直接操作DOM元素**

### ✅ 强制执行规则

- **只有官方控件不具备的功能，才允许开发自定义功能**
- **所有自定义功能必须先获得客户明确确认方案**
- **必须使用官方推荐的TypeScript类型定义**
- **必须遵循官方事件处理机制**

## 📋 强制性开发前检查流程 **（必须完成所有步骤）**

**在实现任何Element Plus相关功能前，必须按序执行：**

1. **📖 查阅官方文档** - 确认是否有对应的官方方法
2. **🔍 方法验证** - 测试官方方法是否满足需求  
3. **📋 记录决策** - 在代码注释中记录选择官方方法或自定义的原因
4. **✅ 团队评审** - 自定义功能需要技术负责人评审确认
5. **📝 文档更新** - 在项目文档中记录使用的官方方法和配置

## 🎯 常见违规行为示例

### ❌ 严重违规示例

```typescript
// ❌ 禁止：自定义实现官方已有的Tree展开功能
const handleCustomExpand = (nodeId) => {
  // 自定义展开逻辑...
}

// ❌ 禁止：手动CSS类控制菜单高亮
.custom-active-menu {
  background-color: #409EFF;
}

// ❌ 禁止：直接DOM操作绕过官方API
document.querySelector('.el-tree-node').classList.add('custom-class')
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

## ⚠️ 违规处理机制

### 发现违规代码的处理流程

1. **🚫 立即拒绝合并** - 违规代码不允许合并到主分支
2. **📋 记录违规问题** - 在问题跟踪表中记录具体违规内容
3. **🔄 强制重构** - 必须按官方方式重构后才能重新提交
4. **📚 知识分享** - 将正确方法分享给团队，避免重复问题

### 技术负责人评审要求

对于确实需要自定义功能的情况：
1. 提供详细的需求分析和官方功能不足的说明
2. 提供自定义实现的技术方案和风险评估
3. 获得技术负责人和客户的双重确认
4. 在代码中添加详细的注释说明决策原因
5. 定期回顾是否有官方更新可以替代自定义实现

---

**📋 强制执行标准**: 本文档是项目开发的强制执行标准，任何违反本规范的代码都不应该合并到主分支。 