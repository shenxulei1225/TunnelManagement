# 业务模型只读字段实现总结

## 实现概述

根据设计决策，统一使用 `readonly` 字段来控制业务模型的删除权限，实现了灵活且语义清晰的权限控制机制。

## 核心变更

### 1. 数据库层面
- ✅ 添加了 `readonly` 字段到 `dynamic_business_model` 表
- ✅ 系统模型自动设置为只读
- ✅ 自定义模型默认可编辑
- ✅ 添加了索引以提高查询性能

### 2. 后端实现
- ✅ 更新了 `BusinessModelDO` 实体类
- ✅ 更新了所有 VO 类（BaseVO、CreateReqVO、UpdateReqVO、RespVO）
- ✅ 修改了删除逻辑，统一使用 `readonly` 字段检查
- ✅ 移除了基于 `modelType` 的删除检查
- ✅ 更新了错误码
- ✅ **移除了模型类型修改限制**
- ✅ **添加了模型类型变更时的自动处理逻辑**

### 3. 前端实现
- ✅ 删除按钮根据 `readonly` 字段显示/隐藏
- ✅ 添加了状态字段（启用/禁用）的表单控件
- ✅ 添加了状态标签和只读标签显示
- ✅ 更新了表单数据结构
- ✅ **模型类型字段在编辑时也可以修改**

## 业务逻辑

### 字段职责分离
- **modelType**：表示模型来源（0:系统，1:自定义）
- **status**：控制启用/禁用（0:禁用，1:启用）
- **readonly**：控制是否可删除（0:可编辑，1:只读）

### 前端功能增强
- ✅ 添加了模型类型选择（系统/自定义）
- ✅ 显示模型类型标签（蓝色系统标签，绿色自定义标签）
- ✅ 新增时默认为自定义类型
- ✅ 编辑时回显模型类型

### 删除权限控制
```java
// 删除时的逻辑
if (Boolean.TRUE.equals(model.getReadonly())) {
    throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_READONLY_CANNOT_DELETE);
}
```

### 模型类型变更自动处理
```java
// 模型类型修改时的自动处理
if (model.getModelType() != null && !Objects.equals(oldModel.getModelType(), model.getModelType())) {
    // 如果从自定义改为系统类型，自动设置为只读
    if (model.getModelType() == 0 && oldModel.getModelType() == 1) {
        model.setReadonly(true);
    }
    // 如果从系统改为自定义类型，保持只读状态不变（由用户手动控制）
}
```

### 数据迁移策略
```sql
-- 系统模型设置为只读
UPDATE dynamic_business_model SET readonly = 1 WHERE model_type = 0;
-- 自定义模型默认可编辑
UPDATE dynamic_business_model SET readonly = 0 WHERE model_type = 1;
```

## 优势

1. **语义清晰**：每个字段职责单一明确
2. **灵活控制**：可以独立设置每个模型的删除权限
3. **向后兼容**：保持现有功能不变
4. **扩展性好**：未来可以基于 readonly 扩展更多权限控制

## 测试用例

1. ✅ 创建可编辑模型 → 可以删除
2. ✅ 创建只读模型 → 不能删除
3. ✅ 系统模型 → 自动只读，不能删除
4. ✅ 状态变更 → 不影响删除权限
5. ✅ 只读变更 → 影响删除权限
6. ✅ **模型类型修改（自定义→系统）→ 自动设为只读**
7. ✅ **模型类型修改（系统→自定义）→ 保持只读状态**
8. ✅ **模型类型字段在编辑时可修改**

## 菜单创建参数

当通过菜单管理API自动创建业务模型管理页面时，需要提供以下参数：

```json
{
  "name": "业务模型管理",
  "path": "/dynamic/model", 
  "component": "dynamic/model/index",
  "permission": "dynamic:model:query",
  "type": 1,
  "icon": "ep:setting",
  "sort": 1,
  "status": 1
}
```

**子菜单权限**：
- `dynamic:model:create` - 创建权限
- `dynamic:model:update` - 更新权限  
- `dynamic:model:delete` - 删除权限
- `dynamic:model:query` - 查询权限

## 部署步骤

1. **执行数据库迁移**
   ```bash
   mysql -u username -p database_name < sql/mysql/20250703_add_readonly_field.sql
   ```

2. **重启后端服务**
   ```bash
   # 重新编译并启动服务
   mvn clean package
   java -jar target/cheers-server.jar
   ```

3. **刷新前端页面**
   - 清除浏览器缓存
   - 重新加载页面

4. **验证功能**
   - 测试创建、编辑、删除功能
   - 验证只读模型的删除保护
   - 检查状态和只读标签显示

## 注意事项

1. **数据迁移**：确保执行数据库迁移脚本
2. **权限检查**：删除操作会检查 readonly 字段
3. **前端显示**：只读模型会显示"只读"标签
4. **向后兼容**：现有功能不受影响 