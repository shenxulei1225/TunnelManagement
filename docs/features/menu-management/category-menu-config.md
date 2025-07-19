# Category 菜单管理配置

## 新的分类设计理念

### 设计变更
1. **分类通用化**：分类不再绑定特定业务类型，而是通用的分类体系
2. **关联关系**：通过 `system_category_biz_type_rel` 表建立分类与具体业务的关联关系
3. **灵活复用**：同一个分类可以被多个业务类型使用

### 使用场景
- **设备分类**：可以同时用于设备管理、维护管理、巡检管理等业务
- **产品分类**：可以同时用于产品管理、库存管理、销售管理等业务
- **字段分类**：可以同时用于不同模块的字段管理

## 菜单创建参数

### 1. 分类管理主菜单
```json
{
  "name": "分类管理",
  "permission": "",
  "type": 2,
  "sort": 3,
  "parentId": 1,
  "path": "category",
  "icon": "ep:folder",
  "component": "system/category/index",
  "componentName": "Category",
  "status": 0,
  "visible": "1",
  "keepAlive": "1",
  "alwaysShow": "1"
}
```

### 2. 分类查询权限
```json
{
  "name": "分类查询",
  "permission": "system:category:query",
  "type": 3,
  "sort": 1,
  "parentId": [分类管理菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": null,
  "status": 0,
  "visible": "1",
  "keepAlive": "1",
  "alwaysShow": "1"
}
```

### 3. 分类创建权限
```json
{
  "name": "分类创建",
  "permission": "system:category:create",
  "type": 3,
  "sort": 2,
  "parentId": [分类管理菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": null,
  "status": 0,
  "visible": "1",
  "keepAlive": "1",
  "alwaysShow": "1"
}
```

### 4. 分类更新权限
```json
{
  "name": "分类更新",
  "permission": "system:category:update",
  "type": 3,
  "sort": 3,
  "parentId": [分类管理菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": null,
  "status": 0,
  "visible": "1",
  "keepAlive": "1",
  "alwaysShow": "1"
}
```

### 5. 分类删除权限
```json
{
  "name": "分类删除",
  "permission": "system:category:delete",
  "type": 3,
  "sort": 4,
  "parentId": [分类管理菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": null,
  "status": 0,
  "visible": "1",
  "keepAlive": "1",
  "alwaysShow": "1"
}
```

## API 接口说明

### 分类管理接口

#### 创建分类
- **URL**: `POST /system/category/create`
- **权限**: `system:category:create`
- **参数**: `CategoryCreateReqVO`

#### 更新分类
- **URL**: `PUT /system/category/update`
- **权限**: `system:category:update`
- **参数**: `CategoryUpdateReqVO`

#### 删除分类
- **URL**: `DELETE /system/category/delete`
- **权限**: `system:category:delete`
- **参数**: `id` (Long)

#### 获取分类详情
- **URL**: `GET /system/category/get`
- **权限**: `system:category:query`
- **参数**: `id` (Long)

#### 获取分类列表
- **URL**: `GET /system/category/list`
- **权限**: `system:category:query`
- **参数**: `ids` (List<Long>)

#### 获取分类树
- **URL**: `GET /system/category/tree`
- **权限**: `system:category:query`
- **参数**: 无

#### 根据父分类获取分类列表
- **URL**: `GET /system/category/list-by-parent`
- **权限**: `system:category:query`
- **参数**: `parentId` (Long, 可选)

### 分类业务类型关联关系接口

#### 创建分类业务类型关联关系
- **URL**: `POST /system/category-biz-type-rel/create`
- **权限**: `system:category:create`
- **参数**: `CategoryBizTypeRelCreateReqVO`

#### 删除分类业务类型关联关系
- **URL**: `DELETE /system/category-biz-type-rel/delete`
- **权限**: `system:category:delete`
- **参数**: `id` (Long)

#### 获取分类业务类型关联关系列表
- **URL**: `GET /system/category-biz-type-rel/list`
- **权限**: `system:category:query`
- **参数**: `businessType` (String), `businessId` (Long)

#### 根据业务类型获取分类树
- **URL**: `GET /system/category/tree-by-business`
- **权限**: `system:category:query`
- **参数**: `businessType` (String)

## 前端页面路径
- **列表页面**: `tunnel-management-ui/src/views/system/category/index.vue`
- **表单页面**: `tunnel-management-ui/src/views/system/category/CategoryForm.vue`
- **关联关系页面**: `tunnel-management-ui/src/views/system/category/CategoryBizTypeRelForm.vue`

## 数据库表结构

### system_category 表
- 通用分类表，不绑定特定业务
- 支持树形结构
- 支持多租户

### system_category_biz_type_rel 表
- 分类与业务类型的关联关系表
- 支持一个分类关联多个业务类型
- 支持关联类型和必填标记

## 注意事项
1. Category 支持多租户，每个租户的分类数据相互隔离
2. 系统只读分类禁止删除和修改
3. 删除分类时会同时删除其所有子分类
4. 分类支持树形结构，通过 `treePath` 和 `level` 字段维护层级关系
5. 通过关联关系表实现分类的灵活复用
6. 业务类型在关联关系层面定义，而不是在分类本身 