package com.meta.socialnetwork.controller;

import com.meta.socialnetwork.model.Chat;
import com.meta.socialnetwork.model.Hello;
import com.meta.socialnetwork.service.Chat.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

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
