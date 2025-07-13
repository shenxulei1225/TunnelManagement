# 业务模型只读字段实现说明

## 设计决策记录

### 类型选择讨论

在实现业务模型删除控制时，系统中存在两种类型实现：

#### 1. modelType（模型类型）
- **SYSTEM(0, "系统")** - 系统内置的模型
- **CUSTOM(1, "自定义")** - 用户自定义的模型

#### 2. readonly（只读字段）
- **false(0)** - 可编辑，可以删除
- **true(1)** - 只读，不能删除

### 方案对比分析

| 方案 | 优点 | 缺点 |
|------|------|------|
| **使用 modelType** | • 语义清晰，系统类型 vs 自定义类型<br>• 符合业务逻辑，系统模型不应该被删除<br>• 代码中已有相关逻辑 | • 不够灵活，系统类型就一定不能删除<br>• 无法处理"系统模型但允许删除"的特殊情况 |
| **使用 readonly** | • 更灵活，可以独立控制每个模型的删除权限<br>• 语义明确，专门用于控制删除权限<br>• 可以处理各种复杂场景 | • 需要额外的字段<br>• 增加了数据复杂度 |

### 最终决策：统一使用 readonly 字段

**决策理由：**

1. **语义更清晰**
   - `modelType` 表示模型的来源（系统 vs 自定义）
   - `readonly` 专门表示是否可删除
   - 两个概念独立，更符合单一职责原则

2. **更灵活**
   - 系统模型可以设置为可删除（特殊情况）
   - 自定义模型可以设置为只读（保护重要模型）
   - 支持更复杂的业务场景

3. **扩展性更好**
   - 未来可以基于 `readonly` 扩展更多权限控制
   - 比如：只读模型不能编辑、不能导出等

4. **数据迁移简单**
   - 系统模型自动设置为只读
   - 自定义模型默认可编辑
   - 保持向后兼容

**实现策略：**
```java
// 删除时的逻辑
@Override
public void deleteModel(Long id) {
    BusinessModelDO model = validateModelExists(id);
    
    // 只检查 readonly 字段
    if (Boolean.TRUE.equals(model.getReadonly())) {
        throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_READONLY_CANNOT_DELETE);
    }
    
    // 删除逻辑...
}
```

### 关于 modelType 字段的保留决策

经过进一步讨论，决定保留 `modelType` 字段，原因如下：

1. **业务语义清晰**
   - 系统模型：内置的、预定义的模型
   - 自定义模型：用户创建的模型
   - 这个区分在业务上有重要意义

2. **功能扩展性**
   - 系统模型可能有特殊处理逻辑
   - 比如：系统模型可能有默认配置、特殊权限等
   - 未来可能需要基于模型类型做不同的业务处理

3. **数据统计和分析**
   - 可以统计系统模型 vs 自定义模型的数量
   - 分析用户使用习惯
   - 系统管理需要

4. **UI展示需求**
   - 前端需要区分显示系统模型和自定义模型
   - 不同的图标、颜色、分组等

**字段职责最终分工：**
- **modelType**：表示模型来源（0:系统，1:自定义）
- **status**：控制启用/禁用（0:禁用，1:启用）
- **readonly**：控制是否可删除（0:可编辑，1:只读）

**数据迁移策略：**
```sql
-- 系统模型设置为只读
UPDATE dynamic_business_model SET readonly = 1 WHERE model_type = 0;
-- 自定义模型默认可编辑
UPDATE dynamic_business_model SET readonly = 0 WHERE model_type = 1;
```

---

## 概述

为业务模型管理功能添加了 `readonly` 字段，用于控制业务模型是否可以被删除。该字段与 `status` 字段完全独立，实现了两个不同的业务概念：

- **status（状态）**：控制业务模型是否启用（0:禁用，1:启用）
- **readonly（只读）**：控制业务模型是否可以被删除（0:可编辑，1:只读）

## 数据库变更

### 表结构变更

在 `dynamic_business_model` 表中添加了 `readonly` 字段：

```sql
ALTER TABLE `dynamic_business_model` 
ADD COLUMN `readonly` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否只读（0:可编辑，1:只读）' AFTER `status`;
```

### 数据迁移

- 系统类型的模型（`model_type = 0`）自动设置为只读
- 自定义类型的模型默认为可编辑

## 后端实现

### 1. 实体类更新

在 `BusinessModelDO` 中添加了 `readonly` 字段：

```java
/**
 * 是否只读（0:可编辑，1:只读）
 */
private Boolean readonly;
```

### 2. VO类更新

在 `DynamicModelBaseVO` 中添加了 `readonly` 字段：

```java
@Schema(description = "是否只读", example = "false")
private Boolean readonly;
```

### 3. 服务层逻辑

- **创建时**：默认为 `false`（可编辑）
- **更新时**：允许修改 `readonly` 字段
- **删除时**：检查 `readonly` 字段，只读模型不允许删除

### 4. 错误码

添加了新的错误码：

```java
ErrorCode BUSINESS_MODEL_READONLY_CANNOT_DELETE = new ErrorCode(1002002009, "只读的业务模型不允许删除");
```

## 前端实现

### 1. 删除按钮显示逻辑

```vue
<el-button
  v-if="!element.readonly"
  size="small"
  type="danger"
  class="full-btn"
  @click="onDelete(element)"
>删除</el-button>
```

### 2. 状态字段

在新增和编辑弹窗中添加了状态选择：

```vue
<el-form-item label="状态" prop="status">
  <el-radio-group v-model="form.status">
    <el-radio :label="1">启用</el-radio>
    <el-radio :label="0">禁用</el-radio>
  </el-radio-group>
</el-form-item>
```

### 3. 状态显示

在列表中显示状态标签：

```vue
<span class="status-tag" :class="element.status === 1 ? 'enabled' : 'disabled'">
  {{ element.status === 1 ? '启用' : '禁用' }}
</span>
```

## API接口

### 创建业务模型

```http
POST /dynamic/model/create
{
  "name": "测试模型",
  "code": "test_model",
  "description": "这是一个测试模型",
  "status": 1,
  "readonly": false
}
```

### 更新业务模型

```http
PUT /dynamic/model/update
{
  "id": 1,
  "name": "更新后的模型",
  "code": "updated_model",
  "description": "更新后的描述",
  "status": 1,
  "readonly": false
}
```

### 删除业务模型

```http
DELETE /dynamic/model/delete?id=1
```

**注意**：只读模型删除时会返回错误。

## 业务规则

1. **状态和只读独立**：状态控制启用/禁用，只读控制是否可删除
2. **系统模型只读**：系统类型的模型自动设置为只读
3. **自定义模型可编辑**：用户创建的模型默认为可编辑
4. **删除保护**：只读模型不允许删除，前端不显示删除按钮，后端也会校验

## 测试用例

1. 创建可编辑模型 → 可以删除
2. 创建只读模型 → 不能删除
3. 系统模型 → 自动只读，不能删除
4. 状态变更 → 不影响删除权限
5. 只读变更 → 影响删除权限

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