package com.project.chatting.controller;

import com.project.chatting.dto.ChatRoom;
import com.project.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // Controller 명시
@RequestMapping("/chat") // 요청받을 URI 설정
public class ChatController {
    private final ChatService chatService;

    @PostMapping // HTTP POST
    // URI query parameter 또는 HTML Form data를 RequestParam으로 수신
    public ChatRoom createRoom(@RequestParam String roomName){
        return chatService.createRoom(roomName);
    }
    @GetMapping // HTTP GET
    public List<ChatRoom> findAllRoom(){
        return chatService.findAllRoom();
    }
}
