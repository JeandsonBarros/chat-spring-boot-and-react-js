package com.chat.br.services;

import com.chat.br.dtos.MessageDto;
import com.chat.br.enums.StatusMessage;
import com.chat.br.models.Chat;
import com.chat.br.models.Message;
import com.chat.br.models.UserModel;
import com.chat.br.repository.ChatRepository;
import com.chat.br.repository.MessageRepository;
import com.chat.br.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;

    public ResponseEntity<Object> sendingMessage(MessageDto messageDto){

        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if(messageDto.getEmailRecipient().equals(auth.getName()))
                return new ResponseEntity<>("The sender email and the recipient email cannot be the same.", HttpStatus.BAD_REQUEST);

            var sender = userRepository.findByEmail(auth.getName()).get();
            var recipient =  userRepository.findByEmail(messageDto.getEmailRecipient());

            if(!recipient.isPresent())
                return new ResponseEntity<>("Message recipient does not exist", HttpStatus.NOT_FOUND);

            var asChat1 = chatRepository.findByUser1AndUser2(sender, recipient.get());
            var asChat2 = chatRepository.findByUser1AndUser2(recipient.get(), sender);
            Chat chat;

            if(!asChat1.isPresent() && !asChat2.isPresent()){
                chat = new Chat();
                chat.setUser1(sender);
                chat.setUser2(recipient.get());

            } else if (asChat1.isPresent()) {
                chat = asChat1.get();
            }else {
                chat = asChat2.get();
            }

            var saveChat = chatRepository.save(chat);

            var message = new Message();
            message.setText(messageDto.getText());
            message.setSender(sender);
            message.setRecipient(recipient.get());
            message.setSendDateMessage(LocalDateTime.now());
            message.setChat(saveChat);
            message.setStatusMessage(StatusMessage.SENT);
            var newMessage = messageRepository.save(message);

            return new ResponseEntity<>(newMessage, HttpStatus.CREATED);

        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Error sending message", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> getSentMessages(Pageable paging){

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var sender = userRepository.findByEmail(auth.getName()).get();

            var messages = messageRepository.findBySender(sender, paging);
            return new ResponseEntity<>(messages, HttpStatus.OK);

        }catch (Exception e){
        	System.out.println(e);
            return new ResponseEntity<>("Error fetching messages", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> getIncomingMessages(Pageable paging){

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var sender = userRepository.findByEmail(auth.getName()).get();

            var messages = messageRepository.findByRecipient(sender, paging);
            return new ResponseEntity<>(messages, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>("Error fetching messages", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> getMessagesByRecipient(String recipientEmail, Pageable paging){

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var sender = userRepository.findByEmail(auth.getName()).get();
            var recipient =  userRepository.findByEmail(recipientEmail);

            if(!recipient.isPresent()){
                return new ResponseEntity<>("This sender does not exist", HttpStatus.NOT_FOUND);
            }

            var messages = messageRepository.findBySenderAndRecipientOrSenderAndRecipient(sender, recipient.get(), recipient.get(), sender, paging);
            return new ResponseEntity<>(messages, HttpStatus.OK);

        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Error fetching messages", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> getChats(Pageable paging) {
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var user = userRepository.findByEmail(auth.getName()).get();

            var chats = chatRepository.findByUser1OrUser2(user, user, paging);

            return new ResponseEntity<>(chats, HttpStatus.OK);

        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
