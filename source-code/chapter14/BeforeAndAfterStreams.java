// Java 25+
// Feature shown: stream pipelines vs imperative loops, final in Java 8+

/**
 * Listing 14.1 — BeforeAndAfterStreams.java
 * Demonstrates: Imperative loop vs declarative stream pipeline for
 * filtering and aggregating domain objects
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;

public class BeforeAndAfterStreams {

    private static final Logger LOG =
            Logger.getLogger(BeforeAndAfterStreams.class.getName());

    record Order(String orderId,
                 String customerId,
                 double amount,
                 int quantity,
                 String status,
                 String region) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("O1", "C1", 250.0, 2, "CONFIRMED", "UK"),
            new Order("O2", "C2",  80.0, 1, "CONFIRMED", "UK"),  // below threshold
            new Order("O3", "C3", 150.0, 3, "PENDING",   "UK"),  // wrong status
            new Order("O4", "C4", 300.0, 1, "CONFIRMED", "DE"),  // wrong region
            new Order("O5", "C5", 120.0, 4, "CONFIRMED", "UK")
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // Imperative: loop manages cursor, conditions, and accumulation
        // Business intent is buried inside control flow
        double totalRevenue = 0.0;
        for (Order order : orders) {
            if (order.status().equals("CONFIRMED")
                    && order.region().equals("UK")
                    && order.amount() > 100.0) {
                totalRevenue += order.amount();  // manual accumulation
            }
        }

        // Declarative: pipeline reads as a business statement
        // From orders — keep confirmed — keep UK — keep above 100 — sum amounts
        double totalRevenue2 = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))  // keep confirmed
                .filter(o -> o.region().equals("UK"))         // keep UK
                .filter(o -> o.amount() > 100.0)              // keep above threshold
                .mapToDouble(Order::amount)                   // extract primitive double
                .sum();                                       // terminal: aggregate

        LOG.info("Imperative total revenue:   " + totalRevenue);
        LOG.info("Declarative total revenue:  " + totalRevenue2);
        LOG.info("Results match: " + (totalRevenue == totalRevenue2));

        // Output:
        // Imperative total revenue:   620.0
        // Declarative total revenue:  620.0
        // Results match: true
    }
}