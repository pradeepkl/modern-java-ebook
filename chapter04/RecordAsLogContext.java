// Java 25+
// Feature shown: records as structured log context, final in Java 16+

/**
 * Listing 4.13 — RecordAsLogContext.java
 * Demonstrates: Using a record as a structured log context carrier,
 * providing typed fields and automatic toString for log messages.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 16+ for records; Java 25+ for compact source files
 * and instance main methods (JEP 512), compiled with --enable-preview
 * --release 21 for the void main() instance main method.
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

// Record carries structured log context — no boilerplate needed
record LogContext(
        String orderId,
        String userId,
        String operation) {
}

public class RecordAsLogContext {

    private static final Logger logger =
            Logger.getLogger(RecordAsLogContext.class.getName());

    void main() {
        // Simulate values that would come from a real request context
        var orderId = "ORD-123";
        var userId  = "USR-456";

        // Construct the log context record with typed fields
        var context = new LogContext(
                orderId,
                userId,
                "payment-processing"
        );

        // Record's auto-generated toString produces structured output
        logger.log(Level.INFO, () -> "Processing: " + context);

        // Access individual fields for targeted log messages
        logger.log(Level.INFO,
                () -> "Order " + context.orderId()
                    + " initiated by user " + context.userId());

        // Demonstrate equals and hashCode come for free with records
        var duplicate = new LogContext("ORD-123", "USR-456", "payment-processing");
        logger.log(Level.INFO,
                () -> "Contexts equal: " + context.equals(duplicate));

        // Output:
        // Processing: LogContext[orderId=ORD-123, userId=USR-456,
        //             operation=payment-processing]
        // Order ORD-123 initiated by user USR-456
        // Contexts equal: true
    }
}