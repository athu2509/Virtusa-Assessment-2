package smartvault;

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static VaultManager vault = new VaultManager();

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       SmartVault v1.0");
        System.out.println("================================");

        vault.addAccount(new Account(101, "Alice", 5000.00, Account.Role.ADMIN));
        vault.addAccount(new Account(102, "Bob",   1200.00, Account.Role.USER));
        vault.addAccount(new Account(103, "Carol",    0.00, Account.Role.USER));

        System.out.print("\nEnter your account ID: ");
        Account me = null;

        try {
            int inputId = Integer.parseInt(sc.nextLine().trim());
            me = vault.findById(inputId);
        } catch (NumberFormatException e) {
            System.out.println("That's not a valid ID. Exiting.");
            return;
        }

        if (me == null) {
            System.out.println("Account not found. Exiting.");
            return;
        }

        System.out.println("Logged in as: " + me.getOwner() + " [" + me.getRole() + "]");

        boolean active = true;
        while (active) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. My Statement");
            if (me.getRole() == Account.Role.ADMIN) {
                System.out.println("4. All Accounts [Admin]");
            }
            System.out.println("5. Exit");
            System.out.print("Pick: ");

            String pick = sc.nextLine().trim();

            if (pick.equals("1")) {
                doDeposit(me);

            } else if (pick.equals("2")) {
                doWithdraw(me);

            } else if (pick.equals("3")) {
                me.printMiniStatement();

            } else if (pick.equals("4")) {
                try {
                    vault.showAllAccounts(me);
                } catch (UnauthorizedAccessException ex) {
                    System.out.println("Access denied: " + ex.getMessage());
                }

            } else if (pick.equals("5")) {
                System.out.println("Bye, " + me.getOwner() + "!");
                active = false;

            } else {
                System.out.println("Not a valid option, try again.");
            }
        }

        sc.close();
    }

    static void doDeposit(Account acc) {
        System.out.print("Amount to deposit: $");
        String raw = sc.nextLine().trim();

        if (raw.isEmpty()) {
            System.out.println("You didn't enter anything.");
            return;
        }

        try {
            double amt = Double.parseDouble(raw);
            acc.deposit(amt);
        } catch (NumberFormatException e) {
            System.out.println("That doesn't look like a number: " + raw);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    static void doWithdraw(Account acc) {
        System.out.print("Amount to withdraw: $");
        String raw = sc.nextLine().trim();

        if (raw.isEmpty()) {
            System.out.println("You didn't enter anything.");
            return;
        }

        try {
            double amt = Double.parseDouble(raw);
            acc.withdraw(amt);
        } catch (NumberFormatException e) {
            System.out.println("That doesn't look like a number: " + raw);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
