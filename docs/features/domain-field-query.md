# Domain-Field 查询功能

## 概述

提供了查看指定Domain下Field列表和指定Field下Domain列表的查询功能，支持详细信息的联合查询，方便用户了解领域和字段的关联关系。

## 🔍 **功能特性**

### 1. **查询类型**
- ✅ 查看指定领域下的字段列表（带字段详情）
- ✅ 查看指定字段关联的领域列表（带领域详情）
- ✅ 支持JOIN查询，一次获取完整信息
- ✅ 自动排序，领域按字段排序，字段按领域排序

### 2. **数据内容**
每个查询结果包含：
- 关联关系信息（ID、是否必填、排序、备注、创建时间）
- 领域详情（ID、名称、编码）
- 字段详情（ID、名称、编码、类型）

## 🎯 **业务场景**

### 场景1：查看领域包含的字段
```
需求：查看"建筑领域"包含哪些字段
操作：GET /system/domain/fields?domainId=1

返回结果：
[
  {
    "id": 1,
    "domainId": 1,
    "domainName": "建筑领域",
    "domainCode": "architecture",
    "fieldId": 1,
    "fieldName": "建筑高度",
    "fieldCode": "building_height",
    "fieldType": "number",
    "required": true,
    "sort": 1,
    "remark": "建筑物的高度信息",
    "createTime": "2024-01-01T00:00:00"
  },
  {
    "id": 2,
    "domainId": 1,
    "domainName": "建筑领域", 
    "domainCode": "architecture",
    "fieldId": 2,
    "fieldName": "建筑面积",
    "fieldCode": "building_area",
    "fieldType": "number",
    "required": true,
    "sort": 2,
    "remark": "建筑物的面积信息",
    "createTime": "2024-01-01T00:00:00"
  }
]
```

### 场景2：查看字段被哪些领域使用
```
需求：查看"材料类型"字段被哪些领域使用
操作：GET /system/domain/domains-by-field?fieldId=3

返回结果：
[
  {
    "id": 3,
    "domainId": 1,
    "domainName": "建筑领域",
    "domainCode": "architecture", 
    "fieldId": 3,
    "fieldName": "材料类型",
    "fieldCode": "material_type",
    "fieldType": "enum",
    "required": false,
    "sort": 3,
    "remark": "建筑使用的材料类型",
    "createTime": "2024-01-01T00:00:00"
  },
  {
    "id": 4,
    "domainId": 2,
    "domainName": "设备领域",
    "domainCode": "equipment",
    "fieldId": 3,
    "fieldName": "材料类型",
    "fieldCode": "material_type", 
    "fieldType": "enum",
    "required": true,
    "sort": 2,
    "remark": "设备制造的材料类型",
    "createTime": "2024-01-01T00:00:00"
  }
]
```

## 🔧 **API接口**

### 1. 查看指定领域下的字段列表

```http
GET /system/domain/fields?domainId={domainId}
```

**参数：**
- `domainId` (Long, 必填)：领域ID

**响应：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "domainId": 1,
      "domainName": "建筑领域",
      "domainCode": "architecture",
      "fieldId": 1,
      "fieldName": "建筑高度",
      "fieldCode": "building_height",
      "fieldType": "number",
      "required": true,
      "sort": 1,
      "remark": "备注信息",
      "createTime": "2024-01-01T00:00:00"
    }
  ],
  "msg": "操作成功"
}
```

### 2. 查看指定字段关联的领域列表

```http
GET /system/domain/domains-by-field?fieldId={fieldId}
```

**参数：**
- `fieldId` (Long, 必填)：字段ID

**响应：**
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "domainId": 1,
      "domainName": "建筑领域",
      "domainCode": "architecture",
      "fieldId": 1,
      "fieldName": "建筑高度",
      "fieldCode": "building_height",
      "fieldType": "number",
      "required": true,
      "sort": 1,
      "remark": "备注信息",
      "createTime": "2024-01-01T00:00:00"
    }
  ],
  "msg": "操作成功"
}
```

## 📊 **数据字段说明**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 关联关系ID |
| domainId | Long | 领域ID |
| domainName | String | 领域名称 |
| domainCode | String | 领域编码 |
| fieldId | Long | 字段ID |
| fieldName | String | 字段名称 |
| fieldCode | String | 字段编码 |
| fieldType | String | 字段类型（string/number/date/enum等） |
| required | Boolean | 在该领域中是否必填 |
| sort | Integer | 在该领域中的排序 |
| remark | String | 备注信息 |
| createTime | LocalDateTime | 关联创建时间 |

