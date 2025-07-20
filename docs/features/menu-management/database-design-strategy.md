# 树形数据数据库设计策略

## 概述

对于 `hierarchy_group`（多级组）和 `category`（分类）等树形数据，我们需要考虑数据库设计的最佳实践。

## 设计方案对比

### 方案一：统一表设计（推荐）

#### 核心思想
- 使用一个通用的树形表结构
- 通过 `type` 字段区分不同业务类型
- 统一的层级管理逻辑

#### 表结构设计
```sql
-- 通用树形数据表
CREATE TABLE `sys_tree_node` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(50) NOT NULL COMMENT '业务类型：hierarchy/category/department/region/menu',
  `name` varchar(100) NOT NULL COMMENT '节点名称',
  `code` varchar(100) DEFAULT NULL COMMENT '节点编码',
  `parent_id` bigint DEFAULT 0 COMMENT '父级ID',
  `level` int DEFAULT 0 COMMENT '层级',
  `path` varchar(500) DEFAULT '' COMMENT '路径',
  `sort` int DEFAULT 0 COMMENT '排序',
  `icon` varchar(50) DEFAULT 'Folder' COMMENT '图标',
  `color` varchar(20) DEFAULT '#409EFF' COMMENT '颜色',
  `description` varchar(500) DEFAULT '' COMMENT '描述',
  `status` tinyint DEFAULT 0 COMMENT '状态：0-正常，1-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_type_parent` (`type`, `parent_id`),
  KEY `idx_type_sort` (`type`, `sort`),
  KEY `idx_type_path` (`type`, `path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用树形数据表';
```

#### 优势
1. **统一管理**：所有树形数据使用相同的表结构
2. **简化维护**：只需要维护一套树形逻辑
3. **性能优化**：可以针对树形查询进行统一优化
4. **扩展性强**：新增业务类型只需添加记录，无需修改表结构
5. **数据一致性**：统一的约束和索引策略

#### 劣势
1. **表可能较大**：所有树形数据集中在一个表中
2. **查询复杂度**：需要根据 `type` 字段过滤
3. **业务隔离**：不同业务的数据在同一个表中

### 方案二：分表设计

#### 核心思想
- 每种业务类型使用独立的表
- 表结构相似但独立管理
- 业务逻辑完全隔离

#### 表结构设计
```sql
-- 多级组表
CREATE TABLE `sys_hierarchy_group` (
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
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort` (`sort`),
  KEY `idx_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='多级组表';

-- 分类表
CREATE TABLE `sys_category` (
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
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort` (`sort`),
  KEY `idx_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 部门表
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '部门名称',
  `code` varchar(100) DEFAULT NULL COMMENT '部门编码',
  `parent_id` bigint DEFAULT 0 COMMENT '父级ID',
  `level` int DEFAULT 0 COMMENT '层级',
  `path` varchar(500) DEFAULT '' COMMENT '路径',
  `sort` int DEFAULT 0 COMMENT '排序',
  `icon` varchar(50) DEFAULT 'OfficeBuilding' COMMENT '图标',
  `color` varchar(20) DEFAULT '#E6A23C' COMMENT '颜色',
  `description` varchar(500) DEFAULT '' COMMENT '描述',
  `status` tinyint DEFAULT 0 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort` (`sort`),
  KEY `idx_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';
```

#### 优势
1. **业务隔离**：不同业务的数据完全独立
2. **查询性能**：针对特定业务的查询性能更好
3. **扩展性**：可以为不同业务定制不同的字段
4. **维护性**：每个表的维护相对独立

#### 劣势
1. **代码重复**：需要为每个表写相似的CRUD逻辑
2. **维护成本**：需要维护多套相似的代码
3. **数据一致性**：不同表之间的约束可能不一致
4. **扩展困难**：新增业务类型需要创建新表

## 推荐方案：统一表设计

### 理由

1. **符合当前架构**：与前端通用框架的设计理念一致
2. **简化开发**：只需要一套树形管理逻辑
3. **统一维护**：所有树形数据的维护逻辑统一
4. **性能可控**：通过合理的索引设计保证性能

### 实现方案

#### 1. **后端实体设计**
```java
@Data
@TableName("sys_tree_node")
public class TreeNode {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("type")
    private String type; // hierarchy, category, department, region, menu
    
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
    
    // 子节点（非数据库字段）
    @TableField(exist = false)
    private List<TreeNode> children;
}
```

#### 2. **后端服务设计**
```java
@Service
public class TreeNodeService {
    
    /**
     * 获取指定类型的树形数据
     */
    public List<TreeNode> getTreeByType(String type) {
        LambdaQueryWrapper<TreeNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TreeNode::getType, type)
               .eq(TreeNode::getDeleted, 0)
               .orderByAsc(TreeNode::getSort);
        
        List<TreeNode> nodes = baseMapper.selectList(wrapper);
        return buildTree(nodes);
    }
    
    /**
     * 创建节点
     */
    public void createNode(TreeNode node) {
        // 设置默认值
        node.setCreateTime(LocalDateTime.now());
        node.setUpdateTime(LocalDateTime.now());
        node.setDeleted(0);
        
        // 计算层级和路径
        calculateLevelAndPath(node);
        
        baseMapper.insert(node);
    }
    
    /**
     * 更新节点
     */
    public void updateNode(TreeNode node) {
        node.setUpdateTime(LocalDateTime.now());
        
        // 如果父级发生变化，重新计算层级和路径
        TreeNode oldNode = baseMapper.selectById(node.getId());
        if (!Objects.equals(oldNode.getParentId(), node.getParentId())) {
            calculateLevelAndPath(node);
        }
        
        baseMapper.updateById(node);
    }
    
    /**
     * 删除节点
     */
    public void deleteNode(Long id) {
        // 检查是否有子节点
        LambdaQueryWrapper<TreeNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TreeNode::getParentId, id)
               .eq(TreeNode::getDeleted, 0);
        
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new ServiceException("存在子节点，无法删除");
        }
        
        // 逻辑删除
        TreeNode node = new TreeNode();
        node.setId(id);
        node.setDeleted(1);
        node.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(node);
    }
    
    /**
     * 构建树形结构
     */
    private List<TreeNode> buildTree(List<TreeNode> nodes) {
        Map<Long, TreeNode> nodeMap = new HashMap<>();
        List<TreeNode> rootNodes = new ArrayList<>();
        
        // 构建节点映射
        for (TreeNode node : nodes) {
            nodeMap.put(node.getId(), node);
        }
        
        // 构建树形结构
        for (TreeNode node : nodes) {
            if (node.getParentId() == 0) {
                rootNodes.add(node);
            } else {
                TreeNode parent = nodeMap.get(node.getParentId());
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
     * 计算层级和路径
     */
    private void calculateLevelAndPath(TreeNode node) {
        if (node.getParentId() == 0) {
            node.setLevel(1);
            node.setPath("/" + node.getId());
        } else {
            TreeNode parent = baseMapper.selectById(node.getParentId());
            if (parent != null) {
                node.setLevel(parent.getLevel() + 1);
                node.setPath(parent.getPath() + "/" + node.getId());
            }
        }
    }
}
```

#### 3. **前端API适配**
```typescript
// 多级组API
export const getHierarchyGroupTreeApi = () => {
  return request.get('/system/tree-node/tree', {
    params: { type: 'hierarchy' }
  })
}

export const createHierarchyGroupApi = (data: any) => {
  return request.post('/system/tree-node', {
    ...data,
    type: 'hierarchy'
  })
}

export const updateHierarchyGroupApi = (data: any) => {
  return request.put(`/system/tree-node/${data.id}`, {
    ...data,
    type: 'hierarchy'
  })
}

export const deleteHierarchyGroupApi = (id: number) => {
  return request.delete(`/system/tree-node/${id}`, {
    params: { type: 'hierarchy' }
  })
}

// 分类API
export const getCategoryTreeApi = () => {
  return request.get('/system/tree-node/tree', {
    params: { type: 'category' }
  })
}

export const createCategoryApi = (data: any) => {
  return request.post('/system/tree-node', {
    ...data,
    type: 'category'
  })
}

export const updateCategoryApi = (data: any) => {
  return request.put(`/system/tree-node/${data.id}`, {
    ...data,
    type: 'category'
  })
}

export const deleteCategoryApi = (id: number) => {
  return request.delete(`/system/tree-node/${id}`, {
    params: { type: 'category' }
  })
}
```

## 性能优化建议

### 1. **索引设计**
```sql
-- 复合索引，优化按类型和父级查询
CREATE INDEX idx_type_parent ON sys_tree_node(type, parent_id);

-- 复合索引，优化按类型和排序查询
CREATE INDEX idx_type_sort ON sys_tree_node(type, sort);

-- 复合索引，优化按类型和路径查询
CREATE INDEX idx_type_path ON sys_tree_node(type, path);
```

### 2. **查询优化**
```java
// 使用缓存优化频繁查询
@Cacheable(value = "treeNode", key = "#type")
public List<TreeNode> getTreeByType(String type) {
    // 查询逻辑
}
```

### 3. **分页查询**
```java
// 对于大数据量，支持分页查询
public PageResult<TreeNode> getTreeNodePage(String type, TreeNodePageReq req) {
    Page<TreeNode> page = new Page<>(req.getPageNo(), req.getPageSize());
    
    LambdaQueryWrapper<TreeNode> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(TreeNode::getType, type)
           .eq(TreeNode::getDeleted, 0)
           .like(StringUtils.isNotBlank(req.getName()), TreeNode::getName, req.getName())
           .orderByAsc(TreeNode::getSort);
    
    return baseMapper.selectPage(page, wrapper);
}
```

## 总结

**推荐使用统一表设计**，理由如下：

1. **架构一致性**：与前端通用框架的设计理念完全一致
2. **开发效率**：只需要维护一套树形管理逻辑
3. **维护成本**：统一的代码库，降低维护成本
4. **扩展性强**：新增业务类型只需添加记录
5. **性能可控**：通过合理的索引和缓存策略保证性能

这种设计既保持了技术实现的统一性，又通过 `type` 字段实现了业务语义的区分，完美契合了我们的通用框架设计理念！ 