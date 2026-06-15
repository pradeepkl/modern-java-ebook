// Java 25+
// Feature shown: Stream.toList() vs Collectors.toList(), final in Java 16+

/**
 * Listing 12.7 — StreamToList.java
 * Demonstrates: Stream.toList() returning an unmodifiable list versus
 * Collectors.toList() returning a mutable ArrayList, and
 * Collectors.toUnmodifiableList() as the pre-Java-16 equivalent.
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (uses void main() instance main method, JEP 512)
 */
package chapter12;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public class StreamToList {

    private static final Logger log =
            Logger.getLogger(StreamToList.class.getName());

    record Order(String orderId, double amount, String status) {}

    void main() {

        List<Order> orders = List.of(
                new Order("ORD-001", 99.99, "CONFIRMED"),
                new Order("ORD-002", 49.99, "PENDING"),
                new Order("ORD-003", 149.99, "CONFIRMED"));

        // Collectors.toList() — returns a mutable ArrayList
        // Mutating this result is possible but unintended
        List<Order> mutableResult = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.toList()); // mutable ArrayList

        mutableResult.add(new Order("ORD-999", 0.0, "TEST")); // allowed
        log.info("Mutable result size after add: " + mutableResult.size());

        // Stream.toList() — returns an unmodifiable list (Java 16+)
        // Communicates intent: this is a result, not a working buffer
        List<Order> immutableResult = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList(); // unmodifiable

        // Collectors.toUnmodifiableList() — equivalent for older pipelines
        List<Order> unmodifiableResult = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.toUnmodifiableList());

        log.info("Confirmed via toList(): " + immutableResult.size());
        log.info("Confirmed via toUnmodifiableList(): " + unmodifiableResult.size());

        // Attempting to modify Stream.toList() result throws UnsupportedOperationException
        try {
            immutableResult.add(new Order("ORD-999", 0.0, "TEST"));
        } catch (UnsupportedOperationException e) {
            log.info("Stream.toList() result is unmodifiable — modification rejected");
        }

        // Output:
        // Mutable result size after add: 3
        // Confirmed via toList(): 2
        // Confirmed via toUnmodifiableList(): 2
        // Stream.toList() result is unmodifiable — modification rejected
    }
}