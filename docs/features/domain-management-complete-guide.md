# Domain 管理功能完整指南

## 📋 **功能概述**

Domain管理功能是一个全面的领域模型管理系统，支持树形结构、统计分析、批量处理等企业级功能。本文档整理了所有已实现的功能特性和技术细节。

## 🎯 **核心功能列表**

### 1. **基础CRUD操作**
- ✅ 创建领域模型
- ✅ 更新领域模型
- ✅ 删除领域模型（支持级联删除子领域）
- ✅ 查询领域模型（单个/列表/分页）
- ✅ Excel导入导出

### 2. **树形结构管理**
- ✅ 父子层级关系维护
- ✅ 树形数据展示
- ✅ 节点拖拽移动
- ✅ 层级深度计算
- ✅ 循环引用检测

### 3. **Domain-Field关联管理**
- ✅ 添加字段关联（支持重复关联容错）
- ✅ 移除字段关联
- ✅ 字段在领域间移动
- ✅ 关联属性配置（必填、排序、备注）
- ✅ 批量关联处理

### 4. **统计分析功能**
- ✅ 直接字段数量统计
- ✅ 递归字段数量统计（包含子领域）
- ✅ 直接子领域数量统计
- ✅ 递归子领域数量统计（所有后代）
- ✅ 层级深度统计
- ✅ 批量统计查询

### 5. **批量处理功能**
- ✅ 批量删除领域（递归删除子领域）
- ✅ 批量删除字段关联关系
- ✅ 批量统计信息获取
- ✅ 事务保证数据一致性

### 6. **前端集成支持**
- ✅ Element Plus Tree组件格式支持
- ✅ 列表格式数据（带统计信息）
- ✅ 树形格式数据（带统计信息）
- ✅ 拖拽排序支持
- ✅ 图标类型映射

## 🔧 **API接口完整列表**

### 基础CRUD接口

| 接口 | 方法 | 路径 | 功能 |
|------|------|------|------|
| 创建领域 | POST | `/system/domain/create` | 创建新的领域模型 |
| 更新领域 | PUT | `/system/domain/update` | 更新已有领域模型 |
| 删除领域 | DELETE | `/system/domain/delete` | 删除指定领域（检查子领域） |
| 获取领域详情 | GET | `/system/domain/get` | 获取单个领域详细信息 |
| 获取领域列表 | GET | `/system/domain/list` | 获取领域列表（基础格式） |
| 获取领域分页 | GET | `/system/domain/page` | 获取领域分页数据 |
| 导出Excel | GET | `/system/domain/export-excel` | 导出领域数据到Excel |

### 增强查询接口

| 接口 | 方法 | 路径 | 功能 |
|------|------|------|------|
| 列表带统计 | GET | `/system/domain/list-with-statistics` | 获取带统计信息的列表 |
| 树形结构 | GET | `/system/domain/tree` | 获取Element Plus Tree格式数据 |
| 指定领域字段 | GET | `/system/domain/fields` | 获取指定领域下的字段列表 |
| 指定字段领域 | GET | `/system/domain/domains-by-field` | 获取指定字段关联的领域 |

### 关联管理接口

| 接口 | 方法 | 路径 | 功能 |
|------|------|------|------|
| 添加字段关联 | POST | `/system/domain/field-rel/add` | 添加或更新领域字段关联 |
| 移除字段关联 | DELETE | `/system/domain/field-rel/remove` | 移除领域字段关联 |
| 获取关联列表 | GET | `/system/domain/field-rel/list` | 获取领域的字段关联列表 |
| 字段移动 | POST | `/system/domain/field-move` | 移动字段到其他领域 |

### 树形操作接口

| 接口 | 方法 | 路径 | 功能 |
|------|------|------|------|
| 拖拽移动 | POST | `/system/domain/move` | 拖拽移动领域节点 |
| 批量排序 | POST | `/system/domain/batch-sort` | 批量更新领域排序 |

### 统计分析接口

| 接口 | 方法 | 路径 | 功能 |
|------|------|------|------|
| 单个统计 | GET | `/system/domain/statistics` | 获取单个领域统计信息 |
| 批量统计 | POST | `/system/domain/statistics/batch` | 批量获取多个领域统计 |

### 批量处理接口

