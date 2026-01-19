package com.banking.ui;

import com.banking.Account;
import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final BankingSystem bankingSystem;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JTextArea statisticsArea;

    public AdminDashboard() {
        this.bankingSystem = Main.bankingSystem;
        initUI();
        loadAllAccounts();
        updateStatistics();
    }

    private void initUI() {
        setTitle("Admin Dashboard");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(Theme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(root);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createCenterPanel(), BorderLayout.CENTER);
    }

    /* ================= HEADER ================= */

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND_COLOR);

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.TEXT_PRIMARY);

        JButton exitBtn = createPrimaryButton("Exit Admin Mode");
        exitBtn.addActionListener(e -> {
            new MainWindow().setVisible(true);
            dispose();
        });

        header.add(title, BorderLayout.WEST);
        header.add(exitBtn, BorderLayout.EAST);
        return header;
    }

    /* ================= SIDEBAR ================= */

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(5, 1, 10, 10));
        sidebar.setBackground(Theme.PANEL_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 0));

        sidebar.add(actionButton("View All Accounts", this::loadAllAccounts));
        sidebar.add(actionButton("View Frozen Accounts", this::viewFrozenAccounts));
        sidebar.add(actionButton("Freeze Account", this::freezeAccount));
        sidebar.add(actionButton("Unfreeze Account", this::unfreezeAccount));
        sidebar.add(actionButton("Close Account", this::closeAccount));

        return sidebar;
    }

    /* ================= CENTER ================= */

    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(Theme.BACKGROUND_COLOR);

        center.add(createSearchPanel(), BorderLayout.NORTH);
        center.add(createTable(), BorderLayout.CENTER);
        center.add(createStatisticsPanel(), BorderLayout.SOUTH);

        return center;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(Theme.PANEL_COLOR);

        searchField = new JTextField(20);
        searchField.setFont(Theme.BODY_FONT);

        searchTypeCombo = new JComboBox<>(new String[]{"By Number", "By Name"});
        searchTypeCombo.setFont(Theme.BODY_FONT);

        JButton searchBtn = createPrimaryButton("Search");
        searchBtn.addActionListener(e -> searchAccounts());

        panel.add(new JLabelStyled("Search:"));
        panel.add(searchField);
        panel.add(searchTypeCombo);
        panel.add(searchBtn);

        return panel;
    }

    private JScrollPane createTable() {
        tableModel = new DefaultTableModel(
                new String[]{"Account Number", "Holder", "Balance", "Status"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        accountTable = new JTable(tableModel);
        accountTable.setRowHeight(26);
        accountTable.setFont(Theme.BODY_FONT);
        accountTable.setForeground(Theme.TEXT_PRIMARY);
        accountTable.setBackground(Theme.CARD_COLOR);
        accountTable.getTableHeader().setBackground(Theme.PRIMARY_COLOR);
        accountTable.getTableHeader().setForeground(Theme.TEXT_PRIMARY);
        accountTable.getTableHeader().setFont(Theme.SUBHEADER_FONT);

        JScrollPane scroll = new JScrollPane(accountTable);
        scroll.getViewport().setBackground(Theme.CARD_COLOR);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));

        return scroll;
    }

    private JScrollPane createStatisticsPanel() {
        statisticsArea = new JTextArea(6, 30);
        statisticsArea.setEditable(false);
        statisticsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statisticsArea.setBackground(Theme.PANEL_COLOR);
        statisticsArea.setForeground(Theme.TEXT_SECONDARY);

        JScrollPane scroll = new JScrollPane(statisticsArea);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        return scroll;
    }

    /* ================= BUTTON FACTORY ================= */

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.SUBHEADER_FONT);
        btn.setBackground(Theme.BUTTON_PRIMARY);
        btn.setForeground(Theme.BUTTON_TEXT);
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton actionButton(String text, Runnable action) {
        JButton btn = createPrimaryButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    /* ================= LOGIC (UNCHANGED) ================= */

    private void loadAllAccounts() {
        tableModel.setRowCount(0);
        String data = bankingSystem.displayAllAccounts();
        if (data.contains("No accounts")) return;

        for (String line : data.split("\n")) {
            if (line.contains("Account Number"))
                parseAndAdd(line);
        }
    }

    private void parseAndAdd(String line) {
        String[] p = line.split("\\|");
        tableModel.addRow(new Object[]{
                p[0].split(":")[1].trim(),
                p[1].split(":")[1].trim(),
                p[2].split(":")[1].trim(),
                p[3].split(":")[1].trim()
        });
    }

    private void searchAccounts() {
        tableModel.setRowCount(0);
        String term = searchField.getText().trim();
        if (term.isEmpty()) return;

        List<Account> results = searchTypeCombo.getSelectedIndex() == 0
                ? bankingSystem.searchAccountsByNumber(term)
                : bankingSystem.searchAccountsByName(term);

        for (Account a : results) {
            tableModel.addRow(new Object[]{
                    a.getAccountNumber(),
                    a.getHolderName(),
                    a.getBalance(),
                    a.getStatus()
            });
        }
    }

    private void freezeAccount() { actOnSelected(bankingSystem::freezeAccount); }
    private void unfreezeAccount() { actOnSelected(bankingSystem::unfreezeAccount); }
    private void closeAccount() { actOnSelected(bankingSystem::closeAccount); }

    private void actOnSelected(java.util.function.Consumer<String> action) {
        int row = accountTable.getSelectedRow();
        if (row == -1) return;
        action.accept((String) tableModel.getValueAt(row, 0));
        loadAllAccounts();
        updateStatistics();
    }

    private void viewFrozenAccounts() {
        tableModel.setRowCount(0);
        for (Account a : bankingSystem.getFrozenAccounts()) {
            tableModel.addRow(new Object[]{
                    a.getAccountNumber(),
                    a.getHolderName(),
                    a.getBalance(),
                    a.getStatus()
            });
        }
    }

    private void updateStatistics() {
        statisticsArea.setText(bankingSystem.getBankStatistics());
    }

    /* ================= SMALL HELPER ================= */

    private static class JLabelStyled extends JLabel {
        JLabelStyled(String text) {
            super(text);
            setFont(Theme.SUBHEADER_FONT);
            setForeground(Theme.TEXT_PRIMARY);
        }
    }
}
