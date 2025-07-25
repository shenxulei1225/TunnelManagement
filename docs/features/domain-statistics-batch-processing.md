# Domain 统计与批量处理功能

## 概述

实现了Domain关联Field数量的递归统计功能和批量处理功能，提供了高效的数据统计和批量操作能力。

## 🔢 **统计功能**

### 1. **统计类型**
- ✅ **直接字段统计**：统计当前领域直接关联的字段数量
- ✅ **直接子领域统计**：统计当前领域的直接子领域数量
- ✅ **递归字段统计**：统计当前领域及所有子领域的字段总数
- ✅ **递归子领域统计**：统计当前领域的所有后代领域总数
- ✅ **层级深度统计**：计算当前领域在树形结构中的深度

### 2. **统计数据结构**

```json
{
  "domainId": 1,
  "domainName": "建筑领域",
  "domainCode": "architecture",
  "directFieldCount": 5,
  "directChildCount": 3,
  "totalFieldCount": 15,
  "totalChildCount": 8,
  "depth": 2
}
```

## 🎯 **业务场景**

### 场景1：领域规模评估
```
需求：评估"建筑领域"的复杂度和规模
操作：GET /system/domain/statistics?domainId=1

结果解读：
- 直接字段数量：5个（领域本身的复杂度）
- 总字段数量：15个（包含所有子领域的总复杂度）
- 直接子领域：3个（直接管理的子域数量）
- 总子领域：8个（管理范围的总体积）
- 层级深度：2（在树形结构中的位置）
```

### 场景2：数据迁移规划
```
需求：批量删除多个废弃的领域
操作：POST /system/domain/delete/batch

优势：
- 自动处理递归删除所有子领域
- 批量清理关联的字段关系
- 事务保证数据一致性
- 性能优化避免N+1问题
```

## 🆕 **增强功能**

### 1. **Domain Tree携带统计信息**
- ✅ 新增 `/system/domain/list-with-statistics` 接口
- ✅ 在获取Domain列表时自动携带统计信息
- ✅ 批量查询优化，避免N+1性能问题
- ✅ 前端可直接显示统计数据，无需额外请求

### 2. **重复关联容错处理**
- ✅ 重复添加Field到Domain关联时不再报错
- ✅ 自动更新已有关联的属性（required、sort、remark）
- ✅ 移动Field时自动处理目标Domain的重复关联
- ✅ 提升用户体验，减少操作错误

## 🔧 **API接口详解**

### 1. Domain列表（带统计信息）

```http
GET /system/domain/list-with-statistics?parentId={parentId}
```

**功能**：获取Domain树形列表，同时携带每个Domain的统计信息

**响应示例**：
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "name": "建筑领域",
      "code": "architecture",
      "parentId": 0,
      "description": "建筑相关的业务领域",
      "type": "business",
      "status": 1,
      "sort": 1,
      "remark": "备注信息",
      "createTime": "2024-01-01T00:00:00",
      "directFieldCount": 5,
      "directChildCount": 3,
      "totalFieldCount": 15,
      "totalChildCount": 8,
      "depth": 1
    }
  ],
  "msg": "操作成功"
}
```

**性能优势**：
- 一次请求获取完整信息
- 批量查询统计数据
- 避免前端多次API调用

### 2. 单个领域统计

```http
GET /system/domain/statistics?domainId={domainId}
```

**功能**：获取指定领域的详细统计信息

**算法**：
1. 查询直接关联字段数量
2. 查询直接子领域数量
3. 递归获取所有后代领域ID
4. 批量统计所有后代领域的字段数量
5. 计算层级深度

**性能优化**：
- 使用批量查询避免N+1问题
- 递归算法优化，一次性获取所有后代

### 3. 批量领域统计

```http
POST /system/domain/statistics/batch
Content-Type: application/json

[1, 2, 3, 4, 5]
```

**功能**：批量获取多个领域的统计信息

**容错处理**：
- 某个领域统计失败不影响其他领域
- 日志记录失败原因
- 返回成功处理的统计结果

### 4. 批量删除领域

```http
POST /system/domain/delete/batch
Content-Type: application/json

