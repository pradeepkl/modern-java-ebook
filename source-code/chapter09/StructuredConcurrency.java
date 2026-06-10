// ⚠️ COMPILATION WARNING: Review needed
// Compiler error: chapter09\StructuredConcurrency.java:11: error: StructuredTaskScope is a preview API and is disabled by default.
// Java 21+ (preview feature — compile with: javac --enable-preview --release 21 StructuredConcurrency.java)
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

@SuppressWarnings({"preview", "restricted"})
public class StructuredConcurrency {

    private static final Logger log =
            Logger.getLogger(StructuredConcurrency.class.getName());

    record Order(String orderId, double total) {}
    record CustomerProfile(String tier) {}
    record PricingDecision(double finalPrice) {}

    // ShutdownOnFailure cancels all siblings if any task fails
    @SuppressWarnings("preview")
    public static PricingDecision processOrder(String orderId)
            throws Exception {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // Fork order fetch task concurrently
            StructuredTaskScope.Subtask<Order> orderTask =
                    scope.fork(() -> new Order(orderId, 299.99));

            // Fork customer profile fetch task concurrently
            StructuredTaskScope.Subtask<CustomerProfile> profileTask =
                    scope.fork(() -> new CustomerProfile("GOLD"));

            // Block until all tasks complete or any task fails
            scope.join()
                 .throwIfFailed(); // rethrows first failure if any

            // Safe to call .get() only after join() + throwIfFailed()
            Order order = orderTask.get();
            CustomerProfile profile = profileTask.get();

            // Apply discount based on customer tier
            double discount = profile.tier().equals("GOLD") ? 0.1 : 0.0;
            return new PricingDecision(order.total() * (1 - discount));

        }
        // Scope closes here — all subtasks guaranteed complete or cancelled
    }

    @SuppressWarnings("preview")
    public static void main(String[] args) throws Exception {
        PricingDecision decision = processOrder("ORD-001");
        log.info("Final price: " + decision.finalPrice());

        // Output: INFO: Final price: 269.991
    }
}