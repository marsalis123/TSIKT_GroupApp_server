package org.example.test_1_server;

public class JobLogDto {
    public int id;
    public int jobId;
    public int userId;
    public String workText;
    public String commitMsg;
    public String pdfPath;
    public String createdAt;
    public String authorName; // naplníme joinom s users

    public JobLogDto() {}

    public JobLogDto(int id, int jobId, int userId,
                     String workText, String commitMsg,
                     String pdfPath, String createdAt,
                     String authorName) {
        this.id = id;
        this.jobId = jobId;
        this.userId = userId;
        this.workText = workText;
        this.commitMsg = commitMsg;
        this.pdfPath = pdfPath;
        this.createdAt = createdAt;
        this.authorName = authorName;
    }
}
