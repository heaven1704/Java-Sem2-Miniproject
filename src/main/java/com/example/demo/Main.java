package com.example.demo;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

import com.example.datahandlers.UserHandler;

public class Main {

    public static void main(String[] args) {

        UserHandler userHandler = null;
        try {
            userHandler = new UserHandler("userDatabase.db");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
            return;
        }

        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        while (running) {
            System.out.println("Welcome! Choose an option:");
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    new Thread(() -> {
                        try {
                            signUp(scanner, userHandler);
                        } catch (SQLException e) {
                            System.out.println("An error occurred while signing up.");
                            e.printStackTrace();
                        }
                    }).start();
                    break;

                case 2:
                    new Thread(() -> {
                        try {
                            signIn(scanner, userHandler);
                        } catch (SQLException e) {
                            System.out.println("An error occurred while signing in.");
                            e.printStackTrace();
                        }
                    }).start();
                    break;

                case 3:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        try {
            userHandler.close();
        } catch (SQLException e) {
            System.out.println("Failed to close the database connection.");
        }
        scanner.close();
    }

    private static void signUp(Scanner scanner, UserHandler userHandler) throws SQLException {
        synchronized (userHandler) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter city: ");
            String city = scanner.nextLine();

            System.out.print("Enter user type (farmer/customer): ");
            String userType = scanner.nextLine();

  
            if (!userHandler.getId(email).isEmpty()) {
                System.out.println("Error: A user with this email already exists.");
            } else {
 
                userHandler.new_user(name, email, userType, password, city);
                System.out.println("Sign-up successful! " + name);
            }
        }
    }

    private static void signIn(Scanner scanner, UserHandler userHandler) throws SQLException {
        synchronized (userHandler) {
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
    
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
    
            Map<String, Object> user = userHandler.get_user("email", email);
    

            if (user != null && user.containsKey("password") && user.get("password").equals(password)) {
                System.out.println("Sign-in successful! " + user.get("name"));
            } else {
                System.out.println("Error: Incorrect email or password.");
            }
        }
    }
}