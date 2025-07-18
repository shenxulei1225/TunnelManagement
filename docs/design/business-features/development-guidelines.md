# 动态业务开发指导文档

## 一、开发概述

### 1.1 开发目标

实现**用户无需编写任何代码**即可创建完整的动态业务系统，包括：
- 动态业务模型创建
- 动态表结构管理
- 动态树形结构支持
- 通用API接口
- 配置驱动的业务逻辑

### 1.2 核心原则

1. **无代码原则**：用户无需编写任何代码
2. **配置驱动**：通过配置控制所有业务行为
3. **模块化设计**：通用功能与业务逻辑分离
4. **性能优化**：使用treePath等优化技术
5. **扩展友好**：预留扩展点，支持自定义逻辑

## 二、模块架构设计

### 2.1 模块划分

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

### 2.2 模块职责

#### 2.2.1 cheers-trees 模块

**职责：**
- 提供通用的树形结构接口
- 实现树形数据的构建、查询、操作
- 提供树形工具类
- 支持treePath优化

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

#### 2.2.2 cheers-module-dynamic-business 模块

**职责：**
- 实现具体的动态业务逻辑
- 提供通用业务服务
- 管理业务配置
- 处理动态表结构
- 支持treePath优化

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

## 三、开发步骤

### 3.1 第一步：设计通用接口

#### 3.1.1 在cheers-trees中设计通用树形接口

```java
// cheers-trees/src/main/java/com/cheers/framework/trees/
package com.cheers.framework.trees;

/**
 * 通用树形实体接口
 */
public interface TreeEntity<T> {
    Long getId();
    Long getParentId();
    void setParentId(Long parentId);
    List<T> getChildren();
    void setChildren(List<T> children);
    Integer getSort();
    void setSort(Integer sort);
}

/**
 * 通用树形服务接口
 */
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

#### 3.1.2 创建树形工具类

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

### 3.2 第二步：实现动态业务服务

#### 3.2.1 创建动态树节点实体

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
    
    private String treePath;  // 树路径，如：/1/5/10/
    
    private Integer level;    // 层级深度
    
    private Integer sort;
    
    private String config;    // 节点配置JSON
    
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
    
    /**
     * 构建树路径
     */
    public void buildTreePath(String parentTreePath) {
        this.treePath = (parentTreePath == null ? "/" : parentTreePath) + this.id + "/";
        this.level = this.treePath.split("/").length - 1;
    }
    
    /**
     * 获取父节点ID列表
     */
    public List<Long> getParentIds() {
        if (StringUtils.isEmpty(treePath) || "/".equals(treePath)) {
            return new ArrayList<>();
        }
        
        return Arrays.stream(treePath.split("/"))
            .filter(id -> !id.isEmpty())
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }
    
    /**
     * 获取路径名称
     */
    public String getPathName() {
        return treePath.replaceAll("/", " / ");
    }
}
```

