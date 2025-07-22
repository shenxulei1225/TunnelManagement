# 页面设计器 - 菜单管理配置参数

## 🎯 菜单配置概述

页面设计器功能包含两个主要模块：
1. **页面设计器** - 用于设计和构建页面
2. **页面模板** - 用于管理页面模板

## 📋 主菜单配置

### 页面设计器主菜单

| 参数名称 | 参数值 | 说明 |
|---------|--------|------|
| 菜单名称 | 页面设计器 | 显示在左侧菜单的名称 |
| 上级菜单 | 系统管理 | 选择父级菜单 |
| 菜单图标 | ep:edit-pen | Element Plus图标 |
| 路由地址 | page-designer | 前端路由路径 |
| 组件路径 | system/page-designer/index | Vue组件文件路径 |
| 权限标识 | system:page-designer:query | 查看权限标识 |
| 菜单类型 | 菜单 | 选择菜单类型 |
| 显示状态 | 显示 | 是否在菜单中显示 |
| 菜单状态 | 正常 | 菜单启用状态 |
| 排序 | 7 | 在同级菜单中的排序 |

### 页面模板主菜单

| 参数名称 | 参数值 | 说明 |
|---------|--------|------|
| 菜单名称 | 页面模板 | 显示在左侧菜单的名称 |
| 上级菜单 | 系统管理 | 选择父级菜单 |
| 菜单图标 | ep:files | Element Plus图标 |
| 路由地址 | page-template | 前端路由路径 |
| 组件路径 | system/page-template/index | Vue组件文件路径 |
| 权限标识 | system:page-template:query | 查看权限标识 |
| 菜单类型 | 菜单 | 选择菜单类型 |
| 显示状态 | 显示 | 是否在菜单中显示 |
| 菜单状态 | 正常 | 菜单启用状态 |
| 排序 | 8 | 在同级菜单中的排序 |

## 🔧 API调用参数

### 创建页面设计器主菜单

