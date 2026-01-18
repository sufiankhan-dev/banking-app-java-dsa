package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountDialog extends JDialog {
    private JTextField accountNumberField;
    private JTextField holderNameField;
    private JTextField balanceField;
    private JPasswordField pinField;
    private JPasswordField confirmPinField;
    private JLabel accountNumberError;
    private JLabel holderNameError;
    private JLabel balanceError;
    private JLabel pinError;
    private JButton createButton;
    private BankingSystem bankingSystem;

    public CreateAccountDialog(JFrame parent) {
        super(parent, "Create New Account", true);
        this.bankingSystem = Main.bankingSystem;
        initializeUI();
    }

    private void initializeUI() {
        setSize(450, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel accountLabel = new JLabel("Account Number:");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        accountLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(accountLabel, gbc);
        gbc.gridx = 1;
        accountNumberField = new JTextField(20);
        accountNumberField.setFont(new Font("Arial", Font.PLAIN, 12));
        accountNumberField.getDocument().addDocumentListener(new AccountNumberValidator());
        formPanel.add(accountNumberField, gbc);
        gbc.gridx = 2;
        accountNumberError = new JLabel(" ");
        accountNumberError.setForeground(Color.RED);
        accountNumberError.setFont(new Font("Arial", Font.PLAIN, 11));
        accountNumberError.setPreferredSize(new Dimension(200, 20));
        formPanel.add(accountNumberError, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel holderLabel = new JLabel("Holder Name:");
        holderLabel.setFont(new Font("Arial", Font.BOLD, 12));
        holderLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(holderLabel, gbc);
        gbc.gridx = 1;
        holderNameField = new JTextField(20);
        holderNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        holderNameField.getDocument().addDocumentListener(new HolderNameValidator());
        formPanel.add(holderNameField, gbc);
        gbc.gridx = 2;
        holderNameError = new JLabel(" ");
        holderNameError.setForeground(Color.RED);
        holderNameError.setFont(new Font("Arial", Font.PLAIN, 11));
        holderNameError.setPreferredSize(new Dimension(200, 20));
        formPanel.add(holderNameError, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel balanceLabel = new JLabel("Initial Balance:");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        balanceLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(balanceLabel, gbc);
        gbc.gridx = 1;
        balanceField = new JTextField(20);
        balanceField.setFont(new Font("Arial", Font.PLAIN, 12));
        balanceField.getDocument().addDocumentListener(new BalanceValidator());
        formPanel.add(balanceField, gbc);
        gbc.gridx = 2;
        balanceError = new JLabel(" ");
        balanceError.setForeground(Color.RED);
        balanceError.setFont(new Font("Arial", Font.PLAIN, 11));
        balanceError.setPreferredSize(new Dimension(200, 20));
        formPanel.add(balanceError, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.BOLD, 12));
        pinLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(pinLabel, gbc);
        gbc.gridx = 1;
        pinField = new JPasswordField(20);
        pinField.setFont(new Font("Arial", Font.PLAIN, 12));
        pinField.getDocument().addDocumentListener(new PinValidator());
        formPanel.add(pinField, gbc);
        gbc.gridx = 2;
        pinError = new JLabel(" ");
        pinError.setForeground(Color.RED);
        pinError.setFont(new Font("Arial", Font.PLAIN, 11));
        pinError.setPreferredSize(new Dimension(200, 20));
        formPanel.add(pinError, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel confirmPinLabel = new JLabel("Confirm PIN:");
        confirmPinLabel.setFont(new Font("Arial", Font.BOLD, 12));
        confirmPinLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(confirmPinLabel, gbc);
        gbc.gridx = 1;
        confirmPinField = new JPasswordField(20);
        confirmPinField.getDocument().addDocumentListener(new PinValidator());
        formPanel.add(confirmPinField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));
        createButton = new JButton("Create Account");
        createButton.setPreferredSize(new Dimension(130, 40));
        createButton.setBackground(new Color(0, 102, 204));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Arial", Font.BOLD, 13));
        createButton.setFocusPainted(false);
        createButton.setOpaque(true);
        createButton.setContentAreaFilled(true);
        createButton.setBorderPainted(true);
        createButton.setBorder(BorderFactory.createRaisedBevelBorder());
        createButton.setEnabled(false);
        createButton.addActionListener(new CreateAccountActionListener());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(130, 40));
        cancelButton.setBackground(new Color(150, 150, 150));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 13));
        cancelButton.setFocusPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setBorderPainted(true);
        cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void validateForm() {
        boolean isValid = accountNumberField.getText().trim().length() > 0 &&
                holderNameField.getText().trim().length() > 0 &&
                balanceField.getText().trim().length() > 0 &&
                new String(pinField.getPassword()).trim().length() > 0 &&
                new String(confirmPinField.getPassword()).trim().length() > 0 &&
                accountNumberError.getText().equals(" ") &&
                holderNameError.getText().equals(" ") &&
                balanceError.getText().equals(" ") &&
                pinError.getText().equals(" ");
        createButton.setEnabled(isValid);
    }

    private class AccountNumberValidator implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validateAccountNumber();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateAccountNumber();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateAccountNumber();
        }

        private void validateAccountNumber() {
            String accountNumber = accountNumberField.getText().trim();
            if (accountNumber.isEmpty()) {
                accountNumberError.setText("Account number cannot be empty");
            } else if (bankingSystem.accountExists(accountNumber)) {
                accountNumberError.setText("Account number already exists");
            } else {
                accountNumberError.setText(" ");
            }
            validateForm();
        }
    }

    private class HolderNameValidator implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validateHolderName();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateHolderName();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateHolderName();
        }

        private void validateHolderName() {
            String holderName = holderNameField.getText().trim();
            if (holderName.isEmpty()) {
                holderNameError.setText("Holder name cannot be empty");
            } else {
                holderNameError.setText(" ");
            }
            validateForm();
        }
    }

    private class BalanceValidator implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validateBalance();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateBalance();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateBalance();
        }

        private void validateBalance() {
            String balanceText = balanceField.getText().trim();
            if (balanceText.isEmpty()) {
                balanceError.setText("Balance cannot be empty");
            } else {
                try {
                    double balance = Double.parseDouble(balanceText);
                    if (balance < 0) {
                        balanceError.setText("Balance cannot be negative");
                    } else {
                        balanceError.setText(" ");
                    }
                } catch (NumberFormatException ex) {
                    balanceError.setText("Invalid number format");
                }
            }
            validateForm();
        }
    }

    private class PinValidator implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validatePin();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validatePin();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validatePin();
        }

        private void validatePin() {
            String pin = new String(pinField.getPassword()).trim();
            String confirmPin = new String(confirmPinField.getPassword()).trim();
            
            if (pin.isEmpty()) {
                pinError.setText("PIN cannot be empty");
            } else if (!pin.equals(confirmPin)) {
                pinError.setText("PINs do not match");
            } else {
                pinError.setText(" ");
            }
            validateForm();
        }
    }

    private class CreateAccountActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String accountNumber = accountNumberField.getText().trim();
            String holderName = holderNameField.getText().trim();
            String balanceText = balanceField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            try {
                double initialBalance = Double.parseDouble(balanceText);
                bankingSystem.createAccount(accountNumber, holderName, initialBalance, pin);
                JOptionPane.showMessageDialog(CreateAccountDialog.this,
                        "Account created successfully!\nAccount Number: " + accountNumber,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CreateAccountDialog.this,
                        "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
