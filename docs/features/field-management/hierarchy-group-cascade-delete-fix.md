# 层级组删除级联删除修复

## 问题描述

用户反馈：删除分组后，没有看到关联的字段从关系表中删除。

## 问题分析

在删除层级组（hierarchy group）时，后端只删除了以下内容：
1. `system_hierarchy_group` 表中的分组记录
2. `system_hierarchy_group_relation` 表中的分组关系记录

但是**没有删除**`system_field_hierarchy_rel` 表中的字段与该分组的关联关系，导致：
- 数据库中存在孤儿记录
- 前端可能显示已删除分组的字段关联
- 数据一致性问题

## 修复方案

### 1. 后端修复

修改 `HierarchyGroupServiceImpl.deleteHierarchyGroup` 方法，添加删除字段层级关系的逻辑：

```java
@Override
public void deleteHierarchyGroup(Long id) {
    // 校验存在
    validateHierarchyGroupExists(id);
    // 删除层级组
    hierarchyGroupMapper.deleteById(id);
    // 删除层级组关联关系
    hierarchyGroupRelationMapper.deleteByHierarchyGroupId(id);
    // 删除字段层级关系 (新增)
    fieldHierarchyRelService.deleteFieldHierarchyRelsByGroupId(id);
}
```

### 2. 依赖注入

添加 `FieldHierarchyRelService` 的依赖注入：

```java
@Resource
private FieldHierarchyRelService fieldHierarchyRelService;
```

### 3. 导入包

添加正确的导入：

```java
import com.cheers.arch.module.system.service.field.FieldHierarchyRelService;
```

## 影响的表

### 删除操作涉及的表：

1. **`system_hierarchy_group`** - 层级组主表
2. **`system_hierarchy_group_relation`** - 层级组关系表
3. **`system_field_hierarchy_rel`** - 字段层级关系表 ⚠️ **此表之前未被正确清理**

### 数据一致性

修复后，删除层级组将确保：
- 所有相关的字段关联关系被同步删除
- 不会产生孤儿记录
- 前端界面能正确反映删除结果

## 测试验证

### 1. 数据库验证

使用测试脚本 `sql/mysql/test_hierarchy_group_cascade_delete.sql` 验证：

```sql
-- 查看删除前的字段关联
SELECT field_id, hierarchy_group_id 
FROM system_field_hierarchy_rel 
WHERE hierarchy_group_id = {要删除的分组ID};

-- 删除分组后验证
SELECT COUNT(*) as remaining_relations
FROM system_field_hierarchy_rel 
WHERE hierarchy_group_id = {已删除的分组ID};
-- 应该返回 0
```

### 2. 前端验证

1. 选择一个有字段关联的分组
2. 记录该分组关联的字段
3. 删除该分组
4. 刷新字段管理页面
5. 确认之前关联的字段不再显示该分组的关联关系

### 3. API验证

调用删除API后检查：

```bash
# 删除分组
DELETE /system/hierarchy-group/delete?id={分组ID}

# 验证字段的分组关联已被清除
GET /system/field/hierarchy-rels?fieldIds={字段ID列表}
```

## 相关文件

### 修改的文件：
- `cheers-module-system/src/main/java/com/cheers/arch/module/system/service/hierarchy/impl/HierarchyGroupServiceImpl.java`

### 新增的文件：
- `sql/mysql/test_hierarchy_group_cascade_delete.sql`
- `docs/features/field-management/hierarchy-group-cascade-delete-fix.md`

### 涉及的服务：
- `HierarchyGroupService` - 层级组管理服务
- `FieldHierarchyRelService` - 字段层级关系服务

## 注意事项

1. **数据备份**：在生产环境应用此修复前，建议备份相关数据表
2. **已有孤儿数据**：对于修复前产生的孤儿记录，可能需要手动清理
3. **事务一致性**：删除操作在同一事务中执行，确保数据一致性

## 清理已有孤儿数据

如果需要清理修复前产生的孤儿记录：

```sql
-- 查找孤儿记录
SELECT fhr.* 
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE hg.id IS NULL;

-- 删除孤儿记录（谨慎操作）
DELETE fhr 
FROM system_field_hierarchy_rel fhr
LEFT JOIN system_hierarchy_group hg ON fhr.hierarchy_group_id = hg.id
WHERE hg.id IS NULL;
``` 