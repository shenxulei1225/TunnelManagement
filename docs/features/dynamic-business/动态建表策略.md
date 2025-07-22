# 动态建表场景下的数据库设计策略

## 概述

当系统支持动态建表时，我们需要重新考虑数据库设计策略。动态建表意味着每个业务模块可能有自己独立的表结构，这种情况下需要权衡统一表设计和分表设计的利弊。

## 动态建表场景分析

### 1. **动态建表的特点**

#### **业务需求**
- 不同业务模块有完全不同的字段需求
- 字段数量和类型差异很大
- 需要支持字段的动态添加和删除
- 需要支持表结构的动态调整

#### **技术挑战**
- 表结构不固定
- 字段映射复杂
- 查询性能优化困难
- 数据迁移和版本管理复杂

### 2. **设计策略对比**

## 方案一：统一表 + 扩展字段设计（推荐）

### 核心思想
- 使用统一的树形表结构作为基础
- 通过 JSON 字段存储扩展属性
- 通过元数据表管理字段定义

### 表结构设计

#### 1. **基础树形表**
```sql
-- 基础树形表（固定结构）
CREATE TABLE `sys_tree_node` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(50) NOT NULL COMMENT '业务类型',
  `name` varchar(100) NOT NULL COMMENT '节点名称',
  `code` varchar(100) DEFAULT NULL COMMENT '节点编码',
  `parent_id` bigint DEFAULT 0 COMMENT '父级ID',
  `level` int DEFAULT 0 COMMENT '层级',
  `path` varchar(500) DEFAULT '' COMMENT '路径',
  `sort` int DEFAULT 0 COMMENT '排序',
  `icon` varchar(50) DEFAULT 'Folder' COMMENT '图标',
  `color` varchar(20) DEFAULT '#409EFF' COMMENT '颜色',
  `description` varchar(500) DEFAULT '' COMMENT '描述',
  `status` tinyint DEFAULT 0 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  `ext_data` json DEFAULT NULL COMMENT '扩展数据（JSON格式）',
  PRIMARY KEY (`id`),
  KEY `idx_type_parent` (`type`, `parent_id`),
  KEY `idx_type_sort` (`type`, `sort`),
  KEY `idx_type_path` (`type`, `path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用树形数据表';
