// Java 25+
// Feature shown: stream filtering operators (filter, distinct, limit, skip, takeWhile, dropWhile), final in Java 9+

/**
 * Listing 14.5 — FilteringOperators.java
 * Demonstrates: filter, distinct, limit, skip, takeWhile, and dropWhile
 * on a stream of Order records, showing SQL equivalents for each operator.
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;

public class FilteringOperators {

    private static final Logger LOG = Logger.getLogger(FilteringOperators.class.getName());

    record Order(String orderId, String status, String region, double amount) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "CONFIRMED", "NORTH", 1200.0),
            new Order("ORD-002", "CONFIRMED", "SOUTH", 950.0),
            new Order("ORD-003", "PENDING",   "NORTH", 800.0),
            new Order("ORD-004", "CONFIRMED", "EAST",  750.0),
            new Order("ORD-005", "CANCELLED", "SOUTH", 600.0),
            new Order("ORD-006", "CONFIRMED", "NORTH", 550.0),
            new Order("ORD-007", "PENDING",   "EAST",  490.0),
            new Order("ORD-008", "CONFIRMED", "WEST",  400.0),
            new Order("ORD-009", "CONFIRMED", "NORTH", 300.0),
            new Order("ORD-010", "PENDING",   "WEST",  200.0)
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // filter — SQL: WHERE status = 'CONFIRMED'
        List<Order> confirmed = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList();
        LOG.info("confirmed count: " + confirmed.size());

        // distinct — SQL: SELECT DISTINCT region
        List<String> uniqueRegions = orders.stream()
                .map(Order::region)
                .distinct()
                .toList();
        LOG.info("unique regions: " + uniqueRegions);

        // limit — SQL: LIMIT 3
        List<Order> topThree = orders.stream()
                .limit(3)
                .toList();
        LOG.info("top 3 ids: " + topThree.stream().map(Order::orderId).toList());

        // skip — SQL: OFFSET 7
        List<Order> afterFirst7 = orders.stream()
                .skip(7)
                .toList();
        LOG.info("after skip 7: " + afterFirst7.stream().map(Order::orderId).toList());

        // limit + skip — SQL: LIMIT 3 OFFSET 3 (page 2, page size 3)
        List<Order> page2 = orders.stream()
                .skip(3)
                .limit(3)
                .toList();
        LOG.info("page 2: " + page2.stream().map(Order::orderId).toList());

        // takeWhile — keep while amount > 500, stop at first failure
        List<Order> whileHighValue = orders.stream()
                .takeWhile(o -> o.amount() > 500.0)
                .toList();
        LOG.info("takeWhile > 500: " + whileHighValue.stream().map(Order::orderId).toList());

        // dropWhile — discard while amount > 500, keep the rest
        List<Order> afterHighValue = orders.stream()
                .dropWhile(o -> o.amount() > 500.0)
                .toList();
        LOG.info("dropWhile > 500: " + afterHighValue.stream().map(Order::orderId).toList());

        // Output:
        // confirmed count: 6
        // unique regions: [NORTH, SOUTH, EAST, WEST]
        // top 3 ids: [ORD-001, ORD-002, ORD-003]
        // after skip 7: [ORD-008, ORD-009, ORD-010]
        // page 2: [ORD-004, ORD-005, ORD-006]
        // takeWhile > 500: [ORD-001, ORD-002, ORD-003, ORD-004, ORD-005, ORD-006]
        // dropWhile > 500: [ORD-007, ORD-008, ORD-009, ORD-010]
    }
}