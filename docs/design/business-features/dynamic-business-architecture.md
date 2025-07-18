# 动态业务架构设计文档

## 一、概述

本文档描述了动态业务系统的架构设计，旨在实现**用户无需编写任何代码**即可创建完整的动态业务系统。

## 二、核心设计原则

### 2.1 无代码原则
- 用户无需编写任何代码
- 通过配置驱动所有业务逻辑
- 即时生效，无需重启或重新编译

### 2.2 模块化原则
- 通用功能与业务逻辑分离
- 可复用的组件独立成模块
- 清晰的模块职责划分

## 三、模块架构设计

### 3.1 模块划分

```plaintext
cheers-framework/
├── cheers-common/                    # 通用工具类
├── cheers-web/                       # Web框架
├── cheers-mybatis/                   # 数据访问层
└── cheers-trees/                     # 通用树形结构支持

cheers-module-dynamic-business/        # 动态业务模块
├── 通用动态业务服务
├── 配置管理
└── 业务扩展点
```

### 3.2 模块职责

#### 3.2.1 cheers-trees 模块（通用树形支持）

**职责：**
- 提供通用的树形结构接口
- 实现树形数据的构建、查询、操作
- 提供树形工具类

**核心接口：**
```java
// 通用树形实体接口
public interface TreeEntity<T> {
    Long getId();
    Long getParentId();
    void setParentId(Long parentId);
    List<T> getChildren();
    void setChildren(List<T> children);
    Integer getSort();
    void setSort(Integer sort);
}

// 通用树形服务接口
public interface TreeService<T extends TreeEntity<T>> {
    List<T> getTree();
    List<T> getSubTree(Long parentId);
    T addNode(T node);
    void updateNode(T node);
    void deleteNode(Long id);
    void moveNode(Long id, Long newParentId);
    List<T> getNodePath(Long id);
}
```

#### 3.2.2 cheers-module-dynamic-business 模块（动态业务实现）

**职责：**
- 实现具体的动态业务逻辑
- 提供通用业务服务
- 管理业务配置
- 处理动态表结构

**核心服务：**
```java
// 通用动态业务服务
public interface DynamicBusinessService {
    Long create(String businessType, Map<String, Object> data);
    void update(String businessType, Long id, Map<String, Object> data);
    void delete(String businessType, Long id);
    Map<String, Object> get(String businessType, Long id);
    List<Map<String, Object>> list(String businessType, Map<String, Object> params);
    PageResult<Map<String, Object>> page(String businessType, PageParam pageParam, Map<String, Object> params);
}
```

## 四、无代码实现方案

### 4.1 通用接口设计

#### 4.1.1 通用业务服务接口

```java
// 通用业务服务接口 - 所有动态业务都使用这个接口
public interface DynamicBusinessService {
    
    /**
     * 通用创建方法
     */
    Long create(String businessType, Map<String, Object> data);
    
    /**
     * 通用更新方法
     */
    void update(String businessType, Long id, Map<String, Object> data);
    
    /**
     * 通用删除方法
     */
    void delete(String businessType, Long id);
    
    /**
     * 通用查询方法
     */
    Map<String, Object> get(String businessType, Long id);
    
    /**
     * 通用列表查询
     */
    List<Map<String, Object>> list(String businessType, Map<String, Object> params);
    
    /**
     * 通用分页查询
     */
    PageResult<Map<String, Object>> page(String businessType, PageParam pageParam, Map<String, Object> params);
}
```

#### 4.1.2 配置驱动的实现

