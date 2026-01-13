package com.banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BankingSystem {
    private Map<String, Account> accounts;
    private Map<String, List<Transaction>> transactions;
    private String currentLoggedInAccount;

    public BankingSystem() {
        this.accounts = new HashMap<>();
        this.transactions = new HashMap<>();
        this.currentLoggedInAccount = null;
    }

    public boolean createAccount(String accountNumber, String holderName, double initialBalance, String pin) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account number already exists: " + accountNumber);
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        
        Account account = new Account(accountNumber, holderName, initialBalance, pin);
        accounts.put(accountNumber, account);
        transactions.put(accountNumber, new ArrayList<>());
        return true;
    }

    public boolean updateAccountInfo(String accountNumber, String newHolderName) {
        Account account = getAccount(accountNumber);
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot update a closed account");
        }
        
        if (newHolderName != null && !newHolderName.trim().isEmpty()) {
            account.setHolderName(newHolderName);
        }
        return true;
    }

    public boolean closeAccount(String accountNumber) {
        Account account = getAccount(accountNumber);
        account.setStatus(AccountStatus.CLOSED);
        return true;
    }

    public boolean login(String accountNumber, String pin) {
        if (!accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        
        Account account = accounts.get(accountNumber);
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot login to a closed account");
        }
        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalStateException("Account is frozen. Please contact administrator");
        }
        if (!account.validatePin(pin)) {
            throw new IllegalArgumentException("Invalid PIN");
        }
        
        currentLoggedInAccount = accountNumber;
        return true;
    }

    public boolean logout() {
        if (currentLoggedInAccount == null) {
            return false;
        }
        currentLoggedInAccount = null;
        return true;
    }

    public String getCurrentLoggedInAccount() {
        return currentLoggedInAccount;
    }

    public boolean isLoggedIn() {
        return currentLoggedInAccount != null;
    }

    public String getAccountHolderName(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account.getHolderName();
    }

    public boolean deposit(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        account.deposit(amount);
        
        Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.DEPOSIT, amount);
        transactions.get(accountNumber).add(transaction);
        
        return true;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
        
        Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.WITHDRAW, amount);
        transactions.get(accountNumber).add(transaction);
        
        return true;
    }

    public boolean transferFunds(String senderAccount, String receiverAccount, double amount) {
        if (!accounts.containsKey(senderAccount)) {
            throw new IllegalArgumentException("Sender account not found: " + senderAccount);
        }
        if (!accounts.containsKey(receiverAccount)) {
            throw new IllegalArgumentException("Receiver account not found: " + receiverAccount);
        }
        if (senderAccount.equals(receiverAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        Account sender = accounts.get(senderAccount);
        Account receiver = accounts.get(receiverAccount);

        if (sender.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Sender account is not active. Status: " + sender.getStatus());
        }
        if (receiver.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Receiver account is not active. Status: " + receiver.getStatus());
        }

        if (amount > sender.getBalance()) {
            throw new IllegalArgumentException("Insufficient funds. Available balance: " + sender.getBalance());
        }

        try {
            sender.withdraw(amount);
            
            receiver.deposit(amount);
            
            Transaction transferOut = new Transaction(senderAccount, Transaction.TransactionType.TRANSFER_OUT, amount, receiverAccount);
            Transaction transferIn = new Transaction(receiverAccount, Transaction.TransactionType.TRANSFER_IN, amount, senderAccount);
            
            transactions.get(senderAccount).add(transferOut);
            transactions.get(receiverAccount).add(transferIn);
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }
    }

    public double getBalance(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account.getBalance();
    }

    public boolean isValidTransaction(String accountNumber, double amount, boolean isWithdrawal) {
        if (!accounts.containsKey(accountNumber)) {
            return false;
        }
        
        Account account = accounts.get(accountNumber);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            return false;
        }
        
        if (isWithdrawal) {
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

    public List<Transaction> getTransactionHistory(String accountNumber) {
        if (!transactions.containsKey(accountNumber)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(transactions.get(accountNumber));
    }

    public List<Transaction> getLastNTransactions(String accountNumber, int n) {
        List<Transaction> allTransactions = getTransactionHistory(accountNumber);
        int size = allTransactions.size();
        int start = Math.max(0, size - n);
        return allTransactions.subList(start, size);
    }

    public String displayTransactionHistory(String accountNumber) {
        List<Transaction> history = getTransactionHistory(accountNumber);
        if (history.isEmpty()) {
            return "No transactions found for account: " + accountNumber;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Transaction History for Account: ").append(accountNumber).append(" ===\n");
        for (Transaction txn : history) {
            sb.append(txn.toString()).append("\n");
        }
        return sb.toString();
    }

    public boolean freezeAccount(String accountNumber) {
        Account account = getAccount(accountNumber);
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot freeze a closed account");
        }
        account.setStatus(AccountStatus.FROZEN);
        return true;
    }

    public boolean unfreezeAccount(String accountNumber) {
        Account account = getAccount(accountNumber);
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot unfreeze a closed account");
        }
        account.setStatus(AccountStatus.ACTIVE);
        return true;
    }

    public List<Account> searchAccountsByNumber(String searchTerm) {
        return accounts.values().stream()
                .filter(acc -> acc.getAccountNumber().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Account> searchAccountsByName(String searchTerm) {
        return accounts.values().stream()
                .filter(acc -> acc.getHolderName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Account> getFrozenAccounts() {
        return accounts.values().stream()
                .filter(acc -> acc.getStatus() == AccountStatus.FROZEN)
                .collect(Collectors.toList());
    }

    public List<Account> getClosedAccounts() {
        return accounts.values().stream()
                .filter(acc -> acc.getStatus() == AccountStatus.CLOSED)
                .collect(Collectors.toList());
    }

    public int getTotalAccountCount() {
        return accounts.size();
    }

    public double getTotalBankBalance() {
        return accounts.values().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public Account getHighestBalanceAccount() {
        return accounts.values().stream()
                .max((a1, a2) -> Double.compare(a1.getBalance(), a2.getBalance()))
                .orElse(null);
    }

    public String getBankStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Bank Statistics ===\n");
        sb.append("Total Accounts: ").append(getTotalAccountCount()).append("\n");
        sb.append("Total Bank Balance: $").append(String.format("%.2f", getTotalBankBalance())).append("\n");
        
        Account highest = getHighestBalanceAccount();
        if (highest != null) {
            sb.append("Highest Balance Holder: ").append(highest.getHolderName())
              .append(" | Account: ").append(highest.getAccountNumber())
              .append(" | Balance: $").append(String.format("%.2f", highest.getBalance())).append("\n");
        }
        
        long activeCount = accounts.values().stream().filter(a -> a.getStatus() == AccountStatus.ACTIVE).count();
        long frozenCount = accounts.values().stream().filter(a -> a.getStatus() == AccountStatus.FROZEN).count();
        long closedCount = accounts.values().stream().filter(a -> a.getStatus() == AccountStatus.CLOSED).count();
        
        sb.append("Active Accounts: ").append(activeCount).append("\n");
        sb.append("Frozen Accounts: ").append(frozenCount).append("\n");
        sb.append("Closed Accounts: ").append(closedCount).append("\n");
        
        return sb.toString();
    }

    public boolean changePin(String accountNumber, String oldPin, String newPin) {
        Account account = getAccount(accountNumber);
        account.changePin(oldPin, newPin);
        return true;
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
