package com.chat.br.models;

import com.chat.br.enums.StatusMessage;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "TB_MESSAGE")
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID messageId;
    @Column(columnDefinition = "TEXT")
    private String text;
    private StatusMessage statusMessage;
    private LocalDateTime sendDateMessage;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserModel sender;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserModel recipient;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Chat chat;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StatusMessage getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(StatusMessage statusMessage) {
        this.statusMessage = statusMessage;
    }

    public LocalDateTime getSendDateMessage() {
        return sendDateMessage;
    }

    public void setSendDateMessage(LocalDateTime sendDateMessage) {
        this.sendDateMessage = sendDateMessage;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public UserModel getRecipient() {
        return recipient;
    }

    public void setRecipient(UserModel recipient) {
        this.recipient = recipient;
    }
}
