// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 14.15 — SideEffectTerminal.java
 * Demonstrates: forEach and forEachOrdered as side-effect terminals on streams
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;

public class SideEffectTerminal {

    private static final Logger log = Logger.getLogger(SideEffectTerminal.class.getName());

    record Order(String orderId, String status, double amount, String region) {}

    void main() {

        List<Order> orders = List.of(
            new Order("ORD-001", "CONFIRMED", 250.0, "US"),
            new Order("ORD-002", "PENDING",   100.0, "UK"),
            new Order("ORD-003", "CONFIRMED", 480.0, "EU"),
            new Order("ORD-004", "CONFIRMED",  75.0, "US"),
            new Order("ORD-005", "CANCELLED", 320.0, "UK")
        );

        // forEach — apply a side effect to every element
        // Use for: logging, sending events, writing to external systems
        // Do NOT use for: building results (use map + toList instead)
        orders.stream()
              .filter(o -> o.status().equals("CONFIRMED"))
              .forEach(o ->
                  log.info("Dispatching: " + o.orderId()));

        // forEachOrdered — guarantees encounter order
        // Important for parallel streams where forEach
        // does not guarantee order
        orders.parallelStream()
              .filter(o -> o.status().equals("CONFIRMED"))
              .forEachOrdered(o ->
                  log.info("Ordered: " + o.orderId()));

        // Output:
        // INFO: Dispatching: ORD-001
        // INFO: Dispatching: ORD-003
        // INFO: Dispatching: ORD-004
        // INFO: Ordered: ORD-001
        // INFO: Ordered: ORD-003
        // INFO: Ordered: ORD-004
    }
}