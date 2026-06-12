// Java 8+
/**
 * Listing 10.10 — AtomicCounter.java
 * Demonstrates: Thread-safe counter using AtomicInteger without synchronized
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 8+
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

    public static void main(String[] args) throws InterruptedException {

        // 10 threads competing to increment the shared counter
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Submit 1000 increment tasks — each uses CAS internally
        for (int i = 0; i < 1000; i++) {
            executor.submit(() ->
                    count.incrementAndGet()); // atomic compare-and-set, no lock needed
        }

        executor.shutdown();

        // Wait for all tasks to complete before reading final value
        boolean finished = executor.awaitTermination(5, TimeUnit.SECONDS);

        if (finished) {
            // Should always print 1000 — no lost updates
            log.info("Final count: " + count.get());
        } else {
            log.warning("Executor did not terminate in time");
        }

        // Output: INFO: Final count: 1000
    }
}