# 🔗 通用文件管理器API规范 v3.0

> **基于现有分类管理系统的文件管理架构**
>
> **更新说明**: 复用现有的 `system_field_category` 分类管理系统，在其下创建文件管理根分类及子分类。

## 📋 架构设计

### 分类管理集成方案

项目已有完善的分类管理功能（`system_field_category`表），我们只需要：

1. **添加文件管理根分类**: 在现有分类体系下创建"文件管理"根节点
2. **创建业务子分类**: 根据不同业务场景创建细分分类
3. **复用现有API**: 直接使用现有的分类管理API和组件

### 分类层级结构设计

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

## 🗄️ 数据库设计

### 1. 文件分类初始化SQL

```sql
-- 插入文件管理根分类
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    create_time, creator, tenant_id
) VALUES (
    0, 'file_management', '文件管理', '/1', 1, 100,
    NOW(), 'system', 1
);

-- 获取根分类ID并插入子分类
SET @root_id = LAST_INSERT_ID();

-- 工作台文档分类
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    create_time, creator, tenant_id
) VALUES 
(@root_id, 'workspace_docs', '工作台文档', CONCAT('/', @root_id, '/2'), 2, 10, NOW(), 'system', 1),
(@root_id, 'maintenance_manual', '维护手册', CONCAT('/', @root_id, '/3'), 2, 20, NOW(), 'system', 1),
(@root_id, 'user_profile', '用户资料', CONCAT('/', @root_id, '/4'), 2, 30, NOW(), 'system', 1),
(@root_id, 'project_attachments', '项目附件', CONCAT('/', @root_id, '/5'), 2, 40, NOW(), 'system', 1),
(@root_id, 'system_files', '系统文件', CONCAT('/', @root_id, '/6'), 2, 50, NOW(), 'system', 1);

-- 工作台文档子分类
SET @workspace_id = (SELECT id FROM system_field_category WHERE code = 'workspace_docs' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    create_time, creator, tenant_id
) VALUES 
(@workspace_id, 'design_docs', '设计文档', CONCAT('/', @root_id, '/', @workspace_id, '/7'), 3, 10, NOW(), 'system', 1),
(@workspace_id, 'project_docs', '项目文档', CONCAT('/', @root_id, '/', @workspace_id, '/8'), 3, 20, NOW(), 'system', 1),
(@workspace_id, 'temp_files', '临时文件', CONCAT('/', @root_id, '/', @workspace_id, '/9'), 3, 30, NOW(), 'system', 1);

-- 维护手册子分类
SET @manual_id = (SELECT id FROM system_field_category WHERE code = 'maintenance_manual' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    create_time, creator, tenant_id
) VALUES 
(@manual_id, 'operation_manual', '操作手册', CONCAT('/', @root_id, '/', @manual_id, '/10'), 3, 10, NOW(), 'system', 1),
(@manual_id, 'troubleshooting', '故障排除', CONCAT('/', @root_id, '/', @manual_id, '/11'), 3, 20, NOW(), 'system', 1),
(@manual_id, 'system_config', '系统配置', CONCAT('/', @root_id, '/', @manual_id, '/12'), 3, 30, NOW(), 'system', 1);

-- 用户资料子分类
SET @profile_id = (SELECT id FROM system_field_category WHERE code = 'user_profile' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    create_time, creator, tenant_id
) VALUES 
(@profile_id, 'avatar_images', '头像图片', CONCAT('/', @root_id, '/', @profile_id, '/13'), 3, 10, NOW(), 'system', 1),
(@profile_id, 'personal_docs', '个人文档', CONCAT('/', @root_id, '/', @profile_id, '/14'), 3, 20, NOW(), 'system', 1),
(@profile_id, 'user_settings', '用户设置', CONCAT('/', @root_id, '/', @profile_id, '/15'), 3, 30, NOW(), 'system', 1);

-- 项目附件子分类
SET @attachment_id = (SELECT id FROM system_field_category WHERE code = 'project_attachments' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    create_time, creator, tenant_id
) VALUES 
(@attachment_id, 'requirements', '需求文档', CONCAT('/', @root_id, '/', @attachment_id, '/16'), 3, 10, NOW(), 'system', 1),
(@attachment_id, 'design_files', '设计稿', CONCAT('/', @root_id, '/', @attachment_id, '/17'), 3, 20, NOW(), 'system', 1),
(@attachment_id, 'test_files', '测试文件', CONCAT('/', @root_id, '/', @attachment_id, '/18'), 3, 30, NOW(), 'system', 1);

-- 系统文件子分类  
SET @system_id = (SELECT id FROM system_field_category WHERE code = 'system_files' AND parent_id = @root_id);
INSERT INTO system_field_category (
    parent_id, code, name, tree_path, level, sort, 
    create_time, creator, tenant_id
) VALUES 
(@system_id, 'config_files', '配置文件', CONCAT('/', @root_id, '/', @system_id, '/19'), 3, 10, NOW(), 'system', 1),
(@system_id, 'log_files', '日志文件', CONCAT('/', @root_id, '/', @system_id, '/20'), 3, 20, NOW(), 'system', 1),
(@system_id, 'backup_files', '备份文件', CONCAT('/', @root_id, '/', @system_id, '/21'), 3, 30, NOW(), 'system', 1);
```

