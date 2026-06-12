// Java 5+
/**
 * Listing 10.4 — OrderStatus.java
 * Demonstrates: Enum immutability for thread-safe shared state in concurrent systems
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 5+
 */
package chapter10;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class OrderStatus {

    // Immutable by construction — safe to share across threads
    enum Status {
        CREATED, PAID, SHIPPED, DELIVERED, CANCELLED
    }

    private static final Logger log = Logger.getLogger(OrderStatus.class.getName());

    // Volatile ensures visibility of enum reference across threads
    static volatile Status currentStatus = Status.CREATED;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Simulate workflow transitions on one thread
        executor.submit(() -> {
            currentStatus = Status.PAID;
            log.info("Order transitioned to: " + currentStatus);
            currentStatus = Status.SHIPPED;
            log.info("Order transitioned to: " + currentStatus);
        });

        // Multiple threads safely read the shared enum value — no synchronisation needed
        for (int i = 0; i < 6; i++) {
            executor.submit(() -> {
                Status status = currentStatus;
                log.info("Thread observed status: " + status
                        + " | isFinal=" + isFinalState(status));
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Enum constants are singletons — identity comparison is safe and idiomatic
        log.info("All statuses: " + Arrays.toString(Status.values()));
        log.info("DELIVERED is final: " + isFinalState(Status.DELIVERED));
        log.info("PAID is final: "      + isFinalState(Status.PAID));

        // Output:
        // Order transitioned to: PAID
        // Order transitioned to: SHIPPED
        // Thread observed status: SHIPPED | isFinal=false
        // All statuses: [CREATED, PAID, SHIPPED, DELIVERED, CANCELLED]
        // DELIVERED is final: true
        // PAID is final: false
    }

    static boolean isFinalState(Status status) {
        return status == Status.DELIVERED || status == Status.CANCELLED;
    }
}