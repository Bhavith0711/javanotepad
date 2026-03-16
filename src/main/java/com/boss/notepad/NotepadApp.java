package com.boss.notepad;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class NotepadApp extends JFrame {
    private JTextArea textArea;
    private JLabel statusBar;
    private NoteDAO noteDAO;
    private boolean isDarkMode = false;

    public NotepadApp() {
        noteDAO = new NoteDAO();
        setTitle("Boss Notepad Elite");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 1. Text Area with Real-time Word Count
        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateWordCount(); }
            public void removeUpdate(DocumentEvent e) { updateWordCount(); }
            public void changedUpdate(DocumentEvent e) { updateWordCount(); }
        });

        // 2. Status Bar
        statusBar = new JLabel(" Words: 0 | Mode: Light");
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        // 3. Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("Search in DB");
        searchPanel.add(new JLabel(" Quick Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        searchBtn.addActionListener(e -> {
            List<String> results = noteDAO.searchNotes(searchField.getText());
            JOptionPane.showMessageDialog(this, "Found in: " + results.toString());
        });

        // Layout Assembly
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setJMenuBar(createMenuBar());
    }
    private void exportToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Note as Text File");

        // Show the "Save" dialog
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();

            // Ensure it ends with .txt
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".txt")) {
                fileToSave = new java.io.File(filePath + ".txt");
            }

            try (java.io.FileWriter writer = new java.io.FileWriter(fileToSave)) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(this, "Exported successfully to: " + fileToSave.getName());
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu view = new JMenu("View");

        // File Actions
        JMenuItem save = new JMenuItem("Save/Update to DB");
        save.addActionListener(e -> {
            String t = JOptionPane.showInputDialog(this, "Note Title:");
            if (t != null && !t.isEmpty()) {
                noteDAO.saveOrUpdateNote(t, textArea.getText());
                JOptionPane.showMessageDialog(this, "Database Updated, boss!");
            }
            // Inside createMenuBar() ...
            JMenuItem exportItem = new JMenuItem("Export to .txt");
            exportItem.addActionListener(b -> exportToFile());

// Add it to the file menu
            file.add(exportItem);
        });

        JMenuItem open = new JMenuItem("Open from DB");
        open.addActionListener(e -> {
            List<String> titles = noteDAO.getAllTitles();
            String res = (String) JOptionPane.showInputDialog(null, "Select Note", "Open",
                    JOptionPane.PLAIN_MESSAGE, null, titles.toArray(), "");
            if (res != null) textArea.setText(noteDAO.getNoteContent(res));
        });

        // View Actions (Dark Mode)
        JMenuItem darkModBtn = new JMenuItem("Toggle Dark Mode");
        darkModBtn.addActionListener(e -> toggleDarkMode());

        file.add(open); file.add(save);
        view.add(darkModBtn);
        mb.add(file); mb.add(view);
        return mb;
    }

    private void updateWordCount() {
        String text = textArea.getText().trim();
        int words = text.isEmpty() ? 0 : text.split("\\s+").length;
        statusBar.setText(" Words: " + words + " | Mode: " + (isDarkMode ? "Dark" : "Light"));
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            textArea.setBackground(new Color(43, 43, 43));
            textArea.setForeground(Color.WHITE);
            textArea.setCaretColor(Color.WHITE);
        } else {
            textArea.setBackground(Color.WHITE);
            textArea.setForeground(Color.BLACK);
            textArea.setCaretColor(Color.BLACK);
        }
        updateWordCount();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NotepadApp().setVisible(true));
    }
}