[1, 2, 3]
```

**功能**：批量删除领域（包含所有子领域）

**处理逻辑**：
1. 校验所有领域存在性
2. 收集所有需要删除的领域ID（包含递归子领域）
3. 批量删除所有字段关联关系
4. 批量删除所有领域实体
5. 事务保证原子性

### 5. 批量清理字段关联

```http
POST /system/domain/field-relations/remove/batch
Content-Type: application/json

[1, 2, 3]
```

**功能**：批量删除指定字段的所有领域关联关系

**使用场景**：
- 字段废弃时清理关联
- 数据迁移时批量处理
- 系统维护时批量清理

## 💡 **核心算法解析**

### 1. **递归统计算法**

```java
// 获取所有后代领域ID（递归）
public List<Long> selectAllDescendantIds(Long parentId) {
    List<Long> result = new ArrayList<>();
    collectDescendantIds(parentId, result);
    return result;
}

// 递归收集后代ID
private void collectDescendantIds(Long parentId, List<Long> result) {
    List<DomainDO> children = selectByParentId(parentId);
    for (DomainDO child : children) {
        result.add(child.getId());
        collectDescendantIds(child.getId(), result); // 递归调用
    }
}
```

### 2. **批量统计优化**

```java
// 批量统计多个领域的字段数量
@Select("""
    SELECT domain_id, COUNT(*) as field_count 
    FROM system_domain_field_rel 
    WHERE domain_id IN (<foreach...>) AND deleted = 0
    GROUP BY domain_id
""")
List<Map<String, Object>> countFieldsByDomainIds(Collection<Long> domainIds);
```

### 3. **层级深度算法**

```java
private Integer calculateDomainDepth(Long domainId) {
    int depth = 0;
    Long currentId = domainId;
    
    while (currentId != null && !Objects.equals(currentId, PARENT_ID_ROOT)) {
        DomainDO domain = domainMapper.selectById(currentId);
        if (domain == null) break;
        depth++;
        currentId = domain.getParentId();
    }
    return depth;
}
```

## 🎨 **前端集成示例**

### Vue 3 统计展示

```vue
<template>
  <div class="domain-statistics">
    <el-card title="领域统计信息">
      <el-form :model="statisticsForm" label-width="120px">
        <el-form-item label="选择领域">
          <el-select v-model="selectedDomainId" @change="loadStatistics">
            <el-option 
              v-for="domain in domains" 
              :key="domain.id" 
              :label="domain.name" 
              :value="domain.id"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <div v-if="statistics" class="statistics-grid">
        <div class="stat-item">
          <div class="stat-number">{{ statistics.directFieldCount }}</div>
          <div class="stat-label">直接字段</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ statistics.totalFieldCount }}</div>
          <div class="stat-label">总字段数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ statistics.directChildCount }}</div>
          <div class="stat-label">直接子领域</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ statistics.totalChildCount }}</div>
          <div class="stat-label">总子领域数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ statistics.depth }}</div>
          <div class="stat-label">层级深度</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { DomainApi } from '@/api/system/domain'

const selectedDomainId = ref(null)
const statistics = ref(null)
const domains = ref([])

const loadStatistics = async () => {
  if (!selectedDomainId.value) return
  
  try {
    const response = await DomainApi.getDomainStatistics(selectedDomainId.value)
    statistics.value = response.data
  } catch (error) {
    ElMessage.error('加载统计信息失败')
  }
}
</script>

<style scoped>
.statistics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}
</style>
```

### React 批量操作示例

```jsx
import React, { useState } from 'react'
import { Button, Table, Modal, message } from 'antd'
import { DomainApi } from '@/api/system/domain'

