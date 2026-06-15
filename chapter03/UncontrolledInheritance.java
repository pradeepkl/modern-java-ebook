// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

package chapter03;

import java.util.logging.Logger;

/**
 * Listing 3.9 — UncontrolledInheritance.java
 * Demonstrates: the risks of uncontrolled inheritance where a subclass
 * overrides a method and silently mutates protected state, violating
 * the intended contract of the base class.
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
class BankAccount {

    private static final Logger log = Logger.getLogger(BankAccount.class.getName());

    protected double balance; // accessible to subclasses — a design risk

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        balance += amount; // straightforward deposit
    }

    public double getBalance() {
        return balance;
    }
}

class SavingsAccount extends BankAccount {

    private static final Logger log = Logger.getLogger(SavingsAccount.class.getName());

    private final double interestRate;

    public SavingsAccount(double interestRate) {
        this.interestRate = interestRate;
    }

    // Overriding deposit to silently add interest as a side effect
    // This violates the caller's expectation that deposit(100) adds exactly 100
    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        balance += amount * interestRate; // direct mutation of protected field
    }
}

public class UncontrolledInheritance {

    private static final Logger log = Logger.getLogger(UncontrolledInheritance.class.getName());

    void main() {
        BankAccount account = new BankAccount();
        account.deposit(100.0);
        log.info("BankAccount balance after deposit of 100: " + account.getBalance());

        SavingsAccount savings = new SavingsAccount(0.05); // 5 percent interest rate
        savings.deposit(100.0); // caller expects balance to be 100, but it is 105
        log.info("SavingsAccount balance after deposit of 100 at 5 pct interest: "
                + savings.getBalance());

        // Output:
        // BankAccount balance after deposit of 100: 100.0
        // SavingsAccount balance after deposit of 100 at 5 pct interest: 105.0
    }
}