// Java 16+
/**
 * Listing 10.3 — EncapsulatedMutation.java
 * Demonstrates: Encapsulating mutable state inside an Aggregate class
 *               while exposing only immutable snapshots to callers.
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 16+
 */
package chapter10;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class EncapsulatedMutation {

    private static final Logger log =
            Logger.getLogger(EncapsulatedMutation.class.getName());

    // Immutable data carrier — safe to share across boundaries
    record Order(String orderId, double amount) {}

    // Generic aggregate — owns mutation for any item type
    static class Aggregate<T> {

        private final List<T> items = new ArrayList<>(); // mutable, private

        // Only the aggregate itself calls add()
        public void add(T item) {
            items.add(item);
        }

        // Controlled mutation via predicate — caller cannot touch the list directly
        public void removeIf(Predicate<T> condition) {
            items.removeIf(condition);
        }

        // Consumers receive an immutable snapshot — cannot modify internal state
        public List<T> snapshot() {
            return List.copyOf(items); // defensive copy, unmodifiable
        }

        public int size() {
            return items.size();
        }
    }

    public static void main(String[] args) {
        Aggregate<Order> service = new Aggregate<>();
        service.add(new Order("ORD-001", 99.99));
        service.add(new Order("ORD-002", 149.99));

        // Caller gets a snapshot — not the live internal list
        List<Order> snapshot = service.snapshot();

        try {
            // Attempt to mutate the snapshot — runtime blocks it
            snapshot.add(new Order("ORD-999", 0.0));
        } catch (UnsupportedOperationException e) {
            log.info("Cannot modify snapshot — internal state protected");
        }

        // Remove low-value orders through the controlled API
        service.removeIf(o -> o.amount() < 100.0);
        log.info("Aggregate size after removeIf: " + service.size()); // 1

        // Output:
        // INFO: Cannot modify snapshot — internal state protected
        // INFO: Aggregate size after removeIf: 1
    }
}