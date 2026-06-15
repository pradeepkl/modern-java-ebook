// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

package chapter04;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listing 4.6 — SemanticLoggingAtBoundary.java
 * Demonstrates: Semantic logging at translation boundaries using lazy message
 * construction and exception wrapping with cause preservation.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
class SemanticLoggingAtBoundary {

    private static final Logger logger =
            Logger.getLogger(SemanticLoggingAtBoundary.class.getName());

    // Domain exception representing a payment failure at the service layer
    static class PaymentFailedException extends RuntimeException {
        PaymentFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Simulated payment service that always throws SQLException
    static class PaymentService {
        void process(String paymentId) throws SQLException {
            throw new SQLException("Connection refused for payment: " + paymentId);
        }
    }

    private final PaymentService paymentService = new PaymentService();

    // Processes a payment, logging lazily and translating the exception
    void processPayment(String paymentId) {
        try {
            paymentService.process(paymentId);
        } catch (SQLException e) {
            // Lazy supplier avoids string construction if SEVERE is not active
            logger.log(
                Level.SEVERE,
                () -> "Failed to process payment: " + paymentId
            );
            // Wrap low-level SQLException in a domain exception, preserving cause
            throw new PaymentFailedException("Payment processing failed", e);
        }
    }

    void main() {
        SemanticLoggingAtBoundary boundary = new SemanticLoggingAtBoundary();
        String paymentId = "PAY-20240601-9981";

        try {
            boundary.processPayment(paymentId);
        } catch (PaymentFailedException ex) {
            // Log the domain exception with its preserved cause chain
            logger.log(Level.WARNING,
                    "Caught domain exception: " + ex.getMessage(), ex);
        }

        // Output:
        // SEVERE: Failed to process payment: PAY-20240601-9981
        // WARNING: Caught domain exception: Payment processing failed
    }
}