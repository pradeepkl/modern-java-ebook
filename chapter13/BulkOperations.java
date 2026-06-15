// Java 25+
// Feature shown: bulk collection operations (forEach, removeIf, replaceAll), final in Java 8+

/**
 * Listing 13.3 — BulkOperations.java
 * Demonstrates: forEach, removeIf, and replaceAll as declarative bulk operations
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BulkOperations {

    private static final Logger log = Logger.getLogger(BulkOperations.class.getName());

    record Order(String orderId, double amount, String status) {}

    void main() {

        List<Order> orders = new ArrayList<>(List.of(
            new Order("ORD-001", 120.00, "CONFIRMED"),
            new Order("ORD-002",  85.50, "CANCELLED"),
            new Order("ORD-003", 200.00, "CONFIRMED"),
            new Order("ORD-004",  45.00, "CANCELLED"),
            new Order("ORD-005", 310.00, "PENDING")
        ));

        // forEach — apply a side effect to every element
        // Correct use: logging, notification, audit
        log.info("--- forEach: log all orders ---");
        orders.forEach(o ->
            log.info("Processing: " + o.orderId()));

        // removeIf — remove elements matching a predicate
        // Modifies the list in place — use on owned lists only
        orders.removeIf(o ->
            o.status().equals("CANCELLED"));

        log.info("--- After removeIf (CANCELLED removed): " + orders.size() + " orders remain ---");
        orders.forEach(o ->
            log.info("Remaining: " + o.orderId() + " status=" + o.status()));

        // replaceAll — replace every element with a transformed version
        // List size unchanged; applies a 10% discount to each order amount
        orders.replaceAll(o ->
            new Order(o.orderId(), o.amount() * 0.9, o.status()));

        log.info("--- After replaceAll (10% discount applied) ---");
        orders.forEach(o ->
            log.info("Order " + o.orderId() + " new amount=" + o.amount()));

        // Output:
        // Processing: ORD-001
        // Processing: ORD-002
        // Processing: ORD-003
        // Processing: ORD-004
        // Processing: ORD-005
        // After removeIf: 3 orders remain
        // Remaining: ORD-001 status=CONFIRMED
        // Remaining: ORD-003 status=CONFIRMED
        // Remaining: ORD-005 status=PENDING
        // Order ORD-001 new amount=108.0
        // Order ORD-003 new amount=180.0
        // Order ORD-005 new amount=279.0
    }
}