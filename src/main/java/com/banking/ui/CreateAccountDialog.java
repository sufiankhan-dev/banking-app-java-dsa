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

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        accountNumberField = new JTextField(20);
        accountNumberField.getDocument().addDocumentListener(new AccountNumberValidator());
        formPanel.add(accountNumberField, gbc);
        gbc.gridx = 2;
        accountNumberError = new JLabel(" ");
        accountNumberError.setForeground(Color.RED);
        accountNumberError.setPreferredSize(new Dimension(200, 20));
        formPanel.add(accountNumberError, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Holder Name:"), gbc);
        gbc.gridx = 1;
        holderNameField = new JTextField(20);
        holderNameField.getDocument().addDocumentListener(new HolderNameValidator());
        formPanel.add(holderNameField, gbc);
        gbc.gridx = 2;
        holderNameError = new JLabel(" ");
        holderNameError.setForeground(Color.RED);
        holderNameError.setPreferredSize(new Dimension(200, 20));
        formPanel.add(holderNameError, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Initial Balance:"), gbc);
        gbc.gridx = 1;
        balanceField = new JTextField(20);
        balanceField.getDocument().addDocumentListener(new BalanceValidator());
        formPanel.add(balanceField, gbc);
        gbc.gridx = 2;
        balanceError = new JLabel(" ");
        balanceError.setForeground(Color.RED);
        balanceError.setPreferredSize(new Dimension(200, 20));
        formPanel.add(balanceError, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("PIN:"), gbc);
        gbc.gridx = 1;
        pinField = new JPasswordField(20);
        pinField.getDocument().addDocumentListener(new PinValidator());
        formPanel.add(pinField, gbc);
        gbc.gridx = 2;
        pinError = new JLabel(" ");
        pinError.setForeground(Color.RED);
        pinError.setPreferredSize(new Dimension(200, 20));
        formPanel.add(pinError, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Confirm PIN:"), gbc);
        gbc.gridx = 1;
        confirmPinField = new JPasswordField(20);
        confirmPinField.getDocument().addDocumentListener(new PinValidator());
        formPanel.add(confirmPinField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        createButton = new JButton("Create Account");
        createButton.setPreferredSize(new Dimension(120, 35));
        createButton.setBackground(new Color(0, 153, 76));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setEnabled(false);
        createButton.addActionListener(new CreateAccountActionListener());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 35));
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
