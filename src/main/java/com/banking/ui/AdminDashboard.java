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
        mainPanel.setBackground(new Color(245, 245, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 70, 150));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton exitButton = new JButton("Exit Admin Mode");
        exitButton.setPreferredSize(new Dimension(150, 35));
        exitButton.setBackground(new Color(0, 70, 150));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.setFocusPainted(false);
        exitButton.setOpaque(true);
        exitButton.setContentAreaFilled(true);
        exitButton.setBorderPainted(true);
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exitButton.addActionListener(e -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            dispose();
        });
        headerPanel.add(exitButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Admin Operations",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(0, 70, 150)));
        buttonPanel.setPreferredSize(new Dimension(250, 0));

        JButton viewAllButton = createAdminButton("View All Accounts", new Color(0, 102, 204));
        viewAllButton.addActionListener(e -> loadAllAccounts());

        JButton searchButton = createAdminButton("Search Account", new Color(0, 102, 204));
        searchButton.addActionListener(e -> searchAccounts());

        JButton freezeButton = createAdminButton("Freeze Account", new Color(0, 102, 204));
        freezeButton.addActionListener(e -> freezeAccount());

        JButton unfreezeButton = createAdminButton("Unfreeze Account", new Color(0, 102, 204));
        unfreezeButton.addActionListener(e -> unfreezeAccount());

        JButton closeAccountButton = createAdminButton("Close Account", new Color(0, 102, 204));
        closeAccountButton.addActionListener(e -> closeAccount());

        JButton viewFrozenButton = createAdminButton("View Frozen Accounts", new Color(0, 102, 204));
        viewFrozenButton.addActionListener(e -> viewFrozenAccounts());

        JButton statisticsButton = createAdminButton("Bank Statistics", new Color(0, 102, 204));
        statisticsButton.addActionListener(e -> updateStatistics());

        buttonPanel.add(viewAllButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(freezeButton);
        buttonPanel.add(unfreezeButton);
        buttonPanel.add(closeAccountButton);
        buttonPanel.add(viewFrozenButton);
        buttonPanel.add(statisticsButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setBackground(new Color(255, 255, 255));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Search",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            new Color(0, 70, 150)));
        searchField = new JTextField(15);
        searchTypeCombo = new JComboBox<>(new String[]{"By Number", "By Name"});
        JButton searchNowButton = new JButton("Search");
        searchNowButton.setBackground(new Color(0, 102, 204));
        searchNowButton.setForeground(Color.WHITE);
        searchNowButton.setFont(new Font("Arial", Font.BOLD, 11));
        searchNowButton.setFocusPainted(false);
        searchNowButton.setOpaque(true);
        searchNowButton.setContentAreaFilled(true);
        searchNowButton.setBorderPainted(true);
        searchNowButton.setBorder(BorderFactory.createRaisedBevelBorder());
        searchNowButton.addActionListener(e -> searchAccounts());
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchLabel.setForeground(new Color(50, 50, 50));
        searchPanel.add(searchLabel);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
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
        accountTable.setBackground(Color.WHITE);
        accountTable.setForeground(new Color(30, 30, 30));
        accountTable.getTableHeader().setBackground(new Color(0, 102, 204));
        accountTable.getTableHeader().setForeground(Color.WHITE);
        accountTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        accountTable.setFont(new Font("Arial", Font.PLAIN, 11));
        accountTable.setGridColor(new Color(200, 200, 200));
        JScrollPane tableScrollPane = new JScrollPane(accountTable);
        tableScrollPane.setBackground(Color.WHITE);

        statisticsArea = new JTextArea(8, 30);
        statisticsArea.setEditable(false);
        statisticsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statisticsArea.setBackground(new Color(255, 255, 255));
        statisticsArea.setForeground(new Color(30, 30, 30));
        statisticsArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Bank Statistics",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            new Color(0, 70, 150)));
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
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
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
