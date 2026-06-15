// Java 25+
// Feature shown: volatile field for cross-thread visibility, final in Java 8+

/**
 * Listing 10.7 — VolatileVisibility.java
 * Demonstrates: volatile field ensuring cross-thread visibility of a flag
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

import java.util.logging.Logger;

public class VolatileVisibility {

    private static final Logger log =
            Logger.getLogger(VolatileVisibility.class.getName());

    // Without volatile, the JVM may cache this value
    // in a thread-local register. The worker thread
    // may never see the update from the main thread.
    static volatile boolean running = true;

    void main() throws InterruptedException {

        Thread worker = new Thread(() -> {
            int iterations = 0;
            // Loop reads 'running' from main memory on every iteration
            while (running) {
                iterations++;
            }
            // Guaranteed to reach here because volatile write is visible
            log.info("Stopped after " + iterations + " iterations");
        });

        worker.start();

        // Allow the worker thread to spin for a short time
        Thread.sleep(10);

        // Without volatile, this write may not be
        // visible to the worker thread in time.
        running = false;

        // Wait for the worker to observe the flag and exit
        worker.join();

        log.info("Worker thread finished cleanly");

        // Output:
        // INFO: Stopped after <N> iterations
        // INFO: Worker thread finished cleanly
    }
}