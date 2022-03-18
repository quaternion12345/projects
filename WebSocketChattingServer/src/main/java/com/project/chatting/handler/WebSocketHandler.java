package com.project.chatting.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatting.dto.ChatMessage;
import com.project.chatting.dto.ChatRoom;
import com.project.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component // Bean 생성
// 한 서버에 여러 클라이언트가 접속하므로 이 메세지를 처리할 Handler
// Console에 log로 Message를 출력하고 Client에게 답을 전달
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
        log.info("payload {}", payload);
//        TextMessage textMessage = new TextMessage("Welcome chatting server");
//        session.sendMessage(textMessage);
        // WebSocketCleint로부터 전달받은 ChatMessage payload를 ChatMessage로 변환
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        // ChatMessage로부터 채팅방 번호를 얻어 채팅방을 조회
        ChatRoom chatRoom = chatService.findRoomById(chatMessage.getRoomId());
        // 해당 채팅방에 입장한 모든 클라이언트(WebSocketSession)에 ChatMessage 발송
        chatRoom.handleActions(session, chatMessage, chatService);
    }
}
