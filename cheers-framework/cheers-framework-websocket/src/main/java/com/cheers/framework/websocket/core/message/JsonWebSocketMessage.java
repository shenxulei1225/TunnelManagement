package com.cheers.framework.websocket.core.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * WebSocket 消息
 */
@Data
@Accessors(chain = true)
public class JsonWebSocketMessage {

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 会话编号
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