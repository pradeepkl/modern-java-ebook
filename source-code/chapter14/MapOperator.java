// Java 25+
// Feature shown: stream map operator (one-to-one transformation), final in Java 8+

/**
 * Listing 14.6 — MapOperator.java
 * Demonstrates: stream map() for one-to-one element transformation,
 *               including type changes, field extraction, and chained maps
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;

public class MapOperator {

    private static final Logger LOG = Logger.getLogger(MapOperator.class.getName());

    record Order(String orderId, String region, double amount, String status) {}

    record OrderSummary(String orderId, double amount) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("O1", "north", 820.00, "CONFIRMED"),
            new Order("O2", "south", 450.00, "PENDING"),
            new Order("O3", "east",  610.00, "CONFIRMED"),
            new Order("O4", "west",  390.00, "CONFIRMED"),
            new Order("O5", "north", 275.00, "CANCELLED")
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // String -> String: extract region then uppercase
        List<String> upperRegions = orders.stream()
                .map(Order::region)
                .map(String::toUpperCase)
                .toList();
        LOG.info("Upper regions: " + upperRegions);

        // Order -> OrderSummary: type change via constructor
        List<OrderSummary> summaries = orders.stream()
                .map(o -> new OrderSummary(o.orderId(), o.amount()))
                .toList();
        LOG.info("Summaries: " + summaries);

        // Order -> Double: extract a numeric field
        List<Double> amounts = orders.stream()
                .map(Order::amount)
                .toList();
        LOG.info("Amounts: " + amounts);

        // Chained maps: filter then format confirmed amounts
        List<String> formattedAmounts = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .map(Order::amount)
                .map(amount -> String.format("%.2f GBP", amount))
                .toList();
        LOG.info("Formatted confirmed amounts: " + formattedAmounts);

        // Output:
        // Upper regions: [NORTH, SOUTH, EAST, WEST, NORTH]
        // Summaries: [OrderSummary[orderId=O1, amount=820.0], ...]
        // Amounts: [820.0, 450.0, 610.0, 390.0, 275.0]
        // Formatted confirmed amounts: [820.00 GBP, 610.00 GBP, 390.00 GBP]
    }
}