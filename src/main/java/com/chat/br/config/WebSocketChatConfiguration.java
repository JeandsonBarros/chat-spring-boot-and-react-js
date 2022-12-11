package com.chat.br.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketChatConfiguration implements WebSocketMessageBrokerConfigurer {

    //https://umes4ever.medium.com/real-time-application-using-websocket-spring-boot-java-react-js-flutter-eb87fe95f94f

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // with sockjs
        registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*").withSockJS();
        //registry.addEndpoint("/ws-message").setAllowedOrigins("http://localhost:3000").withSockJS();

        // without sockjs
        //registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*");
    }


}
