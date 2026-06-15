// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 3.9 — UncontrolledInheritance.java
 * Demonstrates: the risks of uncontrolled inheritance where a subclass
 * violates the intended design of a base class by overriding a method
 * and introducing unexpected side effects via direct field access.
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

class BankAccount {

    private static final Logger log = Logger.getLogger(BankAccount.class.getName());

    // Protected field — accessible to subclasses, but risky
    protected double balance;

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        balance += amount; // Intended: only increment by the deposited amount
        log.info("Deposited " + amount + ", balance is now " + balance);
    }

    public double getBalance() {
        return balance;
    }
}

// Subclass that violates the intended design of BankAccount
class SavingsAccount extends BankAccount {

    private final double interestRate;

    public SavingsAccount(double interestRate) {
        this.interestRate = interestRate;
    }

    // Overriding deposit to silently add interest as a side effect
    @Override
    public void deposit(double amount) {
        super.deposit(amount);                  // Calls base deposit
        balance += amount * interestRate;       // Mutates protected field directly
    }
}

public class UncontrolledInheritance {

    private static final Logger log = Logger.getLogger(UncontrolledInheritance.class.getName());

    void main() {
        BankAccount regular = new BankAccount();
        regular.deposit(1000.0);
        log.info("Regular account balance: " + regular.getBalance());

        // SavingsAccount silently inflates balance beyond the deposited amount
        SavingsAccount savings = new SavingsAccount(0.05);
        savings.deposit(1000.0);
        log.info("Savings account balance: " + savings.getBalance());

        // Output:
        // Deposited 1000.0, balance is now 1000.0
        // Regular account balance: 1000.0
        // Deposited 1000.0, balance is now 1000.0
        // Savings account balance: 1050.0
    }
}