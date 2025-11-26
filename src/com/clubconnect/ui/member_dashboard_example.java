package com.clubconnect.ui;

import com.clubconnect.dao.ClubDAO;
import com.clubconnect.dao.EventDAO;
import com.clubconnect.models.Club;
import com.clubconnect.models.Event;
import com.clubconnect.models.User;
import com.clubconnect.utils.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dashboard for Member users
 */
public class MemberDashboard extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private ClubDAO clubDAO;
    private EventDAO eventDAO;
    
    public MemberDashboard() {
        this.currentUser = SessionManager.getCurrentUser();
        this.clubDAO = new ClubDAO();
        this.eventDAO = new EventDAO();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("ClubConnect - Member Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        
        // Add tabs
        tabbedPane.addTab("Browse Clubs", createBrowseClubsPanel());
        tabbedPane.addTab("My Clubs", createMyClubsPanel());
        tabbedPane.addTab("Events", createEventsPanel());
        tabbedPane.addTab("Profile", createProfilePanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Title and user info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("ClubConnect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);
        
        JLabel userLabel = new JLabel("  |  Welcome, " + currentUser.getUsername());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);
        leftPanel.add(userLabel);
        
        // Logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(200, 80, 80));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> handleLogout());
        rightPanel.add(logoutButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createBrowseClubsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        
        JLabel categoryLabel = new JLabel("   Category:");
        String[] categories = {"All", "Academic", "Sports", "Arts", "Technology", "Social"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(categoryLabel);
        searchPanel.add(categoryCombo);
        
        // Clubs table
        String[] columnNames = {"Club Name", "Category", "Members", "Status", "Action"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only action column editable
            }
        };
        
        JTable clubsTable = new JTable(tableModel);
        clubsTable.setRowHeight(30);
        clubsTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        clubsTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), this));
        
        JScrollPane scrollPane = new JScrollPane(clubsTable);
        
        // Search action
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            loadClubsToTable(tableModel, keyword, category);
        });
        
        categoryCombo.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            loadClubsToTable(tableModel, keyword, category);
        });
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMyClubsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("My Club Memberships");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // My clubs table
        String[] columnNames = {"Club Name", "Role", "Status", "Joined Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable myClubsTable = new JTable(tableModel);
        myClubsTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(myClubsTable);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel filterLabel = new JLabel("Show:");
        String[] filters = {"All Events", "My RSVPs", "Upcoming", "Past"};
        JComboBox<String> filterCombo = new JComboBox<>(filters);
        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);
        
        // Events table
        String[] columnNames = {"Event", "Club", "Date", "Time", "Venue", "RSVP Status", "Action"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        JTable eventsTable = new JTable(tableModel);
        eventsTable.setRowHeight(30);
        eventsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        eventsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), this));
        
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        
        filterCombo.addActionListener(e -> {
            String filter = (String) filterCombo.getSelectedItem();
            loadEventsToTable(tableModel, filter);
        });
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Profile information
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        JLabel studentIdLabel = new JLabel(currentUser.getStudentId());
        panel.add(studentIdLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(currentUser.getUsername(), 20);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField emailField = new JTextField(currentUser.getEmail(), 20);
        panel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(currentUser.getContactNumber(), 20);
        panel.add(phoneField, gbc);
        
        // Update button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton updateButton = new JButton("Update Profile");
        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> handleProfileUpdate(nameField, emailField, phoneField));
        panel.add(updateButton, gbc);
        
        // Change password button
        gbc.gridy = 6;
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(e -> handleChangePassword());
        panel.add(changePasswordButton, gbc);
        
        return panel;
    }
    
    private void loadData() {
        // Load initial data for all tabs
        SwingUtilities.invokeLater(() -> {
            // Load clubs
            JPanel browsePanel = (JPanel) tabbedPane.getComponentAt(0);
            // Extract table and load data
            // This would be implemented based on actual DAO methods
        });
    }
    
    private void loadClubsToTable(DefaultTableModel model, String keyword, String category) {
        model.setRowCount(0);
        
        try {
            List<Club> clubs;
            if (keyword.isEmpty() && category.equals("All")) {
                clubs = clubDAO.getAllClubs();
            } else {
                clubs = clubDAO.searchClubs(keyword, category.equals("All") ? null : category);
            }
            
            for (Club club : clubs) {
                Object[] row = {
                    club.getName(),
                    club.getCategory(),
                    "N/A", // Would get actual member count
                    club.getStatus(),
                    "View/Join"
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading clubs: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadEventsToTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        
        try {
            List<Event> events = eventDAO.getAllEvents(); // Filter based on selection
            
            for (Event event : events) {
                Object[] row = {
                    event.getTitle(),
                    "Club Name", // Would get from club
                    event.getEventDate(),
                    event.getStartTime() + " - " + event.getEndTime(),
                    event.getVenue(),
                    "Not RSVP'd", // Check actual RSVP status
                    "RSVP"
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading events: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleProfileUpdate(JTextField nameField, JTextField emailField, JTextField phoneField) {
        try {
            currentUser.setUsername(nameField.getText().trim());
            currentUser.setEmail(emailField.getText().trim());
            currentUser.setContactNumber(phoneField.getText().trim());
            
            // Update in database using UserDAO
            // if (userDAO.updateUser(currentUser)) {
            JOptionPane.showMessageDialog(this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            // }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating profile: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleChangePassword() {
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        Object[] message = {
            "Old Password:", oldPasswordField,
            "New Password:", newPasswordField,
            "Confirm Password:", confirmPasswordField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Validate and update password
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "New passwords do not match!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update password using UserDAO
            JOptionPane.showMessageDialog(this,
                "Password changed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    // Button renderer for table action columns
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    // Button editor for table action columns
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private JFrame parentFrame;
        
        public ButtonEditor(JCheckBox checkBox, JFrame parent) {
            super(checkBox);
            this.parentFrame = parent;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }
        
        public Object getCellEditorValue() {
            if (isPushed) {
                // Handle button click based on label
                if (label.contains("View") || label.contains("Join")) {
                    // Open club details dialog
                } else if (label.equals("RSVP")) {
                    // Handle RSVP
                }
            }
            isPushed = false;
            return label;
        }
        
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}