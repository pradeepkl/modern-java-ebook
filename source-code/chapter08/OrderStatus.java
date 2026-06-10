// Java 5+
/**
 * Listing 8.4 — OrderStatus.java
 * Demonstrates: Enum as immutable, thread-safe singleton constants
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 5+
 */
package chapter08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

// Immutable by construction — safe to share across threads
public enum OrderStatus {
    CREATED, PAID, SHIPPED, DELIVERED, CANCELLED;

    private static final Logger log = Logger.getLogger(
            OrderStatus.class.getName());

    // Enum values are singletons — no synchronisation needed to read them
    public boolean isFinal() {
        return this == DELIVERED || this == CANCELLED;
    }

    public static void main(String[] args) throws InterruptedException {

        // Enum constant initialised once at class load — shared freely
        OrderStatus current = OrderStatus.SHIPPED;

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple threads reading the same enum constant safely
        for (int i = 0; i < 8; i++) {
            final int threadId = i;
            executor.submit(() ->
                log.info("Thread-" + threadId
                        + " sees status: " + current
                        + " | isFinal=" + current.isFinal())
            );
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Demonstrate all enum values — each is an immutable singleton
        for (OrderStatus status : OrderStatus.values()) {
            log.info("Status: " + status + " | ordinal=" + status.ordinal()
                    + " | isFinal=" + status.isFinal());
        }

        // Output:
        // Thread-0 sees status: SHIPPED | isFinal=false
        // Status: CREATED   | ordinal=0 | isFinal=false
        // Status: DELIVERED | ordinal=3 | isFinal=true
        // Status: CANCELLED | ordinal=4 | isFinal=true
    }
}