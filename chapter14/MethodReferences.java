// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 14.18 — MethodReferences.java
 * Demonstrates: lambda vs method reference in stream pipelines
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;

public class MethodReferences {

    private static final Logger log = Logger.getLogger(MethodReferences.class.getName());

    record Order(long orderId, String region, double amount, int quantity, String status) {}

    void main() {
        double threshold = 150.0;

        List<Order> orders = List.of(
            new Order(1L, "north", 200.0, 3, "CONFIRMED"),
            new Order(2L, "south", 80.0,  1, "PENDING"),
            new Order(3L, "east",  320.5, 5, "CONFIRMED"),
            new Order(4L, "north", 175.0, 2, "CONFIRMED"),
            new Order(5L, "west",  50.0,  1, "CANCELLED")
        );

        // Lambda — explicit but verbose
        List<Long> idsLambda = orders.stream()
                .map(o -> o.orderId())          // explicit lambda
                .toList();
        log.info("IDs via lambda: " + idsLambda);

        // Method reference — reads as the data flow
        List<Long> idsRef = orders.stream()
                .map(Order::orderId)            // Order -> long (autoboxed)
                .toList();
        log.info("IDs via method ref: " + idsRef);

        // Chained instance method references
        List<String> regions = orders.stream()
                .map(Order::region)             // Order -> String
                .map(String::toUpperCase)       // String -> String
                .distinct()
                .sorted()
                .toList();
        log.info("Distinct regions: " + regions);

        // Lambda preferred — compound predicate cannot be a method reference
        List<Order> confirmed = orders.stream()
                .filter(o -> o.amount() > threshold
                        && o.status().equals("CONFIRMED"))
                .toList();
        log.info("Confirmed above threshold: " + confirmed.size());

        // Static method reference: Math::round (Double -> Long)
        List<Long> rounded = orders.stream()
                .map(Order::amount)             // Order -> Double
                .map(Math::round)               // Double -> Long
                .toList();
        log.info("Rounded amounts: " + rounded);

        // Output:
        // IDs via lambda: [1, 2, 3, 4, 5]
        // IDs via method ref: [1, 2, 3, 4, 5]
        // Distinct regions: [EAST, NORTH, SOUTH, WEST]
        // Confirmed above threshold: 2
        // Rounded amounts: [200, 80, 321, 175, 50]
    }
}