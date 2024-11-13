package com.example.controllers;

import org.springframework.web.bind.annotation.*;

import com.example.datahandlers.CartHandler;
import com.example.datahandlers.ProductHandler;
import com.example.datahandlers.UserHandler;

import java.util.Map;
import java.util.List;
import java.util.Collections;

@RestController
@CrossOrigin(origins = "*") // Allow CORS for all routes
@RequestMapping("/api")
public class Controller {

    // Data handlers
    private final UserHandler userHandler;
    private final ProductHandler productHandler;
    private final CartHandler cartHandler;

    // Constructor for initializing handlers
    public Controller() {
        userHandler = initializeUserHandler();
        productHandler = initializeProductHandler();
        cartHandler = initializeCartHandler();
    }

    // Private helper methods to initialize handlers
    private UserHandler initializeUserHandler() {
        try {
            return new UserHandler("database.db");
        } catch (Exception e) {
            System.out.println("Error initializing UserHandler: " + e.toString());
            return null;
        }
    }

    private ProductHandler initializeProductHandler() {
        try {
            return new ProductHandler("database.db");
        } catch (Exception e) {
            System.out.println("Error initializing ProductHandler: " + e.toString());
            return null;
        }
    }

    private CartHandler initializeCartHandler() {
        try {
            return new CartHandler("database.db");
        } catch (Exception e) {
            System.out.println("Error initializing CartHandler: " + e.toString());
            return null;
        }
    }

    // =======================
    // Public API Endpoints
    // =======================
    
    // Greet route
    @GetMapping("/greet")
    public String greet() {
        return "Hello, Welcome to Spring Boot!";
    }

    @GetMapping("/profile/{field}/{value}")
    public Map<String, Object> getProfile(@PathVariable String field, @PathVariable String value) {
        try {
            return userHandler.get_user(field, value);
        } catch (Exception e) {
            System.out.println("Error fetching profile: " + e.toString());
            return Collections.emptyMap();
        }
    }

    // POST route to create a new user
    @PostMapping("/createUser")
    public String createUser(@RequestBody Map<String, String> userData) {
        try {
            String name = userData.get("name");
            String email = userData.get("email");
            String password = userData.get("password");
            String type = userData.get("type");
            String address = userData.get("address");

            userHandler.new_user(name, email, type, password, address);
            return "User created successfully!";
        } catch (Exception e) {
            return "Error creating user: " + e.getMessage();
        }
    }

    // DELETE route to delete a user by ID
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        try {
            userHandler.delete_user(id);
            return "User with ID " + id + " deleted successfully!";
        } catch (Exception e) {
            return "Error deleting user: " + e.getMessage();
        }
    }

    // PUT route to update user data
    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable int id, @RequestBody Map<String, String> updatedData) {
        try {
            String name = updatedData.get("name");
            String password = updatedData.get("password");
            String type = updatedData.get("type");
            String address = updatedData.get("address");

            userHandler.update_user(id, name, password, type, address);
            return "User with ID " + id + " updated successfully!";
        } catch (Exception e) {
            return "Error updating user: " + e.getMessage();
        }
    }

    // Get all products
    @GetMapping("/allProducts")
    public List<Map<String, Object>> allProducts() {
        try {
            return productHandler.getAllProducts();
        } catch (Exception e) {
            System.out.println("Error fetching products: " + e.toString());
            return Collections.emptyList();
        }
    }

    // =======================
    // Cart API Endpoints
    // =======================
    
    // Add a product to the cart for a specific user
    @PostMapping("/cart/add")
    public String addProductToCart(@RequestBody Map<String, String> data) {
        try {
            String userId = data.get("user_id");
            String productId = data.get("product_id");
            cartHandler.addProduct(userId, productId);
            return "Product added to cart successfully!";
        } catch (Exception e) {
            return "Error adding product to cart: " + e.getMessage();
        }
    }

    // Remove a product from the cart by entry_id
    @DeleteMapping("/cart/remove/{entry_id}")
    public String removeProductFromCart(@PathVariable int entry_id) {
        try {
            cartHandler.removeProduct(entry_id);
            return "Product removed from cart successfully!";
        } catch (Exception e) {
            return "Error removing product from cart: " + e.getMessage();
        }
    }

    // Get all cart items for a user
    @GetMapping("/cart/{user_id}")
    public List<Map<String, Object>> getCart(@PathVariable String user_id) {
        try {
            return cartHandler.listProducts(user_id);
        } catch (Exception e) {
            System.out.println("Error fetching cart: " + e.toString());
            return Collections.emptyList();
        }
    }

    // =======================
    // Sign-in API Endpoint
    // =======================
    
    // POST route to handle user sign-in
    @PostMapping("/submitForm")
    public String submitForm(@RequestBody Map<String, String> formData) {
        String email = formData.get("email");
        String password = formData.get("password");

        // Verify user credentials
        try {
            Map<String, Object> user = userHandler.get_user("email", email);
            if (user.isEmpty()) {
                return "User not found";
            }

            // Check if password matches (you should hash passwords in a real-world app)
            if (user.get("password").equals(password)) {
                return "User signed in successfully";
            } else {
                return "Invalid password";
            }
        } catch (Exception e) {
            return "Error during sign-in: " + e.getMessage();
        }
    }
}
