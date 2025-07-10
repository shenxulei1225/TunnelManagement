# Cheers Framework Common模块使用指南

## 目录

- [1. 概述](#1-概述)
- [2. 核心功能](#2-核心功能)
- [3. 使用指南](#3-使用指南)
- [4. 最佳实践](#4-最佳实践)
- [5. 常见问题](#5-常见问题)
- [6. API参考](#6-api参考)

## 1. 概述

`cheers-framework-common`模块提供了一系列通用工具和基础功能，用于简化开发过程，提高代码质量和开发效率。本文档详细介绍了该模块的使用方法和最佳实践。

## 2. 核心功能

### 2.1 数据校验

#### 2.1.1 校验注解
- `@Mobile`：手机号码校验
  ```java
  @Mobile
  private String phoneNumber;
  ```

- `@Telephone`：电话号码校验
  ```java
  @Telephone
  private String telNumber;
  ```

- `@InEnum`：枚举值校验
  ```java
  @InEnum(value = UserTypeEnum.class)
  private Integer userType;
  ```

#### 2.1.2 ValidationUtils工具类
```java
// 快速校验单个属性
ValidationUtils.validateProperty(object, "fieldName");

// 校验整个对象
ValidationUtils.validate(object);
```

### 2.2 核心工具类

#### 2.2.1 KeyValue类
用于处理键值对数据：
```java
// 创建键值对
KeyValue<String, Integer> kv = new KeyValue<>("key", 100);

// 在列表中使用
List<KeyValue<String, Object>> dataList = new ArrayList<>();
dataList.add(new KeyValue<>("name", "张三"));
dataList.add(new KeyValue<>("age", 25));
```

#### 2.2.2 ArrayValuable接口
用于枚举值数组转换：
```java
public enum UserTypeEnum implements ArrayValuable<Integer> {
    ADMIN(1, "管理员"),
    USER(2, "普通用户");
    
    private final Integer value;
    private final String label;
    
    @Override
    public Integer[] array() {
        return new Integer[]{ADMIN.value, USER.value};
    }
}
```

### 2.3 通用工具包

#### 2.3.1 ServletUtils
处理Servlet相关操作：
```java
// 获取请求参数
String param = ServletUtils.getParameter("paramName");

// 获取请求头
String header = ServletUtils.getHeader("headerName");

// 获取当前请求对象
HttpServletRequest request = ServletUtils.getRequest();
```

#### 2.3.2 HttpUtils
HTTP请求工具：
```java
// 发送GET请求
String result = HttpUtils.get(url);

// 发送POST请求
String result = HttpUtils.post(url, params);

// 下载文件
HttpUtils.download(url, localPath);
```

#### 2.3.3 NumberUtils
数字处理工具：
```java
// 安全的数字转换
Integer value = NumberUtils.parseInteger("123");

// 数字格式化
String formatted = NumberUtils.format(1234.5678, "###.##");
```

#### 2.3.4 MoneyUtils
金额处理工具：
```java
// 分转元
BigDecimal yuan = MoneyUtils.fenToYuan(10000);

// 元转分
Long fen = MoneyUtils.yuanToFen(new BigDecimal("100.00"));

// 金额格式化
String formatted = MoneyUtils.format(new BigDecimal("1234.56"));
```

## 3. 使用指南

### 3.1 环境要求
- JDK 17+
- Maven 3.8+
- Spring Boot 3.x

### 3.2 快速开始

1. 引入依赖
```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-common</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

2. 配置说明
```yaml
cheers:
  # 项目配置
  project:
    name: Cheers项目
```

### 3.3 常用场景

#### 3.3.1 参数校验
```java
@Data
public class UserDTO {
    @Mobile
    private String mobile;
    
    @InEnum(value = UserTypeEnum.class)
    private Integer userType;
}

// 在Service中使用
ValidationUtils.validate(userDTO);
```

#### 3.3.2 HTTP请求处理
```java
// 获取当前请求IP
String ip = ServletUtils.getClientIP();

// 发送HTTP请求
Map<String, Object> params = new HashMap<>();
params.put("key", "value");
String result = HttpUtils.post(url, params);
```

## 4. 最佳实践

### 4.1 参数校验
- 使用注解进行声明式校验
- 合理使用分组校验
- 统一的异常处理

### 4.2 工具类使用
- 优先使用框架提供的工具类
- 注意异常处理和日志记录
- 遵循统一的编码规范

### 4.3 性能优化
- 合理使用缓存
- 避免重复创建对象
- 注意资源释放

## 5. 常见问题

### 5.1 校验相关
Q: 如何自定义校验注解？
A: 创建注解类和对应的验证器，参考`@Mobile`的实现。

Q: 如何处理校验异常？
A: 使用全局异常处理器统一处理`MethodArgumentNotValidException`。

### 5.2 工具类使用
Q: 如何扩展工具类功能？
A: 可以继承现有工具类，或创建新的工具类。

## 6. API参考

### 6.1 校验注解
| 注解 | 说明 | 示例 |
|------|------|------|
| @Mobile | 手机号校验 | @Mobile private String mobile; |
| @Telephone | 电话号码校验 | @Telephone private String tel; |
| @InEnum | 枚举值校验 | @InEnum(UserType.class) private Integer type; |

### 6.2 工具类
| 工具类 | 主要功能 | 使用场景 |
|--------|----------|----------|
| ServletUtils | Servlet操作 | Web请求处理 |
| HttpUtils | HTTP请求 | 远程调用 |
| NumberUtils | 数字处理 | 数值转换 |
| MoneyUtils | 金额处理 | 交易计算 |

## 更新记录

| 版本  | 日期       | 更新内容               | 作者 |
|------|------------|----------------------|------|
| 1.0.0 | 2024-01-24 | 初始版本               | AI   | 