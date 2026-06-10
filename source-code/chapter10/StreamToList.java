// Java 16+
/**
 * Listing 10.7 — StreamToList.java
 * Demonstrates: Stream.toList() vs Collectors.toList() vs Collectors.toUnmodifiableList()
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 16+
 */
package chapter10;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public class StreamToList {

    private static final Logger log =
            Logger.getLogger(StreamToList.class.getName());

    record Order(String orderId, double amount, String status) {}

    public static void main(String[] args) {

        List<Order> orders = List.of(
                new Order("ORD-001", 99.99, "CONFIRMED"),
                new Order("ORD-002", 49.99, "PENDING"),
                new Order("ORD-003", 149.99, "CONFIRMED"));

        // Collectors.toList() — returns mutable ArrayList
        // Mutating this result is possible but unintended
        List<Order> mutableResult = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.toList()); // mutable — allows accidental mutation

        mutableResult.add(new Order("ORD-999", 0.0, "FAKE")); // compiles and runs
        log.info("Mutable result size after add: " + mutableResult.size());

        // Stream.toList() — returns unmodifiable list (Java 16+)
        // Communicates intent: this is a result, not a working buffer
        List<Order> immutableResult = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList(); // unmodifiable — preferred in Java 16+

        // Collectors.toUnmodifiableList() — Java 10+
        // Equivalent to Stream.toList() for older-style pipelines
        List<Order> unmodifiableResult = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.toUnmodifiableList());

        log.info("Confirmed orders (toList): " + immutableResult.size());
        log.info("Confirmed orders (toUnmodifiableList): " + unmodifiableResult.size());

        // Attempting mutation on Stream.toList() result throws UnsupportedOperationException
        try {
            immutableResult.add(new Order("ORD-999", 0.0, "FAKE"));
        } catch (UnsupportedOperationException e) {
            log.info("Stream.toList() is unmodifiable — mutation rejected");
        }

        // Attempting mutation on toUnmodifiableList() result also throws
        try {
            unmodifiableResult.add(new Order("ORD-999", 0.0, "FAKE"));
        } catch (UnsupportedOperationException e) {
            log.info("toUnmodifiableList() is unmodifiable — mutation rejected");
        }

        // Output:
        // INFO: Mutable result size after add: 3
        // INFO: Confirmed orders (toList): 2
        // INFO: Confirmed orders (toUnmodifiableList): 2
        // INFO: Stream.toList() is unmodifiable — mutation rejected
        // INFO: toUnmodifiableList() is unmodifiable — mutation rejected
    }
}