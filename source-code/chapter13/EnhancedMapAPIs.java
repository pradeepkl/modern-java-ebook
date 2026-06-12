// Java 8+
/**
 * Listing 13.7 — EnhancedMapAPIs.java
 * Demonstrates: computeIfAbsent, computeIfPresent, merge, getOrDefault
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
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

    public static void main(String[] args) {

        List<Order> orders = List.of(
                new Order("C01", 250.0),
                new Order("C01", 80.0),
                new Order("C02", 400.0),
                new Order("C02", 150.0),
                new Order("C99", 0.0)
        );

        Map<String, List<Order>> grouped = new HashMap<>();
        Map<String, Double> totalsByCustomer = new HashMap<>();

        for (Order order : orders) {
            String customerId = order.customerId();

            // computeIfAbsent — create list if missing, then add in one call
            grouped.computeIfAbsent(customerId,
                            id -> new ArrayList<>())
                    .add(order);

            // merge — accumulate totals: absent → store amount, present → sum
            totalsByCustomer.merge(
                    customerId,
                    order.amount(),
                    Double::sum);
        }

        // computeIfPresent — remove low-value orders only if key exists
        grouped.computeIfPresent("C01",
                (id, orderList) -> {
                    orderList.removeIf(o -> o.amount() < 100.0);
                    return orderList; // returning null would remove the key
                });

        // getOrDefault — safe retrieval with fallback for absent key
        double total = totalsByCustomer.getOrDefault("C99", 0.0);

        LOG.info("Grouped orders: " + grouped);
        LOG.info("Totals by customer: " + totalsByCustomer);
        LOG.info("Total for C99 (getOrDefault): " + total);

        // Output:
        // Grouped orders: {C01=[Order[customerId=C01, amount=250.0]], C02=[...]}
        // Totals by customer: {C01=330.0, C02=550.0, C99=0.0}
        // Total for C99 (getOrDefault): 0.0
    }
}