| 接口 | 方法 | 路径 | 功能 |
|------|------|------|------|
| 批量删除领域 | POST | `/system/domain/delete/batch` | 批量删除领域（递归） |
| 批量清理关联 | POST | `/system/domain/field-relations/remove/batch` | 批量删除字段关联 |

## 📊 **数据结构详解**

### 1. DomainDO（数据实体）

```java
public class DomainDO extends TenantBaseDO {
    private Long id;                    // 主键
    private String name;                // 领域名称
    private String code;                // 领域编码
    private Long parentId;              // 父领域ID
    private String description;         // 领域描述
    private String type;                // 领域类型
    private Integer status;             // 状态（0禁用 1启用）
    private Integer sort;               // 排序
    private String remark;              // 备注
}
```

### 2. DomainRespVO（基础响应）

```java
public class DomainRespVO {
    // 基础字段（同DomainDO）
    private Long id;
    private String name;
    // ... 其他字段
    
    // 统计信息字段
    private Integer directFieldCount;   // 直接字段数量
    private Integer directChildCount;   // 直接子领域数量
    private Integer totalFieldCount;    // 总字段数量（递归）
    private Integer totalChildCount;    // 总子领域数量（递归）
    private Integer depth;              // 层级深度
}
```

### 3. DomainTreeRespVO（树形响应）

```java
public class DomainTreeRespVO {
    // 基础字段（同DomainRespVO）
    // ...
    
    // Element Plus Tree 组件字段
    private String label;               // 树节点标签
    private String value;               // 树节点值
    private Boolean isLeaf;             // 是否叶子节点
    private Boolean disabled;           // 是否禁用
    private List<DomainTreeRespVO> children; // 子节点列表
    
    // 扩展字段
    private String icon;                // 图标
    private Boolean expanded;           // 是否展开
    private String nodeType;            // 节点类型
}
```

### 4. DomainStatisticsRespVO（统计响应）

```java
public class DomainStatisticsRespVO {
    private Long domainId;              // 领域ID
    private String domainName;          // 领域名称
    private String domainCode;          // 领域编码
    private Integer directFieldCount;   // 直接字段数量
    private Integer directChildCount;   // 直接子领域数量
    private Integer totalFieldCount;    // 总字段数量
    private Integer totalChildCount;    // 总子领域数量
    private Integer depth;              // 层级深度
}
```

### 5. DomainFieldRelDO（关联实体）

```java
public class DomainFieldRelDO extends TenantBaseDO {
    private Long id;                    // 主键
    private Long domainId;              // 领域ID
    private Long fieldId;               // 字段ID
    private Boolean required;           // 是否必填
    private Integer sort;               // 排序
    private String remark;              // 备注
}
```

## 🎨 **前端集成示例**

### 1. Element Plus Tree 组件使用

```vue
<template>
  <div class="domain-tree-container">
    <el-tree
      :data="domainTreeData"
      :props="treeProps"
      :default-expand-all="false"
      :expand-on-click-node="false"
      show-checkbox
      node-key="id"
      @node-click="handleNodeClick"
      @check="handleNodeCheck"
    >
      <template #default="{ node, data }">
        <div class="tree-node">
          <el-icon class="node-icon">
            <component :is="getIconComponent(data.icon)" />
          </el-icon>
          <span class="node-label">{{ data.label }}</span>
          <div class="node-statistics">
            <el-tag size="small" type="info">
              字段: {{ data.totalFieldCount }}
            </el-tag>
            <el-tag size="small" type="primary">
              子域: {{ data.totalChildCount }}
            </el-tag>
          </div>
        </div>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DomainApi } from '@/api/system/domain'

const domainTreeData = ref([])

const treeProps = {
  children: 'children',
  label: 'label',
  disabled: 'disabled',
  isLeaf: 'isLeaf'
}

const loadDomainTree = async () => {
  try {
    const response = await DomainApi.getDomainTree({})
    domainTreeData.value = response.data
  } catch (error) {
    ElMessage.error('加载领域树失败')
  }
}

const handleNodeClick = (data) => {
  console.log('点击节点:', data)
}

const handleNodeCheck = (data, checkedInfo) => {
  console.log('勾选节点:', data, checkedInfo)
}

const getIconComponent = (iconType) => {
  const iconMap = {
    'business': 'Business',
    'technical': 'Setting',
    'database': 'Database',
    'service': 'Service',
    'folder': 'Folder'
  }
  return iconMap[iconType] || 'Folder'
}

onMounted(() => {
  loadDomainTree()
})
</script>

<style scoped>
.domain-tree-container {
  padding: 20px;
}

.tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.node-icon {
  font-size: 16px;
  color: #409eff;
}

.node-label {
  flex: 1;
  font-size: 14px;
}

.node-statistics {
  display: flex;
  gap: 4px;
}
</style>
```