```java
// 动态业务服务实现 - 一个实现类处理所有业务类型
@Service
public class DynamicBusinessServiceImpl implements DynamicBusinessService {
    
    @Resource
    private JdbcTemplate jdbcTemplate;
    
    @Resource
    private BusinessModelService businessModelService;
    
    @Resource
    private FieldDefinitionService fieldDefinitionService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(String businessType, Map<String, Object> data) {
        // 1. 获取业务模型配置
        BusinessModelDO model = businessModelService.getModelByCode(businessType);
        List<FieldDefinitionDO> fields = fieldDefinitionService.getFieldsByModelId(model.getId());
        
        // 2. 动态构建SQL
        String sql = buildInsertSQL(model.getTableName(), fields);
        Object[] params = buildParams(fields, data);
        
        // 3. 执行SQL
        jdbcTemplate.update(sql, params);
        return getLastInsertId();
    }
    
    @Override
    public Map<String, Object> get(String businessType, Long id) {
        BusinessModelDO model = businessModelService.getModelByCode(businessType);
        String sql = "SELECT * FROM " + model.getTableName() + " WHERE id = ?";
        return jdbcTemplate.queryForMap(sql, id);
    }
    
    @Override
    public List<Map<String, Object>> list(String businessType, Map<String, Object> params) {
        BusinessModelDO model = businessModelService.getModelByCode(businessType);
        List<FieldDefinitionDO> fields = fieldDefinitionService.getFieldsByModelId(model.getId());
        
        // 动态构建查询SQL
        StringBuilder sql = new StringBuilder("SELECT * FROM " + model.getTableName() + " WHERE 1=1");
        List<Object> queryParams = new ArrayList<>();
        
        // 根据字段配置动态添加查询条件
        for (FieldDefinitionDO field : fields) {
            if (params.containsKey(field.getCode()) && params.get(field.getCode()) != null) {
                sql.append(" AND ").append(field.getCode()).append(" = ?");
                queryParams.add(params.get(field.getCode()));
            }
        }
        
        sql.append(" ORDER BY create_time DESC");
        return jdbcTemplate.queryForList(sql.toString(), queryParams.toArray());
    }
    
    private String buildInsertSQL(String tableName, List<FieldDefinitionDO> fields) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder(") VALUES (");
        
        for (int i = 0; i < fields.size(); i++) {
            FieldDefinitionDO field = fields.get(i);
            sql.append(field.getCode());
            values.append("?");
            
            if (i < fields.size() - 1) {
                sql.append(", ");
                values.append(", ");
            }
        }
        
        values.append(")");
        return sql.toString() + values.toString();
    }
    
    private Object[] buildParams(List<FieldDefinitionDO> fields, Map<String, Object> data) {
        Object[] params = new Object[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            FieldDefinitionDO field = fields.get(i);
            params[i] = data.getOrDefault(field.getCode(), null);
        }
        return params;
    }
}
```

### 4.2 通用Controller设计

```java
// 通用动态业务控制器 - 处理所有动态业务
@RestController
@RequestMapping("/dynamic/business")
@Tag(name = "动态业务管理")
public class DynamicBusinessController {
    
    @Resource
    private DynamicBusinessService dynamicBusinessService;
    
    @PostMapping("/{businessType}/create")
    @Operation(summary = "创建动态业务数据")
    public CommonResult<Long> create(@PathVariable String businessType, 
                                   @RequestBody Map<String, Object> data) {
        Long id = dynamicBusinessService.create(businessType, data);
        return success(id);
    }
    
    @PutMapping("/{businessType}/{id}")
    @Operation(summary = "更新动态业务数据")
    public CommonResult<Boolean> update(@PathVariable String businessType,
                                      @PathVariable Long id,
                                      @RequestBody Map<String, Object> data) {
        dynamicBusinessService.update(businessType, id, data);
        return success(true);
    }
    
    @DeleteMapping("/{businessType}/{id}")
    @Operation(summary = "删除动态业务数据")
    public CommonResult<Boolean> delete(@PathVariable String businessType,
                                      @PathVariable Long id) {
        dynamicBusinessService.delete(businessType, id);
        return success(true);
    }
    
    @GetMapping("/{businessType}/{id}")
    @Operation(summary = "获取动态业务数据")
    public CommonResult<Map<String, Object>> get(@PathVariable String businessType,
                                                @PathVariable Long id) {
        Map<String, Object> data = dynamicBusinessService.get(businessType, id);
        return success(data);
    }
    
    @GetMapping("/{businessType}/list")
    @Operation(summary = "获取动态业务数据列表")
    public CommonResult<List<Map<String, Object>>> list(@PathVariable String businessType,
                                                       @RequestParam Map<String, Object> params) {
        List<Map<String, Object>> list = dynamicBusinessService.list(businessType, params);
        return success(list);
    }
    
    @GetMapping("/{businessType}/page")
    @Operation(summary = "分页获取动态业务数据")
    public CommonResult<PageResult<Map<String, Object>>> page(@PathVariable String businessType,
                                                             @Valid PageParam pageParam,
                                                             @RequestParam Map<String, Object> params) {
        PageResult<Map<String, Object>> page = dynamicBusinessService.page(businessType, pageParam, params);
        return success(page);
    }
}
```

