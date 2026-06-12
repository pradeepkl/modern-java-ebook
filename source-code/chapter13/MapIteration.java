// Java 8+
/**
 * Listing 13.4 — MapIteration.java
 * Demonstrates: Map.forEach, Map.replaceAll vs entrySet iteration
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter13;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MapIteration {

    private static final Logger log = Logger.getLogger(MapIteration.class.getName());

    public static void main(String[] args) {

        // Build a map of customer IDs to their order lists
        Map<String, List<String>> ordersByCustomer = new HashMap<>();
        ordersByCustomer.put("CUST-001", Arrays.asList("ORD-1", "ORD-2", "ORD-3"));
        ordersByCustomer.put("CUST-002", Arrays.asList("ORD-4", "ORD-5"));
        ordersByCustomer.put("CUST-003", Arrays.asList("ORD-6"));

        // NOT IDEAL: Pre-Java-8 entrySet iteration — ceremony dominates
        log.info("--- entrySet iteration (pre-Java-8 style) ---");
        for (Map.Entry<String, List<String>> entry
                : ordersByCustomer.entrySet()) {
            log.info(entry.getKey()
                    + " -> " + entry.getValue().size()); // boilerplate getKey/getValue
        }

        // Correct approach: Map.forEach — key and value directly
        log.info("--- Map.forEach (declarative) ---");
        ordersByCustomer.forEach((customerId, orderIds) ->
                log.info(customerId
                        + " -> " + orderIds.size())); // no entry wrapper needed

        // Build a mutable prices map for replaceAll demonstration
        Map<String, Double> prices = new HashMap<>();
        prices.put("PROD-A", 100.0);
        prices.put("PROD-B", 250.0);
        prices.put("PROD-C", 80.0);

        log.info("--- prices before replaceAll ---");
        prices.forEach((productId, price) ->
                log.info(productId + " = " + price));

        // Map.replaceAll — transform every value in place
        // key and current value → new value (10% discount applied)
        prices.replaceAll((productId, price) -> price * 0.9);

        log.info("--- prices after replaceAll (10% discount) ---");
        prices.forEach((productId, price) ->
                log.info(productId + " = " + price)); // all values reduced by 10%

        // Output:
        // --- entrySet iteration (pre-Java-8 style) ---
        // CUST-001 -> 3
        // CUST-002 -> 2
        // CUST-003 -> 1
        // --- Map.forEach (declarative) ---
        // CUST-001 -> 3
        // CUST-002 -> 2
        // CUST-003 -> 1
        // --- prices before replaceAll ---
        // PROD-A = 100.0  PROD-B = 250.0  PROD-C = 80.0
        // --- prices after replaceAll (10% discount) ---
        // PROD-A = 90.0   PROD-B = 225.0  PROD-C = 72.0
    }
}