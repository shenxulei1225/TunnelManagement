# 📁 工作台文件管理模块使用指南

## 🎯 概述

本文档介绍了整理后的工作台文件管理模块的使用方式。该模块将原有分散的文件管理功能整合为统一的、可复用的模块，提供了完整的文件操作功能。

## 📋 模块结构

```
tunnel-management-ui/src/core/file-manager/
├── index.ts                    # 模块入口
├── types.ts                    # 类型定义
├── utils.ts                    # 工具函数
├── FileManager.ts              # 核心文件管理器
├── FileUploadManager.ts        # 文件上传管理器
├── FileDownloadManager.ts      # 文件下载管理器
├── FilePreviewManager.ts       # 文件预览管理器
├── FileValidationManager.ts    # 文件验证管理器
├── WorkspaceFileManager.ts     # 工作台文件管理器
└── composables/                # Vue Composables
    ├── useFileManager.ts       # 文件管理
    ├── useFileUpload.ts        # 文件上传
    ├── useFileDownload.ts      # 文件下载
    └── useFilePreview.ts       # 文件预览
```

## 🚀 快速开始

### 1. 基础文件管理

```typescript
import { useFileManager } from '@/core/file-manager'

export default {
  setup() {
    // 初始化文件管理器
    const {
      files,
      loading,
      selectedFiles,
      loadFiles,
      addFile,
      updateFile,
      deleteFile,
      downloadFile
    } = useFileManager({
      maxFileSize: 50 * 1024 * 1024, // 50MB
      allowedTypes: ['image/*', 'application/pdf'],
      enableThumbnail: true
    })

    // 加载文件列表
    onMounted(() => {
      loadFiles()
    })

    return {
      files,
      loading,
      selectedFiles,
      addFile,
      updateFile,
      deleteFile,
      downloadFile
    }
  }
}
```

### 2. 文件上传

```typescript
import { useFileUpload } from '@/core/file-manager'

export default {
  setup() {
    const {
      uploading,
      uploadedFiles,
      uploadErrors,
      uploadFile,
      uploadFiles
    } = useFileUpload({
      directory: 'workspace',
      maxSize: 10 * 1024 * 1024, // 10MB
      allowedTypes: ['.jpg', '.png', '.pdf'],
      onProgress: (progress) => {
        console.log('上传进度:', progress)
      },
      onSuccess: (file) => {
        console.log('上传成功:', file)
      }
    })

    // 单文件上传
    const handleFileUpload = async (file: File) => {
      const result = await uploadFile(file)
      if (result.success) {
        ElMessage.success('上传成功')
      } else {
        ElMessage.error(result.message)
      }
    }

    // 批量上传
    const handleMultipleUpload = async (files: File[]) => {
      const result = await uploadFiles(files)
      console.log('上传结果:', result)
    }

    return {
      uploading,
      uploadedFiles,
      uploadErrors,
      handleFileUpload,
      handleMultipleUpload
    }
  }
}
```

### 3. 文件下载

```typescript
import { useFileDownload } from '@/core/file-manager'

export default {
  setup() {
    const {
      downloading,
      downloadErrors,
      downloadFile,
      downloadFiles
    } = useFileDownload()

    // 下载单个文件
    const handleDownload = async (file: FileInfo) => {
      await downloadFile(file, {
        filename: `${file.name}_download.${file.extension}`
      })
    }

    // 批量下载为ZIP
    const handleBatchDownload = async (files: FileInfo[]) => {
      await downloadFiles(files, {
        zipName: 'workspace_files.zip'
      })
    }

    return {
      downloading,
      downloadErrors,
      handleDownload,
      handleBatchDownload
    }
  }
}
```

### 4. 文件预览

