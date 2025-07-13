# 业务模型管理 - 数据库设计

## 表结构设计

### dynamic_business_model 表

业务模型配置表，存储所有业务模型的基本信息和配置。

```sql
CREATE TABLE `dynamic_business_model` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模型ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `code` varchar(100) NOT NULL COMMENT '业务类型编码',
    `name` varchar(100) NOT NULL COMMENT '业务类型名称',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `table_name` varchar(100) DEFAULT NULL COMMENT '数据表名',
    `model_type` tinyint NOT NULL DEFAULT '1' COMMENT '模型类型（0:系统，1:自定义）',
    `sort` int DEFAULT '0' COMMENT '排序号',
    `structure_type` tinyint NOT NULL DEFAULT '1' COMMENT '数据结构类型（1:树形，2:列表）',
    `storage_strategy` tinyint NOT NULL DEFAULT '1' COMMENT '存储策略（1:单表，2:分表）',
    `config` text COMMENT '配置JSON',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0:禁用，1:启用）',
    `readonly` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否只读（0:可编辑，1:只读）',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_code` (`tenant_id`, `code`),
    KEY `idx_readonly` (`readonly`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务模型配置表';
```

## 字段说明

### 核心字段

| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `id` | bigint | AUTO_INCREMENT | 主键ID |
| `tenant_id` | bigint | - | 租户ID，支持多租户 |
| `code` | varchar(100) | - | 业务模型编码，租户内唯一 |
| `name` | varchar(100) | - | 业务模型名称 |
| `description` | varchar(500) | NULL | 业务模型描述 |

### 配置字段

| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `table_name` | varchar(100) | NULL | 对应的数据表名 |
| `model_type` | tinyint | 1 | 模型类型（0:系统，1:自定义） |
| `structure_type` | tinyint | 1 | 数据结构类型（1:树形，2:列表） |
| `storage_strategy` | tinyint | 1 | 存储策略（1:单表，2:分表） |
| `config` | text | NULL | 模型配置，JSON格式 |

### 状态字段

| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `status` | tinyint | 0 | 状态（0:禁用，1:启用） |
| `readonly` | bit(1) | b'0' | 是否只读（0:可编辑，1:只读） |
| `sort` | int | 0 | 排序号，用于拖拽排序 |

### 审计字段

| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `creator` | varchar(64) | '' | 创建者 |
| `create_time` | datetime | CURRENT_TIMESTAMP | 创建时间 |
| `updater` | varchar(64) | '' | 更新者 |
| `update_time` | datetime | CURRENT_TIMESTAMP | 更新时间 |
| `deleted` | bit(1) | b'0' | 逻辑删除标记 |

## 索引设计

### 主键索引
- `PRIMARY KEY (id)` - 主键索引

### 唯一索引
- `uk_tenant_code (tenant_id, code)` - 租户内编码唯一索引

### 普通索引
- `idx_readonly (readonly)` - 只读状态索引，用于快速查询只读模型

## 数据迁移

### 添加 readonly 字段

```sql
-- 为业务模型表添加只读字段
-- 执行时间：2025-07-03
-- 说明：添加readonly字段来控制业务模型是否可删除，与status字段独立

ALTER TABLE `dynamic_business_model` 
ADD COLUMN `readonly` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否只读（0:可编辑，1:只读）' AFTER `status`;

-- 更新现有数据，系统类型的模型设置为只读
UPDATE `dynamic_business_model` 
SET `readonly` = b'1' 
WHERE `model_type` = 0;

-- 自定义类型的模型默认可编辑
UPDATE `dynamic_business_model` 
SET `readonly` = b'0' 
WHERE `model_type` = 1;

-- 添加索引以提高查询性能
ALTER TABLE `dynamic_business_model` 
ADD INDEX `idx_readonly` (`readonly`);

-- 验证数据
SELECT 
    id,
    name,
    code,
    model_type,
    status,
    readonly,
    CASE 
        WHEN readonly = 1 THEN '只读'
        ELSE '可编辑'
    END as readonly_desc
FROM `dynamic_business_model` 
ORDER BY id;
```

## 数据约束

### 业务约束

1. **编码唯一性**
   - 同一租户内，模型编码必须唯一
   - 编码格式：字母开头，只能包含字母、数字、下划线

2. **表名约束**
   - 表名必须以 `dynamic_` 开头
   - 表名不能与现有表冲突

3. **状态约束**
   - status 只能是 0 或 1
   - readonly 只能是 0 或 1

### 数据完整性

1. **外键约束**
   - 暂无外键约束，通过应用层保证数据一致性

2. **非空约束**
   - `tenant_id`、`code`、`name` 不能为空
   - `model_type`、`structure_type`、`storage_strategy` 不能为空

## 数据示例

### 系统模型示例

```sql
INSERT INTO `dynamic_business_model` (
    `tenant_id`, `code`, `name`, `description`, 
    `table_name`, `model_type`, `status`, `readonly`
) VALUES (
    1, 'user', '用户管理', '系统内置的用户管理模型',
    'dynamic_user', 0, 1, 1
);
```

### 自定义模型示例

```sql
INSERT INTO `dynamic_business_model` (
    `tenant_id`, `code`, `name`, `description`, 
    `table_name`, `model_type`, `status`, `readonly`
) VALUES (
    1, 'product', '产品管理', '用户自定义的产品管理模型',
    'dynamic_product', 1, 1, 0
);
```

## 性能优化

### 查询优化

1. **分页查询**
   ```sql
   SELECT * FROM dynamic_business_model 
   WHERE tenant_id = ? AND deleted = 0
   ORDER BY sort DESC, id DESC
   LIMIT ?, ?
   ```

2. **条件查询**
   ```sql
   SELECT * FROM dynamic_business_model 
   WHERE tenant_id = ? AND readonly = 0 AND deleted = 0
   ```

### 索引优化

1. **复合索引**
   - 考虑添加 `(tenant_id, status, deleted)` 复合索引
   - 考虑添加 `(tenant_id, model_type, deleted)` 复合索引

2. **覆盖索引**
   - 对于列表查询，可以考虑覆盖索引减少回表

## 扩展性设计

### 未来扩展字段

1. **版本控制**
   - `version` - 模型版本号
   - `version_comment` - 版本说明

2. **权限扩展**
   - `permission_level` - 权限级别
   - `access_control` - 访问控制配置

3. **分类管理**
   - `category_id` - 分类ID
   - `tags` - 标签，JSON格式

### 分表策略

当数据量较大时，可以考虑按租户分表：

```sql
-- 按租户分表
CREATE TABLE `dynamic_business_model_tenant_1` LIKE `dynamic_business_model`;
CREATE TABLE `dynamic_business_model_tenant_2` LIKE `dynamic_business_model`;
``` 