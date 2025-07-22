# 通用树系统设计总结

## 🎯 设计约定

### 核心原则
- **统一使用 `system_tree_data_rel` 表**：作为所有树形结构的数据存储核心
- **完全动态化**：无需预定义树类型，支持任意数据类型的树形关联
- **配置驱动**：通过配置表实现树类型和数据类型的灵活管理

## 📊 核心表结构

### 1. system_tree_data_rel（通用树数据关联表）
```sql
CREATE TABLE system_tree_data_rel (
  id BIGINT PRIMARY KEY,
  tree_type VARCHAR(50),      -- 树类型：field_category/hierarchy_group等
  tree_node_id BIGINT,        -- 树节点ID
  data_type VARCHAR(50),      -- 数据类型：field_def/device/region等
  data_id BIGINT,             -- 数据ID
  data_name VARCHAR(255),     -- 数据名称
  metadata JSON               -- 扩展元数据
);
```

### 2. system_tree_config（树结构配置表）
```sql
CREATE TABLE system_tree_config (
  id BIGINT PRIMARY KEY,
  tree_type VARCHAR(50),      -- 树类型编码
  tree_name VARCHAR(100),     -- 树类型名称
  allowed_data_types JSON,    -- 允许的数据类型列表
  max_level INT               -- 最大层级
);
```

### 3. system_data_type_meta（数据类型元数据表）
```sql
CREATE TABLE system_data_type_meta (
  id BIGINT PRIMARY KEY,
  data_type VARCHAR(50),      -- 数据类型编码
  data_name VARCHAR(100),     -- 数据类型名称
  table_name VARCHAR(100),    -- 对应的数据表名
  id_field VARCHAR(50),       -- ID字段名
  name_field VARCHAR(50)      -- 名称字段名
);
```

## 🔧 API接口约定

### 核心接口
- `GET /admin-api/system/tree-data/data/{treeType}` - 获取树数据
- `POST /admin-api/system/tree-data/node/{treeType}` - 添加树节点
- `PUT /admin-api/system/tree-data/node/{treeType}/{nodeId}` - 更新树节点
- `DELETE /admin-api/system/tree-data/node/{treeType}/{nodeId}` - 删除树节点

### 前端API文件
- `tunnel-management-ui/src/api/system/universal-tree.ts` - 通用树API接口

## 🎨 前端组件约定

### 统一组件
- `UniversalTreeView.vue` - 统一树形组件
- 支持拖拽、搜索、动态字段、展开折叠等功能
- 通过props配置实现不同功能需求

### 使用示例
```vue
<template>
  <UniversalTreeView
    :data="treeData"
    :expand-config="{ defaultExpandAll: false, persistExpandState: true }"
    :search-config="{ searchable: true, autoExpandOnSearch: true }"
    :display-config="{ showToolbar: true, showStats: true }"
    :interaction-config="{ editable: true, deletable: true, addable: true }"
    @node-click="handleNodeClick"
    @node-add="handleAddNode"
    @node-edit="handleEditNode"
    @node-delete="handleDeleteNode"
  />
</template>
```

## 📁 目录结构约定

### 前端目录
- `views/system/universal-tree/` - 通用树管理页面
- `components/Common/UniversalTreeView.vue` - 统一树形组件
- `api/system/universal-tree.ts` - 通用树API接口

### 后端目录
- `cheers-module-system/src/main/java/com/cheers/arch/module/system/controller/admin/tree/` - 树形控制器
- `cheers-module-system/src/main/java/com/cheers/arch/module/system/service/tree/` - 树形服务

## 🗄️ 数据库脚本约定

### 表结构脚本
- `sql/mysql/20250703_create_universal_tree_tables.sql` - 创建通用树表结构

### 初始化脚本
- `sql/mysql/20250703_init_universal_tree_data_fixed.sql` - 初始化通用树数据

### 清理脚本
- `sql/mysql/20250703_clean_universal_tree_data.sql` - 清理通用树数据

## 📚 文档约定

### 核心文档
- `docs/features/universal-tree-system.md` - 通用树数据管理系统（核心设计）
- `docs/features/universal-tree-setup-guide.md` - 通用树初始化指南
- `docs/design/core-architecture/universal-tree-data-relation-design.md` - 通用树数据关联设计

### 使用指南
- `docs/features/universal-tree-troubleshooting.md` - 通用树故障排除指南
- `docs/features/menu-management/universal-tree-composables-usage.md` - 通用树组件使用指南

## ❌ 已废弃的设计

### 废弃的表结构
- `business_module` 表 - 已废弃，功能已迁移到通用树系统
- 任何基于 `business_module` 的设计文档

### 废弃的文档
- `sql/mysql/20250127_create_business_module.sql` - 已删除
- `docs/features/menu-management/business-module-multi-level-summary.md` - 已删除

## ✅ 实施检查清单

### 新功能开发时
- [ ] 使用 `system_tree_data_rel` 表存储树形数据
- [ ] 在 `system_tree_config` 中配置树类型
- [ ] 在 `system_data_type_meta` 中配置数据类型
- [ ] 使用 `UniversalTreeView.vue` 组件
- [ ] 调用 `universal-tree.ts` 中的API接口
- [ ] 参考最新的设计文档

### 代码审查时
- [ ] 确认没有使用 `business_module` 表
- [ ] 确认没有使用过时的API接口
- [ ] 确认使用了统一的树形组件
- [ ] 确认遵循了最新的设计约定

## 🎯 总结

通用树系统提供了一个完全动态、可扩展的树形结构管理解决方案。通过统一的数据模型和灵活的配置，可以支持各种业务场景的树形数据管理需求，真正实现了"一次开发，多次使用"的目标。

**核心约定**：所有树形结构都使用 `system_tree_data_rel` 表为核心的通用树系统，不再使用 `business_module` 表。 