# 字段属性管理页面菜单配置参数

## ✨ 字段分类页面改进

### ✅ 新增编辑页面优化
基于用户需求，对字段分类的新增编辑页面进行了全面改进：

1. **🔧 隐藏字段Key列**
   - 在字段选择弹窗中移除了"字段Key"列
   - 简化界面显示，更关注用户友好的字段名称

2. **🌐 类型中文显示**
   - 所有字段类型现在都显示为中文
   - 支持：字符串、整数、小数、日期、枚举、布尔、图片、结构体、计算等
   - 兼容大小写变体，确保显示准确

3. **🔍 增强搜索功能**
   - **字段名称搜索**：支持按字段显示名称和字段Key搜索
   - **类型筛选器**：下拉选择特定字段类型进行过滤
   - **实时搜索**：输入即时过滤结果

4. **📋 示例效果展示**
   - **枚举字段**：显示选择器，预选第一个选项
   - **结构体字段**：显示标签列表
   - **计算字段**：显示计算结果示例
   - **图片字段**：显示"图片列表"标签
   - **其他类型**：显示对应的示例输入框
   - 与字段定义页面保持一致的视觉效果

### 🎯 界面布局优化
- **弹窗宽度扩展**：从600px扩展到800px，容纳更多列
- **表格最大高度**：设置400px，超出内容可滚动
- **列宽优化**：合理分配各列宽度，确保信息完整显示
- **操作体验**：保持原有的选择和确认操作流程

## 🔧 关键问题修复

### ✅ 修复1：字段编辑保存丢失分类关联问题
**问题**：编辑字段枚举值后，字段从分类中消失
**根因**：前端提交时删除了 `categoryIds` 字段
**修复**：保留 `categoryIds` 字段，确保更新时不丢失分类关联关系

### ✅ 修复2：区域编辑字段排序问题  
**问题**：区域编辑页面的自定义字段未按字段分类关系表中的排序显示
**根因**：只获取字段定义，没有获取关联表中的排序信息
**修复**：同时获取字段定义和分类关联信息，按关联表的 `sort` 字段排序

### ✅ 修复3：自定义字段保存失败问题
**问题**：填写自定义字段后保存失败
**根因**：MyBatis-Plus JSON 字段配置缺少 `autoResultMap = true`
**修复**：添加完整的 JSON 字段映射配置

## 自定义字段保存问题解决方案

### 问题原因
前端表单提交时的数据清理逻辑过于严格，会过滤掉`extraAttrs`中的自定义字段值，特别是空字符串、0、false等有效值被误判为无效数据。

### 解决方案
已修复`RegionForm.vue`中的`submitForm`方法，改进数据处理逻辑：
1. **专门处理extraAttrs**：只过滤undefined值，保留所有其他有效值
2. **精确控制字段**：明确指定要提交的字段，避免意外过滤
3. **增强调试信息**：添加详细的控制台日志，便于排查问题

## 菜单管理手动创建参数

### 主菜单配置

