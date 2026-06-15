// Java 25+
// Feature shown: stream collectors (toMap variants), final in Java 10+

/**
 * Listing 15.5 — ToMap.java
 * Demonstrates: Collectors.toMap with key/value extractors, merge functions,
 *               and map factory for controlling implementation type
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ToMap {

    record Order(String orderId, String customerId, double amount, String status, String region) {}

    private static final Logger LOG = Logger.getLogger(ToMap.class.getName());

    void main() {
        List<Order> orders = List.of(
            new Order("O1", "C1", 250.00, "CONFIRMED", "NORTH"),
            new Order("O2", "C2", 180.50, "PENDING",   "SOUTH"),
            new Order("O3", "C1", 320.75, "CONFIRMED", "NORTH"),
            new Order("O4", "C3", 99.99,  "CANCELLED", "EAST")
        );

        // Basic toMap — key: orderId, value: amount
        Map<String, Double> amountByOrderId = orders.stream()
                .collect(Collectors.toMap(
                        Order::orderId,   // key extractor
                        Order::amount));  // value extractor
        LOG.info("amountByOrderId: " + amountByOrderId);

        // toMap with full object as value (identity value extractor)
        Map<String, Order> orderById = orders.stream()
                .collect(Collectors.toMap(
                        Order::orderId,
                        o -> o));         // whole object as value
        LOG.info("orderById keys: " + orderById.keySet());

        // Merge function — keep the higher-value order per customer
        Map<String, Order> highestByCustomer = orders.stream()
                .collect(Collectors.toMap(
                        Order::customerId,
                        o -> o,
                        (existing, replacement) ->
                                existing.amount() > replacement.amount()
                                        ? existing : replacement));
        LOG.info("highestByCustomer: " + highestByCustomer.get("C1").orderId()
                + " amount=" + highestByCustomer.get("C1").amount());

        // toMap with LinkedHashMap factory — preserves insertion order
        Map<String, Double> orderedMap = orders.stream()
                .sorted(Comparator.comparing(Order::orderId))
                .collect(Collectors.toMap(
                        Order::orderId,
                        Order::amount,
                        (a, b) -> a,           // no collision expected
                        LinkedHashMap::new));   // ordered map implementation
        LOG.info("orderedMap (LinkedHashMap): " + orderedMap);

        // toMap for string transformation — status lookup by orderId
        Map<String, String> statusByOrderId = orders.stream()
                .collect(Collectors.toMap(
                        Order::orderId,
                        Order::status));
        LOG.info("statusByOrderId: " + statusByOrderId);

        // Output:
        // amountByOrderId: {O1=250.0, O2=180.5, O3=320.75, O4=99.99}
        // orderById keys: [O1, O2, O3, O4]
        // highestByCustomer: O3 amount=320.75
        // orderedMap (LinkedHashMap): {O1=250.0, O2=180.5, O3=320.75, O4=99.99}
        // statusByOrderId: {O1=CONFIRMED, O2=PENDING, O3=CONFIRMED, O4=CANCELLED}
    }
}