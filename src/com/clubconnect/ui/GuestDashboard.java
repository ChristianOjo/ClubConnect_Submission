package com.clubconnect.ui;

import com.clubconnect.utils.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Guest Dashboard for unauthenticated users.
 * Allows viewing public club listings and event calendars.
 */
public class GuestDashboard extends JFrame {
    
    public GuestDashboard() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ClubConnect - Guest View");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container with a professional look
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        
        JLabel titleLabel = new JLabel("ClubConnect - Public Listings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton logoutButton = new JButton("Login/Register");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(255, 165, 0)); // Orange for contrast
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
        
        // Tab 1: Public Club Listings
        JPanel clubPanel = new JPanel(new BorderLayout());
        clubPanel.add(new JLabel("Public Club Listings (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Clubs", clubPanel);
        
        // Tab 2: Event Calendar
        JPanel eventPanel = new JPanel(new BorderLayout());
        eventPanel.add(new JLabel("Public Event Calendar (Placeholder for Section B)", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Events", eventPanel);
        
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
