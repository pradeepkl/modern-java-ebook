// Java 17+
/**
 * Listing 6.17 — PatternMatchingInStreams.java
 * Demonstrates: instanceof pattern binding inside stream pipelines
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 17+
 */
package chapter06;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PatternMatchingInStreams {

    private static final Logger log =
            Logger.getLogger(PatternMatchingInStreams.class.getName());

    sealed interface OrderEvent permits OrderPlaced, OrderCancelled {}

    record OrderPlaced(String orderId, double amount) implements OrderEvent {}

    record OrderCancelled(String orderId, String reason) implements OrderEvent {}

    // Returns IDs of all cancelled orders using mapMulti + pattern binding
    public static List<String> cancelledOrderIds(List<OrderEvent> events) {
        return events.stream()
                .<String>mapMulti((event, downstream) -> {
                    // instanceof binds oc directly — no explicit cast needed
                    if (event instanceof OrderCancelled oc) {
                        downstream.accept(oc.orderId());
                    }
                })
                .collect(Collectors.toList());
    }

    // Logs orders where amount exceeds threshold using pattern binding in filter
    public static void logHighValueOrders(List<OrderEvent> events) {
        events.stream()
                // Pattern binding with guard condition in a single expression
                .filter(e -> e instanceof OrderPlaced op && op.amount() > 5000)
                .forEach(e -> {
                    // Re-bind in forEach because filter scope does not extend here
                    if (e instanceof OrderPlaced op) {
                        log.info("High value: " + op.orderId() + " = " + op.amount());
                    }
                });
    }

    public static void main(String[] args) {
        List<OrderEvent> events = List.of(
                new OrderPlaced("ORD-001", 1200.00),
                new OrderCancelled("ORD-002", "Out of stock"),
                new OrderPlaced("ORD-003", 7500.00),
                new OrderCancelled("ORD-004", "Customer request"),
                new OrderPlaced("ORD-005", 6200.00)
        );

        List<String> cancelled = cancelledOrderIds(events);
        // Log each cancelled order ID
        cancelled.forEach(id -> log.info("Cancelled order: " + id));

        logHighValueOrders(events);

        // Output:
        // INFO: Cancelled order: ORD-002
        // INFO: Cancelled order: ORD-004
        // INFO: High value: ORD-003 = 7500.0
        // INFO: High value: ORD-005 = 6200.0
    }
}