// Java 21+
/**
 * Listing 9.12 — VirtualThreads.java
 * Demonstrates: Virtual threads vs platform threads for I/O-bound concurrency
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 21+
 */
package chapter09;

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
                    // Blocking is cheap for virtual threads —
                    // carrier thread is reassigned while this parks
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        virtualExecutor.shutdown();
        // All 10,000 tasks complete in ~100ms, not 5000ms
        virtualExecutor.awaitTermination(30, TimeUnit.SECONDS);

        long elapsed = System.currentTimeMillis() - start;
        log.info("10,000 tasks completed in: " + elapsed + "ms");

        // Explicit virtual thread creation with a named thread
        Thread vThread = Thread.ofVirtual()
                .name("order-processor")   // named for observability
                .start(() ->
                    log.info("Running on virtual thread: "
                            + Thread.currentThread().isVirtual()));

        vThread.join(); // wait for the named virtual thread to finish

        platformExecutor.shutdown();

        // Output:
        // INFO: 10,000 tasks completed in: ~130ms
        // INFO: Running on virtual thread: true
    }
}