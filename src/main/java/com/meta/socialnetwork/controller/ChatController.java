package com.meta.socialnetwork.controller;

import com.meta.socialnetwork.model.Chat;
import com.meta.socialnetwork.model.Hello;
import com.meta.socialnetwork.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Optional;

@RestController
public class ChatController implements WebSocketMessageBrokerConfigurer {
    @Autowired
    IChatService chatService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    public void greeting(Chat chat) throws Exception {
        System.out.println("chat ==> "+chat);
        chatService.saves(chat);
        simpMessagingTemplate.convertAndSend("/topic/public", new Hello(chat.getName() +" : " + chat.getMessage()));
    }

}
