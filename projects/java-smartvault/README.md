# SmartVault — Personal Finance Manager

Built this as a console-based Java app that simulates a basic banking system with role-based access. The idea came from a real problem — digital wallet apps that let users overdraft because there's no proper validation layer.

Two roles: ADMIN and USER. Admin can see all accounts. Users can only touch their own. Both hit the same codebase but get different access.

## What it does

Login with your account ID. Then you get a menu — deposit, withdraw, view your last 5 transactions. If you're an admin, there's an extra option to see everyone's account and balance.

Try withdrawing more than your balance. Try depositing a negative number. Try typing letters instead of a number. It handles all of it without crashing.

## Files

Account.java — the core. Holds balance, owner, role, transaction history. All fields are private. Deposit and withdraw methods do the validation before touching the balance.

VaultManager.java — keeps the list of all accounts. Has the admin-only method to show everyone.

InsufficientFundsException.java — custom exception for when someone tries to withdraw more than they have. Tells them exactly how much they're short by.

UnauthorizedAccessException.java — fires when a USER tries to access something only an ADMIN can do.

Main.java — the menu loop. Reads input, calls the right methods, catches exceptions and shows friendly messages.

## How to run

```bash
cd projects/java-smartvault/src
javac smartvault/*.java
java smartvault.Main
```

## Test accounts

ID 101 — Alice, ADMIN, $5000.00
ID 102 — Bob, USER, $1200.00
ID 103 — Carol, USER, $0.00

## Concepts used

Encapsulation, custom exceptions, enums, ArrayList, role-based access control
