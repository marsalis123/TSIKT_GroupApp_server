package org.example.test_1_server;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpleNotificationHandler handler;

    public NotificationService(SimpleNotificationHandler handler) {
        this.handler = handler;
    }

    public void sendToGroup(int groupId, NotificationDto dto) {
        String json = String.format(
                "{\"type\":\"%s\",\"message\":\"%s\",\"groupId\":%d}",
                dto.type.replace("\"", "\\\""),
                dto.message.replace("\"", "\\\""),
                groupId
        );
        System.out.println("WS SEND: " + json);
        handler.broadcast(json);
    }
}
