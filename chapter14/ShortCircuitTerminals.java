// Java 25+
// Feature shown: stream short-circuit terminals, final in Java 8+

/**
 * Listing 14.13 — ShortCircuitTerminals.java
 * Demonstrates: short-circuit terminal and intermediate operations on streams
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ShortCircuitTerminals {

    private static final Logger LOG = Logger.getLogger(ShortCircuitTerminals.class.getName());

    record Order(String orderId, double amount, String status, String region) {}

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", 120.0,  "CONFIRMED", "US"),
            new Order("ORD-002", 750.0,  "URGENT",    "UK"),
            new Order("ORD-003", 340.0,  "CONFIRMED", "UK"),
            new Order("ORD-004", 980.0,  "CONFIRMED", "EU"),
            new Order("ORD-005", 55.0,   "CONFIRMED", "UK"),
            new Order("ORD-006", 610.0,  "URGENT",    "UK"),
            new Order("ORD-007", -10.0,  "CANCELLED", "US")
        );

        // findFirst — stops at the first element passing the filter
        Optional<Order> first = orders.stream()
                .filter(o -> o.amount() > 500.0)
                .findFirst();
        LOG.info("findFirst > 500: " + first.map(Order::orderId).orElse("none"));

        // findAny — parallel stream, returns whichever thread finds a match first
        Optional<Order> any = orders.parallelStream()
                .filter(o -> o.amount() > 500.0)
                .findAny();
        LOG.info("findAny > 500: " + any.map(Order::orderId).orElse("none"));

        // anyMatch — stops as soon as one match is found
        boolean hasUrgent = orders.stream()
                .anyMatch(o -> o.status().equals("URGENT"));
        LOG.info("anyMatch URGENT: " + hasUrgent);

        // allMatch — stops as soon as one element fails the predicate
        boolean allValid = orders.stream()
                .allMatch(o -> o.amount() > 0);
        LOG.info("allMatch amount > 0: " + allValid);

        // noneMatch — stops as soon as one element satisfies the predicate
        boolean noNegative = orders.stream()
                .noneMatch(o -> o.amount() < 0);
        LOG.info("noneMatch amount < 0: " + noNegative);

        // limit() combined with filter — processes at most 5 UK elements
        List<Order> sample = orders.stream()
                .filter(o -> o.region().equals("UK"))
                .limit(5)       // short-circuit intermediate
                .toList();      // collects only up to 5 elements
        LOG.info("UK sample size: " + sample.size());

        // Output:
        // findFirst > 500: ORD-002
        // findAny > 500: ORD-002 (or any matching order in parallel)
        // anyMatch URGENT: true
        // allMatch amount > 0: false
        // noneMatch amount < 0: false
        // UK sample size: 4
    }
}