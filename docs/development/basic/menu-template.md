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

## 🚀 JavaScript自动创建工具

### 菜单创建工具类
```javascript
/**
 * 菜单自动创建工具类
 */
export class MenuCreator {
  
  /**
   * 创建完整的页面菜单配置（包含主菜单、子页面、权限按钮）
   * @param {Object} config - 配置对象
   * @returns {Promise<Object>} 创建结果
   */
  static async createPageMenuComplete(config) {
    const {
      // 主菜单配置
      mainMenuName,
      mainMenuIcon = 'ep:menu',
      mainMenuSort = Date.now(),
      mainMenuParentId = 0,
      
      // 页面配置
      pageName,
      pagePath,
      pageIcon = 'ep:document',
      pageComponent,
      pageComponentName,
      modulePrefix,
      
      // 权限配置
      permissions = ['query', 'create', 'update', 'delete', 'export']
    } = config
    
    try {
      // 1. 创建主菜单（目录）
      const mainMenuId = await MenuApi.createMenu({
        name: mainMenuName,
        type: 1, // 目录
        sort: mainMenuSort,
        parentId: mainMenuParentId,
        path: pagePath.split('/')[1] || pagePath,
        icon: mainMenuIcon,
        component: '',
        componentName: '',
        permission: '',
        status: 0,
        visible: true,
        keepAlive: false,
        alwaysShow: true
      })
      
      console.log(`✅ 主菜单创建成功: ${mainMenuName} (ID: ${mainMenuId})`)
      
      // 2. 创建页面菜单
      const pageMenuId = await MenuApi.createMenu({
        name: pageName,
        type: 2, // 菜单
        sort: mainMenuSort + 10,
        parentId: mainMenuId,
        path: pagePath.split('/').pop() || pagePath,
        icon: pageIcon,
        component: pageComponent,
        componentName: pageComponentName,
        permission: `${modulePrefix}:${pagePath.split('/').pop()}:query`,
        status: 0,
        visible: true,
        keepAlive: true,
        alwaysShow: false
      })
      
      console.log(`✅ 页面菜单创建成功: ${pageName} (ID: ${pageMenuId})`)
      
      // 3. 创建权限按钮
      const permissionIds = []
      const permissionNames = {
        query: '查询',
        create: '新增', 
        update: '编辑',
        delete: '删除',
        export: '导出'
      }
      
      for (let i = 0; i < permissions.length; i++) {
        const perm = permissions[i]
        const permId = await MenuApi.createMenu({
          name: permissionNames[perm] || perm,
          type: 3, // 按钮
          sort: i + 1,
          parentId: pageMenuId,
          path: '',
          icon: '',
          component: '',
          componentName: '',
          permission: `${modulePrefix}:${pagePath.split('/').pop()}:${perm}`,
          status: 0,
          visible: true,
          keepAlive: false,
          alwaysShow: false
        })
        permissionIds.push(permId)
        console.log(`✅ 权限按钮创建成功: ${permissionNames[perm]} (ID: ${permId})`)
      }
      
      return {
        success: true,
        data: {
          mainMenuId,
          pageMenuId,
          permissionIds
        },
        message: '菜单创建完成'
      }
      
    } catch (error) {
      console.error('❌ 菜单创建失败:', error)
      return {
        success: false,
        error: error.message
      }
    }
  }
  
  /**
   * 快速创建单个页面菜单
   * @param {Object} config - 页面配置
   */
  static async createSinglePage(config) {
    const {
      name,
      parentId = 0,
      path,
      icon = 'ep:document',
      component,
      componentName,
      permission,
      sort = Date.now()
    } = config
    
    return await MenuApi.createMenu({
      name,
      type: 2,
      sort,
      parentId,
      path,
      icon,
      component,
      componentName,
      permission,
      status: 0,
      visible: true,
      keepAlive: true,
      alwaysShow: false
    })
  }
}

// 使用示例
const example = async () => {
  // 创建完整的用户管理菜单
  await MenuCreator.createPageMenuComplete({
    mainMenuName: '系统管理',
    mainMenuIcon: 'ep:setting',
    pageName: '用户管理', 
    pagePath: '/system/user',
    pageIcon: 'ep:user',
    pageComponent: 'system/user/index',
    pageComponentName: 'SystemUser',
    modulePrefix: 'system'
  })
  
  // 创建单个页面
  await MenuCreator.createSinglePage({
    name: '个人资料',
    parentId: 1000,
    path: 'profile',
    component: 'system/user/profile',
    componentName: 'UserProfile',
    permission: 'system:user:profile'
  })
}
```

### 批量菜单创建工具
```javascript
/**
 * 批量创建菜单工具
 */
export const batchCreateMenus = async (menuConfigs) => {
  const results = []
  
  for (const config of menuConfigs) {
    try {
      const menuId = await MenuApi.createMenu(config)
      results.push({
        success: true,
        config,
        menuId,
        message: `菜单 ${config.name} 创建成功`
      })
      console.log(`✅ ${config.name} 创建成功 (ID: ${menuId})`)
    } catch (error) {
      results.push({
        success: false,
        config,
        error: error.message,
        message: `菜单 ${config.name} 创建失败`
      })
      console.error(`❌ ${config.name} 创建失败:`, error)
    }
  }
  
  return results
}
```

## 📋 每个新增页面的菜单参数清单

**🔴 每次生成新页面时，请提供以下菜单创建参数：**

```javascript
// 页面菜单参数模板
const pageMenuParams = {
  // 【必填】基础信息
  name: '页面显示名称',           // 例：'用户管理'
  type: 2,                      // 固定值：菜单类型
  sort: 时间戳或序号,            // 例：Date.now()
  parentId: 父菜单ID,           // 例：1000
  
  // 【必填】路由信息  
  path: '路由路径',              // 例：'user' 或 '/system/user'
  component: '组件路径',         // 例：'system/user/index'
  componentName: '组件名称',     // 例：'SystemUser'
  
  // 【必填】权限信息
  permission: '权限标识',        // 例：'system:user:query'
  
  // 【可选】显示配置
  icon: '图标名称',              // 例：'ep:user'
  status: 0,                    // 固定值：启用状态
  visible: true,                // 固定值：显示
  keepAlive: true,              // 固定值：缓存页面
  alwaysShow: false            // 固定值：不总是显示
}
```

## ⚡ 快速配置生成器

```javascript
/**
 * 根据页面信息快速生成菜单配置
 */
export const generateMenuConfig = (pageName, modulePath, parentId = 0) => {
  const timestamp = Date.now()
  const pathParts = modulePath.split('/')
  const module = pathParts[0]
  const page = pathParts[pathParts.length - 1]
  
  return {
    name: pageName,
    type: 2,
    sort: timestamp,
    parentId: parentId,
    path: pathParts.length > 1 ? page : modulePath,
    icon: 'ep:document',
    component: `${modulePath}/index`,
    componentName: pathParts.map(p => 
      p.replace(/-/g, ' ').replace(/\b\w/g, l => l.toUpperCase()).replace(/\s/g, '')
    ).join(''),
    permission: `${module}:${page}:query`,
    status: 0,
    visible: true,
    keepAlive: true,
    alwaysShow: false
  }
}

// 使用示例
const userMenuConfig = generateMenuConfig('用户管理', 'system/user', 1000)
console.log('菜单配置:', userMenuConfig)
```

**📌 记住：每次生成页面后，都要调用菜单管理API创建对应的菜单项！**

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