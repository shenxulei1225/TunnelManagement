# 时间范围查询模式标准

## 问题描述

在查询 VO 中，经常需要按时间范围进行查询。传统的做法是使用 `LocalDateTime[]` 数组，但这种方式存在以下问题：

1. **语义不清晰**：数组中的两个元素分别代表什么不够明确
2. **前端使用复杂**：前端需要构造数组格式
3. **验证困难**：难以验证开始时间是否小于结束时间
4. **文档不友好**：API 文档中无法明确说明字段含义

## 推荐方案

### 1. 使用独立的时间字段

```java
@Schema(description = "创建开始时间")
@DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
private LocalDateTime createTimeBegin;

@Schema(description = "创建结束时间")
@DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
private LocalDateTime createTimeEnd;
```

### 2. 优势

1. **语义清晰**：字段名明确表示开始和结束时间
2. **前端友好**：前端可以直接使用两个独立的时间字段
3. **验证简单**：可以轻松验证 `createTimeBegin <= createTimeEnd`
4. **文档友好**：API 文档中字段含义明确
5. **类型安全**：避免了数组越界等问题

### 3. Mapper 查询实现

```java
default PageResult<EntityDO> selectEntityPage(EntityPageReqVO pageReqVO) {
    return selectPage(pageReqVO, new LambdaQueryWrapperX<EntityDO>()
            // 其他查询条件...
            .betweenIfPresent(EntityDO::getCreateTime, 
                    pageReqVO.getCreateTimeBegin(), 
                    pageReqVO.getCreateTimeEnd())
            .orderByDesc(EntityDO::getId));
}
```

## 迁移指南

### 步骤1：更新 VO 类

**旧模式：**
```java
@Schema(description = "创建时间")
@DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
private LocalDateTime[] createTime;
```

**新模式：**
```java
@Schema(description = "创建开始时间")
@DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
private LocalDateTime createTimeBegin;

@Schema(description = "创建结束时间")
@DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
private LocalDateTime createTimeEnd;
```

### 步骤2：更新 Mapper 查询

**旧模式：**
```java
.betweenIfPresent(EntityDO::getCreateTime, pageReqVO.getCreateTime())
```

**新模式：**
```java
.betweenIfPresent(EntityDO::getCreateTime, 
        pageReqVO.getCreateTimeBegin(), 
        pageReqVO.getCreateTimeEnd())
```

### 步骤3：更新前端调用

**旧模式：**
```javascript
// 前端需要构造数组
const params = {
  createTime: [startTime, endTime]
}
```

**新模式：**
```javascript
// 前端直接使用两个字段
const params = {
  createTimeBegin: startTime,
  createTimeEnd: endTime
}
```

## 验证规则

建议在 VO 类中添加验证规则：

```java
@Schema(description = "创建开始时间")
@DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
private LocalDateTime createTimeBegin;

@Schema(description = "创建结束时间")
@DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
@AssertTrue(message = "结束时间不能早于开始时间")
private LocalDateTime createTimeEnd;

// 自定义验证方法
@AssertTrue(message = "结束时间不能早于开始时间")
public boolean isTimeRangeValid() {
    if (createTimeBegin == null || createTimeEnd == null) {
        return true; // 允许空值
    }
    return !createTimeEnd.isBefore(createTimeBegin);
}
```

## 常见时间字段命名

| 字段类型 | 开始时间字段名 | 结束时间字段名 |
|---------|---------------|---------------|
| 创建时间 | createTimeBegin | createTimeEnd |
| 更新时间 | updateTimeBegin | updateTimeEnd |
| 发送时间 | sendTimeBegin | sendTimeEnd |
| 接收时间 | receiveTimeBegin | receiveTimeEnd |
| 购买时间 | purchaseTimeBegin | purchaseTimeEnd |
| 安装时间 | installTimeBegin | installTimeEnd |
| 保修到期 | warrantyExpireTimeBegin | warrantyExpireTimeEnd |

## 注意事项

1. **向后兼容**：如果已有 API 在使用数组模式，建议在新版本中逐步迁移
2. **前端适配**：需要同步更新前端代码
3. **文档更新**：及时更新 API 文档
4. **测试覆盖**：添加相应的单元测试和集成测试

## 相关文件

需要迁移的文件列表：
- `FieldPageReqVO.java` - 字段分页查询
- `FieldExportReqVO.java` - 字段导出查询
- 其他使用 `LocalDateTime[]` 的 VO 类 