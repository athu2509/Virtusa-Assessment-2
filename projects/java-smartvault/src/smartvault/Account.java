package smartvault;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {

    public enum Role { ADMIN, USER }

    private int id;
    private String owner;
    private double balance;
    private Role role;
    private ArrayList<String> history = new ArrayList<>();

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public Account(int id, String owner, double openingBalance, Role role) {
        if (openingBalance < 0) {
            System.out.println("[WARN] Opening balance was negative, setting to 0.");
            openingBalance = 0;
        }
        this.id      = id;
        this.owner   = owner;
        this.balance = openingBalance;
        this.role    = role;
        addToHistory("Account opened | balance: $" + f(openingBalance));
        System.out.println("[LOG] New account created -> " + owner + " | ID: " + id + " | Role: " + role);
    }

    public void deposit(double amt) {
        if (amt <= 0) {
            System.out.println("[ERROR] Deposit failed — amount must be greater than zero.");
            throw new IllegalArgumentException("Deposit must be positive.");
        }
        if (amt > 100000) {
            System.out.println("[WARN] Large deposit flagged: $" + f(amt) + " — proceeding anyway.");
        }
        balance += amt;
        addToHistory("Deposit  +$" + f(amt) + "  | balance now: $" + f(balance));
        System.out.println("[LOG] Deposit OK -> $" + f(amt) + " | New balance: $" + f(balance));
    }

    public void withdraw(double amt) throws InsufficientFundsException {
        if (amt <= 0) {
            System.out.println("[ERROR] Withdraw failed — amount must be greater than zero.");
            throw new IllegalArgumentException("Withdrawal must be positive.");
        }
        if (amt > balance) {
            System.out.println("[ERROR] Withdraw blocked — not enough funds for $" + f(amt));
            throw new InsufficientFundsException(amt - balance);
        }
        balance -= amt;
        addToHistory("Withdraw -$" + f(amt) + "  | balance now: $" + f(balance));
        System.out.println("[LOG] Withdraw OK -> $" + f(amt) + " | New balance: $" + f(balance));
    }

    public void printMiniStatement() {
        System.out.println("\n  Statement for: " + owner);
        System.out.println("  " + "-".repeat(40));
        if (history.isEmpty()) {
            System.out.println("  No transactions on record.");
        } else {
            for (String line : history) {
                System.out.println("  " + line);
            }
        }
        System.out.println("  " + "-".repeat(40));
        System.out.println("  Current Balance: $" + f(balance));
    }

    private void addToHistory(String entry) {
        String stamped = LocalDateTime.now().format(fmt) + "  " + entry;
        if (history.size() == 5) history.remove(0);
        history.add(stamped);
    }

    private String f(double val) {
        return String.format("%.2f", val);
    }

    public int    getId()      { return id; }
    public String getOwner()   { return owner; }
    public double getBalance() { return balance; }
    public Role   getRole()    { return role; }
}
