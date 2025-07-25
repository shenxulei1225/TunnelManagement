# Domain-Field 级联删除关系说明

## 概述

Domain和Field之间是多对多关系，通过中间表`system_domain_field_rel`进行关联。当删除Domain或Field时，系统只删除关联关系，不删除实体本身，确保数据的完整性。

## 🔗 **多对多关系图**

```
Domain (1) ←→ (M) DomainFieldRel (M) ←→ (1) Field

一个Domain可以包含多个Field
一个Field可以属于多个Domain
通过DomainFieldRel中间表管理关联关系
```

## 📋 **删除策略**

### 1. **删除Domain时**

**操作流程**：
```java
@Override
public void deleteDomain(Long id) {
    // 1. 校验Domain存在
    validateDomainExists(id);
    
    // 2. 校验是否有子Domain（树形结构约束）
    List<DomainDO> childDomains = domainMapper.selectByParentId(id);
    if (!childDomains.isEmpty()) {
        throw exception(ErrorCodeConstants.DOMAIN_EXISTS_CHILDREN);
    }
    
    // 3. 删除Domain与Field的关联关系（不删除Field实体）
    domainFieldRelMapper.deleteByDomainId(id);
    
    // 4. 删除Domain实体
    domainMapper.deleteById(id);
}
```

**影响范围**：
- ✅ 删除Domain实体
- ✅ 删除该Domain的所有字段关联
- ❌ **不删除**Field实体
- ❌ **不影响**其他Domain与Field的关联

### 2. **删除Field时**

**操作流程**：
```java
@Override
public void deleteField(Long id) {
    // 1. 校验Field存在
    validateFieldExists(id);
    
    // 2. 删除Field与Domain的关联关系（不删除Domain实体）
    domainFieldRelMapper.deleteByFieldId(id);
    
    // 3. 删除Field实体
    fieldMapper.deleteById(id);
}
```

**影响范围**：
- ✅ 删除Field实体
- ✅ 删除该Field的所有领域关联
- ❌ **不删除**Domain实体
- ❌ **不影响**其他Field与Domain的关联

## 🎯 **实际场景示例**

### 场景1：删除Domain
```
删除前：
建筑领域 (Domain ID=1)
├── 建筑高度 (Field ID=1)
├── 建筑面积 (Field ID=2)
└── 材料类型 (Field ID=3)

设备领域 (Domain ID=2)
├── 设备型号 (Field ID=4)
├── 材料类型 (Field ID=3) ← 与建筑领域共享
└── 功率参数 (Field ID=5)

执行：DELETE /system/domain/delete?id=1

删除后：
建筑领域 (Domain ID=1) ← 已删除

设备领域 (Domain ID=2) ← 保持不变
├── 设备型号 (Field ID=4)
├── 材料类型 (Field ID=3) ← 仍然存在且可用
└── 功率参数 (Field ID=5)

独立Field：
├── 建筑高度 (Field ID=1) ← 仍然存在，可重新关联
├── 建筑面积 (Field ID=2) ← 仍然存在，可重新关联
└── 材料类型 (Field ID=3) ← 仍然存在，继续为设备领域服务
```

### 场景2：删除Field
```
删除前：
建筑领域 (Domain ID=1)
├── 建筑高度 (Field ID=1)
├── 建筑面积 (Field ID=2)
└── 材料类型 (Field ID=3)

设备领域 (Domain ID=2)
├── 设备型号 (Field ID=4)
├── 材料类型 (Field ID=3) ← 与建筑领域共享
└── 功率参数 (Field ID=5)

执行：DELETE /system/field/delete?id=3

删除后：
建筑领域 (Domain ID=1) ← 保持存在
├── 建筑高度 (Field ID=1)
├── 建筑面积 (Field ID=2)
└── (材料类型已移除)

设备领域 (Domain ID=2) ← 保持存在
├── 设备型号 (Field ID=4)
├── (材料类型已移除)
└── 功率参数 (Field ID=5)

删除的Field：
材料类型 (Field ID=3) ← 已完全删除
```

## 🔧 **API接口**

### Domain删除
```http
DELETE /system/domain/delete?id={domainId}
```

### Field删除
```http
DELETE /system/field/delete?id={fieldId}
```

### 验证关联已删除
```http
GET /system/domain/field-rel/list?domainId={domainId}
```

## 🧪 **测试用例**

### 测试1：删除Domain验证
```http
# 1. 创建Domain-Field关联
POST /system/domain/field-rel/add?domainId=1&fieldId=1

# 2. 验证关联存在
GET /system/domain/field-rel/list?domainId=1
# 预期：返回关联数据

# 3. 删除Domain
DELETE /system/domain/delete?id=1

# 4. 验证Field仍存在
GET /system/field/get?id=1
# 预期：Field数据完整

# 5. 验证关联已删除
GET /system/domain/field-rel/list?domainId=1
# 预期：返回空列表
```

### 测试2：删除Field验证
```http
# 1. 创建Domain-Field关联
POST /system/domain/field-rel/add?domainId=1&fieldId=1

# 2. 验证关联存在
GET /system/domain/field-rel/list?domainId=1
# 预期：返回关联数据

# 3. 删除Field
DELETE /system/field/delete?id=1

# 4. 验证Domain仍存在
GET /system/domain/get?id=1
# 预期：Domain数据完整

# 5. 验证关联已删除
GET /system/domain/field-rel/list?domainId=1
# 预期：返回空列表
```

## 💡 **设计优势**

### 1. **数据安全性**
- 删除操作不会意外丢失重要的实体数据
- 只清理不再需要的关联关系
- 保护其他Domain/Field的正常使用

### 2. **业务灵活性**
- Field可以在删除Domain后重新关联到其他Domain
- Domain可以在删除Field后添加新的Field
- 支持复杂的业务变更场景

### 3. **系统稳定性**
- 避免因删除操作导致的级联数据丢失
- 减少误操作的影响范围
- 便于数据恢复和重新组织

### 4. **扩展性**
- 中间表可以添加更多业务属性
- 支持软删除和硬删除两种模式
- 便于实现更复杂的业务规则

## ⚠️ **注意事项**

1. **删除前确认**：建议在删除前显示影响范围
2. **数据备份**：重要操作前进行数据备份
3. **权限控制**：确保只有授权用户可以执行删除操作
4. **日志记录**：记录所有删除操作的详细日志
5. **批量操作**：大量数据删除时考虑性能优化

## 📊 **关联表结构**

```sql
CREATE TABLE `system_domain_field_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `domain_id` bigint NOT NULL COMMENT '领域ID',
  `field_id` bigint NOT NULL COMMENT '字段ID',
  `required` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否必填',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  -- 审计字段
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_domain_field` (`domain_id`,`field_id`,`deleted`,`tenant_id`),
  KEY `idx_domain` (`domain_id`),
  KEY `idx_field` (`field_id`)
) COMMENT='领域模型字段关联表';
```

这样的级联删除策略既保证了数据的完整性，又提供了足够的业务灵活性！ 