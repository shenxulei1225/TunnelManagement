# 文件管理分类集成方案

> **基于现有 `system_field_category` 分类管理系统的文件管理架构**

## 📋 概述

系统已有完善的分类管理功能，我们只需要在现有的 `system_field_category` 表中添加一个"文件管理"根分类，然后在其下创建各种业务分类，即可实现细粒度的文件分类管理。

## 🗂️ 分类层级设计

```
文件管理 (file_management) - 根分类
├── 工作台文档 (workspace_docs)
│   ├── 设计文档 (design_docs)
│   ├── 项目文档 (project_docs)
│   └── 临时文件 (temp_files)
├── 维护手册 (maintenance_manual)
│   ├── 操作手册 (operation_manual)
│   ├── 故障排除 (troubleshooting)
│   └── 系统配置 (system_config)
├── 用户资料 (user_profile)
│   ├── 头像图片 (avatar_images)
│   ├── 个人文档 (personal_docs)
│   └── 用户设置 (user_settings)
├── 项目附件 (project_attachments)
│   ├── 需求文档 (requirements)
│   ├── 设计稿 (design_files)
│   └── 测试文件 (test_files)
└── 系统文件 (system_files)
    ├── 配置文件 (config_files)
    ├── 日志文件 (log_files)
    └── 备份文件 (backup_files)
```

## 🛠️ 实施步骤

### 1. 执行分类初始化SQL

运行 `sql/mysql/file_management_categories.sql` 脚本：

```bash
# 在MySQL中执行
mysql -u username -p database_name < sql/mysql/file_management_categories.sql
```

### 2. 验证分类创建结果

```sql
-- 查看文件管理分类树
SELECT 
    id,
    parent_id,
    code,
    name,
    tree_path,
    level,
    sort
FROM system_field_category 
WHERE code = 'file_management' 
   OR tree_path LIKE '%/file_management/%'
ORDER BY level, sort;
```

### 3. 复用现有分类管理组件

系统已有的分类管理组件可以直接使用：

```vue
<template>
  <div class="file-category-manager">
    <!-- 复用现有的分类树组件 -->
    <CategoryTree 
      :biz-type="'file_management'"
      @select="handleCategorySelect" 
    />
  </div>
</template>

<script setup lang="ts">
import CategoryTree from '@/views/system/field/FieldCategory/CategoryTree.vue'

const handleCategorySelect = (category) => {
  console.log('选中的文件分类:', category)
  // 根据选中的分类加载对应的文件列表
}
</script>
```

## 🔧 文件管理API设计

### TypeScript接口定义

```typescript
// 文件信息接口
export interface FileInfo {
  id: number
  categoryId: number              // 关联 system_field_category.id
  businessId?: string            // 业务关联ID（项目ID、用户ID等）
  originalName: string           // 原始文件名
  storedName: string            // 存储文件名
  filePath: string              // 文件存储路径
  fileSize: number              // 文件大小
  mimeType?: string             // MIME类型
  fileExtension?: string        // 文件扩展名
  uploadStatus: number          // 上传状态
  accessLevel: number           // 访问级别
  tags?: string[]               // 文件标签
  description?: string          // 文件描述
  downloadCount: number         // 下载次数
  createTime: string
  tenantId: number
}

// 分类配置接口
export interface CategoryConfig {
  id: number
  code: string
  name: string
  maxFileSize: number           // 最大文件大小
  allowedTypes: string[]        // 允许的文件类型
  maxFiles: number              // 最大文件数量
  autoCleanup: boolean          // 是否自动清理过期文件
}
```

### API函数实现

```typescript
// 文件管理API
export const FileManagementApi = {
  // 获取文件分类树（复用现有API）
  getCategories: async () => {
    return await getCategoryTree('file_management')
  },

  // 根据分类获取文件列表
  getFilesByCategory: async (categoryId: number, businessId?: string) => {
    return await request.get({
      url: '/api/files/list',
      params: { 
        categoryId, 
        businessId,
        tenantId: getCurrentTenantId()
      }
    })
  },

  // 上传文件到指定分类
  uploadToCategory: async (categoryId: number, file: File, businessId?: string) => {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('categoryId', categoryId.toString())
    if (businessId) formData.append('businessId', businessId)

    return await request.post({
      url: '/api/files/upload',
      data: formData,
      headers: { 
        'Content-Type': 'multipart/form-data',
        'tenant-id': getCurrentTenantId()
      }
    })
  }
}
```

## 🎨 通用文件管理器组件

