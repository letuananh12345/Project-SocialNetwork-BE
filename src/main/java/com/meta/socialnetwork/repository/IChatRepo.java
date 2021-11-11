package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IChatRepo extends JpaRepository<Chat, Long> {
}
