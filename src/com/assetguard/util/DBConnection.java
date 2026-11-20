package com.assetguard.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:sqlite:assetguard.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(JDBC_URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    private static void initializeDatabase() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "full_name TEXT NOT NULL, "
                + "role TEXT NOT NULL CHECK(role IN ('REPORTER', 'ADMIN')), "
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP);";

        String createAssets = "CREATE TABLE IF NOT EXISTS assets ("
                + "asset_id TEXT PRIMARY KEY, "
                + "asset_name TEXT NOT NULL, "
                + "category TEXT NOT NULL CHECK(category IN ('Computer', 'Microcontroller', 'Network', 'Peripheral')), "
                + "lab_location TEXT NOT NULL, "
                + "operational_status TEXT DEFAULT 'ACTIVE' CHECK(operational_status IN ('ACTIVE', 'UNDER_REPAIR', 'DECOMMISSIONED')));";

        String createIncidents = "CREATE TABLE IF NOT EXISTS incidents ("
                + "incident_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "asset_id TEXT NOT NULL, "
                + "reported_by TEXT NOT NULL, "
                + "issue_category TEXT NOT NULL, "
                + "description TEXT NOT NULL, "
                + "severity TEXT NOT NULL CHECK(severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')), "
                + "status TEXT DEFAULT 'OPEN' CHECK(status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED')), "
                + "admin_remarks TEXT DEFAULT 'Pending review', "
                + "reported_at DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "resolved_at DATETIME NULL, "
                + "FOREIGN KEY (asset_id) REFERENCES assets(asset_id) ON DELETE CASCADE);";

        String seedUsers = "INSERT OR IGNORE INTO users (username, password, full_name, role) VALUES "
                + "('admin_mec', 'admin123', 'Lab Administrator', 'ADMIN'), "
                + "('hrishikesh', 'pass123', 'Hrishikesh B', 'REPORTER'), "
                + "('kanishk', 'pass123', 'Kanishk Nair', 'REPORTER'), "
                + "('athul', 'pass123', 'Athul G S Nair', 'REPORTER'), "
                + "('savanth', 'pass123', 'Savanth R S', 'REPORTER');";

        String seedAssets = "INSERT OR IGNORE INTO assets (asset_id, asset_name, category, lab_location) VALUES "
                + "('CSLAB1-PC12', 'Dell OptiPlex 7090 Desktop', 'Computer', 'CSE Programming Lab 1'), "
                + "('MP-ARD-08', 'Arduino Uno R3 Kit #08', 'Microcontroller', 'Microprocessor & Embedded Lab'), "
                + "('NET-SW-02', 'Cisco Catalyst 24-Port Switch', 'Network', 'CSE Network Lab'), "
                + "('CSLAB2-KB05', 'Logitech USB Keyboard #05', 'Peripheral', 'CSE Programming Lab 2');";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createAssets);
            stmt.execute(createIncidents);
            stmt.execute(seedUsers);
            stmt.execute(seedAssets);
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}
