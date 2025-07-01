# DiagnosticAlert 诊断警告组件菜单配置

## 概述

本文档提供了为 `DiagnosticAlert` 诊断警告组件及其演示页面创建菜单时所需的手工配置参数。通过菜单管理 API 接口，可以自动创建相应的菜单项。

## 菜单创建参数

### 1. 诊断工具主菜单

用于创建"诊断工具"顶级菜单：

```json
{
  "name": "DiagnosticTools",
  "path": "/diagnostic",
  "component": "Layout",
  "redirect": "/diagnostic/alert-demo",
  "meta": {
    "title": "诊断工具",
    "icon": "ep:warning-filled",
    "noCache": false,
    "canTo": true,
    "hidden": false,
    "affix": false,
    "activeMenu": "",
    "breadcrumb": true,
    "permission": ["system:diagnostic:view"]
  },
  "parentId": null,
  "sort": 800,
  "status": 1,
  "type": 1,
  "visible": true
}
```

### 2. 诊断警告组件演示页面

用于创建诊断警告组件的演示页面菜单：

```json
{
  "name": "DiagnosticAlertDemo",
  "path": "/diagnostic/alert-demo",
  "component": "system/diagnostic/DiagnosticDemo",
  "meta": {
    "title": "诊断警告演示",
    "icon": "ep:warning",
    "noCache": false,
    "canTo": true,
    "hidden": false,
    "affix": false,
    "activeMenu": "",
    "breadcrumb": true,
    "permission": ["system:diagnostic:alert:view"]
  },
  "parentId": "DiagnosticTools菜单ID",
  "sort": 100,
  "status": 1,
  "type": 2,
  "visible": true
}
```

### 3. 组件文档页面（可选）

用于创建组件文档页面菜单：

```json
{
  "name": "DiagnosticAlertDocs",
  "path": "/diagnostic/alert-docs",
  "component": "system/diagnostic/DiagnosticDocs",
  "meta": {
    "title": "组件文档",
    "icon": "ep:document",
    "noCache": false,
    "canTo": true,
    "hidden": false,
    "affix": false,
    "activeMenu": "",
    "breadcrumb": true,
    "permission": ["system:diagnostic:docs:view"]
  },
  "parentId": "DiagnosticTools菜单ID",
  "sort": 200,
  "status": 1,
  "type": 2,
  "visible": true
}
```

## 参数说明

### 必填参数

- **name**: 菜单唯一标识，建议使用 PascalCase 命名
- **path**: 菜单路径，必须以 `/` 开头
- **component**: 组件路径，相对于 `src/views/` 目录
- **meta.title**: 菜单显示标题
- **parentId**: 父菜单ID（顶级菜单为 null）
- **sort**: 排序号，数字越小越靠前
- **status**: 菜单状态（1=启用，0=禁用）
- **type**: 菜单类型（1=目录，2=菜单，3=按钮）
- **visible**: 是否在菜单中显示

### 可选参数

- **redirect**: 重定向路径（目录类型菜单常用）
- **meta.icon**: 菜单图标，使用 Element Plus 图标
- **meta.noCache**: 是否不缓存页面（默认 false）
- **meta.canTo**: 是否可以跳转（默认 true）
- **meta.hidden**: 是否隐藏菜单（默认 false）
- **meta.affix**: 是否固定在标签页（默认 false）
- **meta.activeMenu**: 激活的菜单路径
- **meta.breadcrumb**: 是否显示面包屑（默认 true）
- **meta.permission**: 权限标识数组

## 权限配置

### 建议的权限层级结构

```
system:diagnostic:view          # 诊断工具模块查看权限
├── system:diagnostic:alert:view      # 诊断警告组件查看权限
├── system:diagnostic:alert:demo      # 演示页面访问权限
├── system:diagnostic:docs:view       # 文档查看权限
└── system:diagnostic:manage          # 诊断工具管理权限
```

### 权限分配建议

- **开发人员**: 拥有所有 `system:diagnostic:*` 权限
- **测试人员**: 拥有 `system:diagnostic:view` 和 `system:diagnostic:alert:demo` 权限
- **管理员**: 拥有 `system:diagnostic:manage` 权限
- **普通用户**: 根据需要分配特定权限

## API 调用示例

### 使用菜单管理 API 创建菜单

