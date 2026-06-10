// Java 16+
/**
 * Listing 10.5 — DefensiveCopying.java
 * Demonstrates: Defensive copying at construction and return boundaries
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 16+
 */
package chapter10;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.logging.Logger;

public class DefensiveCopying {

    private static final Logger log =
            Logger.getLogger(DefensiveCopying.class.getName());

    record Order(String orderId, double amount) {}

    // Generic summary — defensive copies protect
    // internal state for any item type T
    static class ImmutableSummary<T> {

        private final List<T> items;
        private final double total;

        // Defensive copy on construction —
        // caller cannot mutate internal state
        // by modifying the list they passed in
        public ImmutableSummary(List<T> items, ToDoubleFunction<T> valueExtractor) {
            this.items = List.copyOf(items); // ✅ snapshot taken here
            this.total = this.items.stream()
                    .mapToDouble(valueExtractor)
                    .sum();
        }

        // Defensive copy on return —
        // caller cannot mutate internal state
        // through the returned list
        public List<T> items() {
            return List.copyOf(items); // ✅ new copy each time
        }

        public double total() { return total; }
        public int size()     { return items.size(); }
    }

    public static void main(String[] args) {
        List<Order> mutable = new ArrayList<>();
        mutable.add(new Order("ORD-001", 99.99));
        mutable.add(new Order("ORD-002", 149.99));

        ImmutableSummary<Order> summary =
                new ImmutableSummary<>(mutable, Order::amount);

        // Mutating the original list after construction has no effect
        mutable.add(new Order("ORD-003", 49.99));
        log.info("Total: "  + summary.total()); // 249.98, not 299.97
        log.info("Count: "  + summary.size());  // 2, not 3

        // Mutating the returned list has no effect on internal state
        List<Order> returned = summary.items();
        try {
            returned.add(new Order("ORD-004", 9.99)); // throws
        } catch (UnsupportedOperationException e) {
            log.info("Returned list is unmodifiable — internal state protected");
        }
        log.info("Count after attempted mutation: " + summary.size()); // still 2

        // Output:
        // Total: 249.98
        // Count: 2
        // Returned list is unmodifiable — internal state protected
        // Count after attempted mutation: 2
    }
}