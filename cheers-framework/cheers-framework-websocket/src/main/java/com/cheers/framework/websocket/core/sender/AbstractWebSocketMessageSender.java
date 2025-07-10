package com.cheers.framework.websocket.core.sender;

import com.cheers.framework.websocket.core.message.JsonWebSocketMessage;
import com.cheers.framework.websocket.core.session.WebSocketSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket 消息发送器的抽象类
 */
@Slf4j
public abstract class AbstractWebSocketMessageSender implements WebSocketMessageSender {

    protected final WebSocketSessionManager sessionManager;
    protected final ObjectMapper objectMapper;

    public AbstractWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void send(Integer userType, Long userId, String messageType, String messageContent) {
        // 获得用户的 Session
        WebSocketSession session = sessionManager.getSession(userId);
        if (session == null) {
            log.error("[send][userId({}) 不存在对应的 session]", userId);
            return;
        }
        // 发送消息
        send(session.getId(), userType, userId, messageType, messageContent);
    }

    @Override
    public void send(String sessionId, Integer userType, Long userId, String messageType, String messageContent) {
        // 创建消息
        JsonWebSocketMessage message = new JsonWebSocketMessage()
                .setSessionId(sessionId)
                .setUserType(userType)
                .setUserId(userId)
                .setMessageType(messageType)
                .setMessageContent(messageContent);
        // 序列化消息
        try {
            String messageText = objectMapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(messageText);
            // 发送消息
            WebSocketSession session = sessionManager.getSession(sessionId);
            if (session == null) {
                log.error("[send][sessionId({}) 不存在对应的 session]", sessionId);
                return;
            }
            session.sendMessage(textMessage);
        } catch (Exception e) {
            log.error("[send][sessionId({}) 发送消息失败]", sessionId, e);
        }
    }

    @Override
    public void sendAll(String messageType, String messageContent) {
        // 获得所有 Session
        for (WebSocketSession session : sessionManager.getSessions()) {
            // 发送消息
            send(session.getId(), null, null, messageType, messageContent);
        }
    }
} 