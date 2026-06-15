// Java 25+
// Feature shown: thenCompose for dependent async operations, final in Java 8+
/**
 * Listing 11.4 — ThenCompose.java
 * Demonstrates: chaining dependent async operations with thenCompose
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

public class ThenCompose {

    private static final Logger log =
            Logger.getLogger(ThenCompose.class.getName());

    record Order(String orderId, String customerId) {}
    record CustomerProfile(String customerId, String tier) {}
    record PricingDecision(double finalPrice) {}

    static CompletableFuture<Order> fetchOrder(
            String orderId, ExecutorService exec) {
        return CompletableFuture.supplyAsync(
                () -> new Order(orderId, "CUST-42"), exec);
    }

    static CompletableFuture<CustomerProfile> fetchProfile(
            String customerId, ExecutorService exec) {
        return CompletableFuture.supplyAsync(
                () -> new CustomerProfile(customerId, "GOLD"), exec);
    }

    void main() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        CompletableFuture<PricingDecision> pipeline =
            fetchOrder("ORD-001", executor)

            // thenCompose — second async op depends on result of first.
            // Returns the inner CompletableFuture, not a nested one.
            // thenApply here would yield CompletableFuture<CompletableFuture<...>>
            .thenCompose(order ->
                fetchProfile(order.customerId(), executor)
                    .thenApply(profile -> {
                        double discount =
                            profile.tier().equals("GOLD") ? 0.1 : 0.0;
                        return new PricingDecision(299.99 * (1 - discount));
                    }));

        PricingDecision decision = pipeline.get();
        log.info("Final price: " + decision.finalPrice());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        // Output: INFO: Final price: 269.991
    }
}