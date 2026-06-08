// Java 9+
/**
 * Listing 3.7 — WithdrawalPolicyWithPrivate.java
 * Demonstrates: Private methods in interfaces for encapsulating shared logic
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 9+
 */
package chapter03;

@FunctionalInterface
interface WithdrawalPolicy {
    boolean canWithdraw(double balance, double amount);

    default WithdrawalPolicy and(WithdrawalPolicy other) {
        return (balance, amount) ->
                this.canWithdraw(balance, amount)
                        && other.canWithdraw(balance, amount);
    }

    // Private method encapsulates shared validation logic
    private boolean isValidAmount(double amount) {
        return amount > 0; // Amount must be positive
    }

    default boolean validate(double balance, double amount) {
        return isValidAmount(amount) && canWithdraw(balance, amount);
    }
}

public class WithdrawalPolicyWithPrivate {

    public static void main(String[] args) {
        // Policy: no overdraft allowed
        WithdrawalPolicy noOverdraft = (balance, amount) -> amount <= balance;

        // Policy: max single transaction is $500
        WithdrawalPolicy maxTransactionLimit = (balance, amount) -> amount <= 500;

        // Combine both policies using the default and() method
        WithdrawalPolicy combinedPolicy = noOverdraft.and(maxTransactionLimit);

        double balance = 800.0;

        // validate() uses the private isValidAmount() internally
        System.out.println("Withdraw $300 from $800 balance: "
                + combinedPolicy.validate(balance, 300));  // true

        System.out.println("Withdraw $600 from $800 balance: "
                + combinedPolicy.validate(balance, 600));  // false (exceeds $500 limit)

        System.out.println("Withdraw $900 from $800 balance: "
                + combinedPolicy.validate(balance, 900));  // false (overdraft)

        System.out.println("Withdraw $-50 from $800 balance: "
                + combinedPolicy.validate(balance, -50)); // false (invalid amount)

        // Output:
        // Withdraw $300 from $800 balance: true
        // Withdraw $600 from $800 balance: false
        // Withdraw $900 from $800 balance: false
        // Withdraw $-50 from $800 balance: false
    }
}