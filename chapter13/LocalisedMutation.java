// Java 25+
// Feature shown: Map.merge for localised accumulation, final in Java 8+

/**
 * Listing 13.11 — LocalisedMutation.java
 * Demonstrates: Localised mutation using Map.merge versus shared
 * accumulation using getOrDefault/put in a manual loop.
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

public class LocalisedMutation {

    private static final Logger log =
            Logger.getLogger(LocalisedMutation.class.getName());

    record Order(String region, double amount) {}

    void main() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("UK",  99.99));
        orders.add(new Order("US", 149.99));
        orders.add(new Order("EU", 199.99));
        orders.add(new Order("UK",  49.99));
        orders.add(new Order("US",  79.99));

        // NOT IDEAL: shared accumulation — temporary variable scattered
        // across getOrDefault and put; mutation is spread over two lines
        Map<String, Double> totalsImperative = new HashMap<>();
        for (Order order : orders) {
            double current = totalsImperative
                    .getOrDefault(order.region(), 0.0); // read
            totalsImperative.put(order.region(),
                    current + order.amount());           // write
        }
        log.info("Imperative totals: " + totalsImperative);

        // Correct approach: localised — merge owns the accumulation entirely
        // No temporary variable. No scattered getOrDefault/put.
        Map<String, Double> totals = new HashMap<>();
        orders.forEach(order ->
                totals.merge(
                        order.region(),   // key
                        order.amount(),   // value if absent
                        Double::sum));    // merge function if present

        log.info("Localised totals: " + totals);

        // Output:
        // Imperative totals: {EU=199.99, US=229.98, UK=149.98}
        // Localised totals:  {EU=199.99, US=229.98, UK=149.98}
    }
}