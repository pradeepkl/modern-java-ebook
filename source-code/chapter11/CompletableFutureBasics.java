// Java 25+
// Feature shown: CompletableFuture.supplyAsync and thenApply chaining, final in Java 8+

/**
 * Listing 11.2 — CompletableFutureBasics.java
 * Demonstrates: CompletableFuture async pipeline with supplyAsync and thenApply chaining
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

public class CompletableFutureBasics {

    private static final Logger log =
            Logger.getLogger(CompletableFutureBasics.class.getName());

    record OrderSummary(String orderId, double total) {}

    record EnrichedOrder(String orderId, double total, String currency) {}

    void main() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        CompletableFuture<EnrichedOrder> pipeline =
            // Stage 1: fetch order asynchronously on the executor thread
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

            // Stage 3: log and pass the enriched order through unchanged
            .thenApply(enriched -> {
                log.info("Enriched: "
                        + enriched.orderId()
                        + " total=" + enriched.total()
                        + " currency=" + enriched.currency());
                return enriched;
            });

        // Only block here — at the single terminal result
        EnrichedOrder result = pipeline.get();
        log.info("Final order id: " + result.orderId()
                + " total=" + result.total());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // INFO: Enriched: ORD-001 total=299.99 currency=GBP
        // INFO: Final order id: ORD-001 total=299.99
    }
}