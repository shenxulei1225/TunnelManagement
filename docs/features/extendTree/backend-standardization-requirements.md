# 后端ExtendTree标准化调整需求

## 📋 现状分析

### ✅ 已符合标准的模块

#### 1. **HierarchyGroupVO** - 完全符合标准
```java
// HierarchyGroupBaseVO.java
public class HierarchyGroupBaseVO {
    private String name;        // ✅ 必需字段
    private String code;        // ✅ 可选字段
    private Long parentId;      // ✅ 必需字段
    private Integer sort;       // ✅ 必需字段
    private String color;       // ✅ 可选字段
    private String icon;        // ✅ 可选字段
    private String description; // ✅ 可选字段
    private Integer status;     // ✅ 必需字段 - @NotNull
}
```

#### 2. **RegionVO** - 基本符合标准
```java
// RegionRespVO.java 
public class RegionRespVO {
    private Long id;           // ✅ 必需字段
    private String name;       // ✅ 必需字段
    private Long parentId;     // ✅ 必需字段
    private Integer sort;      // ✅ 必需字段
    private Integer status;    // ✅ 必需字段
    // 缺少: code, color, icon, description (可选字段，影响较小)
}
```

### ❌ 需要调整的模块

#### 1. **FieldCategoryVO** - 缺少关键字段
**问题**: 字段分类相关VO主要是关系型的，没有独立的分类树结构
**当前状态**: 
- 只有`FieldCategoryRelVO` - 管理字段与分类的关系
- 缺少`FieldCategoryVO` - 管理分类本身的树形结构

#### 2. **DeviceVO** - 缺少树形字段
```java
// DeviceRespVO.java 
public class DeviceRespVO {
    private Long id;           // ✅ 必需字段
    private String name;       // ✅ 必需字段
    private Long parentId;     // ✅ 必需字段
    private Integer sort;      // ✅ 必需字段
    private Integer status;    // ✅ 必需字段
    // 缺少: code, color, icon, description (如果需要树形显示)
}
```

## 🎯 需要调整的具体内容

### 1. **创建FieldCategoryVO (新增)**

当前项目中缺少字段分类的树形结构管理，需要新增：

```java
// 新增: FieldCategoryBaseVO.java
@Data
public class FieldCategoryBaseVO {
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类名称不能为空")
    private String name;

    @Schema(description = "分类编码")
    private String code;

    @Schema(description = "父分类ID", example = "1")
    private Long parentId;

    @Schema(description = "排序号", example = "1")
    private Integer sort;

    @Schema(description = "分类颜色", example = "#409EFF")
    private String color;

    @Schema(description = "分类图标", example = "Folder")
    private String icon;

    @Schema(description = "分类描述")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private Integer status;
}

// 新增: FieldCategoryRespVO.java
@Data
@EqualsAndHashCode(callSuper = true)
public class FieldCategoryRespVO extends FieldCategoryBaseVO {
    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "层级", example = "1")
    private Integer level;

    @Schema(description = "分类路径", example = "/1/2")
    private String treePath;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "子分类列表")
    private List<FieldCategoryRespVO> children;

    @Schema(description = "关联字段数量", example = "10")
    private Long count;
}

// 新增: FieldCategoryCreateReqVO.java、FieldCategoryUpdateReqVO.java等
```

### 2. **创建FieldCategoryController (新增)**

需要新增字段分类的树形管理控制器：

```java
// 新增: FieldCategoryController.java
@RestController
@RequestMapping("/system/field-category-tree")
@RequiredArgsConstructor
@Validated
public class FieldCategoryController {
    
    private final FieldCategoryService fieldCategoryService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:field-category:create')")
    public CommonResult<Long> createFieldCategory(@Valid @RequestBody FieldCategoryCreateReqVO createReqVO) {
        return success(fieldCategoryService.createFieldCategory(createReqVO));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:field-category:update')")
    public CommonResult<Boolean> updateFieldCategory(@Valid @RequestBody FieldCategoryUpdateReqVO updateReqVO) {
        fieldCategoryService.updateFieldCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:field-category:delete')")
    public CommonResult<Boolean> deleteFieldCategory(@RequestParam("id") Long id) {
        fieldCategoryService.deleteFieldCategory(id);
        return success(true);
    }

    @GetMapping("/tree")
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<List<FieldCategoryRespVO>> getFieldCategoryTree() {
        return success(fieldCategoryService.getFieldCategoryTree());
    }
}
```

### 3. **扩展DeviceVO (可选)**

如果设备需要支持ExtendTree的完整功能：

