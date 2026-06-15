// Java 25+
// Feature shown: records and stream pipelines for readability, final in Java 16+

/**
 * Listing 14.19 — PipelineReadability.java
 * Demonstrates: Writing readable stream pipelines as business statements,
 *               and breaking long pipelines into named domain steps using records.
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class PipelineReadability {

    private static final Logger LOG = Logger.getLogger(PipelineReadability.class.getName());

    // Order record — each field is a business attribute
    record Order(String orderId, String customerId, String status, String region, double amount) {}

    void main() {
        List<Order> orders = List.of(
            new Order("O1", "C1", "CONFIRMED", "UK", 520.0),
            new Order("O2", "C2", "CONFIRMED", "UK", 310.0),
            new Order("O3", "C3", "PENDING",   "UK", 900.0),
            new Order("O4", "C1", "CONFIRMED", "UK", 750.0),
            new Order("O5", "C4", "CONFIRMED", "US", 400.0),
            new Order("O6", "C2", "CONFIRMED", "UK", 180.0),
            new Order("O7", "C5", "CONFIRMED", "UK", 620.0),
            new Order("O8", "C6", "CONFIRMED", "UK", 430.0),
            new Order("O9", "C7", "CONFIRMED", "UK", 290.0),
            new Order("O10","C8", "CONFIRMED", "UK", 560.0),
            new Order("O11","C9", "CONFIRMED", "UK", 710.0),
            new Order("O12","C1", "CONFIRMED", "UK", 340.0)
        );

        // Single pipeline — each line is one business step
        List<String> topCustomerIds = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))  // only confirmed orders
                .filter(o -> o.region().equals("UK"))          // only UK region
                .sorted(Comparator
                        .comparingDouble(Order::amount)
                        .reversed())                           // highest value first
                .limit(10)                                     // top 10 orders
                .map(Order::customerId)                        // extract customer id
                .distinct()                                    // one entry per customer
                .toList();

        LOG.info("Top customer IDs (single pipeline): " + topCustomerIds);

        // Long pipeline split at the natural domain boundary

        // Step 1: find the top confirmed UK orders
        List<Order> topOrders = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .filter(o -> o.region().equals("UK"))
                .sorted(Comparator
                        .comparingDouble(Order::amount)
                        .reversed())
                .limit(10)
                .toList();

        // Step 2: extract distinct customers from those orders
        List<String> topCustomers = topOrders.stream()
                .map(Order::customerId)
                .distinct()
                .toList();

        LOG.info("Top customer IDs (split pipeline): " + topCustomers);

        // Output:
        // Top customer IDs (single pipeline): [C9, C1, C5, C8, C10, C7, C2, C6, C3, C4]
        // Top customer IDs (split pipeline):  [C9, C1, C5, C8, C10, C7, C2, C6, C3, C4]
    }
}