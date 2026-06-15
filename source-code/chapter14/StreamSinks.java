// Java 25+
// Feature shown: stream terminal operations (collection, scalar, string, file sinks), final in Java 16+

/**
 * Listing 14.16 — StreamSinks.java
 * Demonstrates: stream sinks — toList, toSet, joining, count, sum, max, forEach
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StreamSinks {

    private static final Logger log = Logger.getLogger(StreamSinks.class.getName());

    record Order(String orderId, String region, double amount, String status) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "UK",  250.0, "CONFIRMED"),
            new Order("ORD-002", "US",  150.0, "CONFIRMED"),
            new Order("ORD-003", "UK",   80.0, "PENDING"),
            new Order("ORD-004", "EU",  320.0, "CONFIRMED"),
            new Order("ORD-005", "US",   45.0, "CANCELLED")
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // toList() — unmodifiable List, Java 16+
        List<Order> resultList = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList();
        log.info("Confirmed orders: " + resultList.size());

        // collect(Collectors.toSet()) — deduplicated regions
        Set<String> regionSet = orders.stream()
                .map(Order::region)
                .collect(Collectors.toSet());
        log.info("Distinct regions: " + regionSet.size());

        // Collectors.joining — comma-separated order IDs
        String csv = orders.stream()
                .map(Order::orderId)
                .collect(Collectors.joining(", "));
        log.info("CSV: " + csv);

        // Collectors.joining with prefix and suffix
        String withBrackets = orders.stream()
                .map(Order::orderId)
                .collect(Collectors.joining(", ", "[", "]"));
        log.info("Bracketed: " + withBrackets);

        // count — scalar sink
        long ukCount = orders.stream()
                .filter(o -> o.region().equals("UK"))
                .count();
        log.info("UK orders: " + ukCount);

        // sum — numeric scalar sink
        double total = orders.stream()
                .mapToDouble(Order::amount)
                .sum();
        log.info("Total amount: " + total);

        // max — returns Optional<Order>
        Optional<Order> highest = orders.stream()
                .max(Comparator.comparingDouble(Order::amount));
        highest.ifPresent(o -> log.info("Highest order: " + o.orderId()
                + " amount=" + o.amount()));

        // forEach — side-effect sink: log each confirmed order
        orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .forEach(o -> log.info("Dispatching confirmed: " + o.orderId()));

        // Output:
        // Confirmed orders: 3
        // Distinct regions: 3
        // CSV: ORD-001, ORD-002, ORD-003, ORD-004, ORD-005
        // Bracketed: [ORD-001, ORD-002, ORD-003, ORD-004, ORD-005]
        // UK orders: 2
        // Total amount: 845.0
        // Highest order: ORD-004 amount=320.0
        // Dispatching confirmed: ORD-001
        // Dispatching confirmed: ORD-002
        // Dispatching confirmed: ORD-004
    }
}