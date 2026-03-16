package com.boss.notepad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/notepad_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Bhavith@18"; // Update this!

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}