// Java 25+
// Feature shown: default methods in interfaces, final in Java 8+

/**
 * Listing 3.4 — PaymentProcessorWithDefault.java
 * Demonstrates: default methods in interfaces allowing shared behavior
 * alongside abstract contract methods, enabling interface evolution
 * without breaking existing implementations.
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+ for default methods; Java 25+ for void main()
 * instance main method (JEP 512)
 */
package chapter03;

import java.util.logging.Logger;

public class PaymentProcessorWithDefault {

    private static final Logger LOG =
            Logger.getLogger(PaymentProcessorWithDefault.class.getName());

    // Functional interface with one abstract method and one default method
    @FunctionalInterface
    interface PaymentProcessor {

        // Abstract method — must be implemented (here via lambda)
        void processPayment(double amount);

        // Default method — shared implementation available to all instances
        default void refundPayment(double amount) {
            LOG.info("Refunding payment of $" + amount);
        }
    }

    void main() {
        // Lambda satisfies the single abstract method processPayment
        PaymentProcessor creditCardProcessor =
                amount -> LOG.info(
                        "Processing credit card payment of $" + amount);

        // Calls the default method — no override needed
        creditCardProcessor.refundPayment(100.0);

        // Calls the lambda-provided implementation
        creditCardProcessor.processPayment(150.0);

        // A second processor with its own lambda, sharing the same default
        PaymentProcessor paypalProcessor =
                amount -> LOG.info(
                        "Processing PayPal payment of $" + amount);

        paypalProcessor.refundPayment(75.0);
        paypalProcessor.processPayment(75.0);

        // Output:
        // INFO: Refunding payment of $100.0
        // INFO: Processing credit card payment of $150.0
        // INFO: Refunding payment of $75.0
        // INFO: Processing PayPal payment of $75.0
    }
}