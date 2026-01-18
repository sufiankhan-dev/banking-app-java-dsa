package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private JTextField accountNumberField;
    private JPasswordField pinField;
    private JLabel errorLabel;
    private BankingSystem bankingSystem;

    public MainWindow() {
        this.bankingSystem = Main.bankingSystem;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Banking System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(245, 245, 250));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(245, 245, 250));
        JLabel titleLabel = new JLabel("Welcome to Banking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 70, 150));
        headerPanel.add(titleLabel);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255));
        loginPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Login", 
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 70, 150)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel accountLabel = new JLabel("Account Number:");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        accountLabel.setForeground(new Color(50, 50, 50));
        loginPanel.add(accountLabel, gbc);

        gbc.gridx = 1;
        accountNumberField = new JTextField(25);
        accountNumberField.setPreferredSize(new Dimension(200, 30));
        accountNumberField.setFont(new Font("Arial", Font.PLAIN, 12));
        accountNumberField.setForeground(Color.BLACK);
        accountNumberField.setBackground(Color.WHITE);
        accountNumberField.setEnabled(true);
        accountNumberField.setEditable(true);
        accountNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        loginPanel.add(accountNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.BOLD, 12));
        pinLabel.setForeground(new Color(50, 50, 50));
        loginPanel.add(pinLabel, gbc);

        gbc.gridx = 1;
        pinField = new JPasswordField(25);
        pinField.setPreferredSize(new Dimension(200, 30));
        pinField.setForeground(Color.BLACK);
        pinField.setBackground(Color.WHITE);
        pinField.setEnabled(true);
        pinField.setEditable(true);
        pinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        loginPanel.add(pinField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorderPainted(true);
        loginButton.setBorder(BorderFactory.createRaisedBevelBorder());
        loginButton.addActionListener(new LoginActionListener());
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        optionsPanel.setBackground(new Color(255, 255, 255));
        optionsPanel.setOpaque(true);
        
        JButton createAccountLink = new JButton("Create New Account");
        createAccountLink.setBorderPainted(false);
        createAccountLink.setContentAreaFilled(false);
        createAccountLink.setForeground(new Color(0, 102, 204));
        createAccountLink.setFont(new Font("Arial", Font.PLAIN, 12));
        createAccountLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountLink.addActionListener(e -> {
            CreateAccountDialog dialog = new CreateAccountDialog(this);
            dialog.setVisible(true);
        });
        optionsPanel.add(createAccountLink);
        
        JLabel separator = new JLabel("|");
        separator.setForeground(new Color(150, 150, 150));
        optionsPanel.add(separator);
        
        JButton adminLoginLink = new JButton("Admin Login");
        adminLoginLink.setBorderPainted(false);
        adminLoginLink.setContentAreaFilled(false);
        adminLoginLink.setForeground(new Color(0, 102, 204));
        adminLoginLink.setFont(new Font("Arial", Font.PLAIN, 12));
        adminLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminLoginLink.addActionListener(new AdminLoginActionListener());
        optionsPanel.add(adminLoginLink);
        
        loginPanel.add(optionsPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton createAccountButton = new JButton("Create New Account");
        createAccountButton.setPreferredSize(new Dimension(180, 40));
        createAccountButton.setBackground(new Color(0, 102, 204));
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 13));
        createAccountButton.setFocusPainted(false);
        createAccountButton.setOpaque(true);
        createAccountButton.setContentAreaFilled(true);
        createAccountButton.setBorderPainted(true);
        createAccountButton.setBorder(BorderFactory.createRaisedBevelBorder());
        createAccountButton.addActionListener(e -> {
            CreateAccountDialog dialog = new CreateAccountDialog(this);
            dialog.setVisible(true);
        });

        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.setPreferredSize(new Dimension(180, 40));
        adminLoginButton.setBackground(new Color(0, 102, 204));
        adminLoginButton.setForeground(Color.WHITE);
        adminLoginButton.setFont(new Font("Arial", Font.BOLD, 13));
        adminLoginButton.setFocusPainted(false);
        adminLoginButton.setOpaque(true);
        adminLoginButton.setContentAreaFilled(true);
        adminLoginButton.setBorderPainted(true);
        adminLoginButton.setBorder(BorderFactory.createRaisedBevelBorder());
        adminLoginButton.addActionListener(new AdminLoginActionListener());

        buttonPanel.add(createAccountButton);
        buttonPanel.add(adminLoginButton);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(new Color(245, 245, 250));
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        mainPanel.add(errorPanel, BorderLayout.AFTER_LAST_LINE);

        add(mainPanel);
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String accountNumber = accountNumberField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (accountNumber.isEmpty() || pin.isEmpty()) {
                errorLabel.setText("Please enter both account number and PIN");
                return;
            }

            try {
                bankingSystem.login(accountNumber, pin);
                String holderName = bankingSystem.getAccountHolderName(accountNumber);
                errorLabel.setText(" ");
                
                UserDashboard userDashboard = new UserDashboard();
                userDashboard.setVisible(true);
                dispose();
            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
                pinField.setText("");
            }
        }
    }

    private class AdminLoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = JOptionPane.showInputDialog(MainWindow.this, "Enter Admin Username:", "Admin Authentication", JOptionPane.QUESTION_MESSAGE);
            if (username == null) return;

            String password = JOptionPane.showInputDialog(MainWindow.this, "Enter Admin Password:", "Admin Authentication", JOptionPane.QUESTION_MESSAGE);
            if (password == null) return;

            if (username.equals("admin") && password.equals("admin123")) {
                errorLabel.setText(" ");
                AdminDashboard adminDashboard = new AdminDashboard();
                adminDashboard.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(MainWindow.this, "Invalid admin credentials. Access denied.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
