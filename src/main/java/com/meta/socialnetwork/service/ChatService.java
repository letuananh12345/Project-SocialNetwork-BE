package com.meta.socialnetwork.service;

import com.meta.socialnetwork.model.Chat;
import com.meta.socialnetwork.repository.IChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ChatService implements IChatService{
    @Autowired
    IChatRepo chatRepo;
    @Override
    public Iterable<Chat> findAll() {
        return chatRepo.findAll();
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return chatRepo.findById(id);
    }

    @Override
    public void save(Chat chat) {

    }

    @Override
    public void remove(Long id) {

    }

    public Chat saves(Chat chat){
        return chatRepo.save(chat);
    }
}