```javascript
// POST /system/menu/create
{
  "name": "页面设计器",
  "type": 2,
  "sort": 7,
  "parentId": 1,
  "path": "page-designer",
  "icon": "ep:edit-pen",
  "component": "system/page-designer/index",
  "componentName": "PageDesigner",
  "permission": "system:page-designer:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

### 创建页面模板主菜单

```javascript
// POST /system/menu/create
{
  "name": "页面模板",
  "type": 2,
  "sort": 8,
  "parentId": 1,
  "path": "page-template",
  "icon": "ep:files",
  "component": "system/page-template/index",
  "componentName": "PageTemplate",
  "permission": "system:page-template:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

## 🔐 权限按钮配置

### 页面设计器权限按钮

#### 查看权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面设计查询 |
| 权限标识 | system:page-designer:query |
| 按钮类型 | 按钮 |

#### 新增权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面设计新增 |
| 权限标识 | system:page-designer:create |
| 按钮类型 | 按钮 |

#### 修改权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面设计修改 |
| 权限标识 | system:page-designer:update |
| 按钮类型 | 按钮 |

#### 删除权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面设计删除 |
| 权限标识 | system:page-designer:delete |
| 按钮类型 | 按钮 |

#### 构建权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面构建 |
| 权限标识 | system:page-designer:build |
| 按钮类型 | 按钮 |

#### 部署权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面部署 |
| 权限标识 | system:page-designer:deploy |
| 按钮类型 | 按钮 |

### 页面模板权限按钮

#### 查看权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面模板查询 |
| 权限标识 | system:page-template:query |
| 按钮类型 | 按钮 |

#### 新增权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面模板新增 |
| 权限标识 | system:page-template:create |
| 按钮类型 | 按钮 |

#### 修改权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面模板修改 |
| 权限标识 | system:page-template:update |
| 按钮类型 | 按钮 |

#### 删除权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 页面模板删除 |
| 权限标识 | system:page-template:delete |
| 按钮类型 | 按钮 |

## 📝 批量创建权限按钮的API调用

### 页面设计器权限按钮

```javascript
const pageDesignerPermissions = [
  { name: "页面设计查询", permission: "system:page-designer:query", sort: 1 },
  { name: "页面设计新增", permission: "system:page-designer:create", sort: 2 },
  { name: "页面设计修改", permission: "system:page-designer:update", sort: 3 },
  { name: "页面设计删除", permission: "system:page-designer:delete", sort: 4 },
  { name: "页面构建", permission: "system:page-designer:build", sort: 5 },
  { name: "页面部署", permission: "system:page-designer:deploy", sort: 6 }
];

// 为每个权限创建子菜单
pageDesignerPermissions.forEach(async (perm) => {
  await fetch('/system/menu/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      "name": perm.name,
      "type": 3,
      "sort": perm.sort,
      "parentId": pageDesignerMenuId, // 页面设计器主菜单创建后返回的ID
      "path": "",
      "icon": "",
      "component": "",
      "componentName": "",
      "permission": perm.permission,
      "status": 0,
      "visible": true,
      "keepAlive": false,
      "alwaysShow": false
    })
  });
});
```

### 页面模板权限按钮

```javascript
const pageTemplatePermissions = [
  { name: "页面模板查询", permission: "system:page-template:query", sort: 1 },
  { name: "页面模板新增", permission: "system:page-template:create", sort: 2 },
  { name: "页面模板修改", permission: "system:page-template:update", sort: 3 },
  { name: "页面模板删除", permission: "system:page-template:delete", sort: 4 }
];

// 为每个权限创建子菜单
pageTemplatePermissions.forEach(async (perm) => {
  await fetch('/system/menu/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      "name": perm.name,
      "type": 3,
      "sort": perm.sort,
      "parentId": pageTemplateMenuId, // 页面模板主菜单创建后返回的ID
      "path": "",
      "icon": "",
      "component": "",
      "componentName": "",
      "permission": perm.permission,
      "status": 0,
      "visible": true,
      "keepAlive": false,
      "alwaysShow": false
    })
  });
});
```

## 🎯 菜单创建状态跟踪

| 菜单名称 | 路径 | 创建状态 | 菜单ID | 创建时间 | 备注 |
|----------|------|----------|--------|----------|------|
| 页面设计器 | `/system/page-designer` | 📋 待创建 | - | - | 主菜单 |
| 页面模板 | `/system/page-template` | 📋 待创建 | - | - | 主菜单 |

## 📋 使用说明

### 1. 创建主菜单
1. 进入菜单管理：系统管理 → 菜单管理
2. 点击"新增"按钮
3. 选择"系统管理"作为上级菜单
4. 填入页面设计器主菜单参数
5. 保存并记录返回的菜单ID
6. 重复上述步骤创建页面模板主菜单

### 2. 创建权限按钮
1. 选择刚创建的页面设计器菜单
2. 点击"新增"按钮
3. 选择"按钮"类型
4. 依次创建所有权限按钮
5. 重复上述步骤为页面模板创建权限按钮

### 3. 权限分配
1. 进入角色管理：系统管理 → 角色管理
2. 选择需要分配权限的角色
3. 在权限配置中勾选页面设计器相关权限
4. 保存权限配置

## 🔧 故障排除

### 常见问题

**问题1：菜单创建后不可见**
```
原因：菜单状态或可见性配置错误
解决方案：
1. 检查菜单状态是否为"正常"
2. 确认可见性设置为"显示"
3. 验证父菜单是否正确
```

**问题2：权限按钮不显示**
```
原因：权限按钮配置错误
解决方案：
1. 确认按钮类型为"按钮"
2. 检查父菜单是否正确
3. 验证权限标识格式
```

**问题3：组件路径错误**
```
原因：Vue组件路径配置错误
解决方案：
1. 确认组件文件存在
2. 检查路径格式：system/page-designer/index
3. 验证组件名称与defineOptions中的name一致
```

## 📊 最佳实践

1. **菜单排序**：使用合理的排序号，避免与现有菜单冲突
2. **图标选择**：使用Element Plus图标，格式为`ep:图标名`
3. **权限标识**：遵循`模块:功能:操作`的命名规范
4. **组件路径**：相对于`src/views/`的路径
5. **缓存配置**：主菜单建议开启缓存，按钮不缓存

## 🎯 自动化菜单创建

可以使用以下工具类自动创建菜单：

```javascript
import { MenuCreator } from '@/utils/menuCreator'

// 创建页面设计器菜单
await MenuCreator.createPageMenu({
  pageName: "页面设计器",
  modulePrefix: "system",
  parentMenuId: 1, // 系统管理
  routePath: "page-designer",
  componentPath: "system/page-designer/index",
  componentName: "PageDesigner",
  icon: "ep:edit-pen",
  sort: 7,
  permissions: ["query", "create", "update", "delete", "build", "deploy"]
})

// 创建页面模板菜单
await MenuCreator.createPageMenu({
  pageName: "页面模板",
  modulePrefix: "system",
  parentMenuId: 1, // 系统管理
  routePath: "page-template",
  componentPath: "system/page-template/index",
  componentName: "PageTemplate",
  icon: "ep:files",
  sort: 8,
  permissions: ["query", "create", "update", "delete"]
})
``` 