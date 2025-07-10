package com.cheers.framework.job.core.model;

import lombok.Data;

/**
 * 定时任务信息
 */
@Data
public class JobInfo {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务分组
     */
    private String group;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 是否允许并发执行
     */
    private boolean concurrent;

    /**
     * 是否开启分布式锁
     */
    private boolean distributedLock;

    /**
     * 分布式锁的超时时间,单位:秒
     */
    private long lockTimeout;

    /**
     * 分布式锁的等待时间,单位:秒
     */
    private long lockWaitTime;

    /**
     * 任务超时时间,单位:秒
     */
    private long timeout;

    /**
     * 是否在失败时重试
     */
    private boolean retryOnFailure;

    /**
     * 重试次数
     */
    private int retryCount;

    /**
     * 重试间隔,单位:毫秒
     */
    private long retryInterval;

    /**
     * 是否自动启动
     */
    private boolean autoStartup;

    /**
     * 依赖的任务名称
     */
    private String[] dependencies;

    /**
     * 任务处理器
     */
    private Class<?> handlerClass;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 上次执行时间
     */
    private Long lastExecuteTime;

    /**
     * 下次执行时间
     */
    private Long nextExecuteTime;

} 