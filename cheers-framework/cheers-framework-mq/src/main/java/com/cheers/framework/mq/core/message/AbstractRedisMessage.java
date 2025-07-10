package com.cheers.framework.mq.core.message;

/**
 * Redis 消息抽象基类
 */
public abstract class AbstractRedisMessage {

    /**
     * 获取消息的唯一标识
     * 
     * @return 消息的唯一标识
     */
    public abstract String getMessageId();

    /**
     * 获取消息的频道
     * 
     * @return 消息的频道
     */
    public abstract String getChannel();

} 