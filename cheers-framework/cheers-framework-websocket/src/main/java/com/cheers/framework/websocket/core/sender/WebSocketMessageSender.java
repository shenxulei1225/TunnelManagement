package com.cheers.framework.websocket.core.sender;

/**
 * WebSocket 消息发送器接口
 */
public interface WebSocketMessageSender {

    /**
     * 发送消息给指定用户
     *
     * @param userType 用户类型
     * @param userId 用户编号
     * @param messageType 消息类型
     * @param messageContent 消息内容
     */
    void send(Integer userType, Long userId, String messageType, String messageContent);

    /**
     * 发送消息给指定会话
     *
     * @param sessionId 会话编号
     * @param userType 用户类型
     * @param userId 用户编号
     * @param messageType 消息类型
     * @param messageContent 消息内容
     */
    void send(String sessionId, Integer userType, Long userId, String messageType, String messageContent);

    /**
     * 发送消息给所有用户
     *
     * @param messageType 消息类型
     * @param messageContent 消息内容
     */
    void sendAll(String messageType, String messageContent);
} 