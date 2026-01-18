package com.banking;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHandler {
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final String DELIMITER = "|||";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void saveAccounts(Map<String, Account> accounts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account account : accounts.values()) {
                writer.println(accountToLine(account));
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts to file: " + e.getMessage());
        }
    }

    public static Map<String, Account> loadAccounts() {
        Map<String, Account> accounts = new HashMap<>();
        
        if (!Files.exists(Paths.get(ACCOUNTS_FILE))) {
            return accounts;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                try {
                    Account account = lineToAccount(line);
                    accounts.put(account.getAccountNumber(), account);
                } catch (Exception e) {
                    System.err.println("Error parsing account line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts from file: " + e.getMessage());
        }
        
        return accounts;
    }

    public static void saveTransactions(Map<String, List<Transaction>> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Map.Entry<String, List<Transaction>> entry : transactions.entrySet()) {
                for (Transaction transaction : entry.getValue()) {
                    writer.println(transactionToLine(transaction));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions to file: " + e.getMessage());
        }
    }

    public static Map<String, List<Transaction>> loadTransactions() {
        Map<String, List<Transaction>> transactions = new HashMap<>();
        
        if (!Files.exists(Paths.get(TRANSACTIONS_FILE))) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                try {
                    Transaction transaction = lineToTransaction(line);
                    String accountNumber = transaction.getAccountNumber();
                    transactions.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(transaction);
                } catch (Exception e) {
                    System.err.println("Error parsing transaction line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions from file: " + e.getMessage());
        }
        
        return transactions;
    }

    private static String accountToLine(Account account) {
        String username = account.getUsername() != null ? account.getUsername() : "";
        return String.join(DELIMITER,
            account.getAccountNumber(),
            username,
            account.getHolderName(),
            String.valueOf(account.getBalance()),
            account.getStatus().toString(),
            account.getHashedPin()
        );
    }

    private static Account lineToAccount(String line) {
        String[] parts = line.split("\\|\\|\\|", -1);
        
        String accountNumber;
        String username;
        String holderName;
        double balance;
        AccountStatus status;
        String hashedPin;
        
        if (parts.length == 5) {
            accountNumber = parts[0];
            username = accountNumber; 
            holderName = parts[1];
            balance = Double.parseDouble(parts[2]);
            status = AccountStatus.valueOf(parts[3]);
            hashedPin = parts[4];
        } else if (parts.length == 6) {
            accountNumber = parts[0];
            username = parts[1].isEmpty() ? accountNumber : parts[1];
            holderName = parts[2];
            balance = Double.parseDouble(parts[3]);
            status = AccountStatus.valueOf(parts[4]);
            hashedPin = parts[5];
        } else {
            throw new IllegalArgumentException("Invalid account format: expected 5 or 6 parts, got " + parts.length);
        }
        
        Account account = new Account(accountNumber, username, holderName, 0, "temp");
        account.setBalance(balance);
        account.setStatus(status);
        account.setHashedPin(hashedPin);
        
        return account;
    }

    private static String transactionToLine(Transaction transaction) {
        String relatedAccount = transaction.getRelatedAccount() != null ? transaction.getRelatedAccount() : "";
        String dateStr = transaction.getDate().format(DATE_FORMATTER);
        
        return String.join(DELIMITER,
            transaction.getTransactionId(),
            transaction.getAccountNumber(),
            transaction.getType().toString(),
            String.valueOf(transaction.getAmount()),
            dateStr,
            relatedAccount
        );
    }

    private static Transaction lineToTransaction(String line) {
        String[] parts = line.split("\\|\\|\\|", -1);
        if (parts.length < 5 || parts.length > 6) {
            throw new IllegalArgumentException("Invalid transaction format");
        }
        
        String transactionId = parts[0];
        String accountNumber = parts[1];
        Transaction.TransactionType type = Transaction.TransactionType.valueOf(parts[2]);
        double amount = Double.parseDouble(parts[3]);
        LocalDateTime date = LocalDateTime.parse(parts[4], DATE_FORMATTER);
        String relatedAccount = (parts.length == 6 && !parts[5].isEmpty()) ? parts[5] : null;
        
        Transaction transaction;
        if (relatedAccount != null) {
            transaction = new Transaction(accountNumber, type, amount, relatedAccount);
        } else {
            transaction = new Transaction(accountNumber, type, amount);
        }
        
        transaction.setTransactionId(transactionId);
        transaction.setDate(date);
        
        return transaction;
    }
}
