package smartvault;

import java.util.ArrayList;

public class VaultManager {

    private ArrayList<Account> allAccounts = new ArrayList<>();

    public void addAccount(Account acc) {
        allAccounts.add(acc);
    }

    public Account findById(int id) {
        for (Account a : allAccounts) {
            if (a.getId() == id) return a;
        }
        System.out.println("[WARN] No account found with ID: " + id);
        return null;
    }

    public void showAllAccounts(Account caller) throws UnauthorizedAccessException {
        if (caller.getRole() != Account.Role.ADMIN) {
            System.out.println("[ERROR] User '" + caller.getOwner() + "' tried to access admin panel.");
            throw new UnauthorizedAccessException("View All Accounts");
        }
        System.out.println("\n  [ADMIN] All registered accounts:");
        System.out.println("  " + "-".repeat(50));
        for (Account a : allAccounts) {
            System.out.printf("  ID: %d  |  %-18s  |  %-5s  |  $%.2f%n",
                    a.getId(), a.getOwner(), a.getRole(), a.getBalance());
        }
        System.out.println("  " + "-".repeat(50));
    }
}
