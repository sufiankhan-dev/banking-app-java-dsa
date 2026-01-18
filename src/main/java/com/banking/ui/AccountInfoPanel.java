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
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        accountNumberLabel = new JLabel();
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(accountNumberLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Holder Name:"), gbc);
        gbc.gridx = 1;
        holderNameLabel = new JLabel();
        holderNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(holderNameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1;
        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(0, 153, 76));
        add(balanceLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(statusLabel, gbc);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(100, 30));
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
                        statusLabel.setForeground(new Color(0, 153, 76));
                    } else if (status.equals("FROZEN")) {
                        statusLabel.setForeground(new Color(204, 102, 0));
                    } else {
                        statusLabel.setForeground(Color.RED);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading account info: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