```typescript
import { useFilePreview } from '@/core/file-manager'

export default {
  setup() {
    const {
      currentFile,
      previewVisible,
      previewInfo,
      canPreview,
      previewFile,
      closePreview,
      nextFile,
      prevFile
    } = useFilePreview({
      enablePreview: true,
      maxPreviewSize: 10 * 1024 * 1024,
      supportedTypes: ['image', 'video', 'audio', 'text']
    })

    // 预览文件
    const handlePreview = (file: FileInfo) => {
      if (canPreview.value) {
        previewFile(file)
      } else {
        ElMessage.warning('该文件类型不支持预览')
      }
    }

    return {
      currentFile,
      previewVisible,
      previewInfo,
      canPreview,
      handlePreview,
      closePreview,
      nextFile,
      prevFile
    }
  }
}
```

## 🔧 高级用法

### 1. 工作台文件管理

```typescript
import { WorkspaceFileManager } from '@/core/file-manager'

// 创建工作台文件管理器实例
const workspaceManager = new WorkspaceFileManager({
  enableVersioning: true,
  autoBackup: true
})

// 设置当前项目
workspaceManager.setCurrentProject({
  id: 1,
  name: '我的设计项目'
})

// 创建工作台文件
const result = await workspaceManager.createWorkspaceFile({
  name: '登录页面设计.fig',
  type: 'design',
  projectId: 1,
  starred: true,
  metadata: {
    description: '移动端登录页面设计',
    tags: ['移动端', '登录', 'UI设计']
  }
})

// 收藏文件
workspaceManager.toggleStarFile(fileId)

// 获取收藏的文件
const starredFiles = workspaceManager.getStarredFiles()

// 获取最近访问的文件
const recentFiles = workspaceManager.getRecentFiles(10)

// 获取项目文件
const projectFiles = workspaceManager.getProjectFiles(1)
```

### 2. 文件验证

```typescript
import { FileValidationManager } from '@/core/file-manager'

const validator = new FileValidationManager()

// 添加验证规则
const rules = FileValidationManager.createCommonRules()

validator.addRule(rules.maxSize(10 * 1024 * 1024)) // 最大10MB
validator.addRule(rules.allowedTypes(['.jpg', '.png', '.pdf']))
validator.addRule(rules.imageDimensions(1920, 1080)) // 图片尺寸限制

// 验证文件
const file = new File(['...'], 'test.jpg', { type: 'image/jpeg' })
const validation = await validator.validateFile(file)

if (!validation.success) {
  console.error('文件验证失败:', validation.message)
}
```

### 3. 文件搜索和过滤

```typescript
import { useFileManager } from '@/core/file-manager'
import { FileCategory } from '@/core/file-manager/types'

const { manager, setSearchFilter, setSortOptions } = useFileManager()

// 设置搜索过滤器
setSearchFilter({
  name: '设计',
  category: FileCategory.DESIGN,
  starred: true,
  dateRange: [new Date('2024-01-01'), new Date()],
  sizeRange: [0, 10 * 1024 * 1024] // 0-10MB
})

// 设置排序选项
setSortOptions({
  field: 'updateTime',
  order: 'desc'
})

// 直接使用管理器搜索
const searchResults = manager.searchFiles({
  name: '登录',
  type: 'design'
})
```

### 4. 事件监听

```typescript
import { FileManager } from '@/core/file-manager'

const manager = new FileManager()

// 监听文件事件
manager.on('fileAdded', (file) => {
  console.log('文件已添加:', file)
  // 刷新文件列表
  refreshFileList()
})

manager.on('fileDeleted', (file) => {
  console.log('文件已删除:', file)
  // 更新UI状态
  updateUIState()
})

manager.on('fileStarToggled', ({ file, starred }) => {
  console.log(`文件${starred ? '收藏' : '取消收藏'}:`, file)
})

// 清理事件监听
onBeforeUnmount(() => {
  manager.destroy()
})
```

## 📊 类型定义

### FileInfo

```typescript
interface FileInfo {
  id: string | number
  name: string
  path: string
  size: number
  type: string
  mimeType: string
  extension: string
  url?: string
  thumbnail?: string
  createTime: Date
  updateTime: Date
  hash?: string
  category?: FileCategory
  tags?: string[]
  metadata?: Record<string, any>
}
```

