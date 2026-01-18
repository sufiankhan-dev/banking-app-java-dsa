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
    private JPanel formPanel;

    public CreateAccountDialog(JFrame parent) {
        super(parent, "Create New Account", true);
        this.bankingSystem = Main.bankingSystem;
        initializeUI();
    }

    private void initializeUI() {
        setSize(600, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel accountLabel = new JLabel("Account Number:");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        accountLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(accountLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        accountNumberField = new JTextField(25);
        accountNumberField.setMinimumSize(new Dimension(250, 30));
        accountNumberField.setPreferredSize(new Dimension(250, 30));
        accountNumberField.setMaximumSize(new Dimension(300, 30));
        accountNumberField.setFont(new Font("Arial", Font.PLAIN, 12));
        accountNumberField.setForeground(Color.BLACK);
        accountNumberField.setBackground(Color.WHITE);
        accountNumberField.setEnabled(true);
        accountNumberField.setEditable(true);
        accountNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        accountNumberField.getDocument().addDocumentListener(new AccountNumberValidator());
        formPanel.add(accountNumberField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        accountNumberError = new JLabel(" ");
        accountNumberError.setForeground(Color.RED);
        accountNumberError.setFont(new Font("Arial", Font.PLAIN, 11));
        accountNumberError.setPreferredSize(new Dimension(200, 0));
        accountNumberError.setMinimumSize(new Dimension(200, 0));
        accountNumberError.setMaximumSize(new Dimension(200, 0));
        formPanel.add(accountNumberError, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel holderLabel = new JLabel("Holder Name:");
        holderLabel.setFont(new Font("Arial", Font.BOLD, 12));
        holderLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(holderLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        holderNameField = new JTextField(25);
        holderNameField.setMinimumSize(new Dimension(250, 30));
        holderNameField.setPreferredSize(new Dimension(250, 30));
        holderNameField.setMaximumSize(new Dimension(300, 30));
        holderNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        holderNameField.setForeground(Color.BLACK);
        holderNameField.setBackground(Color.WHITE);
        holderNameField.setEnabled(true);
        holderNameField.setEditable(true);
        holderNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        holderNameField.getDocument().addDocumentListener(new HolderNameValidator());
        formPanel.add(holderNameField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        holderNameError = new JLabel(" ");
        holderNameError.setForeground(Color.RED);
        holderNameError.setFont(new Font("Arial", Font.PLAIN, 11));
        holderNameError.setPreferredSize(new Dimension(200, 0));
        holderNameError.setMinimumSize(new Dimension(200, 0));
        holderNameError.setMaximumSize(new Dimension(200, 0));
        formPanel.add(holderNameError, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel balanceLabel = new JLabel("Initial Balance:");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        balanceLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(balanceLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        balanceField = new JTextField(25);
        balanceField.setMinimumSize(new Dimension(250, 30));
        balanceField.setPreferredSize(new Dimension(250, 30));
        balanceField.setMaximumSize(new Dimension(300, 30));
        balanceField.setFont(new Font("Arial", Font.PLAIN, 12));
        balanceField.setForeground(Color.BLACK);
        balanceField.setBackground(Color.WHITE);
        balanceField.setEnabled(true);
        balanceField.setEditable(true);
        balanceField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        balanceField.getDocument().addDocumentListener(new BalanceValidator());
        formPanel.add(balanceField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        balanceError = new JLabel(" ");
        balanceError.setForeground(Color.RED);
        balanceError.setFont(new Font("Arial", Font.PLAIN, 11));
        balanceError.setPreferredSize(new Dimension(200, 0));
        balanceError.setMinimumSize(new Dimension(200, 0));
        balanceError.setMaximumSize(new Dimension(200, 0));
        formPanel.add(balanceError, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.BOLD, 12));
        pinLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(pinLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        pinField = new JPasswordField(25);
        pinField.setMinimumSize(new Dimension(250, 30));
        pinField.setPreferredSize(new Dimension(250, 30));
        pinField.setMaximumSize(new Dimension(300, 30));
        pinField.setFont(new Font("Arial", Font.PLAIN, 12));
        pinField.setForeground(Color.BLACK);
        pinField.setBackground(Color.WHITE);
        pinField.setEnabled(true);
        pinField.setEditable(true);
        pinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        pinField.getDocument().addDocumentListener(new PinValidator());
        formPanel.add(pinField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;

        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel confirmPinLabel = new JLabel("Confirm PIN:");
        confirmPinLabel.setFont(new Font("Arial", Font.BOLD, 12));
        confirmPinLabel.setForeground(new Color(50, 50, 50));
        formPanel.add(confirmPinLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        confirmPinField = new JPasswordField(25);
        confirmPinField.setMinimumSize(new Dimension(250, 30));
        confirmPinField.setPreferredSize(new Dimension(250, 30));
        confirmPinField.setMaximumSize(new Dimension(300, 30));
        confirmPinField.setForeground(Color.BLACK);
        confirmPinField.setBackground(Color.WHITE);
        confirmPinField.setEnabled(true);
        confirmPinField.setEditable(true);
        confirmPinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        confirmPinField.getDocument().addDocumentListener(new PinValidator());
        formPanel.add(confirmPinField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        pinError = new JLabel(" ");
        pinError.setForeground(Color.RED);
        pinError.setFont(new Font("Arial", Font.PLAIN, 11));
        pinError.setPreferredSize(new Dimension(200, 0));
        pinError.setMinimumSize(new Dimension(200, 0));
        pinError.setMaximumSize(new Dimension(200, 0));
        // PIN error label is shared for both PIN and Confirm PIN fields
        formPanel.add(pinError, gbc);
        gbc.gridwidth = 1;

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
        
        SwingUtilities.invokeLater(() -> {
            accountNumberField.requestFocus();
        });
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
                accountNumberError.setPreferredSize(new Dimension(200, 20));
                accountNumberError.setMinimumSize(new Dimension(200, 20));
                accountNumberError.setMaximumSize(new Dimension(200, 20));
            } else if (bankingSystem.accountExists(accountNumber)) {
                accountNumberError.setText("Account number already exists");
                accountNumberError.setPreferredSize(new Dimension(200, 20));
                accountNumberError.setMinimumSize(new Dimension(200, 20));
                accountNumberError.setMaximumSize(new Dimension(200, 20));
            } else {
                accountNumberError.setText(" ");
                accountNumberError.setPreferredSize(new Dimension(200, 0));
                accountNumberError.setMinimumSize(new Dimension(200, 0));
                accountNumberError.setMaximumSize(new Dimension(200, 0));
            }
            formPanel.revalidate();
            formPanel.repaint();
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
                holderNameError.setPreferredSize(new Dimension(200, 20));
                holderNameError.setMinimumSize(new Dimension(200, 20));
                holderNameError.setMaximumSize(new Dimension(200, 20));
            } else {
                holderNameError.setText(" ");
                holderNameError.setPreferredSize(new Dimension(200, 0));
                holderNameError.setMinimumSize(new Dimension(200, 0));
                holderNameError.setMaximumSize(new Dimension(200, 0));
            }
            formPanel.revalidate();
            formPanel.repaint();
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
                balanceError.setPreferredSize(new Dimension(200, 20));
                balanceError.setMinimumSize(new Dimension(200, 20));
                balanceError.setMaximumSize(new Dimension(200, 20));
            } else {
                try {
                    double balance = Double.parseDouble(balanceText);
                    if (balance < 0) {
                        balanceError.setText("Balance cannot be negative");
                        balanceError.setPreferredSize(new Dimension(200, 20));
                        balanceError.setMinimumSize(new Dimension(200, 20));
                        balanceError.setMaximumSize(new Dimension(200, 20));
                    } else {
                        balanceError.setText(" ");
                        balanceError.setPreferredSize(new Dimension(200, 0));
                        balanceError.setMinimumSize(new Dimension(200, 0));
                        balanceError.setMaximumSize(new Dimension(200, 0));
                    }
                } catch (NumberFormatException ex) {
                    balanceError.setText("Invalid number format");
                    balanceError.setPreferredSize(new Dimension(200, 20));
                    balanceError.setMinimumSize(new Dimension(200, 20));
                    balanceError.setMaximumSize(new Dimension(200, 20));
                }
            }
            formPanel.revalidate();
            formPanel.repaint();
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
                pinError.setPreferredSize(new Dimension(200, 20));
                pinError.setMinimumSize(new Dimension(200, 20));
                pinError.setMaximumSize(new Dimension(200, 20));
            } else if (!pin.equals(confirmPin)) {
                pinError.setText("PINs do not match");
                pinError.setPreferredSize(new Dimension(200, 20));
                pinError.setMinimumSize(new Dimension(200, 20));
                pinError.setMaximumSize(new Dimension(200, 20));
            } else {
                pinError.setText(" ");
                pinError.setPreferredSize(new Dimension(200, 0));
                pinError.setMinimumSize(new Dimension(200, 0));
                pinError.setMaximumSize(new Dimension(200, 0));
            }
            formPanel.revalidate();
            formPanel.repaint();
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
