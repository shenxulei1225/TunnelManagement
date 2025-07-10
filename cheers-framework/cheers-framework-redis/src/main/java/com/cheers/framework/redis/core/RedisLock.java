package com.cheers.framework.redis.core;

import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁实现
 */
@Component
public class RedisLock {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 尝试获取锁
     *
     * @param lockKey    锁的key
     * @param waitTime   等待时间
     * @param leaseTime  持有锁的时间
     * @param timeUnit   时间单位
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁的key
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 判断当前线程是否持有锁
     *
     * @param lockKey 锁的key
     * @return 是否持有锁
     */
    public boolean isHeldByCurrentThread(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.isHeldByCurrentThread();
    }

    /**
     * 强制释放锁
     *
     * @param lockKey 锁的key
     */
    public void forceUnlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.forceUnlock();
    }

    /**
     * 获取锁，如果锁不可用则等待
     *
     * @param lockKey   锁的key
     * @param leaseTime 持有锁的时间
     * @param timeUnit  时间单位
     */
    public void lock(String lockKey, long leaseTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, timeUnit);
    }

    /**
     * 获取锁
     *
     * @param lockKey 锁的key
     */
    public void lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
    }

} 