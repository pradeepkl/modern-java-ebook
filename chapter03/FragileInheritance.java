// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 3.1 — FragileInheritance.java
 * Demonstrates: how classical inheritance breaks contracts silently
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

class Account {
    protected double balance;

    Account(double initialBalance) {
        this.balance = initialBalance;
    }

    // Base class enforces validation and invariants
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        if (amount > balance) {
            throw new IllegalStateException("Insufficient balance");
        }
        balance -= amount; // Invariant: balance never goes negative
    }

    public double getBalance() {
        return balance;
    }
}

class OverdraftAccount extends Account {

    OverdraftAccount(double initialBalance) {
        super(initialBalance);
    }

    @Override
    public void withdraw(double amount) {
        // Silently breaks the base class contract: balance can go negative
        if (amount <= balance + 1000) {
            balance -= amount; // No validation of amount <= 0
        }
    }
}

public class FragileInheritance {

    private static final Logger LOG = Logger.getLogger(FragileInheritance.class.getName());

    void main() {
        Account standard = new Account(500.0);
        standard.withdraw(200.0);
        LOG.info("Standard balance after withdraw: " + standard.getBalance()); // 300.0

        OverdraftAccount overdraft = new OverdraftAccount(500.0);
        overdraft.withdraw(1400.0); // Allowed: 1400 <= 500 + 1000
        LOG.info("Overdraft balance after withdraw: " + overdraft.getBalance()); // -900.0

        // Base class contract promised balance >= 0; subclass silently violates it
        LOG.info("Contract broken — negative balance: " + (overdraft.getBalance() < 0));

        // Output:
        // Standard balance after withdraw: 300.0
        // Overdraft balance after withdraw: -900.0
        // Contract broken -- negative balance: true
    }
}