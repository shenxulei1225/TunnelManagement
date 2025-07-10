# 数据权限模块

## 功能介绍

数据权限模块提供了灵活的数据访问控制方案,支持多种规则组合方式,并提供了规则缓存和动态更新等功能。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-data-permission</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件

```yaml
cheers:
  data-permission:
    # 是否开启数据权限
    enable: true
    # 是否打印规则日志
    print-rule: true
    # 忽略的表名
    ignore-tables:
      - sys_user
    # 忽略的请求
    ignore-urls:
      - /login
```

### 3. 使用示例

#### 3.1 定义数据权限规则

```java
@Component
public class DeptDataPermissionRule implements DataPermissionRule {
    
    @Override
    public String getResource() {
        return "sys_user";
    }
    
    @Override
    public Expression getExpression(FilterContext context) {
        // 获取当前用户部门ID
        Long deptId = context.getContext("deptId");
        // 返回过滤条件
        return new SimpleExpression("dept_id", deptId);
    }
}
```

#### 3.2 使用注解控制

```java
@DataPermission(type = DeptDataPermissionRule.class)
public List<UserDO> getUserList(UserQuery query) {
    return userMapper.selectList(query);
}

// 组合多个规则
@DataPermission(type = {DeptDataPermissionRule.class, RoleDataPermissionRule.class}, 
                match = DataPermissionRule.MatchType.AND)
public List<UserDO> getUserList(UserQuery query) {
    return userMapper.selectList(query);
}
```

#### 3.3 动态创建规则组合

```java
// 创建规则组合
String combinationId = ruleFactory.createRuleCombination(
    Arrays.asList(DeptDataPermissionRule.class, RoleDataPermissionRule.class),
    RuleCombination.Type.AND
);

// 使用规则组合
List<DataPermissionRule> rules = ruleFactory.getRuleCombination(combinationId).getRules();
```

## 核心功能

### 1. 规则组合

支持灵活的规则组合方式:

1. AND组合
   - 多个规则同时满足
   - 适合严格权限控制

2. OR组合
   - 满足任一规则即可
   - 适合灵活权限控制

3. 动态组合
   - 运行时创建组合
   - 支持组合缓存

### 2. 规则缓存

高效的规则缓存机制:

1. 缓存结构
   - 使用ConcurrentHashMap
   - 支持并发访问

2. 缓存管理
   - 动态更新规则
   - 定期清理缓存

3. 性能优化
   - 预编译表达式
   - 减少运行时开销

### 3. 表达式引擎

强大的表达式处理能力:

1. 表达式类型
   - 简单比较
   - 复杂条件
   - 自定义函数

2. 上下文支持
   - 支持参数传递
   - 动态获取上下文

3. 扩展能力
   - 自定义操作符
   - 自定义函数

## 最佳实践

### 1. 规则设计

1. 粒度控制
   - 按业务划分规则
   - 避免规则过于复杂

2. 组合策略
   - 合理使用AND/OR
   - 控制组合深度

3. 性能考虑
   - 优化表达式复杂度
   - 合理使用缓存

### 2. 缓存优化

1. 缓存配置
   - 设置合理的缓存大小
   - 配置过期策略

2. 更新策略
   - 及时清理无效规则
   - 定期刷新缓存

3. 监控管理
   - 记录缓存命中率
   - 监控内存占用

### 3. 扩展开发

1. 自定义规则
   - 实现Rule接口
   - 注册到Spring容器

2. 自定义表达式
   - 扩展Expression接口
   - 实现解析逻辑

3. 自定义函数
   - 注册函数实现
   - 配置函数映射

## 常见问题

### Q1: 如何处理复杂的权限规则?
A1: 可以通过以下方式:
1. 拆分为多个简单规则
2. 使用规则组合
3. 实现自定义表达式

### Q2: 如何优化规则执行性能?
A2: 建议采取以下措施:
1. 使用规则缓存
2. 优化表达式复杂度
3. 合理设置缓存策略

### Q3: 如何实现动态权限控制?
A3: 可以通过以下方式:
1. 使用规则工厂动态创建
2. 实现规则动态更新
3. 配置动态生效

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基本的数据权限控制
- 支持规则组合功能
- 提供注解方式配置

### v1.1.0 (2024-03-01)
- 优化规则缓存机制
- 添加动态规则支持
- 完善监控功能 