package com.clubconnect.ui;

import com.clubconnect.dao.UserDAO;
import com.clubconnect.models.User;
import com.clubconnect.utils.PasswordHasher;
import com.clubconnect.utils.Validator;

import javax.swing.*;
import java.awt.*;

/**
 * Registration frame for new user sign-up
 */
public class RegistrationFrame extends JFrame {
    private JTextField studentIdField;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private UserDAO userDAO;
    private JFrame parentFrame;
    
    public RegistrationFrame(JFrame parent) {
        this.parentFrame = parent;
        this.userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("ClubConnect - Register");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(100, 180, 100));
        headerPanel.setPreferredSize(new Dimension(500, 70));
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Student ID:"), gbc);
        
        gbc.gridx = 1;
        studentIdField = new JTextField(20);
        formPanel.add(studentIdField, gbc);
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Phone (Optional):"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));
        
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(100, 180, 100));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(120, 35));
        registerButton.addActionListener(e -> handleRegistration());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(200, 80, 80));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> handleCancel());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Add panels
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void handleRegistration() {
        try {
            // Get input values
            String studentId = studentIdField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Validate inputs
            if (!validateInputs(studentId, username, email, phone, password, confirmPassword)) {
                return;
            }
            
            // Check if student ID already exists
            if (userDAO.studentIdExists(studentId)) {
                JOptionPane.showMessageDialog(this,
                    "Student ID already registered",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if email already exists
            if (userDAO.emailExists(email)) {
                JOptionPane.showMessageDialog(this,
                    "Email already registered",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create user
            User newUser = new User(studentId, username, email, PasswordHasher.hashPassword(password));
            newUser.setContactNumber(phone.isEmpty() ? null : phone);
            
            // Save to database
            if (userDAO.createUser(newUser)) {
                JOptionPane.showMessageDialog(this,
                    "Registration successful! You can now login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                handleCancel();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Registration failed. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "An error occurred during registration: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateInputs(String studentId, String username, String email, 
                                   String phone, String password, String confirmPassword) {
        // Check empty fields
        if (!Validator.isNotEmpty(studentId) || !Validator.isNotEmpty(username) || 
            !Validator.isNotEmpty(email) || !Validator.isNotEmpty(password)) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate student ID
        if (!Validator.isValidStudentId(studentId)) {
            JOptionPane.showMessageDialog(this,
                "Student ID must be between 5 and 50 characters",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate email
        if (!Validator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate phone (if provided)
        if (!phone.isEmpty() && !Validator.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid phone number (10-15 digits)",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate password
        if (!Validator.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters long",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Check password match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void handleCancel() {
        dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }
}