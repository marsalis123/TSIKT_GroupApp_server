package org.example.test_1_server;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {

    public boolean addFeedMessage(FeedMessageDto msg) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "INSERT INTO feed_messages " +
                    "(group_id, title, content, pdf_path, created_by) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, msg.groupId);
            pstmt.setString(2, msg.title);
            pstmt.setString(3, msg.content);
            pstmt.setString(4, msg.pdfPath);
            pstmt.setInt(5, msg.createdBy);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FeedMessageDto> getFeedMessages(int groupId) {
        List<FeedMessageDto> list = new ArrayList<>();
        try (Connection conn = DBManager.getConnection()) {
            String sql = "SELECT * FROM feed_messages " +
                    "WHERE group_id = ? ORDER BY created_at DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, groupId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new FeedMessageDto(
                        rs.getInt("id"),
                        rs.getInt("group_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("pdf_path"),
                        rs.getInt("created_by"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
