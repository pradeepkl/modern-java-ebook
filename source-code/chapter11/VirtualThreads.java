// Java 21+
/**
 * Listing 11.12 — VirtualThreads.java
 * Demonstrates: Virtual threads vs platform threads, scaling I/O-bound tasks
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 21+
 */
package chapter11;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class VirtualThreads {

    private static final Logger log =
            Logger.getLogger(VirtualThreads.class.getName());

    public static void main(String[] args) throws Exception {

        // Platform thread executor — bounded by OS resources
        // Spring Boot Tomcat default: 200 threads
        ExecutorService platformExecutor =
                Executors.newFixedThreadPool(200);

        // Virtual thread executor — JVM-managed, scales to millions
        ExecutorService virtualExecutor =
                Executors.newVirtualThreadPerTaskExecutor();

        long start = System.currentTimeMillis();

        // 10,000 tasks — each simulating an I/O wait of 100ms
        // Platform threads: 9,800 would queue behind the 200-thread pool
        // Virtual threads: all 10,000 run concurrently, parking cheaply
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
        // Should complete in ~100ms, not 10,000 * 100ms / 200 threads = 5000ms
        virtualExecutor.awaitTermination(30, TimeUnit.SECONDS);

        long elapsed = System.currentTimeMillis() - start;
        log.info("10,000 tasks completed in: " + elapsed + "ms");

        // Explicit virtual thread creation with name and start
        Thread vThread = Thread.ofVirtual()
                .name("order-processor")
                .start(() ->
                    log.info("Running on virtual thread: "
                            + Thread.currentThread().isVirtual()));

        vThread.join(); // Wait for the named virtual thread to finish

        platformExecutor.shutdown();

        // Output:
        // INFO: 10,000 tasks completed in: ~130ms
        // INFO: Running on virtual thread: true
    }
}