#### 3.2.2 实现动态树形服务

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
        // 使用treePath优化查询
        DynamicTreeNodeDO parent = dynamicTreeNodeMapper.selectById(parentId);
        if (parent == null) {
            return new ArrayList<>();
        }
        
        // 查询所有以parent.treePath开头的节点
        String prefix = parent.getTreePath();
        return dynamicTreeNodeMapper.selectByTreePathPrefix(prefix);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DynamicTreeNodeDO addNode(DynamicTreeNodeDO node) {
        // 设置排序号
        if (node.getSort() == null) {
            node.setSort(getNextSort(node.getParentId()));
        }
        
        // 构建树路径
        if (node.getParentId() != null && node.getParentId() != 0) {
            DynamicTreeNodeDO parent = dynamicTreeNodeMapper.selectById(node.getParentId());
            if (parent == null) {
                throw new ServiceException("父节点不存在");
            }
            node.buildTreePath(parent.getTreePath());
        } else {
            node.buildTreePath("/");
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
        
        // 获取新父节点的树路径
        String newParentTreePath = "/";
        if (newParentId != null && newParentId != 0) {
            DynamicTreeNodeDO newParent = dynamicTreeNodeMapper.selectById(newParentId);
            if (newParent == null) {
                throw new ServiceException("新父节点不存在");
            }
            newParentTreePath = newParent.getTreePath();
        }
        
        // 更新当前节点的树路径
        String oldTreePath = node.getTreePath();
        node.setParentId(newParentId);
        node.buildTreePath(newParentTreePath);
        
        // 更新节点
        dynamicTreeNodeMapper.updateById(node);
        
        // 更新所有子节点的树路径
        updateChildrenTreePath(node, oldTreePath);
    }
    
    @Override
    public List<DynamicTreeNodeDO> getNodePath(Long id) {
        DynamicTreeNodeDO node = dynamicTreeNodeMapper.selectById(id);
        if (node == null) {
            return new ArrayList<>();
        }
        
        // 通过treePath直接获取父节点ID列表
        List<Long> parentIds = node.getParentIds();
        if (parentIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 批量查询父节点
        return dynamicTreeNodeMapper.selectBatchIds(parentIds);
    }
    
    /**
     * 更新子节点的树路径
     */
    private void updateChildrenTreePath(DynamicTreeNodeDO node, String oldTreePath) {
        // 查询所有以oldTreePath开头的子节点
        List<DynamicTreeNodeDO> children = dynamicTreeNodeMapper.selectByTreePathPrefix(oldTreePath);
        
        for (DynamicTreeNodeDO child : children) {
            // 构建新的树路径
            String newTreePath = node.getTreePath() + child.getId() + "/";
            child.setTreePath(newTreePath);
            child.setLevel(newTreePath.split("/").length - 1);
            
            // 更新子节点
            dynamicTreeNodeMapper.updateById(child);
        }
    }
    
    /**
     * 检查循环引用
     */
    private boolean isCircularReference(Long nodeId, Long newParentId) {
        if (nodeId.equals(newParentId)) {
            return true;
        }
        
        DynamicTreeNodeDO newParent = dynamicTreeNodeMapper.selectById(newParentId);
        if (newParent == null) {
            return false;
        }
        
        // 检查新父节点是否在当前节点的子树中
        return newParent.getTreePath().startsWith(
            dynamicTreeNodeMapper.selectById(nodeId).getTreePath()
        );
    }
}
```

### 3.3 第三步：创建通用业务服务

#### 3.3.1 实现通用动态业务服务

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

### 3.4 第四步：开发通用控制器

#### 3.4.1 创建通用动态业务控制器

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

### 3.5 第五步：创建前端组件

#### 3.5.1 通用树形组件

```typescript
// 通用树形组件
<template>
  <div class="dynamic-tree">
    <el-tree
      :data="treeData"
      :props="treeProps"
      :draggable="draggable"
      @node-drop="handleNodeDrop"
      @node-click="handleNodeClick"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <span>{{ data.name }}</span>
          <span class="path-info">{{ data.pathName }}</span>
          <span class="level-info">L{{ data.level }}</span>
          <span class="actions">
            <el-button size="small" @click.stop="handleAdd(data)">添加</el-button>
            <el-button size="small" @click.stop="handleEdit(data)">编辑</el-button>
            <el-button size="small" @click.stop="handleDelete(data)">删除</el-button>
          </span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  businessType: String,
  draggable: {
    type: Boolean,
    default: true
  }
})

const treeData = ref([])
const treeProps = {
  children: 'children',
  label: 'name'
}