const DomainBatchOperations = () => {
  const [selectedRowKeys, setSelectedRowKeys] = useState([])
  const [loading, setLoading] = useState(false)

  // 批量删除领域
  const handleBatchDelete = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning('请选择要删除的领域')
      return
    }

    Modal.confirm({
      title: '确认批量删除',
      content: `确定要删除选中的 ${selectedRowKeys.length} 个领域吗？这将同时删除所有子领域。`,
      onOk: async () => {
        setLoading(true)
        try {
          await DomainApi.deleteDomainsBatch(selectedRowKeys)
          message.success('批量删除成功')
          setSelectedRowKeys([])
          // 刷新数据
        } catch (error) {
          message.error('批量删除失败')
        } finally {
          setLoading(false)
        }
      },
    })
  }

  // 批量获取统计信息
  const handleBatchStatistics = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning('请选择要统计的领域')
      return
    }

    setLoading(true)
    try {
      const response = await DomainApi.getBatchDomainStatistics(selectedRowKeys)
      // 显示统计结果
      console.log('统计结果:', response.data)
      message.success('统计完成')
    } catch (error) {
      message.error('统计失败')
    } finally {
      setLoading(false)
    }
  }

  const rowSelection = {
    selectedRowKeys,
    onChange: setSelectedRowKeys,
  }

  return (
    <div>
      <div style={{ marginBottom: 16 }}>
        <Button 
          type="primary" 
          onClick={handleBatchStatistics}
          disabled={selectedRowKeys.length === 0}
          loading={loading}
        >
          批量统计
        </Button>
        <Button 
          danger 
          onClick={handleBatchDelete}
          disabled={selectedRowKeys.length === 0}
          loading={loading}
          style={{ marginLeft: 8 }}
        >
          批量删除
        </Button>
        <span style={{ marginLeft: 8 }}>
          已选择 {selectedRowKeys.length} 项
        </span>
      </div>

      <Table
        rowSelection={rowSelection}
        dataSource={domains}
        columns={columns}
        rowKey="id"
        loading={loading}
      />
    </div>
  )
}

export default DomainBatchOperations
```

## 🧪 **测试用例**

### 测试1：单个领域统计
```http
# 测试正常情况
GET /system/domain/statistics?domainId=1
# 预期：返回完整统计信息

# 测试不存在的领域
GET /system/domain/statistics?domainId=999
# 预期：返回错误"领域模型不存在"

# 测试根领域
GET /system/domain/statistics?domainId=0
# 预期：返回根领域统计
```

### 测试2：批量统计
```http
# 测试多个领域
POST /system/domain/statistics/batch
Content-Type: application/json
[1, 2, 3]

# 测试部分领域不存在
POST /system/domain/statistics/batch
Content-Type: application/json
[1, 999, 3]
# 预期：返回存在领域的统计，忽略不存在的
```

### 测试3：批量删除
```http
# 测试正常删除
POST /system/domain/delete/batch
Content-Type: application/json
[4, 5]

# 测试删除包含子领域的领域
POST /system/domain/delete/batch
Content-Type: application/json
[1]
# 预期：删除领域1及其所有子领域
```

## ⚠️ **注意事项**

### 1. **性能考虑**
- 递归统计在深层级结构时可能较慢
- 批量操作建议限制数量（如最多100个）
- 大量数据时考虑异步处理

### 2. **数据一致性**
- 所有批量操作都使用事务保证原子性
- 删除操作不可逆，需要确认机制
- 统计数据实时计算，保证准确性

### 3. **权限控制**
- 统计功能需要`system:domain:query`权限
- 批量删除需要`system:domain:delete`权限
- 批量操作建议增加额外权限验证

## 📋 **菜单配置参数**

为统计功能页面创建菜单：

```json
{
  "name": "领域统计分析",
  "type": 2,
  "sort": 4030,
  "parentId": 4000,
  "path": "domain-statistics",
  "icon": "ep:data-analysis",
  "component": "system/domain/statistics/index",
  "componentName": "SystemDomainStatistics",
  "permission": "system:domain:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

为批量操作功能创建菜单：

```json
{
  "name": "领域批量管理",
  "type": 2,
  "sort": 4040,
  "parentId": 4000,
  "path": "domain-batch",
  "icon": "ep:operation",
  "component": "system/domain/batch/index",
  "componentName": "SystemDomainBatch",
  "permission": "system:domain:update",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

通过这些统计和批量处理功能，可以高效地管理Domain-Field关系，提供强大的数据分析和批量操作能力！ 