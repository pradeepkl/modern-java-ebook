// Java 25+
// Feature shown: stream peek operator for pipeline debugging, final in Java 8+

/**
 * Listing 14.8 — PeekOperator.java
 * Demonstrates: Using peek() to observe elements at intermediate pipeline stages
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PeekOperator {

    private static final Logger log = Logger.getLogger(PeekOperator.class.getName());

    record Order(String orderId, String status, double amount) {}

    // Simulates enrichment — appends a suffix to the orderId
    static Order enrichOrder(Order o) {
        return new Order(o.orderId() + "-ENRICHED", o.status(), o.amount());
    }

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "CONFIRMED", 120.00),
            new Order("ORD-002", "PENDING",   45.50),
            new Order("ORD-003", "CONFIRMED", 310.75),
            new Order("ORD-004", "CANCELLED", 89.00),
            new Order("ORD-005", "CONFIRMED", 55.20)
        );
    }

    void main() {
        log.setLevel(Level.FINE);
        log.getParent().getHandlers()[0].setLevel(Level.FINE);

        List<Order> orders = getOrders();

        // peek — observe elements passing through the pipeline
        // Use for debugging only — not for production side effects
        List<Order> result = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .peek(o -> log.fine(
                        "After filter: " + o.orderId()))   // logs each confirmed order
                .map(o -> enrichOrder(o))
                .peek(o -> log.fine(
                        "After enrich: " + o.orderId()))   // logs each enriched order
                .toList();

        // Log the final result count at INFO so it always appears
        log.info("Confirmed and enriched orders: " + result.size());
        result.forEach(o -> log.info(
                "  orderId=" + o.orderId() + "  amount=" + o.amount()));

        // Output:
        // FINE After filter: ORD-001
        // FINE After enrich: ORD-001-ENRICHED
        // FINE After filter: ORD-003
        // FINE After enrich: ORD-003-ENRICHED
        // FINE After filter: ORD-005
        // FINE After enrich: ORD-005-ENRICHED
        // INFO Confirmed and enriched orders: 3
        // INFO   orderId=ORD-001-ENRICHED  amount=120.0
        // INFO   orderId=ORD-003-ENRICHED  amount=310.75
        // INFO   orderId=ORD-005-ENRICHED  amount=55.2
    }
}