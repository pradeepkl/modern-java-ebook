// Java 14+
/**
 * Listing 6.5 — SwitchExpression.java
 * Demonstrates: Switch as an expression that returns a value directly
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 14+
 */
package chapter06;

import java.util.logging.Logger;

public class SwitchExpression {

    private static final Logger log = Logger.getLogger(
            SwitchExpression.class.getName());

    // Enum with all order lifecycle states
    enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED,
        DELIVERED, CANCELLED
    }

    /**
     * Returns a customer-facing message for the given order status.
     * Switch expression covers all enum constants — no default needed.
     */
    public static String customerMessage(OrderStatus status) {
        // Switch as expression — returns a value directly
        return switch (status) {
            case PENDING   -> "Your order is being reviewed";
            case CONFIRMED -> "Your order is confirmed";
            case SHIPPED   -> "Your order is on the way";
            case DELIVERED -> "Your order has been delivered";
            case CANCELLED -> "Your order has been cancelled";
        };
        // No default needed — OrderStatus is an enum
        // and all constants are covered.
        // The compiler verifies exhaustiveness.
    }

    public static void main(String[] args) {
        // Demonstrate each enum constant producing its message
        for (OrderStatus status : OrderStatus.values()) {
            String message = customerMessage(status); // switch expr result
            log.info(status.name() + " -> " + message);
        }

        // Output:
        // INFO: PENDING    -> Your order is being reviewed
        // INFO: CONFIRMED  -> Your order is confirmed
        // INFO: SHIPPED    -> Your order is on the way
        // INFO: DELIVERED  -> Your order has been delivered
        // INFO: CANCELLED  -> Your order has been cancelled
    }
}