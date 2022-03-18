package com.project.chatting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatting.dto.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct // DI 이후 초기화 --> service 보다 먼저 수행
    private void init(){
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom(){ // 전체 목록을 조회
        return new ArrayList<>(chatRooms.values());
    }
    public ChatRoom findRoomById(String roomId){ // id를 이용해 조회
        return chatRooms.get(roomId);
    }
    public ChatRoom createRoom(String roomName){ // 채팅방 생성
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .roomName(roomName)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }
    public <T> void sendMessage(WebSocketSession session, T message){
        // Websocket Session에 Message 전송
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }
        catch(IOException e){
            log.error(e.getMessage(), e);
        }
    }
    
}
