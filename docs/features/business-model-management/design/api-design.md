# 业务模型管理 - API设计

## API 概述

业务模型管理模块提供完整的 CRUD 操作和权限控制功能。

### 基础信息

- **基础路径**: `/dynamic/model`
- **认证方式**: Bearer Token
- **数据格式**: JSON
- **字符编码**: UTF-8

## API 列表

### 1. 创建业务模型

**接口地址**: `POST /dynamic/model/create`

**权限要求**: `dynamic:model:create`

**请求参数**:
```json
{
  "name": "用户管理",
  "code": "user",
  "description": "用户管理模型",
  "tableName": "dynamic_user",
  "modelType": 1,
  "status": 1,
  "readonly": false
}
```

**响应示例**:
```json
{
  "code": 0,
  "data": 1,
  "msg": "操作成功"
}
```

### 2. 更新业务模型

**接口地址**: `PUT /dynamic/model/update`

**权限要求**: `dynamic:model:update`

**请求参数**:
```json
{
  "id": 1,
  "name": "用户管理",
  "code": "user",
  "description": "用户管理模型",
  "tableName": "dynamic_user",
  "modelType": 1,
  "status": 1,
  "readonly": false
}
```

**响应示例**:
```json
{
  "code": 0,
  "data": true,
  "msg": "操作成功"
}
```

### 3. 删除业务模型

**接口地址**: `DELETE /dynamic/model/delete`

**权限要求**: `dynamic:model:delete`

**请求参数**:
```
id=1
```

**响应示例**:
```json
{
  "code": 0,
  "data": true,
  "msg": "操作成功"
}
```

### 4. 获取业务模型详情

**接口地址**: `GET /dynamic/model/get`

**权限要求**: `dynamic:model:query`

**请求参数**:
```
id=1
```

**响应示例**:
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "name": "用户管理",
    "code": "user",
    "description": "用户管理模型",
    "tableName": "dynamic_user",
    "modelType": 1,
    "status": 1,
    "readonly": false,
    "directoryName": "系统管理",
    "createTime": "2025-07-03T10:00:00"
  },
  "msg": "操作成功"
}
```

### 5. 获取业务模型列表

**接口地址**: `GET /dynamic/model/list`

**权限要求**: `dynamic:model:query`

**请求参数**:
```
ids=1,2,3
```

**响应示例**:
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "name": "用户管理",
      "code": "user",
      "description": "用户管理模型",
      "tableName": "dynamic_user",
      "modelType": 1,
      "status": 1,
      "readonly": false,
      "directoryName": "系统管理",
      "createTime": "2025-07-03T10:00:00"
    }
  ],
  "msg": "操作成功"
}
```

### 6. 分页查询业务模型

**接口地址**: `GET /dynamic/model/page`

**权限要求**: `dynamic:model:query`

**请求参数**:
```
pageNo=1&pageSize=10&name=用户&status=1
```

**响应示例**:
```json
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "name": "用户管理",
        "code": "user",
        "description": "用户管理模型",
        "tableName": "dynamic_user",
        "modelType": 1,
        "status": 1,
        "readonly": false,
        "directoryName": "系统管理",
        "createTime": "2025-07-03T10:00:00"
      }
    ],
    "total": 1
  },
  "msg": "操作成功"
}
```

### 7. 按目录分页查询

**接口地址**: `GET /dynamic/model/page-by-directory`

**权限要求**: `dynamic:model:query`

**请求参数**:
```
pageNo=1&pageSize=10&name=用户&status=1&directoryId=1
```

**响应示例**:
```json
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "name": "用户管理",
        "code": "user",
        "description": "用户管理模型",
        "tableName": "dynamic_user",
        "modelType": 1,
        "status": 1,
        "readonly": false,
        "directoryName": "系统管理",
        "createTime": "2025-07-03T10:00:00"
      }
    ],
    "total": 1
  },
  "msg": "操作成功"
}
```

### 8. 批量更新排序

**接口地址**: `POST /dynamic/model/batch-update-sort`

**权限要求**: `dynamic:model:update`

**请求参数**:
```json
[
  {"id": 1, "sort": 1},
  {"id": 2, "sort": 2},
  {"id": 3, "sort": 3}
]
```

**响应示例**:
```json
{
  "code": 0,
  "data": true,
  "msg": "操作成功"
}
```

### 9. 获取目录树

**接口地址**: `GET /dynamic/model/directory-tree`

**权限要求**: `dynamic:model:query`

