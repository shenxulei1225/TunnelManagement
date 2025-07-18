# 动态树形结构实现文档

## 一、概述

本文档详细描述了动态树形结构的实现方案，旨在实现**用户无需编写任何代码**即可创建和管理树形业务数据。

## 二、核心设计

### 2.1 通用树形接口设计

#### 2.1.1 树形实体接口

```java
// cheers-trees/src/main/java/com/cheers/framework/trees/
package com.cheers.framework.trees;

/**
 * 通用树形实体接口
 */
public interface TreeEntity<T> {
    
    /**
     * 获取节点ID
     */
    Long getId();
    
    /**
     * 获取父节点ID
     */
    Long getParentId();
    
    /**
     * 设置父节点ID
     */
    void setParentId(Long parentId);
    
    /**
     * 获取子节点列表
     */
    List<T> getChildren();
    
    /**
     * 设置子节点列表
     */
    void setChildren(List<T> children);
    
    /**
     * 获取排序字段
     */
    Integer getSort();
    
    /**
     * 设置排序字段
     */
    void setSort(Integer sort);
}
```

#### 2.1.2 树形服务接口

```java
/**
 * 通用树形服务接口
 */
public interface TreeService<T extends TreeEntity<T>> {
    
    /**
     * 获取完整树结构
     */
    List<T> getTree();
    
    /**
     * 获取指定节点的子树
     */
    List<T> getSubTree(Long parentId);
    
    /**
     * 添加节点
     */
    T addNode(T node);
    
    /**
     * 更新节点
     */
    void updateNode(T node);
    
    /**
     * 删除节点
     */
    void deleteNode(Long id);
    
    /**
     * 移动节点
     */
    void moveNode(Long id, Long newParentId);
    
    /**
     * 获取节点路径
     */
    List<T> getNodePath(Long id);
}
```

### 2.2 通用树形工具类

```java
/**
 * 通用树形工具类
 */
@Component
public class TreeUtils {
    
    /**
     * 构建树形结构
     */
    public static <T extends TreeEntity<T>> List<T> buildTree(List<T> nodes) {
        Map<Long, T> nodeMap = new HashMap<>();
        List<T> rootNodes = new ArrayList<>();
        
        // 构建节点映射
        for (T node : nodes) {
            nodeMap.put(node.getId(), node);
        }
        
        // 构建树形结构
        for (T node : nodes) {
            if (node.getParentId() == null || node.getParentId() == 0) {
                rootNodes.add(node);
            } else {
                T parent = nodeMap.get(node.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(node);
                }
            }
        }
        
        return rootNodes;
    }
    
    /**
     * 扁平化树形结构
     */
    public static <T extends TreeEntity<T>> List<T> flattenTree(List<T> tree) {
        List<T> result = new ArrayList<>();
        flattenTreeRecursive(tree, result);
        return result;
    }
    
    private static <T extends TreeEntity<T>> void flattenTreeRecursive(List<T> nodes, List<T> result) {
        for (T node : nodes) {
            result.add(node);
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                flattenTreeRecursive(node.getChildren(), result);
            }
        }
    }
}
```

## 三、动态树形业务实现

### 3.1 动态树形实体

```java
// cheers-module-dynamic-business/src/main/java/com/cheers/module/dynamic/
@Data
@TableName("dynamic_tree_node")
public class DynamicTreeNodeDO implements TreeEntity<DynamicTreeNodeDO> {
    
    @TableId
    private Long id;
    
    private String businessType;  // 业务类型
    
    private Long parentId;
    
    private String name;
    
    private String code;
    
    private Integer sort;
    
    private String config;  // 节点配置JSON
    
    private List<DynamicTreeNodeDO> children;
    
    // 实现TreeEntity接口方法
    @Override
    public Long getId() {
        return id;
    }
    
    @Override
    public Long getParentId() {
        return parentId;
    }
    
    @Override
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    @Override
    public List<DynamicTreeNodeDO> getChildren() {
        return children;
    }
    
    @Override
    public void setChildren(List<DynamicTreeNodeDO> children) {
        this.children = children;
    }
    
    @Override
    public Integer getSort() {
        return sort;
    }
    
    @Override
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
```

### 3.2 动态树形服务实现

