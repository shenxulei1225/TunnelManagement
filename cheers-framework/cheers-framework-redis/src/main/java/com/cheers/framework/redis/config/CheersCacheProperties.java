package com.cheers.framework.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cache 配置属性类
 */
@ConfigurationProperties(prefix = "cheers.cache")
public class CheersCacheProperties {

    /**
     * 缓存过期时间，单位：毫秒
     */
    private int timeoutMillis = 60 * 1000; // 默认 1 分钟

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public CheersCacheProperties setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
        return this;
    }

} 