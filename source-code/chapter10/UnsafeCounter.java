// Java 25+
// Feature shown: unsynchronized shared counter (race condition), final in Java 8+
package chapter10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 10.2 — UnsafeCounter.java
 * Demonstrates: shared mutable state without synchronization, producing a race condition
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class UnsafeCounter {

    private static final Logger log =
            Logger.getLogger(UnsafeCounter.class.getName());

    // Shared mutable state — no coordination between threads
    static int count = 0;

    void main() throws InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(10); // 10 threads competing for count

        // 1000 tasks each incrementing count once
        // Expected result: 1000
        // Actual result:   unpredictable due to lost updates
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> count++); // read-increment-write is not atomic
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