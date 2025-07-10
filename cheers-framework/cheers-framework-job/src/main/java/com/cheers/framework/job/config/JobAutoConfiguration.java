package com.cheers.framework.job.config;

import com.cheers.framework.job.core.annotations.Job;
import com.cheers.framework.job.core.handler.JobHandler;
import com.cheers.framework.job.core.handler.JobHandlerInvoker;
import com.cheers.framework.job.core.model.JobInfo;
import com.cheers.framework.job.core.registry.JobHandlerRegistry;
import com.cheers.framework.job.core.scheduler.SchedulerManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.*;

/**
 * 定时任务自动配置
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(JobProperties.class)
@ConditionalOnProperty(prefix = "cheers.job", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JobAutoConfiguration {

    @Autowired
    private JobProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setAutoStartup(true);
        return schedulerFactoryBean;
    }

    @Bean
    public SchedulerManager schedulerManager(SchedulerFactoryBean schedulerFactoryBean) {
        return new SchedulerManager(schedulerFactoryBean);
    }

    @Bean
    public JobHandlerRegistry jobHandlerRegistry() {
        return new JobHandlerRegistry();
    }

    @PostConstruct
    public void init() throws SchedulerException {
        // 获取所有JobHandler
        Map<String, JobHandler> handlerMap = applicationContext.getBeansOfType(JobHandler.class);
        if (handlerMap.isEmpty()) {
            return;
        }

        // 注册处理器
        JobHandlerRegistry registry = applicationContext.getBean(JobHandlerRegistry.class);
        SchedulerManager schedulerManager = applicationContext.getBean(SchedulerManager.class);

        // 按依赖关系排序
        List<JobHandler> handlers = sortByDependencies(handlerMap.values());

        // 注册并启动任务
        for (JobHandler handler : handlers) {
            Job job = handler.getJob();
            String jobName = job.name();

            // 注册处理器
            registry.registerHandler(jobName, handler);

            // 如果配置了自动启动，则添加任务
            if (job.autoStartup()) {
                JobInfo jobInfo = new JobInfo();
                jobInfo.setName(jobName);
                jobInfo.setGroup(job.group());
                jobInfo.setCron(job.cron());
                jobInfo.setConcurrent(job.concurrent());
                jobInfo.setDistributedLock(job.distributedLock());
                jobInfo.setLockTimeout(job.lockTimeout());
                jobInfo.setLockWaitTime(job.lockWaitTime());
                jobInfo.setTimeout(job.timeout());
                jobInfo.setRetryOnFailure(job.retryOnFailure());
                jobInfo.setRetryCount(job.retryCount());
                jobInfo.setRetryInterval(job.retryInterval());
                jobInfo.setDependencies(job.dependencies());
                jobInfo.setHandlerClass(handler.getClass());

                schedulerManager.addJob(jobInfo);
                log.info("[init][任务({})注册成功]", jobName);
            }
        }
    }

    /**
     * 按依赖关系排序
     */
    private List<JobHandler> sortByDependencies(Collection<JobHandler> handlers) {
        // 构建依赖图
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, JobHandler> handlerMap = new HashMap<>();

        for (JobHandler handler : handlers) {
            String jobName = handler.getJob().name();
            handlerMap.put(jobName, handler);
            graph.put(jobName, new HashSet<>());
            
            String[] dependencies = handler.getJob().dependencies();
            if (dependencies != null) {
                graph.get(jobName).addAll(Arrays.asList(dependencies));
            }
        }

        // 拓扑排序
        List<JobHandler> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();

        for (String jobName : graph.keySet()) {
            if (!visited.contains(jobName)) {
                if (!topologicalSort(jobName, graph, visited, visiting, handlerMap, result)) {
                    log.error("[sortByDependencies][任务依赖存在循环]");
                    // 如果存在循环依赖，返回原始顺序
                    return new ArrayList<>(handlers);
                }
            }
        }

        return result;
    }

    /**
     * 拓扑排序
     */
    private boolean topologicalSort(String jobName, Map<String, Set<String>> graph,
                                  Set<String> visited, Set<String> visiting,
                                  Map<String, JobHandler> handlerMap, List<JobHandler> result) {
        visiting.add(jobName);

        for (String dependency : graph.get(jobName)) {
            if (!handlerMap.containsKey(dependency)) {
                log.warn("[topologicalSort][任务({})的依赖({})不存在]", jobName, dependency);
                continue;
            }

            if (visiting.contains(dependency)) {
                return false; // 存在循环依赖
            }

            if (!visited.contains(dependency)) {
                if (!topologicalSort(dependency, graph, visited, visiting, handlerMap, result)) {
                    return false;
                }
            }
        }

        visiting.remove(jobName);
        visited.add(jobName);
        result.add(handlerMap.get(jobName));
        return true;
    }
} 