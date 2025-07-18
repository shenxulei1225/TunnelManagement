# 字段语义目录管理菜单配置参数

## 功能概述
字段语义目录管理页面用于根据业务语义分类（如测量类、电气类、地理类等）对字段进行分组管理，帮助用户快速找到相关字段并避免重复创建。

## 菜单创建配置参数

### 基本信息
| 参数名称 | 值 | 必填 | 说明 |
|---------|---|------|------|
| 菜单名称 | 字段语义目录管理 | ✓ | 显示在菜单中的名称 |
| 菜单标识 | field-semantic-directory | ✓ | 唯一标识符 |
| 路径 | /system/field/semantic-directory | ✓ | 前端路由路径 |
| 组件路径 | system/field/FieldDef/FieldDefSemanticDirectory | ✓ | Vue组件路径 |
| 图标 | el-icon-folder-opened | - | 菜单图标 |
| 排序 | 1030 | - | 菜单排序值 |

### 层级结构
| 参数名称 | 值 | 必填 | 说明 |
|---------|---|------|------|
| 父级菜单 | 字段管理 | ✓ | 挂载到字段管理模块下 |
| 菜单类型 | 菜单 | ✓ | 类型为菜单页面 |
| 是否可见 | 是 | ✓ | 在菜单中显示 |
| 是否缓存 | 是 | - | 保持页面状态 |

### 权限配置
| 权限标识 | 权限名称 | 说明 |
|---------|---------|------|
| system:field-def:semantic-directory | 字段语义目录查看 | 查看语义目录和字段列表权限 |
| system:field-def:semantic-directory:update | 字段语义目录更新 | 移动字段到语义目录的权限 |
| system:field-def:semantic-directory:batch | 字段语义目录批量更新 | 批量操作字段语义目录权限 |

### API接口路径
后端提供的API接口：
- GET `/admin-api/system/field-def/list-by-semantic-directory` - 根据语义目录获取字段列表
- PUT `/admin-api/system/field-def/update-semantic-directory` - 更新字段语义目录
- PUT `/admin-api/system/field-def/batch-update-semantic-directory` - 批量更新字段语义目录

### 页面功能特性
1. **左侧语义目录树**：显示8种预设语义分类（测量类、电气类、地理类、合同类、维护类、检查类、状态类、时间类）
2. **右侧字段管理区**：支持卡片视图和表格视图切换
3. **拖拽移动**：支持拖拽字段到不同语义目录
4. **搜索筛选**：支持目录搜索和字段搜索
5. **批量操作**：支持批量移动字段到指定语义目录

## 完整配置JSON（示例）

```json
{
  "name": "字段语义目录管理",
  "path": "/system/field/semantic-directory", 
  "component": "system/field/FieldDef/FieldDefSemanticDirectory",
  "meta": {
    "title": "字段语义目录管理",
    "icon": "el-icon-folder-opened",
    "keepAlive": true
  },
  "permissions": [
    "system:field-def:semantic-directory",
    "system:field-def:semantic-directory:update", 
    "system:field-def:semantic-directory:batch"
  ],
  "parentPath": "/system/field",
  "sort": 1030,
  "visible": true,
  "type": "MENU"
}
```

## 依赖的数据表和服务

### 数据表依赖
- `system_field_def` - 字段定义表（新增了semantic_directory_id字段）
- `tree_config` - 目录树配置表（businessType='FIELD_SEMANTIC'）

### 服务依赖
- `FieldDefService` - 字段定义服务
- `TreeConfigService` - 目录树配置服务
- `cheers-directory` - 目录管理框架模块

## 预设数据说明

### 语义目录分类
系统预设了8种语义分类：

1. **测量类 (measurement)** - 长度、面积、体积等测量相关字段
2. **电气类 (electrical)** - 电压、电流、功率等电气相关字段
3. **地理类 (geographic)** - 坐标、海拔、位置等地理相关字段
4. **合同类 (contract)** - 合同编号、当事方、金额等合同相关字段
5. **维护类 (maintenance)** - 维护周期、维护时间等维护相关字段
6. **检查类 (inspection)** - 检查结果、检查时间、检查人员等检查相关字段
7. **状态类 (status)** - 运行状态、健康状态等状态相关字段
8. **时间类 (temporal)** - 时间戳、日期等时间相关字段

### 预设字段定义
系统预设了25+常用字段，分布在各个语义分类中，包括：
- 测量类：长度、宽度、高度、面积、体积等
- 电气类：电压、电流、功率、电阻等
- 地理类：经度、纬度、海拔等
- 合同类：合同编号、合同方、合同金额等
- 维护类：维护周期、最后维护时间等
- 检查类：检查结果、检查时间、检查人员等
- 状态类：运行状态、健康状态等
- 时间类：创建时间、更新时间等

## 业务价值
1. **语义化管理**：按照业务语义分类组织字段，提高字段管理效率
2. **避免重复**：通过分类浏览快速找到现有字段，避免重复创建
3. **标准化**：预设常用字段和分类，促进字段定义标准化
4. **提升体验**：直观的树形结构和拖拽操作，提升用户体验 