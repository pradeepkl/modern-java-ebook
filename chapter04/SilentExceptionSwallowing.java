// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listing 4.18 — SilentExceptionSwallowing.java
 * Demonstrates: the "silent exception swallowing" smell and the corrected pattern
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class SilentExceptionSwallowing {

    private static final Logger LOG =
            Logger.getLogger(SilentExceptionSwallowing.class.getName());

    // Simulates a payment operation that may fail
    void processPayment() throws Exception {
        throw new Exception("Payment gateway timeout");
    }

    // SMELL: silent swallow — failure is invisible to callers and operators
    void badHandling() {
        try {
            processPayment();
        } catch (Exception e) {
            // nothing here — exception is silently discarded
        }
    }

    // CORRECTED: log at minimum; rethrow or wrap when the caller must know
    void goodHandling() {
        try {
            processPayment();
        } catch (Exception e) {
            // Log the full stack trace so operators can diagnose the failure
            LOG.log(Level.SEVERE, "Payment processing failed: {0}", e.getMessage());
            // Wrap and rethrow so the caller can react appropriately
            throw new IllegalStateException("Payment could not be completed", e);
        }
    }

    void main() {
        // Demonstrate the bad pattern — no output, failure is invisible
        badHandling();
        LOG.info("After badHandling: no indication that payment failed");

        // Demonstrate the corrected pattern — failure is logged and propagated
        try {
            goodHandling();
        } catch (IllegalStateException ex) {
            LOG.log(Level.WARNING,
                    "Caught from goodHandling: {0}", ex.getMessage());
        }

        // Output:
        // INFO: After badHandling: no indication that payment failed
        // SEVERE: Payment processing failed: Payment gateway timeout
        // WARNING: Caught from goodHandling: Payment could not be completed
    }
}