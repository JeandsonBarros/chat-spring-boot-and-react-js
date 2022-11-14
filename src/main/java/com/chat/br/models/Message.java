package com.chat.br.models;

import com.chat.br.enums.StatusMessage;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
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
    private UserModel sender;
    @ManyToOne
    private UserModel recipient;
}
