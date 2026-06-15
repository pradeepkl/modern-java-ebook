// Java 25+
// Feature shown: switch expressions, final in Java 14+

/**
 * Listing 6.5 — SwitchExpression.java
 * Demonstrates: switch used as an expression that returns a value directly,
 * with exhaustiveness checking over an enum type.
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 14+ for switch expressions; compiled with --enable-preview
 * --release 21 for the void main() instance main method (final in Java 25+,
 * JEP 512).
 */
package chapter06;

import java.util.logging.Logger;

public class SwitchExpression {

    private static final Logger log =
            Logger.getLogger(SwitchExpression.class.getName());

    enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED,
        DELIVERED, CANCELLED
    }

    // Switch as expression — returns a value directly
    public static String customerMessage(OrderStatus status) {
        return switch (status) {
            case PENDING   -> "Your order is being reviewed";
            case CONFIRMED -> "Your order is confirmed";
            case SHIPPED   -> "Your order is on the way";
            case DELIVERED -> "Your order has been delivered";
            case CANCELLED -> "Your order has been cancelled";
            // No default needed — all enum constants are covered.
            // The compiler verifies exhaustiveness.
        };
    }

    void main() {
        // Iterate over every OrderStatus constant and log the message
        for (OrderStatus status : OrderStatus.values()) {
            String message = customerMessage(status);
            log.info(status + " -> " + message);
        }
        // Output:
        // INFO: PENDING    -> Your order is being reviewed
        // INFO: CONFIRMED  -> Your order is confirmed
        // INFO: SHIPPED    -> Your order is on the way
        // INFO: DELIVERED  -> Your order has been delivered
        // INFO: CANCELLED  -> Your order has been cancelled
    }
}