package com.simplon.ttm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jakarta.validation.Valid;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${server.host}")
    private String API_HOST;

    /**
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(API_HOST);
        registry.addEndpoint("/wss")
                .setAllowedOriginPatterns(API_HOST);
    }

    /**
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //  Préfixe des @MessageMapping
        registry.setApplicationDestinationPrefixes("/");
        //  Préfixe des destinations abonnées dans le front (template.convertAndSend)
        registry.enableSimpleBroker("/getMessages", "/newMessage", "/deleteMessage", "/updateMessage");
    }
}
