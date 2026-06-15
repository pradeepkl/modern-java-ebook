// Java 25+
// Feature shown: private methods in interfaces, final in Java 9+

/**
 * Listing 3.7 — WithdrawalPolicyWithPrivate.java
 * Demonstrates: private methods in interfaces for encapsulating shared logic
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 9+ for private interface methods; void main() requires Java 25+
 * (compiled with --enable-preview --release 21 for the void main() instance main method)
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

    void main() {
        Logger log = Logger.getLogger(WithdrawalPolicyWithPrivate.class.getName());

        // No overdraft: withdrawal must not exceed balance
        WithdrawalPolicy noOverdraft = (balance, amount) -> amount <= balance;

        // Max transaction limit: withdrawal must not exceed 500
        WithdrawalPolicy maxLimit = (balance, amount) -> amount <= 500;

        // Combine both policies using the default and() method
        WithdrawalPolicy combined = noOverdraft.and(maxLimit);

        // validate() uses the private isValidAmount() internally
        log.info("validate(1000, 300): " + combined.validate(1000, 300));  // true
        log.info("validate(1000, 600): " + combined.validate(1000, 600));  // false (exceeds limit)
        log.info("validate(200, 300):  " + combined.validate(200, 300));   // false (overdraft)
        log.info("validate(1000, -50): " + combined.validate(1000, -50));  // false (invalid amount)

        // Output:
        // validate(1000, 300): true
        // validate(1000, 600): false
        // validate(200, 300):  false
        // validate(1000, -50): false
    }
}