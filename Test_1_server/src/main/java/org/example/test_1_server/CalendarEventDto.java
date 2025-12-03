package org.example.test_1_server;

public class CalendarEventDto {
    public int id;
    public int groupId;
    public int createdBy;
    public String title;
    public String description;
    public String date;
    public String color;
    public boolean notify;
    public String createdAt;

    public CalendarEventDto() {}

    public CalendarEventDto(int id, int groupId, int createdBy,
                            String title, String description,
                            String date, String color,
                            boolean notify, String createdAt) {
        this.id = id;
        this.groupId = groupId;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.date = date;
        this.color = color;
        this.notify = notify;
        this.createdAt = createdAt;
    }
}
