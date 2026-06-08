// Java 8+
/**
 * Listing 3.1 — FragileInheritance.java
 * Demonstrates: How classical inheritance creates fragile contracts
 *               when subclasses silently override base class behavior.
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+
 */
package chapter03;

public class FragileInheritance {

    // Base class with explicit validation contract
    static class Account {
        protected double balance;

        public Account(double initialBalance) {
            this.balance = initialBalance;
        }

        // Contract: amount must be positive and not exceed balance
        public void withdraw(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid amount");
            }
            if (amount > balance) {
                throw new IllegalStateException("Insufficient balance");
            }
            balance -= amount; // Safe deduction after validation
        }

        public double getBalance() {
            return balance;
        }
    }

    // Subclass silently breaks the base class contract
    static class OverdraftAccount extends Account {

        public OverdraftAccount(double initialBalance) {
            super(initialBalance);
        }

        @Override
        public void withdraw(double amount) {
            // Skips validation — allows overdraft up to 1000
            // Callers expecting IllegalArgumentException for amount <= 0
            // will be silently surprised: no exception is thrown
            if (amount <= balance + 1000) {
                balance -= amount; // May go negative — contract broken
            }
        }
    }

    public static void main(String[] args) {
        Account regular = new Account(500.0);
        regular.withdraw(200.0);
        System.out.println("Regular balance after withdraw: " + regular.getBalance());

        // Polymorphic call — caller expects base class contract
        Account overdraft = new OverdraftAccount(500.0);
        overdraft.withdraw(1200.0); // Silently succeeds — no exception!
        System.out.println("Overdraft balance after withdraw: " + overdraft.getBalance());

        // Negative amount — base class throws, subclass silently ignores
        overdraft.withdraw(-50.0);
        System.out.println("Overdraft balance after invalid withdraw: " + overdraft.getBalance());

        // Output:
        // Regular balance after withdraw: 300.0
        // Overdraft balance after withdraw: -700.0
        // Overdraft balance after invalid withdraw: -700.0
    }
}