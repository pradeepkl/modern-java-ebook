// Java 25+
// Feature shown: hidden mutation bug from shared mutable lists, final in Java 8+
/**
 * Listing 12.2 — HiddenMutationBug.java
 * Demonstrates: hidden side effects when two callers share the same mutable list;
 * applyVipDiscount silently removes elements, corrupting the count seen by countOrders
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter12;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HiddenMutationBug {

    private static final Logger log =
            Logger.getLogger(HiddenMutationBug.class.getName());

    record Order(String orderId, double amount) {}

    // Returns a mutable list — ownership is not communicated
    static List<Order> fetchOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("ORD-001", 99.99));
        orders.add(new Order("ORD-002", 149.99));
        orders.add(new Order("ORD-003", 49.99));
        return orders;
    }

    // Modifies the list it received — the caller does not know this happens
    static double applyVipDiscount(List<Order> orders) {
        orders.removeIf(o -> o.amount() < 100.0); // silent mutation of shared list
        return orders.stream()
                .mapToDouble(o -> o.amount() * 0.9)
                .sum();
    }

    // Expects to see all original orders — but the list was already mutated
    static long countOrders(List<Order> orders) {
        return orders.size();
    }

    void main() {
        List<Order> orders = fetchOrders(); // three orders

        // Both methods receive the same list reference
        double discounted = applyVipDiscount(orders); // removes two orders silently
        long count = countOrders(orders);             // sees only one order

        log.info("Discounted total: " + discounted);
        log.info("Order count: " + count);
        // Expected count: 3
        // Actual count:   1 — applyVipDiscount removed two orders from the shared list

        // Output:
        // INFO: Discounted total: 134.991
        // INFO: Order count: 1
    }
}