# UX Designer 模块

## 模块介绍

UX Designer 是一个专业的用户体验设计工具模块，提供 Figma 风格的设计工作台，支持多平台导入导出功能。

品牌理念：**"Experience Everything, Export Everywhere"**（体验一切，导出无界）

## 主要功能

- 🎨 **工作台管理**：提供 Figma 风格的设计工作台
- 📁 **文件管理**：支持设计文件的创建、编辑、复制、删除等操作
- 📊 **项目管理**：项目组织和协作功能
- 🎭 **模板中心**：预制设计模板库
- 🧩 **组件库**：可重用的设计组件
- 💾 **资源管理**：设计资源的统一管理

## 数据库表结构

### 主要表
- `uxd_workspace_file`：工作台文件表
- `uxd_workspace_project`：工作台项目表

### 支持的数据库
- MySQL 5.7+
- PostgreSQL 10+
- Oracle 11g+
- SQL Server 2017+

## 菜单管理配置参数

为了实现设计自动生成页面时通过调用菜单管理的API接口自动创建，以下是每个页面创建时需要的**手工配置参数**：

### 1. UX Designer 主菜单
```json
{
  "name": "UX Designer",
  "permission": "",
  "type": 1,
  "sort": 50,
  "parent_id": 0,
  "path": "/uxdesigner",
  "icon": "ep:brush",
  "component": null,
  "component_name": null,
  "status": 0,
  "visible": 1,
  "keep_alive": 1,
  "always_show": 1
}
```

### 2. 工作台页面
```json
{
  "name": "工作台",
  "permission": "uxdesigner:workspace:query",
  "type": 2,
  "sort": 1,
  "parent_id": "[UX Designer主菜单ID]",
  "path": "workspace",
  "icon": "ep:monitor",
  "component": "uxdesigner/workspace/index",
  "component_name": "UXDesignerWorkspace",
  "status": 0,
  "visible": 1,
  "keep_alive": 1,
  "always_show": 1
}
```

### 3. 设计器页面
```json
{
  "name": "设计器",
  "permission": "uxdesigner:designer:query",
  "type": 2,
  "sort": 2,
  "parent_id": "[UX Designer主菜单ID]",
  "path": "designer",
  "icon": "ep:edit",
  "component": "uxdesigner/designer/index",
  "component_name": "UXDesignerCanvas",
  "status": 0,
  "visible": 1,
  "keep_alive": 1,
  "always_show": 1
}
```

### 4. 模板中心页面
```json
{
  "name": "模板中心",
  "permission": "uxdesigner:template:query",
  "type": 2,
  "sort": 3,
  "parent_id": "[UX Designer主菜单ID]",
  "path": "template",
  "icon": "ep:collection-tag",
  "component": "uxdesigner/template/index",
  "component_name": "UXDesignerTemplate",
  "status": 0,
  "visible": 1,
  "keep_alive": 1,
  "always_show": 1
}
```

### 5. 组件库页面
```json
{
  "name": "组件库",
  "permission": "uxdesigner:component:query",
  "type": 2,
  "sort": 4,
  "parent_id": "[UX Designer主菜单ID]",
  "path": "component",
  "icon": "ep:grid",
  "component": "uxdesigner/component/index",
  "component_name": "UXDesignerComponent",
  "status": 0,
  "visible": 1,
  "keep_alive": 1,
  "always_show": 1
}
```

### 6. 资源管理页面
```json
{
  "name": "资源管理",
  "permission": "uxdesigner:asset:query",
  "type": 2,
  "sort": 5,
  "parent_id": "[UX Designer主菜单ID]",
  "path": "asset",
  "icon": "ep:folder",
  "component": "uxdesigner/asset/index",
  "component_name": "UXDesignerAsset",
  "status": 0,
  "visible": 1,
  "keep_alive": 1,
  "always_show": 1
}
```

## 权限配置参数

### 工作台文件管理权限
```json
[
  {
    "name": "文件查询",
    "permission": "uxdesigner:workspace-file:query",
    "type": 3,
    "sort": 1,
    "parent_id": "[工作台菜单ID]"
  },
  {
    "name": "文件创建",
    "permission": "uxdesigner:workspace-file:create",
    "type": 3,
    "sort": 2,
    "parent_id": "[工作台菜单ID]"
  },
  {
    "name": "文件更新",
    "permission": "uxdesigner:workspace-file:update",
    "type": 3,
    "sort": 3,
    "parent_id": "[工作台菜单ID]"
  },
  {
    "name": "文件删除",
    "permission": "uxdesigner:workspace-file:delete",
    "type": 3,
    "sort": 4,
    "parent_id": "[工作台菜单ID]"
  },
  {
    "name": "文件导出",
    "permission": "uxdesigner:workspace-file:export",
    "type": 3,
    "sort": 5,
    "parent_id": "[工作台菜单ID]"
  }
]
```

## API 接口

### 工作台文件管理 API
- `POST /uxdesigner/workspace/file/create` - 创建文件
- `GET /uxdesigner/workspace/file/page` - 分页查询文件
- `GET /uxdesigner/workspace/file/get` - 获取文件详情
- `PUT /uxdesigner/workspace/file/update` - 更新文件
- `DELETE /uxdesigner/workspace/file/delete` - 删除文件
- `POST /uxdesigner/workspace/file/duplicate` - 复制文件
- `PUT /uxdesigner/workspace/file/star` - 收藏/取消收藏
- `PUT /uxdesigner/workspace/file/move-to-trash` - 移到回收站
- `PUT /uxdesigner/workspace/file/restore-from-trash` - 恢复文件

## 部署说明

1. **数据库初始化**：执行对应数据库的 SQL 文件
   - MySQL: `sql/mysql/uxdesigner_tables.sql`
   - PostgreSQL: `sql/postgresql/uxdesigner_tables.sql`

2. **模块依赖**：模块已添加到主项目的 `pom.xml` 和 `yudao-server` 中

3. **菜单配置**：执行 SQL 文件中的菜单管理配置，或使用上述参数手动创建

## 技术栈

- **后端**：Spring Boot 3.x + MyBatis Plus + Java 17
- **前端**：Vue 3 + TypeScript + Element Plus
- **数据库**：MySQL/PostgreSQL/Oracle/SQL Server
- **缓存**：Redis
- **权限**：Spring Security

## 包结构

```
com.cheers.uxdesigner
├── config/                 # 配置类
├── controller/             # 控制器层
│   └── admin/
│       └── workspace/
├── dal/                    # 数据访问层
│   ├── dataobject/        # 实体类
│   └── mysql/             # Mapper
├── service/                # 业务逻辑层
└── vo/                     # 视图对象
``` 