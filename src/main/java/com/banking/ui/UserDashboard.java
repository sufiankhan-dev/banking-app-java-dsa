package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserDashboard extends JFrame {
    private BankingSystem bankingSystem;
    private String accountNumber;
    private AccountInfoPanel accountInfoPanel;
    private JLabel statusLabel;

    public UserDashboard() {
        this.bankingSystem = Main.bankingSystem;
        this.accountNumber = bankingSystem.getCurrentLoggedInAccount();
        initializeUI();
        refreshAccountInfo();
    }

    private void initializeUI() {
        setTitle("Banking System - User Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));
        String holderName = bankingSystem.getAccountHolderName(accountNumber);
        JLabel welcomeLabel = new JLabel("Welcome, " + holderName);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(0, 70, 150));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(new Color(180, 0, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.setOpaque(true);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setBorderPainted(true);
        logoutButton.setBorder(BorderFactory.createRaisedBevelBorder());
        logoutButton.addActionListener(e -> {
            bankingSystem.logout();
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            dispose();
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Banking Operations",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(0, 70, 150)));

        JButton depositButton = createMenuButton("Deposit Money", new Color(0, 153, 76));
        depositButton.addActionListener(e -> {
            String amountText = JOptionPane.showInputDialog(this, "Enter Amount to Deposit:", "Deposit Money", JOptionPane.QUESTION_MESSAGE);
            if (amountText == null || amountText.trim().isEmpty()) return;
            
            try {
                double amount = Double.parseDouble(amountText.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                bankingSystem.deposit(accountNumber, amount);
                JOptionPane.showMessageDialog(this, 
                    "Deposit successful!\nNew Balance: $" + String.format("%.2f", bankingSystem.getBalance(accountNumber)), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAccountInfo();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton withdrawButton = createMenuButton("Withdraw Money", new Color(204, 102, 0));
        withdrawButton.addActionListener(e -> {
            String amountText = JOptionPane.showInputDialog(this, "Enter Amount to Withdraw:", "Withdraw Money", JOptionPane.QUESTION_MESSAGE);
            if (amountText == null || amountText.trim().isEmpty()) return;
            
            try {
                double amount = Double.parseDouble(amountText.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                bankingSystem.withdraw(accountNumber, amount);
                JOptionPane.showMessageDialog(this, 
                    "Withdrawal successful!\nRemaining Balance: $" + String.format("%.2f", bankingSystem.getBalance(accountNumber)), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAccountInfo();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton transferButton = createMenuButton("Transfer Funds", new Color(153, 0, 153));
        transferButton.addActionListener(e -> {
            String receiverAccount = JOptionPane.showInputDialog(this, "Enter Receiver Account Number:", "Transfer Funds", JOptionPane.QUESTION_MESSAGE);
            if (receiverAccount == null || receiverAccount.trim().isEmpty()) return;
            
            String amountText = JOptionPane.showInputDialog(this, "Enter Amount to Transfer:", "Transfer Funds", JOptionPane.QUESTION_MESSAGE);
            if (amountText == null || amountText.trim().isEmpty()) return;
            
            try {
                double amount = Double.parseDouble(amountText.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                bankingSystem.transferFunds(accountNumber, receiverAccount.trim(), amount);
                JOptionPane.showMessageDialog(this, 
                    "Transfer successful!\nYour Remaining Balance: $" + String.format("%.2f", bankingSystem.getBalance(accountNumber)), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshAccountInfo();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton viewInfoButton = createMenuButton("View Account Information", new Color(0, 102, 204));
        viewInfoButton.addActionListener(e -> refreshAccountInfo());

        JButton viewHistoryButton = createMenuButton("View Transaction History", new Color(102, 102, 102));
        viewHistoryButton.addActionListener(e -> {
            TransactionHistoryPanel historyPanel = new TransactionHistoryPanel(accountNumber);
            JDialog historyDialog = new JDialog(this, "Transaction History", true);
            historyDialog.add(historyPanel);
            historyDialog.setSize(800, 500);
            historyDialog.setLocationRelativeTo(this);
            historyDialog.setVisible(true);
        });

        JButton updateInfoButton = createMenuButton("Update Account Info", new Color(0, 153, 153));
        updateInfoButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this, "Enter New Holder Name:", "Update Account Info", JOptionPane.QUESTION_MESSAGE);
            if (newName != null && !newName.trim().isEmpty()) {
                try {
                    bankingSystem.updateAccountInfo(accountNumber, newName.trim());
                    JOptionPane.showMessageDialog(this, "Account information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshAccountInfo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton changePinButton = createMenuButton("Change PIN", new Color(153, 153, 0));
        changePinButton.addActionListener(e -> {
            String oldPin = JOptionPane.showInputDialog(this, "Enter Current PIN:", "Change PIN", JOptionPane.QUESTION_MESSAGE);
            if (oldPin == null) return;

            String newPin = JOptionPane.showInputDialog(this, "Enter New PIN:", "Change PIN", JOptionPane.QUESTION_MESSAGE);
            if (newPin == null) return;

            String confirmPin = JOptionPane.showInputDialog(this, "Confirm New PIN:", "Change PIN", JOptionPane.QUESTION_MESSAGE);
            if (confirmPin == null) return;

            if (!newPin.equals(confirmPin)) {
                JOptionPane.showMessageDialog(this, "New PINs do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                bankingSystem.changePin(accountNumber, oldPin, newPin);
                JOptionPane.showMessageDialog(this, "PIN changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transferButton);
        buttonPanel.add(viewInfoButton);
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(changePinButton);

        accountInfoPanel = new AccountInfoPanel(accountNumber);
        accountInfoPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));

        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setPreferredSize(new Dimension(0, 30));
        statusLabel.setBackground(new Color(240, 240, 240));
        statusLabel.setOpaque(true);
        statusLabel.setForeground(new Color(50, 50, 50));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(accountInfoPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    public void refreshAccountInfo() {
        accountInfoPanel.refresh();
        statusLabel.setText("Last updated: " + new java.util.Date().toString());
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}
