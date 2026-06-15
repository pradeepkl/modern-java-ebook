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
import java.util.stream.Stream;

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
        List<Order> merged = Stream.of(
                ukOrders.stream(),
                usOrders.stream(),
                getEUOrders().stream())
                .flatMap(s -> s)                          // flatten each sub-stream
                .sorted(Comparator.comparing(Order::orderId))
                .toList();

        merged.forEach(o ->
            LOG.info(o.orderId() + " | " + o.region() + " | " + o.amount())
        );

        LOG.info("Total orders merged: " + merged.size());

        // Output:
        // EU-002 | EU | 180.0
        // EU-004 | EU | 450.0
        // UK-001 | UK | 120.0
        // UK-002 | UK | 340.0
        // US-001 | US | 210.0
        // US-003 | US | 95.0
        // Total orders merged: 6
    }
}