package com.cheers.framework.redis.core;

import com.cheers.framework.redis.config.CheersCacheProperties;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 支持自定义过期时间的 Redis 缓存管理器
 */
public class TimeoutRedisCacheManager extends RedisCacheManager {

    private final CheersCacheProperties cacheProperties;

    public TimeoutRedisCacheManager(RedisCacheWriter cacheWriter,
                                  RedisCacheConfiguration defaultCacheConfiguration,
                                  CheersCacheProperties cacheProperties) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheProperties = cacheProperties;
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        if (!StringUtils.hasText(name)) {
            return super.createRedisCache(name, cacheConfig);
        }

        // 解析超时时间
        Duration duration = this.parseTimeout(name);
        if (duration == null) {
            return super.createRedisCache(name, cacheConfig);
        }

        // 创建自定义超时时间的 RedisCache
        cacheConfig = cacheConfig.entryTtl(duration);
        return super.createRedisCache(name, cacheConfig);
    }

    private Duration parseTimeout(String name) {
        // # 格式：name#timeout
        // 例如：user#100，表示 user 缓存的超时时间为 100 毫秒
        int idx = name.lastIndexOf('#');
        if (idx == -1) { // 不存在超时时间，则使用全局配置
            return Duration.ofMillis(cacheProperties.getTimeoutMillis());
        }

        // 解析超时时间
        String timeout = name.substring(idx + 1);
        if (!StringUtils.hasText(timeout)) {
            return null;
        }
        try {
            return Duration.ofMillis(Long.parseLong(timeout));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

} 