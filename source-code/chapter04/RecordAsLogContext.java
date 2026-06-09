// Java 16+
/**
 * Listing 4.13 — RecordAsLogContext.java
 * Demonstrates: Using a record as structured log context for domain operations
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 16+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordAsLogContext {

    // Record carries structured log context — no boilerplate needed
    public record LogContext(
            String orderId,
            String userId,
            String operation) {
    }

    private static final Logger logger =
            Logger.getLogger(RecordAsLogContext.class.getName());

    /**
     * Simulates payment processing with structured log context.
     *
     * @param orderId   the order identifier
     * @param userId    the user identifier
     */
    static void processPayment(String orderId, String userId) {
        // Build a typed, structured context record — no manual toString needed
        var context = new LogContext(
                orderId,
                userId,
                "payment-processing"
        );

        // Lambda defers string construction; record's toString is auto-generated
        logger.log(Level.INFO, () -> "Processing: " + context);

        // Individual fields are accessible as typed accessors
        logger.log(Level.FINE, () -> "Order: " + context.orderId()
                + ", User: " + context.userId()
                + ", Op: " + context.operation());
    }

    public static void main(String[] args) {
        processPayment("ORD-123", "USR-456");

        // Demonstrate record equality and toString directly
        var ctx1 = new LogContext("ORD-123", "USR-456", "payment-processing");
        var ctx2 = new LogContext("ORD-123", "USR-456", "payment-processing");

        logger.log(Level.INFO, "{0}", ctx1);           // auto-generated toString
        logger.log(Level.INFO, "{0}", ctx1.equals(ctx2)); // auto-generated equals

        // Output:
        // INFO: Processing: LogContext[orderId=ORD-123, userId=USR-456, operation=payment-processing]
        // INFO: LogContext[orderId=ORD-123, userId=USR-456, operation=payment-processing]
        // INFO: true
    }
}