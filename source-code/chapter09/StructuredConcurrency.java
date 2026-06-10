// Java 21+ (preview feature — compile with --enable-preview --source 21)
/**
 * Listing 9.11 — StructuredConcurrency.java
 * Demonstrates: StructuredTaskScope.ShutdownOnFailure for bounded
 *               concurrent task execution with automatic sibling cancellation
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 21+ with --enable-preview
 */
package chapter09;

import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class StructuredConcurrency {

    private static final Logger log =
            Logger.getLogger(StructuredConcurrency.class.getName());

    // Records representing domain objects
    record Order(String orderId, double total) {}
    record CustomerProfile(String tier) {}
    record PricingDecision(double finalPrice) {}

    public static PricingDecision processOrder(
            String orderId) throws Exception {

        // ShutdownOnFailure cancels all siblings if any task fails
        try (var scope =
                new StructuredTaskScope.ShutdownOnFailure()) {

            // Fork concurrent subtasks within the scope
            StructuredTaskScope.Subtask<Order> orderTask =
                    scope.fork(() ->
                            new Order(orderId, 299.99));

            StructuredTaskScope.Subtask<CustomerProfile>
                    profileTask = scope.fork(() ->
                            new CustomerProfile("GOLD"));

            // Block until all tasks complete or any task fails
            scope.join()
                 .throwIfFailed(); // rethrow if any subtask threw

            // Safe to call .get() only after join() + throwIfFailed()
            Order order = orderTask.get();
            CustomerProfile profile = profileTask.get();

            // Apply tier-based discount
            double discount = profile.tier()
                    .equals("GOLD") ? 0.1 : 0.0;
            return new PricingDecision(
                    order.total() * (1 - discount));
        }
        // Scope closes here — all tasks guaranteed complete or cancelled
    }

    public static void main(String[] args) throws Exception {
        PricingDecision decision = processOrder("ORD-001");
        log.info("Final price: " + decision.finalPrice());
        // Output: INFO: Final price: 269.991
    }
}