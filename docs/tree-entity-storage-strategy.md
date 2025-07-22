# TreeEntity框架存储策略分析

## 🌳 存储策略对比

### 策略一：业务独立建表 (当前主要模式)

#### 适用场景
- **成熟业务**: 如部门、菜单、区域管理
- **复杂业务逻辑**: 需要大量业务特有字段
- **高性能要求**: 需要针对性优化
- **数据安全**: 强隔离需求

#### 实施例子
```java
// 部门管理 - 独立表
@TableName("system_dept")
public class DeptDO extends TenantBaseDO implements TreeEntity<Long> {
    private Long id;
    private String name;
    private Long parentId;
    private String treePath;
    private Integer level;
    // 部门特有字段
    private Long leaderUserId;  // 负责人
    private String phone;       // 联系电话
    private String email;       // 邮箱
}

// 菜单管理 - 独立表  
@TableName("system_menu")
public class MenuDO extends BaseDO implements TreeEntity<Long> {
    private Long id;
    private String name;
    private Long parentId;
    private String treePath;
    private Integer level;
    // 菜单特有字段
    private String permission;   // 权限标识
    private String component;    // 组件路径
    private String path;         // 路由地址
    private Boolean visible;     // 是否可见
}
```

#### 优势
- ✅ **性能优秀**: 专表专用，可针对性优化
- ✅ **业务隔离**: 完全独立，互不影响
- ✅ **扩展自由**: 可添加任意业务字段
- ✅ **迁移平滑**: 现有业务改动最小

#### 劣势
- ❌ **表数量多**: 每个树形业务需要独立表
- ❌ **重复代码**: 基础树逻辑重复实现

### 策略二：通用树表 (框架推荐)

#### 适用场景
- **简单分类**: 如商品分类、文档分类
- **标准树结构**: 字段需求相似
- **快速开发**: 新业务快速上线
- **统一管理**: 希望集中管理所有树结构

#### 实施例子
```java
// 通用树节点
@TableName("system_tree_node")  
public class TreeNodeDO extends TenantBaseDO implements TreeEntity<Long> {
    private Long id;
    private String treeType;        // 业务类型标识
    private String name;
    private String code;
    private Long parentId;
    private String treePath;
    private Integer level;
    private Integer sort;
    private String icon;
    private String color;
    private Map<String, Object> extraAttrs;  // JSON扩展字段
}

// 业务使用
TreeNodeDO categoryNode = new TreeNodeDO()
    .setTreeType("category")
    .setName("电子产品")
    .setExtraAttrs(Map.of(
        "displayType", "grid",
        "imageUrl", "https://...",
        "priority", 1
    ));
```

#### 数据示例
```sql
-- system_tree_node 表数据
INSERT INTO system_tree_node VALUES 
(1, 'category', '电子产品', 'electronics', 0, '/', 1, 1, ...),
(2, 'category', '手机', 'phone', 1, '/1/', 2, 1, ...),
(3, 'directory', '技术文档', 'tech-docs', 0, '/', 1, 1, ...),
(4, 'hierarchy', '研发部门', 'rd-dept', 0, '/', 1, 1, ...);
```

#### 优势
- ✅ **表结构统一**: 一个表管理所有树形业务
- ✅ **开发效率**: 新业务无需建表，直接使用
- ✅ **代码复用**: 通用TreeService处理所有逻辑
- ✅ **维护简单**: 只需维护一套表结构

#### 劣势
- ❌ **性能风险**: 数据量大时需要分区优化
- ❌ **扩展限制**: 复杂业务字段需要JSON存储
- ❌ **业务耦合**: 不同业务共享表结构

### 策略三：混合模式 (实际推荐)

#### 分类原则
```
核心业务 → 独立建表
├── system_menu      (菜单管理)
├── system_dept      (部门管理)  
├── system_region    (区域管理)
└── system_device    (设备管理)

通用分类 → 统一建表
├── category         (商品分类)
├── directory        (文档目录)
├── hierarchy        (业务分组)
└── tag              (标签分类)
```

