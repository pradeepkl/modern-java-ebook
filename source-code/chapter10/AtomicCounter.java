// Java 25+
// Feature shown: AtomicInteger lock-free counter, final in Java 8+
/**
 * Listing 10.10 — AtomicCounter.java
 * Demonstrates: AtomicInteger for thread-safe lock-free counting
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class AtomicCounter {

    private static final Logger log =
            Logger.getLogger(AtomicCounter.class.getName());

    // AtomicInteger — thread-safe without synchronized
    static AtomicInteger count = new AtomicInteger(0);

    void main() throws InterruptedException {

        // Fixed pool of 10 threads submitting 1000 increments
        ExecutorService executor =
                Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            // Each task increments atomically via compare-and-set
            executor.submit(() -> count.incrementAndGet());
        }

        // Stop accepting new tasks
        executor.shutdown();

        // Wait for all in-flight increments to complete
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // All 1000 increments are visible — no lost updates
        log.info("Final count: " + count.get());

        // Output: INFO: Final count: 1000
    }
}