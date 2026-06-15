// Java 25+
// Feature shown: pattern matching for instanceof with conditions, final in Java 16+
/**
 * Listing 6.4 — InstanceofWithCondition.java
 * Demonstrates: combining instanceof pattern matching with a boolean condition
 *               using &&, so the binding variable is only in scope when both
 *               the type check and the condition are true.
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 17+ for sealed interfaces; instanceof pattern matching final
 *           in Java 16+. Instance main method final in Java 25+ (JEP 512).
 */
package chapter06;

import java.util.logging.Logger;

public class InstanceofWithCondition {

    private static final Logger log =
            Logger.getLogger(InstanceofWithCondition.class.getName());

    // Sealed interface — compiler knows all permitted subtypes
    sealed interface OrderEvent
            permits OrderPlaced, OrderCancelled {}

    record OrderPlaced(String orderId, double amount)
            implements OrderEvent {}

    record OrderCancelled(String orderId, String reason)
            implements OrderEvent {}

    // Pattern variable op is bound only when type AND condition are both true
    public static void classify(OrderEvent event) {
        if (event instanceof OrderPlaced op
                && op.amount() > 10_000) {               // guard condition
            log.info("High-value order: "
                    + op.orderId() + " amount: " + op.amount());
        } else if (event instanceof OrderPlaced op) {    // standard amount
            log.info("Standard order: " + op.orderId());
        } else if (event instanceof OrderCancelled oc
                && oc.reason().equals("FRAUD")) {        // fraud guard
            log.warning("Fraud cancellation: " + oc.orderId());
        } else if (event instanceof OrderCancelled oc) { // other reason
            log.info("Cancellation: "
                    + oc.orderId() + " reason: " + oc.reason());
        }
    }

    void main() {
        classify(new OrderPlaced("ORD-001", 25_000.00)); // high-value
        classify(new OrderPlaced("ORD-002", 500.00));    // standard
        classify(new OrderCancelled("ORD-003", "FRAUD")); // fraud
        classify(new OrderCancelled("ORD-004", "CUSTOMER_REQUEST")); // normal

        // Output:
        // INFO: High-value order: ORD-001 amount: 25000.0
        // INFO: Standard order: ORD-002
        // WARNING: Fraud cancellation: ORD-003
        // INFO: Cancellation: ORD-004 reason: CUSTOMER_REQUEST
    }
}