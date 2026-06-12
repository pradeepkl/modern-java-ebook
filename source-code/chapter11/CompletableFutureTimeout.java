// Java 9+
/**
 * Listing 11.6 — CompletableFutureTimeout.java
 * Demonstrates: orTimeout and completeOnTimeout for bounding async wait time
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 9+
 */
package chapter11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CompletableFutureTimeout {

    private static final Logger log =
            Logger.getLogger(CompletableFutureTimeout.class.getName());

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // orTimeout — completes exceptionally with
        // TimeoutException if not done within the window
        CompletableFuture<String> withTimeout =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(2000); // slow operation
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return "late result";
                }, executor)
                .orTimeout(1, TimeUnit.SECONDS); // cancel after 1s

        try {
            log.info(withTimeout.get());
        } catch (Exception e) {
            // TimeoutException wrapped in ExecutionException
            log.warning("Timed out: " + e.getCause()
                    .getClass().getSimpleName());
        }

        // completeOnTimeout — provides a fallback value
        // instead of completing exceptionally
        CompletableFuture<String> withFallback =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(2000); // also too slow
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return "late result";
                }, executor)
                .completeOnTimeout("fallback-result", 1, TimeUnit.SECONDS);

        log.info("Result: " + withFallback.get()); // returns fallback

        // Graceful executor shutdown
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }

        // Output:
        // WARNING: Timed out: TimeoutException
        // INFO: Result: fallback-result
    }
}