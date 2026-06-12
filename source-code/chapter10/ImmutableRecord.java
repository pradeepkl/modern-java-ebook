// Java 16+
/**
 * Listing 10.3 — ImmutableRecord.java
 * Demonstrates: Records as immutable, thread-safe shared state
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 16+
 */
package chapter10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ImmutableRecord {

    private static final Logger log =
            Logger.getLogger(ImmutableRecord.class.getName());

    // Immutable by construction — safe to share across threads
    // All fields are final; no setters exist
    record OrderEvent(String orderId,
                      String customerId,
                      double amount,
                      String status) {}

    public static void main(String[] args) throws InterruptedException {

        // Created once, shared freely — no coordination needed
        // Every thread reads the same immutable snapshot
        OrderEvent event = new OrderEvent(
                "ORD-001", "CUST-42", 299.99, "PLACED");

        // Four threads will share this single record instance
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple threads reading the same record safely
        // No locks, no volatile, no synchronisation required
        for (int i = 0; i < 10; i++) {
            executor.submit(() ->
                log.info("Processing: "
                        + event.orderId()
                        + " | customer: " + event.customerId()
                        + " | amount: " + event.amount()
                        + " | status: " + event.status()));
        }

        executor.shutdown();
        // Wait up to 5 seconds for all tasks to complete
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Record fields are always consistent — no partial reads possible
        log.info("Event processed: " + event);

        // Output:
        // INFO: Processing: ORD-001 | customer: CUST-42 | amount: 299.99 | status: PLACED
        // INFO: Processing: ORD-001 | customer: CUST-42 | amount: 299.99 | status: PLACED
        // ... (10 lines, all identical, order may vary by thread scheduling)
        // INFO: Event processed: OrderEvent[orderId=ORD-001, customerId=CUST-42, amount=299.99, status=PLACED]
    }
}