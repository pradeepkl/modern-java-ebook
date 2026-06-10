// Java 8+
/**
 * Listing 8.11 — ThreadLocalCorrelationId.java
 * Demonstrates: Per-thread correlation ID using ThreadLocal for request tracing
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

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
    // Thread pool threads are reused — failing to remove leaves
    // the previous request's ID on the thread for the next request
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

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit three requests; each thread sees only its own correlation ID
        for (int i = 1; i <= 3; i++) {
            final String requestId = "REQ-" + i + "-" + UUID.randomUUID().toString().substring(0, 8);
            executor.submit(() -> handleRequest(requestId));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: [REQ-1-xxxxxxxx] Processing request
        // INFO: [REQ-1-xxxxxxxx] Order processed
        // INFO: [REQ-1-xxxxxxxx] Notification sent
        // INFO: [REQ-2-xxxxxxxx] Processing request
        // ... (each request's ID is isolated to its thread)
    }
}