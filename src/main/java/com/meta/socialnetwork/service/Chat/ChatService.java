package com.meta.socialnetwork.service.Chat;

import com.meta.socialnetwork.model.Chat;
import com.meta.socialnetwork.repository.IChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ChatService  implements IChatService{
    @Autowired
    IChatRepo chatRepo;
    @Override
    public Chat saves(Chat chat) {
        return chatRepo.save(chat);
    }

    @Override
    public Iterable findAll() {
        return chatRepo.findAll();
    }

    @Override
    public Optional findById(Long id) {
        return chatRepo.findById(id);
    }

    @Override
    public void save(Object o) {

    }

    @Override
    public void remove(Long id) {

    }
}
