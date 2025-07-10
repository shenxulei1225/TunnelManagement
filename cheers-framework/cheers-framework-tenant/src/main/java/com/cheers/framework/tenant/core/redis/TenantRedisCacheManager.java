package com.cheers.framework.tenant.core.redis;

import cn.hutool.core.collection.CollUtil;
import com.cheers.framework.tenant.config.TenantProperties;
import com.cheers.framework.tenant.core.context.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 多租户的Redis缓存管理器
 * 1. 支持多租户下的缓存隔离
 * 2. 支持忽略特定缓存的租户隔离
 */
@Slf4j
public class TenantRedisCacheManager extends RedisCacheManager {

    private final TenantProperties properties;
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;

    public TenantRedisCacheManager(RedisCacheWriter cacheWriter,
                                 RedisCacheConfiguration defaultCacheConfiguration,
                                 TenantProperties properties) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.properties = properties;
    }

    @Override
    public Cache getCache(String name) {
        // 如果是忽略租户的缓存,直接返回
        if (isIgnoreCache(name)) {
            return super.getCache(name);
        }

        // 获取租户ID
        Long tenantId = getTenantId();
        if (tenantId == null) {
            return super.getCache(name);
        }

        // 生成带租户的缓存名
        String tenantCacheName = generateTenantCacheName(name, tenantId);
        // 创建或获取缓存
        Cache cache = super.getCache(tenantCacheName);
        if (cache != null) {
            return cache;
        }

        // 创建新的缓存
        return createTenantCache(name, tenantCacheName);
    }

    @Override
    public Collection<String> getCacheNames() {
        Set<String> cacheNames = new HashSet<>(super.getCacheNames());
        return Collections.unmodifiableCollection(cacheNames);
    }

    /**
     * 判断是否为忽略租户的缓存
     */
    private boolean isIgnoreCache(String name) {
        Set<String> ignoreCaches = properties.getIgnoreCaches();
        return TenantContextHolder.isIgnore() || 
               (ignoreCaches != null && ignoreCaches.contains(name));
    }

    /**
     * 获取当前租户ID
     */
    private Long getTenantId() {
        return TenantContextHolder.isAllowCrossTenant() ? 
               TenantContextHolder.getVisitTenantId() : 
               TenantContextHolder.getTenantId();
    }

    /**
     * 生成带租户的缓存名
     */
    private String generateTenantCacheName(String name, Long tenantId) {
        return name + ":tenant:" + tenantId;
    }

    /**
     * 创建租户缓存
     */
    private Cache createTenantCache(String name, String tenantCacheName) {
        log.debug("[createTenantCache][创建租户缓存,name:{},tenantCacheName:{}]", name, tenantCacheName);
        return new TenantRedisCache(tenantCacheName, cacheWriter, defaultCacheConfig);
    }

} 