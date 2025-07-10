# Common模块

## 功能介绍

Common模块提供了框架的公共基础功能支持,包括通用工具类、异常处理、数据校验、类型转换等基础特性。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-common</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  common:
    # 异常处理
    exception:
      # 包含堆栈
      include-stack-trace: true
      # 国际化
      i18n:
        enabled: true
        basename: i18n/messages
    # 数据校验
    validation:
      # 快速失败
      fail-fast: true
    # 类型转换
    convert:
      # 日期格式
      date-format: yyyy-MM-dd HH:mm:ss
      # 时区
      time-zone: GMT+8
```

### 3. 使用示例

#### 3.1 工具类使用

```java
// 字符串工具
String str = StringUtils.trim(" hello ");
boolean empty = StringUtils.isEmpty(str);
String camel = StringUtils.toCamelCase("user_name");

// 日期工具
Date now = DateUtils.now();
String formatted = DateUtils.format(now, "yyyy-MM-dd");
Date parsed = DateUtils.parse("2024-01-01", "yyyy-MM-dd");

// 加密工具
String md5 = EncryptUtils.md5("password");
String encrypted = EncryptUtils.aesEncrypt("content", "key");
String decrypted = EncryptUtils.aesDecrypt(encrypted, "key");
```

#### 3.2 异常处理

```java
@RestController
@RequestMapping("/api")
public class ApiController {
    
    @GetMapping("/test")
    public Result<String> test() {
        // 业务异常
        if (condition) {
            throw new BusinessException("业务异常");
        }
        
        // 参数异常
        if (param == null) {
            throw new ParamException("参数不能为空");
        }
        
        // 系统异常
        try {
            return service.process();
        } catch (Exception e) {
            throw new SystemException("系统异常", e);
        }
    }
}
```

#### 3.3 数据校验

```java
@Data
public class UserDTO {
    
    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 20, message = "用户名长度必须在4-20之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "密码必须是6-20位字母或数字")
    private String password;
    
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;
    
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

## 核心功能

### 1. 工具类

丰富的工具类支持:

1. 字符串工具
   - 格式化
   - 转换
   - 验证
   - 处理

2. 日期工具
   - 格式化
   - 解析
   - 计算
   - 比较

3. 加密工具
   - 对称加密
   - 非对称加密
   - 散列算法
   - 编码转换

### 2. 异常处理

统一的异常处理机制:

1. 异常体系
   - 基础异常
   - 业务异常
   - 系统异常
   - 自定义异常

2. 异常配置
   - 异常编码
   - 异常消息
   - 异常分类
   - 异常处理

3. 异常工具
   - 异常包装
   - 异常提取
   - 异常转换
   - 异常链处理

### 3. 数据校验

完整的数据校验支持:

1. 注解校验
   - 空值校验
   - 长度校验
   - 范围校验
   - 格式校验

2. 自定义校验
   - 自定义注解
   - 自定义规则
   - 自定义消息
   - 分组校验

3. 校验工具
   - 参数校验
   - 对象校验
   - 集合校验
   - 嵌套校验

## 最佳实践

### 1. 工具类使用

1. 命名规范
   - 见名知意
   - 统一风格
   - 简洁清晰
   - 易于理解

2. 功能设计
   - 单一职责
   - 通用性强
   - 易于扩展
   - 性能优化

3. 使用建议
   - 合理封装
   - 充分复用
   - 注意性能
   - 考虑安全

### 2. 异常处理

1. 异常设计
   - 异常分层
   - 异常分类
   - 异常信息
   - 异常处理

2. 处理原则
   - 早发现
   - 早处理
   - 精准定位
   - 优雅处理

3. 最佳实践
   - 异常转换
   - 异常包装
   - 异常记录
   - 异常恢复

### 3. 数据校验

1. 校验设计
   - 分层校验
   - 组合校验
   - 动态校验
   - 自定义校验

2. 性能优化
   - 快速失败
   - 缓存校验
   - 批量校验
   - 异步校验

3. 使用建议
   - 统一规范
   - 友好提示
   - 完整校验
   - 性能考虑

## 常见问题

### Q1: 如何优化工具类性能?
A1: 可以通过以下方式:
1. 合理缓存
2. 减少对象创建
3. 算法优化

### Q2: 如何处理异常链?
A2: 建议采取以下措施:
1. 异常包装
2. 原因保留
3. 堆栈处理

### Q3: 如何提高校验效率?
A3: 可以通过以下方式:
1. 快速失败
2. 缓存验证器
3. 并行校验

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基础工具类
- 支持异常处理
- 提供数据校验

### v1.1.0 (2024-03-01)
- 优化工具性能
- 增强异常处理
- 完善校验功能 