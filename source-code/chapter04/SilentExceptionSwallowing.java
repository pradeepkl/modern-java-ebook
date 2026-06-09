// Java 8+
/**
 * Listing 4.17 — SilentExceptionSwallowing.java
 * Demonstrates: The "silent exception swallowing" anti-pattern and its modern fix
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SilentExceptionSwallowing {

    private static final Logger logger =
            Logger.getLogger(SilentExceptionSwallowing.class.getName());

    // Simulates a payment processing operation that may fail
    static void processPayment() throws Exception {
        throw new Exception("Payment gateway timeout");
    }

    // SMELL: Silent swallowing — exception is caught and discarded
    static void badPaymentHandling() {
        try {
            processPayment();
        } catch (Exception e) {
            // nothing here — exception silently disappears
        }
    }

    // BETTER: Log the exception so failures are visible
    static void loggedPaymentHandling() {
        try {
            processPayment();
        } catch (Exception e) {
            // At minimum, log the failure with context
            logger.log(Level.WARNING, "Payment processing failed", e);
        }
    }

    // BEST: Translate and rethrow as a meaningful domain exception
    static void translatedPaymentHandling() throws PaymentException {
        try {
            processPayment();
        } catch (Exception e) {
            // Wrap in a domain-specific exception with context preserved
            throw new PaymentException("Unable to process payment", e);
        }
    }

    // Simple domain exception for demonstration
    static class PaymentException extends Exception {
        PaymentException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static void main(String[] args) {
        // Demonstrate the silent swallow — no output, failure hidden
        badPaymentHandling();
        logger.log(Level.INFO, "After bad handling: no indication of failure");

        // Demonstrate logging approach
        loggedPaymentHandling();
        logger.log(Level.INFO, "After logged handling: failure was recorded");

        // Demonstrate translation approach
        try {
            translatedPaymentHandling();
        } catch (PaymentException e) {
            logger.log(Level.WARNING, "Caught domain exception: {0}", e.getMessage());
            logger.log(Level.WARNING, "Caused by: {0}", e.getCause().getMessage());
        }

        // Output:
        // INFO: After bad handling: no indication of failure
        // WARNING: Payment processing failed (logged to console)
        // INFO: After logged handling: failure was recorded
        // WARNING: Caught domain exception: Unable to process payment
        // WARNING: Caused by: Payment gateway timeout
    }
}