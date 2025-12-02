package org.example.test_1_server;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    public List<GroupDto> getUserGroups(String username) {
        List<GroupDto> groups = new ArrayList<>();

        try (Connection conn = DBManager.getConnection()) {
            // nájdi id užívateľa podľa username
            String sqlUser = "SELECT id FROM users WHERE username = ?";
            PreparedStatement p = conn.prepareStatement(sqlUser);
            p.setString(1, username);
            ResultSet rs = p.executeQuery();
            int uid = rs.next() ? rs.getInt("id") : -1;

            // skupiny kde je user člen alebo vlastník
            String sql = "SELECT DISTINCT g.* FROM groups g " +
                    "LEFT JOIN group_members m ON g.id = m.group_id " +
                    "WHERE g.owner = ? OR m.user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setInt(2, uid);
            ResultSet groupsRs = pstmt.executeQuery();

            while (groupsRs.next()) {
                groups.add(new GroupDto(
                        groupsRs.getInt("id"),
                        groupsRs.getString("name"),
                        groupsRs.getString("owner")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

    public boolean addGroup(String name, String ownerUsername) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "INSERT INTO groups (name, owner) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, ownerUsername);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteGroup(int groupId) {
        try (Connection conn = DBManager.getConnection()) {

            // najprv zmaž členstvá
            String sqlMembers = "DELETE FROM group_members WHERE group_id = ?";
            try (PreparedStatement pm = conn.prepareStatement(sqlMembers)) {
                pm.setInt(1, groupId);
                pm.executeUpdate();
            }

            // potom samotnú skupinu
            String sqlGroup = "DELETE FROM groups WHERE id = ?";
            try (PreparedStatement pg = conn.prepareStatement(sqlGroup)) {
                pg.setInt(1, groupId);
                pg.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addUserToGroup(int groupId, int userId) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "INSERT OR IGNORE INTO group_members (group_id, user_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeUserFromGroup(int groupId, int userId) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "DELETE FROM group_members WHERE group_id = ? AND user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<UserDto> getGroupMembers(int groupId) {
        List<UserDto> members = new ArrayList<>();
        try (Connection conn = DBManager.getConnection()) {
            String sql = "SELECT u.* FROM users u " +
                    "JOIN group_members gm ON u.id = gm.user_id " +
                    "WHERE gm.group_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, groupId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                members.add(new UserDto(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("age"),
                        rs.getString("photo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }


}
