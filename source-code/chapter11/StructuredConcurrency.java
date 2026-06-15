// Java 25+
// Feature shown: StructuredTaskScope structured concurrency, final in Java 25+
package chapter11;

import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

/**
 * Listing 11.11 — StructuredConcurrency.java
 * Demonstrates: StructuredTaskScope.ShutdownOnFailure for bounded concurrent tasks
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class StructuredConcurrency {

    private static final Logger log =
            Logger.getLogger(StructuredConcurrency.class.getName());

    record Order(String orderId, double total) {}
    record CustomerProfile(String tier) {}
    record PricingDecision(double finalPrice) {}

    // Both subtasks run concurrently; failure in either cancels the other
    public static PricingDecision processOrder(String orderId) throws Exception {

        // ShutdownOnFailure cancels all siblings if any task fails
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            StructuredTaskScope.Subtask<Order> orderTask =
                    scope.fork(() -> new Order(orderId, 299.99)); // fork order fetch

            StructuredTaskScope.Subtask<CustomerProfile> profileTask =
                    scope.fork(() -> new CustomerProfile("GOLD")); // fork profile fetch

            scope.join();           // wait for all tasks to complete or any to fail
            scope.throwIfFailed();  // propagate exception if any subtask failed

            Order order = orderTask.get();           // safe after join + throwIfFailed
            CustomerProfile profile = profileTask.get();

            double discount = profile.tier().equals("GOLD") ? 0.1 : 0.0;
            return new PricingDecision(order.total() * (1 - discount));
        }
        // Scope exits here — all tasks guaranteed complete or cancelled
    }

    void main() throws Exception {
        PricingDecision decision = processOrder("ORD-001");
        log.info("Final price: " + decision.finalPrice());
        // Output: INFO: Final price: 269.991
    }
}