package com.banking;

import java.util.HashMap;
import java.util.Map;

public class BankingSystem {
    private Map<String, Account> accounts;

    public BankingSystem() {
        this.accounts = new HashMap<>();
    }

    public boolean createAccount(String accountNumber, String holderName, double initialBalance) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account number already exists: " + accountNumber);
        }
        
        Account account = new Account(accountNumber, holderName, initialBalance);
        accounts.put(accountNumber, account);
        return true;
    }

    public boolean deposit(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        return account.deposit(amount);
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        return account.withdraw(amount);
    }

    public double getBalance(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account.getBalance();
    }

    public boolean isValidTransaction(String accountNumber, double amount, boolean isWithdrawal) {
        if (!accounts.containsKey(accountNumber)) {
            return false;
        }
        
        if (isWithdrawal) {
            Account account = accounts.get(accountNumber);
            return amount > 0 && amount <= account.getBalance();
        } else {
            return amount > 0;
        }
    }

    public String displayAccountInfo(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account.toString();
    }

    public String displayAllAccounts() {
        if (accounts.isEmpty()) {
            return "No accounts found in the system.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== All Accounts ===\n");
        for (Account account : accounts.values()) {
            sb.append(account.toString()).append("\n");
        }
        return sb.toString();
    }

    private Account getAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        
        Account account = accounts.get(accountNumber.trim());
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }

    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public int getAccountCount() {
        return accounts.size();
    }
}

