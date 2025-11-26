package com.clubconnect.utils;

import com.clubconnect.models.User;

/**
 * Manages user session
 */
public class SessionManager {
    private static User currentUser;
    
    /**
     * Sets the current logged-in user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    /**
     * Gets the current logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is logged in
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Logs out the current user
     */
    public static void logout() {
        currentUser = null;
    }
    
    /**
     * Checks if current user is admin
     */
    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == User.UserRole.ADMIN;
    }
    
    /**
     * Checks if current user is leader
     */
    public static boolean isLeader() {
        return currentUser != null && currentUser.getRole() == User.UserRole.LEADER;
    }
    
    /**
     * Checks if current user is member
     */
    public static boolean isMember() {
        return currentUser != null && currentUser.getRole() == User.UserRole.MEMBER;
    }
    
    /**
     * Checks if current user is guest
     */
    public static boolean isGuest() {
        return currentUser != null && currentUser.getRole() == User.UserRole.GUEST;
    }
}