### FileCategory

```typescript
enum FileCategory {
  DOCUMENT = 'document',
  IMAGE = 'image',
  VIDEO = 'video',
  AUDIO = 'audio',
  ARCHIVE = 'archive',
  CODE = 'code',
  DESIGN = 'design',
  OTHER = 'other'
}
```

### FileOperationResult

```typescript
interface FileOperationResult<T = any> {
  success: boolean
  message?: string
  data?: T
  error?: Error
}
```

## 🎨 组件示例

### 文件列表组件

```vue
<template>
  <div class="file-manager">
    <!-- 搜索和过滤 -->
    <div class="file-toolbar">
      <el-input 
        v-model="searchTerm" 
        placeholder="搜索文件..." 
        clearable
        @input="handleSearch"
      />
      <el-select v-model="selectedCategory" @change="handleCategoryFilter">
        <el-option label="全部" value="" />
        <el-option label="图片" :value="FileCategory.IMAGE" />
        <el-option label="文档" :value="FileCategory.DOCUMENT" />
        <el-option label="设计" :value="FileCategory.DESIGN" />
      </el-select>
      <el-button type="primary" @click="openUploadDialog">
        <el-icon><Upload /></el-icon>
        上传文件
      </el-button>
    </div>

    <!-- 文件列表 -->
    <div class="file-list" v-loading="loading">
      <div 
        v-for="file in files" 
        :key="file.id"
        class="file-item"
        :class="{ selected: selectedFiles.includes(file.id.toString()) }"
        @click="toggleFileSelection(file.id.toString())"
        @dblclick="previewFile(file)"
      >
        <div class="file-thumbnail">
          <img v-if="file.thumbnail" :src="file.thumbnail" :alt="file.name" />
          <el-icon v-else><Document /></el-icon>
        </div>
        <div class="file-info">
          <div class="file-name">{{ file.name }}</div>
          <div class="file-meta">
            {{ formatFileSize(file.size) }} • {{ formatTime(file.updateTime) }}
          </div>
        </div>
        <div class="file-actions">
          <el-button size="small" @click.stop="downloadFile(file.id)">
            <el-icon><Download /></el-icon>
          </el-button>
          <el-button size="small" @click.stop="toggleStar(file.id)">
            <el-icon>
              <StarFilled v-if="file.metadata?.starred" />
              <Star v-else />
            </el-icon>
          </el-button>
          <el-dropdown @command="handleAction">
            <el-button size="small">
              <el-icon><More /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item :command="{ action: 'rename', file }">
                  重命名
                </el-dropdown-item>
                <el-dropdown-item :command="{ action: 'duplicate', file }">
                  复制
                </el-dropdown-item>
                <el-dropdown-item :command="{ action: 'delete', file }" divided>
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 文件预览 -->
    <FilePreviewDialog
      v-model="previewVisible"
      :file="currentFile"
      :files="files"
      @next="nextFile(files)"
      @prev="prevFile(files)"
    />

    <!-- 上传对话框 -->
    <FileUploadDialog v-model="uploadVisible" @success="handleUploadSuccess" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useFileManager, useFilePreview, formatFileSize, formatTime } from '@/core/file-manager'
import { FileCategory } from '@/core/file-manager/types'

// 文件管理
const {
  files,
  loading,
  selectedFiles,
  loadFiles,
  deleteFile,
  downloadFile,
  duplicateFile,
  updateFile,
  toggleFileSelection,
  setSearchFilter,
  manager
} = useFileManager()

// 文件预览
const {
  currentFile,
  previewVisible,
  previewFile,
  nextFile,
  prevFile
} = useFilePreview()

// 状态
const searchTerm = ref('')
const selectedCategory = ref('')
const uploadVisible = ref(false)

// 方法
const handleSearch = () => {
  setSearchFilter({ name: searchTerm.value })
}

const handleCategoryFilter = () => {
  setSearchFilter({ 
    category: selectedCategory.value as FileCategory || undefined 
  })
}

const toggleStar = (fileId: string | number) => {
  if (manager instanceof WorkspaceFileManager) {
    manager.toggleStarFile(fileId)
  }
}

const openUploadDialog = () => {
  uploadVisible.value = true
}

const handleUploadSuccess = () => {
  loadFiles() // 刷新文件列表
}

const handleAction = ({ action, file }) => {
  switch (action) {
    case 'rename':
      // 处理重命名
      break
    case 'duplicate':
      duplicateFile(file.id)
      break
    case 'delete':
      deleteFile(file.id)
      break
  }
}

onMounted(() => {
  loadFiles()
})
</script>

<style scoped>
.file-manager {
  padding: 20px;
}

.file-toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  align-items: center;
}

.file-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.file-item {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.file-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.file-item.selected {
  border-color: #409eff;
  background-color: #f0f8ff;
}

.file-thumbnail {
  width: 100%;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  border-radius: 4px;
  margin-bottom: 8px;
}

.file-thumbnail img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.file-info {
  margin-bottom: 8px;
}

.file-name {
  font-weight: 500;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-meta {
  font-size: 12px;
  color: #999;
}

.file-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
```

