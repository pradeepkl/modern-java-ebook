// Java 8+
/**
 * Listing 3.3 — PaymentProcessorInterface.java
 * Demonstrates: Functional interfaces as behavioral blueprints with lambda implementations
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+
 */
package chapter03;

/**
 * A functional interface defining a single payment processing contract.
 * Any lambda with signature (double) -> void satisfies this interface.
 */
@FunctionalInterface
interface PaymentProcessor {
    void processPayment(double amount); // single abstract method
}

public class PaymentProcessorInterface {

    public static void main(String[] args) {

        // Lambda implementation: credit card processor
        PaymentProcessor creditCardProcessor =
                amount -> System.out.println(
                        "Processing credit card payment of $" + amount);

        // Lambda implementation: PayPal processor
        PaymentProcessor paypalProcessor =
                amount -> System.out.println(
                        "Processing PayPal payment of $" + amount);

        // Lambda implementation: cryptocurrency processor
        PaymentProcessor cryptoProcessor =
                amount -> System.out.println(
                        "Processing cryptocurrency payment of $" + amount);

        // Each processor is a distinct behavioral strategy — no subclasses needed
        creditCardProcessor.processPayment(100.0);
        paypalProcessor.processPayment(150.0);
        cryptoProcessor.processPayment(200.0);

        // Processors can be selected dynamically at runtime
        System.out.println("\n--- Dynamic dispatch via interface reference ---");
        PaymentProcessor selected = selectProcessor("paypal");
        selected.processPayment(299.99);

        // Output:
        // Processing credit card payment of $100.0
        // Processing PayPal payment of $150.0
        // Processing cryptocurrency payment of $200.0
        //
        // --- Dynamic dispatch via interface reference ---
        // Processing PayPal payment of $299.99
    }

    /** Returns a processor strategy based on a runtime string value. */
    static PaymentProcessor selectProcessor(String type) {
        return switch (type) {                                  // switch expression (Java 14+)
            case "credit" -> amount -> System.out.println("Processing credit card payment of $" + amount);
            case "paypal" -> amount -> System.out.println("Processing PayPal payment of $" + amount);
            default       -> amount -> System.out.println("Processing cryptocurrency payment of $" + amount);
        };
    }
}