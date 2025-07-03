# UX设计器工作台菜单配置

## 概述

本文档提供UX设计器工作台页面创建菜单时所需的手工配置参数，用于通过菜单管理API接口自动创建。

## 📋 主菜单配置参数

### 1. UX设计器主菜单

```json
{
  "name": "UX设计器",
  "type": 1,
  "sort": 5000,
  "parentId": 0,
  "path": "ux-designer",
  "icon": "ep:brush",
  "component": "",
  "componentName": "",
  "permission": "",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": true
}
```

### 2. 工作台页面菜单

```json
{
  "name": "工作台",
  "type": 2,
  "sort": 5010,
  "parentId": 5000,
  "path": "workspace",
  "icon": "ep:monitor",
  "component": "ux-designer/workspace/index",
  "componentName": "UXDesignerWorkspace",
  "permission": "uxdesigner:workspace:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

## 🔐 权限按钮配置参数

### 查询权限
```json
{
  "name": "查询",
  "type": 3,
  "sort": 1,
  "parentId": "[工作台菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "uxdesigner:workspace-project:query",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

### 新增权限
```json
{
  "name": "新增",
  "type": 3,
  "sort": 2,
  "parentId": "[工作台菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "uxdesigner:workspace-project:create",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

### 编辑权限
```json
{
  "name": "编辑",
  "type": 3,
  "sort": 3,
  "parentId": "[工作台菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "uxdesigner:workspace-project:update",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

### 删除权限
```json
{
  "name": "删除",
  "type": 3,
  "sort": 4,
  "parentId": "[工作台菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "uxdesigner:workspace-project:delete",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

### 复制权限
```json
{
  "name": "复制",
  "type": 3,
  "sort": 5,
  "parentId": "[工作台菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "uxdesigner:workspace-project:create",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

## 🚀 API自动创建代码示例

```javascript
/**
 * 自动创建UX设计器工作台菜单
 */
const createUXDesignerWorkspaceMenus = async () => {
  try {
    // 1. 创建主菜单
    const mainMenuId = await MenuApi.createMenu({
      name: "UX设计器",
      type: 1,
      sort: 5000,
      parentId: 0,
      path: "ux-designer",
      icon: "ep:brush",
      component: "",
      componentName: "",
      permission: "",
      status: 0,
      visible: true,
      keepAlive: false,
      alwaysShow: true
    })

    // 2. 创建工作台页面菜单
    const workspaceMenuId = await MenuApi.createMenu({
      name: "工作台",
      type: 2,
      sort: 5010,
      parentId: mainMenuId,
      path: "workspace",
      icon: "ep:monitor",
      component: "ux-designer/workspace/index",
      componentName: "UXDesignerWorkspace",
      permission: "uxdesigner:workspace:query",
      status: 0,
      visible: true,
      keepAlive: true,
      alwaysShow: false
    })

         // 3. 创建权限按钮
     const permissions = [
       { name: "查询", permission: "uxdesigner:workspace-project:query", sort: 1 },
       { name: "新增", permission: "uxdesigner:workspace-project:create", sort: 2 },
       { name: "编辑", permission: "uxdesigner:workspace-project:update", sort: 3 },
       { name: "删除", permission: "uxdesigner:workspace-project:delete", sort: 4 },
       { name: "复制", permission: "uxdesigner:workspace-project:create", sort: 5 }
     ]

    for (const perm of permissions) {
      await MenuApi.createMenu({
        name: perm.name,
        type: 3,
        sort: perm.sort,
        parentId: workspaceMenuId,
        path: "",
        icon: "",
        component: "",
        componentName: "",
        permission: perm.permission,
        status: 0,
        visible: true,
        keepAlive: false,
        alwaysShow: false
      })
    }

    console.log('UX设计器工作台菜单创建成功')
  } catch (error) {
    console.error('菜单创建失败:', error)
  }
}
```

## 📊 菜单层次结构

```
UX设计器 (ep:brush)
└── 工作台 (ep:monitor)
    ├── 查询权限
    ├── 新增权限  
    ├── 编辑权限
    ├── 删除权限
    └── 复制权限（用于项目模板、版本管理）
```

## 🎯 配置说明

### 必填参数
- **name**: 菜单显示名称
- **type**: 菜单类型（1=目录，2=菜单，3=按钮）
- **sort**: 显示顺序
- **parentId**: 父菜单ID
- **status**: 状态（0=正常，1=停用）

### 核心配置
- **路由路径**: `ux-designer/workspace` 
- **组件路径**: `ux-designer/workspace/index`
- **组件名称**: `UXDesignerWorkspace`
- **权限前缀**: `uxdesigner:workspace-project`

### 图标说明
- 主菜单图标：`ep:brush` (画笔图标)
- 工作台图标：`ep:monitor` (显示器图标)

## ⚠️ 注意事项

1. **数据库准备**: 确保已执行 `20250125_create_uxd_workspace_project.sql` 脚本
2. **后端接口**: 确保 `WorkspaceProjectController` 已部署
3. **权限验证**: 权限标识需与后端 `@PreAuthorize` 注解匹配
4. **租户配置**: 菜单创建时确保使用正确的租户ID

## 🔗 相关文件

- 后端控制器: `cheers-module-uxdesigner/src/main/java/com/cheers/uxdesigner/controller/admin/workspace/WorkspaceProjectController.java`
- 前端页面: `tunnel-management-ui/src/views/ux-designer/workspace/index.vue`
- 数据库脚本: `sql/mysql/20250125_create_uxd_workspace_project.sql`
- API接口: `tunnel-management-ui/src/api/system/workspace/project.ts` 