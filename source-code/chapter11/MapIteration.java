// Java 8+
/**
 * Listing 11.3 — MapIteration.java
 * Demonstrates: Map.forEach vs entrySet iteration, and Map.replaceAll
 * Chapter 11: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MapIteration {

    private static final Logger log = Logger.getLogger(MapIteration.class.getName());

    public static void main(String[] args) {

        // Build a map of customer IDs to their order IDs
        Map<String, List<String>> ordersByCustomer = new HashMap<>();
        ordersByCustomer.put("CUST-001", Arrays.asList("ORD-1", "ORD-2", "ORD-3"));
        ordersByCustomer.put("CUST-002", Arrays.asList("ORD-4"));
        ordersByCustomer.put("CUST-003", Arrays.asList("ORD-5", "ORD-6"));

        // ❌ Pre-Java-8 entrySet iteration — ceremony dominates
        log.info("--- entrySet iteration ---");
        for (Map.Entry<String, List<String>> entry
                : ordersByCustomer.entrySet()) {
            log.info(entry.getKey()           // key extracted via getKey()
                    + " -> " + entry.getValue().size()); // value via getValue()
        }

        // ✅ Map.forEach — key and value directly, no Entry boilerplate
        log.info("--- Map.forEach ---");
        ordersByCustomer.forEach((customerId, orderIds) ->
                log.info(customerId            // key bound directly
                        + " -> " + orderIds.size())); // value bound directly

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
                log.info(productId + " = " + price));

        // Output:
        // --- entrySet iteration ---
        // CUST-001 -> 3
        // CUST-002 -> 1
        // CUST-003 -> 2
        // --- Map.forEach ---
        // CUST-001 -> 3
        // CUST-002 -> 1
        // CUST-003 -> 2
        // --- prices before replaceAll ---
        // PROD-A = 100.0  PROD-B = 250.0  PROD-C = 80.0
        // --- prices after replaceAll (10% discount) ---
        // PROD-A = 90.0   PROD-B = 225.0  PROD-C = 72.0
    }
}