# 字段归组功能实现文档

## 概述

字段归组功能允许用户将字段定义关联到多个分级组（hierarchy group），实现字段的树形分组管理。用户可以通过树形选择器直观地选择多个分组，支持1对多的关系设计。

## 功能特性

### 1. 用户友好的分组选择
- **树形选择器**：提供直观的树形界面让用户选择分组
- **多选支持**：支持选择多个分组，实现1对多关系
- **搜索功能**：支持按分组名称搜索
- **当前分组显示**：显示字段当前所属的所有分组
- **未分组选项**：支持将字段设置为未分组状态

### 2. 数据库设计

#### 关联表结构
```sql
CREATE TABLE `system_field_def_hierarchy_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_def_id` bigint NOT NULL COMMENT '字段定义 ID',
  `hierarchy_group_id` bigint NOT NULL COMMENT '分级组 ID',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_field_hierarchy` (`field_def_id`, `hierarchy_group_id`),
  KEY `idx_field_def` (`field_def_id`),
  KEY `idx_hierarchy_group` (`hierarchy_group_id`)
) COMMENT='字段定义-分级组 关联表';
```

**关系设计说明**：
- 一个字段可以属于多个分级组（1对多关系）
- 通过关联表维护字段和分组的对应关系
- 唯一约束确保同一字段不会重复关联到同一分组

### 3. 后端实现

#### 数据对象
- `FieldDefHierarchyRelDO`：关联表数据对象
- `FieldDefHierarchyRelMapper`：数据访问层
- `FieldDefHierarchyRelService`：业务逻辑层

#### API 接口
- `PUT /system/field/update-hierarchy-group`：更新字段单个分组
- `PUT /system/field/update-hierarchy-groups`：批量更新字段分组关联
- `GET /system/field/hierarchy-rels`：获取字段分组关联信息

#### 核心方法
```java
// 创建字段-分组关联（支持1对多）
Long createFieldDefHierarchyRel(Long fieldDefId, Long hierarchyGroupId)

// 批量更新字段的分组关联
void updateFieldDefHierarchyRels(Long fieldDefId, List<Long> hierarchyGroupIds)

// 获取字段的所有分组ID
List<Long> getAllHierarchyGroupIdsByFieldDefId(Long fieldDefId)

// 删除特定的字段-分组关联
void deleteSpecificFieldDefHierarchyRel(Long fieldDefId, Long hierarchyGroupId)
```

### 4. 前端实现

#### 组件结构
- `FieldGroupSelector.vue`：分组选择器组件（支持多选）
- `FieldManagement.vue`：字段管理页面（集成分组选择器）

#### 功能特性
- **多选树形展示**：以树形结构展示所有可用的分级组，支持多选
- **搜索过滤**：支持按分组名称搜索
- **当前状态显示**：显示字段当前所属的所有分组
- **确认/取消操作**：提供确认和取消操作

#### API 调用
```typescript
// 更新字段单个分组
export const updateFieldHierarchy = (fieldId: number, hierarchyGroupId?: number) => {
  return request.put<boolean>({
    url: '/system/field/update-hierarchy-group',
    data: { fieldId, hierarchyGroupId }
  })
}

// 批量更新字段分组（支持1对多）
export const updateFieldHierarchies = (fieldId: number, hierarchyGroupIds?: number[]) => {
  return request.put<boolean>({
    url: '/system/field/update-hierarchy-groups',
    data: { fieldId, hierarchyGroupIds }
  })
}

// 获取字段分组关联信息
export const getFieldHierarchyRels = (fieldIds: number[]) => {
  return request.get<any[]>({
    url: '/system/field/hierarchy-rels',
    params: { fieldIds: fieldIds.join(',') }
  })
}
```

## 使用流程

### 1. 字段归组操作
1. 在字段管理页面，点击字段卡片上的"归组"按钮
2. 弹出分组选择器对话框
3. 在树形结构中选择多个目标分组（支持多选）
4. 点击"确定"完成归组操作
5. 系统会一次性更新字段的所有分组关联（替换现有关联）

### 2. 分组选择器界面
- **字段信息展示**：显示字段名称、标识、数据类型
- **分组树形结构**：展示所有可用的分级组，支持多选
- **搜索功能**：支持按分组名称搜索
- **当前分组高亮**：显示字段当前所属的所有分组

## 技术实现细节

### 1. 数据关联
- 一个字段可以属于多个分级组（1对多关系）
- 支持将字段设置为未分组状态（删除所有关联）
- 通过关联表维护字段和分组的对应关系
- 唯一约束防止重复关联

### 2. 前端状态管理
- 使用 Vue 3 Composition API
- 响应式数据管理
- 组件间通信通过 props 和 events
- 支持多选状态管理

### 3. 错误处理
- API 调用失败时的错误提示
- 网络异常时的降级处理
- 用户操作取消的处理

## 扩展功能

### 1. 批量归组
- 支持批量选择字段进行归组
- 批量更新接口优化

### 2. 分组统计
- 显示每个分组下的字段数量
- 分组使用情况统计

### 3. 权限控制
- 基于用户权限的分组操作控制
- 分组管理权限分离

## 总结

字段归组功能通过关联表设计实现了字段和分级组的1对多关联，用户界面采用支持多选的树形选择器提供了直观的操作体验。整个功能模块化设计，便于维护和扩展，支持灵活的字段分组管理。 