// Java 8+
/**
 * Listing 13.3 — BulkOperations.java
 * Demonstrates: forEach, removeIf, and replaceAll bulk operations on collections
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class BulkOperations {

    private static final Logger log = Logger.getLogger(BulkOperations.class.getName());

    // Simple Order record to represent domain data
    record Order(String orderId, double amount, String status) {}

    public static void main(String[] args) {

        // Build a mutable list of sample orders
        List<Order> orders = new ArrayList<>(Arrays.asList(
            new Order("ORD-001", 120.00, "CONFIRMED"),
            new Order("ORD-002",  45.00, "CANCELLED"),
            new Order("ORD-003", 200.00, "CONFIRMED"),
            new Order("ORD-004",  80.00, "CANCELLED"),
            new Order("ORD-005", 310.00, "PENDING")
        ));

        // forEach — apply a side effect to every element
        // Correct use: logging, notification, audit
        log.info("=== forEach: log all orders ===");
        orders.forEach(o ->
                log.info("Processing: " + o.orderId()));

        // removeIf — remove elements matching a predicate
        // Modifies the list in place — use on owned lists only
        orders.removeIf(o ->
                o.status().equals("CANCELLED"));

        log.info("=== After removeIf (CANCELLED removed): " + orders.size() + " orders remain ===");

        // replaceAll — replace every element with a transformed version
        // List size unchanged; each element is replaced by the lambda result
        orders.replaceAll(o ->
                new Order(o.orderId(),
                        o.amount() * 0.9,   // apply 10% discount
                        o.status()));

        log.info("=== After replaceAll (10% discount applied) ===");
        orders.forEach(o ->
                log.info(o.orderId() + " | " + o.amount() + " | " + o.status()));

        // Output:
        // Processing: ORD-001 ... ORD-005 (forEach logs all 5)
        // After removeIf: 3 orders remain (ORD-002, ORD-004 removed)
        // After replaceAll: ORD-001=108.0, ORD-003=180.0, ORD-005=279.0
    }
}