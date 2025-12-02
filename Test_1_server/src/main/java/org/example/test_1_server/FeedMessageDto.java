package org.example.test_1_server;

public class FeedMessageDto {
    public int id;
    public int groupId;
    public String title;
    public String content;
    public String pdfPath;
    public int createdBy;
    public String createdAt;

    public FeedMessageDto() {}

    public FeedMessageDto(int id, int groupId, String title, String content,
                          String pdfPath, int createdBy, String createdAt) {
        this.id = id;
        this.groupId = groupId;
        this.title = title;
        this.content = content;
        this.pdfPath = pdfPath;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
}
