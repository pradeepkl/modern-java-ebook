// Java 8+
/**
 * Listing 3.5 — WithdrawalPolicyWithDefault.java
 * Demonstrates: Functional interfaces with default methods for policy composition
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+
 */
package chapter03;

public class WithdrawalPolicyWithDefault {

    // Functional interface with a default composition method
    @FunctionalInterface
    interface WithdrawalPolicy {
        boolean canWithdraw(double balance, double amount);

        // Combine two policies: both must allow the withdrawal
        default WithdrawalPolicy and(WithdrawalPolicy other) {
            return (balance, amount) ->
                    this.canWithdraw(balance, amount)
                            && other.canWithdraw(balance, amount);
        }
    }

    public static void main(String[] args) {

        // Policy 1: no overdraft allowed
        WithdrawalPolicy noOverdraft =
                (balance, amount) -> amount <= balance;

        // Policy 2: single transaction capped at $500
        WithdrawalPolicy maxTransactionLimit =
                (balance, amount) -> amount <= 500;

        // Combine policies using the default method
        WithdrawalPolicy combinedPolicy =
                noOverdraft.and(maxTransactionLimit);

        double balance = 800.0;

        // Test individual policies
        System.out.println("Balance: $" + balance);
        System.out.println("--- No Overdraft Policy ---");
        System.out.println("Withdraw $300: " + noOverdraft.canWithdraw(balance, 300));   // true
        System.out.println("Withdraw $900: " + noOverdraft.canWithdraw(balance, 900));   // false

        System.out.println("--- Max Transaction Policy ---");
        System.out.println("Withdraw $300: " + maxTransactionLimit.canWithdraw(balance, 300)); // true
        System.out.println("Withdraw $600: " + maxTransactionLimit.canWithdraw(balance, 600)); // false

        System.out.println("--- Combined Policy ---");
        System.out.println("Withdraw $300: " + combinedPolicy.canWithdraw(balance, 300)); // true
        System.out.println("Withdraw $600: " + combinedPolicy.canWithdraw(balance, 600)); // false (exceeds limit)
        System.out.println("Withdraw $900: " + combinedPolicy.canWithdraw(balance, 900)); // false (overdraft)

        // Output:
        // Balance: $800.0
        // --- No Overdraft Policy ---
        // Withdraw $300: true
        // Withdraw $900: false
        // --- Max Transaction Policy ---
        // Withdraw $300: true
        // Withdraw $600: false
        // --- Combined Policy ---
        // Withdraw $300: true
        // Withdraw $600: false
        // Withdraw $900: false
    }
}