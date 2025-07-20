# 实例存储解决方案

## 概述

在实际使用中，不同业务对象（DO）会有很多实例，这些实例的存储和管理是一个关键问题。我们需要设计一个既能支持动态字段，又能高效管理大量实例的存储方案。

## 问题分析

### 1. **实例存储的挑战**

#### **数据量问题**
- 每个业务对象可能有成千上万个实例
- 不同业务对象的实例数量差异很大
- 需要支持实例的快速查询和检索

#### **字段差异问题**
- 不同业务对象的字段定义不同
- 同一业务对象的不同实例可能有不同的字段值
- 需要支持字段的动态添加和删除

#### **性能问题**
- 大量实例的查询性能
- 复杂字段的索引和搜索
- 数据分页和排序

### 2. **存储方案对比**

## 方案一：统一实例表设计（推荐）

### 核心思想
- 使用统一的实例表存储所有业务对象的实例
- 通过 JSON 字段存储实例的字段值
- 通过元数据管理字段定义和实例关系

### 表结构设计

#### 1. **实例表**
```sql
-- 统一实例表
CREATE TABLE `sys_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `object_type` varchar(50) NOT NULL COMMENT '对象类型',
  `object_id` bigint NOT NULL COMMENT '对象ID（关联树形节点）',
  `instance_name` varchar(100) NOT NULL COMMENT '实例名称',
  `instance_code` varchar(100) DEFAULT NULL COMMENT '实例编码',
  `status` tinyint DEFAULT 0 COMMENT '状态：0-正常，1-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  `field_values` json DEFAULT NULL COMMENT '字段值（JSON格式）',
  PRIMARY KEY (`id`),
  KEY `idx_object_type_id` (`object_type`, `object_id`),
  KEY `idx_instance_code` (`instance_code`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='统一实例表';
```

#### 2. **字段定义表（扩展）**
```sql
-- 字段定义表（支持实例字段）
CREATE TABLE `sys_field_definition` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `table_type` varchar(50) NOT NULL COMMENT '表类型',
  `field_name` varchar(100) NOT NULL COMMENT '字段名称',
  `field_label` varchar(100) NOT NULL COMMENT '字段标签',
  `field_type` varchar(50) NOT NULL COMMENT '字段类型：string/number/boolean/date/select/text',
  `field_length` int DEFAULT NULL COMMENT '字段长度',
  `is_required` tinyint DEFAULT 0 COMMENT '是否必填',
  `default_value` varchar(500) DEFAULT NULL COMMENT '默认值',
  `validation_rules` json DEFAULT NULL COMMENT '验证规则',
  `display_order` int DEFAULT 0 COMMENT '显示顺序',
  `is_searchable` tinyint DEFAULT 0 COMMENT '是否可搜索',
  `is_sortable` tinyint DEFAULT 0 COMMENT '是否可排序',
  `is_display` tinyint DEFAULT 1 COMMENT '是否显示',
  `options` json DEFAULT NULL COMMENT '选项值（用于select类型）',
  `is_instance_field` tinyint DEFAULT 0 COMMENT '是否为实例字段',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_table_field` (`table_type`, `field_name`),
  KEY `idx_table_type` (`table_type`),
  KEY `idx_instance_field` (`is_instance_field`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字段定义表';
```

#### 3. **数据示例**
```sql
-- 字段定义示例
INSERT INTO `sys_field_definition` VALUES
-- 多级组字段（节点字段）
(1, 'hierarchy', 'field_type', '字段类型', 'select', NULL, 1, 'string', '{"required": true}', 1, 1, 1, 1, '["string","number","boolean","date"]', 0, NOW(), NOW(), 1, 1, 0),
(2, 'hierarchy', 'field_length', '字段长度', 'number', NULL, 0, '255', '{"min": 1, "max": 1000}', 2, 0, 1, 1, NULL, 0, NOW(), NOW(), 1, 1, 0),

-- 多级组实例字段
(3, 'hierarchy', 'field_value', '字段值', 'string', 1000, 0, '', '{"maxLength": 1000}', 1, 1, 0, 1, NULL, 1, NOW(), NOW(), 1, 1, 0),
(4, 'hierarchy', 'field_comment', '字段注释', 'text', NULL, 0, '', '{"maxLength": 2000}', 2, 0, 0, 1, NULL, 1, NOW(), NOW(), 1, 1, 0),
(5, 'hierarchy', 'is_required', '是否必填', 'boolean', NULL, 0, 'false', NULL, 3, 0, 1, 1, NULL, 1, NOW(), NOW(), 1, 1, 0),

-- 分类字段（节点字段）
(6, 'category', 'category_type', '分类类型', 'select', NULL, 1, 'content', '{"required": true}', 1, 1, 1, 1, '["content","product","article"]', 0, NOW(), NOW(), 1, 1, 0),
(7, 'category', 'seo_keywords', 'SEO关键词', 'string', 500, 0, '', '{"maxLength": 500}', 2, 0, 0, 1, NULL, 0, NOW(), NOW(), 1, 1, 0),

-- 分类实例字段
(8, 'category', 'content_count', '内容数量', 'number', NULL, 0, '0', '{"min": 0}', 1, 1, 1, 1, NULL, 1, NOW(), NOW(), 1, 1, 0),
(9, 'category', 'last_update', '最后更新', 'date', NULL, 0, '', NULL, 2, 0, 1, 1, NULL, 1, NOW(), NOW(), 1, 1, 0);

-- 实例数据示例
INSERT INTO `sys_instance` VALUES
-- 多级组实例
(1, 'hierarchy', 1, '用户姓名字段', 'USER_NAME_FIELD', 0, NOW(), NOW(), 1, 1, 0, '{"field_value": "张三", "field_comment": "用户真实姓名", "is_required": true}'),
(2, 'hierarchy', 1, '用户年龄字段', 'USER_AGE_FIELD', 0, NOW(), NOW(), 1, 1, 0, '{"field_value": "25", "field_comment": "用户年龄", "is_required": false}'),
(3, 'hierarchy', 2, '基础信息字段', 'BASIC_INFO_FIELD', 0, NOW(), NOW(), 1, 1, 0, '{"field_value": "基础信息", "field_comment": "用户基础信息", "is_required": true}'),

-- 分类实例
(4, 'category', 3, '电子产品分类', 'ELECTRONIC_PRODUCT', 0, NOW(), NOW(), 1, 1, 0, '{"content_count": 150, "last_update": "2024-12-01"}'),
(5, 'category', 3, '服装分类', 'CLOTHING_CATEGORY', 0, NOW(), NOW(), 1, 1, 0, '{"content_count": 89, "last_update": "2024-11-30"}');
```

### 优势
1. **统一管理**：所有实例使用相同的表结构
2. **动态扩展**：通过 JSON 字段支持任意字段值
3. **查询灵活**：支持复杂的字段查询和过滤
4. **性能可控**：通过合理的索引设计保证性能
5. **维护简单**：只需要维护一套实例管理逻辑

### 劣势
1. **JSON 查询复杂**：需要处理 JSON 字段的查询
2. **索引限制**：JSON 字段的索引功能有限
3. **数据验证复杂**：需要在应用层处理字段验证

## 方案二：分表设计

### 核心思想
- 为每个业务对象创建独立的实例表
- 表结构根据字段定义动态生成
- 通过元数据管理表结构

### 实现方案

#### 1. **动态实例表示例**
```sql
-- 多级组实例表（动态生成）
CREATE TABLE `sys_hierarchy_instance_20241201` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `object_id` bigint NOT NULL COMMENT '对象ID',
  `instance_name` varchar(100) NOT NULL COMMENT '实例名称',
  `instance_code` varchar(100) DEFAULT NULL COMMENT '实例编码',
  `status` tinyint DEFAULT 0 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  `field_value` varchar(1000) DEFAULT '' COMMENT '字段值',
  `field_comment` text COMMENT '字段注释',
  `is_required` tinyint DEFAULT 0 COMMENT '是否必填',
  PRIMARY KEY (`id`),
  KEY `idx_object_id` (`object_id`),
  KEY `idx_instance_code` (`instance_code`),
  KEY `idx_field_value` (`field_value`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='多级组实例表';

-- 分类实例表（动态生成）
CREATE TABLE `sys_category_instance_20241201` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `object_id` bigint NOT NULL COMMENT '对象ID',
  `instance_name` varchar(100) NOT NULL COMMENT '实例名称',
  `instance_code` varchar(100) DEFAULT NULL COMMENT '实例编码',
  `status` tinyint DEFAULT 0 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  `content_count` int DEFAULT 0 COMMENT '内容数量',
  `last_update` date DEFAULT NULL COMMENT '最后更新',
  PRIMARY KEY (`id`),
  KEY `idx_object_id` (`object_id`),
  KEY `idx_instance_code` (`instance_code`),
  KEY `idx_content_count` (`content_count`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类实例表';
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

## 推荐方案：统一实例表设计

### 理由

1. **符合动态建表需求**：通过 JSON 字段支持任意字段值
2. **维护成本低**：只需要维护一套实例管理逻辑
3. **扩展性强**：新增字段只需更新字段定义表
4. **性能可控**：通过合理的索引设计保证性能
5. **开发效率高**：统一的API接口和数据结构

### 实现方案

#### 1. **后端实体设计**
```java
@Data
@TableName("sys_instance")
public class Instance {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("object_type")
    private String objectType;
    
    @TableField("object_id")
    private Long objectId;
    
    @TableField("instance_name")
    private String instanceName;
    
    @TableField("instance_code")
    private String instanceCode;
    
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
    
    @TableField("field_values")
    private String fieldValues; // JSON 格式的字段值
    
    @TableField(exist = false)
    private Map<String, Object> fieldValuesMap; // 字段值的 Map 形式
    
    @TableField(exist = false)
    private TreeNode objectNode; // 关联的对象节点
}
```

#### 2. **服务层设计**
```java
@Service
public class InstanceService {
    
    @Autowired
    private FieldDefinitionService fieldDefinitionService;
    
    @Autowired
    private TreeNodeService treeNodeService;
    
    /**
     * 获取指定对象的实例列表
     */
    public List<Instance> getInstancesByObject(String objectType, Long objectId) {
        // 获取字段定义
        List<FieldDefinition> fieldDefinitions = fieldDefinitionService.getByTableType(objectType);
        
        // 获取实例数据
        LambdaQueryWrapper<Instance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Instance::getObjectType, objectType)
               .eq(Instance::getObjectId, objectId)
               .eq(Instance::getDeleted, 0)
               .orderByDesc(Instance::getCreateTime);
        
        List<Instance> instances = baseMapper.selectList(wrapper);
        
        // 处理字段值
        for (Instance instance : instances) {
            processFieldValues(instance, fieldDefinitions);
        }
        
        return instances;
    }
    
    /**
     * 创建实例
     */
    public void createInstance(Instance instance) {
        // 验证字段值
        validateFieldValues(instance);
        
        // 设置默认值
        instance.setCreateTime(LocalDateTime.now());
        instance.setUpdateTime(LocalDateTime.now());
        instance.setDeleted(0);
        
        // 处理字段值
        processFieldValuesForSave(instance);
        
        baseMapper.insert(instance);
    }
    
    /**
     * 更新实例
     */
    public void updateInstance(Instance instance) {
        instance.setUpdateTime(LocalDateTime.now());
        
        // 验证字段值
        validateFieldValues(instance);
        
        // 处理字段值
        processFieldValuesForSave(instance);
        
        baseMapper.updateById(instance);
    }
    
    /**
     * 删除实例
     */
    public void deleteInstance(Long id) {
        Instance instance = new Instance();
        instance.setId(id);
        instance.setDeleted(1);
        instance.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(instance);
    }
    
    /**
     * 批量创建实例
     */
    public void batchCreateInstances(List<Instance> instances) {
        for (Instance instance : instances) {
            createInstance(instance);
        }
    }
    
    /**
     * 处理字段值
     */
    private void processFieldValues(Instance instance, List<FieldDefinition> fieldDefinitions) {
        if (StringUtils.isNotBlank(instance.getFieldValues())) {
            try {
                Map<String, Object> fieldValuesMap = JsonUtils.parseObject(instance.getFieldValues(), Map.class);
                instance.setFieldValuesMap(fieldValuesMap);
            } catch (Exception e) {
                log.error("解析字段值失败", e);
            }
        }
    }
    
    /**
     * 验证字段值
     */
    private void validateFieldValues(Instance instance) {
        if (instance.getFieldValuesMap() != null) {
            List<FieldDefinition> fieldDefinitions = fieldDefinitionService.getByTableType(instance.getObjectType());
            
            for (FieldDefinition fieldDef : fieldDefinitions) {
                if (fieldDef.getIsInstanceField() == 1 && fieldDef.getIsRequired() == 1) {
                    Object value = instance.getFieldValuesMap().get(fieldDef.getFieldName());
                    if (value == null || StringUtils.isBlank(value.toString())) {
                        throw new ServiceException(fieldDef.getFieldLabel() + "不能为空");
                    }
                }
            }
        }
    }
    
    /**
     * 处理保存时的字段值
     */
    private void processFieldValuesForSave(Instance instance) {
        if (instance.getFieldValuesMap() != null) {
            instance.setFieldValues(JsonUtils.toJsonString(instance.getFieldValuesMap()));
        }
    }
    
    /**
     * 搜索实例
     */
    public PageResult<Instance> searchInstances(String objectType, InstanceSearchReq req) {
        Page<Instance> page = new Page<>(req.getPageNo(), req.getPageSize());
        
        LambdaQueryWrapper<Instance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Instance::getObjectType, objectType)
               .eq(Instance::getDeleted, 0)
               .like(StringUtils.isNotBlank(req.getInstanceName()), Instance::getInstanceName, req.getInstanceName())
               .like(StringUtils.isNotBlank(req.getInstanceCode()), Instance::getInstanceCode, req.getInstanceCode())
               .orderByDesc(Instance::getCreateTime);
        
        // 支持字段值搜索
        if (StringUtils.isNotBlank(req.getFieldValue())) {
            wrapper.apply("JSON_EXTRACT(field_values, '$.{0}') LIKE '%{1}%'", 
                         req.getFieldName(), req.getFieldValue());
        }
        
        return baseMapper.selectPage(page, wrapper);
    }
}
```

#### 3. **前端适配**
```typescript
// 实例管理组合式函数
export function useInstanceManagement(objectType: string, objectId: number) {
  const [instances, setInstances] = useState<Instance[]>([])
  const [fieldDefinitions, setFieldDefinitions] = useState<FieldDefinition[]>([])
  const [loading, setLoading] = useState(false)
  
  // 获取字段定义
  const fetchFieldDefinitions = async () => {
    const definitions = await getFieldDefinitionsApi(objectType)
    setFieldDefinitions(definitions.filter(f => f.isInstanceField === 1))
  }
  
  // 获取实例列表
  const fetchInstances = async () => {
    setLoading(true)
    try {
      const data = await getInstancesApi(objectType, objectId)
      setInstances(data)
    } finally {
      setLoading(false)
    }
  }
  
  // 创建实例
  const createInstance = async (formData: any) => {
    const fieldValues: Record<string, any> = {}
    
    // 处理实例字段值
    fieldDefinitions.forEach(field => {
      if (formData[field.fieldName] !== undefined) {
        fieldValues[field.fieldName] = formData[field.fieldName]
      }
    })
    
    await createInstanceApi({
      objectType,
      objectId,
      instanceName: formData.instanceName,
      instanceCode: formData.instanceCode,
      status: formData.status,
      fieldValues: JSON.stringify(fieldValues)
    })
    
    await fetchInstances()
  }
  
  // 更新实例
  const updateInstance = async (id: number, formData: any) => {
    const fieldValues: Record<string, any> = {}
    
    // 处理实例字段值
    fieldDefinitions.forEach(field => {
      if (formData[field.fieldName] !== undefined) {
        fieldValues[field.fieldName] = formData[field.fieldName]
      }
    })
    
    await updateInstanceApi(id, {
      instanceName: formData.instanceName,
      instanceCode: formData.instanceCode,
      status: formData.status,
      fieldValues: JSON.stringify(fieldValues)
    })
    
    await fetchInstances()
  }
  
  // 删除实例
  const deleteInstance = async (id: number) => {
    await deleteInstanceApi(id)
    await fetchInstances()
  }
  
  // 搜索实例
  const searchInstances = async (searchReq: InstanceSearchReq) => {
    setLoading(true)
    try {
      const data = await searchInstancesApi(objectType, searchReq)
      setInstances(data.records)
    } finally {
      setLoading(false)
    }
  }
  
  return {
    instances,
    fieldDefinitions,
    loading,
    fetchFieldDefinitions,
    fetchInstances,
    createInstance,
    updateInstance,
    deleteInstance,
    searchInstances
  }
}
```

## 性能优化建议

### 1. **JSON 字段索引**
```sql
-- 为常用的 JSON 字段创建虚拟列索引
ALTER TABLE sys_instance 
ADD COLUMN ext_field_value VARCHAR(100) 
GENERATED ALWAYS AS (JSON_UNQUOTE(JSON_EXTRACT(field_values, '$.field_value'))) VIRTUAL;