```javascript
// 创建诊断工具主菜单
const createDiagnosticMenu = async () => {
  const menuData = {
    name: "DiagnosticTools",
    path: "/diagnostic",
    component: "Layout",
    redirect: "/diagnostic/alert-demo",
    meta: {
      title: "诊断工具",
      icon: "ep:warning-filled",
      permission: ["system:diagnostic:view"]
    },
    parentId: null,
    sort: 800,
    status: 1,
    type: 1,
    visible: true
  }
  
  try {
    const response = await menuApi.create(menuData)
    console.log('诊断工具菜单创建成功:', response)
    return response.data.id
  } catch (error) {
    console.error('创建菜单失败:', error)
    throw error
  }
}

// 创建演示页面菜单
const createAlertDemoMenu = async (parentId) => {
  const menuData = {
    name: "DiagnosticAlertDemo",
    path: "/diagnostic/alert-demo",
    component: "system/diagnostic/DiagnosticDemo",
    meta: {
      title: "诊断警告演示",
      icon: "ep:warning",
      permission: ["system:diagnostic:alert:view"]
    },
    parentId: parentId,
    sort: 100,
    status: 1,
    type: 2,
    visible: true
  }
  
  try {
    const response = await menuApi.create(menuData)
    console.log('演示页面菜单创建成功:', response)
    return response.data.id
  } catch (error) {
    console.error('创建菜单失败:', error)
    throw error
  }
}

// 自动创建所有相关菜单
const setupDiagnosticMenus = async () => {
  try {
    // 1. 创建主菜单
    const parentMenuId = await createDiagnosticMenu()
    
    // 2. 创建演示页面菜单
    await createAlertDemoMenu(parentMenuId)
    
    console.log('所有诊断工具菜单创建完成')
  } catch (error) {
    console.error('菜单创建过程中出现错误:', error)
  }
}
```

## 批量创建脚本

以下是用于批量创建菜单的 SQL 脚本：

```sql
-- 创建诊断工具主菜单
INSERT INTO system_menu (
  name, path, component, redirect, type, visible, status, sort,
  icon, permission, create_time, update_time
) VALUES (
  'DiagnosticTools', '/diagnostic', 'Layout', '/diagnostic/alert-demo',
  1, 1, 1, 800,
  'ep:warning-filled', 'system:diagnostic:view',
  NOW(), NOW()
);

-- 获取刚创建的菜单ID
SET @parent_id = LAST_INSERT_ID();

-- 创建诊断警告演示页面菜单
INSERT INTO system_menu (
  name, path, component, parent_id, type, visible, status, sort,
  icon, permission, create_time, update_time
) VALUES (
  'DiagnosticAlertDemo', '/diagnostic/alert-demo', 'system/diagnostic/DiagnosticDemo',
  @parent_id, 2, 1, 1, 100,
  'ep:warning', 'system:diagnostic:alert:view',
  NOW(), NOW()
);

-- 创建组件文档页面菜单（可选）
INSERT INTO system_menu (
  name, path, component, parent_id, type, visible, status, sort,
  icon, permission, create_time, update_time
) VALUES (
  'DiagnosticAlertDocs', '/diagnostic/alert-docs', 'system/diagnostic/DiagnosticDocs',
  @parent_id, 2, 1, 1, 200,
  'ep:document', 'system:diagnostic:docs:view',
  NOW(), NOW()
);
```

## 验证清单

创建菜单后，请验证以下项目：

### 功能验证
- [ ] 菜单在侧边栏正确显示
- [ ] 菜单图标显示正常
- [ ] 点击菜单能够正确跳转
- [ ] 页面加载正常，组件渲染正确
- [ ] 面包屑导航显示正确

### 权限验证
- [ ] 有权限的用户可以看到菜单
- [ ] 无权限的用户看不到菜单
- [ ] 权限验证在页面级别生效
- [ ] 权限变更后立即生效

### 性能验证
- [ ] 页面加载速度正常
- [ ] 组件交互响应及时
- [ ] 内存使用正常，无明显泄漏

## 故障排除

### 常见问题

1. **菜单不显示**
   - 检查权限配置是否正确
   - 确认 `visible` 和 `status` 字段值
   - 验证父子菜单关系

2. **页面无法加载**
   - 检查 `component` 路径是否正确
   - 确认组件文件是否存在
   - 查看浏览器控制台错误信息

3. **权限验证失效**
   - 检查权限标识是否正确
   - 确认用户是否拥有相应权限
   - 验证权限中间件配置

### 调试建议

```javascript
// 在浏览器控制台中调试菜单配置
console.log('当前菜单配置:', JSON.stringify(menuConfig, null, 2))
console.log('用户权限:', userPermissions)
console.log('菜单权限验证结果:', hasPermission(menuConfig.meta.permission))
```

## 更新记录

- **v1.0.0** (2024-01): 初始版本，包含基础菜单配置
- **v1.1.0** (2024-02): 新增权限配置和API调用示例
- **v1.2.0** (2024-03): 增加批量创建脚本和故障排除指南 