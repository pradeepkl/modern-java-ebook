// Java 25+
// Feature shown: sealed classes and interfaces, final in Java 17+

/**
 * Listing 3.10 — SealedBankAccount.java
 * Demonstrates: sealed classes restricting inheritance to permitted subclasses
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 17+ for sealed classes; Java 25+ for void main() instance
 * main method (JEP 512: Compact Source Files and Instance Main Methods)
 */
package chapter03;

import java.util.logging.Logger;

public class SealedBankAccount {

    private static final Logger LOG = Logger.getLogger(SealedBankAccount.class.getName());

    // Sealed class: only SavingsAccount and CheckingAccount may extend it
    public sealed static class BankAccount
            permits SavingsAccount, CheckingAccount {

        protected double balance;

        // final prevents subclasses from overriding deposit behavior
        public final void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid amount");
            }
            balance += amount;
        }

        public double getBalance() {
            return balance;
        }
    }

    // Must be final, non-sealed, or sealed itself
    public static final class SavingsAccount extends BankAccount {

        private final double interestRate;

        public SavingsAccount(double interestRate) {
            this.interestRate = interestRate;
        }

        public double getInterestRate() {
            return interestRate;
        }
    }

    public static final class CheckingAccount extends BankAccount {

        private final double overdraftLimit;

        public CheckingAccount(double overdraftLimit) {
            this.overdraftLimit = overdraftLimit;
        }

        public double getOverdraftLimit() {
            return overdraftLimit;
        }
    }

    void main() {
        SavingsAccount savings = new SavingsAccount(0.03);
        savings.deposit(500.0);
        LOG.info("Savings balance: " + savings.getBalance()
                + ", rate: " + savings.getInterestRate());

        CheckingAccount checking = new CheckingAccount(200.0);
        checking.deposit(1000.0);
        LOG.info("Checking balance: " + checking.getBalance()
                + ", overdraft limit: " + checking.getOverdraftLimit());

        // Output:
        // INFO: Savings balance: 500.0, rate: 0.03
        // INFO: Checking balance: 1000.0, overdraft limit: 200.0
    }
}