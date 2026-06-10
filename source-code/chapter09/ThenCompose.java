// Java 8+
package chapter09;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 9.4 — ThenCompose.java
 * Demonstrates: Chaining dependent async tasks with thenCompose
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
public class ThenCompose {

    private static final Logger log =
            Logger.getLogger(ThenCompose.class.getName());

    record Order(String orderId, String customerId) {}
    record CustomerProfile(String customerId, String tier) {}
    record PricingDecision(double finalPrice) {}

    // Simulates async order fetch from a remote service
    static CompletableFuture<Order> fetchOrder(
            String orderId, ExecutorService exec) {
        return CompletableFuture.supplyAsync(
                () -> new Order(orderId, "CUST-42"), exec);
    }

    // Simulates async profile fetch — depends on customerId from Order
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
            // thenApply here would yield CompletableFuture<CompletableFuture<...>>
            .thenCompose(order ->
                fetchProfile(order.customerId(), executor)
                    .thenApply(profile -> {
                        // Apply GOLD tier discount of 10%
                        double discount =
                            profile.tier().equals("GOLD") ? 0.1 : 0.0;
                        return new PricingDecision(299.99 * (1 - discount));
                    }));

        PricingDecision decision = pipeline.get(); // block only at terminal result
        log.info("Final price: " + decision.finalPrice());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output: INFO: Final price: 269.991
    }
}