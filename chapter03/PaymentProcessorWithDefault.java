// Java 25+
// Feature shown: default methods in interfaces, final in Java 8+

/**
 * Listing 3.4 — PaymentProcessorWithDefault.java
 * Demonstrates: default methods in interfaces allowing shared behavior
 *               alongside abstract method contracts and lambda implementations
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (uses void main() instance main method, JEP 512)
 */
package chapter03;

import java.util.logging.Logger;

@FunctionalInterface
interface PaymentProcessor {
    // Abstract method — must be implemented by each processor
    void processPayment(double amount);

    // Default method — shared refund behavior for all processors
    default void refundPayment(double amount) {
        Logger log = Logger.getLogger(PaymentProcessor.class.getName());
        log.info("Refunding payment of $" + amount);
    }
}

public class PaymentProcessorWithDefault {

    private static final Logger logger =
            Logger.getLogger(PaymentProcessorWithDefault.class.getName());

    void main() {
        // Lambda provides the abstract method; default method is inherited
        PaymentProcessor creditCardProcessor =
                amount -> logger.info(
                        "Processing credit card payment of $" + amount);

        PaymentProcessor paypalProcessor =
                amount -> logger.info(
                        "Processing PayPal payment of $" + amount);

        // Uses default refund implementation from the interface
        creditCardProcessor.refundPayment(100.0);

        // Uses lambda-provided implementation
        creditCardProcessor.processPayment(150.0);

        // PayPal processor also inherits the default refund behavior
        paypalProcessor.refundPayment(75.0);
        paypalProcessor.processPayment(200.0);

        // Output:
        // INFO: Refunding payment of $100.0
        // INFO: Processing credit card payment of $150.0
        // INFO: Refunding payment of $75.0
        // INFO: Processing PayPal payment of $200.0
    }
}