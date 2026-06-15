// Java 25+
// Feature shown: defensive copying with List.copyOf, final in Java 16+
/**
 * Listing 12.5 — DefensiveCopying.java
 * Demonstrates: defensive copying at construction and return boundaries
 * using List.copyOf to protect internal state in a generic summary class
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter12;

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
        public ImmutableSummary(
                List<T> items,
                ToDoubleFunction<T> valueExtractor) {
            this.items = List.copyOf(items); // snapshot taken here
            this.total = this.items.stream()
                    .mapToDouble(valueExtractor)
                    .sum();
        }

        // Defensive copy on return —
        // caller cannot mutate internal state
        // through the returned list
        public List<T> items() {
            return List.copyOf(items); // new copy each time
        }

        public double total() { return total; }

        public int size() { return items.size(); }
    }

    void main() {
        List<Order> original = new ArrayList<>();
        original.add(new Order("ORD-001", 99.99));
        original.add(new Order("ORD-002", 149.99));

        ImmutableSummary<Order> summary =
                new ImmutableSummary<>(original, Order::amount);

        original.add(new Order("ORD-003", 49.99)); // mutate after construction

        log.info("Total: " + summary.total());  // 249.98 — unaffected
        log.info("Count: " + summary.size());   // 2 — unaffected

        List<Order> returned = summary.items();
        log.info("Returned list size: " + returned.size()); // 2

        // Output:
        // Total: 249.98
        // Count: 2
        // Returned list size: 2
    }
}