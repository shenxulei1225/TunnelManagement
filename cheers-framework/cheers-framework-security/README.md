# Security模块

## 功能介绍

Security模块提供了应用安全的基础功能支持,包括认证授权、访问控制、密码管理、安全审计等特性。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-security</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  security:
    # 认证配置
    auth:
      # 登录接口
      login-url: /auth/login
      # 登出接口
      logout-url: /auth/logout
      # 排除路径
      ignore-urls:
        - /auth/captcha
        - /doc.html
    # JWT配置
    jwt:
      # 密钥
      secret-key: abcdefghijklmnopqrstuvwxyz
      # 过期时间(秒)
      expire-time: 7200
    # 密码配置
    password:
      # 加密算法
      encoder: bcrypt
      # 加密强度
      strength: 10
```

### 3. 使用示例

#### 3.1 认证配置

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // 公开接口
            .antMatchers("/auth/**").permitAll()
            // 认证接口
            .anyRequest().authenticated()
            // 开启表单登录
            .and().formLogin()
            // 禁用session
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
```

#### 3.2 权限控制

```java
@RestController
@RequestMapping("/user")
public class UserController {
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public Result<List<UserVO>> listUsers() {
        return Result.success(userService.list());
    }
    
    @PreAuthorize("hasPermission('/user/add', 'create')")
    @PostMapping("/add")
    public Result<Void> addUser(@RequestBody UserDTO user) {
        userService.add(user);
        return Result.success();
    }
}
```

#### 3.3 安全审计

```java
@Aspect
@Component
public class SecurityAuditAspect {
    
    @Around("@annotation(securityAudit)")
    public Object audit(ProceedingJoinPoint point, SecurityAudit securityAudit) {
        // 记录操作日志
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        log.info("用户[{}]执行操作[{}]", username, securityAudit.value());
        return point.proceed();
    }
}
```

## 核心功能

### 1. 认证授权

完整的认证授权体系:

1. 认证方式
   - 用户名密码
   - 手机验证码
   - 社交登录
   - 扫码登录

2. 授权模型
   - 角色授权
   - 权限授权
   - 数据授权
   - 接口授权

3. 会话管理
   - Token管理
   - 会话控制
   - 并发控制
   - 登录控制

### 2. 访问控制

细粒度的访问控制机制:

1. URL控制
   - 路径匹配
   - 方法控制
   - 表达式控制
   - 动态控制

2. 数据控制
   - 字段过滤
   - 数据脱敏
   - 结果过滤
   - 权限校验

3. 操作控制
   - 操作审计
   - 风险控制
   - 频率控制
   - 时间控制

### 3. 安全防护

全方位的安全防护措施:

1. 密码安全
   - 加密算法
   - 密码策略
   - 密码重置
   - 密码历史

2. 攻击防护
   - XSS防护
   - CSRF防护
   - SQL注入防护
   - 请求重放防护

3. 安全审计
   - 操作日志
   - 登录日志
   - 异常日志
   - 审计报告

## 最佳实践

### 1. 认证设计

1. 认证流程
   - 多因素认证
   - 验证码校验
   - 登录限制
   - 异常处理

2. Token管理
   - 生成策略
   - 存储方式
   - 刷新机制
   - 失效处理

3. 会话控制
   - 会话超时
   - 并发登录
   - 踢出策略
   - 状态同步

### 2. 授权管理

1. 权限模型
   - 角色设计
   - 权限粒度
   - 继承关系
   - 动态权限

2. 权限分配
   - 角色分配
   - 权限分配
   - 数据权限
   - 临时授权

3. 权限校验
   - 校验时机
   - 校验方式
   - 缓存优化
   - 降级处理

### 3. 安全加固

1. 密码管理
   - 复杂度要求
   - 定期更换
   - 加密存储
   - 传输加密

2. 访问控制
   - 白名单控制
   - 黑名单控制
   - 限流控制
   - 风控规则

3. 安全监控
   - 实时监控
   - 异常告警
   - 审计分析
   - 应急响应

## 常见问题

### Q1: 如何实现单点登录?
A1: 可以通过以下方式:
1. 共享Session
2. Token同步
3. OAuth2.0

### Q2: 如何处理权限动态更新?
A2: 建议采取以下措施:
1. 缓存失效
2. 消息通知
3. 定时刷新

### Q3: 如何防止暴力破解?
A3: 可以通过以下方式:
1. 验证码机制
2. 账号锁定
3. IP限制

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基础认证授权
- 支持访问控制
- 提供安全审计

### v1.1.0 (2024-03-01)
- 优化认证流程
- 增强授权管理
- 完善安全防护 