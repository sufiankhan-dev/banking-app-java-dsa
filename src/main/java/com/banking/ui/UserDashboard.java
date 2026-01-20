package com.banking.ui;

import com.banking.Account;
import com.banking.BankingSystem;
import com.banking.Main;
import com.banking.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserDashboard extends JFrame {

    private BankingSystem bankingSystem;
    private String accountNumber;
    private AccountInfoPanel accountInfoPanel;
    private JLabel statusLabel;
    private JTable recentTransactionsTable;
    private DefaultTableModel transactionTableModel;
    private JLabel welcomeLabel;
    private JLabel usernameLabel;

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
        mainPanel.setBackground(Theme.BACKGROUND_COLOR);

        // HEADER
        Account account = bankingSystem.getAccount(accountNumber);
        welcomeLabel = new JLabel("Welcome, " + account.getHolderName());
        welcomeLabel.setFont(Theme.HEADER_FONT);
        welcomeLabel.setForeground(Theme.TEXT_PRIMARY);

        usernameLabel = new JLabel("@" + account.getUsername());
        usernameLabel.setFont(Theme.SUBHEADER_FONT);
        usernameLabel.setForeground(Theme.TEXT_SECONDARY);

        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        welcomePanel.setBackground(Theme.PANEL_COLOR);
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(usernameLabel);

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, Theme.BUTTON_PRIMARY);
        Theme.addHoverEffect(
                logoutButton,
                Theme.BUTTON_PRIMARY,
                Theme.BUTTON_PRIMARY_HOVER
        );
        logoutButton.addActionListener(e -> {
            bankingSystem.logout();
            new MainWindow().setVisible(true);
            dispose();
        });

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.PANEL_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        headerPanel.add(welcomePanel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 12, 12));
        buttonPanel.setBackground(Theme.PANEL_COLOR);
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                "Banking Operations",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                Theme.SUBHEADER_FONT,
                Theme.TEXT_PRIMARY
        ));
        buttonPanel.setPreferredSize(new Dimension(220, 0));

        JButton depositButton = createActionButton("Deposit Money");
        Theme.addHoverEffect(
                depositButton,
                Theme.BUTTON_PRIMARY,
                Theme.BUTTON_PRIMARY_HOVER
        );
        depositButton.addActionListener(e -> performDeposit());

        JButton withdrawButton = createActionButton("Withdraw Money");
        Theme.addHoverEffect(
                withdrawButton,
                Theme.BUTTON_PRIMARY,
                Theme.BUTTON_PRIMARY_HOVER
        );
        withdrawButton.addActionListener(e -> performWithdraw());

        JButton transferButton = createActionButton("Transfer Funds");
        Theme.addHoverEffect(
                transferButton,
                Theme.BUTTON_PRIMARY,
                Theme.BUTTON_PRIMARY_HOVER
        );
        transferButton.addActionListener(e -> performTransfer());

        JButton viewHistoryButton = createActionButton("View Transaction History");
        Theme.addHoverEffect(
                viewHistoryButton,
                Theme.BUTTON_PRIMARY,
                Theme.BUTTON_PRIMARY_HOVER
        );
        viewHistoryButton.addActionListener(e -> showTransactionHistory());

        JButton updateInfoButton = createActionButton("Update Account Info");
        Theme.addHoverEffect(
                updateInfoButton,
                Theme.BUTTON_PRIMARY,
                Theme.BUTTON_PRIMARY_HOVER
        );
        updateInfoButton.addActionListener(e -> updateAccountInfo());

        JButton changePinButton = createActionButton("Change PIN");
        Theme.addHoverEffect(
                changePinButton,
                Theme.BUTTON_PRIMARY,
                Theme.BUTTON_PRIMARY_HOVER
        );
        changePinButton.addActionListener(e -> changePin());

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transferButton);
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(changePinButton);

        // CENTER PANEL
        accountInfoPanel = new AccountInfoPanel(accountNumber);
        accountInfoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                "Account Information",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                Theme.SUBHEADER_FONT,
                Theme.TEXT_PRIMARY
        ));

        JPanel centerContentPanel = new JPanel(new BorderLayout(15, 15));
        centerContentPanel.setBackground(Theme.BACKGROUND_COLOR);
        centerContentPanel.add(accountInfoPanel, BorderLayout.NORTH);
        centerContentPanel.add(createRecentTransactionsPanel(), BorderLayout.CENTER);

        // STATUS LABEL
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(Theme.SUBHEADER_FONT);
        statusLabel.setForeground(Theme.TEXT_SECONDARY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Theme.PANEL_COLOR);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(centerContentPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, Theme.BUTTON_PRIMARY);
        return button;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Theme.BUTTON_TEXT);
        button.setFont(Theme.SUBHEADER_FONT);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        button.setPreferredSize(new Dimension(200, 50));
    }

    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.PANEL_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR),
                "Recent Transactions (Last 5)",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                Theme.SUBHEADER_FONT,
                Theme.TEXT_PRIMARY
        ));

        String[] columnNames = {"Type", "Amount", "Date", "Related Account"};
        transactionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        recentTransactionsTable = new JTable(transactionTableModel);
        recentTransactionsTable.setRowHeight(30);
        recentTransactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentTransactionsTable.setFont(Theme.BODY_FONT);
        recentTransactionsTable.setBackground(Theme.CARD_COLOR);
        recentTransactionsTable.setForeground(Theme.TEXT_PRIMARY);
        recentTransactionsTable.getTableHeader().setFont(Theme.SUBHEADER_FONT);
        recentTransactionsTable.getTableHeader().setBackground(Theme.PRIMARY_COLOR);
        recentTransactionsTable.getTableHeader().setForeground(Theme.TEXT_PRIMARY);
        recentTransactionsTable.setGridColor(Theme.BORDER_COLOR);

        JScrollPane scrollPane = new JScrollPane(recentTransactionsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadRecentTransactions() {
        transactionTableModel.setRowCount(0);
        List<Transaction> transactions = bankingSystem.getLastNTransactions(accountNumber, 5);

        if (transactions.isEmpty()) {
            transactionTableModel.addRow(new Object[]{"No transactions", "", "", ""});
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        for (Transaction txn : transactions) {
            String type = txn.getType().toString();
            String amount = "$" + String.format("%.2f", txn.getAmount());
            String date = txn.getDate().format(formatter);
            String relatedAccount = txn.getRelatedAccount() != null ? txn.getRelatedAccount() : "-";
            transactionTableModel.addRow(new Object[]{type, amount, date, relatedAccount});
        }
    }

    public void refreshAccountInfo() {
        accountInfoPanel.refresh();
        loadRecentTransactions();
        statusLabel.setText("Last updated: " + java.time.LocalDateTime.now());
    }

    // ------------------ ACTION METHODS ------------------

    private void performDeposit() {
        String input = JOptionPane.showInputDialog(this, "Enter Amount to Deposit:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            double amount = Double.parseDouble(input.trim());
            if (amount <= 0) throw new Exception("Amount must be > 0");
            bankingSystem.deposit(accountNumber, amount);
            JOptionPane.showMessageDialog(this, "Deposit successful!\nNew Balance: $" + bankingSystem.getBalance(accountNumber));
            refreshAccountInfo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void performWithdraw() {
        String input = JOptionPane.showInputDialog(this, "Enter Amount to Withdraw:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            double amount = Double.parseDouble(input.trim());
            if (amount <= 0) throw new Exception("Amount must be > 0");
            bankingSystem.withdraw(accountNumber, amount);
            JOptionPane.showMessageDialog(this, "Withdrawal successful!\nRemaining Balance: $" + bankingSystem.getBalance(accountNumber));
            refreshAccountInfo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void performTransfer() {
        String receiver = JOptionPane.showInputDialog(this, "Receiver Account Number:");
        if (receiver == null || receiver.trim().isEmpty()) return;

        String amountStr = JOptionPane.showInputDialog(this, "Amount to Transfer:");
        if (amountStr == null || amountStr.trim().isEmpty()) return;

        try {
            double amount = Double.parseDouble(amountStr.trim());
            if (amount <= 0) throw new Exception("Amount must be > 0");
            bankingSystem.transferFunds(accountNumber, receiver.trim(), amount);
            JOptionPane.showMessageDialog(this, "Transfer successful!\nRemaining Balance: $" + bankingSystem.getBalance(accountNumber));
            refreshAccountInfo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateAccountInfo() {
        Account acc = bankingSystem.getAccount(accountNumber);
        JTextField usernameField = new JTextField(acc.getUsername());
        JTextField holderField = new JTextField(acc.getHolderName());
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Username:")); panel.add(usernameField);
        panel.add(new JLabel("Holder Name:")); panel.add(holderField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Account Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                bankingSystem.updateUsername(accountNumber, usernameField.getText().trim());
                bankingSystem.updateAccountInfo(accountNumber, holderField.getText().trim());
                welcomeLabel.setText("Welcome, " + holderField.getText().trim());
                usernameLabel.setText("@" + usernameField.getText().trim());
                refreshAccountInfo();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void changePin() {
        String oldPin = JOptionPane.showInputDialog(this, "Current PIN:");
        if (oldPin == null) return;
        String newPin = JOptionPane.showInputDialog(this, "New PIN:");
        if (newPin == null) return;
        String confirm = JOptionPane.showInputDialog(this, "Confirm New PIN:");
        if (confirm == null) return;

        if (!newPin.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "PINs do not match!");
            return;
        }

        try {
            bankingSystem.changePin(accountNumber, oldPin, newPin);
            JOptionPane.showMessageDialog(this, "PIN changed successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void showTransactionHistory() {
        TransactionHistoryPanel historyPanel = new TransactionHistoryPanel(accountNumber);
        JDialog dialog = new JDialog(this, "Transaction History", true);
        dialog.add(historyPanel);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