// 加载树数据
const loadTreeData = async () => {
  const response = await api.post(`/dynamic/universal/${props.businessType}/tree`, {
    operation: 'tree',
    params: {
      treeOperation: 'getTree'
    }
  })
  treeData.value = response.data.map(node => ({
    ...node,
    pathName: node.treePath.replace(/\//g, ' / ').trim()
  }))
}

// 处理节点拖拽
const handleNodeDrop = async (draggingNode, dropNode, dropType) => {
  const nodeId = draggingNode.data.id
  const newParentId = dropType === 'inner' ? dropNode.data.id : dropNode.data.parentId
  
  await api.post(`/dynamic/universal/${props.businessType}/tree`, {
    operation: 'tree',
    params: {
      treeOperation: 'moveNode',
      nodeId: nodeId,
      newParentId: newParentId
    }
  })
  
  await loadTreeData()
}

// 添加节点
const handleAdd = async (parentNode) => {
  const nodeName = await showInputDialog('请输入节点名称')
  if (nodeName) {
    await api.post(`/dynamic/universal/${props.businessType}/tree`, {
      operation: 'tree',
      params: {
        treeOperation: 'addNode',
        name: nodeName,
        parentId: parentNode ? parentNode.id : 0
      }
    })
    await loadTreeData()
  }
}

// 编辑节点
const handleEdit = async (node) => {
  const newName = await showInputDialog('请输入新名称', node.name)
  if (newName) {
    await api.post(`/dynamic/universal/${props.businessType}`, {
      operation: 'update',
      params: {
        id: node.id,
        name: newName
      }
    })
    await loadTreeData()
  }
}

// 删除节点
const handleDelete = async (node) => {
  const confirmed = await showConfirmDialog('确定要删除这个节点吗？')
  if (confirmed) {
    await api.post(`/dynamic/universal/${props.businessType}`, {
      operation: 'delete',
      params: {
        id: node.id
      }
    })
    await loadTreeData()
  }
}

onMounted(() => {
  loadTreeData()
})
</script>
```

### 3.6 第六步：测试验证

#### 3.6.1 功能测试

```javascript
// 测试用例
describe('动态树形业务测试', () => {
  
  test('创建节点', async () => {
    const result = await api.post('/dynamic/universal/org', {
      operation: 'tree',
      params: {
        treeOperation: 'addNode',
        name: '技术部',
        parentId: 1
      }
    })
    expect(result.data).toBeDefined()
  })
  
  test('移动节点', async () => {
    const result = await api.post('/dynamic/universal/org', {
      operation: 'tree',
      params: {
        treeOperation: 'moveNode',
        nodeId: 5,
        newParentId: 2
      }
    })
    expect(result.success).toBe(true)
  })
  
  test('获取树结构', async () => {
    const result = await api.post('/dynamic/universal/org', {
      operation: 'tree',
      params: {
        treeOperation: 'getTree'
      }
    })
    expect(Array.isArray(result.data)).toBe(true)
  })
})
```

#### 3.6.2 性能测试

```java
// 性能测试
@Test
public void testTreePerformance() {
    // 测试大量节点的查询性能
    long startTime = System.currentTimeMillis();
    List<DynamicTreeNodeDO> tree = dynamicTreeService.getTree();
    long endTime = System.currentTimeMillis();
    
    // 确保查询时间在可接受范围内
    assertTrue("查询时间过长", (endTime - startTime) < 1000);
}
```

## 四、最佳实践

### 4.1 设计原则

1. **配置优先**：优先使用配置而非代码
2. **通用接口**：设计通用的接口和工具类
3. **模块分离**：通用功能与业务逻辑分离
4. **扩展友好**：预留扩展点，支持自定义逻辑
5. **性能优化**：使用treePath等优化技术

### 4.2 开发规范

1. **命名规范**：
   - 类名：PascalCase
   - 方法名：camelCase
   - 常量：UPPER_SNAKE_CASE
   - 数据库字段：snake_case

2. **代码规范**：
   - 使用Lombok减少样板代码
   - 添加完整的JavaDoc注释
   - 遵循阿里巴巴Java开发手册

3. **异常处理**：
   - 使用统一的异常处理机制
   - 提供有意义的错误信息
   - 记录详细的错误日志

### 4.3 性能优化

1. **数据库优化**：
   - 合理设计索引
   - 使用treePath优化查询
   - 避免N+1查询问题

2. **缓存策略**：
   - 缓存热点数据
   - 使用Redis缓存
   - 实现缓存更新机制

3. **并发控制**：
   - 使用事务确保数据一致性
   - 避免死锁问题
   - 实现乐观锁机制

## 五、注意事项

### 5.1 安全考虑

1. **SQL注入防护**：
   - 使用参数化查询
   - 验证用户输入
   - 限制SQL执行权限

2. **权限控制**：
   - 实现细粒度权限控制
   - 基于路径进行权限验证
   - 记录操作日志

3. **数据验证**：
   - 验证业务规则
   - 检查数据完整性
   - 防止循环引用

### 5.2 维护考虑

1. **数据备份**：
   - 定期备份重要数据
   - 实现数据恢复机制
   - 监控数据变化

2. **版本管理**：
   - 使用Git进行版本控制
   - 记录重要的变更日志
   - 实现回滚机制

3. **监控告警**：
   - 监控系统性能
   - 设置告警阈值
   - 及时处理异常

### 5.3 扩展考虑

1. **插件机制**：
   - 支持自定义业务逻辑
   - 实现插件接口
   - 提供扩展点

2. **多租户支持**：
   - 实现租户隔离
   - 支持租户配置
   - 管理租户资源

3. **国际化支持**：
   - 支持多语言
   - 实现本地化配置
   - 处理时区问题

## 六、总结

通过以上开发指导，我们实现了真正的**无代码动态业务创建**系统：

1. **模块化架构**：通用功能与业务逻辑分离
2. **配置驱动**：通过配置控制所有业务行为
3. **通用接口**：统一的API接口设计
4. **性能优化**：使用treePath等优化技术
5. **即时生效**：配置保存后立即可用

这样的设计既保证了架构的清晰性，又实现了用户无需编写任何代码就能创建完整的动态业务系统的目标。 