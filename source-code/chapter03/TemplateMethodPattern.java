// Java 8+
/**
 * Listing 3.8 — TemplateMethodPattern.java
 * Demonstrates: Template Method Pattern using abstract classes
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+
 */
package chapter03;

public class TemplateMethodPattern {

    // Abstract base class defines the fixed algorithm skeleton
    abstract static class PaymentProcessor {

        // Template method — final prevents subclasses from altering the algorithm
        public final void process(double amount) {
            validate(amount);   // shared step
            debit(amount);      // variation point
            notifyUser(amount); // shared step
        }

        // Subclasses must provide their own debit implementation
        protected abstract void debit(double amount);

        // Shared validation — can be overridden if needed
        protected void validate(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid amount: " + amount);
            }
        }

        // Default notification — can be overridden if needed
        protected void notifyUser(double amount) {
            System.out.println("Payment of " + amount + " processed.");
        }
    }

    // Concrete subclass — only varies the debit step
    static class CreditCardPayment extends PaymentProcessor {

        @Override
        protected void debit(double amount) {
            System.out.println("Debiting credit card for " + amount);
        }
    }

    // Another concrete subclass — different debit behaviour
    static class UpiPayment extends PaymentProcessor {

        @Override
        protected void debit(double amount) {
            System.out.println("Processing UPI payment for " + amount);
        }
    }

    public static void main(String[] args) {
        PaymentProcessor card = new CreditCardPayment();
        card.process(150.00); // fixed algorithm, credit card debit step

        System.out.println("---");

        PaymentProcessor upi = new UpiPayment();
        upi.process(75.50);   // fixed algorithm, UPI debit step

        try {
            card.process(-10); // triggers shared validation
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        // Output:
        // Debiting credit card for 150.0
        // Payment of 150.0 processed.
        // ---
        // Processing UPI payment for 75.5
        // Payment of 75.5 processed.
        // Caught: Invalid amount: -10.0
    }
}