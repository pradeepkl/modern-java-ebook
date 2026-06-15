// Java 25+
// Feature shown: sealed classes and interfaces, final in Java 17+
/**
 * Listing 3.10 — SealedBankAccount.java
 * Demonstrates: sealed classes with permitted subclasses, preventing
 * unintended extension and preserving behavioral contracts via final methods
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 17+ for sealed classes; Java 25+ for void main()
 * instance main method (JEP 512)
 */
package chapter03;

import java.util.logging.Logger;

public sealed class SealedBankAccount
        permits SealedBankAccount.SavingsAccount, SealedBankAccount.CheckingAccount {

    private static final Logger LOG = Logger.getLogger(SealedBankAccount.class.getName());

    protected double balance;

    // final prevents subclasses from overriding and altering deposit semantics
    public final void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount; // only permitted subclasses can extend this class
    }

    public double getBalance() {
        return balance;
    }

    // --- Permitted subclass 1 ---
    static final class SavingsAccount extends SealedBankAccount {
        private final double interestRate;

        SavingsAccount(double interestRate) {
            this.interestRate = interestRate; // rate stored; deposit() cannot be overridden
        }

        double getInterestRate() {
            return interestRate;
        }
    }

    // --- Permitted subclass 2 ---
    static final class CheckingAccount extends SealedBankAccount {
        private final double overdraftLimit;

        CheckingAccount(double overdraftLimit) {
            this.overdraftLimit = overdraftLimit;
        }

        double getOverdraftLimit() {
            return overdraftLimit;
        }
    }

    void main() {
        SavingsAccount savings = new SavingsAccount(0.03);
        savings.deposit(500.00);
        LOG.info("Savings balance: " + savings.getBalance()); // 500.0

        CheckingAccount checking = new CheckingAccount(200.00);
        checking.deposit(1000.00);
        checking.deposit(250.00);
        LOG.info("Checking balance: " + checking.getBalance()); // 1250.0
        LOG.info("Overdraft limit: " + checking.getOverdraftLimit()); // 200.0

        // Compiler enforces the sealed hierarchy — no other class may extend BankAccount
        // Output:
        // INFO: Savings balance: 500.0
        // INFO: Checking balance: 1250.0
        // INFO: Overdraft limit: 200.0
    }
}