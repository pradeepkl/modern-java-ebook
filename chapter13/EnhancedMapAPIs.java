// Java 25+
// Feature shown: enhanced Map APIs (computeIfAbsent, computeIfPresent, merge, getOrDefault), final in Java 8+

/**
 * Listing 13.7 — EnhancedMapAPIs.java
 * Demonstrates: computeIfAbsent, computeIfPresent, merge, and getOrDefault
 * for declarative, intent-revealing Map mutation
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

public class EnhancedMapAPIs {

    private static final Logger LOG = Logger.getLogger(EnhancedMapAPIs.class.getName());

    record Order(String customerId, double amount) {}

    void main() {
        List<Order> orders = List.of(
                new Order("C01", 250.0),
                new Order("C01", 80.0),
                new Order("C02", 400.0),
                new Order("C01", 150.0),
                new Order("C02", 60.0)
        );

        Map<String, List<Order>> grouped = new HashMap<>();
        Map<String, Double> totalsByCustomer = new HashMap<>();

        for (Order order : orders) {
            String customerId = order.customerId();

            // computeIfAbsent — create-if-missing in one call
            // Returns the existing or newly created value
            grouped.computeIfAbsent(customerId,
                            id -> new ArrayList<>())
                    .add(order);

            // merge — accumulate totals by customer key
            // If absent: store initial value; if present: combine with Double::sum
            totalsByCustomer.merge(
                    order.customerId(),
                    order.amount(),
                    Double::sum);
        }

        LOG.info("Grouped orders: " + grouped);

        // computeIfPresent — update only if key exists; no-op if absent
        grouped.computeIfPresent("C01",
                (id, orderList) -> {
                    orderList.removeIf(o -> o.amount() < 100.0); // remove small orders
                    return orderList;
                });

        LOG.info("C01 orders after filter: " + grouped.get("C01"));

        // getOrDefault — safe retrieval with fallback value
        double total = totalsByCustomer.getOrDefault("C99", 0.0);
        LOG.info("Total for C99 (absent key): " + total);
        LOG.info("Total for C01: " + totalsByCustomer.getOrDefault("C01", 0.0));

        // Output:
        // Grouped orders: {C01=[Order[customerId=C01, amount=250.0], ...], C02=[...]}
        // C01 orders after filter: [Order[customerId=C01, amount=250.0], Order[customerId=C01, amount=150.0]]
        // Total for C99 (absent key): 0.0
        // Total for C01: 480.0
    }
}