### 2. 统计仪表板展示

```vue
<template>
  <div class="domain-dashboard">
    <el-card title="领域统计概览">
      <el-form :model="queryForm" inline>
        <el-form-item label="选择领域">
          <el-select 
            v-model="selectedDomainId" 
            placeholder="选择要分析的领域"
            @change="loadDomainStatistics"
          >
            <el-option
              v-for="domain in domains"
              :key="domain.id"
              :label="domain.name"
              :value="domain.id"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <div v-if="statistics" class="statistics-panel">
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-value">{{ statistics.directFieldCount }}</div>
            <div class="stat-label">直接字段</div>
            <div class="stat-desc">当前领域直接关联的字段数量</div>
          </div>
          
          <div class="stat-card">
            <div class="stat-value">{{ statistics.totalFieldCount }}</div>
            <div class="stat-label">总字段数</div>
            <div class="stat-desc">包含所有子领域的字段总数</div>
          </div>
          
          <div class="stat-card">
            <div class="stat-value">{{ statistics.directChildCount }}</div>
            <div class="stat-label">直接子领域</div>
            <div class="stat-desc">当前领域的直接子领域数量</div>
          </div>
          
          <div class="stat-card">
            <div class="stat-value">{{ statistics.totalChildCount }}</div>
            <div class="stat-label">总子领域数</div>
            <div class="stat-desc">包含所有后代领域的总数</div>
          </div>
          
          <div class="stat-card">
            <div class="stat-value">{{ statistics.depth }}</div>
            <div class="stat-label">层级深度</div>
            <div class="stat-desc">在树形结构中的深度</div>
          </div>
        </div>

        <!-- 字段分布图表 -->
        <div class="chart-container">
          <el-card title="字段分布分析">
            <div ref="fieldChart" style="height: 300px;"></div>
          </el-card>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { DomainApi } from '@/api/system/domain'

const selectedDomainId = ref(null)
const statistics = ref(null)
const domains = ref([])
const fieldChart = ref(null)

const loadDomainStatistics = async () => {
  if (!selectedDomainId.value) return
  
  try {
    const response = await DomainApi.getDomainStatistics(selectedDomainId.value)
    statistics.value = response.data
    
    // 更新图表
    nextTick(() => {
      updateFieldChart()
    })
  } catch (error) {
    ElMessage.error('加载统计信息失败')
  }
}

const updateFieldChart = () => {
  if (!fieldChart.value || !statistics.value) return
  
  const chart = echarts.init(fieldChart.value)
  const option = {
    title: {
      text: '字段分布统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    series: [
      {
        name: '字段分布',
        type: 'pie',
        radius: '60%',
        data: [
          {
            value: statistics.value.directFieldCount,
            name: '当前领域字段'
          },
          {
            value: statistics.value.totalFieldCount - statistics.value.directFieldCount,
            name: '子领域字段'
          }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
}

onMounted(async () => {
  // 加载领域列表
  try {
    const response = await DomainApi.getDomainList({})
    domains.value = response.data
  } catch (error) {
    ElMessage.error('加载领域列表失败')
  }
})
</script>

<style scoped>
.domain-dashboard {
  padding: 20px;
}

.statistics-panel {
  margin-top: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 18px;
  margin-bottom: 4px;
}

.stat-desc {
  font-size: 12px;
  opacity: 0.9;
}

.chart-container {
  margin-top: 20px;
}
</style>
```

### 3. 批量操作管理界面