```java
/**
 * 动态树形业务服务
 */
@Service
public class DynamicTreeService implements TreeService<DynamicTreeNodeDO> {
    
    @Resource
    private DynamicTreeNodeMapper dynamicTreeNodeMapper;
    
    @Resource
    private TreeUtils treeUtils;
    
    @Override
    public List<DynamicTreeNodeDO> getTree() {
        List<DynamicTreeNodeDO> nodes = dynamicTreeNodeMapper.selectList();
        return treeUtils.buildTree(nodes);
    }
    
    @Override
    public List<DynamicTreeNodeDO> getSubTree(Long parentId) {
        List<DynamicTreeNodeDO> allNodes = dynamicTreeNodeMapper.selectList();
        List<DynamicTreeNodeDO> subNodes = filterSubNodes(allNodes, parentId);
        return treeUtils.buildTree(subNodes);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DynamicTreeNodeDO addNode(DynamicTreeNodeDO node) {
        // 设置排序号
        if (node.getSort() == null) {
            node.setSort(getNextSort(node.getParentId()));
        }
        
        dynamicTreeNodeMapper.insert(node);
        return node;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveNode(Long id, Long newParentId) {
        DynamicTreeNodeDO node = dynamicTreeNodeMapper.selectById(id);
        if (node == null) {
            throw new ServiceException("节点不存在");
        }
        
        // 检查是否形成循环引用
        if (isCircularReference(id, newParentId)) {
            throw new ServiceException("不能将节点移动到其子节点下");
        }
        
        node.setParentId(newParentId);
        dynamicTreeNodeMapper.updateById(node);
    }
    
    /**
     * 获取下一个排序号
     */
    private Integer getNextSort(Long parentId) {
        Integer maxSort = dynamicTreeNodeMapper.selectMaxSortByParentId(parentId);
        return maxSort == null ? 1 : maxSort + 1;
    }
    
    /**
     * 检查循环引用
     */
    private boolean isCircularReference(Long nodeId, Long newParentId) {
        if (nodeId.equals(newParentId)) {
            return true;
        }
        
        DynamicTreeNodeDO parent = dynamicTreeNodeMapper.selectById(newParentId);
        while (parent != null) {
            if (parent.getId().equals(nodeId)) {
                return true;
            }
            parent = dynamicTreeNodeMapper.selectById(parent.getParentId());
        }
        
        return false;
    }
}
```

## 四、通用动态业务服务

```java
/**
 * 通用动态业务服务
 */
@Service
public class UniversalDynamicService {
    
    @Resource
    private JdbcTemplate jdbcTemplate;
    
    @Resource
    private DynamicTreeService dynamicTreeService;
    
    /**
     * 通用业务操作
     */
    public Object handleBusiness(String businessType, String operation, Map<String, Object> params) {
        BusinessModelDO model = getBusinessModel(businessType);
        
        switch (operation) {
            case "create":
                return handleCreate(model, params);
            case "update":
                return handleUpdate(model, params);
            case "delete":
                return handleDelete(model, params);
            case "get":
                return handleGet(model, params);
            case "list":
                return handleList(model, params);
            case "tree":
                return handleTree(model, params);
            case "move":
                return handleMove(model, params);
            default:
                throw new ServiceException("不支持的操作: " + operation);
        }
    }
    
    /**
     * 处理树形结构操作
     */
    private Object handleTree(BusinessModelDO model, Map<String, Object> params) {
        String treeOperation = (String) params.get("treeOperation");
        
        switch (treeOperation) {
            case "getTree":
                return getTree(model);
            case "getSubTree":
                Long parentId = (Long) params.get("parentId");
                return getSubTree(model, parentId);
            case "addNode":
                return addTreeNode(model, params);
            case "moveNode":
                Long nodeId = (Long) params.get("nodeId");
                Long newParentId = (Long) params.get("newParentId");
                return moveTreeNode(model, nodeId, newParentId);
            default:
                throw new ServiceException("不支持的树操作: " + treeOperation);
        }
    }
    
    /**
     * 获取完整树结构
     */
    private List<Map<String, Object>> getTree(BusinessModelDO model) {
        // 查询数据
        String sql = "SELECT * FROM " + model.getTableName() + " ORDER BY sort, id";
        List<Map<String, Object>> nodes = jdbcTemplate.queryForList(sql);
        
        // 使用通用树工具构建树形结构
        return TreeUtils.buildTree(nodes);
    }
    
    /**
     * 获取子树
     */
    private List<Map<String, Object>> getSubTree(BusinessModelDO model, Long parentId) {
        // 递归查询所有子节点
        List<Map<String, Object>> allNodes = getAllSubNodes(model, parentId);
        return TreeUtils.buildTree(allNodes);
    }
    
    /**
     * 递归获取所有子节点
     */
    private List<Map<String, Object>> getAllSubNodes(BusinessModelDO model, Long parentId) {
        String sql = "SELECT * FROM " + model.getTableName() + " WHERE parent_id = ?";
        List<Map<String, Object>> children = jdbcTemplate.queryForList(sql, parentId);
        
        List<Map<String, Object>> allNodes = new ArrayList<>(children);
        for (Map<String, Object> child : children) {
            Long childId = (Long) child.get("id");
            allNodes.addAll(getAllSubNodes(model, childId));
        }
        
        return allNodes;
    }
    
    /**
     * 添加树节点
     */
    private Long addTreeNode(BusinessModelDO model, Map<String, Object> params) {
        // 构建插入SQL
        StringBuilder sql = new StringBuilder("INSERT INTO " + model.getTableName() + " (");
        StringBuilder values = new StringBuilder(") VALUES (");
        List<Object> sqlParams = new ArrayList<>();
        
        // 添加字段
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!entry.getKey().equals("treeOperation")) {
                sql.append(entry.getKey()).append(", ");
                values.append("?, ");
                sqlParams.add(entry.getValue());
            }
        }
        
        // 添加创建时间
        sql.append("create_time, ");
        values.append("NOW(), ");
        
        // 移除最后的逗号
        sql.setLength(sql.length() - 2);
        values.setLength(values.length() - 2);
        
        sql.append(values).append(")");
        
        jdbcTemplate.update(sql.toString(), sqlParams.toArray());
        return getLastInsertId();
    }
    
    /**
     * 移动树节点
     */
    private void moveTreeNode(BusinessModelDO model, Long nodeId, Long newParentId) {
        String sql = "UPDATE " + model.getTableName() + " SET parent_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, newParentId, nodeId);
    }
}
```

