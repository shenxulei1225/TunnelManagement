package com.cheers.framework.websocket.core.sender.local;

import com.cheers.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import com.cheers.framework.websocket.core.session.WebSocketSessionManager;

/**
 * 本地 WebSocket 消息发送器实现类
 */
public class LocalWebSocketMessageSender extends AbstractWebSocketMessageSender {

    public LocalWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        super(sessionManager);
    }

} 