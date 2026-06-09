// Java 8+
/**
 * Listing 4.6 — SemanticLoggingAtBoundary.java
 * Demonstrates: Semantic logging at layer boundaries with lazy message construction
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SemanticLoggingAtBoundary {

    private static final Logger logger =
            Logger.getLogger(SemanticLoggingAtBoundary.class.getName());

    // Domain-level exception meaningful to the service layer
    static class PaymentFailedException extends RuntimeException {
        public PaymentFailedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Simulated payment service that throws a low-level SQLException
    static class PaymentService {
        public void process(String paymentId) throws SQLException {
            throw new SQLException("Connection timeout for payment: " + paymentId);
        }
    }

    private final PaymentService paymentService = new PaymentService();

    // Translation boundary: log with context, then wrap and rethrow
    public void processPayment(String paymentId) {
        try {
            paymentService.process(paymentId); // may throw SQLException
        } catch (SQLException e) {
            // Lazy supplier avoids string construction if SEVERE is not active
            logger.log(
                Level.SEVERE,
                () -> "Failed to process payment: " + paymentId
            );
            // Wrap low-level exception in domain exception, preserving cause
            throw new PaymentFailedException(
                    "Payment processing failed", e);
        }
    }

    public static void main(String[] args) {
        SemanticLoggingAtBoundary boundary = new SemanticLoggingAtBoundary();
        String paymentId = "PAY-20240101-9988";

        try {
            boundary.processPayment(paymentId); // triggers exception path
        } catch (PaymentFailedException e) {
            // Presentation layer sees only the domain exception
            logger.log(Level.WARNING, "Caught domain exception: {0}", e.getMessage());
            logger.log(Level.WARNING, "Root cause: {0}", e.getCause().getMessage());
        }

        // Output:
        // SEVERE: Failed to process payment: PAY-20240101-9988
        // WARNING: Caught domain exception: Payment processing failed
        // WARNING: Root cause: Connection timeout for payment: PAY-20240101-9988
    }
}