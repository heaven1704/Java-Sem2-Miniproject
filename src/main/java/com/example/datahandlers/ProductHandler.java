package com.example.datahandlers;

import java.sql.*;
import java.util.*;

public class ProductHandler {
    private Connection connection;

    public ProductHandler(String dbFilePath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath + "?busy_timeout=30000");
        createProductTable();
    }

    private synchronized void createProductTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "product_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "capacity INTEGER, " +      
                "price REAL, " +             
                "storage INTEGER)";       
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }


    public synchronized int addProduct(String name, int capacity, double price, int storage) throws SQLException {
        String sql = "INSERT INTO products (name, capacity, price, storage) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, name);
        pstmt.setInt(2, capacity);
        pstmt.setDouble(3, price);
        pstmt.setInt(4, storage);
        pstmt.executeUpdate();


        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return -1; 
    }


    public int getId(String name) throws SQLException {
        String sql = "SELECT product_id FROM products WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("product_id");
        }
        return -1;
    }


    public String getName(int id) throws SQLException {
        String sql = "SELECT name FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("name");
        }
        return null;
    }


    public double getPrice(int id) throws SQLException {
        String sql = "SELECT price FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getDouble("price");
        }
        return 0.0; 
    }


    public int getCapacity(int id) throws SQLException {
        String sql = "SELECT capacity FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("capacity");
        }
        return 0; 
    }


    public int getStorage(int id) throws SQLException {
        String sql = "SELECT storage FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("storage");
        }
        return 0;
    }


    public synchronized void setPrice(int id, double price) throws SQLException {
        String sql = "UPDATE products SET price = ? WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setDouble(1, price);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }


    public synchronized void setCapacity(int id, int capacity) throws SQLException {
        String sql = "UPDATE products SET capacity = ? WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, capacity);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }


    public synchronized void setStorage(int id, int storage) throws SQLException {
        String sql = "UPDATE products SET storage = ? WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, storage);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }

    public Map<String, Object> getProduct(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Map<String, Object> product = new HashMap<>();
            product.put("product_id", rs.getInt("product_id"));
            product.put("name", rs.getString("name"));
            product.put("capacity", rs.getInt("capacity"));
            product.put("price", rs.getDouble("price"));
            product.put("storage", rs.getInt("storage"));
            return product;
        }
        return null;
    }

 
    public List<Map<String, Object>> getAllProducts() throws SQLException {
        String sql = "SELECT * FROM products";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Map<String, Object>> products = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> product = new HashMap<>();
            product.put("product_id", rs.getInt("product_id"));
            product.put("name", rs.getString("name"));
            product.put("capacity", rs.getInt("capacity"));
            product.put("price", rs.getDouble("price"));
            product.put("storage", rs.getInt("storage"));
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