## 🔄 菜单配置参数

为每个新增的文件管理页面提供菜单配置参数：

### 主文件管理页面

```javascript
const fileManagerMenuConfig = {
  name: '文件管理',
  type: 2, // 菜单
  sort: 100,
  parentId: 0,
  path: '/workspace/files',
  icon: 'folder',
  component: 'views/workspace/files/index',
  componentName: 'WorkspaceFiles',
  permission: 'workspace:file:query',
  status: 1,
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```

### 文件上传页面

```javascript
const fileUploadMenuConfig = {
  name: '文件上传',
  type: 2, // 菜单
  sort: 101,
  parentId: fileManagerMenuConfig.id,
  path: '/workspace/files/upload',
  icon: 'upload',
  component: 'views/workspace/files/upload',
  componentName: 'FileUpload',
  permission: 'workspace:file:create',
  status: 1,
  visible: true,
  keepAlive: false,
  alwaysShow: false
}
```

### 回收站页面

```javascript
const trashMenuConfig = {
  name: '回收站',
  type: 2, // 菜单
  sort: 102,
  parentId: fileManagerMenuConfig.id,
  path: '/workspace/files/trash',
  icon: 'delete',
  component: 'views/workspace/files/trash',
  componentName: 'FileTrash',
  permission: 'workspace:file:delete',
  status: 1,
  visible: true,
  keepAlive: false,
  alwaysShow: false
}
```

## 📈 性能优化建议

1. **虚拟滚动**：文件列表较多时使用虚拟滚动
2. **懒加载**：缩略图和预览内容懒加载
3. **缓存策略**：合理设置文件信息缓存
4. **分页加载**：大量文件时采用分页加载
5. **防抖处理**：搜索输入防抖，减少API调用

## 🔒 安全注意事项

1. **文件类型验证**：严格验证上传文件类型
2. **文件大小限制**：设置合理的文件大小限制
3. **路径遍历防护**：防止路径遍历攻击
4. **权限控制**：确保用户只能访问有权限的文件
5. **病毒扫描**：可选的文件病毒扫描功能

## 🚨 故障排除

### 常见问题

1. **文件上传失败**
   - 检查文件大小是否超限
   - 检查文件类型是否支持
   - 检查网络连接状态

2. **文件预览不显示**
   - 检查文件URL是否有效
   - 检查文件类型是否支持预览
   - 检查浏览器兼容性

3. **文件下载失败**
   - 检查文件是否存在
   - 检查用户权限
   - 检查浏览器下载设置

## 📝 更新日志

### v1.0.0 (2024-01-20)
- ✨ 初始版本发布
- 🔧 整合原有文件管理功能
- 📦 提供统一的模块化接口
- 🎯 支持工作台特定功能

## 📞 支持

如有问题或建议，请联系开发团队或提交Issue。 