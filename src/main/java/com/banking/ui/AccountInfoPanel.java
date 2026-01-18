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
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 245, 250));
        
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(245, 245, 250));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        JPanel accountCard = createInfoCard("Account Number", "");
        accountNumberLabel = (JLabel) ((JPanel) accountCard.getComponent(1)).getComponent(0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(accountCard, gbc);

        JPanel holderCard = createInfoCard("Holder Name", "");
        holderNameLabel = (JLabel) ((JPanel) holderCard.getComponent(1)).getComponent(0);
        gbc.gridx = 1;
        gbc.gridy = 0;
        cardPanel.add(holderCard, gbc);

        JPanel balanceCard = createHighlightedCard("Balance", "");
        balanceLabel = (JLabel) ((JPanel) balanceCard.getComponent(1)).getComponent(0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        cardPanel.add(balanceCard, gbc);
        gbc.gridwidth = 1;

        JPanel statusCard = createInfoCard("Status", "");
        statusLabel = (JLabel) ((JPanel) statusCard.getComponent(1)).getComponent(0);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        cardPanel.add(statusCard, gbc);
        gbc.gridwidth = 1;
        
        add(cardPanel, BorderLayout.NORTH);

        refresh();
    }
    
    private JPanel createInfoCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout(12, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(new Font("Arial", Font.BOLD, 11));
        labelComponent.setForeground(new Color(120, 120, 140));
        
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valuePanel.setBackground(Color.WHITE);
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.BOLD, 16));
        valueComponent.setForeground(new Color(30, 30, 30));
        valuePanel.add(valueComponent);
        
        card.add(labelComponent, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createHighlightedCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(new Color(245, 255, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 60), 2),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)));
        
        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        labelComponent.setForeground(new Color(0, 100, 50));
        
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valuePanel.setBackground(new Color(245, 255, 250));
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.BOLD, 28));
        valueComponent.setForeground(new Color(0, 120, 60));
        valuePanel.add(valueComponent);
        
        card.add(labelComponent, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }

    public void refresh() {
        try {
            com.banking.Account account = bankingSystem.getAccount(accountNumber);
            accountNumberLabel.setText(account.getAccountNumber());
            holderNameLabel.setText(account.getHolderName());
            balanceLabel.setText("$" + String.format("%.2f", account.getBalance()));
            
            String status = account.getStatus().toString();
            statusLabel.setText(status);
            if (status.equals("ACTIVE")) {
                statusLabel.setForeground(new Color(0, 102, 204));
                statusLabel.setFont(new Font("Arial", Font.BOLD, 15));
            } else if (status.equals("FROZEN")) {
                statusLabel.setForeground(new Color(204, 102, 0));
                statusLabel.setFont(new Font("Arial", Font.BOLD, 15));
            } else {
                statusLabel.setForeground(new Color(150, 150, 150));
                statusLabel.setFont(new Font("Arial", Font.BOLD, 15));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading account info: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
