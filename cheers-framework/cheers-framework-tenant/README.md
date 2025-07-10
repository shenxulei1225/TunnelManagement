# 多租户模块

## 功能介绍

多租户模块提供了完整的多租户数据隔离解决方案,支持多种隔离级别,并提供了跨租户访问和缓存隔离等功能。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-tenant</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  tenant:
    # 是否开启多租户
    enable: true
    # 默认租户ID
    default-tenant-id: 1
    # 超级管理员租户ID
    super-tenant-id: 0
    # 是否允许跨租户访问
    enable-cross-tenant: false
    # 数据隔离级别(1:独立数据库, 2:独立Schema, 3:独立表, 4:独立字段)
    isolation-level: 4
    # 需要忽略多租户的表名
    ignore-tables:
      - sys_tenant
      - sys_user
    # 需要忽略多租户的请求
    ignore-urls:
      - /login
      - /logout
    # 需要忽略多租户的缓存
    ignore-caches:
      - sys:dict
    # 允许跨租户访问的角色
    allow-cross-tenant-roles:
      - SUPER_ADMIN
```

### 3. 使用示例

#### 3.1 获取当前租户ID

```java
// 获取当前租户ID
Long tenantId = TenantContextHolder.getTenantId();

// 设置是否忽略租户
TenantContextHolder.setIgnore(true);

// 设置访问租户ID(用于跨租户访问)
TenantContextHolder.setVisitTenantId(2L);
```

#### 3.2 Redis缓存隔离

```java
@Cacheable(value = "user", key = "#id")
public UserDO getUser(Long id) {
    // 会自动按租户隔离缓存
    return userMapper.selectById(id);
}
```

#### 3.3 数据库隔离

```java
// 实体类
@TableName("sys_user")
public class UserDO {
    
    /**
     * 租户ID字段
     */
    @TenantId
    private Long tenantId;
    
    // 其他字段...
}

// Mapper接口
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
    // 会自动注入租户ID条件
    List<UserDO> selectList(@Param("wrapper") Wrapper<UserDO> wrapper);
}
```

## 核心功能

### 1. 数据隔离级别

支持四种隔离级别:

1. 独立数据库
   - 每个租户使用独立的数据库
   - 物理隔离,安全性最高
   - 适合大型租户

2. 独立Schema
   - 共享数据库,独立Schema
   - 中等隔离级别
   - 适合中型租户

3. 独立表
   - 共享数据库和Schema,独立表
   - 表名添加租户ID后缀
   - 适合小型租户

4. 独立字段
   - 共享数据库、Schema和表
   - 通过字段区分租户
   - 适合小规模系统

### 2. 跨租户访问

提供了灵活的跨租户访问控制:

1. 超级管理员
   - 可以访问所有租户数据
   - 通过`super-tenant-id`配置

2. 角色控制
   - 可配置允许跨租户访问的角色
   - 通过`allow-cross-tenant-roles`配置

3. 动态切换
   - 支持运行时切换访问租户
   - 通过`TenantContextHolder`控制

### 3. 缓存隔离

基于Redis的多租户缓存隔离:

1. 自动隔离
   - 自动为缓存key添加租户前缀
   - 支持忽略特定缓存

2. 性能优化
   - 优化key生成策略
   - 减少内存占用

3. 缓存管理
   - 支持按租户清理缓存
   - 提供缓存统计功能

## 最佳实践

### 1. 隔离级别选择

- 租户数量少,数据量大 -> 独立数据库
- 租户数量适中,要求高 -> 独立Schema
- 租户数量多,要求一般 -> 独立字段
- 系统简单,成本敏感 -> 独立字段

### 2. 性能优化

1. 合理使用ignore配置
   - 将公共表添加到ignore-tables
   - 将公共缓存添加到ignore-caches

2. 缓存策略
   - 针对热点数据使用缓存
   - 及时清理无用缓存

3. 数据库优化
   - 租户字段建立索引
   - 大表按租户分区

### 3. 安全建议

1. 租户ID管理
   - 避免使用连续的租户ID
   - 定期清理无效租户

2. 访问控制
   - 限制跨租户访问范围
   - 记录跨租户访问日志

3. 数据保护
   - 定期备份租户数据
   - 提供数据导出功能

## 常见问题

### Q1: 如何选择合适的隔离级别?
A1: 根据租户规模、安全要求和成本预算综合考虑。一般建议:
- 企业级应用: 独立数据库
- 部门级应用: 独立Schema
- 小型应用: 独立字段

### Q2: 如何处理跨租户数据共享?
A2: 可以通过以下方式:
1. 使用ignore-tables配置公共表
2. 实现自定义的数据共享规则
3. 使用消息队列进行数据同步

### Q3: 如何优化大量租户场景的性能?
A3: 可以采取以下措施:
1. 使用数据库分库分表
2. 优化缓存策略
3. 实现租户级别的连接池

## 更新记录

### v1.0.0 (2024-01-01)
- 支持多种数据隔离级别
- 实现跨租户访问控制
- 提供Redis缓存隔离

### v1.1.0 (2024-03-01)
- 优化缓存隔离性能
- 添加租户数据统计功能
- 完善租户管理接口 