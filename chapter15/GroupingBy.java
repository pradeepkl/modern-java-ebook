// Java 25+
// Feature shown: Collectors.groupingBy with downstream collectors, final in Java 21+

/**
 * Listing 15.2 — GroupingBy.java
 * Demonstrates: groupingBy with single-level, computed-key, downstream,
 *               and nested grouping collectors
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GroupingBy {

    private static final Logger LOG = Logger.getLogger(GroupingBy.class.getName());

    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "C1", 149.99, "CONFIRMED", "UK"),
            new Order("ORD-002", "C2",  99.99, "PENDING",   "UK"),
            new Order("ORD-003", "C3", 299.98, "CONFIRMED", "UK"),
            new Order("ORD-004", "C4", 199.99, "CANCELLED", "EU"),
            new Order("ORD-005", "C5", 299.99, "CONFIRMED", "US")
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // Single-level grouping by status
        Map<String, List<Order>> byStatus = orders.stream()
                .collect(Collectors.groupingBy(Order::status));
        LOG.info("byStatus keys: " + byStatus.keySet());

        // Group by region
        Map<String, List<Order>> byRegion = orders.stream()
                .collect(Collectors.groupingBy(Order::region));
        LOG.info("byRegion keys: " + byRegion.keySet());

        // Computed key — amount bucket
        Map<String, List<Order>> byAmountBucket = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.amount() > 500.0 ? "HIGH" : "STANDARD"));
        LOG.info("byAmountBucket keys: " + byAmountBucket.keySet());

        // Downstream: counting per status
        Map<String, Long> countByStatus = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::status,
                        Collectors.counting()));
        LOG.info("countByStatus: " + countByStatus);

        // Downstream: summing revenue per region
        Map<String, Double> revenueByRegion = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::region,
                        Collectors.summingDouble(Order::amount)));
        LOG.info("revenueByRegion: " + revenueByRegion);

        // Downstream: averaging amount per region
        Map<String, Double> avgByRegion = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::region,
                        Collectors.averagingDouble(Order::amount)));
        LOG.info("avgByRegion: " + avgByRegion);

        // Nested groupingBy: region -> status -> orders
        Map<String, Map<String, List<Order>>> nested = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::region,
                        Collectors.groupingBy(Order::status)));
        LOG.info("nested UK keys: " + nested.get("UK").keySet());

        // Output:
        // byStatus keys: [CONFIRMED, PENDING, CANCELLED]
        // byRegion keys: [UK, EU, US]
        // byAmountBucket keys: [STANDARD]
        // countByStatus: {CONFIRMED=3, PENDING=1, CANCELLED=1}
        // revenueByRegion: {UK=549.96, EU=199.99, US=299.99}
        // avgByRegion: {UK=183.32, EU=199.99, US=299.99}
        // nested UK keys: [CONFIRMED, PENDING]
    }
}