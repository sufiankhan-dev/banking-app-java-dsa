package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField pinField;
    private JLabel errorLabel;
    private final BankingSystem bankingSystem = Main.bankingSystem;

    public MainWindow() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Banking System - Login");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBackground(Theme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildLoginCard(), BorderLayout.CENTER);
        root.add(buildErrorLabel(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JLabel title = new JLabel("Welcome to Banking System", SwingConstants.CENTER);
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.TEXT_PRIMARY);

        JPanel panel = new JPanel();
        panel.setBackground(Theme.BACKGROUND_COLOR);
        panel.add(title);
        return panel;
    }

    private JPanel buildLoginCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD_COLOR);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(label("Username"), gbc);

        gbc.gridx = 1;
        usernameField = inputField();
        card.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        card.add(label("PIN"), gbc);

        gbc.gridx = 1;
        pinField = new JPasswordField(20);
        styleInput(pinField);
        card.add(pinField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(primaryButton("Login", this::login), gbc);

        gbc.gridy = 3;
        card.add(linkButton("Create New Account", e ->
                new CreateAccountDialog(this).setVisible(true)
        ), gbc);

        gbc.gridy = 4;
        card.add(linkButton("Admin Login", e -> adminLogin()), gbc);

        return card;
    }

    private JLabel buildErrorLabel() {
        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(Theme.BODY_FONT);
        return errorLabel;
    }

    // ---------- Helpers ----------

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.SUBHEADER_FONT);
        l.setForeground(Theme.TEXT_SECONDARY);
        return l;
    }

    private JTextField inputField() {
        JTextField field = new JTextField(20);
        styleInput(field);
        return field;
    }

    private void styleInput(JTextField field) {
        field.setFont(Theme.BODY_FONT);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setBackground(Theme.PANEL_COLOR);
        field.setCaretColor(Theme.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
    }

    private JButton primaryButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.SUBHEADER_FONT);
        btn.setBackground(Theme.BUTTON_PRIMARY);
        btn.setForeground(Theme.BUTTON_TEXT);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JButton linkButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.BODY_FONT);
        btn.setForeground(Theme.PRIMARY_COLOR);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.addActionListener(action);
        return btn;
    }

    // ---------- Logic ----------

    private void login() {
        String username = usernameField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (username.isEmpty() || pin.isEmpty()) {
            errorLabel.setText("Username and PIN required");
            return;
        }

        try {
            bankingSystem.login(username, pin);
            new UserDashboard().setVisible(true);
            dispose();
        } catch (Exception ex) {
            errorLabel.setText(ex.getMessage());
            pinField.setText("");
        }
    }

    private void adminLogin() {
        String u = JOptionPane.showInputDialog(this, "Admin Username");
        String p = JOptionPane.showInputDialog(this, "Admin Password");

        if ("admin".equals(u) && "admin123".equals(p)) {
            new AdminDashboard().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid admin credentials");
        }
    }
}
