// Java 25+
// Feature shown: virtual threads (JEP 444), final in Java 21+

/**
 * Listing 11.12 — VirtualThreads.java
 * Demonstrates: virtual threads scaling to 10,000 concurrent tasks
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 21+ for virtual threads; void main() instance main
 * method requires Java 25+ (JEP 512)
 */
package chapter11;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class VirtualThreads {

    private static final Logger log =
            Logger.getLogger(VirtualThreads.class.getName());

    void main() throws Exception {

        // Platform thread executor — bounded by OS resources
        // Spring Boot Tomcat default is 200 threads
        ExecutorService platformExecutor =
                Executors.newFixedThreadPool(200);

        // Virtual thread executor — JVM-managed lightweight threads
        // Can scale to millions of concurrent virtual threads
        ExecutorService virtualExecutor =
                Executors.newVirtualThreadPerTaskExecutor();

        long start = System.currentTimeMillis();

        // 10,000 tasks — each simulating an I/O wait of 100ms
        // With platform threads (pool=200): ~5000ms total
        // With virtual threads: ~100ms total
        for (int i = 0; i < 10_000; i++) {
            virtualExecutor.submit(() -> {
                try {
                    // Simulate I/O — blocking is cheap for virtual threads
                    // JVM parks the virtual thread; carrier thread stays free
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        virtualExecutor.shutdown();
        virtualExecutor.awaitTermination(30, TimeUnit.SECONDS);

        long elapsed = System.currentTimeMillis() - start;
        log.info("10,000 tasks completed in: " + elapsed + "ms");

        // Single virtual thread — explicit creation via Thread.ofVirtual()
        Thread vThread = Thread.ofVirtual()
                .name("order-processor")
                .start(() ->
                    log.info("Running on virtual thread: "
                            + Thread.currentThread().isVirtual()));

        vThread.join(); // wait for the named virtual thread to finish

        platformExecutor.shutdown();

        // Output:
        // INFO: 10,000 tasks completed in: 134ms
        // INFO: Running on virtual thread: true
    }
}