package com.cheers.framework.websocket.core.sender.redis;

import com.cheers.framework.mq.redis.core.RedisMQTemplate;
import com.cheers.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import com.cheers.framework.websocket.core.session.WebSocketSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

/**
 * 基于 Redis 的 WebSocket 消息发送器实现类
 */
@Slf4j
public class RedisWebSocketMessageSender extends AbstractWebSocketMessageSender {

    private final RedisMQTemplate redisMQTemplate;

    public RedisWebSocketMessageSender(WebSocketSessionManager sessionManager,
                                     RedisMQTemplate redisMQTemplate) {
        super(sessionManager);
        this.redisMQTemplate = redisMQTemplate;
    }

    @Override
    public void send(String sessionId, Integer userType, Long userId, String messageType, String messageContent) {
        // 1. 发送 Redis 消息
        RedisWebSocketMessage mqMessage = new RedisWebSocketMessage()
                .setSessionId(sessionId).setUserType(userType).setUserId(userId)
                .setMessageType(messageType).setMessageContent(messageContent);
        redisMQTemplate.send(mqMessage);

        // 2. 如果是本地 Session，则直接发送
        if (sessionId != null) {
            WebSocketSession session = getSessionManager().getSession(sessionId);
            if (session != null) {
                super.send(sessionId, userType, userId, messageType, messageContent);
            }
            return;
        }
        if (userType != null && userId != null) {
            Collection<WebSocketSession> sessions = getSessionManager().getSessionList(userType, userId);
            if (!sessions.isEmpty()) {
                super.send(sessionId, userType, userId, messageType, messageContent);
            }
            return;
        }
        if (userType != null) {
            Collection<WebSocketSession> sessions = getSessionManager().getSessionList(userType);
            if (!sessions.isEmpty()) {
                super.send(sessionId, userType, userId, messageType, messageContent);
            }
        }
    }

} 