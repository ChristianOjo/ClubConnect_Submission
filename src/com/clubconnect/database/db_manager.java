package com.clubconnect.database;

import java.sql.*;
import java.io.*;
import java.util.Properties;

/**
 * Manages database connections and operations for ClubConnect
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "clubconnect_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    private static Connection connection;
    
    /**
     * Establishes connection to MySQL database
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
        }
        return connection;
    }
    
    /**
     * Initializes database and creates tables if they don't exist
     */
    public static void initializeDatabase() {
        try {
            // Create database if not exists
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            conn.close();
            
            // Connect to the database
            connection = getConnection();
            
            // Create tables
            createTables();
            
            // Import data if exists
            importDatabaseFromFile();
            
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates all required tables
     */
    private static void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        
        // Users table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS users (" +
            "user_id INT AUTO_INCREMENT PRIMARY KEY," +
            "student_id VARCHAR(50) UNIQUE NOT NULL," +
            "username VARCHAR(100) NOT NULL," +
            "email VARCHAR(100) UNIQUE NOT NULL," +
            "password VARCHAR(255) NOT NULL," +
            "role ENUM('ADMIN', 'LEADER', 'MEMBER', 'GUEST') DEFAULT 'MEMBER'," +
            "contact_number VARCHAR(20)," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE'" +
            ")"
        );
        
        // Clubs table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS clubs (" +
            "club_id INT AUTO_INCREMENT PRIMARY KEY," +
            "name VARCHAR(200) NOT NULL," +
            "category VARCHAR(100)," +
            "mission_statement TEXT," +
            "description TEXT," +
            "created_by INT," +
            "initial_budget DECIMAL(10,2) DEFAULT 0," +
            "current_budget DECIMAL(10,2) DEFAULT 0," +
            "status ENUM('PENDING', 'ACTIVE', 'INACTIVE', 'ARCHIVED') DEFAULT 'PENDING'," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (created_by) REFERENCES users(user_id)" +
            ")"
        );
        
        // Club Members table (junction table)
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS club_members (" +
            "membership_id INT AUTO_INCREMENT PRIMARY KEY," +
            "club_id INT," +
            "user_id INT," +
            "role ENUM('LEADER', 'MEMBER') DEFAULT 'MEMBER'," +
            "status ENUM('PENDING', 'ACTIVE', 'ALUMNI') DEFAULT 'PENDING'," +
            "joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (club_id) REFERENCES clubs(club_id) ON DELETE CASCADE," +
            "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
            "UNIQUE KEY unique_membership (club_id, user_id)" +
            ")"
        );
        
        // Events table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS events (" +
            "event_id INT AUTO_INCREMENT PRIMARY KEY," +
            "club_id INT," +
            "title VARCHAR(200) NOT NULL," +
            "description TEXT," +
            "event_date DATE NOT NULL," +
            "start_time TIME NOT NULL," +
            "end_time TIME NOT NULL," +
            "venue VARCHAR(200)," +
            "capacity INT," +
            "event_type ENUM('MEETING', 'SEATING', 'EVENT') DEFAULT 'EVENT'," +
            "budget_requested BOOLEAN DEFAULT FALSE," +
            "budget_amount DECIMAL(10,2)," +
            "budget_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING'," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (club_id) REFERENCES clubs(club_id) ON DELETE CASCADE" +
            ")"
        );
        
        // RSVPs table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS rsvps (" +
            "rsvp_id INT AUTO_INCREMENT PRIMARY KEY," +
            "event_id INT," +
            "user_id INT," +
            "status ENUM('ATTENDING', 'NOT_ATTENDING', 'WAITLIST') DEFAULT 'ATTENDING'," +
            "attended BOOLEAN DEFAULT FALSE," +
            "rsvp_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE," +
            "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
            "UNIQUE KEY unique_rsvp (event_id, user_id)" +
            ")"
        );
        
        // Resources table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS resources (" +
            "resource_id INT AUTO_INCREMENT PRIMARY KEY," +
            "resource_name VARCHAR(200) NOT NULL," +
            "resource_type VARCHAR(100)," +
            "capacity INT," +
            "status ENUM('AVAILABLE', 'UNAVAILABLE') DEFAULT 'AVAILABLE'" +
            ")"
        );
        
        // Resource Bookings table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS resource_bookings (" +
            "booking_id INT AUTO_INCREMENT PRIMARY KEY," +
            "event_id INT," +
            "resource_id INT," +
            "booking_date DATE," +
            "start_time TIME," +
            "end_time TIME," +
            "status ENUM('CONFIRMED', 'CANCELLED') DEFAULT 'CONFIRMED'," +
            "FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE," +
            "FOREIGN KEY (resource_id) REFERENCES resources(resource_id)" +
            ")"
        );
        
        // Budget Transactions table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS budget_transactions (" +
            "transaction_id INT AUTO_INCREMENT PRIMARY KEY," +
            "club_id INT," +
            "event_id INT NULL," +
            "transaction_type ENUM('INCOME', 'EXPENSE') NOT NULL," +
            "amount DECIMAL(10,2) NOT NULL," +
            "category VARCHAR(100)," +
            "description TEXT," +
            "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "approved_by INT," +
            "FOREIGN KEY (club_id) REFERENCES clubs(club_id) ON DELETE CASCADE," +
            "FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE SET NULL," +
            "FOREIGN KEY (approved_by) REFERENCES users(user_id)" +
            ")"
        );
        
        // Announcements table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS announcements (" +
            "announcement_id INT AUTO_INCREMENT PRIMARY KEY," +
            "club_id INT," +
            "title VARCHAR(200) NOT NULL," +
            "content TEXT," +
            "created_by INT," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (club_id) REFERENCES clubs(club_id) ON DELETE CASCADE," +
            "FOREIGN KEY (created_by) REFERENCES users(user_id)" +
            ")"
        );
        
        // Discussion Board table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS discussions (" +
            "discussion_id INT AUTO_INCREMENT PRIMARY KEY," +
            "club_id INT," +
            "event_id INT NULL," +
            "user_id INT," +
            "parent_id INT NULL," +
            "content TEXT," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (club_id) REFERENCES clubs(club_id) ON DELETE CASCADE," +
            "FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE," +
            "FOREIGN KEY (user_id) REFERENCES users(user_id)," +
            "FOREIGN KEY (parent_id) REFERENCES discussions(discussion_id) ON DELETE CASCADE" +
            ")"
        );
        
        // Notifications table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS notifications (" +
            "notification_id INT AUTO_INCREMENT PRIMARY KEY," +
            "user_id INT," +
            "title VARCHAR(200)," +
            "message TEXT," +
            "notification_type VARCHAR(50)," +
            "is_read BOOLEAN DEFAULT FALSE," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
            ")"
        );
        
        stmt.close();
    }
    
    /**
     * Imports database from backup file if exists
     */
    private static void importDatabaseFromFile() {
        File backupFile = new File("database_backup.sql");
        if (backupFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
                Statement stmt = connection.createStatement();
                StringBuilder sql = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    sql.append(line);
                    if (line.trim().endsWith(";")) {
                        stmt.executeUpdate(sql.toString());
                        sql = new StringBuilder();
                    }
                }
                stmt.close();
                System.out.println("Database imported from backup file.");
            } catch (Exception e) {
                System.err.println("Error importing database: " + e.getMessage());
            }
        }
    }
    
    /**
     * Exports database to backup file
     */
    public static void exportDatabase() {
        Thread exportThread = new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                    "mysqldump",
                    "-u" + DB_USER,
                    "-p" + DB_PASSWORD,
                    DB_NAME
                );
                
                Process process = pb.start();
                
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                     FileWriter writer = new FileWriter("database_backup.sql")) {
                    
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line + "\n");
                    }
                    System.out.println("Database exported successfully!");
                }
                
                process.waitFor();
            } catch (Exception e) {
                System.err.println("Error exporting database: " + e.getMessage());
            }
        });
        
        exportThread.start();
    }
    
    /**
     * Closes database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}