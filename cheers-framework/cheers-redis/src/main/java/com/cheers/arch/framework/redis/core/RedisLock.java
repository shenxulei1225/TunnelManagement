package com.cheers.arch.framework.redis.core;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁实现
 */
@Slf4j
@Component
public class RedisLock {

    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取分布式锁
     *
     * @param key 锁的key
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 锁标识，用于解锁
     */
    public String lock(String key, long timeout, TimeUnit unit) {
        String value = IdUtil.fastSimpleUUID();
        boolean success = redisUtils.tryLock(key, value, timeout, unit);
        if (success) {
            return value;
        }
        return null;
    }

    /**
     * 释放分布式锁
     *
     * @param key 锁的key
     * @param value 锁标识
     * @return 是否释放成功
     */
    public boolean unlock(String key, String value) {
        if (value == null) {
            return false;
        }
        try {
            return redisUtils.releaseLock(key, value);
        } catch (Exception e) {
            log.error("释放分布式锁失败", e);
            return false;
        }
    }

    /**
     * 获取分布式锁并执行
     *
     * @param key 锁的key
     * @param timeout 超时时间
     * @param unit 时间单位
     * @param runnable 要执行的操作
     * @return 是否执行成功
     */
    public boolean lockAndRun(String key, long timeout, TimeUnit unit, Runnable runnable) {
        String value = lock(key, timeout, unit);
        if (value != null) {
            try {
                runnable.run();
                return true;
            } finally {
                unlock(key, value);
            }
        }
        return false;
    }
} 