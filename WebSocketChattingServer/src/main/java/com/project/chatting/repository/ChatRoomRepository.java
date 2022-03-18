package com.project.chatting.repository;

import com.project.chatting.dto.ChatRoom;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository // repository 선언
public class ChatRoomRepository {
    // DB나 저장소 대신, 현재는 임시로 채팅방 정보를 Map에 저장
    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct // DI 이후 초기화되어 service보다 먼저 초기화
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom(){
        // 채팅방을 최근에 생성된 것이 앞으로 가도록 정렬하여 반환
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoom findRoomById(String id){
        return chatRoomMap.get(id);
    }

    public ChatRoom createChatRoom(String name){
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
