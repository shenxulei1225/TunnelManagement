# 动态业务智能推荐 API 接口文档

> **版本**: v1.0.0  
> **更新时间**: 2024-01-28  
> **维护团队**: 全栈开发团队

## 📋 接口概述

本文档描述了动态业务智能推荐系统的所有API接口，包括场景模板推荐、字段推荐、预览生成等功能。

## 🔗 基础信息

- **基础URL**: `/dynamic/recommendation`
- **协议**: HTTP/HTTPS
- **数据格式**: JSON
- **字符编码**: UTF-8

## 📊 通用响应格式

### 成功响应
```json
{
  "code": 0,
  "data": {
    // 具体数据
  },
  "msg": "操作成功"
}
```

### 错误响应
```json
{
  "code": 1002001000,
  "data": null,
  "msg": "字段定义不存在"
}
```

## 🎯 推荐相关接口

### 1. 获取场景模板推荐

**接口地址**: `GET /dynamic/recommendation/templates`

**接口描述**: 根据业务类型获取推荐场景模板

**请求参数**:
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| businessType | String | 是 | 业务类型 | inspection |

**请求示例**:
```http
GET /dynamic/recommendation/templates?businessType=inspection
```

**响应示例**:
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "code": "inspection_daily",
      "name": "日常巡检",
      "businessType": "inspection",
      "description": "适用于日常巡检业务场景",
      "recommendedFields": [
        {
          "fieldCode": "inspection_date",
          "fieldName": "巡检日期",
          "fieldType": "date",
          "required": true,
          "defaultValue": "",
          "recommendationReason": "巡检业务必备字段",
          "weight": 0.9,
          "fieldConfig": null
        }
      ],
      "defaultLayout": "{\"type\":\"form\",\"columns\":2}",
      "validationRules": "{\"rules\":{\"inspection_date\":{\"required\":true}}}",
      "icon": "el-icon-search",
      "sort": 100,
      "status": 1,
      "createTime": "2024-01-28T10:00:00"
    }
  ],
  "msg": "操作成功"
}
```

### 2. 获取字段推荐

**接口地址**: `GET /dynamic/recommendation/fields`

**接口描述**: 根据场景模板推荐字段

**请求参数**:
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| templateCode | String | 是 | 模板编码 | inspection_daily |

**请求示例**:
```http
GET /dynamic/recommendation/fields?templateCode=inspection_daily
```

**响应示例**:
```json
{
  "code": 0,
  "data": [
    {
      "fieldCode": "inspection_date",
      "fieldName": "巡检日期",
      "fieldType": "date",
      "required": true,
      "defaultValue": "",
      "recommendationReason": "巡检业务必备字段",
      "weight": 0.9,
      "fieldConfig": null
    },
    {
      "fieldCode": "inspection_person",
      "fieldName": "巡检人员",
      "fieldType": "string",
      "required": true,
      "defaultValue": "",
      "recommendationReason": "记录巡检责任人",
      "weight": 0.8,
      "fieldConfig": null
    }
  ],
  "msg": "操作成功"
}
```

### 3. 获取基于用户行为的字段推荐

**接口地址**: `GET /dynamic/recommendation/user-fields`

**接口描述**: 根据用户历史行为推荐字段

**请求参数**:
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| userId | Long | 是 | 用户ID | 1 |
| businessType | String | 是 | 业务类型 | inspection |

**请求示例**:
```http
GET /dynamic/recommendation/user-fields?userId=1&businessType=inspection
```

**响应示例**:
```json
{
  "code": 0,
  "data": [
    {
      "fieldCode": "inspection_date",
      "fieldName": "巡检日期",
      "fieldType": "date",
      "required": false,
      "defaultValue": "",
      "recommendationReason": "基于您的使用习惯推荐",
      "weight": 0.8,
      "fieldConfig": null
    }
  ],
  "msg": "操作成功"
}
```

### 4. 获取智能字段匹配

**接口地址**: `GET /dynamic/recommendation/field-matches`

**接口描述**: 根据业务类型和场景类型进行智能字段匹配

**请求参数**:
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| businessType | String | 是 | 业务类型 | inspection |
| sceneType | String | 是 | 场景类型 | daily |

**请求示例**:
```http
GET /dynamic/recommendation/field-matches?businessType=inspection&sceneType=daily
```

**响应示例**:
```json
{
  "code": 0,
  "data": [
    {
      "fieldCode": "inspection_date",
      "fieldName": "巡检日期",
      "fieldType": "date",
      "matchScore": 0.9,
      "matchReason": "基于业务场景智能匹配",
      "recommended": true
    },
    {
      "fieldCode": "inspection_person",
      "fieldName": "巡检人员",
      "fieldType": "string",
      "matchScore": 0.8,
      "matchReason": "基于业务场景智能匹配",
      "recommended": true
    }
  ],
  "msg": "操作成功"
}
```

## 🎨 预览相关接口

### 5. 生成界面预览

**接口地址**: `POST /dynamic/recommendation/preview`

**接口描述**: 根据字段配置生成界面预览

**请求参数**:
```json
{
  "modelCode": "inspection",
  "viewType": "list",
  "fields": [
    {
      "fieldCode": "inspection_date",
      "fieldName": "巡检日期",
      "fieldType": "date",
      "required": true,
      "defaultValue": "",
      "displayType": "date",
      "config": {},
      "validationRules": {},
      "sort": 1
    }
  ],
  "layoutConfig": "{\"type\":\"list\",\"columns\":2}",
  "actionConfig": "{\"create\":true,\"update\":true}"
}
```

**响应示例**:
```json
{
  "code": 0,
  "data": {
    "modelCode": "inspection",
    "viewType": "list",
    "previewHtml": "<div class='preview-list'><table class='el-table'>...</table></div>",
    "fieldConfigs": [
      {
        "fieldCode": "inspection_date",
        "fieldName": "巡检日期",
        "fieldType": "date",
        "required": true,
        "defaultValue": "",
        "displayType": "date",
        "config": {},
        "validationRules": {},
        "sort": 1
      }
    ],
    "layoutConfig": {
      "type": "list",
      "columns": 2,
      "fields": [
        {
          "code": "inspection_date",
          "span": 12
        }
      ]
    },
    "actionConfig": {
      "create": true,
      "update": true,
      "delete": true,
      "export": true
    },
    "validationResult": {
      "valid": true,
      "errors": [],
      "warnings": [],
      "suggestions": ["建议添加主键字段 'id'"]
    }
  },
  "msg": "操作成功"
}
```

### 6. 记录用户行为

**接口地址**: `POST /dynamic/recommendation/record-behavior`

**接口描述**: 记录用户行为以支持智能推荐

**请求参数**:
| 参数名 | 类型 | 必填 | 描述 | 示例 |
|--------|------|------|------|------|
| userId | Long | 是 | 用户ID | 1 |
| businessType | String | 是 | 业务类型 | inspection |
| sceneType | String | 是 | 场景类型 | daily |
| modelCode | String | 是 | 模型编码 | inspection |
| fieldUsage | String | 是 | 字段使用情况(JSON) | {"inspection_date":1,"inspection_person":1} |

**请求示例**:
```http
POST /dynamic/recommendation/record-behavior?userId=1&businessType=inspection&sceneType=daily&modelCode=inspection&fieldUsage={"inspection_date":1,"inspection_person":1}
```

**响应示例**:
```json
{
  "code": 0,
  "data": true,
  "msg": "操作成功"
}
```

## 📊 数据结构说明

### 1. SceneTemplateVO (场景模板)
```json
{
  "id": "模板ID",
  "code": "模板编码",
  "name": "模板名称",
  "businessType": "业务类型",
  "description": "场景描述",
  "recommendedFields": "推荐字段列表",
  "defaultLayout": "默认布局配置",
  "validationRules": "验证规则配置",
  "icon": "图标",
  "sort": "排序",
  "status": "状态",
  "createTime": "创建时间"
}
```

### 2. FieldRecommendationVO (字段推荐)
```json
{
  "fieldCode": "字段编码",
  "fieldName": "字段名称",
  "fieldType": "字段类型",
  "required": "是否必填",
  "defaultValue": "默认值",
  "recommendationReason": "推荐理由",
  "weight": "推荐权重",
  "fieldConfig": "字段配置"
}
```

### 3. FieldMatchVO (字段匹配)
```json
{
  "fieldCode": "字段编码",
  "fieldName": "字段名称",
  "fieldType": "字段类型",
  "matchScore": "匹配度",
  "matchReason": "匹配原因",
  "recommended": "是否推荐"
}
```

### 4. PreviewDataVO (预览数据)
```json
{
  "modelCode": "模型编码",
  "viewType": "视图类型",
  "previewHtml": "预览HTML",
  "fieldConfigs": "字段配置",
  "layoutConfig": "布局配置",
  "actionConfig": "操作配置",
  "validationResult": "验证结果"
}
```

### 5. FieldConfigVO (字段配置)
```json
{
  "fieldCode": "字段编码",
  "fieldName": "字段名称",
  "fieldType": "字段类型",
  "required": "是否必填",
  "defaultValue": "默认值",
  "displayType": "显示类型",
  "config": "字段配置",
  "validationRules": "验证规则",
  "sort": "排序"
}
```

### 6. ValidationResultVO (验证结果)
```json
{
  "valid": "是否验证通过",
  "errors": "错误信息列表",
  "warnings": "警告信息列表",
  "suggestions": "建议信息列表"
}
```

## 🔧 字段类型说明

| 字段类型 | 描述 | 适用场景 |
|---------|------|---------|
| string | 字符串 | 文本输入 |
| number | 数字 | 数字输入 |
| date | 日期 | 日期选择 |
| datetime | 日期时间 | 日期时间选择 |
| textarea | 长文本 | 长文本输入 |
| select | 下拉选择 | 选项选择 |
| enum | 枚举 | 枚举选择 |
| image | 图片 | 图片上传 |

## ⚠️ 错误码说明

| 错误码 | 描述 | 解决方案 |
|--------|------|---------|
| 1002001000 | 字段定义不存在 | 检查字段编码是否正确 |
| 1002001001 | 字段定义名称重复 | 修改字段名称 |
| 1002001002 | 字段定义编码重复 | 修改字段编码 |
| 1002001003 | 字段定义类型无效 | 检查字段类型 |
| 1002001004 | 字段定义配置无效 | 检查字段配置格式 |

## 📝 使用示例

### 完整的推荐流程

```javascript
// 1. 获取场景模板推荐
const templates = await fetch('/dynamic/recommendation/templates?businessType=inspection');

