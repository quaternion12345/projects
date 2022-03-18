package com.project.chatting.dto;

import com.project.chatting.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {
    private String roomId; // 채팅방 번호
    private String roomName; // 채팅방 이름
    // 구독자 관리가 자동으로 수행되므로
    // WebSocket Session을 관리할 필요가 없음
    public static ChatRoom create(String roomName){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        return chatRoom;
    }
/*    private Set<WebSocketSession> sessions = new HashSet<>(); // 클라이언트 정보 목록

    @Builder
    public ChatRoom(String roomId, String roomName){
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService){
        if(chatMessage.getType().equals(ChatMessage.MessageType.ENTER)){
            // 입장인 경우
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        }
        // 메세지를 채팅방에 전송
        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService){
        // 병렬처리로 메세지 전송
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
*/
}

