// Java 17+
/**
 * Listing 6.4 — InstanceofWithCondition.java
 * Demonstrates: Combining instanceof pattern matching with conditions using &&
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 17+
 */
package chapter06;

import java.util.logging.Logger;

public class InstanceofWithCondition {

    private static final Logger log =
            Logger.getLogger(InstanceofWithCondition.class.getName());

    // Sealed interface restricts permitted subtypes to the two records below
    sealed interface OrderEvent
            permits OrderPlaced, OrderCancelled {}

    record OrderPlaced(String orderId, double amount)
            implements OrderEvent {}

    record OrderCancelled(String orderId, String reason)
            implements OrderEvent {}

    public static void classify(OrderEvent event) {
        // Type check AND condition in one expression.
        // op is only bound when both are true.
        if (event instanceof OrderPlaced op && op.amount() > 10_000) {
            log.info("High-value order: " + op.orderId()
                    + " amount: " + op.amount());
        } else if (event instanceof OrderPlaced op) {
            // Reached only when amount <= 10_000
            log.info("Standard order: " + op.orderId());
        } else if (event instanceof OrderCancelled oc
                && oc.reason().equals("FRAUD")) {
            // Binding oc is scoped to this branch only
            log.warning("Fraud cancellation: " + oc.orderId());
        } else if (event instanceof OrderCancelled oc) {
            log.info("Cancellation: " + oc.orderId()
                    + " reason: " + oc.reason());
        }
    }

    public static void main(String[] args) {
        classify(new OrderPlaced("ORD-001", 25_000.00)); // high-value
        classify(new OrderPlaced("ORD-002", 99.99));     // standard
        classify(new OrderCancelled("ORD-003", "FRAUD")); // fraud
        classify(new OrderCancelled("ORD-004", "CUSTOMER_REQUEST")); // normal

        // Output:
        // INFO: High-value order: ORD-001 amount: 25000.0
        // INFO: Standard order: ORD-002
        // WARNING: Fraud cancellation: ORD-003
        // INFO: Cancellation: ORD-004 reason: CUSTOMER_REQUEST
    }
}