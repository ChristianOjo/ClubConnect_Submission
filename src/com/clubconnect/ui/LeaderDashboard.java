package com.clubconnect.ui;

import com.clubconnect.models.User;
import com.clubconnect.utils.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Leader Dashboard for authenticated users with LEADER role.
 * Allows managing club, events, and budget requests.
 */
public class LeaderDashboard extends JFrame {
    
    public LeaderDashboard() {
        initComponents();
    }
    
    private void initComponents() {
        User currentUser = SessionManager.getCurrentUser();
        setTitle("ClubConnect - Leader Dashboard: " + currentUser.getUsername());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container with a professional look
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 165, 0)); // Orange for Leader
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("ClubConnect - Leader Dashboard", SwingConstants.CENTER);
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
        
        // Tab 1: My Club Management
        JPanel clubMgmtPanel = new JPanel(new BorderLayout());
        clubMgmtPanel.add(new JLabel("My Club Management (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Club Management", clubMgmtPanel);
        
        // Tab 2: Event Scheduling
        JPanel eventPanel = new JPanel(new BorderLayout());
        eventPanel.add(new JLabel("Event Scheduling and Management (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Event Scheduling", eventPanel);
        
        // Tab 3: Budget Requests
        JPanel budgetPanel = new JPanel(new BorderLayout());
        budgetPanel.add(new JLabel("Budget Requests and Tracking (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Budget", budgetPanel);
        
        // Tab 4: Member Management
        JPanel memberPanel = new JPanel(new BorderLayout());
        memberPanel.add(new JLabel("Club Member Management (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Members", memberPanel);
        
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
