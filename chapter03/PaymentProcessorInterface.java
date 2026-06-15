// Java 25+
// Feature shown: functional interfaces, final in Java 8+

/**
 * Listing 3.3 — PaymentProcessorInterface.java
 * Demonstrates: functional interfaces used as behavioral blueprints,
 * assigned via lambda expressions for different payment strategies
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

// Functional interface: a single abstract method defines the contract
@FunctionalInterface
interface PaymentProcessor {
    void processPayment(double amount);
}

public class PaymentProcessorInterface {

    private static final Logger LOG =
            Logger.getLogger(PaymentProcessorInterface.class.getName());

    void main() {

        // Each lambda is a distinct implementation of PaymentProcessor
        PaymentProcessor creditCardProcessor =
                amount -> LOG.info(
                        "Processing credit card payment of $" + amount);

        PaymentProcessor paypalProcessor =
                amount -> LOG.info(
                        "Processing PayPal payment of $" + amount);

        PaymentProcessor cryptoProcessor =
                amount -> LOG.info(
                        "Processing cryptocurrency payment of $" + amount);

        // Invoke each processor with a different payment amount
        creditCardProcessor.processPayment(100.0);  // credit card path
        paypalProcessor.processPayment(150.0);       // PayPal path
        cryptoProcessor.processPayment(200.0);       // crypto path

        // Output:
        // INFO: Processing credit card payment of $100.0
        // INFO: Processing PayPal payment of $150.0
        // INFO: Processing cryptocurrency payment of $200.0
    }
}