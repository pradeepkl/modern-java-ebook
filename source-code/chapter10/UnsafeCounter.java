// Java 8+
package chapter10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 10.2 — UnsafeCounter.java
 * Demonstrates: Shared mutable state without coordination leads to lost updates
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
public class UnsafeCounter {

    private static final Logger log =
            Logger.getLogger(UnsafeCounter.class.getName());

    // Shared mutable state — no synchronization, no atomics
    static int count = 0;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Submit 1000 tasks, each incrementing count once
        // Expected result: 1000
        // Actual result:   unpredictable — typically less than 1000
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> count++); // count++ is NOT atomic
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // count++ compiles to three steps: read, increment, write.
        // Two threads can read the same value simultaneously,
        // both increment it independently, and both write back
        // the same result — one increment is silently lost.
        log.info("Final count (expected 1000): " + count);

        // Output: Final count (expected 1000): 963  (varies per run)
    }
}