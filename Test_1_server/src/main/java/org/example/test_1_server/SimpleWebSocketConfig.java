package org.example.test_1_server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class SimpleWebSocketConfig implements WebSocketConfigurer {

    private final SimpleNotificationHandler handler;

    public SimpleWebSocketConfig(SimpleNotificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws-notify")
                .setAllowedOriginPatterns("*");
    }
}
