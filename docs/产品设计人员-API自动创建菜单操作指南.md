# 产品设计人员 - API自动创建菜单操作指南

## 📋 目录

1. [前置条件](#前置条件)
2. [认证问题解决](#认证问题解决)
3. [使用方法](#使用方法)
4. [常见问题](#常见问题)
5. [菜单参数说明](#菜单参数说明)

## 🔧 前置条件

### 1. 系统环境要求
- ✅ 已成功部署并运行管廊管理系统
- ✅ 具有管理员权限账户
- ✅ 能正常访问管理后台界面

### 2. 权限要求
- ✅ 具有菜单管理权限
- ✅ 具有系统管理权限
- ✅ 建议使用超级管理员账户

## 🔑 认证问题解决

### 问题：⚠️ 警告: 未找到登录认证信息，请先登录系统

#### 解决方案一：确保正确登录

1. **打开管理后台**
   ```
   http://你的域名/admin
   ```

2. **使用管理员账户登录**
   - 用户名：admin
   - 密码：admin123
   - 租户：芋道源码（默认）

3. **登录成功后，确认页面状态**
   - 确保能看到左侧菜单栏
   - 确保右上角显示用户信息
   - 确保页面URL包含 `/admin`

#### 解决方案二：手动获取认证token

如果仍然提示认证问题，请按以下步骤操作：

1. **打开浏览器开发者工具**
   - Windows/Linux：按 `F12`
   - Mac：按 `Cmd + Option + I`

2. **检查本地存储**
   ```javascript
   // 在控制台执行以下命令检查认证信息
   console.log('AccessToken:', localStorage.getItem('ACCESS_TOKEN'))
   console.log('RefreshToken:', localStorage.getItem('REFRESH_TOKEN'))
   ```

3. **如果token为空，重新登录**
   - 清除浏览器缓存
   - 重新登录管理后台
   - 再次检查token是否存在

## 🚀 使用方法

### 方法一：使用快速菜单创建工具

1. **加载工具脚本**
   
   在管理后台任意页面，按 `F12` 打开开发者工具，切换到 `Console` 标签页，复制粘贴以下代码：

   ```javascript
   // 快速菜单创建工具
   const MenuAPI = {
     async createMenu(data) {
       const token = localStorage.getItem('ACCESS_TOKEN') || sessionStorage.getItem('ACCESS_TOKEN')
       if (!token) {
         throw new Error('未找到认证token，请先登录系统')
       }
       
       const response = await fetch('/api/system/menu/create', {
         method: 'POST',
         headers: {
           'Content-Type': 'application/json',
           'Authorization': `Bearer ${token}`
         },
         body: JSON.stringify(data)
       })
       
       if (!response.ok) {
         throw new Error(`API请求失败: ${response.status}`)
       }
       
       const result = await response.json()
       if (result.code !== 0) {
         throw new Error(`创建失败: ${result.msg}`)
       }
       
       return result.data
     }
   }

   // 创建设备监控Dashboard菜单
   async function createDeviceMonitorMenu() {
     console.log('🚀 开始创建设备监控菜单...')
     
     try {
       // 1. 创建主菜单
       const mainMenu = await MenuAPI.createMenu({
         name: "设备监控",
         type: 2,
         sort: 7,
         parentId: 1,
         path: "device-monitor",
         icon: "ep:monitor",
         component: "system/device/monitor/index",
         componentName: "DeviceMonitorDashboard",
         permission: "system:device-monitor:query",
         status: 0,
         visible: true,
         keepAlive: true,
         alwaysShow: false
       })
       
       console.log(`✅ 主菜单创建成功 (ID: ${mainMenu})`)
       
       // 2. 创建权限按钮
       const permissions = [
         { name: "监控查询", code: "query" },
         { name: "报告导出", code: "export" },
         { name: "告警处理", code: "alert-handle" },
         { name: "设备控制", code: "control" }
       ]
       
       for (let i = 0; i < permissions.length; i++) {
         const perm = permissions[i]
         const permId = await MenuAPI.createMenu({
           name: perm.name,
           type: 3,
           sort: i + 1,
           parentId: mainMenu,
           path: "",
           icon: "",
           component: "",
           componentName: "",
           permission: `system:device-monitor:${perm.code}`,
           status: 0,
           visible: true,
           keepAlive: false,
           alwaysShow: false
         })
         console.log(`✅ 权限按钮创建成功: ${perm.name} (ID: ${permId})`)
       }
       
       console.log('🎉 设备监控菜单创建完成！')
       console.log('📝 请刷新页面查看新菜单')
       
     } catch (error) {
       console.error('❌ 创建失败:', error)
       alert(`创建失败: ${error.message}`)
     }
   }

   // 执行创建
   createDeviceMonitorMenu()
   ```

2. **执行结果**
   - 成功：控制台显示 "🎉 设备监控菜单创建完成！"
   - 失败：显示具体错误信息

### 方法二：使用菜单设计器界面

1. **访问菜单设计器**
   ```
   http://你的域名/admin/#/system/menu-designer
   ```

2. **选择创建方式**
   - 点击 "单页面菜单" 卡片
   - 填写菜单配置信息
   - 点击 "创建菜单" 按钮

3. **使用预设模板**
   - 点击 "设备监控Dashboard" 模板
   - 确认配置信息
   - 点击 "创建菜单" 按钮

## 🔍 常见问题

### Q1: 提示"API请求失败: 401"
**解决方案：**
- 检查是否正确登录
- 确认token是否过期
- 重新登录后再试

### Q2: 提示"创建失败: 菜单名称已存在"
**解决方案：**
- 修改菜单名称
- 或者先删除已存在的菜单

### Q3: 创建成功但看不到菜单
**解决方案：**
- 刷新页面（F5）
- 清除浏览器缓存
- 重新登录

### Q4: 权限按钮没有显示
**解决方案：**
- 确认用户角色是否有对应权限
- 检查权限配置是否正确
- 联系管理员分配权限

## 📊 菜单参数说明

### 主菜单参数

| 参数 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| name | string | ✅ | 菜单名称 | "设备监控" |
| type | number | ✅ | 菜单类型 | 2 (菜单) |
| sort | number | ✅ | 排序号 | 7 |
| parentId | number | ✅ | 父菜单ID | 1 |
| path | string | ✅ | 路由路径 | "device-monitor" |
| icon | string | ❌ | 图标 | "ep:monitor" |
| component | string | ✅ | 组件路径 | "system/device/monitor/index" |
| componentName | string | ✅ | 组件名称 | "DeviceMonitorDashboard" |
| permission | string | ✅ | 权限标识 | "system:device-monitor:query" |
| status | number | ✅ | 状态 | 0 (正常) |
| visible | boolean | ✅ | 是否显示 | true |
| keepAlive | boolean | ✅ | 是否缓存 | true |
| alwaysShow | boolean | ✅ | 始终显示 | false |

### 权限按钮参数

| 参数 | 类型 | 必填 | 说明 | 示例 |
|------|------|------|------|------|
| name | string | ✅ | 按钮名称 | "监控查询" |
| type | number | ✅ | 类型 | 3 (按钮) |
| sort | number | ✅ | 排序号 | 1 |
| parentId | number | ✅ | 父菜单ID | 主菜单ID |
| path | string | ✅ | 路径 | "" (空字符串) |
| icon | string | ✅ | 图标 | "" (空字符串) |
| component | string | ✅ | 组件 | "" (空字符串) |
| componentName | string | ✅ | 组件名 | "" (空字符串) |
| permission | string | ✅ | 权限标识 | "system:device-monitor:query" |
| status | number | ✅ | 状态 | 0 (正常) |
| visible | boolean | ✅ | 是否显示 | true |
| keepAlive | boolean | ✅ | 是否缓存 | false |
| alwaysShow | boolean | ✅ | 始终显示 | false |

## 🎯 设备监控Dashboard菜单配置示例

### 完整配置 JSON

```json
{
  "主菜单": {
    "name": "设备监控",
    "type": 2,
    "sort": 7,
    "parentId": 1,
    "path": "device-monitor",
    "icon": "ep:monitor",
    "component": "system/device/monitor/index",
    "componentName": "DeviceMonitorDashboard",
    "permission": "system:device-monitor:query",
    "status": 0,
    "visible": true,
    "keepAlive": true,
    "alwaysShow": false
  },
  "权限按钮": [
    {
      "name": "监控查询",
      "type": 3,
      "sort": 1,
      "parentId": "[主菜单ID]",
      "path": "",
      "icon": "",
      "component": "",
      "componentName": "",
      "permission": "system:device-monitor:query",
      "status": 0,
      "visible": true,
      "keepAlive": false,
      "alwaysShow": false
    },
    {
      "name": "报告导出",
      "type": 3,
      "sort": 2,
      "parentId": "[主菜单ID]",
      "path": "",
      "icon": "",
      "component": "",
      "componentName": "",
      "permission": "system:device-monitor:export",
      "status": 0,
      "visible": true,
      "keepAlive": false,
      "alwaysShow": false
    },
    {
      "name": "告警处理",
      "type": 3,
      "sort": 3,
      "parentId": "[主菜单ID]",
      "path": "",
      "icon": "",
      "component": "",
      "componentName": "",
      "permission": "system:device-monitor:alert-handle",
      "status": 0,
      "visible": true,
      "keepAlive": false,
      "alwaysShow": false
    },
    {
      "name": "设备控制",
      "type": 3,
      "sort": 4,
      "parentId": "[主菜单ID]",
      "path": "",
      "icon": "",
      "component": "",
      "componentName": "",
      "permission": "system:device-monitor:control",
      "status": 0,
      "visible": true,
      "keepAlive": false,
      "alwaysShow": false
    }
  ]
}
```

## 📞 技术支持

如果在使用过程中遇到问题，请：

1. **检查控制台错误信息**
2. **确认网络连接正常**
3. **验证账户权限**
4. **联系技术支持**

---

**注意事项：**
- 请在管理后台页面执行脚本
- 确保网络连接稳定
- 建议使用Chrome浏览器
- 操作前请备份重要数据 