**响应示例**:
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "children": [
        {
          "id": 2,
          "name": "用户管理"
        }
      ]
    }
  ],
  "msg": "操作成功"
}
```

## 数据模型

### 请求模型

#### DynamicModelCreateReqVO
```json
{
  "name": "string",           // 模型名称，必填
  "code": "string",           // 模型编码，可选（自动生成）
  "description": "string",    // 描述，可选
  "tableName": "string",      // 表名，可选（自动生成）
  "modelType": "integer",     // 模型类型（0:系统，1:自定义），默认1
  "status": "integer",        // 状态（0:禁用，1:启用），默认0
  "readonly": "boolean"       // 是否只读，默认false
}
```

#### DynamicModelUpdateReqVO
```json
{
  "id": "long",              // 模型ID，必填
  "name": "string",          // 模型名称，必填
  "code": "string",          // 模型编码，可选
  "description": "string",   // 描述，可选
  "tableName": "string",     // 表名，可选
  "modelType": "integer",    // 模型类型，可选
  "status": "integer",       // 状态，可选
  "readonly": "boolean"      // 是否只读，可选
}
```

#### DynamicModelPageReqVO
```json
{
  "pageNo": "integer",       // 页码，默认1
  "pageSize": "integer",     // 每页大小，默认10
  "name": "string",          // 模型名称，模糊查询
  "status": "integer",       // 状态，精确查询
  "directoryId": "long"      // 目录ID，精确查询
}
```

### 响应模型

#### DynamicModelRespVO
```json
{
  "id": "long",              // 模型ID
  "name": "string",          // 模型名称
  "code": "string",          // 模型编码
  "description": "string",   // 描述
  "tableName": "string",     // 表名
  "modelType": "integer",    // 模型类型
  "status": "integer",       // 状态
  "readonly": "boolean",     // 是否只读
  "directoryName": "string", // 目录名称
  "createTime": "datetime"   // 创建时间
}
```

## 错误码

| 错误码 | 说明 |
|--------|------|
| 1002002000 | 业务模型不存在 |
| 1002002001 | 业务模型名称重复 |
| 1002002002 | 业务模型编码重复 |
| 1002002003 | 业务模型状态无效 |
| 1002002004 | 业务模型编码不能为空 |
| 1002002005 | 业务模型编码格式无效 |
| 1002002006 | 表名格式无效，必须以dynamic_开头 |
| 1002002007 | 只读的业务模型不允许删除 |

## 权限控制

### 权限矩阵

| 操作 | 权限标识 | 说明 |
|------|----------|------|
| 创建 | `dynamic:model:create` | 创建业务模型 |
| 更新 | `dynamic:model:update` | 更新业务模型 |
| 删除 | `dynamic:model:delete` | 删除业务模型 |
| 查询 | `dynamic:model:query` | 查询业务模型 |

### 权限检查逻辑

1. **接口级权限检查**
   - 通过 `@PreAuthorize` 注解检查用户权限

2. **业务级权限检查**
   - 删除时检查 `readonly` 字段
   - 更新时自动处理模型类型变更（自定义→系统时自动设为只读）

## 测试用例

### 创建测试

```bash
# 创建可编辑模型
curl -X POST "http://localhost:48080/dynamic/model/create" \
  -H "Authorization: Bearer {{token}}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试模型",
    "code": "test_model",
    "description": "这是一个测试模型",
    "status": 1,
    "modelType": 1,
    "readonly": false
  }'

# 创建只读模型
curl -X POST "http://localhost:48080/dynamic/model/create" \
  -H "Authorization: Bearer {{token}}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "只读模型",
    "code": "readonly_model",
    "description": "这是一个只读模型",
    "status": 0,
    "modelType": 1,
    "readonly": true
  }'
```

### 删除测试

```bash
# 删除可编辑模型（成功）
curl -X DELETE "http://localhost:48080/dynamic/model/delete?id=1" \
  -H "Authorization: Bearer {{token}}"

# 删除只读模型（失败）
curl -X DELETE "http://localhost:48080/dynamic/model/delete?id=2" \
  -H "Authorization: Bearer {{token}}"
```

## 性能优化

### 查询优化

1. **分页查询**
   - 使用 LIMIT 和 OFFSET 进行分页
   - 添加适当的索引

2. **条件查询**
   - 支持多条件组合查询
   - 使用索引优化查询性能

### 缓存策略

1. **模型信息缓存**
   - 缓存模型基本信息
   - 缓存模型配置信息

2. **权限缓存**
   - 缓存用户权限信息
   - 缓存模型权限信息

## 扩展性设计

### 未来扩展

1. **批量操作**
   - 批量创建模型
   - 批量更新状态
   - 批量删除模型

2. **导入导出**
   - 模型配置导入
   - 模型配置导出
   - 模型模板管理

3. **版本控制**
   - 模型版本管理
   - 模型变更历史
   - 模型回滚功能 