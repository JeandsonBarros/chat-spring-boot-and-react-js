package com.chat.br.controllers;

import com.chat.br.dtos.MessageDto;

import com.chat.br.services.ChatService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.*;


@CrossOrigin
@RestController
@RequestMapping("/chat/")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<Object> getChats(@PageableDefault(page = 0, size = 30, sort = "messages.sendDateMessage") Pageable paging){
        return chatService.getChats(paging);
    }

    @PostMapping
    public ResponseEntity<String> sendingMessage(@RequestBody @Valid MessageDto messageDto){
        return chatService.sendingMessage(messageDto);
    }

    @GetMapping("/sent")
    public ResponseEntity<Object> getSentMessages(@PageableDefault(page = 0, size = 10, sort = "sendDateMessage") Pageable paging){
        return chatService.getSentMessages(paging);
    }

    @GetMapping("/received")
    public ResponseEntity<Object> getIncomingMessages(@PageableDefault(page = 0, size = 10, sort = "sendDateMessage") Pageable paging){

        return chatService.getIncomingMessages(paging);

    }

    @GetMapping("/user/{recipient}")
    public ResponseEntity<Object> getDialogByRecipient(@PageableDefault(page = 0, size = 30, direction = Sort.Direction.DESC, sort = "sendDateMessage") Pageable paging, @PathVariable String recipient){

        return chatService.getMessagesByRecipient(recipient, paging);

    }

}
