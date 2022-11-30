package com.chat.br.repository;

import com.chat.br.models.Chat;
import com.chat.br.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Optional<Chat> findByUser1AndUser2(UserModel user1, UserModel user2);
    Page<Chat> findByUser1OrUser2(UserModel user1, UserModel user2, Pageable pageable);
    Optional<Chat> findByChatIdAndUser1AndUser2(UUID chatId, UserModel user1, UserModel user2);
}
