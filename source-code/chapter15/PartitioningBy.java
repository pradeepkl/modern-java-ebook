// Java 25+
// Feature shown: stream collectors partitioningBy, final in Java 8+

/**
 * Listing 15.3 — PartitioningBy.java
 * Demonstrates: Collectors.partitioningBy for binary stream partitioning
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

    record Order(String id, String status, String region, double amount) {}

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", "CONFIRMED", "UK",  149.99),
            new Order("ORD-002", "PENDING",   "UK",  299.99),
            new Order("ORD-003", "CONFIRMED", "UK",  599.99),
            new Order("ORD-004", "CANCELLED", "US",  199.99),
            new Order("ORD-005", "CONFIRMED", "EU",  749.99),
            new Order("ORD-006", "CONFIRMED", "US",   99.99),
            new Order("ORD-007", "PENDING",   "EU",  899.99),
            new Order("ORD-008", "CONFIRMED", "UK",  349.99),
            new Order("ORD-009", "CANCELLED", "US",   49.99),
            new Order("ORD-010", "CONFIRMED", "EU",  649.99)
        );

        // partitioningBy — always produces exactly two groups (true / false)
        Map<Boolean, List<Order>> byValue =
            orders.stream()
                  .collect(Collectors.partitioningBy(
                      o -> o.amount() > 500.0));

        List<Order> highValue = byValue.get(true);   // amount > 500
        List<Order> standard  = byValue.get(false);  // amount <= 500

        LOG.info("High-value orders : " + highValue.stream()
                .map(Order::id).toList());
        LOG.info("Standard orders   : " + standard.stream()
                .map(Order::id).toList());

        // partitioningBy with downstream collector — count per partition
        Map<Boolean, Long> countByValue =
            orders.stream()
                  .collect(Collectors.partitioningBy(
                      o -> o.amount() > 500.0,
                      Collectors.counting()));        // downstream: count

        LOG.info("Count high-value=" + countByValue.get(true)
               + ", standard=" + countByValue.get(false));

        // partitioningBy with downstream — sum revenue per partition
        Map<Boolean, Double> revenueByValue =
            orders.stream()
                  .collect(Collectors.partitioningBy(
                      o -> o.amount() > 500.0,
                      Collectors.summingDouble(Order::amount))); // downstream: sum

        LOG.info(String.format(
            "Revenue high-value=%.2f, standard=%.2f",
            revenueByValue.get(true),
            revenueByValue.get(false)));

        // Output:
        // High-value orders : [ORD-003, ORD-005, ORD-007, ORD-010]
        // Standard orders   : [ORD-001, ORD-002, ORD-004, ORD-006, ORD-008, ORD-009]
        // Count high-value=4, standard=6
        // Revenue high-value=2899.96, standard=949.94
    }
}