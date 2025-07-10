# IP模块

## 功能介绍

IP模块提供了IP地址解析和地理位置查询的功能支持,包括IP地址解析、地理位置查询、区域判断、IP黑白名单等特性。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-ip</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  ip:
    # IP数据库配置
    db-type: local # 可选: local, webapi
    db-path: ip2region.db
    # WebAPI配置(使用第三方服务时)
    web-api:
      url: http://ip.taobao.com/service/getIpInfo.php
      timeout: 3000
    # 黑白名单
    blacklist:
      - 192.168.1.1
      - 192.168.1.0/24
    whitelist:
      - 127.0.0.1
      - 10.0.0.0/8
```

### 3. 使用示例

#### 3.1 IP地址解析

```java
@Autowired
private IpService ipService;

// 解析IP地址
IpInfo info = ipService.parse("114.114.114.114");
System.out.println("国家: " + info.getCountry());
System.out.println("省份: " + info.getProvince());
System.out.println("城市: " + info.getCity());
System.out.println("运营商: " + info.getIsp());

// 判断IP归属
boolean isChina = ipService.isChina("114.114.114.114");
boolean isLocal = ipService.isLocal("127.0.0.1");
```

#### 3.2 IP访问控制

```java
@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Autowired
    private IpService ipService;
    
    @GetMapping("/test")
    public Result<String> test(HttpServletRequest request) {
        // 获取客户端IP
        String clientIp = IpUtils.getClientIp(request);
        
        // 检查黑白名单
        if (ipService.isBlacklisted(clientIp)) {
            return Result.error("IP被禁止访问");
        }
        
        if (!ipService.isWhitelisted(clientIp)) {
            return Result.error("IP未授权访问");
        }
        
        return Result.success("访问成功");
    }
}
```

#### 3.3 地理位置限制

```java
@Aspect
@Component
public class IpRegionCheckAspect {
    
    @Around("@annotation(ipRegionCheck)")
    public Object check(ProceedingJoinPoint point, IpRegionCheck ipRegionCheck) {
        // 获取客户端IP
        String clientIp = IpUtils.getClientIp();
        
        // 检查地区限制
        IpInfo info = ipService.parse(clientIp);
        if (!ipRegionCheck.regions().contains(info.getProvince())) {
            throw new BusinessException("当前地区不支持访问");
        }
        
        return point.proceed();
    }
}
```

## 核心功能

### 1. IP解析

完整的IP地址解析功能:

1. 地址解析
   - IPv4解析
   - IPv6解析
   - 特殊地址
   - 保留地址

2. 地理信息
   - 国家/地区
   - 省份
   - 城市
   - 运营商

3. 解析方式
   - 本地数据库
   - 在线API
   - 混合模式
   - 缓存优化

### 2. 访问控制

灵活的IP访问控制机制:

1. 黑白名单
   - IP精确匹配
   - 网段匹配
   - 通配符匹配
   - 正则匹配

2. 规则管理
   - 规则配置
   - 规则优先级
   - 规则缓存
   - 规则更新

3. 控制策略
   - 全局控制
   - 接口控制
   - 动态控制
   - 临时控制

### 3. 地理限制

地理位置访问限制功能:

1. 区域限制
   - 国家限制
   - 省份限制
   - 城市限制
   - 自定义区域

2. 策略配置
   - 允许策略
   - 禁止策略
   - 默认策略
   - 例外策略

3. 限制方式
   - 注解方式
   - 配置方式
   - 动态方式
   - 组合方式

## 最佳实践

### 1. IP解析

1. 数据源选择
   - 本地优先
   - 在线备份
   - 定期更新
   - 失败切换

2. 性能优化
   - 缓存使用
   - 批量查询
   - 异步处理
   - 预加载

3. 准确性保证
   - 数据验证
   - 结果校验
   - 异常处理
   - 降级处理

### 2. 访问控制

1. 规则设计
   - 规则分层
   - 规则组合
   - 规则继承
   - 规则覆盖

2. 性能考虑
   - 规则缓存
   - 快速匹配
   - 并发处理
   - 超时控制

3. 安全防护
   - 防护策略
   - 攻击检测
   - 风险控制
   - 审计日志

### 3. 运维管理

1. 监控告警
   - 访问监控
   - 异常监控
   - 性能监控
   - 阈值告警

2. 日志管理
   - 访问日志
   - 操作日志
   - 异常日志
   - 审计日志

3. 配置管理
   - 配置更新
   - 配置备份
   - 配置验证
   - 配置回滚

## 常见问题

### Q1: 如何提高IP解析准确性?
A1: 可以通过以下方式:
1. 多数据源对比
2. 定期更新数据
3. 结果验证

### Q2: 如何优化性能?
A2: 建议采取以下措施:
1. 合理使用缓存
2. 批量查询处理
3. 异步解析处理

### Q3: 如何处理IP代理问题?
A3: 可以通过以下方式:
1. 获取X-Forwarded-For
2. 配置代理白名单
3. 多级代理解析

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基础IP解析
- 支持访问控制
- 提供地理限制

### v1.1.0 (2024-03-01)
- 优化解析性能
- 增强控制功能
- 完善监控管理 