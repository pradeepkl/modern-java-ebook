// Java 8+
/**
 * Listing 13.11 — LocalisedMutation.java
 * Demonstrates: Localised mutation using Map.merge() vs shared mutable state
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter13;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LocalisedMutation {

    private static final Logger log = Logger.getLogger(LocalisedMutation.class.getName());

    record Order(String region, double amount) {}

    public static void main(String[] args) {

        List<Order> orders = List.of(
                new Order("UK",  49.99),
                new Order("US",  99.99),
                new Order("EU", 199.99),
                new Order("UK",  99.99),
                new Order("US", 129.99)
        );

        // NOT IDEAL: Shared accumulation — mutable state shared across
        // the loop body and the temporary variable
        Map<String, Double> imperativeTotals = new HashMap<>();
        for (Order order : orders) {
            double current = imperativeTotals
                    .getOrDefault(order.region(), 0.0); // read scattered here
            imperativeTotals.put(order.region(),
                    current + order.amount());           // write scattered here
        }
        log.info("Imperative totals: " + imperativeTotals);

        // Correct approach: Localised — merge owns the accumulation entirely
        // No temporary variable. No scattered getOrDefault/put.
        Map<String, Double> totals = new HashMap<>();
        orders.forEach(order ->
                totals.merge(
                        order.region(),   // key
                        order.amount(),   // value if absent
                        Double::sum));    // merge function if present

        log.info("Declarative totals: " + totals);
        // Output: EU: 199.99, UK: 149.98, US: 229.98
    }
}