### 2. 文件信息表设计

```sql
-- 文件信息表（复用现有分类管理）
CREATE TABLE IF NOT EXISTS sys_file_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文件ID',
    category_id BIGINT NOT NULL COMMENT '分类ID，关联system_field_category.id',
    business_id VARCHAR(64) DEFAULT NULL COMMENT '业务关联ID（如用户ID、项目ID等）',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    stored_name VARCHAR(255) NOT NULL COMMENT '存储文件名',
    file_path VARCHAR(512) NOT NULL COMMENT '文件存储路径',
    file_size BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小（字节）',
    mime_type VARCHAR(100) DEFAULT NULL COMMENT 'MIME类型',
    file_extension VARCHAR(20) DEFAULT NULL COMMENT '文件扩展名',
    md5_hash VARCHAR(32) DEFAULT NULL COMMENT 'MD5校验值',
    upload_status TINYINT NOT NULL DEFAULT 1 COMMENT '上传状态：1-成功，2-失败，3-处理中',
    access_level TINYINT NOT NULL DEFAULT 1 COMMENT '访问级别：1-公开，2-内部，3-私有',
    tags JSON DEFAULT NULL COMMENT '文件标签（JSON数组）',
    metadata JSON DEFAULT NULL COMMENT '文件元数据（JSON对象）',
    description TEXT DEFAULT NULL COMMENT '文件描述',
    download_count INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    last_access_time DATETIME DEFAULT NULL COMMENT '最后访问时间',
    expiry_time DATETIME DEFAULT NULL COMMENT '过期时间（NULL表示永不过期）',
    
    -- 基础字段
    creator VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT NULL COMMENT '更新者', 
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户ID',
    
    -- 索引
    INDEX idx_category_business (category_id, business_id),
    INDEX idx_tenant_creator (tenant_id, creator),
    INDEX idx_upload_time (create_time),
    INDEX idx_file_hash (md5_hash),
    INDEX idx_expiry (expiry_time),
    
    -- 外键约束
    FOREIGN KEY (category_id) REFERENCES system_field_category(id) ON UPDATE CASCADE
) COMMENT '文件信息表';
```

## 🔧 API接口设计

### 1. 分类管理API（复用现有）

