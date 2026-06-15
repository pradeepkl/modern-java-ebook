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

    private static final Logger log =
            Logger.getLogger(CompositionOverInheritance.class.getName());

    void main() {
        // Policies expressed as lambdas — behavior is explicit and swappable
        WithdrawalPolicy noOverdraft =
                (balance, amount) -> amount <= balance;

        WithdrawalPolicy overdraftUpTo1000 =
                (balance, amount) -> amount <= balance + 1000;

        // Separate accounts to demonstrate each policy independently
        Account baseForRegular = new Account(500);
        Account baseForOverdraft = new Account(500);

        WithdrawableAccount regularAccount =
                new WithdrawableAccount(baseForRegular, noOverdraft);

        WithdrawableAccount overdraftAccount =
                new WithdrawableAccount(baseForOverdraft, overdraftUpTo1000);

        // Regular account rejects withdrawal exceeding balance
        try {
            regularAccount.withdraw(600); // Throws IllegalStateException
            log.info("Regular account withdrew 600");
        } catch (IllegalStateException e) {
            log.info("Regular account rejected: " + e.getMessage());
        }

        // Overdraft account permits withdrawal within overdraft limit
        overdraftAccount.withdraw(600); // Succeeds
        log.info("Overdraft account balance after 600 withdrawal: "
                + overdraftAccount.balance());

        // Output:
        // Regular account rejected: Insufficient balance
        // Overdraft account balance after 600 withdrawal: -100.0
    }
}