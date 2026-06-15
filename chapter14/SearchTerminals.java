// Java 25+
// Feature shown: stream search terminals (findFirst, findAny, anyMatch, allMatch, noneMatch), final in Java 8+

/**
 * Listing 14.12 — SearchTerminals.java
 * Demonstrates: Stream search terminals — short-circuit operations that stop
 * processing early once a result is determined.
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.logging.Logger;

public class SearchTerminals {

    private static final Logger LOG = Logger.getLogger(SearchTerminals.class.getName());

    record Order(String orderId, String region, double amount, String status) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "US",   250.0,  "CONFIRMED"),
            new Order("ORD-002", "UK",   1200.0, "CONFIRMED"),
            new Order("ORD-003", "UK",   450.0,  "CONFIRMED"),
            new Order("ORD-004", "EU",   800.0,  "CONFIRMED"),
            new Order("ORD-005", "US",   3000.0, "CONFIRMED")
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // findFirst — first element matching the pipeline; stops at first match
        Optional<Order> firstUK = orders.stream()
                .filter(o -> o.region().equals("UK"))
                .findFirst();
        LOG.info("findFirst UK: " + firstUK.map(Order::orderId).orElse("none"));

        // findAny — any element (faster in parallel streams); no ordering guarantee
        Optional<Order> anyUK = orders.stream()
                .filter(o -> o.region().equals("UK"))
                .findAny();
        LOG.info("findAny UK present: " + anyUK.isPresent());

        // anyMatch — does any element satisfy the predicate? stops at first true
        boolean hasHighValue = orders.stream()
                .anyMatch(o -> o.amount() > 1000.0);
        LOG.info("anyMatch amount > 1000: " + hasHighValue);

        // allMatch — do ALL elements satisfy the predicate? stops at first false
        boolean allConfirmed = orders.stream()
                .allMatch(o -> o.status().equals("CONFIRMED"));
        LOG.info("allMatch CONFIRMED: " + allConfirmed);

        // noneMatch — does NO element satisfy the predicate? stops at first true
        boolean noCancelled = orders.stream()
                .noneMatch(o -> o.status().equals("CANCELLED"));
        LOG.info("noneMatch CANCELLED: " + noCancelled);

        // Output:
        // findFirst UK: ORD-002
        // findAny UK present: true
        // anyMatch amount > 1000: true
        // allMatch CONFIRMED: true
        // noneMatch CANCELLED: true
    }
}