```vue
<template>
  <div class="universal-file-manager">
    <div class="file-manager-layout">
      <!-- 左侧：分类树 -->
      <div class="category-panel">
        <el-card shadow="never">
          <template #header>
            <span>文件分类</span>
          </template>
          <CategoryTree 
            ref="categoryTreeRef"
            @select="handleCategorySelect"
          />
        </el-card>
      </div>

      <!-- 右侧：文件列表 -->
      <div class="file-panel">
        <el-card shadow="never">
          <template #header>
            <div class="file-header">
              <span>{{ selectedCategory?.name || '请选择分类' }}</span>
              <div class="file-actions">
                <el-button 
                  type="primary" 
                  :icon="Upload"
                  @click="handleUpload"
                  :disabled="!selectedCategory"
                >
                  选择文件
                </el-button>
              </div>
            </div>
          </template>

          <!-- 文件列表 -->
          <el-table 
            :data="fileList"
            :loading="loading"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="originalName" label="文件名" />
            <el-table-column prop="fileSize" label="大小" :formatter="formatFileSize" />
            <el-table-column prop="createTime" label="上传时间" />
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button type="text" @click="downloadFile(row)">下载</el-button>
                <el-button type="text" @click="previewFile(row)">预览</el-button>
                <el-button type="text" @click="deleteFile(row)" class="danger">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>

    <!-- 文件上传对话框 -->
    <el-dialog v-model="uploadVisible" title="上传文件" width="500px">
      <el-upload
        drag
        :action="uploadUrl"
        :headers="uploadHeaders"
        :data="uploadData"
        :on-success="handleUploadSuccess"
        :before-upload="beforeUpload"
        multiple
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
      </el-upload>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { Upload, UploadFilled } from '@element-plus/icons-vue'
import CategoryTree from '@/views/system/field/FieldCategory/CategoryTree.vue'
import { FileManagementApi } from '@/api/system/fileManagement'

// Props
interface Props {
  businessId?: string           // 业务ID，用于关联特定业务数据
  defaultCategoryCode?: string  // 默认选中的分类code
  readonly?: boolean            // 是否只读模式
}

const props = withDefaults(defineProps<Props>(), {
  readonly: false
})

// 响应式数据
const selectedCategory = ref(null)
const fileList = ref([])
const loading = ref(false)
const uploadVisible = ref(false)

// 上传配置
const uploadUrl = '/api/files/upload'
const uploadHeaders = computed(() => ({ 'tenant-id': getCurrentTenantId() }))
const uploadData = computed(() => ({
  categoryId: selectedCategory.value?.id,
  businessId: props.businessId
}))

// 事件处理
const handleCategorySelect = async (category) => {
  selectedCategory.value = category
  await loadFileList()
}

const loadFileList = async () => {
  if (!selectedCategory.value) return
  
  loading.value = true
  try {
    const result = await FileManagementApi.getFilesByCategory(
      selectedCategory.value.id,
      props.businessId
    )
    fileList.value = result.data
  } finally {
    loading.value = false
  }
}

const handleUpload = () => {
  uploadVisible.value = true
}

const handleUploadSuccess = () => {
  uploadVisible.value = false
  loadFileList()
}

// 工具函数
const formatFileSize = (row, column, cellValue) => {
  if (cellValue < 1024) return cellValue + ' B'
  if (cellValue < 1024 * 1024) return (cellValue / 1024).toFixed(1) + ' KB'
  return (cellValue / (1024 * 1024)).toFixed(1) + ' MB'
}

const getCurrentTenantId = () => {
  // 获取当前租户ID的逻辑
  return 1
}
</script>

<style scoped>
.universal-file-manager {
  height: 100%;
}

.file-manager-layout {
  display: flex;
  height: 100%;
  gap: 16px;
}

.category-panel {
  width: 300px;
  flex-shrink: 0;
}

.file-panel {
  flex: 1;
}

.file-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.danger {
  color: #f56c6c;
}
</style>
```

## 📊 菜单配置参数

### 1. 文件管理主页面

```javascript
const fileManagementMenuConfig = {
  name: '文件管理',
  type: 2,                    // 菜单
  sort: 100,
  parentId: 0,                // 根据实际父菜单ID调整
  path: '/files/management',
  icon: 'folder',
  component: 'files/management/index',
  componentName: 'FilesManagement',
  permission: 'files:management:query',
  status: 0,                  // 启用
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```

### 2. 工作台文件页面

```javascript
const workspaceFilesMenuConfig = {
  name: '工作台文件',
  type: 2,
  sort: 120,
  parentId: workspaceMenuId,  // 工作台父菜单ID
  path: '/workspace/files',
  icon: 'document',
  component: 'workspace/files/index',
  componentName: 'WorkspaceFiles',
  permission: 'workspace:files:query',
  status: 0,
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```

### 3. 维护手册文件页面

