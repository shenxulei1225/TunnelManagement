# selectBatchIds 弃用修复指南

## 问题描述

MyBatis Plus 中的 `selectBatchIds` 方法已经被弃用，会产生编译警告：

```
The method selectBatchIds(Collection<? extends Serializable>) from the type BaseMapper<T> is deprecated
```

## 解决方案

### 1. 推荐方案：在 Mapper 中添加专用方法

在每个 Mapper 接口中添加一个专用的批量查询方法：

```java
@Mapper
public interface FieldMapper extends BaseMapperX<FieldDO> {
    
    /**
     * 根据ID列表批量查询
     *
     * @param ids ID列表
     * @return 实体列表
     */
    default List<FieldDO> selectFieldListByIds(java.util.Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<FieldDO>()
                .in(FieldDO::getId, ids));
    }
}
```

### 2. Service 层调用

在 Service 实现中使用新的方法：

```java
@Override
public List<FieldDO> getFieldList(List<Long> ids) {
    return fieldMapper.selectFieldListByIds(ids);
}
```

### 3. 通用模板

对于所有需要批量查询的 Mapper，可以使用以下模板：

```java
/**
 * 根据ID列表批量查询
 *
 * @param ids ID列表
 * @return 实体列表
 */
default List<EntityDO> selectEntityListByIds(java.util.Collection<Long> ids) {
    if (ids == null || ids.isEmpty()) {
        return java.util.Collections.emptyList();
    }
    return selectList(new LambdaQueryWrapperX<EntityDO>()
            .in(EntityDO::getId, ids));
}
```

## 优势

1. **消除警告**：不再使用弃用的方法
2. **性能优化**：使用 `in` 查询，性能更好
3. **代码清晰**：方法名更明确，意图更清楚
4. **空值处理**：统一处理空集合的情况
5. **类型安全**：使用泛型，类型更安全

## 迁移步骤

### 步骤1：在 Mapper 中添加方法

```java
// 在 FieldMapper.java 中添加
default List<FieldDO> selectFieldListByIds(java.util.Collection<Long> ids) {
    if (ids == null || ids.isEmpty()) {
        return java.util.Collections.emptyList();
    }
    return selectList(new LambdaQueryWrapperX<FieldDO>()
            .in(FieldDO::getId, ids));
}
```

### 步骤2：更新 Service 实现

```java
// 在 FieldServiceImpl.java 中替换
@Override
public List<FieldDO> getFieldList(List<Long> ids) {
    return fieldMapper.selectFieldListByIds(ids);
}
```

### 步骤3：验证功能

确保批量查询功能正常工作，包括：
- 空列表处理
- 单个ID查询
- 多个ID查询
- 不存在的ID处理

## 注意事项

1. **空值处理**：确保正确处理 `null` 和空集合
2. **性能考虑**：对于大量ID的查询，考虑分批处理
3. **事务一致性**：确保在事务中正确处理批量查询
4. **测试覆盖**：添加相应的单元测试

## 批量处理优化

对于大量ID的查询，可以考虑分批处理：

```java
/**
 * 分批批量查询
 */
default List<FieldDO> selectFieldListByIdsBatch(java.util.Collection<Long> ids, int batchSize) {
    if (ids == null || ids.isEmpty()) {
        return java.util.Collections.emptyList();
    }
    
    List<FieldDO> result = new java.util.ArrayList<>();
    java.util.List<Long> idList = new java.util.ArrayList<>(ids);
    
    for (int i = 0; i < idList.size(); i += batchSize) {
        int end = Math.min(i + batchSize, idList.size());
        java.util.List<Long> batch = idList.subList(i, end);
        result.addAll(selectList(new LambdaQueryWrapperX<FieldDO>()
                .in(FieldDO::getId, batch)));
    }
    
    return result;
}
```

## 相关文件

需要修复的文件列表：
- `cheers-module-system/src/main/java/com/cheers/arch/module/system/service/field/impl/FieldServiceImpl.java`
- `cheers-module-system/src/main/java/com/cheers/arch/module/system/dal/mysql/field/FieldMapper.java`
- 其他使用 `selectBatchIds` 的 Service 和 Mapper 文件 