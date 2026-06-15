// Java 25+
// Feature shown: immutable records shared safely across threads, final in Java 16+

/**
 * Listing 10.3 — ImmutableRecord.java
 * Demonstrates: immutable records shared safely across threads without synchronisation
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (instance main method via JEP 512)
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
    // All fields are final; no setters exist; no coordination needed
    record OrderEvent(String orderId,
                      String customerId,
                      double amount,
                      String status) {}

    void main() throws InterruptedException {

        // Created once, shared freely — no coordination needed
        OrderEvent event = new OrderEvent(
                "ORD-001", "CUST-42", 299.99, "PLACED");

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple threads reading the same record safely
        // No locks, no volatile, no AtomicReference required
        for (int i = 0; i < 10; i++) {
            executor.submit(() ->
                log.info("Processing: "
                        + event.orderId()
                        + " amount: " + event.amount()
                        + " status: " + event.status()));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output (order may vary across threads):
        // INFO: Processing: ORD-001 amount: 299.99 status: PLACED
        // INFO: Processing: ORD-001 amount: 299.99 status: PLACED
        // ... (10 lines total, all identical values)
    }
}