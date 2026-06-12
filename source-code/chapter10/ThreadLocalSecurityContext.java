// Java 8+
/**
 * Listing 10.12 — ThreadLocalSecurityContext.java
 * Demonstrates: Thread-local security context for per-request user isolation
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter10;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ThreadLocalSecurityContext {

    private static final Logger log =
            Logger.getLogger(ThreadLocalSecurityContext.class.getName());

    record UserContext(String userId, String role, String tenantId) {}

    // Each thread carries its own authenticated user
    private static final ThreadLocal<UserContext> currentUser = new ThreadLocal<>();

    public static void setUser(UserContext user) {
        currentUser.set(user);
    }

    public static UserContext getUser() {
        return currentUser.get();
    }

    // Always clear after request — thread pool reuse
    public static void clearUser() {
        currentUser.remove();
    }

    public static void handleRequest(UserContext user, Runnable requestLogic) {
        try {
            setUser(user);
            // All downstream calls within this request can access
            // the authenticated user without it being passed as a parameter
            requestLogic.run();
        } finally {
            clearUser(); // Critical: prevent context leaking to next request
        }
    }

    public static void processOrder(String orderId) {
        UserContext user = getUser();
        if (user == null) {
            throw new IllegalStateException("No authenticated user in context");
        }
        log.info("Order " + orderId
                + " processed by " + user.userId()
                + " (tenant: " + user.tenantId() + ")");
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // Simulate two concurrent requests with different users
        UserContext alice = new UserContext("alice", "ADMIN", "tenant-A");
        UserContext bob   = new UserContext("bob",   "USER",  "tenant-B");

        pool.submit(() -> handleRequest(alice, () -> processOrder("ORD-001")));
        pool.submit(() -> handleRequest(bob,   () -> processOrder("ORD-002")));

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: Order ORD-001 processed by alice (tenant: tenant-A)
        // INFO: Order ORD-002 processed by bob (tenant: tenant-B)
    }
}