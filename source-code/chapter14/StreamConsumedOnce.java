// Java 25+
// Feature shown: stream single-use consumption and IllegalStateException, final in Java 8+

/**
 * Listing 14.20 — StreamConsumedOnce.java
 * Demonstrates: A stream can only be consumed once; reuse throws IllegalStateException
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class StreamConsumedOnce {

    private static final Logger LOG = Logger.getLogger(StreamConsumedOnce.class.getName());

    record Order(String status, String region, double amount, String customerId) {}

    void main() {
        List<Order> orders = List.of(
            new Order("CONFIRMED", "UK", 120.0, "C1"),
            new Order("CONFIRMED", "US", 80.0,  "C2"),
            new Order("PENDING",   "UK", 50.0,  "C3"),
            new Order("CONFIRMED", "UK", 200.0, "C4")
        );

        // Demonstrate that reusing an exhausted stream throws IllegalStateException
        Stream<Order> stream = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"));

        long count = stream.count(); // terminal operation — stream is now exhausted
        LOG.info("Count of CONFIRMED orders: " + count);

        try {
            // Attempting a second terminal operation on the same stream
            List<Order> list = stream.toList(); // IllegalStateException expected
            LOG.info("List size: " + list.size());
        } catch (IllegalStateException e) {
            LOG.warning("Stream already consumed — caught: " + e.getClass().getSimpleName());
        }

        // Correct approach: create a fresh stream for each terminal operation
        long count2 = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .count();
        LOG.info("Fresh stream count: " + count2);

        List<Order> list2 = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList();
        LOG.info("Fresh stream list size: " + list2.size());

        // Output:
        // Count of CONFIRMED orders: 3
        // Stream already consumed — caught: IllegalStateException
        // Fresh stream count: 3
        // Fresh stream list size: 3
    }
}