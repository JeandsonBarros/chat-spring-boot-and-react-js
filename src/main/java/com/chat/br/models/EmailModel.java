package com.chat.br.models;

import com.chat.br.enums.StatusMessage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;


@Entity
@Table(name = "TB_EMAIL")
public class EmailModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID emailId;
    private String emailTo;
    private String subject;
    private int recoveryCode;
    private Long codeExpirationTime;
    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDateTime sendDateEmail;
    private StatusMessage statusMessage;

    public UUID getEmailId() {
        return emailId;
    }

    public void setEmailId(UUID emailId) {
        this.emailId = emailId;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(int recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    public Long getCodeExpirationTime() {
        return codeExpirationTime;
    }

    public void setCodeExpirationTime(Long codeExpirationTime) {
        this.codeExpirationTime = codeExpirationTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendDateEmail() {
        return sendDateEmail;
    }

    public void setSendDateEmail(LocalDateTime sendDateEmail) {
        this.sendDateEmail = sendDateEmail;
    }

    public StatusMessage getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(StatusMessage statusMessage) {
        this.statusMessage = statusMessage;
    }
}
