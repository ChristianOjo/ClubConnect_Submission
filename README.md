# ClubConnect - University Clubs Management System

This project implements the core functionality for the ClubConnect system.

## Section A: What I Implemented

I focused on delivering the core requirements for Section A of the assignment. Here is a breakdown of the main components:

1.  **User Authentication and Roles:**
    *   **Login & Registration:** I built the `LoginFrame.java` and `RegistrationFrame.java`. I used `PasswordHasher.java` for secure password hashing and `Validator.java` for input checks.
    *   **Role-Based Access:** After a successful login, the user is directed to the correct dashboard (`AdminDashboard`, `LeaderDashboard`, `MemberDashboard`) based on their role.
    *   **Session Management:** The `SessionManager.java` class handles keeping track of the currently logged-in user.
    *   **Guest Mode:** I also included a guest mode for unauthenticated viewing.

2.  **Look and Feel (GUI):**
    *   The entire application uses **Java Swing**. I aimed for a clean, professional, and consistent look across all the main screens (`LoginFrame`, `RegistrationFrame`, and the dashboard placeholders).

3.  **Data Persistence (Database Setup):**
    *   The system is designed to connect to a **MySQL** database using JDBC.
    *   **Automatic Setup:** The `DatabaseManager.java` is set up to automatically create the database (`clubconnect_db`) and all necessary tables (users, clubs, events, etc.) the first time the application runs.

## Project Structure

The project follows a standard Java package structure:



\`\`\`
.
├── src
│   └── com
│       └── clubconnect
│           ├── MainApp.java             <- Main entry point
│           ├── dao                     <- Data Access Objects (DAO)
│           ├── database                <- Database connection and management
│           ├── models                  <- Data Models (POJOs)
│           ├── ui                      <- User Interface (GUI Frames/Panels)
│           └── utils                   <- Utility classes (Hashing, Validation, Session)
├── run.sh                          <- Script to compile and run the application
├── C7-JAV-11-END-QP.pdf            <- Assignment Question Paper
├── CompleteProjectGuide-ClubConnect.pdf
├── IMPLEMENTATION_GUIDE.md.pdf
├── PROJECT_STRUCTURE.md.pdf
└── QUICK_REFERENCE_CARD.md.pdf
\`\`\`


---

## Important Note on Database Connection

**I was unable to install the MySQL server on my local machine, so I could not perform an end-to-end test of the database connection and initialization logic.**

However, the code is structured to meet the **Data Persistence** requirement:
*   It uses JDBC and is configured to connect to a MySQL server on `localhost:3306`.
*   The `DatabaseManager.java` contains the logic to automatically create the database and tables on first run.
*   The default credentials in `db_manager.java` are set to a common testing configuration (`DB_USER = "root"`, `DB_PASSWORD = ""`). **If your local MySQL server requires a password, please update these constants before running.**

---

## How to Run the Application

**Prerequisites:**
1.  Java Development Kit (JDK) 8 or higher.
2.  A running MySQL server instance.
3.  The MySQL JDBC Connector JAR file (e.g., `mysql-connector-j-9.5.0.jar`) must be placed in the project root directory.

**Steps:**
1.  **Get the JDBC Connector:** Make sure you have the MySQL JDBC driver JAR file in the root folder.
2.  **Check Credentials:** If your MySQL server uses a password, edit `src/com/clubconnect/database/db_manager.java` and update the `DB_USER` and `DB_PASSWORD` constants.
3.  **Run the Script:**
    ```bash
    ./run.sh
    ```

The script will compile all Java files and start the `MainApp`. The application will then initialize the database and open the login screen.

**A Note on Dashboards:** The dashboard classes are currently placeholders for the functionality required in Section B. This submission focuses entirely on the successful implementation of the Section A requirements (Authentication, Registration, Session Management, and Database Initialization).
