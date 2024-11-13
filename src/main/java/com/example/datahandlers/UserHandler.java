package com.example.datahandlers;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.utils.Tools;

public class UserHandler {
    private Connection connection;

    public UserHandler(String dbFilePath) throws SQLException {
 
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath + "?busy_timeout=30000");
        createUserTable();
    }

    private synchronized void createUserTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS user (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +  
                "password TEXT NOT NULL, " +
                "city TEXT NOT NULL" +  
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            Tools.print("User table created");
        }
    }

    public synchronized void new_user(String name, String email, String type, String password, String city) throws SQLException {
        String insertSQL = "INSERT INTO user (name, email, type, password, city) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, type);
            pstmt.setString(4, password);
            pstmt.setString(5, city); 
            pstmt.executeUpdate();
            Tools.print("User added " + name + " " + email + " " + type + " " + password + " " + city);
        }
    }

    public Map<String, Object> get_user(String field, String value) throws SQLException {
        Map<String, Object> userMap = new HashMap<>();
        String selectSQL = "SELECT * FROM user WHERE " + field + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userMap.put("user_id", String.valueOf(rs.getInt("user_id")));
                userMap.put("name", rs.getString("name"));
                userMap.put("email", rs.getString("email"));
                userMap.put("type", rs.getString("type"));
                userMap.put("city", rs.getString("city"));  
            }
        }
        return userMap;
    }

    public String getEmail(String id) throws SQLException {
        try {
            Map<String, Object> value = get_user("user_id", id);
            return (String) value.get("email");
        } catch (Exception e) {
            return "";
        }
    }

    public String getId(String email) throws SQLException {
        try {
            Map<String, Object> value = get_user("email", email);
            return (String) value.get("user_id");
        } catch (Exception e) {
            return "";
        }
    }

    public String getName(String id) throws SQLException {
        try {
            Map<String, Object> value = get_user("user_id", id);
            return (String) value.get("name");
        } catch (Exception e) {
            return "";
        }
    }

    public List<Map<String, String>> get_all_users() throws SQLException {
        List<Map<String, String>> usersList = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM user";

        try (PreparedStatement pstmt = connection.prepareStatement(selectAllSQL);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> userMap = new HashMap<>();
                userMap.put("user_id", String.valueOf(rs.getInt("user_id")));
                userMap.put("name", rs.getString("name"));
                userMap.put("email", rs.getString("email"));
                userMap.put("type", rs.getString("type"));
                userMap.put("password", rs.getString("password"));
                userMap.put("city", rs.getString("city"));  // Change address to city
                usersList.add(userMap);
            }
        }
        return usersList;
    }

    public synchronized void delete_user(int userId) throws SQLException {
        String deleteSQL = "DELETE FROM user WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            Tools.print("User deleted: " + userId);
        }
    }

    public synchronized void update_user(int userId, String name, String password, String type, String city) throws SQLException {
        StringBuilder updateSQL = new StringBuilder("UPDATE user SET ");
        boolean first = true;

        if (name != null) {
            updateSQL.append("name = ? ");
            first = false;
        }

        if (password != null) {
            if (!first) updateSQL.append(", ");
            updateSQL.append("password = ? ");
            first = false;
        }

        if (type != null) {
            if (!first) updateSQL.append(", ");
            updateSQL.append("type = ? ");
            first = false;
        }

        if (city != null) {  // Change address to city
            if (!first) updateSQL.append(", ");
            updateSQL.append("city = ? ");
        }

        updateSQL.append("WHERE user_id = ?");

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL.toString())) {
            int paramIndex = 1;

            if (name != null) pstmt.setString(paramIndex++, name);
            if (password != null) pstmt.setString(paramIndex++, password);
            if (type != null) pstmt.setString(paramIndex++, type);
            if (city != null) pstmt.setString(paramIndex++, city);

            pstmt.setInt(paramIndex, userId);
            pstmt.executeUpdate();
        }
    }

    public synchronized void close() throws SQLException {

        if (connection != null) {
            connection.close();
        }
    }
}
