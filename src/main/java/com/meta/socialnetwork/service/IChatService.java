package com.meta.socialnetwork.service;

import com.meta.socialnetwork.model.Chat;

public interface IChatService extends IService<Chat>{
    Chat saves(Chat chat);
}