```javascript
const maintenanceFilesMenuConfig = {
  name: '维护手册',
  type: 2,
  sort: 130,
  parentId: systemMenuId,     // 系统管理父菜单ID
  path: '/system/maintenance-files',
  icon: 'notebook',
  component: 'system/maintenance/files/index',
  componentName: 'MaintenanceFiles',
  permission: 'system:maintenance:query',
  status: 0,
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```

### 4. 用户文件管理页面

```javascript
const userFilesMenuConfig = {
  name: '我的文件',
  type: 2,
  sort: 140,
  parentId: userCenterMenuId, // 用户中心父菜单ID
  path: '/user/files',
  icon: 'user-filled',
  component: 'user/files/index',
  componentName: 'UserFiles',
  permission: 'user:files:query',
  status: 0,
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```

### 5. 批量创建菜单工具函数

```javascript
/**
 * 批量创建文件管理相关菜单
 */
export const createFileManagementMenus = async () => {
  const menus = [
    fileManagementMenuConfig,
    workspaceFilesMenuConfig,
    maintenanceFilesMenuConfig,
    userFilesMenuConfig
  ]
  
  console.log('开始创建文件管理相关菜单...')
  
  for (const menuConfig of menus) {
    try {
      const result = await MenuApi.createMenu(menuConfig)
      console.log(`✅ 菜单 "${menuConfig.name}" 创建成功，ID: ${result.data}`)
      
      // 为每个菜单提供详细的配置信息
      console.log('菜单配置详情:', {
        name: menuConfig.name,
        path: menuConfig.path,
        component: menuConfig.component,
        permission: menuConfig.permission
      })
    } catch (error) {
      console.error(`❌ 菜单 "${menuConfig.name}" 创建失败:`, error)
    }
  }
  
  console.log('文件管理菜单创建完成！')
}

// 使用示例
// createFileManagementMenus()
```

## 🚀 使用示例

### 1. 在UX设计器中拖拽使用

设计师可以直接拖拽文件管理模块到页面：

```vue
<!-- 拖拽后自动生成的组件 -->
<UniversalFileManager
  :business-id="currentProjectId"
  :default-category-code="'design_docs'"
  :readonly="false"
/>
```

**自动生成的菜单配置参数：**

```javascript
// 设计器自动生成的菜单配置
const autoGeneratedMenuConfig = {
  name: '项目设计文档',
  type: 2,
  sort: generateSort(),
  parentId: currentPageMenuId,
  path: '/project/design-docs',
  icon: 'document',
  component: 'project/design-docs/index',
  componentName: 'ProjectDesignDocs',
  permission: 'project:design-docs:query',
  status: 0,
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```

### 2. 编程方式集成

```typescript
// 在任何Vue组件中使用
import UniversalFileManager from '@/components/files/UniversalFileManager.vue'

// 业务场景示例
const scenarios = {
  // 工作台设计页面文档
  workspaceDesign: {
    businessId: 'workspace_001',
    defaultCategoryCode: 'design_docs'
  },
  
  // 维护手册
  maintenanceManual: {
    businessId: 'manual_001', 
    defaultCategoryCode: 'operation_manual'
  },
  
  // 用户头像
  userAvatar: {
    businessId: `user_${userId}`,
    defaultCategoryCode: 'avatar_images'
  }
}
```

## 🔒 多租户和权限支持

### 租户隔离

- **分类隔离**: 每个租户有独立的文件分类树
- **数据隔离**: 通过 `tenant_id` 字段确保数据安全
- **存储隔离**: 文件存储路径包含租户标识

### 权限控制

```javascript
// 权限配置示例
const filePermissions = {
  'files:management:query':   '查看文件管理',
  'files:management:upload':  '上传文件',
  'files:management:download': '下载文件', 
  'files:management:delete':  '删除文件',
  'files:category:manage':    '管理文件分类'
}
```

## 📝 重要提醒

### 多租户注意事项

⚠️ **务必注意租户ID设置**：
- 所有分类数据必须包含正确的 `tenant_id`
- Mock数据不能设置 `tenant_id` 为 0，应设置为具体的租户ID
- 前端API调用必须包含 `tenant-id` 请求头

### 使用checklist

- [ ] 执行分类初始化SQL脚本
- [ ] 验证分类树创建成功
- [ ] 创建对应的菜单项
- [ ] 配置用户角色权限
- [ ] 测试多租户数据隔离
- [ ] 验证文件上传下载功能

---

> **优势总结**：通过复用现有的分类管理系统，我们实现了：
> - **零重复开发**：完全复用现有分类管理功能
> - **统一体验**：与系统其他分类管理保持一致的用户体验  
> - **细粒度分类**：支持多级分类，满足复杂业务需求
> - **自动化集成**：设计器拖拽即可使用，自动生成菜单配置 