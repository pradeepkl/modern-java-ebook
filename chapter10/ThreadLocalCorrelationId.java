// Java 25+
// Feature shown: ThreadLocal per-thread isolated state, final in Java 8+

/**
 * Listing 10.11 — ThreadLocalCorrelationId.java
 * Demonstrates: ThreadLocal providing per-thread isolated state for
 * request-scoped correlation IDs without any thread coordination.
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ThreadLocalCorrelationId {

    private static final Logger log =
            Logger.getLogger(ThreadLocalCorrelationId.class.getName());

    // Each thread gets its own correlation ID — no sharing, no coordination
    private static final ThreadLocal<String> correlationId =
            ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    public static String get() {
        return correlationId.get();
    }

    public static void set(String id) {
        correlationId.set(id);
    }

    // CRITICAL: always remove when done
    // Thread pool threads are reused — failing to remove leaves the
    // previous request's ID on the thread for the next request
    public static void clear() {
        correlationId.remove();
    }

    public static void handleRequest(String requestId) {
        try {
            set(requestId);
            log.info("[" + get() + "] Processing request");
            processOrder();
            sendNotification();
        } finally {
            clear(); // Always clean up — thread pool reuse
        }
    }

    private static void processOrder() {
        log.info("[" + get() + "] Order processed");
    }

    private static void sendNotification() {
        log.info("[" + get() + "] Notification sent");
    }

    void main() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Simulate two concurrent requests with distinct IDs
        executor.submit(() -> handleRequest("req-alpha"));
        executor.submit(() -> handleRequest("req-beta"));

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: [req-alpha] Processing request
        // INFO: [req-alpha] Order processed
        // INFO: [req-alpha] Notification sent
        // INFO: [req-beta] Processing request
        // INFO: [req-beta] Order processed
        // INFO: [req-beta] Notification sent
    }
}