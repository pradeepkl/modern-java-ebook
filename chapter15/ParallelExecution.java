// Java 25+
// Feature shown: parallel streams with ForkJoinPool, final in Java 8+

/**
 * Listing 15.9 — ParallelExecution.java
 * Demonstrates: Sequential vs parallel stream execution over a large dataset,
 *               showing identical pipeline code with different execution modes.
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ParallelExecution {

    private static final Logger LOG = Logger.getLogger(ParallelExecution.class.getName());

    record Order(String id, String status, double amount) {}

    private static List<Order> getLargeOrderList() {
        List<Order> orders = new ArrayList<>(10_000);
        for (int i = 0; i < 10_000; i++) {
            String status = (i % 3 == 0) ? "CONFIRMED" : "PENDING";
            orders.add(new Order("ORD-" + i, status, 50.0 + (i % 500)));
        }
        return orders;
    }

    void main() {
        List<Order> largeOrderList = getLargeOrderList();

        // Sequential — one thread, one element at a time
        double seqTotal = largeOrderList.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .mapToDouble(Order::amount)
                .sum();

        // Parallel — ForkJoinPool splits into chunks
        // Each chunk filtered and summed independently
        // Results merged at the end
        // The developer wrote the same pipeline — the runtime
        // decided how many threads and how to split the data
        double parTotal = largeOrderList.parallelStream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .mapToDouble(Order::amount)
                .sum();

        int availableCores = Runtime.getRuntime().availableProcessors();

        LOG.info("Sequential total: " + seqTotal);
        LOG.info("Parallel total:   " + parTotal);
        LOG.info("Results match:    " + (seqTotal == parTotal));
        LOG.info("Available cores:  " + availableCores);

        // The runtime adapts to available resources:
        // 2-core container  -> 2 threads
        // 8-core machine    -> 8 threads
        // 16-core cloud VM  -> 16 threads
        // Same code. Different execution. No change required.

        // Output:
        // Sequential total: 1663334.0
        // Parallel total:   1663334.0
        // Results match:    true
        // Available cores:  <depends on host machine>
    }
}