```typescript
// 复用现有的分类管理API
import { getCategoryTree, createCategory, updateCategory, deleteCategory } from '@/api/system/fieldCategory'

// 获取文件管理分类树
export const getFileCategories = async (tenantId: number) => {
  return await getCategoryTree('file_management')
}

// 获取指定分类的配置
export const getFileCategoryConfig = async (categoryId: number) => {
  // 这里可以扩展获取分类的文件配置信息
  return {
    id: categoryId,
    maxFileSize: 100 * 1024 * 1024, // 100MB
    allowedTypes: ['*'], // 允许所有类型
    maxFiles: 1000,
    accessLevel: 1
  }
}
```

### 2. 文件管理API

```typescript
// 文件基础信息接口
export interface FileInfo {
  id: number
  categoryId: number
  businessId?: string
  originalName: string
  storedName: string
  filePath: string
  fileSize: number
  mimeType?: string
  fileExtension?: string
  md5Hash?: string
  uploadStatus: number
  accessLevel: number
  tags?: string[]
  metadata?: Record<string, any>
  description?: string
  downloadCount: number
  lastAccessTime?: string
  expiryTime?: string
  creator?: string
  createTime: string
  updater?: string
  updateTime: string
  tenantId: number
}

// 文件查询参数
export interface FileQueryParams {
  categoryId?: number
  businessId?: string
  fileName?: string
  fileType?: string
  minSize?: number
  maxSize?: number
  uploadTimeStart?: string
  uploadTimeEnd?: string
  tags?: string[]
  accessLevel?: number
  page: number
  size: number
  tenantId: number
}

// 文件上传参数
export interface FileUploadParams {
  categoryId: number
  businessId?: string
  file: File
  description?: string
  tags?: string[]
  accessLevel?: number
  expiryTime?: string
}
```

### 3. REST API端点

```typescript
// 文件管理API
export const FileApi = {
  // 获取文件列表
  getFileList: async (params: FileQueryParams) => {
    return await request.get({ 
      url: '/api/files/list', 
      params,
      headers: { 'tenant-id': params.tenantId }
    })
  },

  // 上传文件
  uploadFile: async (params: FileUploadParams) => {
    const formData = new FormData()
    formData.append('file', params.file)
    formData.append('categoryId', params.categoryId.toString())
    if (params.businessId) formData.append('businessId', params.businessId)
    if (params.description) formData.append('description', params.description)
    if (params.tags) formData.append('tags', JSON.stringify(params.tags))
    if (params.accessLevel) formData.append('accessLevel', params.accessLevel.toString())
    if (params.expiryTime) formData.append('expiryTime', params.expiryTime)

    return await request.post({
      url: '/api/files/upload',
      data: formData,
      headers: { 
        'Content-Type': 'multipart/form-data',
        'tenant-id': getCurrentTenantId()
      }
    })
  },

  // 下载文件
  downloadFile: async (fileId: number) => {
    return await request.download({
      url: `/api/files/download/${fileId}`,
      headers: { 'tenant-id': getCurrentTenantId() }
    })
  },

  // 删除文件
  deleteFile: async (fileId: number) => {
    return await request.delete({
      url: `/api/files/delete/${fileId}`,
      headers: { 'tenant-id': getCurrentTenantId() }
    })
  },

  // 批量操作
  batchDownload: async (fileIds: number[]) => {
    return await request.post({
      url: '/api/files/batch-download',
      data: { fileIds },
      headers: { 'tenant-id': getCurrentTenantId() }
    })
  },

  // 获取文件信息
  getFileInfo: async (fileId: number) => {
    return await request.get({
      url: `/api/files/info/${fileId}`,
      headers: { 'tenant-id': getCurrentTenantId() }
    })
  }
}
```

## 💻 Java后端实现

### 1. 文件信息实体类

