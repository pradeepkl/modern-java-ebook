// Java 25+
// Feature shown: stream terminal operators with toList(), final in Java 16+

/**
 * Listing 14.10 — CollectionTerminals.java
 * Demonstrates: Stream terminal operators that collect results into collections,
 *               comparing toList(), Collectors.toList(), and Collectors.toUnmodifiableList()
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public class CollectionTerminals {

    private static final Logger log = Logger.getLogger(CollectionTerminals.class.getName());

    record Order(String orderId, String status, String region, double amount) {}

    void main() {
        List<Order> orders = List.of(
            new Order("A1", "CONFIRMED", "NORTH", 250.0),
            new Order("A2", "PENDING",   "SOUTH", 100.0),
            new Order("A3", "CONFIRMED", "EAST",  400.0),
            new Order("A4", "CANCELLED", "WEST",  75.0),
            new Order("A5", "CONFIRMED", "NORTH", 320.0)
        );

        // toList() — unmodifiable list, Java 16+
        List<Order> confirmed = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList(); // unmodifiable — no add/remove allowed

        log.info("toList() confirmed count: " + confirmed.size());

        // Collectors.toList() — returns a mutable ArrayList
        List<Order> mutableList = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.toList()); // mutable — can add/remove

        mutableList.add(new Order("X9", "CONFIRMED", "SOUTH", 50.0));
        log.info("Collectors.toList() after add: " + mutableList.size());

        // Collectors.toUnmodifiableList() — unmodifiable, available since Java 10+
        List<Order> unmodifiable = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.toUnmodifiableList());

        log.info("Collectors.toUnmodifiableList() count: " + unmodifiable.size());

        // Verify toList() result is truly unmodifiable
        try {
            confirmed.add(new Order("Z1", "CONFIRMED", "WEST", 10.0));
        } catch (UnsupportedOperationException e) {
            log.info("toList() is unmodifiable — add rejected as expected");
        }

        // Output:
        // INFO: toList() confirmed count: 3
        // INFO: Collectors.toList() after add: 4
        // INFO: Collectors.toUnmodifiableList() count: 3
        // INFO: toList() is unmodifiable — add rejected as expected
    }
}