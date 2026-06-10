// Java 8+
/**
 * Listing 8.7 — VolatileVisibility.java
 * Demonstrates: Using volatile to ensure cross-thread visibility of a flag
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

import java.util.logging.Logger;

public class VolatileVisibility {

    private static final Logger log =
            Logger.getLogger(VolatileVisibility.class.getName());

    // Without volatile, the JVM may cache this value
    // in a thread-local register. The worker thread
    // may never see the update from the main thread.
    static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {

        Thread worker = new Thread(() -> {
            int iterations = 0;
            // Loop reads 'running' from main memory on every iteration
            while (running) {
                iterations++;
            }
            // Guaranteed to execute because volatile ensures visibility
            log.info("Stopped after " + iterations + " iterations");
        });

        worker.start();

        // Give the worker thread time to spin up and begin looping
        Thread.sleep(10);

        // Without volatile, this write may not be
        // visible to the worker thread in time.
        running = false; // Write goes directly to main memory

        // Wait for the worker thread to observe the flag and terminate
        worker.join();

        log.info("Worker thread has terminated cleanly");

        // Output:
        // INFO: Stopped after <N> iterations
        // INFO: Worker thread has terminated cleanly
    }
}