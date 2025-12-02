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

    public UserDto findUser(String value) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? OR email = ? OR id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            pstmt.setString(2, value);
            try {
                pstmt.setInt(3, Integer.parseInt(value));
            } catch (NumberFormatException ex) {
                pstmt.setInt(3, -1);
            }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean saveUser(UserDto dto) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "UPDATE users SET name = ?, email = ?, age = ?, photo = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.name);
            pstmt.setString(2, dto.email);
            pstmt.setInt(3, dto.age);
            pstmt.setString(4, dto.photoPath);
            pstmt.setString(5, dto.username);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
