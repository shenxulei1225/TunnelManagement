# MQ模块

## 功能介绍

MQ模块提供了消息队列的基础功能支持,包括消息发送、消息消费、延迟队列、死信队列等特性。支持RabbitMQ、Kafka、RocketMQ等主流消息中间件。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-mq</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  mq:
    # 消息中间件类型
    type: rabbitmq # 可选: rabbitmq, kafka, rocketmq
    # RabbitMQ配置
    rabbitmq:
      host: localhost
      port: 5672
      username: guest
      password: guest
      # 虚拟主机
      virtual-host: /
      # 发送方确认
      publisher-confirm: true
      # 发送方返回
      publisher-returns: true
    # 重试配置
    retry:
      # 最大重试次数
      max-attempts: 3
      # 初始间隔
      initial-interval: 1000
      # 最大间隔
      max-interval: 10000
      # 间隔倍数
      multiplier: 2.0
```

### 3. 使用示例

#### 3.1 消息发送

```java
@Service
public class OrderService {
    
    @Autowired
    private MessageTemplate messageTemplate;
    
    public void createOrder(OrderDTO order) {
        // 执行业务逻辑
        OrderDO orderDO = orderMapper.insert(order);
        
        // 发送消息
        OrderMessage message = new OrderMessage()
            .setOrderId(orderDO.getId())
            .setUserId(orderDO.getUserId())
            .setAmount(orderDO.getAmount());
            
        messageTemplate.convertAndSend("order.created", message);
    }
    
    public void cancelOrder(Long orderId) {
        // 发送延迟消息
        messageTemplate.convertAndSend("order.cancel", orderId, message -> {
            // 设置消息属性
            message.setDelay(30 * 60 * 1000L); // 30分钟后取消
            return message;
        });
    }
}
```

#### 3.2 消息消费

```java
@Component
public class OrderMessageListener {
    
    @RabbitListener(queues = "order.created")
    public void handleOrderCreated(OrderMessage message) {
        // 处理订单创建消息
        log.info("收到订单创建消息: {}", message);
        
        // 执行业务逻辑
        orderService.processOrder(message);
    }
    
    @RabbitListener(queues = "order.cancel")
    public void handleOrderCancel(Long orderId) {
        // 处理订单取消消息
        log.info("收到订单取消消息: {}", orderId);
        
        // 执行取消逻辑
        orderService.cancelOrder(orderId);
    }
}
```

#### 3.3 消息重试

```java
@Component
public class RetryMessageHandler {
    
    @RabbitListener(queues = "order.retry")
    @RetryableMessage(
        maxAttempts = 3,
        backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    public void handleWithRetry(Message message) {
        try {
            // 处理消息
            processMessage(message);
        } catch (Exception e) {
            // 重试异常处理
            log.error("消息处理失败,准备重试: {}", message, e);
            throw e;
        }
    }
}
```

## 核心功能

### 1. 消息发送

完整的消息发送功能:

1. 发送方式
   - 同步发送
   - 异步发送
   - 批量发送
   - 事务发送

2. 消息属性
   - 消息ID
   - 消息类型
   - 消息标签
   - 消息属性

3. 可靠性
   - 发送确认
   - 失败重试
   - 消息持久化
   - 消息幂等

### 2. 消息消费

灵活的消息消费机制:

1. 消费模式
   - 推送消费
   - 拉取消费
   - 广播消费
   - 集群消费

2. 消费控制
   - 并发控制
   - 顺序消费
   - 重试策略
   - 死信处理

3. 消费监控
   - 消费进度
   - 消费延迟
   - 消费异常
   - 消费统计

### 3. 高级特性

丰富的高级功能支持:

1. 延迟队列
   - 定时投递
   - 延时投递
   - 周期投递
   - 优先级队列

2. 死信队列
   - 超时未消费
   - 消费失败
   - 消息拒绝
   - 队列已满

3. 消息追踪
   - 消息轨迹
   - 消息检索
   - 消息统计
   - 消息监控

## 最佳实践

### 1. 消息设计

1. 消息格式
   - 消息协议
   - 数据格式
   - 版本控制
   - 向后兼容

2. 主题设计
   - 主题划分
   - 队列数量
   - 分区策略
   - 消息路由

3. 性能优化
   - 批量处理
   - 压缩传输
   - 连接复用
   - 资源控制

### 2. 可靠性设计

1. 消息可靠
   - 消息确认
   - 消息持久化
   - 消息重试
   - 消息补偿

2. 消费可靠
   - 幂等处理
   - 事务处理
   - 失败处理
   - 死信处理

3. 高可用设计
   - 集群部署
   - 故障转移
   - 负载均衡
   - 容灾备份

### 3. 运维管理

1. 监控告警
   - 队列监控
   - 消息监控
   - 消费监控
   - 性能监控

2. 运维工具
   - 消息查询
   - 消息重发
   - 消息清理
   - 配置管理

3. 问题诊断
   - 日志分析
   - 性能分析
   - 异常排查
   - 链路追踪

## 常见问题

### Q1: 如何保证消息不丢失?
A1: 可以通过以下方式:
1. 生产者确认
2. 消息持久化
3. 消费者确认

### Q2: 如何处理重复消息?
A2: 建议采取以下措施:
1. 消息幂等
2. 业务去重
3. 唯一索引

### Q3: 如何提高消息处理性能?
A3: 可以通过以下方式:
1. 批量处理
2. 并发消费
3. 消息压缩

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基础消息收发
- 支持消息重试
- 提供死信队列

### v1.1.0 (2024-03-01)
- 优化消息性能
- 增强可靠性
- 完善监控功能 