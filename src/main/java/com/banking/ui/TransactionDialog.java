package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import java.awt.*;

public class TransactionDialog extends JDialog {

    public enum TransactionType { DEPOSIT, WITHDRAW, TRANSFER }

    private final TransactionType type;
    private final BankingSystem bankingSystem = Main.bankingSystem;
    private final String accountNumber;

    private JTextField amountField;
    private JTextField receiverField;
    private JLabel errorLabel;

    public TransactionDialog(JFrame parent, TransactionType type) {
        super(parent, title(type), true);
        this.type = type;
        this.accountNumber = bankingSystem.getCurrentLoggedInAccount();
        initUI();
    }

    private static String title(TransactionType t) {
        return switch (t) {
            case DEPOSIT -> "Deposit Money";
            case WITHDRAW -> "Withdraw Money";
            case TRANSFER -> "Transfer Funds";
        };
    }

    private void initUI() {
        setSize(420, type == TransactionType.TRANSFER ? 260 : 210);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(Theme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(root);

        root.add(formPanel(), BorderLayout.CENTER);
        root.add(buttonPanel(), BorderLayout.SOUTH);
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        if (type == TransactionType.TRANSFER) {
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(label("Receiver Account"), gbc);

            gbc.gridx = 1;
            receiverField = input();
            panel.add(receiverField, gbc);
            row++;
        }

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(label("Amount"), gbc);

        gbc.gridx = 1;
        amountField = input();
        panel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = row + 1;
        gbc.gridwidth = 2;
        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(Theme.BODY_FONT);
        panel.add(errorLabel, gbc);

        return panel;
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panel.setBackground(Theme.BACKGROUND_COLOR);

        panel.add(primaryButton(actionText(), this::submit));
        panel.add(secondaryButton("Cancel", this::dispose));

        return panel;
    }

    // ---------- Actions ----------

    private void submit() {
        String amtText = amountField.getText().trim();

        if (amtText.isEmpty()) {
            error("Amount required");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtText);
        } catch (NumberFormatException e) {
            error("Invalid amount");
            return;
        }

        if (amount <= 0) {
            error("Amount must be greater than zero");
            return;
        }

        try {
            switch (type) {
                case DEPOSIT -> bankingSystem.deposit(accountNumber, amount);
                case WITHDRAW -> bankingSystem.withdraw(accountNumber, amount);
                case TRANSFER -> {
                    String receiver = receiverField.getText().trim();
                    if (receiver.isEmpty()) {
                        error("Receiver account required");
                        return;
                    }
                    bankingSystem.transferFunds(accountNumber, receiver, amount);
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    successMessage(amount),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    // ---------- Helpers ----------

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.SUBHEADER_FONT);
        l.setForeground(Theme.TEXT_SECONDARY);
        return l;
    }

    private JTextField input() {
        JTextField f = new JTextField(15);
        f.setFont(Theme.BODY_FONT);
        f.setForeground(Theme.TEXT_PRIMARY);
        f.setBackground(Theme.PANEL_COLOR);
        f.setCaretColor(Theme.TEXT_PRIMARY);
        f.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        return f;
    }

    private JButton primaryButton(String text, Runnable action) {
        JButton b = new JButton(text);
        b.setFont(Theme.SUBHEADER_FONT);
        b.setBackground(Theme.BUTTON_PRIMARY);
        b.setForeground(Theme.BUTTON_TEXT);
        b.setFocusPainted(false);
        b.addActionListener(e -> action.run());
        return b;
    }

    private JButton secondaryButton(String text, Runnable action) {
        JButton b = new JButton(text);
        b.setFont(Theme.SUBHEADER_FONT);
        b.setBackground(Theme.PANEL_COLOR);
        b.setForeground(Theme.TEXT_PRIMARY);
        b.setFocusPainted(false);
        b.addActionListener(e -> action.run());
        return b;
    }

    private void error(String msg) {
        errorLabel.setText(msg);
    }

    private String actionText() {
        return switch (type) {
            case DEPOSIT -> "Deposit";
            case WITHDRAW -> "Withdraw";
            case TRANSFER -> "Transfer";
        };
    }

    private String successMessage(double amt) {
        return actionText() + " successful.\nBalance: $" +
                String.format("%.2f", bankingSystem.getBalance(accountNumber));
    }
}