CREATE INDEX idx_ext_field_value ON sys_instance(object_type, ext_field_value);
```

### 2. **缓存策略**
```java
@Cacheable(value = "instance", key = "#objectType + '_' + #objectId")
public List<Instance> getInstancesByObject(String objectType, Long objectId) {
    // 查询实例
}
```

### 3. **分页查询优化**
```java
public PageResult<Instance> getInstancePage(String objectType, InstancePageReq req) {
    Page<Instance> page = new Page<>(req.getPageNo(), req.getPageSize());
    
    LambdaQueryWrapper<Instance> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Instance::getObjectType, objectType)
           .eq(Instance::getDeleted, 0)
           .like(StringUtils.isNotBlank(req.getInstanceName()), Instance::getInstanceName, req.getInstanceName())
           .orderByDesc(Instance::getCreateTime);
    
    // 支持字段值搜索
    if (StringUtils.isNotBlank(req.getFieldValue())) {
        wrapper.apply("JSON_EXTRACT(field_values, '$.{0}') LIKE '%{1}%'", 
                     req.getFieldName(), req.getFieldValue());
    }
    
    return baseMapper.selectPage(page, wrapper);
}
```

## 总结

对于实例存储，**推荐使用统一实例表设计**，理由如下：

1. **符合动态建表需求**：通过 JSON 字段支持任意字段值
2. **维护成本低**：只需要维护一套实例管理逻辑
3. **扩展性强**：新增字段只需更新字段定义表
4. **性能可控**：通过合理的索引设计保证性能
5. **开发效率高**：统一的API接口和数据结构
6. **数据一致性**：通过字段定义表保证数据一致性

这种设计既满足了大量实例的存储需求，又保持了技术实现的统一性，是实例存储的最佳选择！ 