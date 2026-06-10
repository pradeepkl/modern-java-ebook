// Java 8+
package chapter09;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 9.3 — ThenApplyVsAsync.java
 * Demonstrates: Difference between thenApply (same completing thread)
 *               and thenApplyAsync (new task submitted to executor)
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
public class ThenApplyVsAsync {

    private static final Logger log =
            Logger.getLogger(ThenApplyVsAsync.class.getName());

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // thenApply — continuation runs on the completing thread
        // If supplyAsync completes on pool-thread-1,
        // the thenApply lambda also runs on pool-thread-1
        CompletableFuture<String> inline =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supplyAsync (inline) on: "
                                    + Thread.currentThread().getName());
                            return "order-data";
                        }, executor)
                        .thenApply(data -> {
                            log.info("thenApply (inline) on: "
                                    + Thread.currentThread().getName());
                            return data.toUpperCase(); // same thread as supplier
                        });

        // thenApplyAsync — continuation submitted to executor as a new task,
        // potentially on a different thread from the completing stage
        CompletableFuture<String> offloaded =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supplyAsync (offloaded) on: "
                                    + Thread.currentThread().getName());
                            return "order-data";
                        }, executor)
                        .thenApplyAsync(data -> {
                            log.info("thenApplyAsync (offloaded) on: "
                                    + Thread.currentThread().getName());
                            return data.toUpperCase(); // new task on executor
                        }, executor);

        log.info("Inline result:    " + inline.get());
        log.info("Offloaded result: " + offloaded.get());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // supplyAsync (inline) on: pool-1-thread-1
        // thenApply (inline) on: pool-1-thread-1
        // supplyAsync (offloaded) on: pool-1-thread-2
        // thenApplyAsync (offloaded) on: pool-1-thread-3
        // Inline result:    ORDER-DATA
        // Offloaded result: ORDER-DATA
    }
}