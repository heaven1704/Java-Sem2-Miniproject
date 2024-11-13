package com.example.datahandlers;

import java.sql.*;
import java.util.*;
import com.example.utils.Tools;

public class CartHandler {
    private Connection connection;

    public CartHandler(String dbFilePath) throws SQLException {

        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath + "?busy_timeout=30000");
        createCartTable();
    }


    private synchronized void createCartTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS cart (entry_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id TEXT, product_id TEXT)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        Tools.print("Cart table created");
    }


    public synchronized void addProduct(String user_id, String product_id) throws SQLException {
        String sql = "INSERT INTO cart (user_id, product_id) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user_id);
        pstmt.setString(2, product_id);
        pstmt.executeUpdate();
        Tools.print("Added product: ");
    }


    public synchronized void removeProduct(int entry_id) throws SQLException {
        String sql = "DELETE FROM cart WHERE entry_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, entry_id);
        pstmt.executeUpdate();
        Tools.print("Removed product: ");
    }


    public int getTotal(String user_id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user_id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1); 
        }
        return 0;
    }


    public List<Map<String, Object>> listProducts(String user_id) throws SQLException {
        String sql = "SELECT * FROM cart WHERE user_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user_id);
        ResultSet rs = pstmt.executeQuery();
        List<Map<String, Object>> products = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> product = new HashMap<>();
            product.put("entry_id", String.valueOf(rs.getInt("entry_id")));
            product.put("user_id", rs.getString("user_id"));
            product.put("product_id", rs.getString("product_id"));
            products.add(product);
        }
        return products;
    }


    public synchronized void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
