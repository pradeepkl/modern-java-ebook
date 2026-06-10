// Java 8+
/**
 * Listing 8.6 — SynchronizedCounter.java
 * Demonstrates: Using synchronized to protect shared mutable state from race conditions
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SynchronizedCounter {

    private static final Logger log =
            Logger.getLogger(SynchronizedCounter.class.getName());

    // Shared mutable state — vulnerable to race conditions without synchronisation
    static int count = 0;

    // synchronized ensures only one thread executes this method at a time
    // prevents interleaved increments that would produce incorrect totals
    public static synchronized void increment() {
        count++; // read-modify-write is atomic under the monitor lock
    }

    public static void main(String[] args) throws InterruptedException {

        // Ten threads competing to increment a shared counter
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Submit 1000 increment tasks across the thread pool
        for (int i = 0; i < 1000; i++) {
            executor.submit(SynchronizedCounter::increment);
        }

        // Orderly shutdown — wait for all tasks to complete
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Without synchronized, count would likely be less than 1000
        log.info("Final count: " + count);

        // Output: INFO: Final count: 1000
    }
}