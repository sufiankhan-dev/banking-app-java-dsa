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
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Welcome to Banking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        headerPanel.add(titleLabel);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Account Number:"), gbc);

        gbc.gridx = 1;
        accountNumberField = new JTextField(20);
        loginPanel.add(accountNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("PIN:"), gbc);

        gbc.gridx = 1;
        pinField = new JPasswordField(20);
        loginPanel.add(pinField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(150, 35));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new LoginActionListener());
        loginPanel.add(loginButton, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton createAccountButton = new JButton("Create New Account");
        createAccountButton.setPreferredSize(new Dimension(180, 35));
        createAccountButton.setBackground(new Color(0, 153, 76));
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setFocusPainted(false);
        createAccountButton.addActionListener(e -> {
            CreateAccountDialog dialog = new CreateAccountDialog(this);
            dialog.setVisible(true);
        });

        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.setPreferredSize(new Dimension(180, 35));
        adminLoginButton.setBackground(new Color(204, 102, 0));
        adminLoginButton.setForeground(Color.WHITE);
        adminLoginButton.setFocusPainted(false);
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
        errorPanel.setBackground(Color.WHITE);
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
