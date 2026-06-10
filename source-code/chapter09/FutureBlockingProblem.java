// Java 8+
/**
 * Listing 9.1 — FutureBlockingProblem.java
 * Demonstrates: The blocking nature of Future.get() and why chained
 *               dependent tasks expose the limits of raw Future-based concurrency.
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
package chapter09;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class FutureBlockingProblem {

    private static final Logger log =
            Logger.getLogger(FutureBlockingProblem.class.getName());

    record OrderSummary(String orderId, double total) {}
    record CustomerProfile(String customerId, String tier) {}
    record PricingDecision(double finalPrice) {}

    // Simulates a remote order fetch (e.g., database or HTTP call)
    static OrderSummary fetchOrder(String orderId) {
        return new OrderSummary(orderId, 299.99);
    }

    // Simulates a remote profile fetch
    static CustomerProfile fetchProfile(String customerId) {
        return new CustomerProfile(customerId, "GOLD");
    }

    // Depends on both order and profile — cannot run until both complete
    static PricingDecision calculatePrice(OrderSummary order,
                                          CustomerProfile profile) {
        double discount = profile.tier().equals("GOLD") ? 0.1 : 0.0;
        return new PricingDecision(order.total() * (1 - discount));
    }

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Step 1: submit order and profile fetches in parallel
        Future<OrderSummary> orderFuture =
                executor.submit(() -> fetchOrder("ORD-001"));
        Future<CustomerProfile> profileFuture =
                executor.submit(() -> fetchProfile("CUST-42"));

        // ❌ Both .get() calls block the calling thread.
        // While waiting for orderFuture, this thread is idle — held hostage to I/O.
        OrderSummary order     = orderFuture.get();    // blocks until order arrives
        CustomerProfile profile = profileFuture.get(); // blocks until profile arrives

        // Step 2: pricing depends on both — submitted only after both unblock
        Future<PricingDecision> priceFuture =
                executor.submit(() -> calculatePrice(order, profile));

        PricingDecision decision = priceFuture.get(); // blocks again
        log.info("Final price: " + decision.finalPrice());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output: INFO: Final price: 269.991
    }
}