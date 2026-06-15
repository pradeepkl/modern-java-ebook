// Java 25+
// Feature shown: parallel and sequential stream switching, final in Java 8+

/**
 * Listing 15.8 — ParallelSwitching.java
 * Demonstrates: switching between parallel and sequential stream execution modes
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ParallelSwitching {

    private static final Logger LOG = Logger.getLogger(ParallelSwitching.class.getName());

    record Order(String orderId, double amount, String status) {}

    private static List<Order> getOrders() {
        return List.of(
            new Order("O1", 50.0,  "PENDING"),
            new Order("O2", 150.0, "CONFIRMED"),
            new Order("O3", 200.0, "CONFIRMED"),
            new Order("O4", 75.0,  "SHIPPED"),
            new Order("O5", 300.0, "CONFIRMED")
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // parallel from source using parallelStream()
        List<Order> result1 = orders.parallelStream()
                .filter(o -> o.amount() > 100.0)
                .toList();
        LOG.info("parallelStream() result count: " + result1.size());

        // switch to parallel mid-pipeline — applies to entire pipeline
        List<Order> result2 = orders.stream()
                .parallel()                          // whole pipeline becomes parallel
                .filter(o -> o.amount() > 100.0)
                .toList();
        LOG.info("stream().parallel() result count: " + result2.size());

        // .parallel() after intermediate ops — still applies to entire pipeline
        List<Order> result3 = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .parallel()                          // whole pipeline becomes parallel
                .map(o -> new Order(o.orderId(), o.amount() * 1.1, o.status()))
                .toList();
        LOG.info("parallel() after filter result count: " + result3.size());

        // .sequential() switches entire pipeline back to sequential
        List<Order> result4 = orders.parallelStream()
                .filter(o -> o.amount() > 100.0)
                .sequential()                        // whole pipeline becomes sequential
                .toList();
        LOG.info("sequential() result count: " + result4.size());

        // isParallel() — inspect current execution mode before terminal op
        Stream<Order> stream = orders.parallelStream();
        boolean running = stream.isParallel();       // true — parallel mode active
        LOG.info("isParallel() on parallelStream(): " + running);
        stream.close();

        // Output:
        // parallelStream() result count: 3
        // stream().parallel() result count: 3
        // parallel() after filter result count: 3
        // sequential() result count: 3
        // isParallel() on parallelStream(): true
    }
}