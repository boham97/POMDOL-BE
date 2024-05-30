package com.pomdol.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.ConcurrentHashMap;
//웹소켓 연결 시 gateway 에서 복호화 후 header에 userId 넣기
//유저 id를 보고 유저아이디와 session 매칭 -> concurrentHashMap
// 웹소켓 연결 끊어 지면 어떤 유저인지 파악

@Component
@Slf4j
public class WebSocketEventListener {
    private final ConcurrentHashMap<String, String> sessionHashMap = new ConcurrentHashMap<>();
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String userId = headerAccessor.getFirstNativeHeader("userId");
        if (userId == null || sessionId == null){
            log.warn("New WebSocket connection established with null userId for session: {}", sessionId);
        }else{
            log.info("New WebSocket connection established: " + userId);
            sessionHashMap.put(sessionId, userId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String closedUserId = sessionHashMap.get(sessionId);
        log.info("WebSocket connection closed: {}", closedUserId);
        if (sessionId == null || closedUserId == null){
            log.warn("New WebSocket disconnection with null sessionId: {} userId: {}", sessionId, closedUserId);
        }else{
            log.info("WebSocket connection closed: {}", closedUserId);
            sessionHashMap.remove(sessionId);
        }
    }
}