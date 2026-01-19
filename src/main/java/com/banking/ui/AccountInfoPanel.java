package com.banking.ui;

import com.banking.Account;
import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import java.awt.*;

public class AccountInfoPanel extends JPanel {

    private String accountNumber;
    private JLabel accountNumberLabel;
    private JLabel holderNameLabel;
    private JLabel usernameLabel;
    private JLabel balanceLabel;
    private BankingSystem bankingSystem;

    public AccountInfoPanel(String accountNumber) {
        this.accountNumber = accountNumber;
        this.bankingSystem = Main.bankingSystem;
        initializeUI();
        refresh();
    }

    private void initializeUI() {
        setLayout(new GridLayout(4, 1, 5, 5));
        setBackground(Theme.CARD_COLOR);

        accountNumberLabel = createLabel();
        holderNameLabel = createLabel();
        usernameLabel = createLabel();
        balanceLabel = createLabel();

        add(accountNumberLabel);
        add(holderNameLabel);
        add(usernameLabel);
        add(balanceLabel);
    }

    private JLabel createLabel() {
        JLabel label = new JLabel();
        label.setFont(Theme.BODY_FONT);
        label.setForeground(Theme.TEXT_PRIMARY);
        return label;
    }

    public void refresh() {
        Account account = bankingSystem.getAccount(accountNumber);
        if (account == null) return;

        accountNumberLabel.setText("Account Number: " + account.getAccountNumber());
        holderNameLabel.setText("Holder Name: " + account.getHolderName());
        usernameLabel.setText("Username: " + account.getUsername());
        balanceLabel.setText("Balance: $" + String.format("%.2f", account.getBalance()));
    }
}
