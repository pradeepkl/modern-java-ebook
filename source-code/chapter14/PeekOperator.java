// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 14.8 — PeekOperator.java
 * Demonstrates: peek operator for observing elements in a stream pipeline
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PeekOperator {

    private static final Logger log = Logger.getLogger(PeekOperator.class.getName());

    record Order(String orderId, String status, double amount) {}

    // Simulate enrichment — adds a prefix to the orderId
    static Order enrichOrder(Order o) {
        return new Order("ENR-" + o.orderId(), o.status(), o.amount() * 1.1);
    }

    static List<Order> getSampleOrders() {
        return List.of(
            new Order("ORD-001", "CONFIRMED", 120.00),
            new Order("ORD-002", "PENDING",   45.00),
            new Order("ORD-003", "CONFIRMED", 200.00),
            new Order("ORD-004", "CANCELLED", 75.00),
            new Order("ORD-005", "CONFIRMED", 310.00)
        );
    }

    void main() {
        // Enable FINE logging so peek output is visible
        log.setLevel(Level.FINE);
        var handler = new java.util.logging.ConsoleHandler();
        handler.setLevel(Level.FINE);
        log.addHandler(handler);
        log.setUseParentHandlers(false);

        List<Order> orders = getSampleOrders();

        // peek — observe elements passing through the pipeline
        // Use for debugging only — not for production side effects
        List<Order> result = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))  // keep only confirmed
                .peek(o -> log.fine(
                        "After filter: " + o.orderId()))       // observe post-filter
                .map(o -> enrichOrder(o))                      // enrich each order
                .peek(o -> log.fine(
                        "After enrich: " + o.orderId()))       // observe post-enrich
                .toList();

        // Log the final result count at INFO level
        log.info("Confirmed and enriched orders: " + result.size());
        result.forEach(o -> log.info(
                o.orderId() + " amount=" + String.format("%.2f", o.amount())));

        // Output:
        // FINE: After filter: ORD-001
        // FINE: After enrich: ENR-ORD-001
        // FINE: After filter: ORD-003
        // FINE: After enrich: ENR-ORD-003
        // FINE: After filter: ORD-005
        // FINE: After enrich: ENR-ORD-005
        // INFO: Confirmed and enriched orders: 3
        // INFO: ENR-ORD-001 amount=132.00
        // INFO: ENR-ORD-003 amount=220.00
        // INFO: ENR-ORD-005 amount=341.00
    }
}