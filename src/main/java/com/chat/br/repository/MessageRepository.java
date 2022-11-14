package com.chat.br.repository;

import com.chat.br.models.Message;
import com.chat.br.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findBySenderAndRecipient(UserModel sender, UserModel recipient, Pageable pageable);
    Page<Message> findBySender(UserModel sender, Pageable pageable);
}