```vue
<template>
  <div class="domain-batch-management">
    <el-card title="领域批量管理">
      <!-- 操作工具栏 -->
      <div class="toolbar">
        <el-button 
          type="primary" 
          @click="loadDomainList"
          :loading="loading"
        >
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
        
        <el-button 
          type="success" 
          @click="batchGetStatistics"
          :disabled="selectedRowKeys.length === 0"
          :loading="loading"
        >
          <el-icon><DataAnalysis /></el-icon>
          批量统计
        </el-button>
        
        <el-button 
          type="danger" 
          @click="batchDeleteDomains"
          :disabled="selectedRowKeys.length === 0"
          :loading="loading"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
        
        <span class="selection-info">
          已选择 {{ selectedRowKeys.length }} 项
        </span>
      </div>

      <!-- 数据表格 -->
      <el-table
        :data="domainList"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="领域名称" min-width="150" />
        <el-table-column prop="code" label="领域编码" min-width="120" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ row.type || '未分类' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="directFieldCount" label="直接字段" width="100">
          <template #default="{ row }">
            <el-badge :value="row.directFieldCount || 0" type="info" />
          </template>
        </el-table-column>
        <el-table-column prop="totalFieldCount" label="总字段" width="100">
          <template #default="{ row }">
            <el-badge :value="row.totalFieldCount || 0" type="primary" />
          </template>
        </el-table-column>
        <el-table-column prop="directChildCount" label="子领域" width="100">
          <template #default="{ row }">
            <el-badge :value="row.directChildCount || 0" type="warning" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch 
              v-model="row.status" 
              :active-value="1" 
              :inactive-value="0"
              @change="updateDomainStatus(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewStatistics(row)">
              统计详情
            </el-button>
            <el-button type="warning" size="small" @click="manageDomainFields(row)">
              字段管理
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="queryParams.pageNo"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadDomainList"
          @current-change="loadDomainList"
        />
      </div>
    </el-card>

    <!-- 统计详情对话框 -->
    <el-dialog
      v-model="statisticsDialogVisible"
      title="统计详情"
      width="600px"
    >
      <div v-if="currentStatistics">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="领域名称">
            {{ currentStatistics.domainName }}
          </el-descriptions-item>
          <el-descriptions-item label="领域编码">
            {{ currentStatistics.domainCode }}
          </el-descriptions-item>
          <el-descriptions-item label="直接字段数量">
            <el-tag type="info">{{ currentStatistics.directFieldCount }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总字段数量">
            <el-tag type="primary">{{ currentStatistics.totalFieldCount }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="直接子领域数量">
            <el-tag type="warning">{{ currentStatistics.directChildCount }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总子领域数量">
            <el-tag type="danger">{{ currentStatistics.totalChildCount }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="层级深度">
            <el-tag>{{ currentStatistics.depth }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { DomainApi } from '@/api/system/domain'

const loading = ref(false)
const domainList = ref([])
const selectedRowKeys = ref([])
const total = ref(0)
const statisticsDialogVisible = ref(false)
const currentStatistics = ref(null)

const queryParams = ref({
  pageNo: 1,
  pageSize: 20
})

const loadDomainList = async () => {
  loading.value = true
  try {
    const response = await DomainApi.getDomainListWithStatistics(queryParams.value)
    domainList.value = response.data
    total.value = response.total || response.data.length
  } catch (error) {
    ElMessage.error('加载领域列表失败')
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (selection) => {
  selectedRowKeys.value = selection.map(row => row.id)
}

const batchGetStatistics = async () => {
  if (selectedRowKeys.value.length === 0) {
    ElMessage.warning('请选择要统计的领域')
    return
  }

  loading.value = true
  try {
    const response = await DomainApi.getBatchDomainStatistics(selectedRowKeys.value)
    
    // 显示统计结果
    const statisticsText = response.data.map(stat => 
      `${stat.domainName}: 字段${stat.totalFieldCount}个, 子域${stat.totalChildCount}个`
    ).join('\n')
    
    ElMessageBox.alert(statisticsText, '批量统计结果', {
      confirmButtonText: '确定'
    })
  } catch (error) {
    ElMessage.error('批量统计失败')
  } finally {
    loading.value = false
  }
}

const batchDeleteDomains = async () => {
  if (selectedRowKeys.value.length === 0) {
    ElMessage.warning('请选择要删除的领域')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRowKeys.value.length} 个领域吗？这将同时删除所有子领域。`,
      '批量删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    loading.value = true
    await DomainApi.deleteDomainsBatch(selectedRowKeys.value)
    
    ElMessage.success('批量删除成功')
    selectedRowKeys.value = []
    await loadDomainList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  } finally {
    loading.value = false
  }
}

