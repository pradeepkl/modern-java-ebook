// Java 8+
/**
 * Listing 11.4 — ThenCompose.java
 * Demonstrates: Chaining dependent async tasks with thenCompose
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 8+
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

    // Simulates async fetch of an order by ID
    static CompletableFuture<Order> fetchOrder(
            String orderId, ExecutorService exec) {
        return CompletableFuture.supplyAsync(
                () -> new Order(orderId, "CUST-42"), exec);
    }

    // Simulates async fetch of a customer profile
    static CompletableFuture<CustomerProfile> fetchProfile(
            String customerId, ExecutorService exec) {
        return CompletableFuture.supplyAsync(
                () -> new CustomerProfile(customerId, "GOLD"), exec);
    }

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        CompletableFuture<PricingDecision> pipeline =
            fetchOrder("ORD-001", executor)

            // thenCompose — second async op depends on result of first.
            // Returns the inner CompletableFuture, not a nested one.
            // Using thenApply here would yield CompletableFuture<CompletableFuture<...>>
            .thenCompose(order ->
                    fetchProfile(order.customerId(), executor)
                    .thenApply(profile -> {
                        // Apply 10% discount for GOLD tier customers
                        double discount =
                            profile.tier().equals("GOLD") ? 0.1 : 0.0;
                        return new PricingDecision(299.99 * (1 - discount));
                    }));

        // Block only at the terminal result
        PricingDecision decision = pipeline.get();
        log.info("Final price: " + decision.finalPrice());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output: INFO: Final price: 269.991
    }
}