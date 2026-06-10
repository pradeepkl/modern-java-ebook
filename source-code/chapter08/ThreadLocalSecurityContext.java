// Java 8+
/**
 * Listing 8.12 — ThreadLocalSecurityContext.java
 * Demonstrates: Per-thread security context using ThreadLocal for request-scoped user authentication
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ThreadLocalSecurityContext {

    private static final Logger log =
            Logger.getLogger(ThreadLocalSecurityContext.class.getName());

    record UserContext(String userId, String role, String tenantId) {}

    // Each thread carries its own authenticated user — no sharing, no coordination
    private static final ThreadLocal<UserContext> currentUser = new ThreadLocal<>();

    public static void setUser(UserContext user) {
        currentUser.set(user); // binds user to the calling thread only
    }

    public static UserContext getUser() {
        return currentUser.get(); // retrieves this thread's user
    }

    // Always clear after request — thread pool reuse risk
    public static void clearUser() {
        currentUser.remove(); // prevents stale context on reused pool threads
    }

    public static void handleRequest(UserContext user, Runnable requestLogic) {
        try {
            setUser(user);
            // All downstream calls within this request see the authenticated user
            requestLogic.run();
        } finally {
            clearUser(); // critical: always remove in finally block
        }
    }

    public static void processOrder(String orderId) {
        UserContext user = getUser();
        if (user == null) {
            throw new IllegalStateException("No authenticated user in context");
        }
        log.info("Order " + orderId
                + " processed by " + user.userId()
                + " (role: " + user.role()
                + ", tenant: " + user.tenantId() + ")");
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // Simulate two concurrent requests with different users
        pool.submit(() -> handleRequest(
                new UserContext("alice", "ADMIN", "tenant-A"),
                () -> processOrder("ORD-001")));

        pool.submit(() -> handleRequest(
                new UserContext("bob", "USER", "tenant-B"),
                () -> processOrder("ORD-002")));

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        // Output:
        // INFO: Order ORD-001 processed by alice (role: ADMIN, tenant: tenant-A)
        // INFO: Order ORD-002 processed by bob (role: USER, tenant: tenant-B)
    }
}