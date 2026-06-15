// Java 25+
// Feature shown: Future.get() blocking on dependent tasks, final in Java 8+
/**
 * Listing 11.1 — FutureBlockingProblem.java
 * Demonstrates: How Future.get() blocks the calling thread when tasks depend
 * on each other, illustrating the core limitation of raw Future composition.
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter11;

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

    static OrderSummary fetchOrder(String orderId) {
        return new OrderSummary(orderId, 299.99); // simulates I/O
    }

    static CustomerProfile fetchProfile(String customerId) {
        return new CustomerProfile(customerId, "GOLD"); // simulates I/O
    }

    static PricingDecision calculatePrice(
            OrderSummary order, CustomerProfile profile) {
        double discount = profile.tier().equals("GOLD") ? 0.1 : 0.0;
        return new PricingDecision(order.total() * (1 - discount));
    }

    void main() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Step 1: submit order and profile fetches in parallel
        Future<OrderSummary> orderFuture =
                executor.submit(() -> fetchOrder("ORD-001"));
        Future<CustomerProfile> profileFuture =
                executor.submit(() -> fetchProfile("CUST-42"));

        // NOT IDEAL: get() blocks the calling thread.
        // While waiting for orderFuture, this thread is idle.
        OrderSummary order = orderFuture.get();        // blocks here
        CustomerProfile profile = profileFuture.get(); // blocks here

        // Step 2: pricing depends on both results — submitted only after both block
        Future<PricingDecision> priceFuture =
                executor.submit(() -> calculatePrice(order, profile));

        PricingDecision decision = priceFuture.get(); // blocks again
        log.info("Final price: " + decision.finalPrice());

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // INFO: Final price: 269.991
    }
}