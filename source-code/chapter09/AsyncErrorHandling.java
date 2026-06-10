// Java 8+
/**
 * Listing 9.7 — AsyncErrorHandling.java
 * Demonstrates: Error handling in async pipelines using exceptionally, handle, and whenComplete
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
package chapter09;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AsyncErrorHandling {

    private static final Logger log =
            Logger.getLogger(AsyncErrorHandling.class.getName());

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // exceptionally — recover from failure with a fallback value
        // Cast needed so compiler infers CompletableFuture<String> correctly
        CompletableFuture<String> withRecovery =
                CompletableFuture.<String>supplyAsync(() -> {
                    throw new RuntimeException("Payment gateway unavailable");
                }, executor)
                .exceptionally(ex -> {
                    log.warning("Recovering from: " + ex.getMessage());
                    return "fallback-response"; // substitute value on failure
                });

        log.info("Recovery result: " + withRecovery.get());

        // handle — called on both success and failure
        // provides access to both result and exception for transformation
        CompletableFuture<String> withHandle =
                CompletableFuture.supplyAsync(
                        () -> "order-processed", executor)
                .handle((result, ex) -> {
                    if (ex != null) {
                        log.warning("Failed: " + ex.getMessage());
                        return "error-response";
                    }
                    return result.toUpperCase(); // transform on success
                });

        log.info("Handle result: " + withHandle.get());

        // whenComplete — called on both outcomes for side effects only
        // does not transform the result; passes it through unchanged
        CompletableFuture<String> withLogging =
                CompletableFuture.supplyAsync(
                        () -> "notification-sent", executor)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.severe("Async failure: " + ex.getMessage());
                    } else {
                        log.info("Completed: " + result); // observe, not transform
                    }
                });

        withLogging.get();

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // WARNING: Recovering from: java.lang.RuntimeException: Payment gateway unavailable
        // INFO: Recovery result: fallback-response
        // INFO: Handle result: ORDER-PROCESSED
        // INFO: Completed: notification-sent
    }
}