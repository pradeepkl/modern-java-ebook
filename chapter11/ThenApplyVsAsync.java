// Java 25+
// Feature shown: thenApply vs thenApplyAsync continuation threading, final in Java 8+
package chapter11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 11.3 — ThenApplyVsAsync.java
 * Demonstrates: thenApply runs continuation on the completing thread;
 * thenApplyAsync submits continuation to an executor as a new task.
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 8+ for CompletableFuture; void main() instance main
 * method requires Java 25+ (JEP 512: Compact Source Files and Instance
 * Main Methods)
 */
public class ThenApplyVsAsync {

    private static final Logger log =
            Logger.getLogger(ThenApplyVsAsync.class.getName());

    void main() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // thenApply — continuation runs on the completing thread
        // If supplyAsync completes on pool-thread-1,
        // the thenApply lambda also runs on pool-thread-1
        CompletableFuture<String> inline =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supply on: " + Thread.currentThread().getName());
                            return "order-data";
                        }, executor)
                        .thenApply(data -> {
                            log.info("thenApply on: " + Thread.currentThread().getName());
                            return data.toUpperCase(); // same thread as supplier
                        });

        // thenApplyAsync — continuation submitted to executor as a new task,
        // potentially on a different thread from the completing stage
        CompletableFuture<String> offloaded =
                CompletableFuture
                        .supplyAsync(() -> {
                            log.info("supply on: " + Thread.currentThread().getName());
                            return "order-data";
                        }, executor)
                        .thenApplyAsync(data -> {
                            log.info("thenApplyAsync on: " + Thread.currentThread().getName());
                            return data.toUpperCase(); // new task on executor
                        }, executor);

        log.info("Inline result:    " + inline.get());
        log.info("Offloaded result: " + offloaded.get());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // supply on: pool-1-thread-1
        // thenApply on: pool-1-thread-1
        // supply on: pool-1-thread-2
        // thenApplyAsync on: pool-1-thread-3
        // Inline result:    ORDER-DATA
        // Offloaded result: ORDER-DATA
    }
}