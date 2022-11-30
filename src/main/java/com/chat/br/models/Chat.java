package com.chat.br.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="TB_CHAT")
public class Chat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID chatId;

    @ManyToOne
    @JoinColumn(name = "user_1_user_id")
    private UserModel user1;
    @ManyToOne
    @JoinColumn(name = "user_2_user_id")
    private UserModel user2;
    @OneToMany
    private List<Message> messages = new ArrayList<>();

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UserModel getUser1() {
        return user1;
    }

    public void setUser1(UserModel user1) {
        this.user1 = user1;
    }

    public UserModel getUser2() {
        return user2;
    }

    public void setUser2(UserModel user2) {
        this.user2 = user2;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(Message messages) {
        this.messages.add(messages);
    }
}
