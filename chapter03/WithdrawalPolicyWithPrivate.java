// Java 25+
// Feature shown: private methods in interfaces, final in Java 9+

/**
 * Listing 3.7 — WithdrawalPolicyWithPrivate.java
 * Demonstrates: private methods in interfaces for encapsulating shared logic
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 9+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

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
        return amount > 0;
    }

    default boolean validate(double balance, double amount) {
        return isValidAmount(amount) && canWithdraw(balance, amount);
    }
}

public class WithdrawalPolicyWithPrivate {

    private static final Logger LOG =
            Logger.getLogger(WithdrawalPolicyWithPrivate.class.getName());

    void main() {
        // No overdraft: withdrawal must not exceed balance
        WithdrawalPolicy noOverdraft = (balance, amount) -> amount <= balance;

        // Max transaction limit: single withdrawal capped at 500
        WithdrawalPolicy maxLimit = (balance, amount) -> amount <= 500;

        // Combine both policies using the default and() method
        WithdrawalPolicy combined = noOverdraft.and(maxLimit);

        double balance = 800.0;

        // validate() uses the private isValidAmount() internally
        LOG.info("Withdraw 300 from 800: " + combined.validate(balance, 300));
        LOG.info("Withdraw 600 from 800: " + combined.validate(balance, 600));
        LOG.info("Withdraw -50 from 800: " + combined.validate(balance, -50));
        LOG.info("Withdraw 0 from 800:   " + combined.validate(balance, 0));

        // Output:
        // Withdraw 300 from 800: true
        // Withdraw 600 from 800: false
        // Withdraw -50 from 800: false
        // Withdraw 0 from 800:   false
    }
}