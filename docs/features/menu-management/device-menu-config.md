# 设备档案管理 - 菜单管理配置参数

## 🔥 多租户支持

**重要更新**：设备档案管理现已支持多租户功能！

### 多租户特性
- ✅ **数据隔离**：不同租户的设备数据完全隔离，确保数据安全
- ✅ **设备编号唯一性**：设备编号在租户内唯一，不同租户可以使用相同编号
- ✅ **自动权限控制**：系统自动根据用户所属租户过滤数据
- ✅ **兼容性**：完全兼容现有单租户部署，升级无缝

### 数据库升级
如果您是从旧版本升级，请执行以下SQL脚本：
```sql
-- 执行 sql/mysql/20250702_device_add_tenant_support.sql
```

### 配置说明
1. **租户功能开启**：确保 `application.yaml` 中租户功能已启用
2. **数据迁移**：现有数据会自动分配给默认租户（ID=1）
3. **API调用**：前端请求需要携带正确的 `tenant-id` 头部

## 主菜单配置

### 设备档案管理主菜单

| 参数名称 | 参数值 | 说明 |
|---------|--------|------|
| 菜单名称 | 设备档案 | 显示在左侧菜单的名称 |
| 上级菜单 | 系统管理 | 选择父级菜单 |
| 菜单图标 | ep:monitor | Element Plus图标 |
| 路由地址 | device | 前端路由路径 |
| 组件路径 | system/device/index | Vue组件文件路径 |
| 权限标识 | system:device:query | 查看权限标识 |
| 菜单类型 | 菜单 | 选择菜单类型 |
| 显示状态 | 显示 | 是否在菜单中显示 |
| 菜单状态 | 正常 | 菜单启用状态 |
| 排序 | 6 | 在同级菜单中的排序 |

## 权限按钮配置

### 查看权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 设备查询 |
| 权限标识 | system:device:query |
| 按钮类型 | 按钮 |

### 新增权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 设备新增 |
| 权限标识 | system:device:create |
| 按钮类型 | 按钮 |

### 修改权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 设备修改 |
| 权限标识 | system:device:update |
| 按钮类型 | 按钮 |

### 删除权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 设备删除 |
| 权限标识 | system:device:delete |
| 按钮类型 | 按钮 |

### 导出权限
| 参数名称 | 参数值 |
|---------|--------|
| 按钮名称 | 设备导出 |
| 权限标识 | system:device:export |
| 按钮类型 | 按钮 |

## API接口自动创建示例代码

```javascript
// 菜单管理API调用示例
const createDeviceMenus = async () => {
  // 1. 创建主菜单
  const mainMenu = {
    name: '设备档案',
    parentId: 1, // 系统管理菜单ID
    icon: 'ep:monitor',
    path: 'device',
    component: 'system/device/index',
    permission: 'system:device:query',
    type: 1, // 菜单类型
    visible: true,
    status: 0, // 正常状态
    sort: 6
  }
  
  const menuId = await createMenu(mainMenu)
  
  // 2. 创建权限按钮
  const permissions = [
    { name: '设备查询', permission: 'system:device:query', parentId: menuId },
    { name: '设备新增', permission: 'system:device:create', parentId: menuId },
    { name: '设备修改', permission: 'system:device:update', parentId: menuId },
    { name: '设备删除', permission: 'system:device:delete', parentId: menuId },
    { name: '设备导出', permission: 'system:device:export', parentId: menuId }
  ]
  
  for (const perm of permissions) {
    await createMenu({
      ...perm,
      type: 3, // 按钮类型
      visible: false,
      status: 0,
      sort: 1
    })
  }
}
```

## 完整菜单层次结构

```
系统管理
├── 用户管理
├── 角色管理
├── 菜单管理
├── 部门管理
├── 岗位管理
├── 区域管理
└── 设备档案 ← 新增（支持多租户）
    ├── 设备查询
    ├── 设备新增
    ├── 设备修改
    ├── 设备删除
    └── 设备导出
```

## 路由配置示例

```javascript
// router/modules/system.js
const systemRoutes = [
  // ... 其他路由
  {
    path: '/system/device',
    component: () => import('@/views/system/device/index.vue'),
    name: 'SystemDevice',
    meta: {
      title: '设备档案',
      icon: 'ep:monitor',
      permission: ['system:device:query']
    }
  }
]
```

## 权限验证示例

```javascript
// 在组件中使用权限验证
<template>
  <el-button 
    v-hasPermi="['system:device:create']"
    @click="handleCreate"
  >
    新增设备
  </el-button>
</template>

// 在代码中验证权限
if (this.$hasPermission('system:device:update')) {
  // 执行更新操作
}
```

## 多租户API调用示例

```javascript
// 前端API调用（自动带租户头部）
import request from '@/config/axios'

// 查询当前租户的设备列表
const getDeviceList = async (params) => {
  return await request.get({ 
    url: '/system/device/list', 
    params,
    headers: {
      'tenant-id': getCurrentTenantId() // 自动添加租户ID
    }
  })
}
```

## 注意事项

### 🔒 多租户相关
1. **租户隔离**：确保API请求携带正确的 `tenant-id` 头部
2. **数据安全**：不同租户的设备数据完全隔离，无法跨租户访问
3. **设备编号**：同一设备编号可以在不同租户中使用
4. **权限控制**：权限验证会自动考虑租户范围

### 🛠️ 技术要求
1. **权限标识规范**：遵循 `模块:功能:操作` 的命名规范
2. **菜单排序**：确保在系统管理模块中合理排序
3. **图标选择**：使用Element Plus图标库中的图标
4. **路由路径**：确保与前端路由配置一致
5. **组件路径**：确保Vue组件文件存在且路径正确

## 验证方法

1. 创建菜单后，检查左侧导航菜单是否显示
2. 验证各个权限按钮是否正常工作
3. 测试不同角色的用户访问权限
4. 确保页面路由跳转正常
5. 验证权限控制是否生效
6. **多租户验证**：测试不同租户用户只能看到自己的设备数据

## 功能特点

- ✅ 支持多级设备管理（父子设备关系）
- ✅ 支持自定义字段存储（通过extraAttrs JSON字段）
- ✅ 完整的CRUD操作
- ✅ 设备状态管理（正常、停用、维修中、报废）
- ✅ 设备信息详情展示
- ✅ Excel导出功能
- ✅ **多租户数据隔离**
- ✅ **租户级别设备编号唯一性**
- ✅ **自动权限控制** 