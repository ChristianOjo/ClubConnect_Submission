package com.clubconnect;

import com.clubconnect.database.DatabaseManager;
import com.clubconnect.ui.LoginFrame;

import javax.swing.*;

/**
 * Main application entry point for ClubConnect.
 * Initializes the database and starts the LoginFrame.
 */
public class MainApp {
    public static void main(String[] args) {
        // 1. Initialize Database (Requirement: Automatically create DB/tables on start)
        DatabaseManager.initializeDatabase();

        // 2. Start the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
