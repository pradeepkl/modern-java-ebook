// Java 25+
// Feature shown: unsynchronized shared counter (race condition), final in Java 8+

/**
 * Listing 10.2 — UnsafeCounter.java
 * Demonstrates: shared mutable state without synchronization, exposing a race condition
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UnsafeCounter {

    private static final Logger log =
            Logger.getLogger(UnsafeCounter.class.getName());

    // Shared mutable state — no coordination across threads
    static int count = 0;

    void main() throws InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(10);

        // 1000 tasks each incrementing count once
        // Expected result: 1000
        // Actual result:   unpredictable due to race condition
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> count++); // not atomic: read, increment, write
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // count++ compiles to three separate operations: read, increment, write.
        // Two threads can read the same value simultaneously,
        // both increment it independently, and each write back
        // the same result — causing one increment to be lost.
        log.info("Final count (expected 1000, actual varies): " + count);

        // Output: Final count (expected 1000, actual varies): 947
    }
}