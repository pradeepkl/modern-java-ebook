// Java 8+
package chapter09;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Listing 9.5 — ParallelComposition.java
 * Demonstrates: Parallel composition with thenCombine and allOf
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
public class ParallelComposition {

    private static final Logger log =
            Logger.getLogger(ParallelComposition.class.getName());

    record InventoryStatus(String productId, int available) {}
    record PriceQuote(String productId, double price) {}
    record ProductInfo(InventoryStatus inventory, PriceQuote price) {}

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // thenCombine — two independent tasks run in parallel;
        // combines results when BOTH complete
        CompletableFuture<ProductInfo> productInfo =
            CompletableFuture.supplyAsync(
                    () -> new InventoryStatus("P001", 42), executor)
            .thenCombine(
                CompletableFuture.supplyAsync(
                        () -> new PriceQuote("P001", 29.99), executor),
                ProductInfo::new); // BiFunction merges both results

        ProductInfo info = productInfo.get();
        log.info("Product: " + info.inventory() + " / " + info.price());

        // allOf — wait for ALL futures in a list to complete
        List<String> orderIds = List.of("ORD-001", "ORD-002", "ORD-003");

        List<CompletableFuture<String>> futures =
                orderIds.stream()
                        .map(id -> CompletableFuture.supplyAsync(
                                () -> "processed:" + id, executor))
                        .collect(Collectors.toList());

        // allOf returns Void; used only to signal completion of all
        CompletableFuture<Void> allDone =
                CompletableFuture.allOf(
                        futures.toArray(new CompletableFuture[0]));

        allDone.get(); // block until every future finishes

        // Safe to join() now — all futures already completed
        List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        log.info("All processed: " + results);

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // INFO: Product: InventoryStatus[productId=P001, available=42] / PriceQuote[productId=P001, price=29.99]
        // INFO: All processed: [processed:ORD-001, processed:ORD-002, processed:ORD-003]
    }
}