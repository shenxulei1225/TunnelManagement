package com.cheers.framework.websocket.core.sender.kafka;

import com.cheers.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import com.cheers.framework.websocket.core.session.WebSocketSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

/**
 * 基于 Kafka 的 WebSocket 消息发送器实现类
 */
@Slf4j
public class KafkaWebSocketMessageSender extends AbstractWebSocketMessageSender {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final String topic;

    public KafkaWebSocketMessageSender(WebSocketSessionManager sessionManager,
                                     KafkaTemplate<Object, Object> kafkaTemplate,
                                     String topic) {
        super(sessionManager);
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void send(String sessionId, Integer userType, Long userId, String messageType, String messageContent) {
        // 1. 发送 Kafka 消息
        KafkaWebSocketMessage mqMessage = new KafkaWebSocketMessage()
                .setSessionId(sessionId).setUserType(userType).setUserId(userId)
                .setMessageType(messageType).setMessageContent(messageContent);
        kafkaTemplate.send(topic, mqMessage);

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