## 五、使用流程

### 5.1 用户操作流程

```plaintext
用户操作流程：
1. 登录管理后台
2. 进入"动态业务管理"
3. 点击"创建业务模型"
4. 填写基本信息（名称、描述等）
5. 配置字段（字段名、类型、验证规则等）
6. 配置表单布局
7. 配置列表显示
8. 保存配置
9. 系统自动创建数据表
10. 立即可用，无需编写任何代码
```

### 5.2 API调用示例

```javascript
// 用户只需要通过API调用，无需编写任何代码

// 1. 创建组织架构树
const createOrgTree = async () => {
  await api.post('/dynamic/universal/org', {
    operation: 'tree',
    params: {
      treeOperation: 'addNode',
      name: '技术部',
      parentId: 1
    }
  })
}

// 2. 移动节点
const moveNode = async () => {
  await api.post('/dynamic/universal/org', {
    operation: 'tree',
    params: {
      treeOperation: 'moveNode',
      nodeId: 5,
      newParentId: 2
    }
  })
}

// 3. 获取树结构
const getTree = async () => {
  const response = await api.post('/dynamic/universal/org', {
    operation: 'tree',
    params: {
      treeOperation: 'getTree'
    }
  })
  return response.data
}
```

## 六、优势与特点

### 6.1 完全无代码的优势

1. **零编程门槛**：业务人员无需任何编程知识
2. **即时生效**：配置保存后立即可用
3. **统一接口**：所有动态业务使用相同的API
4. **配置驱动**：通过配置控制所有行为
5. **易于维护**：无需管理大量生成的代码
6. **性能优化**：统一的实现，便于优化

### 6.2 与代码生成方案对比

| 方案 | 用户编程要求 | 维护复杂度 | 性能 | 灵活性 |
|------|-------------|-----------|------|--------|
| 代码生成 | 需要编写接口 | 高（管理大量文件） | 高 | 高 |
| 配置驱动 | 无需编程 | 低（统一管理） | 中 | 中 |

## 七、开发指导

### 7.1 开发原则

1. **配置优先**：优先使用配置而非代码
2. **通用接口**：设计通用的接口和工具类
3. **模块分离**：通用功能与业务逻辑分离
4. **扩展友好**：预留扩展点，支持自定义逻辑

### 7.2 开发步骤

1. **设计通用接口**：在cheers-trees中设计通用树形接口
2. **实现通用工具**：提供通用的树形操作工具类
3. **创建动态服务**：在dynamic-business中实现通用业务服务
4. **设计配置管理**：实现配置驱动的业务逻辑
5. **开发前端组件**：创建通用的动态组件
6. **测试验证**：确保无代码创建业务的功能完整性

### 7.3 注意事项

1. **性能考虑**：动态SQL构建的性能优化
2. **安全考虑**：防止SQL注入等安全问题
3. **事务管理**：确保数据一致性
4. **错误处理**：完善的异常处理机制
5. **缓存策略**：合理的缓存设计

## 八、总结

本架构设计实现了真正的**无代码动态业务创建**，通过以下核心设计：

1. **模块化架构**：通用功能与业务逻辑分离
2. **配置驱动**：通过配置控制所有业务行为
3. **通用接口**：统一的API接口设计
4. **即时生效**：配置保存后立即可用

这样的设计既保证了架构的清晰性，又实现了用户无需编写任何代码就能创建完整的动态业务系统的目标。 