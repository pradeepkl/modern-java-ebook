// Java 25+
// Feature shown: stream sorted() ordering operators, final in Java 8+

/**
 * Listing 14.9 — OrderingOperators.java
 * Demonstrates: sorted() with natural order, custom Comparator, multi-field sort,
 * and filter-before-sort efficiency pattern in stream pipelines
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class OrderingOperators {

    private static final Logger log = Logger.getLogger(OrderingOperators.class.getName());

    record Order(String orderId, String region, double amount, String status) {}

    static List<Order> sampleOrders() {
        return List.of(
            new Order("O1", "WEST",   450.0, "CONFIRMED"),
            new Order("O2", "EAST",   120.0, "PENDING"),
            new Order("O3", "WEST",   980.0, "CONFIRMED"),
            new Order("O4", "NORTH",  310.0, "CONFIRMED"),
            new Order("O5", "EAST",   750.0, "CONFIRMED"),
            new Order("O6", "NORTH",   80.0, "PENDING"),
            new Order("O7", "SOUTH",  560.0, "CONFIRMED")
        );
    }

    void main() {
        List<Order> orders = sampleOrders();

        // sorted() — natural order (Comparable required)
        // SQL equivalent: ORDER BY region ASC
        List<String> sortedRegions = orders.stream()
                .map(Order::region)
                .distinct()
                .sorted()           // natural String order
                .toList();
        log.info("Sorted regions: " + sortedRegions);

        // sorted(Comparator) — custom ordering
        // SQL equivalent: ORDER BY amount DESC
        List<Order> byAmountDesc = orders.stream()
                .sorted(Comparator
                        .comparingDouble(Order::amount)
                        .reversed())
                .toList();
        log.info("By amount desc (first): " + byAmountDesc.get(0).orderId()
                + " amount=" + byAmountDesc.get(0).amount());

        // Multi-field sort: region ASC, amount DESC
        // SQL equivalent: ORDER BY region ASC, amount DESC
        List<Order> multiSort = orders.stream()
                .sorted(Comparator
                        .comparing(Order::region)
                        .thenComparing(Comparator
                                .comparingDouble(Order::amount)
                                .reversed()))
                .toList();
        log.info("Multi-sort first: " + multiSort.get(0).region()
                + " / " + multiSort.get(0).amount());

        // Efficient: filter first, then sort fewer elements
        List<Order> efficient = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))  // reduce set first
                .sorted(Comparator.comparingDouble(Order::amount).reversed())
                .limit(3)
                .toList();
        log.info("Efficient top-3 confirmed: " + efficient.stream()
                .map(o -> o.orderId() + "=" + o.amount()).toList());

        // Output:
        // Sorted regions: [EAST, NORTH, SOUTH, WEST]
        // By amount desc (first): O3 amount=980.0
        // Multi-sort first: EAST / 750.0
        // Efficient top-3 confirmed: [O3=980.0, O5=750.0, O7=560.0]
    }
}