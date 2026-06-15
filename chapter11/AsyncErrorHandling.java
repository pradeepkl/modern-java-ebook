// Java 25+
// Feature shown: exceptionally, handle, and whenComplete error handling, final in Java 8+
/**
 * Listing 11.7 — AsyncErrorHandling.java
 * Demonstrates: exceptionally, handle, and whenComplete for async error handling
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AsyncErrorHandling {

    private static final Logger log =
            Logger.getLogger(AsyncErrorHandling.class.getName());

    void main() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // exceptionally — recover from failure with a fallback value
        // only invoked when the upstream stage completes exceptionally
        // explicit type witness resolves inference to CompletableFuture<String>
        CompletableFuture<String> withRecovery =
                CompletableFuture.<String>supplyAsync(() -> {
                    throw new RuntimeException("Payment gateway unavailable");
                }, executor)
                .exceptionally(ex -> {
                    log.warning("Recovering from: " + ex.getMessage());
                    return "fallback-response"; // substitute value on failure
                });

        log.info("Recovery result: " + withRecovery.get());

        // handle — invoked on both success and failure
        // receives (result, exception); exactly one will be non-null
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

        // whenComplete — invoked on both outcomes for side effects only
        // does not transform the result; passes it through unchanged
        CompletableFuture<String> withLogging =
                CompletableFuture.supplyAsync(
                        () -> "notification-sent", executor)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.severe("Async failure: " + ex.getMessage());
                    } else {
                        log.info("Completed: " + result);
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