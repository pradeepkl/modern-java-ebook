// Java 8+
/**
 * Listing 3.2 — CompositionOverInheritance.java
 * Demonstrates: Composition over inheritance using functional interfaces
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+
 */
package chapter03;

// Functional interface representing a pluggable withdrawal policy
@FunctionalInterface
interface WithdrawalPolicy {
    boolean canWithdraw(double balance, double amount);
}

// Account holds state only — no behavioral assumptions baked in
class Account {
    private double balance;

    public Account(double balance) {
        this.balance = balance;
    }

    public double balance() {
        return balance;
    }

    void debit(double amount) {
        balance -= amount; // Reduces balance unconditionally
    }
}

// Composed class: combines Account (state) with WithdrawalPolicy (behavior)
class WithdrawableAccount {
    private final Account account;
    private final WithdrawalPolicy policy; // Behavior injected, not inherited

    public WithdrawableAccount(Account account, WithdrawalPolicy policy) {
        this.account = account;
        this.policy = policy;
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
        if (!policy.canWithdraw(account.balance(), amount))
            throw new IllegalStateException("Insufficient balance");
        account.debit(amount);
        System.out.printf("Withdrew %.0f — balance now %.0f%n", amount, account.balance());
    }
}

public class CompositionOverInheritance {
    public static void main(String[] args) {
        // Different policies expressed as lambdas — no subclassing needed
        WithdrawalPolicy noOverdraft       = (balance, amount) -> amount <= balance;
        WithdrawalPolicy overdraftUpTo1000 = (balance, amount) -> amount <= balance + 1000;

        Account baseAccount = new Account(500);

        // Same state, different behavior — composed at construction time
        WithdrawableAccount regularAccount  = new WithdrawableAccount(new Account(500), noOverdraft);
        WithdrawableAccount overdraftAccount = new WithdrawableAccount(baseAccount, overdraftUpTo1000);

        // Regular account rejects overdraft
        try {
            regularAccount.withdraw(600); // Exceeds balance of 500
        } catch (IllegalStateException e) {
            System.out.println("Regular account: " + e.getMessage()); // Insufficient balance
        }

        // Overdraft account allows withdrawal within overdraft limit
        overdraftAccount.withdraw(600); // balance=500, overdraft allows up to 1500

        // Output:
        // Regular account: Insufficient balance
        // Withdrew 600 — balance now -100
    }
}