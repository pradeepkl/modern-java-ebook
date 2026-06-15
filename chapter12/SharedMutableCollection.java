// Java 25+
// Feature shown: exposing an internal mutable list reference, final in Java 8+
/**
 * Listing 12.1 — SharedMutableCollection.java
 * Demonstrates: the danger of returning an internal mutable list directly,
 * allowing external callers to corrupt encapsulated service state
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter12;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SharedMutableCollection {

    private static final Logger log =
            Logger.getLogger(SharedMutableCollection.class.getName());

    record Order(String orderId, double amount) {}

    static class OrderService {
        // Internal list — should never be exposed directly
        private final List<Order> orders = new ArrayList<>();

        public void add(Order order) {
            orders.add(order);
        }

        // Returns the internal list — caller can mutate it freely
        public List<Order> getOrders() {
            return orders; // ownership leak: caller gains full mutation rights
        }
    }

    void main() {
        OrderService service = new OrderService();
        service.add(new Order("ORD-001", 99.99));
        service.add(new Order("ORD-002", 149.99));

        log.info("Orders before external mutation: "
                + service.getOrders().size()); // 2

        // External code obtains a direct reference to the internal list
        List<Order> orders = service.getOrders();

        // External mutation silently destroys internal service state
        orders.clear(); // no exception, no warning — state is gone

        log.info("Orders remaining: "
                + service.getOrders().size()); // 0

        // Output:
        // Orders before external mutation: 2
        // Orders remaining: 0
        // The service's internal state was destroyed by code
        // that had no business doing so.
    }
}