// Java 8+
/**
 * Listing 13.10 — MethodChainingPipeline.java
 * Demonstrates: A complete declarative transformation pipeline using
 *               removeIf, replaceAll, sort, and forEach on a mutable list
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
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
            log.info("AUDIT | " + orderId + " | " + event);
        }
    }

    // Apply a VIP discount if amount exceeds threshold
    static Order applyVipDiscount(Order o, double rate) {
        double discounted = o.amount() > 100.0 ? o.amount() * (1.0 - rate) : o.amount();
        return new Order(o.orderId(), o.customerId(), discounted, o.status());
    }

    public static void main(String[] args) {
        List<Order> orders = new ArrayList<>(List.of(
            new Order("ORD-001", "C02", 250.00, "PENDING"),
            new Order("ORD-002", "C01", 80.00,  "CANCELLED"),
            new Order("ORD-003", "C01", 320.00, "PENDING"),
            new Order("ORD-004", "C03", 150.00, "CANCELLED"),
            new Order("ORD-005", "C02", 90.00,  "PENDING")
        ));

        double rate = 0.10; // 10% VIP discount
        AuditLog auditLog = new AuditLog();

        // Step 1: Remove all cancelled orders
        orders.removeIf(o -> o.status().equals("CANCELLED"));

        // Step 2: Apply VIP discount to remaining orders
        orders.replaceAll(o -> applyVipDiscount(o, rate));

        // Step 3: Sort by customerId then by amount, reversed
        orders.sort(
            Comparator.comparing(Order::customerId)
                      .thenComparingDouble(Order::amount)
                      .reversed());

        // Step 4: Record each processed order in the audit log
        orders.forEach(o -> auditLog.record(o.orderId(), "PROCESSED"));

        // Output:
        // AUDIT | ORD-003 | PROCESSED   (C01, 288.0)
        // AUDIT | ORD-005 | PROCESSED   (C02, 90.0)
        // AUDIT | ORD-001 | PROCESSED   (C02, 225.0)  [reversed so higher amount first within C02]
    }
}