package org.example.test_1_server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.Random;

@Service
public class UserService {


    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean register(UserDto dto) {
        try (Connection conn = DBManager.getConnection()) {

            int publicId = generateRandomPublicId(conn);

            String sql = "INSERT INTO users (public_id, username, password, name, email, age, photo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, publicId);
            pstmt.setString(2, dto.username);
            pstmt.setString(3, passwordEncoder.encode(dto.password));
            pstmt.setString(4, dto.name);
            pstmt.setString(5, dto.email);
            pstmt.setInt(6, dto.age);
            pstmt.setString(7, dto.photoPath);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public UserDto login(String username, String rawPassword) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (storedHash != null && passwordEncoder.matches(rawPassword, storedHash)) {
                    return new UserDto(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getString("photo")
                    );
                }
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
    private int generateRandomPublicId(Connection conn) throws SQLException {
        java.util.Random rnd = new java.util.Random();

        while (true) {
            int candidate = 100000 + rnd.nextInt(900000); // 100000–999999

            String checkSql = "SELECT 1 FROM users WHERE public_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, candidate);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return candidate; // ešte neexistuje
                    }
                }
            }
            // ak existuje, vygenerujeme ďalšie číslo
        }
    }


}
