# 定时任务模块

## 功能介绍

定时任务模块基于Quartz实现,提供了任务分组、依赖管理、生命周期管理等功能,支持灵活的任务调度需求。

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.cheers</groupId>
    <artifactId>cheers-framework-job</artifactId>
    <version>${cheers.version}</version>
</dependency>
```

### 2. 配置文件  

```yaml
cheers:
  job:
    # 是否开启定时任务
    enable: true
    # 任务线程池配置
    thread-pool:
      core-pool-size: 10
      maximum-pool-size: 20
      keep-alive-time: 60
    # 任务存储配置
    store:
      # 使用数据库存储
      type: jdbc
      # 表前缀
      table-prefix: QRTZ_
    # 集群配置
    cluster:
      # 是否开启集群
      enable: false
```

### 3. 使用示例

#### 3.1 创建任务处理器

```java
@Component
public class DemoJobHandler implements JobHandler {
    
    @Override
    public String getName() {
        return "demoJob";
    }
    
    @Override
    public void execute(JobContext context) throws Exception {
        String param = context.getParam();
        // 执行任务逻辑
        log.info("执行定时任务,参数:{}", param);
    }
}
```

#### 3.2 任务管理

```java
@Autowired
private SchedulerManager schedulerManager;

// 添加任务
JobInfo jobInfo = new JobInfo()
    .setId("job1")
    .setGroup("demo")
    .setDescription("示例任务")
    .setCron("0 0/1 * * * ?");
schedulerManager.addJob(jobInfo);

// 暂停任务
schedulerManager.pauseJob("job1");

// 恢复任务
schedulerManager.resumeJob("job1");

// 删除任务
schedulerManager.removeJob("job1");
```

#### 3.3 任务依赖

```java
// 设置任务依赖
JobInfo jobInfo = new JobInfo()
    .setId("job2")
    .setDependencies(Arrays.asList("job1"));
schedulerManager.addJob(jobInfo);

// 触发任务(会自动检查依赖)
schedulerManager.triggerJob("job2");
```

## 核心功能

### 1. 任务分组

支持灵活的任务分组管理:

1. 分组结构
   - 按业务划分分组
   - 分组任务批量操作
   - 分组级别监控

2. 分组配置
   - 分组参数配置
   - 分组级别开关
   - 分组任务模板

3. 分组操作
   - 批量启停
   - 批量删除
   - 批量迁移

### 2. 任务依赖

完善的任务依赖管理:

1. 依赖配置
   - 支持多任务依赖
   - 支持依赖条件
   - 支持依赖超时

2. 依赖检查
   - 自动检查依赖状态
   - 支持强制执行
   - 依赖执行日志

3. 依赖监控
   - 依赖链路追踪
   - 依赖状态统计
   - 依赖异常告警

### 3. 生命周期

完整的任务生命周期管理:

1. 状态管理
   - 任务状态流转
   - 状态变更通知
   - 状态持久化

2. 执行控制
   - 支持暂停/恢复
   - 支持立即执行
   - 支持取消执行

3. 监控告警
   - 执行状态监控
   - 执行时间监控
   - 异常情况告警

## 最佳实践

### 1. 任务设计

1. 任务粒度
   - 合理划分任务
   - 避免任务过大
   - 保持单一职责

2. 执行策略
   - 设置合理超时
   - 配置重试策略
   - 处理并发情况

3. 参数配置
   - 参数可配置化
   - 避免硬编码
   - 支持动态修改

### 2. 依赖管理

1. 依赖设计
   - 避免循环依赖
   - 控制依赖深度
   - 合理设置超时

2. 异常处理
   - 依赖失败处理
   - 超时处理策略
   - 补偿机制

3. 监控设计
   - 依赖链路监控
   - 执行状态监控
   - 性能监控

### 3. 集群部署

1. 节点管理
   - 节点注册发现
   - 节点健康检查
   - 节点负载均衡

2. 任务分配
   - 任务分片策略
   - 任务负载均衡
   - 任务故障转移

3. 数据同步
   - 任务数据同步
   - 执行记录同步
   - 状态数据同步

## 常见问题

### Q1: 如何处理任务堆积?
A1: 可以采取以下措施:
1. 增加执行线程
2. 优化任务执行逻辑
3. 实现任务分片

### Q2: 如何确保任务按顺序执行?
A2: 可以通过以下方式:
1. 使用任务依赖
2. 配置串行执行
3. 使用任务锁

### Q3: 如何处理任务失败?
A3: 建议采取以下措施:
1. 配置重试策略
2. 实现补偿机制
3. 设置告警通知

## 更新记录

### v1.0.0 (2024-01-01)
- 实现基本的任务调度
- 支持任务分组管理
- 提供任务依赖功能

### v1.1.0 (2024-03-01)
- 优化任务执行引擎
- 添加集群支持
- 完善监控功能 