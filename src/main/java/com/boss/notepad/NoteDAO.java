package com.boss.notepad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    public void saveOrUpdateNote(String title, String body) {
        // This "UPSERT" logic updates the note if the title exists, otherwise inserts it
        String query = "INSERT INTO notes (title, body, last_modified) VALUES (?, ?, CURRENT_TIMESTAMP) " +
                "ON CONFLICT (title) DO UPDATE SET body = EXCLUDED.body, last_modified = CURRENT_TIMESTAMP";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, body);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<String> searchNotes(String keyword) {
        List<String> titles = new ArrayList<>();
        String query = "SELECT title FROM notes WHERE body ILIKE ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) titles.add(rs.getString("title"));
        } catch (SQLException e) { e.printStackTrace(); }
        return titles;
    }

    public List<String> getAllTitles() {
        List<String> titles = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT title FROM notes ORDER BY last_modified DESC")) {
            while (rs.next()) titles.add(rs.getString("title"));
        } catch (SQLException e) { e.printStackTrace(); }
        return titles;
    }

    public String getNoteContent(String title) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT body FROM notes WHERE title = ?")) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("body");
        } catch (SQLException e) { e.printStackTrace(); }
        return "";
    }
}