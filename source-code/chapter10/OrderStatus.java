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
        // Enum constant shared freely — no synchronisation required
        OrderStatus current = OrderStatus.CREATED;

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple threads reading the same enum constant safely
        for (int i = 0; i < 8; i++) {
            final int step = i;
            executor.submit(() -> {
                // Enum values are singletons — identity comparison is safe
                OrderStatus status = step < 2 ? OrderStatus.CREATED
                        : step < 4 ? OrderStatus.PAID
                        : step < 6 ? OrderStatus.SHIPPED
                        : OrderStatus.DELIVERED;
                log.info("Thread observed status: " + status
                        + " | same as CREATED: " + (status == current));
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // Thread observed status: CREATED | same as CREATED: true
        // Thread observed status: PAID    | same as CREATED: false
        // Thread observed status: SHIPPED | same as CREATED: false
        // Thread observed status: DELIVERED | same as CREATED: false
        // (order of lines may vary; values are always consistent)
    }
}