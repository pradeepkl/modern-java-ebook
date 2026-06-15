// Java 25+
// Feature shown: orTimeout and completeOnTimeout, final in Java 9+
/**
 * Listing 11.6 — CompletableFutureTimeout.java
 * Demonstrates: orTimeout (completes exceptionally on timeout) and
 *               completeOnTimeout (provides a fallback value on timeout)
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 9+ for orTimeout/completeOnTimeout; Java 25+ for
 * the void main() instance main method (JEP 512)
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

    void main() throws Exception {

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
                .orTimeout(1, TimeUnit.SECONDS); // cancel after 1 s

        try {
            log.info(withTimeout.get());
        } catch (Exception e) {
            // TimeoutException is wrapped in ExecutionException
            log.warning("Timed out: "
                    + e.getCause().getClass().getSimpleName());
        }

        // completeOnTimeout — provides a fallback value
        // instead of completing exceptionally
        CompletableFuture<String> withFallback =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(2000); // slow operation
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return "late result";
                }, executor)
                .completeOnTimeout(
                        "fallback-result",
                        1, TimeUnit.SECONDS); // use fallback after 1 s

        log.info("Result: " + withFallback.get());

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