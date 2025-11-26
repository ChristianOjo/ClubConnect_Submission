package com.clubconnect.ui;

import com.clubconnect.models.User;
import com.clubconnect.utils.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Member Dashboard for authenticated users with MEMBER role.
 * Allows browsing clubs, applying for membership, RSVPing to events, etc.
 */
public class MemberDashboard extends JFrame {
    
    public MemberDashboard() {
        initComponents();
    }
    
    private void initComponents() {
        User currentUser = SessionManager.getCurrentUser();
        setTitle("ClubConnect - Member Dashboard: " + currentUser.getUsername());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container with a professional look
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(1000, 60));
        
        JLabel titleLabel = new JLabel("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole().name() + ")", SwingConstants.CENTER);
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
        
        // Tab 1: My Clubs
        JPanel myClubsPanel = new JPanel(new BorderLayout());
        myClubsPanel.add(new JLabel("My Clubs (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("My Clubs", myClubsPanel);
        
        // Tab 2: Browse Clubs
        JPanel browseClubsPanel = new JPanel(new BorderLayout());
        browseClubsPanel.add(new JLabel("Browse Clubs (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Browse Clubs", browseClubsPanel);
        
        // Tab 3: My Events
        JPanel myEventsPanel = new JPanel(new BorderLayout());
        myEventsPanel.add(new JLabel("My Events/RSVPs (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("My Events", myEventsPanel);
        
        // Tab 4: Profile
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.add(new JLabel("User Profile Management (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Profile", profilePanel);
        
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
