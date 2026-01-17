package com.banking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private String accountNumber;
    private TransactionType type;
    private double amount;
    private LocalDateTime date;
    private String relatedAccount; 

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        TRANSFER_OUT,
        TRANSFER_IN
    }

    public Transaction(String accountNumber, TransactionType type, double amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.relatedAccount = null;
    }

    public Transaction(String accountNumber, TransactionType type, double amount, String relatedAccount) {
        this.transactionId = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.relatedAccount = relatedAccount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getRelatedAccount() {
        return relatedAccount;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String relatedInfo = relatedAccount != null ? " | Related Account: " + relatedAccount : "";
        return String.format("TxnID: %s | Account: %s | Type: %s | Amount: $%.2f | Date: %s%s",
                transactionId.substring(0, 8), accountNumber, type, amount, 
                date.format(formatter), relatedInfo);
    }
}