## 🎨 **前端集成示例**

### Vue 3 示例

```vue
<template>
  <div class="domain-field-query">
    <!-- 查看领域的字段 -->
    <el-card title="领域字段列表">
      <el-select v-model="selectedDomainId" placeholder="选择领域" @change="loadDomainFields">
        <el-option 
          v-for="domain in domains" 
          :key="domain.id" 
          :label="domain.name" 
          :value="domain.id"
        />
      </el-select>
      
      <el-table :data="domainFields" style="margin-top: 20px">
        <el-table-column prop="fieldName" label="字段名称" />
        <el-table-column prop="fieldCode" label="字段编码" />
        <el-table-column prop="fieldType" label="字段类型" />
        <el-table-column prop="required" label="是否必填">
          <template #default="{ row }">
            <el-tag :type="row.required ? 'danger' : 'info'">
              {{ row.required ? '必填' : '选填' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </el-card>

    <!-- 查看字段的领域 -->
    <el-card title="字段领域列表" style="margin-top: 20px">
      <el-select v-model="selectedFieldId" placeholder="选择字段" @change="loadFieldDomains">
        <el-option 
          v-for="field in fields" 
          :key="field.id" 
          :label="field.name" 
          :value="field.id"
        />
      </el-select>
      
      <el-table :data="fieldDomains" style="margin-top: 20px">
        <el-table-column prop="domainName" label="领域名称" />
        <el-table-column prop="domainCode" label="领域编码" />
        <el-table-column prop="required" label="是否必填">
          <template #default="{ row }">
            <el-tag :type="row.required ? 'danger' : 'info'">
              {{ row.required ? '必填' : '选填' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DomainApi } from '@/api/system/domain'

const selectedDomainId = ref(null)
const selectedFieldId = ref(null)
const domains = ref([])
const fields = ref([])
const domainFields = ref([])
const fieldDomains = ref([])

// 加载领域的字段列表
const loadDomainFields = async () => {
  if (!selectedDomainId.value) return
  
  try {
    const response = await DomainApi.getFieldsByDomainId(selectedDomainId.value)
    domainFields.value = response.data
  } catch (error) {
    ElMessage.error('加载字段列表失败')
  }
}

// 加载字段的领域列表
const loadFieldDomains = async () => {
  if (!selectedFieldId.value) return
  
  try {
    const response = await DomainApi.getDomainsByFieldId(selectedFieldId.value)
    fieldDomains.value = response.data
  } catch (error) {
    ElMessage.error('加载领域列表失败')
  }
}

// 初始化数据
onMounted(async () => {
  // 加载领域列表和字段列表
  // domains.value = await DomainApi.getDomainList()
  // fields.value = await FieldApi.getFieldList()
})
</script>
```

### React 示例

