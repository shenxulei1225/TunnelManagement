# UI转换器菜单配置指南

## 📋 概述

本文档说明如何在系统中添加UI转换器的菜单配置，使其能够通过系统的菜单管理功能正常访问。

## 🚀 快速配置

### 方法一：通过管理界面配置

1. **登录系统管理后台**
2. **进入菜单管理**
   - 路径：`系统管理` → `菜单管理`
3. **添加主菜单**
   - 点击"新增"按钮
   - 填写以下信息：
     ```
     菜单名称: UI转换器
     菜单类型: 目录
     路由地址: /converter
     组件路径: Layout
     权限标识: converter:view
     菜单图标: magic-stick
     排序: 100
     状态: 正常
     ```
4. **添加子菜单**
   - 选择刚创建的"UI转换器"菜单
   - 点击"新增"按钮，添加子菜单：

   **转换演示页面：**
   ```
   菜单名称: 转换演示
   菜单类型: 菜单
   路由地址: /converter/demo
   组件路径: converter/demo
   权限标识: converter:demo:view
   菜单图标: cpu
   排序: 1
   状态: 正常
   ```

   **完整转换器页面：**
   ```
   菜单名称: 完整转换器
   菜单类型: 菜单
   路由地址: /converter/index
   组件路径: converter/ConverterView
   权限标识: converter:full:view
   菜单图标: magic-stick
   排序: 2
   状态: 正常
   ```

### 方法二：通过SQL脚本配置

1. **选择对应的数据库脚本**
   - MySQL: `sql/mysql/converter_menu.sql`
   - PostgreSQL: `sql/postgresql/converter_menu.sql`

2. **执行SQL脚本**
   ```bash
   # MySQL
   mysql -u username -p database_name < sql/mysql/converter_menu.sql
   
   # PostgreSQL
   psql -U username -d database_name -f sql/postgresql/converter_menu.sql
   ```

3. **验证配置**
   - 重启后端服务
   - 重新登录系统
   - 检查菜单是否正常显示

## 📁 文件结构

转换器相关的文件结构如下：

```
tunnel-management-ui/src/
├── core/converter/           # 转换器核心引擎
│   ├── index.ts             # 入口文件
│   ├── types.ts             # 类型定义
│   ├── UniversalConverter.ts # 通用转换器
│   ├── utils.ts             # 工具函数
│   ├── importers/           # 导入器
│   │   └── FigmaImporter.ts
│   └── converters/          # 转换器
│       └── VueConverter.ts
├── views/converter/          # 页面组件
│   ├── demo.vue             # 演示页面
│   ├── ConverterView.vue    # 完整转换器页面
│   └── index.vue            # 索引页面
└── ...
```

## 🔧 组件路径说明

在菜单配置中，组件路径对应关系如下：

| 组件路径 | 实际文件路径 | 说明 |
|---------|-------------|------|
| `converter/demo` | `src/views/converter/demo.vue` | 转换演示页面 |
| `converter/ConverterView` | `src/views/converter/ConverterView.vue` | 完整转换器 |
| `converter/index` | `src/views/converter/index.vue` | 索引页面 |

## 🛡️ 权限配置

### 权限标识说明

- `converter:view` - UI转换器模块查看权限
- `converter:demo:view` - 转换演示页面访问权限
- `converter:full:view` - 完整转换器页面访问权限

### 角色权限分配

确保用户角色具有相应的权限：

1. **进入角色管理**
   - 路径：`系统管理` → `角色管理`
2. **编辑角色权限**
   - 选择目标角色
   - 在权限分配中勾选UI转换器相关权限
3. **保存配置**

## 🎨 图标说明

系统使用Element Plus图标，可选图标包括：

- `magic-stick` - 魔法棒图标（主菜单推荐）
- `cpu` - CPU图标（演示页面推荐）
- `document-copy` - 文档复制图标
- `tools` - 工具图标
- `edit` - 编辑图标

## 🐛 常见问题

### 1. 菜单不显示

**可能原因：**
- 权限配置不正确
- 组件路径错误
- 缓存问题

**解决方案：**
- 检查用户角色权限
- 验证组件路径是否存在
- 清除浏览器缓存并重新登录

### 2. 页面加载失败

**可能原因：**
- 组件文件不存在
- 导入路径错误
- TypeScript类型错误

**解决方案：**
- 确认文件路径正确
- 检查控制台错误信息
- 修复TypeScript类型问题

### 3. 转换功能异常

**可能原因：**
- 转换器引擎未正确导入
- 依赖模块缺失

**解决方案：**
- 检查转换器模块导入
- 安装缺失的依赖包

## 📝 测试步骤

1. **菜单显示测试**
   - 登录系统
   - 检查左侧菜单是否显示"UI转换器"
   - 展开菜单检查子项

2. **页面访问测试**
   - 点击"转换演示"菜单
   - 验证页面是否正常加载
   - 测试基本功能

3. **转换功能测试**
   - 选择示例模板
   - 配置转换选项
   - 执行转换操作
   - 检查生成结果

## 🎯 下一步

配置完成后，您可以：

1. **访问转换演示页面** - 体验基本转换功能
2. **使用完整转换器** - 进行复杂的设计转换
3. **扩展转换器功能** - 添加新的导入器和转换器
4. **自定义模板** - 创建更多设计模板

## 📞 技术支持

如果在配置过程中遇到问题，请：

1. 检查控制台错误信息
2. 查看系统日志
3. 参考本文档的常见问题部分
4. 联系技术支持团队 