## 五、通用Controller

```java
/**
 * 通用动态业务控制器
 */
@RestController
@RequestMapping("/dynamic/universal")
@Tag(name = "通用动态业务")
public class UniversalDynamicController {
    
    @Resource
    private UniversalDynamicService universalDynamicService;
    
    @PostMapping("/{businessType}")
    @Operation(summary = "通用业务操作")
    public CommonResult<Object> handleBusiness(@PathVariable String businessType,
                                            @RequestBody Map<String, Object> request) {
        String operation = (String) request.get("operation");
        Map<String, Object> params = (Map<String, Object>) request.get("params");
        
        Object result = universalDynamicService.handleBusiness(businessType, operation, params);
        return success(result);
    }
    
    @PostMapping("/{businessType}/tree")
    @Operation(summary = "树形结构操作")
    public CommonResult<Object> handleTree(@PathVariable String businessType,
                                         @RequestBody Map<String, Object> request) {
        request.put("operation", "tree");
        return handleBusiness(businessType, request);
    }
}
```

## 六、使用示例

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

## 七、优势总结

### 7.1 完全无代码的优势

1. **零编程门槛**：业务人员无需任何编程知识
2. **即时生效**：配置保存后立即可用
3. **统一接口**：所有动态业务使用相同的API
4. **配置驱动**：通过配置控制所有行为
5. **易于维护**：无需管理大量生成的代码
6. **性能优化**：统一的实现，便于优化

### 7.2 树形结构特点

1. **通用性**：支持任意业务类型的树形结构
2. **可拖拽**：支持节点拖拽调整位置
3. **递归操作**：支持递归删除、移动等操作
4. **循环检测**：防止形成循环引用
5. **路径查询**：支持获取节点完整路径

## 八、开发指导

### 8.1 开发步骤

1. **实现通用树形接口**：在cheers-trees中实现TreeEntity和TreeService
2. **创建树形工具类**：提供通用的树形操作工具
3. **实现动态树服务**：在dynamic-business中实现具体的树形业务逻辑
4. **开发通用控制器**：提供统一的API接口
5. **创建前端组件**：实现可拖拽的树形组件
6. **测试验证**：确保树形操作的功能完整性

### 8.2 注意事项

1. **性能考虑**：大量节点时的查询性能优化
2. **并发安全**：节点操作时的并发控制
3. **数据一致性**：确保树形结构的完整性
4. **用户体验**：拖拽操作的流畅性
5. **错误处理**：完善的异常处理机制

这样的设计实现了真正的**无代码树形业务创建**，用户只需通过配置和API调用即可创建和管理复杂的树形结构！ 