```java
// 扩展: DeviceBaseVO.java
@Data
public class DeviceBaseVO {
    // 现有字段保持不变
    private String name;
    private String deviceCode;
    private Long parentId;
    private Integer sort;
    private Integer status;
    
    // 新增支持ExtendTree的字段
    @Schema(description = "设备颜色", example = "#409EFF")
    private String color;

    @Schema(description = "设备图标", example = "Device")
    private String icon;

    @Schema(description = "设备描述")
    private String description;
}
```

## 🔧 数据库调整

### 1. **新增字段分类表**

```sql
-- 新增字段分类表
CREATE TABLE system_field_category (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    code VARCHAR(50) COMMENT '分类编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    sort INT DEFAULT 0 COMMENT '排序号',
    color VARCHAR(20) DEFAULT '#409EFF' COMMENT '分类颜色',
    icon VARCHAR(50) DEFAULT 'Folder' COMMENT '分类图标',
    description VARCHAR(500) COMMENT '分类描述',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态(0-启用 1-禁用)',
    level INT DEFAULT 1 COMMENT '层级',
    tree_path VARCHAR(500) COMMENT '分类路径',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字段分类表';

-- 添加索引
CREATE INDEX idx_field_category_parent_id ON system_field_category(parent_id);
CREATE INDEX idx_field_category_status ON system_field_category(status);
CREATE UNIQUE INDEX uk_field_category_code ON system_field_category(code) WHERE deleted = 0;
```

### 2. **扩展现有表（可选）**

```sql
-- 为设备表添加ExtendTree支持字段（如果需要）
ALTER TABLE system_device 
ADD COLUMN color VARCHAR(20) DEFAULT '#409EFF' COMMENT '设备颜色',
ADD COLUMN icon VARCHAR(50) DEFAULT 'Device' COMMENT '设备图标',
ADD COLUMN description VARCHAR(500) COMMENT '设备描述';

-- 为区域表添加缺失的ExtendTree字段（如果需要）
ALTER TABLE system_region 
ADD COLUMN code VARCHAR(50) COMMENT '区域编码',
ADD COLUMN color VARCHAR(20) DEFAULT '#409EFF' COMMENT '区域颜色',
ADD COLUMN icon VARCHAR(50) DEFAULT 'Location' COMMENT '区域图标',
ADD COLUMN description VARCHAR(500) COMMENT '区域描述';
```

## 📋 优先级和实施建议

### 🚨 高优先级（立即需要）
1. **字段分类树形管理**
   - 创建`FieldCategoryVO`系列类
   - 创建`FieldCategoryService`和`FieldCategoryController`
   - 创建`system_field_category`表
   - **原因**: 当前字段分类只有关系管理，缺少分类本身的树形管理

### 🔶 中优先级（计划中）
2. **现有VO标准化验证**
   - 为缺少`@NotNull(message = "状态不能为空")`的VO添加验证
   - 确保所有树形VO都有完整的必需字段验证

### 🔷 低优先级（可选）
3. **扩展字段支持**
   - 为`DeviceVO`、`RegionVO`等添加`color`、`icon`、`description`字段
   - **原因**: 这些字段不是必需的，但可以增强UI显示效果

## 🎯 与前端的协调

### 前端适配器支持
前端已经准备了数据适配器来处理不同的后端数据结构：

```typescript
// 前端已支持
TreeDataAdapters.fieldCategory = (data: any): ExtendedTreeNode => {
  return {
    id: data.id,
    name: data.name,
    parentId: data.parentId || 0,
    sort: data.sort || 0,
    status: data.status ?? 0, // 自动设置默认状态
    code: data.code,
    // 其他字段...
  }
}
```

### API路径约定
- 层级组: `/system/hierarchy-group/*` ✅ 已实现
- 字段分类: `/system/field-category-tree/*` 🚧 需要新增
- 设备管理: `/system/device/*` ✅ 已实现
- 区域管理: `/system/region/*` ✅ 已实现

## 📋 总结

**立即需要的调整**:
1. 创建字段分类的树形管理功能（高优先级）
2. 补充缺失的字段验证注解（中优先级）

**可选的调整**:
1. 为现有模块添加ExtendTree扩展字段（低优先级）

**无需调整**:
1. `HierarchyGroupVO` - 已完全符合标准 ✅
2. `RegionVO`、`DeviceVO` - 基本符合标准，可通过适配器处理 ✅

这样的调整方案可以确保ExtendTree组件在所有模块中都能正常工作，同时保持向后兼容性。 