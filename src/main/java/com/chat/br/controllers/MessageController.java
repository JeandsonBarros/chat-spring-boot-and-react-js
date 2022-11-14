package com.chat.br.controllers;

import com.chat.br.dtos.MessageDto;

import com.chat.br.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.*;

@RestController
@RequestMapping("/message/")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<String> sendingMessage(@RequestBody @Valid MessageDto messageDto){

        int status = messageService.sendingMessage(messageDto);

        if(status==201)
            return new ResponseEntity<>("Message sent", HttpStatus.CREATED);
        else if (status==404) {
            return new ResponseEntity<>("Message recipient does not exist", HttpStatus.NOT_FOUND);
        } else if (status==400) {
            return new ResponseEntity<>("The sender email and the recipient email cannot be the same.", HttpStatus.BAD_REQUEST);
        } else{
            return new ResponseEntity<>("Error sending message", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getDialogs(@PageableDefault(page = 0, size = 10, sort = "sendDateMessage") Pageable paging){

        return messageService.getDialogs(paging);

    }

    @GetMapping("/{recipient}")
    public ResponseEntity<Object> getDialog(@PageableDefault(page = 0, size = 10, sort = "sendDateMessage") Pageable paging, @PathVariable String recipient){

        return messageService.getDialog(recipient, paging);

    }

}
