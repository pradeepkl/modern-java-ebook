// Java 25+
// Feature shown: stream pipeline composition, final in Java 8+
/**
 * Listing 15.11 — PipelineComposition.java
 * Demonstrates: composing stream pipelines via method return values,
 *               parameterised predicates, and multi-level transformations
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PipelineComposition {

    private static final Logger log =
            Logger.getLogger(PipelineComposition.class.getName());

    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    record RegionReport(String region, long count,
                        double total, double average) {}

    // Returns a configured stream — caller applies terminal operation
    static Stream<Order> confirmedOrders(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"));
    }

    // Parameterised pipeline — caller supplies the filter criteria
    static Stream<Order> ordersMatching(List<Order> orders,
                                        Predicate<Order> criteria) {
        return orders.stream().filter(criteria);
    }

    // Composed pipeline — delegates to confirmedOrders, then collects
    static Map<String, Double> revenueByRegion(List<Order> orders) {
        return confirmedOrders(orders)
                .collect(Collectors.groupingBy(
                        Order::region,
                        Collectors.summingDouble(Order::amount)));
    }

    // Multi-level transformation: raw orders -> regional summary report
    static List<RegionReport> buildRegionalReport(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .collect(Collectors.groupingBy(Order::region))
                .entrySet().stream()
                .map(entry -> {
                    String region = entry.getKey();
                    List<Order> regionOrders = entry.getValue();
                    long count = regionOrders.size();
                    double total = regionOrders.stream()
                            .mapToDouble(Order::amount).sum();
                    double average = total / count;
                    return new RegionReport(region, count, total, average);
                })
                .sorted(Comparator.comparingDouble(RegionReport::total).reversed())
                .toList();
    }

    void main() {
        List<Order> orders = List.of(
                new Order("ORD-001", "C1", 499.99, "CONFIRMED", "UK"),
                new Order("ORD-002", "C2", 149.99, "CONFIRMED", "US"),
                new Order("ORD-003", "C1", 299.99, "CONFIRMED", "UK"),
                new Order("ORD-004", "C3",  99.99, "PENDING",   "EU"),
                new Order("ORD-005", "C2", 599.99, "CONFIRMED", "US"));

        List<RegionReport> report = buildRegionalReport(orders);

        // Log each regional summary line
        report.forEach(r -> log.info(
                r.region() + " | count=" + r.count()
                + " | total=" + r.total()
                + " | avg=" + r.average()));

        // Output:
        // UK | count=2 | total=799.98 | avg=399.99
        // US | count=2 | total=749.98 | avg=374.99
    }
}