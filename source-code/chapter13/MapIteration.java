// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.4 — MapIteration.java
 * Demonstrates: Map.forEach and Map.replaceAll vs entrySet iteration
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MapIteration {

    private static final Logger log = Logger.getLogger(MapIteration.class.getName());

    void main() {
        // Build a map of customer IDs to their order ID lists
        Map<String, List<String>> ordersByCustomer = new HashMap<>();
        ordersByCustomer.put("C001", List.of("O1", "O2", "O3"));
        ordersByCustomer.put("C002", List.of("O4"));
        ordersByCustomer.put("C003", List.of("O5", "O6"));

        // NOT IDEAL: Pre-Java-8 entrySet iteration — ceremony dominates
        for (Map.Entry<String, List<String>> entry
                : ordersByCustomer.entrySet()) {
            log.info(entry.getKey()
                    + " -> " + entry.getValue().size()); // key and value via getters
        }

        // Correct approach: Map.forEach — key and value directly
        // No Map.Entry, no getKey()/getValue() boilerplate
        ordersByCustomer.forEach((customerId, orderIds) ->
                log.info(customerId
                        + " -> " + orderIds.size()));

        // Build a mutable prices map for replaceAll demonstration
        Map<String, Double> prices = new HashMap<>();
        prices.put("P001", 100.0);
        prices.put("P002", 250.0);
        prices.put("P003", 75.0);

        // Map.replaceAll — transform every value in place
        // key and current value -> new value (10% discount applied)
        prices.replaceAll((productId, price) -> price * 0.9);

        // Log the updated prices after replaceAll
        prices.forEach((productId, price) ->
                log.info(productId + " -> " + price));

        // Output:
        // C001 -> 3
        // C002 -> 1
        // C003 -> 2
        // C001 -> 3
        // C002 -> 1
        // C003 -> 2
        // P001 -> 90.0
        // P002 -> 225.0
        // P003 -> 67.5
    }
}