package com.meta.socialnetwork.service.Chat;

import com.meta.socialnetwork.model.Chat;
import com.meta.socialnetwork.service.IService;

public interface IChatService extends IService<Chat> {
    Chat saves(Chat chat);
}
