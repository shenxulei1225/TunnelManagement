# WebSocket模块

## 功能介绍

WebSocket模块提供了实时通信的基础功能支持,包括连接管理、消息处理、会话管理、集群支持等特性。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-websocket</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  websocket:
    # 端点配置
    endpoint: /ws
    # 允许的源
    allowed-origins: "*"
    # 心跳配置
    heartbeat:
      # 间隔时间(秒)
      interval: 60
      # 超时时间(秒)
      timeout: 180
    # 消息配置
    message:
      # 最大消息大小
      max-size: 65536
      # 缓冲区大小
      buffer-size: 8192
```

### 3. 使用示例

#### 3.1 消息处理器

```java
@Component
public class ChatMessageHandler extends TextWebSocketHandler {
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 处理文本消息
        String payload = message.getPayload();
        log.info("收到消息: {}", payload);
        
        // 发送响应
        session.sendMessage(new TextMessage("收到消息: " + payload));
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 连接建立后的处理
        log.info("建立连接: {}", session.getId());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 连接关闭后的处理
        log.info("关闭连接: {}", session.getId());
    }
}
```

#### 3.2 消息发送

```java
@Service
public class MessageService {
    
    @Autowired
    private WebSocketMessageSender messageSender;
    
    public void sendToUser(String userId, String message) {
        // 发送给指定用户
        messageSender.sendToUser(userId, message);
    }
    
    public void sendToAll(String message) {
        // 广播消息
        messageSender.sendToAll(message);
    }
    
    public void sendToGroup(String groupId, String message) {
        // 发送给指定组
        messageSender.sendToGroup(groupId, message);
    }
}
```

#### 3.3 认证拦截

```java
@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 握手前的认证处理
        String token = request.getHeaders().getFirst("Sec-WebSocket-Protocol");
        if (StringUtils.isBlank(token)) {
            return false;
        }
        
        // 验证token
        LoginUser user = tokenService.getLoginUser(token);
        if (user == null) {
            return false;
        }
        
        // 保存用户信息
        attributes.put(WebSocketConstants.LOGIN_USER_KEY, user);
        return true;
    }
}
```

## 核心功能

### 1. 连接管理

完整的连接生命周期管理:

1. 连接建立
   - 握手认证
   - 会话创建
   - 资源分配
   - 状态初始化

2. 连接维护
   - 心跳检测
   - 状态监控
   - 异常处理
   - 资源回收

3. 连接关闭
   - 主动关闭
   - 超时关闭
   - 异常关闭
   - 资源释放

### 2. 消息处理

灵活的消息处理机制:

1. 消息类型
   - 文本消息
   - 二进制消息
   - 事件消息
   - 系统消息

2. 消息路由
   - 点对点
   - 广播
   - 组播
   - 订阅发布

3. 消息过滤
   - 内容过滤
   - 频率控制
   - 大小限制
   - 黑名单过滤

### 3. 集群支持

可靠的集群通信支持:

1. 会话同步
   - 会话复制
   - 状态同步
   - 断线重连
   - 负载均衡

2. 消息同步
   - 消息转发
   - 消息确认
   - 消息重试
   - 消息持久化

3. 集群管理
   - 节点发现
   - 健康检查
   - 故障转移
   - 负载均衡

## 最佳实践

### 1. 连接设计

1. 认证机制
   - Token认证
   - 签名认证
   - 证书认证
   - 多因素认证

2. 连接控制
   - 连接限制
   - 重连机制
   - 并发控制
   - 超时处理

3. 性能优化
   - 连接池化
   - 资源控制
   - 线程优化
   - 内存优化

### 2. 消息设计

1. 消息格式
   - 协议定义
   - 格式规范
   - 版本控制
   - 兼容处理

2. 消息处理
   - 异步处理
   - 批量处理
   - 优先级处理
   - 重试机制

3. 消息监控
   - 消息统计
   - 性能监控
   - 异常监控
   - 日志记录

### 3. 集群设计

1. 架构设计
   - 分布式架构
   - 高可用设计
   - 扩展性设计
   - 容错设计

2. 数据同步
   - 会话同步
   - 消息同步
   - 状态同步
   - 配置同步

3. 运维管理
   - 监控告警
   - 日志管理
   - 配置管理
   - 运维工具

## 常见问题

### Q1: 如何处理连接断开?
A1: 可以通过以下方式:
1. 心跳检测
2. 自动重连
3. 会话保持

### Q2: 如何实现消息可靠性?
A2: 建议采取以下措施:
1. 消息确认
2. 消息重试
3. 消息持久化

### Q3: 如何优化性能?
A3: 可以通过以下方式:
1. 连接池化
2. 消息压缩
3. 批量处理

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基础连接管理
- 支持消息处理
- 提供集群支持

### v1.1.0 (2024-03-01)
- 优化连接管理
- 增强消息处理
- 完善集群功能 