# SuperAction 设计策略分析

## 核心问题澄清

SuperAction的设计目标是什么？
- **当前理解**：将所有操作变成动态可配置的通用操作渲染器
- **新的思考**：是否应该将标准操作（CRUD、导入导出）固化，提供开箱即用的功能

## 设计策略对比

### 方案A：完全动态配置
```typescript
// 使用方式：需要详细配置每个操作
<SuperAction :actions="[
  {
    key: 'create',
    label: '新增',
    icon: 'Plus',
    type: 'primary',
    handler: (context) => { /* 自定义逻辑 */ }
  },
  // ... 更多配置
]" />
```

**优势**：极高灵活性，一个组件适配所有场景
**劣势**：配置复杂，类型安全性差，调试困难

### 方案B：标准化固化
```typescript
// 使用方式：开箱即用
<SuperAction mode="data-management" 
             :enabled-actions="['create', 'edit', 'delete', 'import', 'export']"
             @create="handleCreate"
             @edit="handleEdit" />
```

**优势**：使用简单，类型安全，调试容易
**劣势**：灵活性降低，扩展需要修改组件

## 推荐方案：混合策略

### 核心理念
- **标准化**：将常见的数据管理操作固化为预设模式
- **可扩展**：保留自定义操作的配置能力
- **场景化**：针对不同业务场景提供专门的SuperAction变种

### 实现策略

#### 1. 按业务场景分类的SuperAction组件

```typescript
// 数据管理场景
<SuperDataAction 
  :enabled-actions="['create', 'edit', 'delete', 'refresh', 'import', 'export']"
  :selection-mode="'multiple'"
  @create="handleCreate"
  @edit="handleEdit"
  @delete="handleDelete" />

// 表单场景  
<SuperFormAction
  :enabled-actions="['save', 'submit', 'reset', 'cancel']"
  :form-state="formState"
  @save="handleSave"
  @submit="handleSubmit" />

// 仪表板场景
<SuperDashboardAction
  :enabled-actions="['refresh', 'fullscreen', 'export', 'settings']"
  @refresh="handleRefresh"
  @fullscreen="toggleFullscreen" />

// 通用场景（保留原有的动态配置能力）
<SuperAction :actions="customActions" />
```

#### 2. 标准操作的类型定义

```typescript
// 数据管理标准操作
export type DataManagementAction = 
  | 'create' | 'edit' | 'delete' | 'view' 
  | 'refresh' | 'import' | 'export' 
  | 'batch-edit' | 'batch-delete'

// 表单标准操作  
export type FormAction = 
  | 'save' | 'submit' | 'reset' | 'cancel'
  | 'preview' | 'validate'

// 仪表板标准操作
export type DashboardAction = 
  | 'refresh' | 'fullscreen' | 'export' 
  | 'settings' | 'share'
```

#### 3. 渐进式增强的配置

```typescript
// 基础使用：开箱即用
<SuperDataAction :enabled-actions="['create', 'edit', 'delete']" />

// 中级使用：自定义行为
<SuperDataAction 
  :enabled-actions="['create', 'edit', 'delete']"
  :action-config="{
    create: { requirePermission: 'data.create' },
    delete: { confirmMessage: '确定删除这些重要数据吗？' }
  }" />

// 高级使用：完全自定义
<SuperDataAction 
  :enabled-actions="['create', 'edit', 'delete']"
  :custom-actions="[
    { key: 'audit', label: '审核', icon: 'Check', handler: handleAudit }
  ]" />
```

### 组件架构设计

```typescript
// 基础SuperAction（保留完全动态配置）
export class SuperAction {
  // 完全动态的操作配置
  actions: Action[]
}

// 场景化SuperAction基类
export abstract class ScenarioSuperAction extends SuperAction {
  // 预定义的标准操作
  abstract getStandardActions(): Record<string, StandardAction>
  
  // 启用的操作列表
  enabledActions: string[]
  
  // 操作配置覆盖
  actionConfig?: Record<string, Partial<ActionConfig>>
  
  // 自定义操作
  customActions?: Action[]
}

// 具体的场景实现
export class SuperDataAction extends ScenarioSuperAction {
  getStandardActions() {
    return {
      create: new CreateAction(),
      edit: new EditAction(),
      delete: new DeleteAction(),
      // ...
    }
  }
}
```

## 实施建议

### 第一阶段：重构现有SuperAction
1. **保留兼容性**：现有的动态配置方式继续支持
2. **提取标准操作**：将CRUD等操作提取为标准Action类
3. **添加场景模式**：通过`mode`属性支持预设场景

### 第二阶段：场景化组件
1. **创建场景化组件**：SuperDataAction、SuperFormAction等
2. **迁移示例**：更新demo和文档，展示新的使用方式
3. **类型增强**：提供完整的TypeScript支持

### 第三阶段：生态完善
1. **配置生成器**：为DynamicConfigurator提供场景化的配置选项
2. **主题系统**：不同场景的视觉主题
3. **权限集成**：标准操作的权限控制

## 决策建议

基于对业务需求的理解，建议采用**混合策略**：

1. **数据管理场景**：使用固化的SuperDataAction，提供开箱即用的CRUD功能
2. **其他场景**：根据需要创建专门的场景化组件
3. **特殊需求**：保留原有的SuperAction作为完全自定义的后备方案

这样既能满足常见场景的易用性需求，又保持了系统的灵活性和扩展性。