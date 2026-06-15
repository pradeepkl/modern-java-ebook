// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 3.8 — TemplateMethodPattern.java
 * Demonstrates: Template Method pattern using abstract classes to define a
 * fixed algorithm skeleton with variation points for subclasses.
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

// Abstract base class defines the fixed algorithm skeleton
abstract class PaymentProcessor {

    protected static final Logger log =
            Logger.getLogger(PaymentProcessor.class.getName());

    // Template method defines the fixed algorithm — cannot be overridden
    public final void process(double amount) {
        validate(amount);   // shared step
        debit(amount);      // variation point
        notifyUser(amount); // shared step
    }

    // Variation point — subclasses must provide their own debit logic
    protected abstract void debit(double amount);

    protected void validate(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
    }

    protected void notifyUser(double amount) {
        log.info("Payment of " + amount + " processed");
    }
}

// Concrete subclass — credit card variant
class CreditCardPayment extends PaymentProcessor {

    @Override
    protected void debit(double amount) {
        log.info("Debiting credit card for " + amount);
    }
}

// Concrete subclass — UPI variant
class UpiPayment extends PaymentProcessor {

    @Override
    protected void debit(double amount) {
        log.info("Processing UPI payment for " + amount);
    }
}

public class TemplateMethodPattern {

    void main() {
        PaymentProcessor card = new CreditCardPayment();
        card.process(150.0); // runs validate -> debit (card) -> notifyUser

        PaymentProcessor upi = new UpiPayment();
        upi.process(75.0);   // runs validate -> debit (upi) -> notifyUser

        // Output:
        // INFO: Debiting credit card for 150.0
        // INFO: Payment of 150.0 processed
        // INFO: Processing UPI payment for 75.0
        // INFO: Payment of 75.0 processed
    }
}