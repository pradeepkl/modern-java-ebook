// Java 25+
// Feature shown: stream composition operators (concat, of, ofNullable, iterate, generate), final in Java 9+

/**
 * Listing 15.7 — StreamComposition.java
 * Demonstrates: Stream.concat, Stream.of, Stream.ofNullable, Stream.iterate, Stream.generate
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamComposition {

    private static final Logger LOG = Logger.getLogger(StreamComposition.class.getName());

    record Order(String orderId, String region, double amount) {}

    private static List<Order> getUKOrders() {
        return List.of(new Order("ORD-1002", "UK", 300.0), new Order("ORD-1005", "UK", 600.0));
    }

    private static List<Order> getUSOrders() {
        return List.of(new Order("ORD-1001", "US", 450.0), new Order("ORD-1004", "US", 120.0));
    }

    private static List<Order> getEUOrders() {
        return List.of(new Order("ORD-1003", "EU", 800.0));
    }

    private static String getRegion() { return "APAC"; }

    private static String getRegionOverride(String orderId) {
        return orderId.equals("ORD-1001") ? "US-WEST" : null; // null for most
    }

    void main() {
        // Stream.concat — combine two streams sequentially (UNION ALL)
        List<Order> ukOrders = getUKOrders();
        List<Order> usOrders = getUSOrders();
        List<Order> allOrders = Stream.concat(ukOrders.stream(), usOrders.stream())
                .sorted(Comparator.comparing(Order::orderId))
                .toList();
        LOG.info("Combined and sorted: " + allOrders.stream().map(Order::orderId).toList());

        // Stream.concat nested — more than two sources
        List<Order> allRegions = Stream.concat(
                Stream.concat(ukOrders.stream(), usOrders.stream()),
                getEUOrders().stream())
                .toList();
        LOG.info("All regions count: " + allRegions.size());

        // Stream.of — fixed set of known values
        Set<String> statusSet = Stream.of("CONFIRMED", "PENDING", "SHIPPED", "DELIVERED")
                .collect(Collectors.toSet());
        LOG.info("Valid statuses: " + statusSet.size() + " entries");

        // Stream.ofNullable — null-safe single element
        String maybeRegion = getRegion(); // non-null here
        long regionCount = Stream.ofNullable(maybeRegion).count();
        LOG.info("Region stream size (non-null): " + regionCount);

        // flatMap with ofNullable — filter nulls from a stream
        List<String> overrides = allRegions.stream()
                .map(o -> getRegionOverride(o.orderId()))
                .flatMap(Stream::ofNullable)
                .toList();
        LOG.info("Non-null region overrides: " + overrides);

        // Stream.iterate — three-arg form (Java 9+) with built-in termination
        List<Integer> orderNumbers = Stream.iterate(1000, n -> n <= 1010, n -> n + 1)
                .toList();
        LOG.info("Order numbers: " + orderNumbers);

        // Stream.generate — values from a Supplier, paired with limit()
        List<String> requestIds = Stream.generate(() -> "REQ-" + UUID.randomUUID())
                .limit(3)
                .toList();
        LOG.info("Generated request IDs count: " + requestIds.size());

        // Output:
        // Combined and sorted: [ORD-1001, ORD-1002, ORD-1004, ORD-1005]
        // All regions count: 5
        // Valid statuses: 4 entries
        // Region stream size (non-null): 1
        // Non-null region overrides: [US-WEST]
        // Order numbers: [1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010]
        // Generated request IDs count: 3
    }
}