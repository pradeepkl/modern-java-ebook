// Java 25+
// Feature shown: stream short-circuit terminal operations, final in Java 8+

/**
 * Listing 14.4 — ShortCircuitInline.java
 * Demonstrates: findFirst() and anyMatch() short-circuit evaluation in streams
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ShortCircuitInline {

    private static final Logger LOG = Logger.getLogger(ShortCircuitInline.class.getName());

    record Order(String orderId, double amount, String status) {}

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", 250.0,  "CONFIRMED"),
            new Order("ORD-002", 750.0,  "CONFIRMED"), // first above 500
            new Order("ORD-003", 1200.0, "CONFIRMED"), // above 1000
            new Order("ORD-004", 300.0,  "PENDING"),
            new Order("ORD-005", 900.0,  "CONFIRMED")
        );

        // findFirst() stops as soon as one match is found
        // If the first element passes filter, no other element
        // is ever evaluated — regardless of collection size
        Optional<Order> first = orders.stream()
                .filter(o -> {
                    LOG.info("filter(>500) checking: " + o.orderId()); // shows early stop
                    return o.amount() > 500.0;
                })
                .findFirst(); // stops at first match

        first.ifPresentOrElse(
            o -> LOG.info("findFirst result: " + o.orderId() + " amount=" + o.amount()),
            () -> LOG.info("findFirst result: none")
        );

        // anyMatch() stops at the first true result
        boolean hasHighValue = orders.stream()
                .anyMatch(o -> {
                    LOG.info("anyMatch(>1000) checking: " + o.orderId()); // shows early stop
                    return o.amount() > 1000.0;
                });
        // Does not scan the entire list if found early

        LOG.info("hasHighValue (any order > 1000): " + hasHighValue);

        // Output:
        // filter(>500) checking: ORD-001
        // filter(>500) checking: ORD-002
        // findFirst result: ORD-002 amount=750.0
        // anyMatch(>1000) checking: ORD-001
        // anyMatch(>1000) checking: ORD-002
        // anyMatch(>1000) checking: ORD-003
        // hasHighValue (any order > 1000): true
    }
}