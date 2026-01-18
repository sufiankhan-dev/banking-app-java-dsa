package com.banking.ui;

import com.banking.Account;
import com.banking.BankingSystem;
import com.banking.Main;
import com.banking.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        mainPanel.setBackground(new Color(245, 245, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        Account account = bankingSystem.getAccount(accountNumber);
        String holderName = account.getHolderName();
        String username = account.getUsername();
        
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        welcomePanel.setBackground(new Color(245, 245, 250));
        welcomeLabel = new JLabel("Welcome, " + holderName + " ");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(0, 70, 150));
        welcomePanel.add(welcomeLabel);
        
        usernameLabel = new JLabel("@" + username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(100, 100, 120));
        welcomePanel.add(usernameLabel);
        
        headerPanel.add(welcomePanel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(110, 40));
        logoutButton.setBackground(new Color(0, 70, 150));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));
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

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 12, 12));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Banking Operations",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 70, 150)));
        buttonPanel.setPreferredSize(new Dimension(220, 0));

        JButton depositButton = createMenuButton("Deposit Money", new Color(0, 102, 204));
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

        JButton withdrawButton = createMenuButton("Withdraw Money", new Color(0, 102, 204));
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

        JButton transferButton = createMenuButton("Transfer Funds", new Color(0, 102, 204));
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

        JButton viewHistoryButton = createMenuButton("View Transaction History", new Color(0, 102, 204));
        viewHistoryButton.addActionListener(e -> {
            TransactionHistoryPanel historyPanel = new TransactionHistoryPanel(accountNumber);
            JDialog historyDialog = new JDialog(this, "Transaction History", true);
            historyDialog.add(historyPanel);
            historyDialog.setSize(800, 500);
            historyDialog.setLocationRelativeTo(this);
            historyDialog.setVisible(true);
        });

        JButton updateInfoButton = createMenuButton("Update Account Info", new Color(0, 102, 204));
        updateInfoButton.addActionListener(e -> {
            Account currentAccount = bankingSystem.getAccount(accountNumber);
            String currentUsername = currentAccount.getUsername();
            String currentHolderName = currentAccount.getHolderName();
            
            JPanel updatePanel = new JPanel(new GridBagLayout());
            updatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            updatePanel.add(new JLabel("Username:"), gbc);
            gbc.gridx = 1;
            JTextField usernameField = new JTextField(currentUsername, 20);
            updatePanel.add(usernameField, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            updatePanel.add(new JLabel("Holder Name:"), gbc);
            gbc.gridx = 1;
            JTextField holderNameField = new JTextField(currentHolderName, 20);
            updatePanel.add(holderNameField, gbc);
            
            int result = JOptionPane.showConfirmDialog(this, updatePanel, "Update Account Info", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                String newUsername = usernameField.getText().trim();
                String newHolderName = holderNameField.getText().trim();
                
                try {
                    if (!newUsername.isEmpty() && !newUsername.equals(currentUsername)) {
                        bankingSystem.updateUsername(accountNumber, newUsername);
                    }
                    if (!newHolderName.isEmpty() && !newHolderName.equals(currentHolderName)) {
                        bankingSystem.updateAccountInfo(accountNumber, newHolderName);
                    }
                    JOptionPane.showMessageDialog(this, "Account information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshAccountInfo();
                    updateHeader();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton changePinButton = createMenuButton("Change PIN", new Color(0, 102, 204));
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
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(changePinButton);

        accountInfoPanel = new AccountInfoPanel(accountNumber);
        accountInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Account Information",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 70, 150)));

        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        statusLabel.setPreferredSize(new Dimension(0, 35));
        statusLabel.setBackground(new Color(250, 250, 250));
        statusLabel.setOpaque(true);
        statusLabel.setForeground(new Color(80, 80, 80));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel centerContentPanel = new JPanel(new BorderLayout(15, 15));
        centerContentPanel.setBackground(new Color(245, 245, 250));
        centerContentPanel.add(accountInfoPanel, BorderLayout.NORTH);
        
        JPanel transactionsPanel = createRecentTransactionsPanel();
        centerContentPanel.add(transactionsPanel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(centerContentPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 55));
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

    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Recent Transactions (Last 5)",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 70, 150)));
        
        String[] columnNames = {"Type", "Amount", "Date", "Related Account"};
        transactionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        recentTransactionsTable = new JTable(transactionTableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String firstCellValue = getValueAt(row, 0) != null ? getValueAt(row, 0).toString() : "";
                if (firstCellValue.equals("No transactions")) {
                    c.setBackground(new Color(250, 250, 250));
                    c.setForeground(new Color(150, 150, 150));
                    if (c instanceof JLabel) {
                        ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    }
                } else if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 252));
                } else {
                    c.setBackground(new Color(0, 102, 204));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        recentTransactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentTransactionsTable.setRowHeight(30);
        recentTransactionsTable.getTableHeader().setReorderingAllowed(false);
        recentTransactionsTable.setBackground(Color.WHITE);
        recentTransactionsTable.setForeground(new Color(30, 30, 30));
        recentTransactionsTable.setSelectionBackground(new Color(0, 102, 204));
        recentTransactionsTable.setSelectionForeground(Color.WHITE);
        recentTransactionsTable.getTableHeader().setBackground(new Color(0, 102, 204));
        recentTransactionsTable.getTableHeader().setForeground(Color.WHITE);
        recentTransactionsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        recentTransactionsTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        recentTransactionsTable.setFont(new Font("Arial", Font.PLAIN, 11));
        recentTransactionsTable.setGridColor(new Color(230, 230, 235));
        recentTransactionsTable.setShowGrid(true);
        recentTransactionsTable.setIntercellSpacing(new Dimension(0, 0));
        
        recentTransactionsTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        recentTransactionsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        recentTransactionsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        recentTransactionsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(recentTransactionsTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 225), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
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
        
        java.util.Collections.reverse(transactions);
        
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
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        statusLabel.setText("Last updated: " + sdf.format(new java.util.Date()));
    }
    
    private void updateHeader() {
        Account account = bankingSystem.getAccount(accountNumber);
        String holderName = account.getHolderName();
        String username = account.getUsername();
        welcomeLabel.setText("Welcome, " + holderName + " ");
        usernameLabel.setText("@" + username);
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}
