package com.banking;

import java.util.Scanner;

public class Main {
    private static BankingSystem bankingSystem;
    private static Scanner scanner;

    public static void main(String[] args) {
        bankingSystem = new BankingSystem();
        scanner = new Scanner(System.in);
        
        System.out.println("========================================");
        System.out.println("   Welcome to Banking System (CLI)");
        System.out.println("   Using HashMap for Account Storage");
        System.out.println("========================================\n");
        
        boolean running = true;
        
        while (running) {
            displayMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        createAccount();
                        break;
                    case "2":
                        deposit();
                        break;
                    case "3":
                        withdraw();
                        break;
                    case "4":
                        checkBalance();
                        break;
                    case "5":
                        displayAccountInfo();
                        break;
                    case "6":
                        displayAllAccounts();
                        break;
                    case "7":
                        validateTransaction();
                        break;
                    case "8":
                        running = false;
                        System.out.println("\nThank you for using Banking System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.\n");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }
        
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- Banking System Menu ---");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Check Balance");
        System.out.println("5. Display Account Information");
        System.out.println("6. Display All Accounts");
        System.out.println("7. Validate Transaction");
        System.out.println("8. Exit");
        System.out.println("----------------------------");
    }

    private static void createAccount() {
        System.out.println("\n--- Create New Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.print("Enter Holder Name: ");
        String holderName = scanner.nextLine().trim();
        
        System.out.print("Enter Initial Balance: $");
        String balanceInput = scanner.nextLine().trim();
        
        try {
            double initialBalance = Double.parseDouble(balanceInput);
            bankingSystem.createAccount(accountNumber, holderName, initialBalance);
            System.out.println("Account created successfully!");
            System.out.println(bankingSystem.displayAccountInfo(accountNumber));
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid balance amount. Please enter a valid number.");
        }
    }

    private static void deposit() {
        System.out.println("\n--- Deposit Money ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.print("Enter Amount to Deposit: $");
        String amountInput = scanner.nextLine().trim();
        
        try {
            double amount = Double.parseDouble(amountInput);
            bankingSystem.deposit(accountNumber, amount);
            System.out.println("Deposit successful!");
            System.out.println("New Balance: $" + String.format("%.2f", bankingSystem.getBalance(accountNumber)));
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount. Please enter a valid number.");
        }
    }

    private static void withdraw() {
        System.out.println("\n--- Withdraw Money ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.print("Enter Amount to Withdraw: $");
        String amountInput = scanner.nextLine().trim();
        
        try {
            double amount = Double.parseDouble(amountInput);
            bankingSystem.withdraw(accountNumber, amount);
            System.out.println("Withdrawal successful!");
            System.out.println("Remaining Balance: $" + String.format("%.2f", bankingSystem.getBalance(accountNumber)));
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount. Please enter a valid number.");
        }
    }

    private static void checkBalance() {
        System.out.println("\n--- Check Balance ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        double balance = bankingSystem.getBalance(accountNumber);
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
    }

    private static void displayAccountInfo() {
        System.out.println("\n--- Account Information ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.println(bankingSystem.displayAccountInfo(accountNumber));
    }

    private static void displayAllAccounts() {
        System.out.println("\n" + bankingSystem.displayAllAccounts());
    }
        
    private static void validateTransaction() {
        System.out.println("\n--- Validate Transaction ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.print("Enter Transaction Amount: $");
        String amountInput = scanner.nextLine().trim();
        
        System.out.print("Is this a withdrawal? (yes/no): ");
        String isWithdrawalInput = scanner.nextLine().trim().toLowerCase();
        boolean isWithdrawal = isWithdrawalInput.equals("yes") || isWithdrawalInput.equals("y");
        
        try {
            double amount = Double.parseDouble(amountInput);
            boolean isValid = bankingSystem.isValidTransaction(accountNumber, amount, isWithdrawal);
            
            if (isValid) {
                System.out.println("Transaction is VALID.");
                if (isWithdrawal) {
                    System.out.println("Account has sufficient funds for withdrawal.");
                } else {
                    System.out.println("Deposit amount is valid.");
                }
            } else {
                System.out.println("Transaction is INVALID.");
                if (!bankingSystem.accountExists(accountNumber)) {
                    System.out.println("Reason: Account does not exist.");
                } else if (isWithdrawal) {
                    System.out.println("Reason: Insufficient funds or invalid amount.");
                } else {
                    System.out.println("Reason: Invalid amount.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount. Please enter a valid number.");
        }
    }
}

