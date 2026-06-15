// Java 25+
// Feature shown: stream collectors (toList, toSet, joining, counting), final in Java 16+
/**
 * Listing 15.1 — CollectorBasics.java
 * Demonstrates: Core Collectors vocabulary: toList, toSet, toUnmodifiableList,
 *               joining, and counting applied to a stream of Order records.
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CollectorBasics {

    private static final Logger LOG = Logger.getLogger(CollectorBasics.class.getName());

    // Domain record used throughout Chapter 15
    record Order(String orderId, String customerId, double amount,
                 String status, String region) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "C1", 250.0, "CONFIRMED", "NORTH"),
            new Order("ORD-002", "C2",  80.0, "PENDING",   "SOUTH"),
            new Order("ORD-003", "C3", 175.0, "CONFIRMED", "NORTH"),
            new Order("ORD-004", "C4",  50.0, "CANCELLED", "EAST"),
            new Order("ORD-005", "C5", 320.0, "CONFIRMED", "SOUTH")
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // toList() — built into stream API, Java 16+
        List<Order> confirmed = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList();                                      // immutable list
        LOG.info("Confirmed orders: " + confirmed.size());

        // Collectors.toSet() — deduplicated, no order guarantee
        Set<String> regions = orders.stream()
                .map(Order::region)
                .collect(Collectors.toSet());
        LOG.info("Distinct regions: " + regions);

        // Collectors.toUnmodifiableList() — Java 10+
        List<Order> highValue = orders.stream()
                .filter(o -> o.amount() > 100.0)
                .collect(Collectors.toUnmodifiableList());
        LOG.info("High-value orders: " + highValue.size());

        // Collectors.joining — delimiter only
        String orderIds = orders.stream()
                .map(Order::orderId)
                .collect(Collectors.joining(", "));
        LOG.info("Order IDs: " + orderIds);

        // Collectors.joining — delimiter, prefix, suffix
        String formatted = orders.stream()
                .map(Order::orderId)
                .collect(Collectors.joining(", ", "[", "]"));
        LOG.info("Formatted: " + formatted);

        // Collectors.counting() — count matching elements
        long confirmedCount = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.counting());
        LOG.info("Confirmed count: " + confirmedCount);

        // Output:
        // Confirmed orders: 3
        // Distinct regions: [NORTH, SOUTH, EAST]
        // High-value orders: 3
        // Order IDs: ORD-001, ORD-002, ORD-003, ORD-004, ORD-005
        // Formatted: [ORD-001, ORD-002, ORD-003, ORD-004, ORD-005]
        // Confirmed count: 3
    }
}