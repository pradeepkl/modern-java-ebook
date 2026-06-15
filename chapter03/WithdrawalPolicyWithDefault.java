// Java 25+
// Feature shown: default methods in interfaces, final in Java 8+

/**
 * Listing 3.5 — WithdrawalPolicyWithDefault.java
 * Demonstrates: composing functional interfaces using default methods
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

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

public class WithdrawalPolicyWithDefault {

    private static final Logger LOG =
            Logger.getLogger(WithdrawalPolicyWithDefault.class.getName());

    void main() {
        // Policy: withdrawal must not exceed current balance
        WithdrawalPolicy noOverdraft =
                (balance, amount) -> amount <= balance;

        // Policy: single transaction must not exceed 500
        WithdrawalPolicy maxTransactionLimit =
                (balance, amount) -> amount <= 500;

        // Combine policies using the default and() method
        WithdrawalPolicy combinedPolicy =
                noOverdraft.and(maxTransactionLimit);

        double balance = 800.0;

        // Passes both policies: within balance and under limit
        LOG.info("Withdraw 300 from 800: "
                + combinedPolicy.canWithdraw(balance, 300));

        // Fails maxTransactionLimit: exceeds 500
        LOG.info("Withdraw 600 from 800: "
                + combinedPolicy.canWithdraw(balance, 600));

        // Fails noOverdraft: exceeds balance
        LOG.info("Withdraw 900 from 800: "
                + combinedPolicy.canWithdraw(balance, 900));

        // Output:
        // Withdraw 300 from 800: true
        // Withdraw 600 from 800: false
        // Withdraw 900 from 800: false
    }
}