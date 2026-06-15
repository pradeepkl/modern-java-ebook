// Java 25+
// Feature shown: enhanced Map APIs (computeIfAbsent, computeIfPresent, merge, getOrDefault), final in Java 8+

/**
 * Listing 13.7 — EnhancedMapAPIs.java
 * Demonstrates: computeIfAbsent, computeIfPresent, merge, and getOrDefault
 * on java.util.Map for declarative, intent-revealing mutation.
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
                new Order("C01",  80.0),
                new Order("C02", 400.0),
                new Order("C01", 150.0),
                new Order("C02",  60.0)
        );

        // computeIfAbsent — create-if-missing in one call
        Map<String, List<Order>> grouped = new HashMap<>();
        for (Order order : orders) {
            grouped.computeIfAbsent(order.customerId(),
                            id -> new ArrayList<>())   // list created only when absent
                    .add(order);
        }
        LOG.info("Grouped keys: " + grouped.keySet());

        // computeIfPresent — update only if key exists; no-op when absent
        grouped.computeIfPresent("C01",
                (id, orderList) -> {
                    orderList.removeIf(o -> o.amount() < 100.0); // drop small orders
                    return orderList;
                });
        LOG.info("C01 after filter (amounts >= 100): " + grouped.get("C01").stream()
                .map(Order::amount).toList());

        // merge — accumulate totals by customer key
        Map<String, Double> totalsByCustomer = new HashMap<>();
        for (Order order : orders) {
            totalsByCustomer.merge(
                    order.customerId(),
                    order.amount(),
                    Double::sum);              // combine existing + new amount
        }
        LOG.info("Totals: " + totalsByCustomer);

        // getOrDefault — safe retrieval with fallback; no null check needed
        double total = totalsByCustomer.getOrDefault("C99", 0.0);
        LOG.info("Total for unknown customer C99: " + total);

        // Output:
        // Grouped keys: [C01, C02]
        // C01 after filter (amounts >= 100): [250.0, 150.0]
        // Totals: {C01=480.0, C02=460.0}
        // Total for unknown customer C99: 0.0
    }
}