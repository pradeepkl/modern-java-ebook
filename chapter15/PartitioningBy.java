// Java 25+
// Feature shown: stream collectors partitioningBy, final in Java 8+

/**
 * Listing 15.3 — PartitioningBy.java
 * Demonstrates: Collectors.partitioningBy with simple predicate and downstream collectors
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PartitioningBy {

    private static final Logger LOG = Logger.getLogger(PartitioningBy.class.getName());

    record Order(String id, double amount, String status, String region) {}

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", 149.99, "CONFIRMED", "UK"),
            new Order("ORD-002", 299.99, "PENDING",   "US"),
            new Order("ORD-003", 599.99, "CONFIRMED", "UK"),
            new Order("ORD-004", 49.99,  "CANCELLED", "EU"),
            new Order("ORD-005", 799.99, "CONFIRMED", "US"),
            new Order("ORD-006", 89.99,  "CONFIRMED", "EU"),
            new Order("ORD-007", 199.99, "PENDING",   "UK"),
            new Order("ORD-008", 999.99, "CONFIRMED", "US"),
            new Order("ORD-009", 39.99,  "CANCELLED", "EU"),
            new Order("ORD-010", 124.99, "CONFIRMED", "UK")
        );

        // partitioningBy — always produces exactly two groups (true / false)
        Map<Boolean, List<Order>> byValue =
                orders.stream()
                      .collect(Collectors.partitioningBy(
                              o -> o.amount() > 500.0));

        List<Order> highValue = byValue.get(true);   // amount > 500
        List<Order> standard  = byValue.get(false);  // amount <= 500

        LOG.info("High-value orders : " + highValue.stream().map(Order::id).toList());
        LOG.info("Standard orders   : " + standard.stream().map(Order::id).toList());

        // partitioningBy with downstream collector — count per partition
        Map<Boolean, Long> countByValue =
                orders.stream()
                      .collect(Collectors.partitioningBy(
                              o -> o.amount() > 500.0,
                              Collectors.counting()));

        LOG.info("Count high-value  : " + countByValue.get(true));   // 3
        LOG.info("Count standard    : " + countByValue.get(false));  // 7

        // partitioningBy with downstream — sum revenue per partition
        Map<Boolean, Double> revenueByValue =
                orders.stream()
                      .collect(Collectors.partitioningBy(
                              o -> o.amount() > 500.0,
                              Collectors.summingDouble(Order::amount)));

        LOG.info("Revenue high-value: " + revenueByValue.get(true));
        LOG.info("Revenue standard  : " + revenueByValue.get(false));

        // Output:
        // High-value orders : [ORD-003, ORD-005, ORD-008]
        // Standard orders   : [ORD-001, ORD-002, ORD-004, ORD-006, ORD-007, ORD-009, ORD-010]
        // Count high-value  : 3
        // Count standard    : 7
        // Revenue high-value: 2399.97
        // Revenue standard  : 954.93
    }
}