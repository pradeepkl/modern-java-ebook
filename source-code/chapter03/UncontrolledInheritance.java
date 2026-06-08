// Java 8+
/**
 * Listing 3.9 — UncontrolledInheritance.java
 * Demonstrates: How unrestricted inheritance allows subclasses to violate
 *               the intended design and invariants of a base class.
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+
 */
package chapter03;

// Base class with a clear contract: deposit must be a positive amount
class BankAccount {
    protected double balance;

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        balance += amount; // Only the deposited amount should be added
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
        super.deposit(amount);
        balance += amount * interestRate; // Mutates balance beyond the contract
    }
}

public class UncontrolledInheritance {

    public static void main(String[] args) {
        BankAccount basic = new BankAccount();
        basic.deposit(1000.0);
        // Expected: balance == 1000.0
        System.out.println("BankAccount balance after deposit of 1000: "
                + basic.getBalance());

        SavingsAccount savings = new SavingsAccount(0.05);
        savings.deposit(1000.0);
        // Unexpected: balance == 1050.0 due to silent side effect in override
        System.out.println("SavingsAccount balance after deposit of 1000 (rate=5%): "
                + savings.getBalance());

        // Demonstrates the Liskov Substitution Principle violation:
        // treating SavingsAccount as BankAccount yields surprising results
        BankAccount polymorphic = new SavingsAccount(0.10);
        polymorphic.deposit(500.0);
        System.out.println("Polymorphic SavingsAccount balance after deposit of 500 (rate=10%): "
                + polymorphic.getBalance());

        // Output:
        // BankAccount balance after deposit of 1000: 1000.0
        // SavingsAccount balance after deposit of 1000 (rate=5%): 1050.0
        // Polymorphic SavingsAccount balance after deposit of 500 (rate=10%): 550.0
    }
}