// Java 25+
// Feature shown: composition with functional interfaces, final in Java 8+

/**
 * Listing 3.2 — CompositionOverInheritance.java
 * Demonstrates: composition with functional interfaces to replace fragile inheritance
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

@FunctionalInterface
interface WithdrawalPolicy {
    boolean canWithdraw(double balance, double amount);
}

class Account {
    private double balance;

    public Account(double balance) {
        this.balance = balance;
    }

    public double balance() {
        return balance;
    }

    void debit(double amount) {
        balance -= amount;
    }
}

class WithdrawableAccount {
    private final Account account;
    private final WithdrawalPolicy policy;

    public WithdrawableAccount(Account account, WithdrawalPolicy policy) {
        this.account = account;
        this.policy = policy;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        if (!policy.canWithdraw(account.balance(), amount)) {
            throw new IllegalStateException("Insufficient balance");
        }
        account.debit(amount);
    }

    public double balance() {
        return account.balance();
    }
}

public class CompositionOverInheritance {

    private static final Logger LOG =
            Logger.getLogger(CompositionOverInheritance.class.getName());

    void main() {
        // Policies expressed as lambdas — behavior is explicit and swappable
        WithdrawalPolicy noOverdraft =
                (balance, amount) -> amount <= balance;

        WithdrawalPolicy overdraftUpTo1000 =
                (balance, amount) -> amount <= balance + 1000;

        // Account holds state only — no behavioral assumptions baked in
        Account baseAccount = new Account(500);
        Account overdraftBase = new Account(500);

        WithdrawableAccount regularAccount =
                new WithdrawableAccount(baseAccount, noOverdraft);

        WithdrawableAccount overdraftAccount =
                new WithdrawableAccount(overdraftBase, overdraftUpTo1000);

        // Regular account rejects withdrawal exceeding balance
        try {
            regularAccount.withdraw(600); // Throws IllegalStateException
            LOG.info("Regular account withdrew 600");
        } catch (IllegalStateException e) {
            LOG.info("Regular account blocked: " + e.getMessage());
        }

        // Overdraft account permits withdrawal within overdraft limit
        overdraftAccount.withdraw(600); // Succeeds
        LOG.info("Overdraft account balance after 600 withdrawal: "
                + overdraftAccount.balance());

        // Output:
        // Regular account blocked: Insufficient balance
        // Overdraft account balance after 600 withdrawal: -100.0
    }
}