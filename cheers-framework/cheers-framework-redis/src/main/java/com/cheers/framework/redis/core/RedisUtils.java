package com.cheers.framework.redis.core;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 */
@Component
public class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     *
     * @param key   缓存key
     * @param value 缓存值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存，带过期时间
     *
     * @param key      缓存key
     * @param value    缓存值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置缓存，带过期时间
     *
     * @param key      缓存key
     * @param value    缓存值
     * @param duration 过期时间
     */
    public void set(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存key
     * @return 是否删除成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除缓存
     *
     * @param keys 缓存key集合
     * @return 成功删除的数量
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 设置过期时间
     *
     * @param key      缓存key
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 设置过期时间
     *
     * @param key      缓存key
     * @param duration 过期时间
     * @return 是否设置成功
     */
    public Boolean expire(String key, Duration duration) {
        return redisTemplate.expire(key, duration);
    }

    /**
     * 获取过期时间
     *
     * @param key 缓存key
     * @return 过期时间（秒）
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key 缓存key
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 递增
     *
     * @param key   缓存key
     * @param delta 递增值
     * @return 递增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   缓存key
     * @param delta 递减值
     * @return 递减后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 获取List中指定范围的元素
     *
     * @param key   缓存key
     * @param start 开始位置
     * @param end   结束位置
     * @return 元素列表
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取List长度
     *
     * @param key 缓存key
     * @return List长度
     */
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取List中指定索引的元素
     *
     * @param key   缓存key
     * @param index 索引
     * @return 元素
     */
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 将元素添加到List
     *
     * @param key   缓存key
     * @param value 元素
     * @return List长度
     */
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 批量将元素添加到List
     *
     * @param key    缓存key
     * @param values 元素集合
     * @return List长度
     */
    public Long lPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 将元素添加到List（如果List存在）
     *
     * @param key   缓存key
     * @param value 元素
     * @return List长度
     */
    public Long lPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 从List中移除并获取第一个元素
     *
     * @param key 缓存key
     * @return 元素
     */
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从List中移除并获取第一个元素（阻塞版）
     *
     * @param key      缓存key
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     * @return 元素
     */
    public Object lBPop(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForList().leftPop(key, timeout, timeUnit);
    }

} 