package com.clubconnect.ui;

import com.clubconnect.dao.UserDAO;
import com.clubconnect.models.User;
import com.clubconnect.utils.PasswordHasher;
import com.clubconnect.utils.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Login frame for user authentication
 */
public class LoginFrame extends JFrame {
    private JTextField studentIdField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ClubConnect - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(450, 80));
        JLabel titleLabel = new JLabel("ClubConnect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(studentIdLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        studentIdField = new JTextField(20);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(studentIdField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.addActionListener(e -> handleLogin());
        
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(100, 180, 100));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(120, 35));
        registerButton.addActionListener(e -> openRegistration());
        
        JButton guestButton = new JButton("Guest Mode");
        guestButton.setFont(new Font("Arial", Font.PLAIN, 12));
        guestButton.setForeground(new Color(70, 130, 180));
        guestButton.setContentAreaFilled(false);
        guestButton.setBorderPainted(false);
        guestButton.addActionListener(e -> handleGuestMode());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        formPanel.add(buttonPanel, gbc);
        
        gbc.gridy = 3;
        formPanel.add(guestButton, gbc);
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Enter key listener
        passwordField.addActionListener(e -> handleLogin());
    }
    
    private void handleLogin() {
        String studentId = studentIdField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (studentId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both Student ID and Password",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Hash the password
        String hashedPassword = PasswordHasher.hashPassword(password);
        
        // Authenticate
        User user = userDAO.authenticate(studentId, hashedPassword);
        
        if (user != null) {
            // Set session
            SessionManager.setCurrentUser(user);
            
            // Open appropriate dashboard
            openDashboard(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid Student ID or Password",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
    
    private void openRegistration() {
        RegistrationFrame registrationFrame = new RegistrationFrame(this);
        registrationFrame.setVisible(true);
        setVisible(false);
    }
    
    private void handleGuestMode() {
        // Create guest user
        User guest = new User();
        guest.setUsername("Guest");
        guest.setRole(User.UserRole.GUEST);
        SessionManager.setCurrentUser(guest);
        
        // Open guest dashboard
        GuestDashboard guestDashboard = new GuestDashboard();
        guestDashboard.setVisible(true);
        dispose();
    }
    
    private void openDashboard(User user) {
        switch (user.getRole()) {
            case ADMIN:
                AdminDashboard adminDashboard = new AdminDashboard();
                adminDashboard.setVisible(true);
                break;
            case LEADER:
                LeaderDashboard leaderDashboard = new LeaderDashboard();
                leaderDashboard.setVisible(true);
                break;
            case MEMBER:
                MemberDashboard memberDashboard = new MemberDashboard();
                memberDashboard.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this,
                    "Unknown user role",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}