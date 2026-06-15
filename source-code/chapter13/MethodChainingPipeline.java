// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.10 — MethodChainingPipeline.java
 * Demonstrates: A complete transformation pipeline using removeIf, replaceAll,
 *               sort with chained Comparators, and forEach on a mutable list
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

    record AuditLog() {
        void record(String orderId, String event) {
            log.info("AUDIT: " + orderId + " -> " + event);
        }
    }

    // Apply a VIP discount to an order if rate > 0
    static Order applyVipDiscount(Order o, double rate) {
        return new Order(o.orderId(), o.customerId(), o.amount() * (1.0 - rate), o.status());
    }

    void main() {
        double rate = 0.10; // 10 percent VIP discount

        List<Order> orders = new ArrayList<>(List.of(
            new Order("ORD-1", "CUST-B", 250.00, "CANCELLED"),
            new Order("ORD-2", "CUST-A", 180.00, "PENDING"),
            new Order("ORD-3", "CUST-B", 95.00,  "PENDING"),
            new Order("ORD-4", "CUST-A", 320.00, "CANCELLED"),
            new Order("ORD-5", "CUST-C", 410.00, "PENDING")
        ));

        AuditLog auditLog = new AuditLog();

        // Step 1: remove all cancelled orders
        orders.removeIf(o -> o.status().equals("CANCELLED"));

        // Step 2: apply VIP discount to every remaining order
        orders.replaceAll(o -> applyVipDiscount(o, rate));

        // Step 3: sort by customerId, then by amount descending
        orders.sort(
            Comparator.comparing(Order::customerId)
                      .thenComparingDouble(Order::amount)
                      .reversed());

        // Step 4: record each processed order in the audit log
        orders.forEach(o ->
            auditLog.record(o.orderId(), "PROCESSED"));

        // Output:
        // AUDIT: ORD-5 -> PROCESSED   (CUST-C, 369.00)
        // AUDIT: ORD-3 -> PROCESSED   (CUST-B, 85.50)
        // AUDIT: ORD-2 -> PROCESSED   (CUST-A, 162.00)
    }
}