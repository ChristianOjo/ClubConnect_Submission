package com.clubconnect.models;

import java.sql.Timestamp;

/**
 * Represents a user in the ClubConnect system
 */
public class User {
    private int userId;
    private String studentId;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private String contactNumber;
    private Timestamp createdAt;
    private UserStatus status;
    
    public enum UserRole {
        ADMIN, LEADER, MEMBER, GUEST
    }
    
    public enum UserStatus {
        ACTIVE, INACTIVE
    }
    
    /**
     * Default constructor
     */
    public User() {
        this.role = UserRole.MEMBER;
        this.status = UserStatus.ACTIVE;
    }
    
    /**
     * Constructor with essential fields
     */
    public User(String studentId, String username, String email, String password) {
        this.studentId = studentId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = UserRole.MEMBER;
        this.status = UserStatus.ACTIVE;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return username + " (" + studentId + ")";
    }
}