const viewStatistics = async (row) => {
  try {
    const response = await DomainApi.getDomainStatistics(row.id)
    currentStatistics.value = response.data
    statisticsDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取统计信息失败')
  }
}

const manageDomainFields = (row) => {
  // 跳转到字段管理页面
  // router.push({ name: 'DomainFieldManagement', params: { domainId: row.id } })
  ElMessage.info(`跳转到领域 ${row.name} 的字段管理页面`)
}

const updateDomainStatus = async (row) => {
  try {
    await DomainApi.updateDomain({
      id: row.id,
      status: row.status
    })
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('状态更新失败')
    // 回滚状态
    row.status = row.status === 1 ? 0 : 1
  }
}

onMounted(() => {
  loadDomainList()
})
</script>

<style scoped>
.domain-batch-management {
  padding: 20px;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.selection-info {
  margin-left: auto;
  color: #606266;
  font-size: 14px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
```

## 🧪 **测试用例完整集合**

### 1. 基础CRUD测试

```http
### ===== 基础CRUD测试 =====

### 创建根领域
POST {{baseUrl}}/system/domain/create
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "name": "建筑领域",
  "code": "architecture",
  "parentId": 0,
  "description": "建筑相关的业务领域",
  "type": "business",
  "status": 1,
  "sort": 1,
  "remark": "根领域"
}

### 创建子领域
POST {{baseUrl}}/system/domain/create
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "name": "住宅建筑",
  "code": "residential",
  "parentId": 1,
  "description": "住宅类建筑",
  "type": "business",
  "status": 1,
  "sort": 1,
  "remark": "子领域"
}

### 更新领域
PUT {{baseUrl}}/system/domain/update
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "id": 1,
  "name": "建筑工程领域",
  "code": "architecture",
  "parentId": 0,
  "description": "建筑工程相关的业务领域",
  "type": "business",
  "status": 1,
  "sort": 1,
  "remark": "更新后的根领域"
}

### 获取领域详情
GET {{baseUrl}}/system/domain/get?id=1
Authorization: Bearer {{token}}

### 删除子领域
DELETE {{baseUrl}}/system/domain/delete?id=2
Authorization: Bearer {{token}}
```

### 2. 树形结构测试

```http
### ===== 树形结构测试 =====

### 获取基础列表
GET {{baseUrl}}/system/domain/list
Authorization: Bearer {{token}}

### 获取带统计信息的列表
GET {{baseUrl}}/system/domain/list-with-statistics
Authorization: Bearer {{token}}

### 获取Element Plus Tree格式
GET {{baseUrl}}/system/domain/tree
Authorization: Bearer {{token}}

### 拖拽移动领域
POST {{baseUrl}}/system/domain/move
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "id": 2,
  "targetParentId": 3,
  "targetSort": 1
}

### 批量更新排序
POST {{baseUrl}}/system/domain/batch-sort
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "parentId": 1,
  "sorts": {
    "2": 1,
    "3": 2,
    "4": 3
  }
}
```

### 3. 关联管理测试

```http
### ===== 关联管理测试 =====

### 添加字段关联
POST {{baseUrl}}/system/domain/field-rel/add?domainId=1&fieldId=1&required=true&sort=1&remark=测试关联
Authorization: Bearer {{token}}

### 重复添加（测试容错）
POST {{baseUrl}}/system/domain/field-rel/add?domainId=1&fieldId=1&required=false&sort=2&remark=重复关联测试
Authorization: Bearer {{token}}

### 移除字段关联
DELETE {{baseUrl}}/system/domain/field-rel/remove?domainId=1&fieldId=1
Authorization: Bearer {{token}}

### 获取领域字段关联列表
GET {{baseUrl}}/system/domain/field-rel/list?domainId=1
Authorization: Bearer {{token}}

### 字段在领域间移动
POST {{baseUrl}}/system/domain/field-move
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "fieldId": 1,
  "sourceDomainId": 1,
  "targetDomainId": 2,
  "required": true,
  "sort": 1,
  "remark": "移动测试"
}
```

### 4. 查询功能测试

```http
### ===== 查询功能测试 =====

