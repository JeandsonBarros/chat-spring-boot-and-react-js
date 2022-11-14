package com.chat.br.services;

import com.chat.br.dtos.MessageDto;
import com.chat.br.enums.StatusMessage;
import com.chat.br.models.Message;
import com.chat.br.repository.MessageRepository;
import com.chat.br.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    public Integer sendingMessage(MessageDto messageDto){

        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if(messageDto.getEmailRecipient().equals(auth.getName()))
                return 400;

            var sender = userRepository.findByEmail(auth.getName()).get();
            var recipient =  userRepository.findByEmail(messageDto.getEmailRecipient());

            if(!recipient.isPresent())
                return 404;

            var message = new Message();
            message.setText(messageDto.getText());
            message.setSender(sender);
            message.setRecipient(recipient.get());
            message.setSendDateMessage(LocalDateTime.now());
            message.setStatusMessage(StatusMessage.SENT);

            messageRepository.save(message);

            return 201;

        }catch (Exception e){
            return 500;
        }

    }

    public ResponseEntity<Object> getDialog(String recipientEmail, Pageable paging){

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var sender = userRepository.findByEmail(auth.getName()).get();
            var recipient =  userRepository.findByEmail(recipientEmail);

            if(!recipient.isPresent()){
                return new ResponseEntity<>("This sender does not exist", HttpStatus.NOT_FOUND);
            }

            var messages = messageRepository.findBySenderAndRecipient(sender, recipient.get(), paging);
            return new ResponseEntity<>(messages, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>("Error fetching messages", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> getDialogs(Pageable paging) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var sender = userRepository.findByEmail(auth.getName()).get();

            var messages = messageRepository.findBySender(sender, paging);
            return new ResponseEntity<>(messages, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>("Error fetching messages", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
