// Java 25+
// Feature shown: ThreadLocal for per-thread security context, final in Java 8+

/**
 * Listing 10.12 — ThreadLocalSecurityContext.java
 * Demonstrates: ThreadLocal used to propagate a per-thread security context
 *               without passing the authenticated user as a method parameter.
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

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
            // All downstream calls within this request can access the
            // authenticated user without it being passed as a parameter
            requestLogic.run();
        } finally {
            clearUser(); // critical: prevents context leaking to next request
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

    void main() {
        UserContext alice = new UserContext("alice", "ADMIN", "tenant-42");
        UserContext bob   = new UserContext("bob",   "USER",  "tenant-99");

        // Simulate two sequential requests on the same thread
        handleRequest(alice, () -> processOrder("ORD-001"));
        handleRequest(bob,   () -> processOrder("ORD-002"));

        // Confirm context is cleared after each request
        log.info("Context after requests: " + getUser()); // null expected

        // Output:
        // Order ORD-001 processed by alice (tenant: tenant-42)
        // Order ORD-002 processed by bob (tenant: tenant-99)
        // Context after requests: null
    }
}