// Java 25+
// Feature shown: collectingAndThen downstream collector, final in Java 21+

/**
 * Listing 15.4b — CollectingAndThen.java
 * Demonstrates: collectingAndThen() to apply a finisher transformation
 * after collection — wrapping in unmodifiable map and counting filtered results
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

    record Order(String orderId, String region, double amount, String status) {}

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", "UK", 150.0, "CONFIRMED"),
            new Order("ORD-002", "US", 320.0, "CONFIRMED"),
            new Order("ORD-003", "UK", 275.0, "PENDING"),
            new Order("ORD-004", "US",  90.0, "CONFIRMED"),
            new Order("ORD-005", "EU", 410.0, "PENDING")
        );

        // Wrap the grouped result in an unmodifiable map
        Map<String, List<Order>> unmodifiableGroups =
            orders.stream()
                .collect(Collectors.collectingAndThen(
                    Collectors.groupingBy(Order::region),
                    Collections::unmodifiableMap)); // finisher applied after grouping

        LOG.info("Regions in unmodifiable map: " + unmodifiableGroups.keySet());

        // Verify the map is truly unmodifiable
        try {
            unmodifiableGroups.put("AU", List.of());
        } catch (UnsupportedOperationException e) {
            LOG.info("Map is unmodifiable — mutation rejected as expected");
        }

        // Collect to a list, then immediately get its size via finisher
        int confirmedCount =
            orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    List::size)); // finisher: list -> int

        LOG.info("Confirmed order count: " + confirmedCount);

        // Output:
        // Regions in unmodifiable map: [UK, US, EU]
        // Map is unmodifiable -- mutation rejected as expected
        // Confirmed order count: 3
    }
}