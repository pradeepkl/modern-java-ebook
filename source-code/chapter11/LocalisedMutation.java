// Java 8+
/**
 * Listing 11.8 — LocalisedMutation.java
 * Demonstrates: Localised mutation using Map.merge() vs shared accumulation
 * Chapter 11: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LocalisedMutation {

    private static final Logger log = Logger.getLogger(LocalisedMutation.class.getName());

    record Order(String region, double amount) {}

    public static void main(String[] args) {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order("UK", 99.99));
        orders.add(new Order("US", 149.99));
        orders.add(new Order("EU", 199.99));
        orders.add(new Order("UK", 49.99));
        orders.add(new Order("US", 79.99));

        // ❌ Shared accumulation — mutable state shared across
        // the loop body and the temporary variable
        Map<String, Double> imperativeTotals = new HashMap<>();
        for (Order order : orders) {
            double current = imperativeTotals
                    .getOrDefault(order.region(), 0.0); // read scattered here
            imperativeTotals.put(order.region(),
                    current + order.amount());          // write scattered here
        }
        log.info("Imperative totals: " + imperativeTotals);

        // ✅ Localised — merge owns the accumulation entirely
        // No temporary variable. No scattered getOrDefault/put.
        Map<String, Double> totals = new HashMap<>();
        orders.forEach(order ->
                totals.merge(
                        order.region(),   // key
                        order.amount(),   // value if absent
                        Double::sum));    // merge function if present

        // Log each region total in a declarative style
        totals.forEach((region, total) ->
                log.info(region + ": " + total));

        // Output: UK: 149.98, US: 229.98, EU: 199.99
    }
}