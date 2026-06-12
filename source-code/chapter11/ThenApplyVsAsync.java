// Java 8+
/**
 * Listing 11.3 — ThenApplyVsAsync.java
 * Demonstrates: thenApply vs thenApplyAsync thread scheduling behaviour
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
package chapter11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ThenApplyVsAsync {

    private static final Logger log =
            Logger.getLogger(ThenApplyVsAsync.class.getName());

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // thenApply — continuation runs on the completing thread
        // If supplyAsync completes on thread-2,
        // the thenApply lambda also runs on thread-2
        CompletableFuture<String> inline =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supplyAsync (inline) on: "
                                    + Thread.currentThread().getName());
                            return "order-data";
                        }, executor)
                        .thenApply(data -> {
                            // Runs on whichever thread completed supplyAsync
                            log.info("thenApply on: "
                                    + Thread.currentThread().getName());
                            return data.toUpperCase();
                        });

        // thenApplyAsync — continuation submitted to executor
        // as a new task, potentially on a different thread
        CompletableFuture<String> offloaded =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supplyAsync (offloaded) on: "
                                    + Thread.currentThread().getName());
                            return "order-data";
                        }, executor)
                        .thenApplyAsync(data -> {
                            // Submitted as a new task to the executor
                            log.info("thenApplyAsync on: "
                                    + Thread.currentThread().getName());
                            return data.toUpperCase();
                        }, executor);

        // Block only at the terminal result
        log.info("Inline result:    " + inline.get());
        log.info("Offloaded result: " + offloaded.get());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // INFO: supplyAsync (inline) on: pool-1-thread-1
        // INFO: thenApply on: pool-1-thread-1
        // INFO: supplyAsync (offloaded) on: pool-1-thread-2
        // INFO: thenApplyAsync on: pool-1-thread-3
        // INFO: Inline result:    ORDER-DATA
        // INFO: Offloaded result: ORDER-DATA
    }
}