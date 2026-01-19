package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;
import com.banking.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionHistoryPanel extends JPanel {

    private final BankingSystem bankingSystem = Main.bankingSystem;
    private final String accountNumber;

    private JTable table;
    private DefaultTableModel model;
    private JTextField filterField;
    private JLabel errorLabel;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    public TransactionHistoryPanel(String accountNumber) {
        this.accountNumber = accountNumber;
        initUI();
        render(bankingSystem.getTransactionHistory(accountNumber));
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(title(), BorderLayout.NORTH);
        add(tablePanel(), BorderLayout.CENTER);
        add(filterPanel(), BorderLayout.SOUTH);
    }

    private JLabel title() {
        JLabel l = new JLabel("Transaction History – Account " + accountNumber);
        l.setFont(Theme.HEADER_FONT);
        l.setForeground(Theme.TEXT_PRIMARY);
        return l;
    }

    private JScrollPane tablePanel() {
        model = new DefaultTableModel(
                new String[]{"ID", "Type", "Amount", "Date", "Related Account"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setFont(Theme.BODY_FONT);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setBackground(Theme.PANEL_COLOR);
        table.setRowHeight(30);
        table.setSelectionBackground(Theme.PRIMARY_COLOR);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);

        JTableHeader header = table.getTableHeader();
        header.setFont(Theme.SUBHEADER_FONT);
        header.setBackground(Theme.CARD_COLOR);
        header.setForeground(Theme.TEXT_PRIMARY);
        header.setReorderingAllowed(false);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        sp.getViewport().setBackground(Theme.PANEL_COLOR);
        return sp;
    }

    private JPanel filterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Theme.BACKGROUND_COLOR);

        panel.add(label("Show last N:"));

        filterField = input();
        panel.add(filterField);

        panel.add(primaryButton("Filter", this::filter));
        panel.add(primaryButton("Show All", this::showAll));

        errorLabel = new JLabel(" ");
        errorLabel.setFont(Theme.BODY_FONT);
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);

        return panel;
    }

    // ---------- Actions ----------

    private void showAll() {
        render(bankingSystem.getTransactionHistory(accountNumber));
    }

    private void filter() {
        String txt = filterField.getText().trim();
        if (txt.isEmpty()) {
            error("Enter a number");
            return;
        }

        try {
            int n = Integer.parseInt(txt);
            if (n <= 0) {
                error("Must be > 0");
                return;
            }
            render(bankingSystem.getLastNTransactions(accountNumber, n));
        } catch (NumberFormatException e) {
            error("Invalid number");
        }
    }

    private void render(List<Transaction> txns) {
        model.setRowCount(0);
        error(" ");

        if (txns.isEmpty()) {
            model.addRow(new Object[]{"—", "No transactions", "", "", ""});
            return;
        }

        for (int i = txns.size() - 1; i >= 0; i--) {
            Transaction t = txns.get(i);
            model.addRow(new Object[]{
                    t.getTransactionId().substring(0, 8),
                    t.getType(),
                    "$" + String.format("%.2f", t.getAmount()),
                    t.getDate().format(FORMATTER),
                    t.getRelatedAccount() == null ? "-" : t.getRelatedAccount()
            });
        }
    }

    // ---------- UI Helpers ----------

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.SUBHEADER_FONT);
        l.setForeground(Theme.TEXT_SECONDARY);
        return l;
    }

    private JTextField input() {
        JTextField f = new JTextField(8);
        f.setFont(Theme.BODY_FONT);
        f.setForeground(Theme.TEXT_PRIMARY);
        f.setBackground(Theme.PANEL_COLOR);
        f.setCaretColor(Theme.TEXT_PRIMARY);
        f.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        return f;
    }

    private JButton primaryButton(String text, Runnable r) {
        JButton b = new JButton(text);
        b.setFont(Theme.SUBHEADER_FONT);
        b.setBackground(Theme.BUTTON_PRIMARY);
        b.setForeground(Theme.BUTTON_TEXT);
        b.setFocusPainted(false);
        b.addActionListener(e -> r.run());
        return b;
    }

    private void error(String msg) {
        errorLabel.setText(msg);
    }
}
