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
import java.util.stream.Stream;
import java.util.logging.Logger;

public class ParallelSwitching {

    private static final Logger LOG = Logger.getLogger(ParallelSwitching.class.getName());

    record Order(String orderId, double amount, String status) {}

    private static List<Order> getOrders() {
        return List.of(
            new Order("O1", 150.0, "CONFIRMED"),
            new Order("O2",  50.0, "PENDING"),
            new Order("O3", 200.0, "CONFIRMED"),
            new Order("O4",  80.0, "SHIPPED"),
            new Order("O5", 300.0, "CONFIRMED")
        );
    }

    private static Order enrich(Order o) {
        return new Order(o.orderId(), o.amount() * 1.1, o.status());
    }

    void main() {
        List<Order> orders = getOrders();

        // parallel from source — parallelStream()
        List<Order> result1 = orders.parallelStream()
                .filter(o -> o.amount() > 100.0)
                .toList();
        LOG.info("parallelStream result size: " + result1.size());

        // switch to parallel mid-pipeline — same effect as above
        List<Order> result2 = orders.stream().parallel()
                .filter(o -> o.amount() > 100.0)
                .toList();
        LOG.info("stream().parallel() result size: " + result2.size());

        // .parallel() after filter — whole pipeline becomes parallel
        List<Order> result3 = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .parallel()          // entire pipeline runs in parallel
                .map(ParallelSwitching::enrich)
                .toList();
        LOG.info("parallel after filter result size: " + result3.size());

        // .sequential() overrides parallel — whole pipeline becomes sequential
        List<Order> result4 = orders.parallelStream()
                .filter(o -> o.amount() > 100.0)
                .sequential()        // entire pipeline reverts to sequential
                .toList();
        LOG.info("sequential() override result size: " + result4.size());

        // isParallel() — inspect execution mode before terminal operation
        Stream<Order> stream = orders.parallelStream();
        boolean running = stream.isParallel(); // true
        LOG.info("isParallel() on parallelStream: " + running);
        stream.close(); // close the inspected stream without consuming it

        // Output:
        // parallelStream result size: 3
        // stream().parallel() result size: 3
        // parallel after filter result size: 3
        // sequential() override result size: 3
        // isParallel() on parallelStream: true
    }
}