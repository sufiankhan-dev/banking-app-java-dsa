# Banking System (Using HashMap)

A command-line interface (CLI) based Java application for managing bank accounts using HashMap data structure.

## Group-8 Members

- Javeria Razzaq
- Ayesha Iqbal
- Sehrish Amin
- Sufian Khan

## Features

- **Stores account details using account number as key**: Uses HashMap for efficient O(1) lookup
- **Handles deposits and withdrawals**: Secure transaction processing with validation
- **Checks balance and transaction validity**: Real-time balance checking and transaction validation
- **Displays account information**: View individual or all accounts

## Project Structure

```
dsa-project/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── banking/
│                   ├── Account.java          # Account entity class
│                   ├── BankingSystem.java    # Core banking logic with HashMap
│                   └── Main.java            # CLI interface
└── README.md
```

## Classes

### 1. Account Class

Represents a bank account with:

- Account number (unique identifier)
- Holder name
- Balance
- Methods: `deposit()`, `withdraw()`, `getBalance()`

### 2. BankingSystem Class

Manages all accounts using HashMap:

- Key: Account number (String)
- Value: Account object
- Methods: `createAccount()`, `deposit()`, `withdraw()`, `getBalance()`, `isValidTransaction()`, `displayAccountInfo()`

### 3. Main Class

Provides CLI interface with menu-driven operations:

- Create Account
- Deposit Money
- Withdraw Money
- Check Balance
- Display Account Information
- Display All Accounts
- Validate Transaction
- Exit

## How to Compile and Run

### Prerequisites

- Java JDK 8 or higher installed
- Command line/terminal access

### Compilation

```bash
# Navigate to project root
cd dsa-project

# Compile Java files
javac -d out src/main/java/com/banking/*.java
```

### Running the Application

```bash
# Run the application
java -cp out com.banking.Main
```

### Alternative: Using Maven/Gradle structure

If you prefer to use an IDE like IntelliJ IDEA or Eclipse:

1. Import the project
2. Set `src/main/java` as source root
3. Run `Main.java`

## Usage Example

```
========================================
   Welcome to Banking System (CLI)
   Using HashMap for Account Storage
========================================

--- Banking System Menu ---
1. Create Account
2. Deposit Money
3. Withdraw Money
4. Check Balance
5. Display Account Information
6. Display All Accounts
7. Validate Transaction
8. Exit
----------------------------
Enter your choice: 1

--- Create New Account ---
Enter Account Number: ACC001
Enter Holder Name: John Doe
Enter Initial Balance: $1000
Account created successfully!
Account Number: ACC001 | Holder: John Doe | Balance: $1000.00
```

## Key Features Implementation

1. **HashMap Storage**: Account numbers are used as keys for O(1) average-case lookup
2. **Transaction Validation**: Checks account existence and sufficient funds before withdrawal
3. **Error Handling**: Comprehensive validation for all inputs and operations
4. **User-Friendly CLI**: Menu-driven interface with clear prompts and messages

## Design Decisions

- **HashMap for Storage**: Provides fast O(1) average-case time complexity for account lookups
- **Separate Classes**: Follows object-oriented principles with clear separation of concerns
- **Input Validation**: All user inputs are validated to prevent errors
- **Exception Handling**: Proper error messages guide users when operations fail
