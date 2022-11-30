package com.chat.br.repository;

import com.chat.br.models.Message;
import com.chat.br.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findBySenderAndRecipientOrSenderAndRecipient(UserModel sender, UserModel recipient, UserModel sender1, UserModel recipient1, Pageable pageable);
    Page<Message> findByRecipient(UserModel recipient, Pageable pageable);
    Page<Message> findBySender(UserModel sender, Pageable pageable);
}
