package com.clubconnect.ui;

import com.clubconnect.models.User;
import com.clubconnect.utils.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Admin Dashboard for authenticated users with ADMIN role.
 * Oversees all clubs, approves new clubs and budget requests, manages user accounts.
 */
public class AdminDashboard extends JFrame {
    
    public AdminDashboard() {
        initComponents();
    }
    
    private void initComponents() {
        User currentUser = SessionManager.getCurrentUser();
        setTitle("ClubConnect - Admin Dashboard: " + currentUser.getUsername());
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container with a professional look
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 128, 0)); // Green for Admin
        headerPanel.setPreferredSize(new Dimension(1400, 60));
        
        JLabel titleLabel = new JLabel("ClubConnect - Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(200, 80, 80)); // Red for logout
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> handleLogout());
        
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(logoutButton);
        headerPanel.add(buttonWrapper, BorderLayout.EAST);
        
        // Content Panel (Tabbed Pane for navigation)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Tab 1: User Management
        JPanel userMgmtPanel = new JPanel(new BorderLayout());
        userMgmtPanel.add(new JLabel("User Account Management (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("User Management", userMgmtPanel);
        
        // Tab 2: Club Approval
        JPanel clubApprovalPanel = new JPanel(new BorderLayout());
        clubApprovalPanel.add(new JLabel("Club Creation and Approval (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Club Approval", clubApprovalPanel);
        
        // Tab 3: Budget Approval
        JPanel budgetApprovalPanel = new JPanel(new BorderLayout());
        budgetApprovalPanel.add(new JLabel("Budget Request Approval (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Budget Approval", budgetApprovalPanel);
        
        // Tab 4: Global Reports
        JPanel reportsPanel = new JPanel(new BorderLayout());
        reportsPanel.add(new JLabel("Global Reports and Analytics (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Reports", reportsPanel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void handleLogout() {
        SessionManager.logout();
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        dispose();
    }
}
