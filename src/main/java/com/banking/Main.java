package com.banking;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static BankingSystem bankingSystem;
    private static Scanner scanner;
    private static boolean isAdminMode = false;

    public static void main(String[] args) {
        bankingSystem = new BankingSystem();
        scanner = new Scanner(System.in);
        
        System.out.println("========================================");
        System.out.println("   Welcome to Banking System (CLI)");
        System.out.println("   Using HashMap for Account Storage");
        System.out.println("========================================\n");
        
        boolean running = true;
        
        while (running) {
            if (!bankingSystem.isLoggedIn() && !isAdminMode) {
                displayMainMenu();
            } else if (isAdminMode) {
                displayAdminMenu();
            } else {
                displayUserMenu();
            }
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            
            try {
                if (!bankingSystem.isLoggedIn() && !isAdminMode) {
                    switch (choice) {
                        case "1":
                            login();
                            break;
                        case "2":
                            createAccount();
                            break;
                        case "3":
                            isAdminMode = true;
                            System.out.println("\nSwitched to Admin Mode");
                            break;
                        case "4":
                            running = false;
                            System.out.println("\nThank you for using Banking System. Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.\n");
                    }
                } else if (isAdminMode) {
                    switch (choice) {
                        case "1":
                            displayAllAccounts();
                            break;
                        case "2":
                            searchAccountByNumber();
                            break;
                        case "3":
                            searchAccountByName();
                            break;
                        case "4":
                            displayFrozenAccounts();
                            break;
                        case "5":
                            freezeAccount();
                            break;
                        case "6":
                            unfreezeAccount();
                            break;
                        case "7":
                            closeAccount();
                            break;
                        case "8":
                            displayBankStatistics();
                            break;
                        case "9":
                            isAdminMode = false;
                            System.out.println("\nExited Admin Mode");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.\n");
                    }
                } else {
                    switch (choice) {
                        case "1":
                            deposit();
                            break;
                        case "2":
                            withdraw();
                            break;
                        case "3":
                            transferFunds();
                            break;
                        case "4":
                            displayAccountInfo();
                            break;
                        case "5":
                            displayTransactionHistory();
                            break;
                        case "6":
                            displayLastNTransactions();
                            break;
                        case "7":
                            updateAccountInfo();
                            break;
                        case "8":
                            changePin();
                            break;
                        case "9":
                            bankingSystem.logout();
                            System.out.println("\nLogged out successfully!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.\n");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }
        
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Create New Account");
        System.out.println("3. Admin Mode");
        System.out.println("4. Exit");
        System.out.println("------------------");
    }

    private static void displayUserMenu() {
        String accountNumber = bankingSystem.getCurrentLoggedInAccount();
        String holderName = bankingSystem.getAccountHolderName(accountNumber);
        System.out.println("\n--- User Menu (Logged in as: " + holderName + ") ---");
        System.out.println("1. Deposit Money");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Transfer Funds");
        System.out.println("4. View Account Information");
        System.out.println("5. View Transaction History");
        System.out.println("6. View Last N Transactions");
        System.out.println("7. Update Account Info");
        System.out.println("8. Change PIN");
        System.out.println("9. Logout");
        System.out.println("----------------------------------------");
    }

    private static void displayAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. Display All Accounts");
        System.out.println("2. Search Account by Number");
        System.out.println("3. Search Account by Name");
        System.out.println("4. Display Frozen Accounts");
        System.out.println("5. Freeze Account");
        System.out.println("6. Unfreeze Account");
        System.out.println("7. Close Account");
        System.out.println("8. Bank Statistics");
        System.out.println("9. Exit Admin Mode");
        System.out.println("-------------------");
    }

    private static void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine().trim();
        
        bankingSystem.login(accountNumber, pin);
        String holderName = bankingSystem.getAccountHolderName(accountNumber);
        System.out.println("Login successful! Welcome, " + holderName);
    }

    private static void createAccount() {
        System.out.println("\n--- Create New Account ---");
        
        String accountNumber = null;
        while (true) {
            System.out.print("Enter Account Number: ");
            accountNumber = scanner.nextLine().trim();
            
            if (accountNumber.isEmpty()) {
                System.out.println("Error: Account number cannot be empty. Please try again.");
                continue;
            }
            
            if (bankingSystem.accountExists(accountNumber)) {
                System.out.println("Error: Account number already exists. Please try a different account number.");
                continue;
            }
            
            break;
        }
        
        String holderName = null;
        while (true) {
            System.out.print("Enter Holder Name: ");
            holderName = scanner.nextLine().trim();
            
            if (holderName.isEmpty()) {
                System.out.println("Error: Holder name cannot be empty. Please try again.");
                continue;
            }
            
            break;
        }
        
        double initialBalance = 0;
        while (true) {
            System.out.print("Enter Initial Balance: $");
            String balanceInput = scanner.nextLine().trim();
            
            if (balanceInput.isEmpty()) {
                System.out.println("Error: Balance cannot be empty. Please try again.");
                continue;
            }
            
            try {
                initialBalance = Double.parseDouble(balanceInput);
                if (initialBalance < 0) {
                    System.out.println("Error: Initial balance cannot be negative. Please enter a non-negative amount.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid balance amount. Please enter a valid number.");
                continue;
            }
        }
        
        String pin = null;
        while (true) {
            System.out.print("Enter PIN: ");
            pin = scanner.nextLine().trim();
            
            if (pin.isEmpty()) {
                System.out.println("Error: PIN cannot be empty. Please try again.");
                continue;
            }
            
            break;
        }
        
        try {
            bankingSystem.createAccount(accountNumber, holderName, initialBalance, pin);
            System.out.println("Account created successfully!");
            System.out.println(bankingSystem.displayAccountInfo(accountNumber));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deposit() {
        System.out.println("\n--- Deposit Money ---");
        String accountNumber = bankingSystem.getCurrentLoggedInAccount();
        System.out.println("Account: " + accountNumber);
        
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
        String accountNumber = bankingSystem.getCurrentLoggedInAccount();
        System.out.println("Account: " + accountNumber);
        
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

    private static void transferFunds() {
        System.out.println("\n--- Transfer Funds ---");
        String senderAccount = bankingSystem.getCurrentLoggedInAccount();
        System.out.println("From Account: " + senderAccount);
        
        System.out.print("Enter Receiver Account Number: ");
        String receiverAccount = scanner.nextLine().trim();
        
        System.out.print("Enter Amount to Transfer: $");
        String amountInput = scanner.nextLine().trim();
        
        try {
            double amount = Double.parseDouble(amountInput);
            bankingSystem.transferFunds(senderAccount, receiverAccount, amount);
            System.out.println("Transfer successful!");
            System.out.println("Your Remaining Balance: $" + String.format("%.2f", bankingSystem.getBalance(senderAccount)));
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount. Please enter a valid number.");
        }
    }

    private static void displayAccountInfo() {
        System.out.println("\n--- Account Information ---");
        String accountNumber = bankingSystem.getCurrentLoggedInAccount();
        System.out.println(bankingSystem.displayAccountInfo(accountNumber));
    }

    private static void displayTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        String accountNumber = bankingSystem.getCurrentLoggedInAccount();
        System.out.println(bankingSystem.displayTransactionHistory(accountNumber));
    }

    private static void displayLastNTransactions() {
        System.out.println("\n--- Last N Transactions ---");
        String accountNumber = bankingSystem.getCurrentLoggedInAccount();
        
        System.out.print("Enter number of transactions to view: ");
        String nInput = scanner.nextLine().trim();
        
        try {
            int n = Integer.parseInt(nInput);
            List<Transaction> transactions = bankingSystem.getLastNTransactions(accountNumber, n);
            
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                System.out.println("=== Last " + n + " Transactions ===\n");
                for (Transaction txn : transactions) {
                    System.out.println(txn.toString());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number. Please enter a valid integer.");
        }
    }

    private static void updateAccountInfo() {
        System.out.println("\n--- Update Account Information ---");
        String accountNumber = bankingSystem.getCurrentLoggedInAccount();
        
        System.out.print("Enter New Holder Name (press Enter to skip): ");
        String newName = scanner.nextLine().trim();
        
        if (newName.isEmpty()) {
            System.out.println("No changes made.");
            return;
        }
        
        bankingSystem.updateAccountInfo(accountNumber, newName);
        System.out.println("Account information updated successfully!");
        System.out.println(bankingSystem.displayAccountInfo(accountNumber));
    }

    private static void changePin() {
        System.out.println("\n--- Change PIN ---");
        String accountNumber = bankingSystem.getCurrentLoggedInAccount();
        
        System.out.print("Enter Current PIN: ");
        String oldPin = scanner.nextLine().trim();
        
        System.out.print("Enter New PIN: ");
        String newPin = scanner.nextLine().trim();
        
        System.out.print("Confirm New PIN: ");
        String confirmPin = scanner.nextLine().trim();
        
        if (!newPin.equals(confirmPin)) {
            System.out.println("Error: New PINs do not match.");
            return;
        }
        
        bankingSystem.changePin(accountNumber, oldPin, newPin);
        System.out.println("PIN changed successfully!");
    }

    private static void displayAllAccounts() {
        System.out.println("\n" + bankingSystem.displayAllAccounts());
    }

    private static void searchAccountByNumber() {
        System.out.println("\n--- Search Account by Number ---");
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().trim();
        
        List<Account> results = bankingSystem.searchAccountsByNumber(searchTerm);
        if (results.isEmpty()) {
            System.out.println("No accounts found matching: " + searchTerm);
        } else {
            System.out.println("=== Search Results ===\n");
            for (Account acc : results) {
                System.out.println(acc.toString());
            }
        }
    }

    private static void searchAccountByName() {
        System.out.println("\n--- Search Account by Name ---");
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().trim();
        
        List<Account> results = bankingSystem.searchAccountsByName(searchTerm);
        if (results.isEmpty()) {
            System.out.println("No accounts found matching: " + searchTerm);
        } else {
            System.out.println("=== Search Results ===\n");
            for (Account acc : results) {
                System.out.println(acc.toString());
            }
        }
    }

    private static void displayFrozenAccounts() {
        System.out.println("\n--- Frozen Accounts ---");
        List<Account> frozen = bankingSystem.getFrozenAccounts();
        if (frozen.isEmpty()) {
            System.out.println("No frozen accounts found.");
        } else {
            for (Account acc : frozen) {
                System.out.println(acc.toString());
            }
        }
    }

    private static void freezeAccount() {
        System.out.println("\n--- Freeze Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        bankingSystem.freezeAccount(accountNumber);
        System.out.println("Account frozen successfully!");
        System.out.println(bankingSystem.displayAccountInfo(accountNumber));
    }

    private static void unfreezeAccount() {
        System.out.println("\n--- Unfreeze Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        bankingSystem.unfreezeAccount(accountNumber);
        System.out.println("Account unfrozen successfully!");
        System.out.println(bankingSystem.displayAccountInfo(accountNumber));
    }

    private static void closeAccount() {
        System.out.println("\n--- Close Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        
        System.out.print("Are you sure you want to close this account? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            bankingSystem.closeAccount(accountNumber);
            System.out.println("Account closed successfully!");
            System.out.println(bankingSystem.displayAccountInfo(accountNumber));
        } else {
            System.out.println("Account closure cancelled.");
        }
    }

    private static void displayBankStatistics() {
        System.out.println("\n" + bankingSystem.getBankStatistics());
    }
}
