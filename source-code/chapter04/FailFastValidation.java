// Java 8+
/**
 * Listing 4.3 — FailFastValidation.java
 * Demonstrates: Fail-fast validation by checking preconditions early
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.util.ArrayList;
import java.util.List;

public class FailFastValidation {

    // Simple Order model for demonstration
    static class Order {
        private final String id;
        private final List<String> items;

        Order(String id, List<String> items) {
            this.id = id;
            this.items = items;
        }

        List<String> getItems() { return items; }
        String getId()          { return id; }
    }

    // Fail-fast: validate at the entry point before any processing begins
    public void processOrder(Order order) {
        if (order == null) {
            // Reject null immediately — do not let it propagate deeper
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getItems().isEmpty()) {
            // Reject empty orders before any business logic runs
            throw new IllegalArgumentException(
                    "Order must contain at least one item");
        }
        // Invariants satisfied — safe to proceed with processing
        System.out.println("Processing order: " + order.getId()
                + " with " + order.getItems().size() + " item(s).");
    }

    public static void main(String[] args) {
        FailFastValidation validator = new FailFastValidation();

        // Valid order — should process successfully
        Order valid = new Order("ORD-001", List.of("Widget", "Gadget"));
        validator.processOrder(valid);

        // Empty order — should fail fast with a clear message
        try {
            Order empty = new Order("ORD-002", new ArrayList<>());
            validator.processOrder(empty);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // Null order — should fail fast with a clear message
        try {
            validator.processOrder(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // Output:
        // Processing order: ORD-001 with 2 item(s).
        // Caught: Order must contain at least one item
        // Caught: Order cannot be null
    }
}