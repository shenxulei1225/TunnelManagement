package com.cheers.framework.job.core.annotations;

import java.lang.annotation.*;

/**
 * 定时任务注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Job {

    /**
     * 任务名称
     */
    String name();

    /**
     * cron表达式
     */
    String cron();

    /**
     * 任务分组
     */
    String group() default "default";

    /**
     * 是否允许并发执行
     */
    boolean concurrent() default false;

    /**
     * 是否开启分布式锁
     */
    boolean distributedLock() default true;

    /**
     * 分布式锁的超时时间,单位:秒
     */
    long lockTimeout() default 60;

    /**
     * 分布式锁的等待时间,单位:秒
     */
    long lockWaitTime() default 10;

    /**
     * 任务超时时间,单位:秒
     * 默认不超时
     */
    long timeout() default 0;

    /**
     * 是否在失败时重试
     */
    boolean retryOnFailure() default false;

    /**
     * 重试次数
     * 仅当retryOnFailure为true时生效
     */
    int retryCount() default 3;

    /**
     * 重试间隔,单位:毫秒
     * 仅当retryOnFailure为true时生效
     */
    long retryInterval() default 1000;

    /**
     * 是否自动启动
     */
    boolean autoStartup() default true;

    /**
     * 依赖的任务名称
     * 当依赖的任务执行完成后，才会执行本任务
     */
    String[] dependencies() default {};

} 