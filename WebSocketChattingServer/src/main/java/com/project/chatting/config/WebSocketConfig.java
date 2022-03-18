package com.project.chatting.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker // Stomp 사용을 위해 Broker로 대체

/*
//@EnableWebSocket // WebSocket 활성화
// EndPoint를 /ws/chat으로 설정
// 도메인이 다른 서버에서도 접속이 가능하도록 CORS로 setAllowedOrigins("*") 추가
// ws://localhost:8080/ws/chat으로 연결하여 통신 가능
// WebSocket의 경우 http가 아닌 ws로 시작하는 별개의 프로토콜을 사용
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
    }
}
 */
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        // 메세지 publishing의 prefix는 /pub
        // 메세지 subscribing의 prefiex는 /sub
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // stomp websocket의 connection endpoint를 /ws-stomp로 설정
        // ws://localhost:8080/ws-stomp로 접속
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();
    }
}