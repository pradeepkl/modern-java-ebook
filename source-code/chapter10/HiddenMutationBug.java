// Java 8+
/**
 * Listing 10.2 — HiddenMutationBug.java
 * Demonstrates: Hidden side effects when a method mutates a shared list
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 8+
 */
package chapter10;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HiddenMutationBug {

    private static final Logger log =
            Logger.getLogger(HiddenMutationBug.class.getName());

    record Order(String orderId, double amount) {}

    // Builds and returns a fresh mutable list of orders
    static List<Order> fetchOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("ORD-001", 99.99));
        orders.add(new Order("ORD-002", 149.99));
        orders.add(new Order("ORD-003", 49.99));
        return orders;
    }

    // ❌ Silently removes elements from the list it received
    static double applyVipDiscount(List<Order> orders) {
        // Caller does not know this mutates the shared list
        orders.removeIf(o -> o.amount() < 100.0); // removes ORD-001 and ORD-003
        return orders.stream()
                .mapToDouble(o -> o.amount() * 0.9)
                .sum();
    }

    // Expects the full original list — but it has already been mutated
    static long countOrders(List<Order> orders) {
        return orders.size(); // returns 1, not 3
    }

    public static void main(String[] args) {
        List<Order> orders = fetchOrders(); // 3 orders

        log.info("Before discount — order count: " + orders.size()); // 3

        // Both methods share the same list reference
        double discounted = applyVipDiscount(orders); // mutates orders!
        long count = countOrders(orders);             // sees mutated list

        log.info("Discounted total: " + discounted);  // 134.991
        log.info("Order count: " + count);            // 1 — bug!

        // Expected count: 3
        // Actual count:   1 — applyVipDiscount silently removed two orders
        // from the shared list before countOrders was called

        // Output:
        // Before discount — order count: 3
        // Discounted total: 134.991
        // Order count: 1
    }
}