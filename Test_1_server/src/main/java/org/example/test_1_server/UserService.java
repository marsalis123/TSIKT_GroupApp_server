package org.example.test_1_server;



import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Random;

@Service
public class UserService {

    public boolean register(String username, String password) {
        try (Connection conn = DBManager.getConnection()) {
            Random rand = new Random();
            int newId = rand.nextInt(90000) + 10000;
            String sql = "INSERT INTO users (username, password, name, email, age, photo, id) " +
                    "VALUES (?, ?, '', '', 0, '', ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, newId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            // napr. duplicate username
            e.printStackTrace();
            return false;
        }
    }

    public UserDto login(String username, String password) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new UserDto(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("age"),
                        rs.getString("photo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
