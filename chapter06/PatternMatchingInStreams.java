// Java 25+
// Feature shown: pattern matching for instanceof in stream pipelines, final in Java 16+

package chapter06;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Listing 6.17 — PatternMatchingInStreams.java
 * Demonstrates: instanceof pattern binding inside stream pipelines using
 * mapMulti and filter, eliminating explicit casts entirely.
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 16+ for pattern matching for instanceof; Java 25+ for
 * the void main() instance main method (JEP 512).
 */
public class PatternMatchingInStreams {

    private static final Logger log =
            Logger.getLogger(PatternMatchingInStreams.class.getName());

    sealed interface OrderEvent
            permits OrderPlaced, OrderCancelled {}

    record OrderPlaced(String orderId, double amount)
            implements OrderEvent {}

    record OrderCancelled(String orderId, String reason)
            implements OrderEvent {}

    // Returns only the IDs of cancelled orders — no explicit cast needed.
    static List<String> cancelledOrderIds(List<OrderEvent> events) {
        return events.stream()
                .<String>mapMulti((event, downstream) -> {
                    // instanceof binds oc directly; no cast required
                    if (event instanceof OrderCancelled oc) {
                        downstream.accept(oc.orderId());
                    }
                })
                .collect(Collectors.toList());
    }

    // Logs orders whose amount exceeds 5000.
    static void logHighValueOrders(List<OrderEvent> events) {
        events.stream()
                // Pattern binding with a guard condition in filter
                .filter(e -> e instanceof OrderPlaced op && op.amount() > 5000)
                .forEach(e -> {
                    // Re-bind in forEach because op from filter is out of scope
                    if (e instanceof OrderPlaced op) {
                        log.info("High value: " + op.orderId()
                                + " = " + op.amount());
                    }
                });
    }

    void main() {
        List<OrderEvent> events = List.of(
                new OrderPlaced("ORD-1", 120.00),
                new OrderCancelled("ORD-2", "Out of stock"),
                new OrderPlaced("ORD-3", 7500.00),
                new OrderCancelled("ORD-4", "Customer request"),
                new OrderPlaced("ORD-5", 6200.00)
        );

        List<String> cancelled = cancelledOrderIds(events);
        log.info("Cancelled order IDs: " + cancelled);

        logHighValueOrders(events);
        // Output:
        // INFO: Cancelled order IDs: [ORD-2, ORD-4]
        // INFO: High value: ORD-3 = 7500.0
        // INFO: High value: ORD-5 = 6200.0
    }
}