```

#### 2. **字段定义表**
```sql
-- 字段定义表
CREATE TABLE `sys_field_definition` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `table_type` varchar(50) NOT NULL COMMENT '表类型',
  `field_name` varchar(100) NOT NULL COMMENT '字段名称',
  `field_label` varchar(100) NOT NULL COMMENT '字段标签',
  `field_type` varchar(50) NOT NULL COMMENT '字段类型：string/number/boolean/date/select',
  `field_length` int DEFAULT NULL COMMENT '字段长度',
  `is_required` tinyint DEFAULT 0 COMMENT '是否必填',
  `default_value` varchar(500) DEFAULT NULL COMMENT '默认值',
  `validation_rules` json DEFAULT NULL COMMENT '验证规则',
  `display_order` int DEFAULT 0 COMMENT '显示顺序',
  `is_searchable` tinyint DEFAULT 0 COMMENT '是否可搜索',
  `is_sortable` tinyint DEFAULT 0 COMMENT '是否可排序',
  `options` json DEFAULT NULL COMMENT '选项值（用于select类型）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_table_field` (`table_type`, `field_name`),
  KEY `idx_table_type` (`table_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字段定义表';
```

#### 3. **数据示例**
```sql
-- 字段定义示例
INSERT INTO `sys_field_definition` VALUES
(1, 'hierarchy', 'field_type', '字段类型', 'select', NULL, 1, 'string', '{"required": true}', 1, 1, 1, '["string","number","boolean","date"]', NOW(), NOW(), 1, 1, 0),
(2, 'hierarchy', 'field_length', '字段长度', 'number', NULL, 0, '255', '{"min": 1, "max": 1000}', 2, 0, 1, NULL, NOW(), NOW(), 1, 1, 0),
(3, 'category', 'category_type', '分类类型', 'select', NULL, 1, 'content', '{"required": true}', 1, 1, 1, '["content","product","article"]', NOW(), NOW(), 1, 1, 0),
(4, 'category', 'seo_keywords', 'SEO关键词', 'string', 500, 0, '', '{"maxLength": 500}', 2, 0, 0, NULL, NOW(), NOW(), 1, 1, 0);

-- 树形数据示例
INSERT INTO `sys_tree_node` VALUES
(1, 'hierarchy', '用户信息组', 'USER_INFO', 0, 1, '/1', 1, 'Folder', '#409EFF', '用户相关字段分组', 0, NOW(), NOW(), 1, 1, 0, '{"field_type": "string", "field_length": 100}'),
(2, 'hierarchy', '基础字段', 'BASIC_FIELDS', 1, 2, '/1/2', 1, 'Document', '#409EFF', '基础信息字段', 0, NOW(), NOW(), 1, 1, 0, '{"field_type": "string", "field_length": 50}'),
(3, 'category', '产品分类', 'PRODUCT_CATEGORY', 0, 1, '/3', 1, 'Collection', '#67C23A', '产品相关分类', 0, NOW(), NOW(), 1, 1, 0, '{"category_type": "product", "seo_keywords": "产品,分类"}');
```

### 优势
1. **统一管理**：所有树形数据使用相同的表结构
2. **动态扩展**：通过 JSON 字段支持任意扩展
3. **类型安全**：通过字段定义表保证数据一致性
4. **查询性能**：可以针对 JSON 字段建立索引
5. **维护简单**：只需要维护一套树形逻辑

### 劣势
1. **JSON 查询复杂**：需要处理 JSON 字段的查询
2. **索引限制**：JSON 字段的索引功能有限
3. **数据验证复杂**：需要在应用层处理字段验证

## 方案二：动态分表设计

### 核心思想
- 为每个业务类型创建独立的表
- 表结构根据字段定义动态生成
- 通过元数据管理表结构

### 实现方案

#### 1. **表结构管理**
```sql
-- 表结构定义表
CREATE TABLE `sys_table_definition` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `table_name` varchar(100) NOT NULL COMMENT '表名',
  `table_label` varchar(100) NOT NULL COMMENT '表标签',
  `table_type` varchar(50) NOT NULL COMMENT '表类型',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_table_name` (`table_name`),
  KEY `idx_table_type` (`table_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表结构定义表';
```

#### 2. **动态建表示例**
```sql
-- 多级组表（动态生成）
CREATE TABLE `sys_hierarchy_group_20241201` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '组名称',
  `code` varchar(100) DEFAULT NULL COMMENT '组编码',
  `parent_id` bigint DEFAULT 0 COMMENT '父级ID',
  `level` int DEFAULT 0 COMMENT '层级',
  `path` varchar(500) DEFAULT '' COMMENT '路径',
  `sort` int DEFAULT 0 COMMENT '排序',
  `icon` varchar(50) DEFAULT 'Folder' COMMENT '图标',
  `color` varchar(20) DEFAULT '#409EFF' COMMENT '颜色',
  `description` varchar(500) DEFAULT '' COMMENT '描述',
  `status` tinyint DEFAULT 0 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  `field_type` varchar(50) DEFAULT 'string' COMMENT '字段类型',
  `field_length` int DEFAULT 255 COMMENT '字段长度',
  `is_required` tinyint DEFAULT 0 COMMENT '是否必填',
  `validation_rules` json DEFAULT NULL COMMENT '验证规则',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort` (`sort`),
  KEY `idx_path` (`path`),
  KEY `idx_field_type` (`field_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='多级组表';

-- 分类表（动态生成）
CREATE TABLE `sys_category_20241201` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `code` varchar(100) DEFAULT NULL COMMENT '分类编码',
  `parent_id` bigint DEFAULT 0 COMMENT '父级ID',
  `level` int DEFAULT 0 COMMENT '层级',
  `path` varchar(500) DEFAULT '' COMMENT '路径',
  `sort` int DEFAULT 0 COMMENT '排序',
  `icon` varchar(50) DEFAULT 'Collection' COMMENT '图标',
  `color` varchar(20) DEFAULT '#67C23A' COMMENT '颜色',
  `description` varchar(500) DEFAULT '' COMMENT '描述',
  `status` tinyint DEFAULT 0 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  `category_type` varchar(50) DEFAULT 'content' COMMENT '分类类型',
  `seo_keywords` varchar(500) DEFAULT '' COMMENT 'SEO关键词',
  `seo_description` varchar(1000) DEFAULT '' COMMENT 'SEO描述',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort` (`sort`),
  KEY `idx_path` (`path`),
  KEY `idx_category_type` (`category_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';
```

### 优势
1. **性能最优**：每个表都有专门的索引
2. **查询简单**：标准的 SQL 查询
3. **类型安全**：数据库级别的类型约束
4. **扩展灵活**：可以为不同业务定制字段

### 劣势
1. **维护复杂**：需要管理多个表结构
2. **代码重复**：需要为每个表写相似的逻辑
3. **数据迁移困难**：表结构变更需要数据迁移
4. **开发成本高**：需要动态生成表结构

## 推荐方案：统一表 + 扩展字段设计

### 理由

1. **符合动态建表需求**：通过 JSON 字段支持任意扩展
2. **维护成本低**：只需要维护一套树形逻辑
3. **扩展性强**：新增字段只需更新字段定义表
4. **性能可控**：通过合理的索引设计保证性能
5. **开发效率高**：统一的API接口和数据结构

### 实现方案

#### 1. **后端实体设计**
```java
@Data
@TableName("sys_tree_node")
public class TreeNode {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("type")
    private String type;
    
    @TableField("name")
    private String name;
    
    @TableField("code")
    private String code;
    
    @TableField("parent_id")
    private Long parentId;
    
    @TableField("level")
    private Integer level;
    
    @TableField("path")
    private String path;
    
    @TableField("sort")
    private Integer sort;
    
    @TableField("icon")
    private String icon;
    
    @TableField("color")
    private String color;
    
    @TableField("description")
    private String description;
    
    @TableField("status")
    private Integer status;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableField("create_by")
    private Long createBy;
    
    @TableField("update_by")
    private Long updateBy;
    
    @TableField("deleted")
    private Integer deleted;
    
    @TableField("ext_data")
    private String extData; // JSON 格式的扩展数据
    
    @TableField(exist = false)
    private List<TreeNode> children;
    
    @TableField(exist = false)
    private Map<String, Object> extDataMap; // 扩展数据的 Map 形式
}
```

#### 2. **字段定义实体**
```java
@Data
@TableName("sys_field_definition")
public class FieldDefinition {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("table_type")
    private String tableType;
    
    @TableField("field_name")
    private String fieldName;
    
    @TableField("field_label")
    private String fieldLabel;
    
    @TableField("field_type")
    private String fieldType;
    
    @TableField("field_length")
    private Integer fieldLength;
    
    @TableField("is_required")
    private Integer isRequired;
    
    @TableField("default_value")
    private String defaultValue;
    
    @TableField("validation_rules")
    private String validationRules;
    
    @TableField("display_order")
    private Integer displayOrder;
    
    @TableField("is_searchable")
    private Integer isSearchable;
    
    @TableField("is_sortable")
    private Integer isSortable;
    
    @TableField("options")
    private String options;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableField("create_by")
    private Long createBy;
    
    @TableField("update_by")
    private Long updateBy;
    
    @TableField("deleted")
    private Integer deleted;
}
```

#### 3. **服务层设计**
```java
@Service
public class TreeNodeService {
    
    @Autowired
    private FieldDefinitionService fieldDefinitionService;
    
    /**
     * 获取指定类型的树形数据（包含扩展字段）
     */
    public List<TreeNode> getTreeByType(String type) {
        // 获取字段定义
        List<FieldDefinition> fieldDefinitions = fieldDefinitionService.getByTableType(type);
        
        // 获取树形数据
        LambdaQueryWrapper<TreeNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TreeNode::getType, type)
               .eq(TreeNode::getDeleted, 0)
               .orderByAsc(TreeNode::getSort);
        
        List<TreeNode> nodes = baseMapper.selectList(wrapper);
        
        // 处理扩展数据
        for (TreeNode node : nodes) {
            processExtData(node, fieldDefinitions);
        }
        
        return buildTree(nodes);
    }
    
    /**
     * 创建节点（支持扩展字段）
     */
    public void createNode(TreeNode node) {
        // 验证扩展字段
        validateExtData(node);
        
        // 设置默认值
        node.setCreateTime(LocalDateTime.now());
        node.setUpdateTime(LocalDateTime.now());
        node.setDeleted(0);
        
        // 计算层级和路径
        calculateLevelAndPath(node);
        
        // 处理扩展数据
        processExtDataForSave(node);
        
        baseMapper.insert(node);
    }
    
    /**
     * 处理扩展数据
     */
    private void processExtData(TreeNode node, List<FieldDefinition> fieldDefinitions) {
        if (StringUtils.isNotBlank(node.getExtData())) {
            try {
                Map<String, Object> extDataMap = JsonUtils.parseObject(node.getExtData(), Map.class);
                node.setExtDataMap(extDataMap);
            } catch (Exception e) {
                log.error("解析扩展数据失败", e);
            }
        }
    }
    
    /**
     * 验证扩展数据
     */
    private void validateExtData(TreeNode node) {
        if (node.getExtDataMap() != null) {
            List<FieldDefinition> fieldDefinitions = fieldDefinitionService.getByTableType(node.getType());
            
            for (FieldDefinition fieldDef : fieldDefinitions) {
                if (fieldDef.getIsRequired() == 1) {
                    Object value = node.getExtDataMap().get(fieldDef.getFieldName());
                    if (value == null || StringUtils.isBlank(value.toString())) {
                        throw new ServiceException(fieldDef.getFieldLabel() + "不能为空");
                    }
                }
            }
        }
    }
    
    /**
     * 处理保存时的扩展数据
     */
    private void processExtDataForSave(TreeNode node) {
        if (node.getExtDataMap() != null) {
            node.setExtData(JsonUtils.toJsonString(node.getExtDataMap()));
        }
    }
}
```

#### 4. **前端适配**
```typescript
// 动态字段处理
export function useDynamicTreeNode(type: string) {
  const [fieldDefinitions, setFieldDefinitions] = useState<FieldDefinition[]>([])
  const [treeData, setTreeData] = useState<TreeNode[]>([])
  
  // 获取字段定义
  const fetchFieldDefinitions = async () => {
    const definitions = await getFieldDefinitionsApi(type)
    setFieldDefinitions(definitions)
  }
  
  // 获取树形数据
  const fetchTreeData = async () => {
    const data = await getTreeNodeTreeApi(type)
    setTreeData(data)
  }
  
  // 创建节点（支持动态字段）
  const createNode = async (formData: any) => {
    const extData: Record<string, any> = {}
    
    // 处理扩展字段
    fieldDefinitions.forEach(field => {
      if (formData[field.fieldName] !== undefined) {
        extData[field.fieldName] = formData[field.fieldName]
      }
    })
    
    await createTreeNodeApi({
      type,
      name: formData.name,
      code: formData.code,
      parentId: formData.parentId,
      icon: formData.icon,
      color: formData.color,
      description: formData.description,
      extData: JSON.stringify(extData)
    })
  }
  
  return {
    fieldDefinitions,
    treeData,
    fetchFieldDefinitions,
    fetchTreeData,
    createNode
  }
}
```

## 性能优化建议

### 1. **JSON 字段索引**
```sql
-- 为常用的 JSON 字段创建虚拟列索引
ALTER TABLE sys_tree_node 
ADD COLUMN ext_field_type VARCHAR(50) 
GENERATED ALWAYS AS (JSON_UNQUOTE(JSON_EXTRACT(ext_data, '$.field_type'))) VIRTUAL;

CREATE INDEX idx_ext_field_type ON sys_tree_node(type, ext_field_type);
```

### 2. **缓存策略**
```java
@Cacheable(value = "fieldDefinition", key = "#tableType")
public List<FieldDefinition> getByTableType(String tableType) {
    // 查询字段定义
}
```

### 3. **分页查询优化**
```java
public PageResult<TreeNode> getTreeNodePage(String type, TreeNodePageReq req) {
    Page<TreeNode> page = new Page<>(req.getPageNo(), req.getPageSize());
    
    LambdaQueryWrapper<TreeNode> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(TreeNode::getType, type)
           .eq(TreeNode::getDeleted, 0)
           .like(StringUtils.isNotBlank(req.getName()), TreeNode::getName, req.getName())
           .orderByAsc(TreeNode::getSort);
    
    // 支持扩展字段搜索
    if (StringUtils.isNotBlank(req.getExtField())) {
        wrapper.apply("JSON_EXTRACT(ext_data, '$.{0}') LIKE '%{1}%'", 
                     req.getExtFieldName(), req.getExtField());
    }
    
    return baseMapper.selectPage(page, wrapper);
}
```

## 总结

在动态建表场景下，**推荐使用统一表 + 扩展字段设计**，理由如下：

1. **符合动态建表需求**：通过 JSON 字段支持任意扩展
2. **维护成本低**：只需要维护一套树形逻辑
3. **扩展性强**：新增字段只需更新字段定义表
4. **性能可控**：通过合理的索引设计保证性能
5. **开发效率高**：统一的API接口和数据结构
6. **数据一致性**：通过字段定义表保证数据一致性

这种设计既满足了动态建表的需求，又保持了技术实现的统一性，是动态建表场景下的最佳选择！ 