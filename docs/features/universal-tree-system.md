# 通用树数据管理系统

## 概述

通用树数据管理系统是一个完全动态的树形结构管理解决方案，支持任意类型的树结构，无需预先在代码中定义。系统通过统一的数据模型和灵活的配置，可以支持各种业务场景的树形数据管理需求。

## 核心特性

### 1. 完全动态化
- **无需预定义树类型**：系统不依赖任何预定义的树类型，所有树结构都是动态创建的
- **灵活的数据类型**：支持任意数据类型的树节点，通过配置即可扩展
- **可配置的树属性**：每个树类型都可以有自己的配置属性

### 2. 统一数据模型
- **通用树数据关联表**：`system_tree_data_rel` 存储所有树结构数据
- **树配置表**：`system_tree_config` 存储树类型的配置信息
- **数据类型元数据表**：`system_data_type_meta` 存储数据类型的元信息

### 3. 灵活的元数据支持
- **JSON格式元数据**：每个树节点都可以存储自定义的元数据
- **动态属性扩展**：通过元数据可以扩展节点的任意属性
- **类型安全**：支持类型化的元数据访问

## 系统架构

### 数据库设计

#### 1. system_tree_data_rel（树数据关联表）
```sql
CREATE TABLE `system_tree_data_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tree_type` varchar(64) NOT NULL COMMENT '树类型',
  `tree_node_id` bigint NOT NULL COMMENT '树节点ID',
  `data_type` varchar(64) NOT NULL COMMENT '数据类型',
  `data_id` bigint NOT NULL COMMENT '数据ID',
  `data_name` varchar(128) NOT NULL COMMENT '数据名称',
  `data_type_label` varchar(64) COMMENT '数据类型标签',
  `display_order` int DEFAULT '0' COMMENT '显示顺序',
  `is_required` tinyint(1) DEFAULT '0' COMMENT '是否必填',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态',
  `metadata` json COMMENT '扩展元数据',
  PRIMARY KEY (`id`),
  KEY `idx_tree_type` (`tree_type`),
  KEY `idx_tree_node_id` (`tree_node_id`),
  KEY `idx_data_type` (`data_type`)
) COMMENT='通用树数据关联表';
```

#### 2. system_tree_config（树配置表）
```sql
CREATE TABLE `system_tree_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tree_type` varchar(64) NOT NULL COMMENT '树类型',
  `tree_name` varchar(128) NOT NULL COMMENT '树名称',
  `description` varchar(512) COMMENT '描述',
  `allowed_data_types` json COMMENT '允许的数据类型',
  `max_level` int DEFAULT '5' COMMENT '最大层级',
  `node_name_label` varchar(64) COMMENT '节点名称标签',
  `node_code_label` varchar(64) COMMENT '节点编码标签',
  `sort_type` tinyint DEFAULT '1' COMMENT '排序类型',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态',
  `config_json` json COMMENT '配置JSON',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tree_type` (`tree_type`)
) COMMENT='树配置表';
```

#### 3. system_data_type_meta（数据类型元数据表）
```sql
CREATE TABLE `system_data_type_meta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_type` varchar(64) NOT NULL COMMENT '数据类型',
  `data_name` varchar(128) NOT NULL COMMENT '数据名称',
  `description` varchar(512) COMMENT '描述',
  `icon` varchar(64) COMMENT '图标',
  `color` varchar(32) COMMENT '颜色',
  `table_name` varchar(128) COMMENT '关联表名',
  `id_field` varchar(64) NOT NULL COMMENT 'ID字段',
  `name_field` varchar(64) NOT NULL COMMENT '名称字段',
  `status_field` varchar(64) COMMENT '状态字段',
  `config_json` json COMMENT '配置JSON',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_data_type` (`data_type`)
) COMMENT='数据类型元数据表';
```

### API设计

#### 1. 获取树数据
```http
GET /admin-api/system/tree/data/{treeType}
```

**响应示例：**
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

#### 2. 添加树节点
```http
POST /admin-api/system/tree/node/{treeType}
```

#### 3. 更新树节点
```http
PUT /admin-api/system/tree/node/{treeType}/{nodeId}
```

#### 4. 删除树节点
```http
DELETE /admin-api/system/tree/node/{treeType}/{nodeId}
```

## 使用示例

### 1. 字段分类树
```javascript
// 前端调用
const fieldTree = await universalTreeApi.getTreeData('field_category');
```

### 2. 分级组树
```javascript
// 前端调用
const hierarchyTree = await universalTreeApi.getTreeData('hierarchy_group');
```

### 3. 设备树
```javascript
// 前端调用
const deviceTree = await universalTreeApi.getTreeData('device_tree');
```

## 元数据规范

### 1. 分类节点元数据
```json
{
  "parentId": 0,
  "level": 1,
  "code": "basic_info",
  "description": "基础信息字段分类"
}
```

### 2. 字段节点元数据
```json
{
  "parentId": 1,
  "level": 2,
  "fieldKey": "device_name",
  "fieldType": "string",
  "description": "设备名称字段"
}
```

### 3. 分组节点元数据
```json
{
  "parentId": 101,
  "level": 2,
  "code": "tech_dept",
  "description": "技术部"
}
```

## 扩展性

### 1. 添加新的树类型
1. 在 `system_tree_config` 表中添加新树类型的配置
2. 在 `system_data_type_meta` 表中添加相关数据类型的元数据
3. 在 `system_tree_data_rel` 表中插入树数据

### 2. 添加新的数据类型
1. 在 `system_data_type_meta` 表中添加新数据类型的元数据
2. 更新相关树类型的 `allowed_data_types` 配置

### 3. 扩展节点属性
通过 `metadata` 字段存储任意自定义属性，系统会自动解析并返回给前端。

## 优势

1. **完全动态**：无需修改代码即可支持新的树类型
2. **统一管理**：所有树结构使用统一的数据模型和API
3. **灵活扩展**：通过元数据可以扩展任意属性
4. **类型安全**：支持类型化的元数据访问
5. **性能优化**：通过索引优化查询性能
6. **易于维护**：统一的数据模型便于维护和扩展

## 总结

通用树数据管理系统提供了一个完全动态、可扩展的树形结构管理解决方案。通过统一的数据模型和灵活的配置，可以支持各种业务场景的树形数据管理需求，真正实现了"一次开发，多次使用"的目标。 