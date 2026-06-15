// Java 25+
// Feature shown: stream sorted() and Comparator chaining, final in Java 8+

/**
 * Listing 14.9 — OrderingOperators.java
 * Demonstrates: sorted() with natural order, custom Comparator, multi-field
 * sort, and filter-before-sort efficiency pattern in stream pipelines.
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

record Order(String orderId, String region, double amount, String status) {}

public class OrderingOperators {

    private static final Logger log = Logger.getLogger(OrderingOperators.class.getName());

    void main() {
        List<Order> orders = List.of(
            new Order("O1", "WEST",   250.0, "CONFIRMED"),
            new Order("O2", "EAST",   180.0, "PENDING"),
            new Order("O3", "WEST",   420.0, "CONFIRMED"),
            new Order("O4", "NORTH",  310.0, "CONFIRMED"),
            new Order("O5", "EAST",   95.0,  "CANCELLED"),
            new Order("O6", "NORTH",  530.0, "CONFIRMED"),
            new Order("O7", "SOUTH",  75.0,  "PENDING")
        );

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
        log.info("By amount desc: " + byAmountDesc.stream()
                .map(o -> o.orderId() + "=" + o.amount()).toList());

        // Multi-field sort
        // SQL equivalent: ORDER BY region ASC, amount DESC
        List<Order> multiSort = orders.stream()
                .sorted(Comparator
                        .comparing(Order::region)
                        .thenComparing(Comparator
                                .comparingDouble(Order::amount)
                                .reversed()))
                .toList();
        log.info("Multi-sort: " + multiSort.stream()
                .map(o -> o.region() + "/" + o.amount()).toList());

        // Efficient: filter first, then sort fewer elements
        List<Order> efficient = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))  // reduce set first
                .sorted(Comparator.comparingDouble(Order::amount).reversed())
                .limit(10)
                .toList();
        log.info("Efficient top confirmed: " + efficient.stream()
                .map(Order::orderId).toList());

        // Output:
        // Sorted regions: [EAST, NORTH, SOUTH, WEST]
        // By amount desc: [O6=530.0, O3=420.0, O4=310.0, O1=250.0, O2=180.0, O5=95.0, O7=75.0]
        // Multi-sort: [EAST/180.0, EAST/95.0, NORTH/530.0, NORTH/310.0, SOUTH/75.0, WEST/420.0, WEST/250.0]
        // Efficient top confirmed: [O6, O3, O4, O1]
    }
}