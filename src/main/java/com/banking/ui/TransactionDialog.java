package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransactionDialog extends JDialog {
    public enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER
    }

    private TransactionType type;
    private BankingSystem bankingSystem;
    private String accountNumber;
    private JTextField amountField;
    private JTextField receiverAccountField;
    private JLabel errorLabel;

    public TransactionDialog(JFrame parent, TransactionType type) {
        super(parent, getTitle(type), true);
        this.type = type;
        this.bankingSystem = Main.bankingSystem;
        this.accountNumber = bankingSystem.getCurrentLoggedInAccount();
        initializeUI();
    }

    private static String getTitle(TransactionType type) {
        switch (type) {
            case DEPOSIT:
                return "Deposit Money";
            case WITHDRAW:
                return "Withdraw Money";
            case TRANSFER:
                return "Transfer Funds";
            default:
                return "Transaction";
        }
    }

    private void initializeUI() {
        setSize(400, type == TransactionType.TRANSFER ? 250 : 200);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        if (type == TransactionType.TRANSFER) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(new JLabel("Receiver Account Number:"), gbc);
            gbc.gridx = 1;
            receiverAccountField = new JTextField(15);
            formPanel.add(receiverAccountField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(new JLabel("Amount:"), gbc);
        } else {
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(new JLabel("Amount:"), gbc);
        }

        gbc.gridx = 1;
        amountField = new JTextField(15);
        formPanel.add(amountField, gbc);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = type == TransactionType.TRANSFER ? 2 : 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(errorLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton submitButton = new JButton(getSubmitButtonText());
        submitButton.setPreferredSize(new Dimension(120, 35));
        submitButton.setBackground(new Color(0, 153, 76));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(new SubmitActionListener());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private String getSubmitButtonText() {
        switch (type) {
            case DEPOSIT:
                return "Deposit";
            case WITHDRAW:
                return "Withdraw";
            case TRANSFER:
                return "Transfer";
            default:
                return "Submit";
        }
    }

    private class SubmitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String amountText = amountField.getText().trim();
            
            if (amountText.isEmpty()) {
                errorLabel.setText("Please enter an amount");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    errorLabel.setText("Amount must be greater than zero");
                    return;
                }

                if (type == TransactionType.DEPOSIT) {
                    bankingSystem.deposit(accountNumber, amount);
                    JOptionPane.showMessageDialog(TransactionDialog.this,
                            "Deposit successful!\nNew Balance: $" + String.format("%.2f", bankingSystem.getBalance(accountNumber)),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else if (type == TransactionType.WITHDRAW) {
                    bankingSystem.withdraw(accountNumber, amount);
                    JOptionPane.showMessageDialog(TransactionDialog.this,
                            "Withdrawal successful!\nRemaining Balance: $" + String.format("%.2f", bankingSystem.getBalance(accountNumber)),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else if (type == TransactionType.TRANSFER) {
                    String receiverAccount = receiverAccountField.getText().trim();
                    if (receiverAccount.isEmpty()) {
                        errorLabel.setText("Please enter receiver account number");
                        return;
                    }
                    bankingSystem.transferFunds(accountNumber, receiverAccount, amount);
                    JOptionPane.showMessageDialog(TransactionDialog.this,
                            "Transfer successful!\nYour Remaining Balance: $" + String.format("%.2f", bankingSystem.getBalance(accountNumber)),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid amount format");
            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
            }
        }
    }
}