```java
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file_info")
public class FileInfoDO extends TenantBaseDO {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    private String businessId;
    
    @NotBlank(message = "原始文件名不能为空")
    private String originalName;
    
    @NotBlank(message = "存储文件名不能为空")
    private String storedName;
    
    @NotBlank(message = "文件路径不能为空")
    private String filePath;
    
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;
    
    private String mimeType;
    private String fileExtension;
    private String md5Hash;
    
    @NotNull(message = "上传状态不能为空")
    private Integer uploadStatus;
    
    @NotNull(message = "访问级别不能为空")
    private Integer accessLevel;
    
    private String tags;  // JSON字符串
    private String metadata;  // JSON字符串
    private String description;
    
    @Builder.Default
    private Integer downloadCount = 0;
    
    private LocalDateTime lastAccessTime;
    private LocalDateTime expiryTime;
}
```

### 2. 文件服务实现

```java
@Service
@Validated
public class FileManagementService {
    
    @Autowired
    private FileInfoMapper fileInfoMapper;
    
    @Autowired
    private FieldCategoryService categoryService;
    
    /**
     * 上传文件
     */  
    @Transactional
    public Long uploadFile(FileUploadReqVO reqVO, MultipartFile file) {
        // 1. 验证分类是否存在且属于文件管理
        validateFileCategory(reqVO.getCategoryId());
        
        // 2. 验证文件
        validateFile(file, reqVO.getCategoryId());
        
        // 3. 生成文件存储信息
        String storedName = generateStoredName(file.getOriginalFilename());
        String filePath = generateFilePath(reqVO.getCategoryId(), storedName);
        
        // 4. 保存文件到存储
        saveFileToStorage(file, filePath);
        
        // 5. 保存文件信息到数据库
        FileInfoDO fileInfo = FileInfoDO.builder()
            .categoryId(reqVO.getCategoryId())
            .businessId(reqVO.getBusinessId())
            .originalName(file.getOriginalFilename())
            .storedName(storedName)
            .filePath(filePath)
            .fileSize(file.getSize())
            .mimeType(file.getContentType())
            .fileExtension(getFileExtension(file.getOriginalFilename()))
            .md5Hash(calculateMD5(file))
            .uploadStatus(1) // 成功
            .accessLevel(reqVO.getAccessLevel())
            .tags(JsonUtils.toJsonString(reqVO.getTags()))
            .description(reqVO.getDescription())
            .expiryTime(reqVO.getExpiryTime())
            .build();
            
        fileInfoMapper.insert(fileInfo);
        return fileInfo.getId();
    }
    
    /**
     * 获取文件列表
     */
    public PageResult<FileInfoVO> getFileList(FileQueryReqVO reqVO) {
        // 构建查询条件
        LambdaQueryWrapper<FileInfoDO> wrapper = new LambdaQueryWrapper<FileInfoDO>()
            .eq(reqVO.getCategoryId() != null, FileInfoDO::getCategoryId, reqVO.getCategoryId())
            .eq(StrUtil.isNotBlank(reqVO.getBusinessId()), FileInfoDO::getBusinessId, reqVO.getBusinessId())
            .like(StrUtil.isNotBlank(reqVO.getFileName()), FileInfoDO::getOriginalName, reqVO.getFileName())
            .eq(reqVO.getAccessLevel() != null, FileInfoDO::getAccessLevel, reqVO.getAccessLevel())
            .ge(reqVO.getMinSize() != null, FileInfoDO::getFileSize, reqVO.getMinSize())
            .le(reqVO.getMaxSize() != null, FileInfoDO::getFileSize, reqVO.getMaxSize())
            .between(reqVO.getUploadTimeStart() != null && reqVO.getUploadTimeEnd() != null,
                    FileInfoDO::getCreateTime, reqVO.getUploadTimeStart(), reqVO.getUploadTimeEnd())
            .orderByDesc(FileInfoDO::getCreateTime);
            
        // 分页查询
        Page<FileInfoDO> page = fileInfoMapper.selectPage(reqVO, wrapper);
        
        // 转换为VO
        return new PageResult<>(page.getRecords().stream()
            .map(this::convertToVO)
            .collect(Collectors.toList()), page.getTotal());
    }
    
    /**
     * 验证文件分类
     */
    private void validateFileCategory(Long categoryId) {
        FieldCategoryDO category = categoryService.getCategory(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 验证是否属于文件管理分类树
        if (!isFileManagementCategory(category)) {
            throw new BusinessException("分类不属于文件管理范围");
        }
    }
    
    private boolean isFileManagementCategory(FieldCategoryDO category) {
        // 通过treePath判断是否属于文件管理分类
        return category.getTreePath().contains("/file_management/") || 
               "file_management".equals(category.getCode());
    }
}
```

