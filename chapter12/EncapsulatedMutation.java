// Java 25+
// Feature shown: encapsulated mutation with List.copyOf snapshots, final in Java 16+
package chapter12;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Listing 12.3 — EncapsulatedMutation.java
 * Demonstrates: encapsulating mutable state inside an aggregate and
 * exposing only immutable snapshots to external callers via List.copyOf
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class EncapsulatedMutation {

    private static final Logger log =
            Logger.getLogger(EncapsulatedMutation.class.getName());

    record Order(String orderId, double amount) {}

    // Generic aggregate — owns mutation for any item type
    static class Aggregate<T> {

        private final List<T> items = new ArrayList<>();

        public void add(T item) {
            items.add(item);
        }

        public void removeIf(Predicate<T> condition) {
            items.removeIf(condition);
        }

        // Consumers receive an immutable snapshot
        // They cannot modify internal state
        public List<T> snapshot() {
            return List.copyOf(items); // defensive copy, unmodifiable
        }

        public int size() {
            return items.size();
        }
    }

    void main() {
        Aggregate<Order> service = new Aggregate<>();
        service.add(new Order("ORD-001", 99.99));
        service.add(new Order("ORD-002", 149.99));

        List<Order> snapshot = service.snapshot(); // immutable view

        try {
            snapshot.add(new Order("ORD-999", 0.0)); // must throw
        } catch (UnsupportedOperationException e) {
            log.info("Cannot modify snapshot — internal state protected");
        }

        log.info("Aggregate size: " + service.size()); // still 2

        // Output:
        // INFO: Cannot modify snapshot — internal state protected
        // INFO: Aggregate size: 2
    }
}