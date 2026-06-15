// Java 25+
// Feature shown: stream flatMap for merging multiple streams, final in Java 8+

/**
 * Listing 15.7b — StreamMerge.java
 * Demonstrates: merging more than two streams using flatMap over a Stream of streams
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.List;
import java.util.Comparator;
import java.util.logging.Logger;

public class StreamMerge {

    private static final Logger LOG = Logger.getLogger(StreamMerge.class.getName());

    record Order(String orderId, String region, double amount) {}

    // Simulated data sources
    static List<Order> getUKOrders() {
        return List.of(
            new Order("UK-001", "UK", 120.0),
            new Order("UK-002", "UK", 340.0)
        );
    }

    static List<Order> getUSOrders() {
        return List.of(
            new Order("US-001", "US", 210.0),
            new Order("US-003", "US", 95.0)
        );
    }

    static List<Order> getEUOrders() {
        return List.of(
            new Order("EU-002", "EU", 180.0),
            new Order("EU-004", "EU", 450.0)
        );
    }

    void main() {
        List<Order> ukOrders = getUKOrders();
        List<Order> usOrders = getUSOrders();

        // flatMap over a Stream of streams — cleaner than nested Stream.concat
        // for three or more sources; SQL equivalent: UNION ALL across regions
        List<Order> merged = java.util.stream.Stream.of(
                ukOrders.stream(),
                usOrders.stream(),
                getEUOrders().stream())
                .flatMap(s -> s)                              // flatten each sub-stream
                .sorted(Comparator.comparing(Order::orderId)) // stable ordering
                .toList();

        merged.forEach(o ->
            LOG.info(String.format("orderId=%s region=%s amount=%.2f",
                o.orderId(), o.region(), o.amount()))
        );

        LOG.info("Total orders merged: " + merged.size());

        // Output:
        // orderId=EU-002 region=EU amount=180.00
        // orderId=EU-004 region=EU amount=450.00
        // orderId=UK-001 region=UK amount=120.00
        // orderId=UK-002 region=UK amount=340.00
        // orderId=US-001 region=US amount=210.00
        // orderId=US-003 region=US amount=95.00
        // Total orders merged: 6
    }
}