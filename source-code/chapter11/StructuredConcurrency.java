// Java 21+ (preview feature — compile with --enable-preview --source 21)
/**
 * Listing 11.11 — StructuredConcurrency.java
 * Demonstrates: StructuredTaskScope.ShutdownOnFailure for structured concurrency
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 21+ with --enable-preview
 */
package chapter11;

import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class StructuredConcurrency {

    private static final Logger log =
            Logger.getLogger(StructuredConcurrency.class.getName());

    // Domain records used in the pricing workflow
    record Order(String orderId, double total) {}
    record CustomerProfile(String tier) {}
    record PricingDecision(double finalPrice) {}

    /**
     * Fetches order and customer profile concurrently,
     * then computes a final price based on customer tier.
     */
    public static PricingDecision processOrder(String orderId) throws Exception {

        // ShutdownOnFailure cancels all siblings if any task fails
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // Fork subtask: fetch order details
            StructuredTaskScope.Subtask<Order> orderTask =
                    scope.fork(() -> new Order(orderId, 299.99));

            // Fork subtask: fetch customer profile concurrently
            StructuredTaskScope.Subtask<CustomerProfile> profileTask =
                    scope.fork(() -> new CustomerProfile("GOLD"));

            // Wait for all tasks to complete; throw if any failed
            scope.join().throwIfFailed();

            // Both subtasks completed — safe to call .get()
            Order order = orderTask.get();
            CustomerProfile profile = profileTask.get();

            // Apply 10% discount for GOLD tier customers
            double discount = profile.tier().equals("GOLD") ? 0.1 : 0.0;
            return new PricingDecision(order.total() * (1 - discount));

        } // Scope closes here — all tasks guaranteed complete or cancelled
    }

    public static void main(String[] args) throws Exception {
        PricingDecision decision = processOrder("ORD-001");
        // Log the computed final price after discount
        log.info("Final price: " + decision.finalPrice());

        // Output: INFO: Final price: 269.991
    }
}