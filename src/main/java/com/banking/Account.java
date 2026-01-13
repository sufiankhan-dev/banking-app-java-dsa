package com.banking;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account {
    private String accountNumber;
    private String holderName;
    private double balance;
    private AccountStatus status;
    private String hashedPin;

    public Account(String accountNumber, String holderName, double initialBalance, String pin) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name cannot be null or empty");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (pin == null || pin.trim().isEmpty()) {
            throw new IllegalArgumentException("PIN cannot be null or empty");
        }
        
        this.accountNumber = accountNumber.trim();
        this.holderName = holderName.trim();
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
        this.hashedPin = hashPin(pin);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setHolderName(String holderName) {
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name cannot be null or empty");
        }
        this.holderName = holderName.trim();
    }

    public boolean validatePin(String pin) {
        if (pin == null || pin.trim().isEmpty()) {
            return false;
        }
        String hashedInput = hashPin(pin);
        return hashedPin.equals(hashedInput);
    }

    public void changePin(String oldPin, String newPin) {
        if (!validatePin(oldPin)) {
            throw new IllegalArgumentException("Invalid current PIN");
        }
        if (newPin == null || newPin.trim().isEmpty()) {
            throw new IllegalArgumentException("New PIN cannot be null or empty");
        }
        this.hashedPin = hashPin(newPin);
    }

    private String hashPin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(pin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(pin.hashCode());
        }
    }

    public boolean deposit(double amount) {
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot deposit to a closed account");
        }
        if (status == AccountStatus.FROZEN) {
            throw new IllegalStateException("Cannot deposit to a frozen account");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot withdraw from a closed account");
        }
        if (status == AccountStatus.FROZEN) {
            throw new IllegalStateException("Cannot withdraw from a frozen account");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds. Available balance: " + balance);
        }
        balance -= amount;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Account Number: %s | Holder: %s | Balance: $%.2f | Status: %s", 
                           accountNumber, holderName, balance, status);
    }
}

