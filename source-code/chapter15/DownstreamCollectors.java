// Java 25+
// Feature shown: downstream collectors (counting, summingDouble, mapping, filtering, teeing), final in Java 21+

/**
 * Listing 15.4 — DownstreamCollectors.java
 * Demonstrates: downstream collectors composed inside groupingBy —
 *   counting(), summingDouble(), averagingDouble(), mapping(),
 *   filtering() (Java 9+), and teeing() (Java 12+)
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DownstreamCollectors {

    private static final Logger LOG = Logger.getLogger(DownstreamCollectors.class.getName());

    record Order(String orderId, String region, double amount) {}
    record RegionStats(long count, double total) {}

    void main() {
        List<Order> orders = List.of(
                new Order("ORD-001", "UK", 150.0),
                new Order("ORD-002", "US", 300.0),
                new Order("ORD-003", "UK", 450.0),
                new Order("ORD-004", "EU", 100.0)
        );

        // counting() — count per group
        Map<String, Long> countPerRegion = orders.stream()
                .collect(Collectors.groupingBy(Order::region, Collectors.counting()));
        LOG.info("Count per region: " + countPerRegion);

        // summingDouble() — sum per group
        Map<String, Double> sumPerRegion = orders.stream()
                .collect(Collectors.groupingBy(Order::region,
                        Collectors.summingDouble(Order::amount)));
        LOG.info("Sum per region: " + sumPerRegion);

        // averagingDouble() — average per group
        Map<String, Double> avgPerRegion = orders.stream()
                .collect(Collectors.groupingBy(Order::region,
                        Collectors.averagingDouble(Order::amount)));
        LOG.info("Avg per region: " + avgPerRegion);

        // mapping() — transform elements before collecting into each group
        Map<String, List<String>> idsByRegion = orders.stream()
                .collect(Collectors.groupingBy(Order::region,
                        Collectors.mapping(Order::orderId, Collectors.toList())));
        LOG.info("IDs by region: " + idsByRegion);

        // filtering() — filter within each group (Java 9+)
        // Preserves all group keys even when filtered group is empty
        Map<String, List<Order>> highValueByRegion = orders.stream()
                .collect(Collectors.groupingBy(Order::region,
                        Collectors.filtering(o -> o.amount() > 200.0, Collectors.toList())));
        LOG.info("High-value by region: " + highValueByRegion);

        // teeing() — two collectors in one pass (Java 12+)
        RegionStats ukStats = orders.stream()
                .filter(o -> o.region().equals("UK"))
                .collect(Collectors.teeing(
                        Collectors.counting(),
                        Collectors.summingDouble(Order::amount),
                        RegionStats::new));
        LOG.info("UK stats — count=" + ukStats.count() + ", total=" + ukStats.total());

        // Output:
        // Count per region: {EU=1, UK=2, US=1}
        // Sum per region: {EU=100.0, UK=600.0, US=300.0}
        // Avg per region: {EU=100.0, UK=300.0, US=300.0}
        // IDs by region: {EU=[ORD-004], UK=[ORD-001, ORD-003], US=[ORD-002]}
        // High-value by region: {EU=[], UK=[ORD-003], US=[ORD-002]}
        // UK stats — count=2, total=600.0
    }
}