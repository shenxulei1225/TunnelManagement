package com.cheers.framework.websocket.core.sender.redis;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Redis WebSocket 消息
 */
@Data
@Accessors(chain = true)
public class RedisWebSocketMessage {

    /**
     * Session 编号
     */
    private String sessionId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 消息内容
     */
    private String messageContent;
} 