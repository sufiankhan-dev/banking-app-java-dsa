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
        return String.join(DELIMITER,
            account.getAccountNumber(),
            account.getHolderName(),
            String.valueOf(account.getBalance()),
            account.getStatus().toString(),
            account.getHashedPin()
        );
    }

    private static Account lineToAccount(String line) {
        String[] parts = line.split("\\|\\|\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid account format");
        }
        
        String accountNumber = parts[0];
        String holderName = parts[1];
        double balance = Double.parseDouble(parts[2]);
        AccountStatus status = AccountStatus.valueOf(parts[3]);
        String hashedPin = parts[4];
        
        Account account = new Account(accountNumber, holderName, 0, "temp");
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
        String[] parts = line.split("\\|\\|\\|");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid transaction format");
        }
        
        String transactionId = parts[0];
        String accountNumber = parts[1];
        Transaction.TransactionType type = Transaction.TransactionType.valueOf(parts[2]);
        double amount = Double.parseDouble(parts[3]);
        LocalDateTime date = LocalDateTime.parse(parts[4], DATE_FORMATTER);
        String relatedAccount = parts[5].isEmpty() ? null : parts[5];
        
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
