package com.cheers.framework.job.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 定时任务配置
 */
@Data
@ConfigurationProperties(prefix = "cheers.job")
public class JobProperties {

    /**
     * 是否开启定时任务
     */
    private boolean enable = true;

    /**
     * 是否开启自动注册定时任务
     */
    private boolean autoRegister = true;

    /**
     * 是否开启分布式锁
     */
    private boolean distributedLock = true;

    /**
     * 分布式锁的超时时间,单位:秒
     */
    private long lockTimeout = 60;

    /**
     * 分布式锁的等待时间,单位:秒
     */
    private long lockWaitTime = 10;

} 