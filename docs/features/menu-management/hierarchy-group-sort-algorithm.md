# Hierarchy Group 重排序算法

## 概述

当节点拖拽后，需要对同级节点进行重排序，确保排序字段的连续性和正确性。

## 数据库字段要求

### 必需字段
```sql
CREATE TABLE hierarchy_group (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  parent_id BIGINT DEFAULT 0,    -- 父级ID，0表示顶级节点
  sort INT DEFAULT 0,            -- 排序字段，同级内唯一
  level INT DEFAULT 0,           -- 层级深度
  path VARCHAR(500),             -- 路径，如 "0.1.2"
  -- 其他字段...
);
```

## 重排序算法

### 1. 拖拽到前面 (prev)

```java
// 伪代码
public void moveToPrev(Long draggedId, Long targetId) {
    // 1. 获取目标节点的同级信息
    HierarchyGroup target = getById(targetId);
    List<HierarchyGroup> siblings = getSiblingsByParentId(target.getParentId());
    
    // 2. 计算新的排序值
    int newSort;
    if (target.getSort() == 0) {
        // 目标节点是第一个，插入到最前面
        newSort = -10;
    } else {
        // 插入到目标节点之前
        HierarchyGroup prevSibling = getPreviousSibling(siblings, target);
        newSort = prevSibling.getSort() + (target.getSort() - prevSibling.getSort()) / 2;
    }
    
    // 3. 更新被拖拽节点
    updateNode(draggedId, target.getParentId(), newSort);
    
    // 4. 重新排序同级节点
    reorderSiblings(target.getParentId());
}
```

### 2. 拖拽到后面 (next)

```java
// 伪代码
public void moveToNext(Long draggedId, Long targetId) {
    // 1. 获取目标节点的同级信息
    HierarchyGroup target = getById(targetId);
    List<HierarchyGroup> siblings = getSiblingsByParentId(target.getParentId());
    
    // 2. 计算新的排序值
    int newSort;
    HierarchyGroup nextSibling = getNextSibling(siblings, target);
    if (nextSibling == null) {
        // 目标节点是最后一个，插入到最后面
        newSort = target.getSort() + 10;
    } else {
        // 插入到目标节点之后
        newSort = target.getSort() + (nextSibling.getSort() - target.getSort()) / 2;
    }
    
    // 3. 更新被拖拽节点
    updateNode(draggedId, target.getParentId(), newSort);
    
    // 4. 重新排序同级节点
    reorderSiblings(target.getParentId());
}
```

### 3. 拖拽到内部 (inner)

```java
// 伪代码
public void moveToInner(Long draggedId, Long targetId) {
    // 1. 获取目标节点的子节点
    List<HierarchyGroup> children = getChildrenByParentId(targetId);
    
    // 2. 计算新的排序值
    int newSort = children.isEmpty() ? 0 : children.get(children.size() - 1).getSort() + 10;
    
    // 3. 更新被拖拽节点
    updateNode(draggedId, targetId, newSort);
}
```

## 重新排序算法

### 方法一：重置排序值

```java
public void reorderSiblings(Long parentId) {
    List<HierarchyGroup> siblings = getSiblingsByParentId(parentId);
    
    // 按当前排序值排序
    siblings.sort(Comparator.comparing(HierarchyGroup::getSort));
    
    // 重新分配排序值，间隔为10
    for (int i = 0; i < siblings.size(); i++) {
        HierarchyGroup sibling = siblings.get(i);
        sibling.setSort(i * 10);
        updateById(sibling);
    }
}
```

### 方法二：批量更新

```java
public void reorderSiblingsBatch(Long parentId) {
    List<HierarchyGroup> siblings = getSiblingsByParentId(parentId);
    
    // 按当前排序值排序
    siblings.sort(Comparator.comparing(HierarchyGroup::getSort));
    
    // 批量更新
    List<HierarchyGroup> updates = new ArrayList<>();
    for (int i = 0; i < siblings.size(); i++) {
        HierarchyGroup sibling = siblings.get(i);
        sibling.setSort(i * 10);
        updates.add(sibling);
    }
    
    batchUpdate(updates);
}
```

## API 接口设计

### 1. 拖拽更新接口

```java
@PutMapping("/drag-update")
public CommonResult<Boolean> dragUpdate(@RequestBody DragUpdateReqVO reqVO) {
    hierarchyGroupService.dragUpdate(reqVO);
    return success(true);
}

public class DragUpdateReqVO {
    private Long draggedId;      // 被拖拽节点ID
    private Long dropId;         // 目标节点ID
    private String dropType;     // 拖拽类型：prev, next, inner
}
```

### 2. 批量排序接口

```java
@PutMapping("/reorder-siblings")
public CommonResult<Boolean> reorderSiblings(@RequestParam Long parentId) {
    hierarchyGroupService.reorderSiblings(parentId);
    return success(true);
}
```

## 前端调用方式

### 1. 拖拽时调用

```typescript
// 拖拽更新
const dragUpdate = async (draggedId: number, dropId: number, dropType: string) => {
  await request.put({
    url: '/system/hierarchy-group/drag-update',
    data: { draggedId, dropId, dropType }
  })
}
```

### 2. 需要时重新排序

```typescript
// 重新排序同级节点
const reorderSiblings = async (parentId: number) => {
  await request.put({
    url: '/system/hierarchy-group/reorder-siblings',
    params: { parentId }
  })
}
```

## 性能优化建议

### 1. 索引优化

```sql
-- 为排序查询添加索引
CREATE INDEX idx_hierarchy_group_parent_sort ON hierarchy_group(parent_id, sort);
CREATE INDEX idx_hierarchy_group_parent_id ON hierarchy_group(parent_id);
```

### 2. 批量操作

```java
// 使用批量更新减少数据库操作
@Transactional
public void batchUpdateSort(List<HierarchyGroup> groups) {
    if (groups.isEmpty()) return;
    
    // 使用 JDBC 批量更新
    jdbcTemplate.batchUpdate(
        "UPDATE hierarchy_group SET sort = ?, parent_id = ? WHERE id = ?",
        groups,
        100,
        (ps, group) -> {
            ps.setInt(1, group.getSort());
            ps.setLong(2, group.getParentId());
            ps.setLong(3, group.getId());
        }
    );
}
```

### 3. 缓存策略

```java
// 缓存同级节点列表
@Cacheable(value = "hierarchy_siblings", key = "#parentId")
public List<HierarchyGroup> getSiblingsByParentId(Long parentId) {
    return hierarchyGroupMapper.selectByParentId(parentId);
}
```

## 总结

要实现拖拽持久化，后端需要：

1. **parent_id 字段**：确定同级关系
2. **sort 字段**：同级内排序
3. **重排序算法**：确保排序值连续
4. **批量更新接口**：提高性能
5. **适当的索引**：优化查询性能

这样就能实现完整的拖拽持久化功能。 