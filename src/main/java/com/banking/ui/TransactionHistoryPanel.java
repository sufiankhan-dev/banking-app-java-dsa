package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;
import com.banking.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
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

        transactionTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String firstCellValue = getValueAt(row, 0) != null ? getValueAt(row, 0).toString() : "";
                if (firstCellValue.equals("No transactions found")) {
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
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setRowHeight(32);
        transactionTable.getTableHeader().setReorderingAllowed(false);
        transactionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        transactionTable.setBackground(Color.WHITE);
        transactionTable.setForeground(new Color(30, 30, 30));
        transactionTable.setSelectionBackground(new Color(0, 102, 204));
        transactionTable.setSelectionForeground(Color.WHITE);
        transactionTable.getTableHeader().setBackground(new Color(0, 102, 204));
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        transactionTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 12));
        transactionTable.setGridColor(new Color(230, 230, 235));
        transactionTable.setShowGrid(true);
        transactionTable.setIntercellSpacing(new Dimension(0, 0));
        
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setPreferredSize(new Dimension(750, 400));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 225), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        filterPanel.setBackground(new Color(255, 255, 255));
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 225), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        JLabel filterLabel = new JLabel("Show Last N Transactions:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 13));
        filterLabel.setForeground(new Color(50, 50, 50));
        filterPanel.add(filterLabel);
        filterField = new JTextField(12);
        filterField.setPreferredSize(new Dimension(100, 30));
        filterField.setFont(new Font("Arial", Font.PLAIN, 12));
        filterField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        filterField.addActionListener(e -> filterTransactions());
        filterPanel.add(filterField);
        filterButton = new JButton("Filter");
        filterButton.setPreferredSize(new Dimension(90, 30));
        filterButton.setBackground(new Color(0, 102, 204));
        filterButton.setForeground(Color.WHITE);
        filterButton.setFont(new Font("Arial", Font.BOLD, 12));
        filterButton.setFocusPainted(false);
        filterButton.setOpaque(true);
        filterButton.setContentAreaFilled(true);
        filterButton.setBorderPainted(true);
        filterButton.setBorder(BorderFactory.createRaisedBevelBorder());
        filterButton.addActionListener(e -> filterTransactions());
        filterPanel.add(filterButton);
        showAllButton = new JButton("Show All");
        showAllButton.setPreferredSize(new Dimension(100, 30));
        showAllButton.setBackground(new Color(0, 102, 204));
        showAllButton.setForeground(Color.WHITE);
        showAllButton.setFont(new Font("Arial", Font.BOLD, 12));
        showAllButton.setFocusPainted(false);
        showAllButton.setOpaque(true);
        showAllButton.setContentAreaFilled(true);
        showAllButton.setBorderPainted(true);
        showAllButton.setBorder(BorderFactory.createRaisedBevelBorder());
        showAllButton.addActionListener(e -> loadAllTransactions());
        filterPanel.add(showAllButton);

        JLabel titleLabel = new JLabel("Transaction History for Account: " + accountNumber);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 70, 150));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(filterPanel, BorderLayout.SOUTH);
    }

    private void loadAllTransactions() {
        tableModel.setRowCount(0);
        List<Transaction> transactions = bankingSystem.getTransactionHistory(accountNumber);
        
        if (transactions.isEmpty()) {
            tableModel.addRow(new Object[]{"No transactions found", "", "", "", ""});
            return;
        }

        java.util.Collections.reverse(transactions);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        for (Transaction txn : transactions) {
            String txnId = txn.getTransactionId().substring(0, 8);
            String type = txn.getType().toString();
            String amount = "$" + String.format("%.2f", txn.getAmount());
            String date = txn.getDate().format(formatter);
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

            tableModel.setRowCount(0);
            
            if (totalTransactions == 0) {
                tableModel.addRow(new Object[]{"No transactions found", "", "", "", ""});
                return;
            }

            String message = "";
            if (n > totalTransactions) {
                message = "You requested " + n + " transactions, but only " + totalTransactions + " available.\nShowing all " + totalTransactions + " transactions.";
                JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
            }

            List<Transaction> transactions = bankingSystem.getLastNTransactions(accountNumber, n);

            java.util.Collections.reverse(transactions);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            for (Transaction txn : transactions) {
                String txnId = txn.getTransactionId().substring(0, 8);
                String type = txn.getType().toString();
                String amount = "$" + String.format("%.2f", txn.getAmount());
                String date = txn.getDate().format(formatter);
                String relatedAccount = txn.getRelatedAccount() != null ? txn.getRelatedAccount() : "-";
                
                tableModel.addRow(new Object[]{txnId, type, amount, date, relatedAccount});
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
