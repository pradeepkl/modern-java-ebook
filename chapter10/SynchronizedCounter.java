// Java 25+
// Feature shown: synchronized method for mutual exclusion, final in Java 8+

/**
 * Listing 10.6 — SynchronizedCounter.java
 * Demonstrates: synchronized method ensuring mutual exclusion over shared mutable state
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SynchronizedCounter {

    private static final Logger log =
            Logger.getLogger(SynchronizedCounter.class.getName());

    // shared mutable state accessed by multiple threads
    static int count = 0;

    // synchronized ensures only one thread executes this method at a time
    public static synchronized void increment() {
        count++; // read-modify-write is atomic under the lock
    }

    void main() throws InterruptedException {

        // ten threads will each submit 100 increment tasks
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            executor.submit(SynchronizedCounter::increment); // method reference
        }

        executor.shutdown();
        // wait up to 5 seconds for all tasks to complete
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // without synchronized, count would be less than 1000 due to lost updates
        log.info("Final count: " + count);
        // Output: INFO: Final count: 1000
    }
}