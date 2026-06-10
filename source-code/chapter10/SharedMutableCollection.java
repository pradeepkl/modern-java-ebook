// Java 8+
/**
 * Listing 10.1 — SharedMutableCollection.java
 * Demonstrates: The hidden cost of exposing internal mutable collections
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 8+
 */
package chapter10;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SharedMutableCollection {

    private static final Logger log =
            Logger.getLogger(SharedMutableCollection.class.getName());

    record Order(String orderId, double amount) {}

    static class OrderService {

        // Internal mutable list — should never be exposed directly
        private final List<Order> orders = new ArrayList<>();

        public void add(Order order) {
            orders.add(order); // Adds to the internal list
        }

        // ❌ Returns the internal list — caller can mutate it freely
        public List<Order> getOrders() {
            return orders;
        }
    }

    public static void main(String[] args) {
        OrderService service = new OrderService();
        service.add(new Order("ORD-001", 99.99));
        service.add(new Order("ORD-002", 149.99));

        log.info("Orders before external mutation: "
                + service.getOrders().size()); // Expect 2

        // External code obtains a direct reference to the internal list
        List<Order> orders = service.getOrders();

        // ❌ External mutation destroys the service's internal state
        orders.clear();

        // The service's state is now corrupted — it has no orders
        log.info("Orders remaining after clear(): "
                + service.getOrders().size());

        log.info("Internal state was destroyed by external caller"
                + " — ownership was never communicated or enforced.");

        // Output:
        // Orders before external mutation: 2
        // Orders remaining after clear(): 0
        // Internal state was destroyed by external caller
        //   — ownership was never communicated or enforced.
    }
}