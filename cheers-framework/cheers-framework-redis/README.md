# Redis模块

## 功能介绍

Redis模块提供了Redis缓存的基础功能支持,包括缓存操作、分布式锁、消息订阅、限流等特性。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-redis</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  redis:
    # 单机配置
    host: localhost
    port: 6379
    database: 0
    # 集群配置
    cluster:
      nodes:
        - 127.0.0.1:7001
        - 127.0.0.1:7002
    # 连接池配置
    pool:
      max-active: 8
      max-idle: 8
      min-idle: 0
      max-wait: -1
    # 缓存配置
    cache:
      # 默认过期时间(秒)
      default-timeout: 1800
      # 是否允许空值
      allow-null: true
```

### 3. 使用示例

#### 3.1 缓存操作

```java
@Autowired
private RedisTemplate<String, Object> redisTemplate;

// 设置缓存
redisTemplate.opsForValue().set("user:1", user);

// 获取缓存
User user = redisTemplate.opsForValue().get("user:1");

// 删除缓存
redisTemplate.delete("user:1");

// 设置过期时间
redisTemplate.expire("user:1", 1, TimeUnit.HOURS);
```

#### 3.2 分布式锁

```java
@Autowired
private RedisLockRegistry lockRegistry;

// 获取锁
Lock lock = lockRegistry.obtain("order:1");
try {
    // 尝试加锁
    if (lock.tryLock(5, TimeUnit.SECONDS)) {
        // 执行业务逻辑
        orderService.process();
    }
} finally {
    // 释放锁
    lock.unlock();
}
```

#### 3.3 消息订阅

```java
@Component
public class OrderMessageListener {
    
    @RedisListener(topic = "order:created")
    public void handleOrderCreated(String message) {
        // 处理订单创建消息
        log.info("收到订单创建消息: {}", message);
    }
}
```

## 核心功能

### 1. 缓存管理

完善的缓存管理机制:

1. 缓存操作
   - 基础操作
   - 批量操作
   - 原子操作
   - 管道操作

2. 缓存策略
   - 过期策略
   - 淘汰策略
   - 更新策略
   - 预加载策略

3. 缓存工具
   - 序列化工具
   - 压缩工具
   - 监控工具
   - 统计工具

### 2. 分布式功能

强大的分布式特性支持:

1. 分布式锁
   - 可重入锁
   - 读写锁
   - 公平锁
   - 联锁

2. 限流功能
   - 计数器限流
   - 令牌桶限流
   - 滑动窗口限流
   - 分布式限流

3. 消息通信
   - 发布订阅
   - 消息队列
   - 延迟队列
   - 优先级队列

### 3. 集群管理

完整的集群管理功能:

1. 节点管理
   - 节点发现
   - 健康检查
   - 故障转移
   - 负载均衡

2. 数据同步
   - 主从复制
   - 数据迁移
   - 数据备份
   - 数据恢复

3. 监控运维
   - 性能监控
   - 状态监控
   - 日志管理
   - 报警通知

## 最佳实践

### 1. 缓存设计

1. 键设计
   - 命名规范
   - 前缀管理
   - 分隔符统一
   - 长度控制

2. 数据结构
   - 合理选择
   - 压缩优化
   - 空间控制
   - 过期管理

3. 更新策略
   - 更新机制
   - 一致性保证
   - 并发控制
   - 异常处理

### 2. 性能优化

1. 连接管理
   - 连接池配置
   - 超时设置
   - 重试机制
   - 异常处理

2. 数据压缩
   - 压缩算法
   - 压缩阈值
   - 压缩策略
   - 性能平衡

3. 批量操作
   - 管道使用
   - 批量大小
   - 超时控制
   - 异常处理

### 3. 安全配置

1. 访问控制
   - 密码认证
   - IP限制
   - 命令限制
   - 账号管理

2. 数据安全
   - 数据加密
   - 备份策略
   - 恢复机制
   - 审计日志

3. 运维安全
   - 监控告警
   - 日志记录
   - 性能基线
   - 应急预案

## 常见问题

### Q1: 如何处理缓存穿透?
A1: 可以采取以下措施:
1. 布隆过滤器
2. 空值缓存
3. 参数校验

### Q2: 如何解决缓存雪崩?
A2: 建议采取以下措施:
1. 过期时间随机化
2. 熔断降级
3. 多级缓存

### Q3: 如何保证缓存一致性?
A3: 可以通过以下方式:
1. 更新策略选择
2. 并发控制
3. 异常回滚

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基础缓存功能
- 支持分布式锁
- 提供消息订阅

### v1.1.0 (2024-03-01)
- 优化缓存性能
- 增强集群功能
- 完善监控运维 