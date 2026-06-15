// Java 25+
// Feature shown: collectingAndThen downstream collector, final in Java 21+

/**
 * Listing 15.4b — CollectingAndThen.java
 * Demonstrates: collectingAndThen() to apply a finisher transformation
 * after collection, including wrapping in unmodifiable map and
 * collecting to list then applying a non-trivial finisher function.
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CollectingAndThen {

    private static final Logger LOG = Logger.getLogger(CollectingAndThen.class.getName());

    record Order(String orderId, String region, String status, double amount) {}

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", "UK", "CONFIRMED", 150.0),
            new Order("ORD-002", "US", "CONFIRMED", 320.0),
            new Order("ORD-003", "UK", "PENDING",   210.0),
            new Order("ORD-004", "US", "PENDING",    80.0),
            new Order("ORD-005", "UK", "CONFIRMED", 175.0)
        );

        // Wrap the grouped result in an unmodifiable map
        Map<String, List<Order>> unmodifiableGroups =
            orders.stream()
                .collect(Collectors.collectingAndThen(
                    Collectors.groupingBy(Order::region),
                    Collections::unmodifiableMap)); // finisher wraps result

        LOG.info("Regions in unmodifiable map: " + unmodifiableGroups.keySet());

        // Verify the map is truly unmodifiable
        try {
            unmodifiableGroups.put("EU", List.of());
        } catch (UnsupportedOperationException e) {
            LOG.info("Map is unmodifiable — mutation rejected as expected");
        }

        // Collect to a list, then immediately get its size via finisher
        int confirmedCount =
            orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    List::size)); // finisher transforms List -> int

        LOG.info("Confirmed order count: " + confirmedCount);

        // Collect to list, then apply a non-trivial finisher: sum amounts
        double totalUkAmount =
            orders.stream()
                .filter(o -> o.region().equals("UK"))
                .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> list.stream().mapToDouble(Order::amount).sum()));

        LOG.info("Total UK amount: " + totalUkAmount);

        // Output:
        // Regions in unmodifiable map: [UK, US]
        // Map is unmodifiable -- mutation rejected as expected
        // Confirmed order count: 3
        // Total UK amount: 535.0
    }
}