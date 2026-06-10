// Java 8+
/**
 * Listing 8.10 — AtomicCounter.java
 * Demonstrates: Thread-safe counter using AtomicInteger without synchronized
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

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

        // Fixed thread pool with 10 threads
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Submit 1000 increment tasks concurrently
        for (int i = 0; i < 1000; i++) {
            executor.submit(() ->
                    count.incrementAndGet()); // CAS-based atomic increment
        }

        // Initiate orderly shutdown — no new tasks accepted
        executor.shutdown();

        // Wait for all submitted tasks to complete
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // All 1000 increments must be reflected — no lost updates
        log.info("Final count: " + count.get());

        // Output: INFO: Final count: 1000
    }
}