# 通用树系统故障排除指南

## 问题诊断

### 1. API路径问题

**错误信息**: `请求地址不存在:admin-api/system/tree/data/field_category`

**原因**: 前端请求路径与后端控制器路径不匹配

**解决方案**:
- 后端控制器路径: `/system/tree-data/data/{treeType}` (避免与TreeController冲突)
- 前端API调用路径: `/system/tree-data/data/{treeType}`
- 系统会自动添加 `/admin-api` 前缀，最终路径为: `/admin-api/system/tree-data/data/{treeType}`

**路径配置说明**:
- 项目使用自动路径前缀配置
- `WebProperties.adminApi = new Api("/admin-api", "**.controller.admin.**")`
- 所有在 `**.controller.admin.**` 包下的 `@RestController` 类会自动获得 `/admin-api` 前缀
- 最终API路径 = `/admin-api` + `@RequestMapping` 路径

### 2. 前端组件错误

**错误信息**: `Failed to resolve component: Plus, FolderAdd, Refresh, Edit`

**原因**: 缺少图标组件导入

**解决方案**:
```typescript
import { Plus, FolderAdd, Refresh, Edit } from '@element-plus/icons-vue'
```

### 3. 数据库初始化问题

**错误信息**: `1062 - Duplicate entry 'field_category-0' for key 'system_tree_config.uk_tree_type'`

**原因**: 重复插入相同的数据

**解决方案**:
1. 先清理现有数据
2. 使用 `INSERT IGNORE` 避免重复插入

## 完整初始化步骤

### 步骤1: 清理现有数据
```sql
-- 执行清理脚本
source sql/mysql/20250703_clean_universal_tree_data.sql
```

### 步骤2: 初始化数据
```sql
-- 执行初始化脚本
source sql/mysql/20250703_init_universal_tree_data_fixed.sql
```

### 步骤3: 验证数据
```sql
-- 执行测试脚本
source sql/mysql/20250703_test_universal_tree.sql
```

### 步骤4: 重启后端服务
```bash
# 重启Spring Boot应用
```

### 步骤5: 测试API
```bash
# 测试API是否可访问
curl -X GET "http://localhost:48080/admin-api/system/tree-data/data/field_category" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 常见问题

### Q1: 为什么看不到任何数据？
A1: 检查以下几点：
- 数据库是否已初始化
- 后端服务是否启动
- API路径是否正确
- 前端网络请求是否成功

### Q2: 如何调试API问题？
A2: 
- 检查浏览器开发者工具的网络面板
- 查看后端日志
- 使用Postman测试API

### Q3: 如何添加新的树类型？
A3: 
1. 在 `system_tree_config` 表中添加配置
2. 在 `system_data_type_meta` 表中添加数据类型
3. 在 `system_tree_data_rel` 表中添加数据
4. 重启服务

## 调试工具

### 1. 数据库查询工具
```sql
-- 检查表结构
DESCRIBE system_tree_data_rel;
DESCRIBE system_tree_config;
DESCRIBE system_data_type_meta;

-- 检查数据
SELECT * FROM system_tree_data_rel WHERE tree_type = 'field_category';
```

### 2. API测试工具
```bash
# 使用curl测试API
curl -X GET "http://localhost:48080/admin-api/system/tree-data/data/field_category" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 3. 前端调试
```javascript
// 在浏览器控制台中测试API
fetch('/admin-api/system/tree-data/data/field_category', {
  headers: {
    'Authorization': 'Bearer YOUR_TOKEN'
  }
})
.then(response => response.json())
.then(data => console.log(data));
```

## 联系支持

如果问题仍然存在，请提供以下信息：
1. 错误信息截图
2. 浏览器开发者工具的网络面板截图
3. 后端日志
4. 数据库查询结果 