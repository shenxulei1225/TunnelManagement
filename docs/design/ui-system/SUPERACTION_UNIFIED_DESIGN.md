# SuperAction 统一设计方案

## 🎯 设计理念

基于您的建议，我们将各种场景的Action整合到一个统一的组件中，通过**场景参数**来控制显示的操作按钮，同时保持**细粒度的可用性控制**。

## 🏗️ 架构设计

### 核心组件：SuperActionV2

```vue
<SuperActionV2
  scenario="data-management"
  :enabled-actions="['create', 'edit', 'delete', 'refresh']"
  :action-config="{
    delete: { confirmMessage: '确定删除吗？' },
    edit: { disabled: (context) => context.selectedItems[0]?.readonly }
  }"
  @create="handleCreate"
  @edit="handleEdit"
/>
```

### 支持的场景

| 场景 | 标识 | 包含操作 | 适用场景 |
|------|------|----------|----------|
| 数据管理 | `data-management` | CRUD、导入导出、批量操作 | 数据表格、列表管理 |
| 表单操作 | `form` | 保存、提交、重置、验证 | 表单页面、编辑界面 |
| 仪表板 | `dashboard` | 刷新、全屏、导出、设置 | 监控面板、报表页面 |
| 工作流 | `workflow` | 启动、暂停、审批、拒绝 | 流程管理、审批系统 |
| 自定义 | `custom` | 完全自定义 | 特殊业务场景 |

## 🎛️ 配置层次

### 1. 场景级配置
```typescript
// 选择场景，获得该场景的所有标准操作
scenario: 'data-management'
```

### 2. 操作级配置
```typescript
// 从场景操作中选择需要的操作
enabledActions: ['create', 'edit', 'delete', 'refresh']
```

### 3. 细节级配置
```typescript
// 对具体操作进行个性化配置
actionConfig: {
  delete: {
    confirmMessage: '确定删除这些重要数据吗？',
    disabled: (context) => context.selectedItems.some(item => item.readonly)
  },
  edit: {
    hidden: (context) => !hasPermission('edit')
  }
}
```

## ✨ 核心特性

### 1. 场景化标准操作

每个场景都有预定义的标准操作集合：

```typescript
// 数据管理场景 - 10个标准操作
DATA_MANAGEMENT_ACTIONS = [
  'create', 'edit', 'delete', 'view',           // 基础CRUD
  'refresh', 'import', 'export',                // 数据管理
  'batch-edit', 'batch-delete', 'batch-export'  // 批量操作
]

// 表单场景 - 7个标准操作  
FORM_ACTIONS = [
  'save', 'submit', 'reset', 'cancel',     // 基础表单
  'preview', 'validate', 'draft'           // 增强功能
]
```

### 2. 智能的条件控制

```typescript
// 自动处理业务逻辑
condition: {
  visible: (context) => {
    // 批量操作自动判断选择模式和数量
    if (action.key.startsWith('batch-')) {
      return config.business.selectionMode === 'multiple' && 
             context.selectedItems.length >= config.business.batchMinSelection
    }
    return true
  },
  disabled: (context) => {
    // 只读项目自动禁用编辑删除
    if (['edit', 'delete'].includes(action.key)) {
      const item = context.currentItem || context.selectedItems[0]
      return item?.readonly === true
    }
    return false
  }
}
```

### 3. 类型安全的事件系统

```typescript
interface Emits {
  // 通用事件
  action: [actionKey: string, context: ActionContext]
  
  // 具体操作事件 - 自动推导类型
  create: [context: ActionContext]
  edit: [context: ActionContext]
  delete: [context: ActionContext]
  // ... 所有操作都有对应的类型安全事件
}
```

### 4. 渐进式配置复杂度

#### 基础使用（零配置）
```vue
<SuperActionV2 scenario="data-management" />
<!-- 自动显示：create, edit, delete, view, refresh -->
```

#### 中级使用（选择操作）
```vue
<SuperActionV2 
  scenario="data-management"
  :enabled-actions="['create', 'edit', 'delete']" />
```

#### 高级使用（自定义配置）
```vue
<SuperActionV2 
  scenario="data-management"
  :enabled-actions="['create', 'edit', 'delete']"
  :action-config="{
    delete: { 
      confirmMessage: '确定删除吗？',
      requirePermission: 'data.delete'
    }
  }"
  :custom-actions="[
    { key: 'audit', label: '审核', handler: handleAudit }
  ]" />
```

## 🎨 使用示例

### 数据管理场景
```vue
<SuperActionV2
  scenario="data-management"
  :enabled-actions="['create', 'edit', 'delete', 'refresh', 'export']"
  :config="{
    business: {
      selectionMode: 'multiple',
      batchMinSelection: 2
    }
  }"
  @create="handleCreate"
  @delete="handleDelete"
/>
```

### 表单场景
```vue
<SuperActionV2
  scenario="form"
  :enabled-actions="['save', 'submit', 'reset', 'cancel']"
  :config="{
    business: {
      formState: 'editing',
      autoSave: true
    }
  }"
  @save="handleSave"
  @submit="handleSubmit"
/>
```

### 仪表板场景
```vue
<SuperActionV2
  scenario="dashboard"
  :enabled-actions="['refresh', 'fullscreen', 'export']"
  :config="{
    business: {
      refreshInterval: 30,
      allowFullscreen: true
    }
  }"
  @refresh="handleRefresh"
  @fullscreen="toggleFullscreen"
/>
```

## 📊 优势对比

| 特性 | 之前的方案 | 统一设计方案 |
|------|-----------|-------------|
| **组件数量** | 多个场景组件 | ✅ 一个统一组件 |
| **使用复杂度** | 需要选择合适组件 | ✅ 只需指定场景 |
| **配置灵活性** | 中等 | ✅ 高度灵活 |
| **类型安全** | 分散的类型定义 | ✅ 统一的类型系统 |
| **维护成本** | 多个组件维护 | ✅ 单一组件维护 |
| **学习成本** | 需了解多个API | ✅ 学会一个API |

## 🔧 实现细节

### 场景注册表
- `ScenarioActionRegistry.ts` - 统一管理所有场景的操作定义
- 支持动态注册新场景
- 操作按组织结构清晰分类

### 配置适配器
- 自动将业务配置转换为具体的操作配置
- 处理场景特定的业务逻辑
- 支持权限、条件、确认等增强功能

### 事件系统
- 通用 `action` 事件 + 具体操作事件
- 完整的TypeScript类型支持
- 自动的事件名称转换

## 🎯 总结

这个统一设计方案实现了：

1. **简化使用**：一个组件解决所有场景
2. **灵活配置**：从零配置到完全自定义
3. **类型安全**：完整的TypeScript支持
4. **智能化**：自动处理常见业务逻辑
5. **可扩展**：轻松添加新场景和操作

**核心理念**：通过场景参数统一入口，通过启用列表控制显示，通过配置对象精细调节。既保持了组件的统一性，又提供了足够的灵活性。