# 字段管理API接口结构文档

## 概述

字段管理系统包含四个核心功能模块，每个模块都有独立的API接口文件，功能清晰分离：

## 1. 字段定义API

**路径**：`@/api/system/field/index.ts`
**功能**：管理字段定义本身
**数据库表**：`system_field`

### 主要接口
- `getFieldPage()` - 获取字段定义分页
- `getFieldList()` - 获取字段定义列表
- `createField()` - 创建字段定义
- `updateField()` - 更新字段定义
- `deleteField()` - 删除字段定义
- `getField()` - 获取字段定义详情
- `getFieldByKey()` - 根据字段键获取字段
- `existsFieldKey()` - 检查字段键是否存在
- `getFieldTypes()` - 获取字段类型列表

### 数据表结构
```sql
system_field (
  id BIGINT PRIMARY KEY,
  field_key VARCHAR(100) NOT NULL,
  field_label VARCHAR(200) NOT NULL,
  value_type VARCHAR(50) NOT NULL,
  unit VARCHAR(50),
  enum_json TEXT,
  sort INT DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME
)
```

## 2. 字段分组API

**路径**：`@/api/system/fieldHierarchy/index.ts`
**功能**：管理字段分组，用户快速找到需要用的字段
**数据库表**：`field_hierarchy_rel`

### 主要接口
- `getFieldHierarchyTree()` - 获取字段分组树
- `getFieldHierarchyList()` - 获取字段分组列表
- `createFieldHierarchy()` - 创建字段分组
- `updateFieldHierarchy()` - 更新字段分组
- `deleteFieldHierarchy()` - 删除字段分组
- `getFieldsByHierarchyId()` - 根据分组ID获取字段列表
- `linkFieldHierarchy()` - 关联字段到分组
- `unlinkFieldHierarchy()` - 解除字段与分组的关联

### 功能特点
- 目的：用户快速找到需要用的字段
- 特点：所有字段存放在分组中，便于查找和管理
- 支持树形结构，便于组织和管理

## 3. 分类API

**路径**：`@/api/system/category/index.ts`
**功能**：管理分类树结构
**数据库表**：`system_category`

### 主要接口
- `getCategoryTree()` - 获取分类树
- `getCategoryList()` - 获取分类列表
- `createCategory()` - 创建分类
- `updateCategory()` - 更新分类
- `deleteCategory()` - 删除分类
- `getCategory()` - 获取分类详情
- `moveCategory()` - 移动分类
- `sortCategories()` - 排序分类
- `getCategoryListByParentId()` - 根据父分类获取子分类
- `existsCategoryCode()` - 检查分类编码是否存在
- `getCategoryPath()` - 获取分类路径

### 数据表结构
```sql
system_category (
  id BIGINT PRIMARY KEY,
  parent_id BIGINT,
  code VARCHAR(100) NOT NULL,
  name VARCHAR(200) NOT NULL,
  sort INT DEFAULT 0,
  level INT DEFAULT 1,
  tree_path VARCHAR(500),
  business_type VARCHAR(50),
  create_time DATETIME,
  update_time DATETIME
)
```

## 4. 字段分类关联API

**路径**：`@/api/system/fieldCategory/index.ts`
**功能**：管理字段与分类的关联关系，为不同的category设置所需的字段
**数据库表**：`field_category_rel`

### 主要接口
- `getFieldsByCategoryId()` - 根据分类ID获取字段列表
- `getCategoryIdsByFieldId()` - 根据字段ID获取分类ID列表
- `linkFieldCategory()` - 关联字段到分类
- `unlinkFieldCategory()` - 解除字段与分类的关联
- `batchCreateFieldCategoryRels()` - 批量创建字段分类关联
- `replaceFieldCategoryRels()` - 替换字段的所有分类关联
- `batchUpdateFieldSortInCategory()` - 批量更新字段在分类中的排序

### 功能特点
- 目的：为不同的category设置所需的字段
- 特点：为特定业务分类配置所需的字段
- 支持排序和批量操作

### 数据表结构
```sql
field_category_rel (
  id BIGINT PRIMARY KEY,
  field_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  sort INT DEFAULT 0,
  create_time DATETIME,
  UNIQUE KEY uk_field_category (field_id, category_id)
)
```

## 功能区分对比

| 功能模块 | 目的 | 数据库表 | 特点 |
|---------|------|----------|------|
| 字段定义 | 管理字段的基本信息 | `system_field` | 字段的元数据管理 |
| 字段分组 | 用户快速找到需要用的字段 | `field_hierarchy_rel` | 便于查找和管理 |
| 分类 | 管理业务分类树结构 | `system_category` | 通用的分类树管理 |
| 字段分类关联 | 为不同的category设置所需的字段 | `field_category_rel` | 为特定业务分类配置字段 |

## API接口调用正确性验证

### 字段分组页面
- ✅ 分组树：`/system/field-hierarchy/tree` - 正确
- ✅ 分组列表：`/system/field-hierarchy/list-by-parent` - 正确
- ✅ 分组CRUD：`/system/field-hierarchy/create/update/delete/get` - 正确
- ✅ 分组关联：`/system/field-hierarchy-rel/*` - 正确

### 分类页面
- ✅ 分类树：`/system/category/tree` - 正确
- ✅ 分类列表：`/system/category/list-by-parent` - 正确
- ✅ 分类CRUD：`/system/category/create/update/delete/get` - 正确

### 字段分类关联页面
- ✅ 分类关联：`/system/field-category/*` - 正确

## 目录结构

```
tunnel-management-ui/src/api/system/
├── field/
│   └── index.ts          # 字段定义API
├── fieldHierarchy/
│   └── index.ts          # 字段分组API
├── category/
│   └── index.ts          # 分类API
└── fieldCategory/
    └── index.ts          # 字段分类关联API
```

## 使用原则

1. **功能分离**：每个功能模块有独立的API文件
2. **职责清晰**：每个API文件只负责对应的业务功能
3. **命名规范**：API路径与功能模块对应
4. **数据一致性**：确保前后端数据模型一致

## 注意事项

1. 字段分组和字段分类是两个不同的功能，不要混淆
2. 字段分组用于快速查找，字段分类用于业务配置
3. 所有API接口都遵循RESTful规范
4. 批量操作接口提高性能，减少网络请求
5. 排序功能支持拖拽排序和手动排序 