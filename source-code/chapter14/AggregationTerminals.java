// Java 25+
// Feature shown: stream aggregation terminals (count, sum, average, max, min, reduce), final in Java 8+

/**
 * Listing 14.11 — AggregationTerminals.java
 * Demonstrates: Stream aggregation terminals: count, sum, average, max, min, and reduce
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.logging.Logger;

public class AggregationTerminals {

    private static final Logger LOG = Logger.getLogger(AggregationTerminals.class.getName());

    record Order(String orderId, String status, String region, double amount) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "CONFIRMED", "NORTH", 250.00),
            new Order("ORD-002", "CONFIRMED", "SOUTH", 480.50),
            new Order("ORD-003", "PENDING",   "EAST",  120.75),
            new Order("ORD-004", "CONFIRMED", "WEST",  310.00),
            new Order("ORD-005", "CANCELLED", "NORTH",  95.00)
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // count — how many elements pass through
        // SQL equivalent: SELECT COUNT(*) FROM orders WHERE status = 'CONFIRMED'
        long confirmedCount = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .count();
        LOG.info("Confirmed count: " + confirmedCount);

        // sum via mapToDouble — numeric aggregation
        // SQL equivalent: SELECT SUM(amount) FROM orders
        double totalRevenue = orders.stream()
                .mapToDouble(Order::amount)
                .sum();
        LOG.info("Total revenue: " + totalRevenue);

        // average — SQL equivalent: SELECT AVG(amount)
        OptionalDouble avgOrder = orders.stream()
                .mapToDouble(Order::amount)
                .average();
        LOG.info("Average order: " + avgOrder.orElse(0.0));

        // max — SQL equivalent: SELECT MAX(amount)
        Optional<Order> highestOrder = orders.stream()
                .max(Comparator.comparingDouble(Order::amount));
        highestOrder.ifPresent(o -> LOG.info("Highest order: " + o.orderId() + " = " + o.amount()));

        // min — SQL equivalent: SELECT MIN(amount)
        Optional<Order> lowestOrder = orders.stream()
                .min(Comparator.comparingDouble(Order::amount));
        lowestOrder.ifPresent(o -> LOG.info("Lowest order: " + o.orderId() + " = " + o.amount()));

        // reduce — the foundation of all aggregation
        // identity: starting value; accumulator: how to combine two values
        double total = orders.stream()
                .mapToDouble(Order::amount)
                .reduce(0.0, Double::sum); // equivalent to sum()
        LOG.info("Reduce total: " + total);

        // reduce for non-numeric aggregation — concatenate all order IDs
        String allIds = orders.stream()
                .map(Order::orderId)
                .reduce("", (acc, id) -> acc.isEmpty() ? id : acc + "," + id);
        LOG.info("All IDs: " + allIds);

        // Output:
        // Confirmed count: 3
        // Total revenue: 1256.25
        // Average order: 251.25
        // Highest order: ORD-002 = 480.5
        // Lowest order: ORD-005 = 95.0
        // Reduce total: 1256.25
        // All IDs: ORD-001,ORD-002,ORD-003,ORD-004,ORD-005
    }
}