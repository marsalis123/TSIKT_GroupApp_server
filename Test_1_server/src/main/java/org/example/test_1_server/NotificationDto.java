package org.example.test_1_server;

public class NotificationDto {
    public String type;      // "FEED","JOB","CALENDAR"
    public String message;   // text do zvončeka
    public int groupId;

    public NotificationDto() {}

    public NotificationDto(String type, String message, int groupId) {
        this.type = type;
        this.message = message;
        this.groupId = groupId;
    }
}
