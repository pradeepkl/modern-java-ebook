// Java 8+
/**
 * Listing 11.5 — EnhancedMapAPIs.java
 * Demonstrates: computeIfAbsent, computeIfPresent, merge, getOrDefault
 * Chapter 11: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter11;

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
                new Order("C01", 150.0),
                new Order("C99", 0.0)
        );

        Map<String, List<Order>> grouped = new HashMap<>();
        Map<String, Double> totalsByCustomer = new HashMap<>();

        for (Order order : orders) {
            String customerId = order.customerId();

            // ✅ computeIfAbsent — create-if-missing in one call
            grouped.computeIfAbsent(customerId, id -> new ArrayList<>())
                   .add(order);

            // merge — accumulate totals; absent: store amount, present: sum
            totalsByCustomer.merge(customerId, order.amount(), Double::sum);
        }

        // computeIfPresent — remove low-value orders only if key exists
        grouped.computeIfPresent("C01", (id, orderList) -> {
            orderList.removeIf(o -> o.amount() < 100.0); // removes 80.0
            return orderList;
        });

        // getOrDefault — safe retrieval with fallback for missing key
        double unknownTotal = totalsByCustomer.getOrDefault("C99", 0.0);

        LOG.info("Grouped orders: " + grouped);
        LOG.info("Totals by customer: " + totalsByCustomer);
        LOG.info("C01 orders after filter: " + grouped.get("C01").size());
        LOG.info("C99 total (getOrDefault): " + unknownTotal);

        // Output:
        // Grouped orders: {C01=[Order[C01,250.0], Order[C01,150.0]], C02=[...], C99=[...]}
        // Totals by customer: {C01=480.0, C02=400.0, C99=0.0}
        // C01 orders after filter: 2
        // C99 total (getOrDefault): 0.0
    }
}