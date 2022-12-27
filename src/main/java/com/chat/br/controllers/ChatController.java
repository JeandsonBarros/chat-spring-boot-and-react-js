package com.chat.br.controllers;

import com.chat.br.dtos.MessageDto;

import com.chat.br.models.Message;
import com.chat.br.services.ChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.querydsl.QPageRequest;


@CrossOrigin
@RestController
@RequestMapping("/chat/")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    SimpMessagingTemplate template;
    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload MessageDto textMessageDTO) {
        // receive message from client
        System.out.println(textMessageDTO.getText());
    }
    @SendTo("/topic/message/{email}")
    public Message broadcastMessage(@Payload Message message, @PathVariable String email) {
        System.out.println(email);
        return message;
    }
    @Operation(summary = "List all authenticated user's chats")
    @GetMapping
    public ResponseEntity<Object> getChats(@PageableDefault(page = 0, size = 30, sort = "messages.sendDateMessage") Pageable paging){
        return chatService.getChats(paging);
    }
    @Operation(summary = "Send message to another user")
    @PostMapping
    public ResponseEntity<Object> sendingMessage(@RequestBody @Valid MessageDto messageDto){

        var newMessage = chatService.sendingMessage(messageDto);
        template.convertAndSend("/topic/message/"+messageDto.getEmailRecipient(), newMessage);
        return newMessage;
    }
    @Operation(summary = "Lists all messages sent by the authenticated user")
    @GetMapping("/sent")
    public ResponseEntity<Object> getSentMessages(@PageableDefault(page = 0) Pageable paging){ 	
    	Pageable pageable = PageRequest.of(paging.getPageNumber(), 30, Sort.by(
    		    Order.asc("sendDateMessage")));
    	return chatService.getSentMessages(pageable);
    }

    @Operation(summary = "Lists all messages received by the authenticated user")
    @GetMapping("/received")
    public ResponseEntity<Object> getIncomingMessages(@PageableDefault(page = 0) Pageable paging){
    	Pageable pageable = PageRequest.of(paging.getPageNumber(), 30, Sort.by(
    		    Order.asc("sendDateMessage")));
        return chatService.getIncomingMessages(pageable);

    }
    @Operation(summary = "Get chat with another user")
    @GetMapping("/user/{recipient}")
    public ResponseEntity<Object> getDialogByRecipient(@PageableDefault(page = 0, size = 30, direction = Sort.Direction.DESC, sort = "sendDateMessage") Pageable paging, @PathVariable String recipient){

        return chatService.getMessagesByRecipient(recipient, paging);

    }

}
