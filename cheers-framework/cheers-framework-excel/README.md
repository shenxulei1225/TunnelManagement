# Excel模块

## 功能介绍

Excel模块基于EasyExcel实现,提供了丰富的Excel导入导出功能,支持自定义样式、数据校验、格式转换等特性。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-excel</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 使用示例

#### 2.1 导出Excel

```java
@Data
public class UserExcel {
    
    @ExcelProperty("用户名")
    private String username;
    
    @ExcelProperty("部门")
    @DictFormat(dictType = "sys_dept")
    private String deptName;
    
    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @ExcelProperty("状态")
    @ExcelColumnSelect({"正常", "禁用"})
    private Integer status;
}

// 导出Excel
@GetMapping("/export")
public void export(HttpServletResponse response) {
    // 准备数据
    List<UserExcel> list = userService.queryExportList();
    
    // 导出
    ExcelUtils.write(response, "用户列表", UserExcel.class, list);
}
```

#### 2.2 导入Excel

```java
@PostMapping("/import")
public String importExcel(@RequestParam("file") MultipartFile file) throws IOException {
    // 读取Excel
    List<UserExcel> list = ExcelUtils.read(file, UserExcel.class);
    
    // 处理数据
    userService.importUser(list);
    return "success";
}
```

#### 2.3 自定义样式

```java
// 创建Excel写入处理器
ExcelSheetWriteHandler handler = new ExcelSheetWriteHandler();

// 添加列宽
handler.addColumnWidth(0, 20);
handler.addColumnWidth(1, 15);

// 添加数据校验
handler.addValidation("status", new String[]{"正常", "禁用"});

// 导出Excel
ExcelUtils.write(response, "用户列表", UserExcel.class, list, handler);
```

## 核心功能

### 1. 样式支持

丰富的单元格样式:

1. 预定义样式
   - 标题样式
   - 内容样式
   - 数字样式
   - 日期样式

2. 自定义样式
   - 字体设置
   - 边框设置
   - 背景颜色
   - 对齐方式

3. 格式设置
   - 数字格式
   - 日期格式
   - 文本格式
   - 自定义格式

### 2. 数据校验

完善的数据校验机制:

1. 基础校验
   - 必填校验
   - 长度校验
   - 范围校验
   - 格式校验

2. 下拉选择
   - 静态数据
   - 动态数据
   - 级联选择
   - 多选支持

3. 自定义校验
   - 自定义规则
   - 自定义提示
   - 自定义错误处理

### 3. 格式转换

强大的数据转换能力:

1. 内置转换器
   - 数字转换
   - 日期转换
   - 枚举转换
   - 布尔转换

2. 字典转换
   - 系统字典
   - 自定义字典
   - 动态字典
   - 多级字典

3. 自定义转换
   - 实现转换接口
   - 注册转换器
   - 配置转换规则

## 最佳实践

### 1. 导出优化

1. 大数据量处理
   - 分页导出
   - 异步导出
   - 流式导出

2. 内存优化
   - 合理设置缓存
   - 及时释放资源
   - 控制并发数量

3. 性能优化
   - 减少样式种类
   - 合并相同样式
   - 使用简单格式

### 2. 导入优化

1. 数据校验
   - 分层校验
   - 批量校验
   - 自定义校验

2. 错误处理
   - 友好提示
   - 错误记录
   - 部分导入

3. 并发处理
   - 并发导入
   - 任务队列
   - 状态跟踪

### 3. 模板管理

1. 模板设计
   - 统一风格
   - 清晰布局
   - 友好提示

2. 模板维护
   - 版本管理
   - 动态更新
   - 历史记录

3. 模板共享
   - 模板复用
   - 权限控制
   - 使用统计

## 常见问题

### Q1: 如何处理大数据量Excel?
A1: 可以采取以下措施:
1. 使用分页导出
2. 实现异步处理
3. 采用流式导出

### Q2: 如何优化导入性能?
A2: 建议采取以下措施:
1. 批量处理数据
2. 使用多线程导入
3. 优化数据校验

### Q3: 如何处理复杂格式转换?
A3: 可以通过以下方式:
1. 实现自定义转换器
2. 使用组合转换器
3. 配置转换规则

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基本的导入导出
- 支持样式自定义
- 提供数据校验

### v1.1.0 (2024-03-01)
- 优化大数据量处理
- 添加异步导出支持
- 完善错误处理 