#### 判断标准
| 维度 | 独立建表 | 通用建表 |
|------|----------|----------|
| **业务重要性** | 核心业务 | 辅助功能 |
| **字段复杂度** | >10个特有字段 | <5个特有字段 |
| **数据规模** | 10万+节点 | <5万节点 |
| **性能要求** | 高并发访问 | 一般访问 |
| **开发周期** | 长期维护 | 快速上线 |

## 🎯 具体实施建议

### 当前项目状态
已完成CategoryDO迁移，使用**独立表**模式:
```java
@TableName("system_category")
public class CategoryDO extends TenantBaseDO implements TreeEntity<Long> {
    // 已迁移完成，保持独立表
}
```

### 后续迁移规划

#### 阶段一：核心业务保持独立表
```bash
# 继续独立表模式迁移
1. DeptDO      - 部门管理 (复杂权限逻辑)
2. MenuDO      - 菜单管理 (路由组件字段)  
3. RegionDO    - 区域管理 (已迁移，保持现状)
4. DeviceDO    - 设备管理 (设备特有属性多)
```

#### 阶段二：简单分类使用通用表
```bash
# 新业务直接使用system_tree_node
1. HierarchyGroupDO  → TreeNodeDO (treeType='hierarchy')
2. DirectoryDO       → TreeNodeDO (treeType='directory')
3. 新的商品分类       → TreeNodeDO (treeType='product_category')
4. 新的标签分类       → TreeNodeDO (treeType='tag_category')
```

### 迁移示例

#### HierarchyGroupDO → TreeNodeDO
```java
// 原来的独立表
@TableName("system_hierarchy_group")
public class HierarchyGroupDO extends TenantBaseDO {
    private Long id;
    private String name;
    private String code;
    private Long parentId;
    private String groupType;    // 特有字段
    private String usageType;    // 特有字段
}

// 迁移到通用表
TreeNodeDO hierarchyNode = new TreeNodeDO()
    .setTreeType("hierarchy")
    .setName("设备分组")
    .setCode("device_group")
    .setExtraAttrs(Map.of(
        "groupType", "EQUIPMENT",
        "usageType", "BUSINESS"
    ));
```

## 💡 最佳实践

### 1. 设计原则
- **先业务后技术**: 根据业务特点选择存储策略
- **先简单后复杂**: 新业务可先用通用表，复杂后再拆分
- **平滑迁移**: 保持API兼容，逐步迁移

### 2. 性能优化
```sql
-- 通用表分区策略
ALTER TABLE system_tree_node PARTITION BY HASH(tree_type) PARTITIONS 8;

-- 针对性索引
CREATE INDEX idx_tree_type_parent ON system_tree_node(tree_type, parent_id);
CREATE INDEX idx_tree_type_code ON system_tree_node(tree_type, code);
```

### 3. 代码复用
```java
// 通用TreeService适配不同存储
@Service
public class UnifiedTreeService {
    
    // 独立表业务
    public List<CategoryDO> getCategoryTree() {
        return categoryService.getCategoryTree();
    }
    
    // 通用表业务  
    public List<TreeNodeDO> getHierarchyTree() {
        return treeNodeService.getTreeByType("hierarchy");
    }
}
```

## 📊 性能对比

### 数据规模测试 (模拟)
| 存储策略 | 1万节点 | 10万节点 | 100万节点 |
|----------|---------|-----------|-----------|
| **独立表** | 50ms | 200ms | 1.2s |
| **通用表** | 80ms | 350ms | 2.8s |
| **分区通用表** | 60ms | 250ms | 1.5s |

### 开发效率对比
| 功能 | 独立表 | 通用表 |
|------|--------|--------|
| **新建树业务** | 2天 | 2小时 |
| **添加字段** | 30分钟 | 5分钟 |
| **性能优化** | 容易 | 需要考虑全局 |

## 🚀 推荐方案

### 针对您的项目
建议采用**混合模式**：

1. **保持现有独立表**
   - CategoryDO (已迁移)
   - DeptDO, MenuDO, RegionDO, DeviceDO

2. **新业务使用通用表**  
   - 简单分类业务直接用system_tree_node
   - 复杂业务再考虑独立表

3. **渐进式优化**
   - 监控通用表性能
   - 必要时拆分热点业务到独立表

这样既保持了现有业务的稳定性，又为新业务提供了快速开发能力！ 