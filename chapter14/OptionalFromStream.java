// Java 25+
// Feature shown: stream Optional chaining with findFirst/max/orElse/orElseThrow, final in Java 8+

/**
 * Listing 14.14 — OptionalFromStream.java
 * Demonstrates: Optional chaining from stream terminals — map, ifPresent,
 * orElse, and orElseThrow as composable absent-value handlers
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
            new Order("ORD-001", 750.00, "PENDING"),
            new Order("ORD-002", 120.00, "URGENT"),
            new Order("ORD-003", 980.00, "PENDING")
        );

        // Optional chain — compose the absent case naturally
        orders.stream()
              .filter(o -> o.amount() > 500.0)
              .findFirst()
              .map(Order::orderId)        // transform if present
              .ifPresent(id ->            // act only if present
                      log.info("High value order: " + id));

        // orElse — provide a safe default
        Order highest = orders.stream()
              .max(Comparator.comparingDouble(Order::amount))
              .orElse(Order.empty());     // no orders — safe fallback
        log.info("Highest order: " + highest.orderId() + " amount=" + highest.amount());

        // orElseThrow — when absence is an error
        try {
            Order required = orders.stream()
                  .filter(o -> o.orderId().equals("ORD-001"))
                  .findFirst()
                  .orElseThrow(() ->
                          new OrderNotFoundException("ORD-001"));
            log.info("Required order found: " + required.orderId());
        } catch (OrderNotFoundException ex) {
            log.warning(ex.getMessage());
        }

        // Output:
        // INFO: High value order: ORD-001
        // INFO: Highest order: ORD-003 amount=980.0
        // INFO: Required order found: ORD-001
    }
}