```jsx
import React, { useState, useEffect } from 'react'
import { Select, Table, Tag, Card, message } from 'antd'
import { DomainApi } from '@/api/system/domain'

const DomainFieldQuery = () => {
  const [selectedDomainId, setSelectedDomainId] = useState(null)
  const [selectedFieldId, setSelectedFieldId] = useState(null)
  const [domains, setDomains] = useState([])
  const [fields, setFields] = useState([])
  const [domainFields, setDomainFields] = useState([])
  const [fieldDomains, setFieldDomains] = useState([])

  // 加载领域的字段列表
  const loadDomainFields = async (domainId) => {
    try {
      const response = await DomainApi.getFieldsByDomainId(domainId)
      setDomainFields(response.data)
    } catch (error) {
      message.error('加载字段列表失败')
    }
  }

  // 加载字段的领域列表
  const loadFieldDomains = async (fieldId) => {
    try {
      const response = await DomainApi.getDomainsByFieldId(fieldId)
      setFieldDomains(response.data)
    } catch (error) {
      message.error('加载领域列表失败')
    }
  }

  const domainFieldColumns = [
    { title: '字段名称', dataIndex: 'fieldName', key: 'fieldName' },
    { title: '字段编码', dataIndex: 'fieldCode', key: 'fieldCode' },
    { title: '字段类型', dataIndex: 'fieldType', key: 'fieldType' },
    {
      title: '是否必填',
      dataIndex: 'required',
      key: 'required',
      render: (required) => (
        <Tag color={required ? 'red' : 'default'}>
          {required ? '必填' : '选填'}
        </Tag>
      ),
    },
    { title: '排序', dataIndex: 'sort', key: 'sort' },
    { title: '备注', dataIndex: 'remark', key: 'remark' },
  ]

  const fieldDomainColumns = [
    { title: '领域名称', dataIndex: 'domainName', key: 'domainName' },
    { title: '领域编码', dataIndex: 'domainCode', key: 'domainCode' },
    {
      title: '是否必填',
      dataIndex: 'required',
      key: 'required',
      render: (required) => (
        <Tag color={required ? 'red' : 'default'}>
          {required ? '必填' : '选填'}
        </Tag>
      ),
    },
    { title: '排序', dataIndex: 'sort', key: 'sort' },
    { title: '备注', dataIndex: 'remark', key: 'remark' },
  ]

  return (
    <div className="domain-field-query">
      <Card title="领域字段列表">
        <Select
          placeholder="选择领域"
          style={{ width: 200, marginBottom: 20 }}
          onChange={(value) => {
            setSelectedDomainId(value)
            loadDomainFields(value)
          }}
        >
          {domains.map(domain => (
            <Select.Option key={domain.id} value={domain.id}>
              {domain.name}
            </Select.Option>
          ))}
        </Select>
        
        <Table
          dataSource={domainFields}
          columns={domainFieldColumns}
          rowKey="id"
          pagination={false}
        />
      </Card>

      <Card title="字段领域列表" style={{ marginTop: 20 }}>
        <Select
          placeholder="选择字段"
          style={{ width: 200, marginBottom: 20 }}
          onChange={(value) => {
            setSelectedFieldId(value)
            loadFieldDomains(value)
          }}
        >
          {fields.map(field => (
            <Select.Option key={field.id} value={field.id}>
              {field.name}
            </Select.Option>
          ))}
        </Select>
        
        <Table
          dataSource={fieldDomains}
          columns={fieldDomainColumns}
          rowKey="id"
          pagination={false}
        />
      </Card>
    </div>
  )
}

export default DomainFieldQuery
```

## 🧪 **测试用例**

### 测试1：查看领域字段
```http
# 1. 查看领域1的字段列表
GET /system/domain/fields?domainId=1
# 预期：返回该领域下的所有字段及详情

# 2. 查看不存在的领域
GET /system/domain/fields?domainId=999
# 预期：返回错误"领域模型不存在"

# 3. 查看空领域（无字段）
GET /system/domain/fields?domainId=3
# 预期：返回空数组[]
```

### 测试2：查看字段领域
```http
# 1. 查看字段1的领域列表
GET /system/domain/domains-by-field?fieldId=1
# 预期：返回该字段关联的所有领域及详情

# 2. 查看不存在的字段
GET /system/domain/domains-by-field?fieldId=999
# 预期：返回空数组[]（字段不存在但不报错）

# 3. 查看孤立字段（无关联）
GET /system/domain/domains-by-field?fieldId=5
# 预期：返回空数组[]
```

## 💡 **高级特性**

### 1. **智能排序**
- 领域字段列表：按字段在领域中的sort排序
- 字段领域列表：按领域的sort和创建时间排序

### 2. **JOIN查询优化**
- 一次查询获取完整信息，避免N+1查询问题
- 支持LEFT JOIN确保数据完整性

### 3. **扩展性**
- 可以添加更多筛选条件
- 可以支持分页查询
- 可以添加搜索功能

## ⚠️ **注意事项**

1. **权限控制**：需要`system:domain:query`权限
2. **数据完整性**：使用LEFT JOIN确保即使关联数据缺失也能正常返回
3. **性能考虑**：大量数据时考虑添加分页功能
4. **缓存策略**：频繁查询的数据可以考虑缓存

## 📋 **菜单配置参数**

为查询功能页面创建菜单：

```json
{
  "name": "领域字段查询",
  "type": 2,
  "sort": 4020,
  "parentId": 4000,
  "path": "domain-field-query",
  "icon": "ep:search",
  "component": "system/domain/query/index",
  "componentName": "SystemDomainFieldQuery",
  "permission": "system:domain:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

通过这些查询功能，用户可以方便地了解Domain和Field之间的关联关系，为业务分析和数据管理提供便利！ 