// Java 25+
// Feature shown: enums, final in Java 5+
package chapter10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 10.4 — OrderStatus.java
 * Demonstrates: enums as immutable singleton constants safe for concurrent access
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */

// Immutable by construction — safe to share across threads
// Each constant is a singleton initialised once at class loading time
enum OrderStatus {
    CREATED, PAID, SHIPPED, DELIVERED, CANCELLED
}

class OrderStatusDemo {

    private static final Logger log = Logger.getLogger(
            OrderStatusDemo.class.getName());

    void main() throws InterruptedException {
        // Enum constant shared across threads — no synchronisation needed
        OrderStatus current = OrderStatus.PAID;

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple threads reading the same enum constant safely
        for (int i = 0; i < 8; i++) {
            executor.submit(() ->
                log.info("Order status observed: " + current.name()
                        + " ordinal=" + current.ordinal())
            );
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Enum values are comparable by identity — always consistent
        log.info("Is PAID? " + (current == OrderStatus.PAID));
        log.info("Is SHIPPED? " + (current == OrderStatus.SHIPPED));

        // Output:
        // Order status observed: PAID ordinal=1
        // Order status observed: PAID ordinal=1
        // ... (8 lines, all identical — no race conditions possible)
        // Is PAID? true
        // Is SHIPPED? false
    }
}