// Java 25+
// Feature shown: functional interfaces, final in Java 8+

/**
 * Listing 3.3 — PaymentProcessorInterface.java
 * Demonstrates: functional interfaces used as behavioral blueprints,
 * implemented via lambda expressions for diverse payment strategies
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

@FunctionalInterface
interface PaymentProcessor {
    // Single abstract method — qualifies this as a functional interface
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

        // Each processor handles the same contract with different behavior
        creditCardProcessor.processPayment(100.0);
        paypalProcessor.processPayment(150.0);
        cryptoProcessor.processPayment(200.0);

        // Output:
        // INFO: Processing credit card payment of $100.0
        // INFO: Processing PayPal payment of $150.0
        // INFO: Processing cryptocurrency payment of $200.0
    }
}