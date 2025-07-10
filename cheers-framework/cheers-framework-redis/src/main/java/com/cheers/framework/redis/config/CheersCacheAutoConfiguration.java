package com.cheers.framework.redis.config;

import com.cheers.framework.redis.core.TimeoutRedisCacheManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Objects;

/**
 * Cache 配置类，基于 Redis 实现
 */
@AutoConfiguration
@EnableConfigurationProperties({CacheProperties.class, CheersCacheProperties.class})
@EnableCaching
public class CheersCacheAutoConfiguration {

    /**
     * RedisCacheConfiguration Bean
     *
     * @param cacheProperties 缓存配置
     * @return RedisCacheConfiguration
     */
    @Bean
    @Primary
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        // 设置使用 JSON 序列化方式
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        // 设置 CacheProperties.Redis 的配置
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (Objects.nonNull(redisProperties.getTimeToLive())) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory,
                                             RedisCacheConfiguration redisCacheConfiguration,
                                             CheersCacheProperties cheersCacheProperties) {
        // 创建 RedisCacheWriter 对象
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        // 创建 TenantRedisCacheManager 对象
        return new TimeoutRedisCacheManager(cacheWriter, redisCacheConfiguration, cheersCacheProperties);
    }

} 