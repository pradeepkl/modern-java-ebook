// Java 8+
package chapter09;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 9.2 — CompletableFutureBasics.java
 * Demonstrates: Declarative async pipelines using CompletableFuture
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
public class CompletableFutureBasics {

    private static final Logger log =
            Logger.getLogger(CompletableFutureBasics.class.getName());

    // Immutable data carriers for the pipeline stages
    record OrderSummary(String orderId, double total) {}
    record EnrichedOrder(String orderId, double total, String currency) {}

    public static void main(String[] args) throws Exception {

        // Fixed thread pool — stages execute on pooled threads
        ExecutorService executor = Executors.newFixedThreadPool(4);

        CompletableFuture<EnrichedOrder> pipeline =

            // Stage 1: fetch order asynchronously on executor thread
            CompletableFuture.supplyAsync(
                    () -> new OrderSummary("ORD-001", 299.99),
                    executor)

            // Stage 2: transform — runs when Stage 1 completes
            // No blocking — the runtime chains execution automatically
            .thenApply(order ->
                    new EnrichedOrder(
                            order.orderId(),
                            order.total(),
                            "GBP"))

            // Stage 3: log and pass through unchanged
            .thenApply(enriched -> {
                log.info("Enriched: "
                        + enriched.orderId()
                        + " total=" + enriched.total()
                        + " currency=" + enriched.currency());
                return enriched; // propagate to next stage
            });

        // Single blocking call — only at the terminal result
        EnrichedOrder result = pipeline.get();
        log.info("Final: " + result.orderId()
                + " " + result.total()
                + " " + result.currency());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // INFO: Enriched: ORD-001 total=299.99 currency=GBP
        // INFO: Final: ORD-001 299.99 GBP
    }
}