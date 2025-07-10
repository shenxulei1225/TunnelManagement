# Web模块

## 功能介绍

Web模块提供了Web应用开发的基础功能支持,包括统一响应处理、全局异常处理、跨域配置、接口文档等特性。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-web</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  web:
    # 跨域配置
    cors:
      enable: true
      allowed-origins: "*"
      allowed-methods: "*"
    # 接口文档
    swagger:
      enable: true
      title: "API文档"
      version: "1.0.0"
    # 统一响应
    response:
      # 包装路径
      base-package: com.cheers
      # 排除路径
      exclude-paths: 
        - /actuator/**
    # 全局异常
    exception:
      # 打印堆栈
      print-stack: true
```

### 3. 使用示例

#### 3.1 统一响应

```java
@RestController
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        UserVO user = userService.getCurrentUser();
        // 会自动包装为统一响应格式
        return Result.success(user);
    }
    
    @PostMapping("/add")
    public Result<Void> addUser(@RequestBody UserDTO user) {
        userService.addUser(user);
        return Result.success();
    }
}
```

#### 3.2 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ServiceException.class)
    public Result<Void> handleServiceException(ServiceException ex) {
        log.error("业务异常", ex);
        return Result.error(ex.getCode(), ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(SystemErrorCode.SYSTEM_ERROR);
    }
}
```

## 核心功能

### 1. 统一响应

标准的响应格式处理:

1. 响应结构
   - 状态码
   - 响应消息
   - 业务数据
   - 扩展信息

2. 响应包装
   - 自动包装
   - 注解控制
   - 路径排除
   - 类型过滤

3. 响应优化
   - 数据脱敏
   - 字段过滤
   - 空值处理
   - 日期格式化

### 2. 异常处理

完善的异常处理机制:

1. 异常分类
   - 业务异常
   - 系统异常
   - 验证异常
   - 权限异常

2. 异常配置
   - 堆栈打印
   - 错误码定义
   - 消息国际化
   - 异常转换

3. 异常处理
   - 统一处理
   - 分类处理
   - 自定义处理
   - 异常通知

### 3. 接口文档

基于Swagger的接口文档:

1. 文档配置
   - 基本信息
   - 分组配置
   - 安全配置
   - 响应配置

2. 注解支持
   - 接口说明
   - 参数说明
   - 响应说明
   - 示例数据

3. 文档增强
   - 导出支持
   - 版本管理
   - 在线调试
   - 文档缓存

## 最佳实践

### 1. 响应设计

1. 状态码规范
   - 统一定义
   - 分类管理
   - 见名知意
   - 适度冗余

2. 数据封装
   - 合理分层
   - 字段精简
   - 类型统一
   - 命名规范

3. 性能优化
   - 合理缓存
   - 数据压缩
   - 按需加载
   - 并发处理

### 2. 异常处理

1. 异常设计
   - 异常继承
   - 错误码映射
   - 参数校验
   - 异常转换

2. 日志记录
   - 分级记录
   - 关键信息
   - 链路追踪
   - 异常统计

3. 错误提示
   - 友好提示
   - 安全考虑
   - 国际化支持
   - 动态配置

### 3. 接口设计

1. 接口规范
   - RESTful设计
   - 版本控制
   - 参数校验
   - 响应封装

2. 文档管理
   - 及时更新
   - 示例完整
   - 说明清晰
   - 分组管理

3. 安全控制
   - 认证授权
   - 数据校验
   - 访问控制
   - 限流降级

## 常见问题

### Q1: 如何自定义响应格式?
A1: 可以通过以下方式:
1. 继承ResponseBodyAdvice
2. 自定义Result类
3. 配置消息转换器

### Q2: 如何处理特殊异常?
A2: 建议采取以下措施:
1. 自定义ExceptionHandler
2. 实现异常转换器
3. 配置异常处理链

### Q3: 如何优化接口文档?
A3: 可以通过以下方式:
1. 使用注解详细说明
2. 添加示例数据
3. 实现文档缓存

## 更新记录

### v1.0.0 (2024-01-01)
- 实现统一响应处理
- 支持全局异常处理
- 集成Swagger文档

### v1.1.0 (2024-03-01)
- 优化响应格式
- 增强异常处理
- 完善接口文档 