package com.project.chatting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType{
        ENTER, TALK
    }
    private MessageType type; // 메세지 타입(입장 or 대화)
    private String roomId; // 채팅방 번호
    private String sender; // 메세지 보낸 사람
    private String message; // 메세지 내용
}
