// Java 25+
// Feature shown: stream collectors toMap variants, final in Java 10+

/**
 * Listing 15.5 — ToMap.java
 * Demonstrates: Collectors.toMap with key extractor, value extractor,
 *               merge function, and map factory overloads
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

    private static final Logger LOG = Logger.getLogger(ToMap.class.getName());

    record Order(String orderId, String customerId, double amount,
                 String status, String region) {}

    void main() {
        List<Order> orders = List.of(
            new Order("O1", "C1", 250.0, "CONFIRMED", "NORTH"),
            new Order("O2", "C2", 180.0, "PENDING",   "SOUTH"),
            new Order("O3", "C1", 320.0, "CONFIRMED", "NORTH"),
            new Order("O4", "C3", 95.0,  "CANCELLED", "EAST")
        );

        // Basic toMap — key extractor, value extractor
        Map<String, Double> amountByOrderId = orders.stream()
                .collect(Collectors.toMap(
                        Order::orderId,   // key
                        Order::amount));  // value
        LOG.info("amountByOrderId: " + amountByOrderId);

        // toMap with full object as value (identity)
        Map<String, Order> orderById = orders.stream()
                .collect(Collectors.toMap(
                        Order::orderId,
                        o -> o));         // whole object as value
        LOG.info("orderById keys: " + orderById.keySet());

        // Merge function — keep highest-value order per customer
        Map<String, Order> highestByCustomer = orders.stream()
                .collect(Collectors.toMap(
                        Order::customerId,
                        o -> o,
                        (existing, replacement) ->
                                existing.amount() > replacement.amount()
                                        ? existing : replacement));
        LOG.info("highestByCustomer C1 amount: "
                + highestByCustomer.get("C1").amount());

        // toMap with map factory — LinkedHashMap preserves insertion order
        Map<String, Double> orderedMap = orders.stream()
                .sorted(Comparator.comparing(Order::orderId))
                .collect(Collectors.toMap(
                        Order::orderId,
                        Order::amount,
                        (a, b) -> a,           // no collision expected
                        LinkedHashMap::new));   // ordered result
        LOG.info("orderedMap (insertion order): " + orderedMap);

        // toMap for transformation — extract status by order ID
        Map<String, String> statusByOrderId = orders.stream()
                .collect(Collectors.toMap(
                        Order::orderId,
                        Order::status));
        LOG.info("statusByOrderId: " + statusByOrderId);

        // Output:
        // amountByOrderId: {O1=250.0, O2=180.0, O3=320.0, O4=95.0}
        // orderById keys: [O1, O2, O3, O4]
        // highestByCustomer C1 amount: 320.0
        // orderedMap (insertion order): {O1=250.0, O2=180.0, O3=320.0, O4=95.0}
        // statusByOrderId: {O1=CONFIRMED, O2=PENDING, O3=CONFIRMED, O4=CANCELLED}
    }
}