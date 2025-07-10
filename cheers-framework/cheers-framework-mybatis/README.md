# MyBatis模块

## 功能介绍

MyBatis模块提供了数据访问层的基础功能支持,包括通用CRUD、分页查询、多数据源、SQL监控等特性。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-mybatis</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  mybatis:
    # 实体包路径
    type-aliases-package: com.cheers.**.domain
    # 映射文件路径
    mapper-locations: classpath*:mapper/**/*Mapper.xml
    # 配置项
    configuration:
      # 驼峰命名
      map-underscore-to-camel-case: true
      # 缓存开关
      cache-enabled: false
    # 分页配置
    page:
      # 方言
      dialect: mysql
      # 合理化
      reasonable: true
    # 多数据源
    dynamic:
      # 默认数据源
      primary: master
      # 严格模式
      strict: true
      # 数据源配置
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/master
          username: root
          password: root
        slave:
          url: jdbc:mysql://localhost:3306/slave
          username: root
          password: root
```

### 3. 使用示例

#### 3.1 通用CRUD

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    // 继承通用方法
    // insert、update、delete、select等
    
    // 自定义方法
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User selectByUsername(String username);
}

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    // 使用通用方法
    public void addUser(User user) {
        // 插入数据
        save(user);
    }
    
    public void updateUser(User user) {
        // 更新数据
        updateById(user);
    }
    
    public void deleteUser(Long id) {
        // 删除数据
        removeById(id);
    }
    
    public User getUser(Long id) {
        // 查询数据
        return getById(id);
    }
}
```

#### 3.2 分页查询

```java
@RestController
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/page")
    public Result<IPage<User>> page(PageParam param) {
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
            .like(User::getUsername, param.getKeyword())
            .orderByDesc(User::getCreateTime);
            
        // 执行分页查询
        Page<User> page = userService.page(new Page<>(param.getPageNum(), param.getPageSize()), wrapper);
        return Result.success(page);
    }
}
```

#### 3.3 多数据源

```java
@Service
public class OrderService {
    
    @DS("master")
    public void createOrder(Order order) {
        // 主库操作
        orderMapper.insert(order);
    }
    
    @DS("slave")
    public List<Order> listOrders() {
        // 从库查询
        return orderMapper.selectList(null);
    }
}
```

## 核心功能

### 1. 通用CRUD

完整的数据操作支持:

1. 基础操作
   - 新增
   - 修改
   - 删除
   - 查询

2. 条件构造
   - Lambda条件
   - 动态条件
   - 嵌套条件
   - 自定义条件

3. 批量操作
   - 批量插入
   - 批量更新
   - 批量删除
   - 批量查询

### 2. 分页查询

强大的分页功能支持:

1. 分页参数
   - 页码
   - 页大小
   - 排序
   - 条件

2. 分页优化
   - 性能优化
   - 合理化
   - 总数优化
   - 溢出处理

3. 分页结果
   - 数据列表
   - 总记录数
   - 总页数
   - 导航页

### 3. 多数据源

灵活的多数据源管理:

1. 数据源配置
   - 主从配置
   - 动态切换
   - 负载均衡
   - 故障转移

2. 事务管理
   - 本地事务
   - 分布式事务
   - 事务传播
   - 异常回滚

3. 监控管理
   - 连接监控
   - SQL监控
   - 性能监控
   - 异常监控

## 最佳实践

### 1. 数据访问

1. DAO设计
   - 接口设计
   - 方法命名
   - 参数设计
   - 返回值设计

2. SQL优化
   - 索引使用
   - 连接优化
   - 子查询优化
   - 分页优化

3. 缓存使用
   - 缓存策略
   - 缓存粒度
   - 缓存更新
   - 缓存清理

### 2. 性能优化

1. 连接管理
   - 连接池配置
   - 超时设置
   - 重试机制
   - 异常处理

2. 执行优化
   - 批量操作
   - 延迟加载
   - 结果集处理
   - 内存优化

3. SQL优化
   - 执行计划
   - 索引优化
   - 语句优化
   - 参数优化

### 3. 规范建议

1. 命名规范
   - 表命名
   - 字段命名
   - 索引命名
   - 方法命名

2. 开发规范
   - 注释规范
   - 异常处理
   - 日志记录
   - 代码复用

3. 安全规范
   - SQL注入
   - 权限控制
   - 敏感数据
   - 审计日志

## 常见问题

### Q1: 如何优化分页查询?
A1: 可以通过以下方式:
1. 索引优化
2. 延迟关联
3. 总数优化

### Q2: 如何处理大批量数据?
A2: 建议采取以下措施:
1. 批量操作
2. 分批处理
3. 异步处理

### Q3: 如何解决多数据源事务?
A3: 可以通过以下方式:
1. 分布式事务
2. 最终一致性
3. 补偿机制

## 更新记录

### v1.0.0 (2024-01-01)
- 实现通用CRUD
- 支持分页查询
- 提供多数据源

### v1.1.0 (2024-03-01)
- 优化执行性能
- 增强功能特性
- 完善监控管理 