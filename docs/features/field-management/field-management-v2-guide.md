# 字段管理V2页面使用指南

## 概述

字段管理V2页面是基于通用树结构设计的新一代字段管理系统，采用`UniversalTreeView`组件实现，提供了更统一、更灵活的字段管理体验。

## 功能特性

### 1. 通用树结构
- **统一管理**：使用通用树结构管理字段分类和字段
- **类型安全**：通过配置确保数据类型的合法性
- **扩展性强**：支持新增树类型和数据类型
- **性能优化**：合理的索引和查询优化

### 2. 界面特性
- **左侧树形结构**：显示字段分类和字段的层级关系
- **右侧详情面板**：显示选中节点的详细信息
- **搜索功能**：支持按分类名称和字段名称搜索
- **拖拽操作**：支持节点拖拽排序和移动
- **右键菜单**：提供快捷操作菜单

### 3. 操作功能
- **添加分类**：创建新的字段分类
- **添加字段**：在指定分类下添加字段
- **编辑节点**：修改分类或字段信息
- **删除节点**：删除分类或字段
- **批量操作**：支持批量添加字段到分类

## 页面结构

### 1. 页面布局
```
┌─────────────────────────────────────────────────────────────┐
│                    页面标题和操作栏                          │
├─────────────────────┬───────────────────────────────────────┤
│                     │                                       │
│     左侧树形结构     │           右侧详情面板                │
│                     │                                       │
│   - 字段分类树       │   - 分类详情                          │
│   - 搜索功能         │   - 字段详情                          │
│   - 工具栏           │   - 字段列表                          │
│   - 右键菜单         │   - 空状态                            │
│                     │                                       │
└─────────────────────┴───────────────────────────────────────┘
```

### 2. 数据模型

#### 树节点结构
```typescript
interface TreeNode {
  id: number
  name: string
  code?: string
  type: 'category' | 'field'
  level: number
  sort: number
  parentId?: number
  children?: TreeNode[]
  
  // 字段特有属性
  fieldLabel?: string
  fieldKey?: string
  fieldType?: string
  required?: boolean
  defaultValue?: string
  validation?: string
  description?: string
  categoryId?: number
  
  // 分类特有属性
  fieldCount?: number
  fields?: any[]
  
  // 通用属性
  createTime?: string
  updateTime?: string
}
```

## 使用指南

### 1. 访问页面
- 路径：`/test/field-management-v2`
- 菜单：测试中心 → 字段管理V2

### 2. 基本操作

#### 查看字段分类
1. 在左侧树形结构中展开分类节点
2. 点击分类节点查看详情
3. 在右侧面板查看分类信息和包含的字段

#### 查看字段详情
1. 在左侧树形结构中点击字段节点
2. 在右侧面板查看字段的详细信息
3. 包括字段类型、是否必填、默认值等

#### 添加分类
1. 点击页面右上角的"添加分类"按钮
2. 或右键点击树节点选择"添加"
3. 填写分类名称、编码等信息
4. 点击确定保存

#### 添加字段
1. 右键点击分类节点选择"添加"
2. 或点击页面右上角的"添加字段"按钮
3. 填写字段名称、编码、类型等信息
4. 选择所属分类
5. 点击确定保存

#### 编辑节点
1. 右键点击节点选择"编辑"
2. 或选中节点后点击右侧面板的"编辑"按钮
3. 修改相关信息
4. 点击确定保存

#### 删除节点
1. 右键点击节点选择"删除"
2. 确认删除操作
3. 注意：删除分类会同时删除其下的所有字段

### 3. 高级功能

#### 搜索功能
- 在左侧搜索框中输入关键词
- 支持按分类名称和字段名称搜索
- 搜索结果会自动高亮显示
- 支持清空搜索条件

#### 拖拽操作
- 支持同级节点拖拽排序
- 支持跨级拖拽移动
- 拖拽时会显示可放置位置的提示

#### 展开/折叠
- 支持全部展开/折叠
- 搜索时自动展开匹配路径
- 展开状态会被记住

## API接口

### 1. 通用树API
```typescript
// 获取字段分类树
fieldManagementApi.getFieldCategoryTree()

// 添加字段分类
fieldManagementApi.addFieldCategory(data)

// 更新字段分类
fieldManagementApi.updateFieldCategory(id, data)

// 删除字段分类
fieldManagementApi.deleteFieldCategory(id)

// 添加字段
fieldManagementApi.addField(data)

// 更新字段
fieldManagementApi.updateField(id, data)

// 删除字段
fieldManagementApi.deleteField(id)
```

### 2. 数据结构
```sql
-- 通用树结构-数据关联表
system_tree_data_rel
├── tree_type: 树类型（field_category）
├── tree_node_id: 树节点ID
├── data_type: 数据类型（field_def）
├── data_id: 数据ID
├── data_name: 数据名称
├── display_order: 显示顺序
├── is_required: 是否必填
└── metadata: 扩展元数据

-- 树结构配置表
system_tree_config
├── tree_type: 树类型编码
├── tree_name: 树类型名称
├── allowed_data_types: 允许的数据类型列表
├── max_level: 最大层级
└── config_json: 扩展配置

-- 数据类型元数据表
system_data_type_meta
├── data_type: 数据类型编码
├── data_name: 数据类型名称
├── table_name: 对应的数据表名
├── id_field: ID字段名
├── name_field: 名称字段名
└── config_json: 扩展配置
```

## 技术实现

### 1. 前端组件
- **UniversalTreeView**：通用树形组件
- **Element Plus**：UI组件库
- **Vue 3 Composition API**：响应式数据管理

### 2. 后端支持
- **通用树API**：提供统一的树结构管理接口
- **字段管理API**：提供字段相关的业务接口
- **数据关联**：通过关联表管理树节点和数据的关系

### 3. 数据库设计
- **system_tree_data_rel**：通用树结构-数据关联表
- **system_tree_config**：树结构配置表
- **system_data_type_meta**：数据类型元数据表

## 优势特点

### 1. 统一性
- 使用通用的树形组件
- 统一的API接口设计
- 一致的用户交互体验

### 2. 扩展性
- 支持新增树类型
- 支持新增数据类型
- 灵活的配置机制

### 3. 性能优化
- 合理的索引设计
- 支持虚拟滚动
- 高效的查询优化

### 4. 用户体验
- 直观的树形结构
- 丰富的交互功能
- 完善的错误处理

## 后续规划

### 1. 功能增强
- 支持字段模板
- 支持字段验证规则配置
- 支持字段权限管理

### 2. 性能优化
- 支持大数据量处理
- 优化查询性能
- 增加缓存机制

### 3. 用户体验
- 支持自定义主题
- 支持多语言
- 增加操作引导

## 总结

字段管理V2页面通过通用树结构设计，实现了统一、灵活、高效的字段管理系统。它不仅解决了现有系统的局限性，还为未来的功能扩展奠定了坚实的基础。 