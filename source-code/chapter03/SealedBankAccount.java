// Java 17+
/**
 * Listing 3.10 — SealedBankAccount.java
 * Demonstrates: Sealed classes with permitted subclasses for controlled inheritance
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 17+
 */
package chapter03;

// Sealed base class — only SavingsAccount and CheckingAccount may extend it
sealed class BankAccount permits SavingsAccount, CheckingAccount {

    protected double balance;

    // final prevents overriding to preserve deposit behavior across all subclasses
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

// Must be final, sealed, or non-sealed — here final closes the hierarchy
final class SavingsAccount extends BankAccount {

    private final double interestRate;

    public SavingsAccount(double interestRate) {
        this.interestRate = interestRate; // e.g. 0.05 for 5%
    }

    public void applyInterest() {
        balance += balance * interestRate; // interest applied explicitly, not via deposit
    }

    public double getInterestRate() {
        return interestRate;
    }
}

// CheckingAccount is also final — no further extension permitted
final class CheckingAccount extends BankAccount {

    private final double overdraftLimit;

    public CheckingAccount(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}

public class SealedBankAccount {

    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount(0.05);
        savings.deposit(1000.00);         // deposit is final — behavior is guaranteed
        savings.applyInterest();          // interest applied separately, not as side effect
        System.out.println("Savings balance after interest: " + savings.getBalance());

        CheckingAccount checking = new CheckingAccount(500.00);
        checking.deposit(250.00);         // same final deposit method — consistent behavior
        System.out.println("Checking balance: " + checking.getBalance());
        System.out.println("Overdraft limit: " + checking.getOverdraftLimit());

        // Compiler knows all permitted subclasses — exhaustive reasoning is possible
        BankAccount account = savings;
        if (account instanceof SavingsAccount sa) {
            System.out.println("Interest rate: " + sa.getInterestRate());
        }

        // Output:
        // Savings balance after interest: 1050.0
        // Checking balance: 250.0
        // Overdraft limit: 500.0
        // Interest rate: 0.05
    }
}