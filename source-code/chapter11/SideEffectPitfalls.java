// Java 8+
/**
 * Listing 11.9 — SideEffectPitfalls.java
 * Demonstrates: Separating side effects from transformations in forEach
 * Chapter 11: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter11;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SideEffectPitfalls {

    private static final Logger LOG = Logger.getLogger(SideEffectPitfalls.class.getName());

    record Order(String orderId, double amount, String status) {}

    public static void main(String[] args) {

        List<Order> orders = List.of(
            new Order("A1", 250.0, "ACTIVE"),
            new Order("B2",  45.0, "ACTIVE"),
            new Order("C3", 180.0, "ACTIVE"),
            new Order("D4",  30.0, "ACTIVE")
        );

        // ❌ Pitfall — forEach doing two things at once
        List<String> auditLogBad = new ArrayList<>();
        List<Order> highValueBad = new ArrayList<>();
        orders.forEach(o -> {
            auditLogBad.add("Processed: " + o.orderId()); // side effect
            if (o.amount() > 100.0) {
                highValueBad.add(o); // ❌ mutation of external list mixed in
            }
        });
        LOG.info("❌ Bad audit log size: " + auditLogBad.size());
        LOG.info("❌ Bad high-value count: " + highValueBad.size());

        // ✅ Separate the side effect from the transformation
        List<String> auditLog = new ArrayList<>();
        orders.forEach(o ->
            auditLog.add("Processed: " + o.orderId())); // side effect only

        List<Order> highValue = orders.stream()
            .filter(o -> o.amount() > 100.0)            // transformation only
            .toList();

        LOG.info("✅ Audit log: " + auditLog);
        LOG.info("✅ High-value orders: " + highValue.stream().map(Order::orderId).toList());

        // ❌ Stateful lambda — shared mutable counter
        int[] counter = {0};
        orders.forEach(o -> counter[0]++); // ❌ mutating array slot as workaround
        LOG.info("❌ Stateful counter result: " + counter[0]);

        // ✅ Use the API directly — no lambda, no mutation
        int count = orders.size();
        LOG.info("✅ Clean count: " + count);

        // Output:
        // ❌ Bad audit log size: 4
        // ❌ Bad high-value count: 2
        // ✅ Audit log: [Processed: A1, Processed: B2, Processed: C3, Processed: D4]
        // ✅ High-value orders: [A1, C3]
        // ❌ Stateful counter result: 4
        // ✅ Clean count: 4
    }
}