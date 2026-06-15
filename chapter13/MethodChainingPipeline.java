// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.10 — MethodChainingPipeline.java
 * Demonstrates: A complete declarative transformation pipeline using
 * removeIf, replaceAll, sort, and forEach on a mutable list of orders.
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class MethodChainingPipeline {

    private static final Logger log = Logger.getLogger(MethodChainingPipeline.class.getName());

    record Order(String orderId, String customerId, double amount, String status) {}

    // Apply a VIP discount to an order if the rate is positive
    static Order applyVipDiscount(Order o, double rate) {
        return new Order(o.orderId(), o.customerId(), o.amount() * (1.0 - rate), o.status());
    }

    // Simple audit log that records order events
    static class AuditLog {
        void record(String orderId, String event) {
            log.info("AUDIT | " + orderId + " | " + event);
        }
    }

    void main() {
        List<Order> orders = new ArrayList<>(List.of(
            new Order("O1", "C2", 500.00, "CANCELLED"),
            new Order("O2", "C1", 200.00, "PENDING"),
            new Order("O3", "C2", 150.00, "PENDING"),
            new Order("O4", "C1", 800.00, "PENDING")
        ));

        AuditLog auditLog = new AuditLog();
        double rate = 0.10; // 10% VIP discount

        // Step 1: Remove all cancelled orders
        orders.removeIf(o -> o.status().equals("CANCELLED"));

        // Step 2: Apply VIP discount to every remaining order
        orders.replaceAll(o -> applyVipDiscount(o, rate));

        // Step 3: Sort by customerId, then by amount descending
        orders.sort(
            Comparator.comparing(Order::customerId)
                      .thenComparingDouble(Order::amount)
                      .reversed());

        // Step 4: Record each processed order in the audit log
        orders.forEach(o -> auditLog.record(o.orderId(), "PROCESSED"));

        // Output:
        // AUDIT | O4 | PROCESSED
        // AUDIT | O2 | PROCESSED
        // AUDIT | O3 | PROCESSED
    }
}