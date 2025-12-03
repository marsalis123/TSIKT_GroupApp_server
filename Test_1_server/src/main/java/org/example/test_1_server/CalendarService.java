package org.example.test_1_server;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    public boolean addEvent(CalendarEventDto e) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "INSERT INTO calendar_events " +
                    "(group_id, created_by, title, description, date, color, notify) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, e.groupId);
            pstmt.setInt(2, e.createdBy);
            pstmt.setString(3, e.title);
            pstmt.setString(4, e.description);
            pstmt.setString(5, e.date);
            pstmt.setString(6, e.color);
            pstmt.setInt(7, e.notify ? 1 : 0);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // všetky eventy pre 1 skupinu a mesiac "YYYY-MM"
    public List<CalendarEventDto> getEventsForGroupMonth(int groupId, String yearMonth) {
        List<CalendarEventDto> events = new ArrayList<>();
        try (Connection conn = DBManager.getConnection()) {
            String sql = "SELECT * FROM calendar_events " +
                    "WHERE group_id = ? AND substr(date,1,7) = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, groupId);
            pstmt.setString(2, yearMonth);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                events.add(mapRow(rs));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return events;
    }

    // všetky eventy pre VŠETKY skupiny usera v danom mesiaci
    public List<CalendarEventDto> getEventsForUsersGroups(List<Integer> groupIds, YearMonth month) {
        List<CalendarEventDto> events = new ArrayList<>();
        if (groupIds == null || groupIds.isEmpty()) return events;

        String ym = month.toString(); // "2025-12"
        try (Connection conn = DBManager.getConnection()) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM calendar_events WHERE substr(date,1,7) = ? AND group_id IN (");
            for (int i = 0; i < groupIds.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append("?");
            }
            sb.append(")");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, ym);
            for (int i = 0; i < groupIds.size(); i++) {
                pstmt.setInt(i + 2, groupIds.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) events.add(mapRow(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return events;
    }

    private CalendarEventDto mapRow(ResultSet rs) throws SQLException {
        return new CalendarEventDto(
                rs.getInt("id"),
                rs.getInt("group_id"),
                rs.getInt("created_by"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("date"),
                rs.getString("color"),
                rs.getInt("notify") != 0,
                rs.getString("created_at")
        );
    }
}
