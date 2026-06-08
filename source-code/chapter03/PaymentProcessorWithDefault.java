// Java 8+
/**
 * Listing 3.4 — PaymentProcessorWithDefault.java
 * Demonstrates: Default methods in functional interfaces
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+
 */
package chapter03;

public class PaymentProcessorWithDefault {

    // Functional interface with one abstract method and one default method
    @FunctionalInterface
    interface PaymentProcessor {
        // Abstract method — must be implemented (e.g., via lambda)
        void processPayment(double amount);

        // Default method — shared implementation available to all instances
        default void refundPayment(double amount) {
            System.out.println("Refunding payment of $" + amount);
        }
    }

    public static void main(String[] args) {

        // Lambda provides only the abstract method implementation
        PaymentProcessor creditCardProcessor =
                amount -> System.out.println(
                        "Processing credit card payment of $" + amount);

        PaymentProcessor paypalProcessor =
                amount -> System.out.println(
                        "Processing PayPal payment of $" + amount);

        // Uses the default refundPayment() from the interface
        creditCardProcessor.refundPayment(100.0);

        // Uses the lambda-provided processPayment() implementation
        creditCardProcessor.processPayment(150.0);

        // PayPal processor also inherits the default refund behavior
        paypalProcessor.refundPayment(75.0);

        // PayPal processor uses its own lambda for processing
        paypalProcessor.processPayment(200.0);

        // Output:
        // Refunding payment of $100.0
        // Processing credit card payment of $150.0
        // Refunding payment of $75.0
        // Processing PayPal payment of $200.0
    }
}