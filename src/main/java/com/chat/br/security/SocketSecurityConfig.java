package com.chat.br.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration
//@EnableWebSocketSecurity
public class SocketSecurityConfig {

    //https://www.baeldung.com/spring-security-websockets
    //https://docs.spring.io/spring-security/reference/servlet/integrations/websocket.html#:~:text=WebSockets%20reuse%20the%20same%20authentication,the%20HttpServletRequest%20is%20overridden%20automatically.
    @Bean
    AuthorizationManager<Message<?>> authorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {

        messages
                .simpTypeMatchers(
                        SimpMessageType.CONNECT,
                        SimpMessageType.DISCONNECT,
                        SimpMessageType.OTHER,
                        SimpMessageType.MESSAGE,
                        SimpMessageType.SUBSCRIBE,
                        SimpMessageType.CONNECT_ACK,
                        SimpMessageType.UNSUBSCRIBE
                        ).permitAll()
                .simpSubscribeDestMatchers("/ws-message/**").permitAll()
                .simpSubscribeDestMatchers("/topic/message/**").permitAll()
                .simpDestMatchers("/sendMessage/**").permitAll()

                .anyMessage().authenticated();

        return messages.build();
    }
}
