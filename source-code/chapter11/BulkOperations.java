// Java 8+
/**
 * Listing 11.2 — BulkOperations.java
 * Demonstrates: forEach, removeIf, and replaceAll bulk operations on collections
 * Chapter 11: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class BulkOperations {

    private static final Logger log = Logger.getLogger(BulkOperations.class.getName());

    // Simple record to represent an order
    record Order(String orderId, double amount, String status) {}

    public static void main(String[] args) {

        // Build a mutable list — bulk operations require owned, mutable lists
        List<Order> orders = new ArrayList<>(Arrays.asList(
            new Order("ORD-001", 100.00, "CONFIRMED"),
            new Order("ORD-002", 200.00, "CANCELLED"),
            new Order("ORD-003", 150.00, "CONFIRMED"),
            new Order("ORD-004",  50.00, "CANCELLED"),
            new Order("ORD-005", 300.00, "PENDING")
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

        log.info("=== After removeIf: CANCELLED orders removed ===");
        orders.forEach(o ->
                log.info("Remaining: " + o.orderId() + " [" + o.status() + "]"));

        // replaceAll — replace every element with a transformed version
        // List size unchanged; applies 10% discount to each order amount
        orders.replaceAll(o ->
                new Order(o.orderId(),
                        o.amount() * 0.9,
                        o.status()));

        log.info("=== After replaceAll: 10% discount applied ===");
        orders.forEach(o ->
                log.info(o.orderId() + " -> $" + o.amount() + " [" + o.status() + "]"));

        // Output:
        // Processing: ORD-001 ... ORD-005 (forEach logs all 5)
        // Remaining: ORD-001 [CONFIRMED], ORD-003 [CONFIRMED], ORD-005 [PENDING]
        // ORD-001 -> $90.0 [CONFIRMED]
        // ORD-003 -> $135.0 [CONFIRMED]
        // ORD-005 -> $270.0 [PENDING]
    }
}