## 🎨 前端组件集成

### 1. 通用文件管理器组件

```vue
<template>
  <div class="universal-file-manager">
    <!-- 分类选择器 -->
    <div class="category-selector">
      <CategoryTree 
        :biz-type="'file_management'"
        @select="handleCategorySelect" 
      />
    </div>
    
    <!-- 文件列表区域 -->
    <div class="file-content">
      <FileToolbar 
        :selected-category="selectedCategory"
        @upload="handleUpload"
        @refresh="loadFileList"
      />
      
      <FileList 
        :files="fileList"
        :loading="loading"
        @download="handleDownload"
        @delete="handleDelete"
        @preview="handlePreview"
      />
      
      <Pagination 
        v-model:current="queryParams.page"
        v-model:pageSize="queryParams.size"
        :total="total"
        @change="loadFileList"
      />
    </div>
    
    <!-- 文件上传对话框 -->
    <FileUploadDialog
      v-model:visible="uploadVisible"
      :category-id="selectedCategory?.id"
      @success="handleUploadSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getCategoryTree } from '@/api/system/fieldCategory'
import { FileApi } from '@/api/system/fileManagement'
import CategoryTree from '@/views/system/field/FieldCategory/CategoryTree.vue'

const props = defineProps<{
  businessId?: string
  defaultCategoryCode?: string
}>()

const selectedCategory = ref()
const fileList = ref([])
const loading = ref(false)
const total = ref(0)
const uploadVisible = ref(false)

const queryParams = reactive({
  categoryId: undefined,
  businessId: props.businessId,
  page: 1,
  size: 20,
  tenantId: getCurrentTenantId()
})

const handleCategorySelect = (category) => {
  selectedCategory.value = category
  queryParams.categoryId = category.id
  queryParams.page = 1
  loadFileList()
}

const loadFileList = async () => {
  if (!queryParams.categoryId) return
  
  loading.value = true
  try {
    const result = await FileApi.getFileList(queryParams)
    fileList.value = result.data.records
    total.value = result.data.total
  } finally {
    loading.value = false
  }
}

const handleUpload = () => {
  if (!selectedCategory.value) {
    ElMessage.warning('请先选择分类')
    return
  }
  uploadVisible.value = true
}

const handleUploadSuccess = () => {
  uploadVisible.value = false
  loadFileList()
}

onMounted(() => {
  // 如果指定了默认分类，自动选择
  if (props.defaultCategoryCode) {
    // 根据code查找并选择分类
  }
})
</script>
```

### 2. 拖拽文件管理模块

```vue
<template>
  <div class="draggable-file-manager-module">
    <div class="module-header">
      <i class="el-icon-folder"></i>
      <span>文件管理</span>
      <el-dropdown @command="handleCommand">
        <el-button type="text">
          <i class="el-icon-setting"></i>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="config">配置</el-dropdown-item>
            <el-dropdown-item command="refresh">刷新</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    
    <div class="module-content">
      <UniversalFileManager
        :business-id="config.businessId"
        :default-category-code="config.categoryCode"
      />
    </div>
    
    <!-- 配置对话框 -->
    <el-dialog
      v-model="configVisible"
      title="文件管理配置"
      width="500px"
    >
      <FileManagerConfig
        v-model="config"
        @save="handleSaveConfig"
      />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import UniversalFileManager from './UniversalFileManager.vue'
import FileManagerConfig from './FileManagerConfig.vue'

const configVisible = ref(false)
const config = reactive({
  businessId: '',
  categoryCode: 'workspace_docs',
  maxFileSize: 100 * 1024 * 1024,
  allowedTypes: ['*'],
  showUpload: true,
  showDownload: true,
  showPreview: true
})

const handleCommand = (command: string) => {
  switch (command) {
    case 'config':
      configVisible.value = true
      break
    case 'refresh':
      // 刷新文件列表
      break
  }
}

const handleSaveConfig = () => {
  configVisible.value = false
  // 保存配置到本地存储或后端
}
</script>
```

