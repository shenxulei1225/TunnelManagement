package com.cheers.framework.job.core.handler;

import com.cheers.framework.job.config.JobProperties;
import com.cheers.framework.job.core.annotations.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 定时任务处理器
 */
@Slf4j
public abstract class JobHandler {

    /**
     * 任务名称
     */
    private final String name;

    /**
     * 任务配置
     */
    private final Job job;

    /**
     * 全局配置
     */
    private final JobProperties properties;

    /**
     * 锁
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 触发器
     */
    private final CronTrigger trigger;

    /**
     * 执行器
     */
    private final ExecutorService executor;

    public JobHandler(Job job, JobProperties properties) {
        this.job = job;
        this.properties = properties;
        this.name = job.name();
        this.trigger = new CronTrigger(job.cron());
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("job-" + name);
            return thread;
        });
    }

    /**
     * 执行任务
     */
    public void execute(String jobName) {
        if (!name.equals(jobName)) {
            return;
        }

        // 如果不允许并发执行且当前有任务在执行，直接返回
        if (!job.concurrent() && !lock.tryLock()) {
            log.info("[execute][任务({})正在执行中,跳过本次执行]", name);
            return;
        }

        // 如果开启了分布式锁,则尝试获取锁
        if (job.distributedLock() && properties.isDistributedLock()) {
            if (!lock.tryLock()) {
                log.info("[execute][任务({})已经被其他服务执行]", name);
                return;
            }
        }

        try {
            // 执行任务
            executeWithRetry();
        } finally {
            // 释放锁
            if (!job.concurrent() || (job.distributedLock() && properties.isDistributedLock())) {
                lock.unlock();
            }
        }
    }

    /**
     * 执行任务(支持重试)
     */
    private void executeWithRetry() {
        int retryCount = job.retryOnFailure() ? job.retryCount() : 0;
        long retryInterval = job.retryInterval();

        for (int i = 0; i <= retryCount; i++) {
            try {
                // 执行任务
                Future<?> future = executor.submit(() -> {
                    try {
                        log.info("[execute][任务({})开始执行]", name);
                        doExecute();
                        log.info("[execute][任务({})执行完成]", name);
                    } catch (Exception e) {
                        log.error("[execute][任务({})执行异常]", name, e);
                        throw new RuntimeException(e);
                    }
                });

                // 等待任务完成或超时
                if (job.timeout() > 0) {
                    future.get(job.timeout(), TimeUnit.SECONDS);
                } else {
                    future.get();
                }
                return;
            } catch (TimeoutException e) {
                log.error("[execute][任务({})执行超时]", name);
                break;
            } catch (Exception e) {
                if (i == retryCount) {
                    log.error("[execute][任务({})重试{}次后仍然失败]", name, retryCount, e);
                } else {
                    log.warn("[execute][任务({})执行失败,{}ms后进行第{}次重试]", name, retryInterval, i + 1);
                    try {
                        Thread.sleep(retryInterval);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 执行任务
     */
    protected abstract void doExecute() throws Exception;

    /**
     * 获取任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取任务配置
     */
    public Job getJob() {
        return job;
    }

    /**
     * 获取触发器
     */
    public CronTrigger getTrigger() {
        return trigger;
    }

    /**
     * 关闭执行器
     */
    public void shutdown() {
        executor.shutdownNow();
    }

} 