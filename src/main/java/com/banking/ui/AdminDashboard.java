package com.banking.ui;

import com.banking.Account;
import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboard extends JFrame {
    private BankingSystem bankingSystem;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JTextArea statisticsArea;

    public AdminDashboard() {
        this.bankingSystem = Main.bankingSystem;
        initializeUI();
        loadAllAccounts();
        updateStatistics();
    }

    private void initializeUI() {
        setTitle("Banking System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(204, 102, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton exitButton = new JButton("Exit Admin Mode");
        exitButton.setPreferredSize(new Dimension(150, 30));
        exitButton.setBackground(new Color(204, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            dispose();
        });
        headerPanel.add(exitButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Admin Operations"));
        buttonPanel.setPreferredSize(new Dimension(250, 0));

        JButton viewAllButton = createAdminButton("View All Accounts", new Color(0, 102, 204));
        viewAllButton.addActionListener(e -> loadAllAccounts());

        JButton searchButton = createAdminButton("Search Account", new Color(0, 153, 76));
        searchButton.addActionListener(e -> searchAccounts());

        JButton freezeButton = createAdminButton("Freeze Account", new Color(204, 102, 0));
        freezeButton.addActionListener(e -> freezeAccount());

        JButton unfreezeButton = createAdminButton("Unfreeze Account", new Color(153, 153, 0));
        unfreezeButton.addActionListener(e -> unfreezeAccount());

        JButton closeAccountButton = createAdminButton("Close Account", new Color(204, 0, 0));
        closeAccountButton.addActionListener(e -> closeAccount());

        JButton viewFrozenButton = createAdminButton("View Frozen Accounts", new Color(153, 0, 153));
        viewFrozenButton.addActionListener(e -> viewFrozenAccounts());

        JButton statisticsButton = createAdminButton("Bank Statistics", new Color(102, 102, 102));
        statisticsButton.addActionListener(e -> updateStatistics());

        buttonPanel.add(viewAllButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(freezeButton);
        buttonPanel.add(unfreezeButton);
        buttonPanel.add(closeAccountButton);
        buttonPanel.add(viewFrozenButton);
        buttonPanel.add(statisticsButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchField = new JTextField(15);
        searchTypeCombo = new JComboBox<>(new String[]{"By Number", "By Name"});
        JButton searchNowButton = new JButton("Search");
        searchNowButton.addActionListener(e -> searchAccounts());
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchNowButton);

        String[] columnNames = {"Account Number", "Holder Name", "Balance", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        accountTable = new JTable(tableModel);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountTable.setRowHeight(25);
        accountTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScrollPane = new JScrollPane(accountTable);

        statisticsArea = new JTextArea(8, 30);
        statisticsArea.setEditable(false);
        statisticsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statisticsArea.setBorder(BorderFactory.createTitledBorder("Bank Statistics"));
        JScrollPane statisticsScrollPane = new JScrollPane(statisticsArea);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(statisticsScrollPane, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createAdminButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        return button;
    }

    private void loadAllAccounts() {
        tableModel.setRowCount(0);
        String allAccounts = bankingSystem.displayAllAccounts();
        if (allAccounts.equals("No accounts found in the system.")) {
            return;
        }
        String[] lines = allAccounts.split("\n");
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) continue;
            parseAndAddAccountLine(lines[i]);
        }
    }

    private void parseAndAddAccountLine(String line) {
        String[] parts = line.split("\\|");
        String accountNumber = "";
        String holderName = "";
        String balance = "";
        String status = "";

        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("Account Number:")) {
                accountNumber = part.substring("Account Number:".length()).trim();
            } else if (part.startsWith("Holder:")) {
                holderName = part.substring("Holder:".length()).trim();
            } else if (part.startsWith("Balance:")) {
                balance = part.substring("Balance:".length()).trim();
            } else if (part.startsWith("Status:")) {
                status = part.substring("Status:".length()).trim();
            }
        }
        tableModel.addRow(new Object[]{accountNumber, holderName, balance, status});
    }

    private void searchAccounts() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        List<Account> results;
        
        if (searchTypeCombo.getSelectedItem().equals("By Number")) {
            results = bankingSystem.searchAccountsByNumber(searchTerm);
        } else {
            results = bankingSystem.searchAccountsByName(searchTerm);
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No accounts found matching: " + searchTerm, "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Account account : results) {
            tableModel.addRow(new Object[]{
                account.getAccountNumber(),
                account.getHolderName(),
                "$" + String.format("%.2f", account.getBalance()),
                account.getStatus().toString()
            });
        }
    }

    private void freezeAccount() {
        String accountNumber = getSelectedAccountNumber();
        if (accountNumber == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to freeze account: " + accountNumber + "?",
            "Confirm Freeze", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bankingSystem.freezeAccount(accountNumber);
                JOptionPane.showMessageDialog(this, "Account frozen successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllAccounts();
                updateStatistics();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void unfreezeAccount() {
        String accountNumber = getSelectedAccountNumber();
        if (accountNumber == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to unfreeze account: " + accountNumber + "?",
            "Confirm Unfreeze", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bankingSystem.unfreezeAccount(accountNumber);
                JOptionPane.showMessageDialog(this, "Account unfrozen successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllAccounts();
                updateStatistics();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void closeAccount() {
        String accountNumber = getSelectedAccountNumber();
        if (accountNumber == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to CLOSE account: " + accountNumber + "?\nThis action cannot be undone!",
            "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bankingSystem.closeAccount(accountNumber);
                JOptionPane.showMessageDialog(this, "Account closed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAllAccounts();
                updateStatistics();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewFrozenAccounts() {
        tableModel.setRowCount(0);
        List<Account> frozenAccounts = bankingSystem.getFrozenAccounts();
        
        if (frozenAccounts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No frozen accounts found.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Account account : frozenAccounts) {
            tableModel.addRow(new Object[]{
                account.getAccountNumber(),
                account.getHolderName(),
                "$" + String.format("%.2f", account.getBalance()),
                account.getStatus().toString()
            });
        }
    }

    private void updateStatistics() {
        String statistics = bankingSystem.getBankStatistics();
        statisticsArea.setText(statistics);
    }

    private String getSelectedAccountNumber() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (String) tableModel.getValueAt(selectedRow, 0);
    }
}