## 📊 菜单配置参数

为每个文件管理相关页面提供菜单创建参数：

### 1. 主文件管理页面

```javascript
const fileManagementMenuConfig = {
  name: '文件管理',
  type: 2, // 菜单
  sort: 100,
  parentId: 0, // 根据实际父菜单ID设置
  path: '/files/management',
  icon: 'folder',
  component: 'files/management/index',
  componentName: 'FilesManagement',
  permission: 'files:management:query',
  status: 0, // 启用
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```

### 2. 文件分类管理页面

```javascript
const fileCategoryMenuConfig = {
  name: '文件分类管理',
  type: 2,
  sort: 110,
  parentId: fileManagementMenuId,
  path: '/files/category',
  icon: 'tree',
  component: 'files/category/index',
  componentName: 'FileCategory',
  permission: 'files:category:query',
  status: 0,
  visible: true,
  keepAlive: true,
  alwaysShow: false
}
```

### 3. 工作台文件页面

```javascript
const workspaceFilesMenuConfig = {
  name: '工作台文件',
  type: 2,
  sort: 120,
  parentId: workspaceMenuId, // 工作台菜单ID
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

### 4. 批量菜单创建工具函数

```javascript
/**
 * 批量创建文件管理相关菜单
 */
export const createFileManagementMenus = async () => {
  const menus = [
    fileManagementMenuConfig,
    fileCategoryMenuConfig,
    workspaceFilesMenuConfig
  ]
  
  for (const menuConfig of menus) {
    try {
      const result = await MenuApi.createMenu(menuConfig)
      console.log(`菜单 ${menuConfig.name} 创建成功，ID: ${result.data}`)
    } catch (error) {
      console.error(`菜单 ${menuConfig.name} 创建失败:`, error)
    }
  }
}
```

## 🚀 使用说明

### 1. 初始化步骤

1. **执行SQL脚本**: 运行上面的SQL脚本初始化文件分类
2. **创建菜单**: 使用提供的菜单配置参数创建相关菜单
3. **部署组件**: 将文件管理组件集成到项目中
4. **配置权限**: 为用户角色分配文件管理相关权限

### 2. 在设计器中使用

设计人员可以直接拖拽 `DraggableFileManagerModule` 到设计页面：

- **自动上下文识别**: 根据页面类型自动选择对应的文件分类
- **可视化配置**: 通过配置面板设置业务ID、分类等参数  
- **零代码集成**: 拖拽即可使用，无需编写代码

### 3. 编程方式集成

```typescript
// 在任何Vue组件中使用
import UniversalFileManager from '@/components/files/UniversalFileManager.vue'

// 在模板中使用
<UniversalFileManager
  business-id="project_001"
  default-category-code="design_docs"
/>
```

## 🔒 多租户支持

- **数据隔离**: 通过 `tenant_id` 字段实现租户间数据隔离
- **分类隔离**: 每个租户有独立的文件分类树
- **权限控制**: 支持租户级别的文件访问权限控制
- **存储隔离**: 文件存储路径包含租户标识

---

> **注意**: 此版本基于现有的 `system_field_category` 分类管理系统，充分复用了现有的分类管理功能和组件，减少了开发工作量，提高了系统的一致性。 