package com.chat.br.repository;

import com.chat.br.models.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<EmailModel, UUID>{
    Optional<EmailModel> findByEmailTo(String emailTo);
    Optional<EmailModel> findByEmailToAndRecoveryCode(String emailTo, int recoveryCode);
}