#### 字段属性管理主菜单
```json
{
  "name": "字段属性管理",
  "type": 2,
  "sort": 1005,
  "parentId": 1,
  "path": "field-def",
  "icon": "ep:baseball",
  "component": "system/field/FieldDef/index",
  "componentName": "FieldDef",
  "permission": "system:field-def:list",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

### 菜单管理创建参数详解

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| name | String | ✅ | 菜单显示名称 | "字段属性管理" |
| type | Integer | ✅ | 菜单类型：1=目录，2=菜单，3=按钮 | 2 |
| sort | Integer | ✅ | 显示顺序，数值越小越靠前 | 1005 |
| parentId | Long | ✅ | 父菜单ID，顶级菜单为1 | 1 |
| path | String | ✅ | 路由地址 | "field-def" |
| icon | String | ❌ | 菜单图标 | "ep:baseball" |
| component | String | ✅ | 组件路径 | "system/field/FieldDef/index" |
| componentName | String | ✅ | 组件名称 | "FieldDef" |
| permission | String | ✅ | 权限标识 | "system:field-def:list" |
| status | Integer | ✅ | 状态：0=正常，1=停用 | 0 |
| visible | Boolean | ✅ | 是否显示在菜单中 | true |
| keepAlive | Boolean | ✅ | 是否缓存 | true |
| alwaysShow | Boolean | ✅ | 是否总是显示 | false |

## 子菜单（权限按钮）配置参数

### 1. 查看权限
```json
{
  "name": "查看",
  "type": 3,
  "sort": 1,
  "parentId": "[字段属性管理菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "system:field-def:query",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

### 2. 新增权限
```json
{
  "name": "新增",
  "type": 3,
  "sort": 2,
  "parentId": "[字段属性管理菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "system:field-def:create",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

### 3. 修改权限
```json
{
  "name": "修改",
  "type": 3,
  "sort": 3,
  "parentId": "[字段属性管理菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "system:field-def:update",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

### 4. 删除权限
```json
{
  "name": "删除",
  "type": 3,
  "sort": 4,
  "parentId": "[字段属性管理菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "system:field-def:delete",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

### 5. 导出权限
```json
{
  "name": "导出",
  "type": 3,
  "sort": 5,
  "parentId": "[字段属性管理菜单ID]",
  "path": "",
  "icon": "",
  "component": "",
  "componentName": "",
  "permission": "system:field-def:export",
  "status": 0,
  "visible": true,
  "keepAlive": false,
  "alwaysShow": false
}
```

## API接口自动创建参数

### 创建主菜单的API调用
```javascript
// POST /system/menu/create
{
  "name": "字段属性管理",
  "type": 2,
  "sort": 1005,
  "parentId": 1,
  "path": "field-def",
  "icon": "ep:baseball",
  "component": "system/field/FieldDef/index",
  "componentName": "FieldDef",
  "permission": "system:field-def:list",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

### 批量创建权限按钮的API调用
```javascript
const permissions = [
  { name: "查看", permission: "system:field-def:query", sort: 1 },
  { name: "新增", permission: "system:field-def:create", sort: 2 },
  { name: "修改", permission: "system:field-def:update", sort: 3 },
  { name: "删除", permission: "system:field-def:delete", sort: 4 },
  { name: "导出", permission: "system:field-def:export", sort: 5 }
];

// 为每个权限创建子菜单
permissions.forEach(async (perm) => {
  await fetch('/system/menu/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      "name": perm.name,
      "type": 3,
      "sort": perm.sort,
      "parentId": mainMenuId, // 主菜单创建后返回的ID
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

## 字段说明

### 菜单类型说明
- `type: 1` - 目录
- `type: 2` - 菜单
- `type: 3` - 按钮（权限）

### 状态说明
- `status: 0` - 正常
- `status: 1` - 禁用

### 可见性说明
- `visible: true` - 显示
- `visible: false` - 隐藏

### 组件路径规则
- 菜单组件路径：`system/field/FieldDef/index`
- 按钮组件路径：留空
- 权限标识格式：`system:模块:操作`

## 使用注意事项

1. **parentId获取**：创建主菜单后，需要获取返回的菜单ID，用作子菜单的parentId
2. **权限验证**：确保创建的权限标识与后端控制器中的权限注解匹配  
3. **排序号**：建议主菜单使用1000+的排序号，避免与系统菜单冲突
4. **图标选择**：建议使用Element Plus图标，格式为`ep:图标名`
5. **组件名称**：ComponentName应与Vue组件的defineOptions中的name一致

## 菜单管理界面操作步骤

1. **进入菜单管理**: 系统管理 → 菜单管理
2. **添加主菜单**: 
   - 选择"系统管理"作为上级菜单
   - 填入上述主菜单参数
3. **添加权限按钮**:
   - 选择新创建的"字段属性管理"菜单
   - 依次添加查看、新增、修改、删除、导出权限

## 字段说明

- **name**: 菜单显示名称
- **type**: 菜单类型（1=目录，2=菜单，3=按钮）
- **sort**: 显示顺序
- **parentId**: 父菜单ID（1=系统管理）
- **path**: 路由地址
- **icon**: 菜单图标（使用Element Plus图标）
- **component**: 组件路径（相对于src/views/）
- **componentName**: 组件名称（用于缓存）
- **permission**: 权限标识（用于权限控制）
- **status**: 菜单状态（0=正常，1=停用）
- **visible**: 是否显示（true=显示，false=隐藏）
- **keepAlive**: 是否缓存（true=缓存，false=不缓存）
- **alwaysShow**: 是否总是显示（对于只有一个子菜单的情况）

## API接口自动创建调用参数

如果通过API自动创建菜单，可以使用以下调用：

```javascript
// 创建主菜单
await MenuApi.createMenu({
  name: "字段属性管理",
  type: 2,
  sort: 1005,
  parentId: 1,
  path: "field-def",
  icon: "ep:baseball",
  component: "system/field/FieldDef/index",
  componentName: "FieldDef",
  permission: "system:field-def:list",
  status: 0,
  visible: true,
  keepAlive: true,
  alwaysShow: false
});

// 获取创建的菜单ID后，创建权限按钮
const permissions = ["query", "create", "update", "delete", "export"];
for (const perm of permissions) {
  await MenuApi.createMenu({
    name: getPermissionName(perm),
    type: 3,
    sort: permissions.indexOf(perm) + 1,
    parentId: mainMenuId,
    path: "",
    icon: "",
    component: "",
    componentName: "",
    permission: `system:field-def:${perm}`,
    status: 0,
    visible: true,
    keepAlive: false,
    alwaysShow: false
  });
}
``` 