// Java 8+
/**
 * Listing 8.2 — UnsafeCounter.java
 * Demonstrates: Shared mutable state without coordination leads to lost updates
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UnsafeCounter {

    private static final Logger log =
            Logger.getLogger(UnsafeCounter.class.getName());

    // Shared mutable state — no coordination between threads
    static int count = 0;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 1000 tasks each incrementing count once
        // Expected result: 1000
        // Actual result:   unpredictable due to race conditions
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> count++); // count++ is NOT atomic
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // count++ compiles to: read, increment, write (three separate steps).
        // Two threads can read the same value simultaneously,
        // both increment it independently, and both write back
        // the same result — one increment is silently lost.
        log.info("Final count (expected 1000): " + count);

        // Output: Final count (expected 1000): [some value <= 1000, often less]
    }
}