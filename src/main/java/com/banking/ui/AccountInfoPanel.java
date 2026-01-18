package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import java.awt.*;

public class AccountInfoPanel extends JPanel {
    private String accountNumber;
    private BankingSystem bankingSystem;
    private JLabel accountNumberLabel;
    private JLabel holderNameLabel;
    private JLabel balanceLabel;
    private JLabel statusLabel;

    public AccountInfoPanel(String accountNumber) {
        this.accountNumber = accountNumber;
        this.bankingSystem = Main.bankingSystem;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel accountLabel = new JLabel("Account Number:");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        accountLabel.setForeground(new Color(50, 50, 50));
        add(accountLabel, gbc);
        gbc.gridx = 1;
        accountNumberLabel = new JLabel();
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accountNumberLabel.setForeground(new Color(0, 70, 150));
        add(accountNumberLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel holderLabel = new JLabel("Holder Name:");
        holderLabel.setFont(new Font("Arial", Font.BOLD, 12));
        holderLabel.setForeground(new Color(50, 50, 50));
        add(holderLabel, gbc);
        gbc.gridx = 1;
        holderNameLabel = new JLabel();
        holderNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        holderNameLabel.setForeground(new Color(30, 30, 30));
        add(holderNameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel balanceTextLabel = new JLabel("Balance:");
        balanceTextLabel.setFont(new Font("Arial", Font.BOLD, 12));
        balanceTextLabel.setForeground(new Color(50, 50, 50));
        add(balanceTextLabel, gbc);
        gbc.gridx = 1;
        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(0, 120, 60));
        add(balanceLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel statusTextLabel = new JLabel("Status:");
        statusTextLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusTextLabel.setForeground(new Color(50, 50, 50));
        add(statusTextLabel, gbc);
        gbc.gridx = 1;
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(statusLabel, gbc);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.setBackground(new Color(0, 102, 204));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.setFocusPainted(false);
        refreshButton.setOpaque(true);
        refreshButton.setContentAreaFilled(true);
        refreshButton.setBorderPainted(true);
        refreshButton.setBorder(BorderFactory.createRaisedBevelBorder());
        refreshButton.addActionListener(e -> refresh());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(refreshButton, gbc);

        refresh();
    }

    public void refresh() {
        try {
            String info = bankingSystem.displayAccountInfo(accountNumber);
            String[] parts = info.split("\\|");
            
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("Account Number:")) {
                    accountNumberLabel.setText(part.substring("Account Number:".length()).trim());
                } else if (part.startsWith("Holder:")) {
                    holderNameLabel.setText(part.substring("Holder:".length()).trim());
                } else if (part.startsWith("Balance:")) {
                    balanceLabel.setText(part.substring("Balance:".length()).trim());
                } else if (part.startsWith("Status:")) {
                    String status = part.substring("Status:".length()).trim();
                    statusLabel.setText(status);
                    if (status.equals("ACTIVE")) {
                        statusLabel.setForeground(new Color(0, 102, 204));
                    } else if (status.equals("FROZEN")) {
                        statusLabel.setForeground(new Color(0, 70, 150));
                    } else {
                        statusLabel.setForeground(new Color(0, 70, 150));
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading account info: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
