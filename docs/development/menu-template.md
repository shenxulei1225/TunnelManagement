# 📋 菜单配置参数模板

⚠️ **多租户重要提醒：**
- 在多租户系统中创建菜单时，请确保登录状态下使用正确的租户ID
- 避免在租户ID为0的情况下创建菜单，这会导致菜单在系统中不可见
- 建议使用默认租户（ID: 1）进行菜单配置和测试

## 🎯 页面菜单配置模板

### 基础页面菜单配置

```json
{
  "name": "[页面名称]",
  "type": 2,
  "sort": [显示顺序，建议使用时间戳],
  "parentId": [上级菜单ID],
  "path": "[路由路径]",
  "icon": "[图标名称]",
  "component": "[组件路径]",
  "componentName": "[组件名称，PascalCase]",
  "permission": "[权限标识]",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

### UX设计器模块菜单配置示例

#### 主菜单配置
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

#### 工作台页面配置
```json
{
  "name": "设计工作台",
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

#### 设计器页面配置
```json
{
  "name": "可视化设计器",
  "type": 2,
  "sort": 5020,
  "parentId": 5000,
  "path": "designer",
  "icon": "ep:edit",
  "component": "ux-designer/designer/index",
  "componentName": "UXDesignerCanvas",
  "permission": "uxdesigner:designer:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

#### 组件库页面配置
```json
{
  "name": "组件库",
  "type": 2,
  "sort": 5030,
  "parentId": 5000,
  "path": "component",
  "icon": "ep:grid",
  "component": "ux-designer/component/index",
  "componentName": "UXDesignerComponent",
  "permission": "uxdesigner:component:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

#### 模板中心页面配置
```json
{
  "name": "模板中心",
  "type": 2,
  "sort": 5040,
  "parentId": 5000,
  "path": "template",
  "icon": "ep:collection-tag",
  "component": "ux-designer/template/index",
  "componentName": "UXDesignerTemplate",
  "permission": "uxdesigner:template:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

#### 资源管理页面配置
```json
{
  "name": "资源管理",
  "type": 2,
  "sort": 5050,
  "parentId": 5000,
  "path": "asset",
  "icon": "ep:folder",
  "component": "ux-designer/asset/index",
  "componentName": "UXDesignerAsset",
  "permission": "uxdesigner:asset:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

## 🔧 权限按钮配置模板

### 基础操作权限
```json
// 查询权限
{
  "name": "查询",
  "type": 3,
  "sort": 1,
  "parentId": [上级菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "[模块前缀]:[功能]:query",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}

// 新增权限
{
  "name": "新增",
  "type": 3,
  "sort": 2,
  "parentId": [上级菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "[模块前缀]:[功能]:create",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}

// 编辑权限
{
  "name": "编辑",
  "type": 3,
  "sort": 3,
  "parentId": [上级菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "[模块前缀]:[功能]:update",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}

// 删除权限
{
  "name": "删除",
  "type": 3,
  "sort": 4,
  "parentId": [上级菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "[模块前缀]:[功能]:delete",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}

// 导出权限
{
  "name": "导出",
  "type": 3,
  "sort": 5,
  "parentId": [上级菜单ID],
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "[模块前缀]:[功能]:export",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

## 🚀 快速生成工具

### JavaScript工具函数

```javascript
/**
 * 快速生成菜单配置
 */
function generateMenuConfig(options) {
  const {
    name,
    type = 2,
    parentId,
    path,
    icon = 'ep:document',
    component,
    modulePrefix,
    actionSuffix = 'query',
    sort
  } = options
  
  const componentName = component 
    ? component.split('/').map(part => 
        part.replace(/-/g, ' ').replace(/\b\w/g, l => l.toUpperCase()).replace(/\s/g, '')
      ).join('')
    : ''
  
  return {
    name,
    type,
    sort: sort || Date.now(),
    parentId,
    path: path || '',
    icon,
    component: component || '',
    componentName,
    permission: type === 2 ? `${modulePrefix}:${path}:${actionSuffix}` : '',
    status: 0,
    visible: true,
    keepAlive: type === 2,
    alwaysShow: type === 1
  }
}

// 使用示例
const menuConfig = generateMenuConfig({
  name: '用户管理',
  parentId: 1000,
  path: 'user',
  icon: 'ep:user',
  component: 'system/user/index',
  modulePrefix: 'system'
})

console.log('菜单配置：', JSON.stringify(menuConfig, null, 2))
```

## 📝 使用说明

1. **创建主菜单**：先创建 type=1 的目录菜单
2. **创建子菜单**：在主菜单下创建 type=2 的页面菜单
3. **创建权限按钮**：为页面菜单创建 type=3 的操作权限
4. **设置排序**：使用合理的 sort 值确保菜单顺序
5. **权限标识**：遵循 `模块:功能:操作` 的命名规范

## 🎯 每次生成页面时的提示模板

```
🎯 菜单管理配置参数：
{
  "name": "[页面名称]",
  "type": 2,
  "sort": [当前时间戳],
  "parentId": [上级菜单ID],
  "path": "[路由路径]",
  "icon": "[图标名称]",
  "component": "[组件路径]",
  "componentName": "[组件名称]",
  "permission": "[模块前缀]:[路由路径]:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}

📝 请复制以上配置到菜单管理系统中创建菜单
```

## 🔧 故障排除

### 多租户相关问题

**问题1：菜单创建后不可见**
```
原因：菜单在错误的租户下创建（通常是租户ID为0）
解决方案：
1. 检查当前登录状态的租户ID
2. 确保使用正确的租户账号登录系统
3. 重新在正确的租户下创建菜单
```

**问题2：权限分配异常**
```
原因：跨租户权限分配或权限标识符错误
解决方案：
1. 确认角色和菜单在同一租户下
2. 检查权限标识符格式是否正确
3. 验证权限层级关系
```

**问题3：菜单数据查询为空**
```
SQL检查：
SELECT * FROM system_menu WHERE tenant_id = 0; -- 检查是否有错误数据
UPDATE system_menu SET tenant_id = 1 WHERE tenant_id = 0; -- 修复错误数据（谨慎操作）
```

### 最佳实践提醒

1. ✅ **始终使用默认租户（ID: 1）进行系统菜单配置**
2. ✅ **创建菜单前确认当前租户上下文**
3. ✅ **定期检查菜单数据的租户ID一致性**
4. ❌ **避免在租户ID为0的状态下操作菜单**
5. ❌ **避免手动修改菜单的租户ID**