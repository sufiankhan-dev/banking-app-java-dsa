# Banking System

A comprehensive Java-based banking application with a modern graphical user interface (GUI) for managing bank accounts, transactions, and user authentication. Built using Java Swing and implementing efficient data structures for optimal performance.

## 🚀 Features

### User Features
- **Account Management**
  - Create new accounts with username-based authentication
  - Auto-generated unique 5-digit account numbers
  - Update account information (username and holder name)
  - View account details with modern card-based UI

- **Transaction Operations**
  - Deposit money with real-time balance updates
  - Withdraw funds with balance validation
  - Transfer funds between accounts
  - View transaction history with filtering options
  - Display recent transactions (last 5) on dashboard

- **Security**
  - PIN-based authentication with SHA-256 hashing
  - Account status management (ACTIVE, FROZEN, CLOSED)
  - Secure login system with username and PIN

- **User Interface**
  - Modern, clean GUI built with Java Swing
  - Real-time account information display
  - Transaction history tables with sorting
  - Responsive and intuitive design

### Admin Features
- **Account Management**
  - Search accounts by number or name
  - Freeze/unfreeze accounts
  - Close accounts
  - View all accounts

- **Bank Statistics**
  - Total accounts count
  - Total bank balance
  - Highest balance holder
  - Account status breakdown (Active, Frozen, Closed)

## 🛠️ Technologies Used

- **Java** - Core programming language
- **Java Swing** - GUI framework
- **HashMap** - Efficient O(1) account lookup
- **File I/O** - Persistent data storage
- **SHA-256** - PIN hashing for security

## 📋 Prerequisites

- Java JDK 8 or higher
- Any Java-compatible IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

## 🔧 Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd dsa-project
   ```

2. **Compile the project**
   ```bash
   javac -d out -cp out src/main/java/com/banking/*.java src/main/java/com/banking/ui/*.java src/main/java/com/banking/Main.java
   ```

3. **Run the application**
   ```bash
   java -cp out com.banking.Main
   ```

### Using an IDE

1. Import the project into your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Set `src/main/java` as the source root
3. Run the `Main.java` class from `com.banking` package

## 📖 Usage

### Creating an Account

1. Launch the application
2. Click "Create New Account" on the login screen
3. Enter:
   - Username (must be unique)
   - Holder Name
   - Initial Balance
   - PIN (for account security)
4. Your account number will be auto-generated and displayed

### Logging In

1. Enter your username and PIN on the login screen
2. Click "Login" to access your dashboard
3. For admin access, click "Admin Login" and enter admin credentials

### Performing Transactions

- **Deposit**: Click "Deposit Money", enter the amount
- **Withdraw**: Click "Withdraw Money", enter the amount
- **Transfer**: Click "Transfer Funds", enter receiver account number and amount
- **View History**: Click "View Transaction History" to see all transactions

### Admin Operations

1. Login as admin
2. Search accounts using the search panel
3. Use account actions to freeze, unfreeze, or close accounts
4. View bank statistics in the statistics panel

## 📁 Project Structure

```
dsa-project/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── banking/
│                   ├── Account.java              # Account entity with status management
│                   ├── AccountStatus.java        # Account status enum
│                   ├── BankingSystem.java        # Core banking logic with HashMap
│                   ├── FileHandler.java          # File persistence layer
│                   ├── Main.java                 # Application entry point
│                   └── Transaction.java          # Transaction entity
│                   └── ui/
│                       ├── AccountInfoPanel.java      # Account information display
│                       ├── AdminDashboard.java       # Admin interface
│                       ├── CreateAccountDialog.java   # Account creation dialog
│                       ├── MainWindow.java            # Login window
│                       ├── TransactionDialog.java     # Transaction dialogs
│                       ├── TransactionHistoryPanel.java # Transaction history view
│                       └── UserDashboard.java         # User dashboard
├── accounts.txt          # Account data storage (auto-generated)
├── transactions.txt      # Transaction data storage (auto-generated)
├── .gitignore           # Git ignore rules
└── README.md            # Project documentation
```

## 🏗️ Architecture

### Core Components

- **Account**: Represents a bank account with username, holder name, balance, status, and hashed PIN
- **BankingSystem**: Manages all accounts using HashMap for O(1) lookup, handles transactions, and provides business logic
- **FileHandler**: Handles persistence by saving/loading accounts and transactions to/from text files
- **Transaction**: Represents financial transactions (DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT)

### Data Structures

- **HashMap<String, Account>**: Stores accounts with account number as key for efficient lookup
- **HashMap<String, String>**: Maps usernames to account numbers for login
- **HashMap<String, List<Transaction>>**: Stores transaction history per account

## 🔐 Security Features

- PIN hashing using SHA-256 algorithm
- Account status validation before transactions
- Balance validation for withdrawals and transfers
- Username uniqueness enforcement

## 💾 Data Persistence

The application automatically saves all data to:
- `accounts.txt` - Account information
- `transactions.txt` - Transaction history

Data is loaded automatically on application startup and saved after every operation.

## 🎨 UI Features

- Modern card-based design for account information
- Color-coded account status (Blue: Active, Orange: Frozen, Gray: Closed)
- Real-time balance display in green
- Transaction tables with alternating row colors
- Responsive layouts with proper spacing and typography

## 📝 Key Design Decisions

- **HashMap Storage**: Provides O(1) average-case time complexity for account lookups
- **File-based Persistence**: Simple, reliable storage without database dependencies
- **Username-based Authentication**: User-friendly login system with auto-generated account numbers
- **Transaction History**: Complete audit trail of all financial operations
- **Status Management**: Account status system for administrative control

## 🐛 Troubleshooting

### Application won't start
- Ensure Java JDK 8+ is installed
- Check that all source files are present
- Verify compilation completed without errors

### Login issues
- Verify username is correct (case-insensitive)
- Check that account is not frozen or closed
- Ensure PIN is entered correctly

### Data not persisting
- Check file permissions in the project directory
- Ensure `accounts.txt` and `transactions.txt` are not read-only

## 📄 License

This project is open source and available for educational purposes.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome. Please feel free to check the issues page.

---

**Note**: This is a desktop application. Data files (`accounts.txt` and `transactions.txt`) are stored locally in the project directory.
