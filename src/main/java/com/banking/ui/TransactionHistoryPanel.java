package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;
import com.banking.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionHistoryPanel extends JPanel {
    private BankingSystem bankingSystem;
    private String accountNumber;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JTextField filterField;
    private JButton filterButton;
    private JButton showAllButton;

    public TransactionHistoryPanel(String accountNumber) {
        this.accountNumber = accountNumber;
        this.bankingSystem = Main.bankingSystem;
        initializeUI();
        loadAllTransactions();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 250));

        String[] columnNames = {"Transaction ID", "Type", "Amount", "Date", "Related Account"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setRowHeight(25);
        transactionTable.getTableHeader().setReorderingAllowed(false);
        transactionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        transactionTable.setBackground(Color.WHITE);
        transactionTable.setForeground(new Color(30, 30, 30));
        transactionTable.getTableHeader().setBackground(new Color(0, 102, 204));
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 11));
        transactionTable.setGridColor(new Color(200, 200, 200));
        
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setPreferredSize(new Dimension(750, 350));
        scrollPane.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(new Color(255, 255, 255));
        JLabel filterLabel = new JLabel("Show Last N Transactions:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        filterLabel.setForeground(new Color(50, 50, 50));
        filterPanel.add(filterLabel);
        filterField = new JTextField(10);
        filterPanel.add(filterField);
        filterButton = new JButton("Filter");
        filterButton.setBackground(new Color(0, 102, 204));
        filterButton.setForeground(Color.WHITE);
        filterButton.setFont(new Font("Arial", Font.BOLD, 11));
        filterButton.setFocusPainted(false);
        filterButton.setOpaque(true);
        filterButton.setContentAreaFilled(true);
        filterButton.setBorderPainted(true);
        filterButton.setBorder(BorderFactory.createRaisedBevelBorder());
        filterButton.addActionListener(e -> filterTransactions());
        filterPanel.add(filterButton);
        showAllButton = new JButton("Show All");
        showAllButton.setBackground(new Color(0, 102, 204));
        showAllButton.setForeground(Color.WHITE);
        showAllButton.setFont(new Font("Arial", Font.BOLD, 11));
        showAllButton.setFocusPainted(false);
        showAllButton.setOpaque(true);
        showAllButton.setContentAreaFilled(true);
        showAllButton.setBorderPainted(true);
        showAllButton.setBorder(BorderFactory.createRaisedBevelBorder());
        showAllButton.addActionListener(e -> loadAllTransactions());
        filterPanel.add(showAllButton);

        JLabel titleLabel = new JLabel("Transaction History for Account: " + accountNumber);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(0, 70, 150));
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(filterPanel, BorderLayout.SOUTH);
    }

    private void loadAllTransactions() {
        tableModel.setRowCount(0);
        List<Transaction> transactions = bankingSystem.getTransactionHistory(accountNumber);
        
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for this account.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Transaction txn : transactions) {
            String txnId = txn.getTransactionId().substring(0, 8);
            String type = txn.getType().toString();
            String amount = "$" + String.format("%.2f", txn.getAmount());
            String date = txn.getDate().toString();
            String relatedAccount = txn.getRelatedAccount() != null ? txn.getRelatedAccount() : "-";
            
            tableModel.addRow(new Object[]{txnId, type, amount, date, relatedAccount});
        }
    }

    private void filterTransactions() {
        String nText = filterField.getText().trim();
        if (nText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int n = Integer.parseInt(nText);
            if (n <= 0) {
                JOptionPane.showMessageDialog(this, "Number must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Transaction> allTransactions = bankingSystem.getTransactionHistory(accountNumber);
            int totalTransactions = allTransactions.size();

            if (totalTransactions == 0) {
                JOptionPane.showMessageDialog(this, "No transactions found for this account.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String message = "";
            if (n > totalTransactions) {
                message = "You requested " + n + " transactions, but only " + totalTransactions + " available.\nShowing all " + totalTransactions + " transactions.";
                JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
            }

            tableModel.setRowCount(0);
            List<Transaction> transactions = bankingSystem.getLastNTransactions(accountNumber, n);

            for (Transaction txn : transactions) {
                String txnId = txn.getTransactionId().substring(0, 8);
                String type = txn.getType().toString();
                String amount = "$" + String.format("%.2f", txn.getAmount());
                String date = txn.getDate().toString();
                String relatedAccount = txn.getRelatedAccount() != null ? txn.getRelatedAccount() : "-";
                
                tableModel.addRow(new Object[]{txnId, type, amount, date, relatedAccount});
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
