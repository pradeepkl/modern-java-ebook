// Java 25+
// Feature shown: stream toList() terminal operator, final in Java 16+

/**
 * Listing 14.10 — CollectionTerminals.java
 * Demonstrates: toList() vs collect(Collectors.toList()) vs collect(Collectors.toUnmodifiableList())
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public class CollectionTerminals {

    private static final Logger log = Logger.getLogger(CollectionTerminals.class.getName());

    record Order(String orderId, String status, double amount, String region) {}

    void main() {
        List<Order> orders = List.of(
            new Order("O1", "CONFIRMED", 250.0, "NORTH"),
            new Order("O2", "PENDING",   100.0, "SOUTH"),
            new Order("O3", "CONFIRMED", 400.0, "EAST"),
            new Order("O4", "CANCELLED",  75.0, "WEST"),
            new Order("O5", "CONFIRMED", 180.0, "NORTH")
        );

        // toList() — unmodifiable list, Java 16+
        List<Order> confirmed = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList(); // unmodifiable
        log.info("toList() confirmed count: " + confirmed.size());

        // Collectors.toList() — returns a mutable ArrayList
        List<Order> mutableList = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.toList()); // mutable ArrayList
        mutableList.add(new Order("O6", "CONFIRMED", 50.0, "SOUTH")); // allowed
        log.info("Collectors.toList() count after add: " + mutableList.size());

        // Collectors.toUnmodifiableList() — unmodifiable, Java 10+
        List<Order> unmodifiable = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.toUnmodifiableList());
        log.info("Collectors.toUnmodifiableList() count: " + unmodifiable.size());

        // Verify toList() result is unmodifiable
        try {
            confirmed.add(new Order("O7", "CONFIRMED", 99.0, "EAST"));
        } catch (UnsupportedOperationException e) {
            log.info("toList() is unmodifiable — add rejected as expected");
        }

        // Output:
        // toList() confirmed count: 3
        // Collectors.toList() count after add: 4
        // Collectors.toUnmodifiableList() count: 3
        // toList() is unmodifiable — add rejected as expected
    }
}