// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.12 — SideEffectPitfalls.java
 * Demonstrates: Pitfalls of mixing side effects with transformation in forEach,
 *               and the correct approach of separating concerns using stream filter
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SideEffectPitfalls {

    private static final Logger LOG = Logger.getLogger(SideEffectPitfalls.class.getName());

    record Order(String orderId, double amount) {}

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", 250.00),
            new Order("ORD-002",  45.00),
            new Order("ORD-003", 175.50),
            new Order("ORD-004",  80.00),
            new Order("ORD-005", 320.99)
        );

        List<String> auditLog = new ArrayList<>();
        List<Order> highValue = new ArrayList<>();

        // NOT IDEAL: forEach doing two things at once
        // Side effect (audit) mixed with transformation (filter)
        orders.forEach(o -> {
            auditLog.add("Processed: " + o.orderId()); // side effect
            if (o.amount() > 100.0) {
                highValue.add(o); // mutation of external list
            }
        });

        LOG.info("Pitfall audit log size: " + auditLog.size());
        LOG.info("Pitfall high-value count: " + highValue.size());

        // Correct approach: separate the side effect from the transformation
        List<String> cleanAuditLog = new ArrayList<>();
        orders.forEach(o ->
                cleanAuditLog.add("Processed: " + o.orderId())); // side effect only

        List<Order> cleanHighValue = orders.stream()
                .filter(o -> o.amount() > 100.0)   // transformation only
                .toList();

        LOG.info("Clean audit log size: " + cleanAuditLog.size());
        LOG.info("Clean high-value count: " + cleanHighValue.size());

        // NOT IDEAL: stateful lambda — shared mutable counter
        int[] counter = {0};
        orders.forEach(o -> counter[0]++); // mutates array element from lambda
        LOG.info("Stateful counter result: " + counter[0]);

        // Correct approach: use the API directly — no lambda, no mutation
        int count = orders.size();
        LOG.info("Direct count result: " + count);

        // Output:
        // Pitfall audit log size: 5
        // Pitfall high-value count: 3
        // Clean audit log size: 5
        // Clean high-value count: 3
        // Stateful counter result: 5
        // Direct count result: 5
    }
}