// Java 25+
// Feature shown: stream Optional terminals (findFirst, max, orElse, orElseThrow), final in Java 8+

/**
 * Listing 14.14 — OptionalFromStream.java
 * Demonstrates: composing Optional results from stream terminals
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class OptionalFromStream {

    private static final Logger log = Logger.getLogger(OptionalFromStream.class.getName());

    record Order(String orderId, double amount, String status) {
        static Order empty() {
            return new Order("NONE", 0.0, "EMPTY");
        }
    }

    static class OrderNotFoundException extends RuntimeException {
        OrderNotFoundException(String id) {
            super("Order not found: " + id);
        }
    }

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", 750.0, "PENDING"),
            new Order("ORD-002", 200.0, "URGENT"),
            new Order("ORD-003", 1200.0, "PENDING")
        );

        // Optional chain — transform and act only if present
        orders.stream()
            .filter(o -> o.amount() > 500.0)
            .findFirst()
            .map(Order::orderId)        // transform if present
            .ifPresent(id ->            // act only if present
                log.info("High value order: " + id));

        // orElse — provide a safe default when stream is empty
        Order highest = orders.stream()
            .max(Comparator.comparingDouble(Order::amount))
            .orElse(Order.empty());     // no orders — safe fallback
        log.info("Highest order: " + highest.orderId() + " amount=" + highest.amount());

        // orElseThrow — absence is an error condition
        try {
            Order required = orders.stream()
                .filter(o -> o.orderId().equals("ORD-001"))
                .findFirst()
                .orElseThrow(() ->
                    new OrderNotFoundException("ORD-001")); // throws if absent
            log.info("Required order found: " + required.orderId());
        } catch (OrderNotFoundException ex) {
            log.warning(ex.getMessage());
        }

        // Output:
        // INFO: High value order: ORD-001
        // INFO: Highest order: ORD-003 amount=1200.0
        // INFO: Required order found: ORD-001
    }
}