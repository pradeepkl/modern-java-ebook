// Java 25+
// Feature shown: AtomicInteger lock-free counter, final in Java 8+
/**
 * Listing 10.10 — AtomicCounter.java
 * Demonstrates: AtomicInteger for lock-free thread-safe counting
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
    // Uses hardware-level compare-and-set (CAS) operations
    static AtomicInteger count = new AtomicInteger(0);

    void main() throws InterruptedException {

        // 10 threads each submitting 100 increments = 1000 total
        ExecutorService executor =
                Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            // incrementAndGet is atomic — no lost updates under contention
            executor.submit(() -> count.incrementAndGet());
        }

        // Stop accepting new tasks
        executor.shutdown();

        // Wait for all 1000 increments to complete
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // All increments are visible — no synchronization needed here
        log.info("Final count: " + count.get());

        // Output: INFO: Final count: 1000
    }
}