### 查看指定领域下的字段列表
GET {{baseUrl}}/system/domain/fields?domainId=1
Authorization: Bearer {{token}}

### 查看指定字段关联的领域列表
GET {{baseUrl}}/system/domain/domains-by-field?fieldId=1
Authorization: Bearer {{token}}

### 获取单个领域统计信息
GET {{baseUrl}}/system/domain/statistics?domainId=1
Authorization: Bearer {{token}}

### 批量获取多个领域统计信息
POST {{baseUrl}}/system/domain/statistics/batch
Authorization: Bearer {{token}}
Content-Type: application/json

[1, 2, 3]
```

### 5. 批量处理测试

```http
### ===== 批量处理测试 =====

### 批量删除领域
POST {{baseUrl}}/system/domain/delete/batch
Authorization: Bearer {{token}}
Content-Type: application/json

[4, 5]

### 批量删除字段关联
POST {{baseUrl}}/system/domain/field-relations/remove/batch
Authorization: Bearer {{token}}
Content-Type: application/json

[1, 2, 3]
```

## 💡 **性能优化策略**

### 1. **数据库优化**

```sql
-- 关键索引
CREATE INDEX idx_domain_parent_sort ON system_domain(parent_id, sort);
CREATE INDEX idx_domain_type_status ON system_domain(type, status);
CREATE INDEX idx_domain_field_rel_domain ON system_domain_field_rel(domain_id);
CREATE INDEX idx_domain_field_rel_field ON system_domain_field_rel(field_id);

-- 复合索引优化
CREATE INDEX idx_domain_parent_name_deleted ON system_domain(parent_id, name, deleted, tenant_id);
CREATE INDEX idx_domain_field_rel_unique ON system_domain_field_rel(domain_id, field_id, deleted, tenant_id);
```

### 2. **查询优化**

- **批量查询**：使用 `IN` 语句批量获取统计信息
- **JOIN优化**：使用LEFT JOIN避免N+1查询问题
- **分页查询**：大数据量时使用分页避免内存溢出
- **缓存策略**：对频繁查询的统计数据进行缓存

### 3. **算法优化**

- **递归算法**：使用迭代代替递归，减少栈溢出风险
- **树构建**：一次性构建完整树结构，避免多次查询
- **排序优化**：在数据库层面完成排序，减少内存消耗

## ⚠️ **注意事项与最佳实践**

### 1. **数据一致性**
- 所有批量操作都使用事务保证原子性
- 删除操作前检查依赖关系
- 树形结构变更时检查循环引用

### 2. **性能考虑**
- 深层级树结构可能影响查询性能
- 批量操作建议限制数量（如最多100个）
- 大量数据时考虑异步处理

### 3. **安全控制**
- 所有接口都有权限验证
- 敏感操作需要二次确认
- 审计日志记录关键操作

### 4. **用户体验**
- 重复操作提供友好提示而非错误
- 批量操作提供进度反馈
- 操作结果及时通知用户

## 📋 **菜单配置完整参数**

### 主菜单配置

```json
{
  "name": "领域管理",
  "type": 1,
  "sort": 4000,
  "parentId": 0,
  "path": "domain",
  "icon": "ep:management",
  "component": "LAYOUT",
  "permission": "",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": true
}
```

### 子菜单配置

```json
[
  {
    "name": "领域列表",
    "type": 2,
    "sort": 4010,
    "parentId": 4000,
    "path": "domain-list",
    "icon": "ep:list",
    "component": "system/domain/index",
    "componentName": "SystemDomain",
    "permission": "system:domain:query",
    "status": 0,
    "visible": true,
    "keepAlive": true,
    "alwaysShow": false
  },
  {
    "name": "领域树管理",
    "type": 2,
    "sort": 4015,
    "parentId": 4000,
    "path": "domain-tree",
    "icon": "ep:tree",
    "component": "system/domain/tree/index",
    "componentName": "SystemDomainTree",
    "permission": "system:domain:query",
    "status": 0,
    "visible": true,
    "keepAlive": true,
    "alwaysShow": false
  },
  {
    "name": "字段关联查询",
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
  },
  {
    "name": "统计分析",
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
  },
  {
    "name": "批量管理",
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
]
```

通过这个完整的Domain管理功能，您可以实现企业级的领域模型管理，支持复杂的业务场景和高性能的数据处理需求！ 