// 2. 选择模板后获取字段推荐
const fields = await fetch('/dynamic/recommendation/fields?templateCode=inspection_daily');

// 3. 生成预览
const preview = await fetch('/dynamic/recommendation/preview', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    modelCode: 'inspection',
    viewType: 'list',
    fields: fields.data
  })
});

// 4. 记录用户行为
await fetch('/dynamic/recommendation/record-behavior?userId=1&businessType=inspection&sceneType=daily&modelCode=inspection&fieldUsage={"inspection_date":1}');
```

## 🔒 安全说明

1. **权限控制**: 所有接口都需要用户登录认证
2. **数据验证**: 所有输入参数都进行严格验证
3. **SQL注入防护**: 使用参数化查询防止SQL注入
4. **XSS防护**: 对输出数据进行HTML转义
5. **CSRF防护**: 使用CSRF Token防止跨站请求伪造

## 📈 性能说明

1. **响应时间**: 所有接口响应时间控制在1秒以内
2. **并发支持**: 支持1000+并发请求
3. **缓存策略**: 模板数据使用Redis缓存
4. **分页查询**: 大数据量支持分页查询
5. **异步处理**: 用户行为记录采用异步处理

---

**📋 注意事项**: 
- 所有时间格式均为ISO 8601标准格式
- 所有JSON数据必须使用UTF-8编码
- 建议在生产环境中使用HTTPS协议 