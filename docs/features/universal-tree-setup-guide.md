# 通用树数据管理系统初始化指南

## 问题解决

如果您遇到 `Duplicate entry` 错误，说明数据库中已经存在相同的记录。请按照以下步骤解决：

## 初始化步骤

### 1. 创建数据库表

首先执行表结构创建脚本：

```sql
-- 执行 sql/mysql/20250703_create_universal_tree_tables.sql
```

### 2. 清理现有数据（如果需要重新初始化）

如果数据库中已经存在相关数据，请先执行清理脚本：

```sql
-- 执行 sql/mysql/20250703_clean_universal_tree_data.sql
```

### 3. 初始化数据

执行修复版本的初始化脚本：

```sql
-- 执行 sql/mysql/20250703_init_universal_tree_data_fixed.sql
```

## 脚本说明

### 1. 表结构脚本 (`20250703_create_universal_tree_tables.sql`)
- 创建 `system_tree_data_rel` 表：存储树数据关联
- 创建 `system_tree_config` 表：存储树配置
- 创建 `system_data_type_meta` 表：存储数据类型元数据

### 2. 清理脚本 (`20250703_clean_universal_tree_data.sql`)
- 清理现有的树数据关联记录
- 清理树配置记录
- 清理数据类型元数据记录

### 3. 初始化脚本 (`20250703_init_universal_tree_data_fixed.sql`)
- 使用 `INSERT IGNORE` 避免重复插入错误
- 插入字段分类树、分级组树、设备树的配置和数据
- 包含完整的测试数据

## 验证初始化

### 1. 检查表结构
```sql
-- 检查表是否创建成功
SHOW TABLES LIKE 'system_tree_%';
SHOW TABLES LIKE 'system_data_type_meta';
```

### 2. 检查配置数据
```sql
-- 检查树配置
SELECT * FROM system_tree_config;

-- 检查数据类型元数据
SELECT * FROM system_data_type_meta;
```

### 3. 检查树数据
```sql
-- 检查字段分类树数据
SELECT * FROM system_tree_data_rel WHERE tree_type = 'field_category';

-- 检查分级组树数据
SELECT * FROM system_tree_data_rel WHERE tree_type = 'hierarchy_group';

-- 检查设备树数据
SELECT * FROM system_tree_data_rel WHERE tree_type = 'device_tree';
```

## API测试

### 1. 测试字段分类树API
```bash
curl -X GET "http://localhost:48080/admin-api/system/tree/data/field_category" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2. 测试分级组树API
```bash
curl -X GET "http://localhost:48080/admin-api/system/tree/data/hierarchy_group" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 3. 测试设备树API
```bash
curl -X GET "http://localhost:48080/admin-api/system/tree/data/device_tree" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 预期响应

### 字段分类树响应示例
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "name": "基础信息",
      "type": "category",
      "level": 1,
      "sort": 1,
      "parentId": 0,
      "fieldCount": 3,
      "children": [
        {
          "id": 11,
          "name": "设备名称",
          "type": "field",
          "level": 2,
          "sort": 1,
          "parentId": 1,
          "fieldLabel": "设备名称",
          "fieldKey": "device_name",
          "fieldType": "string",
          "required": true,
          "categoryId": 1
        }
      ]
    }
  ],
  "msg": "操作成功"
}
```

## 常见问题

### 1. Duplicate entry 错误
**原因**：数据库中已存在相同的记录
**解决**：执行清理脚本后重新初始化

### 2. 表不存在错误
**原因**：未执行表结构创建脚本
**解决**：先执行 `20250703_create_universal_tree_tables.sql`

### 3. API返回空数据
**原因**：数据未正确初始化
**解决**：检查数据库连接，重新执行初始化脚本

### 4. 权限错误
**原因**：缺少访问权限
**解决**：确保使用正确的认证令牌

## 下一步

初始化完成后，您可以：

1. **测试前端页面**：访问字段管理页面，查看树形结构
2. **添加新树类型**：通过配置添加新的树类型
3. **扩展功能**：基于通用树系统开发更多功能

## 技术支持

如果遇到问题，请检查：
1. 数据库连接是否正常
2. 脚本是否按顺序执行
3. 后端服务是否正常启动
4. 前端API调用是否正确 