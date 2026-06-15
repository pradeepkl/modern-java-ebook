// Java 25+
// Feature shown: parallel streams with ForkJoinPool, final in Java 8+

/**
 * Listing 15.10 — ParallelStreamGuide.java
 * Demonstrates: When to use and avoid parallel streams, thread-safe collection,
 *               and custom ForkJoinPool for controlled parallelism
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class ParallelStreamGuide {

    private static final Logger LOG = Logger.getLogger(ParallelStreamGuide.class.getName());

    record Order(double amount, String status) {}

    static List<Order> getLargeOrderList() {
        return IntStream.rangeClosed(1, 200_000)
                .mapToObj(i -> new Order(i * 0.5, i % 2 == 0 ? "CONFIRMED" : "PENDING"))
                .toList();
    }

    void main() throws Exception {
        List<Order> largeOrderList = getLargeOrderList();

        // NOT IDEAL: Unsafe — shared mutable ArrayList causes race conditions
        List<Order> unsafeResult = new ArrayList<>();
        largeOrderList.parallelStream()
                .filter(o -> o.amount() > 100.0)
                .forEach(unsafeResult::add); // race condition — size may be wrong
        LOG.info("Unsafe result size (may vary): " + unsafeResult.size());

        // Safe — toList() collector is thread-safe
        List<Order> safeResult = largeOrderList.parallelStream()
                .filter(o -> o.amount() > 100.0)
                .toList(); // guaranteed correct size
        LOG.info("Safe result size: " + safeResult.size());

        // Custom ForkJoinPool — isolate parallelism from the common pool
        ForkJoinPool customPool = new ForkJoinPool(4); // exactly 4 worker threads
        try {
            double total = customPool.submit(() ->
                    largeOrderList.parallelStream()
                            .mapToDouble(Order::amount)
                            .sum()
            ).get(); // blocks until complete
            LOG.info("Total amount via custom pool (4 threads): " + total);
        } finally {
            customPool.shutdown(); // always release custom pool resources
        }

        // Output:
        // Unsafe result size (may vary): <some value, possibly less than 99800>
        // Safe result size: 199800
        // Total amount via custom pool (4 threads): 1.0000025E10
    }
}