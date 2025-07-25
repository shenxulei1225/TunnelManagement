# Field在Domain间移动功能

## 概述

当需要将字段从一个领域移动到另一个领域时，系统会自动执行"解除旧关联 + 创建新关联"的原子操作，确保数据的一致性和完整性。

## 🔄 **移动流程**

### 1. **操作步骤**
```
1. 校验源领域和目标领域存在
2. 校验字段在源领域中确实存在关联
3. 校验字段在目标领域中不存在关联（避免重复）
4. 删除源领域的关联关系
5. 创建目标领域的关联关系
6. 重新整理源领域的字段排序
```

### 2. **事务保证**
整个移动过程在一个事务中完成，确保要么全部成功，要么全部回滚，避免数据不一致。

## 🎯 **业务场景示例**

### 场景：字段重新分类
```
初始状态：
建筑领域 (Domain ID=1)
├── 建筑高度 (Field ID=1) - 必填
├── 建筑面积 (Field ID=2) - 必填  
└── 材料类型 (Field ID=3) - 非必填

设备领域 (Domain ID=2)
├── 设备型号 (Field ID=4) - 必填
└── 功率参数 (Field ID=5) - 必填

操作：将"材料类型"从建筑领域移动到设备领域
{
  "fieldId": 3,
  "sourceDomainId": 1,
  "targetDomainId": 2,
  "required": true,        // 在设备领域中设为必填
  "sort": 2,              // 排在设备型号后面
  "remark": "材料管理统一到设备领域"
}

移动后：
建筑领域 (Domain ID=1)
├── 建筑高度 (Field ID=1) - 必填
└── 建筑面积 (Field ID=2) - 必填  

设备领域 (Domain ID=2)
├── 设备型号 (Field ID=4) - 必填
├── 材料类型 (Field ID=3) - 必填 ← 新移动过来
└── 功率参数 (Field ID=5) - 必填
```

## 🔧 **API接口**

### 移动字段接口
```http
POST /system/domain/field-move
Content-Type: application/json

{
  "fieldId": 3,              // 要移动的字段ID
  "sourceDomainId": 1,       // 源领域ID
  "targetDomainId": 2,       // 目标领域ID
  "required": true,          // 在目标领域中是否必填（可选）
  "sort": 2,                 // 在目标领域中的排序（可选）
  "remark": "移动原因"       // 备注信息（可选）
}
```

### 响应
```json
{
  "code": 0,
  "data": true,
  "msg": "移动成功"
}
```

## 📋 **参数说明**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| fieldId | Long | ✅ | 要移动的字段ID |
| sourceDomainId | Long | ✅ | 源领域ID |
| targetDomainId | Long | ✅ | 目标领域ID |
| required | Boolean | ❌ | 在目标领域中是否必填，不传则保持原设置 |
| sort | Integer | ❌ | 在目标领域中的排序，不传则自动分配到末尾 |
| remark | String | ❌ | 备注信息，不传则自动生成 |

## ✅ **校验规则**

### 1. **前置校验**
- ✅ 字段必须存在
- ✅ 源领域必须存在
- ✅ 目标领域必须存在
- ✅ 源领域和目标领域不能相同

### 2. **关联校验**
- ✅ 字段在源领域中必须存在关联关系
- ✅ 字段在目标领域中不能已存在关联关系

### 3. **业务校验**
- ✅ 用户必须有`system:domain:update`权限
- ✅ 操作在事务中执行，保证原子性

## 🧪 **测试用例**

### 测试1：正常移动
```http
# 1. 创建初始关联
POST /system/domain/field-rel/add?domainId=1&fieldId=1&required=false&sort=1

# 2. 验证初始状态
GET /system/domain/field-rel/list?domainId=1
# 预期：包含字段1

GET /system/domain/field-rel/list?domainId=2  
# 预期：不包含字段1

# 3. 执行移动
POST /system/domain/field-move
{
  "fieldId": 1,
  "sourceDomainId": 1,
  "targetDomainId": 2,
  "required": true,
  "sort": 1
}

# 4. 验证移动结果
GET /system/domain/field-rel/list?domainId=1
# 预期：不包含字段1

GET /system/domain/field-rel/list?domainId=2
# 预期：包含字段1，且required=true
```

### 测试2：错误场景
```http
# 尝试移动不存在的关联
POST /system/domain/field-move
{
  "fieldId": 999,
  "sourceDomainId": 1,
  "targetDomainId": 2
}
# 预期：返回错误"字段分类关联不存在"

# 尝试移动到已有关联的目标领域
POST /system/domain/field-move
{
  "fieldId": 1,
  "sourceDomainId": 1, 
  "targetDomainId": 2  // 假设字段1已在领域2中
}
# 预期：返回错误"字段分类关联已存在"

# 尝试移动到相同领域
POST /system/domain/field-move
{
  "fieldId": 1,
  "sourceDomainId": 1,
  "targetDomainId": 1
}
# 预期：返回错误"源领域和目标领域不能相同"
```

## 💡 **高级特性**

### 1. **智能排序**
- 如果不指定sort参数，系统自动分配到目标领域的末尾
- 移动后自动整理源领域的排序，消除空隙

### 2. **属性继承**
- 如果不指定required参数，保持在源领域中的设置
- 自动生成移动日志备注

### 3. **批量移动**（可扩展）
```java
// 未来可以扩展批量移动功能
void moveFieldsToDomain(List<Long> fieldIds, Long sourceDomainId, Long targetDomainId);
```

## ⚠️ **注意事项**

1. **数据一致性**：移动操作在事务中执行，确保原子性
2. **权限控制**：需要domain update权限
3. **并发安全**：多用户同时操作时的并发控制
4. **日志记录**：建议记录移动操作的详细日志
5. **业务影响**：移动前评估对现有业务的影响

## 🔗 **相关功能**

- [Domain-Field关联管理](./domain-field-relationship.md)
- [Domain-Field级联删除](./domain-field-cascade-delete.md)
- [Domain拖拽功能](./domain-drag-drop-integration.md)

## 📊 **错误码**

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 1_002_032_000 | 领域模型不存在 | 源或目标领域不存在 |
| 1_002_032_007 | 源领域和目标领域不能相同 | 不能移动到相同领域 |
| 1_002_031_001 | 字段分类关联已存在 | 目标领域已有该字段 |
| 1_002_031_002 | 字段分类关联不存在 | 源领域没有该字段 |

通过这个移动功能，可以灵活地重新组织字段和领域的关系，适应不断变化的业务需求！ 