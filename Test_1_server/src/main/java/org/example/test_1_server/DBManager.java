package org.example.test_1_server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static final String DB_URL = "jdbc:sqlite:users.db"; // rovnaký súbor ako klient

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void createTables() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, " +
                    "password TEXT NOT NULL, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "age INTEGER, " +
                    "photo TEXT, " +
                    "id INTEGER" +
                    ")";
            stmt.executeUpdate(sql);

            // NEW: groups
            String sqlGroups = "CREATE TABLE IF NOT EXISTS groups (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "owner TEXT NOT NULL" +
                    ")";
            stmt.executeUpdate(sqlGroups);

            // NEW: group_members
            String sqlGroupMembers = "CREATE TABLE IF NOT EXISTS group_members (" +
                    "group_id INTEGER NOT NULL, " +
                    "user_id INTEGER NOT NULL, " +
                    "PRIMARY KEY (group_id, user_id)" +
                    ")";
            stmt.executeUpdate(sqlGroupMembers);

            String sqlFeed = "CREATE TABLE IF NOT EXISTS feed_messages (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "group_id INTEGER NOT NULL, " +
                    "title TEXT NOT NULL, " +
                    "content TEXT, " +
                    "pdf_path TEXT, " +
                    "created_by INTEGER NOT NULL, " +
                    "created_at TEXT DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(sqlFeed);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
