// Java 25+
// Feature shown: returning empty collections instead of null, final in Java 9+

/**
 * Listing 12.9 — NullVsEmptyCollection.java
 * Demonstrates: returning empty collections instead of null from collection-returning methods
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter12;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class NullVsEmptyCollection {

    private static final Logger log = Logger.getLogger(
            NullVsEmptyCollection.class.getName());

    // Simulated data store: only "C001" exists
    private static final Map<String, List<String>> ORDERS = Map.of(
            "C001", List.of("ORDER-1", "ORDER-2", "ORDER-3")
    );

    // NOT IDEAL: null forces every caller to add a null check
    List<String> findByCustomerUnsafe(String customerId) {
        if (!customerExists(customerId)) return null; // caller must null-check
        return queryOrders(customerId);
    }

    // Correct approach: empty collection — callers can use the result
    // directly without a null check
    List<String> findByCustomer(String customerId) {
        if (!customerExists(customerId)) return List.of(); // safe, no null
        return queryOrders(customerId);
    }

    private boolean customerExists(String customerId) {
        return ORDERS.containsKey(customerId);
    }

    private List<String> queryOrders(String customerId) {
        return ORDERS.getOrDefault(customerId, List.of());
    }

    void main() {
        // Unsafe version — null return requires defensive null check
        List<String> unsafeResult = findByCustomerUnsafe("C999");
        if (unsafeResult == null) {
            log.info("Unsafe: null returned for unknown customer — caller must guard");
        }

        // Safe version — empty list returned, no null check needed
        List<String> emptyResult = findByCustomer("C999");
        log.info("Safe: result for unknown customer has " +
                emptyResult.size() + " orders"); // no NPE risk

        // Safe version — known customer returns actual orders
        List<String> orders = findByCustomer("C001");
        log.info("Safe: found " + orders.size() + " orders for C001: " + orders);

        // Caller can iterate directly — no null guard required
        for (String order : findByCustomer("C999")) {
            log.info("Order: " + order); // loop body never reached, no NPE
        }

        // Output:
        // Unsafe: null returned for unknown customer -- caller must guard
        // Safe: result for unknown customer has 0 orders
        // Safe: found 3 orders for C001: [ORDER-1, ORDER-2, ORDER-3]
    }
}