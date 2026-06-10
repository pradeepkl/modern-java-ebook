// Java 16+
/**
 * Listing 8.5 — ImmutableRecord.java
 * Demonstrates: Records as immutable, thread-safe shared state
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 16+
 */
package chapter08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ImmutableRecord {

    private static final Logger log =
            Logger.getLogger(ImmutableRecord.class.getName());

    // Immutable by construction — safe to share across threads
    // All fields are final; no setters exist; no coordination needed
    record OrderEvent(String orderId,
                      String customerId,
                      double amount,
                      String status) {}

    public static void main(String[] args) throws InterruptedException {

        // Created once, shared freely — no synchronisation required
        OrderEvent event = new OrderEvent(
                "ORD-001", "CUST-42", 299.99, "PLACED");

        // Four threads will all read the same immutable record
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple threads reading the same record safely
        for (int i = 0; i < 10; i++) {
            final int taskId = i; // effectively final for lambda
            executor.submit(() ->
                log.info("Task-" + taskId
                        + " processing: " + event.orderId()
                        + " customer: " + event.customerId()
                        + " amount: " + event.amount()
                        + " status: " + event.status()));
        }

        executor.shutdown();
        // Wait up to 5 seconds for all tasks to complete
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Record's auto-generated toString() is also thread-safe
        log.info("Shared event: " + event);

        // Output:
        // Task-0 processing: ORD-001 customer: CUST-42 amount: 299.99 status: PLACED
        // Task-1 processing: ORD-001 customer: CUST-42 amount: 299.99 status: PLACED
        // ... (10 lines, order may vary, values always consistent)
        // Shared event: OrderEvent[orderId=ORD-001, customerId=CUST-42, amount=299.99, status=PLACED]
    }
}