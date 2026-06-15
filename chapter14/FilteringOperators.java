// Java 25+
// Feature shown: stream filtering operators (filter, distinct, limit, skip, takeWhile, dropWhile), final in Java 9+

/**
 * Listing 14.5 — FilteringOperators.java
 * Demonstrates: filter, distinct, limit, skip, takeWhile, dropWhile on streams
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 9+ for takeWhile/dropWhile; compiled with --enable-preview --release 21
 * for the void main() instance main method
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;

public class FilteringOperators {

    record Order(String orderId, String status, String region, double amount) {}

    private static final Logger LOG = Logger.getLogger(FilteringOperators.class.getName());

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "CONFIRMED", "NORTH", 1200.0),
            new Order("ORD-002", "CONFIRMED", "SOUTH", 950.0),
            new Order("ORD-003", "PENDING",   "NORTH", 800.0),
            new Order("ORD-004", "CONFIRMED", "EAST",  600.0),
            new Order("ORD-005", "CANCELLED", "SOUTH", 450.0),
            new Order("ORD-006", "CONFIRMED", "EAST",  300.0),
            new Order("ORD-007", "PENDING",   "WEST",  200.0)
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // filter — keep elements matching a predicate (SQL: WHERE status = 'CONFIRMED')
        List<Order> confirmed = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList();
        LOG.info("Confirmed orders: " + confirmed.size());

        // distinct — remove duplicates based on equals() (SQL: SELECT DISTINCT region)
        List<String> uniqueRegions = orders.stream()
                .map(Order::region)
                .distinct()
                .toList();
        LOG.info("Unique regions: " + uniqueRegions);

        // limit — keep at most n elements (SQL: LIMIT 3)
        List<Order> topThree = orders.stream()
                .limit(3)
                .toList();
        LOG.info("Top 3 orders: " + topThree.size());

        // skip — discard the first n elements (SQL: OFFSET 2)
        List<Order> afterFirst2 = orders.stream()
                .skip(2)
                .toList();
        LOG.info("After skipping 2: " + afterFirst2.size());

        // limit + skip together = pagination (SQL: LIMIT 2 OFFSET 2)
        List<Order> page2 = orders.stream()
                .skip(2)   // skip first 2
                .limit(2)  // take next 2
                .toList();
        LOG.info("Page 2 (skip 2, limit 2): " + page2.size());

        // takeWhile — keep elements while predicate holds; stops at first failure
        List<Order> whileHighValue = orders.stream()
                .takeWhile(o -> o.amount() > 500.0)
                .toList();
        LOG.info("takeWhile amount > 500: " + whileHighValue.size());

        // dropWhile — discard elements while predicate holds, then keep the rest
        List<Order> afterHighValue = orders.stream()
                .dropWhile(o -> o.amount() > 500.0)
                .toList();
        LOG.info("dropWhile amount > 500: " + afterHighValue.size());

        // Output:
        // Confirmed orders: 4
        // Unique regions: [NORTH, SOUTH, EAST, WEST]
        // Top 3 orders: 3
        // After skipping 2: 5
        // Page 2 (skip 2, limit 2): 2
        // takeWhile amount > 500: 4
        // dropWhile amount > 500: 3
    }
}